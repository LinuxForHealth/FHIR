/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.path.function;

import static com.ibm.watsonhealth.fhir.model.path.evaluator.FHIRPathEvaluator.SINGLETON_FALSE;
import static com.ibm.watsonhealth.fhir.model.path.evaluator.FHIRPathEvaluator.SINGLETON_TRUE;
import static com.ibm.watsonhealth.fhir.model.path.util.FHIRPathUtil.getBooleanValue;
import static com.ibm.watsonhealth.fhir.model.path.util.FHIRPathUtil.hasBooleanValue;

import java.util.Collection;
import java.util.List;

import com.ibm.watsonhealth.fhir.model.path.FHIRPathBooleanValue;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathNode;

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

    @Override
    public Collection<FHIRPathNode> apply(Collection<FHIRPathNode> context, List<Collection<FHIRPathNode>> arguments) {
        if (hasBooleanValue(context)) {
            FHIRPathBooleanValue value = getBooleanValue(context);
            if (value.not().isTrue()) {
                return SINGLETON_TRUE;
            }
        }
        return SINGLETON_FALSE;
    }
}
