/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.logging.Logger;

import com.ibm.watson.health.fhir.model.resource.DomainResource;
import com.ibm.watson.health.fhir.model.resource.Resource;
import com.ibm.watson.health.fhir.registry.resource.FHIRRegistryResource;
import com.ibm.watson.health.fhir.registry.resource.FHIRRegistryResource.Version;
import com.ibm.watson.health.fhir.registry.spi.FHIRRegistryResourceProvider;

public class FHIRRegistry {
    private static final Logger log = Logger.getLogger(FHIRRegistry.class.getName());
    
    private static final FHIRRegistry INSTANCE = new FHIRRegistry();
    
    private final Map<String, List<FHIRRegistryResource>> resourceMap;
    
    private FHIRRegistry() {
        resourceMap = buildResourceMap();
    }
    
    public static FHIRRegistry getInstance() {
        return INSTANCE;
    }
    
    public <T extends Resource> T getResource(String url, Class<T> resourceType) {
        Objects.requireNonNull(url);
        Objects.requireNonNull(resourceType);
                
        String id = null;
        int index = url.indexOf("#");
        if (index != -1) {
            id = url.substring(index + 1);
            url = url.substring(0, index);
        }
        
        String version = null;
        index = url.indexOf("|");
        if (index != -1) {
            version = url.substring(index + 1);
            url = url.substring(0, index);
        }
        
        return resourceType.cast(getResource(findResource(url, version), url, id));
    }

    private FHIRRegistryResource findResource(String url, String version) {
        List<FHIRRegistryResource> resources = resourceMap.get(url);
        if (resources != null) {
            if (version != null) {
                Version v = Version.from(version);
                for (FHIRRegistryResource resource : resources) {
                    if (resource.getVersion().equals(v)) {
                        return resource;
                    }
                }
                log.warning("Unable to find resource: " + url + " with version: " + version);
            } else {
                return resources.get(resources.size() - 1);
            }
        }
        return null;
    }

    private Resource getResource(FHIRRegistryResource resource, String url, String id) {
        if (resource == null) {
            return null;
        }
        Resource result = resource.getResource();
        if (id != null) {
            if (result.is(DomainResource.class)) {
                for (Resource contained : result.as(DomainResource.class).getContained()) {
                    if (contained.getId() != null && id.equals(contained.getId().getValue())) {
                        return contained;
                    }
                }
                log.warning("Unable to find contained resource with id: " + id + " in resource: " + url);
            } else {
                log.warning("Resource: " + url + " is not a DomainResource");
            }
            return null;
        }
        return result;
    }

    private Map<String, List<FHIRRegistryResource>> buildResourceMap() {
        Map<String, List<FHIRRegistryResource>> resourceMap = new HashMap<>();
        for (FHIRRegistryResourceProvider provider : ServiceLoader.load(FHIRRegistryResourceProvider.class)) {
            for (FHIRRegistryResource resource : provider.getResources()) {
                String url = resource.getUrl();
                List<FHIRRegistryResource> resources = resourceMap.get(url);
                if (resources == null) {
                    resources = new ArrayList<>();
                    resourceMap.put(url, resources);
                }
                if (!resources.contains(resource)) {
                    resources.add(resource);
                }
                Collections.sort(resources, FHIRRegistryResource.VERSION_COMPARATOR);
            }
        }
        return resourceMap;
    }
}