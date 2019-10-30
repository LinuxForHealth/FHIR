/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.db2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.ibm.fhir.database.utils.api.IDatabaseStatement;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.common.DataDefinitionUtil;

/**
 * DB2 Adds table to staging
 */
public class Db2AddTableToStaging implements IDatabaseStatement {
    private final String schemaName;
    private final String stagingTableName;
    private final String tableName;

    /**
     * Public Constructor
     * @param schemaName
     * @param stagingTableName
     * @param tableName
     */
    public Db2AddTableToStaging(String schemaName, String stagingTableName, String tableName) {
        DataDefinitionUtil.assertValidName(schemaName);
        DataDefinitionUtil.assertValidName(stagingTableName);
        DataDefinitionUtil.assertValidName(tableName);
        this.schemaName = schemaName;
        this.stagingTableName = stagingTableName;
        this.tableName = tableName;
    }

    @Override
    public void run(IDatabaseTranslator translator, Connection c) {
        final String qname = DataDefinitionUtil.getQualifiedName(schemaName, stagingTableName);
        final String dml = "INSERT INTO " + qname + "(table_name) VALUES (?)";
        
        try (PreparedStatement ps = c.prepareStatement(dml)) {
            ps.setString(1, this.tableName);
            ps.executeUpdate();
        }
        catch (SQLException x) {
            throw translator.translate(x);
        }
    }

}
