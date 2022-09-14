/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.linuxforhealth.fhir.database.utils.api.IDatabaseSupplier;
import org.linuxforhealth.fhir.database.utils.api.IDatabaseTranslator;
import org.linuxforhealth.fhir.database.utils.common.DataDefinitionUtil;

/**
 * Checks to see if the named index exists. In this case, we assume that
 * the schema name is the schema of the index, not the table (although
 * in our FHIR schema, the two would always be the same).
 */
public class OracleDoesIndexExist implements IDatabaseSupplier<Boolean> {
    private final String schemaName;
    private final String indexName;

    /**
     * Check to see if the table exists in the schema (using all_tables)
     * @param schemaName
     * @param viewName
     */
    public OracleDoesIndexExist(String schemaName, String indexName) {
        this.schemaName = DataDefinitionUtil.assertValidName(schemaName);
        this.indexName = DataDefinitionUtil.assertValidName(indexName);
    }

    @Override
    public Boolean run(IDatabaseTranslator translator, Connection c) {
        Boolean result = false;

        final String SQL = ""
                + "SELECT 1 "
                + "  FROM all_indexes "
                + " WHERE owner = ? "
                + "   AND index_name = ? ";

        try (PreparedStatement s = c.prepareStatement(SQL)) {
            s.setString(1, this.schemaName);
            s.setString(2, indexName);
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                result = true;
            }
        } catch (SQLException x) {
            throw translator.translate(x);
        }
        return result;
    }
}
