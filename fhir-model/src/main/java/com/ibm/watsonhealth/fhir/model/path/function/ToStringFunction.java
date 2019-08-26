/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.path.function;

import static com.ibm.watsonhealth.fhir.model.path.FHIRPathStringValue.stringValue;
import static com.ibm.watsonhealth.fhir.model.path.util.FHIRPathUtil.empty;
import static com.ibm.watsonhealth.fhir.model.path.util.FHIRPathUtil.getSingleton;
import static com.ibm.watsonhealth.fhir.model.path.util.FHIRPathUtil.isSingleton;
import static com.ibm.watsonhealth.fhir.model.path.util.FHIRPathUtil.singleton;

import java.util.Collection;
import java.util.List;

import com.ibm.watsonhealth.fhir.model.path.FHIRPathNode;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathPrimitiveValue;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathQuantityNode;
import com.ibm.watsonhealth.fhir.model.path.evaluator.FHIRPathEvaluator.EvaluationContext;

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
        FHIRPathNode node = getSingleton(context);
        if (node instanceof FHIRPathPrimitiveValue || node instanceof FHIRPathQuantityNode) {
            return singleton(stringValue(node.toString()));
        }
        return empty();
    }
}
