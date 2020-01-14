/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.derby;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.IConnectionProvider;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.derby.DerbyMaster;
import com.ibm.fhir.database.utils.model.PhysicalDataModel;
import com.ibm.fhir.database.utils.version.CreateVersionHistory;
import com.ibm.fhir.schema.control.FhirSchemaGenerator;

/**
 * Derby implementation of the FHIR database useful for supporting
 * unit-tests.
 */
public class DerbyFhirDatabase implements AutoCloseable, IConnectionProvider {
    private static final Logger logger = Logger.getLogger(DerbyFhirDatabase.class.getName());
    private static final String DATABASE_NAME = "derby/fhirDB";
    private static final String SCHEMA_NAME = "FHIRDATA";
    private static final String ADMIN_SCHEMA_NAME = "FHIR_ADMIN";

    // The wrapper for managing a derby in-memory instance
    final DerbyMaster derby;

    // current connection cached for this thread
    // <code>final ThreadLocal<ManagedConnection> currentConnection = new ThreadLocal<>();</code>

    public DerbyFhirDatabase() throws SQLException {
        logger.info("Creating Derby database for FHIR: " + DATABASE_NAME);
        derby = new DerbyMaster(DATABASE_NAME);

        // Lambdas are quite tasty for this sort of thing
        derby.runWithAdapter(adapter -> CreateVersionHistory.createTableIfNeeded(ADMIN_SCHEMA_NAME, adapter));

        // Database objects for the admin schema (shared across multiple tenants in the same DB)
        FhirSchemaGenerator gen = new FhirSchemaGenerator(ADMIN_SCHEMA_NAME, SCHEMA_NAME);
        PhysicalDataModel pdm = new PhysicalDataModel();
        gen.buildSchema(pdm);
        gen.buildProcedures(pdm);

        // apply the model we've defined to the new Derby database
        derby.createSchema(pdm);
    }

    @Override
    public void close() throws Exception {
        derby.close();
    }

    @Override
    public void commitTransaction() throws SQLException {
        // NOP
    }

    @Override
    public void describe(String arg0, StringBuilder arg1, String arg2) {
    }

    @Override
    public Connection getConnection() throws SQLException {
        Connection result = derby.createConnection();
        result.setSchema(SCHEMA_NAME);
        return result;
    }

    @Override
    public IDatabaseTranslator getTranslator() {
        return derby.getTranslator();
    }

    @Override
    public void rollbackTransaction() throws SQLException {
        // NOP
    }
}
