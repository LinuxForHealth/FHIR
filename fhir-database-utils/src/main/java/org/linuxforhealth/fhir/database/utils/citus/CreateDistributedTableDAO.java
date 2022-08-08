/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.citus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.database.utils.api.IDatabaseStatement;
import org.linuxforhealth.fhir.database.utils.api.IDatabaseTranslator;
import org.linuxforhealth.fhir.database.utils.common.DataDefinitionUtil;

/**
 * DAO to add a new tenant key record
 */
public class CreateDistributedTableDAO implements IDatabaseStatement {
    private static final Logger logger = Logger.getLogger(CreateDistributedTableDAO.class.getName());

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
            logger.severe("Call failed: " + sql.toString());
            throw translator.translate(x);
        }
    }
}