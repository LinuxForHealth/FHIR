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
import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.getTypeName;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.ibm.watsonhealth.fhir.model.annotation.Constraint;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathNode;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathPrimitiveValue;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathTree;
import com.ibm.watsonhealth.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.watsonhealth.fhir.model.resource.Resource;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.IssueSeverity;
import com.ibm.watsonhealth.fhir.model.type.IssueType;
import com.ibm.watsonhealth.fhir.model.validation.exception.FHIRValidationException;
import com.ibm.watsonhealth.fhir.model.visitor.PathAwareVisitorAdapter;

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
                resource.accept(getTypeName(resource.getClass()), visitor);
            } else if (root.isElementNode()) {
                Element element = root.asElementNode().element();
                element.accept(getTypeName(element.getClass()), visitor);
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

    public static class ValidatingVisitor extends PathAwareVisitorAdapter {
        private static final String WARNING_LEVEL = "Warning";
        private static final String BASE_LOCATION = "(base)";
        
        private final FHIRPathTree tree;
        
        private List<Issue> issues = new ArrayList<>(); 
        
        private ValidatingVisitor(FHIRPathTree tree) {
            this.tree = tree;
        }
        
        @Override
        protected void doVisitStart(String elementName, Element element) {
            validate(element.getClass(), getCurrentPath());            
        }

        @Override
        protected void doVisitStart(String elementName, Resource resource) {
            validate(resource.getClass(), getCurrentPath());            
        }

        private List<Issue> getIssues() {
            return Collections.unmodifiableList(issues);
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

        private void validate(Class<?> type, java.lang.String path) {
            List<Constraint> constraints = getConstraints(type);
            for (Constraint constraint : constraints) {
                if (constraint.modelChecked()) {
                    if (DEBUG) {
                        System.out.println("    Constraint: " + constraint.id() + " is model-checked");
                    }
                    continue;
                }
                validate(constraint, path);
            }
        }

        private void validate(Constraint constraint, java.lang.String path) {
            try {
                if (DEBUG) {
                    System.out.println("    Constraint: " + constraint);
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
                        // constraint validation failed
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
            } catch (Exception e) {
                throw new Error(e);
            }
        }
    }
}
