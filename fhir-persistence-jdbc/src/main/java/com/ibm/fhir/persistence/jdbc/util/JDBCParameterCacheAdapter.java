/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.jdbc.util;

import com.ibm.fhir.persistence.jdbc.FHIRPersistenceJDBCCache;
import com.ibm.fhir.persistence.jdbc.dao.api.LogicalResourceIdentKey;
import com.ibm.fhir.persistence.jdbc.dto.CommonTokenValue;
import com.ibm.fhir.persistence.params.api.IParameterIdentityCache;

/**
 * An adapter to implement the {@link IParameterIdentityCache} using the {@link FHIRPersistenceJDBCCache}
 */
public class JDBCParameterCacheAdapter implements IParameterIdentityCache {

    // The delegate cache
    private final FHIRPersistenceJDBCCache cache;

    /**
     * Public constructor
     * @param cache
     */
    public JDBCParameterCacheAdapter(FHIRPersistenceJDBCCache cache) {
        this.cache = cache;
    }

    @Override
    public Integer getParameterNameId(String parameterName) {
        return cache.getParameterNameCache().getId(parameterName);
    }

    @Override
    public Integer getCodeSystemId(String codeSystem) {
        return cache.getCommonValuesCache().getCodeSystemId(codeSystem);
    }

    @Override
    public Long getCommonTokenValueId(short shardKey, String codeSystem, String tokenValue) {
        return cache.getCommonValuesCache().getCommonTokenValueId(codeSystem, tokenValue);
    }

    @Override
    public void addParameterName(String parameterName, int parameterNameId) {
        cache.getParameterNameCache().addEntry(parameterName, parameterNameId);
    }

    @Override
    public Long getCommonCanonicalValueId(short shardKey, String url) {
        return cache.getCommonValuesCache().getCanonicalId(url);
    }

    @Override
    public void addCommonCanonicalValue(short shardKey, String url, long commonCanonicalValueId) {
        cache.getCommonValuesCache().addCanonicalValue(url, commonCanonicalValueId);
    }

    @Override
    public void addCommonTokenValue(short shardKey, String codeSystem, int codeSystemId, String tokenValue, long commonTokenValueId) {
        CommonTokenValue key = new CommonTokenValue(codeSystem, codeSystemId, tokenValue);
        cache.getCommonValuesCache().addTokenValue(key, commonTokenValueId);
    }

    @Override
    public void addCodeSystem(String codeSystem, int codeSystemId) {
        cache.getCommonValuesCache().addCodeSystem(codeSystem, codeSystemId);
    }

    @Override
    public int getResourceTypeId(String resourceType) {
        return cache.getResourceTypeCache().getId(resourceType);
    }

    @Override
    public Long getLogicalResourceIdentId(String resourceType, String logicalId) {
        final int resourceTypeId = getResourceTypeId(resourceType);
        return cache.getLogicalResourceIdentCache().getLogicalResourceId(resourceTypeId, logicalId);
    }

    @Override
    public void addLogicalResourceIdent(String resourceType, String logicalId, long logicalResourceId) {
        final int resourceTypeId = getResourceTypeId(resourceType);
        LogicalResourceIdentKey key = new LogicalResourceIdentKey(resourceTypeId, logicalId);
        cache.getLogicalResourceIdentCache().addRecord(key, logicalResourceId);
    }
}
