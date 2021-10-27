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
 * Represents a FHIR REST UPDATE interaction
 */
public class FHIRRestInteractionUpdate extends FHIRRestInteractionResource {

    private final String type;
    private final String id;
    private final String ifMatchValue;
    private final String searchQueryString;
    private final boolean skippableUpdate;
    private final String localIdentifier;
    private final Integer ifNoneMatch;

    // The deleted flag status when we read the current resource value
    private boolean deleted;

    /**
     * Public constructor
     *
     * @param entryIndex
     * @param event
     * @param validationResponseEntry
     * @param requestDescription
     * @param requestURL
     * @param initialTime
     * @param type
     * @param id
     * @param newResource
     * @param ifMatchValue
     * @param searchQueryString
     * @param skippableUpdate
     * @param localIdentifier
     * @param ifNoneMatch
     */
    public FHIRRestInteractionUpdate(int entryIndex, FHIRPersistenceEvent event, Entry validationResponseEntry,
            String requestDescription, FHIRUrlParser requestURL, long initialTime, String type, String id,
            Resource newResource, String ifMatchValue, String searchQueryString, boolean skippableUpdate,
            String localIdentifier, Integer ifNoneMatch) {
        super(entryIndex, event, newResource, validationResponseEntry, requestDescription, requestURL, initialTime);
        this.type = type;
        this.id = id;
        this.ifMatchValue = ifMatchValue;
        this.searchQueryString = searchQueryString;
        this.skippableUpdate = skippableUpdate;
        this.localIdentifier = localIdentifier;
        this.ifNoneMatch = ifNoneMatch;
    }

    @Override
    public void accept(FHIRRestInteractionVisitor visitor) throws Exception {

        FHIRRestOperationResponse result = visitor.doUpdate(getEntryIndex(), getEvent(), getValidationResponseEntry(),
                getRequestDescription(), getRequestURL(), getInitialTime(), type, id, getNewResource(),
                getPrevResource(), ifMatchValue, searchQueryString, skippableUpdate, localIdentifier, getWarnings(), deleted, ifNoneMatch);

        // update the resource so we can use it when called in the next processing phase
        if (result != null) {
            if (result.getResource() != null) {
                setNewResource(result.getResource());
            }

            if (result.getPrevResource() != null) {
                setPrevResource(result.getPrevResource());
            }
            
            // Record the deletion status so we can return the correct response when undeleting
            this.deleted = result.isDeleted();
        }
    }
}