/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.path.function;

import static org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator.SINGLETON_FALSE;
import static org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator.SINGLETON_TRUE;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.empty;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.evaluatesToBoolean;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.getSingleton;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.getStringValue;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.isElementNode;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.isFalse;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.isResourceNode;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.isStringValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.model.annotation.Constraint;
import org.linuxforhealth.fhir.model.resource.StructureDefinition;
import org.linuxforhealth.fhir.model.type.code.IssueSeverity;
import org.linuxforhealth.fhir.model.type.code.IssueType;
import org.linuxforhealth.fhir.model.type.code.StructureDefinitionKind;
import org.linuxforhealth.fhir.model.util.ModelSupport;
import org.linuxforhealth.fhir.path.FHIRPathNode;
import org.linuxforhealth.fhir.path.FHIRPathType;
import org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator;
import org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;
import org.linuxforhealth.fhir.path.exception.FHIRPathException;
import org.linuxforhealth.fhir.profile.ProfileSupport;
import org.linuxforhealth.fhir.registry.FHIRRegistry;

public class ConformsToFunction extends FHIRPathAbstractFunction {
    private static final Logger log = Logger.getLogger(ConformsToFunction.class.getName());

    @Override
    public String getName() {
        return "conformsTo";
    }

    @Override
    public int getMinArity() {
        return 1;
    }

    @Override
    public int getMaxArity() {
        return 1;
    }

    @Override
    public Collection<FHIRPathNode> apply(EvaluationContext evaluationContext, Collection<FHIRPathNode> context, List<Collection<FHIRPathNode>> arguments) {
        if (context.isEmpty()) {
            return empty();
        }

        if (!isResourceNode(context) && !isElementNode(context)) {
            throw new IllegalArgumentException("The 'conformsTo' function must be invoked on a Resource or Element node");
        }

        if (!isStringValue(arguments.get(0))) {
            throw new IllegalArgumentException("The argument to the 'conformsTo' function must be a string value");
        }

        FHIRPathNode node = getSingleton(context);
        FHIRPathType type = node.type();
        Class<?> modelClass = type.modelClass();
        String url = getStringValue(arguments.get(0)).string();

        if (FHIRRegistry.getInstance().hasResource(url, StructureDefinition.class)) {
            StructureDefinition structureDefinition = FHIRRegistry.getInstance().getResource(url, StructureDefinition.class);

            if (FHIRPathType.FHIR_UNKNOWN_RESOURCE_TYPE.equals(type)) {
                if (!StructureDefinitionKind.RESOURCE.equals(structureDefinition.getKind())) {
                    // the profile (or base definition) is not applicable to type: UnknownResourceType
                    generateIssue(evaluationContext, IssueSeverity.INFORMATION, IssueType.INVALID, "Conformance check failed: profile (or base definition) '" + url + "' is not applicable to type: UnknownResourceType", node.path());
                    return SINGLETON_FALSE;
                }

                // unknown resource type conforms to any resource profile (or base resource definition)
                return SINGLETON_TRUE;
            }

            if (!ProfileSupport.isApplicable(structureDefinition, modelClass)) {
                // the profile (or base definition) is not applicable to type: modelClass
                generateIssue(evaluationContext, IssueSeverity.INFORMATION, IssueType.INVALID, "Conformance check failed: profile (or base definition) '" + url + "' is not applicable to type: " + ModelSupport.getTypeName(modelClass), node.path());
                return SINGLETON_FALSE;
            }

            if (node.isResourceNode() && node.asResourceNode().resource() == null) {
                // the node was created by the 'resolve' function and is not backed by a FHIR resource
                return SINGLETON_TRUE;
            }

            if (hasCachedFunctionResult(evaluationContext, context, arguments)) {
                Collection<FHIRPathNode> result = getCachedFunctionResult(evaluationContext, context, arguments);

                if (result.isEmpty()) {
                    log.finest("Non-empty (boolean) function result computation is in progress");

                    // non-empty (boolean) function result computation is in progress
                    return SINGLETON_TRUE;
                }

                if (log.isLoggable(Level.FINEST)) {
                    log.finest("Cached non-empty (boolean) function result: " + result);
                }

                // return cached non-empty (boolean) function result
                return result;
            }

            // cache empty function result to indicate that non-empty (boolean) function result computation is in progress
            cacheFunctionResult(evaluationContext, context, arguments, empty());

            if (node.isResourceNode() && evaluationContext.hasConstraint() && ProfileSupport.hasResourceAssertedProfile(node.asResourceNode().resource(), structureDefinition)) {
                // optimization
                if (log.isLoggable(Level.FINEST)) {
                    log.finest("Skipping constraint evaluation for profile '" + url + "'");
                }

                // cache and return non-empty (boolean) function result
                return cacheFunctionResult(evaluationContext, context, arguments, SINGLETON_TRUE);
            }

            // save parent constraint reference
            Constraint parentConstraint = evaluationContext.getConstraint();

            List<Constraint> constraints = new ArrayList<>();
            if (ProfileSupport.isProfile(structureDefinition)) {
                // only generated constraints are checked (base model constraints should be checked by FHIRValidator)
                constraints.addAll(ProfileSupport.getConstraints(url, modelClass));
            }

            FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
            for (Constraint constraint : constraints) {
                evaluationContext.setConstraint(constraint);
                try {
                    Collection<FHIRPathNode> result = evaluator.evaluate(evaluationContext, constraint.expression(), context);
                    if (evaluatesToBoolean(result) && isFalse(result)) {
                        // constraint validation failed
                        generateIssue(evaluationContext, IssueSeverity.INFORMATION, IssueType.INVARIANT, constraint.id() + ": " + constraint.description(), node.path());

                        // restore parent constraint reference
                        evaluationContext.setConstraint(parentConstraint);

                        // cache and return non-empty (boolean) function result
                        return cacheFunctionResult(evaluationContext, context, arguments, SINGLETON_FALSE);
                    }
                } catch (FHIRPathException e) {
                    throw new RuntimeException("An unexpected error occurred while evaluating the following expression: " + constraint.expression(), e);
                }
                evaluationContext.unsetConstraint();
            }

            // restore parent constraint reference
            evaluationContext.setConstraint(parentConstraint);
        } else {
            generateIssue(evaluationContext, IssueSeverity.WARNING, IssueType.NOT_SUPPORTED, "Conformance check was not performed: profile (or base definition) '" + url + "' is not supported", node.path());
        }

        // cache and return non-empty (boolean) function result
        return cacheFunctionResult(evaluationContext, context, arguments, SINGLETON_TRUE);
    }
}
