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
 * Checks to see if the named constraint exists
 */
public class OracleDoesConstraintExist implements IDatabaseSupplier<Boolean> {
    private final String schemaName;
    private final String constraintName;

    /**
     * Check to see if the table exists in the schema (using all_tables)
     * @param schemaName
     * @param constraintName
     */
    public OracleDoesConstraintExist(String schemaName, String constraintName) {
        this.schemaName = DataDefinitionUtil.assertValidName(schemaName);
        this.constraintName = DataDefinitionUtil.assertValidName(constraintName);
    }

    @Override
    public Boolean run(IDatabaseTranslator translator, Connection c) {
        Boolean result = false;

        final String SQL = ""
                + "SELECT 1 "
                + "  FROM all_constraints "
                + " WHERE owner = ? "
                + "   AND constraint_name = ? ";

        try (PreparedStatement s = c.prepareStatement(SQL)) {
            s.setString(1, this.schemaName);
            s.setString(2, constraintName);
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
