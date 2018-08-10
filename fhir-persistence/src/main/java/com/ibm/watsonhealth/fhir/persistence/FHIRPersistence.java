/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence;

import java.util.List;

import com.ibm.watsonhealth.fhir.model.OperationOutcome;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceNotSupportedException;
import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceResourceDeletedException;

/**
 * This interface defines the contract between the FHIR Server's REST API layer and the underlying
 * persistence layer that is responsible for interacting with a particular datastore to manage
 * instances of FHIR Resources.
 */
public interface FHIRPersistence {
    
    /**
     * Stores a new FHIR Resource in the datastore.
     * @param context the FHIRPersistenceContext instance associated with the current request
     * @param resource the FHIR Resource instance to be created in the datastore
     * @throws FHIRPersistenceException
     */
	void create(FHIRPersistenceContext context, Resource resource) throws FHIRPersistenceException;
	
	/**
	 * Retrieves the most recent version of a FHIR Resource from the datastore.
     * @param context the FHIRPersistenceContext instance associated with the current request
	 * @param resourceType the resource type of the Resource instance to be retrieved
	 * @param logicalId the logical id of the Resource instance to be retrieved
	 * @return the FHIR Resource that was retrieved from the datastore
	 * @throws FHIRPersistenceException
	 * @throws FHIRPersistenceResourceDeletedException
	 */
	Resource read(FHIRPersistenceContext context, Class<? extends Resource> resourceType, String logicalId) 
							throws FHIRPersistenceException, FHIRPersistenceResourceDeletedException;
	
	/**
	 * Retrieves a specific version of a FHIR Resource from the datastore.
     * @param context the FHIRPersistenceContext instance associated with the current request
     * @param resourceType the resource type of the Resource instance to be retrieved
     * @param logicalId the logical id of the Resource instance to be retrieved
     * @param versionId the version of the Resource instance to be retrieved
     * @return the FHIR Resource that was retrieved from the datastore
	 * @throws FHIRPersistenceException
	 * @throws FHIRPersistenceResourceDeletedException
	 */
	Resource vread(FHIRPersistenceContext context, Class<? extends Resource> resourceType, String logicalId, String versionId) 
					throws FHIRPersistenceException, FHIRPersistenceResourceDeletedException;;
	
	/**
	 * Updates an existing FHIR Resource by storing a new version in the datastore.
     * @param context the FHIRPersistenceContext instance associated with the current request
	 * @param logicalId the logical id of the FHIR Resource to be updated
	 * @param resource the new contents of the FHIR Resource to be stored
	 * @throws FHIRPersistenceException
	 */
	void update(FHIRPersistenceContext context, String logicalId, Resource resource) throws FHIRPersistenceException;
	
	/**
	 * Deletes the specified FHIR Resource from the datastore.
	 * @param context the FHIRPersistenceContext instance associated with the current request
	 * @param resourceType The type of FHIR Resource to be deleted.
	 * @param logicalId the logical id of the FHIR Resource to be deleted
	 * @return the FHIR Resource that was deleted or null if the specified resource doesn't exist
	 * @throws FHIRPersistenceException
	 */
	default Resource delete(FHIRPersistenceContext context, Class<? extends Resource> resourceType, String logicalId) throws FHIRPersistenceException {
        throw new FHIRPersistenceNotSupportedException("The 'delete' operation is not supported by this persistence implementation");
	}
	
	/**
	 * Retrieves all of the versions of the specified FHIR Resource.
     * @param context the FHIRPersistenceContext instance associated with the current request
     * @param resourceType the resource type of the Resource instances to be retrieved
     * @param logicalId the logical id of the Resource instances to be retrieved
	 * @return a list containing the available versions of the specified FHIR Resource
	 * @throws FHIRPersistenceException
	 */
	List<Resource> history(FHIRPersistenceContext context, Class<? extends Resource> resourceType, String logicalId) throws FHIRPersistenceException;
	
	/**
	 * Performs a search on the specified target resource type using the specified search parameters.
     * @param context the FHIRPersistenceContext instance associated with the current request
	 * @param resourceType the resource type which is the target of the search
	 * @return the list of FHIR Resources of the specified resource type which forms the search result set
	 * @throws FHIRPersistenceException
	 */
	List<Resource> search(FHIRPersistenceContext context, Class<? extends Resource> resourceType) throws FHIRPersistenceException;

	/**
	 * Returns true iff the persistence layer implementation supports transactions.
	 */
	boolean isTransactional();

	/**
	 * Returns an OperationOutcome indicating the current status of the persistence store / backend
	 * @return a list of OperationalOutcomeIssue indicating the status of the underlying datastore
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
}
