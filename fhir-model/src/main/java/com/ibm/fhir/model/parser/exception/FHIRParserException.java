/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.parser.exception;

import com.ibm.fhir.exception.FHIRException;

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
    
    @Override
    public String getMessage() {
        String message = super.getMessage();
        
        if (path != null && !path.isEmpty()) {
            message = message + " [" + path + "]";
        }
        
        return message;
    }
}
