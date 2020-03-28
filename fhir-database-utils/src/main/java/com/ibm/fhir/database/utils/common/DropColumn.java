/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.common;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import com.ibm.fhir.database.utils.api.IDatabaseStatement;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;

/**
 * Drop columns from the schema.table
 */
public class DropColumn implements IDatabaseStatement {
    private final String schemaName;
    private final String tableName;
    private final List<String> columnNames;

    /**
     * Public constructor
     * @param schemaName
     * @param tableName
     */
    public DropColumn(String schemaName, String tableName, String... columnName) {
        DataDefinitionUtil.assertValidName(schemaName);
        DataDefinitionUtil.assertValidName(tableName);
        this.schemaName = schemaName;
        this.tableName = tableName;
        this.columnNames = Arrays.asList(columnName);
    }

    @Override
    public void run(IDatabaseTranslator translator, Connection c) {
        final String qname = DataDefinitionUtil.getQualifiedName(schemaName, tableName);
        final StringBuilder ddl = new StringBuilder("ALTER TABLE " + qname);
        for (String columnName : columnNames) {
            ddl.append("\n\t" + "DROP COLUMN " + columnName);
        }

        try (Statement s = c.createStatement()) {
            s.executeUpdate(ddl.toString());
        }
        catch (SQLException x) {
            throw translator.translate(x);
        }
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
    public List<String> getColumnNames() {
        return columnNames;
    }
}
