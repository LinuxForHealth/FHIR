/*
 * (C) Copyright IBM Corp. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.connection;

import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.database.utils.api.IConnectionProvider;
import org.linuxforhealth.fhir.database.utils.api.ITransaction;
import org.linuxforhealth.fhir.database.utils.transaction.SimpleTransactionProvider;
import org.linuxforhealth.fhir.persistence.FHIRPersistenceTransaction;
import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceDataAccessException;
import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceException;

/**
 * Hides the logic behind obtaining a JDBC {@link Connection} from the DAO code.
 *
 * Use by unit tests or other scenarios where connections are obtained using an
 * IConnectionProvider implementation, outside the scope of a JEE container.
 * Transactions are managed with the help of the SimpleTransactionProvider and
 * wrapped by this class, meaning we have a uniform interface for handling
 * transactions across JEE and unit-test scenarios.
 *
 */
public class FHIRTestTransactionAdapter implements FHIRPersistenceTransaction {
    private static final Logger log = Logger.getLogger(FHIRDbTestConnectionStrategy.class.getName());

    // Provides connections when outside of a container
    private final IConnectionProvider connectionProvider;

    // If given, invoke this callback just prior to commit
    private final IFHIRTransactionAdapterCallback beforeCommitCallback;

    // Support transactions for the persistence unit tests
    private final SimpleTransactionProvider transactionProvider;

    // Just in case we have nesting issues, use ThreadLocal to track the current tx
    private final ThreadLocal<ITransaction> currentTransaction = new ThreadLocal<>();

    // Was this instance responsible for starting the transaction
    private boolean startedByThis;

    // support nesting by tracking the number of begin/end requests
    private int startCount;

    /**
     * Public constructor
     * @param cp
     * @param beforeCommit callback before transaction commit, can be null
     */
    public FHIRTestTransactionAdapter(IConnectionProvider cp, IFHIRTransactionAdapterCallback beforeCommitCallback) {
        this.connectionProvider = cp;
        this.beforeCommitCallback = beforeCommitCallback;

        // provide transaction support for our tests
        this.transactionProvider = new SimpleTransactionProvider(this.connectionProvider);
    }

    @Override
    public void begin() throws FHIRPersistenceException {
        if (currentTransaction.get() == null) {
            // allocate a new transaction
            this.currentTransaction.set(this.transactionProvider.getTransaction());
            this.startedByThis = true;
        }

        // add to the start request counter every time. We only close when this
        // counter reaches 0.
        startCount++;
    }

    @Override
    public void end() throws FHIRPersistenceException {
        if (currentTransaction.get() == null) {
            throw new FHIRPersistenceDataAccessException("Transaction not started");
        }

        try {
            // only end it if we started it
            if (startedByThis && --startCount == 0) {
                try (ITransaction tx = this.currentTransaction.get()) {
                    if (this.beforeCommitCallback != null) {
                        this.beforeCommitCallback.beforeCommit();
                    }
                }
            }
        } catch (Throwable x) {
            // translate to a FHIRPersistenceException
            FHIRPersistenceException fx = new FHIRPersistenceException("Unexpected error while committing a transaction.");
            log.log(Level.SEVERE, fx.getMessage(), x);
            throw fx;
        } finally {
            this.currentTransaction.set(null);
            this.startedByThis = false;
        }
    }

    @Override
    public void setRollbackOnly() throws FHIRPersistenceException {
        if (currentTransaction.get() == null) {
            throw new FHIRPersistenceDataAccessException("Transaction not started");
        }

        // always mark the transaction for rollback, even if not started by this
        this.currentTransaction.get().setRollbackOnly();
    }

    @Override
    public boolean hasBegun() throws FHIRPersistenceException {
        return currentTransaction.get() != null;
    }
}
