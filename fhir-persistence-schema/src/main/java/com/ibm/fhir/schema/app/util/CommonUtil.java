/*
 * (C) Copyright IBM Corp. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.app.util;

import java.io.File;
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
import com.ibm.fhir.database.utils.api.ISchemaAdapter;
import com.ibm.fhir.database.utils.api.SchemaType;
import com.ibm.fhir.database.utils.citus.CitusAdapter;
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
import com.ibm.fhir.schema.build.DistributedSchemaAdapter;
import com.ibm.fhir.schema.build.FhirSchemaAdapter;
import com.ibm.fhir.schema.build.ShardedSchemaAdapter;
import com.ibm.fhir.schema.control.FhirSchemaConstants;

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
        case CITUS:
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
        case MULTITENANT:
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
        case MULTITENANT:
            return new FhirSchemaAdapter(dbAdapter);
        case DISTRIBUTED:
            return new DistributedSchemaAdapter(dbAdapter, DEFAULT_DISTRIBUTION_COLUMN);
        case SHARDED:
            return new ShardedSchemaAdapter(dbAdapter, FhirSchemaConstants.SHARD_KEY);
        default:
            throw new IllegalArgumentException("Unsupported schema type: " + schemaType);
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
        case CITUS:
            return new CitusAdapter(connectionProvider);
        default:
            throw new IllegalStateException("Unsupported db type: " + dbType);
        }
    }
}