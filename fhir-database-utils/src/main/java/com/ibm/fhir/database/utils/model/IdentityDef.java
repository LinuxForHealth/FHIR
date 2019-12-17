/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.model;

/**
 * Represents the definition of a primary key constraint on a table
 */
public class IdentityDef {

    // The name of the column to be the identity
    public final String column;

    // The list of columns comprising the primary key
    public final Generated generated;

    public IdentityDef(String columnName, Generated generatedPref) {
        this.column = columnName;
        this.generated = generatedPref;
    }

    /**
     * Getter for the name of the identity column
     */
    public String getColumnName() {
        return this.column;
    }

    /**
     * Getter for the generation preference
     */
    public Generated getGenerated() {
        return this.generated;
    }
}
