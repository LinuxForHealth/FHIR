/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path.function;

import static com.ibm.fhir.path.evaluator.FHIRPathEvaluator.SINGLETON_FALSE;
import static com.ibm.fhir.path.evaluator.FHIRPathEvaluator.SINGLETON_TRUE;

import java.util.Collection;
import java.util.List;

import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;

public class AllTrueFunction extends FHIRPathAbstractFunction {
    @Override
    public String getName() {
        return "allTrue";
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
        for (FHIRPathNode node : context) {
            if (node.isSystemValue() && node.asSystemValue().isBooleanValue()) {
                if (node.asSystemValue().asBooleanValue().isFalse()) {
                    return SINGLETON_FALSE;
                }
            } else {
                throw new IllegalArgumentException("Invalid argument; expected boolean but found " + node.type());
            }
        }
        return SINGLETON_TRUE;
    }
}
