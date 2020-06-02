/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.derby;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.IConnectionProvider;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.api.ITransactionProvider;
import com.ibm.fhir.database.utils.common.JdbcConnectionProvider;
import com.ibm.fhir.database.utils.common.JdbcPropertyAdapter;
import com.ibm.fhir.database.utils.common.JdbcTarget;
import com.ibm.fhir.database.utils.derby.DerbyAdapter;
import com.ibm.fhir.database.utils.derby.DerbyMaster;
import com.ibm.fhir.database.utils.derby.DerbyTranslator;
import com.ibm.fhir.database.utils.model.PhysicalDataModel;
import com.ibm.fhir.database.utils.pool.PoolConnectionProvider;
import com.ibm.fhir.database.utils.transaction.SimpleTransactionProvider;
import com.ibm.fhir.database.utils.version.CreateVersionHistory;
import com.ibm.fhir.database.utils.version.VersionHistoryService;
import com.ibm.fhir.schema.app.Main;
import com.ibm.fhir.schema.control.FhirSchemaGenerator;
import com.ibm.fhir.schema.control.PopulateParameterNames;
import com.ibm.fhir.schema.control.PopulateResourceTypes;

/**
 * An Apache Derby implementation of the IBM FHIR Server database (useful for supporting unit tests).
 */
public class DerbyFhirDatabase implements AutoCloseable, IConnectionProvider {
    private static final Logger logger = Logger.getLogger(DerbyFhirDatabase.class.getName());
    private static final String DATABASE_NAME = "derby/fhirDB";
    private static final String SCHEMA_NAME = Main.DATA_SCHEMANAME;
    private static final String ADMIN_SCHEMA_NAME = Main.ADMIN_SCHEMANAME;
    private static final String OAUTH_SCHEMANAME = Main.OAUTH_SCHEMANAME;
    private static final String BATCH_SCHEMANAME = Main.BATCH_SCHEMANAME;

    // The translator to help us out with Derby syntax
    private static final IDatabaseTranslator DERBY_TRANSLATOR = new DerbyTranslator();

    // The wrapper for managing a derby in-memory instance
    final DerbyMaster derby;

    /**
     * The default constructor will initialize the database at "derby/fhirDB".
     */
    public DerbyFhirDatabase() throws SQLException {
        this(DATABASE_NAME);
    }

    /**
     * Construct a Derby database at the specified path and deploy the IBM FHIR Server schema.
     */
    public DerbyFhirDatabase(String dbPath) throws SQLException {
        logger.info("Creating Derby database for FHIR: " + dbPath);
        derby = new DerbyMaster(dbPath);

        // Lambdas are quite tasty for this sort of thing
        derby.runWithAdapter(adapter -> CreateVersionHistory.createTableIfNeeded(ADMIN_SCHEMA_NAME, adapter));

        // Database objects for the admin schema (shared across multiple tenants in the same DB)
        FhirSchemaGenerator gen = new FhirSchemaGenerator(ADMIN_SCHEMA_NAME, SCHEMA_NAME);
        PhysicalDataModel pdm = new PhysicalDataModel();
        gen.buildSchema(pdm);

        // apply the model we've defined to the new Derby database
        VersionHistoryService vhs = createVersionHistoryService();
        derby.createSchema(vhs, pdm);

        // Populates Lookup tables
        populateResourceTypeAndParameterNameTableEntries();
    }

    /**
     * prepopulates the bootstrapped derby database with static lookup data.
     * 
     * @throws SQLException
     */
    public void populateResourceTypeAndParameterNameTableEntries() throws SQLException {
        // Fill any static data tables (which are also partitioned by tenant)
        // Prepopulate the Resource Type Tables and Parameters Name/Code Table
        logger.info("started prepopulating lookup table data.");
        DerbyTranslator translator = new DerbyTranslator();
        Connection connection = getConnection();

        // Ensures we don't double up the generated derby db prepopulation.
        // Docs for the table are at https://db.apache.org/derby/docs/10.5/ref/rrefsistabs24269.html
        boolean process = true;
        final String sql = "SELECT COUNT(TABLENAME) AS CNT FROM SYS.SYSTABLES WHERE TABLENAME = 'PARAMETER_NAMES'";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.execute();
            ResultSet set = stmt.getResultSet();
            if (set.next()) {
                int val = set.getInt("CNT");
                if (val > 0) {
                    process = false;
                }
            }
        }

        if (process) {
            PopulateResourceTypes populateResourceTypes =
                    new PopulateResourceTypes(ADMIN_SCHEMA_NAME, SCHEMA_NAME, null);
            populateResourceTypes.run(translator, connection);

            PopulateParameterNames populateParameterNames =
                    new PopulateParameterNames(ADMIN_SCHEMA_NAME, SCHEMA_NAME, null);
            populateParameterNames.run(translator, connection);
            connection.commit();
            logger.info("Finished prepopulating the resource type and search parameter code/name tables tables");
        } else {
            logger.info("Skipped prepopulating the resource type and search parameter code/name tables tables");
        }
    }

    /**
     * Configure the TransactionProvider
     * 
     * @throws SQLException
     */
    public VersionHistoryService createVersionHistoryService() throws SQLException {
        Connection c = derby.getConnection();
        JdbcTarget target = new JdbcTarget(c);

        JdbcPropertyAdapter jdbcAdapter = new JdbcPropertyAdapter(new Properties());
        JdbcConnectionProvider cp = new JdbcConnectionProvider(DERBY_TRANSLATOR, jdbcAdapter);
        PoolConnectionProvider connectionPool = new PoolConnectionProvider(cp, 200);
        ITransactionProvider transactionProvider = new SimpleTransactionProvider(connectionPool);

        DerbyAdapter derbyAdapter = new DerbyAdapter(target);
        CreateVersionHistory.createTableIfNeeded(ADMIN_SCHEMA_NAME, derbyAdapter);

        // Current version history for the data schema
        VersionHistoryService vhs = new VersionHistoryService(ADMIN_SCHEMA_NAME, SCHEMA_NAME, OAUTH_SCHEMANAME, BATCH_SCHEMANAME);
        vhs.setTransactionProvider(transactionProvider);
        vhs.setTarget(derbyAdapter);
        vhs.init();
        return vhs;
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
        Connection result = derby.getConnection();
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