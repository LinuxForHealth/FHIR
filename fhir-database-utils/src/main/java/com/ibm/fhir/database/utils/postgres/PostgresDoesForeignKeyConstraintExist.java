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
 * DAO to check if a named foreign key (FK) constraint exists
 */
public class PostgresDoesForeignKeyConstraintExist implements IDatabaseSupplier<Boolean> {
    
    // The schema of the table
    private final String schemaName;
    
    // The name of the foreign key constraint
    private final String constraintName;

    /**
     * Public constructor
     * @param schemaName
     */
    public PostgresDoesForeignKeyConstraintExist(String schemaName, String constraintName) {
        this.schemaName = DataDefinitionUtil.assertValidName(schemaName).toLowerCase();
        this.constraintName = DataDefinitionUtil.assertValidName(constraintName).toLowerCase();
    }

    @Override
    public Boolean run(IDatabaseTranslator translator, Connection c) {
        Boolean result;
        final String sql = ""
            + "SELECT 1 FROM "
            + "       pg_constraint "
            + " WHERE contype = 'f' "
            + "   AND connamespace = ?::regnamespace "
            + "   AND conname = ? ";
         
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, schemaName);
            ps.setString(2, constraintName);
            ResultSet rs = ps.executeQuery();
            result = Boolean.valueOf(rs.next());
        }
        catch (SQLException x) {
            throw translator.translate(x);
        }
        
        return result;
    }
}