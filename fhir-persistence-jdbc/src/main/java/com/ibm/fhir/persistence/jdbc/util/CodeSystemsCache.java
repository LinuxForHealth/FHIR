/*
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.persistence.jdbc.dao.api.ParameterDAO;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDataAccessException;

/**
 * This class provides a static cache for FHIR Systems that are part of Token type Search parameters.
 */
@Deprecated
public class CodeSystemsCache {
    private static final String CLASSNAME = CodeSystemsCache.class.getName();
    private static final Logger log = Logger.getLogger(CLASSNAME);

    private static boolean enabled = true;

    /**
     * The following is a map of parameter name maps. Each FHIR tenant/datastore combination will have its own
     * mapping of system-name to system-id.
     */
    private static ConcurrentHashMap<String,ConcurrentHashMap<String,Integer>> codeSystemIdMaps = new ConcurrentHashMap<>();

    /**
     * Retrieves the id for the passed system, for the current tenant-datastore.
     * If not found, null is returned.
     * @param parameter The name of a code system
     * @return Integer The id corresponding to the passed code system
     */
    public static Integer getCodeSystemId(String systemName) {

        String tenantDatastoreCacheName = getCacheNameForTenantDatastore();
        Integer systemId = null;
        String encodedSysName = SqlParameterEncoder.encode(systemName);

        if (enabled) {
            ConcurrentHashMap<String,Integer> currentDsMap = codeSystemIdMaps.putIfAbsent(tenantDatastoreCacheName, new ConcurrentHashMap<String,Integer>());
            if (currentDsMap == null) {
                log.fine("getCodeSystemId() - Added new cache map for tennantDatastore=" + tenantDatastoreCacheName);
            }
            currentDsMap = codeSystemIdMaps.get(tenantDatastoreCacheName);
            systemId = currentDsMap.get(encodedSysName);
        }
        return systemId;
    }

    /**
     * Adds the passed code system name and id to the current tenant-datastore cache.
     * @param tenantDatastoreCacheName The name of the datastore-specific cache the entry should be added to.
     * @param systemName A valid code system name.
     * @param systemId The id associated with the passed code system name.
     */
    public static void putCodeSystemId(String tenantDatastoreCacheName, String systemName, Integer systemId) {

        ConcurrentHashMap<String,Integer> currentDsMap;
        Integer tempValue;
        String encodedSysName = SqlParameterEncoder.encode(systemName);

        if (enabled) {
            currentDsMap = codeSystemIdMaps.putIfAbsent(tenantDatastoreCacheName, new ConcurrentHashMap<String,Integer>());
            if (currentDsMap == null) {
                log.fine("putCodeSystemId() - Added new cache map for tennantDatastore=" + tenantDatastoreCacheName);
            }
            currentDsMap = codeSystemIdMaps.get(tenantDatastoreCacheName);
            tempValue = currentDsMap.putIfAbsent(encodedSysName, systemId);
            if (tempValue == null) {
                log.fine("putCodeSystemId() - Added new cache entry, key=" + encodedSysName + "  value=" + systemId + "  tenantDatstoreCacheName=" + tenantDatastoreCacheName);
            }
        }
    }

    /**
     * Adds the passed code system name/id pairs to the the current tenant-datastore cache.
     * @param tenantDatastoreCacheName The name of the datastore-specific cache the entry should be added to.
     * @param newParameters A Map containing code system name/id pairs.
     */
    public static void putCodeSystemIds(String tenantDatastoreCacheName, Map<String, Integer> newCodeSystems) {

        if (enabled) {
            for (Map.Entry<String, Integer> entry : newCodeSystems.entrySet()) {
                 putCodeSystemId(tenantDatastoreCacheName, entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * Returns a String containing a combination of the current tenantId and datastoreId.
     * @return
     */
    public static String getCacheNameForTenantDatastore() {

        StringBuilder cacheName = new StringBuilder();
        cacheName.append(FHIRRequestContext.get().getTenantId())
                 .append("~")
                 .append(FHIRRequestContext.get().getDataStoreId());
        return cacheName.toString();
    }

    /**
     *
     * @return String - A formatted representation of the entire cache managed by this class.
     */
    public static String dumpCacheContents() {

        return CacheUtil.dumpCacheContents("CodeSystemsCache", codeSystemIdMaps);
    }

    /**
     * Determines and reports any discrepancies between the current thread's Code Systems cache and the contents of the database CODE_SYSTEMS table.
     * @param dao A Parameter DAO instance
     * @return String - A report detailing cache/db discrepancies.
     */
    public static String reportCacheDiscrepancies(ParameterDAO dao) {

        String tenantDatstoreCacheName = getCacheNameForTenantDatastore();
        Map<String, Integer> dbMap;
        ConcurrentHashMap<String,Integer> cachedMap = codeSystemIdMaps.get(tenantDatstoreCacheName);
        String discrepancies = "";

        if (enabled) {
            try {
                dbMap = dao.readAllCodeSystems();
                discrepancies = CacheUtil.reportCacheDiscrepancies("CodeSystemsCache", cachedMap, dbMap);
            }
            catch (FHIRPersistenceDBConnectException | FHIRPersistenceDataAccessException e) {
                log.log(Level.SEVERE, "Failure obtaining  all code systems." , e);
                discrepancies = CacheUtil.NEWLINE + "Could not report on CodeSystems cache discrepancies." + CacheUtil.NEWLINE;
            }
        }

        return discrepancies;
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static void setEnabled(boolean newEnabled) {

        if (newEnabled != enabled) {
            synchronized(CodeSystemsCache.class) {
                enabled = newEnabled;
                // When enabling the cache, clear out any old stuff.
                if (newEnabled) {
                    codeSystemIdMaps.clear();
                }
            }
        }
    }
}
