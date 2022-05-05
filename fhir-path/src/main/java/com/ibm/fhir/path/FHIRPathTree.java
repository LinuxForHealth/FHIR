/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;
import java.util.stream.Collectors;

import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Quantity;
import com.ibm.fhir.model.visitor.PathAwareVisitor;

/**
 * A tree of {@link FHIRPathNode} nodes created from a {@link Resource} or an {@link Element}
 */
public class FHIRPathTree {
    private FHIRPathNode root;
    private Map<String, FHIRPathNode> pathNodeMap;

    private FHIRPathTree() { }

    private void setRoot(FHIRPathNode root) {
        this.root = root;
    }

    private void setPathNodeMap(Map<String, FHIRPathNode> pathNodeMap) {
        this.pathNodeMap = Collections.unmodifiableMap(pathNodeMap);
    }

    /**
     * The root node of this FHIRPathTree
     *
     * @return
     *     the root node of this FHIRPathTree
     */
    public FHIRPathNode getRoot() {
        return root;
    }


    /**
     * Get the node at the location given by the path parameter
     *
     * @param path
     *     the location of the node in the tree
     * @return
     *     the node at the location given by the path parameter if exists, otherwise null
     */
    public FHIRPathNode getNode(String path) {
        return pathNodeMap.get(path);
    }

    /**
     * Get the parent of the node parameter
     *
     * @param node
     *     the node
     * @return
     *     the parent of the node parameter if exists, otherwise null
     */
    public FHIRPathNode getParent(FHIRPathNode node) {
        if (node == null) {
            return null;
        }

        int index = node.path().lastIndexOf(".");
        if (index != -1) {
            return pathNodeMap.get(node.path().substring(0, index));
        }

        return null;
    }

    /**
     * Get the siblings of the node parameter
     *
     * @param node
     *     the node
     * @return
     *     the siblings of the node parameter if exists, otherwise empty collection
     */
    public Collection<FHIRPathNode> getSiblings(FHIRPathNode node) {
        FHIRPathNode parent = getParent(node);
        if (parent != null) {
            return parent.children().stream()
                .filter(child -> !child.equals(node))
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
        }
        return Collections.emptyList();
    }

    /**
     * Get the siblings of the node parameter with the given name
     *
     * @param node
     *     the node
     * @param name
     *     the name
     * @return
     *     the siblings of the node parameter with the given name if exists, otherwise empty collection
     */
    public Collection<FHIRPathNode> getSiblings(FHIRPathNode node, String name) {
        return getSiblings(node).stream()
            .filter(sibling -> name.equals(sibling.name()))
            .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
    }

    /**
     * Get the first sibling of the node parameter with the given name
     *
     * @param node
     *     the node
     * @param name
     *     the name
     * @return
     *     the first sibling of the node parameter with the given name if exists, otherwise null
     */
    public FHIRPathNode getSibling(FHIRPathNode node, String name) {
        return getSiblings(node).stream()
            .filter(sibling -> name.equals(sibling.name()))
            .findFirst()
            .orElse(null);
    }

    /**
     * Static factory method for creating FHIRPathTree instances from a {@link Resource}
     *
     * @param resource
     *     the resource
     * @return
     *     a new FHIRPathTree instance
     */
    public static FHIRPathTree tree(Resource resource) {
        Objects.requireNonNull(resource);

        FHIRPathTree tree = new FHIRPathTree();

        BuildingVisitor visitor = new BuildingVisitor(tree);
        resource.accept(visitor);

        tree.setRoot(visitor.getRoot());
        tree.setPathNodeMap(visitor.getPathNodeMap());

        return tree;
    }

    /**
     * Static factory method for creating FHIRPathTree instances from an {@link Element}
     *
     * @param element
     *     the element
     * @return
     *     a new FHIRPathTree instance
     */
    public static FHIRPathTree tree(Element element) {
        Objects.requireNonNull(element);

        FHIRPathTree tree = new FHIRPathTree();

        BuildingVisitor visitor = new BuildingVisitor(tree);
        element.accept(visitor);

        tree.setRoot(visitor.getRoot());
        tree.setPathNodeMap(visitor.getPathNodeMap());

        return tree;
    }

    private static class BuildingVisitor extends PathAwareVisitor {
        private final FHIRPathTree tree;

        private Stack<FHIRPathNode.Builder> builderStack = new Stack<>();
        private FHIRPathNode root;
        private Map<String, FHIRPathNode> pathNodeMap = new HashMap<>();

        private BuildingVisitor(FHIRPathTree tree) {
            this.tree = tree;
        }

        private void build() {
            String path = getPath();

            FHIRPathNode.Builder builder = builderStack.pop();
            FHIRPathNode node = builder.path(path).build();

            pathNodeMap.put(path, node);

            if (builderStack.isEmpty()) {
                root = node;
            } else {
                builderStack.peek().children(node);
            }
        }

        private Map<String, FHIRPathNode> getPathNodeMap() {
            return pathNodeMap;
        }

        private FHIRPathNode getRoot() {
            return root;
        }

        @Override
        protected void doVisitEnd(String elementName, int elementIndex, Element element) {
            build();
        }

        @Override
        protected void doVisitEnd(String elementName, int elementIndex, Resource resource) {
            build();
        }

        @Override
        protected void doVisitStart(String elementName, int elementIndex, Element element) {
            if (element instanceof Quantity) {
                Quantity quantity = (Quantity) element;
                builderStack.push(FHIRPathQuantityNode.builder(quantity).name(elementName).tree(tree));
                FHIRPathQuantityValue value = FHIRPathQuantityValue.quantityValue(quantity);
                if (value != null) {
                    builderStack.peek().value(value);
                }
            } else {
                builderStack.push(FHIRPathElementNode.builder(element).name(elementName).tree(tree));
            }
        }

        @Override
        protected void doVisitStart(String elementName, int elementIndex, Resource resource) {
            builderStack.push(FHIRPathResourceNode.builder(resource).name(elementName).tree(tree));
        }

        @Override
        public void visit(java.lang.String elementName, BigDecimal value) {
            builderStack.peek().value(FHIRPathDecimalValue.decimalValue(getPath(), elementName, value));
        }

        @Override
        public void visit(java.lang.String elementName, byte[] value) {
            builderStack.peek().value(FHIRPathStringValue.stringValue(getPath(), elementName, Base64.getEncoder().encodeToString(value)));
        }

        @Override
        public void visit(java.lang.String elementName, java.lang.Boolean value) {
            builderStack.peek().value(FHIRPathBooleanValue.booleanValue(getPath(), elementName, value));
        }

        @Override
        public void visit(java.lang.String elementName, java.lang.Integer value) {
            builderStack.peek().value(FHIRPathIntegerValue.integerValue(getPath(), elementName, value));
        }

        @Override
        public void doVisit(java.lang.String elementName, java.lang.String value) {
            builderStack.peek().value(FHIRPathStringValue.stringValue(getPath(), elementName, value));
        }

        @Override
        public void visit(java.lang.String elementName, LocalDate value) {
            builderStack.peek().value(FHIRPathDateTimeValue.dateTimeValue(getPath(), elementName, value));
        }

        @Override
        public void visit(java.lang.String elementName, LocalTime value) {
            builderStack.peek().value(FHIRPathTimeValue.timeValue(getPath(), elementName, value));
        }

        @Override
        public void visit(java.lang.String elementName, Year value) {
            builderStack.peek().value(FHIRPathDateTimeValue.dateTimeValue(getPath(), elementName, value));
        }

        @Override
        public void visit(java.lang.String elementName, YearMonth value) {
            builderStack.peek().value(FHIRPathDateTimeValue.dateTimeValue(getPath(), elementName, value));
        }

        @Override
        public void visit(java.lang.String elementName, ZonedDateTime value) {
            builderStack.peek().value(FHIRPathDateTimeValue.dateTimeValue(getPath(), elementName, value));
        }
    }
}
