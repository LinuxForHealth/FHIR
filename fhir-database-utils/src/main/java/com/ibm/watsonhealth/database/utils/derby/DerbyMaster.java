/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.database.utils.derby;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

import com.ibm.watsonhealth.database.utils.api.AllVersionHistoryService;
import com.ibm.watsonhealth.database.utils.api.DataAccessException;
import com.ibm.watsonhealth.database.utils.api.IDatabaseAdapter;
import com.ibm.watsonhealth.database.utils.api.IDatabaseTranslator;
import com.ibm.watsonhealth.database.utils.api.IVersionHistoryService;
import com.ibm.watsonhealth.database.utils.common.JdbcTarget;
import com.ibm.watsonhealth.database.utils.common.PrintTarget;
import com.ibm.watsonhealth.database.utils.model.PhysicalDataModel;


/**
 * Set up an in-memory instance of Derby for use
 * with unit tests
 * @author rarnold
 */
public class DerbyMaster implements AutoCloseable {
    private static final Logger logger = Logger.getLogger(DerbyMaster.class.getName());

    // The translator to help us out with Derby syntax
    private static final IDatabaseTranslator DERBY_TRANSLATOR = new DerbyTranslator();
    
    // The name of the in-memory database we manage
    private final String database;    

    /**
     * Public constructor
     * @param database
     * @throws IllegalStateException if the Derby driver class is not found
     */
    public DerbyMaster(String database) {
        this.database = database;
        try {
            Class.forName(DERBY_TRANSLATOR.getDriverClassName());
        }
        catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }
    
    /**
     * Create a connection to the in-memory Derby database, creating the
     * database if necessary
     * @return
     * @throws SQLException
     */
    public Connection createConnection() throws SQLException {
        logger.info("Opening connection to Derby database: " + database);
        Connection connection;
        try {   
            // Make sure the Derby driver is loaded
                Properties properties = new Properties();
                DerbyPropertyAdapter adapter = new DerbyPropertyAdapter(properties);
                adapter.setDatabase(database);
                connection = DriverManager.getConnection(DERBY_TRANSLATOR.getUrl(properties) + ";create=true");
                connection.setAutoCommit(false);
        }
        catch (SQLException x) {
            throw DERBY_TRANSLATOR.translate(x);
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
     * @param s
     */
    public void createSchema(PhysicalDataModel pdm) {
        final IVersionHistoryService vhs = new AllVersionHistoryService();
        runWithAdapter(target -> pdm.applyWithHistory(target, vhs));
    }

    /**
     * Run the function with an adapter configured for this database
     * @param fn
     */
    public void runWithAdapter(java.util.function.Consumer<IDatabaseAdapter> fn) {
        try {
            try (Connection c = createConnection()) {
                try {
                    JdbcTarget target = new JdbcTarget(c);
                    
                    // Decorate the target so that we print all the DDL before executing
                    PrintTarget printer = new PrintTarget(target);
                    DerbyAdapter adapter = new DerbyAdapter(printer);
                    fn.accept(adapter);
                }
                catch (DataAccessException x) {
                    c.rollback();
                    throw x;
                }
                c.commit();
            }
        }
        catch (SQLException x) {
            throw DERBY_TRANSLATOR.translate(x);
        }
        
    }

    /* (non-Javadoc)
     * @see java.lang.AutoCloseable#close()
     */
    @Override
    public void close() throws Exception {
        // Drop the database we created
        boolean dropped = false;
        try {
            Properties properties = new Properties();
            DerbyPropertyAdapter adapter = new DerbyPropertyAdapter(properties);
            adapter.setDatabase(database);

            final String dropper = DERBY_TRANSLATOR.getUrl(properties) + ";drop=true";
            logger.info("Dropping derby memory DB with: " + dropper);
            DriverManager.getConnection(dropper);
        }
        catch (SQLException x) {
                dropped = true;
        }

        if (!dropped) {
            throw new IllegalStateException("Derby memory database not dropped: " + this.database);
        }
    }
}
