/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.server.rest;

import javax.ws.rs.core.Response.Status;

import org.linuxforhealth.fhir.model.resource.Bundle.Entry;

/**
 * Captures the fact that an issue occurred while translating a bundle entry.
 * Record the issue so that it can be added to the result bundle later
 */
public class FHIRRestInteractionIssue extends FHIRRestInteractionBase {
    final Status status;

    // The Entry capturing the response. Can be null.
    final Entry responseEntry;

    /**
     * Public constructor
     *
     * @param entryIndex
     * @param status
     * @param responseEntry
     */
    public FHIRRestInteractionIssue(int entryIndex, Status status, Entry responseEntry) {
        super(entryIndex, null, null);
        this.status = status;
        this.responseEntry = responseEntry;
    }

    @Override
    public void process(FHIRRestInteractionVisitor visitor) throws Exception {
        visitor.issue(getEntryIndex(), getRequestDescription(), getAccumulatedTime(), status, responseEntry);
    }
}