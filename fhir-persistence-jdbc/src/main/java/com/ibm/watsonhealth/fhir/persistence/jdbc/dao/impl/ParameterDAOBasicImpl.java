/**
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.Properties;
import java.util.logging.Logger;

import com.ibm.watsonhealth.fhir.persistence.jdbc.dao.api.ParameterDAO;
import com.ibm.watsonhealth.fhir.persistence.jdbc.dto.Parameter;
import com.ibm.watsonhealth.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;
import com.ibm.watsonhealth.fhir.persistence.jdbc.exception.FHIRPersistenceDataAccessException;

/**
 * This Data Access Object class provides method implementations for creating, updating, and retrieving rows in the FHIR Parameter table.
 * @author markd
 *
 */
public class ParameterDAOBasicImpl extends FHIRDbDAOBasicImpl<Parameter> implements ParameterDAO {
	
	private static final Logger log = Logger.getLogger(ParameterDAOBasicImpl.class.getName());
	private static final String CLASSNAME = FHIRDbDAOBasicImpl.class.getName(); 
	
	private static final String SQL_INSERT = "INSERT INTO PARAMETER (NAME, RESOURCE_TYPE, VALUE_CODE, VALUE_DATE, VALUE_DATE_END, VALUE_DATE_START, " +
												"VALUE_LATITUDE, VALUE_LONGITUDE, VALUE_NUMBER, VALUE_NUMBER_HIGH, VALUE_NUMBER_LOW, " +
												"VALUE_STRING, VALUE_SYSTEM, RESOURCE_ID) " + 
											    "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	private static final String SQL_DELETE_BY_RESOURCE_ID = "DELETE FROM PARAMETER WHERE RESOURCE_ID = ?";
	
	
	/**
	 * Constructs a DAO instance suitable for acquiring connections from a JDBC Datasource object.
	 */
	public ParameterDAOBasicImpl() {
		super();
	}
	
	/**
	 * Constructs a DAO instance suitable for acquiring connections based on the passed database type specific properties.
	 * @param dbProperties
	 */
	public ParameterDAOBasicImpl(Properties dbProperties) {
		super(dbProperties);
	}
	
	/* (non-Javadoc)
	 * @see com.ibm.watsonhealth.fhir.persistence.jdbc.dao.impl.ParameterDAO#insert(com.ibm.watsonhealth.fhir.persistence.jdbc.dto.Parameter)
	 */
	@Override
	public Parameter insert(Parameter parameter) throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException {
		final String METHODNAME = "insert";
		log.entering(CLASSNAME, METHODNAME);
		
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
				
		try {
			connection = this.getConnection();
			stmt = connection.prepareStatement(SQL_INSERT, PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setString(1, parameter.getName());
			stmt.setString(2, parameter.getResourceType());
			stmt.setString(3, parameter.getValueCode());
			stmt.setTimestamp(4, parameter.getValueDate());
			stmt.setTimestamp(5,  parameter.getValueDateEnd());
			stmt.setTimestamp(6, parameter.getValueDateStart());
			stmt.setObject(7, parameter.getValueLatitude(), Types.DOUBLE);
			stmt.setObject(8, parameter.getValueLongitude(), Types.DOUBLE);
			stmt.setObject(9, parameter.getValueNumber(), Types.DOUBLE);
			stmt.setObject(10, parameter.getValueNumberHigh(), Types.DOUBLE);
			stmt.setObject(11, parameter.getValueNumberLow(), Types.DOUBLE);
			stmt.setString(12, parameter.getValueString());
			stmt.setString(13, parameter.getValueSystem());
			stmt.setLong(14, parameter.getResourceId());
					
			stmt.executeUpdate();
			resultSet = stmt.getGeneratedKeys();
			if (resultSet.next()) {
				parameter.setId(resultSet.getLong(1));
				log.fine("Succesfully inserted Parameter. id=" + parameter.getId());
			}
			else
			{
				throw new FHIRPersistenceDataAccessException("Generated key not returned after new Parameter inserted.");
			}
		}
		catch(FHIRPersistenceDataAccessException | FHIRPersistenceDBConnectException e) {
			throw e;
		}
		catch(Throwable e) {
			throw new FHIRPersistenceDataAccessException("Failure inserting Resource.", e);
		}
		finally {
			this.cleanup(resultSet, stmt, connection);
			log.exiting(CLASSNAME, METHODNAME);
		}
		
		return parameter;
	}
	
	/* (non-Javadoc)
	 * @see com.ibm.watsonhealth.fhir.persistence.jdbc.dao.impl.ParameterDAO#deleteByResource(long)
	 */
	@Override
	public void deleteByResource(long resourceId) throws FHIRPersistenceDBConnectException, FHIRPersistenceDataAccessException {
		final String METHODNAME = "delete";
		log.entering(CLASSNAME, METHODNAME);
		
		Connection connection = null;
		PreparedStatement stmt = null;
						
		try {
			connection = this.getConnection();
			stmt = connection.prepareStatement(SQL_DELETE_BY_RESOURCE_ID);
			stmt.setLong(1, resourceId);
					
			stmt.executeUpdate();
			log.fine("Successfully delete Parameters for Resource id=" + resourceId);
		}
		catch(FHIRPersistenceDBConnectException e) {
			throw e;
		}
		catch(Throwable e) {
			throw new FHIRPersistenceDataAccessException("Failure deleting Paramaters for Resource id=" + resourceId, e);
		}
		finally {
			this.cleanup(stmt, connection);
			log.exiting(CLASSNAME, METHODNAME);
		}
		
	}

}
