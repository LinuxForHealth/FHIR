/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.connection;

import java.sql.Connection;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.dao.impl.FHIRDbDAOImpl;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDataAccessException;


/**
 * Abstraction used to obtain JDBC connections.
 * 
 * @implNote Refactor of the getConnection logic from {@link FHIRDbDAOImpl}. This
 * isolates connection logic from the DAO implementations, promoting separation of
 * concerns, and makes it possible to use different strategies in the future,
 * without having to disrupt the (complex) DAO code again.
 */
public interface FHIRDbConnectionStrategy {

    /**
     * Get a connection to the desired data source.
     * @return a {@link Connection}. Never null.
     */
    public Connection getConnection() throws FHIRPersistenceDBConnectException;

    /**
     * Get the flavor of the database we are working with to reveal its capabilities
     * @return the datastore/source flavor from the FHIR configuration
     * @throws FHIRPersistenceDataAccessException if there is an issue with the configuration
     */
    public FHIRDbFlavor getFlavor() throws FHIRPersistenceDataAccessException;
    
    /**
     * Start a new transaction. Only one transaction is supported on a thread.
     * No support is provided for nested transactions
     * @throws FHIRPersistenceException
     */
    public void txBegin() throws FHIRPersistenceException;
    
    /**
     * End the current transaction by calling commit, or rollback
     * if setRollbackOnly() has been called prior.
     */
    public void txEnd() throws FHIRPersistenceException;

    /**
     * Mark the current transaction so that it can only be rolled back
     * when the application attempts to close the transaction.
     */
    public void txSetRollbackOnly() throws FHIRPersistenceException;
}
