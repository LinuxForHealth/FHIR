/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.db2;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

/**
 * Tests to add DB2 Partitions to a Table
 */
public class Db2AddTablePartitionTest {
    private static final String schemaName = "schema1";
    private static final String tableName = "table1";
    private static final int partitionId = 23; // anything other than a default value
    private static final String tablespaceName = "tablespace1";

    @Test
    public void testStatement() {
        // Ah, testing stuff without an underlying database...what fun
        Db2AddTablePartition atp = new Db2AddTablePartition(schemaName, tableName, partitionId, tablespaceName);

        // We can at least check the SQL string matches our expectation
        String ddl = atp.buildSqlString();
        assertEquals(ddl, "ALTER TABLE " + schemaName + "." + tableName 
                + " ADD PARTITION TENANT" + partitionId 
                + " STARTING FROM " + partitionId + " INCLUSIVE " 
                + " ENDING AT " + partitionId + " INCLUSIVE"
                + " IN " + tablespaceName);
    }
}
