/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.server.rest;


import com.ibm.fhir.model.patch.FHIRPatch;
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
    private final String localIdentifier;
    
    public FHIRRestInteractionPatch(int entryIndex, String requestDescription, FHIRUrlParser requestURL, long initialTime, String type, String id, FHIRPatch patch, String ifMatchValue,
        String searchQueryString, boolean skippableUpdate, String localIdentifier) {
        super(entryIndex, null, null, requestDescription, requestURL, initialTime);
        this.type = type;
        this.id = id;
        this.patch = patch;
        this.ifMatchValue = ifMatchValue;
        this.searchQueryString = searchQueryString;
        this.skippableUpdate = skippableUpdate;
        this.localIdentifier = localIdentifier;
    }
    
    @Override
    public FHIRRestOperationResponse accept(FHIRRestInteractionVisitor visitor) throws Exception {
        FHIRRestOperationResponse result = visitor.doPatch(getEntryIndex(), getValidationResponseEntry(), getRequestDescription(), 
            getRequestURL(), getInitialTime(), type, id, getNewResource(), getPrevResource(), patch, ifMatchValue, searchQueryString, skippableUpdate, getWarnings(), localIdentifier);

        // If the response includes a resource, update our copy so that we can pass to the
        // next visitor.
        if (result != null) {
            if (result.getResource() != null) {
                setNewResource(result.getResource());
            }
            
            if (result.getPrevResource() != null) {
                setPrevResource(result.getPrevResource());
            }
        }

        return result;
    }
}