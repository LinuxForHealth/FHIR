/*
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dao.impl;

import java.sql.Array;
import java.sql.Connection;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.transaction.TransactionSynchronizationRegistry;

import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.dao.api.CodeSystemDAO;
import com.ibm.fhir.persistence.jdbc.dao.api.ParameterDAO;
import com.ibm.fhir.persistence.jdbc.dao.api.ParameterNameDAO;
import com.ibm.fhir.persistence.jdbc.dto.Parameter;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDataAccessException;
import com.ibm.fhir.persistence.jdbc.util.CodeSystemsCache;
import com.ibm.fhir.persistence.jdbc.util.CodeSystemsCacheUpdater;
import com.ibm.fhir.persistence.jdbc.util.ParameterNamesCache;
import com.ibm.fhir.persistence.jdbc.util.ParameterNamesCacheUpdater;
import com.ibm.fhir.persistence.jdbc.util.SqlParameterEncoder;
import com.ibm.fhir.persistence.util.AbstractQueryBuilder;
import com.ibm.fhir.search.SearchConstants.Type;
import com.ibm.fhir.search.util.SearchUtil;

/**
 * This Data Access Object implements the ParameterDAO interface for creating, updating, 
 * and retrieving rows in the IBM FHIR Server parameter-related tables.
 */
public class ParameterDAOImpl extends FHIRDbDAOImpl implements ParameterDAO {
    private static final Logger log = Logger.getLogger(ParameterDAOImpl.class.getName());
    private static final String CLASSNAME = ParameterDAOImpl.class.getName(); 
    
    public static final String DEFAULT_TOKEN_SYSTEM = "default-token-system";
    
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
    public ParameterDAOImpl(TransactionSynchronizationRegistry trxSynchRegistry) {
        super();
        this.runningInTrx = true;
        this.trxSynchRegistry = trxSynchRegistry;
    }
    
    /**
     * Constructs a DAO using the passed externally managed database connection.
     * The connection used by this instance for all DB operations will be the passed connection.
     * @param Connection - A database connection that will be managed by the caller.
     */
    public ParameterDAOImpl(Connection managedConnection) {
        super(managedConnection);
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
                myCodeSystemName = SqlParameterEncoder.encode(myCodeSystemName);
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
                    throw buildExceptionWithIssue(msg, IssueType.TOO_COSTLY);
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
                    throw buildExceptionWithIssue(msg, IssueType.TOO_COSTLY);
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
                    throw buildExceptionWithIssue(msg, IssueType.TOO_COSTLY);
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
                    throw buildExceptionWithIssue(msg, IssueType.TOO_COSTLY);
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
                    throw buildExceptionWithIssue(msg, IssueType.TOO_COSTLY);
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
                    throw buildExceptionWithIssue(msg, IssueType.TOO_COSTLY);
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
