/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.api;

/**
 * When there is an issue removing the Tablespace
 */
public class TableSpaceRemovalException extends DataAccessException {

    // All exceptions are serializable
    private static final long serialVersionUID = -338568883070014498L;

    private boolean transactionRetryable;

    /**
     * Public constructor
     * @param msg
     */
    public TableSpaceRemovalException(String msg) {
        super(msg);
    }

    /**
     * Public constructor
     * @param msg
     * @param t
     */
    public TableSpaceRemovalException(String msg, Throwable t) {
        super(msg, t);
    }

    /**
     * Public constructor
     * @param t
     */
    public TableSpaceRemovalException(Throwable t) {
        super(t);
    }

    /**
     * Setter for the transactionRetryable flag
     * @param flag
     */
    @Override
    public void setTransactionRetryable(boolean flag) {
        this.transactionRetryable = flag;
    }

    /**
     * Getter for the transactionRetryable flag
     * @return true if the transaction could be retried
     */
    @Override
    public boolean isTransactionRetryable() {
        return this.transactionRetryable;
    }
}
