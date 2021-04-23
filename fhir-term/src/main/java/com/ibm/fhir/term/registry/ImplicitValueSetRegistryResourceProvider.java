/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.registry;

import static com.ibm.fhir.cache.util.CacheSupport.createCacheAsMap;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.ValueSet;
import com.ibm.fhir.registry.resource.FHIRRegistryResource;
import com.ibm.fhir.registry.spi.FHIRRegistryResourceProvider;

/**
 * An abstract base class for implicit registry resource provider implementations (e.g. SNOMED, LOINC, etc.)
 */
public abstract class ImplicitValueSetRegistryResourceProvider implements FHIRRegistryResourceProvider {
    private static final Map<String, FHIRRegistryResource> IMPLICIT_VALUE_SET_REGISTRY_RESOURCE_CACHE = createCacheAsMap(1024);

    @Override
    public Collection<FHIRRegistryResource> getProfileResources(String type) {
        return Collections.emptyList();
    }

    @Override
    public FHIRRegistryResource getRegistryResource(Class<? extends Resource> resourceType, String url, String version) {
        if (url == null) {
            return null;
        }
        if (ValueSet.class.equals(resourceType) && isSupported(url)) {
            return IMPLICIT_VALUE_SET_REGISTRY_RESOURCE_CACHE.computeIfAbsent(url, k -> FHIRRegistryResource.from(buildImplicitValueSet(url)));
        }
        return null;
    }

    @Override
    public Collection<FHIRRegistryResource> getRegistryResources() {
        return Collections.emptyList();
    }
    @Override
    public Collection<FHIRRegistryResource> getRegistryResources(Class<? extends Resource> resourceType) {
        return Collections.emptyList();
    }

    @Override
    public Collection<FHIRRegistryResource> getSearchParameterResources(String type) {
        return Collections.emptyList();
    }

    protected abstract ValueSet buildImplicitValueSet(String url);
    protected abstract boolean isSupported(String url);
}
