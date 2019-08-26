/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.validation;

import static com.ibm.watsonhealth.fhir.model.path.util.FHIRPathUtil.isFalse;
import static com.ibm.watsonhealth.fhir.model.path.util.FHIRPathUtil.singleton;
import static com.ibm.watsonhealth.fhir.model.type.String.string;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.ibm.watsonhealth.fhir.model.annotation.Constraint;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathNode;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathResourceNode;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathTree;
import com.ibm.watsonhealth.fhir.model.path.evaluator.FHIRPathEvaluator;
import com.ibm.watsonhealth.fhir.model.path.evaluator.FHIRPathEvaluator.EvaluationContext;
import com.ibm.watsonhealth.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.watsonhealth.fhir.model.resource.Resource;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.IssueSeverity;
import com.ibm.watsonhealth.fhir.model.type.IssueType;
import com.ibm.watsonhealth.fhir.model.util.ModelSupport;
import com.ibm.watsonhealth.fhir.model.validation.exception.FHIRValidationException;
import com.ibm.watsonhealth.fhir.model.visitor.PathAwareVisitorAdapter;

public class FHIRValidator {
    public static boolean DEBUG = false;
    
    private final FHIRPathTree tree;
    
    private FHIRValidator(FHIRPathTree tree) {
        this.tree = Objects.requireNonNull(tree);
        if (!this.tree.getRoot().isResourceNode()) {
            throw new IllegalStateException("Root must be a resource node");
        }
    }
    
    public List<Issue> validate() throws FHIRValidationException {
        try {
            ValidatingVisitor visitor = new ValidatingVisitor(tree);
            tree.getRoot().asResourceNode().resource().accept(visitor);
            return visitor.getIssues();
        } catch (Exception e) {
            throw new FHIRValidationException("An error occurred during validation", e);
        }
    }
    
    public static FHIRValidator validator(FHIRPathTree tree) {
        return new FHIRValidator(tree);
    }
    
    public static FHIRValidator validator(Resource resource) {
        return new FHIRValidator(FHIRPathTree.tree(resource));
    }

    public static class ValidatingVisitor extends PathAwareVisitorAdapter {
        private final FHIRPathTree tree;
        private final FHIRPathEvaluator evaluator;
        private final EvaluationContext evaluationContext;
        
        private List<Issue> issues = new ArrayList<>(); 
        
        private ValidatingVisitor(FHIRPathTree tree) {
            this.tree = tree;
            evaluator = FHIRPathEvaluator.evaluator(tree);
CODE_REMOVED
        }
        
        @Override
        protected void doVisitStart(String elementName, int elementIndex, Element element) {
            validate(element.getClass());
        }

        @Override
        protected void doVisitStart(String elementName, int elementIndex, Resource resource) {
            validate(resource.getClass());
        }

        private List<Issue> getIssues() {
            return Collections.unmodifiableList(issues);
        }

        private FHIRPathResourceNode getResource(Class<?> type, FHIRPathNode node) {
            if (!Resource.class.isAssignableFrom(type)) {
                // the constraint came from a data type
                return (FHIRPathResourceNode) tree.getRoot();
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
                node = tree.getNode(path);
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
                
                Collection<FHIRPathNode> initialContext = singleton(tree.getNode(path));
                if (!Constraint.LOCATION_BASE.equals(constraint.location())) {
                    initialContext = evaluator.evaluate(constraint.location(), initialContext);                  
                }
                
                IssueSeverity severity = Constraint.LEVEL_WARNING.equals(constraint.level()) ? IssueSeverity.WARNING : IssueSeverity.ERROR;
                
                for (FHIRPathNode node : initialContext) {
                    evaluationContext.setExternalConstant("resource", getResource(type, node));
                    
                    Collection<FHIRPathNode> result = evaluator.evaluate(constraint.expression(), singleton(node));
                    
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
