/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.logging.Logger;

import com.ibm.fhir.model.resource.DomainResource;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.registry.resource.FHIRRegistryResource;
import com.ibm.fhir.registry.resource.FHIRRegistryResource.Version;
import com.ibm.fhir.registry.spi.FHIRRegistryResourceProvider;

/**
 * A singleton registry for FHIR definitional resources: http://hl7.org/fhir/definition.html
 */
public final class FHIRRegistry {
    private static final Logger log = Logger.getLogger(FHIRRegistry.class.getName());
    
    private static final FHIRRegistry INSTANCE = new FHIRRegistry();
    
    private final Map<String, List<FHIRRegistryResource>> resourceMap;
    
    private FHIRRegistry() {
        resourceMap = buildResourceMap();
    }
    
    public static FHIRRegistry getInstance() {
        return INSTANCE;
    }
    
    /**
     * Get the latest version of a resource for the given url.
     * 
     * @param url
     *     the url of the resource
     * @return
     *     the version of the resource associated with the input parameter, or null if no such resource exists
     */
    public String getLatestVersion(String url) {
        if (url == null) {
            return null;
        }
        
        int index = url.indexOf("|");
        if (index != -1) {
            url = url.substring(0, index);
        }
        
        FHIRRegistryResource resource = findResource(url, null);
        return (resource != null) ? resource.getVersion().toString() : null;
    }

    /**
     * Get the resource for the given canonical url.
     * 
     * @return
     *    the resource associated with the input parameter, or null if no such resource exists
     */
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

    /**
     * Determine if the resource associated with the given url exists in the registry.
     * 
     * @param url
     *     the url of the resource
     * @return
     *     true if the resource associated with the given url exists in the registry, false otherwise
     */
    public boolean hasResource(String url) {
        if (url == null) {
            return false;
        }
        
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
        
        FHIRRegistryResource resource = findResource(url, version);
        return (id != null) ? (getResource(resource, url, id) != null) : (resource != null);
    }
    

    /**
     * Unload the resource associated with the given url.
     * 
     * @param url
     *     the url of the resource
     */
    public void unloadResource(String url) {
        if (url == null) {
            return;
        }
        
        int index = url.indexOf("#");
        if (index != -1) {
            url = url.substring(0, index);
        }
        
        String version = null;
        index = url.indexOf("|");
        if (index != -1) {
            version = url.substring(index + 1);
            url = url.substring(0, index);
        }
        
        FHIRRegistryResource resource = findResource(url, version);
        if (resource != null) {
            resource.unload();
        }
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
        if (result != null && id != null) {
            if (result.is(DomainResource.class)) {
                for (Resource contained : result.as(DomainResource.class).getContained()) {
                    if (contained.getId() != null && id.equals(contained.getId())) {
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