/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.cache.util;

import java.time.Duration;
import java.util.Map;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

/**
 * A utility class for creating Caffeine cache instances
 */
public final class CacheSupport {
    private CacheSupport() { }

    /**
     * Create a cache with a time-based eviction policy
     *
     * @param <K>
     *     the key type
     * @param <V>
     *     the value type
     * @param duration
     *     the duration of entries after they are written to the cache
     * @return
     *     the cache instance
     */
    public static <K, V> Cache<K, V> createCache(Duration duration) {
        return createCache(duration, false);
    }

    /**
     * Create a cache with a time-based eviction policy
     *
     * @param <K>
     *     the key type
     * @param <V>
     *     the value type
     * @param duration
     *     the duration of entries after they are written to the cache
     * @param recordStats
     *     specifies whether stats should be recorded for the cache
     * @return
     *     the cache instance
     */
    public static <K, V> Cache<K, V> createCache(Duration duration, boolean recordStats) {
        if (recordStats) {
            return Caffeine.newBuilder()
                    .expireAfterWrite(duration)
                    .recordStats()
                    .build();
        }
        return Caffeine.newBuilder()
                .expireAfterWrite(duration)
                .build();
    }

    /**
     * Create a cache with a size-based eviction policy
     *
     * @param <K>
     *     the key type
     * @param <V>
     *     the value type
     * @param maximumSize
     *     the maximum size of the cache before entries are evicted
     * @return
     *     the cache instance
     */
    public static <K, V> Cache<K, V> createCache(int maximumSize) {
        return createCache(maximumSize, false);
    }

    /**
     * Create a cache with a size-based eviction policy
     *
     * @param <K>
     *     the key type
     * @param <V>
     *     the value type
     * @param maximumSize
     *     the maximum size of the cache before entries are evicted
     * @param recordStats
     *     specifies whether stats should be recorded for the cache
     * @return
     *     the cache instance
     */
    public static <K, V> Cache<K, V> createCache(int maximumSize, boolean recordStats) {
        if (recordStats) {
            return Caffeine.newBuilder()
                    .maximumSize(maximumSize)
                    .recordStats()
                    .build();
        }
        return Caffeine.newBuilder()
                .maximumSize(maximumSize)
                .build();
    }

    /**
     * Create a cache with both size and time-based eviction policies
     *
     * @param <K>
     *     the key type
     * @param <V>
     *     the value type
     * @param maximumSize
     *     the maximum size of the cache before entries are evicted
     * @param duration
     *     the duration of entries after they are written to the cache
     * @return
     *     the cache instance
     */
    public static <K, V> Cache<K, V> createCache(int maximumSize, Duration duration) {
        return createCache(maximumSize, duration, false);
    }

    /**
     * Create a cache with both size and time-based eviction policies
     *
     * @param <K>
     *     the key type
     * @param <V>
     *     the value type
     * @param maximumSize
     *     the maximum size of the cache before entries are evicted
     * @param duration
     *     the duration of entries after they are written to the cache
     * @param recordStats
     *     specifies whether stats should be recorded for the cache
     * @return
     *     the cache instance
     */
    public static <K, V> Cache<K, V> createCache(int maximumSize, Duration duration, boolean recordStats) {
        if (recordStats) {
            return Caffeine.newBuilder()
                    .maximumSize(maximumSize)
                    .expireAfterWrite(duration)
                    .recordStats()
                    .build();
        }
        return Caffeine.newBuilder()
                .maximumSize(maximumSize)
                .expireAfterWrite(duration)
                .build();
    }

    /**
     * Create a cache with a time-based eviction policy as a thread-safe map
     *
     * @param <K>
     *     the key type
     * @param <V>
     *     the value type
     * @param duration
     *     the duration of entries after they are written to the cache
     * @return
     *     a thread-safe map view of entries in the cache instance
     */
    public static <K, V> Map<K, V> createCacheAsMap(Duration duration) {
        Cache<K, V> cache = createCache(duration);
        return cache.asMap();
    }

    /**
     * Create a cache with a size-based eviction policy as a thread-safe map
     *
     * @param <K>
     *     the key type
     * @param <V>
     *     the value type
     * @param maximumSize
     *     the maximum size of the cache before entries are evicted
     * @return
     *     a thread-safe map view of entries in the cache instance
     */
    public static <K, V> Map<K, V> createCacheAsMap(int maximumSize) {
        Cache<K, V> cache = createCache(maximumSize);
        return cache.asMap();
    }

    /**
     * Create a cache with both size and time-based eviction policies as a thead-safe map
     *
     * @param <K>
     *     the key type
     * @param <V>
     *     the value type
     * @param maximumSize
     *     the maximum size of the cache before entries are evicted
     * @param duration
     *     the duration of entries after they are written to the cache
     * @return
     *     a thread-safe map view of entries in the cache instance
     */
    public static <K, V> Map<K, V> createCacheAsMap(int maximumSize, Duration duration) {
        Cache<K, V> cache = createCache(maximumSize, duration);
        return cache.asMap();
    }
}
