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
	public static void initCacheIfEmpty(ParameterNormalizedDAO parameterDao) 
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
				if (log.isLoggable(Level.FINE)) {
					log.fine("Initialized Code System name/id cache for tenant datasore: " + tenantDatstoreCacheName);
				}
			}
		}
		finally {
			log.exiting(CLASSNAME, METHODNAME);
		}
	}
	
	/**
	 * Retrieves the id for the passed system, for the current tenant-datastore. 
	 * If not found, null is returned.
	 * @param parameter The name of a code system
	 * @return Integer The id corresponding to the passed code system
	 */
	public static Integer getCodeSystemId(String systemName) {
		
		String tenantDatstoreCacheName = getCacheNameForTenantDatastore();
		ConcurrentHashMap<String,Integer> currentDsMap;
		Integer systemId = null;
		String encodedSysName = SQLParameterEncoder.encode(systemName);
		
		codeSystemIdMaps.putIfAbsent(tenantDatstoreCacheName, new ConcurrentHashMap<String,Integer>());
		currentDsMap = codeSystemIdMaps.get(tenantDatstoreCacheName);
		systemId = currentDsMap.get(encodedSysName);
		
		return systemId;
		 
	}
	
	/**
	 * Adds the passed code system name and id to the current tenant-datastore cache.
	 * @param systemName A valid code system name.
	 * @param systemId The id associated with the passed code system name.
	 */
	public static void putCodeSystemId(String systemName, Integer systemId) {
		
		String tenantDatstoreCacheName = getCacheNameForTenantDatastore();
		ConcurrentHashMap<String,Integer> currentDsMap;
		String encodedSysName = SQLParameterEncoder.encode(systemName);
		
		codeSystemIdMaps.putIfAbsent(tenantDatstoreCacheName, new ConcurrentHashMap<String,Integer>());
		currentDsMap = codeSystemIdMaps.get(tenantDatstoreCacheName);
		currentDsMap.putIfAbsent(encodedSysName, systemId);
		systemId = currentDsMap.get(encodedSysName);
	}
	
	/**
	 * Adds the passed code system name/id pairs to the the current tenant-datastore cache.
	 * @param newParameters A Map containing code system name/id pairs.
	 */
	public static void putCodeSystemIds(Map<String, Integer> newCodeSystems) {
		
		for (Map.Entry<String, Integer> entry : newCodeSystems.entrySet()) {
			 putCodeSystemId(entry.getKey(), entry.getValue());
		}
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
    public static String reportCacheDiscrepancies(ParameterNormalizedDAO dao) {
        
        String tenantDatstoreCacheName = getCacheNameForTenantDatastore();
        Map<String, Integer> dbMap;
        ConcurrentHashMap<String,Integer> cachedMap = codeSystemIdMaps.get(tenantDatstoreCacheName);
        String discrepancies;
        
        try {
            dbMap = dao.readAllCodeSystems();
            discrepancies = CacheUtil.reportCacheDiscrepancies("CodeSystemsCache", cachedMap, dbMap);
        } catch (FHIRPersistenceDBConnectException | FHIRPersistenceDataAccessException e) {
            log.log(Level.SEVERE, "Failure obtaining  all code systems." , e);
            discrepancies = CacheUtil.NEWLINE + "Could not report on CodeSystems cache discrepancies." + CacheUtil.NEWLINE;
        }
        
        return discrepancies;
    }
}
