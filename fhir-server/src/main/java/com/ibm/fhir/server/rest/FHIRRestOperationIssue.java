/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.server.rest;

import javax.ws.rs.core.Response.Status;

import com.ibm.fhir.model.resource.Bundle.Entry;
import com.ibm.fhir.server.operation.spi.FHIRRestOperationResponse;

/**
 * Captures the fact that an issue occurred while translating a bundle entry.
 * Record the issue so that it can be added to the result bundle later
 */
public class FHIRRestOperationIssue extends FHIRRestOperationBase {
    final Status status;
    
    // The Entry capturing the response. Can be null.
    final Entry responseEntry;
    
    public FHIRRestOperationIssue(int entryIndex, String requestDescription, long initialTime, Status status, Entry responseEntry) {
        super(entryIndex, requestDescription, initialTime);
        this.status = status;
        this.responseEntry = responseEntry;
    }
    
    @Override
    public FHIRRestOperationResponse accept(FHIRRestOperationVisitor visitor) throws Exception {
        return visitor.issue(getEntryIndex(), getRequestDescription(), getInitialTime(), status, responseEntry);
    }
}