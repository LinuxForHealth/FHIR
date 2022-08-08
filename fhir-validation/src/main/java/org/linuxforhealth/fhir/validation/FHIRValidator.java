/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.validation;

import static org.linuxforhealth.fhir.model.type.String.string;
import static org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator.SINGLETON_FALSE;
import static org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator.SINGLETON_TRUE;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.evaluatesToBoolean;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.getResourceNode;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.getRootResourceNode;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.isFalse;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.singleton;
import static org.linuxforhealth.fhir.validation.util.FHIRValidationUtil.ISSUE_COMPARATOR;
import static org.linuxforhealth.fhir.validation.util.FHIRValidationUtil.hasErrors;

import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.model.annotation.Constraint;
import org.linuxforhealth.fhir.model.annotation.Constraint.FHIRPathConstraintValidator;
import org.linuxforhealth.fhir.model.constraint.spi.ConstraintValidator;
import org.linuxforhealth.fhir.model.resource.OperationOutcome.Issue;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.resource.StructureDefinition;
import org.linuxforhealth.fhir.model.type.CodeableConcept;
import org.linuxforhealth.fhir.model.type.Extension;
import org.linuxforhealth.fhir.model.type.code.IssueSeverity;
import org.linuxforhealth.fhir.model.type.code.IssueType;
import org.linuxforhealth.fhir.model.util.ModelSupport;
import org.linuxforhealth.fhir.model.visitor.PathAwareCollectingVisitor;
import org.linuxforhealth.fhir.model.visitor.Visitable;
import org.linuxforhealth.fhir.path.FHIRPathElementNode;
import org.linuxforhealth.fhir.path.FHIRPathNode;
import org.linuxforhealth.fhir.path.FHIRPathResourceNode;
import org.linuxforhealth.fhir.path.FHIRPathTree;
import org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator;
import org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;
import org.linuxforhealth.fhir.path.util.DiagnosticsEvaluationListener;
import org.linuxforhealth.fhir.path.visitor.FHIRPathDefaultNodeVisitor;
import org.linuxforhealth.fhir.profile.ProfileSupport;
import org.linuxforhealth.fhir.registry.FHIRRegistry;
import org.linuxforhealth.fhir.validation.exception.FHIRValidationException;

import net.jcip.annotations.NotThreadSafe;

/**
 * A validator that uses conformance resources from the {@link org.linuxforhealth.fhir.registry.FHIRRegistry}
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
        private static final Map<Class<?>, IsValidFunction> IS_VALID_FUNCTION_MAP = new ConcurrentHashMap<>();

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
            validate(elementNode, constraints);
        }

        private boolean isAbsolute(String url) {
            try {
                return new URI(url).isAbsolute();
            } catch (URISyntaxException e) {
                log.warning("Invalid URI: " + url);
            }
            return false;
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

            // path (with array indices) -> Extension instance (which never have a url version suffix)
            Map<String, Extension> pathToExtension = gatherInstanceExtensions(resourceNode);
            // path (with no array indices) -> Extension URLs (with no version suffix)
            Map<String, Set<String>> alreadyGeneratedExtensionConstraints = gatherExtensionConstraints(constraints);

            // for each extension in the resource instance
            for (Entry<String, Extension> entry : pathToExtension.entrySet()) {
                String path = entry.getKey();
                Extension e = entry.getValue();
                String url = e.getUrl();
                if (isAbsolute(url)) {
                    // remove the array indices from the path
                    String reducedPath = path.replaceAll("\\[[0-9]+\\]", "");

                    // if the profile validation already has this covered, then move on to the next extension
                    if (alreadyGeneratedExtensionConstraints.containsKey(reducedPath)
                            && alreadyGeneratedExtensionConstraints.get(reducedPath).contains(url)) {
                        continue;
                    }

                    // otherwise generate the instance-specifc conformsTo constraint (or add a warning if we don't have a definition for it)
                    if (FHIRRegistry.getInstance().hasResource(url, StructureDefinition.class)) {
                        constraints.add(Constraint.Factory.createConstraint("generated-ext~" + path + "<" + url + ">", Constraint.LEVEL_RULE, path,
                                "Extension must conform to definition '" + url + "'",
                                "conformsTo('" + url + "')", SOURCE_VALIDATOR, false, true));
                    } else {
                        issues.add(issue(IssueSeverity.WARNING, IssueType.NOT_SUPPORTED, "Extension definition '" + url + "' is not supported", null, path));
                    }
                }
            }

            validate(resourceNode, constraints);
        }

        /**
         * Collect all the extensions from the resource instance and index them by their simple FHIRPath paths
         */
        private Map<String, Extension> gatherInstanceExtensions(FHIRPathResourceNode resourceNode) {
            PathAwareCollectingVisitor<Extension> extCollector = new PathAwareCollectingVisitor<Extension>(Extension.class);
            resourceNode.resource().accept(extCollector);
            Map<String, Extension> pathToExtension = extCollector.getResult();
            return pathToExtension;
        }

        /**
         * Gather all the constraint ids and process the specially-marked extension ones into a map
         * from the paths to the set of corresponding extension URLs (with no version suffix).
         */
        private Map<String, Set<String>> gatherExtensionConstraints(List<Constraint> constraints) {
            Map<String, Set<String>> alreadyGeneratedExtensionConstraints = new HashMap<>();
            for (Constraint c : constraints) {
                int urlMark = c.id().lastIndexOf("<");
                if (urlMark <= 0 || !c.id().endsWith(">")) {
                    continue;
                }

                String firstPart = c.id().substring(0, urlMark);
                int pathMark = firstPart.lastIndexOf("~");
                if (pathMark <= 0) {
                    continue;
                }
                String path = firstPart.substring(pathMark + 1);

                String url = c.id().substring(urlMark + 1, c.id().length() - 1);
                alreadyGeneratedExtensionConstraints.computeIfAbsent(path, t -> new HashSet<>()).add(url);
            }
            return alreadyGeneratedExtensionConstraints;
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
            ConstraintValidator<?> validator = getConstraintValidator(constraint.validatorClass());

            if ((constraint.expression() == null || constraint.expression().isEmpty()) && validator == null) {
                log.log(Level.WARNING, "No expression or validator for constraint: " + constraint);
                return;
            }

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

                    Collection<FHIRPathNode> result;

                    if (validator != null) {
                        result = isValid(validator, contextNode, constraint) ? SINGLETON_TRUE : SINGLETON_FALSE;
                    } else {
                        diagnosticsEvaluationListener.reset();
                        evaluationContext.setExternalConstant("rootResource", getRootResourceNode(evaluationContext.getTree(), contextNode));
                        evaluationContext.setExternalConstant("resource", getResourceNode(evaluationContext.getTree(), contextNode));

                        result = evaluator.evaluate(evaluationContext, constraint.expression(), singleton(contextNode));

                        issues.addAll(evaluationContext.getIssues());
                        evaluationContext.clearIssues();
                    }

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
                throw new RuntimeException("An error occurred while validating constraint: " + constraint.id() + " with location: " + constraint.location() + ((validator != null) ? " and validator: " + validator.getClass().getName() : " and expression: " + constraint.expression()) + " at path: " + path, e);
            }
        }

        private ConstraintValidator<?> getConstraintValidator(Class<? extends ConstraintValidator<?>> validatorClass) {
            if (validatorClass == null || FHIRPathConstraintValidator.class.equals(validatorClass)) {
                return null;
            }
            try {
                return validatorClass.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                log.log(Level.WARNING, "Unable to instantiate ConstraintValidator class: " + validatorClass.getName());
            }
            return null;
        }

        private boolean isValid(ConstraintValidator<?> validator, FHIRPathNode node, Constraint constraint) {
            return getIsValidFunction(validator.getClass()).apply(validator, getValidationTarget(node), constraint);
        }

        private IsValidFunction getIsValidFunction(Class<?> validatorClass) {
            return IS_VALID_FUNCTION_MAP.computeIfAbsent(validatorClass, k -> computeIsValidFunction(validatorClass));
        }

        private IsValidFunction computeIsValidFunction(Class<?> validatorClass) {
            try {
                MethodHandles.Lookup lookup = MethodHandles.lookup();
                Method isValidMethod = findIsValidMethod(validatorClass);
                MethodHandle handle = lookup.unreflect(isValidMethod);
                CallSite site = LambdaMetafactory.metafactory(
                        lookup,
                        "apply",
                        MethodType.methodType(IsValidFunction.class),
                        MethodType.methodType(Boolean.TYPE, ConstraintValidator.class, Visitable.class, Constraint.class),
                        handle,
                        handle.type());
                return (IsValidFunction) site.getTarget().invoke();
            } catch (Throwable t) {
                throw new RuntimeException("Unable to compute IsValidFunction for ConstraintValidator class: " + validatorClass.getName(), t);
            }
        }

        private Method findIsValidMethod(Class<?> validatorClass) {
            for (Method method : validatorClass.getDeclaredMethods()) {
                if ("isValid".equals(method.getName()) &&
                        (method.getParameterCount() == 2) &&
                        Visitable.class.isAssignableFrom(method.getParameterTypes()[0]) &&
                        Constraint.class.equals(method.getParameterTypes()[1])) {
                    return method;
                }
            }
            throw new AssertionError();
        }

        private Visitable getValidationTarget(FHIRPathNode node) {
            if (node.isElementNode()) {
                return node.asElementNode().element();
            }
            if (node.isResourceNode()) {
                return node.asResourceNode().resource();
            }
            throw new AssertionError();
        }

        @FunctionalInterface
        interface IsValidFunction {
            boolean apply(ConstraintValidator<?> validator, Visitable visitable, Constraint constraint);
        }
    }
}
