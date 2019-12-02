/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path.function;

import static com.ibm.fhir.path.util.FHIRPathUtil.empty;
import static com.ibm.fhir.path.util.FHIRPathUtil.getSingleton;
import static com.ibm.fhir.path.util.FHIRPathUtil.isSingleton;
import static com.ibm.fhir.path.util.FHIRPathUtil.unordered;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;

public class DescendantsFunction extends FHIRPathAbstractFunction {
    @Override
    public String getName() {
        return "descendants";
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
        if (!context.isEmpty()) {
            return unordered(isSingleton(context) ? getSingleton(context).descendants() : context.stream()
                .flatMap(node -> node.descendants().stream())
                .collect(Collectors.toList()));
        }
        return empty();
    }
}
