/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.api;

/**
 * Simple runtime exception so that we can catch and handle
 * where we need
 */
public class DataAccessException extends RuntimeException {

    // All exceptions are serializable
    private static final long serialVersionUID = -3385697603070014498L;
    
    private boolean transactionRetryable;

    /**
     * Public constructor
     * @param msg
     */
    public DataAccessException(String msg) {
        super(msg);
    }

    /**
     * Public constructor
     * @param msg
     * @param t
     */
    public DataAccessException(String msg, Throwable t) {
        super(msg, t);
    }

    /**
     * Public constructor
     * @param t
     */
    public DataAccessException(Throwable t) {
        super(t);
    }
    
    /**
     * Setter for the transactionRetryable flag
     * @param flag
     */
    public void setTransactionRetryable(boolean flag) {
        this.transactionRetryable = flag;
    }

    /**
     * Getter for the transactionRetryable flag
     * @return true if the transaction could be retried
     */
    public boolean isTransactionRetryable() {
        return this.transactionRetryable;
    }
}
