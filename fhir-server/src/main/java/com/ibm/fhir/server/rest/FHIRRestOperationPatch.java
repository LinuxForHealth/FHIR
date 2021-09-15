/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.server.rest;

import com.ibm.fhir.model.patch.FHIRPatch;
import com.ibm.fhir.model.resource.Bundle.Entry;
import com.ibm.fhir.server.operation.spi.FHIRRestOperationResponse;
import com.ibm.fhir.server.util.FHIRUrlParser;

/**
 * Executes a patch operation on the visitor
 */
public class FHIRRestOperationPatch extends FHIRRestOperationBase {

    private final String type;
    private final String id;
    private final FHIRPatch patch;
    private final String ifMatchValue;
    private final String searchQueryString;
    private final boolean skippableUpdate;
    
    public FHIRRestOperationPatch(int entryIndex, String requestDescription, FHIRUrlParser requestURL, long initialTime, String type, String id, FHIRPatch patch, String ifMatchValue,
        String searchQueryString, boolean skippableUpdate) {
        super(entryIndex, requestDescription, requestURL, initialTime);
        this.type = type;
        this.id = id;
        this.patch = patch;
        this.ifMatchValue = ifMatchValue;
        this.searchQueryString = searchQueryString;
        this.skippableUpdate = skippableUpdate;
    }
    
    @Override
    public FHIRRestOperationResponse accept(FHIRRestOperationVisitor visitor) throws Exception {
        return visitor.doPatch(getEntryIndex(), getRequestDescription(), getRequestURL(), getInitialTime(), type, id, patch, ifMatchValue, searchQueryString, skippableUpdate);
    }
}