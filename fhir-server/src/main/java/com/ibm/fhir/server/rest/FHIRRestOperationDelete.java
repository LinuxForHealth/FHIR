/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.server.rest;

import com.ibm.fhir.server.operation.spi.FHIRRestOperationResponse;

/**
 * Executes a delete operation on the visitor
 */
public class FHIRRestOperationDelete extends FHIRRestOperationBase {
    
    final String type;
    final String id;
    final String searchQueryString;
    
    public FHIRRestOperationDelete(int entryIndex, String requestDescription, long initialTime, String type, String id, String searchQueryString) {
        super(entryIndex, requestDescription, initialTime);
        this.type = type;
        this.id = id;
        this.searchQueryString = searchQueryString;
    }

    @Override
    public FHIRRestOperationResponse accept(FHIRRestOperationVisitor visitor) throws Exception {
        return visitor.doDelete(getEntryIndex(), getRequestDescription(), getInitialTime(), type, id, searchQueryString);        
    }
}