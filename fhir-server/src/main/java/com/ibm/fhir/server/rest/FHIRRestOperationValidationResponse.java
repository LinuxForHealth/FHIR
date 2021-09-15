/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.server.rest;

import com.ibm.fhir.model.resource.Bundle.Entry;
import com.ibm.fhir.server.operation.spi.FHIRRestOperationResponse;

/**
 * Captures the fact that a particular entry failed validation.
 */
public class FHIRRestOperationValidationResponse extends FHIRRestOperationResource {

    public FHIRRestOperationValidationResponse(int entryIndex, Entry validationResponseEntry) {
        super(entryIndex, validationResponseEntry, null, null, -1L);
    }
    
    @Override
    public FHIRRestOperationResponse accept(FHIRRestOperationVisitor visitor) throws Exception {
        return visitor.validationResponse(getEntryIndex(), getValidationResponseEntry());
    }
}