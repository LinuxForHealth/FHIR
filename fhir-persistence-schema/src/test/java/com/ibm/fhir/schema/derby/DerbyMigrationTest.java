/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.derby;

import static org.testng.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.LogManager;
import java.util.stream.Collectors;

import org.apache.derby.tools.dblook;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.database.utils.api.IConnectionProvider;
import com.ibm.fhir.database.utils.api.IDatabaseAdapter;
import com.ibm.fhir.database.utils.api.ITransaction;
import com.ibm.fhir.database.utils.api.ITransactionProvider;
import com.ibm.fhir.database.utils.derby.DerbyAdapter;
import com.ibm.fhir.database.utils.derby.DerbyConnectionProvider;
import com.ibm.fhir.database.utils.derby.DerbyMaster;
import com.ibm.fhir.database.utils.model.PhysicalDataModel;
import com.ibm.fhir.database.utils.pool.PoolConnectionProvider;
import com.ibm.fhir.database.utils.transaction.SimpleTransactionProvider;
import com.ibm.fhir.database.utils.version.CreateVersionHistory;
import com.ibm.fhir.database.utils.version.VersionHistoryService;
import com.ibm.fhir.schema.control.FhirSchemaGenerator;
import com.ibm.fhir.schema.prior.FhirSchemaGenerator455;

/**
 * Unit test for migration logic within the IBM FHIR Server schema
 */
public class DerbyMigrationTest {
    private static final String TARGET_DIR = "target/derby/";
    private static final String SCHEMA_NAME = "FHIRDATA";
    private static final String ADMIN_SCHEMA_NAME = "FHIR_ADMIN";
    private static final String OAUTH_SCHEMANAME = "FHIR_OAUTH";

    @BeforeClass(alwaysRun = true)
    protected void setUp() throws SecurityException, IOException
    {
        FileInputStream fis = new FileInputStream(DerbyMigrationTest.class.getResource("/logging.unitTest.properties").getFile());
        LogManager.getLogManager().readConfiguration(fis);
    }

    @Test
    public void testMigrateFhirSchema() throws Exception {
        // The schema for each resource is the same, so we only need to
        // exercise migration for one type. Much quicker.
        Set<String> resourceTypes = Collections.singleton("Observation");

        // 1. Create the newest version of the schema
        String dbPath = TARGET_DIR + "latest";
        DerbyMaster.dropDatabase(dbPath);
        try (DerbyMaster db = new DerbyMaster(dbPath)) {
            // Set up the version history service first if it doesn't yet exist
            db.runWithAdapter(adapter -> CreateVersionHistory.createTableIfNeeded(ADMIN_SCHEMA_NAME, adapter));

            IConnectionProvider cp = new DerbyConnectionProvider(db, null);
            PoolConnectionProvider connectionPool = new PoolConnectionProvider(cp, 200);
            ITransactionProvider transactionProvider = new SimpleTransactionProvider(connectionPool);
            IDatabaseAdapter adapter = new DerbyAdapter(connectionPool);

            VersionHistoryService vhs = new VersionHistoryService(ADMIN_SCHEMA_NAME, SCHEMA_NAME, OAUTH_SCHEMANAME);
            vhs.setTransactionProvider(transactionProvider);
            vhs.setTarget(adapter);
            vhs.init();

            try (ITransaction tx = transactionProvider.getTransaction()) {
                try {
                    createOrUpgradeSchema(db, connectionPool, vhs, resourceTypes);
                } catch (Throwable x) {
                    // let's see who's causing the lock trouble
                    db.dumpLockInfo();
                    throw x;
                }
            }
        }

        // Generate a list of DDL statements describing the new database
        List<String> latest_ddl = inferDDL(dbPath);

        // Create the initial version of the schema, and roll it forward to the latest
        dbPath = TARGET_DIR + "initial";
        DerbyMaster.dropDatabase(dbPath);
        try (DerbyMaster db = new DerbyMaster(dbPath)) {
            // Set up the version history service first if it doesn't yet exist
            db.runWithAdapter(adapter -> CreateVersionHistory.createTableIfNeeded(ADMIN_SCHEMA_NAME, adapter));

            // Current version history for the database. This is used by applyWithHistory
            // to determine which updates to apply and to record the new changes as they
            // are applied
            IConnectionProvider cp = new DerbyConnectionProvider(db, null);
            PoolConnectionProvider connectionPool = new PoolConnectionProvider(cp, 200);
            ITransactionProvider transactionProvider = new SimpleTransactionProvider(connectionPool);
            IDatabaseAdapter adapter = new DerbyAdapter(connectionPool);

            VersionHistoryService vhs = new VersionHistoryService(ADMIN_SCHEMA_NAME, SCHEMA_NAME, OAUTH_SCHEMANAME);
            vhs.setTransactionProvider(transactionProvider);
            vhs.setTarget(adapter);
            vhs.init();

            // 2 Build the old derby database
            try (ITransaction tx = transactionProvider.getTransaction()) {
                try {
                    createOldDerbyDatabase(db, connectionPool, vhs, resourceTypes);
                } catch (Throwable x) {
                    // let's see who's causing the lock trouble
                    db.dumpLockInfo();
                    throw x;
                }
            }

            // refresh the version history service to match what is in the database
            vhs.init();

            // 3. Upgrade the old schema to the new one
            try (ITransaction tx = transactionProvider.getTransaction()) {
                try {
                    createOrUpgradeSchema(db, connectionPool, vhs, resourceTypes);
                } catch (Throwable x) {
                    // let's see who's causing the lock trouble
                    db.dumpLockInfo();
                    throw x;
                }
            }
        }

        System.out.println("FHIR database migrated successfully.");

        // 4. Assert they match
        List<String> migrated_ddl = inferDDL(dbPath);
        //System.out.println(FhirSchemaVersion.V0001.name() + " migrated: " + migrated_ddl);
        //System.out.println(FhirSchemaVersion.V0009.name() + "   latest: " + latest_ddl);

        // If the DDL is different, it's helpful to reveal the delta
        Set<String> migratedSet = new HashSet<>(migrated_ddl);
        Set<String> latestSet = new HashSet<>(latest_ddl);

        List<String> differences = latestSet.stream()
                .filter(element -> !migratedSet.contains(element))
                .collect(Collectors.toList());

        for (String diff: differences) {
            System.out.println("MISSING FROM MIGRATION: " + diff);
        }

        // Check the DDL in the migrated set but not found in the latest
        differences = migratedSet.stream()
                .filter(element -> !latestSet.contains(element))
                .collect(Collectors.toList());

        for (String diff: differences) {
            System.out.println("MISSING FROM LATEST: " + diff);
        }

        assertEquals(latest_ddl, migrated_ddl);
    }

    private void createOrUpgradeSchema(DerbyMaster db, IConnectionProvider pool, VersionHistoryService vhs, Set<String> resourceTypes) throws SQLException {


        FhirSchemaGenerator gen = new FhirSchemaGenerator(ADMIN_SCHEMA_NAME, SCHEMA_NAME, false, resourceTypes);
        PhysicalDataModel pdm = new PhysicalDataModel();
        gen.buildSchema(pdm);

        db.createSchema(pool, vhs, pdm);
    }

    /**
     * Construct a Derby database at the specified path and deploy the IBM FHIR Server schema for the passed resource types.
     */
    private void createOldDerbyDatabase(DerbyMaster derby, IConnectionProvider pool, VersionHistoryService vhs, Set<String> resourceTypes) throws SQLException {

        // Database objects for the admin schema (shared across multiple tenants in the same DB)
        OldFhirSchemaGenerator gen = new OldFhirSchemaGenerator(ADMIN_SCHEMA_NAME, SCHEMA_NAME, resourceTypes);
        PhysicalDataModel pdm = new PhysicalDataModel();
        gen.buildSchema(pdm);
        gen.buildProcedures(pdm);

        // apply the model we've defined to the new Derby database
        derby.createSchema(pool, vhs, pdm);

        System.out.println("Old FHIR database created successfully.");
    }

    /**
     * Construct a Derby database at the specified path and deploy the IBM FHIR Server schema for the passed resource types.
     */
    private void create455DerbyDatabase(DerbyMaster derby, IConnectionProvider pool, VersionHistoryService vhs, Set<String> resourceTypes) throws SQLException {

        // Database objects for the admin schema (shared across multiple tenants in the same DB)
        FhirSchemaGenerator455 gen = new FhirSchemaGenerator455(ADMIN_SCHEMA_NAME, SCHEMA_NAME, false, resourceTypes);
        PhysicalDataModel pdm = new PhysicalDataModel();
        gen.buildSchema(pdm);

        // apply the model we've defined to the new Derby database
        derby.createSchema(pool, vhs, pdm);

        System.out.println("Release 455 FHIR database created successfully.");
    }

    /**
     * Dump the DDL, sort it (lexically), and return the lines
     */
    private List<String> inferDDL(String dbPath) {
        // Nasty trick to capture the output stream of dblook
        // https://stackoverflow.com/a/8708357/161022
        PrintStream originalSysOut = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        System.setOut(ps);

        dblook.main(new String[] {"-d", "jdbc:derby:" + dbPath});

        // Restore the original System.out
        System.out.flush();
        System.setOut(originalSysOut);

        // Trim the first 5 lines of dblook output which are more like metadata; for example:
        // -- Timestamp: 2020-03-25 01:21:36.91
        // -- Source database is: target/derby/2019
        // -- Connection URL is: jdbc:derby:target/derby/2019
        // -- appendLogs: false
        //
        // -- ----------------------------------------------
        // -- DDL Statements for schemas
        // -- ----------------------------------------------
        // ...
        BufferedReader bufferedReader = new BufferedReader(new StringReader(baos.toString()));
        return bufferedReader.lines().skip(5).filter(line -> !line.isEmpty()).sorted().collect(Collectors.toList());
    }

    /**
     * This variant of the schema migration test checks migration from the Release 4.5.5 version
     * to the latest version. This checks that new schema generated for 4.5.5 can also be upgraded
     * @throws Exception
     */
    @Test
    public void testMigrateFhirSchema455() throws Exception {
        // The schema for each resource is the same, so we only need to
        // exercise migration for one type. Much quicker.
        Set<String> resourceTypes = Collections.singleton("Observation");

        // 1. Create the newest version of the schema
        String dbPath = TARGET_DIR + "latest";
        DerbyMaster.dropDatabase(dbPath);
        try (DerbyMaster db = new DerbyMaster(dbPath)) {
            // Set up the version history service first if it doesn't yet exist
            db.runWithAdapter(adapter -> CreateVersionHistory.createTableIfNeeded(ADMIN_SCHEMA_NAME, adapter));

            IConnectionProvider cp = new DerbyConnectionProvider(db, null);
            PoolConnectionProvider connectionPool = new PoolConnectionProvider(cp, 200);
            ITransactionProvider transactionProvider = new SimpleTransactionProvider(connectionPool);
            IDatabaseAdapter adapter = new DerbyAdapter(connectionPool);

            VersionHistoryService vhs = new VersionHistoryService(ADMIN_SCHEMA_NAME, SCHEMA_NAME, OAUTH_SCHEMANAME);
            vhs.setTransactionProvider(transactionProvider);
            vhs.setTarget(adapter);
            vhs.init();

            try (ITransaction tx = transactionProvider.getTransaction()) {
                try {
                    createOrUpgradeSchema(db, connectionPool, vhs, resourceTypes);
                } catch (Throwable x) {
                    // let's see who's causing the lock trouble
                    db.dumpLockInfo();
                    throw x;
                }
            }
        }

        // Generate a list of DDL statements describing the new database
        List<String> latest_ddl = inferDDL(dbPath);

        // Create the Release 4.5.5 version of the schema, and roll it forward to the latest
        dbPath = TARGET_DIR + "initial";
        DerbyMaster.dropDatabase(dbPath);
        try (DerbyMaster db = new DerbyMaster(dbPath)) {
            // Set up the version history service first if it doesn't yet exist
            db.runWithAdapter(adapter -> CreateVersionHistory.createTableIfNeeded(ADMIN_SCHEMA_NAME, adapter));

            // Current version history for the database. This is used by applyWithHistory
            // to determine which updates to apply and to record the new changes as they
            // are applied
            IConnectionProvider cp = new DerbyConnectionProvider(db, null);
            PoolConnectionProvider connectionPool = new PoolConnectionProvider(cp, 200);
            ITransactionProvider transactionProvider = new SimpleTransactionProvider(connectionPool);
            IDatabaseAdapter adapter = new DerbyAdapter(connectionPool);

            VersionHistoryService vhs = new VersionHistoryService(ADMIN_SCHEMA_NAME, SCHEMA_NAME, OAUTH_SCHEMANAME);
            vhs.setTransactionProvider(transactionProvider);
            vhs.setTarget(adapter);
            vhs.init();

            // 2 Build the old derby database
            try (ITransaction tx = transactionProvider.getTransaction()) {
                try {
                    create455DerbyDatabase(db, connectionPool, vhs, resourceTypes);
                } catch (Throwable x) {
                    // let's see who's causing the lock trouble
                    db.dumpLockInfo();
                    throw x;
                }
            }

            // refresh the version history service to match what is in the database
            vhs.init();

            // 3. Upgrade the old schema to the new one
            try (ITransaction tx = transactionProvider.getTransaction()) {
                try {
                    createOrUpgradeSchema(db, connectionPool, vhs, resourceTypes);
                } catch (Throwable x) {
                    // let's see who's causing the lock trouble
                    db.dumpLockInfo();
                    throw x;
                }
            }
        }

        System.out.println("FHIR database migrated successfully.");

        // 4. Assert they match
        List<String> migrated_ddl = inferDDL(dbPath);
        //System.out.println(FhirSchemaVersion.V0001.name() + " migrated: " + migrated_ddl);
        //System.out.println(FhirSchemaVersion.V0009.name() + "   latest: " + latest_ddl);

        // If the DDL is different, it's helpful to reveal the delta
        Set<String> migratedSet = new HashSet<>(migrated_ddl);
        Set<String> latestSet = new HashSet<>(latest_ddl);

        List<String> differences = latestSet.stream()
                .filter(element -> !migratedSet.contains(element))
                .collect(Collectors.toList());

        for (String diff: differences) {
            System.out.println("MISSING FROM MIGRATION: " + diff);
        }

        // Check the DDL in the migrated set but not found in the latest
        differences = migratedSet.stream()
                .filter(element -> !latestSet.contains(element))
                .collect(Collectors.toList());

        for (String diff: differences) {
            System.out.println("MISSING FROM LATEST: " + diff);
        }

        assertEquals(latest_ddl, migrated_ddl);
    }
}