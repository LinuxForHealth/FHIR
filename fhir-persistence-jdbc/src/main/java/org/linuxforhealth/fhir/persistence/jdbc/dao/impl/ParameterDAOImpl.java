/*
 * (C) Copyright IBM Corp. 2017,2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.dao.impl;

import java.sql.Connection;
import java.util.Map;
import java.util.logging.Logger;

import javax.transaction.TransactionSynchronizationRegistry;

import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceDataAccessException;
import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceException;
import org.linuxforhealth.fhir.persistence.jdbc.connection.FHIRDbFlavor;
import org.linuxforhealth.fhir.persistence.jdbc.dao.api.CodeSystemDAO;
import org.linuxforhealth.fhir.persistence.jdbc.dao.api.ParameterDAO;
import org.linuxforhealth.fhir.persistence.jdbc.derby.DerbyCodeSystemDAO;
import org.linuxforhealth.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;
import org.linuxforhealth.fhir.persistence.jdbc.postgres.PostgresCodeSystemDAO;
import org.linuxforhealth.fhir.persistence.jdbc.postgres.PostgresParameterNamesDAO;
import org.linuxforhealth.fhir.persistence.params.api.ParameterNameDAO;
import org.linuxforhealth.fhir.persistence.params.database.DerbyParameterNamesDAO;
import org.linuxforhealth.fhir.persistence.params.database.ParameterNameDAOImpl;

/**
 * This Data Access Object implements the ParameterDAO interface for creating, updating,
 * and retrieving rows in the IBM FHIR Server parameter-related tables.
 */
public class ParameterDAOImpl extends FHIRDbDAOImpl implements ParameterDAO {
    private static final Logger log = Logger.getLogger(ParameterDAOImpl.class.getName());
    private static final String CLASSNAME = ParameterDAOImpl.class.getName();

    private final TransactionSynchronizationRegistry trxSynchRegistry;
    private final boolean runningInTrx;


    /**
     * Constructs a DAO instance suitable for acquiring connections from a JDBC Datasource object.
     * @param Connection - A database connection that will be managed by the caller.
     * @param schemaName
     * @param flavor
     * @param trxSynchRegistry
     */
    public ParameterDAOImpl(Connection connection, String schemaName, FHIRDbFlavor flavor, TransactionSynchronizationRegistry trxSynchRegistry) {
        super(connection, schemaName, flavor);
        this.trxSynchRegistry = trxSynchRegistry;
        this.runningInTrx = true;
    }

    /**
     * Constructs a DAO using the passed externally managed database connection.
     * The connection used by this instance for all DB operations will be the passed connection.
     * @param Connection - A database connection that will be managed by the caller.
     * @param schemaName
     * @param flavor
     */
    public ParameterDAOImpl(Connection connection, String schemaName, FHIRDbFlavor flavor) {
        super(connection, schemaName, flavor);

        // For unit-tests, we don't use managed transactions, so don't have any sync registry.
        this.trxSynchRegistry = null;
        this.runningInTrx = false;
    }

    @Override
    public Map<String, Integer> readAllSearchParameterNames()
                                         throws FHIRPersistenceDBConnectException, FHIRPersistenceDataAccessException {
        final String METHODNAME = "readAllSearchParameterNames";
        log.entering(CLASSNAME, METHODNAME);

        final Connection connection = getConnection(); // do not close
        try {
            ParameterNameDAO pnd = new ParameterNameDAOImpl(connection, getSchemaName());
            return pnd.readAllSearchParameterNames();
        } finally {
            log.exiting(CLASSNAME, METHODNAME);
        }
    }

    @Override
    public Map<String, Integer> readAllCodeSystems()
            throws FHIRPersistenceDBConnectException, FHIRPersistenceDataAccessException {
        final String METHODNAME = "readAllCodeSystems";
        log.entering(CLASSNAME, METHODNAME);

        final Connection connection = getConnection(); // do not close
        try {
            CodeSystemDAO csd = new CodeSystemDAOImpl(connection, getSchemaName());
            return csd.readAllCodeSystems();
        } finally {
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

        final Connection connection = getConnection(); // do not close

        try {
            ParameterNameDAO pnd;
            switch (getFlavor().getType()) {
            case DERBY:
                pnd = new DerbyParameterNamesDAO(connection, getSchemaName());
                break;
            case POSTGRESQL:
            case CITUS:
                pnd = new PostgresParameterNamesDAO(connection, getSchemaName());
                break;
            default:
                pnd = new ParameterNameDAOImpl(connection, getSchemaName());
                break;

            }

            return pnd.readOrAddParameterNameId(parameterName);
        } finally {
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

        final Connection connection = getConnection(); // do not close
        try {
            CodeSystemDAO csd;

            switch (getFlavor().getType()) {
            case DERBY:
                csd = new DerbyCodeSystemDAO(connection, getSchemaName());
                break;
            case POSTGRESQL:
            case CITUS:
                csd = new PostgresCodeSystemDAO(connection, getSchemaName());
                break;
            default:
                csd = new CodeSystemDAOImpl(connection, getSchemaName());
                break;
            }

            return csd.readOrAddCodeSystem(codeSystemName);
        } finally {
            log.exiting(CLASSNAME, METHODNAME);
        }
    }

    /**
     * Acquire and return the id associated with the passed parameter name. Being called here
     * means that we've already had a cache miss in the JDBCIdentityCache
     * @param parameterName The name of a valid FHIR search parameter.
     * @return Integer A parameter id.
     * @throws FHIRPersistenceException
     */
    @Override
    public int acquireParameterNameId(String parameterName) throws FHIRPersistenceException {
        final String METHODNAME = "acquireParameterNameId";
        log.entering(CLASSNAME, METHODNAME);

        Integer parameterNameId;

        try {
            parameterNameId = this.readOrAddParameterNameId(parameterName);
        } finally {
            log.exiting(CLASSNAME, METHODNAME);
        }
        return parameterNameId;

    }

    @Override
    public Integer readParameterNameId(String parameterName) throws FHIRPersistenceDBConnectException, FHIRPersistenceDataAccessException {
        final String METHODNAME = "readParameterNameId";
        log.entering(CLASSNAME, METHODNAME);

        final Connection connection = getConnection(); // do not close
        try {
            ParameterNameDAO pnd = new ParameterNameDAOImpl(connection, getSchemaName());
            return pnd.readParameterNameId(parameterName);
        } finally {
            log.exiting(CLASSNAME, METHODNAME);
        }
    }

    @Override
    public Integer readCodeSystemId(String codeSystemName) throws FHIRPersistenceDBConnectException, FHIRPersistenceDataAccessException {
        final String METHODNAME = "readCodeSystemId";
        log.entering(CLASSNAME, METHODNAME);

        final Connection connection = getConnection(); // do not close
        try {
            CodeSystemDAO csd = new CodeSystemDAOImpl(connection, getSchemaName());
            return csd.readCodeSystemId(codeSystemName);
        } finally {
            log.exiting(CLASSNAME, METHODNAME);
        }
    }

}
