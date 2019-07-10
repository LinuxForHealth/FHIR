/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.path.function;

import java.util.Collection;
import java.util.List;

import com.ibm.watsonhealth.fhir.model.path.FHIRPathNode;

public class CombineFunction implements FHIRPathFunction {
    @Override
    public String getName() {
        return "combine";
    }

    @Override
    public int getMinArity() {
        return 1;
    }

    @Override
    public int getMaxArity() {
        return 1;
    }

    public Collection<FHIRPathNode> apply(Collection<FHIRPathNode> context, List<Collection<FHIRPathNode>> arguments) {
        throw new UnsupportedOperationException("Function: '" + getName() + "' is not supported");
    }
}
