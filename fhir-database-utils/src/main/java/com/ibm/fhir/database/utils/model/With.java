/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.model;

import java.util.Collections;
import java.util.List;

/**
 * WITH to set metadata on the table: WITH (fillfactor=70)
 */
public class With {

    public static final List<With> EMPTY = Collections.emptyList();
    private String name = null;
    private String value = null;

    /**
     * @param name
     * @param value
     */
    public With(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * builds the sql component 'WITH'
     * @return
     */
    public String buildWithComponent() {
        return name + "=" + value;
    }

    /**
     * creates a with statement
     * @param key
     * @param value
     */
    public static With with(String key, String value) {
        return new With(key, value);
    }
}