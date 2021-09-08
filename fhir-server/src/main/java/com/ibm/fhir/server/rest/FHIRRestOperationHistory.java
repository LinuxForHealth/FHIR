/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.server.rest;

import javax.ws.rs.core.MultivaluedMap;

/**
 * Executes a history operation on the visitor
 */
public class FHIRRestOperationHistory extends FHIRRestOperationBase {
    
    private final String type;
    private final String id;
    private final MultivaluedMap<String, String> queryParameters;
    private final String requestUri;
    
    public FHIRRestOperationHistory(int entryIndex, String type, String id, MultivaluedMap<String, String> queryParameters, String requestUri) {
        super(entryIndex);
        this.type = type;
        this.id = id;
        this.queryParameters = queryParameters;
        this.requestUri = requestUri;
    }

    @Override
    public <T> T accept(FHIRRestOperationVisitor<T> visitor) throws Exception {
        return visitor.doHistory(getEntryIndex(), type, id, queryParameters, requestUri);
    }
}