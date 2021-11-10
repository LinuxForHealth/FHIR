/*
 * (C) Copyright IBM Corp. 2016, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence;

import java.util.List;
import java.util.concurrent.Future;
import java.util.function.Function;

import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.fhir.persistence.erase.EraseDTO;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.exception.FHIRPersistenceNotSupportedException;
import com.ibm.fhir.persistence.payload.PayloadKey;

/**
 * This interface defines the contract between the FHIR Server's REST API layer and the underlying
 * persistence layer that is responsible for interacting with a particular datastore to manage
 * instances of FHIR Resources.
 */
public interface FHIRPersistence {

    /**
     * Stores a new FHIR Resource in the datastore. Id assignment handled by the implementation.
     * This method has been deprecated. Instead, generate the logical id first and use the
     * createWithMeta(context, resource) call instead.
     * @param context the FHIRPersistenceContext instance associated with the current request
     * @param resource the FHIR Resource instance to be created in the datastore
     * @return a SingleResourceResult with a copy of resource with Meta fields updated by the persistence layer and/or
     *         an OperationOutcome with hints, warnings, or errors related to the interaction
     * @throws FHIRPersistenceException
     */
    @Deprecated
    <T extends Resource> SingleResourceResult<T> create(FHIRPersistenceContext context, T resource) throws FHIRPersistenceException;

    /**
     * Stores a new FHIR Resource in the datastore. The resource is not modified before it is stored. It
     * must therefore already include correct Meta fields. Should be used instead of
     * method {@link #create(FHIRPersistenceContext, Resource)}.
     *
     * @param context the FHIRPersistenceContext instance associated with the current request
     * @param resource the FHIR Resource instance to be created in the datastore
     * @return a SingleResourceResult with the unmodified resource and/or
     *         an OperationOutcome with hints, warnings, or errors related to the interaction
     * @throws FHIRPersistenceException
     */
    <T extends Resource> SingleResourceResult<T> createWithMeta(FHIRPersistenceContext context, T resource) throws FHIRPersistenceException;

    /**
     * Retrieves the most recent version of a FHIR Resource from the datastore.
     *
     * @param context the FHIRPersistenceContext instance associated with the current request
     * @param resourceType the resource type of the Resource instance to be retrieved
     * @param logicalId the logical id of the Resource instance to be retrieved
     * @return a SingleResourceResult with the FHIR Resource that was retrieved from the datastore and/or
     *         an OperationOutcome with hints, warnings, or errors related to the interaction
     * @throws FHIRPersistenceException
     */
    <T extends Resource> SingleResourceResult<T> read(FHIRPersistenceContext context, Class<T> resourceType, String logicalId)
            throws FHIRPersistenceException;

    /**
     * Retrieves a specific version of a FHIR Resource from the datastore.
     *
     * @param context the FHIRPersistenceContext instance associated with the current request
     * @param resourceType the resource type of the Resource instance to be retrieved
     * @param logicalId the logical id of the Resource instance to be retrieved
     * @param versionId the version of the Resource instance to be retrieved
     * @return a SingleResourceResult with the FHIR Resource that was retrieved from the datastore and/or
     *         an OperationOutcome with hints, warnings, or errors related to the interaction
     * @throws FHIRPersistenceException
     */
    <T extends Resource> SingleResourceResult<T> vread(FHIRPersistenceContext context, Class<T> resourceType, String logicalId, String versionId)
            throws FHIRPersistenceException;

    /**
     * Updates an existing FHIR Resource by storing a new version in the datastore.
     *
     * @param context the FHIRPersistenceContext instance associated with the current request
     * @param logicalId the logical id of the FHIR Resource to be updated
     * @param resource the new contents of the FHIR Resource to be stored
     * @return a SingleResourceResult with a copy of resource with fields updated by the persistence layer and/or
     *         an OperationOutcome with hints, warnings, or errors related to the interaction
     * @throws FHIRPersistenceException
     */
    @Deprecated
    <T extends Resource> SingleResourceResult<T> update(FHIRPersistenceContext context, String logicalId, T resource) throws FHIRPersistenceException;

    /**
     * Updates an existing FHIR Resource by storing a new version in the datastore. This implementation
     * is added to replace the deprecated {@link #update(FHIRPersistenceContext, String, Resource)} method
     * This new method expects the resource being passed in to already be modified with correct
     * meta and id information. It no longer updates the meta itself.
     *
     * @param context the FHIRPersistenceContext instance associated with the current request
     * @param resource the new contents of the FHIR Resource to be stored
     * @return a SingleResourceResult with a copy of resource with fields updated by the persistence layer and/or
     *         an OperationOutcome with hints, warnings, or errors related to the interaction
     * @throws FHIRPersistenceException
     */
    <T extends Resource> SingleResourceResult<T> updateWithMeta(FHIRPersistenceContext context, T resource) throws FHIRPersistenceException;

    /**
     * Deletes the specified FHIR Resource from the datastore.
     *
     * @param context the FHIRPersistenceContext instance associated with the current request
     * @param resourceType The type of FHIR Resource to be deleted.
     * @param logicalId the logical id of the FHIR Resource to be deleted
     * @return a SingleResourceResult with the FHIR Resource that was deleted or null if the specified resource doesn't exist and/or
     *         an OperationOutcome with hints, warnings, or errors related to the interaction
     * @throws FHIRPersistenceException
     */
    default <T extends Resource> SingleResourceResult<T> delete(FHIRPersistenceContext context, Class<T> resourceType, String logicalId) throws FHIRPersistenceException {
        throw new FHIRPersistenceNotSupportedException("The 'delete' operation is not supported by this persistence implementation");
    }

    /**
     * Retrieves all of the versions of the specified FHIR Resource.
     *
     * @param context the FHIRPersistenceContext instance associated with the current request
     * @param resourceType the resource type of the Resource instances to be retrieved
     * @param logicalId the logical id of the Resource instances to be retrieved
     * @return a MultiResourceResult with a list containing the available versions of the specified FHIR Resource and/or
     *         an OperationOutcome with hints, warnings, or errors related to the interaction
     * @throws FHIRPersistenceException
     */
    <T extends Resource> MultiResourceResult<T> history(FHIRPersistenceContext context, Class<T> resourceType, String logicalId) throws FHIRPersistenceException;

    /**
     * Performs a search on the specified target resource type using the specified search parameters.
     *
     * @param context the FHIRPersistenceContext instance associated with the current request
     * @param resourceType the resource type which is the target of the search
     * @return a MultiResourceResult with the list of FHIR Resources in the search result set and/or
     *         an OperationOutcome with hints, warnings, or errors related to the interaction
     * @throws FHIRPersistenceException
     */
    MultiResourceResult<Resource> search(FHIRPersistenceContext context, Class<? extends Resource> resourceType) throws FHIRPersistenceException;

    /**
     * Returns true iff the persistence layer implementation supports transactions.
     */
    boolean isTransactional();

    /**
     * Returns an OperationOutcome indicating the current status of the persistence store / backend
     * @return An OperationOutcome with a list of 0 or more OperationalOutcomeIssue indicating the status of the underlying datastore
     * @throws FHIRPersistenceException
     */
    OperationOutcome getHealth() throws FHIRPersistenceException;

    /**
     * Returns a FHIRPersistenceTransaction object associated with the persistence layer implementation in use.
     * This can then be used to control transactional boundaries.
     */
    FHIRPersistenceTransaction getTransaction();

    /**
     * Returns true iff the persistence layer implementation supports the "delete" operation.
     */
    default boolean isDeleteSupported() {
        return false;
    }

    /**
     * Returns true iff the persistence layer implementation supports update/create and it has been
     * configured in the persistence config.
     * @return
     */
    default boolean isUpdateCreateEnabled() {
        return false;
    }

    /**
     * Generates a resource ID.
     *
     * @return resource ID
     */
    String generateResourceId();

    /**
     * Returns true iff the persistence layer implementation supports the "reindex" special operation
     * @return
     */
    default boolean isReindexSupported() {
        return false;
    }

    /**
     * Initiates reindexing for either a specified list of index IDs,
     * or a randomly chosen resource. The number of resources processed is returned.
     * This can be used by a controller to continue processing until everything is complete.
     * @param context the FHIRPersistenceContext instance associated with the current request.
     * @param operationOutcomeResult accumulate issues in this {@link OperationOutcome.Builder}
     * @param tstamp only reindex resources with a reindex_tstamp less than this
     * @param indexIds list of index IDs of resources to reindex, or null
     * @param resourceLogicalId resourceType/logicalId value of a specific resource to reindex, or null;
     * this parameter is ignored if the indexIds parameter value is non-null
     * @return count of the number of resources reindexed by this call
     * @throws FHIRPersistenceException
     */
    int reindex(FHIRPersistenceContext context, OperationOutcome.Builder operationOutcomeResult, java.time.Instant tstamp, List<Long> indexIds,
        String resourceLogicalId) throws FHIRPersistenceException;

    /**
     * Special function for high speed export of resource payloads. The process
     * function must process the InputStream before returning. Result processing
     * will be stopped if the process function returns Boolean FALSE.
     *
     * @param resourceType the resource type which is the target of the search
     * @param fromLastModified start reading from this timestamp
     * @param fromResourceId start reading from this resourceId if provided (can be null)
     * @param toLastModified do not read beyond this timestamp
     * @param spanSeconds max number of seconds to include fromLastModified
     * @param process function to process each payload record
     * @return the last ResourcePayload processed, or null if no data was found
     * @throws FHIRPersistenceException
     */
    ResourcePayload fetchResourcePayloads(Class<? extends Resource> resourceType,
        java.time.Instant fromLastModified, java.time.Instant toLastModified,
        Function<ResourcePayload,Boolean> process) throws FHIRPersistenceException;

    /**
     * Returns true iff the persistence layer implementation supports the "changes" special operation
     * @return
     */
    default boolean isChangesSupported() {
        return false;
    }

    /**
     * Fetch up to resourceCount records from the RESOURCE_CHANGE_LOG table
     * @param resourceCount the max number of resource change records to fetch
     * @param fromLastModified filter records with record.lastUpdate >= fromLastModified. Optional.
     * @param afterResourceId filter records with record.resourceId > afterResourceId. Optional.
     * @param resourceTypeName filter records with record.resourceType = resourceTypeName. Optional.
     * @return a list containing up to resourceCount elements describing resources which have changed
     */
    List<ResourceChangeLogRecord> changes(int resourceCount, java.time.Instant fromLastModified, Long afterResourceId, String resourceTypeName) throws FHIRPersistenceException;

    /**
     * Erases part or a whole of a resource in the data layer
     * @param eraseDto the details of the user input
     * @return a record indicating the success or partial success of the erase
     * @throws FHIRPersistenceException
     */
    default ResourceEraseRecord erase(EraseDTO eraseDto) throws FHIRPersistenceException {
        throw new FHIRPersistenceException("Erase is not supported");
    }

    /**
     * Retrieves a list of index IDs available for reindexing.
     * @param count the maximum nuber of index IDs to retrieve
     * @param notModifiedAfter only retrieve index IDs for resources not last updated after the specified timestamp
     * @param afterIndexId retrieve index IDs starting after this specified index ID, or null to start with first index ID
     * @param resourceTypeName the resource type of index IDs to return, or null
     * @return list of index IDs available for reindexing
     * @throws FHIRPersistenceException
     */
    List<Long> retrieveIndex(int count, java.time.Instant notModifiedAfter, Long afterIndexId, String resourceTypeName) throws FHIRPersistenceException;

    /**
     * Offload payload storage to another provider. If result is not null, the returned
     * {@link Future} can be used to obtain the status of the operation. If the result
     * is null, then the implementation does not support offloading and the payload must
     * be stored in the traditional manner (e.g. in the RDBMS). A {@link Future} is used
     * because the offloading storage operation may be asynchronous.
     * @param resource
     * @param logicalId
     * @param newVersionNumber
     * @return
     * @throws FHIRPersistenceException
     */
    Future<PayloadKey> storePayload(Resource resource, String logicalId, int newVersionNumber) throws FHIRPersistenceException;
}