/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.path.function;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.ibm.fhir.model.path.FHIRPathNode;

public class UnionFunction extends FHIRPathAbstractFunction {
    @Override
    public String getName() {
        return "union";
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
        Set<FHIRPathNode> union = new HashSet<>(context);
        union.addAll(arguments.get(0));
        return new ArrayList<>(union);
    }
}
