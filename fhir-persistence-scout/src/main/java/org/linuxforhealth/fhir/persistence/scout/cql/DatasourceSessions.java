/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.scout.cql;

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
import org.linuxforhealth.fhir.persistence.scout.CassandraPropertyGroupAdapter;
import org.linuxforhealth.fhir.persistence.scout.ContactPoint;

/**
 * Singleton to manage Cassandra CqlSession connections for each FHIR tenant/datasource
 */
public class DatasourceSessions {
    private static final Logger logger = Logger.getLogger(DatasourceSessions.class.getName());

    private final ConcurrentHashMap<TenantDatasourceKey, CqlSession> sessionMap = new ConcurrentHashMap<>();
    
    private volatile boolean running = true;
    
    /**
     * Singleton pattern safe construction 
     */
    private static class Helper {
        private static DatasourceSessions INSTANCE = new DatasourceSessions();
    }
    
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
     * tenant/datasource
     * @return
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
        return sessionMap.computeIfAbsent(key, DatasourceSessions::newSession);
    }

    /**
     * Build a new CqlSession object for the tenant/datasource.
     * @param key
     * @return
     */
    private static CqlSession newSession(TenantDatasourceKey key) {
        
        String dsPropertyName = FHIRConfiguration.PROPERTY_DATASOURCES + "/" + key.getDatasourceId();
        PropertyGroup dsPG = FHIRConfigHelper.getPropertyGroup(dsPropertyName);
        if (dsPG == null) {
            throw new IllegalStateException("Could not locate configuration property group: " + dsPropertyName);
        }

        try {
            // Get the datasource type (Derby, DB2, etc.).
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
            
            CassandraPropertyGroupAdapter adapter = new CassandraPropertyGroupAdapter(connectionProps);
            return getDatabaseSession(key, adapter, true);
        }
        catch (Exception x) {
            throw new IllegalStateException(x);
        }
    }
    
    /**
     * Get the CqlSession for the Cassandra database matching the configuration defined
     * by the properties adapter.
     * @param adapter
     * @return
     */
    private static CqlSession getDatabaseSession(TenantDatasourceKey key, CassandraPropertyGroupAdapter adapter, boolean setKeyspace) {
        CqlSessionBuilder builder = CqlSession.builder();
        
        for (ContactPoint cp: adapter.getContactPoints()) {
            builder.addContactPoint(new InetSocketAddress(cp.getHost(), cp.getPort()));
        }
        builder.withLocalDatacenter(adapter.getLocalDatacenter());

        if (setKeyspace) {
            // Use the tenant id value directly as for the keyspace
            builder.withKeyspace(key.getTenantId());
        }
        
        return builder.build();
    }

    /**
     * Create a special session without specifying the keyspace, which is needed to support
     * schema creation where we create the keyspace for the first time
     * @return
     */
    public static CqlSession getSessionForBootstrap(String tenantId, String dsId) {

        String dsPropertyName = FHIRConfiguration.PROPERTY_DATASOURCES + "/" + dsId;
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
            
            TenantDatasourceKey key = new TenantDatasourceKey(tenantId, dsId);
            CassandraPropertyGroupAdapter adapter = new CassandraPropertyGroupAdapter(connectionProps);
            return getDatabaseSession(key, adapter, false);
        }
        catch (Exception x) {
            throw new IllegalStateException(x);
        }
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
     * 
     */
    public static void shutdown() {
        logger.info("Shutting down DatasourceSessions");
        getInstance().closeAllSessions();
        logger.info("DatasourceSessions shutdown complete");
    }
}
