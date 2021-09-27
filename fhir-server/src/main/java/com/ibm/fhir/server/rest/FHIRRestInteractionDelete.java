/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.server.rest;

import com.ibm.fhir.server.operation.spi.FHIRRestOperationResponse;
import com.ibm.fhir.server.util.FHIRUrlParser;

/**
 * Executes a delete operation on the visitor
 */
public class FHIRRestInteractionDelete extends FHIRRestInteractionBase {
    
    final String type;
    final String id;
    final String searchQueryString;
    
    public FHIRRestInteractionDelete(int entryIndex, String requestDescription, FHIRUrlParser requestURL, long initialTime, String type, String id, String searchQueryString) {
        super(entryIndex, requestDescription, requestURL, initialTime);
        this.type = type;
        this.id = id;
        this.searchQueryString = searchQueryString;
    }

    @Override
    public FHIRRestOperationResponse accept(FHIRRestInteractionVisitor visitor) throws Exception {
        return visitor.doDelete(getEntryIndex(), getRequestDescription(), getRequestURL(), getInitialTime(), type, id, searchQueryString);        
    }
}