/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.path.function;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;

import com.ibm.watsonhealth.fhir.model.path.FHIRPathNode;
import com.ibm.watsonhealth.fhir.model.path.function.registry.FHIRPathFunctionRegistry;

public interface FHIRPathFunction extends BiFunction<Collection<FHIRPathNode>, List<Collection<FHIRPathNode>>, Collection<FHIRPathNode>> {
    String getName();
    int getMinArity();
    int getMaxArity();
    
    @Override
    Collection<FHIRPathNode> apply(Collection<FHIRPathNode> context, List<Collection<FHIRPathNode>> arguments);
    
    static FHIRPathFunctionRegistry registry() {
        return FHIRPathFunctionRegistry.getInstance();
    }
}
