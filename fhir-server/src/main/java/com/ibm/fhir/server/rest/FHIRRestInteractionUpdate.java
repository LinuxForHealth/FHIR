/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.server.rest;

import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.Bundle.Entry;
import com.ibm.fhir.server.operation.spi.FHIRRestOperationResponse;
import com.ibm.fhir.server.util.FHIRUrlParser;

/**
 * Executes an update operation on the visitor
 */
public class FHIRRestInteractionUpdate extends FHIRRestInteractionResource {

    private final String type;
    private final String id;
    private final String ifMatchValue;    
    private final String searchQueryString;
    private final boolean skippableUpdate;
    private final String localIdentifier;
    
    // The deleted flag status when we read the current resource value
    private boolean deleted;
    
    public FHIRRestInteractionUpdate(int entryIndex, Entry validationResponseEntry, String requestDescription, FHIRUrlParser requestURL, 
        long initialTime, String type, String id, Resource newResource, String ifMatchValue,
        String searchQueryString, boolean skippableUpdate, String localIdentifier) {
        super(entryIndex, newResource, validationResponseEntry, requestDescription, requestURL, initialTime);
        this.type = type;
        this.id = id;
        this.ifMatchValue = ifMatchValue;
        this.searchQueryString = searchQueryString;
        this.skippableUpdate = skippableUpdate;
        this.localIdentifier = localIdentifier;
    }
    
    @Override
    public FHIRRestOperationResponse accept(FHIRRestInteractionVisitor visitor) throws Exception {
        
        FHIRRestOperationResponse result = visitor.doUpdate(getEntryIndex(), getValidationResponseEntry(), getRequestDescription(), getRequestURL(), 
            getInitialTime(), type, id, getNewResource(), getPrevResource(), ifMatchValue, searchQueryString, skippableUpdate, localIdentifier,
            getWarnings(), deleted);
        
        // update the resource so we can use it when called in the next processing phase
        if (result != null) {
            if (result.getResource() != null) {
                setNewResource(result.getResource());
                this.deleted = result.isDeleted();
            }
            
            if (result.getPrevResource() != null) {
                setPrevResource(result.getPrevResource());
            }
        }
        
        return result;
    }
}