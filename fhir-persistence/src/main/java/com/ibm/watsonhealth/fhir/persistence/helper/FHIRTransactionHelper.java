/**
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.helper;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.watsonhealth.fhir.persistence.FHIRPersistenceTransaction;
import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceException;

/**
 * This helper class is used to manage the transaction on the current thread.
 */
public class FHIRTransactionHelper {
    private static final Logger log = Logger.getLogger(FHIRTransactionHelper.class.getName());
    
    private FHIRPersistenceTransaction txn;
    private boolean txnStarted;
    private boolean beginCalled;
    
    public FHIRTransactionHelper(FHIRPersistenceTransaction txn) {
        this.txn = txn;
        txnStarted = false;
    }
    
    /**
     * If a transaction has not yet been started on this thread, then start one.
     * @throws FHIRPersistenceException
     */
    public void begin() throws FHIRPersistenceException {
        if (txn != null) {
            if (!txn.isActive()) {
                log.fine("Starting transaction on current thread...");
                txn.begin();
                txnStarted = true;
            } else {
                log.fine("Transaction is already active on current thread...");
            }
            beginCalled = true;
        }
    }
    
    /**
     * If we previously started a transaction on this thread using this helper instance,
     * then commit it now.
     * @throws FHIRPersistenceException
     */
    public void commit() throws FHIRPersistenceException {
        // If we previously started a transaction with this helper instance, then commit now.
        if (txn != null) {
            if (txnStarted) {
                log.fine("Committing transaction on current thread...");
                txn.commit();
                txnStarted = false;
            } else {
                log.fine("Bypassing commit of already-active transaction on current thread...");
            }
            txn = null;
        }
    }
    
    /**
     * If we previously started a transaction on this thread using this helper instance,
     * then perform a rollback now; otherwise, set the transaction as 'rollback only' to
     * prevent it from being committed later.
     */
    public void rollback() {
        // Perform a "rollback" or "setRollbackOnly" as appropriate.
        if (txn != null && beginCalled) {
            String operation = "???";
            try {
                if (txnStarted) {
                    log.fine("Performing a rollback of transaction on current thread...");
                    operation = "rollback";
                    txn.rollback();
                } else if (txn.isActive()) {
                    log.fine("Performing a set-rollback-only on already-active transaction on current thread...");
                    operation = "set-rollback-only";
                    txn.setRollbackOnly();
                }
            } catch (Throwable t) {
                String msg = "Unexpected exception while trying to perform a transaction '" + operation + "': " + t.getMessage();
                log.log(Level.SEVERE, msg, t);
            }
        }
    }
}
