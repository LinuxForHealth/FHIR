/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.connection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.IConnectionProvider;
import com.ibm.fhir.database.utils.api.ITransaction;
import com.ibm.fhir.database.utils.transaction.SimpleTransactionProvider;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDataAccessException;


/**
 * Hides the logic behind obtaining a JDBC {@link Connection} from the DAO code.
 * 
 * This strategy object is local to a thread, and because we use the same
 * underlying connection to our test database within a transaction, we
 * only need to configure the connection once. We can track this initialization
 * with nothing more complicated than a Boolean flag.
 * 
 * Use by unit tests or other scenarios where connections are obtained using an
 * IConnectionProvider implementation, outside the scope of a JEE container. For
 * example, this is used when connecting to in-memory instances of Derby when
 * running persistence layer unit-tests.
 * 
 */
public class FHIRDbTestConnectionStrategy implements FHIRDbConnectionStrategy {
    private static final Logger log = Logger.getLogger(FHIRDbTestConnectionStrategy.class.getName());
    
    // Provides connections when outside of a container
    private final IConnectionProvider connectionProvider;
    
    private boolean initialized = false;

    // Action to take to initialize a new connection
    private final Action action;
    
    // The type and capability of the database we connect to
    private final FHIRDbFlavor flavor;
    
    // Support transactions for the persistence unit tests
    private final SimpleTransactionProvider transactionProvider;
    
    // Represents the current transaction if not null
    private ITransaction currentTransaction;
        
    /**
     * Public constructor
     * @param cp
     */
    public FHIRDbTestConnectionStrategy(IConnectionProvider cp, Action action) {
        this.connectionProvider = cp;
        this.action = action;

        // we don't support multi-tenancy in our unit-test database
        flavor = new FHIRDbFlavorImpl(cp.getTranslator().getType(), false);
        
        // provide transaction support for our tests
        this.transactionProvider = new SimpleTransactionProvider(this.connectionProvider);
    }

    /* (non-Javadoc)
     * @see com.ibm.fhir.persistence.jdbc.connection.FHIRDbConnectionStrategy#getConnection()
     */
    @Override
    public Connection getConnection() throws FHIRPersistenceDBConnectException {
        try {
            // The connection we get from this connection provider will be wrapped, so it
            // can be closed by the caller.
            Connection result = connectionProvider.getConnection();

            if (this.action != null && !this.initialized) {
                log.fine("Initializing new connection");
                try {
                    action.performOn(result);
                    this.initialized = true;
                    
                    log.fine("Connection initialized");
                }
                catch (Throwable t) {
                    // inialization failed, but the connection is open so we need to close it
                    log.severe("Connection initialization failed");
                    result.close();
                    throw t;
                }
            }
            
            return result;
        }
        catch (SQLException x) {
            throw new FHIRPersistenceDBConnectException("Could not connect to database");
        }
    }

    /* (non-Javadoc)
     * @see com.ibm.fhir.persistence.jdbc.connection.FHIRDbConnectionStrategy#getType()
     */
    @Override
    public FHIRDbFlavor getFlavor() throws FHIRPersistenceDataAccessException {
        return this.flavor;
    }

    /* (non-Javadoc)
     * @see com.ibm.fhir.persistence.jdbc.connection.FHIRDbConnectionStrategy#commit()
     */
    @Override
    public void txEnd() throws FHIRPersistenceException {
        if (currentTransaction == null) {
            throw new FHIRPersistenceDataAccessException("Transaction not started");
        }
        try {
            this.currentTransaction.close();
        }
        catch (Throwable x) {
            // translate to a FHIRPersistenceException
            FHIRPersistenceException fx = new FHIRPersistenceException("Unexpected error while committing a transaction.");
            log.log(Level.SEVERE, fx.getMessage(), x);
            throw fx;
        }
        finally {
            this.currentTransaction = null;
        }
    }

    /* (non-Javadoc)
     * @see com.ibm.fhir.persistence.jdbc.connection.FHIRDbConnectionStrategy#setRollbackOnly()
     */
    @Override
    public void txSetRollbackOnly() throws FHIRPersistenceException {
        if (currentTransaction == null) {
            throw new FHIRPersistenceDataAccessException("Transaction not started");
        }
        
        this.currentTransaction.setRollbackOnly();
    }

    /* (non-Javadoc)
     * @see com.ibm.fhir.persistence.jdbc.connection.FHIRDbConnectionStrategy#begin()
     */
    @Override
    public void txBegin() throws FHIRPersistenceException {
        if (currentTransaction != null) {
            throw new FHIRPersistenceDataAccessException("Transaction already active on this thread");
        }
        
        // allocate a new transaction
        this.currentTransaction = this.transactionProvider.getTransaction();
    }

}
