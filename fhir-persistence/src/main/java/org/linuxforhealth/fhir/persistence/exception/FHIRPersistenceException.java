/*
 * (C) Copyright IBM Corp. 2016,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.exception;

import java.util.Collection;

import org.linuxforhealth.fhir.exception.FHIROperationException;
import org.linuxforhealth.fhir.model.resource.OperationOutcome;

public class FHIRPersistenceException extends FHIROperationException {
    private static final long serialVersionUID = 1L;
    
    public FHIRPersistenceException(String message) {
        super(message);
    }
    
    public FHIRPersistenceException(String message, Throwable cause) {
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
