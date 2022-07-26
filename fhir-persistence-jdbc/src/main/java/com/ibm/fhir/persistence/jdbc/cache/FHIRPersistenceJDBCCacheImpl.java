/*
 * (C) Copyright IBM Corp. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.cache;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import com.ibm.fhir.persistence.jdbc.FHIRPersistenceJDBCCache;
import com.ibm.fhir.persistence.jdbc.dao.api.ICommonValuesCache;
import com.ibm.fhir.persistence.jdbc.dao.api.IIdNameCache;
import com.ibm.fhir.persistence.jdbc.dao.api.ILogicalResourceIdentCache;
import com.ibm.fhir.persistence.jdbc.dao.api.INameIdCache;

/**
 * Aggregates and manages the individual caches used for a tenant
 */
public class FHIRPersistenceJDBCCacheImpl implements FHIRPersistenceJDBCCache {
    private static final Logger logger = Logger.getLogger(FHIRPersistenceJDBCCacheImpl.class.getName());

    private final INameIdCache<Integer> resourceTypeCache;

    private final IIdNameCache<Integer> resourceTypeNameCache;

    private final INameIdCache<Integer> parameterNameCache;

    private final ICommonValuesCache commonValuesCache;

    private final ILogicalResourceIdentCache logicalResourceIdentCache;

    // flag to allow one lucky caller to get the opportunity to prefill
    private final AtomicBoolean needToPrefillFlag = new AtomicBoolean(true);

    /**
     * Public constructor
     * @param resourceTypeCache
     * @param resourceTypeNameCache
     * @param parameterNameCache
     * @param commonValuesCache
     * @param logicalResourceIdentCache
     */
    public FHIRPersistenceJDBCCacheImpl(INameIdCache<Integer> resourceTypeCache, IIdNameCache<Integer> resourceTypeNameCache,
            INameIdCache<Integer> parameterNameCache, ICommonValuesCache commonValuesCache, ILogicalResourceIdentCache logicalResourceIdentCache) {
        this.resourceTypeCache = resourceTypeCache;
        this.resourceTypeNameCache = resourceTypeNameCache;
        this.parameterNameCache = parameterNameCache;
        this.commonValuesCache = commonValuesCache;
        this.logicalResourceIdentCache = logicalResourceIdentCache;
    }

    /**
     * @return the resourceReferenceCache
     */
    public ICommonValuesCache getCommonValuesCache() {
        return commonValuesCache;
    }

    /**
     * @return the resourceTypeCache
     */
    @Override
    public INameIdCache<Integer> getResourceTypeCache() {
        return this.resourceTypeCache;
    }

    /**
     * @return the resourceTypeNameCache
     */
    @Override
    public IIdNameCache<Integer> getResourceTypeNameCache() {
        return this.resourceTypeNameCache;
    }

    /**
     * @return the parameterNameCache
     */
    public INameIdCache<Integer> getParameterNameCache() {
        return parameterNameCache;
    }

    @Override
    public ILogicalResourceIdentCache getLogicalResourceIdentCache() {
        return logicalResourceIdentCache;
    }

    @Override
    public void transactionCommitted() {
        logger.fine("Transaction committed - updating cache shared maps");
        resourceTypeCache.updateSharedMaps();
        resourceTypeNameCache.updateSharedMaps();
        parameterNameCache.updateSharedMaps();
        commonValuesCache.updateSharedMaps();
        logicalResourceIdentCache.updateSharedMaps();
    }

    @Override
    public void transactionRolledBack() {
        logger.fine("Transaction rolled back - clearing local maps");
        resourceTypeCache.clearLocalMaps();
        resourceTypeNameCache.clearLocalMaps();
        parameterNameCache.clearLocalMaps();
        commonValuesCache.clearLocalMaps();
        logicalResourceIdentCache.clearLocalMaps();
    }

    @Override
    public boolean needToPrefill() {
        // To avoid a race condition at server startup, don't reset 
        // the flag until the cache has actually been filled
        return needToPrefillFlag.get();
    }
    
    @Override
    public void clearNeedToPrefill() {
        needToPrefillFlag.set(false);
    }
}