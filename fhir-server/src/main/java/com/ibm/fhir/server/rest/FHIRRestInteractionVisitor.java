/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.rest;

import java.util.List;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;

import com.ibm.fhir.model.patch.FHIRPatch;
import com.ibm.fhir.model.resource.Bundle.Entry;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.persistence.context.FHIRPersistenceEvent;
import com.ibm.fhir.persistence.payload.PayloadPersistenceResponse;
import com.ibm.fhir.server.spi.operation.FHIROperationContext;
import com.ibm.fhir.server.spi.operation.FHIRRestOperationResponse;
import com.ibm.fhir.server.util.FHIRUrlParser;

/**
 * Defines operations which can be performed on the persistence layer
 */
public interface FHIRRestInteractionVisitor {

    /**
     * Performs heavy lifting associated with a 'search' operation.
     *
     * @param entryIndex
     * @param requestDescription
     * @param requestURL
     * @param accumulatedTime
     * @param type
     *            the resource type associated with the search
     * @param compartment
     *            the compartment type to search in, or null if not a compartment search
     * @param compartmentId
     *            the specific compartment to search in, or null if not a compartment search
     * @param queryParameters
     *            a Map containing the query parameters from the request URL
     * @param requestUri
     *            the request URI
     * @param contextResource
     *            the resource context
     * @param checkIfInteractionAllowed
     *            if true, check that the search interaction is permitted
     * @return a FHIRRestOperationResponse containing the search result bundle
     * @throws Exception
     */
    FHIRRestOperationResponse doSearch(int entryIndex, String requestDescription, FHIRUrlParser requestURL,
            long accumulatedTime, String type, String compartment, String compartmentId,
            MultivaluedMap<String, String> queryParameters, String requestUri,
            Resource contextResource, boolean checkInteractionAllowed) throws Exception;

    /**
     * Performs a 'vread' operation by retrieving the specified version of a Resource with no query parameters
     *
     * @param entryIndex
     * @param requestDescription
     * @param requestURL
     * @param accumulatedTime
     * @param type
     *            the resource type associated with the Resource to be retrieved
     * @param id
     *            the id of the Resource to be retrieved
     * @param versionId
     *            the version id of the Resource to be retrieved
     * @param queryParameters
     * @return the Resource
     * @throws Exception
     */
    FHIRRestOperationResponse doVRead(int entryIndex, String requestDescription, FHIRUrlParser requestURL,
            long accumulatedTime, String type, String id, String versionId, MultivaluedMap<String, String> queryParameters)
            throws Exception;

    /**
     * Performs a 'read' operation to retrieve a Resource.
     *
     * @param entryIndex
     * @param requestDescription
     * @param requestURL
     * @param accumulatedTime
     * @param type
     *            the resource type associated with the Resource to be retrieved
     * @param id
     *            the id of the Resource to be retrieved
     * @param throwExcOnNull
     *            whether to throw an exception on null
     * @param contextResource
     *            the resource
     * @param queryParameters
     *            for supporting _elements and _summary for resource read
     * @return a SingleResourceResult wrapping the resource and including its deletion status
     * @throws Exception
     */
    FHIRRestOperationResponse doRead(int entryIndex, String requestDescription, FHIRUrlParser requestURL,
            long accumulatedTime, String type, String id, boolean throwExcOnNull,
            Resource contextResource, MultivaluedMap<String, String> queryParameters, boolean checkInteractionAllowed)
            throws Exception;

    /**
     * Performs the work of retrieving versions of a Resource.
     *
     * @param entryIndex
     * @param requestDescription
     * @param requestURL
     * @param accumulatedTime
     * @param type
     *            the resource type associated with the Resource to be retrieved
     * @param id
     *            the id of the Resource to be retrieved
     * @param queryParameters
     *            a Map containing the query parameters from the request URL
     * @param requestUri
     * @return a Bundle containing the history of the specified Resource
     * @throws Exception
     */
    FHIRRestOperationResponse doHistory(int entryIndex, String requestDescription, FHIRUrlParser requestURL,
            long accumulatedTime, String type, String id, MultivaluedMap<String, String> queryParameters, String requestUri)
            throws Exception;

    /**
     * Performs the heavy lifting associated with a 'create' interaction.
     *
     * @param entryIndex
     * @param event
     * @param warnings
     *            the list of warning issues accumulated for this entry
     * @param validationResponseEntry
     * @param requestDescription
     * @param requestURL
     * @param accumulatedTime
     * @param type
     *            the resource type specified as part of the request URL
     * @param resource
     *            the Resource to be stored.
     * @param ifNoneExist
     *            whether to create the resource if none exists
     * @param localIdentifier
     * @param offloadResponse
     *            the response from payload persistence when offloading
     * @return a FHIRRestOperationResponse object containing the results of the operation
     * @throws Exception
     */
    FHIRRestOperationResponse doCreate(int entryIndex, FHIRPersistenceEvent event, List<Issue> warnings,
            Entry validationResponseEntry, String requestDescription, FHIRUrlParser requestURL, long accumulatedTime,
            String type, Resource resource, String ifNoneExist, String localIdentifier, PayloadPersistenceResponse offloadResponse) throws Exception;

    /**
     * Performs an update operation (a new version of the Resource will be stored).
     *
     * @param entryIndex
     * @param event
     *            the persistence event used for this resource interaction
     * @param validationResponseEntry
     * @param requestDescription
     * @param requestURL
     * @param accumulatedTime
     * @param type
     * @param id
     *            the id of the Resource being updated
     * @param newResource
     *            the new resource to be stored
     * @param prevResource
     *            the old resource value if we have it
     * @param ifMatchValue
     *            an optional "If-Match" header value to request a version-aware update
     * @param searchQueryString
     *            an optional search query string to request a conditional update
     * @param skippableUpdate
     *            if true, and the resource content in the update matches the existing resource on the server, then skip the update;
     *            if false, then always attempt the update
     * @param localIdentifier
     *            if not null, represents the local identifier within the bundle used to support local references
     * @param warnings
     *            the accumulated list of warnings gathered while processing the interaction
     * @param isDeleted
     *            flag to indicate if the resource is currently deleted
     * @param ifNoneMatch
     *            conditional create-on-update
     * @param offloadResponse
     *            the response from payload persistence when offloading
     * @return a FHIRRestOperationResponse that contains the results of the operation
     * @throws Exception
     */
    FHIRRestOperationResponse doUpdate(int entryIndex, FHIRPersistenceEvent event, Entry validationResponseEntry,
            String requestDescription, FHIRUrlParser requestURL, long accumulatedTime, String type, String id,
            Resource newResource, Resource prevResource, String ifMatchValue, String searchQueryString,
            boolean skippableUpdate, String localIdentifier, List<Issue> warnings, boolean isDeleted, Integer ifNoneMatch,
            PayloadPersistenceResponse offloadResponse) throws Exception;

    /**
     * Performs a patch operation (a new version of the Resource will be stored).
     *
     * @param entryIndex
     * @param event
     *            the persistence event used for this resource interaction
     * @param validationResponseEntry
     * @param requestDescription
     * @param requestURL
     * @param accumulatedTime
     * @param type
     *            the type of the resource to be updated
     * @param id
     *            the id of the Resource being updated
     * @param newResource
     *            the latest value of the resource
     * @param prevResource
     *            the value of the resource before any changes
     * @param patch
     *            the patch to apply
     * @param ifMatchValue
     *            an optional "If-Match" header value to request a version-aware update
     * @param searchQueryString
     *            an optional search query string to request a conditional update
     * @param skippableUpdate
     *            if true, and the result of the patch matches the existing resource on the server, then skip the update;
     *            if false, then always attempt the update
     * @param offloadResponse
     *            response from payload persistencen when offloading
     * @param warnings
     * @param localIdentifier
     * @return a FHIRRestOperationResponse that contains the results of the operation
     * @throws Exception
     */
    FHIRRestOperationResponse doPatch(int entryIndex, FHIRPersistenceEvent event, Entry validationResponseEntry,
            String requestDescription, FHIRUrlParser requestURL, long accumulatedTime, String type, String id,
            Resource newResource, Resource prevResource, FHIRPatch patch, String ifMatchValue, String searchQueryString,
            boolean skippableUpdate, List<Issue> warnings, String localIdentifier, PayloadPersistenceResponse offloadResponse) throws Exception;

    /**
     * Helper method which invokes a custom operation.
     *
     * @param method
     * @param entryIndex
     * @param validationResponseEntry
     * @param requestDescription
     * @param requestUrl
     * @param accumulatedTime
     * @param operationContext
     *            the FHIROperationContext associated with the request
     * @param resourceTypeName
     *            the resource type associated with the request
     * @param logicalId
     *            the resource logical id associated with the request
     * @param versionId
     *            the resource version id associated with the request
     * @param resource
     *            the input resource associated with the custom operation to be invoked
     * @param queryParameters
     *            query parameters may be passed instead of a Parameters resource for certain custom operations invoked
     *            via GET
     * @return a Resource that represents the response to the custom operation
     * @throws Exception
     */
    FHIRRestOperationResponse doInvoke(String method, int entryIndex, Entry validationResponseEntry,
            String requestDescription, FHIRUrlParser requestURL, long accumulatedTime,
            FHIROperationContext operationContext, String resourceTypeName, String logicalId, String versionId,
            Resource resource, MultivaluedMap<String, String> queryParameters) throws Exception;

    /**
     * Performs a 'delete' operation on the specified resource.
     *
     * @param type
     *            the resource type associated with the Resource to be deleted
     * @param id
     *            the id of the Resource to be deleted
     * @return a FHIRRestOperationResponse that contains the results of the operation
     * @throws Exception
     */
    FHIRRestOperationResponse doDelete(int entryIndex, String requestDescription, FHIRUrlParser requestURL,
            long accumulatedTime, String type, String id, String searchQueryString) throws Exception;

    /**
     * Add the given validationResponseEntry to the result bundle
     *
     * @param entryIndex
     * @param validationResponseEntry
     * @param requestDescription
     * @param accumulatedTime
     * @return
     * @throws Exception
     */
    FHIRRestOperationResponse validationResponse(int entryIndex, Entry validationResponseEntry,
            String requestDescription, long accumulatedTime) throws Exception;

    /**
     * Add the issue to the result bundle
     *
     * @param entryIndex
     * @param requestDescription
     * @param accumulatedTime
     * @param status
     * @param responseEntry
     * @return
     * @throws Exception
     */
    FHIRRestOperationResponse issue(int entryIndex, String requestDescription, long accumulatedTime, Status status,
            Entry responseEntry) throws Exception;
}