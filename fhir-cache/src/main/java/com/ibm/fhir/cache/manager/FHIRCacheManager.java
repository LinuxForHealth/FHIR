/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.cache.manager;

import static com.ibm.fhir.core.util.CacheKey.key;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import com.ibm.fhir.cache.configuration.Configuration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.core.util.CacheKey;

public final class FHIRCacheManager {
    private static final Map<CacheKey, Cache<?, ?>> MANAGED_CACHE_MAP = new ConcurrentHashMap<>();

    private FHIRCacheManager() { }

    @SuppressWarnings("unchecked")
    public static <K, V> Cache<K, V> getCache(String cacheName) {
        Objects.requireNonNull(cacheName, "cacheName");
        CacheKey key = key(FHIRRequestContext.get().getTenantId(), cacheName);
        return (Cache<K, V>) MANAGED_CACHE_MAP.get(key);
    }

    @SuppressWarnings("unchecked")
    public static <K, V> Cache<K, V> getCache(String cacheName, Configuration configuration) {
        Objects.requireNonNull(cacheName, "cacheName");
        Objects.requireNonNull(configuration, "configuration");
        CacheKey key = key(FHIRRequestContext.get().getTenantId(), cacheName);
        return (Cache<K, V>) MANAGED_CACHE_MAP.computeIfAbsent(key, k -> createCache(configuration));
    }

    public static <K, V> Map<K, V> getCacheAsMap(String cacheName) {
        Objects.requireNonNull(cacheName, "cacheName");
        Cache<K, V> cache = getCache(cacheName);
        return (cache != null) ? cache.asMap() : null;
    }

    public static <K, V> Map<K, V> getCacheAsMap(String cacheName, Configuration configuration) {
        Objects.requireNonNull(cacheName, "cacheName");
        Objects.requireNonNull(configuration, "configuration");
        Cache<K, V> cache = getCache(cacheName, configuration);
        return cache.asMap();
    }

    public static CacheStats getCacheStats(String cacheName) {
        Cache<?, ?> cache = getCache(cacheName);
        return (cache != null) ? cache.stats() : null;
    }

    public static void invalidate(String cacheName, Object key) {
        Cache<?, ?> cache = getCache(cacheName);
        if (cache != null) {
            cache.invalidate(key);
        }
    }

    public static void invalidateAll(String cacheName) {
        Cache<?, ?> cache = getCache(cacheName);
        if (cache != null) {
            cache.invalidateAll();
        }
    }

    public static boolean isManaged(String cacheName) {
        if (cacheName != null) {
            CacheKey key = key(FHIRRequestContext.get().getTenantId(), cacheName);
            return MANAGED_CACHE_MAP.containsKey(key);
        }
        return false;
    }

    private static <K, V> Cache<K, V> createCache(Configuration configuration) {
        if (Objects.nonNull(configuration.getMaximumSize()) && Objects.nonNull(configuration.getDuration())) {
            return Caffeine.newBuilder()
                    .maximumSize(configuration.getMaximumSize())
                    .expireAfterWrite(configuration.getDuration())
                    .recordStats()
                    .build();
        }
        if (Objects.nonNull(configuration.getMaximumSize())) {
            return Caffeine.newBuilder()
                    .maximumSize(configuration.getMaximumSize())
                    .recordStats()
                    .build();
        }
        return Caffeine.newBuilder()
                .expireAfterWrite(configuration.getDuration())
                .recordStats()
                .build();
    }
}
