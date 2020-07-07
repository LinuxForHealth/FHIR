/*
 * (C) Copyright IBM Corp. 2017, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.test.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.ibm.fhir.database.utils.api.IConnectionProvider;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.common.JdbcConnectionProvider;
import com.ibm.fhir.database.utils.common.JdbcPropertyAdapter;
import com.ibm.fhir.database.utils.derby.DerbyMaster;
import com.ibm.fhir.database.utils.derby.DerbyPropertyAdapter;
import com.ibm.fhir.database.utils.derby.DerbyTranslator;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;
import com.ibm.fhir.schema.derby.DerbyFhirDatabase;

/**
 * This utility class initializes and bootstraps a FHIR Derby database for unit testing.
 * If an existing database is found in the target path, it is reused. If not, a new database
 * is defined and the appropriate DDL for the FHIR schema is applied.
 *
 * It's intended that this class be consumed by TestNG tests in the fhir-persistence-jdbc project.
 */
public class DerbyInitializer {
    // All tests use this same database, which we only have to bootstrap once
    public static final String DB_NAME = "target/derby/fhirDB";

    // The translator to help us out with Derby urls/syntax/exceptions
    private static final IDatabaseTranslator DERBY_TRANSLATOR = new DerbyTranslator();

    private Properties dbProps;

    /**
     * Main method to facilitate standalone testing of this class.
     * @param args
     */
    public static void main(String[] args) {
        DerbyInitializer initializer = new DerbyInitializer();
        try {
            initializer.bootstrapDb();
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructs a new DerbyInitializer using default database properties.
     */
    public DerbyInitializer() {
        super();
        this.dbProps = new Properties();
    }

    /**
     * Constructs a new DerbyInitializer using the passed database properties.
     */
    public DerbyInitializer(Properties props) {
        super();
        this.dbProps = props;
    }

    /**
     * Default bootstrap of the database. Does not drop/rebuild.
     *
     * @return a DerbyFhirDatabase instance that represents the create database if one was created or null if it already exists
     * @throws FHIRPersistenceDBConnectException
     * @throws SQLException
     */
    public DerbyFhirDatabase bootstrapDb() throws FHIRPersistenceDBConnectException, SQLException {
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
    public DerbyFhirDatabase bootstrapDb(boolean reset) throws FHIRPersistenceDBConnectException, SQLException {
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
        }
        else {
            System.out.println("Bootstrapping database");
            return new DerbyFhirDatabase(DB_NAME);
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
    public IConnectionProvider getConnectionProvider(boolean reset) throws FHIRPersistenceDBConnectException, SQLException {
        bootstrapDb(reset);
        JdbcPropertyAdapter propAdapter = new JdbcPropertyAdapter(this.dbProps);
        
        // make sure the schema name is correctly set in the properties
        String fhirDataSchema = getDataSchemaName();
        propAdapter.setDefaultSchema(fhirDataSchema);
        
        return new JdbcConnectionProvider(new DerbyTranslator(), propAdapter);
    }
}
