/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.registry;

import static com.ibm.fhir.registry.util.FHIRRegistryUtil.getUrl;
import static com.ibm.fhir.registry.util.FHIRRegistryUtil.isDefinitionalResource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.core.util.LRUCache;
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
import com.ibm.fhir.persistence.interceptor.FHIRPersistenceEvent;
import com.ibm.fhir.persistence.interceptor.FHIRPersistenceInterceptor;
import com.ibm.fhir.registry.resource.FHIRRegistryResource;
import com.ibm.fhir.registry.resource.FHIRRegistryResource.Version;
import com.ibm.fhir.registry.spi.FHIRRegistryResourceProvider;
import com.ibm.fhir.search.context.FHIRSearchContext;
import com.ibm.fhir.search.util.SearchUtil;

public class ServerRegistryResourceProvider implements FHIRRegistryResourceProvider, FHIRPersistenceInterceptor {
    public static final Logger log = Logger.getLogger(ServerRegistryResourceProvider.class.getName());

    private final PersistenceHelper persistenceHelper;
    private final Map<String, Map<String, List<FHIRRegistryResource>>> registryResourceMap = new ConcurrentHashMap<>();

    public ServerRegistryResourceProvider(PersistenceHelper persistenceHelper) {
        try {
            this.persistenceHelper = Objects.requireNonNull(persistenceHelper);
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    @Override
    public FHIRRegistryResource getRegistryResource(Class<? extends Resource> resourceType, String url, String version) {
        String tenantId = FHIRRequestContext.get().getTenantId();
        String dataStoreId = FHIRRequestContext.get().getDataStoreId();
        String key = tenantId + ":" + dataStoreId;
        List<FHIRRegistryResource> registryResources = registryResourceMap.computeIfAbsent(key, k -> LRUCache.createLRUCache(1024))
                .computeIfAbsent(url, k -> computeRegistryResources(resourceType, url));
        if (!registryResources.isEmpty()) {
            if (version != null) {
                Version v = Version.from(version);
                for (FHIRRegistryResource resource : registryResources) {
                    if (resource.getVersion().equals(v)) {
                        return resource;
                    }
                }
                log.warning("Unable to find resource: " + url + " with version: " + version);
            } else {
                return registryResources.get(registryResources.size() - 1);
            }
        }
        return null;
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
        String types = Arrays.asList(ResourceType.ValueSet.values()).stream().map(r -> r.value()).collect(Collectors.joining(","));
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

    @Override
    public void afterCreate(FHIRPersistenceEvent event) {
        updateRegistryResourceMap(event);
    }

    @Override
    public void afterUpdate(FHIRPersistenceEvent event) {
        updateRegistryResourceMap(event);
    }

    @Override
    public void afterDelete(FHIRPersistenceEvent event) {
        updateRegistryResourceMap(event);
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
                List<FHIRRegistryResource> registryResources = new ArrayList<>(searchContext.getTotalCount());
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

    private void updateRegistryResourceMap(FHIRPersistenceEvent event) {
        if (event == null || event.getFhirResource() == null || !isDefinitionalResource(event.getFhirResource())) {
            return;
        }
        Resource resource = event.getFhirResource();
        String url = getUrl(resource);
        if (url != null) {
            String tenantId = FHIRRequestContext.get().getTenantId();
            String dataStoreId = FHIRRequestContext.get().getDataStoreId();
            String key = tenantId + ":" + dataStoreId;
            List<FHIRRegistryResource> previous = registryResourceMap.getOrDefault(key, Collections.emptyMap()).remove(url);
            if (previous != null && !previous.isEmpty()) {
                log.fine("Removed registry resource(s) with url '" + url + "' from the ServerRegistryResourceProvider cache");
            }
        }
    }
}
