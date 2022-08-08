/*
 * (C) Copyright IBM Corp. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc;

import java.sql.Connection;

import javax.transaction.TransactionSynchronizationRegistry;

import org.linuxforhealth.fhir.database.utils.api.IDatabaseTranslator;
import org.linuxforhealth.fhir.database.utils.common.DatabaseTranslatorFactory;
import org.linuxforhealth.fhir.database.utils.derby.DerbyTranslator;
import org.linuxforhealth.fhir.database.utils.postgres.PostgresTranslator;
import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceException;
import org.linuxforhealth.fhir.persistence.jdbc.citus.CitusResourceDAO;
import org.linuxforhealth.fhir.persistence.jdbc.connection.FHIRDbFlavor;
import org.linuxforhealth.fhir.persistence.jdbc.dao.ReindexResourceDAO;
import org.linuxforhealth.fhir.persistence.jdbc.dao.api.FhirSequenceDAO;
import org.linuxforhealth.fhir.persistence.jdbc.dao.api.ParameterDAO;
import org.linuxforhealth.fhir.persistence.jdbc.dao.api.ResourceDAO;
import org.linuxforhealth.fhir.persistence.jdbc.dao.impl.CommonValuesDAO;
import org.linuxforhealth.fhir.persistence.jdbc.derby.DerbyCommonValuesDAO;
import org.linuxforhealth.fhir.persistence.jdbc.derby.DerbyResourceDAO;
import org.linuxforhealth.fhir.persistence.jdbc.impl.ParameterTransactionDataImpl;
import org.linuxforhealth.fhir.persistence.jdbc.postgres.PostgresReindexResourceDAO;
import org.linuxforhealth.fhir.persistence.jdbc.postgres.PostgresResourceDAO;

/**
 * Factory for constructing ResourceDAO implementations specific to a
 * particular {@link FHIRDbFlavor}.
 */
public class FHIRResourceDAOFactory {

    /**
     * Construct a new ResourceDAO implementation matching the database type
     * @param connection valid connection to the database
     * @param adminSchemaName
     * @param schemaName the name of the schema containing the FHIR resource tables
     * @param flavor the type and capability of the database and schema
     * @param trxSynchRegistry
     * @param cache
     * @param ptdi
     * @param shardKey
     * @return a concrete implementation of {@link ResourceDAO}
     * @throws IllegalArgumentException
     * @throws FHIRPersistenceException
     */
    public static ResourceDAO getResourceDAO(Connection connection, String adminSchemaName, String schemaName, FHIRDbFlavor flavor, TransactionSynchronizationRegistry trxSynchRegistry,
        FHIRPersistenceJDBCCache cache, ParameterTransactionDataImpl ptdi, Short shardKey)
        throws IllegalArgumentException, FHIRPersistenceException {
        final ResourceDAO resourceDAO;

        switch (flavor.getType()) {
        case DERBY:
            resourceDAO = new DerbyResourceDAO(connection, schemaName, flavor, trxSynchRegistry, cache, ptdi);
            break;
        case POSTGRESQL:
            resourceDAO = new PostgresResourceDAO(connection, schemaName, flavor, trxSynchRegistry, cache, ptdi, shardKey);
            break;
        case CITUS:
            resourceDAO = new CitusResourceDAO(connection, schemaName, flavor, trxSynchRegistry, cache, ptdi, shardKey);
            break;
        default:
            throw new IllegalArgumentException("Unsupported database type: " + flavor.getType().name());
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

        final IDatabaseTranslator translator;
        final ReindexResourceDAO result;

        switch (flavor.getType()) {
        case DERBY:
            translator = new DerbyTranslator();
            result = new ReindexResourceDAO(connection, translator, parameterDao, schemaName, flavor, cache);
            break;
        case POSTGRESQL:
        case CITUS:
            translator = new PostgresTranslator();
            result = new PostgresReindexResourceDAO(connection, translator, parameterDao, schemaName, flavor, cache);
            break;
        default:
            throw new IllegalArgumentException("Unsupported database type: " + flavor.getType().name());
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
     * 
     * @param connection valid connection to the database
     * @param adminSchemaName
     * @param schemaName the name of the schema containing the FHIR resource tables
     * @param flavor the type and capability of the database and schema
     * @param cache
     * @param shardKey
     * @return a concrete implementation of {@link ResourceDAO}
     * @throws IllegalArgumentException
     * @throws FHIRPersistenceException
     */
    public static ResourceDAO getResourceDAO(Connection connection, String adminSchemaName, String schemaName, FHIRDbFlavor flavor,
        FHIRPersistenceJDBCCache cache, Short shardKey) throws IllegalArgumentException, FHIRPersistenceException {
        final ResourceDAO resourceDAO;

        switch (flavor.getType()) {
        case DERBY:
            resourceDAO = new DerbyResourceDAO(connection, schemaName, flavor, cache);
            break;
        case POSTGRESQL:
            resourceDAO = new PostgresResourceDAO(connection, schemaName, flavor, cache, shardKey);
            break;
        case CITUS:
            resourceDAO = new CitusResourceDAO(connection, schemaName, flavor, cache, shardKey);
            break;
        default:
            throw new IllegalArgumentException("Unsupported database type: " + flavor.getType().name());
        }
        return resourceDAO;
    }

    /**
     * Get a standalone DAO to handle the fetch of records from common_token_values 
     * and common_canonical_values
     * @param connection
     * @param schemaName
     * @param flavor
     * @return
     */
    public static CommonValuesDAO getCommonValuesDAO(Connection connection, String adminSchemaName, String schemaName, FHIRDbFlavor flavor) {

        final CommonValuesDAO rrd;
        switch (flavor.getType()) {
        case DERBY:
            rrd = new DerbyCommonValuesDAO(new DerbyTranslator(), connection, schemaName);
            break;
        case POSTGRESQL:
        case CITUS:
            rrd = new CommonValuesDAO(new PostgresTranslator(), connection, schemaName);
            break;
        default:
            throw new IllegalArgumentException("Unsupported database type: " + flavor.getType().name());
        }
        return rrd;
    }

    /**
     * Get an implementation of {@link FhirSequenceDAO} suitable for the database type described
     * by flavor.
     * @param connection
     * @param flavor
     * @return
     */
    public static FhirSequenceDAO getSequenceDAO(Connection connection, FHIRDbFlavor flavor) {
        final FhirSequenceDAO result;
        switch (flavor.getType()) {
        case DERBY:
            result = new org.linuxforhealth.fhir.persistence.jdbc.derby.FhirSequenceDAOImpl(connection);
            break;
        case POSTGRESQL:
        case CITUS:
            result = new org.linuxforhealth.fhir.persistence.jdbc.postgres.FhirSequenceDAOImpl(connection);
            break;
        default:
            throw new IllegalArgumentException("Unsupported database type: " + flavor.getType().name());
        }
        return result;
    }
}