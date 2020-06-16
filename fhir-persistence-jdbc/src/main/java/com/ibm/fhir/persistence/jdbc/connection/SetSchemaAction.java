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

import com.ibm.fhir.config.FHIRConfigHelper;
import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.config.PropertyGroup;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;


/**
 * Command to set the named schema on a connection
 */
public class SetSchemaAction extends ChainedAction {
    private static final Logger log = Logger.getLogger(SetSchemaAction.class.getName());
    
    private final String schemaName;
    /**
     * Public constructor
     */
    public SetSchemaAction() {
        this.schemaName = null;
    }

    /**
     * Use a provided schema name (handy for testing)
     * @param schemaName
     */
    public SetSchemaAction(String schemaName) {
        this.schemaName = schemaName;
    }

    /**
     * Public constructor
     * @param next action in a chain
     */
    public SetSchemaAction(Action next) {
        super(next);
        this.schemaName = null;
    }

    /* (non-Javadoc)
     * @see com.ibm.fhir.persistence.jdbc.connection.Action#performOn(java.sql.Connection)
     */
    @Override
    public void performOn(Connection c) throws FHIRPersistenceDBConnectException {
        // this is being called the first time we've seen a connection for this
        // particular datastore. Find out which schema is configured, and make
        // sure it is set as the current schema.
        String datastoreId = FHIRRequestContext.get().getDataStoreId();

        // Retrieve the property group pertaining to the specified datastore.
        String dsPropertyName = FHIRConfiguration.PROPERTY_DATASOURCES + "/" + datastoreId;
        PropertyGroup dsPG = FHIRConfigHelper.getPropertyGroup(dsPropertyName);
        if (dsPG != null) {
            String schemaName;
            
            try {
                // Schema name can be given for unit-tests. At runtime, we always use the schema name property from the configuration
                if (this.schemaName != null) {
                    schemaName = this.schemaName;
                }
                else {
                    schemaName = dsPG.getStringProperty("currentSchema", "FHIRDATA");
                }
            }
            catch (Exception x) {
                log.log(Level.SEVERE, "Datastore configuration issue for '" + datastoreId + "'", x);
                throw new FHIRPersistenceDBConnectException("Datastore configuration issue. Details in server logs");
            }
            
            if (log.isLoggable(Level.FINE)) {
                log.fine("Setting currentSchema for [" + datastoreId + "] to '" + schemaName + "'");
            }
            
            try {
                c.setSchema(schemaName);
            } catch (SQLException x) {
                log.log(Level.SEVERE, "failed to set current schema '" + schemaName + "' for datastore [" + datastoreId + "]");
                
                // schemaName is a secret, so don't emit in the exception to avoid propagating to client
                throw new FHIRPersistenceDBConnectException("Failed setting schema on connection");
            }
        } else {
            // rare, so no need to protect call
            log.fine("there are no datasource properties found for : [" + dsPropertyName + "]");
        }
        
        // call the next action in the chain
        super.performOn(c);
    }

}
