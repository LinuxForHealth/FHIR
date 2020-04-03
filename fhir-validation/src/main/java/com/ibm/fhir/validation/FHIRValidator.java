/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.validation;

import static com.ibm.fhir.model.type.String.string;
import static com.ibm.fhir.path.util.FHIRPathUtil.evaluatesToBoolean;
import static com.ibm.fhir.path.util.FHIRPathUtil.isFalse;
import static com.ibm.fhir.path.util.FHIRPathUtil.singleton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.resource.DomainResource;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.code.IssueSeverity;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.path.FHIRPathElementNode;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.FHIRPathResourceNode;
import com.ibm.fhir.path.FHIRPathTree;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;
import com.ibm.fhir.path.visitor.FHIRPathDefaultNodeVisitor;
import com.ibm.fhir.profile.ProfileSupport;
import com.ibm.fhir.validation.exception.FHIRValidationException;

public class FHIRValidator {
    public static boolean DEBUG = false;

    private ValidatingNodeVisitor visitor = new ValidatingNodeVisitor();

    private FHIRValidator() { }

    /**
     * Validate a {@link Resource} against constraints in the base specification and
     * resource-asserted profile references or specific profile references but not both.
     *
     * <p>Resource-asserted profiles which are unknown to the server will be ignored. Conversely,
     * unknown profiles passed explicitly as arguments will result in a FHIRValidationException.
     *
     * <p>Profile references that are passed into this method are only applicable to the outermost
     * resource (not contained resources).
     *
     * @param resource
     *     a {@link Resource} instance (the target of validation)
     * @param profiles
     *     specific profile references to validate the resource against
     * @return
     *     list of issues generated during validation
     * @throws FHIRValidationException
     *     for errors that occur during validation
     */
    public List<Issue> validate(Resource resource, String... profiles) throws FHIRValidationException {
        return validate(resource, (profiles.length == 0), profiles);
    }

    /**
     * Validate a {@link Resource} against constraints in the base specification and
     * resource-asserted profile references and/or specific profile references.
     *
     * <p>Resource-asserted profiles which are unknown to the server will be ignored. Conversely,
     * unknown profiles passed explicitly as arguments will result in a FHIRValidationException.
     *
     * <p>Profile references that are passed into this method are only applicable to the outermost
     * resource (not contained resources).
     *
     * @param resource
     *     a {@link Resource} instance (the target of validation)
     * @param includeResourceAssertedProfiles
     *     whether or not to consider resource-asserted profiles during validation
     * @param profiles
     *     specific profile references to validate the resource against
     * @return
     *     list of issues generated during validation
     * @throws FHIRValidationException
     *     for errors that occur during validation
     */
    public List<Issue> validate(Resource resource, boolean includeResourceAssertedProfiles, String... profiles) throws FHIRValidationException {
        return validate(new EvaluationContext(resource), includeResourceAssertedProfiles, profiles);
    }

    /**
     * Validate a resource, using an {@link EvaluationContext}, against constraints in the base specification and
     * resource-asserted profile references or specific profile references but not both.
     *
     * <p>Resource-asserted profiles which are unknown to the server will be ignored. Conversely,
     * unknown profiles passed explicitly as arguments will result in a FHIRValidationException.
     *
     * <p>Profile references that are passed into this method are only applicable to the outermost
     * resource (not contained resources).
     *
     * @param evaluationContext
     *     the {@link EvaluationContext} for this validation which includes a {@link FHIRPathTree}
     *     built from a {@link Resource} instance (the target of validation)
     * @param profiles
     *     specific profile references to validate the evaluation context against
     * @return
     *     list of issues generated during validation
     * @throws FHIRValidationException
     *     for errors that occur during validation
     * @see {@link FHIRPathEvaluator.EvaluationContext}
     */
    public List<Issue> validate(EvaluationContext evaluationContext, String... profiles) throws FHIRValidationException {
        return validate(evaluationContext, (profiles.length == 0), profiles);
    }

    /**
     * Validate a resource, using an {@link EvaluationContext}, against constraints in the base specification and
     * resource-asserted profile references and/or specific profile references.
     *
     * <p>Resource-asserted profiles which are unknown to the server will be ignored. Conversely,
     * unknown profiles passed explicitly as arguments will result in a FHIRValidationException.
     *
     * <p>Profile references that are passed into this method are only applicable to the outermost
     * resource (not contained resources).
     *
     * @param evaluationContext
     *     the {@link EvaluationContext} for this validation which includes a {@link FHIRPathTree}
     *     built from a {@link Resource} instance (the target of validation)
     * @param includeResourceAssertedProfiles
     *     whether or not to consider resource-asserted profiles during validation
     * @param profiles
     *     specific profile references to validate the evaluation context against
     * @return
     *     list of issues generated during validation
     * @throws FHIRValidationException
     *     for errors that occur during validation
     * @see {@link FHIRPathEvaluator.EvaluationContext}
     */
    public List<Issue> validate(EvaluationContext evaluationContext, boolean includeResourceAssertedProfiles, String... profiles) throws FHIRValidationException {
        Objects.requireNonNull(evaluationContext);
        Objects.requireNonNull(evaluationContext.getTree());
        if (!evaluationContext.getTree().getRoot().isResourceNode()) {
            throw new IllegalArgumentException("Root must be resource node");
        }
        try {
            return visitor.validate(evaluationContext, includeResourceAssertedProfiles, profiles);
        } catch (Exception e) {
            throw new FHIRValidationException("An error occurred during validation", e);
        }
    }

    public static FHIRValidator validator() {
        return new FHIRValidator();
    }

    private static class ValidatingNodeVisitor extends FHIRPathDefaultNodeVisitor {
        private FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        private EvaluationContext evaluationContext;
        private boolean includeResourceAssertedProfiles;
        private List<String> profiles;
        private List<Issue> issues = new ArrayList<>();

        private ValidatingNodeVisitor() { }

        private List<Issue> validate(EvaluationContext evaluationContext, boolean includeResourceAssertedProfiles, String... profiles) {
            reset();
            this.evaluationContext = evaluationContext;
            this.includeResourceAssertedProfiles = includeResourceAssertedProfiles;
            this.profiles = Arrays.asList(profiles);
            this.evaluationContext.getTree().getRoot().accept(this);
            return Collections.unmodifiableList(issues);
        }

        private void reset() {
            issues.clear();
        }

        @Override
        public void doVisit(FHIRPathElementNode node) {
            validate(node);
        }

        @Override
        public void doVisit(FHIRPathResourceNode node) {
            validate(node);
        }

        /**
         * @throws IllegalStateException if the registered constraints cannot be evaluated for the passed node
         */
        private void validate(FHIRPathElementNode elementNode) {
            Class<?> elementType = elementNode.element().getClass();
            validate(elementType, elementNode, ModelSupport.getConstraints(elementType));
        }

        /**
         * @throws IllegalStateException if the registered constraints cannot be evaluated for the passed node
         */
        private void validate(FHIRPathResourceNode resourceNode) {
            Class<?> resourceType = resourceNode.resource().getClass();
            validate(resourceType, resourceNode, ModelSupport.getConstraints(resourceType));
            if (includeResourceAssertedProfiles) {
                validate(resourceType, resourceNode, ProfileSupport.getConstraints(resourceNode.resource()));
            }
            if (!profiles.isEmpty() && !resourceNode.path().contains(".")) {
                validate(resourceType, resourceNode, ProfileSupport.getConstraints(profiles, resourceType));
            }
        }

        /**
         * @throws IllegalStateException if one of the passed constraints cannot be evaluated for the passed node
         * @implNote IllegalStateException is favored over IllegalArgumentException because the constraints
         *           are coming directly from the spec and we'd rather not wrap the IllegalArgumentException from the caller
         */
        private void validate(Class<?> type, FHIRPathNode node, Collection<Constraint> constraints) {
            for (Constraint constraint : constraints) {
                if (constraint.modelChecked()) {
                    if (DEBUG) {
                        System.out.println("    Constraint: " + constraint.id() + " is model-checked");
                    }
                    continue;
                }
                evaluationContext.setConstraint(constraint);
                validate(type, node, constraint);
                evaluationContext.unsetConstraint();
            }
        }

        /**
         * @throws IllegalStateException if the passed constraint cannot be evaluated for the passed node
         * @implNote IllegalStateException is favored over IllegalArgumentException because the constraints
         *           are coming directly from the spec and we'd rather not wrap the IllegalArgumentException from the caller
         */
        private void validate(Class<?> type, FHIRPathNode node, Constraint constraint) {
            String path = node.path();
            try {
                if (DEBUG) {
                    System.out.println("    Constraint: " + constraint);
                }

                Collection<FHIRPathNode> initialContext = singleton(node);
                if (!Constraint.LOCATION_BASE.equals(constraint.location())) {
                    initialContext = evaluator.evaluate(evaluationContext, constraint.location(), initialContext);
                    issues.addAll(evaluationContext.getIssues());
                    evaluationContext.clearIssues();
                }

                IssueSeverity severity = Constraint.LEVEL_WARNING.equals(constraint.level()) ? IssueSeverity.WARNING : IssueSeverity.ERROR;

                for (FHIRPathNode contextNode : initialContext) {
                    evaluationContext.setExternalConstant("rootResource", getRootResourceNode(contextNode));
                    evaluationContext.setExternalConstant("resource", getResourceNode(contextNode));
                    Collection<FHIRPathNode> result = evaluator.evaluate(evaluationContext, constraint.expression(), singleton(contextNode));
                    issues.addAll(evaluationContext.getIssues());
                    evaluationContext.clearIssues();

                    if (evaluatesToBoolean(result) && isFalse(result)) {
                        // constraint validation failed
                        issues.add(Issue.builder()
                            .severity(severity)
                            .code(IssueType.INVARIANT)
                            .details(CodeableConcept.builder()
                                .text(string(constraint.id() + ": " + constraint.description()))
                                .build())
                            .expression(string(contextNode.path()))
                            .build());
                    }

                    if (DEBUG) {
                        System.out.println("    Evaluation result: " + result + ", Path: " + contextNode.path());
                    }
                }
            } catch (Exception e) {
                throw new IllegalStateException("An error occurred while validating constraint: " + constraint.id() +
                    " with location: " + constraint.location() + " and expression: " + constraint.expression() +
                    " at path: " + path, e);
            }
        }

        /**
         * Get the resource node to use as a value for the %resource external constant.
         *
         * @param node
         *     the context node
         * @return
         *     the resource node
         */
        private FHIRPathResourceNode getResourceNode(FHIRPathNode node) {
            // get resource node ancestors for the context node
            List<FHIRPathResourceNode> resourceNodes = getResourceNodes(node);

            // return nearest resource node ancestor
            return resourceNodes.get(0);
        }

        /**
         * Get the resource node to use as a value for the %rootResource external constant.
         *
         * @param node
         *     the context node
         * @return
         *     the rootResource node
         */
        private FHIRPathResourceNode getRootResourceNode(FHIRPathNode node) {
            // get resource node ancestors for the context node
            List<FHIRPathResourceNode> resourceNodes = getResourceNodes(node);
            if (isContained(resourceNodes)) {
                return resourceNodes.get(1);
            } else {
                return resourceNodes.get(0);
            }
        }

        /**
         * Determine whether the list of resource node ancestors indicates containment within a domain resource.
         *
         * @param resourceNodes
         *     the list of resource node ancestors
         * @return
         *     true if there is containment, false otherwise
         */
        private boolean isContained(List<FHIRPathResourceNode> resourceNodes) {
            return resourceNodes.size() > 1 && resourceNodes.get(1).resource().is(DomainResource.class);
        }

        /**
         * Get the list of resource nodes in the path of the given context node (including the context node itself).
         * The ancestors are ordered from node to root (i.e. the node at index 0 is either the current node or
         * the nearest resource node ancestor).
         *
         * @param node
         *     the context node
         * @return
         *     the list of resource node ancestors
         */
        private List<FHIRPathResourceNode> getResourceNodes(FHIRPathNode node) {
            List<FHIRPathResourceNode> resourceNodes = new ArrayList<>();

            if (node.isResourceNode()) {
                resourceNodes.add(node.asResourceNode());
            }

            String path = node.path();
            int index = path.lastIndexOf(".");
            while (index != -1) {
                path = path.substring(0, index);
                node = evaluationContext.getTree().getNode(path);
                if (node.isResourceNode()) {
                    resourceNodes.add(node.asResourceNode());
                }
                index = path.lastIndexOf(".");
            }
            return resourceNodes;
        }
    }
}