/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.connection;

import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.transaction.Status;
import javax.transaction.TransactionSynchronizationRegistry;
import javax.transaction.UserTransaction;

import org.linuxforhealth.fhir.config.FHIRRequestContext;
import org.linuxforhealth.fhir.persistence.FHIRPersistenceTransaction;
import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceDataAccessException;
import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceException;
import org.linuxforhealth.fhir.persistence.jdbc.FHIRPersistenceJDBCCache;
import org.linuxforhealth.fhir.persistence.jdbc.impl.CacheTransactionSync;


/**
 * Adapter to simplify transaction handling. This object is returned by
 * FHIRPersistenceJDBCImpl and can be used instead of the old FHIRTransactionHelper
 */
public class FHIRUserTransactionAdapter implements FHIRPersistenceTransaction {
    private static final Logger log = Logger.getLogger(FHIRUserTransactionAdapter.class.getName());

    // The connection strategy handling the underlying transaction object
    private final UserTransaction userTransaction;

    // The sync registry to send tx commit events to the cache
    private final TransactionSynchronizationRegistry syncRegistry;

    // The cache which needs to be notified when a commit completes
    private final FHIRPersistenceJDBCCache cache;

    // The resource key used for the TransactionData object in held in the TransactionSynchronizationRegistry
    private final String transactionDataKey;

    // Did this instance start the transaction?
    private boolean startedByThis;

    // support nesting by tracking the number of begin/end requests
    private int startCount;

    // A handler to be called after a transaction has completed
    private final Consumer<Boolean> afterTransactionHandler;

    /**
     * Public constructor
     * @param tx
     * @param syncRegistry
     * @param cache
     * @param transactionDataKey
     * @param afterTransactionHandler
     */
    public FHIRUserTransactionAdapter(UserTransaction tx, TransactionSynchronizationRegistry syncRegistry, FHIRPersistenceJDBCCache cache,
            String transactionDataKey, Consumer<Boolean> afterTransactionHandler) {
        this.userTransaction = tx;
        this.syncRegistry = syncRegistry;
        this.cache = cache;
        this.transactionDataKey = transactionDataKey;
        startedByThis = false;
        this.afterTransactionHandler = afterTransactionHandler;
    }

    /**
     * If a transaction has not yet been started on this thread, then start one.
     * @throws FHIRPersistenceException
     */
    @Override
    public void begin() throws FHIRPersistenceException {

        FHIRRequestContext ctx = FHIRRequestContext.get();
        boolean bulk = ctx != null && ctx.isBulk();

        int status = getStatus();
        if (isNoTransaction(status)) {
            // This instance will be responsible for starting and ending
            // the transaction
            log.fine("No current transaction. Starting new transaction on current thread.");
            try {
                userTransaction.begin();
                startedByThis = true;
                this.startCount++;

                // On starting a new transaction, we need to register a callback so that
                // the cache is informed when the transaction commits it can promote thread-local
                // ids to the shared caches.
                syncRegistry.registerInterposedSynchronization(new CacheTransactionSync(this.syncRegistry, this.cache, this.transactionDataKey, this.afterTransactionHandler));

            } catch (Exception x) {
                log.log(Level.SEVERE, "failed to start transaction", x);
                throw new FHIRPersistenceDataAccessException("Start global transaction failed. See server log for details");
            }
        } else if (isMarkedForRollback(status)) {
            // the transaction is active but has already been marked for rollback. This is OK,
            // we just behave as though we're a nested transaction
            this.startCount++;
        } else if (isActive(status) && bulk) {
            // On starting a bulk transaction, we need to register a callback so that
            // the cache is informed when the transaction commits it can promote thread-local
            // ids to the shared caches.
            if (!ctx.isBulkTransactionConfigured()) {
                // Only register on the first encounter
                syncRegistry.registerInterposedSynchronization(new CacheTransactionSync(this.syncRegistry, this.cache, this.transactionDataKey, this.afterTransactionHandler));
                ctx.setBulkTransactionConfigured(true);
            }
            // transaction is already active, so this is a nested request
            this.startCount++;
        } else if (isActive(status)) {
            // transaction is already active, so this is a nested request
            this.startCount++;
        } else {
            // any other status means that we can't begin a new transaction here
            throw new FHIRPersistenceDataAccessException("Cannot begin transaction. Status=" + status);
        }
    }

    /**
     * If we previously started a transaction on this thread using this helper instance,
     * then commit it now.
     * @throws FHIRPersistenceException
     */
    @Override
    public void end() throws FHIRPersistenceException {
        // If we previously started a transaction with this instance and we are the outermost begin/end pair, then commit now.
        if (--startCount == 0 && startedByThis) {
            int status = getStatus();
            if (isActive(status)) {
                // transaction started by this instance and is active
                log.fine("Committing transaction on current thread...");

                try {
                    userTransaction.commit();
                } catch (Exception x) {
                    log.log(Level.SEVERE, "failed to commit transaction", x);
                    throw new FHIRPersistenceDataAccessException("Commit global transaction failed. See server log for details");
                } finally {
                    startedByThis = false;
                }
            } else if (isMarkedForRollback(status)) {
                // transaction started by this instance but has been marked for rollback
                log.fine("Rolling back transaction on current thread...");
                try {
                    userTransaction.rollback();
                } catch (Exception x) {
                    log.log(Level.SEVERE, "failed to rollback transaction", x);
                    throw new FHIRPersistenceDataAccessException("Rollback global transaction failed. See server log for details");
                } finally {
                    startedByThis = false;
                }
            } else {
                // Did not expect the transaction to be in this state
                throw new FHIRPersistenceException("unexpected transaction status: " + status);
            }
        } else {
            // The commit will be handled by the first instance which started the transaction, not this one
            log.fine("This FHIRPersistenceTransaction instance did not start the transaction, so skipping commit");
        }
    }

    @Override
    public void setRollbackOnly() throws FHIRPersistenceException {
        // trap door - mark for rollback, *even* if we didn't start the transaction
        int status = getStatus();
        try {
            // Only mark the transaction for rollback if we started it. This is necessary
            // to correctly handle nested calls in the current JDBC persistence layer.
            // This behavior should be reconsidered so that any layer can mark the
            // transaction for rollback if the error is fatal. Needs a closer look
            // at what constitutes an expected vs. unexpected failure
            if (this.startedByThis && startCount == 1) {
                if (isActive(status)) {
                    log.fine("Marking transaction for rollback.");
                    this.userTransaction.setRollbackOnly();
                } else if (!isMarkedForRollback(status)) {
                    log.warning("mark for rollback - transaction not active, status=" + status);
                    throw new FHIRPersistenceDataAccessException("No current transaction to mark for rollback");
                }
            } else {
                log.fine("Transaction not started by this so ignoring setRollbackOnly");
            }
        } catch (Exception x) {
            log.log(Level.SEVERE, "failed to start transaction, status=" + status, x);
            throw new FHIRPersistenceDataAccessException("Start global transaction failed. See server log for details");
        }
    }

    @Override
    public boolean isTransactional() {

        // well, yes of course we are
        return true;
    }

    /**
     * Is there a transaction currently on this thread?
     * @return true if a global transaction is active on the current thread
     */
    protected boolean isActive(int status) {
        return status == Status.STATUS_ACTIVE;
    }

    /**
     * Has this transaction been marked for rollback?
     * @return
     */
    protected boolean isMarkedForRollback(int status) {
        return status == Status.STATUS_MARKED_ROLLBACK;
    }

    /**
     * Are we in the NO TRANSACTION state?
     * @param status
     * @return
     */
    protected boolean isNoTransaction(int status) {
        return status == Status.STATUS_NO_TRANSACTION;
    }

    /**
     * Get the {@link Status} of the global transaction
     * @return
     * @throws FHIRPersistenceDataAccessException
     */
    protected int getStatus() throws FHIRPersistenceDataAccessException {
        try {
            return userTransaction.getStatus();
        } catch (Throwable e) {
            String msg = "An unexpected error occurred while examining transactional status.";
            FHIRPersistenceDataAccessException fx = new FHIRPersistenceDataAccessException(msg);
            log.log(Level.SEVERE, fx.getMessage(), e);
            throw fx;
        }
    }

    /**
     * Call the supplier function within a begin/end
     * @param <T>
     * @param s
     * @return
     */
    <T> T func(Supplier<T> s) throws FHIRPersistenceException {
        try {
            begin();
            return s.get();
        } finally {
            end();
        }
    }

    @Override
    public boolean hasBegun() throws FHIRPersistenceException {
        // doesn't matter who started it, just want to find out if we are currently
        // inside a transaction
        int status = getStatus();
        return !isNoTransaction(status);
    }
}