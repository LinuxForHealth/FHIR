/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.schema.derby;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.linuxforhealth.fhir.database.utils.api.IConnectionProvider;
import org.linuxforhealth.fhir.database.utils.api.IDatabaseAdapter;
import org.linuxforhealth.fhir.database.utils.api.ISchemaAdapter;
import org.linuxforhealth.fhir.database.utils.api.ITransaction;
import org.linuxforhealth.fhir.database.utils.api.ITransactionProvider;
import org.linuxforhealth.fhir.database.utils.api.SchemaType;
import org.linuxforhealth.fhir.database.utils.common.GetSequenceNextValueDAO;
import org.linuxforhealth.fhir.database.utils.common.JdbcTarget;
import org.linuxforhealth.fhir.database.utils.common.PlainSchemaAdapter;
import org.linuxforhealth.fhir.database.utils.common.PreparedStatementHelper;
import org.linuxforhealth.fhir.database.utils.common.SchemaInfoObject;
import org.linuxforhealth.fhir.database.utils.derby.DerbyAdapter;
import org.linuxforhealth.fhir.database.utils.derby.DerbyMaster;
import org.linuxforhealth.fhir.database.utils.model.DatabaseObjectType;
import org.linuxforhealth.fhir.database.utils.model.PhysicalDataModel;
import org.linuxforhealth.fhir.database.utils.pool.PoolConnectionProvider;
import org.linuxforhealth.fhir.database.utils.transaction.SimpleTransactionProvider;
import org.linuxforhealth.fhir.database.utils.version.CreateWholeSchemaVersion;
import org.linuxforhealth.fhir.database.utils.version.VersionHistoryService;
import org.linuxforhealth.fhir.schema.app.Main;
import org.linuxforhealth.fhir.schema.control.FhirSchemaConstants;
import org.linuxforhealth.fhir.schema.control.FhirSchemaGenerator;
import org.linuxforhealth.fhir.schema.control.GetXXLogicalResourceNeedsMigration;
import org.linuxforhealth.fhir.schema.control.TableHasData;
import org.testng.annotations.Test;

/**
 * Unit test for the DerbyFhirDatabase utility
 */
public class DerbyFhirDatabaseTest {
    private static final String DB_NAME = "target/derby/fhirDB";
    private static final String ADMIN_SCHEMA_NAME = Main.ADMIN_SCHEMANAME;
    private final Instant lastUpdated = Instant.now();
    private static final String SCHEMA_NAME = "FHIRDATA";
    private final String EVIDENCE_LOGICAL_ID = UUID.randomUUID().toString();
    private final String parameterHash = "1Z+NWYZb739Ava9Pd/d7wt2xecKmC2FkfLlCCml0I5M=";

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
            testTableHasDataFunction(db);
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
                assertEquals(adapter.listSchemaObjects(schemaName).size(), 2276);
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

    /**
     * test to check if data exists in the given table.
     *
     * @param cp the connection provider
     * @throws SQLException
     */
    protected void testTableHasDataFunction(IConnectionProvider cp) throws SQLException {


        try (Connection connection = cp.getConnection()) {
            try {
                JdbcTarget tgt = new JdbcTarget(connection);
                DerbyAdapter adapter = new DerbyAdapter(tgt);

                // validate table data when the table is empty
                TableHasData cmd = new TableHasData(SCHEMA_NAME, "evidence_logical_resources", adapter);
                assertFalse(adapter.runStatement(cmd));
                cmd = new TableHasData(SCHEMA_NAME, "evidencevariable_logical_resources", adapter);
                assertFalse(adapter.runStatement(cmd));

                // add records to evidence_logical_resources and evidencevariable_logical_resources tables
                prepareTestDataForTestTableHasDataFunction(connection, adapter);

                //validate table data
                cmd = new TableHasData(SCHEMA_NAME, "evidence_logical_resources", adapter);
                assertTrue(adapter.runStatement(cmd));
                cmd = new TableHasData(SCHEMA_NAME, "evidencevariable_logical_resources", adapter);
                assertTrue(adapter.runStatement(cmd));
                connection.rollback(); // roll back the changes
            } catch (Throwable t) {
                connection.rollback();
                throw t;
            }
        }

    }


    /**
     * Prepare test data to check if data exists in the given table.
     *
     * @param connection the JDBC connection
     * @param adapter the database adapter
     * @throws SQLException
     */
    private void prepareTestDataForTestTableHasDataFunction(Connection connection, IDatabaseAdapter adapter) throws SQLException {
        int resourceTypeId = getResourceType(connection);

        long logicalResourceId = getNextLogicalId(connection, adapter);

        final String insertLogicalResource = "INSERT INTO logical_resources(logical_resource_id, resource_type_id, logical_id, last_updated, is_deleted, parameter_hash)"
                + " VALUES (?,?,?,?,?,?)";
        final Timestamp lastUpdated = Timestamp.from(this.lastUpdated);
        try (PreparedStatement ps = connection.prepareStatement(insertLogicalResource)) {
            PreparedStatementHelper psh = new PreparedStatementHelper(ps);
            psh.setLong(logicalResourceId)
            .setInt(resourceTypeId)
            .setString(EVIDENCE_LOGICAL_ID)
            .setTimestamp(lastUpdated)
            .setString("N")
            .setString(parameterHash);
            ps.executeUpdate();
        }
        addLogicalData(connection, logicalResourceId, lastUpdated, "evidence_logical_resources");
        addLogicalData(connection, logicalResourceId, lastUpdated, "evidencevariable_logical_resources");

    }

    /**
     * Adds test data to logical resource table.
     *
     * @param connnection the JDBC connection
     * @param logicalResourceId the logical resource ID (primary key) of a specific resource
     * @param lastUpdated the last updated time stamp(When the resource version last changed)
     * @param tableName the table name
     * @throws SQLException
     */
    private void addLogicalData(Connection connection, long logicalResourceId, final Timestamp lastUpdated, String tableName) throws SQLException {
        final String insertEvidenceLogicalResource =
          "INSERT INTO " + tableName + "(logical_resource_id, logical_id, is_deleted, last_updated, version_id)"
          + " VALUES (?,?,?,?,?)";

          try (PreparedStatement stmt = connection.prepareStatement(insertEvidenceLogicalResource)) {
              // bind parameters
              PreparedStatementHelper psh = new PreparedStatementHelper(stmt);
              psh.setLong(logicalResourceId)
              .setString("evidence123")
              .setString("N")
              .setTimestamp(lastUpdated)
              .setInt(1);
              stmt.executeUpdate();
          }
    }

    /**
     * Gets the next value from the sequence FHIR_SEQUENCE.
     *
     * @param connection the JDBC connection
     * @param adapter the database adapter
     * @return long - the next value from the sequence FHIR_SEQUENCE
     * @throws SQLException
     * @throws IllegalStateException
     */
    private long getNextLogicalId(Connection connection, IDatabaseAdapter adapter) throws SQLException, IllegalStateException {
        GetSequenceNextValueDAO cv = new GetSequenceNextValueDAO(SCHEMA_NAME, FhirSchemaConstants.FHIR_SEQUENCE);
        Long logicalResourceId = adapter.runStatement(cv);
        return logicalResourceId;
    }


    /**
     * Gets the id associated with the name of the passed Resource type from the Resource_Types table.
     *
     * @param connection the connection
     * @return int - the resource type id(the id associated with the name of the passed Resource type)
     * @throws SQLException
     */
    private int getResourceType(Connection connection) throws SQLException {
        final String SELECT_RESOURCE_TYPES = "SELECT resource_type, resource_type_id FROM resource_types where resource_type = 'Evidence' ";
        int resourceTypeId = 0;
        try (PreparedStatement ps = connection.prepareStatement(SELECT_RESOURCE_TYPES)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                resourceTypeId = rs.getInt(2);
            }
        }
        return resourceTypeId;
    }
}