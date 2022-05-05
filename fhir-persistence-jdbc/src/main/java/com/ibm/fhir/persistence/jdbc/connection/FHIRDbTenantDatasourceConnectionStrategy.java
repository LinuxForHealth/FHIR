/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.connection;

import java.sql.Connection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.transaction.TransactionSynchronizationRegistry;

import com.ibm.fhir.config.FHIRConfigHelper;
import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.config.PropertyGroup;
import com.ibm.fhir.database.utils.api.SchemaType;
import com.ibm.fhir.database.utils.model.DbType;
import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.dao.impl.FHIRDbDAOImpl;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDataAccessException;


/**
 * Hides the logic behind obtaining a JDBC {@link Connection} from the DAO code.
 *
 * Uses datasource and tenant ids configured in the fhir-server-config to
 * map directly to a managed datasource. All managed datasources must be
 * available when the server starts. This differs from the proxy
 * datasource strategy {@link FHIRDbPropsConnectionStrategy} which supports
 * dynamic (programmatic) definition of managed datasources.
 *
 * @implNote Refactored from {@link FHIRDbDAOImpl}. Improves separation of
 *           concerns by removing connection management code from the DAO
 *           and injecting it as a strategy instead. This not only simplifies
 *           things, but also makes it easier to implement new strategies,
 *           such as using a JEE datasource directly instead of the FHIR
 *           proxy datasource used here.
 *           Currently not used - just needs some additional integration
 *           with the fhir-server-configuration to be supported.
 */
public class FHIRDbTenantDatasourceConnectionStrategy extends FHIRDbConnectionStrategyBase {
    private static final Logger log = Logger.getLogger(FHIRDbDAOImpl.class.getName());
    private static final String CLASSNAME = FHIRDbDAOImpl.class.getName();

    // number of nanoseconds in a millisecond
    private static final long NANOMS = 1000000;

    // JNDI prefix  of the (proxy) datasource
    private static final String DATASOURCE_BASE_NAME = "jdbc/fhir";

    // Cache of datasources we've found
    private final Map<String, DataSource> datasourceMap = new ConcurrentHashMap<>();

    // the flavor of the database we are configured to represent
    private final FHIRDbFlavor flavor;

    // Should we use read-only datasources for isReadOnly() requests?
    private final boolean enableReadOnlyReplicas;

    /**
     * Public constructor. The proxy datasource must be present (registered in JNDI)
     * at server startup.
     * @throws FHIRPersistenceDBConnectException if the proxy datasource is not configured
     */
    public FHIRDbTenantDatasourceConnectionStrategy(TransactionSynchronizationRegistry trxSyncRegistry, Action newConnectionAction, boolean enableReadOnlyReplicas) throws FHIRException {
        super(trxSyncRegistry, newConnectionAction);
        this.flavor = createFlavor();
        this.enableReadOnlyReplicas = enableReadOnlyReplicas;
    }

    public static String makeTenantDatasourceJNDIName(String jndiBase, String tenantId, String dsId, boolean readOnly) {
        final StringBuilder builder = new StringBuilder();

        builder.append(jndiBase);
        builder.append("_");
        builder.append(tenantId);
        builder.append("_");
        builder.append(dsId);

        if (readOnly) {
            // Allow the query to hit read-only replicas if we're supporting that
            builder.append("_ro");
        }

        return builder.toString();
    }

    @Override
    public Connection getConnection() throws FHIRPersistenceDBConnectException {
        Connection connection = null;
        final String METHODNAME = "getConnection";

        if (log.isLoggable(Level.FINEST)) {
            log.entering(CLASSNAME, METHODNAME);
        }

        // the dsId/tenantId specific datasource we need to locate
        DataSource datasource;

        // Resources can be routed to different databases using the dsId currently
        // set on the context.
        String tenantId = FHIRRequestContext.get().getTenantId();
        String dsId = FHIRRequestContext.get().getDataStoreId();
        boolean readOnly = this.enableReadOnlyReplicas && FHIRRequestContext.get().isReadOnly();

        // The jndiName may be given explicitly in the fhir-server-config
        String jndiName;

        final String jndiNameProperty = FHIRConfiguration.PROPERTY_DATASOURCES + "/" + dsId + "/jndiName";
        try {
            PropertyGroup fhirConfig = FHIRConfiguration.getInstance().loadConfigurationForTenant(tenantId);
            jndiName = fhirConfig.getStringProperty(jndiNameProperty, null);

            if (readOnly) {
                jndiName = jndiName + "_ro";
            }
        } catch (Exception x) {
            log.log(Level.SEVERE, "Error getting value for tenant/datasource jndiName property: " + jndiNameProperty, x);
            throw new FHIRPersistenceDBConnectException("FHIR server configuration error. See server log for details");
        }

        if (jndiName == null) {
            // Name wasn't provided, so build the name using a standard pattern: jdbc/fhir_<tenantId>_<dsId>[_ro]
            jndiName = makeTenantDatasourceJNDIName(DATASOURCE_BASE_NAME, tenantId, dsId, readOnly);
        }


        // Note: we don't need any synchronization around ConcurrentHashMap, but that
        // doesn't change the fact that we may look up the datasource and put it into
        // the map more than once. That's fine. There aren't any integrity issues, just
        // the chance of doing more work than necessary a single time, but we avoid the
        // need for any synchronization. We cache locally, because we've found that JNDI
        // lookups can become a bottleneck with high concurrency
        datasource = datasourceMap.get(jndiName);
        if (datasource == null) {
            // cache miss
            try {
                InitialContext ctxt = new InitialContext();

                datasource = (DataSource) ctxt.lookup(jndiName);
                datasourceMap.put(jndiName, datasource);
            } catch (Throwable e) {
                // don't emit secrets in exceptions
                log.throwing(this.getClass().getSimpleName(), "getConnection", e);
                FHIRPersistenceDBConnectException fx = new FHIRPersistenceDBConnectException("Failure acquiring datasource");
                throw FHIRDbHelper.severe(log, fx, "Failure acquiring connection for datasource: " + jndiName, e);
            } finally {
                if (log.isLoggable(Level.FINEST)) {
                    log.exiting(CLASSNAME, METHODNAME);
                }
            }
        }

        long start = System.nanoTime();
        if (log.isLoggable(Level.FINE)) {
            log.fine("Getting connection for tenantId/dsId: [" + tenantId + "/" + dsId + "]...");
        }

        // Now use the dsId/tenantId specific JEE datasource to get a connection
        try {
            connection = getConnection(datasource, tenantId, dsId);

            if (log.isLoggable(Level.FINE)) {
                long deltams = (System.nanoTime() - start) / NANOMS;
                log.fine("Got the connection for [" + tenantId + "/" + dsId + "]. Took " + deltams + " ms");
            }
        } catch (Throwable e) {
            // Don't emit secrets in case they are returned to a client
            FHIRPersistenceDBConnectException fx =
                    new FHIRPersistenceDBConnectException("Failure acquiring connection for datasource");
            throw FHIRDbHelper.severe(log, fx, "Failure acquiring connection for datasource: " + jndiName, e);
        } finally {
            if (log.isLoggable(Level.FINEST)) {
                log.exiting(CLASSNAME, METHODNAME);
            }
        }

        return connection;
    }

    @Override
    public FHIRDbFlavor getFlavor() throws FHIRPersistenceDataAccessException {
        return this.flavor;
    }

    /**
     * Identify the flavor of the database using information from the
     * datasource configuration.
     * @return
     * @throws FHIRPersistenceException
     */
    private FHIRDbFlavor createFlavor() throws FHIRPersistenceDataAccessException {
        // TODO duplicate code in FHIRDbConnectionStrategyBase. Suggest refactor when
        // implementing the simple datasource feature - issue-916
        FHIRDbFlavor result;

        String datastoreId = FHIRRequestContext.get().getDataStoreId();

        // Retrieve the property group pertaining to the specified datastore.
        // Find and set the tenantKey for the request, otherwise subsequent pulls from the pool
        // miss the tenantKey.
        String dsPropertyName = FHIRConfiguration.PROPERTY_DATASOURCES + "/" + datastoreId;
        PropertyGroup dsPG = FHIRConfigHelper.getPropertyGroup(dsPropertyName);
        if (dsPG != null) {

            try {
                SchemaType schemaType = SchemaType.PLAIN;
                String schemaTypeValue = dsPG.getStringProperty("schemaType", null);
                if (schemaTypeValue != null) {
                    schemaType = SchemaType.valueOf(schemaTypeValue.toUpperCase());
                }

                String typeValue = dsPG.getStringProperty("type");
                DbType type = DbType.from(typeValue);
                if (type == DbType.DB2) {
                    // For Db2 we currently only support MULTITENANT so we force the schemaType
                    schemaType = SchemaType.MULTITENANT;
                } else {
                    // Make sure for any other database of type we're not being asked to use the
                    // multitenant variant
                    if (schemaType == SchemaType.MULTITENANT) {
                        throw new FHIRPersistenceDataAccessException("schemaType MULTITENANT is only supported for Db2");
                    }
                }

                result = new FHIRDbFlavorImpl(type, schemaType);
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
}
