/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.impl;

import java.util.logging.Logger;

import javax.transaction.Status;
import javax.transaction.Synchronization;

import com.ibm.fhir.persistence.jdbc.FHIRPersistenceJDBCCache;


/**
 * Adapter to synchronize the thread-local caches with the shared caches when the
 * transaction commits. It's important that we don't share ids created inside a
 * transaction with other threads until the data is committed - those ids are not
 * visible to other threads/transactions (the 'I' in ACID).
 */
public class CacheTransactionSync implements Synchronization {
    private static final Logger logger = Logger.getLogger(CacheTransactionSync.class.getName());

    // The cache delegate to call when we receive an event
    private final FHIRPersistenceJDBCCache cache;

    /**
     * Public constructor
     * @param cacheImpl
     */
    public CacheTransactionSync(FHIRPersistenceJDBCCache cache) {
        this.cache = cache;
    }
    
    @Override
    public void beforeCompletion() {
        // Transaction about to commit...we don't care so it's a NOP
    }

    @Override
    public void afterCompletion(int status) {
        if (status == Status.STATUS_COMMITTED) {
            cache.transactionCommitted();
        } else {
            // probably a rollback, so throw away everything
            logger.info("Transaction failed - afterCompletion(status = " + status + ")");
            cache.transactionRolledBack();
        }
    }
}