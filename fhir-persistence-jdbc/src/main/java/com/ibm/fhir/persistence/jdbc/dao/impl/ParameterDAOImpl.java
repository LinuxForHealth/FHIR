/*
 * (C) Copyright IBM Corp. 2017,2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dao.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.transaction.TransactionSynchronizationRegistry;

import com.ibm.fhir.database.utils.model.DbType;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.dao.api.CodeSystemDAO;
import com.ibm.fhir.persistence.jdbc.dao.api.ParameterDAO;
import com.ibm.fhir.persistence.jdbc.dao.api.ParameterNameDAO;
import com.ibm.fhir.persistence.jdbc.derby.DerbyCodeSystemDAO;
import com.ibm.fhir.persistence.jdbc.derby.DerbyParameterNamesDAO;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDataAccessException;
import com.ibm.fhir.persistence.jdbc.postgresql.PostgreSqlCodeSystemDAO;
import com.ibm.fhir.persistence.jdbc.postgresql.PostgreSqlParameterNamesDAO;
import com.ibm.fhir.persistence.jdbc.util.CodeSystemsCache;
import com.ibm.fhir.persistence.jdbc.util.CodeSystemsCacheUpdater;
import com.ibm.fhir.persistence.jdbc.util.ParameterNamesCache;
import com.ibm.fhir.persistence.jdbc.util.ParameterNamesCacheUpdater;
import com.ibm.fhir.persistence.jdbc.util.SqlParameterEncoder;

/**
 * This Data Access Object implements the ParameterDAO interface for creating, updating,
 * and retrieving rows in the IBM FHIR Server parameter-related tables.
 */
public class ParameterDAOImpl extends FHIRDbDAOImpl implements ParameterDAO {
    private static final Logger log = Logger.getLogger(ParameterDAOImpl.class.getName());
    private static final String CLASSNAME = ParameterDAOImpl.class.getName();

    public static final String DEFAULT_TOKEN_SYSTEM = "default-token-system";

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
        } finally {
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
        } finally {
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
        final String METHODNAME = "readOrAddParameterNameId";
        log.entering(CLASSNAME, METHODNAME);

        Connection connection = null;

        try {
            connection = this.getConnection();
            String dbProductName = connection.getMetaData().getDatabaseProductName().toLowerCase();
            ParameterNameDAO pnd;
            if (dbProductName.equals(DbType.POSTGRESQL.value())) {
                pnd = new PostgreSqlParameterNamesDAO(connection);
            } else if (dbProductName.contains(DbType.DERBY.value())) {
                pnd = new DerbyParameterNamesDAO(connection);
            } else {
                pnd = new ParameterNameDAOImpl(connection);
            }

            return pnd.readOrAddParameterNameId(parameterName);
        } catch (SQLException e) {
            throw new FHIRPersistenceDataAccessException("Failed to tell database type from connection!", e);
        } finally {
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
        final String METHODNAME = "readOrAddCodeSystemId";
        log.entering(CLASSNAME, METHODNAME);

        Connection connection = null;
        try {
            connection = this.getConnection();
            String dbProductName = connection.getMetaData().getDatabaseProductName().toLowerCase();
            CodeSystemDAO csd;
            if (dbProductName.equals(DbType.POSTGRESQL.value())) {
                csd = new PostgreSqlCodeSystemDAO(connection);
            } else if (dbProductName.contains(DbType.DERBY.value())) {
                csd = new DerbyCodeSystemDAO(connection);
            } else {
                csd = new CodeSystemDAOImpl(connection);
            }

            return csd.readOrAddCodeSystem(codeSystemName);
        } catch (SQLException e) {
            throw new FHIRPersistenceDataAccessException("Failed to tell database type from connection!", e);
        } finally {
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
                } catch(Throwable e) {
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
                } catch(Throwable e) {
                    FHIRPersistenceException fx = new FHIRPersistenceException("Failure registering ParameterNamesCacheUpdater");
                    throw severe(log, fx, e);
                }
            }
            this.newParameterNameIds.put(parameterName, parameterId);
        }

        log.exiting(CLASSNAME, METHODNAME);

    }


    protected  Integer getParameterNameIdFromCaches(String parameterName) {
        // Get ParameterNameId from ParameterNameIdCache first.
        Integer parameterNameId = ParameterNamesCache.getParameterNameId(parameterName);
        // If not found, then get ParameterNameId from local newParameterNameIds in case this id is already in newParameterNameIds
        // but has not been updated to ParameterNamesCache yet. newParameterNameIds is updated to ParameterNamesCache only when the
        // current transaction is committed.
        if (parameterNameId == null) {
            parameterNameId = this.newParameterNameIds.get(parameterName);
        }
        return parameterNameId;
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
            parameterNameId = getParameterNameIdFromCaches(parameterName);
            if (parameterNameId == null) {
                acquiredFromCache = false;
                parameterNameId = this.readOrAddParameterNameId(parameterName);
                this.addParameterNamesCacheCandidate(parameterName, parameterNameId);
            } else {
                acquiredFromCache = true;
            }
            if (log.isLoggable(Level.FINE)) {
                log.fine("paramenterName=" + parameterName + "  parameterNameId=" + parameterNameId +
                          "  acquiredFromCache=" + acquiredFromCache + "  tenantDatastoreCacheName=" + ParameterNamesCache.getCacheNameForTenantDatastore());
            }
        } finally {
            log.exiting(CLASSNAME, METHODNAME);
        }
        return parameterNameId;

    }

    protected  Integer getCodeSystemIdFromCaches(String codeSystemName) {
        // Get CodeSystemId from CodeSystemsCache first.
        Integer codeSystemId = CodeSystemsCache.getCodeSystemId(codeSystemName);
        // If no found, then get codeSystemId from local newCodeSystemIds in case this id is already in newCodeSystemIds
        // but has not been updated to CodeSystemsCache yet. newCodeSystemIds is updated to CodeSystemsCache only when the
        // current transaction is committed.
        if (codeSystemId == null) {
            codeSystemId = this.newCodeSystemIds.get(codeSystemName);
        }
        return codeSystemId;
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
            codeSystemId = getCodeSystemIdFromCaches(myCodeSystemName);

            if (codeSystemId == null) {
                acquiredFromCache = false;
                myCodeSystemName = SqlParameterEncoder.encode(myCodeSystemName);
                codeSystemId = this.readOrAddCodeSystemId(myCodeSystemName);
                this.addCodeSystemsCacheCandidate(myCodeSystemName, codeSystemId);
            } else {
                acquiredFromCache = true;
            }
            if (log.isLoggable(Level.FINE)) {
                log.fine("codeSystemName=" + myCodeSystemName + "  codeSystemId=" + codeSystemId +
                          "  acquiredFromCache=" + acquiredFromCache + "  tenantDatastoreCacheName=" + CodeSystemsCache.getCacheNameForTenantDatastore());
            }
        } finally {
            log.exiting(CLASSNAME, METHODNAME);
        }

        return codeSystemId;
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
        } finally {
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
        } finally {
            this.cleanup(null, connection);
            log.exiting(CLASSNAME, METHODNAME);
        }
    }

}
