/*
 * (C) Copyright IBM Corp. 2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.exception;

import java.util.Collection;

import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.persistence.exception.FHIRPersistenceDataAccessException;

/**
 * This exception class is thrown when Foreign Key violations are encountered while attempting to access data in the FHIR DB.
 */
public class FHIRPersistenceFKVException extends FHIRPersistenceDataAccessException {

    private static final long serialVersionUID = 4303342119803229856L;

    public FHIRPersistenceFKVException(String message) {
        super(message);
    }

    public FHIRPersistenceFKVException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public FHIRPersistenceFKVException withIssue(OperationOutcome.Issue... issues) {
        super.withIssue(issues);
        return this;
    }
    
    @Override
    public FHIRPersistenceFKVException withIssue(Collection<OperationOutcome.Issue> issues) {
        super.withIssue(issues);
        return this;
    }

}
