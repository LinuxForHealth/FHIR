/**
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.watsonhealth.fhir.persistence.jdbc.dao.api.ParameterNormalizedDAO;
import com.ibm.watsonhealth.fhir.persistence.jdbc.dto.Parameter;
import com.ibm.watsonhealth.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;
import com.ibm.watsonhealth.fhir.persistence.jdbc.exception.FHIRPersistenceDataAccessException;
import com.ibm.watsonhealth.fhir.persistence.jdbc.util.ReferenceDataCache;
import com.ibm.watsonhealth.fhir.search.Parameter.Type;

/**
 * This Data Access Object extends the "basic" implementation to provide functionality specific to the "normalized"
 * relational schema.
 * @author markd
 *
 */
public class ParameterDAONormalizedImpl extends ParameterDAOBasicImpl implements ParameterNormalizedDAO {
	private static final Logger log = Logger.getLogger(ParameterDAONormalizedImpl.class.getName());
	private static final String CLASSNAME = ParameterDAONormalizedImpl.class.getName(); 
	
	private final String SQL_READ_ALL_PARAMETER_NAMES = "SELECT ID, NAME FROM PARAMETER_NAME";
	
	private final String SQL_INSERT_STRING = 
			"INSERT INTO PARAMETER_STRING (PARAMETER_NAME_ID,VALUE_STRING,RESOURCE_ID) " + 
		    "VALUES(?,?,?)";
	
	private final String SQL_INSERT_REFERENCE = 
			"INSERT INTO PARAMETER_REFERENCE (PARAMETER_NAME_ID,VALUE_REFERENCE,RESOURCE_ID) " + 
		    "VALUES(?,?,?)";
	
	private final String SQL_INSERT_URI = 
			"INSERT INTO PARAMETER_URI (PARAMETER_NAME_ID,VALUE_URI,RESOURCE_ID) " + 
		    "VALUES(?,?,?)";

	private final String SQL_INSERT_TOKEN = 
			"INSERT INTO PARAMETER_TOKEN (PARAMETER_NAME_ID,VALUE_SYSTEM,VALUE_CODE,RESOURCE_ID) " + 
		    "VALUES(?,?,?,?)";
	
	private final String SQL_INSERT_LAT_LONG = 
			"INSERT INTO PARAMETER_LAT_LONG (PARAMETER_NAME_ID,VALUE_LATITUDE,VALUE_LONGITUDE,RESOURCE_ID) " + 
		    "VALUES(?,?,?,?)";
	
	private final String SQL_INSERT_NUMBER = 
			"INSERT INTO PARAMETER_NUMBER (PARAMETER_NAME_ID,VALUE_NUMBER,RESOURCE_ID) " + 
		    "VALUES(?,?,?)";
	
	private final String SQL_INSERT_NUMBER_RANGE = 
			"INSERT INTO PARAMETER_NUMBER_RANGE (PARAMETER_NAME_ID,VALUE_NUMBER_LOW,VALUE_NUMBER_HIGH,RESOURCE_ID) " + 
		    "VALUES(?,?,?,?)";
	
	private final String SQL_INSERT_QUANTITY = 
			"INSERT INTO PARAMETER_QUANTITY (PARAMETER_NAME_ID,VALUE_SYSTEM,VALUE_CODE,VALUE_NUMBER,RESOURCE_ID) " + 
		    "VALUES(?,?,?,?,?)";
	
	private final String SQL_INSERT_QUANTITY_RANGE = 
			"INSERT INTO PARAMETER_QUANTITY_RANGE (PARAMETER_NAME_ID,VALUE_SYSTEM,VALUE_CODE,VALUE_NUMBER_LOW,VALUE_NUMBER_HIGH,RESOURCE_ID) " + 
		    "VALUES(?,?,?,?,?,?)";
	
	private final String SQL_INSERT_DATE = "INSERT INTO PARAMETER_DATE (PARAMETER_NAME_ID,VALUE_DATE,RESOURCE_ID) " + 
		    "VALUES(?,?,?)";
	
	private final String SQL_INSERT_DATE_RANGE = 
			"INSERT INTO PARAMETER_DATE_RANGE (PARAMETER_NAME_ID,VALUE_DATE_START,VALUE_DATE_END,RESOURCE_ID) " + 
		    "VALUES(?,?,?,?)";
	
	
	private final String SQL_DELETE_DATE = "DELETE FROM PARAMETER_DATE WHERE RESOURCE_ID= ?";
	private final String SQL_DELETE_DATE_RANGE = "DELETE FROM PARAMETER_DATE_RANGE WHERE RESOURCE_ID= ?";
	private final String SQL_DELETE_LAT_LONG = "DELETE FROM PARAMETER_LAT_LONG WHERE RESOURCE_ID= ?";
	private final String SQL_DELETE_NUMBER = "DELETE FROM PARAMETER_NUMBER WHERE RESOURCE_ID= ?";
	private final String SQL_DELETE_NUMBER_RANGE = "DELETE FROM PARAMETER_NUMBER_RANGE WHERE RESOURCE_ID= ?";
	private final String SQL_DELETE_QUANTITY = "DELETE FROM PARAMETER_QUANTITY WHERE RESOURCE_ID= ?";
	private final String SQL_DELETE_QUANTITY_RANGE = "DELETE FROM PARAMETER_QUANTITY_RANGE WHERE RESOURCE_ID= ?";
	private final String SQL_DELETE_REFERENCE = "DELETE FROM PARAMETER_REFERENCE WHERE RESOURCE_ID= ?";
	private final String SQL_DELETE_STRING = "DELETE FROM PARAMETER_STRING WHERE RESOURCE_ID= ?";
	private final String SQL_DELETE_TOKEN = "DELETE FROM PARAMETER_TOKEN WHERE RESOURCE_ID= ?";
	private final String SQL_DELETE_URI = "DELETE FROM PARAMETER_URI WHERE RESOURCE_ID= ?";
	
	private List<String> deleteStmts = new ArrayList<>(Arrays.asList(new String[] 
						{SQL_DELETE_DATE,SQL_DELETE_DATE_RANGE,SQL_DELETE_LAT_LONG,SQL_DELETE_NUMBER,SQL_DELETE_NUMBER_RANGE,
						 SQL_DELETE_QUANTITY,SQL_DELETE_QUANTITY_RANGE,SQL_DELETE_REFERENCE,SQL_DELETE_STRING,
						 SQL_DELETE_TOKEN,SQL_DELETE_URI}));
	
	
	private PreparedStatement insertStringStmt = null;
	private PreparedStatement insertRefStmt = null;
	private PreparedStatement insertDateStmt = null;
	private PreparedStatement insertDateRangeStmt = null;
	private PreparedStatement insertTokenStmt = null;
	private PreparedStatement insertLatLongStmt = null;
	private PreparedStatement insertNumberStmt = null;
	private PreparedStatement insertNumberRangeStmt = null;
	private PreparedStatement insertQuantityStmt = null;
	private PreparedStatement insertQuantityRangeStmt = null;
	private PreparedStatement insertUriStmt = null;
	
	private Set<PreparedStatement> executableStmts = new HashSet<>();

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
		Type type;
		
				
		try {
			connection = this.getConnection();
			for (Parameter parameter: parameters) {
				// Handle these special case Location related search parameters.
				if (parameter.getName().startsWith("Location-near")) {
					this.processLatLong(parameter, connection);
				}
				else {
					// Dispatch the insertion to the appropriate method based on the search parameter type.
					type = parameter.getType();
					switch(type) {
					case STRING:		this.processString(parameter, connection); 
						    break;
					case REFERENCE:  	this.processReference(parameter, connection);
							break;
					case DATE:       	this.processDate(parameter, connection);
					        break;
					case TOKEN:      	this.processToken(parameter, connection);
							break;
					case NUMBER:     	this.processNumber(parameter, connection);
							break;
					case QUANTITY:   	this.processQuantity(parameter, connection);
							break;
					case URI:  			this.processUri(parameter, connection);
							break;
					
					default: throw new FHIRPersistenceException("Unexpected parameter type: " + type.value());
					}
				}
			}
			// Batch execute all of the statements affected by the previously executed processXXX(...) methods.
			this.executeStmts();
		}
		catch(FHIRPersistenceDBConnectException e) {
			throw e;
		}
		catch(Throwable e) {
			throw new FHIRPersistenceDataAccessException("Failure inserting Parameter batch.", e);
		}
		finally {
			this.cleanup(null,connection);
			log.exiting(CLASSNAME, METHODNAME);
		}

	}

	/* (non-Javadoc)
	 * @see com.ibm.watsonhealth.fhir.persistence.jdbc.dao.api.ParameterDAO#deleteByResource(long)
	 */
	@Override
	public void deleteByResource(long resourceId)
						throws FHIRPersistenceDBConnectException, FHIRPersistenceDataAccessException {
		final String METHODNAME = "deleteByResource";
		log.entering(CLASSNAME, METHODNAME);
				
		Connection connection = null;
		PreparedStatement stmt = null;
		
		try {
			connection = this.getConnection();
			// Delete parameter values for the passed resource id in all of the parameter value tables.
			for (String deleteStmt : this.deleteStmts) {
				stmt = connection.prepareStatement(deleteStmt);
				stmt.setLong(1, resourceId);
				stmt.executeUpdate();
				stmt.close();
			}
		}
		catch (Throwable e) {
			throw new FHIRPersistenceDataAccessException("Failure deleting parameter values",e);
		}
		finally {
			this.cleanup(null, connection);
			log.exiting(CLASSNAME, METHODNAME);
		}
		
	}

	/* (non-Javadoc)
	 * @see com.ibm.watsonhealth.fhir.persistence.jdbc.dao.api.ParameterNormalizedDAO#readAllParameterNames()
	 */
	@Override
	public Map<String, Long> readAllParameterNames() throws FHIRPersistenceDBConnectException, FHIRPersistenceDataAccessException {
		final String METHODNAME = "readAllParameterNames";
		log.entering(CLASSNAME, METHODNAME);
		
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		String parameterName;
		Long parameterId;
		Map<String, Long> resourceTypeIdMap = new HashMap<>();
		String errMsg = "Failure retrieving all Parameter names.";
				
		try {
			connection = this.getConnection();
			stmt = connection.prepareStatement(SQL_READ_ALL_PARAMETER_NAMES);
			resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				parameterName = resultSet.getString("NAME");
				parameterId = resultSet.getLong("ID");
				resourceTypeIdMap.put(parameterName, parameterId);
			}
		}
		catch (Throwable e) {
			throw new FHIRPersistenceDataAccessException(errMsg,e);
		}
		finally {
			this.cleanup(stmt, connection);
			log.exiting(CLASSNAME, METHODNAME);
		}
				
		return resourceTypeIdMap;
	}
	
	/**
	 * Processes a string type search parameter, adding the parameter insertion to the appropriate pre-initialized
	 * PreparedStatement.
	 * @param parm - A string type FHIR search parameter.
	 * @param connection - An active connection to the FHIR DB.
	 * @throws SQLException
	 * @throws FHIRPersistenceException
	 */
	private void processString(Parameter parm, Connection connection) throws SQLException, FHIRPersistenceException {
		final String METHODNAME = "processString";
		log.entering(CLASSNAME, METHODNAME, new Object[] {parm.getName()});
		
		long parmNameId;
		
		try {
			if (insertStringStmt == null) {
				insertStringStmt = connection.prepareStatement(SQL_INSERT_STRING);
				executableStmts.add(insertStringStmt);
			}
			parmNameId = ReferenceDataCache.getParameterId(parm.getName());
			insertStringStmt.setLong(1, parmNameId);
			insertStringStmt.setString(2, parm.getValueString());
			insertStringStmt.setLong(3, parm.getResourceId());
			insertStringStmt.addBatch();
		}
		finally {
			log.exiting(CLASSNAME, METHODNAME);
		}
	}
	
	/**
	 * Processes a reference type search parameter, adding the parameter insertion to the appropriate pre-initialized
	 * PreparedStatement.
	 * @param parm - A reference type FHIR search parameter.
	 * @param connection - An active connection to the FHIR DB.
	 * @throws SQLException
	 * @throws FHIRPersistenceException
	 */
	private void processReference(Parameter parm, Connection connection) throws SQLException, FHIRPersistenceException {
		final String METHODNAME = "processReference";
		log.entering(CLASSNAME, METHODNAME, new Object[] {parm.getName()});
		
		long parmNameId;
		
		try {
			if (insertRefStmt == null) {
				insertRefStmt = connection.prepareStatement(SQL_INSERT_REFERENCE);
				executableStmts.add(insertRefStmt);
			}
			parmNameId = ReferenceDataCache.getParameterId(parm.getName());
			insertRefStmt.setLong(1, parmNameId);
			insertRefStmt.setString(2, parm.getValueString());
			insertRefStmt.setLong(3, parm.getResourceId());
			insertRefStmt.addBatch();
		}
		finally {
			log.exiting(CLASSNAME, METHODNAME);
		}
	}
	
	/**
	 * Processes a URI type search parameter, adding the parameter insertion to the appropriate pre-initialized
	 * PreparedStatement.
	 * @param parm - A URI type FHIR search parameter.
	 * @param connection - An active connection to the FHIR DB.
	 * @throws SQLException
	 * @throws FHIRPersistenceException
	 */
	private void processUri(Parameter parm, Connection connection) throws SQLException, FHIRPersistenceException {
		final String METHODNAME = "processUri";
		log.entering(CLASSNAME, METHODNAME, new Object[] {parm.getName()});
		
		long parmNameId;
		
		try {
			if (insertUriStmt == null) {
				insertUriStmt = connection.prepareStatement(SQL_INSERT_URI);
				executableStmts.add(insertUriStmt);
			}
			parmNameId = ReferenceDataCache.getParameterId(parm.getName());
			insertUriStmt.setLong(1, parmNameId);
			insertUriStmt.setString(2, parm.getValueString());
			insertUriStmt.setLong(3, parm.getResourceId());
			insertUriStmt.addBatch();
		}
		finally {
			log.exiting(CLASSNAME, METHODNAME);
		}
	}
	
	/**
	 * Processes a token type search parameter, adding the parameter insertion to the appropriate pre-initialized
	 * PreparedStatement.
	 * @param parm - A token type FHIR search parameter.
	 * @param connection - An active connection to the FHIR DB.
	 * @throws SQLException
	 * @throws FHIRPersistenceException
	 */
	private void processToken(Parameter parm, Connection connection) throws SQLException, FHIRPersistenceException {
		final String METHODNAME = "processToken";
		log.entering(CLASSNAME, METHODNAME, new Object[] {parm.getName()});
		
		long parmNameId;
		
		try {
			if (insertTokenStmt == null) {
				insertTokenStmt = connection.prepareStatement(SQL_INSERT_TOKEN);
				executableStmts.add(insertTokenStmt);
			}
			parmNameId = ReferenceDataCache.getParameterId(parm.getName());
			insertTokenStmt.setLong(1, parmNameId);
			insertTokenStmt.setString(2, parm.getValueSystem());
			insertTokenStmt.setString(3, parm.getValueCode());
			insertTokenStmt.setLong(4, parm.getResourceId());
			insertTokenStmt.addBatch();
		}
		finally {
			log.exiting(CLASSNAME, METHODNAME);
		}
	}
	
	/**
	 * Processes a latitude/longitude search parameter, adding the parameter insertion to the appropriate pre-initialized
	 * PreparedStatement.
	 * @param parm - A Location realated latitude/longitude type FHIR search parameter.
	 * @param connection - An active connection to the FHIR DB.
	 * @throws SQLException
	 * @throws FHIRPersistenceException
	 */
	private void processLatLong(Parameter parm, Connection connection) throws SQLException, FHIRPersistenceException {
		final String METHODNAME = "processLatLong";
		log.entering(CLASSNAME, METHODNAME, new Object[] {parm.getName()});
		
		long parmNameId;
		
		try {
			if (insertLatLongStmt == null) {
				insertLatLongStmt = connection.prepareStatement(SQL_INSERT_LAT_LONG);
				executableStmts.add(insertLatLongStmt);
			}
			parmNameId = ReferenceDataCache.getParameterId(parm.getName());
			insertLatLongStmt.setLong(1, parmNameId);
			insertLatLongStmt.setObject(2, parm.getValueLatitude(), Types.DOUBLE);
			insertLatLongStmt.setObject(3, parm.getValueLongitude(), Types.DOUBLE);
			insertLatLongStmt.setLong(4, parm.getResourceId());
			insertLatLongStmt.addBatch();
		}
		finally {
			log.exiting(CLASSNAME, METHODNAME);
		}
	}
	
	/**
	 * Processes a number type search parameter, adding the parameter insertion to the appropriate pre-initialized
	 * PreparedStatement.
	 * @param parm - A number type FHIR search parameter.
	 * @param connection - An active connection to the FHIR DB.
	 * @throws SQLException
	 * @throws FHIRPersistenceException
	 */
	private void processNumber(Parameter parm, Connection connection) throws SQLException, FHIRPersistenceException {
		final String METHODNAME = "processNumber";
		log.entering(CLASSNAME, METHODNAME, new Object[] {parm.getName()});
		
		long parmNameId;
		
		try {
			parmNameId = ReferenceDataCache.getParameterId(parm.getName());
			if (parm.getValueNumber() != null) {
				if (insertNumberStmt == null) {
					insertNumberStmt = connection.prepareStatement(SQL_INSERT_NUMBER);
					executableStmts.add(insertNumberStmt);
				}
				insertNumberStmt.setLong(1, parmNameId);
				insertNumberStmt.setObject(2, parm.getValueNumber(), Types.DOUBLE);
				insertNumberStmt.setLong(3, parm.getResourceId());
				insertNumberStmt.addBatch();
			}
			if (parm.getValueNumberLow() != null || parm.getValueNumberHigh() != null) {
				if (insertNumberRangeStmt == null) {
					insertNumberRangeStmt = connection.prepareStatement(SQL_INSERT_NUMBER_RANGE);
					executableStmts.add(insertNumberRangeStmt);
				}
				insertNumberRangeStmt.setLong(1, parmNameId);
				insertNumberRangeStmt.setObject(2, parm.getValueNumberLow(), Types.DOUBLE);
				insertNumberRangeStmt.setObject(3, parm.getValueNumberHigh(), Types.DOUBLE);
				insertNumberRangeStmt.setLong(4, parm.getResourceId());
				insertNumberRangeStmt.addBatch();
			}
		}
		finally {
			log.exiting(CLASSNAME, METHODNAME);
		}
	}
	
	/**
	 * Processes a quantity type search parameter, adding the parameter insertion to the appropriate pre-initialized
	 * PreparedStatement.
	 * @param parm - A quantity type FHIR search parameter.
	 * @param connection - An active connection to the FHIR DB.
	 * @throws SQLException
	 * @throws FHIRPersistenceException
	 */
	private void processQuantity(Parameter parm, Connection connection) throws SQLException, FHIRPersistenceException {
		final String METHODNAME = "processNumber";
		log.entering(CLASSNAME, METHODNAME, new Object[] {parm.getName()});
		
		long parmNameId;
		
		try {
			parmNameId = ReferenceDataCache.getParameterId(parm.getName());
			if (parm.getValueNumber() != null) {
				if (insertQuantityStmt == null) {
					insertQuantityStmt = connection.prepareStatement(SQL_INSERT_QUANTITY);
					executableStmts.add(insertQuantityStmt);
				}
				insertQuantityStmt.setLong(1, parmNameId);
				insertQuantityStmt.setString(2, parm.getValueSystem());
				insertQuantityStmt.setString(3, parm.getValueCode());
				insertQuantityStmt.setObject(4, parm.getValueNumber(), Types.DOUBLE);
				insertQuantityStmt.setLong(5, parm.getResourceId());
				insertQuantityStmt.addBatch();
			}
			if (parm.getValueNumberLow() != null || parm.getValueNumberHigh() != null) {
				if (insertQuantityRangeStmt == null) {
					insertQuantityRangeStmt = connection.prepareStatement(SQL_INSERT_QUANTITY_RANGE);
					executableStmts.add(insertQuantityRangeStmt);
				}
				insertQuantityRangeStmt.setLong(1, parmNameId);
				insertQuantityRangeStmt.setString(2, parm.getValueSystem());
				insertQuantityRangeStmt.setString(3, parm.getValueCode());
				insertQuantityRangeStmt.setObject(4, parm.getValueNumberLow(), Types.DOUBLE);
				insertQuantityRangeStmt.setObject(5, parm.getValueNumberHigh(), Types.DOUBLE);
				insertQuantityRangeStmt.setLong(6, parm.getResourceId());
				insertQuantityRangeStmt.addBatch();
			}
		}
		finally {
			log.exiting(CLASSNAME, METHODNAME);
		}
	}
	
	/**
	 * Processes a date type search parameter, adding the parameter insertion to the appropriate pre-initialized
	 * PreparedStatement.
	 * @param parm - A date type FHIR search parameter.
	 * @param connection - An active connection to the FHIR DB.
	 * @throws SQLException
	 * @throws FHIRPersistenceException
	 */
	private void processDate(Parameter parm, Connection connection) throws SQLException, FHIRPersistenceException {
		final String METHODNAME = "processDate";
		log.entering(CLASSNAME, METHODNAME, new Object[] {parm.getName()});
		
		long parmNameId;
		
		try {
			parmNameId = ReferenceDataCache.getParameterId(parm.getName());
			if (parm.getValueDate() != null) {
				if (insertDateStmt == null) {
					insertDateStmt = connection.prepareStatement(SQL_INSERT_DATE);
					executableStmts.add(insertDateStmt);
				}
				insertDateStmt.setLong(1, parmNameId);
				insertDateStmt.setTimestamp(2, parm.getValueDate());
				insertDateStmt.setLong(3, parm.getResourceId());
				insertDateStmt.addBatch();
			}
			if (parm.getValueDateStart() != null || parm.getValueDateEnd() != null) {
				if (insertDateRangeStmt == null) {
					insertDateRangeStmt = connection.prepareStatement(SQL_INSERT_DATE_RANGE);
					executableStmts.add(insertDateRangeStmt);
				}
				insertDateRangeStmt.setLong(1, parmNameId);
				insertDateRangeStmt.setTimestamp(2, parm.getValueDateStart());
				insertDateRangeStmt.setTimestamp(3, parm.getValueDateEnd());
				insertDateRangeStmt.setLong(4, parm.getResourceId());
				insertDateRangeStmt.addBatch();
			}
		}
		finally {
			log.exiting(CLASSNAME, METHODNAME);
		}
	}
	
	/**
	 * Performs a batch execute on each PreparedStatement in the executableStmts collection.
	 * @throws SQLException
	 */
	private void executeStmts() throws SQLException {
		final String METHODNAME = "executeStmts";
		log.entering(CLASSNAME, METHODNAME);
		
		try {
			for(PreparedStatement stmt : this.executableStmts) {
				stmt.executeBatch();
				stmt.close();
			}
			this.executableStmts.clear();
		}
		finally {
			log.exiting(CLASSNAME, METHODNAME);
		}
	}
	
	
	/**
	 * Performs JDBC resource cleanup.
	 */
	@Override
	protected void cleanup(PreparedStatement stmt, Connection connection) {
		final String METHODNAME = "cleanup(PreparedStatement, Connection)";
		log.entering(CLASSNAME, METHODNAME);
		
		this.insertStringStmt = null;
		this.insertRefStmt = null;
		this.insertDateStmt = null;
		this.insertDateRangeStmt = null;
		this.insertTokenStmt = null;
		this.insertLatLongStmt = null;
		this.insertNumberStmt = null;
		this.insertNumberRangeStmt = null;
		this.insertQuantityStmt = null;
		this.insertQuantityRangeStmt = null;
		this.insertUriStmt = null;
				
		super.cleanup(stmt, connection);
		
		log.exiting(CLASSNAME, METHODNAME);
		
	}
	 

}
