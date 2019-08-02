/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.path.function;

import static com.ibm.watsonhealth.fhir.model.path.util.FHIRPathUtil.getInteger;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.ibm.watsonhealth.fhir.model.path.FHIRPathNode;
import com.ibm.watsonhealth.fhir.model.path.evaluator.FHIRPathEvaluator.Environment;

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
    public Collection<FHIRPathNode> apply(Environment environment, Collection<FHIRPathNode> context, List<Collection<FHIRPathNode>> arguments) {
        Integer num = getInteger(arguments.get(0));
        return context.stream()
                .skip(num)
                .collect(Collectors.toList());
    }
}
