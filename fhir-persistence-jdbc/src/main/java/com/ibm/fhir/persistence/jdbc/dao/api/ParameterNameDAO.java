/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dao.api;

import java.util.Map;

import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDataAccessException;

/**
 * This Data Access Object interface defines APIs specific to parameter_names table.
 * Refactor of the original ParameterNormalizedDAO stuff to split out parameter names,
 * code systems etc for clarity/easier maintenance.
 * @author rarnold
 */
public interface ParameterNameDAO {
    
    /**
     * Reads all rows in the Parameter_Names table and returns the data as a Map
     * @return Map<String, Long> - A map containing key=parameter-name, value=parameter-name-id
     * @throws FHIRPersistenceDataAccessException
     */
    Map<String,Integer> readAllSearchParameterNames() throws FHIRPersistenceDataAccessException;
       
    /**
     * Reads the id associated with the name of the passed Parameter from the Parameter_Names table. If the id for the passed name is not present
     * in the database, an id is generated, persisted, and returned.
     * @param String A valid FHIR search  parameter name.
     * @return Integer - the id associated with the name of the passed Parameter.
     * @throws FHIRPersistenceDataAccessException
     */
    int readOrAddParameterNameId(String parameterName) throws FHIRPersistenceDataAccessException;
    
    /**
     * Read the parameter_name_id for the given parameterName
     * @param parameterName
     * @return the parameter_name_id for for parameter, or null if it doesn't exist
     * @throws FHIRPersistenceDataAccessException
     */
    Integer readParameterNameId(String parameterName) throws FHIRPersistenceDataAccessException;
            
}
