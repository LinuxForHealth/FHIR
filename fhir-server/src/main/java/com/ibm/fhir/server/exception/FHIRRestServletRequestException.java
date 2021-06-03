/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.exception;

import javax.servlet.http.HttpServletResponse;

import com.ibm.fhir.exception.FHIROperationException;

public class FHIRRestServletRequestException extends FHIROperationException {
    private static final long serialVersionUID = 1L;
    private int httpStatusCode = HttpServletResponse.SC_BAD_REQUEST;

    public FHIRRestServletRequestException(String message) {
        super(message);
    }

    public FHIRRestServletRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public FHIRRestServletRequestException(String message, int httpStatusCode) {
        this(message, httpStatusCode, null);
    }

    public FHIRRestServletRequestException(String message, int httpStatusCode, Throwable t) {
        super(message, t);
        this.httpStatusCode = httpStatusCode;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }
}
