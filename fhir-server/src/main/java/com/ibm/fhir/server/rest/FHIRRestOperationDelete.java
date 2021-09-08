/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.server.rest;


/**
 * Executes a delete operation on the visitor
 */
public class FHIRRestOperationDelete extends FHIRRestOperationBase {
    
    final String type;
    final String id;
    final String searchQueryString;
    
    public FHIRRestOperationDelete(int entryIndex, String type, String id, String searchQueryString) {
        super(entryIndex);
        this.type = type;
        this.id = id;
        this.searchQueryString = searchQueryString;
    }

    @Override
    public <T> T accept(FHIRRestOperationVisitor<T> visitor) throws Exception {
        return visitor.doDelete(getEntryIndex(), type, id, searchQueryString);        
    }
}