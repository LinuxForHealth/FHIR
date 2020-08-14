/*
 * (C) Copyright IBM Corp. 2016,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.exception;

import java.util.Collection;

import com.ibm.fhir.model.resource.OperationOutcome;

/**
 * Thrown for methods or features not yet fully implemented.
 */
public class FHIRPersistenceNotSupportedException extends FHIRPersistenceException {

    private static final long serialVersionUID = 1L;

    public FHIRPersistenceNotSupportedException(String message) {
        super(message);
    }

    public FHIRPersistenceNotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public FHIRPersistenceNotSupportedException withIssue(OperationOutcome.Issue... issues) {
        super.withIssue(issues);
        return this;
    }

    @Override
    public FHIRPersistenceNotSupportedException withIssue(Collection<OperationOutcome.Issue> issues) {
        super.withIssue(issues);
        return this;
    }
}
