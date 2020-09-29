/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.impl;

import static com.ibm.fhir.config.FHIRConfiguration.PROPERTY_JDBC_ENABLE_PARAMETER_NAMES_CACHE;


import com.ibm.fhir.persistence.jdbc.FHIRPersistenceJDBCCache;
import com.ibm.fhir.persistence.jdbc.dao.api.ICommonTokenValuesCache;

/**
 * Aggregates the individual caches used for a tenant
 */
public class FHIRPersistenceJDBCCacheImpl implements FHIRPersistenceJDBCCache {
    
    private final ICommonTokenValuesCache resourceReferenceCache;
    
    /**
     * Public constructor
     * @param resourceReferenceCache
     */
    public FHIRPersistenceJDBCCacheImpl(ICommonTokenValuesCache resourceReferenceCache) {
        this.resourceReferenceCache = resourceReferenceCache;
    }

    /**
     * @return the resourceReferenceCache
     */
    public ICommonTokenValuesCache getResourceReferenceCache() {
        return resourceReferenceCache;
    }
    
}
