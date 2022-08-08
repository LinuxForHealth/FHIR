/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.cos.impl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.config.FHIRConfigHelper;
import org.linuxforhealth.fhir.config.FHIRConfiguration;
import org.linuxforhealth.fhir.config.FHIRRequestContext;
import org.linuxforhealth.fhir.config.PropertyGroup;
import org.linuxforhealth.fhir.core.lifecycle.EventCallback;
import org.linuxforhealth.fhir.core.lifecycle.EventManager;
import org.linuxforhealth.fhir.persistence.cos.client.COSPayloadClient;
import org.linuxforhealth.fhir.persistence.cos.client.CosPropertyGroupAdapter;

/**
 * Singleton to manage/isolate COS clients for each FHIR tenant/datasource
 */
public class COSClientManager implements EventCallback {
    private static final Logger logger = Logger.getLogger(COSClientManager.class.getName());

    private final ConcurrentHashMap<TenantDatasourceKey, COSPayloadClient> clientMap = new ConcurrentHashMap<>();

    private volatile boolean running = true;

    /**
     * Singleton pattern safe construction
     */
    private static class Helper {
        private static COSClientManager INSTANCE = new COSClientManager();
    }

    /**
     * Private constructor
     */
    private COSClientManager() {
        // register a callback with the lifecycle event manager so we're notified when the server
        // is shut down
        EventManager.register(this);
    }

    /**
     * Get the singleton instance of this {@link COSClientManager}
     * @return
     */
    public static COSClientManager getInstance() {
        return Helper.INSTANCE;
    }

    /**
     * Get the (shared, thread-safe) client object representing the connection to
     * COS for the current tenant/datasource (see {@link FHIRRequestContext}).
     * @return
     */
    public static COSPayloadClient getClientForTenantDatasource() {
        return COSClientManager.getInstance().getOrCreateClient();
    }

    /**
     * Get or create the connection to COS for the current
     * tenant/datasource
     * @return
     */
    private COSPayloadClient getOrCreateClient() {
        if (!running) {
            throw new IllegalStateException("COSClientManager is shut down");
        }

        // Connections can be tenant-specific, so find out what tenant we're associated with and use its persistence
        // configuration to obtain the appropriate COSPayloadClient instance (shared by multiple threads).
        final String tenantId = FHIRRequestContext.get().getTenantId();
        final String dsId = "default"; // only one payload datasource
        TenantDatasourceKey key = new TenantDatasourceKey(tenantId, dsId);

        // Get the COS client for this tenant/datasource, or create a new one if needed
        return clientMap.computeIfAbsent(key, COSClientManager::newClient);
    }

    /**
     * Build a new COSPayloadClient object for the tenant/datasource represented
     * by the key.
     * @param key
     * @return
     */
    private static COSPayloadClient newClient(TenantDatasourceKey key) {

        String dsPropertyName = FHIRConfiguration.PROPERTY_PERSISTENCE_PAYLOAD + "/" + key.getDatasourceId();
        PropertyGroup dsPG = FHIRConfigHelper.getPropertyGroup(dsPropertyName);
        if (dsPG == null) {
            throw new IllegalStateException("Could not locate configuration property group: " + dsPropertyName);
        }

        try {
            // Get the payload datasource type (cassandra/cos).
            String type = dsPG.getStringProperty("type", null);
            if (type == null) {
                throw new IllegalStateException("Could not locate 'type' property within datasource property group: " + dsPropertyName);
            }

            // Confirm that this is a COS datasource configuration element
            if (!"cos".equals(type)) {
                throw new IllegalStateException("Unsupported 'type' property value within datasource property group: " + type);
            }

            // Check that the connection properties are configured
            PropertyGroup connectionProps = dsPG.getPropertyGroup("connectionProperties");
            if (connectionProps == null) {
                throw new IllegalStateException("Could not locate 'connectionProperties' property group within datasource property group: " + dsPropertyName);
            }

            // Wrap the main properties in an adapter to simplify access
            CosPropertyGroupAdapter adapter = new CosPropertyGroupAdapter(dsPG);
            return new COSPayloadClient(key.getTenantId(), key.getDatasourceId(), adapter);
        } catch (Exception x) {
            throw new IllegalStateException(x);
        }
    }

    /**
     * Get rid of any client connections we're holding on to
     */
    private void closeAllClients() {
        // prevent anyone asking for a client
        this.running = false;
        clientMap.clear();
    }

    /**
     * Called when COSClientManager is used in a main. For app-server, use the {@link EventCallback}
     */
    public static void shutdown() {
        logger.info("Shutting down COSClientManager");
        getInstance().closeAllClients();
        logger.info("COSClientManager shutdown complete");
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
        closeAllClients();
    }
}