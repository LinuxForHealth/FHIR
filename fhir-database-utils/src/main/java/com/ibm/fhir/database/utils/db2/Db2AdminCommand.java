/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.db2;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.ibm.fhir.database.utils.api.IDatabaseStatement;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;

/**
 * Reorg the schema.table
 */
public class Db2AdminCommand implements IDatabaseStatement {
    final String ddl;

    /**
     * Public constructor
     * @param schemaName
     */
    public Db2AdminCommand(String command) {
        this.ddl = "CALL SYSPROC.ADMIN_CMD ('" + command + "')";
    }

    @Override
    public void run(IDatabaseTranslator translator, Connection c) {
        try (Statement s = c.createStatement()) {
            s.executeUpdate(ddl);
        }
        catch (SQLException x) {
            throw translator.translate(x);
        }
    }

}
