/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.validation;

import static com.ibm.watsonhealth.fhir.model.path.util.FHIRPathUtil.eval;
import static com.ibm.watsonhealth.fhir.model.path.util.FHIRPathUtil.getPrimitiveValue;
import static com.ibm.watsonhealth.fhir.model.path.util.FHIRPathUtil.hasPrimitiveValue;
import static com.ibm.watsonhealth.fhir.model.path.util.FHIRPathUtil.singleton;
import static com.ibm.watsonhealth.fhir.model.type.String.string;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import java.util.StringJoiner;

import com.ibm.watsonhealth.fhir.model.annotation.Constraint;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathNode;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathPrimitiveValue;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathTree;
import com.ibm.watsonhealth.fhir.model.path.exception.FHIRPathException;
import com.ibm.watsonhealth.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.watsonhealth.fhir.model.resource.Resource;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.IssueSeverity;
import com.ibm.watsonhealth.fhir.model.type.IssueType;
import com.ibm.watsonhealth.fhir.model.validation.exception.FHIRValidationException;
import com.ibm.watsonhealth.fhir.model.visitor.AbstractVisitor;
import com.ibm.watsonhealth.fhir.model.visitor.Visitable;

public class FHIRValidator {
    public static boolean DEBUG = false;
    
    private final FHIRPathTree tree;
    
    private FHIRValidator(FHIRPathTree tree) {
        this.tree = Objects.requireNonNull(tree);
    }
    
    public List<Issue> validate() throws FHIRValidationException {
        try {
            ValidatingVisitor visitor = new ValidatingVisitor(tree);
            FHIRPathNode root = tree.getRoot();
            if (root.isResourceNode()) {
                Resource resource = root.asResourceNode().resource();
                resource.accept(resource.getClass().getSimpleName(), visitor);
            } else if (root.isElementNode()) {
                Element element = root.asElementNode().element();
                element.accept(element.getClass().getSimpleName(), visitor);;
            }
            return visitor.getIssues();
        } catch (Exception e) {
            throw new FHIRValidationException("An error occurred during validation", e);
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
        private static final String WARNING_LEVEL = "Warning";
        private static final String BASE_LOCATION = "(base)";
        private final Stack<java.lang.String> nameStack = new Stack<>();
        private final Stack<java.lang.String> pathStack = new Stack<>();
        private final Stack<java.lang.Integer> indexStack = new Stack<>();
        private final FHIRPathTree tree;
        private List<Issue> issues = new ArrayList<>(); 
        
        private ValidatingVisitor(FHIRPathTree tree) {
            this.tree = tree;
        }
        
        private List<Issue> getIssues() {
            return Collections.unmodifiableList(issues);
        }
        
        private void checkConstraints(Class<?> type, java.lang.String path) {
            List<Constraint> constraints = getConstraints(type);
            for (Constraint constraint : constraints) {
                try {
                    if (DEBUG) {
                        System.out.println("    Constraint: " + constraint.id() + ", " + constraint.level() + ", " + constraint.location() + ", " + constraint.expression() + ", " + constraint.description());
                    }
                    
                    Collection<FHIRPathNode> initialContext = singleton(tree.getNode(path));
                    if (!BASE_LOCATION.equals(constraint.location())) {
                        initialContext = eval(constraint.location(), initialContext);
                    }
                    
                    java.lang.String expr = constraint.expression();
                    Collection<FHIRPathNode> result = eval(expr, initialContext);
                    
                    if (hasPrimitiveValue(result)) {
                        FHIRPathPrimitiveValue value = getPrimitiveValue(result);
                        if (value.isBooleanValue() && value.asBooleanValue().isFalse()) {
                            // constraint check failed
                            String level = constraint.level();
                            IssueSeverity severity = WARNING_LEVEL.equals(level) ? IssueSeverity.WARNING : IssueSeverity.ERROR;
                            Issue issue = Issue.builder(severity, IssueType.INVARIANT)
                                    .location(string(path))
                                    .diagnostics(string(constraint.id() + ": " + constraint.description()))
                                    .build();
                            issues.add(issue);
                        }
                    }
                    
                    if (DEBUG) {
                        System.out.println("    Evaluation result: " + result);
                    }
                } catch (FHIRPathException e) {
                    throw new Error(e);
                }
            }
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
                for (Constraint constraint : _class.getDeclaredAnnotationsByType(Constraint.class)) {
                    constraints.add(constraint);
                }
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
            checkConstraints(element.getClass(), getPath());
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
            checkConstraints(resource.getClass(), getPath());
            if (index != -1) {
                indexStack.set(indexStack.size() - 1, indexStack.peek() + 1);
            }
        }
    }
}
