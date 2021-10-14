/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc;

import java.sql.Connection;

import javax.transaction.TransactionSynchronizationRegistry;

import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.common.DatabaseTranslatorFactory;
import com.ibm.fhir.database.utils.db2.Db2Translator;
import com.ibm.fhir.database.utils.derby.DerbyTranslator;
import com.ibm.fhir.database.utils.postgres.PostgresTranslator;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.connection.FHIRDbFlavor;
import com.ibm.fhir.persistence.jdbc.dao.ReindexResourceDAO;
import com.ibm.fhir.persistence.jdbc.dao.api.IResourceReferenceDAO;
import com.ibm.fhir.persistence.jdbc.dao.api.ParameterDAO;
import com.ibm.fhir.persistence.jdbc.dao.api.ResourceDAO;
import com.ibm.fhir.persistence.jdbc.dao.impl.ResourceDAOImpl;
import com.ibm.fhir.persistence.jdbc.dao.impl.ResourceReferenceDAO;
import com.ibm.fhir.persistence.jdbc.db2.Db2ResourceReferenceDAO;
import com.ibm.fhir.persistence.jdbc.derby.DerbyResourceDAO;
import com.ibm.fhir.persistence.jdbc.derby.DerbyResourceReferenceDAO;
import com.ibm.fhir.persistence.jdbc.impl.ParameterTransactionDataImpl;
import com.ibm.fhir.persistence.jdbc.postgres.PostgresReindexResourceDAO;
import com.ibm.fhir.persistence.jdbc.postgres.PostgresResourceDAO;
import com.ibm.fhir.persistence.jdbc.postgres.PostgresResourceReferenceDAO;

/**
 * Factory for constructing ResourceDAO implementations specific to a
 * particular {@link FHIRDbFlavor}.
 */
public class FHIRResourceDAOFactory {

    /**
     * Construct a new ResourceDAO implementation matching the database type
     * @param connection valid connection to the database
     * @param schemaName the name of the schema containing the FHIR resource tables
     * @param flavor the type and capability of the database and schema
     * @param trxSynchRegistry
     * @return a concrete implementation of {@link ResourceDAO}
     * @throws IllegalArgumentException
     * @throws FHIRPersistenceException
     */
    public static ResourceDAO getResourceDAO(Connection connection, String adminSchemaName, String schemaName, FHIRDbFlavor flavor, TransactionSynchronizationRegistry trxSynchRegistry,
        FHIRPersistenceJDBCCache cache, ParameterTransactionDataImpl ptdi)
        throws IllegalArgumentException, FHIRPersistenceException {
        ResourceDAO resourceDAO = null;

        IResourceReferenceDAO rrd = getResourceReferenceDAO(connection, adminSchemaName, schemaName, flavor, cache);
        switch (flavor.getType()) {
        case DB2:
            resourceDAO = new ResourceDAOImpl(connection, schemaName, flavor, trxSynchRegistry, cache, rrd, ptdi);
            break;
        case DERBY:
            resourceDAO = new DerbyResourceDAO(connection, schemaName, flavor, trxSynchRegistry, cache, rrd, ptdi);
            break;
        case POSTGRESQL:
            resourceDAO = new PostgresResourceDAO(connection, schemaName, flavor, trxSynchRegistry, cache, rrd, ptdi);
            break;
        }
        return resourceDAO;
    }

    /**
     * Instantiate a new instance of {@link ReindexResourceDAO} configured for the given database type
     * @param connection
     * @param schemaName
     * @param flavor
     * @param trxSynchRegistry
     * @param cache
     * @param parameterDao
     * @return
     */
    public static ReindexResourceDAO getReindexResourceDAO(Connection connection, String adminSchemaName, String schemaName, FHIRDbFlavor flavor, TransactionSynchronizationRegistry trxSynchRegistry,
        FHIRPersistenceJDBCCache cache, ParameterDAO parameterDao) {

        IDatabaseTranslator translator = null;
        ReindexResourceDAO result = null;
        IResourceReferenceDAO rrd = getResourceReferenceDAO(connection, adminSchemaName, schemaName, flavor, cache);

        switch (flavor.getType()) {
        case DB2:
            translator = new Db2Translator();
            result = new ReindexResourceDAO(connection, translator, parameterDao, schemaName, flavor, cache, rrd);
            break;
        case DERBY:
            translator = new DerbyTranslator();
            result = new ReindexResourceDAO(connection, translator, parameterDao, schemaName, flavor, cache, rrd);
            break;
        case POSTGRESQL:
            translator = new PostgresTranslator();
            result = new PostgresReindexResourceDAO(connection, translator, parameterDao, schemaName, flavor, cache, rrd);
            break;
        }
        return result;
    }

    /**
     * Get the {@link IDatabaseTranslator} implementation specific to the given {@link FHIRDbFlavor}
     * @param flavor
     * @return
     */
    public static IDatabaseTranslator getTranslatorForFlavor(FHIRDbFlavor flavor) {
        return DatabaseTranslatorFactory.getTranslator(flavor.getType());
    }

    /**
     * Construct a new ResourceDAO implementation matching the database type
     * @param connection valid connection to the database
     * @param schemaName the name of the schema containing the FHIR resource tables
     * @param flavor the type and capability of the database and schema
     * @return a concrete implementation of {@link ResourceDAO}
     * @throws IllegalArgumentException
     * @throws FHIRPersistenceException
     */
    public static ResourceDAO getResourceDAO(Connection connection, String adminSchemaName, String schemaName, FHIRDbFlavor flavor,
        FHIRPersistenceJDBCCache cache) throws IllegalArgumentException, FHIRPersistenceException {
        ResourceDAO resourceDAO = null;

        IResourceReferenceDAO rrd = getResourceReferenceDAO(connection, adminSchemaName, schemaName, flavor, cache);
        switch (flavor.getType()) {
        case DB2:
            resourceDAO = new ResourceDAOImpl(connection, schemaName, flavor, cache, rrd);
            break;
        case DERBY:
            resourceDAO = new DerbyResourceDAO(connection, schemaName, flavor, cache, rrd);
            break;
        case POSTGRESQL:
            resourceDAO = new PostgresResourceDAO(connection, schemaName, flavor, cache, rrd);
            break;
        }
        return resourceDAO;
    }

    /**
     * Get a standalone DAO to handle the inserts of the common token values and
     * resource token refs just prior to the transaction commit
     * @param connection
     * @param schemaName
     * @param flavor
     * @param cache
     * @return
     */
    public static ResourceReferenceDAO getResourceReferenceDAO(Connection connection, String adminSchemaName, String schemaName, FHIRDbFlavor flavor,
        FHIRPersistenceJDBCCache cache) {

        ResourceReferenceDAO rrd = null;
        switch (flavor.getType()) {
        case DB2:
            rrd = new Db2ResourceReferenceDAO(new Db2Translator(), connection, schemaName, cache.getResourceReferenceCache(), adminSchemaName, cache.getParameterNameCache());
            break;
        case DERBY:
            rrd = new DerbyResourceReferenceDAO(new DerbyTranslator(), connection, schemaName, cache.getResourceReferenceCache(), cache.getParameterNameCache());
            break;
        case POSTGRESQL:
            rrd = new PostgresResourceReferenceDAO(new PostgresTranslator(), connection, schemaName, cache.getResourceReferenceCache(), cache.getParameterNameCache());
            break;
        }
        return rrd;
    }

}
