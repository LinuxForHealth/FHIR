/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.db2;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.ibm.fhir.database.utils.api.IDatabaseStatement;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.common.DataDefinitionUtil;

/**
 * DB2 Add Table Partition
 */
public class Db2AddTablePartition implements IDatabaseStatement {
    private final String schemaName;
    private final String tableName;
    private final int partitionId;
    private final String tablespaceName;

    /**
     * Public constructor
     * 
     * @param tableName
     * @param partitionId
     */
    public Db2AddTablePartition(String schemaName, String tableName, int partitionId, String tablespaceName) {
        DataDefinitionUtil.assertValidName(schemaName);
        DataDefinitionUtil.assertValidName(tableName);
        this.schemaName = schemaName;
        this.tableName = tableName;
        this.partitionId = partitionId;
        this.tablespaceName = tablespaceName;
    }

    @Override
    public void run(IDatabaseTranslator translator, Connection c) {
        try (Statement s = c.createStatement()) {
            s.executeUpdate(buildSqlString());
        } catch (SQLException x) {
            throw translator.translate(x);
        }
    }

    /**
     * Generate the SQL string (DDL) we want to execute
     * 
     * @return
     */
    protected String buildSqlString() {
        final String partitionName = "TENANT" + partitionId;
        final String lowValueStr = Integer.toString(partitionId);
        final String highValueStr = Integer.toString(partitionId);
        return "ALTER TABLE " + schemaName + "." + tableName + " ADD PARTITION " + partitionName + " STARTING FROM "
                + lowValueStr + " INCLUSIVE  ENDING AT " + highValueStr + " INCLUSIVE" + " IN " + this.tablespaceName;
    }
}