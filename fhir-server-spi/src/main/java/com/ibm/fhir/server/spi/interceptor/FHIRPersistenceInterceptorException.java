/*
 * (C) Copyright IBM Corp. 2016, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.spi.interceptor;

import java.util.Collection;

import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;

public class FHIRPersistenceInterceptorException extends FHIRPersistenceException {
    private static final long serialVersionUID = 1L;

    public FHIRPersistenceInterceptorException(String message) {
        super(message);
    }

    public FHIRPersistenceInterceptorException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public FHIRPersistenceInterceptorException withIssue(OperationOutcome.Issue... issues) {
        super.withIssue(issues);
        return this;
    }

    @Override
    public FHIRPersistenceInterceptorException withIssue(Collection<OperationOutcome.Issue> issues) {
        super.withIssue(issues);
        return this;
    }

}
