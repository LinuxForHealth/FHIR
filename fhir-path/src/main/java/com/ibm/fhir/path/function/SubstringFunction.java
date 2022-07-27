/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path.function;

import static com.ibm.fhir.path.util.FHIRPathUtil.empty;
import static com.ibm.fhir.path.util.FHIRPathUtil.getInteger;
import static com.ibm.fhir.path.util.FHIRPathUtil.getStringValue;
import static com.ibm.fhir.path.util.FHIRPathUtil.singleton;
import static com.ibm.fhir.path.util.FHIRPathUtil.checkStringValue;
import java.util.Collection;
import java.util.List;

import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.FHIRPathStringValue;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;

public class SubstringFunction extends FHIRPathAbstractFunction {
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
    public Collection<FHIRPathNode> apply(EvaluationContext evaluationContext, Collection<FHIRPathNode> context, List<Collection<FHIRPathNode>> arguments) {
        
        checkStringValue(context);
        
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
