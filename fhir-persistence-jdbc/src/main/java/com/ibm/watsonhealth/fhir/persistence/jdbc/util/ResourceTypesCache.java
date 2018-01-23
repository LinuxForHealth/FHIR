/**
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.watsonhealth.fhir.config.FHIRRequestContext;
import com.ibm.watsonhealth.fhir.persistence.jdbc.dao.api.ResourceNormalizedDAO;
import com.ibm.watsonhealth.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;
import com.ibm.watsonhealth.fhir.persistence.jdbc.exception.FHIRPersistenceDataAccessException;

/**
 * This class provides a static cache for FHIR Resource type names. This data is gathered from tables
 * defined as part of the "normalized" relational database schema.
 * @author markd
 *
 */

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
     * @param parameterName A valid FHIR search parameter name.
     * @return Integer The id corresponding to the parameter name.
     */
    public static Integer getResourceTypeId(String resourceType) {
        
        String tenantDatstoreCacheName = getCacheNameForTenantDatastore();
        ConcurrentHashMap<String,Integer> currentDsMap;
        Integer resourceTypeId = null;
        
        if (enabled) {
            resourceTypeIdMaps.putIfAbsent(tenantDatstoreCacheName, new ConcurrentHashMap<String,Integer>());
            currentDsMap = resourceTypeIdMaps.get(tenantDatstoreCacheName);
            resourceTypeId = currentDsMap.get(resourceType);
        }
        
        return resourceTypeId;
    }
	
	/**
	 * Returns a String containing a combination of the current tenantId and datastoreId.
	 * @return
	 */
	private static String getCacheNameForTenantDatastore() {
		
		StringBuilder cacheName = new StringBuilder();
		cacheName.append(FHIRRequestContext.get().getTenantId())
				 .append("~")
				 .append(FHIRRequestContext.get().getDataStoreId());
		return cacheName.toString();
	}

	/**
     * Adds the passed resource type name and id to the current tenant-datastore cache.
     * @param resourceType A valid resource type name.
     * @param resourceTypeId The id associated with the passed resource type name.
     */
    public static void putResourceTypeId(String resourceType, Integer resourceTypeId) {

        String tenantDatstoreCacheName = getCacheNameForTenantDatastore();
        ConcurrentHashMap<String,Integer> currentDsMap;
        
        if (enabled) {
            resourceTypeIdMaps.putIfAbsent(tenantDatstoreCacheName, new ConcurrentHashMap<String,Integer>());
            currentDsMap = resourceTypeIdMaps.get(tenantDatstoreCacheName);
            currentDsMap.putIfAbsent(resourceType, resourceTypeId);
        }
    }
    
    /**
     * Adds the passed resource type name/id pairs to the the current tenant-datastore cache.
     * @param newParameters A Map containing resource type name/id pairs.
     */
    public static void putResourceTypeIds(Map<String, Integer> newResourceTypes) {
        
        if (enabled) {
            for (Map.Entry<String, Integer> entry : newResourceTypes.entrySet()) {
                putResourceTypeId(entry.getKey(), entry.getValue());
            }
        }
    }
    
    /**
     * 
     * @return String - A formatted representation of the entire cache managed by this class.
     */
    public static String dumpCacheContents() {
        
        return CacheUtil.dumpCacheContents("ResourceTypesCache", resourceTypeIdMaps);
    }
    
    /**
     * Determines and reports any discrepancies between the current thread's Resource Type cache and the contents of the database RESOURCE_TYPES table.
     * @param dao A Resource DAO instance
     * @return String - A report detailing cache/db discrepancies.
     */
    public static String reportCacheDiscrepancies(ResourceNormalizedDAO dao) {
        
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
