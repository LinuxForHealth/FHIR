/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.core;

/**
 * Enum constants for the allowed values of the fhirVersion MIME-type parameter
 *
 * @implSpec The enum versions are comparable; we use the default {@link Enum#compareTo(Enum)} implementation
 *     and so the versions MUST be declared in the natural order desired for comparison.
 */
public enum FHIRVersionParam {
    VERSION_40("4.0"),
    VERSION_43("4.3");

    private final String value;

    /**
     * Private constructor
     *
     * @param value the fhirVersion value string
     */
    private FHIRVersionParam(String value) {
        this.value = value;
    }

    /**
     * @return
     *     The String value of the fhirVersion parameter
     */
    public java.lang.String value() {
        return value;
    }

    /**
     * Factory method for creating FHIRVersionParam values from a passed string value.
     *
     * @param value
     *     A string that matches one of the allowed FHIRVersionParam values
     * @return
     *     The corresponding FHIRVersionParam or null if a null value was passed
     * @throws IllegalArgumentException
     *     If the passed string is not null and cannot be parsed into an allowed FHIRVersionParam value
     */
    public static FHIRVersionParam from(String value) {
        if (value == null) {
            return null;
        }
        switch (value) {
        case "4.0":
            return VERSION_40;
        case "4.3":
            return VERSION_43;
        default:
            throw new IllegalArgumentException(value);
        }
    }
}
