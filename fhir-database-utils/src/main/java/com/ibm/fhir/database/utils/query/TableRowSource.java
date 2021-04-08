/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query;

/**
 * Represents a table referenced in the from list
 */
public class TableRowSource implements RowSource {
    // optional schema name
    private final String schemaName;

    // The name of the table
    private final String tableName;

    /**
     * Protected constructor for a table without a qualified schema name
     *
     * @param tableName
     */
    protected TableRowSource(String tableName) {
        this.schemaName = null;
        this.tableName  = tableName;
    }

    @Override
    public Alias getImpliedAlias() {
        // use the tableName as the alias (used when a real alias is not given)
        return new Alias(this.tableName);
    }

    /**
     * Protected constructor for a table qualified by schema name
     *
     * @param schemaName
     * @param tableName
     * @param alias
     */
    protected TableRowSource(String schemaName, String tableName) {
        this.schemaName = schemaName;
        this.tableName  = tableName;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        if (schemaName != null) {
            result.append(schemaName).append(".");
        }

        result.append(tableName);

        return result.toString();
    }
}