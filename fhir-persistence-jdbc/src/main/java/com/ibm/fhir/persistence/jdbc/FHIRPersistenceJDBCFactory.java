/*
 * (C) Copyright IBM Corp. 2017, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc;


import com.ibm.fhir.persistence.FHIRPersistence;
import com.ibm.fhir.persistence.FHIRPersistenceFactory;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.cache.FHIRPersistenceJDBCTenantCache;
import com.ibm.fhir.persistence.jdbc.impl.FHIRPersistenceJDBCImpl;
import com.ibm.fhir.search.util.SearchUtil;

/**
 * Factory which serves up instances of the JDBC persistence implementation.
 */
public class FHIRPersistenceJDBCFactory implements FHIRPersistenceFactory {

    // All instances created by this factory share the common cache object (which is tenant-aware)
    private final FHIRPersistenceJDBCTenantCache tenantCache = new FHIRPersistenceJDBCTenantCache();

    @Override
    public FHIRPersistence getInstance(SearchUtil searchHelper) throws FHIRPersistenceException {
        try {
            // each request gets a new instance of the FHIRPersistenceJDBCImpl, sharing
            // the common (tenant-aware) cache object
            FHIRPersistenceJDBCCache cache = tenantCache.getCacheForTenantAndDatasource();
            return new FHIRPersistenceJDBCImpl(cache, getPayloadPersistence(), searchHelper);
        } catch (Exception e) {
            throw new FHIRPersistenceException("Unexpected exception while creating JDBC persistence layer: '" + e.getMessage() + "'", e);
        }
    }
}
