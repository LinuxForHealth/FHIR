/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.patch.exception;

import com.ibm.fhir.exception.FHIRException;

public class FHIRPatchException extends FHIRException {
    private static final long serialVersionUID = 1L;
    protected final String path;

    public FHIRPatchException(String message, String path) {
        super(message);
        this.path = path;
    }

    public FHIRPatchException(String message, Throwable cause) {
        this(message, null, cause);
    }

    public FHIRPatchException(String message, String path, Throwable cause) {
        super(message, cause);
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
