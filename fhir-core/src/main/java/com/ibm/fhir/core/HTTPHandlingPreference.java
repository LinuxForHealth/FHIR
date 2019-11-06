/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.core;

/**
 * An enumeration of handling preference codes.
 * 
 * @see <a href="https://tools.ietf.org/html/rfc7240#section-4.4">https://tools.ietf.org/html/rfc7240#section-4.4</a>
 */
public enum HTTPHandlingPreference {
    /**
     * While any particular error may be recoverable, the client would prefer that the server reject the request
     */
    STRICT("strict"),

    /**
     * In the case of recoverable errors, the client wishes the server to attempt to process the request
     */
    LENIENT("lenient");

    private final String value;

    HTTPHandlingPreference(String value) {
        this.value = value;
    }

    public java.lang.String value() {
        return value;
    }

    public static HTTPHandlingPreference from(String value) {
        for (HTTPHandlingPreference h : HTTPHandlingPreference.values()) {
            if (h.value.equals(value)) {
                return h;
            }
        }
        throw new IllegalArgumentException(value);
    }
}
