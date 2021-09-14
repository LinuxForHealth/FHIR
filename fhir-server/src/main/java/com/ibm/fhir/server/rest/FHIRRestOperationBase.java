/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.server.rest;

/**
 * Base for {@link FHIRRestOperation} implementations, providing
 * common functions
 */
public abstract class FHIRRestOperationBase implements FHIRRestOperation {
    
    // The index of the entry related to this operation in the original bundle
    private final int entryIndex;
    
    // Description of the request for logging
    private final String requestDescription;

    // The time we started processing this request
    private final long initialTime;
    
    /**
     * Protected constructor
     * @param entryIndex
     * @param requestDescription
     * @param initialTime
     */
    protected FHIRRestOperationBase(int entryIndex, String requestDescription, long initialTime) {
        this.entryIndex = entryIndex;
        this.requestDescription = requestDescription;
        this.initialTime = initialTime;
    }

    /**
     * @return the entryIndex
     */
    public int getEntryIndex() {
        return entryIndex;
    }

    /**
     * @return the requestDescription
     */
    public String getRequestDescription() {
        return requestDescription;
    }

    /**
     * @return the initialTime
     */
    public long getInitialTime() {
        return initialTime;
    }
}