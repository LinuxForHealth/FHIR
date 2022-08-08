/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.path.function;

import static org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator.SINGLETON_FALSE;
import static org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator.SINGLETON_TRUE;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.linuxforhealth.fhir.path.FHIRPathNode;
import org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;

public class IsDistinctFunction extends FHIRPathAbstractFunction {
    private boolean DEBUG = false;
    
    @Override
    public String getName() {
        return "isDistinct";
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
        Set<FHIRPathNode> items = new HashSet<>();
        for (FHIRPathNode node : context) {
            if (!items.add(node)) {
                if (DEBUG) {
                    System.out.println("Found duplicate node:" + node);
                }
                return SINGLETON_FALSE;
            }
        }
        return SINGLETON_TRUE;
    }
}
