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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.testng.annotations.Test;

import com.ibm.fhir.database.utils.api.IConnectionProvider;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.api.ISchemaAdapter;
import com.ibm.fhir.database.utils.api.ITransaction;
import com.ibm.fhir.database.utils.api.ITransactionProvider;
import com.ibm.fhir.database.utils.api.SchemaType;
import com.ibm.fhir.database.utils.common.GetSequenceNextValueDAO;
import com.ibm.fhir.database.utils.common.JdbcTarget;
import com.ibm.fhir.database.utils.common.PlainSchemaAdapter;
import com.ibm.fhir.database.utils.common.PreparedStatementHelper;
import com.ibm.fhir.database.utils.common.SchemaInfoObject;
import com.ibm.fhir.database.utils.derby.DerbyAdapter;
import com.ibm.fhir.database.utils.derby.DerbyMaster;
import com.ibm.fhir.database.utils.derby.DerbyTranslator;
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
import com.ibm.fhir.schema.control.TableHasData;

/**
 * Unit test for the DerbyFhirDatabase utility
 */
public class DerbyFhirDatabaseTest {
    private static final String DB_NAME = "target/derby/fhirDB";
    private static final String ADMIN_SCHEMA_NAME = Main.ADMIN_SCHEMANAME;
    private final Instant lastUpdated = Instant.now();
    private static final String SCHEMA_NAME = "FHIRDATA";
    private static final IDatabaseTranslator translator = new DerbyTranslator();
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
                assertEquals(adapter.listSchemaObjects(schemaName).size(), 2564);
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
     * Test table has data function.
     *
     * @param cp the cp
     * @throws SQLException the SQL exception
     */
    protected void testTableHasDataFunction(IConnectionProvider cp) throws SQLException {
        
        
        try (Connection connection = cp.getConnection()) {
            try {
                JdbcTarget tgt = new JdbcTarget(connection);
                DerbyAdapter adapter = new DerbyAdapter(tgt);
                
                // validate table data when the table is empty
                TableHasData cmd = new TableHasData("FHIRDATA", "evidence_logical_resources");
                assertFalse(adapter.runStatement(cmd));
                cmd = new TableHasData("FHIRDATA", "evidencevariable_logical_resources");
                assertFalse(adapter.runStatement(cmd));
                
                // add records to evidence_logical_resources and evidencevariable_logical_resources tables
                prepareTestDataForTestTableHasDataFunction(connection);
                
                //validate table data
                cmd = new TableHasData("FHIRDATA", "evidence_logical_resources");
                assertTrue(adapter.runStatement(cmd));
                cmd = new TableHasData("FHIRDATA", "evidencevariable_logical_resources");
                assertTrue(adapter.runStatement(cmd));
                connection.rollback(); // roll back the changes 
            } catch (Throwable t) {
                connection.rollback();
                throw t;
            }
        }
     
    }

    /**
     * Prepare test data for test table has data function.
     *
     * @param connnection the connection
     * @throws SQLException the SQL exception
     */
    private void prepareTestDataForTestTableHasDataFunction(Connection connection) throws SQLException {
        int resourceTypeId = getResourceType(connection); 
        
        long logicalResourceId = getNextLogicalId(connection); 
        
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
     * Adds the logical data.
     *
     * @param connnection the connection
     * @param logicalResourceId the logical resource id
     * @param lastUpdated the last updated
     * @param tableName the table name
     * @throws SQLException the SQL exception
     */
    private void addLogicalData(Connection connection, long logicalResourceId, final Timestamp lastUpdated, String tableName) throws SQLException {
        final String insertEvidenceLogicalResource =
          "INSERT INTO " + tableName + "(logical_resource_id, logical_id, is_deleted, last_updated, version_id)"
          + " VALUES (?,?,?,?,?)";
          
          try (PreparedStatement stmt = connection.prepareStatement(insertEvidenceLogicalResource)) {
              // bind parameters
              stmt.setLong(1, logicalResourceId);
              stmt.setString(2, "evidence123");
              stmt.setString(3, "N");
              stmt.setTimestamp(4, lastUpdated); 
              stmt.setInt(5, 1); 
              stmt.executeUpdate();
          }
    }

    /**
     * Gets the next logical id.
     *
     * @param connection the connection
     * @return the next logical id
     * @throws SQLException the SQL exception
     * @throws IllegalStateException the illegal state exception
     */
    private long getNextLogicalId(Connection connection) throws SQLException, IllegalStateException {
        final String getNextLogicalId = translator.selectSequenceNextValue(SCHEMA_NAME, "fhir_sequence");
        long logicalResourceId;
        try (Statement s = connection.createStatement()) {
            ResultSet rs = s.executeQuery(getNextLogicalId);
            if (rs.next()) {
                logicalResourceId = rs.getLong(1);
            } else {
                throw new IllegalStateException("no row from '" + getNextLogicalId + "'");
            }
        }
        return logicalResourceId;
    }

  
    /**
     * Gets the resource type.
     *
     * @param connection the connection
     * @return the resource type
     * @throws SQLException the SQL exception
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