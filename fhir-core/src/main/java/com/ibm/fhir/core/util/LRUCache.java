/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.core.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A simple Least Recently Used (LRU) cache implementation using LinkedHashMap
 *
 * see {@link java.util.LinkedHashMap#removeEldestEntry}
 */
@Deprecated
public class LRUCache<K, V> extends LinkedHashMap<K, V> {
    private static final long serialVersionUID = 1L;

    private final int maxEntries;

    private LRUCache(int maxEntries) {
        super(maxEntries, 0.75f, true);
        this.maxEntries = maxEntries;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > maxEntries;
    }

    public static <K, V> Map<K, V> createLRUCache(int maxEntries) {
        return CacheSupport.createCache(maxEntries);
    }
}