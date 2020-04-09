/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path.function;

import static com.ibm.fhir.path.evaluator.FHIRPathEvaluator.SINGLETON_FALSE;
import static com.ibm.fhir.path.evaluator.FHIRPathEvaluator.SINGLETON_TRUE;
import static com.ibm.fhir.path.util.FHIRPathUtil.empty;
import static com.ibm.fhir.path.util.FHIRPathUtil.evaluatesToBoolean;
import static com.ibm.fhir.path.util.FHIRPathUtil.getSingleton;
import static com.ibm.fhir.path.util.FHIRPathUtil.getStringValue;
import static com.ibm.fhir.path.util.FHIRPathUtil.isElementNode;
import static com.ibm.fhir.path.util.FHIRPathUtil.isFalse;
import static com.ibm.fhir.path.util.FHIRPathUtil.isResourceNode;
import static com.ibm.fhir.path.util.FHIRPathUtil.isStringValue;

import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.resource.StructureDefinition;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;
import com.ibm.fhir.path.exception.FHIRPathException;
import com.ibm.fhir.profile.ProfileSupport;
import com.ibm.fhir.registry.FHIRRegistry;

public class ConformsToFunction extends FHIRPathAbstractFunction {
    private static final Logger log = Logger.getLogger(ConformsToFunction.class.getName());
    private static final String HL7_STRUCTURE_DEFINITION_URL_PREFIX = "http://hl7.org/fhir/StructureDefinition/";

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

        if (node.isResourceNode() && node.asResourceNode().resource() == null) {
            return SINGLETON_TRUE;
        }

        Class<?> modelClass = node.type().modelClass();
        String url = getStringValue(arguments.get(0)).string();

        if (modelClass != null && FHIRRegistry.getInstance().hasResource(url, StructureDefinition.class)) {
            if (url.startsWith(HL7_STRUCTURE_DEFINITION_URL_PREFIX)) {
                String s = url.substring(HL7_STRUCTURE_DEFINITION_URL_PREFIX.length());
                if (s.equals(modelClass.getSimpleName())) {
                    return SINGLETON_TRUE;
                }
            }

            FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
            for (Constraint constraint : ProfileSupport.getConstraints(url, modelClass)) {
                try {
                    Collection<FHIRPathNode> result = evaluator.evaluate(evaluationContext, constraint.expression(), context);
                    if (evaluatesToBoolean(result) && isFalse(result)) {
                        return SINGLETON_FALSE;
                    }
                } catch (FHIRPathException e) {
                    log.log(Level.WARNING, "An unexpected error occurred while evaluating the following expression: " + constraint.expression(), e);
                }
            }
        }

        return SINGLETON_TRUE;
    }
}
