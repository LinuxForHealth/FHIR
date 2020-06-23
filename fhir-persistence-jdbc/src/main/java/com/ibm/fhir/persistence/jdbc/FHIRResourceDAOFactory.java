/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc;

import java.sql.Connection;

import javax.transaction.TransactionSynchronizationRegistry;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.connection.FHIRDbFlavor;
import com.ibm.fhir.persistence.jdbc.dao.api.ResourceDAO;
import com.ibm.fhir.persistence.jdbc.dao.impl.ResourceDAOImpl;
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
    public static ResourceDAO getResourceDAO(Connection connection, String schemaName, FHIRDbFlavor flavor, TransactionSynchronizationRegistry trxSynchRegistry)
        throws IllegalArgumentException, FHIRPersistenceException {
        ResourceDAO resourceDAO = null;
        
        switch (flavor.getType()) {
        case DB2:
            resourceDAO = new ResourceDAOImpl(connection, schemaName, flavor, trxSynchRegistry);
            break;
        case DERBY:
            resourceDAO = new DerbyResourceDAO(connection, schemaName, flavor, trxSynchRegistry);
            break;
        case POSTGRESQL:
            resourceDAO = new PostgreSqlResourceDAO(connection, schemaName, flavor, trxSynchRegistry);
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
    public static ResourceDAO getResourceDAO(Connection connection, String schemaName, FHIRDbFlavor flavor) throws IllegalArgumentException, FHIRPersistenceException {
        ResourceDAO resourceDAO = null;
        switch (flavor.getType()) {
        case DB2:
            resourceDAO = new ResourceDAOImpl(connection, schemaName, flavor);
            break;
        case DERBY:
            resourceDAO = new DerbyResourceDAO(connection, schemaName, flavor);
            break;
        case POSTGRESQL:
            resourceDAO = new PostgreSqlResourceDAO(connection, schemaName, flavor);
            break;
        }
        return resourceDAO;
    }

}
