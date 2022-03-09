/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.citus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.ibm.fhir.database.utils.api.IDatabaseStatement;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.common.DataDefinitionUtil;

/**
 * DAO to add a new tenant key record
 */
public class CreateDistributedTableDAO implements IDatabaseStatement {
    private final String schemaName;
    private final String tableName;
    private final String distributionKey;

    /**
     * Public constructor
     * 
     * @param schemaName
     * @param tableName
     * @param distributionKey
     */
    public CreateDistributedTableDAO(String schemaName, String tableName, String distributionKey) {
        DataDefinitionUtil.assertValidName(schemaName);
        DataDefinitionUtil.assertValidName(tableName);
        DataDefinitionUtil.assertValidName(distributionKey);
        this.schemaName = schemaName.toLowerCase();
        this.tableName = tableName.toLowerCase();
        this.distributionKey = distributionKey.toLowerCase();
    }

    @Override
    public void run(IDatabaseTranslator translator, Connection c) {
        // Run the Citus create_distributed_table UDF
        final String table = DataDefinitionUtil.getQualifiedName(schemaName, this.tableName);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT create_distributed_table(");
        sql.append("'").append(table).append("'");
        sql.append(", ");
        sql.append("'").append(distributionKey).append("'");
        sql.append(")");

        try (PreparedStatement ps = c.prepareStatement(sql.toString())) {
            // It's a SELECT statement, but we don't care about the ResultSet
            ps.executeQuery();
        } catch (SQLException x) {
            // Translate the exception into something a little more meaningful
            // for this database type and application
            throw translator.translate(x);
        }
    }
}