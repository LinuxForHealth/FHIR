/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.path.function;

import static com.ibm.fhir.model.path.evaluator.FHIRPathEvaluator.SINGLETON_TRUE;

import java.util.Collection;
import java.util.List;

import com.ibm.fhir.model.path.FHIRPathNode;
import com.ibm.fhir.model.path.evaluator.FHIRPathEvaluator.EvaluationContext;

public class ConformsToFunction extends FHIRPathAbstractFunction {
//  private static final String HL7_STRUCTURE_DEFINITION_URL_PREFIX = "http://hl7.org/fhir/StructureDefinition/";
    
    @Override
    public String getName() {
        return "conformsTo";
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
        /*
        if (!hasResourceNode(context) && !hasElementNode(context)) {
            throw new IllegalArgumentException("The 'conformsTo' function can only be invoked on a Resource or Element node");
        }
        
        if (!hasStringValue(arguments.get(0))) {
            throw new IllegalArgumentException("The argument to the 'conformsTo' function must be a string");
        }
        
        Class<?> modelClass = getSingleton(context).type().modelClass();
        
        String url = getStringValue(arguments.get(0)).string();
        if (url.startsWith(HL7_STRUCTURE_DEFINITION_URL_PREFIX)) {
            String s = url.substring(HL7_STRUCTURE_DEFINITION_URL_PREFIX.length());
            if (modelClass != null && s.equals(modelClass.getSimpleName())) {
                return SINGLETON_TRUE;
            }
        } else {
            throw new IllegalArgumentException("Unrecognized url: " + url);
        }
        
        return SINGLETON_FALSE;
        */
        return SINGLETON_TRUE;
    }
}
