/*
 * (C) Copyright IBM Corp. 2017, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.database.utils.common.JdbcTarget;
import com.ibm.fhir.database.utils.derby.DerbyAdapter;
import com.ibm.fhir.database.utils.model.PhysicalDataModel;
import com.ibm.fhir.database.utils.version.CreateVersionHistory;
import com.ibm.fhir.database.utils.version.VersionHistoryService;
import com.ibm.fhir.schema.control.FhirSchemaGenerator;

/**
 * This class contains bootstrapping code for the Derby Database.
 */
public class DerbyBootstrapper {
    private static final Logger log = Logger.getLogger(DerbyBootstrapper.class.getName());
    private static final String className = DerbyBootstrapper.class.getName();

    /**
     * Bootstraps the FHIR database (only for Derby databases)
     * Note: Since v4.0.0, the schema is generated and applied using fhir-persistence-schema, not liquibase
     * @throws SQLException
     */
    public static void bootstrapDb(DataSource fhirDb) throws SQLException  {
        if (log.isLoggable(Level.FINER)) {
            log.entering(className, "bootstrapDb");
        }

        Connection connection = null;
        String dbDriverName;

        try {
            String msg = "Performing derby db bootstrapping for tenant-id '" + FHIRRequestContext.get().getTenantId()
                    + "', datastore-id '" + FHIRRequestContext.get().getDataStoreId() + "'.";
            log.info(msg);
            log.finer("DataSource: " + fhirDb.toString());
            String tenantId = FHIRRequestContext.get().getTenantId();
            String dsId = FHIRRequestContext.get().getDataStoreId();
            log.finer("Obtaining connection for tenantId/dsId: " + tenantId + "/" + dsId);
            connection = fhirDb.getConnection(tenantId, dsId);
            log.finer("Connection: " + connection.toString());

            dbDriverName = connection.getMetaData().getDriverName();

            if (dbDriverName != null && dbDriverName.contains("Derby")) {
                final String adminSchemaName = "admin_" + tenantId + "_" + dsId;
                final String dataSchemaName = connection.getSchema();

                bootstrap(connection, adminSchemaName, dataSchemaName);
                connection.commit();
            }
        }
        catch (Throwable e) {
            String msg = "Encountered an exception while bootstrapping the FHIR database";
            log.log(Level.SEVERE, msg, e);
        }
        finally {
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
     * @param connection
     * @param adminSchemaName
     * @param dataSchemaName
     * @throws SQLException
     */
    public static void bootstrap(Connection connection, String adminSchemaName, String dataSchemaName) throws SQLException {
        JdbcTarget target = new JdbcTarget(connection);
        DerbyAdapter adapter = new DerbyAdapter(target);

        // Set up the version history service first if it doesn't yet exist
        CreateVersionHistory.createTableIfNeeded(adminSchemaName, adapter);

        // Current version history for the database. This is used by applyWithHistory
        // to determine which updates to apply and to record the new changes as they
        // are applied
        VersionHistoryService vhs = new VersionHistoryService(adminSchemaName, dataSchemaName);
        vhs.setTarget(adapter);
        vhs.init();

        // Define the schema and apply it (or required updates)
        FhirSchemaGenerator gen = new FhirSchemaGenerator(adminSchemaName, dataSchemaName);
        PhysicalDataModel pdm = new PhysicalDataModel();
        gen.buildSchema(pdm);

        // Use the new fhir-persistence-schema mechanism to create/update the derby database
        pdm.applyWithHistory(adapter, vhs);

    }
}
