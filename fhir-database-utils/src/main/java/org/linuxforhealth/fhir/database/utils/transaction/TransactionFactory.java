/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.transaction;

import org.linuxforhealth.fhir.database.utils.api.IConnectionProvider;
import org.linuxforhealth.fhir.database.utils.api.ITransaction;

/**
 * Factory to encapsulate handling of transactions, of which there
 * should be only one active on a thread at a time. This helps to
 * ensure consistent (and correct) commit/rollback handling, which
 * otherwise can get a bit messy.
 * <br>
 * Usage
 * <code>
 * IConnectionProvider cp = getConnectionProvider(...);
 * try (ITransaction tx = TransactionFactory.open(cp)) {
 *   try {
 *       doStuff();
 *   }
 *   catch (Exception x) {
 *       tx.setRollbackOnly();
 *   }
 * }
 * </code>
 * The transaction will commit when {@link AutoCloseable#close()} is called, unless
 * setRollbackOnly() was called, in which case a rollback will be performed instead.
 * <br>
 * To be useable in a JEE context, a light refactor is needed so that an alternative
 * solution can be implemented to use UserTransaction.
 *<br>
 */
public class TransactionFactory {

    // thread-safe lazy instantiation implementation of the singleton pattern
    private static class Holder {
        private static final TransactionFactory INSTANCE = new TransactionFactory();
    }
    
    // To record the open transaction on this thread
    private ThreadLocal<SimpleTransaction> active = new ThreadLocal<>();

    /**
     * Private default constructor
     */
    private TransactionFactory() {
        // singleton, so hiding the constructor
    }

    /**
     * Singleton instance getter
     * @return
     */
    public static TransactionFactory getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * Open a transaction on this thread
     * @return
     */
    public static ITransaction openTransaction(IConnectionProvider cp) {
        return getInstance().openTransactionForThread(cp);
    }
    
    /**
     * Getter for the current transaction on this thread
     * @return
     */
    private SimpleTransaction getTransactionForThread() {
        return active.get();
    }
    
    /**
     * Remove the transaction from the current thread
     */
    private void clearTransactionForThread() {
        active.remove();
    }
    
    /**
     * Open a new transaction for this thread.
     * @return
     * @throws IllegalStateException if a transaction is already open
     */
    private ITransaction openTransactionForThread(IConnectionProvider cp) {
        SimpleTransaction result = getTransactionForThread();
        if (result != null) {
            // to get fancy, we could have the transaction record the stack trace for
            // when it was opened
            throw new IllegalStateException("Transaction is already open");
        }
        
        result = new SimpleTransaction(cp);
        active.set(result);
        
        return result;
    }

    /**
     * Get the current transaction.
     * @return the transaction currently open on this thread
     * @throws IllegalStateException if no transaction is open
     */
    public static ITransaction getTransaction() {
        ITransaction result = getInstance().getTransactionForThread();
        if (result == null) {
            throw new IllegalStateException("No transaction is open");
        }
        return result;
    }
    
    /**
     * Remove the transaction from the current thread. Called from
     * {@link SimpleTransaction#close()}.
     */
    protected static void clearTransaction() {
        getInstance().clearTransactionForThread();
    }
}
