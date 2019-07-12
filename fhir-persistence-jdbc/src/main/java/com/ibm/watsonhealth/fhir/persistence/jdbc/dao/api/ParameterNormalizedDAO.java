/**
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc.dao.api;

import java.sql.Array;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceException;
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

    
    /**
     * Acquire and return the id associated with the passed parameter name.
     * @param parameterName The name of a valid FHIR search parameter.
     * @return Integer A parameter id.
     * @throws FHIRPersistenceException
     */
    Integer acquireParameterNameId(String parameterName) throws FHIRPersistenceException;

    /**
     * Acquire and return the id associated with the passed code-system name.
     * @param codeSystemName The name of a valid code-system.
     * @return Integer A code-system id.
     * @throws FHIRPersistenceException
     */
    public Integer acquireCodeSystemId(String codeSystemName) throws FHIRPersistenceException;
    
    /**
     * Adds a code system name / code system id pair to a candidate collection for population into the CodeSystemsCache.
     * This pair must be present as a row in the FHIR DB CODE_SYSTEMS table.
     * @param codeSystemName A valid code system name.
     * @param codeSystemId The id corresponding to the code system name.
     * @throws FHIRPersistenceException
     */
    void addCodeSystemsCacheCandidate(String codeSystemName, Integer codeSystemId) throws FHIRPersistenceException;    
    
    /**
     * Adds a parameter name / parameter id pair to a candidate collection for population into the ParameterNamesCache.
     * This pair must be present as a row in the FHIR DB PARAMETER_NAMES table.
     * @param parameterName A valid search or sort parameter name.
     * @param parameterId The id corresponding to the parameter name.
     * @throws FHIRPersistenceException
     */
    void addParameterNamesCacheCandidate(String parameterName, Integer parameterId) throws FHIRPersistenceException;
    
    /**
     * Extracts String type FHIR search parameters from the passed collection and creates an SQL array of those parameters and their values.
     * @param connection A connection to the FHIR database.
     * @param schemaName The current schema name.
     * @param parameters A collection of FHIR search parameters.
     * @return An SQL Array containing rows of parameter names and values.
     * @throws FHIRPersistenceException
     */
    Array transformStringParameters(Connection connection, String schemaName, List<Parameter> parameters) throws FHIRPersistenceException;
    
    
    /**
     * Extracts Number type FHIR search parameters from the passed collection and creates an SQL array of those parameters and their values.
     * @param connection A connection to the FHIR database.
     * @param schemaName The current schema name.
     * @param parameters A collection of FHIR search parameters.
     * @return An SQL Array containing rows of parameter names and values.
     * @throws FHIRPersistenceException
     */
    Array transformNumberParameters(Connection connection, String schemaName, List<Parameter> parameters) throws FHIRPersistenceException;
    
    
    /**
     * Extracts Date type FHIR search parameters from the passed collection and creates an SQL array of those parameters and their values.
     * @param connection A connection to the FHIR database.
     * @param schemaName The current schema name.
     * @param parameters A collection of FHIR search parameters.
     * @return An SQL Array containing rows of parameter names and values.
     * @throws FHIRPersistenceException
     */
    Array transformDateParameters(Connection connection, String schemaName, List<Parameter> parameters) throws FHIRPersistenceException;
    
    /**
     * Extracts Latitude/Longitude type FHIR search parameters from the passed collection and creates an SQL array of those parameters and their values.
     * @param connection A connection to the FHIR database.
     * @param schemaName The current schema name.
     * @param parameters A collection of FHIR search parameters.
     * @return An SQL Array containing rows of parameter names and values.
     * @throws FHIRPersistenceException
     */
    Array transformLatLongParameters(Connection connection, String schemaName, List<Parameter> parameters) throws FHIRPersistenceException;
    
    /**
     * Extracts Token type FHIR search parameters from the passed collection and creates an SQL array of those parameters and their values.
     * @param connection A connection to the FHIR database.
     * @param schemaName The current schema name.
     * @param parameters A collection of FHIR search parameters.
     * @return An SQL Array containing rows of parameter names and values.
     * @throws FHIRPersistenceException
     */
    Array transformTokenParameters(Connection connection, String schemaName, List<Parameter> parameters) throws FHIRPersistenceException;
    
    /**
     * Extracts Quantity type FHIR search parameters from the passed collection and creates an SQL array of those parameters and their values.
     * @param connection A connection to the FHIR database.
     * @param schemaName The current schema name.
     * @param parameters A collection of FHIR search parameters.
     * @return An SQL Array containing rows of parameter names and values.
     * @throws FHIRPersistenceException
     */
    Array transformQuantityParameters(Connection connection, String schemaName, List<Parameter> parameters) throws FHIRPersistenceException;
}
