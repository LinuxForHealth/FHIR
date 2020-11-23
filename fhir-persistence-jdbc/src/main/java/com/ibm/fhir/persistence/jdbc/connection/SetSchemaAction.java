/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.connection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;

/**
 * Command to set the named schema on a connection
 */
public class SetSchemaAction extends ChainedAction {
    private static final Logger log = Logger.getLogger(SetSchemaAction.class.getName());

    // supplier to obtain the schema name when we need it
    private final SchemaNameSupplier schemaNameSupplier;
    
    /**
     * Use a provided schema name (handy for testing)
     * @param schemaNameSupplier function to provide the schema name for the current request context
     * @param next the next action to apply, or null if this is the end of the chain
     */
    public SetSchemaAction(SchemaNameSupplier schemaNameSupplier, Action next) {
        super(next);
        this.schemaNameSupplier = schemaNameSupplier;
    }

    @Override
    public void performOn(FHIRDbFlavor flavor, Connection c) throws FHIRPersistenceDBConnectException {
        // this is being called the first time we've seen a connection for this
        // particular datastore. Find out which schema is configured, and make
        // sure it is set as the current schema.
        String schemaName = schemaNameSupplier.getSchemaForRequestContext(c);
        if (schemaName != null) {
            try {
                // See open-liberty issue: https://github.com/OpenLiberty/open-liberty/issues/12824
                log.severe("Calling setSchema(...) may break connection pool/transaction manager");
                c.setSchema(schemaName);
            } catch (SQLException x) {
                log.log(Level.SEVERE, "failed to set current schema '" + schemaName + "'");
                
                // schemaName is a secret, so don't emit in the exception to avoid propagating to client
                throw new FHIRPersistenceDBConnectException("Failed setting schema on connection");
            }
        } else {
            // rare
            log.fine("schemaName is null, so skipping setSchema() on connection");
        }
        
        // call the next action in the chain
        super.performOn(flavor, c);
    }
}
