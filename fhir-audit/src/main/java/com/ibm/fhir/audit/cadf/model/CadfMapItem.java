/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.audit.cadf.model;

/**
 * Representation of the CADF Map Item object.
 */
public final class CadfMapItem {
    private final String key;
    private final Object value;

    public CadfMapItem(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }
}
