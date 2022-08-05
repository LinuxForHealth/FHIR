/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.path.function;

import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.getInteger;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.isUnordered;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.linuxforhealth.fhir.path.FHIRPathNode;
import org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;

public class SkipFunction extends FHIRPathAbstractFunction {
    @Override
    public String getName() {
        return "skip";
    }

    @Override
    public int getMinArity() {
        return 1;
    }

    @Override
    public int getMaxArity() {
        return 1;
    }

    @Override
    public Collection<FHIRPathNode> apply(EvaluationContext evaluationContext, Collection<FHIRPathNode> context, List<Collection<FHIRPathNode>> arguments) {
        if (isUnordered(context)) {
            throw new IllegalArgumentException("Context must be an ordered collection for function: 'skip'");
        }
        Integer num = getInteger(arguments.get(0));
        return context.stream()
                .skip(num)
                .collect(Collectors.toList());
    }
}
