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
 * Executes a create operation on the visitor
 */
public class FHIRRestInteractionCreate extends FHIRRestInteractionResource {
    
    private final String type;
    private final String ifNoneExist;
    private final String localIdentifier;
    
    public FHIRRestInteractionCreate(int entryIndex, Entry validationResponseEntry, String requestDescription, FHIRUrlParser requestURL, long initialTime, String type, Resource resource, String ifNoneExist, String localIdentifier) {
        super(entryIndex, resource, validationResponseEntry, requestDescription, requestURL, initialTime);
        this.type = type;
        this.ifNoneExist = ifNoneExist;
        this.localIdentifier = localIdentifier;
    }

    @Override
    public FHIRRestOperationResponse accept(FHIRRestInteractionVisitor visitor) throws Exception {
        FHIRRestOperationResponse result = visitor.doCreate(getEntryIndex(), getWarnings(), getValidationResponseEntry(), getRequestDescription(), getRequestURL(), getInitialTime(), type, getResource(), ifNoneExist, localIdentifier);
        
        // update the resource so we can use it when called in the next processing phase
        if (result != null && result.getResource() != null) {
            setUpdatedResource(result.getResource());
        }
        
        return result;
    }
}