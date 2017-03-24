/**
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc.dao.api;

import java.util.Map;

import com.ibm.watsonhealth.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;
import com.ibm.watsonhealth.fhir.persistence.jdbc.exception.FHIRPersistenceDataAccessException;

/**
 * This Data Access Object interface defines APIs specific to the Resource DAO implementation 
 * for the "normalized" relational schema.
 * @author markd
 *
 */
public interface ResourceNormalizedDAO extends ResourceDAO {
	
	/**
	 * Reads all rows in the Resource_Type table, and returns that data as a Map.
	 * @return Map<String, Long> - A map containing key=resource-name, value=resource-name-id
	 * @throws FHIRPersistenceDBConnectException
	 * @throws FHIRPersistenceDataAccessException
	 */
	Map<String, Long> readAllResourceTypes() throws FHIRPersistenceDBConnectException, FHIRPersistenceDataAccessException;

}
