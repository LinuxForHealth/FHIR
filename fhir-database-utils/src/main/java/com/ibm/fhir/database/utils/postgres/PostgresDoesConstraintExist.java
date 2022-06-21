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
 * PostgreSQL catalog query to determine if the named constraint exists for the given
 * schema and table
 */
public class PostgresDoesConstraintExist implements IDatabaseSupplier<Boolean> {
    
    // Identity of the constraint
    private final String schemaName;
    private final String tableName;
    private final String constraintName;

    /**
     * Public constructor
     * @param schemaName
     * @param tableName
     * @param constraintName
     */
    public PostgresDoesConstraintExist(String schemaName, String tableName, String constraintName) {
        this.schemaName = DataDefinitionUtil.assertValidName(schemaName).toLowerCase();
        this.tableName = DataDefinitionUtil.assertValidName(tableName).toLowerCase();
        this.constraintName = DataDefinitionUtil.assertValidName(constraintName).toLowerCase();
    }

    @Override
    public Boolean run(IDatabaseTranslator translator, Connection c) {
        Boolean result;

        final String SQL = ""
                + "SELECT 1 FROM "
                + "       pg_catalog.pg_constraint con "
                + "  JOIN pg_catalog.pg_class rel ON rel.oid = con.conrelid "
                + "  JOIN pg_catalog.pg_namespace nsp ON nsp.oid = connamespace "
                + " WHERE nsp.nspname = ? "
                + "   AND rel.relname = ? "
                + "   AND con.conname = ? ";
        
        try (PreparedStatement ps = c.prepareStatement(SQL)) {
            ps.setString(1, schemaName);
            ps.setString(2, tableName);
            ps.setString(3, constraintName);
            ResultSet rs = ps.executeQuery();
            result = Boolean.valueOf(rs.next());
        } catch (SQLException x) {
            throw translator.translate(x);
        }
        
        return result;
    }
}