/**
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.exception;

import java.util.Collection;

import com.ibm.fhir.model.resource.OperationOutcome;

public class FHIRPersistenceResourceDeletedException extends FHIRPersistenceException {
    private static final long serialVersionUID = 1L;
    
    public FHIRPersistenceResourceDeletedException(String message) {
        super(message);
    }
    
    public FHIRPersistenceResourceDeletedException(String message, Throwable cause) {
        super(message, cause);
    }
    
    @Override
    public FHIRPersistenceException withIssue(OperationOutcome.Issue... issues) {
        super.withIssue(issues);
        return this;
    }
    
    @Override
    public FHIRPersistenceException withIssue(Collection<OperationOutcome.Issue> issues) {
        super.withIssue(issues);
        return this;
    }
}
