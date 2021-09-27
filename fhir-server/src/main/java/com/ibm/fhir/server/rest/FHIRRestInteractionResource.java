/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.server.rest;

import com.ibm.fhir.model.resource.Bundle.Entry;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.server.util.FHIRUrlParser;

/**
 * Base for resource-oriented {@link FHIRRestInteraction} implementations which include a validationResponseEntry.
 */
public abstract class FHIRRestInteractionResource extends FHIRRestInteractionBase {
    
    private final Entry validationResponseEntry;
    
    // The initial resource
    private final Resource originalResource;
    
    // The previous resource (e.g. if read from the database
    private Resource prevResource;
    
    // The resource after it has been updated
    private Resource updatedResource;
    
    public FHIRRestInteractionResource(int entryIndex, Resource originalResource, Entry validationResponseEntry, String requestDescription, FHIRUrlParser requestURL, long initialTime) {
        super(entryIndex, requestDescription, requestURL, initialTime);
        this.originalResource = originalResource;
        this.validationResponseEntry = validationResponseEntry;
    }

    /**
     * @return the validationResponseEntry
     */
    public Entry getValidationResponseEntry() {
        return validationResponseEntry;
    }

    /**
     * @return the originalResource
     */
    public Resource getOriginalResource() {
        return originalResource;
    }
    
    /**
     * Setter for updatedResource
     * @param resource
     */
    public void setUpdatedResource(Resource resource) {
        this.updatedResource = resource;
    }

    /**
     * Get the updated resource, or if null, the original resource.
     * @return the most recent instance of the resource
     */
    public Resource getResource() {
        return updatedResource != null ? updatedResource : originalResource;
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
}