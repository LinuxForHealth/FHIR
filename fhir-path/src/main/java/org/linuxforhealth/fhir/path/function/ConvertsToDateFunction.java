/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.path.function;

import static org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator.SINGLETON_FALSE;
import static org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator.SINGLETON_TRUE;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.convertsToDate;

import java.util.Collection;
import java.util.List;

import org.linuxforhealth.fhir.path.FHIRPathNode;
import org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;

public class ConvertsToDateFunction extends FHIRPathAbstractFunction {
    @Override
    public String getName() {
        return "convertsToDate";
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
        return convertsToDate(context) ? SINGLETON_TRUE : SINGLETON_FALSE;
    }
}