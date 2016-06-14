/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.server.exception;

import javax.ws.rs.core.Response.Status;

import com.ibm.watsonhealth.fhir.model.Bundle;
import com.ibm.watsonhealth.fhir.model.OperationOutcome;

public class FHIRRestBundledRequestException extends FHIRRestException {
    private static final long serialVersionUID = 1L;
    private Bundle responseBundle = null;

    public FHIRRestBundledRequestException() {
    }

    public FHIRRestBundledRequestException(String message) {
        super(message);
    }

    public FHIRRestBundledRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public FHIRRestBundledRequestException(Throwable cause) {
        super(cause);
    }

    public FHIRRestBundledRequestException(String message, OperationOutcome operationOutcome, Status httpStatus) {
        super(message, operationOutcome, httpStatus);
    }

    public FHIRRestBundledRequestException(String message, OperationOutcome operationOutcome, Status httpStatus, Throwable t) {
        super(message, operationOutcome, httpStatus, t);
    }

    public FHIRRestBundledRequestException(String message, OperationOutcome operationOutcome, Status httpStatus, Bundle responseBundle) {
        this(message, operationOutcome, httpStatus, responseBundle, null);
    }

    public FHIRRestBundledRequestException(String message, OperationOutcome operationOutcome, Status httpStatus, Bundle responseBundle, Throwable t) {
        super(message, operationOutcome, httpStatus, t);
        this.responseBundle = responseBundle;
    }

    public Bundle getResponseBundle() {
        return responseBundle;
    }
}
