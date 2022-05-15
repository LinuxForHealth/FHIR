/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.connection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;
import javax.transaction.TransactionSynchronizationRegistry;

import com.ibm.fhir.config.FHIRConfigHelper;
import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.config.PropertyGroup;
import com.ibm.fhir.database.utils.api.SchemaType;
import com.ibm.fhir.database.utils.model.DbType;
import com.ibm.fhir.persistence.exception.FHIRPersistenceDataAccessException;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.postgresql.SetPostgresOptimizerOptions;

/**
 * Common base for multi-tenant connection strategy implementations
 */
public abstract class FHIRDbConnectionStrategyBase implements FHIRDbConnectionStrategy, QueryHints {
    private static final Logger log = Logger.getLogger(FHIRDbConnectionStrategyBase.class.getName());
    // We use the sync registry to remember connections we've configured in the current transaction.
    private final TransactionSynchronizationRegistry trxSyncRegistry;

    // the action chain to be applied to new connections
    private final Action newConnectionAction;

    // Type and capability
    private final FHIRDbFlavor flavor;

    /**
     * Protected constructor
     * @param trxSyncRegistry the transaction sync registry
     * @param newConnectionAction actions to apply when a connection is created
     */
    protected FHIRDbConnectionStrategyBase(TransactionSynchronizationRegistry trxSyncRegistry, Action newConnectionAction) throws FHIRPersistenceDataAccessException {
        this.trxSyncRegistry = trxSyncRegistry;
        this.newConnectionAction = newConnectionAction;

        // initialize the flavor from the configuration
        this.flavor = createFlavor();
    }

    /**
     * Check with the transaction sync registry to see if this is the first time
     * we've worked with this connection in the current transaction.
     * @param connection the new connection
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
            newConnectionAction.performOn(this.flavor, connection);

            // and register the key so we don't do this again
            trxSyncRegistry.putResource(key, new Object());
        } else {
            if (log.isLoggable(Level.FINE)) {
                log.fine("Connection already configured. Key='" + key + "'");
            }
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
                String typeValue = dsPG.getStringProperty("type");

                SchemaType schemaType = SchemaType.PLAIN;
                DbType type = DbType.from(typeValue);
                if (type == DbType.DB2) {
                    // We make this absolute for now. May change in the future if we
                    // support a single-tenant schema in DB2.
                    schemaType = SchemaType.MULTITENANT;
                }

                result = new FHIRDbFlavorImpl(type, schemaType);
            } catch (Exception x) {
                log.log(Level.SEVERE, "No type property found for datastore '" + datastoreId + "'", x);
                throw new FHIRPersistenceDataAccessException("Datastore configuration issue. Details in server logs");
            }
        } else {
            log.log(Level.SEVERE, "Missing datastore configuration for '" + datastoreId + "'");
            throw new FHIRPersistenceDataAccessException("Datastore configuration issue. Details in server logs");
        }

        return result;
    }

    /**
     * Get a connection configured for the given tenant and datasourceId
     * @param datasource
     * @param tenantId
     * @param dsId
     * @return
     */
    protected Connection getConnection(DataSource datasource, String tenantId, String dsId) throws SQLException, FHIRPersistenceException {
        // Now use the dsId/tenantId specific JEE datasource to get a connection
        Connection connection = datasource.getConnection();

        try {
            // always
            connection.setAutoCommit(false);

            // configure the connection if it's the first time we've accessed it in this transaction
            configure(connection, tenantId, dsId);
        } catch (Throwable t) {
            // clean up if something goes wrong during configuration
            try {
                connection.close();
            } catch (Throwable x) {
                // NOP...something bad is going on anyway, so don't confuse things
                // by throwing a different exception and hiding the original
            } finally {
                // just to prevent future coding mistakes
                connection = null;
            }
            throw t;
        }

        return connection;
    }

    @Override
    public FHIRDbFlavor getFlavor() throws FHIRPersistenceDataAccessException {
        return this.flavor;
    }

    @Override
    public String getHintValue(String hintProperty) {
        String result;
        String datastoreId = FHIRRequestContext.get().getDataStoreId();

        // Retrieve the property group pertaining to the specified datastore.
        // Find and set the tenantKey for the request, otherwise subsequent pulls from the pool
        // miss the tenantKey.
        String hintPropertyName = FHIRConfiguration.PROPERTY_DATASOURCES + "/" + datastoreId + "/hints";
        PropertyGroup hintPG = FHIRConfigHelper.getPropertyGroup(hintPropertyName);
        if (hintPG != null) {
            try {
                result = hintPG.getStringProperty(hintProperty, null);
            } catch (Exception x) {
                log.log(Level.WARNING, "getting property '" + hintProperty + "' for datastoreId: '" + datastoreId + "'");
                result = null;
            }

        } else {
            result = null;
        }

        return result;
    }

    @Override
    public QueryHints getQueryHints() {
        return this;
    }

    @Override
    public void applySearchOptimizerOptions(Connection c, boolean isCompartment) {
        String datastoreId = FHIRRequestContext.get().getDataStoreId();

        switch (this.flavor.getType()) {
        case POSTGRESQL:
        case CITUS:
            // PostgreSQL needs optimizer options set to address search performance issues
            // as described in issue 1911
            final String pgName = FHIRConfiguration.PROPERTY_DATASOURCES + "/" + datastoreId + "/" + FHIRConfiguration.PROPERTY_JDBC_SEARCH_OPTIMIZER_OPTIONS;
            PropertyGroup optPG = FHIRConfigHelper.getPropertyGroup(pgName);
            SetPostgresOptimizerOptions cmd = new SetPostgresOptimizerOptions(optPG, isCompartment); // null optPG is OK
            cmd.applyTo(c);
            break;
        default:
            // NOP
        }
    }
}
