/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.model;

import com.ibm.fhir.database.utils.api.IDatabaseTypeAdapter;

/**
 * An immutable definition of a column in a table
 */
public abstract class ColumnBase {
    // Name of the column
    private final String name;

    // Does the column allow null values
    private final boolean nullable;

    // The default value for the column (usually null)
    private String defaultVal;

    /**
     * Protected constructor - for use by subclasses
     * @param name
     * @param nullable
     */
    protected ColumnBase(String name, boolean nullable) {
        this.name = name;
        this.nullable = nullable;
        this.defaultVal = null;
    }

    /**
     * Protected constructor - for use by subclasses
     * @param name
     * @param nullable
     * @param defaultVal
     */
    protected ColumnBase(String name, boolean nullable, String defaultVal) {
        this.name = name;
        this.nullable = nullable;
        this.defaultVal = defaultVal;
    }

    /**
     * resets the default value.
     */
    public void resetDefaultValue() {
        this.defaultVal = null;
    }

    /**
     * Getter for the name
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the null flag
     * @return
     */
    public boolean isNullable() {
        return nullable;
    }

    /**
     * Getter for the default value of this column
     * @return possibly null
     */
    public String getDefaultVal() {
        return defaultVal;
    }

    /**
     * Get the type info string
     * @param adapter
     * @return
     */
    public abstract String getTypeInfo(IDatabaseTypeAdapter adapter);

    /**
     * Get the definition of this column as used in create table and create type
     * statements. Simply the name followed by the data type
     * @param adapter
     * @return
     */
    public String getTypeDef(IDatabaseTypeAdapter adapter) {
        return this.name + " " + getTypeInfo(adapter);
    }
}