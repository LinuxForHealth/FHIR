/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.server.rest;

import javax.ws.rs.core.MultivaluedMap;

import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.Bundle.Entry;
import com.ibm.fhir.server.operation.spi.FHIRRestOperationResponse;

/**
 * Executes a read operation on the visitor
 */
public class FHIRRestOperationRead extends FHIRRestOperationBase {

    private final String type;
    private final String id;
    private final boolean throwExcOnNull;
    private final boolean includeDeleted;
    private final Resource contextResource;
    private final MultivaluedMap<String, String> queryParameters;
    private final boolean checkInteractionAllowed;
    
    public FHIRRestOperationRead(int entryIndex, String requestDescription, long initialTime, String type, String id, boolean throwExcOnNull, boolean includeDeleted,
        Resource contextResource, MultivaluedMap<String, String> queryParameters, boolean checkInteractionAllowed) {
        super(entryIndex, requestDescription, initialTime);
        this.type = type;
        this.id = id;
        this.throwExcOnNull = throwExcOnNull;
        this.includeDeleted = includeDeleted;
        this.contextResource = contextResource;
        this.queryParameters = queryParameters;
        this.checkInteractionAllowed = checkInteractionAllowed;
    }
    
    @Override
    public FHIRRestOperationResponse accept(FHIRRestOperationVisitor visitor) throws Exception {
        return visitor.doRead(getEntryIndex(), getRequestDescription(), getInitialTime(), type, id, throwExcOnNull, includeDeleted, contextResource, queryParameters, checkInteractionAllowed);
    }
}