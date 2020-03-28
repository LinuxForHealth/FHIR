/*
 * (C) Copyright IBM Corp. 2020
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
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.LogManager;
import java.util.stream.Collectors;

import org.apache.derby.tools.dblook;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.database.utils.common.JdbcTarget;
import com.ibm.fhir.database.utils.derby.DerbyAdapter;
import com.ibm.fhir.database.utils.derby.DerbyMaster;
import com.ibm.fhir.database.utils.model.PhysicalDataModel;
import com.ibm.fhir.database.utils.version.CreateVersionHistory;
import com.ibm.fhir.database.utils.version.VersionHistoryService;
import com.ibm.fhir.schema.control.FhirSchemaGenerator;

/**
 * Unit test for migration logic within the IBM FHIR Server schema
 */
public class DerbyMigrationTest {
    private static final String TARGET_DIR = "target/derby/";
    private static final String SCHEMA_NAME = "FHIRDATA";
    private static final String ADMIN_SCHEMA_NAME = "FHIR_ADMIN";

    @BeforeClass(alwaysRun = true)
    protected void setUp() throws SecurityException, IOException
    {
        FileInputStream fis = new FileInputStream(DerbyMigrationTest.class.getResource("/logging.unitTest.properties").getFile());
        LogManager.getLogManager().readConfiguration(fis);
    }

    @Test
    public void testMigrateFhirSchema() throws Exception {
        Set<String> resourceTypes = Collections.singleton("Observation");

        // 1. Create the new schema
        String dbPath = TARGET_DIR + "2020-1";
        try (DerbyMaster db_2020_1 = new DerbyMaster(TARGET_DIR + "2020-1")) {
            createOrUpgradeSchema(resourceTypes, db_2020_1);
            List<String> db_2020_1_ddl = inferDDL(dbPath);

            // 2. Create the old schema
            dbPath = TARGET_DIR + "2019";
            try (DerbyMaster db_2019 = createOldDerbyDatabase(dbPath, resourceTypes)) {

                // 3. Upgrade the old schema to the new one
                createOrUpgradeSchema(resourceTypes, db_2019);
                System.out.println("FHIR database migrated successfully.");

                // 4. Assert they match
                List<String> db_2019_migrated_ddl = inferDDL(dbPath);
                System.out.println("2019 migrated: " + db_2019_migrated_ddl);
                System.out.println("2020-1: " + db_2020_1_ddl);
                assertEquals(db_2020_1_ddl, db_2019_migrated_ddl);
            }
        }
    }

    private void createOrUpgradeSchema(Set<String> resourceTypes, DerbyMaster db) throws SQLException {
        Connection conn = db.getConnection();
        // Set up the version history service first if it doesn't yet exist
        db.runWithAdapter(adapter -> CreateVersionHistory.createTableIfNeeded(ADMIN_SCHEMA_NAME, adapter));

        FhirSchemaGenerator gen = new FhirSchemaGenerator(ADMIN_SCHEMA_NAME, SCHEMA_NAME, resourceTypes);
        PhysicalDataModel pdm = new PhysicalDataModel();
        gen.buildSchema(pdm);
        gen.buildProcedures(pdm);

        // Current version history for the database. This is used by applyWithHistory
        // to determine which updates to apply and to record the new changes as they
        // are applied

        JdbcTarget target = new JdbcTarget(conn);
        DerbyAdapter adapter = new DerbyAdapter(target);
        VersionHistoryService vhs = new VersionHistoryService(ADMIN_SCHEMA_NAME, SCHEMA_NAME);
        vhs.setTarget(adapter);
        vhs.init();

        // apply the model we've defined to the new Derby database
        db.createSchema(vhs, pdm);
        conn.commit();
    }

    /**
     * Construct a Derby database at the specified path and deploy the IBM FHIR Server schema for the passed resource types.
     */
    private DerbyMaster createOldDerbyDatabase(String dbPath, Set<String> resourceTypes) throws SQLException {
        DerbyMaster derby = new DerbyMaster(dbPath);

        // Set up the version history service first if it doesn't yet exist
        derby.runWithAdapter(adapter -> CreateVersionHistory.createTableIfNeeded(ADMIN_SCHEMA_NAME, adapter));

        // Database objects for the admin schema (shared across multiple tenants in the same DB)
        OldFhirSchemaGenerator gen = new OldFhirSchemaGenerator(ADMIN_SCHEMA_NAME, SCHEMA_NAME, resourceTypes);
        PhysicalDataModel pdm = new PhysicalDataModel();
        gen.buildSchema(pdm);
        gen.buildProcedures(pdm);

        // Current version history for the database. This is used by applyWithHistory
        // to determine which updates to apply and to record the new changes as they
        // are applied
        Connection conn = derby.getConnection();
        JdbcTarget target = new JdbcTarget(conn);
        DerbyAdapter adapter = new DerbyAdapter(target);
        VersionHistoryService vhs = new VersionHistoryService(ADMIN_SCHEMA_NAME, SCHEMA_NAME);
        vhs.setTarget(adapter);
        vhs.init();

        // apply the model we've defined to the new Derby database
        derby.createSchema(vhs, pdm);
        conn.commit();

        System.out.println("FHIR database created successfully.");
        return derby;
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
}
