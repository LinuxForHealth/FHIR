/*
 * (C) Copyright IBM Corp. 2021
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
public class FHIRPersistenceResourceDoesntExistException extends FHIRPersistenceException {

    private static final long serialVersionUID = -8350452448890112596L;

    public FHIRPersistenceResourceDoesntExistException(String message) {
        super(message);
    }

    public FHIRPersistenceResourceDoesntExistException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public FHIRPersistenceResourceDoesntExistException withIssue(OperationOutcome.Issue... issues) {
        super.withIssue(issues);
        return this;
    }

    @Override
    public FHIRPersistenceResourceDoesntExistException withIssue(Collection<OperationOutcome.Issue> issues) {
        super.withIssue(issues);
        return this;
    }
}
