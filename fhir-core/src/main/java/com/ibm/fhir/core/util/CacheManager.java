/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.core.util;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.stats.CacheStats;

public final class CacheManager {
    private static final CacheManager INSTANCE = new CacheManager();

    private final Map<String, Cache<?, ?>> cacheMap;

    private CacheManager() {
        cacheMap = new ConcurrentHashMap<>();
    }

    public static CacheManager getInstance() {
        return INSTANCE;
    }

    public void addCache(String name, Cache<?, ?> cache) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(cache, "cache");
        cacheMap.put(name, cache);
    }

    public Cache<?, ?> getCache(String name) {
        Objects.requireNonNull(name, "name");
        return cacheMap.get(name);
    }

    public void removeCache(String name) {
        Objects.requireNonNull(name, "name");
        cacheMap.remove(name);
    }

    public CacheStats getCacheStats(String name) {
        Objects.requireNonNull(name, "name");
        Cache<?, ?> cache = cacheMap.get(name);
        if (cache != null) {
            return cache.stats();
        }
        return null;
    }
}
