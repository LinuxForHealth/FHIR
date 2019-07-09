/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.validation;

import java.io.FilterOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import java.util.StringJoiner;
import java.util.UUID;

import com.ibm.watsonhealth.fhir.model.annotation.Constraint;
import com.ibm.watsonhealth.fhir.model.format.Format;
import com.ibm.watsonhealth.fhir.model.generator.FHIRGenerator;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathNode;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathTree;
import com.ibm.watsonhealth.fhir.model.path.evaluator.FHIRPathEvaluator;
import com.ibm.watsonhealth.fhir.model.path.exception.FHIRPathException;
import com.ibm.watsonhealth.fhir.model.resource.Patient;
import com.ibm.watsonhealth.fhir.model.resource.Resource;
import com.ibm.watsonhealth.fhir.model.type.Boolean;
import com.ibm.watsonhealth.fhir.model.type.Date;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.HumanName;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Instant;
import com.ibm.watsonhealth.fhir.model.type.Integer;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.validation.exception.FHIRValidationException;
import com.ibm.watsonhealth.fhir.model.visitor.AbstractVisitor;
import com.ibm.watsonhealth.fhir.model.visitor.Visitable;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

public class FHIRValidator {
    public static boolean DEBUG = false;
    
    private final FHIRPathTree tree;
    
    private FHIRValidator(FHIRPathTree tree) {
        this.tree = Objects.requireNonNull(tree);
    }
    
    public void validate() throws FHIRValidationException {
        Visitor visitor = new ValidatingVisitor(tree);
        FHIRPathNode root = tree.getRoot();
        if (root.isResourceNode()) {
            Resource resource = root.asResourceNode().resource();
            resource.accept(resource.getClass().getSimpleName(), visitor);
        } else if (root.isElementNode()) {
            Element element = root.asElementNode().element();
            element.accept(element.getClass().getSimpleName(), visitor);;
        }
    }
    
    public static FHIRValidator validator(FHIRPathTree tree) {
        return new FHIRValidator(tree);
    }
    
    public static FHIRValidator validator(Element element) {
        return new FHIRValidator(FHIRPathTree.tree(element));
    }
    
    public static FHIRValidator validator(Resource resource) {
        return new FHIRValidator(FHIRPathTree.tree(resource));
    }

    public static class ValidatingVisitor extends AbstractVisitor {
        private final Stack<java.lang.String> nameStack = new Stack<>();
        private final Stack<java.lang.String> pathStack = new Stack<>();
        private final Stack<java.lang.Integer> indexStack = new Stack<>();
        
        private final FHIRPathTree tree;
        
        private ValidatingVisitor(FHIRPathTree tree) {
            this.tree = tree;
        }
        
        private List<Constraint> getConstraints(Class<?> type) {
            List<Class<?>> classes = new ArrayList<>();
            while (!Object.class.equals(type)) {
                classes.add(type);
                type = type.getSuperclass();
            }
            Collections.reverse(classes);
            List<Constraint> constraints = new ArrayList<>();
            for (Class<?> _class : classes) {
                constraints.addAll(Arrays.asList(_class.getDeclaredAnnotationsByType(Constraint.class)));
            }
            return constraints;
        }

        private java.lang.String getElementName(java.lang.String elementName) {
            if (elementName == null && !nameStack.isEmpty()) {
                elementName = nameStack.peek();
            }
            return elementName;
        }
        
        private int getIndex(java.lang.String elementName) {
            if (elementName == null && !indexStack.isEmpty()) {
                return indexStack.peek();
            }
            return -1;
        }

        private java.lang.String getPath() {
            StringJoiner joiner = new StringJoiner(".");
            for (java.lang.String s : pathStack) {
                joiner.add(s);
            }
            return joiner.toString();
        }
        
        private void pathStackPop() {
            pathStack.pop();
        }

        private void pathStackPush(java.lang.String elementName, int index) {
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
        public void visitEnd(java.lang.String elementName, Element element) {
            pathStackPop();
        }
        
        @Override
        public void visitEnd(java.lang.String elementName, List<? extends Visitable> visitables, Class<?> type) {            
            nameStack.pop();
            indexStack.pop();
        }
        
        @Override
        public void visitEnd(java.lang.String elementName, Resource resource) {
            pathStackPop();
        }
        
        @Override
        public void visitStart(java.lang.String elementName, Element element) {
            int index = getIndex(elementName);
            elementName = getElementName(elementName);
            pathStackPush(elementName, index);
            
            Class<?> elementType = element.getClass();
            
            List<Constraint> constraints = getConstraints(elementType);
            if (!constraints.isEmpty()) {
                java.lang.String path = getPath();
                FHIRPathNode node = tree.getNode(path);
                for (Constraint constraint : constraints) {
                    try {
                        System.out.println("Constraint: " + constraint);
                        java.lang.String expr = constraint.expression();
                        Collection<FHIRPathNode> result = FHIRPathEvaluator.evaluator(expr).evaluate(node);
                        if (!result.isEmpty()) {
                            System.out.println("       Evaluation result: " + result.iterator().next());
                        }
                    } catch (FHIRPathException e) {
                    }
                }
            }
            
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
            int index = getIndex(elementName);
            elementName = getElementName(elementName);
            pathStackPush(elementName, index);
            
            Class<?> resourceType = resource.getClass();
            
            List<Constraint> constraints = getConstraints(resourceType);
            if (!constraints.isEmpty()) {
                java.lang.String path = getPath();
                FHIRPathNode node = tree.getNode(path);
                for (Constraint constraint : constraints) {
                    try {
                        System.out.println("Constraint: " + constraint);
                        java.lang.String expr = constraint.expression();
                        Collection<FHIRPathNode> result = FHIRPathEvaluator.evaluator(expr).evaluate(node);
                        if (!result.isEmpty()) {
                            System.out.println("       Evaluation result: " + result.iterator().next());
                        }
                    } catch (FHIRPathException e) {
                    }
                }
            }
            if (index != -1) {
                indexStack.set(indexStack.size() - 1, indexStack.peek() + 1);
            }
        }
    }
    
    public static void main(java.lang.String[] args) throws Exception {
        Id id = Id.builder().value(UUID.randomUUID().toString())
                .extension(Extension.builder("http://www.ibm.com/someExtension")
                    .value(String.of("Hello, World!"))
                    .build())
                .build();
        
        Meta meta = Meta.builder().versionId(Id.of("1"))
                .lastUpdated(Instant.now(true))
                .build();
        
        String given = String.builder().value("John")
                .extension(Extension.builder("http://www.ibm.com/someExtension")
                    .value(String.of("value and extension"))
                    .build())
                .build();
        
        String otherGiven = String.builder()
                .extension(Extension.builder("http://www.ibm.com/someExtension")
                    .value(String.of("extension only"))
                    .build())
                .build();
        
        HumanName name = HumanName.builder()
                .id("someId")
                .given(given)
                .given(otherGiven)
                .given(String.of("value no extension"))
                .family(String.of("Doe"))
                .build();
                
        Patient patient = Patient.builder()
                .id(id)
                .active(Boolean.TRUE)
                .multipleBirth(Integer.of(2))
                .meta(meta)
                .name(name)
                .birthDate(Date.of(LocalDate.now()))
                .build();
        
        FilterOutputStream out = new FilterOutputStream(System.out) {
            public void close() {
                // do nothing
            }
        };
        
        FHIRGenerator.generator(Format.JSON, true).generate(patient, out);
        
        System.out.println("");
        
        FHIRValidator.DEBUG = true;
        FHIRValidator.validator(patient).validate();
    }
}
