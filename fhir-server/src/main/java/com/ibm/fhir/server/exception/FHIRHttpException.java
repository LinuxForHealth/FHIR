/*
 * (C) Copyright IBM Corp. 2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.exception;

import java.util.Collection;

import javax.ws.rs.core.Response.Status;

import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;

/**
 * An exception class used within the FHIR REST API layer.
 */
public class FHIRHttpException extends FHIROperationException {
    private static final long serialVersionUID = 1L;
    
    private Status httpStatus;
    
    public FHIRHttpException(String message) {
        super(message);
    }
    
    public FHIRHttpException(String message, Status httpStatus) {
        super(message);
        setHttpStatus(httpStatus);
    }
    
    public FHIRHttpException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public FHIRHttpException(String message, Status httpStatus, Throwable cause) {
        super(message, cause);
        setHttpStatus(httpStatus);
    }

    public Status getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(Status httpStatus) {
        this.httpStatus = httpStatus;
    }
    
    @Override
    public FHIRHttpException withIssue(Issue... issues) {
        super.withIssue(issues);
        return this;
    }
    
    @Override
    public FHIRHttpException withIssue(Collection<Issue> issues) {
        super.withIssue(issues);
        return this;
    }
}
