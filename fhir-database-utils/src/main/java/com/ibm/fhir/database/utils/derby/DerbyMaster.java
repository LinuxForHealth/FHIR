/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.derby;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.AllVersionHistoryService;
import com.ibm.fhir.database.utils.api.DataAccessException;
import com.ibm.fhir.database.utils.api.IDatabaseAdapter;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.api.IVersionHistoryService;
import com.ibm.fhir.database.utils.common.JdbcTarget;
import com.ibm.fhir.database.utils.common.PrintTarget;
import com.ibm.fhir.database.utils.model.PhysicalDataModel;

/**
 * Set up an instance of Derby for use with unit tests
 */
public class DerbyMaster implements AutoCloseable {
    private static final Logger logger = Logger.getLogger(DerbyMaster.class.getName());

    // The directory holding our derby databases
    private static final String DERBY_DIR = "target/derby/";

    // The translator to help us out with Derby syntax
    private static final IDatabaseTranslator DERBY_TRANSLATOR = new DerbyTranslator();

    // The name of the database we manage
    private final String database;

    // The Version History Service default in this case. 
    private AllVersionHistoryService vhs = new AllVersionHistoryService();

    // Controls if we run derby in debugging mode which enables more logs.
    private static final boolean DEBUG = false;

    private Connection connection;

    /**
     * Public constructor
     * @param database
     * @throws IllegalStateException if the Derby driver class is not found
     */
    public DerbyMaster(String database) {
        this.database = database;

        // Any JDBC 4.0 drivers that are found in class path are automatically loaded,
        // However, any driver prior to JDBC 4.0 has to be loaded with the method Class.forName.
        try {
            Class.forName(DERBY_TRANSLATOR.getDriverClassName());
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }

        // Derby Server Properties are now set using the System Stored procedures.
        try {
            DerbyServerPropertiesMgr.setServerProperties(DEBUG, getConnection());
        } catch (SQLException e) {
            logger.warning("Derby Server Properties not set");
        }
    }

    /**
     * Drop the contents of the database on disk. Must contain 'derby/' in the path
     * as a simple check against accidentally wiping the wrong files
     * @param database
     */
    public static void dropDatabase(String database) {
        if (!database.contains(DERBY_DIR)) {
            throw new IllegalArgumentException("Derby databases must start with: " + DERBY_DIR);
        }

        try {
            File dir = new File(database);
            if (dir.exists()) {
                delete(dir);
            }
        }
        catch (IOException e) {
            throw new IllegalStateException("Failed to delete derby DB: " + database, e);
        }
    }

    /**
     * Delete the derby database directory so that we can start with a clean instance
     * for testing
     * @param file
     * @throws IOException
     */
    private static void delete(File file) throws IOException {
        if (file.isDirectory()) {

            //directory is empty, then delete it
            if (file.list().length == 0) {
                file.delete();
            }
            else {
                // list all the directory contents
                String files[] = file.list();

                for (String temp : files) {
                    //construct the file structure
                    File fileDelete = new File(file, temp);

                    //recursive delete
                    delete(fileDelete);
                }

                // check the directory again, if empty then delete it
                if (file.list().length==0) {
                    file.delete();
                }
            }
        }
        else {
            // if file exists, then delete it
            file.delete();
        }
    }

    /**
     * Get a connection to the configured Derby database, creating the database if necessary.
     * @return
     * @throws SQLException
     * @implNote currently this returns the same connection each time, so don't close it!
     */
    public synchronized Connection getConnection() throws SQLException {
        logger.info("Opening connection to Derby database: " + database);
        if (connection == null) {
            try {
                // Make sure the Derby driver is loaded
                Properties properties = new Properties();
                DerbyPropertyAdapter adapter = new DerbyPropertyAdapter(properties);
                adapter.setDatabase(database);
                adapter.setAutoCreate(true);
                connection = DriverManager.getConnection(DERBY_TRANSLATOR.getUrl(properties));
                connection.setAutoCommit(false);
            }
            catch (SQLException x) {
                throw DERBY_TRANSLATOR.translate(x);
            }
        }
        return connection;
    }

    /**
     * Get the statement translator we use for Derby
     * @return
     */
    public IDatabaseTranslator getTranslator() {
        return DERBY_TRANSLATOR;
    }

    /**
     * Ask the schema to apply itself to our target (adapter pattern)
     * @param pdm
     */
    public void createSchema(PhysicalDataModel pdm) {
        createSchema(vhs, pdm);
    }

    /**
     * Ask the schema to apply itself to our target (adapter pattern)
     * @param vhs
     * @param pdm
     */
    public void createSchema(IVersionHistoryService vhs, PhysicalDataModel pdm) {
        runWithAdapter(target -> pdm.applyWithHistory(target, vhs));
    }

    /**
     * Run the function with an adapter configured for this database
     * 
     * @param fn
     */
    public void runWithAdapter(java.util.function.Consumer<IDatabaseAdapter> fn) {
        try {
            Connection c = getConnection();
            try {
                JdbcTarget target = new JdbcTarget(c);
                DerbyAdapter adapter = new DerbyAdapter(target);

                // Replace the target with a decorated output, so that we print all the DDL before executing
                // The output is very FINE and logs out a lot. 
                if (logger.isLoggable(Level.FINE)) {
                    PrintTarget printer = new PrintTarget(target, logger.isLoggable(Level.FINE));
                    adapter = new DerbyAdapter(printer);
                }
                fn.accept(adapter);
            } catch (DataAccessException x) {
                logger.log(Level.SEVERE, "Error while running", x);
                c.rollback();
                throw x;
            }
            c.commit();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error while running", e);
            throw DERBY_TRANSLATOR.translate(e);
        } finally {
            logger.info("connection was closed");
        }
    }

    @Override
    public void close() throws Exception {
        // Drop the database we created
        boolean dropped = false;
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
            Properties properties = new Properties();
            DerbyPropertyAdapter adapter = new DerbyPropertyAdapter(properties);
            adapter.setDatabase(database);

            final String dropper = DERBY_TRANSLATOR.getUrl(properties) + ";drop=true";
            logger.info("Dropping derby DB with: " + dropper);
            DriverManager.getConnection(dropper);
        }
        catch (SQLException e) {
            logger.info("Expected error while closing the database: " + e.getMessage());
            dropped = true;
        }

        if (!dropped) {
            throw new IllegalStateException("Derby memory database not dropped: " + this.database);
        }
    }
}
