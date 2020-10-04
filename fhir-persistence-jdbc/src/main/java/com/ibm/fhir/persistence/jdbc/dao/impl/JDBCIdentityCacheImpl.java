/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dao.impl;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.FHIRPersistenceJDBCCache;
import com.ibm.fhir.persistence.jdbc.dao.api.JDBCIdentityCache;
import com.ibm.fhir.persistence.jdbc.dao.api.ParameterDAO;
import com.ibm.fhir.persistence.jdbc.dao.api.ResourceDAO;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDataAccessException;


/**
 * Pulls together the DAOs and tenant-specific cache to provide a single place
 * where we can look up the identity of various records we need
 */
public class JDBCIdentityCacheImpl implements JDBCIdentityCache {
    
    // The tenant-specific set of caches
    private final FHIRPersistenceJDBCCache cache;
    
    // The DAO providing access to parameter names
    private final ParameterDAO parameterDAO;

    // The DAO providing access to resource types
    private final ResourceDAO resourceDAO;

    /**
     * Public constructor
     * @param cache
     * @param parameterDAO
     */
    public JDBCIdentityCacheImpl(FHIRPersistenceJDBCCache cache, ResourceDAO resourceDAO, ParameterDAO parameterDAO) {
        this.cache = cache;
        this.resourceDAO = resourceDAO;
        this.parameterDAO = parameterDAO;
    }

    @Override
    public Integer getResourceTypeId(String resourceType) throws FHIRPersistenceException {
        Integer result = cache.getResourceTypeCache().getId(resourceType);
        if (result == null) {
            // try the database instead and cache the result
            result = resourceDAO.readResourceTypeId(resourceType);

            if (result == null) {
                // likely a configuration error, caused by the schema being generated
                // for a subset of all possible resource types
                throw new FHIRPersistenceDataAccessException("Resource type not registered in database: '" + resourceType + "'");
            }
            
            cache.getResourceTypeCache().addEntry(resourceType, result);
        }
        return result;
    }

    @Override
    public Integer getCodeSystemId(String codeSystemName) throws FHIRPersistenceException {
        Integer result = cache.getResourceReferenceCache().getCodeSystemId(codeSystemName);
        if (result == null) {
            // cache miss, so hit the database
            result = parameterDAO.readOrAddCodeSystemId(codeSystemName);
            if (result != null) {
                cache.getResourceReferenceCache().addCodeSystem(codeSystemName, result);
            }
        }
        return result;
    }

    @Override
    public Integer getParameterNameId(String parameterName) throws FHIRPersistenceException {
        Integer result = cache.getParameterNameCache().getId(parameterName);
        if (result == null) {
            result = parameterDAO.acquireParameterNameId(parameterName);
            cache.getParameterNameCache().addEntry(parameterName, result);
        }
        
        return result;
    }

}
