/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.server.rest;

import com.ibm.fhir.model.resource.Bundle.Entry;

/**
 * Captures the fact that a particular entry failed validation.
 */
public class FHIRRestOperationValidationResponse extends FHIRRestOperationBase {

    private final Entry validationResponse;
    
    public FHIRRestOperationValidationResponse(int entryIndex, Entry validationResponse) {
        super(entryIndex);
        this.validationResponse = validationResponse;
    }
    
    @Override
    public <T> T accept(FHIRRestOperationVisitor<T> visitor) throws Exception {
        return visitor.validationResponse(getEntryIndex(), validationResponse);
    }
}