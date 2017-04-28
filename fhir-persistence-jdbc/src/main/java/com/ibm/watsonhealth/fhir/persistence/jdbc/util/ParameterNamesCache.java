/**
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import com.ibm.watsonhealth.fhir.config.FHIRRequestContext;
import com.ibm.watsonhealth.fhir.persistence.jdbc.dao.api.ParameterNormalizedDAO;
import com.ibm.watsonhealth.fhir.persistence.jdbc.dto.Parameter;
import com.ibm.watsonhealth.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;
import com.ibm.watsonhealth.fhir.persistence.jdbc.exception.FHIRPersistenceDataAccessException;

/**
 * This class provides a static cache for FHIR Search Parameter names. This data is gathered from tables
 * defined as part of the "normalized" relational database schema.
 * @author markd
 *
 */

public class ParameterNamesCache {
	private static final String CLASSNAME = ParameterNamesCache.class.getName(); 
	private static final Logger log = Logger.getLogger(CLASSNAME);

	/**
	 * The following is a map of parameter name maps. Each FHIR tenant/datastore combination will have its own
	 * mapping of parameter-name to parameter-name-id.
	 */
	private static ConcurrentHashMap<String,ConcurrentHashMap<String,Integer>> parameterNameIdMaps = new ConcurrentHashMap<>();
	
	/**
	 * Initializes the Parameter name to id map for the current tenant and datastore.
	 * @param parameterDao A parameter data access object used to access the contents of the parameter names table.
	 * @throws FHIRPersistenceDBConnectException
	 * @throws FHIRPersistenceDataAccessException
	 */
	public static synchronized void initCache(ParameterNormalizedDAO parameterDao) 
										throws FHIRPersistenceDBConnectException, FHIRPersistenceDataAccessException {
		final String METHODNAME = "initCache";
		log.entering(CLASSNAME, METHODNAME);
		
		ConcurrentHashMap<String,Integer> currentDsMap;
		String tenantDatstoreCacheName;
		
		try {
			tenantDatstoreCacheName = getCacheNameForTenantDatastore();
			parameterNameIdMaps.putIfAbsent(tenantDatstoreCacheName, new ConcurrentHashMap<String,Integer>());
			currentDsMap = parameterNameIdMaps.get(tenantDatstoreCacheName);
			if (currentDsMap.isEmpty()) {
				currentDsMap.putAll(parameterDao.readAllSearchParameterNames());
			}
			log.fine("Initialized Parameter name/id cache for tenant datasore: " + tenantDatstoreCacheName);
		}
		finally {
			log.exiting(CLASSNAME, METHODNAME);
		}
	}
	
	/**
	 * Retrieves the id for the name contained in the passed Parameter, for the current tenant-datastore. 
	 * If not found, null is returned.
	 * @param parameterName A valid FHIR search parameter name.
	 * @param dao - A DAO used to access Search Parameter related data.
	 * @return Integer The id corresponding to the parameter name.
	 * @throws FHIRPersistenceDataAccessException 
	 * @throws FHIRPersistenceDBConnectException 
	 */
	public static Integer getParameterNameId(String parameterName, ParameterNormalizedDAO dao) 
					throws FHIRPersistenceDBConnectException, FHIRPersistenceDataAccessException {
		
		String tenantDatstoreCacheName = getCacheNameForTenantDatastore();
		ConcurrentHashMap<String,Integer> currentDsMap;
		Integer parameterNameId = null;
		
		parameterNameIdMaps.putIfAbsent(tenantDatstoreCacheName, new ConcurrentHashMap<String,Integer>());
		currentDsMap = parameterNameIdMaps.get(tenantDatstoreCacheName);
		parameterNameId = currentDsMap.get(parameterName);
		// If parameter name/id not found in datastore cache map, retrieve it from the database.
		if (parameterNameId == null) {
			parameterNameId = dao.readParameterNameId(parameterName);
			currentDsMap.putIfAbsent(parameterName, parameterNameId);
		}
		
		return parameterNameId;
		 
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
