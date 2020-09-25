/*
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc;

import static com.ibm.fhir.config.FHIRConfiguration.PROPERTY_JDBC_ENABLE_PARAMETER_NAMES_CACHE;

import com.ibm.fhir.persistence.FHIRPersistence;
import com.ibm.fhir.persistence.FHIRPersistenceFactory;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.FHIRPersistenceJDBCCache;
import com.ibm.fhir.persistence.jdbc.impl.FHIRPersistenceJDBCImpl;

/**
 * Factory which serves up instances of the JDBC persistence implementation.
 */
public class FHIRPersistenceJDBCFactory implements FHIRPersistenceFactory {
    
    // All instances created by this factory share the common cache object (which is tenant-aware)
    private final FHIRPersistenceJDBCTenantCache tenantCache = new FHIRPersistenceJDBCTenantCache();

    @Override
    public FHIRPersistence getInstance() throws FHIRPersistenceException {
        try {
            // each request gets a new instance of the FHIRPersistenceJDBCImpl, sharing
            // the common (tenant-aware) cache object
            FHIRPersistenceJDBCCache cache = tenantCache.getCacheForTenantAndDatasource();
            return new FHIRPersistenceJDBCImpl(cache);
        } catch (Exception e) {
            throw new FHIRPersistenceException("Unexpected exception while creating JDBC persistence layer: ", e); 
        }
    }
}
