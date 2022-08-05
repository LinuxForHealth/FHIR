/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.derby;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.database.utils.api.AllVersionHistoryService;
import org.linuxforhealth.fhir.database.utils.api.DataAccessException;
import org.linuxforhealth.fhir.database.utils.api.IConnectionProvider;
import org.linuxforhealth.fhir.database.utils.api.IDatabaseAdapter;
import org.linuxforhealth.fhir.database.utils.api.IDatabaseTranslator;
import org.linuxforhealth.fhir.database.utils.api.ISchemaAdapter;
import org.linuxforhealth.fhir.database.utils.api.IVersionHistoryService;
import org.linuxforhealth.fhir.database.utils.api.SchemaApplyContext;
import org.linuxforhealth.fhir.database.utils.common.ConnectionProviderTarget;
import org.linuxforhealth.fhir.database.utils.common.DataDefinitionUtil;
import org.linuxforhealth.fhir.database.utils.common.JdbcTarget;
import org.linuxforhealth.fhir.database.utils.common.PlainSchemaAdapter;
import org.linuxforhealth.fhir.database.utils.common.PrintTarget;
import org.linuxforhealth.fhir.database.utils.model.PhysicalDataModel;

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
        try (Connection c = getConnection()) {
            DerbyServerPropertiesMgr.setServerProperties(DEBUG, c);
            c.commit();
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Derby Server Properties not set", e);
        }
    }
    
    /**
     * Derby setSchema fails if the schema doesn't exist, so we need to create that
     * now in order for our connections to succeed when we build out the FHIR database
     * @param schemaName
     */
    public void createSchemaIfNeeded(String schemaName) {
        DataDefinitionUtil.assertSecure(schemaName);
        boolean createSchema = false;
        try (Connection c = getConnection()) {
            try {
                c.setSchema(schemaName);
            } catch (SQLException x) {
                // schema doesn't exist, so we need to create it
                createSchema = true;
            }

            if (createSchema) {
                final String createSchemaDDL = "CREATE SCHEMA " + schemaName;
                JdbcTarget target = new JdbcTarget(c);
                target.runStatement(DERBY_TRANSLATOR, createSchemaDDL);
                
                try {
                    c.commit();
                } catch (SQLException x) {
                    throw DERBY_TRANSLATOR.translate(x);
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect to database: " + database);
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
        
        // Make sure the database is shut down before we try to drop it
        try {
            shutdown(database);
        } catch (IllegalStateException x) {
            // NOP - database doesn't exist anyway, so this is info not warning on purpose
            
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
     * @implNote creates a new connection each time. Should be wrapped in an IConnectionProvider
     *           implementation for use where a transaction might scope multiple open/close
     *           connections. This class returns the driver's connection. For proper transaction
     *           handling, the connection needs to be wrapped, which IConnectionProvider can
     *           take care of.
     * @return
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {
        logger.info("Opening connection to Derby database: " + database);
        try {
            // Make sure the Derby driver is loaded
            Properties properties = new Properties();
            DerbyPropertyAdapter adapter = new DerbyPropertyAdapter(properties);
            adapter.setDatabase(database);
            adapter.setAutoCreate(true);
            Connection connection = DriverManager.getConnection(DERBY_TRANSLATOR.getUrl(properties));
            connection.setAutoCommit(false);
            return connection;
        } catch (SQLException x) {
            throw DERBY_TRANSLATOR.translate(x);
        }
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
     * @param pool the connection pool
     * @param pdm the data model to create
     */
    public void createSchema(IConnectionProvider pool, PhysicalDataModel pdm) {
        createSchema(pool, vhs, pdm);
    }

    /**
     * Ask the schema to apply itself to our target (adapter pattern)
     * @param pool the connection pool
     * @param vhs current version history service
     * @param pdm the data model we want to create
     */
    public void createSchema(IConnectionProvider pool, IVersionHistoryService vhs, PhysicalDataModel pdm) {
        SchemaApplyContext context = SchemaApplyContext.getDefault();
        runWithAdapter(pool, target -> pdm.applyWithHistory(target, context, vhs));
    }

    /**
     * Run the {@link IDatabaseAdapter} command fn using a DerbyAdapter for the given connection pool
     * @param pool provides database connections
     * @param fn the command to execute
     */
    public void runWithAdapter(IConnectionProvider pool, Consumer<ISchemaAdapter> fn) {

        // We need to obtain connections from the same pool as the version history service
        // so we can avoid deadlocks for certain DDL like DROP INDEX
        try {
            // wrap the connection pool in an adapter for the Derby database
            DerbyAdapter adapter = new DerbyAdapter(pool);
            
            // call the Function we've been given using the adapter we just wrapped
            // around the connection.
            fn.accept(wrap(adapter));
        } catch (DataAccessException x) {
            logger.log(Level.SEVERE, "Error while running", x);
            throw x;
        }
    }


    /**
     * Run the function with an adapter configured for this database
     * 
     * @param fn
     */
    public void runWithAdapter(java.util.function.Consumer<ISchemaAdapter> fn) {

        IConnectionProvider cp = new DerbyConnectionProvider(this, null);
        ConnectionProviderTarget target = new ConnectionProviderTarget(cp);
        DerbyAdapter adapter = new DerbyAdapter(target);

        // Replace the target with a decorated output, so that we print all the DDL before executing
        // The output is very FINE and logs out a lot. 
        if (logger.isLoggable(Level.FINE)) {
            PrintTarget printer = new PrintTarget(target, logger.isLoggable(Level.FINE));
            adapter = new DerbyAdapter(printer);
        }
      
        // call the Function we've been given using the adapter we just wrapped
        // around the connection. Each statement executes in its own connection/transaction.
        // We also need to wrap the DerbyAdapter in a plain schema adapter
        fn.accept(wrap(adapter));
    }

    /**
     * Utility method to wrap the database adapter in a plain schema adapter 
     * which acts as a pass-through to the underlying databaseAdapter
     * @param databaseAdapter
     * @return
     */
    public static ISchemaAdapter wrap(IDatabaseAdapter databaseAdapter) {
        return new PlainSchemaAdapter(databaseAdapter);
    }

    /**
     * Diagnostic utility to display all the current locks in the Derby database
     */
    public void dumpLockInfo() {
        DerbyLockDiag diag = new DerbyLockDiag();
        IConnectionProvider cp = new DerbyConnectionProvider(this, null);
        ConnectionProviderTarget target = new ConnectionProviderTarget(cp);
        DerbyAdapter adapter = new DerbyAdapter(target);
        List<LockInfo> locks = adapter.runStatement(diag);
        System.out.println(LockInfo.header());
        locks.forEach(System.out::println);
    }
    
    /**
     * Dump locks using the given connection
     * @param c
     */
    public static void dumpLockInfo(Connection c) {
        // wrap the connection so that we can run our lock diag DAO
        JdbcTarget target = new JdbcTarget(c);
        DerbyAdapter adapter = new DerbyAdapter(target);
        
        DerbyLockDiag diag = new DerbyLockDiag();
        List<LockInfo> locks = adapter.runStatement(diag);
        
        // render
        System.out.println(LockInfo.header());
        locks.forEach(System.out::println);
    }
    
    /**
     * @implNote this drop is only relevant for in-memory Derby databases, which we no
     *           longer use due to the size of the tests. This does not remove any
     *           files on disk.
     */
    @Override
    public void close() throws Exception {
        shutdown(database);
    }
    
    public static void shutdown(String databaseName) {
        boolean shutdown = false;
        try {
            Properties properties = new Properties();
            DerbyPropertyAdapter adapter = new DerbyPropertyAdapter(properties);
            adapter.setDatabase(databaseName);

            final String dropper = DERBY_TRANSLATOR.getUrl(properties) + ";shutdown=true";
            logger.info("Shutting down Derby DB '" + databaseName + "' with: " + dropper);
            
            // should throw an exception if successful
            DriverManager.getConnection(dropper);
        } catch (SQLException e) {
            // should say "Database 'target/derby/...' shutdown."
            logger.info(e.getMessage());
            shutdown = true;
        }

        if (!shutdown) {
            throw new IllegalStateException("Derby database not shut down: '" + databaseName + "'");
        }
        
    }
}
