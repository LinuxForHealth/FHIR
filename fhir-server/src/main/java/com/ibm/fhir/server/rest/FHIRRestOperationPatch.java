/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.server.rest;

import com.ibm.fhir.model.patch.FHIRPatch;

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
    
    public FHIRRestOperationPatch(int entryIndex, String type, String id, FHIRPatch patch, String ifMatchValue,
        String searchQueryString, boolean skippableUpdate) {
        super(entryIndex);
        this.type = type;
        this.id = id;
        this.patch = patch;
        this.ifMatchValue = ifMatchValue;
        this.searchQueryString = searchQueryString;
        this.skippableUpdate = skippableUpdate;
    }
    
    @Override
    public <T> T accept(FHIRRestOperationVisitor<T> visitor) throws Exception {
        return visitor.doPatch(getEntryIndex(), type, id, patch, ifMatchValue, searchQueryString, skippableUpdate);
    }
}