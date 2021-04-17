/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.cache.manager;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import com.ibm.fhir.cache.configuration.Configuration;
import com.ibm.fhir.core.TenantIdProvider;

public final class FHIRCacheManager {
    private static final Map<String, Map<String, Cache<?, ?>>> TENANT_CACHE_MAPS = new ConcurrentHashMap<>();
    private static final TenantIdProvider TENANT_ID_PROVIDER = TenantIdProvider.provider();

    private FHIRCacheManager() { }

    public Set<String> getCacheNames() {
        String tenantId = TENANT_ID_PROVIDER.getTenantId();
        return TENANT_CACHE_MAPS.getOrDefault(tenantId, Collections.emptyMap()).keySet();
    }

    @SuppressWarnings("unchecked")
    public static <K, V> Cache<K, V> getCache(String cacheName) {
        Objects.requireNonNull(cacheName, "cacheName");
        String tenantId = TENANT_ID_PROVIDER.getTenantId();
        Map<String, Cache<?, ?>> tenantCacheMap = TENANT_CACHE_MAPS.getOrDefault(tenantId, Collections.emptyMap());
        return (Cache<K, V>) tenantCacheMap.get(cacheName);
    }

    @SuppressWarnings("unchecked")
    public static <K, V> Cache<K, V> getCache(String cacheName, Configuration configuration) {
        Objects.requireNonNull(cacheName, "cacheName");
        Objects.requireNonNull(configuration, "configuration");
        String tenantId = TENANT_ID_PROVIDER.getTenantId();
        Map<String, Cache<?, ?>> tenantCacheMap = TENANT_CACHE_MAPS.computeIfAbsent(tenantId, k -> new ConcurrentHashMap<>());
        return (Cache<K, V>) tenantCacheMap.computeIfAbsent(cacheName, k -> createCache(configuration));
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
            String tenantId = TENANT_ID_PROVIDER.getTenantId();
            return TENANT_CACHE_MAPS.getOrDefault(tenantId, Collections.emptyMap()).containsKey(cacheName);
        }
        return false;
    }

    public static void removeCache(String cacheName) {
        if (cacheName != null) {
            String tenantId = TENANT_ID_PROVIDER.getTenantId();
            Map<String, Cache<?, ?>> tenantCacheMap = TENANT_CACHE_MAPS.get(tenantId);
            if (tenantCacheMap != null) {
                tenantCacheMap.remove(cacheName);
            }
        }
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
