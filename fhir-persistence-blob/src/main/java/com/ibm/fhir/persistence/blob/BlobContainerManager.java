/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.blob;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import com.azure.core.http.HttpClient;
import com.azure.core.http.okhttp.OkHttpAsyncHttpClientBuilder;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.ibm.fhir.config.FHIRConfigHelper;
import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.config.PropertyGroup;
import com.ibm.fhir.core.lifecycle.EventCallback;
import com.ibm.fhir.core.lifecycle.EventManager;

/**
 * Singleton to abstract and manage Azure Blob containers.
 * Each tenant/datasource gets its own container.
 * 
 * TODO: investigate if BlobContainerClient should be long-lived and shared
 * by multiple threads and how to configure its http client library and executor
 * when running inside Liberty.
 */
public class BlobContainerManager implements EventCallback {
    private static final Logger logger = Logger.getLogger(BlobContainerManager.class.getName());

    // Map holding one container client instance per tenant/datasource
    private final ConcurrentHashMap<TenantDatasourceKey, BlobContainerClient> connectionMap = new ConcurrentHashMap<>();
    
    // so we can reject future requests when shut down
    private volatile boolean running = true;
    
    /**
     * Singleton pattern safe construction 
     */
    private static class Helper {
        private static BlobContainerManager INSTANCE = new BlobContainerManager();
    }
    
    /**
     * Private constructor
     */
    private BlobContainerManager() {
        // receive server lifecycle events
        EventManager.register(this);
    }
    
    /**
     * Get the singleton instance of this class
     * @return
     */
    public static BlobContainerManager getInstance() {
        return Helper.INSTANCE;
    }
    
    /**
     * Get the (shared, thread-safe) connection object representing the Azure
     * Blob connection for the current tenant/datasource 
     * (see {@link FHIRRequestContext}).
     * @return
     */
    public static BlobManagedContainer getSessionForTenantDatasource() {
        return BlobContainerManager.getInstance().getOrCreateSession();
    }

    /**
     * Get or create the Azure Blob client for the current
     * tenant/datasource.
     * @return a BlobManagedContainer for the current tenant/datasource
     */
    private BlobManagedContainer getOrCreateSession() {
        if (!running) {
            throw new IllegalStateException("BlobConnectionManager is shut down");
        }
        
        // Connections can be tenant-specific, so find out what tenant we're associated with and use its persistence
        // configuration to obtain the appropriate instance (shared by multiple threads).
        final String tenantId = FHIRRequestContext.get().getTenantId();
        final String dsId = FHIRRequestContext.get().getDataStoreId();
        TenantDatasourceKey key = new TenantDatasourceKey(tenantId, dsId);

        // Get the session for this tenant/datasource, or create a new one if needed
        BlobContainerClient client = connectionMap.computeIfAbsent(key, BlobContainerManager::newConnection);
        String dsPropertyName = FHIRConfiguration.PROPERTY_PERSISTENCE_PAYLOAD + "/" + key.getDatasourceId();
        BlobPropertyGroupAdapter properties = getPropertyGroupAdapter(dsPropertyName);
        
        return new BlobManagedContainer(client, properties);
    }

    /**
     * Build a new CqlSession object for the tenant/datasource tuple described by key.
     * @param key
     * @return
     */
    private static BlobContainerClient newConnection(TenantDatasourceKey key) {
        String dsPropertyName = FHIRConfiguration.PROPERTY_PERSISTENCE_PAYLOAD + "/" + key.getDatasourceId();
        BlobPropertyGroupAdapter adapter = getPropertyGroupAdapter(dsPropertyName);
        return makeConnection(key, adapter);
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
    public static BlobPropertyGroupAdapter getPropertyGroupAdapter(String dsPropertyName) {
        
        PropertyGroup dsPG = FHIRConfigHelper.getPropertyGroup(dsPropertyName);
        if (dsPG == null) {
            throw new IllegalStateException("Could not locate configuration property group: " + dsPropertyName);
        }

        try {
            // Get the datasource type (should be "azure.blob" in this case).
            String type = dsPG.getStringProperty("type", null);
            if (type == null) {
                throw new IllegalStateException("Could not locate 'type' property within datasource property group: " + dsPropertyName);
            }
    
            // Confirm that this is an Azure Blob datasource configuration element
            if (!"azure.blob".equals(type)) {
                throw new IllegalStateException("Unsupported 'type' property value within datasource property group: " + type);  
            }
    
            // Get the connection properties
            PropertyGroup connectionProps = dsPG.getPropertyGroup("connectionProperties");
            if (connectionProps == null) {
                throw new IllegalStateException("Could not locate 'connectionProperties' property group within datasource property group: " + dsPropertyName);
            }
            
            return new BlobPropertyGroupAdapter(connectionProps);
        }
        catch (Exception x) {
            throw new IllegalStateException(x);
        }
    }
    
    /**
     * Get the BlobContainerClient for the Azure blob endpoint using the configuration
     * described by the {@link BlobPropertyGroupAdapter}.
     * @param key
     * @param adapter
     * @return
     */
    private static BlobContainerClient makeConnection(TenantDatasourceKey key, BlobPropertyGroupAdapter adapter) {
        final String containerName;
        if (adapter.getContainerName() != null) {
            containerName = adapter.getContainerName();
        } else {
            // Fallback option, which can be used as long as the tenant and datasource ids
            // adhere to the restrictions required to container names (alphanum or '-')
            containerName = key.getTenantId().toLowerCase() + "-" + key.getDatasourceId().toLowerCase();
        }

        // Explicitly use the okhttp client so we don't end up with library versioning
        // issues for Netty.
        HttpClient httpClient = new OkHttpAsyncHttpClientBuilder()
                .build();
        BlobContainerClient blobContainerClient = new BlobContainerClientBuilder()
                .httpClient(httpClient)
                .connectionString(adapter.getConnectionString())
                .containerName(containerName)
                .buildClient();
        
        return blobContainerClient;
    }

    /**
     * Get rid of any sessions we're holding on to
     */
    private void closeAllSessions() {
        // prevent anyone asking for a session
        this.running = false;
        connectionMap.clear();
    }

    /**
     * Close any sessions that are currently open to permit a clean exit
     * TODO what shutdown do we need to do
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
        connectionMap.clear();
    }
}