/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.interceptor;

import javax.ws.rs.core.Response.Status;

import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceException;

public class FHIRPersistenceInterceptorException extends FHIRPersistenceException {
    private static final long serialVersionUID = 1L;

    public FHIRPersistenceInterceptorException() {
        super();
    }

    public FHIRPersistenceInterceptorException(String message) {
        super(message);
    }

    public FHIRPersistenceInterceptorException(String message, Throwable cause) {
        super(message, cause);
    }

    public FHIRPersistenceInterceptorException(String message, Status httpStatus, Throwable cause) {
        super(message, httpStatus, cause);
    }

    public FHIRPersistenceInterceptorException(String message, Status httpStatus) {
        super(message, httpStatus);
    }

    public FHIRPersistenceInterceptorException(Throwable cause) {
        super(cause);
    }
}
