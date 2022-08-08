/*
 * (C) Copyright IBM Corp. 2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.test.util;

import static org.testng.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.persistence.jdbc.util.CacheUtil;

/**
 * Tests the Cache
 */
public class CacheUtilTest {

    /**
     * Tests the reportCacheDiscrepancies method passing in equivalent maps.
     */
    @Test
    public void testReportCacheDiscrepencies1() {
        String cacheName = "testCache1";
        String report;
        ConcurrentHashMap<String, Integer> cachedMap = new ConcurrentHashMap<>();
        Map<String, Integer> dbMap = new HashMap<>();

        cachedMap.put("A", 1);
        cachedMap.put("B", 2);
        cachedMap.put("C", 3);
        cachedMap.put("D", 4);
        cachedMap.put("E", 5);

        dbMap.put("A", 1);
        dbMap.put("B", 2);
        dbMap.put("C", 3);
        dbMap.put("D", 4);
        dbMap.put("E", 5);

        report = CacheUtil.reportCacheDiscrepancies(cacheName, cachedMap, dbMap);
        assertNotNull(report);
    }

    /**
     * Tests the reportCacheDiscrepancies method passing in unequivalent maps.
     */
    @Test
    public void testReportCacheDiscrepencies2() {
        String cacheName = "testCache2";
        String report;
        ConcurrentHashMap<String, Integer> cachedMap = new ConcurrentHashMap<>();
        Map<String, Integer> dbMap = new HashMap<>();

        cachedMap.put("A", 1);
        cachedMap.put("B", 2);
        cachedMap.put("E", 5);

        dbMap.put("A", 1);
        dbMap.put("B", 2);
        dbMap.put("C", 3);
        dbMap.put("D", 4);
        dbMap.put("E", 5);

        report = CacheUtil.reportCacheDiscrepancies(cacheName, cachedMap, dbMap);
        assertNotNull(report);
    }

    /**
     * Tests the reportCacheDiscrepancies method passing in unequivalent maps.
     */
    @Test
    public void testReportCacheDiscrepencies3() {
        String cacheName = "testCache3";
        String report;
        ConcurrentHashMap<String, Integer> cachedMap = new ConcurrentHashMap<>();
        Map<String, Integer> dbMap = new HashMap<>();

        cachedMap.put("A", 1);
        cachedMap.put("B", 2);
        cachedMap.put("C", 3);
        cachedMap.put("D", 4);
        cachedMap.put("E", 5);

        dbMap.put("B", 2);
        dbMap.put("C", 3);
        dbMap.put("D", 4);
        dbMap.put("E", 5);

        report = CacheUtil.reportCacheDiscrepancies(cacheName, cachedMap, dbMap);
        assertNotNull(report);
    }

    /**
     * Tests the reportCacheDiscrepancies method passing in equivalent maps.
     */
    @Test
    public void testReportCacheDiscrepencies4() {
        String cacheName = "testCache4";
        String report;
        ConcurrentHashMap<String, Integer> cachedMap = new ConcurrentHashMap<>();
        Map<String, Integer> dbMap = new HashMap<>();

        cachedMap.put("A", 1);
        cachedMap.put("B", 2);
        cachedMap.put("C", 333);
        cachedMap.put("D", 4);
        cachedMap.put("E", 5);

        dbMap.put("A", 1);
        dbMap.put("B", 2);
        dbMap.put("C", 3);
        dbMap.put("D", 4);
        dbMap.put("E", 5);

        report = CacheUtil.reportCacheDiscrepancies(cacheName, cachedMap, dbMap);
        assertNotNull(report);
    }
}