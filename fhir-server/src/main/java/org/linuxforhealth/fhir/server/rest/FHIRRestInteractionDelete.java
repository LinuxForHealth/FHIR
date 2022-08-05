/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.server.rest;

import org.linuxforhealth.fhir.server.util.FHIRUrlParser;

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
     * @param type
     * @param id
     * @param searchQueryString
     */
    public FHIRRestInteractionDelete(int entryIndex, String requestDescription, FHIRUrlParser requestURL,
            String type, String id, String searchQueryString) {
        super(entryIndex, requestDescription, requestURL);
        this.type = type;
        this.id = id;
        this.searchQueryString = searchQueryString;
    }

    @Override
    public void process(FHIRRestInteractionVisitor visitor) throws Exception {
        visitor.doDelete(getEntryIndex(), getRequestDescription(), getRequestURL(), getAccumulatedTime(), type, id, searchQueryString);
    }
}