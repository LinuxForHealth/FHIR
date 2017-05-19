/**
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc.util;

import java.util.concurrent.ConcurrentHashMap;
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
	public static synchronized void initCache(ResourceNormalizedDAO resourceDao) 
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
			log.fine("Initialized Resource type/id cache for tenant datasore: " + tenantDatstoreCacheName);
		}
		finally {
			log.exiting(CLASSNAME, METHODNAME);
		}
	}
	
	/**
	 * Retrieves the id for the name contained in the passed resource type, for the current tenant-datastore. 
	 * If not found, null is returned.
	 * @param parameterName A valid FHIR search parameter name.
	 * @param dao - A DAO used to access Resource type related data.
	 * @return Integer The id corresponding to the parameter name.
	 * @throws FHIRPersistenceDataAccessException 
	 * @throws FHIRPersistenceDBConnectException 
	 */
	public static Integer getResourceTypeId(String resourceType, ResourceNormalizedDAO dao) 
					throws FHIRPersistenceDBConnectException, FHIRPersistenceDataAccessException {
		
		String tenantDatstoreCacheName = getCacheNameForTenantDatastore();
		ConcurrentHashMap<String,Integer> currentDsMap;
		Integer resourceTypeId = null;
		
		resourceTypeIdMaps.putIfAbsent(tenantDatstoreCacheName, new ConcurrentHashMap<String,Integer>());
		currentDsMap = resourceTypeIdMaps.get(tenantDatstoreCacheName);
		resourceTypeId = currentDsMap.get(resourceType);
		// If resource type/id not found in datastore cache map, retrieve it from the database.
		if (resourceTypeId == null) {
			resourceTypeId = dao.readResourceTypeId(resourceType);
			currentDsMap.putIfAbsent(resourceType, resourceTypeId);
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

}
