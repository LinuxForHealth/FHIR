/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.derby;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ibm.fhir.database.utils.api.IDatabaseSupplier;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.common.DataDefinitionUtil;

/**
 * Inspect the Derby catalog to see if the configured constraint exists
 */
public class DerbyDoesForeignKeyConstraintExist implements IDatabaseSupplier<Boolean> {
    
    // The constraint identity
    private final String schemaName;
    private final String tableName;
    private final String constraintName;

    /**
     * Public constructor
     * @param schemaName
     */
    public DerbyDoesForeignKeyConstraintExist(String schemaName, String tableName, String constraintName) {
        this.schemaName = DataDefinitionUtil.assertValidName(schemaName).toUpperCase();
        this.tableName = DataDefinitionUtil.assertValidName(tableName).toUpperCase();
        this.constraintName = DataDefinitionUtil.assertValidName(constraintName).toUpperCase();
    }

    @Override
    public Boolean run(IDatabaseTranslator translator, Connection c) {
        Boolean result;

        // Check the catalog to see if the named constraint exists
        final String sql = ""
                + "SELECT 1 "
                + "  FROM sys.sysschemas     s,"
                + "       sys.sysconstraints c,"
                + "       sys.systables      t "
                + " WHERE t.schemaid = s.schemaid "
                + "   AND c.tableid = t.tableid "
                + "   AND s.schemaname     = ? "
                + "   AND t.tablename      = ? "
                + "   AND c.constraintname = ? ";
        
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
