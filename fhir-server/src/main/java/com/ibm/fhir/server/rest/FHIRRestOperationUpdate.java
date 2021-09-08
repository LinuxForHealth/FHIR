/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.server.rest;

import com.ibm.fhir.model.resource.Resource;

/**
 * Executes an update operation on the visitor
 */
public class FHIRRestOperationUpdate extends FHIRRestOperationBase {

    private final String type;
    private final String id;
    private final Resource newResource;
    private final String ifMatchValue;    
    private final String searchQueryString;
    private final boolean skippableUpdate;
    private final boolean doValidation;
    private final String localIdentifier;
    
    public FHIRRestOperationUpdate(int entryIndex, String type, String id, Resource newResource, String ifMatchValue,
        String searchQueryString, boolean skippableUpdate, boolean doValidation, String localIdentifier) {
        super(entryIndex);
        this.type = type;
        this.id = id;
        this.newResource = newResource;
        this.ifMatchValue = ifMatchValue;
        this.searchQueryString = searchQueryString;
        this.skippableUpdate = skippableUpdate;
        this.doValidation = doValidation;
        this.localIdentifier = localIdentifier;
    }
    
    @Override
    public <T> T accept(FHIRRestOperationVisitor<T> visitor) throws Exception {
        return visitor.doUpdate(getEntryIndex(), type, id, newResource, ifMatchValue, searchQueryString, skippableUpdate, doValidation, localIdentifier);
    }
}