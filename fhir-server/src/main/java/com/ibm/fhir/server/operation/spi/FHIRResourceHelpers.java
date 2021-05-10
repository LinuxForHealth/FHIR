/*
 * (C) Copyright IBM Corp. 2017, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.operation.spi;

import java.time.Instant;

import javax.ws.rs.core.MultivaluedMap;

import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.patch.FHIRPatch;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.persistence.FHIRPersistenceTransaction;
import com.ibm.fhir.persistence.ResourceEraseRecord;
import com.ibm.fhir.persistence.erase.EraseDTO;

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
     * @return a FHIRRestOperationResponse object containing the results of the operation
     * @throws Exception
     */
    default FHIRRestOperationResponse doCreate(String type, Resource resource, String ifNoneExist) throws Exception {
        return doCreate(type, resource, ifNoneExist, DO_VALIDATION);
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
     * @param doValidation
     *            if true, validate the resource; if false, assume the resource has already been validated
     * @return a FHIRRestOperationResponse object containing the results of the operation
     * @throws Exception
     */
    FHIRRestOperationResponse doCreate(String type, Resource resource, String ifNoneExist, boolean doValidation) throws Exception;

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
            String searchQueryString, boolean skippableUpdate) throws Exception {
        return doUpdate(type, id, newResource, ifMatchValue, searchQueryString, skippableUpdate, DO_VALIDATION);
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
    FHIRRestOperationResponse doUpdate(String type, String id, Resource newResource, String ifMatchValue,
            String searchQueryString, boolean skippableUpdate, boolean doValidation) throws Exception;

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
    FHIRRestOperationResponse doPatch(String type, String id, FHIRPatch patch, String ifMatchValue,
            String searchQueryString, boolean skippableUpdate) throws Exception;


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
    FHIRRestOperationResponse doDelete(String type, String id, String searchQueryString) throws Exception;

    /**
     * Performs a 'read' operation to retrieve a Resource.
     *
     * @param type
     *            the resource type associated with the Resource to be retrieved
     * @param id
     *            the id of the Resource to be retrieved
     * @param throwExcOnNull
     *            whether to throw an exception on null
     * @param includeDeleted
     *            allow the read, even if the resource has been deleted
     * @param contextResource
     *            the resource
     * @param queryParameters
     *            for supporting _elements and _summary for resource read
     * @return the Resource
     * @throws Exception
     */
    default Resource doRead(String type, String id, boolean throwExcOnNull, boolean includeDeleted,
            Resource contextResource) throws Exception {
        return doRead(type, id, throwExcOnNull, includeDeleted, contextResource, null);
    }

    /**
     * Performs a 'read' operation to retrieve a Resource with select query parameters.
     *
     * @param type
     *            the resource type associated with the Resource to be retrieved
     * @param id
     *            the id of the Resource to be retrieved
     * @param throwExcOnNull
     *            whether to throw an exception on null
     * @param includeDeleted
     *            allow the read, even if the resource has been deleted
     * @param contextResource
     *            the resource
     * @param queryParameters
     *            for supporting _elements and _summary for resource read
     * @return the Resource
     * @throws Exception
     */
    Resource doRead(String type, String id, boolean throwExcOnNull, boolean includeDeleted,
            Resource contextResource, MultivaluedMap<String, String> queryParameters) throws Exception;

    /**
     * Performs a 'vread' operation by retrieving the specified version of a Resource with no query parameters
     *
     * @param type
     *            the resource type associated with the Resource to be retrieved
     * @param id
     *            the id of the Resource to be retrieved
     * @param versionId
     *            the version id of the Resource to be retrieved
     * @return the Resource
     * @throws Exception
     */
    default Resource doVRead(String type, String id, String versionId) throws Exception {
        return doVRead(type, id, versionId, null);
    }

    /**
     * Performs a 'vread' operation by retrieving the specified version of a Resource.
     *
     * @param type
     *            the resource type associated with the Resource to be retrieved
     * @param id
     *            the id of the Resource to be retrieved
     * @param versionId
     *            the version id of the Resource to be retrieved
     * @param queryParameters
     *            for supporting _elements and _summary for resource vread
     * @return the Resource
     * @throws Exception
     */
    Resource doVRead(String type, String id, String versionId, MultivaluedMap<String, String> queryParameters) throws Exception;

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
     * @return a Bundle containing the history of the specified Resource
     * @throws Exception
     */
    Bundle doHistory(String type, String id, MultivaluedMap<String, String> queryParameters, String requestUri) throws Exception;

    /**
     * Implement the system level history operation to obtain a list of changes to resources
     *
     * @param queryParameters
     *            a Map containing the query parameters from the request URL
     * @param requestUri
     *            the request URI
     * @return a Bundle containing the history of the specified Resource
     * @throws Exception
     */
    Bundle doHistory(MultivaluedMap<String, String> queryParameters, String requestUri) throws Exception;

    /**
     * Performs heavy lifting associated with a 'search' operation.
     *
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
     * @return a Bundle containing the search result set
     * @throws Exception
     */
    Bundle doSearch(String type, String compartment, String compartmentId, MultivaluedMap<String, String> queryParameters,
            String requestUri, Resource contextResource) throws Exception;


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
     * @return a Resource that represents the response to the custom operation
     * @throws Exception
     */
    Resource doInvoke(FHIROperationContext operationContext, String resourceTypeName, String logicalId, String versionId, String operationName,
            Resource resource, MultivaluedMap<String, String> queryParameters) throws Exception;

    /**
     * Processes a bundled request (batch or transaction type).
     *
     * @param bundle
     *            the request Bundle
     * @param skippableUpdates
     *            if true, and the bundle contains an update for which the resource content in the update matches the existing
     *            resource on the server, then skip the update; if false, then always attempt the updates specified in the bundle
     * @return the response Bundle
     */
    Bundle doBundle(Bundle bundle, boolean skippableUpdates) throws Exception;

    FHIRPersistenceTransaction getTransaction() throws Exception;

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
    int doReindex(FHIROperationContext operationContext, OperationOutcome.Builder operationOutcomeResult, Instant tstamp, String resourceLogicalId) throws Exception;
  
    /**
     * Invoke the FHIR Persistence erase operation for a specific instance of the erase.
     * @param operationContext
     * @param eraseBean
     * @return
     * @throws Exception
     */
    default ResourceEraseRecord doErase(FHIROperationContext operationContext, EraseDTO eraseBean) throws FHIROperationException {
        /*
         * @implNote, to keep from breaking other implementations, we are marking it as Unsupported and throwing an exception.
         */
        throw new FHIROperationException("Unsupported for the given platform");
    }
}