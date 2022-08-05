/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.common;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import org.linuxforhealth.fhir.database.utils.api.IDatabaseStatement;
import org.linuxforhealth.fhir.database.utils.api.IDatabaseTranslator;

/**
 * Drop one or more foreign keys from the schema.table
 */
public class DropForeignKeyConstraint implements IDatabaseStatement {
    private final String schemaName;
    private final String tableName;
    private final List<String> constraintNames;

    /**
     * Public constructor
     * @param schemaName
     * @param tableName
     */
    public DropForeignKeyConstraint(String schemaName, String tableName, String... constraintName) {
        DataDefinitionUtil.assertValidName(schemaName);
        DataDefinitionUtil.assertValidName(tableName);
        Arrays.stream(constraintName).forEach(DataDefinitionUtil::assertValidName);
        this.schemaName = schemaName;
        this.tableName = tableName;
        this.constraintNames = Arrays.asList(constraintName);
    }

    @Override
    public void run(IDatabaseTranslator translator, Connection c) {
        String qTableName = DataDefinitionUtil.getQualifiedName(schemaName, tableName);

        // Need to account for the syntax differences between Derby and PostgreSQL
        // DB2: ALTER TABLE tbl DROP FOREIGN KEY fkConstraintName
        // PostgreSQL: ALTER TABLE tbl DROP CONSTRAINT fkConstraintName
        for (String constraintName : constraintNames) {
            String ddl = translator.dropForeignKeyConstraint(qTableName, constraintName);

            try (Statement s = c.createStatement()) {
                s.executeUpdate(ddl);
            }
            catch (SQLException x) {
                throw translator.translate(x);
            }
        }
    }

}
