/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.model.path.function;

import static com.ibm.watson.health.fhir.model.path.FHIRPathStringValue.stringValue;
import static com.ibm.watson.health.fhir.model.path.util.FHIRPathUtil.empty;
import static com.ibm.watson.health.fhir.model.path.util.FHIRPathUtil.getPrimitiveValue;
import static com.ibm.watson.health.fhir.model.path.util.FHIRPathUtil.getSingleton;
import static com.ibm.watson.health.fhir.model.path.util.FHIRPathUtil.hasPrimitiveValue;
import static com.ibm.watson.health.fhir.model.path.util.FHIRPathUtil.isSingleton;
import static com.ibm.watson.health.fhir.model.path.util.FHIRPathUtil.singleton;

import java.util.Collection;
import java.util.List;

import com.ibm.watson.health.fhir.model.path.FHIRPathNode;
import com.ibm.watson.health.fhir.model.path.FHIRPathPrimitiveValue;
import com.ibm.watson.health.fhir.model.path.FHIRPathQuantityNode;
import com.ibm.watson.health.fhir.model.path.evaluator.FHIRPathEvaluator.EvaluationContext;

public class ToStringFunction extends FHIRPathAbstractFunction {
    @Override
    public java.lang.String getName() {
        return "toString";
    }

    @Override
    public int getMinArity() {
        return 0;
    }

    @Override
    public int getMaxArity() {
        return 0;
    }
    
    @Override
    public Collection<FHIRPathNode> apply(EvaluationContext evaluationContext, Collection<FHIRPathNode> context, List<Collection<FHIRPathNode>> arguments) {
        if (!isSingleton(context)) {
            return empty();
        }
        if (hasPrimitiveValue(context)) {
            FHIRPathPrimitiveValue value = getPrimitiveValue(context);
            return singleton(stringValue(value.toString()));
        }
        FHIRPathNode node = getSingleton(context);
        if (node instanceof FHIRPathQuantityNode) {
            return singleton(stringValue(node.toString()));
        }
        return empty();
    }
}
