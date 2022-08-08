/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.server.rest;


import org.linuxforhealth.fhir.model.patch.FHIRPatch;
import org.linuxforhealth.fhir.persistence.context.FHIRPersistenceEvent;
import org.linuxforhealth.fhir.server.spi.operation.FHIRRestOperationResponse;
import org.linuxforhealth.fhir.server.util.FHIRUrlParser;

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
     * @param type
     * @param id
     * @param patch
     * @param ifMatchValue
     * @param searchQueryString
     * @param skippableUpdate
     * @param localIdentifier
     */
    public FHIRRestInteractionPatch(int entryIndex, FHIRPersistenceEvent event, String requestDescription,
            FHIRUrlParser requestURL, String type, String id, FHIRPatch patch, String ifMatchValue,
            String searchQueryString, boolean skippableUpdate, String localIdentifier) {
        super(entryIndex, event, null, null, requestDescription, requestURL);
        this.type = type;
        this.id = id;
        this.patch = patch;
        this.ifMatchValue = ifMatchValue;
        this.searchQueryString = searchQueryString;
        this.skippableUpdate = skippableUpdate;
        this.localIdentifier = localIdentifier;
    }

    @Override
    public void process(FHIRRestInteractionVisitor visitor) throws Exception {
        FHIRRestOperationResponse result = visitor.doPatch(getEntryIndex(), getEvent(), getValidationResponseEntry(),
                getRequestDescription(), getRequestURL(), getAccumulatedTime(), type, id, getNewResource(),
                getPrevResource(), patch, ifMatchValue, searchQueryString, skippableUpdate, getWarnings(), localIdentifier, getOffloadResponse());

        // If the response includes a resource, update our copy so that we can pass to the
        // next visitor.
        if (result != null) {
            if (result.getResource() != null) {
                setNewResource(result.getResource());
            }

            if (result.getPrevResource() != null) {
                setPrevResource(result.getPrevResource());
            }

            if (result.getStorePayloadResponse() != null) {
                setOffloadResponse(result.getStorePayloadResponse());
            }
        }
    }
}