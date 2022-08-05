/*
 * (C) Copyright IBM Corp. 2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.util;

import java.util.Enumeration;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Encapsulates a group of utility methods for managing and diagnosing the in-memory caches used by the JDBC PL.
 */
public class CacheUtil {
    
    protected static final String NEWLINE = System.getProperty("line.separator");
    
    /**
     * Takes the contents of one of the JDBC PL caches and represents the contents in a format suitable for logging.
     * @param cacheName - The name of the JDBC PL cache to be dumped.
     * @param mapOfMaps - The contents of the multi-datastore cache.
     * @return String - A formatted representation of the cache contents.
     */
    public static String dumpCacheContents(String cacheName, ConcurrentHashMap<String,ConcurrentHashMap<String,Integer>> mapOfMaps) {
        
        String cacheKey;
        Enumeration<String> cacheKeys = mapOfMaps.keys();
        ConcurrentHashMap<String,Integer> dbCache;
        StringBuffer dumpedCache = new StringBuffer();
        
        dumpedCache.append(NEWLINE).append("Contents of ").append(cacheName).append(NEWLINE);
        while(cacheKeys.hasMoreElements()) {
            cacheKey = cacheKeys.nextElement();
            dbCache = mapOfMaps.get(cacheKey);
            dumpedCache.append(cacheName).append(" for datastoreid: " + cacheKey).append(NEWLINE);
            dumpedCache.append(dbCache.toString().replaceAll(",", NEWLINE)).append(NEWLINE);
        }
        
        return dumpedCache.toString();
    }
    
    /**
     * Reports on content discrepancies a JDBC PL cache and DB data that backs it.
     * @param cacheName - The name of the JDBC PL cache to be examined.
     * @param cachedMap - The contents of the cache for a single tenantid/datastoreid combination.
     * @param dbMap - The contents of the DB table that back the passed cachedMap.
     * @return String - A report of the discrepancies between the passed maps.
     */
    public static String reportCacheDiscrepancies(String cacheName, ConcurrentHashMap<String,Integer> cachedMap, Map<String, Integer> dbMap) {
        
        StringBuilder report = new StringBuilder();
        int dbMapCount, cachedMapCount;
        Map<String,Integer> masterMap, slaveMap;
        Integer masterMapValue, slaveMapValue;
        String masterMapName, slaveMapName;
        boolean discrepanciesFound = false;
                
        report.append(NEWLINE).append("Discrepancy Report for: ").append(cacheName).append(NEWLINE);
        dbMapCount = dbMap.size();
        cachedMapCount = cachedMap.size();
        if (dbMapCount != cachedMapCount) {
            report.append("Size discrepancy found. dbMapCount = ").append(dbMapCount)
                    .append("  cachedMapCount=" + cachedMapCount).append(NEWLINE); 
        }
        if (dbMapCount >= cachedMapCount) {
            masterMap = dbMap;
            masterMapName = "dbMap";
            slaveMapName = "cahcedMap";
            slaveMap = cachedMap;
        }
        else {
            masterMap = cachedMap;
            masterMapName = "cachedMap";
            slaveMap = dbMap;
            slaveMapName = "dbMap";
        }
        for (String masterMapKey : masterMap.keySet()) {
            masterMapValue = masterMap.get(masterMapKey);
            slaveMapValue = slaveMap.get(masterMapKey);
            if (!Objects.equals(masterMapValue, slaveMapValue)) {
                report.append("Map value mismatch: ").append("key=").append(masterMapKey).append("  ")
                        .append(masterMapName).append(".value=").append(masterMapValue).append("  ")
                        .append(slaveMapName).append(".value=").append(slaveMapValue).append(NEWLINE);
                discrepanciesFound = true;
            }
        }
        if (!discrepanciesFound) {
            report.append("No Discrepancies Found.").append(NEWLINE);
        }
        
        return report.toString();
    }

}
