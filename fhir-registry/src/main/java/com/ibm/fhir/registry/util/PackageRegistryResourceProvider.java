/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.registry.util;

import static com.ibm.fhir.registry.util.FHIRRegistryUtil.isProfile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.ibm.fhir.registry.resource.FHIRRegistryResource;
import com.ibm.fhir.registry.resource.FHIRRegistryResource.Version;
import com.ibm.fhir.registry.spi.FHIRRegistryResourceProvider;

public abstract class PackageRegistryResourceProvider implements FHIRRegistryResourceProvider {
    private static final Logger log = Logger.getLogger(PackageRegistryResourceProvider.class.getName());

    private final Map<String, List<FHIRRegistryResource>> resourceMap;
    private final Map<String, List<FHIRRegistryResource>> profileResourceMap;

    public PackageRegistryResourceProvider() {
        resourceMap = buildResourceMap();
        profileResourceMap = buildProfileResourceMap();
    }

    @Override
    public abstract Collection<FHIRRegistryResource> getResources();

    @Override
    public FHIRRegistryResource getResource(String url, String version) {
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

    @Override
    public Collection<FHIRRegistryResource> getProfileResources(String type) {
        return Collections.unmodifiableCollection(profileResourceMap.getOrDefault(type, Collections.emptyList()));
    }

    private Map<String, List<FHIRRegistryResource>> buildResourceMap() {
        Map<String, List<FHIRRegistryResource>> resourceMap = new HashMap<>();
        for (FHIRRegistryResource resource : getResources()) {
            String url = resource.getUrl();
            List<FHIRRegistryResource> resources = resourceMap.get(url);
            if (resources == null) {
                resources = new ArrayList<>();
                resourceMap.put(url, resources);
            }
            if (!resources.contains(resource)) {
                resources.add(resource);
            }
            Collections.sort(resources);
        }
        return resourceMap;
    }

    private Map<String, List<FHIRRegistryResource>> buildProfileResourceMap() {
        Map<String, List<FHIRRegistryResource>> profileResourceMap = new HashMap<>();
        for (String url : resourceMap.keySet()) {
            List<FHIRRegistryResource> resources = resourceMap.get(url);
            for (FHIRRegistryResource resource : resources) {
                if (isProfile(resource)) {
                    String type = resource.getType();
                    List<FHIRRegistryResource> profileResources = profileResourceMap.get(type);
                    if (profileResources == null) {
                        profileResources = new ArrayList<>();
                        profileResourceMap.put(type, profileResources);
                    }
                    profileResources.add(resource);
                }
            }
        }
        return profileResourceMap;
    }
}