/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dao.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.FHIRPersistenceJDBCCache;
import com.ibm.fhir.persistence.jdbc.dao.api.IResourceReferenceDAO;
import com.ibm.fhir.persistence.jdbc.dao.api.JDBCIdentityCache;
import com.ibm.fhir.persistence.jdbc.dao.api.ParameterDAO;
import com.ibm.fhir.persistence.jdbc.dao.api.ResourceDAO;
import com.ibm.fhir.persistence.jdbc.dto.CommonTokenValue;
import com.ibm.fhir.persistence.jdbc.dto.CommonTokenValueResult;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDataAccessException;


/**
 * Pulls together the DAOs and tenant-specific cache to provide a single place
 * where we can look up the identity of various records we need
 */
public class JDBCIdentityCacheImpl implements JDBCIdentityCache {
    private static final Logger logger = Logger.getLogger(JDBCIdentityCacheImpl.class.getName());

    // The tenant-specific set of caches
    private final FHIRPersistenceJDBCCache cache;

    // The DAO providing access to parameter names
    private final ParameterDAO parameterDAO;

    // The DAO providing access to resource types
    private final ResourceDAO resourceDAO;

    private final IResourceReferenceDAO resourceReferenceDAO;

    /**
     * Public constructor
     * @param cache
     * @param parameterDAO
     * @param rrd
     */
    public JDBCIdentityCacheImpl(FHIRPersistenceJDBCCache cache, ResourceDAO resourceDAO, ParameterDAO parameterDAO, IResourceReferenceDAO rrd) {
        this.cache = cache;
        this.resourceDAO = resourceDAO;
        this.parameterDAO = parameterDAO;
        this.resourceReferenceDAO = rrd;
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

    @Override
    public Long getCommonTokenValueId(String codeSystem, String tokenValue) {
        Long result = cache.getResourceReferenceCache().getCommonTokenValueId(codeSystem, tokenValue);
        if (result == null) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("Cache miss. Fetching common_token_value_id from database: '" + codeSystem + "|" + tokenValue + "'");
            }
            CommonTokenValueResult dto = resourceReferenceDAO.readCommonTokenValueId(codeSystem, tokenValue);
            if (dto != null) {
                // Value exists in the database, so we can add this to our cache. Note that we still
                // choose to add it the thread-local cache - this avoids any locking. The values will
                // be promoted to the shared cache at the end of the transaction. This avoids unnecessary
                // contention.
                result = dto.getCommonTokenValueId();
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("Adding common_token_value_id to cache: '" + codeSystem + "|" + tokenValue + "' = " + result);
                }
                cache.getResourceReferenceCache().addCodeSystem(codeSystem, dto.getCodeSystemId());
                cache.getResourceReferenceCache().addTokenValue(new CommonTokenValue(dto.getCodeSystemId(), tokenValue), result);
            }
        }
        return result;
    }
}