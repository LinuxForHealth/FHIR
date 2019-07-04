/**
 * (C) Copyright IBM Corp. 2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.server.exception;

import java.util.Collection;

import javax.ws.rs.core.Response.Status;

import com.ibm.watsonhealth.fhir.model.resource.OperationOutcome.Issue;

/**
 * Exception to report a virtual resource type related exception
 */
public class FHIRVirtualResourceTypeException extends FHIRHttpException {
    private static final long serialVersionUID = 1L;

    public FHIRVirtualResourceTypeException(String message) {
        super(message);
    }
    
    public FHIRVirtualResourceTypeException(String message, Status httpStatus) {
        super(message, httpStatus);
    }

    public FHIRVirtualResourceTypeException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public FHIRVirtualResourceTypeException(String message, Status httpStatus, Throwable cause) {
        super(message, cause);
        setHttpStatus(httpStatus);
    }

    @Override
    public FHIRVirtualResourceTypeException withIssue(Issue... issues) {
        super.withIssue(issues);
        return this;
    }
    
    @Override
    public FHIRVirtualResourceTypeException withIssue(Collection<Issue> issues) {
        super.withIssue(issues);
        return this;
    }
}
