/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.model;

/**
 * Represents a table check constraint 
 */
public class CheckConstraint {

    // The name of the constraint
    public final String constraintName;

    // The constraint expression
    public final String constraintExpression;
    
    public CheckConstraint(String constraintName, String constraintExpression) {
        this.constraintName = constraintName;
        this.constraintExpression = constraintExpression;
    }
    
    /**
     * Getter for the name of the constraint
     * @return
     */
    public String getConstraintName() {
        return this.constraintName;
    }

    /**
     * Getter for the constraint expression value
     * @return
     */
    public String getConstraintExpression() {
        return this.constraintExpression;
    }
}
