/*
 * (C) Copyright IBM Corp. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.schema.app.util;

import java.io.File;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.database.utils.api.IConnectionProvider;
import org.linuxforhealth.fhir.database.utils.api.IDatabaseAdapter;
import org.linuxforhealth.fhir.database.utils.api.IDatabaseTranslator;
import org.linuxforhealth.fhir.database.utils.api.ISchemaAdapter;
import org.linuxforhealth.fhir.database.utils.api.SchemaType;
import org.linuxforhealth.fhir.database.utils.citus.CitusAdapter;
import org.linuxforhealth.fhir.database.utils.common.JdbcPropertyAdapter;
import org.linuxforhealth.fhir.database.utils.common.JdbcTarget;
import org.linuxforhealth.fhir.database.utils.common.LogFormatter;
import org.linuxforhealth.fhir.database.utils.derby.DerbyAdapter;
import org.linuxforhealth.fhir.database.utils.derby.DerbyPropertyAdapter;
import org.linuxforhealth.fhir.database.utils.model.DbType;
import org.linuxforhealth.fhir.database.utils.oracle.OracleAdapter;
import org.linuxforhealth.fhir.database.utils.oracle.OraclePropertyAdapter;
import org.linuxforhealth.fhir.database.utils.postgres.PostgresAdapter;
import org.linuxforhealth.fhir.database.utils.postgres.PostgresPropertyAdapter;
import org.linuxforhealth.fhir.schema.build.DistributedSchemaAdapter;
import org.linuxforhealth.fhir.schema.build.FhirSchemaAdapter;
import org.linuxforhealth.fhir.schema.build.ShardedSchemaAdapter;
import org.linuxforhealth.fhir.schema.control.FhirSchemaConstants;

/**
 *
 */
public final class CommonUtil {
    // Random generator for new tenant keys and salts
    private static final SecureRandom random = new SecureRandom();
    private static final String DEFAULT_DISTRIBUTION_COLUMN = "LOGICAL_RESOURCE_ID";

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
            String classPath = System.getProperty("java.class.path");
            logger.fine(classPath);            
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
        case DERBY:
            return new DerbyPropertyAdapter(props);
        case ORACLE:
            return new OraclePropertyAdapter(props);
        case POSTGRESQL:
        case CITUS:
            return new PostgresPropertyAdapter(props);
        default:
            throw new IllegalStateException("Unsupported db type: " + dbType);
        }
    }

    /**
     * Factory method to create an {@link IDatabaseAdapter} implementation based on the
     * given dbType.
     * @param dbType
     * @param target
     * @return
     * @throws IllegalStateException if the dbType is unsupported
     */
    public static IDatabaseAdapter getDbAdapter(DbType dbType, JdbcTarget target) {
        switch (dbType) {
        case DERBY:
            return new DerbyAdapter(target);
        case ORACLE:
            return new OracleAdapter(target);
        case POSTGRESQL:
            return new PostgresAdapter(target);
        case CITUS:
            return new CitusAdapter(target);
        default:
            throw new IllegalStateException("Unsupported db type: " + dbType);
        }
    }

    /**
     * Get the schema adapter which will build the schema variant described by
     * the given schemaType
     * @param schemaType
     * @param dbType
     * @param connectionProvider
     * @return
     */
    public static ISchemaAdapter getSchemaAdapter(SchemaType schemaType, DbType dbType, IConnectionProvider connectionProvider) {
        IDatabaseAdapter dbAdapter = getDbAdapter(dbType, connectionProvider);
        switch (schemaType) {
        case PLAIN:
            return new FhirSchemaAdapter(dbAdapter);
        case DISTRIBUTED:
            return new DistributedSchemaAdapter(dbAdapter, DEFAULT_DISTRIBUTION_COLUMN);
        case SHARDED:
            return new ShardedSchemaAdapter(dbAdapter, FhirSchemaConstants.SHARD_KEY);
        default:
            throw new IllegalArgumentException("Unsupported schema type: " + schemaType);
        }
    }

    /**
     * Wrap the given databaseAdapter in an ISchemaAdapter implementation selected
     * by the given schemaType
     * @param schemaType
     * @param dbAdapter
     * @return
     */
    public static ISchemaAdapter getSchemaAdapter(SchemaType schemaType, IDatabaseAdapter dbAdapter) {
        switch (schemaType) {
        case PLAIN:
            return new FhirSchemaAdapter(dbAdapter);
        case DISTRIBUTED:
            return new DistributedSchemaAdapter(dbAdapter, DEFAULT_DISTRIBUTION_COLUMN);
        case SHARDED:
            return new ShardedSchemaAdapter(dbAdapter, FhirSchemaConstants.SHARD_KEY);
        default:
            throw new IllegalArgumentException("Unsupported schema type: " + schemaType);
        }
    }

    /**
     * Factory method to create an {@link IDatabaseAdapter} implementation based on the
     * given dbType.
     * @param dbType
     * @param connectionProvider
     * @return
     * @throws IllegalStateException if the dbType is unsupported
     */
    public static IDatabaseAdapter getDbAdapter(DbType dbType, IConnectionProvider connectionProvider) {
        switch (dbType) {
        case DERBY:
            return new DerbyAdapter(connectionProvider);
        case ORACLE:
            return new OracleAdapter(connectionProvider);
        case POSTGRESQL:
            return new PostgresAdapter(connectionProvider);
        case CITUS:
            return new CitusAdapter(connectionProvider);
        default:
            throw new IllegalStateException("Unsupported db type: " + dbType);
        }
    }
}