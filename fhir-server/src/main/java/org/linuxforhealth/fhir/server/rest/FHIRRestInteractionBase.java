/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.server.rest;

import java.util.ArrayList;
import java.util.List;

import org.linuxforhealth.fhir.model.resource.OperationOutcome.Issue;
import org.linuxforhealth.fhir.server.util.FHIRUrlParser;

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

    // The amount of time accumulated processing this request
    private long accumulatedTime;

    // Any warnings collected when processing this entry
    private final List<Issue> warnings;

    /**
     * Protected constructor
     *
     * @param entryIndex
     * @param event
     * @param requestDescription
     * @param requestURL
     */
    protected FHIRRestInteractionBase(int entryIndex, String requestDescription, FHIRUrlParser requestURL) {
        this.entryIndex = entryIndex;
        this.requestDescription = requestDescription;
        this.requestURL = requestURL;
        this.warnings = new ArrayList<>();
        this.accumulatedTime = 0;
    }
    
    @Override
    public void accept(FHIRRestInteractionVisitor visitor) throws Exception {
        final long start = System.nanoTime();
        try {
            process(visitor);
        } finally {
            long elapsed = System.nanoTime() - start;
            this.accumulatedTime += elapsed;
        }
    }
    
    /**
     * Process this interaction
     * @param visitor
     * @throws Exception
     */
    protected abstract void process(FHIRRestInteractionVisitor visitor) throws Exception;
    
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
     * @return the accumulatedTime
     */
    public long getAccumulatedTime() {
        return accumulatedTime;
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