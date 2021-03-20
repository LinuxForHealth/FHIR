/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.derby;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;

import org.testng.annotations.Test;

import com.ibm.fhir.database.utils.api.IConnectionProvider;
import com.ibm.fhir.database.utils.common.GetSequenceNextValueDAO;
import com.ibm.fhir.database.utils.common.JdbcTarget;
import com.ibm.fhir.database.utils.derby.DerbyAdapter;
import com.ibm.fhir.database.utils.derby.DerbyMaster;
import com.ibm.fhir.schema.control.FhirSchemaConstants;
import com.ibm.fhir.schema.control.GetXXXLogicalResourceNeedsMigration;

/**
 * Unit test for the DerbyFhirDatabase utility
 */
public class DerbyFhirDatabaseTest {
    private static final String DB_NAME = "target/derby/fhirDB";

    @Test
    public void testFhirSchema() throws Exception {
        // We want to test the whole schema creation process, so need to start
        // with a new database.
        System.out.println("Dropping test database: " + DB_NAME);
        DerbyMaster.dropDatabase(DB_NAME);
        try (DerbyFhirDatabase db = new DerbyFhirDatabase(DB_NAME)) {
            System.out.println("FHIR database created successfully.");
            checkDatabase(db);
            testMigrationFunction(db);
        }

        // Now that we've got an existing database, let's try the creation again...which should be a NOP
        try (DerbyFhirDatabase db = new DerbyFhirDatabase(DB_NAME)) {
            System.out.println("FHIR database exists.");
            checkDatabase(db);
        }
    }

    protected void testMigrationFunction(IConnectionProvider cp) throws SQLException {
        try (Connection c = cp.getConnection()) {
            try {
                JdbcTarget tgt = new JdbcTarget(c);
                DerbyAdapter adapter = new DerbyAdapter(tgt);
                GetXXXLogicalResourceNeedsMigration cmd = new GetXXXLogicalResourceNeedsMigration("FHIRDATA", "Observation");
                assertFalse(adapter.runStatement(cmd));
                c.commit();
            } catch (Throwable t) {
                c.rollback();
                throw t;
            }
        }
    }

    protected void checkDatabase(IConnectionProvider cp) throws SQLException {

        try (Connection c = cp.getConnection()) {
            try {
                JdbcTarget tgt = new JdbcTarget(c);
                DerbyAdapter adapter = new DerbyAdapter(tgt);
                checkRefSequence(adapter);
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