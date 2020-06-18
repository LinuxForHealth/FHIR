/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.connection;

import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.transaction.TransactionSynchronizationRegistry;
import javax.transaction.UserTransaction;

import com.ibm.fhir.config.FHIRConfigHelper;
import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.config.PropertyGroup;
import com.ibm.fhir.database.utils.api.DatabaseType;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDataAccessException;


/**
 * Common base for multi-tenant connection strategy implementations
 */
public abstract class FHIRDbConnectionStrategyBase implements FHIRDbConnectionStrategy {
    private static final Logger log = Logger.getLogger(FHIRDbConnectionStrategyBase.class.getName());
    // We use the sync registry to remember connections we've configured in the current transaction.
    private final TransactionSynchronizationRegistry trxSyncRegistry;

    // the action chain to be applied to new connections
    private final Action newConnectionAction;
    
    // The transaction handler
    private final UserTransaction userTransaction;
    
    private boolean rollbackOnly;
    
    // Type and capability 
    private final FHIRDbFlavor flavor;

    /**
     * Protected constructor
     * @param userTx the transaction handler
     * @param trxSyncRegistry
     * @param newConnectionAction
     */
    protected FHIRDbConnectionStrategyBase(UserTransaction userTx, TransactionSynchronizationRegistry trxSyncRegistry, Action newConnectionAction) throws FHIRPersistenceDataAccessException {
        this.userTransaction = userTx;
        this.trxSyncRegistry = trxSyncRegistry;
        this.newConnectionAction = newConnectionAction;
        
        // initialize the flavor from the configuration
        this.flavor = createFlavor();
    }

    /**
     * Check with the transaction sync registry to see if this is the first time
     * we've worked with this connection in the current transaction.
     * @param c the new connection
     * @param tenantId the tenant to which the connection belongs
     * @param dsId the datasource in the tenant to which the connection belongs
     */
    protected void configure(Connection connection, String tenantId, String dsId) throws FHIRPersistenceException {
        // We prefix the  key with the name of this class to avoid any potential conflict with other
        // users of the sync registry.        
        final String key = this.getClass().getName() + "/" + tenantId + "/" + dsId;
        if (trxSyncRegistry.getResource(key) == null) {
            if (log.isLoggable(Level.FINE)) {
                log.fine("Configuring new connection in this transaction. Key='" + key + "'");
            }
            
            // first time...so we need to apply actions. Will be cleared when the transaction commits
            newConnectionAction.performOn(connection);
            
            // and register the key so we don't do this again
            trxSyncRegistry.putResource(key, new Object());
        } else {
            if (log.isLoggable(Level.FINE)) {
                log.fine("Connection already configured. Key='" + key + "'");
            }
        }
    }
    
    /* (non-Javadoc)
     * @see com.ibm.fhir.persistence.jdbc.connection.FHIRDbConnectionStrategy#txBegin()
     */
    @Override
    public void txBegin() throws FHIRPersistenceException {
        try {
            this.rollbackOnly = false;
            userTransaction.begin();
        } catch (Throwable e) {
            String errorMessage = "Unexpected error while rolling a transaction.";
            FHIRPersistenceException fx = new FHIRPersistenceException(errorMessage);
            log.log(Level.SEVERE, fx.getMessage(), e);
            throw fx;
        }

    }


    /* (non-Javadoc)
     * @see com.ibm.fhir.persistence.jdbc.connection.FHIRDbConnectionStrategy#commit()
     */
    @Override
    public void txEnd() throws FHIRPersistenceException {
        try {
            if (this.rollbackOnly) {
                userTransaction.rollback();
            } else {
                userTransaction.commit();
            }
        } catch (Throwable e) {
            FHIRPersistenceException fx = new FHIRPersistenceException("Unexpected error while committing a transaction.");
            log.log(Level.SEVERE, fx.getMessage(), e);
            throw fx;
        }
    }

    /* (non-Javadoc)
     * @see com.ibm.fhir.persistence.jdbc.connection.FHIRDbConnectionStrategy#setRollbackOnly()
     */
    @Override
    public void txSetRollbackOnly() throws FHIRPersistenceException {

        try {
            this.rollbackOnly = true;
            userTransaction.setRollbackOnly();
        } catch (Throwable e) {
            String errorMessage = "Unexpected error while rolling a transaction.";
            FHIRPersistenceException fx = new FHIRPersistenceException(errorMessage);
            log.log(Level.SEVERE, fx.getMessage(), e);
            throw fx;
        }
        
    }
    
    /**
     * Identify the flavor of the database using information from the
     * datasource configuration.
     * @return
     * @throws FHIRPersistenceException
     */
    private FHIRDbFlavor createFlavor() throws FHIRPersistenceDataAccessException {
        FHIRDbFlavor result;
        
        String datastoreId = FHIRRequestContext.get().getDataStoreId();

        // Retrieve the property group pertaining to the specified datastore.
        // Find and set the tenantKey for the request, otherwise subsequent pulls from the pool
        // miss the tenantKey.
        String dsPropertyName = FHIRConfiguration.PROPERTY_DATASOURCES + "/" + datastoreId;
        PropertyGroup dsPG = FHIRConfigHelper.getPropertyGroup(dsPropertyName);
        if (dsPG != null) {
            
            try {
                boolean multitenant = false;
                String typeValue = dsPG.getStringProperty("type");
                
                DatabaseType type = DatabaseType.valueOf(typeValue);
                if (type == DatabaseType.DB2) {
                    // We make this absolute for now. May change in the future if we
                    // support a single-tenant schema in DB2.
                    multitenant = true;
                }
                
                result = new FHIRDbFlavorImpl(type, multitenant);
            }
            catch (Exception x) {
                log.log(Level.SEVERE, "No type property found for datastore '" + datastoreId + "'", x);
                throw new FHIRPersistenceDataAccessException("Datastore configuration issue. Details in server logs");
            }
        } else {
            log.log(Level.SEVERE, "Missing datastore configuration for '" + datastoreId + "'");
            throw new FHIRPersistenceDataAccessException("Datastore configuration issue. Details in server logs");
        }
        
        return result;
    }

    /* (non-Javadoc)
     * @see com.ibm.fhir.persistence.jdbc.connection.FHIRDbConnectionStrategy#getFlavor()
     */
    @Override
    public FHIRDbFlavor getFlavor() throws FHIRPersistenceDataAccessException {
        return this.flavor;
    }

}
