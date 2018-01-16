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
 * This class provides a static cache for FHIR Search Parameter names. This data is gathered from tables
 * defined as part of the "normalized" relational database schema.
 * @author markd
 *
 */

public class ParameterNamesCache {
	private static final String CLASSNAME = ParameterNamesCache.class.getName(); 
	private static final Logger log = Logger.getLogger(CLASSNAME);
	
	private static boolean enabled = true;

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
	public static void initCacheIfEmpty(ParameterNormalizedDAO parameterDao) 
										throws FHIRPersistenceDBConnectException, FHIRPersistenceDataAccessException {
		final String METHODNAME = "initCache";
		log.entering(CLASSNAME, METHODNAME);
		
		ConcurrentHashMap<String,Integer> currentDsMap;
		String tenantDatstoreCacheName;
		
		if (enabled) { 
    		tenantDatstoreCacheName = getCacheNameForTenantDatastore();
    		parameterNameIdMaps.putIfAbsent(tenantDatstoreCacheName, new ConcurrentHashMap<String,Integer>());
    		currentDsMap = parameterNameIdMaps.get(tenantDatstoreCacheName);
    		if (currentDsMap.isEmpty()) {
    			currentDsMap.putAll(parameterDao.readAllSearchParameterNames());
    			if (log.isLoggable(Level.FINE)) {
    				log.fine("Initialized Parameter name/id cache for tenant datasore: " + tenantDatstoreCacheName);
    			}
    		}
		}
	}
	
	/**
	 * Retrieves the id for the name contained in the passed Parameter, for the current tenant-datastore. 
	 * If not found, null is returned.
	 * @param parameterName A valid FHIR search parameter name.
	 * @return Integer The id corresponding to the parameter name.
	 * @throws FHIRPersistenceDataAccessException 
	 * @throws FHIRPersistenceDBConnectException 
	 */
	public static Integer getParameterNameId(String parameterName) {
		
		String tenantDatstoreCacheName = getCacheNameForTenantDatastore();
		ConcurrentHashMap<String,Integer> currentDsMap;
		Integer parameterNameId = null;
		
		if (enabled) {
    		parameterNameIdMaps.putIfAbsent(tenantDatstoreCacheName, new ConcurrentHashMap<String,Integer>());
    		currentDsMap = parameterNameIdMaps.get(tenantDatstoreCacheName);
    		parameterNameId = currentDsMap.get(parameterName);
		}
						
		return parameterNameId;
		 
	}
	
	/**
	 * Adds the passed parameter name and id to the current tenant-datastore cache.
	 * @param parameterName A valid search parameter name.
	 * @param parameterId The id associated with the passed parameter name.
	 */
	public static void putParameterNameId(String parameterName, Integer parameterId) {

		String tenantDatstoreCacheName = getCacheNameForTenantDatastore();
		ConcurrentHashMap<String,Integer> currentDsMap;
		
		if (enabled) {
    		parameterNameIdMaps.putIfAbsent(tenantDatstoreCacheName, new ConcurrentHashMap<String,Integer>());
    		currentDsMap = parameterNameIdMaps.get(tenantDatstoreCacheName);
    		currentDsMap.putIfAbsent(parameterName, parameterId);
		}
	}
	
	/**
	 * Adds the passed search parameter name/id pairs to the the current tenant-datastore cache.
	 * @param newParameters A Map containing parameter name/id pairs.
	 */
	public static void putParameterNameIds(Map<String, Integer> newParameters) {
		
	    if (enabled) {
    		for (Map.Entry<String, Integer> entry : newParameters.entrySet()) {
    			 putParameterNameId(entry.getKey(), entry.getValue());
    		}
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
        
        return CacheUtil.dumpCacheContents("ParameterNamesCache", parameterNameIdMaps);
    }
    
    
    /**
     * Determines and reports any discrepancies between the current thread's Parameter Names cache and the contents of the database PARAMETER_NAMES table.
     * @param dao A Parameter DAO instance
     * @return String - A report detailing cache/db discrepancies.
     */
    public static String reportCacheDiscrepancies(ParameterNormalizedDAO dao) {
        
        String tenantDatstoreCacheName = getCacheNameForTenantDatastore();
        ConcurrentHashMap<String,Integer> cachedMap = parameterNameIdMaps.get(tenantDatstoreCacheName);
        Map<String, Integer> dbMap;
        String discrepancies = "";
        
        if (enabled) {
            try {
                dbMap = dao.readAllSearchParameterNames();
                discrepancies = CacheUtil.reportCacheDiscrepancies("ParameterNamesCache", cachedMap, dbMap);
            } 
            catch (FHIRPersistenceDBConnectException | FHIRPersistenceDataAccessException e) {
                log.log(Level.SEVERE, "Failure obtaining  all search parameter names." , e);
                discrepancies = CacheUtil.NEWLINE + "Could not report on ParameterNames cache discrepancies." + CacheUtil.NEWLINE;
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
                    parameterNameIdMaps.clear();
                }
            }
        }
    }

}
