/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path.function;

import static com.ibm.fhir.path.util.FHIRPathUtil.empty;
import static com.ibm.fhir.path.util.FHIRPathUtil.isUnordered;
import static com.ibm.fhir.path.util.FHIRPathUtil.singleton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;

public class FirstFunction extends FHIRPathAbstractFunction {
    @Override
    public String getName() {
        return "first";
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
        if (isUnordered(context)) {
            throw new IllegalArgumentException("Context must be an ordered collection for function: 'first'");
        }
        if (!context.isEmpty()) {
            List<?> list = (context instanceof List) ? (List<?>) context : new ArrayList<>(context);
            return singleton((FHIRPathNode) list.get(0));
        }
        return empty();
    }
}
