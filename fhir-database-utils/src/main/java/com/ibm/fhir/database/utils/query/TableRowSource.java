/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query;

import com.ibm.fhir.database.utils.common.DataDefinitionUtil;
import com.ibm.fhir.database.utils.query.expression.StatementRenderer;

/**
 * Represents a table referenced in the from list
 *   FROM foo
 *   or
 *   FROM bar.foo
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
        DataDefinitionUtil.assertValidName(tableName);
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
     * @param schemaName
     * @param tableName
     */
    protected TableRowSource(String schemaName, String tableName) {
        DataDefinitionUtil.assertValidName(schemaName);
        DataDefinitionUtil.assertValidName(tableName);
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

    @Override
    public String toPrettyString(boolean pretty) {
        return toString();
    }

    @Override
    public <T> T render(StatementRenderer<T> renderer) {
        return renderer.rowSource(schemaName, tableName);
    }
}