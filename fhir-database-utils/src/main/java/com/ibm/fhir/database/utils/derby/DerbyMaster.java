/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.derby;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
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
    private static final String DERBY_DIR = "derby/";

    // The derby properties file
    private static final String DERBY_PROPERTIES = DERBY_DIR + "derby.properties";

    // The translator to help us out with Derby syntax
    private static final IDatabaseTranslator DERBY_TRANSLATOR = new DerbyTranslator();

    // The name of the database we manage
    private final String database;

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

        try (PrintWriter out = new PrintWriter(DERBY_PROPERTIES)){
            Class.forName(DERBY_TRANSLATOR.getDriverClassName());
            // This speeds up sequence fetching by pre-creating 1000 instead of the default 100.
            out.println("derby.language.sequence.preallocator=1000");
            if (DEBUG) {
                out.println("derby.language.logQueryPlan=true");
                out.println("derby.language.logStatementText=true");
                out.println("derby.locks.deadlockTrace=true");
                out.println("derby.infolog.append=true");
            }
        }
        catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        } catch (FileNotFoundException e1) {
            logger.warning("Failed to create derby.properties file!");
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
        createSchema(new AllVersionHistoryService(), pdm);
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
     * @param fn
     */
    public void runWithAdapter(java.util.function.Consumer<IDatabaseAdapter> fn) {
        try {
            Connection c = getConnection();
            try {
                JdbcTarget target = new JdbcTarget(c);

                if (logger.isLoggable(Level.FINE)) {
                    // Decorate the target so that we print all the DDL before executing
                    PrintTarget printer = new PrintTarget(target, logger.isLoggable(Level.FINE));
                    DerbyAdapter adapter = new DerbyAdapter(printer);
                    fn.accept(adapter);
                }
                else {
                    // Keep the logs a little cleaner by just executing instead of logging all the DDL
                    DerbyAdapter adapter = new DerbyAdapter(target);
                    fn.accept(adapter);
                }
            }
            catch (DataAccessException x) {
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
