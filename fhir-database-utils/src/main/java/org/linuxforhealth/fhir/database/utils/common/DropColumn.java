/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.database.utils.api.DataAccessException;
import org.linuxforhealth.fhir.database.utils.api.IDatabaseStatement;
import org.linuxforhealth.fhir.database.utils.api.IDatabaseTranslator;

/**
 * Drop columns from the schema.table
 */
public class DropColumn implements IDatabaseStatement {
    private static final Logger logger = Logger.getLogger(DropColumn.class.getName());
    private final String schemaName;
    private final String tableName;
    private final List<String> columnNames;
    private final boolean ignoreError;

    /**
     * Public constructor
     * @param schemaName
     * @param tableName
     * @param columnName
     */
    public DropColumn(String schemaName, String tableName, String... columnName) {
        DataDefinitionUtil.assertValidName(schemaName);
        DataDefinitionUtil.assertValidName(tableName);
        this.schemaName = schemaName;
        this.tableName = tableName;
        this.columnNames = Arrays.asList(columnName);
        this.ignoreError = false;
    }

    /**
     * Public constructor
     * @param schemaName
     * @param tableName
     * @param ignoreError
     * @param columnName
     */
    public DropColumn(String schemaName, String tableName, boolean ignoreError, String... columnName) {
        DataDefinitionUtil.assertValidName(schemaName);
        DataDefinitionUtil.assertValidName(tableName);
        this.schemaName = schemaName;
        this.tableName = tableName;
        this.ignoreError = ignoreError;
        this.columnNames = Arrays.asList(columnName);
    }

    @Override
    public void run(IDatabaseTranslator translator, Connection c) {
        final String qname = DataDefinitionUtil.getQualifiedName(schemaName, tableName);
        final StringBuilder ddl = new StringBuilder("ALTER TABLE " + qname);

        int dropCount = 0;
        for (String columnName : columnNames) {
            if (translator.isFamilyPostgreSQL()) {
                if (postgresColumnExists(translator, c, columnName)) {
                    ddl.append("\n\t" + "DROP COLUMN " + columnName);
                    dropCount++;
                }
            } else {
                ddl.append("\n\t" + "DROP COLUMN " + columnName);
                dropCount++;
            }
        }

        if (dropCount > 0) {
            try (Statement s = c.createStatement()) {
                s.executeUpdate(ddl.toString());
            } catch (SQLException x) {
                if (this.ignoreError) {
                    // just log as a warning
                    logger.warning("DropColumn statement failed: DDL='" + ddl + "'; -- " + x.getMessage());
                } else {
                    throw translator.translate(x);
                }
            }
        } else if (!this.ignoreError) {
            final String qn = DataDefinitionUtil.getQualifiedName(this.schemaName, this.tableName);
            throw new DataAccessException("No columns found to drop for table '" + qn + "'");
        }
    }

    /**
     * Special check that the column exists before we try to drop it
     * @param columnName
     * @return
     */
    public boolean postgresColumnExists(IDatabaseTranslator translator, Connection c, String columnName) {
        boolean result;

        final String SQL = ""
                + "SELECT 1 "
                + "  FROM information_schema.columns "
                + " WHERE table_schema = ? "
                + "   AND table_name = ? "
                + "   AND column_name = ? ";

        try (PreparedStatement ps = c.prepareStatement(SQL)) {
            ps.setString(1, this.schemaName.toLowerCase());
            ps.setString(2, this.tableName.toLowerCase());
            ps.setString(3, columnName.toLowerCase());
            ResultSet rs = ps.executeQuery();
            result = rs.next();
        } catch (SQLException x) {
            throw translator.translate(x);
        }

        return result;
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
