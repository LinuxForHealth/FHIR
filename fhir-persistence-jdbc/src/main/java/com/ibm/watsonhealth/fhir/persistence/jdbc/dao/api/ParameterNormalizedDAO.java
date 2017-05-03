/**
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc.dao.api;

import java.util.Map;

import com.ibm.watsonhealth.fhir.persistence.jdbc.dto.Parameter;
import com.ibm.watsonhealth.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;
import com.ibm.watsonhealth.fhir.persistence.jdbc.exception.FHIRPersistenceDataAccessException;

/**
 * This Data Access Object interface defines APIs specific to the Parameter DAO implementation 
 * for the "normalized" relational schema.
 * @author markd
 *
 */
public interface ParameterNormalizedDAO extends ParameterDAO {
	
	
	/**
	 * Reads all rows in the Parameter_Names table and returns the data as a Map
	 * @return Map<String, Long> - A map containing key=parameter-name, value=parameter-name-id
	 * @throws FHIRPersistenceDBConnectException
	 * @throws FHIRPersistenceDataAccessException
	 */
	Map<String,Integer> readAllSearchParameterNames() throws FHIRPersistenceDBConnectException, FHIRPersistenceDataAccessException;
	
	/**
	 * Reads all rows in the Code_Systems table and returns the data as a Map
	 * @return Map<String, Long> - A map containing key=system-name, value=system-id
	 * @throws FHIRPersistenceDBConnectException
	 * @throws FHIRPersistenceDataAccessException
	 */
	Map<String,Integer> readAllCodeSystems() throws FHIRPersistenceDBConnectException, FHIRPersistenceDataAccessException;
	
	
	/**
	 * Reads the id associated with the name of the passed Parameter from the Parameter_Names table. If the id for the passed name is not present
	 * in the database, an id is generated, persisted, and returned.
	 * @param String A valid FHIR search  parameter name.
	 * @return Integer - the id associated with the name of the passed Parameter.
	 * @throws FHIRPersistenceDBConnectException
	 * @throws FHIRPersistenceDataAccessException
	 */
	Integer readParameterNameId(String parameterName) throws FHIRPersistenceDBConnectException, FHIRPersistenceDataAccessException;
	
	/**
	 * Reads the id associated with the name of the passed code system name from the Code_Systems table. If the id for the passed system name is not present
	 * in the database, an id is generated, persisted, and returned.
	 * @param systemName - The name of a FHIR code system.
	 * @return Integer - The id associated with the passed code system name.
	 * @throws FHIRPersistenceDBConnectException
	 * @throws FHIRPersistenceDataAccessException
	 */
	Integer readCodeSystemId(String systemName) throws FHIRPersistenceDBConnectException, FHIRPersistenceDataAccessException;

}
