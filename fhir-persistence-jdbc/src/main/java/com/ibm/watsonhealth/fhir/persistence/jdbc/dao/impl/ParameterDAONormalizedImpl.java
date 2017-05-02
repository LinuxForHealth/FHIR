/**
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.watsonhealth.fhir.persistence.jdbc.dao.api.ParameterNormalizedDAO;
import com.ibm.watsonhealth.fhir.persistence.jdbc.dto.Parameter;
import com.ibm.watsonhealth.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;
import com.ibm.watsonhealth.fhir.persistence.jdbc.exception.FHIRPersistenceDataAccessException;
import com.ibm.watsonhealth.fhir.persistence.jdbc.util.CodeSystemsCache;
import com.ibm.watsonhealth.fhir.persistence.jdbc.util.ParameterNamesCache;
import com.ibm.watsonhealth.fhir.persistence.util.AbstractQueryBuilder;
import com.ibm.watsonhealth.fhir.search.Parameter.Type;

/**
 * This Data Access Object extends the "basic" implementation to provide functionality specific to the "normalized"
 * relational schema.
 * @author markd
 *
 */
public class ParameterDAONormalizedImpl extends FHIRDbDAOBasicImpl<Parameter> implements ParameterNormalizedDAO {
	private static final Logger log = Logger.getLogger(ParameterDAONormalizedImpl.class.getName());
	private static final String CLASSNAME = ParameterDAONormalizedImpl.class.getName(); 
	
	public static final String DEFAULT_TOKEN_SYSTEM = "default-token-system";
	
	private static final String SQL_INSERT = "INSERT INTO PARAMETERS_GTT (PARAMETER_NAME_ID, PARAMETER_TYPE, STR_VALUE, DATE_VALUE, DATE_START, DATE_END, " +
			"NUMBER_VALUE, NUMBER_VALUE_LOW, NUMBER_VALUE_HIGH, LATITUDE_VALUE, LONGITUDE_VALUE, TOKEN_VALUE, CODE_SYSTEM_ID, CODE) " +
			"VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	private static final String SQL_READ_ALL_SEARCH_PARAMETER_NAMES = "SELECT PARAMETER_NAME_ID, PARAMETER_NAME FROM PARAMETER_NAMES";
	
	private static final String SQL_READ_ALL_CODE_SYSTEMS = "SELECT CODE_SYSTEM_NAME, CODE_SYSTEM_ID FROM CODE_SYSTEMS";
	
	private static final String SQL_READ_PARAMETER_NAME = "CALL %s.add_parameter_name(?, ?)";
	
	private static final String SQL_READ_CODE_SYSTEM_ID = "CALL %s.add_code_system(?, ?)";
	
	
	/**
	 * Constructs a DAO instance suitable for acquiring connections from a JDBC Datasource object.
	 */
	public ParameterDAONormalizedImpl() {
		super();
	}
	
	/**
	 * Constructs a DAO instance suitable for acquiring connections based on the passed database type specific properties.
	 * @param dbProperties
	 */
	public ParameterDAONormalizedImpl(Properties dbProperties) {
		super(dbProperties);
	}

	/* (non-Javadoc)
	 * @see com.ibm.watsonhealth.fhir.persistence.jdbc.dao.api.ParameterDAO#insert(java.util.List)
	 */
	@Override
	public void insert(List<Parameter> parameters)
					throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException {
		final String METHODNAME = "insert";
		log.entering(CLASSNAME, METHODNAME);
		
		Connection connection = null;
		PreparedStatement stmt = null;
		String tokenSystem;
						
		try {
			connection = this.getConnection();
			stmt = connection.prepareStatement(SQL_INSERT);
			
			for (Parameter parameter: parameters) {
				stmt.setInt(1, ParameterNamesCache.getParameterNameId(parameter.getName(), this));
				stmt.setString(2, String.valueOf(this.determineParameterTypeChar(parameter)));
				stmt.setString(3, parameter.getValueString());
				stmt.setTimestamp(4, parameter.getValueDate());
				stmt.setTimestamp(5, parameter.getValueDateStart());
				stmt.setTimestamp(6, parameter.getValueDateEnd());
				stmt.setObject(7, parameter.getValueNumber(), Types.DOUBLE);
				stmt.setObject(8, parameter.getValueNumberLow(), Types.DOUBLE);
				stmt.setObject(9, parameter.getValueNumberHigh(), Types.DOUBLE);
				stmt.setObject(10, parameter.getValueLatitude(), Types.DOUBLE);
				stmt.setObject(11, parameter.getValueLongitude(), Types.DOUBLE);
				stmt.setString(12, parameter.getValueCode());
				tokenSystem = parameter.getValueSystem();
				if ((parameter.getType().equals(Type.TOKEN) || parameter.getType().equals(Type.QUANTITY)) &&
					(tokenSystem == null || tokenSystem.isEmpty())) {
						tokenSystem = DEFAULT_TOKEN_SYSTEM;
				}
				if(tokenSystem != null) {
					stmt.setInt(13, CodeSystemsCache.getCodeSystemId(tokenSystem, this));
				}
				else {
					stmt.setObject(13, null, Types.INTEGER);
				}
				stmt.setString(14, parameter.getValueCode());
				stmt.addBatch();
			}
			
			stmt.executeBatch();
			
		}
		catch(FHIRPersistenceDBConnectException e) {
			throw e;
		}
		catch(Throwable e) {
			throw new FHIRPersistenceDataAccessException("Failure inserting Parameter batch.", e);
		}
		finally {
			this.cleanup(stmt, connection);
			log.exiting(CLASSNAME, METHODNAME);
		}

	}

	@Override
	public Map<String, Integer> readAllSearchParameterNames()
										 throws FHIRPersistenceDBConnectException, FHIRPersistenceDataAccessException {
		final String METHODNAME = "readAllSearchParameterNames";
		log.entering(CLASSNAME, METHODNAME);
				
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		String parameterName;
		int parameterId;
		Map<String, Integer> parameterMap = new HashMap<>();
		String errMsg = "Failure retrieving all Search Parameter names.";
				
		try {
			connection = this.getConnection();
			stmt = connection.prepareStatement(SQL_READ_ALL_SEARCH_PARAMETER_NAMES);
			resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				parameterName = resultSet.getString("PARAMETER_NAME");
				parameterId = resultSet.getInt("PARAMETER_ID");
				parameterMap.put(parameterName, parameterId);
			}
		}
		catch (Throwable e) {
			throw new FHIRPersistenceDataAccessException(errMsg,e);
		}
		finally {
			this.cleanup(stmt, connection);
			log.exiting(CLASSNAME, METHODNAME);
		}
				
		return parameterMap;
	}
	
	@Override
	public Map<String, Integer> readAllCodeSystems()
			throws FHIRPersistenceDBConnectException, FHIRPersistenceDataAccessException {
		final String METHODNAME = "readAllCodeSystems";
		log.entering(CLASSNAME, METHODNAME);
				
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		String systemName;
		int systemId;
		Map<String, Integer> systemMap = new HashMap<>();
		String errMsg = "Failure retrieving all code systems.";
				
		try {
			connection = this.getConnection();
			stmt = connection.prepareStatement(SQL_READ_ALL_CODE_SYSTEMS);
			resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				systemName = resultSet.getString("CODE_SYSTEM_NAME");
				systemId = resultSet.getInt("CODE_SYSTEM_ID");
				systemMap.put(systemName, systemId);
			}
		}
		catch (Throwable e) {
			throw new FHIRPersistenceDataAccessException(errMsg,e);
		}
		finally {
			this.cleanup(stmt, connection);
			log.exiting(CLASSNAME, METHODNAME);
		}
				
		return systemMap;
	}
	
	/**
	 * Examines the type of the passed parameter and maps that enumerated Type to a char that can then be persisted
	 * in one of the parameter values tables.
	 * @param parameter A search Parameter containing a valid Type.
	 * @return char - A character indicating the search parameter type that can be persisted.
	 */
	private char determineParameterTypeChar(Parameter parameter) {
		final String METHODNAME = "determineParameterTypeChar";
		log.entering(CLASSNAME, METHODNAME);
		
		char returnChar = 0;
		if(AbstractQueryBuilder.NEAR.equals(parameter.getName()) || 
		   AbstractQueryBuilder.NEAR_DISTANCE.equals(parameter.getName())) {
			returnChar = 'G';
		}
		else {
			switch(parameter.getType()) {
				case REFERENCE :
				case URI: 
				case STRING : returnChar = 'S';
							  break;
							  
				case TOKEN :  returnChar = 'C';
							  break;
				
				case QUANTITY : if (parameter.getValueNumberHigh() != null || parameter.getValueNumberLow() != null) {
								  returnChar = 'Z';	
								}
								else {
								  returnChar = 'Q';
								}
							  break;
							  
				case NUMBER : if (parameter.getValueNumberHigh() != null || parameter.getValueNumberLow() != null) {
							  	returnChar = 'R';
							  } else {
								returnChar = 'N';
							    }
							  break;
					
				case DATE : if (parameter.getValueDateStart() != null || parameter.getValueDateEnd() != null) {
								returnChar = 'P';
							} else {
								returnChar = 'D';
							  }
				 		    break;
			}
		}
		
		log.exiting(CLASSNAME, METHODNAME);
		return returnChar;
	}
	
	/**
	 * Calls a stored procedure to read the name contained in the passed Parameter in the Parameter_Names table.
	 * If it's not in the DB, it will be stored and a unique id will be returned.
	 * @param parameter
	 * @return Integer - The generated id of the stored system.
	 * @throws FHIRPersistenceDBConnectException 
	 * @throws FHIRPersistenceDataAccessException 
	 *  
	 */
	@Override
	public Integer readParameterNameId(String parameterName) throws FHIRPersistenceDBConnectException, FHIRPersistenceDataAccessException  {
		final String METHODNAME = "storeParameterNameId";
		log.entering(CLASSNAME, METHODNAME);
		
		Connection connection = null;
		CallableStatement stmt = null;
		Integer parameterNameId = null;
		String currentSchema;
		String stmtString;
		String errMsg = "Failure storing search parameter name id: name=" + parameterName;
				
		try {
			connection = this.getConnection();
			currentSchema = connection.getSchema().trim();
			stmtString = String.format(SQL_READ_PARAMETER_NAME, currentSchema);
			stmt = connection.prepareCall(stmtString); 
			stmt.setString(1, parameterName);
			stmt.registerOutParameter(2, Types.INTEGER);
			stmt.execute();
			parameterNameId = stmt.getInt(2);
		}
		catch(FHIRPersistenceDBConnectException e) {
			throw e;
		}
		catch (Throwable e) {
			throw new FHIRPersistenceDataAccessException(errMsg,e);
		} 
		finally {
			this.cleanup(stmt, connection);
			log.exiting(CLASSNAME, METHODNAME);
		}
		return parameterNameId;
	}
	
	/**
	 * Calls a stored procedure to read the system contained in the passed Parameter in the Code_Systems table.
	 * If it's not in the DB, it will be stored and a unique id will be returned.
	 * @param parameter
	 * @return Integer - The generated id of the stored system.
	 * @throws FHIRPersistenceDBConnectException 
	 * @throws FHIRPersistenceDataAccessException 
	 * @throws FHIRPersistenceException
	 */
	@Override
	public Integer readCodeSystemId(String systemName) throws FHIRPersistenceDBConnectException, FHIRPersistenceDataAccessException   {
		final String METHODNAME = "storeCodeSystemId";
		log.entering(CLASSNAME, METHODNAME);
		
		Connection connection = null;
		CallableStatement stmt = null;
		Integer systemId = null;
		String currentSchema;
		String stmtString;
		String errMsg = "Failure storing system id: name=" + systemName;
				
		try {
			connection = this.getConnection();
			currentSchema = connection.getSchema().trim();
			stmtString = String.format(SQL_READ_CODE_SYSTEM_ID, currentSchema);
			stmt = connection.prepareCall(stmtString); 
			stmt.setString(1, systemName);
			stmt.registerOutParameter(2, Types.INTEGER);
			stmt.execute();
			systemId = stmt.getInt(2);
		}
		catch(FHIRPersistenceDBConnectException e) {
			throw e;
		}
		catch (Throwable e) {
			throw new FHIRPersistenceDataAccessException(errMsg,e);
		} 
		finally {
			this.cleanup(stmt, connection);
			log.exiting(CLASSNAME, METHODNAME);
		}
		return systemId;
	}
	 
}
