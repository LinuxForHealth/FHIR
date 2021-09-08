/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.server.rest;

import javax.ws.rs.core.MultivaluedMap;

/**
 * Executes a VREAD (version read) operation on the visitor
 */
public class FHIRRestOperationVRead extends FHIRRestOperationBase {

    private final String type;
    private final String id;
    private final String versionId;
    private final MultivaluedMap<String, String> queryParameters;
    
    public FHIRRestOperationVRead(int entryIndex, String type, String id, String versionId, MultivaluedMap<String, String> queryParameters) {
        super(entryIndex);
        this.type = type;
        this.id = id;
        this.versionId = versionId;
        this.queryParameters = queryParameters;
    }
    
    @Override
    public <T> T accept(FHIRRestOperationVisitor<T> visitor) throws Exception {
        return visitor.doVRead(getEntryIndex(), type, id, versionId, queryParameters);
    }
}