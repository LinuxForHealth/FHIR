/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.common;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.ibm.fhir.database.utils.api.IDatabaseStatement;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.model.ColumnBase;

/**
 * Drop columns from the schema.table
 */
public class AddColumn implements IDatabaseStatement {
    private final String schemaName;
    private final String tableName;
    private final ColumnBase column;

    /**
     * Public constructor
     * @param schemaName
     * @param tableName
     */
    public AddColumn(String schemaName, String tableName, ColumnBase column) {
        DataDefinitionUtil.assertValidName(schemaName);
        DataDefinitionUtil.assertValidName(tableName);
        this.schemaName = schemaName;
        this.tableName = tableName;
        this.column = column;
    }

    @Override
    public void run(IDatabaseTranslator translator, Connection c) {
        String qname = DataDefinitionUtil.getQualifiedName(schemaName, tableName);
        String ddl = "ALTER TABLE " + qname + " ADD COLUMN " + columnDef(column);

        try (Statement s = c.createStatement()) {
            s.executeUpdate(ddl.toString());
        }
        catch (SQLException x) {
            throw translator.translate(x);
        }
    }

    /**
     * Build the list of columns in the create table statement
     */
    private String columnDef(ColumnBase column) {
        StringBuilder result = new StringBuilder();
        result.append(column.getName());
        result.append(" ");
        // XXX: this only works for certain column types
        try {
            result.append(column.getTypeInfo(null));
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new UnsupportedOperationException("Adding columns of type " + column.getClass().getSimpleName() +
                    " is not supported at this time.");
        }
        if (!column.isNullable()) {
            result.append(" NOT NULL");
        }

        return result.toString();
    }

    /**
     * @return the schemaName
     */
    public String getSchemaName() {
        return schemaName;
    }

    /**
     * @return the tableName
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * @return the columnNames
     */
    public ColumnBase getColumn() {
        return column;
    }
}
