/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.path.function;

import com.ibm.watsonhealth.fhir.model.path.FHIRPathParser.ExpressionContext;
import com.ibm.watsonhealth.fhir.model.path.evaluator.FHIRPathEvaluator_;
import com.ibm.watsonhealth.fhir.model.path.exception.FHIRPathException;

public interface FHIRPathFunction {
    String getName();

    Object invoke(FHIRPathEvaluator_ evaluator, Object context, ExpressionContext... arguments) throws FHIRPathException;
}
