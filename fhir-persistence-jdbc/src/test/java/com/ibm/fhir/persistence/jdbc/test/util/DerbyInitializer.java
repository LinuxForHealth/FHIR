/*
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.test.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.derby.DerbyMaster;
import com.ibm.fhir.database.utils.derby.DerbyPropertyAdapter;
import com.ibm.fhir.database.utils.derby.DerbyTranslator;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;
import com.ibm.fhir.persistence.jdbc.util.DerbyBootstrapper;

/**
 * This utility class initializes and bootstraps a FHIR Derby database for unit testing. 
 * If an existing database is found in the target path, it is reused. If not, a new database is defined and the appropriate DDL for
 * the FHIR schema is applied.
 * 
 * It's intended that this class be consumed by testng tests in the fhir-persistence-jdbc project.
 * @author markd
 *
 */
public class DerbyInitializer {

    // All tests this same database, which we only have to bootstrap once
    private static final String DB_NAME = "derby/fhirDB";
    
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
     * @throws FHIRPersistenceDBConnectException
     * @throws SQLException
     */
    public void bootstrapDb() throws FHIRPersistenceDBConnectException, SQLException {
        bootstrapDb(false);
    }
    
    /**
     * Get the name of the schema holding all the FHIR normalized resource tables
     * @return
     */
    protected String getDataSchemaName() {
        return dbProps.getProperty("schemaName", "FHIRDATA");
    }
    /**
     * Establishes a connection to fhirDB. Creates the database if necessary complete with tables indexes.
     * @throws FHIRPersistenceDBConnectException
     * @throws SQLException 
     */
    public void bootstrapDb(boolean reset) throws FHIRPersistenceDBConnectException, SQLException {
        final String adminSchemaName = "FHIR_ADMIN";
        final String dataSchemaName = getDataSchemaName();
        
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
        }
        catch (SQLException x) {
            exists = false;
        }
 
        if (exists) {
            System.out.println("Existing database: skipping bootstrap");
        }
        else {
            System.out.println("Bootstrapping database");
            final String url = DERBY_TRANSLATOR.getUrl(dbProps);
            try (Connection connection = DriverManager.getConnection(url + ";create=true")) {
                connection.setAutoCommit(false);
                DerbyBootstrapper.bootstrap(connection, adminSchemaName, dataSchemaName);
            }
            catch (SQLException x) {
                throw DERBY_TRANSLATOR.translate(x);
            } 
        }
    }

    /**
     * Get a connection to an established database
     * Autocommit is disabled (of course)
     * @return
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(DERBY_TRANSLATOR.getUrl(dbProps));
        connection.setAutoCommit(false);
        return connection;
    }
    
}
