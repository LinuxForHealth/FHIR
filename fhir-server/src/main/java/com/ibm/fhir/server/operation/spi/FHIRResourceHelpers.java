/*
 * (C) Copyright IBM Corp. 2017, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.operation.spi;

import java.time.Instant;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import com.ibm.fhir.model.patch.FHIRPatch;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.persistence.FHIRPersistenceTransaction;

/**
 * This interface describes the set of helper methods from the FHIR REST layer that are used by custom operation
 * implementations.
 */
public interface FHIRResourceHelpers {
    // Constant for indicating the need to validate a resource
    public static final boolean DO_VALIDATION = true;
    // Constant for indicating whether an update can be skipped when the requested update resource matches the existing one
    public static final boolean SKIPPABLE_UPDATE = true;

    /**
     * Performs the heavy lifting associated with a 'create' interaction. Validates the resource.
     *
     * @param type
     *            the resource type specified as part of the request URL
     * @param resource
     *            the Resource to be stored.
     * @param ifNoneExist
     *            whether to create the resource if none exists
     * @param requestProperties
     *            additional request properties which supplement the HTTP headers associated with this request
     * @return a FHIRRestOperationResponse object containing the results of the operation
     * @throws Exception
     */
    default FHIRRestOperationResponse doCreate(String type, Resource resource, String ifNoneExist, Map<String, String> requestProperties) throws Exception {
        return doCreate(type, resource, ifNoneExist, requestProperties, DO_VALIDATION);
    }

    /**
     * Performs the heavy lifting associated with a 'create' interaction.
     *
     * @param type
     *            the resource type specified as part of the request URL
     * @param resource
     *            the Resource to be stored.
     * @param ifNoneExist
     *            whether to create the resource if none exists
     * @param requestProperties
     *            additional request properties which supplement the HTTP headers associated with this request
     * @param doValidation
     *            if true, validate the resource; if false, assume the resource has already been validated
     * @return a FHIRRestOperationResponse object containing the results of the operation
     * @throws Exception
     */
    public FHIRRestOperationResponse doCreate(String type, Resource resource, String ifNoneExist, Map<String, String> requestProperties, boolean doValidation) throws Exception;

    /**
     * Performs an update operation (a new version of the Resource will be stored). Validates the resource.
     *
     * @param type
     *            the type of the resource to be updated
     * @param id
     *            the id of the Resource being updated
     * @param newResource
     *            the new resource to be stored
     * @param ifMatchValue
     *            an optional "If-Match" header value to request a version-aware update
     * @param searchQueryString
     *            an optional search query string to request a conditional update
     * @param skippableUpdate
     *            if true, and the resource content in the update matches the existing resource on the server, then skip the update;
     *            if false, then always attempt the update
     * @return a FHIRRestOperationResponse that contains the results of the operation
     * @throws Exception
     */
    default FHIRRestOperationResponse doUpdate(String type, String id, Resource newResource, String ifMatchValue,
            String searchQueryString, Map<String, String> requestProperties, boolean skippableUpdate) throws Exception {
        return doUpdate(type, id, newResource, ifMatchValue, searchQueryString, requestProperties, skippableUpdate, DO_VALIDATION);
    }

    /**
     * Performs an update operation (a new version of the Resource will be stored).
     *
     * @param type
     *            the type of the resource to be updated
     * @param id
     *            the id of the Resource being updated
     * @param newResource
     *            the new resource to be stored
     * @param ifMatchValue
     *            an optional "If-Match" header value to request a version-aware update
     * @param searchQueryString
     *            an optional search query string to request a conditional update
     * @param skippableUpdate
     *            if true, and the resource content in the update matches the existing resource on the server, then skip the update;
     *            if false, then always attempt the update
     * @param doValidation
     *            if true, validate the resource; if false, assume the resource has already been validated
     * @return a FHIRRestOperationResponse that contains the results of the operation
     * @throws Exception
     */
    public FHIRRestOperationResponse doUpdate(String type, String id, Resource newResource, String ifMatchValue,
            String searchQueryString, Map<String, String> requestPropertie, boolean skippableUpdate, boolean doValidation) throws Exception;

    /**
     * Performs a patch operation (a new version of the Resource will be stored).
     *
     * @param type
     *            the type of the resource to be updated
     * @param id
     *            the id of the Resource being updated
     * @param patch
     *            the patch to apply
     * @param ifMatchValue
     *            an optional "If-Match" header value to request a version-aware update
     * @param searchQueryString
     *            an optional search query string to request a conditional update
     * @param skippableUpdate
     *            if true, and the result of the patch matches the existing resource on the server, then skip the update;
     *            if false, then always attempt the update
     * @return a FHIRRestOperationResponse that contains the results of the operation
     * @throws Exception
     */
    public FHIRRestOperationResponse doPatch(String type, String id, FHIRPatch patch, String ifMatchValue,
            String searchQueryString, Map<String, String> requestProperties, boolean skippableUpdate) throws Exception;


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
    public FHIRRestOperationResponse doDelete(String type, String id, String searchQueryString, Map<String, String> requestProperties) throws Exception;

    /**
     * Performs a 'read' operation to retrieve a Resource.
     *
     * @param type
     *            the resource type associated with the Resource to be retrieved
     * @param id
     *            the id of the Resource to be retrieved
     * @param queryParameters
     *            for supporting _elements and _summary for resource read
     * @return the Resource
     * @throws Exception
     */
    public Resource doRead(String type, String id, boolean throwExcOnNull, boolean includeDeleted, Map<String, String> requestProperties,
            Resource contextResource, MultivaluedMap<String, String> queryParameters) throws Exception;

    /**
     * Performs a 'read' operation to retrieve a Resource.
     *
     * @param type
     *            the resource type associated with the Resource to be retrieved
     * @param id
     *            the id of the Resource to be retrieved
     * @return the Resource
     * @throws Exception
     */
    public Resource doRead(String type, String id, boolean throwExcOnNull, boolean includeDeleted, Map<String, String> requestProperties,
            Resource contextResource) throws Exception;

    /**
     * Performs a 'vread' operation by retrieving the specified version of a Resource.
     *
     * @param type
     *            the resource type associated with the Resource to be retrieved
     * @param id
     *            the id of the Resource to be retrieved
     * @param versionId
     *            the version id of the Resource to be retrieved
     * @param requestProperties
     *            additional request properties which supplement the HTTP headers associated with this request
     * @param queryParameters
     *            for supporting _elements and _summary for resource vread
     * @return the Resource
     * @throws Exception
     */
    public Resource doVRead(String type, String id, String versionId, Map<String, String> requestProperties,
            MultivaluedMap<String, String> queryParameters) throws Exception;

    /**
     * Performs a 'vread' operation by retrieving the specified version of a Resource.
     *
     * @param type
     *            the resource type associated with the Resource to be retrieved
     * @param id
     *            the id of the Resource to be retrieved
     * @param versionId
     *            the version id of the Resource to be retrieved
     * @param requestProperties
     *            additional request properties which supplement the HTTP headers associated with this request
     * @return the Resource
     * @throws Exception
     */
    public Resource doVRead(String type, String id, String versionId, Map<String, String> requestProperties) throws Exception;

    /**
     * Performs the work of retrieving versions of a Resource.
     *
     * @param type
     *            the resource type associated with the Resource to be retrieved
     * @param id
     *            the id of the Resource to be retrieved
     * @param queryParameters
     *            a Map containing the query parameters from the request URL
     * @param requestUri
     * @param requestProperties
     * @return a Bundle containing the history of the specified Resource
     * @throws Exception
     */
    public Bundle doHistory(String type, String id, MultivaluedMap<String, String> queryParameters, String requestUri,
            Map<String, String> requestProperties) throws Exception;

    /**
     * Implement the system level history operation to obtain a list of changes to resources
     *
     * @param queryParameters
     *            a Map containing the query parameters from the request URL
     * @param requestUri
     * @param requestProperties
     * @return a Bundle containing the history of the specified Resource
     * @throws Exception
     */
    public Bundle doHistory(MultivaluedMap<String, String> queryParameters, String requestUri, Map<String, String> requestProperties) throws Exception;

    /**
     * Performs heavy lifting associated with a 'search' operation.
     *
     * @param type
     *            the resource type associated with the search
     * @param queryParameters
     *            a Map containing the query parameters from the request URL
     * @return a Bundle containing the search result set
     * @throws Exception
     */
    public Bundle doSearch(String type, String compartment, String compartmentId, MultivaluedMap<String, String> queryParameters,
            String requestUri, Map<String, String> requestProperties, Resource contextResource) throws Exception;


    /**
     * Helper method which invokes a custom operation.
     *
     * @param operationContext
     *            the FHIROperationContext associated with the request
     * @param resourceTypeName
     *            the resource type associated with the request
     * @param logicalId
     *            the resource logical id associated with the request
     * @param versionId
     *            the resource version id associated with the request
     * @param operationName
     *            the name of the custom operation to be invoked
     * @param resource
     *            the input resource associated with the custom operation to be invoked
     * @param queryParameters
     *            query parameters may be passed instead of a Parameters resource for certain custom operations invoked
     *            via GET
     * @param requestProperties
     *            additional request properties which supplement the HTTP headers associated with this request
     * @return a Resource that represents the response to the custom operation
     * @throws Exception
     */
    public Resource doInvoke(FHIROperationContext operationContext, String resourceTypeName, String logicalId, String versionId, String operationName,
            Resource resource, MultivaluedMap<String, String> queryParameters, Map<String, String> requestProperties) throws Exception;

    /**
     * Processes a bundled request (batch or transaction type).
     *
     * @param bundle
     *            the request Bundle
     * @return the response Bundle
     */
    public Bundle doBundle(Bundle bundle, Map<String, String> requestProperties) throws Exception;

    public FHIRPersistenceTransaction getTransaction() throws Exception;

    /**
     * Invoke the FHIR persistence reindex operation for a randomly chosen resource which was
     * last reindexed before the given date
     * @param operationContext
     * @param operationOutcomeResult
     * @param tstamp
     * @param resourceLogicalId a reference to a resource e.g. "Patient/abc123". Can be null
     * @return number of resources reindexed (0 if no resources were found to reindex)
     * @throws Exception
     */
    public int doReindex(FHIROperationContext operationContext, OperationOutcome.Builder operationOutcomeResult, Instant tstamp, String resourceLogicalId) throws Exception;
}