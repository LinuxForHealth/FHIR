/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.rest;

import com.ibm.fhir.model.resource.Bundle.Entry;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.persistence.context.FHIRPersistenceEvent;
import com.ibm.fhir.server.spi.operation.FHIRRestOperationResponse;
import com.ibm.fhir.server.util.FHIRUrlParser;

/**
 * Represents a FHIR REST CREATE interaction
 */
public class FHIRRestInteractionCreate extends FHIRRestInteractionResource {

    private final String type;
    private final String ifNoneExist;
    private final String localIdentifier;

    /**
     * Public constructor
     *
     * @param entryIndex
     * @param validationResponseEntry
     * @param requestDescription
     * @param requestURL
     * @param initialTime
     * @param type
     * @param resource
     * @param ifNoneExist
     * @param localIdentifier
     */
    public FHIRRestInteractionCreate(int entryIndex, FHIRPersistenceEvent event, Entry validationResponseEntry,
            String requestDescription, FHIRUrlParser requestURL, long initialTime, String type, Resource resource,
            String ifNoneExist, String localIdentifier) {
        super(entryIndex, event, resource, validationResponseEntry, requestDescription, requestURL, initialTime);
        this.type = type;
        this.ifNoneExist = ifNoneExist;
        this.localIdentifier = localIdentifier;
    }

    @Override
    public void accept(FHIRRestInteractionVisitor visitor) throws Exception {
        FHIRRestOperationResponse result = visitor.doCreate(getEntryIndex(), getEvent(), getWarnings(),
                getValidationResponseEntry(), getRequestDescription(), getRequestURL(), getInitialTime(), type,
                getNewResource(), ifNoneExist, localIdentifier);

        // update the resource so we can use it when called in the next processing phase
        if (result != null && result.getResource() != null) {
            setNewResource(result.getResource());
        }
    }
}