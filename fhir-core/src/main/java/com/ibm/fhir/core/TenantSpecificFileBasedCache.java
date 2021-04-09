/*
 * (C) Copyright IBM Corp. 2017, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.core;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is a parameterized abstract base class to be used for situations where
 * we need to implement a tenant-specific cache of file-based objects.
 * Examples include: configuration parameters, structure definitions, search parameters, etc.
 */
public abstract class TenantSpecificFileBasedCache<T> {
    private static final Logger log = Logger.getLogger(TenantSpecificFileBasedCache.class.getName());

    private Map<String, CachedObjectHolder<T>> cache;

    // cacheType is used only in trace messages.
    private String cacheType = "<unknown>";

    public TenantSpecificFileBasedCache() {
        cache = new HashMap<String, CachedObjectHolder<T>>();
    }

    public TenantSpecificFileBasedCache(String cacheType) {
        this();
        this.cacheType = cacheType;
    }

    /**
     * Clears the entire cache.
     * This might be useful during testing when you need to clear out the entire cache and re-load.
     */
    public void clearCache() {
        synchronized(cache) {
            cache.clear();
        }
    }

    public abstract String getCacheEntryFilename(String tenantId);
    public abstract T createCachedObject(File file) throws Exception;

    /**
     * @param tenantId
     * @return the cached object for the tenant or null if it could not be found
     * @throws Exception
     */
    public T getCachedObjectForTenant(String tenantId) throws Exception {
        if (log.isLoggable(Level.FINEST)) {
            log.entering(this.getClass().getName(), "getCachedObjectForTenant");
        }
        try {
            // Try to retrieve tenant's cached object from the cache.
            CachedObjectHolder<T> holder = cache.get(tenantId);

            // If we didn't find it or it was stale, then we'll need to lock the cache.
            if (holder == null || holder.isStale()) {
                synchronized (cache) {

                    // Check again to see if we can retrieve the cached object from the cache.
                    holder = cache.get(tenantId);

                    // If the cache object is stale, then discard and re-load below.
                    if (holder != null && holder.isStale()) {
                        log.fine("Cached " + this.cacheType + " for tenant-id '" + tenantId + "' is stale, discarding...");
                        cache.remove(tenantId);
                        holder = null;
                    }

                    // Now check to see if we need to load a new object and add it to the cache.
                    if (holder == null) {
                        String fileName = getCacheEntryFilename(tenantId);
                        File f = new File(fileName);
                        if (log.isLoggable(Level.FINE)) {
                            log.fine("Loading config file from " + f.getAbsolutePath());
                        }
                        T cachedObject = null;

                        // If the file exists, then try to load it.
                        if (f.exists()) {
                            cachedObject = createCachedObject(f);
                        }

                        // If we were able to load the object from disk, then add it to the cache.
                        if (cachedObject != null) {
                            holder = new CachedObjectHolder<T>(fileName, cachedObject);
                            cache.put(tenantId, holder);
                            log.fine("Loaded " + this.cacheType + " for tenant-id '" + tenantId + "' and added it to the cache.");
                        } else {
                            log.fine("Tenant-specific " + this.cacheType + " for tenant '" + tenantId + "' not found, skipping...");
                        }
                    }
                }
            }

            return (holder != null ? holder.getCachedObject() : null);
        } finally {
            if (log.isLoggable(Level.FINEST)) {
                log.exiting(this.getClass().getName(), "getCachedObjectForTenant");
            }
        }
    }
}
