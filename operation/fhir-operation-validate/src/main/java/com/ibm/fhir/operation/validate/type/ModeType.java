/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.operation.validate.type;

/**
 * The Mode Type indicates a valid resource validation mode code.
 * @see <a href="https://hl7.org/fhir/R4B/codesystem-resource-validation-mode.html">resource validation mode</a>
 */
public enum ModeType {

    CREATE("create"),
    UPDATE("update"),
    DELETE("delete"),
    PROFILE("PROFILE");

    private String value;

    /**
     * Instantiates a new ModeType.
     *
     * @param value the value
     */
    ModeType(String value) {
        this.value = value;
    }

    /**
     * method to validate a valid resource validation mode code.
     *
     * @param value 
     *      A string that matches one of the allowed ModeType values.
     * @return 
     *      The corresponding ModeType or null if a null value was passed.
     * @throws IllegalArgumentException
     *     If the passed string is not null and cannot be parsed into an allowed ModeType value.     
     */
    public static ModeType from(String value) {
        if (value == null) {
            return null;
        }
        for(ModeType mt : ModeType.values()) {
            if (mt.value.equals(value)) {
                return mt;
            }
        }
        throw new IllegalArgumentException("Failed to find the mode type");
    }

    public String value() {
        return value;
    }
}