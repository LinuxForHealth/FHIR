/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.server.rest;

import org.linuxforhealth.fhir.model.resource.Bundle.Entry;

/**
 * Captures the fact that a particular entry failed validation.
 */
public class FHIRRestInteractionValidationResponse extends FHIRRestInteractionResource {

    /**
     * Public constructor
     * 
     * @param entryIndex
     * @param validationResponseEntry
     * @param requestDescription
     */
    public FHIRRestInteractionValidationResponse(int entryIndex, Entry validationResponseEntry, String requestDescription) {
        super(entryIndex, null, null, validationResponseEntry, requestDescription, null);
    }

    @Override
    public void process(FHIRRestInteractionVisitor visitor) throws Exception {
        visitor.validationResponse(getEntryIndex(), getValidationResponseEntry(), getRequestDescription(), getAccumulatedTime());
    }
}