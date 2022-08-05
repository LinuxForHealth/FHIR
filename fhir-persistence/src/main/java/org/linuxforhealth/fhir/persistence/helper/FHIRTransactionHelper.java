/*
 * (C) Copyright IBM Corp. 2017, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.helper;

import org.linuxforhealth.fhir.persistence.FHIRPersistenceTransaction;
import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceException;

/**
 * This helper class is used to manage the transaction on the current thread.
 *
 * @implNote After 4.3, the logic to check for which instance of this helper
 *           owns the transaction (txnStarted == true) is moved to the
 *           FHIRPersistenceTransaction implementation, making use of this
 *           helper class optional.
 */
public class FHIRTransactionHelper {

    // The transaction handle we are wrapping
    private FHIRPersistenceTransaction txn;

    public FHIRTransactionHelper(FHIRPersistenceTransaction txn) {
        this.txn = txn;
    }

    /**
     * If a transaction has not yet been started on this thread, then start one.
     * @throws FHIRPersistenceException
     */
    public void begin() throws FHIRPersistenceException {
        txn.begin();
    }

    /**
     * If we previously started a transaction on this thread using this helper instance,
     * then commit it now.
     * @throws FHIRPersistenceException
     */
    public void commit() throws FHIRPersistenceException {
        txn.end();
    }

    /**
     * Same as commit, but is preferred for readability because
     * {@link #commit()} will actually do a rollback if setRollbackOnly
     * is called on the underlying transaction
     * @throws FHIRPersistenceException
     */
    public void end() throws FHIRPersistenceException {
        txn.end();
    }

    /**
     * If we previously started a transaction on this thread using this helper instance,
     * then perform a rollback now; otherwise, set the transaction as 'rollback only' to
     * prevent it from being committed later.
     */
    public void rollback() throws FHIRPersistenceException {
        txn.setRollbackOnly();
        txn.end();
    }

    /**
     * Mark the current transaction for rollback.
     * @throws FHIRPersistenceException
     */
    public void setRollbackOnly() throws FHIRPersistenceException {
        txn.setRollbackOnly();
    }

    /**
     * Find out if we're currently in a transaction
     * @return
     * @throws FHIRPersistenceException
     */
    public boolean hasBegun() throws FHIRPersistenceException {
        return txn.hasBegun();
    }
}