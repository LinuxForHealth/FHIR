/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence;

import java.util.List;

import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.watsonhealth.fhir.search.Parameter;

/**
 * This interface defines the contract between the FHIR Server's REST API layer and the underlying
 * persistence layer that is responsible for interacting with a particular datastore to manage
 * instances of FHIR Resources.
 */
public interface FHIRPersistence {
    
    /**
     * Stores a new FHIR Resource in the datastore.
     * @param resource the FHIR Resource instance to be created in the datastore.
     * @throws FHIRPersistenceException
     */
	void create(Resource resource) throws FHIRPersistenceException;
	
	/**
	 * Retrieves the most recent version of a FHIR Resource from the datastore.
	 * @param resourceType the resource type of the Resource instance to be retrieved
	 * @param logicalId the logical id of the Resource instance to be retrieved
	 * @return the FHIR Resource that was retrieved from the datastore
	 * @throws FHIRPersistenceException
	 */
	Resource read(Class<? extends Resource> resourceType, String logicalId) throws FHIRPersistenceException;
	
	/**
	 * Retrieves a specific version of a FHIR Resource from the datastore.
     * @param resourceType the resource type of the Resource instance to be retrieved
     * @param logicalId the logical id of the Resource instance to be retrieved
     * @param versionId the version of the Resource instance to be retrieved
     * @return the FHIR Resource that was retrieved from the datastore
	 * @return
	 * @throws FHIRPersistenceException
	 */
	Resource vread(Class<? extends Resource> resourceType, String logicalId, String versionId) throws FHIRPersistenceException;
	
	/**
	 * Updates an existing FHIR Resource by storing a new version in the datastore.
	 * @param logicalId the logical id of the FHIR Resource to be updated
	 * @param resource the new contents of the FHIR Resource to be stored
	 * @throws FHIRPersistenceException
	 */
	void update(String logicalId, Resource resource) throws FHIRPersistenceException;
	
	/**
	 * Deletes the specified FHIR Resource from the datastore.
	 * Note: this is currently not implemented, pending a decision on the type of delete to perform.
	 * @param logicalId the logical id of the FHIR Resource to be deleted
	 * @throws FHIRPersistenceException
	 */
	void delete(String logicalId) throws FHIRPersistenceException;
	
	/**
	 * Retrieves all of the versions of the specified FHIR Resource.
     * @param resourceType the resource type of the Resource instances to be retrieved
     * @param logicalId the logical id of the Resource instances to be retrieved
	 * @return a list containing the available versions of the specified FHIR Resource
	 * @throws FHIRPersistenceException
	 */
	List<Resource> history(Class<? extends Resource> resourceType, String logicalId) throws FHIRPersistenceException;
	
	/**
	 * Performs a search on the specified target resource type using the specified search parameters.
	 * @param resourceType the resource type which is the target of the search
	 * @param searchParameters a list of parameters which control the search results
	 * @return the list of FHIR Resources of the specified resource type which forms the search result set
	 * @throws FHIRPersistenceException
	 */
	List<Resource> search(Class<? extends Resource> resourceType, List<Parameter> searchParameters) throws FHIRPersistenceException;
}
