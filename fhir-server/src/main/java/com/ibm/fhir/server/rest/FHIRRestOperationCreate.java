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
public class FHIRRestOperationCreate extends FHIRRestOperationResource {
    
    private final String type;
    private final String ifNoneExist;
    private final String localIdentifier;
    
    // Resource can be updated by a visitor
    private Resource resource;
    
    // Logical id can be updated by a visitor
    private String logicalId;
    
    public FHIRRestOperationCreate(int entryIndex, Entry validationResponseEntry, String requestDescription, FHIRUrlParser requestURL, long initialTime, String type, Resource resource, String ifNoneExist, String localIdentifier) {
        super(entryIndex, validationResponseEntry, requestDescription, requestURL, initialTime);
        this.type = type;
        this.resource = resource;
        this.ifNoneExist = ifNoneExist;
        this.localIdentifier = localIdentifier;
    }

    @Override
    public FHIRRestOperationResponse accept(FHIRRestOperationVisitor visitor) throws Exception {
        FHIRRestOperationResponse result = visitor.doCreate(getEntryIndex(), getValidationResponseEntry(), getRequestDescription(), getRequestURL(), getInitialTime(), type, resource, ifNoneExist, localIdentifier, logicalId);
        
        // update the resource so we can use it the next time we're called
        if (result != null) {
            this.resource = result.getResource();
            this.logicalId = result.getResourceId();
        }
        
        return result;
    }
}