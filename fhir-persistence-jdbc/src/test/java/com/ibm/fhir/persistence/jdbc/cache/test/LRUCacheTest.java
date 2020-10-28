/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.cache.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import java.util.LinkedHashMap;

import org.testng.annotations.Test;

import com.ibm.fhir.persistence.jdbc.cache.LRUCache;

/**
 * Unit tests for {@link LRUCache}
 */
public class LRUCacheTest {

    @Test
    public void testCache() {
        // cache with an LRU size of 2
        LRUCache<String,Long> cache = new LRUCache<>(2);
        cache.put("system1", 1L);
        cache.put("system2", 2L);
        
        assertEquals(1L, (long)cache.get("system1"));
        assertEquals(2L, (long)cache.get("system2"));
        
        LinkedHashMap<String,Long> updates = cache.newUpdateMap();
        updates.put("system1", 1L);
        updates.put("system2", 2L);
        
        cache.update(updates);
        updates.clear();

        // Check we still have stuff in the cache
        assertEquals(1L, (long)cache.get("system1"));
        assertEquals(2L, (long)cache.get("system2"));

        // Add a new value to the updates. Should push out system1
        updates.put("system3", 3L);
        cache.update(updates);
        updates.clear();
        assertEquals(2L, (long)cache.get("system2"));
        assertEquals(3L, (long)cache.get("system3"));
        assertNull(cache.get("system1"));
        
        // put back system1, should lose system2
        updates.put("system1", 1L);
        cache.update(updates);
        assertEquals(1L, (long)cache.get("system1"));
        assertEquals(3L, (long)cache.get("system3"));
        assertNull(cache.get("system2"));
    }
}