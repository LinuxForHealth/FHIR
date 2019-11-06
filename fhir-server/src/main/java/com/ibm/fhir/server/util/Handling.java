/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.server.util;

/**
 * Allowed options for the fhirServer/core/handling config property
 * @see https://tools.ietf.org/html/rfc7240#section-4.4
 */
public enum Handling {
    /**
     * The server behaves in lenient mode by default, but honors any client preference passed in the Prefer header
     */
    LENIENT("lenient"),
    
    /**
     * The server always behaves in lenient mode, ignoring any client preference passed in the Prefer header
     */
    LENIENT_ONLY("lenient-only"),
    
    /**
     * The server behaves in strict mode by default, but honors any client preference passed in the Prefer header
     */
    STRICT("strict"),
    
    /**
     * The server always behaves in strict mode, ignoring any client preference passed in the Prefer header
     */
    STRICT_ONLY("strict-only");
    
    private final java.lang.String value;
    
    private Handling(String value) {
        this.value = value;
    }
    
    public static Handling from(String value) {
        for (Handling h : Handling.values()) {
            if (h.value.equals(value)) {
                return h;
            }
        }
        throw new IllegalArgumentException(value);
    }

    /**
     * The String value of this enumeration
     */
    public String value() {
        return value;
    }
}
