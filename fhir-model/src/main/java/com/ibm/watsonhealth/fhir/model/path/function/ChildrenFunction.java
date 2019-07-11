/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.path.function;

import static com.ibm.watsonhealth.fhir.model.path.util.FHIRPathUtil.empty;
import static com.ibm.watsonhealth.fhir.model.path.util.FHIRPathUtil.getSingleton;
import static com.ibm.watsonhealth.fhir.model.path.util.FHIRPathUtil.isSingleton;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.ibm.watsonhealth.fhir.model.path.FHIRPathNode;

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

    public Collection<FHIRPathNode> apply(Collection<FHIRPathNode> context, List<Collection<FHIRPathNode>> arguments) {
        if (!context.isEmpty()) {
            return isSingleton(context) ? getSingleton(context).children() : context.stream()
                .flatMap(node -> node.children().stream())
                .collect(Collectors.toList());
        }
        return empty();
    }
}
