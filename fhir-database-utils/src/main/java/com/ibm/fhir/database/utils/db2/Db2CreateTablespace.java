/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.db2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.ibm.fhir.database.utils.api.IDatabaseStatement;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.common.DataDefinitionUtil;

/**
 * Creates the DB2 Table Space
 */
public class Db2CreateTablespace implements IDatabaseStatement {
    private final String tablespaceName;
    private final int extentSizeKB;

    /**
     * The constructor builds a table space with the given name. 
     * @param tablespaceName
     */
    public Db2CreateTablespace(String tablespaceName) {
        DataDefinitionUtil.assertValidName(tablespaceName);
        this.tablespaceName = tablespaceName;
        this.extentSizeKB = -1;
    }

    /**
     * Public constructor with optional extent size in KB
     * 
     * @param tablespaceName
     * @param extentSizeKB
     */
    public Db2CreateTablespace(String tablespaceName, int extentSizeKB) {
         DataDefinitionUtil.assertValidName(tablespaceName);
         this.tablespaceName = tablespaceName;
         this.extentSizeKB = extentSizeKB;
     }

    @Override
    public void run(IDatabaseTranslator translator, Connection c) {
        String ddl = "CREATE TABLESPACE " + this.tablespaceName + " MANAGED BY AUTOMATIC STORAGE";
        if (extentSizeKB > 0) {
            ddl += " EXTENTSIZE " + this.extentSizeKB + "K";
        }
        
        // We are optimistic and assume that the tablespace doesn't yet exist. 
        try (Statement s = c.createStatement()) {
            s.executeUpdate(ddl);
        }
        catch (SQLException x) {
            // Perhaps it does already exist...let's check
            if (!tablespaceExists(translator, c)) {
                // Can't create it and it doesn't exist
                throw translator.translate(x);
            }
        }
    }
    
    /**
     * Check if the given tablespace exists
     * @param translator
     * @param c
     * @return
     */
    protected boolean tablespaceExists(IDatabaseTranslator translator, Connection c) {
        boolean result;
        final String sql = "SELECT 1 FROM syscat.tablespaces WHERE tbspace = ?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, this.tablespaceName);
            ResultSet rs = ps.executeQuery();
            result = rs.next();
        }
        catch (SQLException x) {
            throw translator.translate(x);
        }
        return result;
    }

}
