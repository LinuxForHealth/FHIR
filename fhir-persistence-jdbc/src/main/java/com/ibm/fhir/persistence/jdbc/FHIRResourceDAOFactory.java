/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc;

import javax.transaction.TransactionSynchronizationRegistry;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.connection.FHIRDbConnectionStrategy;
import com.ibm.fhir.persistence.jdbc.dao.api.ResourceDAO;
import com.ibm.fhir.persistence.jdbc.dao.impl.ResourceDAOImpl;
import com.ibm.fhir.persistence.jdbc.derby.DerbyResourceDAO;
import com.ibm.fhir.persistence.jdbc.postgresql.PostgreSqlResourceDAO;

public class FHIRResourceDAOFactory {

    /**
     * Construct a new ResourceDAO implementation matching the database type
     * @param strat
     * @param trxSynchRegistry
     * @return a concrete implementation of {@link ResourceDAO}
     * @throws IllegalArgumentException
     * @throws FHIRPersistenceException
     */
    public static ResourceDAO getResourceDAO(FHIRDbConnectionStrategy strat, TransactionSynchronizationRegistry trxSynchRegistry)
        throws IllegalArgumentException, FHIRPersistenceException {
        ResourceDAO resourceDAO = null;
        switch (strat.getFlavor().getType()) {
        case DB2:
            resourceDAO = new ResourceDAOImpl(strat, trxSynchRegistry);
            break;
        case Derby:
            resourceDAO = new DerbyResourceDAO(strat, trxSynchRegistry);
            break;
        case PostgreSQL:
            resourceDAO = new PostgreSqlResourceDAO(strat, trxSynchRegistry);
            break;
        }
        return resourceDAO;
    }

    /**
     * Construct a new ResourceDAO implementation matching the database type
     * @param strat
     * @return a concrete implementation of {@link ResourceDAO}
     * @throws IllegalArgumentException
     * @throws FHIRPersistenceException
     */
    public static ResourceDAO getResourceDAO(FHIRDbConnectionStrategy strat) throws IllegalArgumentException, FHIRPersistenceException {
        ResourceDAO resourceDAO = null;
        switch (strat.getFlavor().getType()) {
        case DB2:
            resourceDAO = new ResourceDAOImpl(strat);
            break;
        case Derby:
            resourceDAO = new DerbyResourceDAO(strat);
            break;
        case PostgreSQL:
            resourceDAO = new PostgreSqlResourceDAO(strat);
            break;
        }
        return resourceDAO;
    }

}
