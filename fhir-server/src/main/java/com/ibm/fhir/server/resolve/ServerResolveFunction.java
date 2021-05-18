/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.resolve;

import static com.ibm.fhir.cache.CacheKey.key;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.cache.CacheKey;
import com.ibm.fhir.cache.CacheManager;
import com.ibm.fhir.cache.CacheManager.Configuration;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.path.function.ResolveFunction;
import com.ibm.fhir.persistence.FHIRPersistence;
import com.ibm.fhir.persistence.SingleResourceResult;
import com.ibm.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.fhir.persistence.context.FHIRPersistenceContextFactory;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.helper.FHIRTransactionHelper;
import com.ibm.fhir.persistence.helper.PersistenceHelper;

public class ServerResolveFunction extends ResolveFunction {
    public static final Logger log = Logger.getLogger(ServerResolveFunction.class.getName());

    private static final String RESOURCE_CACHE_NAME = "com.ibm.fhir.server.resolve.ServerResolveFunction.resourceCache";
    private static final Configuration RESOURCE_CACHE_CONFIGURATION = Configuration.of(Duration.of(1, ChronoUnit.MINUTES));

    private final PersistenceHelper persistenceHelper;

    public ServerResolveFunction(PersistenceHelper persistenceHelper) {
        this.persistenceHelper = persistenceHelper;
    }

    @Override
    public Resource resolve(String resourceType, String logicalId, String versionId) {
        Map<CacheKey, Resource> cacheAsMap = CacheManager.getCacheAsMap(RESOURCE_CACHE_NAME, RESOURCE_CACHE_CONFIGURATION);
        CacheKey key = key(resourceType, logicalId, versionId);
        return cacheAsMap.computeIfAbsent(key, k -> computeResource(resourceType, logicalId, versionId));
    }

    public Resource computeResource(String resourceType, String logicalId, String versionId) {
        FHIRTransactionHelper transactionHelper = null;
        try {
            FHIRPersistence persistence = persistenceHelper.getFHIRPersistenceImplementation();
            transactionHelper = new FHIRTransactionHelper(persistence.getTransaction());

            transactionHelper.begin();

            FHIRPersistenceContext context = FHIRPersistenceContextFactory.createPersistenceContext(null);
            SingleResourceResult<Resource> result = (versionId != null) ?
                    persistence.vread(context, Resource.class, logicalId, versionId) :
                    persistence.read(context, Resource.class, logicalId);

            if (result.isSuccess()) {
                transactionHelper.commit();
                transactionHelper = null;
                return result.getResource();
            }
        } catch (Exception e) {
            log.log(Level.WARNING, "An error occurred during a read or vread interaction", e);
        } finally {
            if (transactionHelper != null) {
                try {
                    transactionHelper.rollback();
                } catch (FHIRPersistenceException e) {
                    log.log(Level.WARNING, "An error occurred ending the current transaction", e);
                }
            }
        }

        return null;
    }
}
