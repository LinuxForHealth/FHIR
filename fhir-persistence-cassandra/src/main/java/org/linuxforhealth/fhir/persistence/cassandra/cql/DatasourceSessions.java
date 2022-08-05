/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.cassandra.cql;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import org.linuxforhealth.fhir.config.FHIRConfigHelper;
import org.linuxforhealth.fhir.config.FHIRConfiguration;
import org.linuxforhealth.fhir.config.FHIRRequestContext;
import org.linuxforhealth.fhir.config.PropertyGroup;
import org.linuxforhealth.fhir.core.lifecycle.EventCallback;
import org.linuxforhealth.fhir.core.lifecycle.EventManager;
import org.linuxforhealth.fhir.persistence.cassandra.CassandraPropertyGroupAdapter;
import org.linuxforhealth.fhir.persistence.cassandra.ContactPoint;

/**
 * Singleton to manage Cassandra CqlSession connections for each FHIR tenant/datasource.
 * CqlSession holds the state of the cluster and is thread-safe. There should be a single
 * value of CqlSession for a given tenant/datasource and this shouldn't be closed by the
 * application until shutdown (handled by the EventCallback server lifecycle events).
 */
public class DatasourceSessions implements EventCallback {
    private static final Logger logger = Logger.getLogger(DatasourceSessions.class.getName());

    // Map holding one CqlSession instance per tenant/datasource
    private final ConcurrentHashMap<TenantDatasourceKey, CqlSession> sessionMap = new ConcurrentHashMap<>();
    
    // so we can reject future requests when shut down
    private volatile boolean running = true;
    
    /**
     * Singleton pattern safe construction 
     */
    private static class Helper {
        private static DatasourceSessions INSTANCE = new DatasourceSessions();
    }
    
    /**
     * Private constructor
     */
    private DatasourceSessions() {
        // receive server lifecycle events
        EventManager.register(this);
    }
    
    /**
     * Get the singleton instance of this class
     * @return
     */
    public static DatasourceSessions getInstance() {
        return Helper.INSTANCE;
    }
    
    /**
     * Get the (shared, thread-safe) session object representing the connection to
     * Cassandra for the current tenant/datasource (see {@link FHIRRequestContext}).
     * @return
     */
    public static CqlSession getSessionForTenantDatasource() {
        return DatasourceSessions.getInstance().getOrCreateSession();
    }

    /**
     * Get or create the CqlSession connection to Cassandra for the current
     * tenant/datasource. The wrapped instance intercepts calls to {@link AutoCloseable#close()}.
     * Users do not need to close the object, but may do so (for instance in
     * a try-with-resource pattern).
     * @return a wrapped instance of the CqlSession for which {@link AutoCloseable#close()} is a NOP
     */
    private CqlSession getOrCreateSession() {
        if (!running) {
            throw new IllegalStateException("DatasourceSessions is shut down");
        }
        
        // Connections can be tenant-specific, so find out what tenant we're associated with and use its persistence
        // configuration to obtain the appropriate CqlSession instance (shared by multiple threads).
        final String tenantId = FHIRRequestContext.get().getTenantId();
        final String dsId = FHIRRequestContext.get().getDataStoreId();
        TenantDatasourceKey key = new TenantDatasourceKey(tenantId, dsId);

        // Get the session for this tenant/datasource, or create a new one if needed
        CqlSession cs = sessionMap.computeIfAbsent(key, DatasourceSessions::newSession);
        
        // Wrap the session so we can intercept calls to #close
        return new CqlSessionWrapper(cs);
    }

    /**
     * Build a new CqlSession object for the tenant/datasource tuple described by key.
     * @param key
     * @return
     */
    private static CqlSession newSession(TenantDatasourceKey key) {
        String dsPropertyName = FHIRConfiguration.PROPERTY_PERSISTENCE_PAYLOAD + "/" + key.getDatasourceId();
        CassandraPropertyGroupAdapter adapter = getPropertyGroupAdapter(dsPropertyName);
        return getDatabaseSession(key, adapter, true);
    }
 
    /**
     * Check if payload persistence is configured for the current tenant/datasource
     * @return
     */
    public static boolean isPayloadPersistenceConfigured() {
        final String tenantId = FHIRRequestContext.get().getTenantId();
        final String dsId = FHIRRequestContext.get().getDataStoreId();
        TenantDatasourceKey key = new TenantDatasourceKey(tenantId, dsId);
        String dsPropertyName = FHIRConfiguration.PROPERTY_PERSISTENCE_PAYLOAD + "/" + key.getDatasourceId();
        PropertyGroup dsPG = FHIRConfigHelper.getPropertyGroup(dsPropertyName);
        return dsPG != null;
    }

    /**
     * Get a CassandraPropertyGroupAdapter bound to the property group described by
     * the given dsPropertyName path (in fhir-server-config.json).
     * @param dsPropertyName
     * @return
     */
    private static CassandraPropertyGroupAdapter getPropertyGroupAdapter(String dsPropertyName) {
        
        PropertyGroup dsPG = FHIRConfigHelper.getPropertyGroup(dsPropertyName);
        if (dsPG == null) {
            throw new IllegalStateException("Could not locate configuration property group: " + dsPropertyName);
        }

        try {
            // Get the datasource type (should be cassandra in this case).
            String type = dsPG.getStringProperty("type", null);
            if (type == null) {
                throw new IllegalStateException("Could not locate 'type' property within datasource property group: " + dsPropertyName);
            }
    
            // Confirm that this is a cassandra datasource configuration element
            if (!"cassandra".equals(type)) {
                throw new IllegalStateException("Unsupported 'type' property value within datasource property group: " + type);  
            }
    
            // Get the connection properties
            PropertyGroup connectionProps = dsPG.getPropertyGroup("connectionProperties");
            if (connectionProps == null) {
                throw new IllegalStateException("Could not locate 'connectionProperties' property group within datasource property group: " + dsPropertyName);
            }
            
            return new CassandraPropertyGroupAdapter(connectionProps);
        }
        catch (Exception x) {
            throw new IllegalStateException(x);
        }
    }
    
    /**
     * Get the CqlSession for the Cassandra database matching the configuration defined
     * by the properties adapter.
     * @param key
     * @param adapter
     * @param setKeyspace
     * @return
     */
    private static CqlSession getDatabaseSession(TenantDatasourceKey key, CassandraPropertyGroupAdapter adapter, boolean setKeyspace) {
        CqlSessionBuilder builder = CqlSession.builder();
        
        for (ContactPoint cp: adapter.getContactPoints()) {
            builder.addContactPoint(new InetSocketAddress(cp.getHost(), cp.getPort()));
        }
        builder.withLocalDatacenter(adapter.getLocalDatacenter());

        if (setKeyspace) {
            String tenantKeyspace = adapter.getTenantKeyspace();
            if (tenantKeyspace == null || tenantKeyspace.isEmpty()) {
                // Use the tenant id value directly as for the keyspace
                tenantKeyspace = key.getTenantId();
            }
            builder.withKeyspace(tenantKeyspace);
        }
        
        return builder.build();
    }

    /**
     * Create a special session without specifying the keyspace, which is needed to support
     * schema creation where we create the keyspace for the first time
     * @param tenantId
     * @param dsId
     * @return
     */    
    public static CqlSession getSessionForBootstrap(String tenantId, String dsId) {

        String dsPropertyName = FHIRConfiguration.PROPERTY_PERSISTENCE_PAYLOAD + "/" + dsId;
        TenantDatasourceKey key = new TenantDatasourceKey(tenantId, dsId);
        CassandraPropertyGroupAdapter adapter = getPropertyGroupAdapter(dsPropertyName);
        return getDatabaseSession(key, adapter, false);
    }

    /**
     * Get rid of any sessions we're holding on to
     */
    private void closeAllSessions() {
        // prevent anyone asking for a session
        this.running = false;
        sessionMap.forEach((k,s) -> closeSession(k, s));
        sessionMap.clear();
    }

    /**
     * Safely close the session associated with this {@link TenantDatasourceKey}, catching
     * and logging any exceptions. Does not rethrow any exception caught when trying to close the
     * session.
     * @param key
     * @param s
     */
    private void closeSession(TenantDatasourceKey key, CqlSession s) {
        try {
            logger.info("Closing session for tenant/datasource: '"
                    + key.getTenantId() + "/" + key.getDatasourceId() + "'");
            s.close();
        } catch (Exception x) {
            logger.log(Level.WARNING, "failed to close session for tenant/datasource: '" 
                    + key.getTenantId() + "/" + key.getDatasourceId() + "'", x);
        }
    }

    /**
     * Close any sessions that are currently open to permit a clean exit
     */
    public static void shutdown() {
        logger.info("Shutting down DatasourceSessions");
        getInstance().closeAllSessions();
        logger.info("DatasourceSessions shutdown complete");
    }

    @Override
    public void serverReady() {
        // NOP
    }

    @Override
    public void startShutdown() {
        this.running = false;
    }

    @Override
    public void finalShutdown() {
        sessionMap.forEach((k,s) -> closeSession(k, s));
        sessionMap.clear();
    }
}