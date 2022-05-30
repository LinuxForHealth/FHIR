/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.remote.index.cache;

import java.time.Duration;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.ibm.fhir.remote.index.api.IdentityCache;
import com.ibm.fhir.remote.index.database.CommonCanonicalValueKey;
import com.ibm.fhir.remote.index.database.CommonTokenValueKey;
import com.ibm.fhir.remote.index.database.ResourceTypeValue;

/**
 * Implementation of a cache we use to reduce the number of databases accesses
 * required to find the id for a given object key
 */
public class IdentityCacheImpl implements IdentityCache {
    private final ConcurrentHashMap<String, Integer> parameterNames = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Integer> resourceTypes = new ConcurrentHashMap<>();
    private final Cache<String, Integer> codeSystemCache;
    private final Cache<CommonTokenValueKey, Long> commonTokenValueCache;
    private final Cache<CommonCanonicalValueKey, Long> commonCanonicalValueCache;
    private static final Integer NULL_INT = null;
    private static final Long NULL_LONG = null;

    /**
     * Public constructor
     */
    public IdentityCacheImpl(int maxCodeSystemCacheSize, Duration codeSystemCacheDuration,
        long maxCommonTokenCacheSize, Duration commonTokenCacheDuration,
        long maxCommonCanonicalCacheSize, Duration commonCanonicalCacheDuration) {
        codeSystemCache = Caffeine.newBuilder()
                .maximumSize(maxCodeSystemCacheSize)
                .expireAfterWrite(codeSystemCacheDuration)
                .build();
        commonTokenValueCache = Caffeine.newBuilder()
                .maximumSize(maxCommonTokenCacheSize)
                .expireAfterWrite(commonTokenCacheDuration)
                .build();
        commonCanonicalValueCache = Caffeine.newBuilder()
                .maximumSize(maxCommonCanonicalCacheSize)
                .expireAfterWrite(commonCanonicalCacheDuration)
                .build();
    }

    /**
     * Initialize the cache
     * @param resourceTypeValues the complete list of resource types
     */
    public void init(Collection<ResourceTypeValue> resourceTypeValues) {
        for (ResourceTypeValue rtv: resourceTypeValues) {
            resourceTypes.put(rtv.getResourceType(), rtv.getResourceTypeId());
        }
    }

    @Override
    public Integer getParameterNameId(String parameterName) {
        // This should only miss if the parameter name value doesn't actually
        // exist. Because the set is relatively small, we store everything.
        return parameterNames.get(parameterName);
    }

    @Override
    public Integer getCodeSystemId(String codeSystem) {
        return codeSystemCache.get(codeSystem, k -> NULL_INT);
    }

    @Override
    public Long getCommonTokenValueId(short shardKey, String codeSystem, String tokenValue) {
        return commonTokenValueCache.get(new CommonTokenValueKey(shardKey, codeSystem, tokenValue), k -> NULL_LONG);
    }

    @Override
    public void addParameterName(String parameterName, int parameterNameId) {
        parameterNames.put(parameterName, parameterNameId);
    }

    @Override
    public Long getCommonCanonicalValueId(short shardKey, String url) {
        return commonCanonicalValueCache.get(new CommonCanonicalValueKey(shardKey, url), k -> NULL_LONG);
    }

    @Override
    public int getResourceTypeId(String resourceType) {
        Integer resourceTypeId = resourceTypes.get(resourceType);
        if (resourceTypeId == null) {
            throw new IllegalArgumentException("Not a valid resource type: " + resourceType);
        }
        return resourceTypeId;
    }
}