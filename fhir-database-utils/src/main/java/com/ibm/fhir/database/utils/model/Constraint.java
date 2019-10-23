/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.model;

/**
 * Represents a named constraint on a table or column
 */
public class Constraint {
    private final String constraintName;
    
    protected Constraint(String constraintName) {
        this.constraintName = constraintName;
    }

    public String getConstraintName() {
        return this.constraintName;
    }
}
