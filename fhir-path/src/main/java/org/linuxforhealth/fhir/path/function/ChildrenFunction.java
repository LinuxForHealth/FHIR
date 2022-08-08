/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.path.function;

import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.empty;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.getSingleton;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.isSingleton;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.unordered;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.linuxforhealth.fhir.path.FHIRPathNode;
import org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;

public class ChildrenFunction extends FHIRPathAbstractFunction {
    @Override
    public String getName() {
        return "children";
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
            return unordered(isSingleton(context) ? getSingleton(context).children() : context.stream()
                .flatMap(node -> node.children().stream())
                .collect(Collectors.toList()));
        }
        return empty();
    }
}
