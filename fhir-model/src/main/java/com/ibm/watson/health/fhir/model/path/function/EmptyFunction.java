/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.model.path.function;

import static com.ibm.watson.health.fhir.model.path.evaluator.FHIRPathEvaluator.SINGLETON_FALSE;
import static com.ibm.watson.health.fhir.model.path.evaluator.FHIRPathEvaluator.SINGLETON_TRUE;

import java.util.Collection;
import java.util.List;

import com.ibm.watson.health.fhir.model.path.FHIRPathNode;
import com.ibm.watson.health.fhir.model.path.evaluator.FHIRPathEvaluator.EvaluationContext;

public class EmptyFunction extends FHIRPathAbstractFunction {
    @Override
    public String getName() {
        return "empty";
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
        return context.isEmpty() ? SINGLETON_TRUE : SINGLETON_FALSE;
    }
}
