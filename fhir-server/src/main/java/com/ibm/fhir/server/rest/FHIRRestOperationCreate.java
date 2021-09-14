/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.server.rest;

import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.Bundle.Entry;
import com.ibm.fhir.server.operation.spi.FHIRRestOperationResponse;

/**
 * Executes a create operation on the visitor
 */
public class FHIRRestOperationCreate extends FHIRRestOperationResource {
    
    private final String type;
    private final String ifNoneExist;
    private final boolean doValidation;
    private final String localIdentifier;
    
    // Resource can be updated by a visitor
    private Resource resource;
    
    // Logical id can be updated by a visitor
    private String logicalId;
    
    public FHIRRestOperationCreate(int entryIndex, Entry validationResponseEntry, String requestDescription, long initialTime, String type, Resource resource, String ifNoneExist, boolean doValidation, String localIdentifier) {
        super(entryIndex, validationResponseEntry, requestDescription, initialTime);
        this.type = type;
        this.resource = resource;
        this.ifNoneExist = ifNoneExist;
        this.doValidation = doValidation;
        this.localIdentifier = localIdentifier;
    }

    @Override
    public FHIRRestOperationResponse accept(FHIRRestOperationVisitor visitor) throws Exception {
        FHIRRestOperationResponse result = visitor.doCreate(getEntryIndex(), getValidationResponseEntry(), getRequestDescription(), getInitialTime(), type, resource, ifNoneExist, doValidation, localIdentifier, logicalId);
        
        // update the resource so we can use it the next time we're called
        if (result != null) {
            this.resource = result.getResource();
            this.logicalId = result.getResourceId();
        }
        
        return result;
    }
}