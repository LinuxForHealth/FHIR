/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc;

import java.sql.Connection;

import javax.transaction.TransactionSynchronizationRegistry;

import com.ibm.fhir.database.utils.db2.Db2Translator;
import com.ibm.fhir.database.utils.derby.DerbyTranslator;
import com.ibm.fhir.database.utils.postgresql.PostgreSqlTranslator;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.connection.FHIRDbFlavor;
import com.ibm.fhir.persistence.jdbc.dao.api.IResourceReferenceCache;
import com.ibm.fhir.persistence.jdbc.dao.api.IResourceReferenceDAO;
import com.ibm.fhir.persistence.jdbc.dao.api.ResourceDAO;
import com.ibm.fhir.persistence.jdbc.dao.impl.ResourceDAOImpl;
import com.ibm.fhir.persistence.jdbc.dao.impl.ResourceReferenceDAO;
import com.ibm.fhir.persistence.jdbc.derby.DerbyResourceDAO;
import com.ibm.fhir.persistence.jdbc.postgresql.PostgreSqlResourceDAO;

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
    public static ResourceDAO getResourceDAO(Connection connection, String schemaName, FHIRDbFlavor flavor, TransactionSynchronizationRegistry trxSynchRegistry, 
        IResourceReferenceCache cache)
        throws IllegalArgumentException, FHIRPersistenceException {
        ResourceDAO resourceDAO = null;
        
        IResourceReferenceDAO rrd;
        switch (flavor.getType()) {
        case DB2:
            rrd = new ResourceReferenceDAO(new Db2Translator(), connection, schemaName, cache);
            resourceDAO = new ResourceDAOImpl(connection, schemaName, flavor, trxSynchRegistry, rrd);
            break;
        case DERBY:
            rrd = new ResourceReferenceDAO(new DerbyTranslator(), connection, schemaName, cache);
            resourceDAO = new DerbyResourceDAO(connection, schemaName, flavor, trxSynchRegistry, rrd);
            break;
        case POSTGRESQL:
            rrd = new ResourceReferenceDAO(new PostgreSqlTranslator(), connection, schemaName, cache);
            resourceDAO = new PostgreSqlResourceDAO(connection, schemaName, flavor, trxSynchRegistry, rrd);
            break;
        }
        return resourceDAO;
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
    public static ResourceDAO getResourceDAO(Connection connection, String schemaName, FHIRDbFlavor flavor, 
        IResourceReferenceCache cache) throws IllegalArgumentException, FHIRPersistenceException {
        ResourceDAO resourceDAO = null;
        
        IResourceReferenceDAO rrd;
        switch (flavor.getType()) {
        case DB2:
            rrd = new ResourceReferenceDAO(new Db2Translator(), connection, schemaName, cache);
            resourceDAO = new ResourceDAOImpl(connection, schemaName, flavor, rrd);
            break;
        case DERBY:
            rrd = new ResourceReferenceDAO(new DerbyTranslator(), connection, schemaName, cache);
            resourceDAO = new DerbyResourceDAO(connection, schemaName, flavor, rrd);
            break;
        case POSTGRESQL:
            rrd = new ResourceReferenceDAO(new PostgreSqlTranslator(), connection, schemaName, cache);
            resourceDAO = new PostgreSqlResourceDAO(connection, schemaName, flavor, rrd);
            break;
        }
        return resourceDAO;
    }
}
