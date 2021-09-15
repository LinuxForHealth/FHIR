/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.server.rest;

import javax.ws.rs.core.MultivaluedMap;

import com.ibm.fhir.model.resource.Bundle.Entry;
import com.ibm.fhir.server.operation.spi.FHIRRestOperationResponse;
import com.ibm.fhir.server.util.FHIRUrlParser;

/**
 * Executes a history operation on the visitor
 */
public class FHIRRestOperationHistory extends FHIRRestOperationBase {
    
    private final String type;
    private final String id;
    private final MultivaluedMap<String, String> queryParameters;
    private final String requestUri;
    
    public FHIRRestOperationHistory(int entryIndex, String requestDescription, FHIRUrlParser requestURL, long initialTime, String type, String id, MultivaluedMap<String, String> queryParameters, String requestUri) {
        super(entryIndex, requestDescription, requestURL, initialTime);
        this.type = type;
        this.id = id;
        this.queryParameters = queryParameters;
        this.requestUri = requestUri;
    }

    @Override
    public FHIRRestOperationResponse accept(FHIRRestOperationVisitor visitor) throws Exception {
        return visitor.doHistory(getEntryIndex(), getRequestDescription(), getRequestURL(), getInitialTime(), type, id, queryParameters, requestUri);
    }
}