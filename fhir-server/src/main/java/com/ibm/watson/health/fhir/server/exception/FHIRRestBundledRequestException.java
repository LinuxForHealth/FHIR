/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.server.exception;

import java.util.Collection;

import javax.ws.rs.core.Response.Status;

import com.ibm.watson.health.fhir.model.resource.Bundle;
import com.ibm.watson.health.fhir.model.resource.OperationOutcome.Issue;

public class FHIRRestBundledRequestException extends FHIRHttpException {
    private static final long serialVersionUID = 1L;
    private Bundle responseBundle = null;

    public FHIRRestBundledRequestException(String message) {
        super(message);
    }

    public FHIRRestBundledRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public FHIRRestBundledRequestException(String message, Status httpStatus) {
        super(message, httpStatus);
    }

    public FHIRRestBundledRequestException(String message, Status httpStatus, Throwable t) {
        super(message, httpStatus, t);
    }

    public FHIRRestBundledRequestException(String message, Status httpStatus, Bundle responseBundle) {
        this(message, httpStatus, responseBundle, null);
    }

    public FHIRRestBundledRequestException(String message, Status httpStatus, Bundle responseBundle, Throwable t) {
        super(message, httpStatus, t);
        this.responseBundle = responseBundle;
    }

    public Bundle getResponseBundle() {
        return responseBundle;
    }
    
    @Override
    public FHIRRestBundledRequestException withIssue(Issue... issues) {
        super.withIssue(issues);
        return this;
    }
    
    @Override
    public FHIRRestBundledRequestException withIssue(Collection<Issue> issues) {
        super.withIssue(issues);
        return this;
    }
}
