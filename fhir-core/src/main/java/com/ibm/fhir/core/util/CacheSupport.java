/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.core.util;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

public final class CacheSupport {
    private CacheSupport() { }

    public static <K, V> Map<K, V> createCache(long maximumSize) {
        Cache<K, V> cache = Caffeine.newBuilder()
                .maximumSize(maximumSize)
                .build();
        return cache.asMap();
    }

    public static <K, V> Map<K, V> createCache(long duration, TimeUnit unit) {
        Cache<K, V> cache = Caffeine.newBuilder()
                .expireAfterWrite(duration, unit)
                .build();
        return cache.asMap();
    }

    public static <K, V> Map<K, V> createCache(long maximumSize, long duration, TimeUnit unit) {
        Cache<K, V> cache = Caffeine.newBuilder()
                .maximumSize(maximumSize)
                .expireAfterWrite(duration, unit)
                .build();
        return cache.asMap();
    }
}
