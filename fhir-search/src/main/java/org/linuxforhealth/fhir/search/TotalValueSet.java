/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.search;

/**
 * Search _total Constants
 */
public enum TotalValueSet {
    NONE("none"),
    ESTIMATE("estimate"),
    ACCURATE("accurate");

    private final String value;

    TotalValueSet(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static TotalValueSet from(String value) {
        for (TotalValueSet c : TotalValueSet.values()) {
            if (c.value.equals(value)) {
                return c;
            }
        }
        throw new IllegalArgumentException(value);
    }
}
