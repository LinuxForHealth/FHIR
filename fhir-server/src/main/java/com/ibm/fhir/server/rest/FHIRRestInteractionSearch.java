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
 * Represents a FHIR REST SEARCH interaction
 */
public class FHIRRestInteractionSearch extends FHIRRestInteractionBase {

    private final String type;
    private final String compartment;
    private final String compartmentId;
    private final MultivaluedMap<String, String> queryParameters;
    private final String requestUri;
    private final Resource contextResource;
    private final boolean checkInteractionAllowed;

    /**
     * Public constructor
     *
     * @param entryIndex
     * @param requestDescription
     * @param requestURL
     * @param initialTime
     * @param type
     * @param compartment
     * @param compartmentId
     * @param queryParameters
     * @param requestUri
     * @param contextResource
     * @param checkInteractionAllowed
     */
    public FHIRRestInteractionSearch(int entryIndex, String requestDescription, FHIRUrlParser requestURL,
            long initialTime, String type, String compartment, String compartmentId,
            MultivaluedMap<String, String> queryParameters, String requestUri, Resource contextResource,
            boolean checkInteractionAllowed) {
        super(entryIndex, requestDescription, requestURL, initialTime);
        this.type = type;
        this.compartment = compartment;
        this.compartmentId = compartmentId;
        this.queryParameters = queryParameters;
        this.requestUri = requestUri;
        this.contextResource = contextResource;
        this.checkInteractionAllowed = checkInteractionAllowed;
    }

    @Override
    public void accept(FHIRRestInteractionVisitor visitor) throws Exception {
        visitor.doSearch(getEntryIndex(), getRequestDescription(), getRequestURL(), getInitialTime(), type, compartment, compartmentId, queryParameters, requestUri, contextResource, checkInteractionAllowed);
    }
}