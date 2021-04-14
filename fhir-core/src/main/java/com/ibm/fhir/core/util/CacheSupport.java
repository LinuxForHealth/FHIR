/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.core.util;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.ibm.fhir.core.annotation.Cacheable;

public class CacheSupport {
    public static <K, V> Map<K, V> createCache(int maximumSize) {
        Cache<K, V> cache = Caffeine.newBuilder()
                .maximumSize(maximumSize)
                .build();
        return cache.asMap();
    }

    public static <K, V> Map<K, V> createCache(String name, int maximumSize) {
        Cache<K, V> cache = Caffeine.newBuilder()
                .maximumSize(maximumSize)
                .build();
        CacheManager.getInstance().addCache(name, cache);
        return cache.asMap();
    }

    public static <K, V> Map<K, V> createCache(int duration, TimeUnit unit) {
        Cache<K, V> cache = Caffeine.newBuilder()
                .expireAfterWrite(duration, unit)
                .build();
        return cache.asMap();
    }

    public static <K, V> Map<K, V> createCache(String name, int duration, TimeUnit unit) {
        Cache<K, V> cache = Caffeine.newBuilder()
                .expireAfterWrite(duration, unit)
                .build();
        CacheManager.getInstance().addCache(name, cache);
        return cache.asMap();
    }

    public static <K, V> Map<K, V> createCache(int maximumSize, int duration, TimeUnit unit) {
        Cache<K, V> cache = Caffeine.newBuilder()
                .maximumSize(maximumSize)
                .expireAfterWrite(duration, unit)
                .build();
        return cache.asMap();
    }

    public static <K, V> Map<K, V> createCache(String name, int maximumSize, int duration, TimeUnit unit) {
        Cache<K, V> cache = Caffeine.newBuilder()
                .maximumSize(maximumSize)
                .expireAfterWrite(duration, unit)
                .build();
        CacheManager.getInstance().addCache(name, cache);
        return cache.asMap();
    }

    public static <K, V> Map<K, V> createCache(Cacheable cacheable) {
        Objects.requireNonNull(cacheable, "cacheable");
        return createCache(cacheable.maximumSize(), cacheable.duration(), cacheable.unit());
    }
}
