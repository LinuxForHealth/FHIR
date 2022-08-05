/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.path.function;

import static org.linuxforhealth.fhir.path.FHIRPathIntegerValue.integerValue;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.empty;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.getSystemValue;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.hasSystemValue;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.singleton;

import java.util.Collection;
import java.util.List;

import org.linuxforhealth.fhir.path.FHIRPathNode;
import org.linuxforhealth.fhir.path.FHIRPathSystemValue;
import org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;

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
        if (!hasSystemValue(context)) {
            return empty();
        }
        FHIRPathSystemValue value = getSystemValue(context);
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
