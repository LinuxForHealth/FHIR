/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.path.function;

import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.convertsToDateTime;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.empty;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.singleton;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.toDateTime;

import java.util.Collection;
import java.util.List;

import org.linuxforhealth.fhir.path.FHIRPathNode;
import org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;

public class ToDateTimeFunction extends FHIRPathAbstractFunction {
    @Override
    public String getName() {
        return "toDateTime";
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
        if (!convertsToDateTime(context)) {
            return empty();
        }
        return singleton(toDateTime(context));
    }
}
