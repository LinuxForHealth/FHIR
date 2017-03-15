/**
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc.dao.api;

import com.ibm.watsonhealth.fhir.persistence.jdbc.dto.Parameter;
import com.ibm.watsonhealth.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;
import com.ibm.watsonhealth.fhir.persistence.jdbc.exception.FHIRPersistenceDataAccessException;

/**
 * This Data Access Object interface defines methods for creating, updating, and retrieving rows in the FHIR Parameter table.
 * @author markd
 *
 */
public interface ParameterDAO extends FHIRDbDAO {

	/**
	 * Inserts the passed Parameter DTO to the FHIR Parameter table. 
	 * After insert, the generated primary key is acquired and set in the Parameter object.
	 * @param parameter
	 * @return Parameter
	 * @throws FHIRPersistenceDataAccessException
	 * @throws FHIRPersistenceDBConnectException
	 */
	Parameter insert(Parameter parameter) throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException;

	/**
	 * Deletes from the Parameter table all rows associated with the passed resource id.
	 * @param resourceId - The id of the resource for which Parameter rows should be deleted.
	 * @throws FHIRPersistenceDBConnectException
	 * @throws FHIRPersistenceDataAccessException 
	 */
	void deleteByResource(long resourceId) throws FHIRPersistenceDBConnectException, FHIRPersistenceDataAccessException;

}
