/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.path.patch;

/**
 * The list of allowed FHIRPath Patch operation types
 */
public enum FHIRPathPatchType {
    /**
     * The content will be appended to the element identified in the path, using the name specified.
     * Add can used for non-repeating elements as long as they do not already exist.
     */
    ADD("add"),

    /**
     * The content will be inserted into the nominated list at the index specified (0 based).
     * Note: add is easier than insert at the end of the list.
     */
    INSERT("insert"),

    /**
     * Delete the content at the specified path (if found)
     */
    DELETE("delete"),

    /**
     * Replace the content at the specified path
     */
    REPLACE("replace"),

    /**
     * Move an element within a single list
     */
    MOVE("move");


    private final String value;

    FHIRPathPatchType(String value) {
        this.value = value;
    }

    public java.lang.String value() {
        return value;
    }

    public static FHIRPathPatchType from(String value) {
        for (FHIRPathPatchType t : FHIRPathPatchType.values()) {
            if (t.value.equals(value)) {
                return t;
            }
        }
        throw new IllegalArgumentException(value);
    }
}
