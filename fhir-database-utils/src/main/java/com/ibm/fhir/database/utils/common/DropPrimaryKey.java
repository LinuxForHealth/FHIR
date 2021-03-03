/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.common;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.ibm.fhir.database.utils.api.IDatabaseStatement;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.model.DbType;

/**
 * Drop the primary key constraint on a table
 */
public class DropPrimaryKey implements IDatabaseStatement {
    private final String schemaName;
    private final String tableName;

    /**
     * Public constructor
     * @param schemaName
     * @param tableName
     */
    public DropPrimaryKey(String schemaName, String tableName) {
        DataDefinitionUtil.assertValidName(schemaName);
        DataDefinitionUtil.assertValidName(tableName);
        this.schemaName = schemaName;
        this.tableName = tableName;
    }

    @Override
    public void run(IDatabaseTranslator translator, Connection c) {
        // ought to be doing this via an adapter, which hides the differences between databases
        final String qname = DataDefinitionUtil.getQualifiedName(this.schemaName, this.tableName);
        final String ddl;
        if (translator.getType() == DbType.POSTGRESQL) {
            // we're a little lucky here because of our consistent approach to naming
            // the primary key. Luckily we won't be doing this too often. To do properly, we
            // should introduce an adapter which can hide the database-specific details (refactor
            // and merge the IDatabaseTranslator).
            ddl = "ALTER TABLE " + qname + " DROP CONSTRAINT pk_" + this.tableName;
        } else {
            ddl = "ALTER TABLE " + qname + " DROP PRIMARY KEY";
        }

        try (Statement s = c.createStatement()) {
            s.executeUpdate(ddl);
        } catch (SQLException x) {
            throw translator.translate(x);
        }
    }
}