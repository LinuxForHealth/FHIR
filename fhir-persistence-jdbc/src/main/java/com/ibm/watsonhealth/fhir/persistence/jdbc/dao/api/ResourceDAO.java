/**
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc.dao.api;

import java.sql.Timestamp;
import java.util.List;

import com.ibm.watsonhealth.fhir.persistence.jdbc.dto.Resource;
import com.ibm.watsonhealth.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;
import com.ibm.watsonhealth.fhir.persistence.jdbc.exception.FHIRPersistenceDataAccessException;

/**
 * This Data Access Object interface provides methods creating, updating, and retrieving rows in the FHIR Resource table.
 * @author markd
 *
 */
public interface ResourceDAO extends FHIRDbDAO {

	/**
	 * Inserts the passed Resource DTO to the FHIR Resource table. 
	 * After insert, the generated primary key is acquired and set in the Resource object.
	 * @param resource
	 * @return Resource
	 * @throws FHIRPersistenceDataAccessException
	 * @throws FHIRPersistenceDBConnectException
	 */
	Resource insert(Resource resource) throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException;

	/**
	 * Reads and returns the latest version of the Resource with the passed logical id and resource type.
	 * If no matching resource is found, null is returned.
	 * @param logicalId
	 * @param resourceType
	 * @return Resource - The most recent version of the Resource with the passed logical id and resource type, or null if not found.
	 * @throws FHIRPersistenceDataAccessException
	 * @throws FHIRPersistenceDBConnectException
	 */
	Resource read(String logicalId, String resourceType)
			throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException;

	/**
	 * Reads and returns the version of the Resource with the passed logical id, resource type, and version id.
	 * If no matching resource is found, null is returned.
	 * @param logicalId
	 * @param resourceType
	 * @param version id
	 * @return Resource - The version of the Resource with the passed logical id, resource type, and version id or null if not found.
	 * @throws FHIRPersistenceDataAccessException
	 * @throws FHIRPersistenceDBConnectException
	 */
	Resource versionRead(String logicalId, String resourceType, int versionId)
			throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException;

	/**
	 * Executes the passed fully-formed SQL Select statement and returns the results
	 * If no matching resources are found, an empty collection is returned.
	 * @param sqlSelect - A fully formed SQL select statement.
	 * @return List<Resource> - A List of Resources that satisfy the passed SQL query string.
	 * @throws FHIRPersistenceDataAccessException
	 * @throws FHIRPersistenceDBConnectException
	 */
	List<Resource> search(String sqlSelect)
			throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException;

	/**
	 * This method supports the execution of a specialized query designed to return Resource ids, based on the contents
	 * of the passed select statement.
	 * Note that the first column to be selected MUST be the Resource.id column.
	 * @param sqlSelect - A select for Resource ids.
	 * @return - A List of resource ids that satisfy the passed SQL query.
	 * @throws FHIRPersistenceDataAccessException
	 * @throws FHIRPersistenceDBConnectException
	 */
	List<Long> searchForIds(String sqlSelect)
			throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException;

	/**
	 * Searches for Resources that contain one of the passed ids.
	 * @param resourceIds - A List of resource ids.
	 * @return List<Resource> - A List of resources matching the the passed list of ids.
	 * @throws FHIRPersistenceDataAccessException
	 * @throws FHIRPersistenceDBConnectException
	 */
	List<Resource> searchByIds(List<Long> resourceIds)
			throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException;

	/**
	 * Executes the passed fully-formed SQL Select COUNT statement and returns the integer count.
	 * 
	 * @param sqlSelect - A fully formed SQL select count statement.
	 * @return int - The count of resources that fulfill the passed SQL select statement.
	 * @throws FHIRPersistenceDataAccessException
	 * @throws FHIRPersistenceDBConnectException
	 */
	int searchCount(String sqlSelectCount) throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException;

	/**
	 * Reads and returns all versions of the Resource with the passed logicalId, ordered by descending version id.
	 * If non-null, the passed fromDateTime is used to limit the returned Resource
	 * versions to those that were updated after the fromDateTime.
	 * @param resourceType - The name of a FHIR Resource type
	 * @param logicalId - The logical id of a FHIR Resource
	 * @param fromDateTime - The starting date/time of the version history.
	 * @return List<Resource> - An ordered list of Resource versions.
	 * @throws FHIRPersistenceDataAccessException
	 * @throws FHIRPersistenceDBConnectException
	 */
	List<Resource> history(String resourceType, String logicalId, Timestamp fromDateTime, int offset, int maxResults)
			throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException;

	/**
	 * Reads and returns the COUNT of all versions of the Resource with the passed logicalId.
	 * If non-null, the passed fromDateTime is used to limit the count of Resource versions to those that were updated after the fromDateTime.
	 * @param resourceType - The name of a FHIR Resource type
	 * @param logicalId - The logical id of a FHIR Resource
	 * @param fromDateTime - The starting date/time of the version history.
	 * @return int - The count of Resource versions.
	 * @throws FHIRPersistenceDataAccessException
	 * @throws FHIRPersistenceDBConnectException
	 */
	int historyCount(String resourceType, String logicalId, Timestamp fromDateTime)
			throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException;

}
