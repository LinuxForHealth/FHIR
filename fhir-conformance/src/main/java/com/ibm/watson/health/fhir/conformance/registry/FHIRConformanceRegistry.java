/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.conformance.registry;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

import com.ibm.watson.health.fhir.conformance.ConformanceResource;
import com.ibm.watson.health.fhir.conformance.spi.ConformanceResourceProvider;
import com.ibm.watson.health.fhir.model.resource.Resource;

public final class FHIRConformanceRegistry {    
    private static final FHIRConformanceRegistry INSTANCE = new FHIRConformanceRegistry();
    
    private final Map<String, ConformanceResource> conformanceResourceMap = new ConcurrentHashMap<>();

    public static FHIRConformanceRegistry getInstance() {
        return INSTANCE;
    }

    private FHIRConformanceRegistry() {
        registerConformanceResources();
    }
    
    public <T extends Resource> T getConformanceResource(String url, Class<T> resourceType) {
        ConformanceResource conformanceResource = conformanceResourceMap.get(url);
        if (conformanceResource != null) {
            return resourceType.cast(conformanceResource.getResource());
        }
        return null;
    }

    public void register(ConformanceResource conformanceResource) {
        conformanceResourceMap.put(conformanceResource.getUrl(), conformanceResource);
    }

    private void registerConformanceResources() {
        for (ConformanceResourceProvider provider : ServiceLoader.load(ConformanceResourceProvider.class)) {
            for (ConformanceResource conformanceResource : provider.getConformanceResources()) {
                register(conformanceResource);
            }
        }
    }

    public static void main(String args[]) {
        FHIRConformanceRegistry.getInstance();
    }
}