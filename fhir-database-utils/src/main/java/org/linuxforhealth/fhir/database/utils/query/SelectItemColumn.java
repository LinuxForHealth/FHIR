/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.query;

/**
 * An item of the SELECT list which is a simple column reference
 */
public class SelectItemColumn extends SelectItem {
    // The object from which we're selecting (e.g. table, or sub-query alias)
    private final String source;

    // The column name or expression
    private final String columnName;

    protected SelectItemColumn(String source, String columnName, Alias alias) {
        super(alias);
        this.source     = source;
        this.columnName = columnName;
    }

    @Override
    public String toString() {
        StringBuilder sqlBuilder = new StringBuilder();

        if (source != null) {
            sqlBuilder.append(source);
            sqlBuilder.append('.');
        }
        sqlBuilder.append(columnName);

        if (super.getAlias() != null) {
            sqlBuilder.append(" AS ");
            sqlBuilder.append(super.getAlias());
        }

        return sqlBuilder.toString();
    }
}