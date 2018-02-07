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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.transaction.TransactionSynchronizationRegistry;
import javax.ws.rs.core.Response.Status;

import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.watsonhealth.fhir.persistence.jdbc.dao.api.ParameterNormalizedDAO;
import com.ibm.watsonhealth.fhir.persistence.jdbc.dto.Parameter;
import com.ibm.watsonhealth.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;
import com.ibm.watsonhealth.fhir.persistence.jdbc.exception.FHIRPersistenceDataAccessException;
import com.ibm.watsonhealth.fhir.persistence.jdbc.util.CodeSystemsCache;
import com.ibm.watsonhealth.fhir.persistence.jdbc.util.CodeSystemsCacheUpdater;
import com.ibm.watsonhealth.fhir.persistence.jdbc.util.ParameterNamesCache;
import com.ibm.watsonhealth.fhir.persistence.jdbc.util.ParameterNamesCacheUpdater;
import com.ibm.watsonhealth.fhir.persistence.jdbc.util.SQLParameterEncoder;
import com.ibm.watsonhealth.fhir.persistence.util.AbstractQueryBuilder;
import com.ibm.watsonhealth.fhir.search.Parameter.Type;
import com.ibm.watsonhealth.fhir.search.util.SearchUtil;

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
	
	private static final String SQL_INSERT = "INSERT INTO PARAMETERS_GTT (PARAMETER_NAME_ID, PARAMETER_TYPE, STR_VALUE, STR_VALUE_LCASE, DATE_VALUE, DATE_START, DATE_END, " +
			"NUMBER_VALUE, NUMBER_VALUE_LOW, NUMBER_VALUE_HIGH, LATITUDE_VALUE, LONGITUDE_VALUE, TOKEN_VALUE, CODE_SYSTEM_ID, CODE) " +
			"VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	private static final String SQL_READ_ALL_SEARCH_PARAMETER_NAMES = "SELECT PARAMETER_NAME_ID, PARAMETER_NAME FROM PARAMETER_NAMES";
	
	private static final String SQL_READ_ALL_CODE_SYSTEMS = "SELECT CODE_SYSTEM_NAME, CODE_SYSTEM_ID FROM CODE_SYSTEMS";
	
	private static final String SQL_READ_PARAMETER_NAME = "CALL %s.add_parameter_name(?, ?)";
	
	private static final String SQL_READ_CODE_SYSTEM_ID = "CALL %s.add_code_system(?, ?)";
	
	private Map<String, Integer> newParameterNameIds = new HashMap<>();
	    
	private Map<String, Integer> newCodeSystemIds = new HashMap<>();
	
	private boolean runningInTrx = false;
    private CodeSystemsCacheUpdater csCacheUpdater = null;
    private ParameterNamesCacheUpdater pnCacheUpdater = null;
    private TransactionSynchronizationRegistry trxSynchRegistry;
	
	
	/**
	 * Constructs a DAO instance suitable for acquiring connections from a JDBC Datasource object.
	 */
	public ParameterDAONormalizedImpl(TransactionSynchronizationRegistry trxSynchRegistry) {
		super();
		this.runningInTrx = true;
		this.trxSynchRegistry = trxSynchRegistry;
	}
	
	/**
	 * Constructs a DAO using the passed externally managed database connection.
	 * The connection used by this instance for all DB operations will be the passed connection.
	 * @param Connection - A database connection that will be managed by the caller.
	 */
	public ParameterDAONormalizedImpl(Connection managedConnection) {
		super(managedConnection);
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
		String parameterName;
        Integer parameterId;
		String tokenSystem;
		Integer tokenSystemId;
		boolean acquiredFromCache;
						
		try {
			connection = this.getConnection();
			stmt = connection.prepareStatement(SQL_INSERT);
			
			for (Parameter parameter: parameters) {
			    parameterName = parameter.getName();
			    parameterId = ParameterNamesCache.getParameterNameId(parameterName);
                if (parameterId == null) {
                    acquiredFromCache = false;
                    parameterId = this.readParameterNameId(parameterName);
                    this.addParameterNamesCacheCandidate(parameterName, parameterId);
                }
                else {
                    acquiredFromCache = true;
                }
                if (log.isLoggable(Level.FINE)) {
                    log.fine("paramenterName=" + parameterName + "  parameterId=" + parameterId + 
                              "  acquiredFromCache=" + acquiredFromCache + "  tenantDatastoreCacheName=" + ParameterNamesCache.getCacheNameForTenantDatastore());
                }
                stmt.setInt(1, parameterId);
				stmt.setString(2, String.valueOf(this.determineParameterTypeChar(parameter)));
				stmt.setString(3, parameter.getValueString());
				stmt.setString(4, SearchUtil.normalizeForSearch(parameter.getValueString()));
				stmt.setTimestamp(5, parameter.getValueDate());
				stmt.setTimestamp(6, parameter.getValueDateStart());
				stmt.setTimestamp(7, parameter.getValueDateEnd());
				stmt.setObject(8, parameter.getValueNumber(), Types.DOUBLE);
				stmt.setObject(9, parameter.getValueNumberLow(), Types.DOUBLE);
				stmt.setObject(10, parameter.getValueNumberHigh(), Types.DOUBLE);
				stmt.setObject(11, parameter.getValueLatitude(), Types.DOUBLE);
				stmt.setObject(12, parameter.getValueLongitude(), Types.DOUBLE);
				stmt.setString(13, parameter.getValueCode());
				tokenSystem = parameter.getValueSystem();
				if ((parameter.getType().equals(Type.TOKEN) || parameter.getType().equals(Type.QUANTITY)) &&
					(tokenSystem == null || tokenSystem.isEmpty())) {
						tokenSystem = DEFAULT_TOKEN_SYSTEM;
				}
				if(tokenSystem != null) {
				    tokenSystemId = CodeSystemsCache.getCodeSystemId(tokenSystem);
                    if (tokenSystemId == null) {
                        acquiredFromCache = false;
                        tokenSystem = SQLParameterEncoder.encode(tokenSystem);
                        tokenSystemId = this.readCodeSystemId(tokenSystem);
                        this.addCodeSystemsCacheCandidate(tokenSystem, tokenSystemId);
                    }
                    else {
                        acquiredFromCache = true;
                    }
                    stmt.setInt(14, tokenSystemId);
                    if (log.isLoggable(Level.FINE)) {
                        log.fine("tokenSystem=" + tokenSystem + "  tokenSystemId=" + tokenSystemId + 
                                  "  acquiredFromCache=" + acquiredFromCache + "  tenantDatastoreCacheName=" + CodeSystemsCache.getCacheNameForTenantDatastore());
                    }
                }
				else {
					stmt.setObject(14, null, Types.INTEGER);
				}
				stmt.setString(15, parameter.getValueCode());
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
				parameterId = resultSet.getInt("PARAMETER_NAME_ID");
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
				case STRING : 	returnChar = 'S';
							  	break;
							  
				case TOKEN :  	returnChar = 'C';
							  	break;
				
				case QUANTITY : returnChar = 'Q';  
							    break;
							  
				case NUMBER : 	returnChar = 'N';
								break;
					
				case DATE : 	returnChar = 'D'; 
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

	/**
     * Adds a code system name / code system id pair to a candidate collection for population into the CodeSystemsCache.
     * This pair must be present as a row in the FHIR DB CODE_SYSTEMS table.
     * @param codeSystemName A valid code system name.
     * @param codeSystemId The id corresponding to the code system name.
     * @throws FHIRPersistenceException
     */
    @Override
    public void addCodeSystemsCacheCandidate(String codeSystemName, Integer codeSystemId)  throws FHIRPersistenceException {
        final String METHODNAME = "addCodeSystemsCacheCandidate";
        log.entering(CLASSNAME, METHODNAME);
        
        if (this.runningInTrx && CodeSystemsCache.isEnabled()) {
            if (this.csCacheUpdater == null) {
                // Register a new CodeSystemsCacheUpdater for this thread/trx, if one hasn't been already registered.
                this.csCacheUpdater = new CodeSystemsCacheUpdater(CodeSystemsCache.getCacheNameForTenantDatastore(), this.newCodeSystemIds);
                try {
                    trxSynchRegistry.registerInterposedSynchronization(csCacheUpdater);
                    log.fine("Registered CodeSystemsCacheUpdater.");
                }
                catch(Throwable e) {
                    throw new FHIRPersistenceException("Failure registering CodeSystemsCacheUpdater", Status.INTERNAL_SERVER_ERROR, e);
                }
            }
            this.newCodeSystemIds.put(codeSystemName, codeSystemId);
        }
        
        log.exiting(CLASSNAME, METHODNAME);
        
    }

    /**
     * Adds a parameter name / parameter id pair to a candidate collection for population into the ParameterNamesCache.
     * This pair must be present as a row in the FHIR DB PARAMETER_NAMES table.
     * @param parameterName A valid search or sort parameter name.
     * @param parameterId The id corresponding to the parameter name.
     * @throws FHIRPersistenceException
     */
    @Override
    public void addParameterNamesCacheCandidate(String parameterName, Integer parameterId) throws FHIRPersistenceException {
        final String METHODNAME = "addParameterNamesCacheCandidate";
        log.entering(CLASSNAME, METHODNAME);
        
        if (this.runningInTrx && ParameterNamesCache.isEnabled()) {
            if (this.pnCacheUpdater == null) {
                // Register a new ParameterNamesCacheUpdater for this thread/trx, if one hasn't been already registered.
                this.pnCacheUpdater = new ParameterNamesCacheUpdater(ParameterNamesCache.getCacheNameForTenantDatastore(), this.newParameterNameIds);
                try {
                    trxSynchRegistry.registerInterposedSynchronization(pnCacheUpdater);
                    log.fine("Registered ParameterNamesCacheUpdater.");
                }
                catch(Throwable e) {
                    throw new FHIRPersistenceException("Failure registering ParameterNamesCacheUpdater", Status.INTERNAL_SERVER_ERROR, e);
                }
            }
            this.newParameterNameIds.put(parameterName, parameterId);
        }
        
        log.exiting(CLASSNAME, METHODNAME);
        
    }
	 
}
