/*
 * (C) Copyright IBM Corp. 2016, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence;

import java.util.UUID;

import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.exception.FHIRPersistenceNotSupportedException;

/**
 * This interface defines the contract between the FHIR Server's REST API layer and the underlying
 * persistence layer that is responsible for interacting with a particular datastore to manage
 * instances of FHIR Resources.
 */
public interface FHIRPersistence {

    /**
     * Stores a new FHIR Resource in the datastore.
     *
     * @param context the FHIRPersistenceContext instance associated with the current request
     * @param resource the FHIR Resource instance to be created in the datastore
     * @return a SingleResourceResult with a copy of resource with Meta fields updated by the persistence layer and/or
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
     *
     * @param context the FHIRPersistenceContext instance associated with the current request
     * @param logicalId the logical id of the FHIR Resource to be updated
     * @param resource the new contents of the FHIR Resource to be stored
     * @return a SingleResourceResult with a copy of resource with fields updated by the persistence layer and/or
     *         an OperationOutcome with hints, warnings, or errors related to the interaction
     * @throws FHIRPersistenceException
     */
    <T extends Resource> SingleResourceResult<T> update(FHIRPersistenceContext context, String logicalId, T resource) throws FHIRPersistenceException;

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
     * Generates a logical identity.
     * 
     * @return logical identity
     */
    default String getLogicalId() {
        return UUID.randomUUID().toString();
    }
}
