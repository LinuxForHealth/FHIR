/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.rest;

import javax.ws.rs.core.MultivaluedMap;

import com.ibm.fhir.server.util.FHIRUrlParser;

/**
 * Represents a FHIR REST READ interaction
 */
public class FHIRRestInteractionRead extends FHIRRestInteractionBase {

    private final String type;
    private final String id;
    private final boolean throwExcOnNull;
    private final MultivaluedMap<String, String> queryParameters;
    private final boolean checkInteractionAllowed;

    /**
     * Public constructor
     *
     * @param entryIndex
     * @param requestDescription
     * @param requestURL
     * @param type
     * @param id
     * @param throwExcOnNull
     * @param queryParameters
     * @param checkInteractionAllowed
     */
    public FHIRRestInteractionRead(int entryIndex, String requestDescription, FHIRUrlParser requestURL,
            String type, String id, boolean throwExcOnNull,
            MultivaluedMap<String, String> queryParameters, boolean checkInteractionAllowed) {
        super(entryIndex, requestDescription, requestURL);
        this.type = type;
        this.id = id;
        this.throwExcOnNull = throwExcOnNull;
        this.queryParameters = queryParameters;
        this.checkInteractionAllowed = checkInteractionAllowed;
    }

    @Override
    public void process(FHIRRestInteractionVisitor visitor) throws Exception {
        visitor.doRead(getEntryIndex(), getRequestDescription(), getRequestURL(), getAccumulatedTime(),
                type, id, throwExcOnNull, queryParameters, checkInteractionAllowed);
    }
}