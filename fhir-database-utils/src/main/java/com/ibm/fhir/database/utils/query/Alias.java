/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query;

/**
 * Simple encapsulation of the alias name of an object in a SQL statement.
 * Used to drive some simple overloading of methods
 * @author rarnold
 *
 */
public class Alias {
    private final String alias;
    
    protected Alias(String alias) {
        this.alias = alias;
    }
    
    @Override
    public String toString() {
        return this.alias;
    }
}
