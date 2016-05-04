/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.exception;

/**
 * Common FHIR Server exception base class.
 */
public class FHIRException extends Exception {
    private static final long serialVersionUID = 1L;

    public FHIRException() {
        super();
    }

    public FHIRException(String message) {
        super(message);
    }

    public FHIRException(String message, Throwable cause) {
        super(message, cause);
    }

    public FHIRException(Throwable cause) {
        super(cause);
    }
}
