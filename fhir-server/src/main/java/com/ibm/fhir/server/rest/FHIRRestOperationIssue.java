/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.server.rest;

import javax.ws.rs.core.Response.Status;

import com.ibm.fhir.model.resource.Bundle.Entry;

/**
 * Captures the fact that an issue occurred while translating a bundle entry.
 * Record the issue so that it can be added to the result bundle later
 */
public class FHIRRestOperationIssue extends FHIRRestOperationBase {
    final Status status;
    
    // The Entry capturing the response. Can be null.
    final Entry responseEntry;
    
    public FHIRRestOperationIssue(int entryIndex, Status status, Entry responseEntry) {
        super(entryIndex);
        this.status = status;
        this.responseEntry = responseEntry;
    }
    
    @Override
    public <T> T accept(FHIRRestOperationVisitor<T> visitor) throws Exception {
        return visitor.issue(getEntryIndex(), status, responseEntry);
    }
}