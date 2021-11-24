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
 * Check the catalog to see if the named index exists
 */
public class PostgresDoesIndexExist implements IDatabaseSupplier<Boolean> {

    // The schema of the index
    private final String schemaName;
    
    // The name of the index
    private final String indexName;

    /**
     * Public constructor
     * @param schemaName
     * @param indexName
     */
    public PostgresDoesIndexExist(String schemaName, String indexName) {
        this.schemaName = DataDefinitionUtil.assertValidName(schemaName.toLowerCase());
        this.indexName = DataDefinitionUtil.assertValidName(indexName.toLowerCase());
    }

    @Override
    public Boolean run(IDatabaseTranslator translator, Connection c) {
        Boolean result = false;
        // For PostgreSQL, identifier names are always in lowercase unless they are surround with double quotes.
        final String sql = ""
                + "SELECT 1 FROM pg_catalog.pg_indexes "
                + " WHERE schemaname = ? "
                + "   AND indexname = ? ";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, schemaName);
            ps.setString(2, indexName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // if we get a row, we know the index exists
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