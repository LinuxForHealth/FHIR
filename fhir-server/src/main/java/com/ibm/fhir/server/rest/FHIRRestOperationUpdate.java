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
public class FHIRRestOperationUpdate extends FHIRRestOperationResource {

    private final String type;
    private final String id;
    private final Resource newResource;
    private final String ifMatchValue;    
    private final String searchQueryString;
    private final boolean skippableUpdate;
    private final String localIdentifier;
    
    public FHIRRestOperationUpdate(int entryIndex, Entry validationResponseEntry, String requestDescription, FHIRUrlParser requestURL, long initialTime, String type, String id, Resource newResource, String ifMatchValue,
        String searchQueryString, boolean skippableUpdate, String localIdentifier) {
        super(entryIndex, validationResponseEntry, requestDescription, requestURL, initialTime);
        this.type = type;
        this.id = id;
        this.newResource = newResource;
        this.ifMatchValue = ifMatchValue;
        this.searchQueryString = searchQueryString;
        this.skippableUpdate = skippableUpdate;
        this.localIdentifier = localIdentifier;
    }
    
    @Override
    public FHIRRestOperationResponse accept(FHIRRestOperationVisitor visitor) throws Exception {
        return visitor.doUpdate(getEntryIndex(), getValidationResponseEntry(), getRequestDescription(), getRequestURL(), getInitialTime(), type, id, newResource, ifMatchValue, searchQueryString, skippableUpdate, localIdentifier);
    }
}