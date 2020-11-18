/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.util;


/**
 * A reference extracted from a FHIR resource, with processing applied to
 * identify the reference type and target resource type
 */
public class ReferenceValue {

    public static enum ReferenceType {
        LITERAL_RELATIVE, // Patient/123abc
        LITERAL_ABSOLUTE, // http(s)://an.other.server/Patient/234def
        LOGICAL,          // e.g. SSN
        DISPLAY_ONLY,     // A Reference with only a display field
        INVALID           // Not a valid reference
    }

    // The type of the resource this reference points to. Can be null
    private final String targetResourceType;

    // The value of the reference
    private final String value;

    // The reference type
    private final ReferenceType type;

    // Option version number to support versioned references when we need this
    private final Integer version;

    public ReferenceValue(String targetResourceType, String value, ReferenceType type, Integer version) {
        this.targetResourceType = targetResourceType;
        this.value = value;
        this.type = type;
        this.version = version;
    }


    /**
     * @return the targetResourceType
     */
    public String getTargetResourceType() {
        return targetResourceType;
    }


    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }


    /**
     * @return the type
     */
    public ReferenceType getType() {
        return type;
    }


    /**
     * @return the version
     */
    public Integer getVersion() {
        return version;
    }
}
