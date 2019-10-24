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
 * Drops the DB2 Tablespace
 */
public class Db2DropTablespace implements IDatabaseStatement {
    
    private final String tablespaceName;

    /**
     * 
     * @param tablespaceName
     */
    public Db2DropTablespace(String tablespaceName) {
        DataDefinitionUtil.assertValidName(tablespaceName);
        this.tablespaceName = tablespaceName;
    }

    @Override
    public void run(IDatabaseTranslator translator, Connection c) {
        String ddl = "DROP TABLESPACE " + this.tablespaceName;

        // We are optimistic and assume that the tablespace currently exists.
        try (Statement s = c.createStatement()) {
            s.executeUpdate(ddl);
        } catch (SQLException x) {
            // Perhaps it has already been dropped. Only propagate an error if it's still
            // there
            if (tablespaceExists(translator, c)) {
                throw translator.translate(x);
            }
        }
    }

    /**
     * Check if the given tablespace exists
     * 
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
        } catch (SQLException x) {
            throw translator.translate(x);
        }
        return result;
    }

}
