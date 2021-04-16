/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.cache.test;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import com.ibm.fhir.cache.configuration.Configuration;
import com.ibm.fhir.cache.manager.FHIRCacheManager;

public class FHIRCacheManagerTest {
    @Test
    public void testTimeBasedEvictionCache() throws InterruptedException {
        Cache<String, Integer> cache = FHIRCacheManager.getCache("testCache", Configuration.of(Duration.of(1000, ChronoUnit.MILLIS)));
        Assert.assertNotNull(cache);

        cache.asMap().put("1", 1);
        Thread.sleep(1000);
        cache.cleanUp();
        Assert.assertTrue(cache.asMap().isEmpty());

        FHIRCacheManager.removeCache("testCache");
        Assert.assertNull(FHIRCacheManager.getCache("testCache"));
    }

    @Test
    public void testSizeBasedEvictionCache() throws InterruptedException {
        Cache<String, Integer> cache = FHIRCacheManager.getCache("testCache", Configuration.of(1));
        Assert.assertNotNull(cache);

        cache.asMap().put("1", 1);
        cache.asMap().put("2", 2);
        cache.cleanUp();
        Assert.assertEquals(cache.asMap().size(), 1);
        Assert.assertNull(cache.asMap().get("1"));
        Assert.assertEquals(cache.asMap().get("2"), Integer.valueOf(2));

        FHIRCacheManager.removeCache("testCache");
        Assert.assertNull(FHIRCacheManager.getCache("testCache"));
    }

    @Test
    public void testCacheStats() {
        Cache<String, Integer> cache = FHIRCacheManager.getCache("testCache", Configuration.of(128));
        Assert.assertNotNull(cache);
        Assert.assertTrue(cache.policy().isRecordingStats());

        CacheStats cacheStats = cache.stats();
        Assert.assertEquals(cacheStats, CacheStats.empty());

        cache.asMap().computeIfAbsent("1", k -> 1);
        cacheStats = cache.stats();
        Assert.assertEquals(cacheStats.hitCount(), 0);
        Assert.assertEquals(cacheStats.missCount(), 1);

        cache.asMap().computeIfAbsent("1", k -> 1);
        cacheStats = cache.stats();
        Assert.assertEquals(cacheStats.hitCount(), 1);
        Assert.assertEquals(cacheStats.missCount(), 1);

        FHIRCacheManager.removeCache("testCache");
        Assert.assertNull(FHIRCacheManager.getCache("testCache"));
    }
}
