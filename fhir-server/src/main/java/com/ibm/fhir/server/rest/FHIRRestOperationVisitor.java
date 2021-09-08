/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.server.rest;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;

import com.ibm.fhir.model.patch.FHIRPatch;
import com.ibm.fhir.model.resource.Bundle.Entry;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.server.operation.spi.FHIROperationContext;

/**
 * Defines operations which can be performed on the persistence layer
 */
public interface FHIRRestOperationVisitor<T> {

    T doSearch(int entryIndex, String type, String compartment, String compartmentId,
        MultivaluedMap<String, String> queryParameters, String requestUri,
        Resource contextResource, boolean checkInteractionAllowed) throws Exception;
    
    T doVRead(int entryIndex, String type, String id, String versionId, MultivaluedMap<String, String> queryParameters) throws Exception;
    
    T doRead(int entryIndex, String type, String id, boolean throwExcOnNull, boolean includeDeleted,
        Resource contextResource, MultivaluedMap<String, String> queryParameters, boolean checkInteractionAllowed) throws Exception;
    
    T doHistory(int entryIndex, String type, String id, MultivaluedMap<String, String> queryParameters, String requestUri) throws Exception;
    
    T doCreate(int entryIndex, String type, Resource resource, String ifNoneExist, boolean doValidation, String localIdentifier) throws Exception;
    
    T doUpdate(int entryIndex, String type, String id, Resource newResource, String ifMatchValue,
        String searchQueryString, boolean skippableUpdate, boolean doValidation, String localIdentifier) throws Exception;
    
    T doPatch(int entryIndex, String type, String id, FHIRPatch patch, String ifMatchValue,
        String searchQueryString, boolean skippableUpdate) throws Exception;
    
    T doInvoke(int entryIndex, FHIROperationContext operationContext, String resourceTypeName,
            String logicalId, String versionId, String operationName,
            Resource resource, MultivaluedMap<String, String> queryParameters) throws Exception;
    
    T doDelete(int entryIndex, String type, String id, String searchQueryString) throws Exception;
    
    T validationResponse(int entryIndex, Entry validationResponseEntry) throws Exception;
    
    T issue(int entryIndex, Status status, Entry responseEntry) throws Exception;
}