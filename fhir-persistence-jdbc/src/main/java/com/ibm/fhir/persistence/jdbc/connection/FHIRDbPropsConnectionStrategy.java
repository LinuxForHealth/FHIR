/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDataAccessException;


/**
 * Hides the logic behind obtaining a JDBC {@link Connection} from the DAO code.
 * 
 * This class implements the old way to obtain DB connections using a {@link Properties} instance
 */
@Deprecated
public class FHIRDbPropsConnectionStrategy implements FHIRDbConnectionStrategy {
    private static final Logger log = Logger.getLogger(FHIRDbPropsConnectionStrategy.class.getName());
    private static final String CLASSNAME = "FHIRDbPropsConnectionStrategy";

    // Contains the connection properties
    private final Properties dbProps;
    
    /**
     * Public constructor
     * @param cp
     */
    public FHIRDbPropsConnectionStrategy(Properties dbProps) throws FHIRPersistenceDBConnectException {
        this.dbProps = dbProps;

        // ensure the driver is loaded
        String dbDriverName = this.dbProps.getProperty(FHIRDbConstants.PROPERTY_DB_DRIVER);
        try {
            Class.forName(dbDriverName);
        } catch (ClassNotFoundException e) {
            // Not concerned about revealing a classname in the exception
            throw new FHIRPersistenceDBConnectException("Failed to load driver: " + dbDriverName, e);
        }

    }

    @Override
    public Connection getConnection() throws FHIRPersistenceDBConnectException {
        // TODO we need to wrap the connection to simplify transaction handling
        final String METHODNAME = "getConnection()";
        
        Connection connection = null;
        String dbUrl;
    
        dbUrl = this.dbProps.getProperty(FHIRDbConstants.PROPERTY_DB_URL);
        try {
            connection = DriverManager.getConnection(dbUrl, this.dbProps);
    
            // Most queries assume the current schema is set up properly
            String schemaName = dbProps.getProperty(FHIRDbConstants.PROPERTY_SCHEMA_NAME, "FHIRDATA");
            connection.setSchema(schemaName);
            
            return connection;
        } catch (Throwable e) {
            // Don't emit secrets like the dbUrl in case they are returned to a client
            FHIRPersistenceDBConnectException fx =
                    new FHIRPersistenceDBConnectException("Failed to acquire DB connection");
            throw FHIRDbHelper.severe(log, fx, "Failed to acquire DB connection. dbUrl=" + dbUrl, e);
        } finally {
            if (log.isLoggable(Level.FINEST)) {
                log.exiting(CLASSNAME, METHODNAME);
            }
        }
    }

    @Override
    public FHIRDbFlavor getFlavor() throws FHIRPersistenceDataAccessException {
        // TODO Auto-generated method stub
        return null;
    }

}
