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
 * This exception class represents failures encountered while attempting to close/cleanup JDBC resources.
 * TODO use Builder fluent pattern to align with R4 model style
 */
public class FHIRPersistenceDBCleanupException extends FHIRPersistenceException {

    private static final long serialVersionUID = -8350452448890342596L;

    
    public FHIRPersistenceDBCleanupException(String message) {
        super(message);
        
    }

    public FHIRPersistenceDBCleanupException(String message, Throwable cause) {
        super(message, cause);
        
    }

    @Override
    public FHIRPersistenceDBCleanupException withIssue(OperationOutcome.Issue... issues) {
        super.withIssue(issues);
        return this;
    }
    
    @Override
    public FHIRPersistenceDBCleanupException withIssue(Collection<OperationOutcome.Issue> issues) {
        super.withIssue(issues);
        return this;
    }

}
