/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.path.util;

import java.util.Collection;

import com.ibm.watsonhealth.fhir.model.path.FHIRPathNode;
import com.ibm.watsonhealth.fhir.model.path.evaluator.FHIRPathEvaluator;
import com.ibm.watsonhealth.fhir.model.path.exception.FHIRPathException;

public final class FHIRPathUtil {
    private FHIRPathUtil() { }

    public static Collection<FHIRPathNode> eval(String expr) throws FHIRPathException {
        return eval(expr, null);
    }

    public static Collection<FHIRPathNode> eval(String expr, Collection<FHIRPathNode> context) throws FHIRPathException {
        return FHIRPathEvaluator.evaluator(expr).evaluate(context);
    }
}
