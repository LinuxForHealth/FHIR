/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.server.rest;

import com.ibm.fhir.model.resource.Resource;

/**
 * Executes a create operation on the visitor
 */
public class FHIRRestOperationCreate extends FHIRRestOperationBase {
    
    private final String type;
    private final Resource resource;
    private final String ifNoneExist;
    private final boolean doValidation;
    private final String localIdentifier;
    
    public FHIRRestOperationCreate(int entryIndex, String type, Resource resource, String ifNoneExist, boolean doValidation, String localIdentifier) {
        super(entryIndex);
        this.type = type;
        this.resource = resource;
        this.ifNoneExist = ifNoneExist;
        this.doValidation = doValidation;
        this.localIdentifier = localIdentifier;
    }

    @Override
    public <T> T accept(FHIRRestOperationVisitor<T> visitor) throws Exception {
        return visitor.doCreate(getEntryIndex(), type, resource, ifNoneExist, doValidation, localIdentifier);
    }
}