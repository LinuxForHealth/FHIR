/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.path.function;

import com.ibm.watsonhealth.fhir.model.path.FHIRPathParser.ExpressionContext;
import com.ibm.watsonhealth.fhir.model.path.evaluator.FHIRPathEvaluator;
import com.ibm.watsonhealth.fhir.model.path.exception.FHIRPathException;

public class ToIntegerFunction implements FHIRPathFunction {
    @Override
    public String getName() {
        return "toInteger";
    }

    @Override
    public Object invoke(FHIRPathEvaluator evaluator, Object context, ExpressionContext... arguments) throws FHIRPathException {
        if (arguments.length != 0) {
            throw new FHIRPathException("Unexpected number of arguments: " + arguments.length);
        }
        if (context instanceof Number) {
            return ((Number) context).intValue();
        }
        return null;
    }
}
