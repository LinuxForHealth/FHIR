/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ibm.fhir.database.utils.api.IDatabaseSupplier;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.common.DataDefinitionUtil;

/**
 * Check the catalog to see if the named view exists
 */
public class PostgresDoesViewExist implements IDatabaseSupplier<Boolean> {

    // The schema of the table
    private final String schemaName;

    // The name of the view
    private final String viewName;

    /**
     * Public constructor
     * @param schemaName
     * @param tableName
     */
    public PostgresDoesViewExist(String schemaName, String viewName) {
        this.schemaName = DataDefinitionUtil.assertValidName(schemaName.toLowerCase());
        this.viewName = DataDefinitionUtil.assertValidName(viewName.toLowerCase());
    }

    @Override
    public Boolean run(IDatabaseTranslator translator, Connection c) {
        Boolean result = false;
        // For PostgreSQL, identifier names are always in lowercase unless they are surround with double quotes.
        final String sql = ""
                + "SELECT 1 FROM information_schema.views "
                + " WHERE table_schema = ? "
                + "   AND table_name = ? ";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, schemaName);
            ps.setString(2, viewName);
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