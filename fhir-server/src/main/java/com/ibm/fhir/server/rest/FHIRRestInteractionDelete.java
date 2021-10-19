/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.rest;

import com.ibm.fhir.server.util.FHIRUrlParser;

/**
 * Represents a FHIR REST DELETE interaction
 */
public class FHIRRestInteractionDelete extends FHIRRestInteractionBase {

    final String type;
    final String id;
    final String searchQueryString;

    /**
     * Public constructor
     *
     * @param entryIndex
     * @param requestDescription
     * @param requestURL
     * @param initialTime
     * @param type
     * @param id
     * @param searchQueryString
     */
    public FHIRRestInteractionDelete(int entryIndex, String requestDescription, FHIRUrlParser requestURL,
            long initialTime, String type, String id, String searchQueryString) {
        super(entryIndex, requestDescription, requestURL, initialTime);
        this.type = type;
        this.id = id;
        this.searchQueryString = searchQueryString;
    }

    @Override
    public void accept(FHIRRestInteractionVisitor visitor) throws Exception {
        visitor.doDelete(getEntryIndex(), getRequestDescription(), getRequestURL(), getInitialTime(), type, id, searchQueryString);
    }
}