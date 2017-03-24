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
 * This Data Access Object interface defines APIs specific to the Parameter DAO implementation 
 * for the "normalized" relational schema.
 * @author markd
 *
 */
public interface ParameterNormalizedDAO extends ParameterDAO {
	
	/**
	 * Reads all rows in the Parameter_name table, and returns that data as a Map.
	 * @return Map<String, Long> - A map containing key=parameter-name, value=parameter-name-id
	 * @throws FHIRPersistenceDBConnectException
	 * @throws FHIRPersistenceDataAccessException
	 */
	Map<String, Long> readAllParameterNames() throws FHIRPersistenceDBConnectException, FHIRPersistenceDataAccessException;

}
