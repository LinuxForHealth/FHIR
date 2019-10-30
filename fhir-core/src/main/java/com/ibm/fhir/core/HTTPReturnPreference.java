/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.core;

/**
 * An enumeration of return preference codes.
 * 
 * see <a href="https://www.hl7.org/fhir/r4/http.html#ops">FHIR Http Operations</a>
 * @author lmsurpre
 */
public enum HTTPReturnPreference {
    /**
     * minimal
     * 
     * <p>A client preference for returning no body.
     */
    MINIMAL("minimal"),

    /**
     * representation
     * 
     * <p>A client preference for returning the full resource.
     */
    REPRESENTATION("representation"),

    /**
     * OperationOutcome
     * 
     * <p>A client preference for returning an OperationOutcome resource containing hints and warnings about the operation rather than the full resource.
     */
    OPERATION_OUTCOME("OperationOutcome");

    private final String value;

    HTTPReturnPreference(String value) {
        this.value = value;
    }

    public java.lang.String value() {
        return value;
    }

    public static HTTPReturnPreference from(String value) {
        for (HTTPReturnPreference c : HTTPReturnPreference.values()) {
            if (c.value.equals(value)) {
                return c;
            }
        }
        throw new IllegalArgumentException(value);
    }
}
