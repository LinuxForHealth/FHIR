/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.schema.derby;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.database.utils.api.IConnectionProvider;
import org.linuxforhealth.fhir.database.utils.api.ITransactionProvider;
import org.linuxforhealth.fhir.database.utils.derby.DerbyConnectionProvider;
import org.linuxforhealth.fhir.database.utils.derby.DerbyMaster;
import org.linuxforhealth.fhir.database.utils.pool.PoolConnectionProvider;
import org.linuxforhealth.fhir.database.utils.schema.SchemaVersionsManager;
import org.linuxforhealth.fhir.database.utils.transaction.SimpleTransactionProvider;
import org.linuxforhealth.fhir.database.utils.version.CreateWholeSchemaVersion;
import org.linuxforhealth.fhir.schema.control.FhirSchemaVersion;

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
                FhirSchemaVersion.getLatestFhirSchemaVersion().vid());

            svm.updateSchemaVersionId(FhirSchemaVersion.V0001.vid());
            assertEquals(svm.getVersionForSchema(), FhirSchemaVersion.V0001.vid());
            svm.updateSchemaVersionId(FhirSchemaVersion.V0002.vid());
            assertEquals(svm.getVersionForSchema(), FhirSchemaVersion.V0002.vid());
            svm.updateSchemaVersionId(FhirSchemaVersion.V0003.vid());
            assertEquals(svm.getVersionForSchema(), FhirSchemaVersion.V0003.vid());

            // Make sure we can correctly determine the latest schema version value
            svm.updateSchemaVersion();
            assertEquals(svm.getVersionForSchema(), FhirSchemaVersion.V0030.vid());

            assertFalse(svm.isSchemaOld());
       }
    }
}