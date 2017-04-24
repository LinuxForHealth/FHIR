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
import com.ibm.watsonhealth.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;
import com.ibm.watsonhealth.fhir.persistence.jdbc.exception.FHIRPersistenceDataAccessException;

/**
 * This class provides a static cache for FHIR Systems that are part of Token type Search parameters. This data is gathered from tables
 * defined as part of the "normalized" relational database schema.
 * @author markd
 *
 */

public class CodeSystemsCache {
	private static final String CLASSNAME = CodeSystemsCache.class.getName(); 
	private static final Logger log = Logger.getLogger(CLASSNAME);

	/**
	 * The following is a map of parameter name maps. Each FHIR tenant/datastore combination will have its own
	 * mapping of system-name to system-id.
	 */
	private static ConcurrentHashMap<String,ConcurrentHashMap<String,Integer>> codeSystemIdMaps = new ConcurrentHashMap<>();
	
	/**
	 * Initializes the code system to id map for the current tenant and datastore.
	 * @param parameterDao A parameter data access object used to access the contents of the code systems table.
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
			codeSystemIdMaps.putIfAbsent(tenantDatstoreCacheName, new ConcurrentHashMap<String,Integer>());
			currentDsMap = codeSystemIdMaps.get(tenantDatstoreCacheName);
			if (currentDsMap.isEmpty()) {
				currentDsMap.putAll(parameterDao.readAllCodeSystems());
			}
			log.fine("Initialized Code System name/id cache for tenant datasore: " + tenantDatstoreCacheName);
		}
		finally {
			log.exiting(CLASSNAME, METHODNAME);
		}
	}
	
	/**
	 * Retrieves the id for the passed system, for the current tenant-datastore. 
	 * If not found, null is returned.
	 * @param parameter The name of a code system
	 * @param dao - A DAO used to access Search Parameter related data.
	 * @return Integer The id corresponding to the passed code system
	 * @throws FHIRPersistenceDataAccessException 
	 * @throws FHIRPersistenceDBConnectException 
	 */
	public static Integer getCodeSystemId(String systemName, ParameterNormalizedDAO dao) throws FHIRPersistenceDBConnectException, FHIRPersistenceDataAccessException {
		
		String tenantDatstoreCacheName = getCacheNameForTenantDatastore();
		ConcurrentHashMap<String,Integer> currentDsMap;
		Integer systemId = null;
		
		codeSystemIdMaps.putIfAbsent(tenantDatstoreCacheName, new ConcurrentHashMap<String,Integer>());
		currentDsMap = codeSystemIdMaps.get(tenantDatstoreCacheName);
		systemId = currentDsMap.get(systemName);
		// If code system name/id not found in datastore cache map, retrieve it from the database.
		if (systemId == null) {
			systemId = dao.readCodeSystemId(systemName);
			currentDsMap.putIfAbsent(systemName, systemId);
		}
		return systemId;
		 
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
