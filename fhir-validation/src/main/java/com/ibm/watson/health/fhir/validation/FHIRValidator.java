/*
 * (C) Copyright IBM Corp. 2019
 * 
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.validation;

import static com.ibm.watson.health.fhir.model.path.util.FHIRPathUtil.isFalse;
import static com.ibm.watson.health.fhir.model.path.util.FHIRPathUtil.singleton;
import static com.ibm.watson.health.fhir.model.type.String.string;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.ibm.watson.health.fhir.model.annotation.Constraint;
import com.ibm.watson.health.fhir.model.path.FHIRPathNode;
import com.ibm.watson.health.fhir.model.path.FHIRPathResourceNode;
import com.ibm.watson.health.fhir.model.path.evaluator.FHIRPathEvaluator;
import com.ibm.watson.health.fhir.model.path.evaluator.FHIRPathEvaluator.EvaluationContext;
import com.ibm.watson.health.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.watson.health.fhir.model.resource.Resource;
import com.ibm.watson.health.fhir.model.type.CodeableConcept;
import com.ibm.watson.health.fhir.model.type.Element;
import com.ibm.watson.health.fhir.model.type.IssueSeverity;
import com.ibm.watson.health.fhir.model.type.IssueType;
import com.ibm.watson.health.fhir.model.util.ModelSupport;
import com.ibm.watson.health.fhir.model.visitor.PathAwareVisitorAdapter;
import com.ibm.watson.health.fhir.validation.exception.FHIRValidationException;

public class FHIRValidator {
    public static boolean DEBUG = false;
    
    private ValidatingVisitor visitor = new ValidatingVisitor();
    
    private FHIRValidator() { }
    
    public List<Issue> validate(Resource resource) throws FHIRValidationException {
        return validate(new EvaluationContext(resource));
    }
    
    public List<Issue> validate(EvaluationContext evaluationContext) throws FHIRValidationException {
        Objects.requireNonNull(evaluationContext);
        Objects.requireNonNull(evaluationContext.getTree());
        if (!evaluationContext.getTree().getRoot().isResourceNode()) {
            throw new IllegalArgumentException("Root must be resource node");
        }
        try {
            return visitor.validate(evaluationContext);
        } catch (Exception e) {
            throw new FHIRValidationException("An error occurred during validation", e);
        }
    }
    
    public static FHIRValidator validator() {
        return new FHIRValidator();
    }

    private static class ValidatingVisitor extends PathAwareVisitorAdapter {
        private FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        private EvaluationContext evaluationContext;
        private List<Issue> issues = new ArrayList<>();
        
        private ValidatingVisitor() { }
        
        private List<Issue> validate(EvaluationContext evaluationContext) {
            reset();
            this.evaluationContext = evaluationContext;
            this.evaluationContext.getTree()
                .getRoot()
                .asResourceNode()
                .resource()
                .accept(this);
            return issues;
        }
        
        private void reset() {
            issues.clear();
        }
        
        @Override
        protected void doVisitStart(String elementName, int elementIndex, Element element) {
            validate(element.getClass());
        }

        @Override
        protected void doVisitStart(String elementName, int elementIndex, Resource resource) {
            validate(resource.getClass());
        }

        private FHIRPathResourceNode getResource(Class<?> type, FHIRPathNode node) {
            if (!Resource.class.isAssignableFrom(type)) {
                // the constraint came from a data type
                return (FHIRPathResourceNode) evaluationContext.getTree().getRoot();
            }
            
            if (node.isResourceNode()) {
                // the current context node is a resource node
                return (FHIRPathResourceNode) node;
            }
            
            // move up in the tree to find the first ancestor that is a resource node
            String path = node.path();
            int index = path.lastIndexOf(".");
            while (index != -1) {
                path = path.substring(0, index);
                node = evaluationContext.getTree().getNode(path);
                if (node instanceof FHIRPathResourceNode) {
                    return (FHIRPathResourceNode) node;
                }
                index = path.lastIndexOf(".");
            }
            
            return null;
        }

        private void validate(Class<?> type) {
            Set<Constraint> constraints = ModelSupport.getConstraints(type);
            String path = getPath();
            for (Constraint constraint : constraints) {
                if (constraint.modelChecked()) {
                    if (DEBUG) {
                        System.out.println("    Constraint: " + constraint.id() + " is model-checked");
                    }
                    continue;
                }
                validate(type, constraint, path);
            }
        }

        private void validate(Class<?> type, Constraint constraint, String path) {
            try {
                if (DEBUG) {
                    System.out.println("    Constraint: " + constraint);
                }
                
                Collection<FHIRPathNode> initialContext = singleton(evaluationContext.getTree().getNode(path));
                if (!Constraint.LOCATION_BASE.equals(constraint.location())) {
                    initialContext = evaluator.evaluate(evaluationContext, constraint.location(), initialContext);                  
                }
                
                IssueSeverity severity = Constraint.LEVEL_WARNING.equals(constraint.level()) ? IssueSeverity.WARNING : IssueSeverity.ERROR;
                
                for (FHIRPathNode node : initialContext) {
                    evaluationContext.setExternalConstant("resource", getResource(type, node));
                    Collection<FHIRPathNode> result = evaluator.evaluate(evaluationContext, constraint.expression(), singleton(node));
                    
                    if (!result.isEmpty() && isFalse(result)) {                        
                        // constraint validation failed
                        Issue issue = Issue.builder()
                                .severity(severity)
                                .code(IssueType.INVARIANT)
                                .details(CodeableConcept.builder().text(string(constraint.id() + ": " + constraint.description())).build())
                                .expression(string(node.path()))
                                .build();
                        
                        issues.add(issue);
                    }
                    
                    if (DEBUG) {
                        System.out.println("    Evaluation result: " + result + ", Path: " + node.path());
                    }                    
                }
            } catch (Exception e) {
                throw new Error("An error occurred while validating constraint: " + constraint.id() + 
                    " with location: " + constraint.location() + " and expression: " + constraint.expression() + 
                    " at path: " + path, e);
            }
        }
    }
}