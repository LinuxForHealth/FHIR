/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.schema;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.database.utils.api.IConnectionProvider;
import org.linuxforhealth.fhir.database.utils.api.ITransactionProvider;
import org.linuxforhealth.fhir.database.utils.derby.DerbyConnectionProvider;
import org.linuxforhealth.fhir.database.utils.derby.DerbyMaster;
import org.linuxforhealth.fhir.database.utils.pool.PoolConnectionProvider;
import org.linuxforhealth.fhir.database.utils.transaction.SimpleTransactionProvider;
import org.linuxforhealth.fhir.database.utils.version.CreateWholeSchemaVersion;

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

            assertFalse(svm.isSchemaOld());
            assertTrue(svm.isSchemaVersionMatch());
       }
    }

    /**
     * Make sure that we don't regress the schema version
     * @throws Exception
     */
    @Test(dependsOnMethods = "test")
    public void testSchemaVersionRegression() throws Exception {
        String dbPath = TARGET_DIR + "fhirdb";
        try (DerbyMaster db = new DerbyMaster(dbPath)) {

            // Create the WHOLE_SCHEMA_VERSION table
            db.runWithAdapter(adapter -> CreateWholeSchemaVersion.createTableIfNeeded(SCHEMA_NAME, adapter));

            IConnectionProvider cp = new DerbyConnectionProvider(db, null);
            PoolConnectionProvider connectionPool = new PoolConnectionProvider(cp, 10);
            ITransactionProvider transactionProvider = new SimpleTransactionProvider(connectionPool);

            // Pretend to be an older version of code
            SchemaVersionsManager svmVersion3 = new SchemaVersionsManager(db.getTranslator(), connectionPool, transactionProvider, SCHEMA_NAME,
                3);

            // Apply the update (should be a NOP because we don't allow regression)
            svmVersion3.updateSchemaVersion();

            // The schema version should still be 5
            assertEquals(svmVersion3.getVersionForSchema(), 5);
            assertFalse(svmVersion3.isSchemaVersionMatch());
        }
    }

    /**
     * Make sure we don't apply changes if the schema is newer than
     * the latest code
     * @throws Exception
     */
    @Test(dependsOnMethods = "test")
    public void testSchemaVersionCurrent() throws Exception {
        String dbPath = TARGET_DIR + "fhirdb";
        try (DerbyMaster db = new DerbyMaster(dbPath)) {

            // Create the WHOLE_SCHEMA_VERSION table
            db.runWithAdapter(adapter -> CreateWholeSchemaVersion.createTableIfNeeded(SCHEMA_NAME, adapter));

            IConnectionProvider cp = new DerbyConnectionProvider(db, null);
            PoolConnectionProvider connectionPool = new PoolConnectionProvider(cp, 10);
            ITransactionProvider transactionProvider = new SimpleTransactionProvider(connectionPool);

            // Pretend to be an older version of code
            SchemaVersionsManager svm = new SchemaVersionsManager(db.getTranslator(), connectionPool, transactionProvider, SCHEMA_NAME,
                3);

            // The current schema version exceeds this code, so there's no need to apply any updates
            assertFalse(svm.isSchemaOld());
            assertFalse(svm.isSchemaVersionMatch());
        }
    }
}