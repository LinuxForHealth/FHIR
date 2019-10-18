/*
 * (C) Copyright IBM Corp. 2016,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.exception;

/**
 * Exception to report an invalid resource type.
 */
public class FHIRInvalidResourceTypeException extends FHIRException {
    private static final long serialVersionUID = 1L;

    public FHIRInvalidResourceTypeException() {
        super();
    }

    public FHIRInvalidResourceTypeException(String message) {
        super(message);
    }

    public FHIRInvalidResourceTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public FHIRInvalidResourceTypeException(Throwable cause) {
        super(cause);
    }
}
