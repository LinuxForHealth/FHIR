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

import com.ibm.watsonhealth.fhir.model.resource.Resource;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.visitor.PathAwareAbstractVisitor;
import com.ibm.watsonhealth.fhir.model.visitor.Visitable;

public class FHIRPathTree {    
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
    
    private static class BuildingVisitor extends PathAwareAbstractVisitor {
        private Stack<FHIRPathNode.Builder> builderStack = new Stack<>();
        private FHIRPathNode root;
        private Map<String, FHIRPathNode> pathNodeMap = new HashMap<>();

        private Map<String, FHIRPathNode> getPathNodeMap() {
            return pathNodeMap;
        }
    
        private FHIRPathNode getRoot() {
            return root;
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
        protected void doVisitEnd(String elementName, Element element) {
            FHIRPathNode.Builder builder = builderStack.pop();
            FHIRPathNode node = builder.build();
            pathNodeMap.put(getPath(), node);
            if (!builderStack.isEmpty()) {
                builderStack.peek().children(node);
            } else {
                root = node;
            }
        }

        @Override
        protected void doVisitEnd(String elementName, List<? extends Visitable> visitables, Class<?> type) {
            // do nothing
        }

        @Override
        protected void doVisitEnd(String elementName, Resource resource) {
            FHIRPathNode.Builder builder = builderStack.pop();
            FHIRPathNode node = builder.build();
            pathNodeMap.put(getPath(), node);
            if (!builderStack.isEmpty()) {
                builderStack.peek().children(node);
            } else {
                root = node;
            }            
        }

        @Override
        protected void doVisitStart(String elementName, Element element) {
            FHIRPathNode.Builder builder = FHIRPathElementNode.builder(element).name(elementName);
            builderStack.push(builder);
        }

        @Override
        protected void doVisitStart(String elementName, List<? extends Visitable> visitables, Class<?> type) {
            // do nothing
        }

        @Override
        protected void doVisitStart(String elementName, Resource resource) {
            FHIRPathNode.Builder builder = FHIRPathResourceNode.builder(resource).name(elementName);
            builderStack.push(builder);            
        }
    }
}
