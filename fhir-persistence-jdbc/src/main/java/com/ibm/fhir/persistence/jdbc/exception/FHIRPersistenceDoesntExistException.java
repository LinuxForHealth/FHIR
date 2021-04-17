/*
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.exception;

import java.util.Collection;

import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;

/**
 * This is an exception indicating that the resource does not exist
 */
public class FHIRPersistenceDoesntExistException extends FHIRPersistenceException {

    private static final long serialVersionUID = -8350452448890112596L;

    // hint as to whether or not the transaction can be retried
    private boolean transactionRetryable = false;

    public FHIRPersistenceDoesntExistException(String message) {
        super(message);
    }

    public FHIRPersistenceDoesntExistException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public FHIRPersistenceDoesntExistException withIssue(OperationOutcome.Issue... issues) {
        super.withIssue(issues);
        return this;
    }

    @Override
    public FHIRPersistenceDoesntExistException withIssue(Collection<OperationOutcome.Issue> issues) {
        super.withIssue(issues);
        return this;
    }
}