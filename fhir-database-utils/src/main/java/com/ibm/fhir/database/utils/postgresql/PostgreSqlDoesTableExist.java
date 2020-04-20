/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.postgresql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ibm.fhir.database.utils.api.IDatabaseSupplier;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.common.DataDefinitionUtil;

/**
 * For this implementation we simply run a select against the table
 * and see if it kicks back with an error (which isn't related to
 * connection failure).
 */
public class PostgreSqlDoesTableExist implements IDatabaseSupplier<Boolean> {

    // The schema of the table
    private final String schemaName;

    // The name of the table
    private final String tableName;

    /**
     * Public constructor
     * @param schemaName
     * @param tableName
     */
    public PostgreSqlDoesTableExist(String schemaName, String tableName) {
        this.schemaName = DataDefinitionUtil.assertValidName(schemaName);
        this.tableName = DataDefinitionUtil.assertValidName(tableName);
    }

    @Override
    public Boolean run(IDatabaseTranslator translator, Connection c) {
        Boolean result;
        // For PostgreSQL, identifier names are always in lowercase unless they are surround with double quotes.
        final String sql = "SELECT EXISTS (" +
                "SELECT FROM information_schema.tables " +
                "WHERE  LOWER(table_schema) = LOWER('" + schemaName +
                "') AND LOWER(table_name) = LOWER('" + tableName + "'))";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getBoolean(1);
            }
            else {
                result = false;
            }
        }
        catch (SQLException x) {
            if (translator.isConnectionError(x)) {
                throw translator.translate(x);
            } else {
                result = false;
            }
        }

        return result;
    }
}
