/*
 * (C) Copyright IBM Corp. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.impl;

import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.transaction.Status;
import javax.transaction.Synchronization;
import javax.transaction.TransactionSynchronizationRegistry;

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.config.MetricHandle;
import com.ibm.fhir.persistence.jdbc.FHIRPersistenceJDBCCache;
import com.ibm.fhir.persistence.jdbc.TransactionData;
import com.ibm.fhir.persistence.jdbc.util.FHIRPersistenceJDBCMetric;


/**
 * Adapter to synchronize the thread-local caches with the shared caches when the
 * transaction commits. It's important that we don't share ids created inside a
 * transaction with other threads until the data is committed - those ids are not
 * visible to other threads/transactions (the 'I' in ACID).
 */
public class CacheTransactionSync implements Synchronization {
    private static final Logger logger = Logger.getLogger(CacheTransactionSync.class.getName());

    // The sync registry...and I have no idea why this isn't passed as context to the methods
    private final TransactionSynchronizationRegistry txSyncRegistry;
    
    // The cache delegate to call when we receive an event
    private final FHIRPersistenceJDBCCache cache;
    
    private final String transactionDataKey;

    // Called after the transaction completes (true == committed; false == rolled back)
    private final Consumer<Boolean> afterTransactionHandler;

    // metric used to capture the time it takes us to perform the commit
    private MetricHandle commitTime;

    /**
     * Public constructor
     * 
     * @param txSyncRegistry
     * @param cache
     * @param transactionDataKey
     * @param afterTransactionHandler a handler called after the transaction completes (true == committed; false == rolled back)
     */
    public CacheTransactionSync(TransactionSynchronizationRegistry txSyncRegistry, FHIRPersistenceJDBCCache cache, String transactionDataKey,
            Consumer<Boolean> afterTransactionHandler) {
        this.txSyncRegistry = txSyncRegistry;
        this.cache = cache;
        this.transactionDataKey = transactionDataKey;
        this.afterTransactionHandler = afterTransactionHandler;
    }
    
    @Override
    public void beforeCompletion() {
        // called just before the commit process starts, so we can use this to persist any data we've
        // been holding onto. This makes it really easy to collect parameters during bundle processing
        // and then insert them efficiently using a series of batches. The big benefit here is that
        // it greatly reduces the amount of time we could be locking rows in common_token_values which
        // helps with throughput.
        Object obj = txSyncRegistry.getResource(this.transactionDataKey);
        if (obj != null && obj instanceof TransactionData) {
            // important to log this when debugging because it might not be obvious how this happens
            logger.fine("Persisting TransactionData found in the TransactionSynchronizationRegistry");
            ((TransactionData)obj).persist();
        }

        // Now that we've processed all of our before completion events, we can open the MetricHandle
        // to measure how long it takes to perform the actual transaction commit
        commitTime = FHIRRequestContext.get().getMetricHandle(FHIRPersistenceJDBCMetric.M_JDBC_COMMIT.name());
    }

    @Override
    public void afterCompletion(int status) {
        if (commitTime != null) {
            commitTime.close();
        }
        try (MetricHandle m = FHIRRequestContext.get().getMetricHandle(FHIRPersistenceJDBCMetric.M_JDBC_AFTER_COMMIT.name())) {
            if (status == Status.STATUS_COMMITTED) {
                cache.transactionCommitted();
                if (afterTransactionHandler != null) {
                    afterTransactionHandler.accept(Boolean.TRUE);
                }
            } else {
                // probably a rollback, so throw away everything
                logger.info("Transaction failed - afterCompletion(status = " + translateStatus(status) + ")");
                cache.transactionRolledBack();
    
                if (afterTransactionHandler != null) {
                    afterTransactionHandler.accept(Boolean.FALSE);
                }
            }
        } catch (Exception x) {
            logger.log(Level.WARNING, FHIRPersistenceJDBCMetric.M_JDBC_AFTER_COMMIT.name(), x);
        }
    }

    /**
     * Translate the transaction Status value to a meaningful name
     * @param status a value from {@link javax.transaction.Status}
     * @return a string describing the transaction status
     */
    public static String translateStatus(int status) {
        String result;
        switch (status) {
        case Status.STATUS_ACTIVE:
            result = "STATUS_ACTIVE";
            break;
        case Status.STATUS_MARKED_ROLLBACK:
            result = "STATUS_MARKED_ROLLBACK";
            break;
        case Status.STATUS_PREPARED:
            result = "STATUS_PREPARED";
            break;
        case Status.STATUS_COMMITTED:
            result = "STATUS_COMMITTED";
            break;
        case Status.STATUS_ROLLEDBACK:
            result = "STATUS_ROLLEDBACK";
            break;
        case Status.STATUS_UNKNOWN:
            result = "STATUS_UNKNOWN";
            break;
        case Status.STATUS_NO_TRANSACTION:
            result = "STATUS_NO_TRANSACTION";
            break;
        case Status.STATUS_PREPARING:
            result = "STATUS_PREPARING";
            break;
        case Status.STATUS_COMMITTING:
            result = "STATUS_COMMITTING";
            break;
        case Status.STATUS_ROLLING_BACK:
            result = "STATUS_ROLLING_BACK";
            break;
        default:
            result = "INVALID_" + status;
            break;
        }
        return result;
    }
}