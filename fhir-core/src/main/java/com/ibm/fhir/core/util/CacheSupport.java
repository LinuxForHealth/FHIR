/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.core.util;

import java.time.Duration;
import java.util.Map;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

public final class CacheSupport {
    private CacheSupport() { }

    public static <K, V> Cache<K, V> createCache(Duration duration) {
        return Caffeine.newBuilder()
                .expireAfterWrite(duration)
                .build();
    }

    public static <K, V> Cache<K, V> createCache(int maximumSize) {
        return Caffeine.newBuilder()
                .maximumSize(maximumSize)
                .build();
    }

    public static <K, V> Cache<K, V> createCache(int maximumSize, Duration duration) {
        return Caffeine.newBuilder()
                .maximumSize(maximumSize)
                .expireAfterWrite(duration)
                .build();
    }

    public static <K, V> Map<K, V> createCacheAsMap(Duration duration) {
        Cache<K, V> cache = createCache(duration);
        return cache.asMap();
    }

    public static <K, V> Map<K, V> createCacheAsMap(int maximumSize) {
        Cache<K,V> cache = createCache(maximumSize);
        return cache.asMap();
    }

    public static <K, V> Map<K, V> createCacheAsMap(int maximumSize, Duration duration) {
        Cache<K, V> cache = createCache(maximumSize, duration);
        return cache.asMap();
    }
}
