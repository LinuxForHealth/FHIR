/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.path;

import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.getTypeName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;
import java.util.stream.Collectors;

import com.ibm.watsonhealth.fhir.model.resource.Resource;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.visitor.AbstractVisitor;
import com.ibm.watsonhealth.fhir.model.visitor.Visitable;

public class FHIRPathTree {
    public static boolean DEBUG = false;
    
    private final FHIRPathNode root;
    private final Map<String, FHIRPathNode> pathNodeMap;
    
    private FHIRPathTree(FHIRPathNode root, Map<String, FHIRPathNode> pathNodeMap) {
        this.root = root;
        this.pathNodeMap = Collections.unmodifiableMap(pathNodeMap);
    }
    
    public FHIRPathNode getRoot() {
        return root;
    }
    
    public FHIRPathNode getNode(String path) {
        return pathNodeMap.get(path);
    }
    
    public static FHIRPathTree tree(Resource resource) {
        Objects.requireNonNull(resource);
        
        BuildingVisitor visitor = new BuildingVisitor();
        resource.accept(getTypeName(resource.getClass()), visitor);
        
        return new FHIRPathTree(visitor.getRoot(), visitor.getPathNodeMap());
    }
    
    public static FHIRPathTree tree(Element element) {
        Objects.requireNonNull(element);
        
        BuildingVisitor visitor = new BuildingVisitor();
        element.accept(getTypeName(element.getClass()), visitor);
        
        return new FHIRPathTree(visitor.getRoot(), visitor.getPathNodeMap());
    }
    
    private static class BuildingVisitor extends AbstractVisitor {
        private final Stack<java.lang.String> nameStack = new Stack<>();
        private final Stack<String> pathStack = new Stack<>();
        private final Stack<Integer> indexStack = new Stack<>();

        private final Stack<FHIRPathNode.Builder> builderStack = new Stack<>();
        
        private FHIRPathNode root;
        private Map<String, FHIRPathNode> pathNodeMap = new HashMap<>();

        private Map<String, FHIRPathNode> getPathNodeMap() {
            return pathNodeMap;
        }
        
        private String getPath() {
            return pathStack.stream().collect(Collectors.joining("."));
        }
    
        private FHIRPathNode getRoot() {
            return root;
        }

        private void pathStackPop() {
            pathStack.pop();
        }

        private void pathStackPush(String elementName, int index) {
            if (index != -1) {
                pathStack.push(elementName + "[" + index + "]");
            } else {
                pathStack.push(elementName);
            }
            if (DEBUG) {
                System.out.println(getPath());
            }
        }

        @Override
        public void visit(java.lang.String elementName, BigDecimal value) {
            if (elementName == null) {
                elementName = nameStack.peek();
            }
            builderStack.peek().value(FHIRPathDecimalValue.decimalValue(elementName, value));
        }
    
        @Override
        public void visit(java.lang.String elementName, byte[] value) {
            if (elementName == null) {
                elementName = nameStack.peek();
            }
            builderStack.peek().value(FHIRPathStringValue.stringValue(elementName, Base64.getEncoder().encodeToString(value)));
        }
    
        @Override
        public void visit(java.lang.String elementName, java.lang.Boolean value) {
            if (elementName == null) {
                elementName = nameStack.peek();
            }
            builderStack.peek().value(FHIRPathBooleanValue.booleanValue(elementName, value));
        }
    
        @Override
        public void visit(java.lang.String elementName, java.lang.Integer value) {
            if (elementName == null) {
                elementName = nameStack.peek();
            }
            builderStack.peek().value(FHIRPathIntegerValue.integerValue(elementName, value));
        }
    
        @Override
        public void visit(java.lang.String elementName, java.lang.String value) {
            if (elementName == null) {
                elementName = nameStack.peek();
            }
            builderStack.peek().value(FHIRPathStringValue.stringValue(elementName, value));
        }
    
        @Override
        public void visit(java.lang.String elementName, LocalDate value) {
            if (elementName == null) {
                elementName = nameStack.peek();
            }
            builderStack.peek().value(FHIRPathDateTimeValue.dateTimeValue(elementName, value));
        }
    
        @Override
        public void visit(java.lang.String elementName, LocalTime value) {
            if (elementName == null) {
                elementName = nameStack.peek();
            }
            builderStack.peek().value(FHIRPathTimeValue.timeValue(elementName, value));
        }
    
        @Override
        public void visit(java.lang.String elementName, Year value) {
            if (elementName == null) {
                elementName = nameStack.peek();
            }
            builderStack.peek().value(FHIRPathDateTimeValue.dateTimeValue(elementName, value));
        }
    
        @Override
        public void visit(java.lang.String elementName, YearMonth value) {
            if (elementName == null) {
                elementName = nameStack.peek();
            }
            builderStack.peek().value(FHIRPathDateTimeValue.dateTimeValue(elementName, value));
        }
    
        @Override
        public void visit(java.lang.String elementName, ZonedDateTime value) {
            if (elementName == null) {
                elementName = nameStack.peek();
            }
            builderStack.peek().value(FHIRPathDateTimeValue.dateTimeValue(elementName, value));
        }
        
        @Override
        public void visitEnd(java.lang.String elementName, Element element) {
            FHIRPathNode.Builder builder = builderStack.pop();
            FHIRPathNode node = builder.build();
            pathNodeMap.put(getPath(), node);
            if (!builderStack.isEmpty()) {
                builderStack.peek().children(node);
            } else {
                root = node;
            }
            pathStackPop();
        }
        
        @Override
        public void visitEnd(java.lang.String elementName, List<? extends Visitable> visitables, Class<?> type) {            
            nameStack.pop();
            indexStack.pop();
        }
        
        @Override
        public void visitEnd(java.lang.String elementName, Resource resource) {
            FHIRPathNode.Builder builder = builderStack.pop();
            FHIRPathNode node = builder.build();
            pathNodeMap.put(getPath(), node);
            if (!builderStack.isEmpty()) {
                builderStack.peek().children(node);
            } else {
                root = node;
            }
            pathStackPop();
        }
        
        @Override
        public void visitStart(java.lang.String elementName, Element element) {
            int index = -1;
            if (elementName == null) {
                elementName = nameStack.peek();
                index = indexStack.peek();
            }
            pathStackPush(elementName, index);
            FHIRPathNode.Builder builder = FHIRPathElementNode.builder(element).name(elementName);
            builderStack.push(builder);
            if (index != -1) {
                indexStack.set(indexStack.size() - 1, indexStack.peek() + 1);
            }
        }
        
        @Override
        public void visitStart(java.lang.String elementName, List<? extends Visitable> visitables, Class<?> type) {
            nameStack.push(elementName);
            indexStack.push(0);
        }
        
        @Override
        public void visitStart(java.lang.String elementName, Resource resource) {
            int index = -1;
            if (elementName == null) {
                elementName = nameStack.peek();
                index = indexStack.peek();
            }
            pathStackPush(elementName, index);
            FHIRPathNode.Builder builder = FHIRPathResourceNode.builder(resource).name(elementName);
            builderStack.push(builder);
            if (index != -1) {
                indexStack.set(indexStack.size() - 1, indexStack.peek() + 1);
            }
        }
    }
}
