/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.path;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;

import com.ibm.watsonhealth.fhir.model.resource.Resource;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.Quantity;
import com.ibm.watsonhealth.fhir.model.visitor.PathAwareAbstractVisitor;

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
        resource.accept(visitor);
        
        return new FHIRPathTree(visitor.getRoot(), visitor.getPathNodeMap());
    }
    
    public static FHIRPathTree tree(Element element) {
        Objects.requireNonNull(element);
        
        BuildingVisitor visitor = new BuildingVisitor();
        element.accept(visitor);
        
        return new FHIRPathTree(visitor.getRoot(), visitor.getPathNodeMap());
    }
    
    private static class BuildingVisitor extends PathAwareAbstractVisitor {
        private Stack<FHIRPathNode.Builder> builderStack = new Stack<>();
        private FHIRPathNode root;
        private Map<String, FHIRPathNode> pathNodeMap = new HashMap<>();

        private void build() {
            String path = getPath();
            
            FHIRPathNode.Builder builder = builderStack.pop();
            FHIRPathNode node = builder.path(path).build();
            
            pathNodeMap.put(path, node);
            
            if (!builderStack.isEmpty()) {
                builderStack.peek().children(node);
            } else {
                root = node;
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
                builderStack.push(FHIRPathQuantityNode.builder((Quantity) element).name(elementName));
            } else {
                builderStack.push(FHIRPathElementNode.builder(element).name(elementName));
            }
        }

        @Override
        protected void doVisitStart(String elementName, int elementIndex, Resource resource) {
            builderStack.push(FHIRPathResourceNode.builder(resource).name(elementName));            
        }

        @Override
        public void visit(java.lang.String elementName, BigDecimal value) {
            builderStack.peek().value(FHIRPathDecimalValue.decimalValue(elementName, value));
        }
    
        @Override
        public void visit(java.lang.String elementName, byte[] value) {
            builderStack.peek().value(FHIRPathStringValue.stringValue(elementName, Base64.getEncoder().encodeToString(value)));
        }
    
        @Override
        public void visit(java.lang.String elementName, java.lang.Boolean value) {
            builderStack.peek().value(FHIRPathBooleanValue.booleanValue(elementName, value));
        }
    
        @Override
        public void visit(java.lang.String elementName, java.lang.Integer value) {
            builderStack.peek().value(FHIRPathIntegerValue.integerValue(elementName, value));
        }
    
        @Override
        public void visit(java.lang.String elementName, java.lang.String value) {
            builderStack.peek().value(FHIRPathStringValue.stringValue(elementName, value));
        }
    
        @Override
        public void visit(java.lang.String elementName, LocalDate value) {
            builderStack.peek().value(FHIRPathDateTimeValue.dateTimeValue(elementName, value));
        }
    
        @Override
        public void visit(java.lang.String elementName, LocalTime value) {
            builderStack.peek().value(FHIRPathTimeValue.timeValue(elementName, value));
        }
    
        @Override
        public void visit(java.lang.String elementName, Year value) {
            builderStack.peek().value(FHIRPathDateTimeValue.dateTimeValue(elementName, value));
        }
    
        @Override
        public void visit(java.lang.String elementName, YearMonth value) {
            builderStack.peek().value(FHIRPathDateTimeValue.dateTimeValue(elementName, value));
        }
    
        @Override
        public void visit(java.lang.String elementName, ZonedDateTime value) {
            builderStack.peek().value(FHIRPathDateTimeValue.dateTimeValue(elementName, value));
        }
    }
}
