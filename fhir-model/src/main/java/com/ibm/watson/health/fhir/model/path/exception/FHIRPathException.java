/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.model.path.exception;

import com.ibm.watson.health.fhir.exception.FHIRException;

public class FHIRPathException extends FHIRException {
    private static final long serialVersionUID = 1L;

    public FHIRPathException(String message) {
        super(message);
    }

    public FHIRPathException(String message, Throwable cause) {
        super(cause);
    }

    public FHIRPathException(Throwable cause) {
        super(cause);
    }
}
