/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.cache;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.linuxforhealth.fhir.persistence.jdbc.dao.api.IIdNameCache;


/**
 * @param <T> the type of the key value held by the cache
 */
public class IdNameCache<T> implements IIdNameCache<T> {

    // The local cache
    private final ThreadLocal<Map<T,String>> local = new ThreadLocal<>();
    
    // The cache shared at the server level
    private final ConcurrentHashMap<T,String> shared = new ConcurrentHashMap<>();
    
    /**
     * Public constructor
     */
    public IdNameCache() {
    }

    @Override
    public void updateSharedMaps() {
        
        Map<T,String> localMap = local.get();
        if (localMap != null) {
            // no need to synchronize because we can use a ConcurrentHashMap
            shared.putAll(localMap);
            localMap.clear();
        }
    }
    
    @Override
    public String getName(T key) {
        String result = null;
        Map<T,String> localMap = local.get();
        if (localMap != null) {
            result = localMap.get(key);
        }

        if (result == null) {
            result = shared.get(key);
        }
        return result;
    }

    @Override
    public Collection<String> getAllNames() {
        Collection<String> result = null;
        Map<T,String> localMap = local.get();
        if (localMap != null) {
            result = localMap.values();
        }

        if (result == null) {
            result = shared.values();
        }
        return result;
    }

    @Override
    public void addEntry(T id, String name) {
        Map<T,String> localMap = local.get();
        if (localMap == null) {
            localMap = new HashMap<>();
            local.set(localMap);
        }
        localMap.put(id, name);
    }

    @Override
    public void reset() {
        local.remove();
        shared.clear();
    }

    @Override
    public void clearLocalMaps() {
        Map<T,String> map = local.get();
        if (map != null) {
            map.clear();
        }
    }

    @Override
    public void prefill(Map<T,String> content) {
        // as the given content is supposed to be already committed in the database,
        // we can add it directly to the shared map
        this.shared.putAll(content);
    }
}