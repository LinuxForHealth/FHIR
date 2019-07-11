/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.path.function;

import static com.ibm.watsonhealth.fhir.model.path.evaluator.FHIRPathEvaluator.SINGLETON_BOOLEAN_FALSE;
import static com.ibm.watsonhealth.fhir.model.path.evaluator.FHIRPathEvaluator.SINGLETON_BOOLEAN_TRUE;
import static com.ibm.watsonhealth.fhir.model.path.util.FHIRPathUtil.empty;
import static com.ibm.watsonhealth.fhir.model.path.util.FHIRPathUtil.getPrimitiveValue;
import static com.ibm.watsonhealth.fhir.model.path.util.FHIRPathUtil.hasPrimitiveValue;

import java.util.Collection;
import java.util.List;

import com.ibm.watsonhealth.fhir.model.path.FHIRPathNode;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathPrimitiveValue;

public class NotFunction extends FHIRPathAbstractFunction {
    @Override
    public String getName() {
        return "not";
    }

    @Override
    public int getMinArity() {
        return 0;
    }

    @Override
    public int getMaxArity() {
        return 0;
    }

    public Collection<FHIRPathNode> apply(Collection<FHIRPathNode> context, List<Collection<FHIRPathNode>> arguments) {
        if (hasPrimitiveValue(context)) {
            FHIRPathPrimitiveValue value = getPrimitiveValue(context);
            if (value.isBooleanValue()) {
                return value.asBooleanValue().not().isTrue() ? SINGLETON_BOOLEAN_TRUE : SINGLETON_BOOLEAN_FALSE;
            }
        }
        return empty();
    }
}
