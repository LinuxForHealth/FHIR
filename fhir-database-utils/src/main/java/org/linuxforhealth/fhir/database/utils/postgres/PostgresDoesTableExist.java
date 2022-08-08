/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.linuxforhealth.fhir.database.utils.api.IDatabaseSupplier;
import org.linuxforhealth.fhir.database.utils.api.IDatabaseTranslator;
import org.linuxforhealth.fhir.database.utils.common.DataDefinitionUtil;

/**
 * For this implementation we simply run a select against the table
 * and see if it kicks back with an error (which isn't related to
 * connection failure).
 */
public class PostgresDoesTableExist implements IDatabaseSupplier<Boolean> {

    // The schema of the table
    private final String schemaName;

    // The name of the table
    private final String tableName;

    /**
     * Public constructor
     * @param schemaName
     * @param tableName
     */
    public PostgresDoesTableExist(String schemaName, String tableName) {
        this.schemaName = DataDefinitionUtil.assertValidName(schemaName);
        this.tableName = DataDefinitionUtil.assertValidName(tableName);
    }

    @Override
    public Boolean run(IDatabaseTranslator translator, Connection c) {
        Boolean result = false;
        // For PostgreSQL, identifier names are always in lowercase unless they are surround with double quotes.
        // Simplify the SQL a bit for compatibility with CockroachDB
        final String sql =
                "  SELECT 1 "
                + "  FROM information_schema.tables "
                + " WHERE table_schema = LOWER('" + schemaName + "') "
                + "   AND table_name = LOWER('" + tableName + "')";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // if we get a row, we know the table exists
                result = true;
            }
        }
        catch (SQLException x) {
            if (translator.isConnectionError(x)) {
                throw translator.translate(x);
            }
        }

        return result;
    }
}