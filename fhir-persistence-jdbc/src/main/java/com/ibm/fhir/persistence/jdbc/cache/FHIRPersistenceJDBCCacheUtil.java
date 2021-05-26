/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.cache;

import java.util.Map;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.FHIRPersistenceJDBCCache;
import com.ibm.fhir.persistence.jdbc.dao.api.ICommonTokenValuesCache;
import com.ibm.fhir.persistence.jdbc.dao.api.ParameterDAO;
import com.ibm.fhir.persistence.jdbc.dao.api.ResourceDAO;

/**
 * Utilities for operating on the FHIRPersistenceJDBCCache
 */
public class FHIRPersistenceJDBCCacheUtil {

    /**
     * Factory function to create a new cache instance
     * @return
     */
    public static FHIRPersistenceJDBCCache create(int codeSystemCacheSize, int tokenValueCacheSize, int canonicalCacheSize) {
        ICommonTokenValuesCache rrc = new CommonTokenValuesCacheImpl(codeSystemCacheSize, tokenValueCacheSize, canonicalCacheSize);
        return new FHIRPersistenceJDBCCacheImpl(new NameIdCache<Integer>(), new NameIdCache<Integer>(), rrc);

    }
    /**
     * Prefill the cache with constants already committed in the database
     * @param connection
     * @param cache
     */
    public static void prefill(ResourceDAO resourceDAO, ParameterDAO parameterDAO, FHIRPersistenceJDBCCache cache) throws FHIRPersistenceException {
        Map<String,Integer> resourceTypes = resourceDAO.readAllResourceTypeNames();
        cache.getResourceTypeCache().prefill(resourceTypes);

        Map<String,Integer> parameterNames = parameterDAO.readAllSearchParameterNames();
        cache.getParameterNameCache().prefill(parameterNames);

        Map<String,Integer> codeSystems = parameterDAO.readAllCodeSystems();
        cache.getResourceReferenceCache().prefillCodeSystems(codeSystems);
    }
}
