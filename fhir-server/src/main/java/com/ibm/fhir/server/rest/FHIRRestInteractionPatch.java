/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.rest;


import com.ibm.fhir.model.patch.FHIRPatch;
import com.ibm.fhir.persistence.context.FHIRPersistenceEvent;
import com.ibm.fhir.server.spi.operation.FHIRRestOperationResponse;
import com.ibm.fhir.server.util.FHIRUrlParser;

/**
 * Represents a FHIR REST PATCH interaction
 */
public class FHIRRestInteractionPatch extends FHIRRestInteractionResource {

    private final String type;
    private final String id;
    private final FHIRPatch patch;
    private final String ifMatchValue;
    private final String searchQueryString;
    private final boolean skippableUpdate;
    private final String localIdentifier;

    /**
     * Public constructor
     *
     * @param entryIndex
     * @param event
     * @param requestDescription
     * @param requestURL
     * @param initialTime
     * @param type
     * @param id
     * @param patch
     * @param ifMatchValue
     * @param searchQueryString
     * @param skippableUpdate
     * @param localIdentifier
     */
    public FHIRRestInteractionPatch(int entryIndex, FHIRPersistenceEvent event, String requestDescription,
            FHIRUrlParser requestURL, long initialTime, String type, String id, FHIRPatch patch, String ifMatchValue,
            String searchQueryString, boolean skippableUpdate, String localIdentifier) {
        super(entryIndex, event, null, null, requestDescription, requestURL, initialTime);
        this.type = type;
        this.id = id;
        this.patch = patch;
        this.ifMatchValue = ifMatchValue;
        this.searchQueryString = searchQueryString;
        this.skippableUpdate = skippableUpdate;
        this.localIdentifier = localIdentifier;
    }

    @Override
    public void accept(FHIRRestInteractionVisitor visitor) throws Exception {
        FHIRRestOperationResponse result = visitor.doPatch(getEntryIndex(), getEvent(), getValidationResponseEntry(),
                getRequestDescription(), getRequestURL(), getInitialTime(), type, id, getNewResource(),
                getPrevResource(), patch, ifMatchValue, searchQueryString, skippableUpdate, getWarnings(), localIdentifier);

        // If the response includes a resource, update our copy so that we can pass to the
        // next visitor.
        if (result != null) {
            if (result.getResource() != null) {
                setNewResource(result.getResource());
            }

            if (result.getPrevResource() != null) {
                setPrevResource(result.getPrevResource());
            }
        }
    }
}