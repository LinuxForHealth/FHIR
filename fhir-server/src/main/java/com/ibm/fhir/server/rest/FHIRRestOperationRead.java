/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.server.rest;

import javax.ws.rs.core.MultivaluedMap;

import com.ibm.fhir.model.resource.Resource;

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
    
    public FHIRRestOperationRead(int entryIndex, String type, String id, boolean throwExcOnNull, boolean includeDeleted,
        Resource contextResource, MultivaluedMap<String, String> queryParameters, boolean checkInteractionAllowed) {
        super(entryIndex);
        this.type = type;
        this.id = id;
        this.throwExcOnNull = throwExcOnNull;
        this.includeDeleted = includeDeleted;
        this.contextResource = contextResource;
        this.queryParameters = queryParameters;
        this.checkInteractionAllowed = checkInteractionAllowed;
    }
    
    @Override
    public <T> T accept(FHIRRestOperationVisitor<T> visitor) throws Exception {
        return visitor.doRead(getEntryIndex(), type, id, throwExcOnNull, includeDeleted, contextResource, queryParameters, checkInteractionAllowed);
    }
}