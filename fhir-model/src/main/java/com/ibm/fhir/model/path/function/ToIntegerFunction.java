/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.path.function;

import static com.ibm.fhir.model.path.FHIRPathIntegerValue.integerValue;
import static com.ibm.fhir.model.path.util.FHIRPathUtil.empty;
import static com.ibm.fhir.model.path.util.FHIRPathUtil.getPrimitiveValue;
import static com.ibm.fhir.model.path.util.FHIRPathUtil.hasPrimitiveValue;
import static com.ibm.fhir.model.path.util.FHIRPathUtil.singleton;

import java.util.Collection;
import java.util.List;

import com.ibm.fhir.model.path.FHIRPathNode;
import com.ibm.fhir.model.path.FHIRPathPrimitiveValue;
import com.ibm.fhir.model.path.evaluator.FHIRPathEvaluator.EvaluationContext;

public class ToIntegerFunction extends FHIRPathAbstractFunction {
    @Override
    public String getName() {
        return "toInteger";
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
        if (!hasPrimitiveValue(context)) {
            return empty();
        }
        FHIRPathPrimitiveValue value = getPrimitiveValue(context);
        if (value.isNumberValue() && value.asNumberValue().isIntegerValue()) {
            return singleton(value);
        }
        if (value.isStringValue()) {
            String string = value.asStringValue().string();
            try {
                return singleton(integerValue(Integer.parseInt(string)));
            } catch (NumberFormatException e) {
                return empty();
            }
        }
        if (value.isBooleanValue()) {
            return singleton(integerValue(value.asBooleanValue().isTrue() ? 1 : 0));
        }            
        return empty();        
    }
}
