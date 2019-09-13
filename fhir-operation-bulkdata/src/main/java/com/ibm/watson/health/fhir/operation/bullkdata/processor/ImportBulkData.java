/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.watson.health.fhir.operation.bullkdata.processor;

import com.ibm.watson.health.fhir.config.FHIRRequestContext;
import com.ibm.watson.health.fhir.exception.FHIROperationException;
import com.ibm.watson.health.fhir.model.resource.Parameters;
import com.ibm.watson.health.fhir.rest.FHIRResourceHelpers;

/**
 * 
 * @link https://docs.google.com/presentation/d/14ZHmam9hwz6-SsCG1YqUIQnJ56bvSqEatebltgEVR6c/edit#slide=id.g594eb0910d_9_126
 * @author pbastide 
 *
 */
public interface ImportBulkData {
    
    /**
     * 
     * Pattern: POST [Base]/$import
     * 
     * @param parameters
     * @param ctx
     * @param resourceHelper
     * @return 
     * 
     * @throws FHIROperationException 
     */
    public Parameters importBase(Parameters parameters, FHIRRequestContext ctx, FHIRResourceHelpers resourceHelper) throws FHIROperationException;
    
}
