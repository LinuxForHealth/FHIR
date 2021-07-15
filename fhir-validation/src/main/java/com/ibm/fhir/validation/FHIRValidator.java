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
import static com.ibm.fhir.validation.util.FHIRValidationUtil.ISSUE_COMPARATOR;
import static com.ibm.fhir.validation.util.FHIRValidationUtil.hasErrors;

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
import com.ibm.fhir.model.constraint.factory.ConstraintFactory;
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
import com.ibm.fhir.path.util.DiagnosticsEvaluationListener;
import com.ibm.fhir.path.visitor.FHIRPathDefaultNodeVisitor;
import com.ibm.fhir.profile.ProfileSupport;
import com.ibm.fhir.registry.FHIRRegistry;
import com.ibm.fhir.validation.exception.FHIRValidationException;

import net.jcip.annotations.NotThreadSafe;

/**
 * A validator that uses conformance resources from the {@link com.ibm.fhir.registry.FHIRRegistry}
 * to validate resource instances against the base specification and, optionally, extended profiles.
 *
 * The static factory method {@link #validator()} is threadsafe, but the created instances are not.
 */
@NotThreadSafe
public class FHIRValidator {
    public static final String SOURCE_VALIDATOR = "http://ibm.com/fhir/validation/FHIRValidator";

    private static final Logger log = Logger.getLogger(FHIRValidator.class.getName());

    private final ValidatingNodeVisitor visitor;
    private final boolean failFast;

    private FHIRValidator() {
        this(false);
    }

    private FHIRValidator(boolean failFast) {
        visitor = new ValidatingNodeVisitor(failFast);
        this.failFast = failFast;
    }

    /**
     * Indicates whether this validator is fail-fast
     *
     * <p>A fail-fast validator is one that terminates on the first issue it finds with a severity of ERROR.
     *
     * @return
     *     true if this validator is fail-fast, false otherwise
     */
    public boolean isFailFast() {
        return failFast;
    }

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
            return visitor.validate(evaluationContext, includeResourceAssertedProfiles, Arrays.asList(profiles));
        } catch (Exception e) {
            throw new FHIRValidationException("An error occurred during validation", e);
        }
    }

    public static FHIRValidator validator() {
        return new FHIRValidator();
    }

    public static FHIRValidator validator(boolean failFast) {
        return new FHIRValidator(failFast);
    }

    private static Issue issue(IssueSeverity severity, IssueType code, String description, FHIRPathNode node) {
        return issue(severity, code, description, null, node.path());
    }

    private static Issue issue(IssueSeverity severity, IssueType code, String details, String diagnostics, String expression) {
        return Issue.builder()
            .severity(severity)
            .code(code)
            .details(CodeableConcept.builder()
                .text(string(details))
                .build())
            .diagnostics((diagnostics != null) ? string(diagnostics) : null)
            .expression(string(expression))
            .build();
    }

    private static class ValidatingNodeVisitor extends FHIRPathDefaultNodeVisitor {
        private final boolean failFast;

        private FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        private EvaluationContext evaluationContext;
        private boolean includeResourceAssertedProfiles;
        private List<String> profiles;
        private List<Issue> issues = new ArrayList<>();
        private DiagnosticsEvaluationListener diagnosticsEvaluationListener = new DiagnosticsEvaluationListener();
        private boolean aborted = false;

        private ValidatingNodeVisitor(boolean failFast) {
            this.failFast = failFast;
        }

        private List<Issue> validate(EvaluationContext evaluationContext, boolean includeResourceAssertedProfiles, List<String> profiles) {
            reset();
            this.evaluationContext = evaluationContext;
            this.includeResourceAssertedProfiles = includeResourceAssertedProfiles;
            this.profiles = profiles;
            this.evaluationContext.getTree().getRoot().accept(this);
            Collections.sort(issues, ISSUE_COMPARATOR);
            return Collections.unmodifiableList(issues);
        }

        private void reset() {
            issues.clear();
            diagnosticsEvaluationListener.reset();
            aborted = false;
        }

        @Override
        protected void visitChildren(FHIRPathNode node) {
            for (FHIRPathNode child : node.children()) {
                if (aborted) {
                    break;
                }
                child.accept(this);
            }
        }

        @Override
        public void doVisit(FHIRPathElementNode node) {
            validate(node);
        }

        @Override
        public void doVisit(FHIRPathResourceNode node) {
            validate(node);
        }

        private void validate(FHIRPathElementNode elementNode) {
            Class<?> elementType = elementNode.element().getClass();
            List<Constraint> constraints = new ArrayList<>(ModelSupport.getConstraints(elementType));
            if (Extension.class.equals(elementType)) {
                String url = elementNode.element().as(Extension.class).getUrl();
                if (isAbsolute(url)) {
                    if (FHIRRegistry.getInstance().hasResource(url, StructureDefinition.class)) {
                        constraints.add(ConstraintFactory.createConstraint("generated-ext-1", Constraint.LEVEL_RULE, Constraint.LOCATION_BASE, "Extension must conform to definition '" + url + "'", "conformsTo('" + url + "')", SOURCE_VALIDATOR, false, true));
                    } else {
                        issues.add(issue(IssueSeverity.WARNING, IssueType.NOT_SUPPORTED, "Extension definition '" + url + "' is not supported", elementNode));
                    }
                }
            }
            validate(elementNode, constraints);
        }

        private void validate(FHIRPathResourceNode resourceNode) {
            Class<?> resourceType = resourceNode.resource().getClass();
            List<Constraint> constraints = new ArrayList<>(ModelSupport.getConstraints(resourceType));
            if (includeResourceAssertedProfiles) {
                List<String> resourceAssertedProfiles = ProfileSupport.getResourceAssertedProfiles(resourceNode.resource());
                validateProfileReferences(resourceNode, resourceAssertedProfiles, true);
                constraints.addAll(ProfileSupport.getConstraints(resourceAssertedProfiles, resourceType));
            }
            if (!profiles.isEmpty() && !resourceNode.path().contains(".")) {
                validateProfileReferences(resourceNode, profiles, false);
                constraints.addAll(ProfileSupport.getConstraints(profiles, resourceType));
            }
            validate(resourceNode, constraints);
        }

        private void validateProfileReferences(FHIRPathResourceNode resourceNode, List<String> profiles, boolean resourceAsserted) {
            Class<?> resourceType = resourceNode.resource().getClass();
            for (String url : profiles) {
                if (aborted) {
                    break;
                }
                StructureDefinition profile = ProfileSupport.getProfile(url);
                if (profile == null) {
                    issues.add(issue(resourceAsserted ? IssueSeverity.WARNING : IssueSeverity.ERROR, IssueType.NOT_SUPPORTED, "Profile '" + url + "' is not supported", resourceNode));
                } else if (!ProfileSupport.isApplicable(profile, resourceType)) {
                    issues.add(issue(IssueSeverity.ERROR, IssueType.INVALID, "Profile '" + url + "' is not applicable to resource type: " + resourceType.getSimpleName(), resourceNode));
                }
                aborted = failFast && hasErrors(issues);
            }
        }

        private void validate(FHIRPathNode node, Collection<Constraint> constraints) {
            for (Constraint constraint : constraints) {
                if (aborted) {
                    break;
                }
                if (constraint.modelChecked()) {
                    if (log.isLoggable(Level.FINER)) {
                        log.finer("    Constraint: " + constraint.id() + " is model-checked");
                    }
                    continue;
                }
                evaluationContext.setConstraint(constraint);
                validate(node, constraint);
                evaluationContext.unsetConstraint();
            }
        }

        private void validate(FHIRPathNode node, Constraint constraint) {
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

                if (constraint.generated()) {
                    evaluationContext.addEvaluationListener(diagnosticsEvaluationListener);
                }

                for (FHIRPathNode contextNode : initialContext) {
                    if (aborted) {
                        break;
                    }

                    diagnosticsEvaluationListener.reset();
                    evaluationContext.setExternalConstant("rootResource", getRootResourceNode(evaluationContext.getTree(), contextNode));
                    evaluationContext.setExternalConstant("resource", getResourceNode(evaluationContext.getTree(), contextNode));

                    Collection<FHIRPathNode> result = evaluator.evaluate(evaluationContext, constraint.expression(), singleton(contextNode));

                    issues.addAll(evaluationContext.getIssues());
                    evaluationContext.clearIssues();

                    if (evaluatesToBoolean(result) && isFalse(result)) {
                        issues.add(issue(severity, IssueType.INVARIANT, constraint.id() + ": " + constraint.description(), diagnosticsEvaluationListener.getDiagnostics(), contextNode.path()));
                        if (failFast && IssueSeverity.ERROR.equals(severity)) {
                            aborted = true;
                        }
                    }

                    if (log.isLoggable(Level.FINER)) {
                        log.finer("    Evaluation result: " + result + ", Path: " + contextNode.path());
                    }
                }

                if (constraint.generated()) {
                    evaluationContext.removeEvaluationListener(diagnosticsEvaluationListener);
                }
            } catch (Exception e) {
                throw new RuntimeException("An error occurred while validating constraint: " + constraint.id() + " with location: " + constraint.location() + " and expression: " + constraint.expression() + " at path: " + path, e);
            }
        }

        private boolean isAbsolute(String url) {
            try {
                return new URI(url).isAbsolute();
            } catch (URISyntaxException e) {
                log.warning("Invalid URI: " + url);
            }
            return false;
        }
    }
}
