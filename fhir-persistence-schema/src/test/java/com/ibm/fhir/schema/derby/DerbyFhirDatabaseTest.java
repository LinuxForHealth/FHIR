/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.derby;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import org.testng.annotations.Test;

import com.ibm.fhir.database.utils.api.IConnectionProvider;
import com.ibm.fhir.database.utils.api.ISchemaAdapter;
import com.ibm.fhir.database.utils.api.ITransaction;
import com.ibm.fhir.database.utils.api.ITransactionProvider;
import com.ibm.fhir.database.utils.api.SchemaType;
import com.ibm.fhir.database.utils.common.GetSequenceNextValueDAO;
import com.ibm.fhir.database.utils.common.JdbcTarget;
import com.ibm.fhir.database.utils.common.PlainSchemaAdapter;
import com.ibm.fhir.database.utils.common.SchemaInfoObject;
import com.ibm.fhir.database.utils.derby.DerbyAdapter;
import com.ibm.fhir.database.utils.derby.DerbyMaster;
import com.ibm.fhir.database.utils.model.DatabaseObjectType;
import com.ibm.fhir.database.utils.model.PhysicalDataModel;
import com.ibm.fhir.database.utils.pool.PoolConnectionProvider;
import com.ibm.fhir.database.utils.transaction.SimpleTransactionProvider;
import com.ibm.fhir.database.utils.version.CreateWholeSchemaVersion;
import com.ibm.fhir.database.utils.version.VersionHistoryService;
import com.ibm.fhir.schema.app.Main;
import com.ibm.fhir.schema.control.FhirSchemaConstants;
import com.ibm.fhir.schema.control.FhirSchemaGenerator;
import com.ibm.fhir.schema.control.GetXXLogicalResourceNeedsMigration;

/**
 * Unit test for the DerbyFhirDatabase utility
 */
public class DerbyFhirDatabaseTest {
    private static final String DB_NAME = "target/derby/fhirDB";
    private static final String ADMIN_SCHEMA_NAME = Main.ADMIN_SCHEMANAME;

    @Test
    public void testFhirSchema() throws Exception {
        // We want to test the whole schema creation process, so need to start
        // with a new database.
        System.out.println("Dropping test database: " + DB_NAME);
        DerbyMaster.dropDatabase(DB_NAME);
        try (DerbyFhirDatabase db = new DerbyFhirDatabase(DB_NAME)) {
            System.out.println("FHIR database created successfully.");
            checkDatabase(db, db.getSchemaName());
            testMigrationFunction(db);
        }

        // Now that we've got an existing database, let's try the creation again...which should be a NOP
        try (DerbyFhirDatabase db = new DerbyFhirDatabase(DB_NAME)) {
            System.out.println("FHIR database exists.");
            checkDatabase(db, db.getSchemaName());

            // Test the schema drop
            testDrop(db, db.getSchemaName());
        }
    }

    /**
     * Test dropping the schema
     * @param cp
     * @param schemaName
     * @throws SQLException
     */
    protected void testDrop(IConnectionProvider cp, String schemaName) throws SQLException {
        PoolConnectionProvider connectionPool = new PoolConnectionProvider(cp, 10);
        ITransactionProvider transactionProvider = new SimpleTransactionProvider(cp);
        DerbyAdapter adapter = new DerbyAdapter(connectionPool);
        ISchemaAdapter schemaAdapter = new PlainSchemaAdapter(adapter);
        VersionHistoryService vhs = new VersionHistoryService(ADMIN_SCHEMA_NAME, schemaName);
        vhs.setTransactionProvider(transactionProvider);
        vhs.setTarget(adapter);

        try (ITransaction tx = transactionProvider.getTransaction()) {
            PhysicalDataModel pdm = new PhysicalDataModel();
            FhirSchemaGenerator gen = new FhirSchemaGenerator(ADMIN_SCHEMA_NAME, schemaName, SchemaType.PLAIN);
            gen.buildSchema(pdm);
            pdm.drop(schemaAdapter, FhirSchemaGenerator.SCHEMA_GROUP_TAG, FhirSchemaGenerator.FHIRDATA_GROUP);

            CreateWholeSchemaVersion.dropTable(schemaName, schemaAdapter);

            // Check that the schema is empty
            List<SchemaInfoObject> schemaObjects = adapter.listSchemaObjects(schemaName);
            boolean schemaIsEmpty = schemaObjects.isEmpty();
            if (!schemaIsEmpty) {
                // When called, we expect the schema to be empty, so let's dump what we have
                final String remaining = schemaObjects.stream().map(Object::toString).collect(Collectors.joining(","));
                System.out.println("Remaining objects in schema '" + schemaName + "': [" + remaining + "]");
            }
            assertTrue(schemaIsEmpty);
        }

        // Now we're all done we can finally clean up the version-history for
        // our schema. Make sure we don't get a version for an object we know
        // we created
        vhs.clearVersionHistory(schemaName);
        vhs.init();
        Integer versionCheck = vhs.getVersion(schemaName, DatabaseObjectType.SEQUENCE.name(), FhirSchemaConstants.FHIR_REF_SEQUENCE);
        assertEquals((int)versionCheck, 0);
    }

    protected void testMigrationFunction(IConnectionProvider cp) throws SQLException {
        try (Connection c = cp.getConnection()) {
            try {
                JdbcTarget tgt = new JdbcTarget(c);
                DerbyAdapter adapter = new DerbyAdapter(tgt);
                GetXXLogicalResourceNeedsMigration cmd = new GetXXLogicalResourceNeedsMigration("FHIRDATA", "Observation");
                assertFalse(adapter.runStatement(cmd));
                c.commit();
            } catch (Throwable t) {
                c.rollback();
                throw t;
            }
        }
    }

    /**
     * Check the FHIR database schema has been set up correctly
     * @param cp
     * @throws SQLException
     */
    protected void checkDatabase(IConnectionProvider cp, String schemaName) throws SQLException {

        try (Connection c = cp.getConnection()) {
            try {
                JdbcTarget tgt = new JdbcTarget(c);
                DerbyAdapter adapter = new DerbyAdapter(tgt);
                checkRefSequence(adapter);

                // Check that we have the correct number of tables. This will need to be updated
                // whenever tables, views or sequences are added or removed
                assertEquals(adapter.listSchemaObjects(schemaName).size(), 2065);
                c.commit();
            } catch (Throwable t) {
                c.rollback();
                throw t;
            }
        }
    }

    /**
     * Check that the FHIR_REF_SEQUENCE has been initialized properly
     * @param adapter
     * @throws SQLException
     */
    protected void checkRefSequence(DerbyAdapter adapter) throws SQLException {
        GetSequenceNextValueDAO cv = new GetSequenceNextValueDAO("FHIRDATA", FhirSchemaConstants.FHIR_REF_SEQUENCE);
        Long result = adapter.runStatement(cv);
        assertNotNull(result);
        assertTrue(result >= FhirSchemaConstants.FHIR_REF_SEQUENCE_START);
    }
}