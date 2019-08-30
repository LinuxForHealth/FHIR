/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.model.path.function;

import static com.ibm.watson.health.fhir.model.path.util.FHIRPathUtil.empty;
import static com.ibm.watson.health.fhir.model.path.util.FHIRPathUtil.singleton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.ibm.watson.health.fhir.model.path.FHIRPathNode;
import com.ibm.watson.health.fhir.model.path.evaluator.FHIRPathEvaluator.EvaluationContext;

public class LastFunction extends FHIRPathAbstractFunction {
    @Override
    public String getName() {
        return "last";
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
            List<?> list = (context instanceof List) ? (List<?>) context : new ArrayList<>(context);
            return singleton((FHIRPathNode) list.get(list.size() - 1));
        }
        return empty();
    }
}
