/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.common;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.ibm.fhir.database.utils.api.IDatabaseStatement;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;

/**
 * @author rarnold
 *
 */
public class DropTable implements IDatabaseStatement {
    private final String schemaName;
    private final String tableName;

    /**
     * Public constructor
     * @param tableName
     * @param partitionId
     */
    public DropTable(String schemaName, String tableName) {
        DataDefinitionUtil.assertValidName(schemaName);
        DataDefinitionUtil.assertValidName(tableName);
        this.schemaName = schemaName;
        this.tableName = tableName;
    }

    /* (non-Javadoc)
     * @see com.ibm.fhir.database.utils.api.IDatabaseStatement#run(com.ibm.fhir.database.utils.api.IDatabaseTranslator, java.sql.Connection)
     */
    @Override
    public void run(IDatabaseTranslator translator, Connection c) {
        final String qname = DataDefinitionUtil.getQualifiedName(schemaName, tableName);
        final String ddl = "DROP TABLE " + qname;
        
        try (Statement s = c.createStatement()) {
            s.executeUpdate(ddl);
        }
        catch (SQLException x) {
            throw translator.translate(x);
        }
    }

}
