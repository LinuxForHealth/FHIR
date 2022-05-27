/*
 * (C) Copyright IBM Corp. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.config.FHIRConfigHelper;
import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.config.PropertyGroup;
import com.ibm.fhir.persistence.jdbc.FHIRPersistenceJDBCCache;

/**
 * Manages separate caches for each tenant.
 */
public class FHIRPersistenceJDBCTenantCache {
    private static final Logger logger = Logger.getLogger(FHIRPersistenceJDBCTenantCache.class.getName());

    // Each tenant/datasource gets its own cache instance so we avoid mixing ids
    private final ConcurrentHashMap<String, FHIRPersistenceJDBCCache> cacheMap = new ConcurrentHashMap<>();

    /**
     * Get the FHIRPersistenceJDBCCache cache associated with the tenant and
     * datasource defined in the current request context.
     * @return the cache instance for the tenant/datasource
     * @throws IllegalStateException if the configuration failed to load
     */
    public FHIRPersistenceJDBCCache getCacheForTenantAndDatasource() {
        final String cacheKey = getCacheKeyForTenantDatasource();

        // Obtain the cache for the current tenant/datasource, creating a new one if need be
        FHIRPersistenceJDBCCache result = cacheMap.computeIfAbsent(cacheKey, (k) -> createCache(k));
        if (result == null) {
            // exception handled inside createCache (which will have been logged)
            // The cacheKey is passed back up since it is the combination of the datastore and the key
            throw new IllegalStateException("createCache failed for tenant " + FHIRRequestContext.get().getTenantId());
        }

        return result;
    }

    /**
     * Create the individual caches we need and wrap them in a container we use to hold them in the
     * tenant map
     * @param sharedExternalSystemNameCacheSize
     * @param sharedExternalReferenceValuesCacheSize
     * @return
     */
    protected FHIRPersistenceJDBCCache createCache(String cacheKey) {
        FHIRPersistenceJDBCCache result;

        try {
            // Create a new cache for the current tenant/datasource
            PropertyGroup fhirConfig = FHIRConfiguration.getInstance().loadConfiguration();
            if (fhirConfig == null) {
                throw new IllegalStateException("Unable to load the default fhir-server-config.json");
            }

            final String datastoreId = FHIRRequestContext.get().getDataStoreId();

            // Retrieve the property group pertaining to the specified datastore.
            String dsPropertyName = FHIRConfiguration.PROPERTY_DATASOURCES + "/" + datastoreId;
            PropertyGroup pg = FHIRConfigHelper.getPropertyGroup(dsPropertyName);
            if (pg == null) {
                logger.severe("Missing datasource configuration for '" + dsPropertyName + "'");
                throw new IllegalStateException("Missing datasource configuration for " + dsPropertyName);
            } else {
                int externalSystemCacheSize = pg.getIntProperty("externalSystemCacheSize", 1000);
                int externalValueCacheSize = pg.getIntProperty("externalValueCacheSize", 100000);
                int canonicalCacheSize = pg.getIntProperty("canonicalCacheSize", 1000);
                int logicalResourceIdentCacheSize = pg.getIntProperty("logicalResourceIdentCacheSize", 100000);
                return FHIRPersistenceJDBCCacheUtil.create(externalSystemCacheSize, externalValueCacheSize, canonicalCacheSize, logicalResourceIdentCacheSize);
            }
        } catch (IllegalStateException ise) {
            throw ise;
        } catch (Exception x) {
            logger.log(Level.SEVERE, "Failed to load configuration", x);
            result = null;
        }

        return result;
    }

    /**
     * Derive a key representing the current request tenant and datasource
     * @return
     */
    protected String getCacheKeyForTenantDatasource() {
        StringBuilder cacheName = new StringBuilder();
        cacheName.append(FHIRRequestContext.get().getTenantId())
        .append("~")
        .append(FHIRRequestContext.get().getDataStoreId());
        return cacheName.toString();
    }
}