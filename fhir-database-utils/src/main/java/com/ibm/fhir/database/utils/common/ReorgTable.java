/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.common;

import java.sql.Connection;

import com.ibm.fhir.database.utils.api.IDatabaseStatement;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.db2.Db2AdminCommand;

/**
 * For databases which support it, execute the reorg table command. This is usually
 * required after a column has been removed or added, or the type of a column has
 * been modified (Db2). This really ought to be done via the adapter, but that will
 * require a much bigger refactor which is not desirable at the current time.
 */
public class ReorgTable implements IDatabaseStatement {
    private final String schemaName;
    private final String tableName;

    /**
     * Public constructor
     * @param schemaName
     * @param tableName the table to reorg
     */
    public ReorgTable(String schemaName, String tableName) {
        DataDefinitionUtil.assertValidName(schemaName);
        DataDefinitionUtil.assertValidName(tableName);
        this.schemaName = schemaName;
        this.tableName = tableName;
    }

    @Override
    public void run(IDatabaseTranslator translator, Connection c) {
        final String fqn = DataDefinitionUtil.getQualifiedName(schemaName, tableName);
        switch (translator.getType()) {
        case DB2:
            Db2AdminCommand cmd = new Db2AdminCommand("REORG TABLE " + fqn);
            cmd.run(translator, c);
            break;
        default:
            // NOP
            break;
        }
    }
}