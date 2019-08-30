/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.database.utils.db2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.ibm.watson.health.database.utils.api.IDatabaseStatement;
import com.ibm.watson.health.database.utils.api.IDatabaseTranslator;
import com.ibm.watson.health.database.utils.common.DataDefinitionUtil;

/**
 * @author rarnold
 *
 */
public class Db2AddTableToStaging implements IDatabaseStatement {
    private final String schemaName;
    private final String stagingTableName;
    private final String tableName;


    /**
     * Public constructor
     * @param tableName
     * @param partitionId
     */
    public Db2AddTableToStaging(String schemaName, String stagingTableName, String tableName) {
        DataDefinitionUtil.assertValidName(schemaName);
        DataDefinitionUtil.assertValidName(stagingTableName);
        DataDefinitionUtil.assertValidName(tableName);
        this.schemaName = schemaName;
        this.stagingTableName = stagingTableName;
        this.tableName = tableName;
    }

    /* (non-Javadoc)
     * @see com.ibm.watson.health.database.utils.api.IDatabaseStatement#run(com.ibm.watson.health.database.utils.api.IDatabaseTranslator, java.sql.Connection)
     */
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
