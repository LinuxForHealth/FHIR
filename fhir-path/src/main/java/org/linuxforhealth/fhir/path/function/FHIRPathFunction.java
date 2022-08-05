/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.path.function;

import java.util.Collection;
import java.util.List;

import org.linuxforhealth.fhir.path.FHIRPathNode;
import org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;
import org.linuxforhealth.fhir.path.function.registry.FHIRPathFunctionRegistry;

public interface FHIRPathFunction {
    String getName();
    int getMinArity();
    int getMaxArity();
    
    Collection<FHIRPathNode> apply(EvaluationContext evaluationContext, Collection<FHIRPathNode> context, List<Collection<FHIRPathNode>> arguments);
    
    static FHIRPathFunctionRegistry registry() {
        return FHIRPathFunctionRegistry.getInstance();
    }
}
