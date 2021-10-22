/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.rest;

import javax.ws.rs.core.MultivaluedMap;

import com.ibm.fhir.server.util.FHIRUrlParser;

/**
 * Represents a FHIR REST HISTORY read interaction
 */
public class FHIRRestInteractionHistory extends FHIRRestInteractionBase {

    private final String type;
    private final String id;
    private final MultivaluedMap<String, String> queryParameters;
    private final String requestUri;

    /**
     * Public constructor
     *
     * @param entryIndex
     * @param requestDescription
     * @param requestURL
     * @param initialTime
     * @param type
     * @param id
     * @param queryParameters
     * @param requestUri
     */
    public FHIRRestInteractionHistory(int entryIndex, String requestDescription, FHIRUrlParser requestURL,
            long initialTime, String type, String id, MultivaluedMap<String, String> queryParameters, String requestUri) {
        super(entryIndex, requestDescription, requestURL, initialTime);
        this.type = type;
        this.id = id;
        this.queryParameters = queryParameters;
        this.requestUri = requestUri;
    }

    @Override
    public void accept(FHIRRestInteractionVisitor visitor) throws Exception {
        visitor.doHistory(getEntryIndex(), getRequestDescription(), getRequestURL(), getInitialTime(), type, id,
                queryParameters, requestUri);
    }
}