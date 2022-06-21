/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.remote.index;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import com.ibm.fhir.database.utils.api.IConnectionProvider;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.common.JdbcConnectionProvider;
import com.ibm.fhir.database.utils.common.JdbcPropertyAdapter;
import com.ibm.fhir.database.utils.derby.DerbyMaster;
import com.ibm.fhir.database.utils.derby.DerbyPropertyAdapter;
import com.ibm.fhir.database.utils.derby.DerbyTranslator;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.schema.derby.DerbyFhirDatabase;

/**
 * Initialize a FHIR database schema in Derby for use by the remote index tests
 */
public class DerbyFhirFactory {
    // All tests use this same database, which we only have to bootstrap once
    public static final String DB_NAME = "target/derby/fhirDB";

    // The translator to help us out with Derby urls/syntax/exceptions
    private static final IDatabaseTranslator DERBY_TRANSLATOR = new DerbyTranslator();

    private Properties dbProps;

    // When not null, restricts the set of resource types we create tables for
    private Set<String> resourceTypeNames;

    /**
     * Constructs a new DerbyInitializer using default database properties.
     */
    public DerbyFhirFactory(Set<String> resourceTypeNames) {
        this.dbProps = new Properties();
        if (resourceTypeNames != null) {
            this.resourceTypeNames = new HashSet<>(resourceTypeNames);
        } else {
            this.resourceTypeNames = null;
        }
    }

    /**
     * Constructs a new DerbyInitializer using the passed database properties.
     */
    public DerbyFhirFactory(Properties props, Set<String> resourceTypeNames) {
        this.dbProps = props;
        if (resourceTypeNames != null) {
            this.resourceTypeNames = new HashSet<>(resourceTypeNames);
        } else {
            this.resourceTypeNames = null;
        }
    }

    /**
     * Default bootstrap of the database. Does not drop/rebuild.
     *
     * @return a DerbyFhirDatabase instance that represents the create database if one was created or null if it already exists
     * @throws FHIRPersistenceDBConnectException
     * @throws SQLException
     */
    public DerbyFhirDatabase bootstrapDb() throws FHIRPersistenceException, SQLException {
        return bootstrapDb(false);
    }

    /**
     * Tests for the existence of fhirDB and creates the database if necessary, complete with tables and indices.
     *
     * @param reset
     *            Whether to "reset" the database by deleting the existing one before attempting the create
     * @return a DerbyFhirDatabase object if one was created or null if it already exists and {@code reset} is false
     * @throws FHIRPersistenceDBConnectException
     * @throws SQLException
     */
    public DerbyFhirDatabase bootstrapDb(boolean reset) throws FHIRPersistenceException, SQLException {
        if (reset) {
            // wipes the disk content of the database. Hopefully there aren't any
            // open connections at this point
            DerbyMaster.dropDatabase(DB_NAME);
        }

        // Inject the DB_NAME into the dbProps
        DerbyPropertyAdapter adapter = new DerbyPropertyAdapter(dbProps);
        adapter.setDatabase(DB_NAME);

        // Only bootstrap the database if it is new
        boolean exists;
        try (Connection connection = getConnection()) {
            exists = true;
        } catch (SQLException x) {
            exists = false;
        }

        if (exists) {
            System.out.println("Existing database: skipping bootstrap");
            return null;
        } else {
            System.out.println("Bootstrapping database");
            if (resourceTypeNames != null) {
                return new DerbyFhirDatabase(DB_NAME, resourceTypeNames);
            } else {
                return new DerbyFhirDatabase(DB_NAME);
            }
        }
    }

    /**
     * Get the name of the schema holding all the FHIR resource tables.
     */
    protected String getDataSchemaName() {
        return dbProps.getProperty("schemaName", "FHIRDATA");
    }

    /**
     * Get a connection to an established database.
     * Autocommit is disabled (of course).
     */
    public Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(DERBY_TRANSLATOR.getUrl(dbProps));
        connection.setAutoCommit(false);
        return connection;
    }

    /**
     * Bootstrap the database if necessary, and get a connection provider for it
     * @return an {@link IConnectionProvider} configured for the FHIR Derby database
     * @param reset resets the database if true
     * @throws SQLException 
     * @throws FHIRPersistenceDBConnectException 
     */
    public IConnectionProvider getConnectionProvider(boolean reset) throws FHIRPersistenceException, SQLException {
        bootstrapDb(reset);
        JdbcPropertyAdapter propAdapter = new JdbcPropertyAdapter(this.dbProps);
        
        // make sure the schema name is correctly set in the properties
        String fhirDataSchema = getDataSchemaName();
        propAdapter.setDefaultSchema(fhirDataSchema);
        
        return new JdbcConnectionProvider(new DerbyTranslator(), propAdapter);
    }
}
