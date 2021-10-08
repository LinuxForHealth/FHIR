/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.registry;

import static com.ibm.fhir.cache.CacheKey.key;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.ibm.fhir.cache.CacheKey;
import com.ibm.fhir.cache.CacheManager;
import com.ibm.fhir.cache.CacheManager.Configuration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.SearchParameter;
import com.ibm.fhir.model.resource.StructureDefinition;
import com.ibm.fhir.model.type.code.ResourceType;
import com.ibm.fhir.persistence.FHIRPersistence;
import com.ibm.fhir.persistence.MultiResourceResult;
import com.ibm.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.fhir.persistence.context.FHIRPersistenceContextFactory;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.helper.FHIRTransactionHelper;
import com.ibm.fhir.persistence.helper.PersistenceHelper;
import com.ibm.fhir.registry.resource.FHIRRegistryResource;
import com.ibm.fhir.registry.spi.AbstractRegistryResourceProvider;
import com.ibm.fhir.search.context.FHIRSearchContext;
import com.ibm.fhir.search.util.SearchUtil;

public class ServerRegistryResourceProvider extends AbstractRegistryResourceProvider {
    public static final Logger log = Logger.getLogger(ServerRegistryResourceProvider.class.getName());

    public static final String REGISTRY_RESOURCE_CACHE_NAME = "com.ibm.fhir.server.registry.ServerRegistryResourceProvider.registryResourceCache";
    public static final Configuration REGISTRY_RESOURCE_CACHE_CONFIGURATION = Configuration.of(1024, Duration.of(1, ChronoUnit.MINUTES));

    private final PersistenceHelper persistenceHelper;

    public ServerRegistryResourceProvider(PersistenceHelper persistenceHelper) {
        try {
            this.persistenceHelper = Objects.requireNonNull(persistenceHelper);
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    @Override
    protected List<FHIRRegistryResource> getRegistryResources(Class<? extends Resource> resourceType, String url) {
        String dataStoreId = FHIRRequestContext.get().getDataStoreId();
        CacheKey key = key(dataStoreId, url);
        try {
            Map<CacheKey, List<FHIRRegistryResource>> cacheAsMap = CacheManager.getCacheAsMap(REGISTRY_RESOURCE_CACHE_NAME, REGISTRY_RESOURCE_CACHE_CONFIGURATION);
            return cacheAsMap.computeIfAbsent(key, k -> computeRegistryResources(resourceType, url));
        } finally {
            CacheManager.reportCacheStats(log, REGISTRY_RESOURCE_CACHE_NAME);
        }
    }

    @Override
    public Collection<FHIRRegistryResource> getRegistryResources(Class<? extends Resource> resourceType) {
        try {
            return getRegistryResources(resourceType, Collections.emptyMap());
        } catch (Exception e) {
            log.log(Level.WARNING, "An error occurred during a search interaction", e);
        }
        return Collections.emptyList();
    }

    @Override
    public Collection<FHIRRegistryResource> getRegistryResources() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<FHIRRegistryResource> getProfileResources() {
        Map<String, List<String>> queryParameters = new HashMap<>();
        String types = Arrays.asList(ResourceType.Value.values()).stream().map(r -> r.value()).collect(Collectors.joining(","));
        queryParameters.put("type", Collections.singletonList(types));
        queryParameters.put("kind", Collections.singletonList("resource"));
        queryParameters.put("derivation", Collections.singletonList("constraint"));
        return getRegistryResources(StructureDefinition.class, queryParameters);
    }

    @Override
    public Collection<FHIRRegistryResource> getProfileResources(String type) {
        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("type", Collections.singletonList(type));
        queryParameters.put("kind", Collections.singletonList("resource"));
        queryParameters.put("derivation", Collections.singletonList("constraint"));
        return getRegistryResources(StructureDefinition.class, queryParameters);
    }

    @Override
    public Collection<FHIRRegistryResource> getSearchParameterResources(String type) {
        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("type", Collections.singletonList(type));
        return getRegistryResources(SearchParameter.class, queryParameters);
    }

    private List<FHIRRegistryResource> computeRegistryResources(Class<? extends Resource> resourceType, String url) {
        FHIRTransactionHelper transactionHelper = null;
        try {
            FHIRPersistence persistence = persistenceHelper.getFHIRPersistenceImplementation();
            transactionHelper = new FHIRTransactionHelper(persistence.getTransaction());

            transactionHelper.begin();

            FHIRSearchContext searchContext = SearchUtil.parseQueryParameters(resourceType, Collections.singletonMap("url", Collections.singletonList(url)));
            searchContext.setPageSize(1000);

            FHIRPersistenceContext context = FHIRPersistenceContextFactory.createPersistenceContext(null, searchContext);
            MultiResourceResult<Resource> result = persistence.search(context, resourceType);

            if (result.isSuccess()) {
                transactionHelper.commit();
                transactionHelper = null;

                return result.getResource().stream()
                        .map(FHIRRegistryResource::from)
                        .filter(Objects::nonNull)
                        .sorted()
                        .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
            }
        } catch (Exception e) {
            log.log(Level.WARNING, "An error occurred during the underlying search interaction; returning empty", e);
        } finally {
            if (transactionHelper != null) {
                try {
                    transactionHelper.rollback();
                } catch (FHIRPersistenceException e) {
                    log.log(Level.WARNING, "An error occurred while rolling back the current transaction", e);
                }
            }
        }
        return Collections.emptyList();
    }

    private Collection<FHIRRegistryResource> getRegistryResources(Class<? extends Resource> resourceType, Map<String, List<String>> queryParameters) {
        FHIRTransactionHelper transactionHelper = null;
        try {
            FHIRPersistence persistence = persistenceHelper.getFHIRPersistenceImplementation();
            transactionHelper = new FHIRTransactionHelper(persistence.getTransaction());

            transactionHelper.begin();

            FHIRSearchContext searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters);
            searchContext.setPageSize(1000);

            FHIRPersistenceContext context = FHIRPersistenceContextFactory.createPersistenceContext(null, searchContext);
            MultiResourceResult<Resource> result = persistence.search(context, resourceType);

            if (result.isSuccess()) {
                List<FHIRRegistryResource> registryResources = new ArrayList<>(searchContext.getTotalCount() != null ? searchContext.getTotalCount() : result.getResource().size());
                registryResources.addAll(result.getResource().stream()
                        .map(FHIRRegistryResource::from)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()));

                int pageNumber = 1;
                int lastPageNumber = searchContext.getLastPageNumber();
                while (pageNumber < lastPageNumber) {
                    searchContext.setPageNumber(++pageNumber);
                    result = persistence.search(context, resourceType);
                    registryResources.addAll(result.getResource().stream()
                            .map(FHIRRegistryResource::from)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList()));
                }

                transactionHelper.commit();
                transactionHelper = null;

                return Collections.unmodifiableList(registryResources);
            }
        } catch (Exception e) {
            log.log(Level.WARNING, "An error occurred during a search interaction", e);
        } finally {
            if (transactionHelper != null) {
                try {
                    transactionHelper.rollback();
                } catch (FHIRPersistenceException e) {
                    log.log(Level.WARNING, "An error occurred ending the current transaction", e);
                }
            }
        }

        return Collections.emptyList();
    }
}
