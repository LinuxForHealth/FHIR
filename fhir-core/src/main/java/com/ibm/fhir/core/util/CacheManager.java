/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.core.util;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

public final class CacheManager {
    private static final Map<String, Cache<?, ?>> CACHE_MAP = new ConcurrentHashMap<>();

    private CacheManager() { }

    public static <K, V> Cache<K, V> createCache(String cacheName, int maximumSize) {
        Cache<K, V> cache = Caffeine.newBuilder()
                .maximumSize(maximumSize)
                .recordStats()
                .build();
        addCache(cacheName, cache);
        return cache;
    }

    public static <K, V> Map<K, V> createCacheAsMap(String cacheName, int maximumSize) {
        Cache<K, V> cache = Caffeine.newBuilder()
                .maximumSize(maximumSize)
                .recordStats()
                .build();
        addCache(cacheName, cache);
        return cache.asMap();
    }

    public static <K, V> Cache<K, V> createCache(String cacheName, Duration duration) {
        Cache<K, V> cache = Caffeine.newBuilder()
                .expireAfterWrite(duration)
                .recordStats()
                .build();
        addCache(cacheName, cache);
        return cache;
    }

    public static <K, V> Map<K, V> createCacheAsMap(String cacheName, Duration duration) {
        Cache<K, V> cache = Caffeine.newBuilder()
                .expireAfterWrite(duration)
                .recordStats()
                .build();
        addCache(cacheName, cache);
        return cache.asMap();
    }

    public static <K, V> Cache<K, V> createCache(String cacheName, int maximumSize, Duration duration) {
        Cache<K, V> cache = Caffeine.newBuilder()
                .maximumSize(maximumSize)
                .expireAfterWrite(duration)
                .recordStats()
                .build();
        addCache(cacheName, cache);
        return cache;
    }

    public static <K, V> Map<K, V> createCacheAsMap(String cacheName, int maximumSize, Duration duration) {
        Cache<K, V> cache = Caffeine.newBuilder()
                .maximumSize(maximumSize)
                .expireAfterWrite(duration)
                .recordStats()
                .build();
        addCache(cacheName, cache);
        return cache.asMap();
    }

    public static void addCache(String cacheName, Cache<?, ?> cache) {
        Objects.requireNonNull(cacheName, "cacheName");
        Objects.requireNonNull(cache, "cache");
        if (isManaged(cacheName)) {
            throw new IllegalArgumentException("Cache with cacheName '" + cacheName + "' is already managed");
        }
        CACHE_MAP.put(cacheName, cache);
    }

    @SuppressWarnings("unchecked")
    public static <K, V> Cache<K, V> getCache(String cacheName) {
        Objects.requireNonNull(cacheName, "cacheName");
        return (Cache<K, V>) CACHE_MAP.get(cacheName);
    }

    public static void removeCache(String cacheName) {
        Objects.requireNonNull(cacheName, "cacheName");
        CACHE_MAP.remove(cacheName);
    }

    public static boolean isManaged(String cacheName) {
        Objects.requireNonNull(cacheName, "cacheName");
        return CACHE_MAP.containsKey(cacheName);
    }
}
