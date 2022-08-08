/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.search.util;


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

    private final String targetResourceType;

    private final String value;

    private final ReferenceType type;

    private final Integer version;

    /**
     * @param targetResourceType
     *          The resource type of the resource being referenced; can be null
     * @param value
     *          The value of the reference; the expected format of the value will vary based on the reference type
     * @param type
     *          The ReferenceType of the reference (LITERAL_RELATIVE | LITERAL_ABSOLUTE | LOGICAL | DISPLAY_ONLY | INVALID)
     * @param version
     *          The version of the target resource as specified in the reference, or null if the reference is not versioned
     */
    public ReferenceValue(String targetResourceType, String value, ReferenceType type, Integer version) {
        this.targetResourceType = targetResourceType;
        this.value = value;
        this.type = type;
        this.version = version;
    }


    /**
     * @return the type of the resource this reference points to; can be null
     */
    public String getTargetResourceType() {
        return targetResourceType;
    }


    /**
     * @return the value of the reference; the expected format of the value will vary based on the reference type
     */
    public String getValue() {
        return value;
    }


    /**
     * @return the type of the reference itself
     */
    public ReferenceType getType() {
        return type;
    }


    /**
     * @return the version of the target resource as specified in the reference, or null if the reference is not versioned
     */
    public Integer getVersion() {
        return version;
    }
}
