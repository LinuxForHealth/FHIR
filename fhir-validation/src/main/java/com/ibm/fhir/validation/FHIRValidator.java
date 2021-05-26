/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.validation;

import static com.ibm.fhir.model.type.String.string;
import static com.ibm.fhir.path.util.FHIRPathUtil.evaluatesToBoolean;
import static com.ibm.fhir.path.util.FHIRPathUtil.getResourceNode;
import static com.ibm.fhir.path.util.FHIRPathUtil.getRootResourceNode;
import static com.ibm.fhir.path.util.FHIRPathUtil.isFalse;
import static com.ibm.fhir.path.util.FHIRPathUtil.singleton;
import static com.ibm.fhir.profile.ProfileSupport.createConstraint;
import static com.ibm.fhir.validation.util.FHIRValidationUtil.ISSUE_COMPARATOR;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.StructureDefinition;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Extension;
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
import com.ibm.fhir.registry.FHIRRegistry;
import com.ibm.fhir.validation.exception.FHIRValidationException;

public class FHIRValidator {
    private static final Logger log = Logger.getLogger(FHIRValidator.class.getName());

    private final ValidatingNodeVisitor visitor = new ValidatingNodeVisitor();

    private FHIRValidator() { }

    /**
     * Validate a {@link Resource} against constraints in the base specification and
     * resource-asserted profile references or specific profile references but not both.
     *
     * <p>Resource-asserted profile references that are not available in the FHIRRegistry result in issues with severity WARNING.
     *
     * <p>Unknown profile references passed as arguments to this method result in issues with severity ERROR.
     *
     * <p>Profiles that are incompatible with the resource type being validated result in issues with severity ERROR.
     *
     * <p>Profile references that are passed into this method are only applicable to the outermost
     * resource (not contained resources).
     *
     * @param resource
     *     a {@link Resource} instance (the target of validation)
     * @param profiles
     *     specific profile references to validate the resource against
     * @return
     *     a non-null, possibly empty list of issues generated during validation (sorted by severity)
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
     * <p>Resource-asserted profile references that are not available in the FHIRRegistry result in issues with severity WARNING.
     *
     * <p>Unknown profile references passed as arguments to this method result in issues with severity ERROR.
     *
     * <p>Profiles that are incompatible with the resource type being validated result in issues with severity ERROR.
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
     *     a non-null, possibly empty list of issues generated during validation (sorted by severity)
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
     * <p>Resource-asserted profile references that are not available in the FHIRRegistry result in issues with severity WARNING.
     *
     * <p>Unknown profile references passed as arguments to this method result in issues with severity ERROR.
     *
     * <p>Profiles that are incompatible with the resource type being validated result in issues with severity ERROR.
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
     *     a non-null, possibly empty list of issues generated during validation (sorted by severity)
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
     * <p>Resource-asserted profile references that are not available in the FHIRRegistry result in issues with severity WARNING.
     *
     * <p>Unknown profile references passed as arguments to this method result in issues with severity ERROR.
     *
     * <p>Profiles that are incompatible with the resource type being validated result in issues with severity ERROR.
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
     *     a non-null, possibly empty list of issues generated during validation (sorted by severity)
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
            evaluationContext.setResolveRelativeReferences(true);
            List<Issue> issues = new ArrayList<>();
            validateProfileReferences(evaluationContext.getTree().getRoot().asResourceNode(), Arrays.asList(profiles), false, issues);
            issues.addAll(visitor.validate(evaluationContext, includeResourceAssertedProfiles, profiles));
            Collections.sort(issues, ISSUE_COMPARATOR);
            return Collections.unmodifiableList(issues);
        } catch (Exception e) {
            throw new FHIRValidationException("An error occurred during validation", e);
        }
    }

    public static FHIRValidator validator() {
        return new FHIRValidator();
    }

    /**
     * Validate a list of profile references to ensure they are supported (known by the FHIR registry) and applicable
     * (the type constrained by the profile is compatible with the resource being validated).
     *
     * <p>Resource-asserted profile references that are not available in the FHIRRegistry result in issues with severity WARNING.
     *
     * <p>Unknown profile references passed as arguments to this method result in issues with severity ERROR.
     *
     * <p>Profiles that are incompatible with the resource type being validated result in issues with severity ERROR.
     *
     * @param resourceNode
     *     the resource node being validated by a FHIRValidator instance
     * @param profiles
     *     the list of profile references to validate
     * @param resourceAsserted
     *     indicates whether the profile references came from the resource or were explicitly passed in as arguments
     * @param issues
     *     the list of issues to add to
     */
    private static void validateProfileReferences(
            FHIRPathResourceNode resourceNode,
            List<String> profiles,
            boolean resourceAsserted,
            List<Issue> issues) {
        Class<?> resourceType = resourceNode.resource().getClass();
        for (String url : profiles) {
            StructureDefinition profile = ProfileSupport.getProfile(url);
            if (profile == null) {
                issues.add(issue(resourceAsserted ? IssueSeverity.WARNING : IssueSeverity.ERROR, IssueType.NOT_SUPPORTED, "Profile '" + url + "' is not supported", resourceNode));
            } else if (!ProfileSupport.isApplicable(profile, resourceType)) {
                issues.add(issue(IssueSeverity.ERROR, IssueType.INVALID, "Profile '" + url + "' is not applicable to resource type: " + resourceType.getSimpleName(), resourceNode));
            }
        }
    }

    private static Issue issue(IssueSeverity severity, IssueType code, String description, FHIRPathNode node) {
        return Issue.builder()
            .severity(severity)
            .code(code)
            .details(CodeableConcept.builder()
                .text(string(description))
                .build())
            .expression(string(node.path()))
            .build();
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
            return issues;
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
         * @throws RuntimeException if the registered constraints cannot be evaluated for the passed element node
         */
        private void validate(FHIRPathElementNode elementNode) {
            Class<?> elementType = elementNode.element().getClass();
            Collection<Constraint> constraints = ModelSupport.getConstraints(elementType);
            if (Extension.class.equals(elementType)) {
                String url = elementNode.element().as(Extension.class).getUrl();
                if (isAbsolute(url)) {
                    if (FHIRRegistry.getInstance().hasResource(url, StructureDefinition.class)) {
                        constraints = new ArrayList<>(constraints);
                        constraints.add(createConstraint("generated-ext-1", Constraint.LEVEL_RULE, Constraint.LOCATION_BASE, "Extension must conform to definition '" + url + "'", "conformsTo('" + url + "')", false, true));
                    } else {
                        issues.add(issue(IssueSeverity.WARNING, IssueType.NOT_SUPPORTED, "Extension definition '" + url + "' is not supported", elementNode));
                    }
                }
            }
            validate(elementType, elementNode, constraints);
        }

        private boolean isAbsolute(String url) {
            try {
                return new URI(url).isAbsolute();
            } catch (URISyntaxException e) {
                log.warning("Invalid URI: " + url);
            }
            return false;
        }

        /**
         * @throws RuntimeException if the registered constraints cannot be evaluated for the passed resource node
         */
        private void validate(FHIRPathResourceNode resourceNode) {
            Class<?> resourceType = resourceNode.resource().getClass();
            validate(resourceType, resourceNode, ModelSupport.getConstraints(resourceType));
            if (includeResourceAssertedProfiles) {
                List<String> resourceAssertedProfiles = ProfileSupport.getResourceAssertedProfiles(resourceNode.resource());
                validateProfileReferences(resourceNode, resourceAssertedProfiles, true, issues);
                validate(resourceType, resourceNode, ProfileSupport.getConstraints(resourceAssertedProfiles, resourceType));
            }
            if (!profiles.isEmpty() && !resourceNode.path().contains(".")) {
                validate(resourceType, resourceNode, ProfileSupport.getConstraints(profiles, resourceType));
            }
        }

        /**
         * @throws RuntimeException if one of the passed constraints cannot be evaluated for the passed node
         */
        private void validate(Class<?> type, FHIRPathNode node, Collection<Constraint> constraints) {
            for (Constraint constraint : constraints) {
                if (constraint.modelChecked()) {
                    if (log.isLoggable(Level.FINER)) {
                        log.finer("    Constraint: " + constraint.id() + " is model-checked");
                    }
                    continue;
                }
                evaluationContext.setConstraint(constraint);
                validate(type, node, constraint);
                evaluationContext.unsetConstraint();
            }
        }

        /**
         * @throws RuntimeException if the passed constraint cannot be evaluated for the passed node
         */
        private void validate(Class<?> type, FHIRPathNode node, Constraint constraint) {
            String path = node.path();
            try {
                if (log.isLoggable(Level.FINER)) {
                    log.finer("    Constraint: " + constraint);
                }

                Collection<FHIRPathNode> initialContext = singleton(node);
                if (!Constraint.LOCATION_BASE.equals(constraint.location())) {
                    initialContext = evaluator.evaluate(evaluationContext, constraint.location(), initialContext);
                    issues.addAll(evaluationContext.getIssues());
                    evaluationContext.clearIssues();
                }

                IssueSeverity severity = Constraint.LEVEL_WARNING.equals(constraint.level()) ? IssueSeverity.WARNING : IssueSeverity.ERROR;

                for (FHIRPathNode contextNode : initialContext) {
                    evaluationContext.setExternalConstant("rootResource", getRootResourceNode(evaluationContext.getTree(), contextNode));
                    evaluationContext.setExternalConstant("resource", getResourceNode(evaluationContext.getTree(), contextNode));
                    Collection<FHIRPathNode> result = evaluator.evaluate(evaluationContext, constraint.expression(), singleton(contextNode));
                    issues.addAll(evaluationContext.getIssues());
                    evaluationContext.clearIssues();

                    if (evaluatesToBoolean(result) && isFalse(result)) {
                        issues.add(issue(severity, IssueType.INVARIANT, constraint.id() + ": " + constraint.description(), contextNode));
                    }

                    if (log.isLoggable(Level.FINER)) {
                        log.finer("    Evaluation result: " + result + ", Path: " + contextNode.path());
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("An error occurred while validating constraint: " + constraint.id() +
                    " with location: " + constraint.location() + " and expression: " + constraint.expression() +
                    " at path: " + path, e);
            }
        }
    }
}
