/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.rest;

import javax.ws.rs.core.MultivaluedMap;

import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.server.util.FHIRUrlParser;

/**
 * Represents a FHIR REST READ interaction
 */
public class FHIRRestInteractionRead extends FHIRRestInteractionBase {

    private final String type;
    private final String id;
    private final boolean throwExcOnNull;
    private final boolean includeDeleted;
    private final Resource contextResource;
    private final MultivaluedMap<String, String> queryParameters;
    private final boolean checkInteractionAllowed;

    /**
     * Public constructor
     *
     * @param entryIndex
     * @param requestDescription
     * @param requestURL
     * @param initialTime
     * @param type
     * @param id
     * @param throwExcOnNull
     * @param includeDeleted
     * @param contextResource
     * @param queryParameters
     * @param checkInteractionAllowed
     */
    public FHIRRestInteractionRead(int entryIndex, String requestDescription, FHIRUrlParser requestURL,
            long initialTime, String type, String id, boolean throwExcOnNull, boolean includeDeleted,
            Resource contextResource, MultivaluedMap<String, String> queryParameters, boolean checkInteractionAllowed) {
        super(entryIndex, requestDescription, requestURL, initialTime);
        this.type = type;
        this.id = id;
        this.throwExcOnNull = throwExcOnNull;
        this.includeDeleted = includeDeleted;
        this.contextResource = contextResource;
        this.queryParameters = queryParameters;
        this.checkInteractionAllowed = checkInteractionAllowed;
    }

    @Override
    public void accept(FHIRRestInteractionVisitor visitor) throws Exception {
        visitor.doRead(getEntryIndex(), getRequestDescription(), getRequestURL(), getInitialTime(), type, id, throwExcOnNull, includeDeleted, contextResource, queryParameters, checkInteractionAllowed);
    }
}