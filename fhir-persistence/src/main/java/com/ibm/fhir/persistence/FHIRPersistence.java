/*
 * (C) Copyright IBM Corp. 2016, 2022
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
import com.ibm.fhir.persistence.payload.PayloadPersistenceResponse;

/**
 * This interface defines the contract between the FHIR Server's REST API layer and the underlying
 * persistence layer that is responsible for interacting with a particular datastore to manage
 * instances of FHIR Resources.
 */
public interface FHIRPersistence {

    /**
     * Stores a new FHIR Resource in the datastore. The resource is not modified before it is stored. It
     * must therefore already include correct Meta fields.
     *
     * @param context the FHIRPersistenceContext instance associated with the current request
     * @param resource the FHIR Resource instance to be created in the datastore
     * @return a SingleResourceResult with the unmodified resource and/or
     *         an OperationOutcome with hints, warnings, or errors related to the interaction
     * @throws FHIRPersistenceException
     */
    <T extends Resource> SingleResourceResult<T> create(FHIRPersistenceContext context, T resource) throws FHIRPersistenceException;

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
     * This new method expects the resource being passed in to already be modified with correct
     * meta and id information. It no longer updates the meta itself.
     *
     * @param context the FHIRPersistenceContext instance associated with the current request
     * @param resource the new contents of the FHIR Resource to be stored
     * @return a SingleResourceResult with a copy of resource with fields updated by the persistence layer and/or
     *         an OperationOutcome with hints, warnings, or errors related to the interaction
     * @throws FHIRPersistenceException
     */
    <T extends Resource> SingleResourceResult<T> update(FHIRPersistenceContext context, T resource) throws FHIRPersistenceException;

    /**
     * Deletes the FHIR resource from the datastore. The delete is a soft-delete, resulting
     * in a new version being created to act as a deletion marker. This version is created
     * without a payload body. The versionId must match the latest version of the resource.
     * This must be checked by the {@link FHIRPersistence} implementation, which should throw a
     * FHIRPersistenceVersionIdMismatchException is there is a mismatch (likely an indication
     * of concurrent changes to the resource).
     * @param <T>
     * @param context the FHIRPersistenceContext instance associated with the current request
     * @param resourceType the resource type of the Resource instance to be deleted
     * @param logicalId the logical id of the Resource instance to be deleted
     * @param versionId the current version of the Resource instance to be deleted
     * @param lastUpdated the modification timestamp to use for the deletion
     * @throws FHIRPersistenceException
     */
    default <T extends Resource> void delete(FHIRPersistenceContext context, Class<T> resourceType, String logicalId, int versionId, 
            com.ibm.fhir.model.type.Instant lastUpdated) throws FHIRPersistenceException {
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
    MultiResourceResult history(FHIRPersistenceContext context, Class<? extends Resource> resourceType, String logicalId) throws FHIRPersistenceException;

    /**
     * Performs a search on the specified target resource type using the specified search parameters.
     *
     * @param context the FHIRPersistenceContext instance associated with the current request
     * @param resourceType the resource type which is the target of the search
     * @return a MultiResourceResult with the list of FHIR Resources in the search result set and/or
     *         an OperationOutcome with hints, warnings, or errors related to the interaction
     * @throws FHIRPersistenceException
     */
    MultiResourceResult search(FHIRPersistenceContext context, Class<? extends Resource> resourceType) throws FHIRPersistenceException;

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
     * Read the resources for each of the change log records in the list, aligning
     * the entries in the returned list to match the entries in the records list.
     * @param records
     * @return a list of Resources with the same number of entries as the given records list
     */
    List<Resource> readResourcesForRecords(List<ResourceChangeLogRecord> records) throws FHIRPersistenceException;

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
     * Returns true iff the persistence layer implementation supports offloading and this has been
     * configured for the tenant/datasource
     * @return
     */
    default boolean isOffloadingSupported() {
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
     * @param sinceLastModified filter records with record.lastUpdate >= sinceLastModified. Optional.
     * @param beforeLastModified filter records with record.lastUpdate <= beforeLastModified. Optional.
     * @param afterResourceId filter records with record.resourceId > afterResourceId. Optional.
     * @param resourceTypeNames filter records matching any resource type name in the list
     * @param excludeTransactionTimeoutWindow flag to exclude resources falling inside server's tx timeout window
     * @param historySortOrder the type of sorting to apply
     * @return a list containing up to resourceCount elements describing resources which have changed
     * @throws FHIRPersistenceException
     */
    List<ResourceChangeLogRecord> changes(int resourceCount, java.time.Instant sinceLastModified, java.time.Instant beforeLastModified, Long changeIdMarker, List<String> resourceTypeNames, boolean excludeTransactionTimeoutWindow, 
            HistorySortOrder historySortOrder) throws FHIRPersistenceException;

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
     * because the offloading storage operation may be asynchronous. This Future must be
     * resolved prior to the transaction commit.
     * @param resource
     * @param logicalId
     * @param newVersionNumber
     * @param resourcePayloadKey
     * @return
     * @throws FHIRPersistenceException
     */
    PayloadPersistenceResponse storePayload(Resource resource, String logicalId, int newVersionNumber, String resourcePayloadKey) throws FHIRPersistenceException;
}