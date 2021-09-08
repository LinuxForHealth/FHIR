/*
 * (C) Copyright IBM Corp. 2017, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.persistence.jdbc.dao.api.ResourceDAO;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDataAccessException;

/**
 * This class provides a static cache for FHIR Resource type names.
 */
@Deprecated
public class ResourceTypesCache {
    private static final String CLASSNAME = ResourceTypesCache.class.getName();
    private static final Logger log = Logger.getLogger(CLASSNAME);

    private static boolean enabled = true;

    /**
     * The following is a map of resource type maps. Each FHIR tenant/datastore combination will have its own
     * mapping of resource-type to resource-type-id.
     */
    private static ConcurrentHashMap<String,ConcurrentHashMap<String,Integer>> resourceTypeIdMaps = new ConcurrentHashMap<>();

    /**
     * Retrieves the id for the name contained in the passed resource type, for the current tenant-datastore.
     * If not found, null is returned.
     * @param resourceType A valid FHIR search parameter name.
     * @return Integer The id corresponding to the parameter name.
     */
    public static Integer getResourceTypeId(String resourceType) {

        String tenantDatstoreCacheName = getCacheNameForTenantDatastore();
        ConcurrentHashMap<String,Integer> currentDsMap;
        Integer resourceTypeId = null;

        if (enabled) {
            currentDsMap = resourceTypeIdMaps.putIfAbsent(tenantDatstoreCacheName, new ConcurrentHashMap<String,Integer>());
            if (currentDsMap == null) {
                log.fine("getResourceTypeId() - Added new cache map for tennantDatastore=" + tenantDatstoreCacheName);
            }
            currentDsMap = resourceTypeIdMaps.get(tenantDatstoreCacheName);
            resourceTypeId = currentDsMap.get(resourceType);
        }

        return resourceTypeId;
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
     * Adds the passed resource type name and id to the current tenant-datastore cache.
     *
     * @param tenantDatastoreCacheName The name of the datastore-specific cache the entry should be added to.
     * @param resourceType A valid resource type name.
     * @param resourceTypeId The id associated with the passed resource type name.
     */
    public static void putResourceTypeId(String tenantDatastoreCacheName, String resourceType, Integer resourceTypeId) {

        ConcurrentHashMap<String,Integer> currentDsMap;
        Integer tempValue;

        if (enabled) {
            currentDsMap = resourceTypeIdMaps.putIfAbsent(tenantDatastoreCacheName, new ConcurrentHashMap<String,Integer>());
            if (currentDsMap == null) {
                log.fine("putResourceTypeId() - Added new cache map for tennantDatastore=" + tenantDatastoreCacheName);
            }
            currentDsMap = resourceTypeIdMaps.get(tenantDatastoreCacheName);
            tempValue = currentDsMap.putIfAbsent(resourceType, resourceTypeId);
            if (tempValue == null) {
                log.fine("putResourceTypeId() - Added new cache entry, key=" + resourceType + "  value=" + resourceTypeId + "  tenantDatastoreCacheName=" + tenantDatastoreCacheName);
            }
        }
    }

    /**
     * Adds the passed resource type name/id pairs to the the current tenant-datastore cache.
     * @param tenantDatastoreCacheName The name of the datastore-specific cache the entry should be added to.
     * @param newResourceTypes A Map containing resource type name/id pairs.
     */
    public static void putResourceTypeIds(String tenantDatastoreCacheName, Map<String, Integer> newResourceTypes) {

        if (enabled) {
            for (Map.Entry<String, Integer> entry : newResourceTypes.entrySet()) {
                putResourceTypeId(tenantDatastoreCacheName, entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     *
     * @return A formatted representation of the entire cache managed by this class.
     */
    public static String dumpCacheContents() {

        return CacheUtil.dumpCacheContents("ResourceTypesCache", resourceTypeIdMaps);
    }

    /**
     * Determines and reports any discrepancies between the current thread's Resource Type cache and the contents of the database RESOURCE_TYPES table.
     * @param dao A Resource DAO instance
     * @return A report detailing cache/db discrepancies.
     */
    public static String reportCacheDiscrepancies(ResourceDAO dao) {

        String tenantDatstoreCacheName = getCacheNameForTenantDatastore();
        Map<String, Integer> dbMap;
        ConcurrentHashMap<String,Integer> cachedMap = resourceTypeIdMaps.get(tenantDatstoreCacheName);
        String discrepancies = "";

        if (enabled) {
            try {
                dbMap = dao.readAllResourceTypeNames();
                discrepancies = CacheUtil.reportCacheDiscrepancies("ResourceTypesCache", cachedMap, dbMap);
            }
            catch (FHIRPersistenceDBConnectException | FHIRPersistenceDataAccessException e) {
                log.log(Level.SEVERE, "Failure obtaining  all resource type names." , e);
                discrepancies = CacheUtil.NEWLINE + "Could not report on ResourceTypes cache discrepancies." + CacheUtil.NEWLINE;
            }
        }

        return discrepancies;
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static void setEnabled(boolean enabled) {
        ResourceTypesCache.enabled = enabled;
    }

}
