/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path.function;

import static com.ibm.fhir.path.evaluator.FHIRPathEvaluator.SINGLETON_FALSE;
import static com.ibm.fhir.path.evaluator.FHIRPathEvaluator.SINGLETON_TRUE;
import static com.ibm.fhir.path.util.FHIRPathUtil.empty;
import static com.ibm.fhir.path.util.FHIRPathUtil.evaluatesToBoolean;
import static com.ibm.fhir.path.util.FHIRPathUtil.evaluatesToTrue;

import java.util.Collection;
import java.util.List;

import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;

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
    public Collection<FHIRPathNode> apply(EvaluationContext evaluationContext, Collection<FHIRPathNode> context, List<Collection<FHIRPathNode>> arguments) {
        if (context.isEmpty()) {
            return empty();
        }
        if (!evaluatesToBoolean(context)) {
            throw new IllegalArgumentException();
        }
        return evaluatesToTrue(context) ? SINGLETON_FALSE : SINGLETON_TRUE;
    }
}
