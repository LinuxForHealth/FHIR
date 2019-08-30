/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.model.parser.exception;

import com.ibm.watson.health.fhir.exception.FHIRException;

public class FHIRParserException extends FHIRException {
    private static final long serialVersionUID = 1L;
    protected final String path;

    public FHIRParserException(String message, String path, Throwable cause) {
        super(message, cause);
        this.path = path;
    }
    
    public String getPath() {
        return path;
    }
}
