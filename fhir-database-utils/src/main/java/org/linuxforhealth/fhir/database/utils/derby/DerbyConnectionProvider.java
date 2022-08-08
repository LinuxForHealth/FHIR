/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.derby;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.database.utils.api.IConnectionProvider;
import org.linuxforhealth.fhir.database.utils.api.IDatabaseTranslator;

/**
 * An {@link IConnectionProvider} wrapper around DerbyMaster
 */
public class DerbyConnectionProvider implements IConnectionProvider {
    private static final Logger logger = Logger.getLogger(DerbyConnectionProvider.class.getName());

    // The wrapper for managing a derby in-memory instance
    private final DerbyMaster derby;
    
    // The schema name to set as current for each new connection
    private final String schemaName;

    /**
     * Wrap the derby database
     * @param derby the Derby database instance to wrap and provide connections for
     * @param the schema name to set as current on each connection, or null to not set the schema
     */
    public DerbyConnectionProvider(DerbyMaster derby, String schemaName) {
        this.derby = derby;
        this.schemaName = schemaName;
    }
    @Override
    public void commitTransaction() throws SQLException {
        // NOP
    }

    @Override
    public void describe(String arg0, StringBuilder arg1, String arg2) {
        // NOP
    }

    @Override
    public Connection getConnection() throws SQLException {
        Connection result = derby.getConnection();
        
        if (schemaName != null && schemaName.length() > 0) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("Setting current schema to '" + schemaName + "'");
            }
            result.setSchema(this.schemaName);
        }
        return result;
    }

    @Override
    public IDatabaseTranslator getTranslator() {
        return derby.getTranslator();
    }

    @Override
    public void rollbackTransaction() throws SQLException {
        // NOP
    }
}
