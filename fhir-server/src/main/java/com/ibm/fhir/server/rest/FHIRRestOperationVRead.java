/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.server.rest;

import javax.ws.rs.core.MultivaluedMap;

import com.ibm.fhir.model.resource.Bundle.Entry;
import com.ibm.fhir.server.operation.spi.FHIRRestOperationResponse;

/**
 * Executes a VREAD (version read) operation on the visitor
 */
public class FHIRRestOperationVRead extends FHIRRestOperationBase {

    private final String type;
    private final String id;
    private final String versionId;
    private final MultivaluedMap<String, String> queryParameters;
    
    public FHIRRestOperationVRead(int entryIndex, String requestDescription, long initialTime, String type, String id, String versionId, MultivaluedMap<String, String> queryParameters) {
        super(entryIndex, requestDescription, initialTime);
        this.type = type;
        this.id = id;
        this.versionId = versionId;
        this.queryParameters = queryParameters;
    }
    
    @Override
    public FHIRRestOperationResponse accept(FHIRRestOperationVisitor visitor) throws Exception {
        return visitor.doVRead(getEntryIndex(), getRequestDescription(), getInitialTime(), type, id, versionId, queryParameters);
    }
}