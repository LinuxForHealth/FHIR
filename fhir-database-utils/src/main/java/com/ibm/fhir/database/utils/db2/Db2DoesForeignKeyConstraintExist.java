/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.db2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ibm.fhir.database.utils.api.IDatabaseSupplier;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.common.DataDefinitionUtil;

/**
 * Check the Db2 catalog to see if the configured constraint exists
 */
public class Db2DoesForeignKeyConstraintExist implements IDatabaseSupplier<Boolean> {
    
    // The constraint identity
    private final String schemaName;
    private final String tableName;
    private final String constraintName;

    /**
     * Public constructor
     * @param schemaName
     */
    public Db2DoesForeignKeyConstraintExist(String schemaName, String tableName, String constraintName) {
        this.schemaName = DataDefinitionUtil.assertValidName(schemaName).toUpperCase();
        this.tableName = DataDefinitionUtil.assertValidName(tableName).toUpperCase();
        this.constraintName = DataDefinitionUtil.assertValidName(constraintName).toUpperCase();
    }

    @Override
    public Boolean run(IDatabaseTranslator translator, Connection c) {
        Boolean result;

        // Grab the list of tables for the configured schema from the DB2 catalog
        final String sql = ""
                + "SELECT 1 FROM SYSCAT.REFERENCES "
                + " WHERE tabschema = ? "
                + "   AND tabname   = ? "
                + "   AND constname = ? ";
        
        try (PreparedStatement ps = c.prepareStatement(sql)) {
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
