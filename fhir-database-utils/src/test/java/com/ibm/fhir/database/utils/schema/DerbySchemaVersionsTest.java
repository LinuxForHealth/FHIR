/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.schema;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import com.ibm.fhir.database.utils.api.IConnectionProvider;
import com.ibm.fhir.database.utils.api.ITransactionProvider;
import com.ibm.fhir.database.utils.derby.DerbyConnectionProvider;
import com.ibm.fhir.database.utils.derby.DerbyMaster;
import com.ibm.fhir.database.utils.pool.PoolConnectionProvider;
import com.ibm.fhir.database.utils.schema.SchemaVersionsManager;
import com.ibm.fhir.database.utils.transaction.SimpleTransactionProvider;
import com.ibm.fhir.database.utils.version.CreateWholeSchemaVersion;

/**
 * Unit test for the WHOLE_SCHEMA_VERSION table
 */
public class DerbySchemaVersionsTest {
    private static final String TARGET_DIR = "target/derby/";
    private static final String SCHEMA_NAME = "APP";

    @Test
    public void test() throws Exception {
        String dbPath = TARGET_DIR + "fhirdb";
        DerbyMaster.dropDatabase(dbPath);
        try (DerbyMaster db = new DerbyMaster(dbPath)) {

            // Create the WHOLE_SCHEMA_VERSION table
            db.runWithAdapter(adapter -> CreateWholeSchemaVersion.createTableIfNeeded(SCHEMA_NAME, adapter));

            IConnectionProvider cp = new DerbyConnectionProvider(db, null);
            PoolConnectionProvider connectionPool = new PoolConnectionProvider(cp, 10);
            ITransactionProvider transactionProvider = new SimpleTransactionProvider(connectionPool);
            SchemaVersionsManager svm = new SchemaVersionsManager(db.getTranslator(), connectionPool, transactionProvider, SCHEMA_NAME,
                5);

            svm.updateSchemaVersionId(1);
            assertEquals(svm.getVersionForSchema(), 1);
            svm.updateSchemaVersionId(2);
            assertEquals(svm.getVersionForSchema(), 2);
            svm.updateSchemaVersionId(3);
            assertEquals(svm.getVersionForSchema(), 3);

            // Make sure we can correctly determine the latest schema version value
            svm.updateSchemaVersion();
            assertEquals(svm.getVersionForSchema(), 5);

            assertTrue(svm.isLatestSchema());
       }
    }
}