/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.validation.exception;

public class FHIRValidationException extends Exception {
    private static final long serialVersionUID = 1L;

    public FHIRValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
