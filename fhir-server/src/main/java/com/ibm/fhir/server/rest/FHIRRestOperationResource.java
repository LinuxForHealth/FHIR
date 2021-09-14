/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.server.rest;

import com.ibm.fhir.model.resource.Bundle.Entry;

/**
 * Base for resource-oriented {@link FHIRRestOperation} implementations which include a validationResponseEntry.
 */
public abstract class FHIRRestOperationResource extends FHIRRestOperationBase {
    
    private final Entry validationResponseEntry;
    
    public FHIRRestOperationResource(int entryIndex, Entry validationResponseEntry, String requestDescription, long initialTime) {
        super(entryIndex, requestDescription, initialTime);
        this.validationResponseEntry = validationResponseEntry;
    }

    /**
     * @return the validationResponseEntry
     */
    public Entry getValidationResponseEntry() {
        return validationResponseEntry;
    }
}