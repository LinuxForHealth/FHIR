/*
 * (C) Copyright IBM Corp. 2017, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import static com.ibm.fhir.schema.control.JavaBatchSchemaGenerator.BATCH_SCHEMANAME;
import static com.ibm.fhir.schema.control.OAuthSchemaGenerator.OAUTH_SCHEMANAME;
import static com.ibm.fhir.schema.app.Main.ADMIN_SCHEMANAME;

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.database.utils.api.IDatabaseAdapter;
import com.ibm.fhir.database.utils.common.JdbcTarget;
import com.ibm.fhir.database.utils.derby.DerbyAdapter;
import com.ibm.fhir.database.utils.derby.DerbyServerPropertiesMgr;
import com.ibm.fhir.database.utils.derby.DerbyTranslator;
import com.ibm.fhir.database.utils.model.DatabaseObjectType;
import com.ibm.fhir.database.utils.model.PhysicalDataModel;
import com.ibm.fhir.database.utils.version.CreateVersionHistory;
import com.ibm.fhir.database.utils.version.VersionHistoryService;
import com.ibm.fhir.schema.control.FhirSchemaGenerator;
import com.ibm.fhir.schema.control.JavaBatchSchemaGenerator;
import com.ibm.fhir.schema.control.OAuthSchemaGenerator;
import com.ibm.fhir.schema.control.PopulateParameterNames;
import com.ibm.fhir.schema.control.PopulateResourceTypes;

/**
 * This class contains bootstrapping code for the Derby Database.
 */
public class DerbyBootstrapper {
    private static final Logger log = Logger.getLogger(DerbyBootstrapper.class.getName());
    private static final String className = DerbyBootstrapper.class.getName();

    /**
     * Bootstraps the FHIR database (only for Derby databases)
     * Note: Since v4.0.0, the schema is generated and applied using fhir-persistence-schema, not liquibase
     *
     * @throws SQLException
     */
    public static void bootstrapDb(DataSource fhirDb) throws SQLException {
        if (log.isLoggable(Level.FINER)) {
            log.entering(className, "bootstrapDb");
        }

        Connection connection = null;
        String dbDriverName;

        try {
            String msg =
                    "Performing derby db bootstrapping for tenant-id '" + FHIRRequestContext.get().getTenantId()
                            + "', datastore-id '" + FHIRRequestContext.get().getDataStoreId() + "'.";
            log.info(msg);
            log.finer("DataSource: " + fhirDb.toString());
            String tenantId = FHIRRequestContext.get().getTenantId();
            String dsId = FHIRRequestContext.get().getDataStoreId();
            log.finer("Obtaining connection for tenantId/dsId: " + tenantId + "/" + dsId);
            connection = fhirDb.getConnection(tenantId, dsId);
            log.finer("Connection: " + connection.toString());

            // Sets the sequence properties on teh database.
            DerbyServerPropertiesMgr.setServerProperties(false, connection);

            dbDriverName = connection.getMetaData().getDriverName();

            if (dbDriverName != null && dbDriverName.contains("Derby")) {
                final String adminSchemaName = "admin_" + tenantId + "_" + dsId;
                final String dataSchemaName = connection.getSchema();

                bootstrap(connection, adminSchemaName, dataSchemaName);
                connection.commit();
            }
        } catch (Throwable e) {
            String msg = "Encountered an exception while bootstrapping the FHIR database";
            log.log(Level.SEVERE, msg, e);
        } finally {
            if (connection != null) {
                log.finer("Closing connection...");
                connection.close();
            }
            if (log.isLoggable(Level.FINER)) {
                log.exiting(className, "bootstrapDb");
            }
        }
    }

    /**
     * Bootstrap the (derby) connection with all the DML we need for an operational FHIR schema
     * Should be idempotent, because we use a version_history table to track which DML statements
     * have been applied, and which are still required
     *
     * @param connection
     * @param adminSchemaName
     * @param dataSchemaName
     * @throws SQLException
     */
    public static void bootstrap(Connection connection, String adminSchemaName, String dataSchemaName)
            throws SQLException {
        JdbcTarget target = new JdbcTarget(connection);
        DerbyAdapter adapter = new DerbyAdapter(target);

        // Set up the version history service first if it doesn't yet exist
        CreateVersionHistory.createTableIfNeeded(adminSchemaName, adapter);

        // Current version history for the database. This is used by applyWithHistory
        // to determine which updates to apply and to record the new changes as they
        // are applied
        VersionHistoryService vhs = new VersionHistoryService(adminSchemaName, dataSchemaName, OAUTH_SCHEMANAME, BATCH_SCHEMANAME);
        vhs.setTarget(adapter);
        vhs.init();

        // Use the version history service to determine if this table existed before we run `applyWithHistory`
        boolean newDb =
                vhs.getVersion(dataSchemaName, DatabaseObjectType.TABLE.name(), "PARAMETER_NAMES") == null
                        || vhs.getVersion(dataSchemaName, DatabaseObjectType.TABLE.name(), "PARAMETER_NAMES") == 0;

        // Define the schema and apply it (or required updates)
        FhirSchemaGenerator gen = new FhirSchemaGenerator(adminSchemaName, dataSchemaName);
        PhysicalDataModel pdm = new PhysicalDataModel();
        gen.buildSchema(pdm);

        // Use the new fhir-persistence-schema mechanism to create/update the derby database
        pdm.applyWithHistory(adapter, vhs);
        
        if (newDb) {
            // prepopulates static lookup data.
            populateResourceTypeAndParameterNameTableEntries(connection, adminSchemaName, dataSchemaName);
        }
    }

    /**
     * prepopulates the bootstrapped derby database with static lookup data.
     *
     * @param connection
     * @param adminSchemaName
     * @param dataSchemaName
     */
    public static void populateResourceTypeAndParameterNameTableEntries(Connection connection, String adminSchemaName,
            String dataSchemaName) {
        // Fill any static data tables (which are also partitioned by tenant)
        // Prepopulate the Resource Type Tables and Parameters Name/Code Table
        log.info("started prepopulating lookup table data.");
        DerbyTranslator translator = new DerbyTranslator();
        PopulateResourceTypes populateResourceTypes = new PopulateResourceTypes(adminSchemaName, dataSchemaName, null);
        populateResourceTypes.run(translator, connection);

        PopulateParameterNames populateParameterNames =
                new PopulateParameterNames(adminSchemaName, dataSchemaName, null);
        populateParameterNames.run(translator, connection);
        log.info("Finished prepopulating the resource type and search parameter code/name tables tables");
    }

    /**
     * Bootstraps the Liberty OAuth 2.0 tables for supporting management of OAuth 2.0 Clients
     */
    public static void bootstrapOauthDb(DataSource ds) throws Exception {
        try (Connection c = ds.getConnection()) {
            try {
                JdbcTarget target = new JdbcTarget(c);
                IDatabaseAdapter adapter = new DerbyAdapter(target);

                // Set up the version history service first if it doesn't yet exist
                CreateVersionHistory.createTableIfNeeded(ADMIN_SCHEMANAME, adapter);

                // Current version history for the database. This is used by applyWithHistory
                // to determine which updates to apply and to record the new changes as they
                // are applied
                VersionHistoryService vhs =
                        new VersionHistoryService(ADMIN_SCHEMANAME, OAUTH_SCHEMANAME);
                vhs.setTarget(adapter);
                vhs.init();

                // Build/update the Liberty OAuth-related tables
                PhysicalDataModel pdm = new PhysicalDataModel();
                OAuthSchemaGenerator oauthSchemaGenerator = new OAuthSchemaGenerator(OAUTH_SCHEMANAME);
                oauthSchemaGenerator.buildOAuthSchema(pdm);
                pdm.applyWithHistory(adapter, vhs);
                c.commit();
            } catch (Exception x) {
                c.rollback();
                throw x;
            }
        }
    }

    /**
     * bootstraps the batch database for derby.
     * @param ds
     * @throws SQLException
     */
    public static void bootstrapBatchDb(DataSource ds) throws SQLException {
        // This is specific to boostrapping where we are not suppose to use the derby db, rather a remote db.
        boolean isDerby = false;
        try (Connection c = ds.getConnection()) {
            try (Statement stmt = c.createStatement();
                    ResultSet rs = stmt.executeQuery("VALUES SYSCS_UTIL.SYSCS_GET_DATABASE_PROPERTY('derby.database.defaultConnectionMode')");) {
                isDerby = rs.next();
                if (log.isLoggable(Level.FINE)) {
                    log.fine(" The results are " + rs.getString(1));
                }
            }
        } catch (java.sql.SQLNonTransientException e) {
            log.fine("Error while checking db that isn't connected, this is expected when not derby" + e.getMessage());
        } catch (Exception e) {
            log.fine("Error while checking db, this is expected" + e.getMessage());
        }

        if (isDerby) {
            try (Connection c = ds.getConnection()) {
                try {
                    JdbcTarget target = new JdbcTarget(c);
                    IDatabaseAdapter adapter = new DerbyAdapter(target);

                    // Set up the version history service first if it doesn't yet exist
                    CreateVersionHistory.createTableIfNeeded(ADMIN_SCHEMANAME, adapter);

                    // Current version history for the database. This is used by applyWithHistory
                    // to determine which updates to apply and to record the new changes as they
                    // are applied
                    VersionHistoryService vhs = new VersionHistoryService(ADMIN_SCHEMANAME, BATCH_SCHEMANAME);
                    vhs.setTarget(adapter);
                    vhs.init();

                    // Build/update the Liberty OAuth-related tables
                    PhysicalDataModel pdm = new PhysicalDataModel();
                    JavaBatchSchemaGenerator javaBatchSchemaGenerator = new JavaBatchSchemaGenerator(BATCH_SCHEMANAME);
                    javaBatchSchemaGenerator.buildJavaBatchSchema(pdm);
                    pdm.applyWithHistory(adapter, vhs);
                    c.commit();
                } catch (Exception x) {
                    c.rollback();
                    throw x;
                }
            }
        }
    }
}