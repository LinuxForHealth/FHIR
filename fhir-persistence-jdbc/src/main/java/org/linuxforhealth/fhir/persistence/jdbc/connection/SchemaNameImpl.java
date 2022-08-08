/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.connection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;

/**
 * Supplier which tries to obtain the schema name from the given delegate,
 * falling back to calling getSchema() from the connection if the supplier
 * returns a null
 */
public class SchemaNameImpl implements SchemaNameSupplier { 
    private static final Logger logger = Logger.getLogger(SchemaNameImpl.class.getName());
    
    // the delegate we use ask for the schema name
    private final SchemaNameSupplier delegate;

    /**
     * Public constructor
     * @param delegate
     */
    public SchemaNameImpl(SchemaNameSupplier delegate) {
        this.delegate = delegate;
    }

    @Override
    public String getSchemaForRequestContext(Connection connection) throws FHIRPersistenceDBConnectException {
        String result = delegate.getSchemaForRequestContext(connection);
        if (result == null) {
            try {
                // fall back to getting the name of the schema from the connection
                // which is slower. We need this because "currentSchema" isn't
                // a supported property in the Derby datasource properties.
                if (result == null) {
                    // log a warning...the schema name will have to be obtained from the
                    // connection later...which is slower because it requires a DB round-trip
                    logger.warning("Calling Connection#getSchema() to obtain schema name. Put schema name in configuration for better performance");
                }
                
                result = connection.getSchema();
                
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("schema name from connection: " + result);
                }
            } catch (SQLException x) {
                throw new FHIRPersistenceDBConnectException("Unable to obtain schema name from connection", x);
            }
        }
        return result;
    }
}
