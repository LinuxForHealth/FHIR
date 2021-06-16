/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.app.util;

import java.io.File;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.IConnectionProvider;
import com.ibm.fhir.database.utils.api.IDatabaseAdapter;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.common.JdbcPropertyAdapter;
import com.ibm.fhir.database.utils.common.JdbcTarget;
import com.ibm.fhir.database.utils.common.LogFormatter;
import com.ibm.fhir.database.utils.db2.Db2Adapter;
import com.ibm.fhir.database.utils.db2.Db2PropertyAdapter;
import com.ibm.fhir.database.utils.derby.DerbyAdapter;
import com.ibm.fhir.database.utils.derby.DerbyPropertyAdapter;
import com.ibm.fhir.database.utils.model.DbType;
import com.ibm.fhir.database.utils.postgres.PostgresAdapter;
import com.ibm.fhir.database.utils.postgres.PostgresPropertyAdapter;

/**
 *
 */
public final class CommonUtil {
    // Random generator for new tenant keys and salts
    private static final SecureRandom random = new SecureRandom();

    /***
     * prints a brief menu to the standard out showing the usage.
     */
    public static void printUsage() {
        PrintStream ps = System.err;
        ps.println("Usage: ");

        // Properties File
        ps.println("--prop-file path-to-property-file");
        ps.println(" * loads the properties from a file");

        // Schema Name
        ps.println("--schema-name schema-name");
        ps.println(" * uses the schema as specified, must be valid.");

        // Grant Permissions to a valid username
        ps.println("--grant-to username");
        ps.println(" * uses the user as specified, must be valid.");
        ps.println(" * and grants permission to the username");

        // Add Tenant Key
        ps.println("--add-tenant-key tenant-key");
        ps.println(" * adds a tenant-key");

        // Updates the Sotred Procedure for a Tenant
        ps.println("--update-proc");
        ps.println(" * updates the stored procedure for a specific tenant");

        // Checks feature compatiblility
        ps.println("--check-compatibility");
        ps.println(" * checks feature compatibility ");

        // Drop the Admin Schema
        ps.println("--drop-admin");
        ps.println(" * drops the admin schema ");

        // Test Tenant
        ps.println("--test-tenant tenantName");
        ps.println(" * used to test with tenantName");

        // Tenant Key
        ps.println("--tenant-key tenantKey");
        ps.println(" * uses the tenant-key in the queries");

        // Tenant Key File
        ps.println("--tenant-key-file tenant-key-file-location");
        ps.println(" * sets the tenant key file location");

        // Update Schema action
        ps.println("--update-schema");
        ps.println(" * deploy or update the schema set by '--schema-name'");

        // Create Schema action
        ps.println("--create-schemas");
        ps.println(" * create the database schemas for batch, oauth, and the fhir schema set by '--schema-name'");

        // Drop Schema action
        ps.println("--drop-schema-fhir");
        ps.println(" * drop the schema set by '--schema-name'");

        // Drop Schema action
        ps.println("--drop-schema-batch");
        ps.println(" * drop the db schema used by liberty's java-batch feature");

        // Drop Schema action
        ps.println("--drop-schema-oauth");
        ps.println(" * drop the db schema used by liberty's oauth/openid connect features");

        // Uses a specified poolsize
        ps.println("--pool-size poolSize");
        ps.println(" * poolsize used with the database actions ");

        // Property used to connect
        ps.println("--prop name=value");
        ps.println(" * name=value that is passed in on the commandline  ");

        // Confirms dropping of the schema
        ps.println("--confirm-drop");
        ps.println(" * confirms the dropping of a schema");

        // Allocates Tenant
        ps.println("--allocate-tenant");
        ps.println(" * allocates a tenant");

        // Drops a Tenant
        ps.println("--drop-tenant tenantName");
        ps.println(" * (phase 1) drops the tenant given the tenantName");

        // Drops detached partition tables
        ps.println("--drop-detached tenantName");
        ps.println(" * (phase 2) drops the detached tenant partition tables given the tenantName");

        // Deletes tenant meta data
        ps.println("--delete-tenant-meta tenantName");
        ps.println(" * deletes tenant metadata given the tenantName");

        ps.println("--list-tenants");
        ps.println(" * fetches list of tenants and current status");

        ps.println("--revoke-all-tenant-keys");
        ps.println(" * revokes the all of the keys for the specified tenant");

        ps.println("--revoke-tenant-key");
        ps.println(" * revokes the key for the specified tenant and tenant key");

        // Dry Run functionality
        ps.println("--dry-run ");
        ps.println(" * simulates the actions of the actions that change the datastore");
    }

    /**
     * Set up the logger using the log.dir system property
     */
    public static void configureLogger() {
        final String logDirectory = System.getProperty("log.dir");
        if (logDirectory == null || logDirectory.isEmpty()) {
            configureLogger(".");
        } else {
            configureLogger(logDirectory);
        }
    }

    /**
     * Print the classpath so we can see what on earth is going on with connecting
     * to DB2 using an api key.
     */
    public static void logClasspath(Logger logger) {
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("CLASSPATH: ");
            ClassLoader cl = ClassLoader.getSystemClassLoader();
            URL[] classpath = ((URLClassLoader) cl).getURLs();
            for (URL u : classpath) {
                logger.fine("  " + u.getFile());
            }
        }
    }

    /**
     * Configure the logger to use the given directory.
     *
     * @param logDir
     */
    public static void configureLogger(final String logDir) {
        File f = new File(logDir, "fhirschema.log");
        LogFormatter.init(f.getPath());
    }

    /**
     * Generate a random 32 byte value encoded as a Base64 string (44 characters).
     *
     * @return
     */
    public static String getRandomKey() {
        byte[] buffer = new byte[32];
        random.nextBytes(buffer);
        Encoder enc = Base64.getEncoder();
        return enc.encodeToString(buffer);
    }

    /**
     * Load the driver class
     */
    public static void loadDriver(IDatabaseTranslator translator) {
        try {
            Class.forName(translator.getDriverClassName());
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }


    public static JdbcPropertyAdapter getPropertyAdapter(DbType dbType, Properties props) {
        switch (dbType) {
        case DB2:
            return new Db2PropertyAdapter(props);
        case DERBY:
            return new DerbyPropertyAdapter(props);
        case POSTGRESQL:
            return new PostgresPropertyAdapter(props);
        default:
            throw new IllegalStateException("Unsupported db type: " + dbType);
        }
    }

    public static IDatabaseAdapter getDbAdapter(DbType dbType, JdbcTarget target) {
        switch (dbType) {
        case DB2:
            return new Db2Adapter(target);
        case DERBY:
            return new DerbyAdapter(target);
        case POSTGRESQL:
            return new PostgresAdapter(target);
        default:
            throw new IllegalStateException("Unsupported db type: " + dbType);
        }
    }

    public static IDatabaseAdapter getDbAdapter(DbType dbType, IConnectionProvider connectionProvider) {
        switch (dbType) {
        case DB2:
            return new Db2Adapter(connectionProvider);
        case DERBY:
            return new DerbyAdapter(connectionProvider);
        case POSTGRESQL:
            return new PostgresAdapter(connectionProvider);
        default:
            throw new IllegalStateException("Unsupported db type: " + dbType);
        }
    }
}