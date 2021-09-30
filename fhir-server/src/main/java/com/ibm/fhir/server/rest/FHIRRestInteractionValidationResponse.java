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
public class FHIRRestInteractionValidationResponse extends FHIRRestInteractionResource {

    /**
     * Public constructor
     * @param entryIndex
     * @param validationResponseEntry
     */
    public FHIRRestInteractionValidationResponse(int entryIndex, Entry validationResponseEntry) {
        super(entryIndex, null, validationResponseEntry, null, null, -1L);
    }
    
    @Override
    public void accept(FHIRRestInteractionVisitor visitor) throws Exception {
        visitor.validationResponse(getEntryIndex(), getValidationResponseEntry());
    }
}