/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.path.function;

import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.empty;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.getInteger;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.getStringValue;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.singleton;

import java.util.Collection;
import java.util.List;

import org.linuxforhealth.fhir.path.FHIRPathNode;
import org.linuxforhealth.fhir.path.FHIRPathStringValue;
import org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;

public class SubstringFunction extends FHIRPathStringAbstractFunction {
    @Override
    public String getName() {
        return "substring";
    }

    @Override
    public int getMinArity() {
        return 1;
    }

    @Override
    public int getMaxArity() {
        return 2;
    }
    
    @Override
    public Collection<FHIRPathNode> doApply(EvaluationContext evaluationContext, Collection<FHIRPathNode> context, List<Collection<FHIRPathNode>> arguments) {
        
        FHIRPathStringValue value = getStringValue(context);
        
        int start = getInteger(arguments.get(0));
        
        if (start < 0 || start > value.length() - 1) {
            return empty();
        }
        
        if (arguments.size() == 2) {
            return singleton(value.substring(start, getInteger(arguments.get(1))));
        }
        
        return singleton(value.substring(start));        
    }
}
