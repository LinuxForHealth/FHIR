/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.server.rest;

import java.util.List;

import com.ibm.fhir.model.patch.FHIRPatch;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.Bundle.Entry;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.server.operation.spi.FHIRRestOperationResponse;
import com.ibm.fhir.server.util.FHIRUrlParser;

/**
 * Executes a patch operation on the visitor
 */
public class FHIRRestInteractionPatch extends FHIRRestInteractionResource {

    private final String type;
    private final String id;
    private final FHIRPatch patch;
    private final String ifMatchValue;
    private final String searchQueryString;
    private final boolean skippableUpdate;
    
    public FHIRRestInteractionPatch(int entryIndex, String requestDescription, FHIRUrlParser requestURL, long initialTime, String type, String id, FHIRPatch patch, String ifMatchValue,
        String searchQueryString, boolean skippableUpdate) {
        super(entryIndex, null, null, requestDescription, requestURL, initialTime);
        this.type = type;
        this.id = id;
        this.patch = patch;
        this.ifMatchValue = ifMatchValue;
        this.searchQueryString = searchQueryString;
        this.skippableUpdate = skippableUpdate;
    }
    
    @Override
    public FHIRRestOperationResponse accept(FHIRRestInteractionVisitor visitor) throws Exception {
        FHIRRestOperationResponse response = visitor.doPatch(getEntryIndex(), getValidationResponseEntry(), getRequestDescription(), getRequestURL(), getInitialTime(), 
            type, id, getResource(), getOriginalResource(), patch, ifMatchValue, searchQueryString, skippableUpdate, getWarnings());

        // If the response includes a resource, update our copy so that we can pass to the
        // next visitor.
        if (response != null && response.getResource() != null) {
            setUpdatedResource(response.getResource());
        }
        
        return response;
    }
}