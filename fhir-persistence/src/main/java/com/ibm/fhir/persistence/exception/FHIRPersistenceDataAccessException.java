/*
 * (C) Copyright IBM Corp. 2017, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.exception;

import java.util.Collection;

import com.ibm.fhir.model.resource.OperationOutcome;

/**
 * This exception class represents failures encountered while attempting to access (read, write) data in the FHIR DB. 
 * TODO replace with Builder fluent pattern to align with the new R4 model style
 */
public class FHIRPersistenceDataAccessException extends FHIRPersistenceException {

    private static final long serialVersionUID = -8350452448890342596L;
    
    // hint as to whether or not the transaction can be retried
    private boolean transactionRetryable;

    public FHIRPersistenceDataAccessException(String message) {
        super(message);
    }

    public FHIRPersistenceDataAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public FHIRPersistenceDataAccessException withIssue(OperationOutcome.Issue... issues) {
        super.withIssue(issues);
        return this;
    }
    
    @Override
    public FHIRPersistenceDataAccessException withIssue(Collection<OperationOutcome.Issue> issues) {
        super.withIssue(issues);
        return this;
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