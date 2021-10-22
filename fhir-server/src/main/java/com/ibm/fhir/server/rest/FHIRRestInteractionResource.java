/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.rest;

import com.ibm.fhir.model.resource.Bundle.Entry;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.persistence.context.FHIRPersistenceEvent;
import com.ibm.fhir.server.util.FHIRUrlParser;

/**
 * Base for resource-oriented {@link FHIRRestInteraction} implementations which include a validationResponseEntry.
 */
public abstract class FHIRRestInteractionResource extends FHIRRestInteractionBase {

    private final Entry validationResponseEntry;

    // The event dispatched at various points while processing this interaction
    private final FHIRPersistenceEvent event;

    // The new resource
    private Resource newResource;

    // The previous resource (e.g. if read from the database
    private Resource prevResource;

    /**
     * Protected constructor
     *
     * @param entryIndex
     * @param event
     * @param newResource
     * @param validationResponseEntry
     * @param requestDescription
     * @param requestURL
     * @param initialTime
     */
    protected FHIRRestInteractionResource(int entryIndex, FHIRPersistenceEvent event, Resource newResource,
            Entry validationResponseEntry, String requestDescription, FHIRUrlParser requestURL, long initialTime) {
        super(entryIndex, requestDescription, requestURL, initialTime);
        this.event = event;
        this.newResource = newResource;
        this.validationResponseEntry = validationResponseEntry;
    }

    /**
     * @return the validationResponseEntry
     */
    public Entry getValidationResponseEntry() {
        return validationResponseEntry;
    }

    /**
     * Setter for updatedResource
     * @param resource
     */
    public void setNewResource(Resource resource) {
        this.newResource = resource;
    }

    /**
     * Get the updated resource, or if null, the original resource.
     * @return the most recent instance of the resource
     */
    public Resource getNewResource() {
        return newResource;
    }

    /**
     * Setter for prevResource
     * @param prevResource
     */
    public void setPrevResource(Resource prevResource) {
        this.prevResource = prevResource;
    }

    /**
     * Get the previous resource
     * @return
     */
    public Resource getPrevResource() {
        return this.prevResource;
    }

    /**
     * @return the event
     */
    public FHIRPersistenceEvent getEvent() {
        return event;
    }
}