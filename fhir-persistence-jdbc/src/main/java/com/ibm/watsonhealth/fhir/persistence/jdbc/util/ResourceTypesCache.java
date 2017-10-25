/**
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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

	/**
	 * The following is a map of resource type maps. Each FHIR tenant/datastore combination will have its own
	 * mapping of resource-type to resource-type-id.
	 */
	private static ConcurrentHashMap<String,ConcurrentHashMap<String,Integer>> resourceTypeIdMaps = new ConcurrentHashMap<>();
	
	/**
	 * Initializes the Resource type to id map for the current tenant and datastore.
	 * @param resourceDao A resource data access object used to access the contents of the resource_types table.
	 * @throws FHIRPersistenceDBConnectException
	 * @throws FHIRPersistenceDataAccessException
	 */
	public static synchronized void initCacheIfEmpty(ResourceNormalizedDAO resourceDao) 
										throws FHIRPersistenceDBConnectException, FHIRPersistenceDataAccessException {
		final String METHODNAME = "initCache";
		log.entering(CLASSNAME, METHODNAME);
		
		ConcurrentHashMap<String,Integer> currentDsMap;
		String tenantDatstoreCacheName;
		
		try {
			tenantDatstoreCacheName = getCacheNameForTenantDatastore();
			resourceTypeIdMaps.putIfAbsent(tenantDatstoreCacheName, new ConcurrentHashMap<String,Integer>());
			currentDsMap = resourceTypeIdMaps.get(tenantDatstoreCacheName);
			if (currentDsMap.isEmpty()) {
				currentDsMap.putAll(resourceDao.readAllResourceTypeNames());
			}
			if (log.isLoggable(Level.FINE)) {
				log.fine("Initialized Resource type/id cache for tenant datasore: " + tenantDatstoreCacheName);
			}
		}
		finally {
			log.exiting(CLASSNAME, METHODNAME);
		}
	}
	
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
        
        resourceTypeIdMaps.putIfAbsent(tenantDatstoreCacheName, new ConcurrentHashMap<String,Integer>());
        currentDsMap = resourceTypeIdMaps.get(tenantDatstoreCacheName);
        resourceTypeId = currentDsMap.get(resourceType);
        
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
	 * Returns all cached resource type ids for the current tenant/datastore.
	 * NOTE: The cache must be initialized prior to calling this method.
	 * @return Collection<Integer> - All cached resource type ids for the current tenant/datastore.
	 */
	public static Collection<Integer> getAllResourceTypeIds() {
		 
		String tenantDatstoreCacheName = getCacheNameForTenantDatastore();
		ConcurrentHashMap<String,Integer> currentDsMap;
		Collection<Integer> resourceTypeIds = new ArrayList<>();
		
		currentDsMap = resourceTypeIdMaps.get(tenantDatstoreCacheName);
		if (currentDsMap != null) {
			resourceTypeIds = currentDsMap.values();
		}
		 
		return resourceTypeIds;
	}

	/**
	 * Returns the resource type name that corresponds to the passed resource type id.
	 * @param resourceTypeId - A valid unique resource type id.
	 * @return String - Resource type name.
	 */
	public static String getResourceTypeName(Integer resourceTypeId) {
		 
		String tenantDatstoreCacheName = getCacheNameForTenantDatastore();
		ConcurrentHashMap<String,Integer> currentDsMap;
		String resourceTypeName = null;
		Map<Integer, String> inversedDsMap;
		
		currentDsMap = resourceTypeIdMaps.get(tenantDatstoreCacheName);
		if (currentDsMap != null) {
			// This map inversion should not be costly from a performance perspective, since there will be less than 100 resource types.
			inversedDsMap = currentDsMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
			resourceTypeName = inversedDsMap.get(resourceTypeId);
		}
		return resourceTypeName;
	}
	
	   /**
     * Adds the passed resource type name and id to the current tenant-datastore cache.
     * @param resourceType A valid resource type name.
     * @param resourceTypeId The id associated with the passed resource type name.
     */
    public static void putResourceTypeId(String resourceType, Integer resourceTypeId) {

        String tenantDatstoreCacheName = getCacheNameForTenantDatastore();
        ConcurrentHashMap<String,Integer> currentDsMap;
        
        resourceTypeIdMaps.putIfAbsent(tenantDatstoreCacheName, new ConcurrentHashMap<String,Integer>());
        currentDsMap = resourceTypeIdMaps.get(tenantDatstoreCacheName);
        currentDsMap.putIfAbsent(resourceType, resourceTypeId);
    }
    
    /**
     * Adds the passed resource type name/id pairs to the the current tenant-datastore cache.
     * @param newParameters A Map containing resource type name/id pairs.
     */
    public static void putResourceTypeIds(Map<String, Integer> newResourceTypes) {
        
        for (Map.Entry<String, Integer> entry : newResourceTypes.entrySet()) {
            putResourceTypeId(entry.getKey(), entry.getValue());
        }
            
    }

}
