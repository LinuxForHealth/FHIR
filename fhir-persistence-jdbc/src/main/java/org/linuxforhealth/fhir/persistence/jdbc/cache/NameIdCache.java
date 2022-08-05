/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.cache;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.linuxforhealth.fhir.persistence.jdbc.dao.api.INameIdCache;


/**
 * @param <T> the type of the identity value held by the cache
 */
public class NameIdCache<T> implements INameIdCache<T> {

    // We use LinkedHashMap for the local map because we also need to maintain order
    // of insertion to make sure we have correct LRU behavior when updating the shared cache
    private final ThreadLocal<Map<String, T>> local = new ThreadLocal<>();
    
    // The cache shared at the server level
    private final ConcurrentHashMap<String, T> shared = new ConcurrentHashMap<>();
    
    /**
     * Public constructor
     */
    public NameIdCache() {
    }

    @Override
    public void updateSharedMaps() {
        
        Map<String,T> localMap = local.get();
        if (localMap != null) {
            // no need to synchronize because we can use a ConcurrentHashMap
            shared.putAll(localMap);
            localMap.clear();
        }
    }
    
    @Override
    public T getId(String key) {
        T result = null;
        Map<String,T> localMap = local.get();
        if (localMap != null) {
            result = localMap.get(key);
        }

        if (result == null) {
            result = shared.get(key);
        }
        return result;
    }

    @Override
    public Collection<T> getAllIds() {
        Collection<T> result = null;
        Map<String,T> localMap = local.get();
        if (localMap != null) {
            result = localMap.values();
        }

        if (result == null) {
            result = shared.values();
        }
        return result;
    }

    @Override
    public void addEntry(String resourceType, T resourceTypeId) {
        Map<String,T> localMap = local.get();
        if (localMap == null) {
            localMap = new HashMap<>();
            local.set(localMap);
        }
        localMap.put(resourceType, resourceTypeId);
    }

    @Override
    public void reset() {
        local.remove();
        shared.clear();
    }

    @Override
    public void clearLocalMaps() {
        Map<String, T> map = local.get();
        if (map != null) {
            map.clear();
        }
    }

    @Override
    public void prefill(Map<String, T> content) {
        // as the given content is supposed to be already committed in the database,
        // we can add it directly to the shared map
        this.shared.putAll(content);
    }
}