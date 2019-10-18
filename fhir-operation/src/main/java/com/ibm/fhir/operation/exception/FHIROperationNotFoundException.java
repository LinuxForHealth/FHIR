/*
 * (C) Copyright IBM Corp. 2016,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.exception;

import java.util.Collection;

import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;

public class FHIROperationNotFoundException extends FHIROperationException {
    private static final long serialVersionUID = 1L;

    public FHIROperationNotFoundException(String message) {
        super(message);
    }

    public FHIROperationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    @Override
    public FHIROperationNotFoundException withIssue(Issue... issues) {
        super.withIssue(issues);
        return this;
    }
    
    @Override
    public FHIROperationNotFoundException withIssue(Collection<Issue> issues) {
        super.withIssue(issues);
        return this;
    }
}
