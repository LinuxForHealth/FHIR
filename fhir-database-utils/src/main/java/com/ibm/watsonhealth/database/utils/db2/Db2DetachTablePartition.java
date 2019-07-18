/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.database.utils.db2;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.ibm.watsonhealth.database.utils.api.IDatabaseStatement;
import com.ibm.watsonhealth.database.utils.api.IDatabaseTranslator;
import com.ibm.watsonhealth.database.utils.common.DataDefinitionUtil;

/**
 * @author rarnold
 *
 */
public class Db2DetachTablePartition implements IDatabaseStatement {
    private final String schemaName;
    private final String tableName;
    private final int partitionId;
    
    // The name of the table the partition is moved into
    private final String intoTableName;


    /**
     * Public constructor
     * @param tableName
     * @param partitionId
     */
    public Db2DetachTablePartition(String schemaName, String tableName, int partitionId, String intoTableName) {
        DataDefinitionUtil.assertValidName(schemaName);
        DataDefinitionUtil.assertValidName(tableName);
        DataDefinitionUtil.assertValidName(intoTableName);
        this.schemaName = schemaName;
        this.tableName = tableName;
        this.partitionId = partitionId;
        this.intoTableName = intoTableName;
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.database.utils.api.IDatabaseStatement#run(com.ibm.watsonhealth.database.utils.api.IDatabaseTranslator, java.sql.Connection)
     */
    @Override
    public void run(IDatabaseTranslator translator, Connection c) {
        final String partitionName = "TENANT" + partitionId;
        final String fromTable = DataDefinitionUtil.getQualifiedName(schemaName, tableName);
        final String targetName = DataDefinitionUtil.getQualifiedName(schemaName, intoTableName);        
        final String ddl = "ALTER TABLE " + fromTable + " DETACH PARTITION " + partitionName + " INTO " + targetName;
        
        try (Statement s = c.createStatement()) {
            s.executeUpdate(ddl);
        }
        catch (SQLException x) {
            throw translator.translate(x);
        }
    }

}
