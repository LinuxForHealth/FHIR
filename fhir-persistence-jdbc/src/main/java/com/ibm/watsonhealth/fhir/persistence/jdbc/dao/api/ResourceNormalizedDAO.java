/**
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc.dao.api;

import java.util.List;

import com.ibm.watsonhealth.fhir.persistence.jdbc.dto.Resource;
import com.ibm.watsonhealth.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;
import com.ibm.watsonhealth.fhir.persistence.jdbc.exception.FHIRPersistenceDataAccessException;
import com.ibm.watsonhealth.fhir.persistence.jdbc.util.SqlQueryData;

/**
 * This Data Access Object interface defines APIs specific to the Resource DAO implementation 
 * for the "normalized" relational schema.
 * 
 * @author markd
 *
 */
public interface ResourceNormalizedDAO extends ResourceDAO {
	
	/**
	 * Executes the search contained in the passed SqlQueryData, using it's encapsulated search string and bind variables.
	 * @param queryData - Contains a search string and (optionally) bind variables.
	 * @return List<Resource> A list of FHIR Resources satisfying the passed search.
	 * @throws FHIRPersistenceDataAccessException
	 * @throws FHIRPersistenceDBConnectException
	 */
	List<Resource> search(SqlQueryData queryData) throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException;
	
	
	/**
	 * Executes a count query based on the data contained in the passed SqlQueryData, using it's encapsulated search string and bind variables.
	 * @param queryData - Contains a search string and (optionally) bind variables.
	 * @return int A count of FHIR Resources satisfying the passed search.
	 * @throws FHIRPersistenceDataAccessException
	 * @throws FHIRPersistenceDBConnectException
	 */
	int searchCount(SqlQueryData queryData) throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException;

	
	

}
