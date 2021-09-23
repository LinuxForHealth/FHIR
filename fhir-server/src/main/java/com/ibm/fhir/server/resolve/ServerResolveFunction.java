/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.resolve;

import static com.ibm.fhir.cache.CacheKey.key;
import static com.ibm.fhir.model.util.ModelSupport.getResourceType;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.cache.CacheKey;
import com.ibm.fhir.cache.CacheManager;
import com.ibm.fhir.cache.CacheManager.Configuration;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.code.IssueSeverity;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;
import com.ibm.fhir.path.function.ResolveFunction;
import com.ibm.fhir.persistence.FHIRPersistence;
import com.ibm.fhir.persistence.SingleResourceResult;
import com.ibm.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.fhir.persistence.context.FHIRPersistenceContextFactory;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.helper.FHIRTransactionHelper;
import com.ibm.fhir.persistence.helper.PersistenceHelper;
import com.ibm.fhir.search.util.ReferenceUtil;

public class ServerResolveFunction extends ResolveFunction {
    private static final Logger log = Logger.getLogger(ServerResolveFunction.class.getName());

    public static final String RESOURCE_CACHE_NAME = "com.ibm.fhir.server.resolve.ServerResolveFunction.resourceCache";
    public static final Configuration RESOURCE_CACHE_CONFIGURATION = Configuration.of(Duration.of(1, ChronoUnit.MINUTES));

    private static final String VREAD = "vread";
    private static final String READ = "read";
    private static final Object NULL = new Object();

    private final PersistenceHelper persistenceHelper;

    public ServerResolveFunction(PersistenceHelper persistenceHelper) {
        this.persistenceHelper = persistenceHelper;
    }

    @Override
    protected Resource resolveRelativeReference(EvaluationContext evaluationContext, FHIRPathNode node, String resourceType, String logicalId, String versionId) {
        Map<CacheKey, Object> cacheAsMap = CacheManager.getCacheAsMap(RESOURCE_CACHE_NAME, RESOURCE_CACHE_CONFIGURATION);
        CacheManager.reportCacheStats(log, RESOURCE_CACHE_NAME);
        CacheKey key = key(resourceType, logicalId, versionId);
        Object result = cacheAsMap.computeIfAbsent(key, k -> computeResource(evaluationContext, node, resourceType, logicalId, versionId));
        return (result != NULL) ? (Resource) result : null;
    }

    @Override
    protected boolean matchesServiceBaseUrl(String baseUrl) {
        try {
            return baseUrl.equals(ReferenceUtil.getServiceBaseUrl());
        } catch (Exception e) {
            log.log(Level.WARNING, "An error occurred getting the service base url", e);
        }
        return false;
    }

    private Object computeResource(EvaluationContext evaluationContext, FHIRPathNode node, String type, String logicalId, String versionId) {
        Class<? extends Resource> resourceType = getResourceType(type);
        String interaction = (versionId != null) ? VREAD : READ;
        FHIRTransactionHelper transactionHelper = null;
        try {
            FHIRPersistence persistence = persistenceHelper.getFHIRPersistenceImplementation();
            transactionHelper = new FHIRTransactionHelper(persistence.getTransaction());

            transactionHelper.begin();

            FHIRPersistenceContext context = FHIRPersistenceContextFactory.createPersistenceContext(null);
            SingleResourceResult<? extends Resource> result = VREAD.equals(interaction) ?
                    persistence.vread(context, resourceType, logicalId, versionId) :
                    persistence.read(context, resourceType, logicalId);

            if (result.isSuccess()) {
                transactionHelper.commit();
                transactionHelper = null;

                return result.getResource();
            }
        } catch (Exception e) {
            log.log(Level.WARNING, "An error occurred during a " + interaction + " interaction", e);
            generateIssue(evaluationContext, IssueSeverity.WARNING, IssueType.EXCEPTION, "Error resolving relative reference: " + e.getMessage(), node.path());
        } finally {
            if (transactionHelper != null) {
                try {
                    transactionHelper.rollback();
                } catch (FHIRPersistenceException e) {
                    log.log(Level.WARNING, "An error occurred ending the current transaction", e);
                }
            }
        }
        return NULL;
    }
}
