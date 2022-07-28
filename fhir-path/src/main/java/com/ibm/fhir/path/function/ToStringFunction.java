/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path.function;

import static com.ibm.fhir.path.FHIRPathStringValue.stringValue;
import static com.ibm.fhir.path.util.FHIRPathUtil.empty;
import static com.ibm.fhir.path.util.FHIRPathUtil.getSystemValue;
import static com.ibm.fhir.path.util.FHIRPathUtil.hasSystemValue;
import static com.ibm.fhir.path.util.FHIRPathUtil.singleton;
import static com.ibm.fhir.path.util.FHIRPathUtil.isSingleton;

import java.util.Collection;
import java.util.List;

import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.FHIRPathSystemValue;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;

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
        if(!isSingleton(context)) {
            throw new IllegalArgumentException("Input collection must not contain more than one item, but found " + context.size());
            
        }
        if (hasSystemValue(context)) {
            FHIRPathSystemValue value = getSystemValue(context);
            return singleton(stringValue(value.toString()));
        }
        return empty();
    }
}
