/*
 * (C) Copyright IBM Corp. 2019, 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path.function;

import static com.ibm.fhir.path.util.FHIRPathUtil.empty;
import static com.ibm.fhir.path.util.FHIRPathUtil.getSingleton;
import static com.ibm.fhir.path.util.FHIRPathUtil.hasStringValue;
import static com.ibm.fhir.path.util.FHIRPathUtil.isSingleton;
import static com.ibm.fhir.path.util.FHIRPathUtil.isStringSubType;

import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.FHIRPathType;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;

/**
 * Abstract class which contains common apply logic for all abstract string manipulation functions
 */
public abstract class FHIRPathStringAbstractFunction extends FHIRPathAbstractFunction {
    
    private static final Logger logger = Logger.getLogger(FHIRPathStringAbstractFunction.class.getName());
    
    /**
     * @implSpec Common apply logic for all abstract string manipulation functions. Delegates to doApply()
     */
    @Override
    public Collection<FHIRPathNode> apply(EvaluationContext evaluationContext, Collection<FHIRPathNode> context, List<Collection<FHIRPathNode>> arguments) {
        if (context.isEmpty()) {
            return empty();
        }
        if (!isSingleton(context)) {
            throw new IllegalArgumentException("Input collection must not contain more than one item, but found " + context.size());
            
        } else if (!hasStringValue(context)) {
            FHIRPathNode node = getSingleton(context);
            if (isStringSubType(node)) {
                logger.fine("collection item does not have a value, returning empty");
                return empty();
            }
            throw new IllegalArgumentException("Input collection item must be of type String, but found '"+node.type().getName()+ "'");
            
        }
        return doApply(evaluationContext, context, arguments);    
    }
    
    /**
     * For function implementation add the logic here
     */
    protected abstract  Collection<FHIRPathNode> doApply(EvaluationContext evaluationContext, Collection<FHIRPathNode> context, List<Collection<FHIRPathNode>> arguments);
    
}
