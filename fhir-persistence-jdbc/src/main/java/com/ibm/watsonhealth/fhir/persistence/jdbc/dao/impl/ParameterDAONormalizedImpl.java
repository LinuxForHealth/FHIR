/**
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc.dao.impl;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Struct;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.transaction.TransactionSynchronizationRegistry;

import com.ibm.watsonhealth.fhir.model.type.IssueType;
import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.watsonhealth.fhir.persistence.jdbc.dao.api.CodeSystemDAO;
import com.ibm.watsonhealth.fhir.persistence.jdbc.dao.api.ParameterNameDAO;
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
import com.ibm.watsonhealth.fhir.search.SearchConstants.Type;
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
    
    private static final int SQL_INSERT_PARAMETERS_MAX_ARRAY_SIZE_DEFAULT = 512;
    private static final int SQL_INSERT_PARAMETERS_MAX_ARRAY_SIZE_STRINGS = 1024;
    
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
        long dbCallStartTime;
        double dbCallDuration;
                        
        try {
            connection = this.getConnection();
            stmt = connection.prepareStatement(SQL_INSERT);
            
            for (Parameter parameter: parameters) {
                parameterName = parameter.getName();
                parameterId = ParameterNamesCache.getParameterNameId(parameterName);
                if (parameterId == null) {
                    acquiredFromCache = false;
                    parameterId = this.readOrAddParameterNameId(parameterName);
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
                        tokenSystemId = this.readOrAddCodeSystemId(tokenSystem);
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
            dbCallStartTime = System.nanoTime();
            stmt.executeBatch();
            dbCallDuration = (System.nanoTime()-dbCallStartTime)/1e6;
            if (log.isLoggable(Level.FINE)) {
                log.fine("Batch DB Parameter insert complete. executionTime=" + dbCallDuration + "ms");
            }
        }
        catch(FHIRPersistenceDBConnectException e) {
            throw e;
        }
        catch(Throwable e) {
            FHIRPersistenceDataAccessException fx = new FHIRPersistenceDataAccessException("Failure inserting Parameter batch.");
            throw severe(log, fx, e);
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
        try {
            connection = this.getConnection();
            ParameterNameDAO pnd = new ParameterNameDAOImpl(connection);
            return pnd.readAllSearchParameterNames();
        }
        finally {
            this.cleanup(null, connection);
            log.exiting(CLASSNAME, METHODNAME);
        }
    }
    
    @Override
    public Map<String, Integer> readAllCodeSystems()
            throws FHIRPersistenceDBConnectException, FHIRPersistenceDataAccessException {
        final String METHODNAME = "readAllCodeSystems";
        log.entering(CLASSNAME, METHODNAME);
        
        Connection connection = null;        
        try {
            connection = this.getConnection();
            CodeSystemDAO csd = new CodeSystemDAOImpl(connection);
            return csd.readAllCodeSystems();
        }
        finally {
            this.cleanup(null, connection);
            log.exiting(CLASSNAME, METHODNAME);
        }
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
                case STRING :     returnChar = 'S';
                                  break;
                              
                case TOKEN :      returnChar = 'C';
                                  break;
                
                case QUANTITY : returnChar = 'Q';  
                                break;
                              
                case NUMBER :     returnChar = 'N';
                                break;
                    
                case DATE :     returnChar = 'D'; 
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
    public int readOrAddParameterNameId(String parameterName) throws FHIRPersistenceDBConnectException, FHIRPersistenceDataAccessException  {
        final String METHODNAME = "readParameterNameId";
        log.entering(CLASSNAME, METHODNAME);
        
        Connection connection = null;
                
        try {
            connection = this.getConnection();
            ParameterNameDAO pnd = new ParameterNameDAOImpl(connection);
            return pnd.readOrAddParameterNameId(parameterName);
        }
        finally {
            this.cleanup(null, connection);
            log.exiting(CLASSNAME, METHODNAME);
        }
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
    public int readOrAddCodeSystemId(String codeSystemName) throws FHIRPersistenceDBConnectException, FHIRPersistenceDataAccessException   {
        final String METHODNAME = "storeCodeSystemId";
        log.entering(CLASSNAME, METHODNAME);
        
        Connection connection = null;        
        try {
            connection = this.getConnection();
            CodeSystemDAO csd = new CodeSystemDAOImpl(connection);
            return csd.readOrAddCodeSystem(codeSystemName);
        }
        finally {
            this.cleanup(null, connection);
            log.exiting(CLASSNAME, METHODNAME);
        }
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
                    FHIRPersistenceException fx = new FHIRPersistenceException("Failure registering CodeSystemsCacheUpdater");
                    throw severe(log, fx, e);
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
                    FHIRPersistenceException fx = new FHIRPersistenceException("Failure registering ParameterNamesCacheUpdater");
                    throw severe(log, fx, e);
                }
            }
            this.newParameterNameIds.put(parameterName, parameterId);
        }
        
        log.exiting(CLASSNAME, METHODNAME);
        
    }
    
    /**
     * Acquire and return the id associated with the passed parameter name.
     * @param parameterName The name of a valid FHIR search parameter.
     * @return Integer A parameter id.
     * @throws FHIRPersistenceException
     */
    @Override
    public int acquireParameterNameId(String parameterName) throws FHIRPersistenceException {
        final String METHODNAME = "acquireParameterNameId";
        log.entering(CLASSNAME, METHODNAME);
        
        Integer parameterNameId;
        boolean acquiredFromCache;
        
        try {
            parameterNameId = ParameterNamesCache.getParameterNameId(parameterName);
            if (parameterNameId == null) {
                acquiredFromCache = false;
                parameterNameId = this.readOrAddParameterNameId(parameterName);
                this.addParameterNamesCacheCandidate(parameterName, parameterNameId);
            }
            else {
                acquiredFromCache = true;
            }
            if (log.isLoggable(Level.FINE)) {
                log.fine("paramenterName=" + parameterName + "  parameterNameId=" + parameterNameId + 
                          "  acquiredFromCache=" + acquiredFromCache + "  tenantDatastoreCacheName=" + ParameterNamesCache.getCacheNameForTenantDatastore());
            }
        }
        finally {
            log.exiting(CLASSNAME, METHODNAME);
        }
        return parameterNameId;
        
    }
    
    /**
     * Acquire and return the id associated with the passed code-system name.
     * @param codeSystemName The name of a valid code-system.
     * @return Integer A code-system id.
     * @throws FHIRPersistenceException
     */
    @Override
    public int acquireCodeSystemId(String codeSystemName) throws FHIRPersistenceException {
        final String METHODNAME = "acquireCodeSystemId";
        log.entering(CLASSNAME, METHODNAME);
        
        String myCodeSystemName = codeSystemName; 
        Integer codeSystemId;
        boolean acquiredFromCache;
        
        try {
            if (myCodeSystemName == null || myCodeSystemName.isEmpty()) {
                myCodeSystemName = DEFAULT_TOKEN_SYSTEM;
            }
            codeSystemId = CodeSystemsCache.getCodeSystemId(myCodeSystemName);
            if (codeSystemId == null) {
                acquiredFromCache = false;
                myCodeSystemName = SQLParameterEncoder.encode(myCodeSystemName);
                codeSystemId = this.readOrAddCodeSystemId(myCodeSystemName);
                this.addCodeSystemsCacheCandidate(myCodeSystemName, codeSystemId);
            }
            else {
                acquiredFromCache = true;
            }
            if (log.isLoggable(Level.FINE)) {
                log.fine("codeSystemName=" + myCodeSystemName + "  codeSystemId=" + codeSystemId + 
                          "  acquiredFromCache=" + acquiredFromCache + "  tenantDatastoreCacheName=" + CodeSystemsCache.getCacheNameForTenantDatastore());
            }
        }
        finally {
            log.exiting(CLASSNAME, METHODNAME);
        }
        
        return codeSystemId;
    }

    @Override
    public Array transformStringParameters(Connection connection, String schemaName, List<Parameter> parameters) throws FHIRPersistenceException {
        final String METHODNAME = "transformStringParameters";
        log.entering(CLASSNAME, METHODNAME);
        
        Array sqlParmArray = null;
        List<Struct> sqlParmList = new ArrayList<>();
        Struct[] structArray;
        Object[] rowData;
        String structTypeName;
        
        try {
            structTypeName = new StringBuilder().append(schemaName).append(".").append("T_STR_VALUES").toString();
            for (Parameter parameter : parameters) {
                if (parameter.getType().equals(Type.STRING) || parameter.getType().equals(Type.REFERENCE) ||
                    parameter.getType().equals(Type.URI)) {
                    rowData = new Object[] {this.acquireParameterNameId(parameter.getName()), parameter.getValueString(), 
                                            SearchUtil.normalizeForSearch(parameter.getValueString())}; 
                    sqlParmList.add(connection.createStruct(structTypeName, rowData));
                }
            }
            if (!sqlParmList.isEmpty()) {
                if (sqlParmList.size() > SQL_INSERT_PARAMETERS_MAX_ARRAY_SIZE_STRINGS) {
                    String msg = "Resource contains too many search parameter values of type " + 
                            "String. " + "Max: " + SQL_INSERT_PARAMETERS_MAX_ARRAY_SIZE_STRINGS + "; " + "Actual: " + sqlParmList.size();
                    throw buildExceptionWithIssue(msg, IssueType.ValueSet.TOO_COSTLY);
                }
                structArray = new Struct[sqlParmList.size()];
                sqlParmList.toArray(structArray);
                sqlParmArray = connection.createArrayOf(structTypeName, structArray);
            }
        }
        catch(FHIRPersistenceDataAccessException e) {
            throw e;
        }
        catch(Throwable e) {
            FHIRPersistenceDataAccessException fx = new FHIRPersistenceDataAccessException("Failure transforming String parameters.");
            throw severe(log, fx, e);
        }
        finally {
            log.exiting(CLASSNAME, METHODNAME);
        }
        
        return sqlParmArray;
    }
    
    @Override
    public Array transformNumberParameters(Connection connection, String schemaName, List<Parameter> parameters) throws FHIRPersistenceException {
        final String METHODNAME = "transformNumberParameters";
        log.entering(CLASSNAME, METHODNAME);
        
        Array sqlParmArray = null;
        List<Struct> sqlParmList = new ArrayList<>();
        Struct[] structArray;
        Object[] rowData;
        String structTypeName;
        
        try {
            structTypeName = new StringBuilder().append(schemaName).append(".").append("T_NUMBER_VALUES").toString();
            for (Parameter parameter : parameters) {
                if (parameter.getType().equals(Type.NUMBER)) {
                    // TODO: we're forcing the BigDecimal into a DOUBLE and we're losing precision...DB schema should be updated to use DECIMAL
                    rowData = new Object[] {this.acquireParameterNameId(parameter.getName()), parameter.getValueNumber().doubleValue()};
                    sqlParmList.add(connection.createStruct(structTypeName, rowData));
                }
            }
            if (!sqlParmList.isEmpty()) {
                if (sqlParmList.size() > SQL_INSERT_PARAMETERS_MAX_ARRAY_SIZE_DEFAULT) {
                    String msg = "Resource contains too many search parameter values of type " + 
                            "Number. " + "Max: " + SQL_INSERT_PARAMETERS_MAX_ARRAY_SIZE_DEFAULT + "; " + "Actual: " + sqlParmList.size();
                    throw buildExceptionWithIssue(msg, IssueType.ValueSet.TOO_COSTLY);
                }
                structArray = new Struct[sqlParmList.size()];
                sqlParmList.toArray(structArray);
                sqlParmArray = connection.createArrayOf(structTypeName, structArray);
            }
        }
        catch(FHIRPersistenceDataAccessException e) {
            throw e;
        }
        catch(Throwable e) {
            FHIRPersistenceDataAccessException fx = new FHIRPersistenceDataAccessException("Failure transforming Number parameters.");
            throw severe(log, fx, e);
        }
        finally {
            log.exiting(CLASSNAME, METHODNAME);
        }
        return sqlParmArray;
    }
    
    @Override
    public Array transformDateParameters(Connection connection, String schemaName, List<Parameter> parameters) throws FHIRPersistenceException {
        final String METHODNAME = "transformDateParameters";
        log.entering(CLASSNAME, METHODNAME);
        
        Array sqlParmArray = null;
        List<Struct> sqlParmList = new ArrayList<>();
        Struct[] structArray;
        Object[] rowData;
        String structTypeName;
        
        try {
            structTypeName = new StringBuilder().append(schemaName).append(".").append("T_DATE_VALUES").toString();
            for (Parameter parameter : parameters) {
                if (parameter.getType().equals(Type.DATE)) {
                    rowData = new Object[] {this.acquireParameterNameId(parameter.getName()), parameter.getValueDate(), 
                                            parameter.getValueDateStart(), parameter.getValueDateEnd()};
                    sqlParmList.add(connection.createStruct(structTypeName, rowData));
                }
            }
            if (!sqlParmList.isEmpty()) {
                if (sqlParmList.size() > SQL_INSERT_PARAMETERS_MAX_ARRAY_SIZE_DEFAULT) {
                    String msg = "Resource contains too many search parameter values of type " + 
                            "Date. " + "Max: " + SQL_INSERT_PARAMETERS_MAX_ARRAY_SIZE_DEFAULT + "; " + "Actual: " + sqlParmList.size();
                    throw buildExceptionWithIssue(msg, IssueType.ValueSet.TOO_COSTLY);
                }
                structArray = new Struct[sqlParmList.size()];
                sqlParmList.toArray(structArray);
                sqlParmArray = connection.createArrayOf(structTypeName, structArray);
            }
        }
        catch(FHIRPersistenceDataAccessException e) {
            throw e;
        }
        catch(Throwable e) {
            FHIRPersistenceDataAccessException fx = new FHIRPersistenceDataAccessException("Failure transforming Date parameters.");
            throw severe(log, fx, e);
        }
        finally {
            log.exiting(CLASSNAME, METHODNAME);
        }
        return sqlParmArray;
    }
    
    @Override
    public Array transformLatLongParameters(Connection connection, String schemaName, List<Parameter> parameters) throws FHIRPersistenceException {
        final String METHODNAME = "transformLatLongParameters";
        log.entering(CLASSNAME, METHODNAME);
        
        Array sqlParmArray = null;
        List<Struct> sqlParmList = new ArrayList<>();
        Struct[] structArray;
        Object[] rowData;
        String structTypeName;
        
        try {
            structTypeName = new StringBuilder().append(schemaName).append(".").append("T_LATLNG_VALUES").toString();
            for (Parameter parameter : parameters) {
                if (AbstractQueryBuilder.NEAR.equals(parameter.getName()) || 
                    AbstractQueryBuilder.NEAR_DISTANCE.equals(parameter.getName())) {
                    rowData = new Object[] {this.acquireParameterNameId(parameter.getName()),
                                            parameter.getValueLatitude(), parameter.getValueLongitude()};
                    sqlParmList.add(connection.createStruct(structTypeName, rowData));
                }
            }
            if (!sqlParmList.isEmpty()) {
                if (sqlParmList.size() > SQL_INSERT_PARAMETERS_MAX_ARRAY_SIZE_DEFAULT) {
                    String msg = "Resource contains too many search parameter values of type " + 
                            "LatLong. " + "Max: " + SQL_INSERT_PARAMETERS_MAX_ARRAY_SIZE_DEFAULT + "; " + "Actual: " + sqlParmList.size();
                    throw buildExceptionWithIssue(msg, IssueType.ValueSet.TOO_COSTLY);
                }
                structArray = new Struct[sqlParmList.size()];
                sqlParmList.toArray(structArray);
                sqlParmArray = connection.createArrayOf(structTypeName, structArray);
            }
        }
        catch(FHIRPersistenceDataAccessException e) {
            throw e;
        }
        catch(Throwable e) {
            FHIRPersistenceDataAccessException fx = new FHIRPersistenceDataAccessException("Failure transforming Lat/Lng parameters.");
            throw severe(log, fx, e);
        }
        finally {
            log.exiting(CLASSNAME, METHODNAME);
        }
        return sqlParmArray;
    }
    
    @Override
    public Array transformTokenParameters(Connection connection, String schemaName, List<Parameter> parameters) throws FHIRPersistenceException {
        final String METHODNAME = "transformTokenParameters";
        log.entering(CLASSNAME, METHODNAME);
        
        Array sqlParmArray = null;
        List<Struct> sqlParmList = new ArrayList<>();
        Struct[] structArray;
        Object[] rowData;
        String structTypeName;
        
        try {
            structTypeName = new StringBuilder().append(schemaName).append(".").append("T_TOKEN_VALUES").toString();
            for (Parameter parameter : parameters) {
                if (parameter.getType().equals(Type.TOKEN)) {
                    rowData = new Object[] {this.acquireParameterNameId(parameter.getName()),
                                            this.acquireCodeSystemId(parameter.getValueSystem()), parameter.getValueCode()};
                    sqlParmList.add(connection.createStruct(structTypeName, rowData));
                }
            }
            if (!sqlParmList.isEmpty()) {
                if (sqlParmList.size() > SQL_INSERT_PARAMETERS_MAX_ARRAY_SIZE_DEFAULT) {
                    String msg = "Resource contains too many search parameter values of type " + 
                            "Token. " + "Max: " + SQL_INSERT_PARAMETERS_MAX_ARRAY_SIZE_DEFAULT + "; " + "Actual: " + sqlParmList.size();
                    throw buildExceptionWithIssue(msg, IssueType.ValueSet.TOO_COSTLY);
                }
                structArray = new Struct[sqlParmList.size()];
                sqlParmList.toArray(structArray);
                sqlParmArray = connection.createArrayOf(structTypeName, structArray);
            }
        }
        catch(FHIRPersistenceDataAccessException e) {
            throw e;
        }
        catch(Throwable e) {
            FHIRPersistenceDataAccessException fx = new FHIRPersistenceDataAccessException("Failure transforming Token parameters.");
            throw severe(log, fx, e);
        }
        finally {
            log.exiting(CLASSNAME, METHODNAME);
        }
        return sqlParmArray;
    }
    
    @Override
    public Array transformQuantityParameters(Connection connection, String schemaName, List<Parameter> parameters) throws FHIRPersistenceException {
        final String METHODNAME = "transformQuantityParameters";
        log.entering(CLASSNAME, METHODNAME);
        
        Array sqlParmArray = null;
        List<Struct> sqlParmList = new ArrayList<>();
        Struct[] structArray;
        Object[] rowData;
        String structTypeName;
        
        try {
            structTypeName = new StringBuilder().append(schemaName).append(".").append("T_QUANTITY_VALUES").toString();
            for (Parameter parameter : parameters) {
                
                if (parameter.getType().equals(Type.QUANTITY)) {
                    // TODO: we're forcing the BigDecimal into a DOUBLE and we're losing precision...DB schema should be updated to use DECIMAL
                    rowData = new Object[] {this.acquireParameterNameId(parameter.getName()),
                                            parameter.getValueCode(), parameter.getValueNumber().doubleValue(),
                                            parameter.getValueNumberLow().doubleValue(), parameter.getValueNumberHigh().doubleValue(),
                                            this.acquireCodeSystemId(parameter.getValueSystem())};
                    sqlParmList.add(connection.createStruct(structTypeName, rowData));
                }
            }
            if (!sqlParmList.isEmpty()) {
                if (sqlParmList.size() > SQL_INSERT_PARAMETERS_MAX_ARRAY_SIZE_DEFAULT) {
                    String msg = "Resource contains too many search parameter values of type " + 
                            "Quantity. " + "Max: " + SQL_INSERT_PARAMETERS_MAX_ARRAY_SIZE_DEFAULT + "; " + "Actual: " + sqlParmList.size();
                    throw buildExceptionWithIssue(msg, IssueType.ValueSet.TOO_COSTLY);
                }
                structArray = new Struct[sqlParmList.size()];
                sqlParmList.toArray(structArray);
                sqlParmArray = connection.createArrayOf(structTypeName, structArray);
            }
        }
        catch(FHIRPersistenceDataAccessException e) {
            throw e;
        }
        catch(Throwable e) {
            FHIRPersistenceDataAccessException fx = new FHIRPersistenceDataAccessException("Failure transforming Quantity parameters.");
            throw severe(log, fx, e);
        }
        finally {
            log.exiting(CLASSNAME, METHODNAME);
        }
        return sqlParmArray;
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.persistence.jdbc.dao.api.ParameterNormalizedDAO#readParameterNameId(java.lang.String)
     */
    @Override
    public Integer readParameterNameId(String parameterName) throws FHIRPersistenceDBConnectException, FHIRPersistenceDataAccessException {
        final String METHODNAME = "readParameterNameId";
        log.entering(CLASSNAME, METHODNAME);
        
        Connection connection = null;
        try {
            connection = this.getConnection();
            ParameterNameDAO pnd = new ParameterNameDAOImpl(connection);
            return pnd.readParameterNameId(parameterName);
        }
        finally {
            this.cleanup(null, connection);
            log.exiting(CLASSNAME, METHODNAME);
        }
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.persistence.jdbc.dao.api.ParameterNormalizedDAO#readCodeSystemId(java.lang.String)
     */
    @Override
    public Integer readCodeSystemId(String codeSystemName) throws FHIRPersistenceDBConnectException, FHIRPersistenceDataAccessException {
        final String METHODNAME = "readCodeSystemId";
        log.entering(CLASSNAME, METHODNAME);
        
        Connection connection = null;
        try {
            connection = this.getConnection();
            CodeSystemDAO csd = new CodeSystemDAOImpl(connection);
            return csd.readCodeSystemId(codeSystemName);
        }
        finally {
            this.cleanup(null, connection);
            log.exiting(CLASSNAME, METHODNAME);
        }
    }
     
}
