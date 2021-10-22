/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.rest;

import java.util.ArrayList;
import java.util.List;

import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.server.util.FHIRUrlParser;

/**
 * Base for {@link FHIRRestInteraction} implementations, providing
 * common functions
 */
public abstract class FHIRRestInteractionBase implements FHIRRestInteraction {

    // The index of the entry related to this operation in the original bundle
    private final int entryIndex;

    // Description of the request for logging
    private final String requestDescription;

    // The requestURL
    private final FHIRUrlParser requestURL;

    // The time we started processing this request
    private final long initialTime;

    // Any warnings collected when processing this entry
    private final List<Issue> warnings;

    /**
     * Protected constructor
     *
     * @param entryIndex
     * @param event
     * @param requestDescription
     * @param requestURL
     * @param initialTime
     */
    protected FHIRRestInteractionBase(int entryIndex, String requestDescription, FHIRUrlParser requestURL,
            long initialTime) {
        this.entryIndex = entryIndex;
        this.requestDescription = requestDescription;
        this.requestURL = requestURL;
        this.initialTime = initialTime;
        this.warnings = new ArrayList<>();
    }

    @Override
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

    /**
     * @return the requestURL
     */
    public FHIRUrlParser getRequestURL() {
        return requestURL;
    }

    /**
     * @return the warnings list
     */
    public List<Issue> getWarnings() {
        return this.warnings;
    }
}