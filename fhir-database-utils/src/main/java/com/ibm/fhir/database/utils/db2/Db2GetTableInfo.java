/*
 * (C) Copyright IBM Corp. 2019, 2020
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
 * DAO to fetch the current value of FHIR_ADMIN.SV_TENANT_ID. Useful for
 * debugging - the value isn't useful outside of the database.
 */
public class Db2GetTableInfo implements IDatabaseSupplier<Db2TableInfo> {
    
    // The schema of the table
    private final String schemaName;

    // The name of the table
    private final String tableName;
    
    /**
     * Public constructor
     * @param schemaName
     * @param tableName
     */
    public Db2GetTableInfo(String schemaName, String tableName) {
        this.schemaName = DataDefinitionUtil.assertValidName(schemaName);
        this.tableName = DataDefinitionUtil.assertValidName(tableName);
    }

    @Override
    public Db2TableInfo run(IDatabaseTranslator translator, Connection c) {
        Db2TableInfo result;
        // Grab info about the table from the DB2 catalog
        final String sql = "SELECT * FROM SYSCAT.TABLES WHERE tabschema = ? and tabname = ?";
        
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, schemaName);
            ps.setString(2, tableName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result = new Db2TableInfo();
            }
            else {
                result = null;
            }
        }
        catch (SQLException x) {
            throw translator.translate(x);
        }
        
        return result;
    }
}
