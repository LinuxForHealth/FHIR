/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.derby;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.IConnectionProvider;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.api.ITransaction;
import com.ibm.fhir.database.utils.api.ITransactionProvider;
import com.ibm.fhir.database.utils.api.SchemaType;
import com.ibm.fhir.database.utils.common.JdbcTarget;
import com.ibm.fhir.database.utils.derby.DerbyAdapter;
import com.ibm.fhir.database.utils.derby.DerbyConnectionProvider;
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

    // The wrapper for managing a derby in-memory instance
    private final DerbyMaster derby;

    // Connection pool used to work alongside the transaction provider
    private final PoolConnectionProvider connectionPool;
    
    // Simple transaction service for use outside of JEE
    private final ITransactionProvider transactionProvider;


    /**
     * The default constructor will initialize the database at "derby/fhirDB".
     */
    public DerbyFhirDatabase() throws SQLException {
        this(DATABASE_NAME);
    }

    /**
     * Initialize the database using the given file-system path and build tables
     * for all the resource types
     * @param dbPath
     * @throws SQLException
     */
    public DerbyFhirDatabase(String dbPath) throws SQLException {
        this(dbPath, null);
    }

    /**
     * Construct a Derby database at the specified path and deploy the IBM FHIR Server schema.
     */
    public DerbyFhirDatabase(String dbPath,  Set<String> resourceTypeNames) throws SQLException {
        logger.info("Creating Derby database for FHIR: " + dbPath);
        derby = new DerbyMaster(dbPath);
        this.connectionPool = new PoolConnectionProvider(new DerbyConnectionProvider(derby, null), 200);
        this.transactionProvider = new SimpleTransactionProvider(connectionPool);

        // Lambdas are quite tasty for this sort of thing
        derby.runWithAdapter(adapter -> CreateVersionHistory.createTableIfNeeded(ADMIN_SCHEMA_NAME, adapter));

        // Database objects for the admin schema (shared across multiple tenants in the same DB)
        PhysicalDataModel pdm = new PhysicalDataModel();
        if (resourceTypeNames == null) {
            FhirSchemaGenerator gen = new FhirSchemaGenerator(ADMIN_SCHEMA_NAME, SCHEMA_NAME, SchemaType.PLAIN);
            gen.buildSchema(pdm);
        } else {
            // just build out a subset of tables
            FhirSchemaGenerator gen = new FhirSchemaGenerator(ADMIN_SCHEMA_NAME, SCHEMA_NAME, SchemaType.PLAIN, resourceTypeNames);
            gen.buildSchema(pdm);
        }

        // apply the model we've defined to the new Derby database
        VersionHistoryService vhs = createVersionHistoryService();
        
        // Create the schema in a managed transaction
        try (ITransaction tx = transactionProvider.getTransaction()) {
            try {
                derby.createSchema(connectionPool, vhs, pdm);
            } catch (Throwable t) {
                // mark the transaction for rollback
                tx.setRollbackOnly();
                throw t;
            }
        }
        
        // Populates Lookup tables
        populateResourceTypeAndParameterNameTableEntries();
    }

    /**
     * Get the FHIR data schema name
     * @return
     */
    public String getSchemaName() {
        return SCHEMA_NAME;
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
        try (Connection connection = getConnection()) {

            // Ensures we don't double up the generated derby db prepopulation.
            boolean populated;
            final String sql = "SELECT 1 FROM PARAMETER_NAMES FETCH FIRST 1 ROWS ONLY";
            try (Statement stmt = connection.createStatement()) {
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    populated = true;
                } else {
                    populated = false;
                }
            }
    
            if (!populated) {
                PopulateResourceTypes populateResourceTypes =
                        new PopulateResourceTypes(SCHEMA_NAME);
                populateResourceTypes.run(translator, connection);
    
                PopulateParameterNames populateParameterNames =
                        new PopulateParameterNames(SCHEMA_NAME);
                populateParameterNames.run(translator, connection);
                logger.info("Finished prepopulating the resource type and search parameter code/name tables tables");
            } else {
                logger.info("Skipped prepopulating the resource type and search parameter code/name tables tables");
            }
            
            // always commit here before we close the connection.
            connection.commit();
        }
    }

    /**
     * Create the version history table and a simple service which is used to
     * access information from it.
     * 
     * @throws SQLException
     */
    public VersionHistoryService createVersionHistoryService() throws SQLException {

        // No complex transaction handling required here. Simply check if the versions
        // table exists, and if not, create it.
        try (Connection c = derby.getConnection()) {
            try {
                JdbcTarget target = new JdbcTarget(c);
                DerbyAdapter derbyAdapter = new DerbyAdapter(target);
                CreateVersionHistory.createTableIfNeeded(ADMIN_SCHEMA_NAME, DerbyMaster.wrap(derbyAdapter));
                c.commit();
            } catch (SQLException x) {
                logger.log(Level.SEVERE, "failed to create version history table", x);
                c.rollback(); // may generate its own exception if the database is messed up
                throw x;
            }
        }
        
        // Current version history for the data schema.
        VersionHistoryService vhs = new VersionHistoryService(ADMIN_SCHEMA_NAME, SCHEMA_NAME, OAUTH_SCHEMANAME, BATCH_SCHEMANAME);
        vhs.setTransactionProvider(transactionProvider);
        vhs.setTarget(new DerbyAdapter(this.connectionPool));
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
        
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("Setting current schema: " + SCHEMA_NAME);
        }
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