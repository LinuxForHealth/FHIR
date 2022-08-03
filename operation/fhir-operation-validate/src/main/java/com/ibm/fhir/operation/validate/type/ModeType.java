/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.operation.validate.type;

/**
 * The Mode Type indicates a valid resource validation mode code.
 * @see <a href="http://hl7.org/fhir/resource-validation-mode">resource validation mode</a>
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
     *      A string that matches one of the allowed ModeType values
     * @return 
     *      The corresponding FHIRVersionParam or null if a null value was passed or if no matching resource validation mode code is found 
     */
    public static ModeType from(String value) {
        for(ModeType mt : ModeType.values()) {
            if (mt.value.equals(value)) {
                return mt;
            }
        }
        return null;
    }

    public String value() {
        return value;
    }
}