/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.registry.spi;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.registry.resource.FHIRRegistryResource;
import com.ibm.fhir.registry.resource.FHIRRegistryResource.Version;

/**
 * An abstract base class for {@link FHIRRegistryResourceProvider} implementations
 */
public abstract class AbstractRegistryResourceProvider implements FHIRRegistryResourceProvider {
    private static final Logger log = Logger.getLogger(AbstractRegistryResourceProvider.class.getName());

    @Override
    public final FHIRRegistryResource getRegistryResource(Class<? extends Resource> resourceType, String url, String version) {
        Objects.requireNonNull(resourceType, "resourceType");
        Objects.requireNonNull(url, "url");
        List<FHIRRegistryResource> registryResources = getRegistryResources(resourceType, url);
        if (!registryResources.isEmpty()) {
            if (version != null) {
                Version v = Version.from(version);
                for (FHIRRegistryResource registryResource : registryResources) {
                    if (registryResource.getVersion().equals(v)) {
                        return registryResource;
                    }
                }
                log.warning("Unable to find resource: " + url + " with version: " + version);
            } else {
                for (FHIRRegistryResource registryResource : registryResources) {
                    if (registryResource.isDefaultVersion()) {
                        // default version
                        return registryResource;
                    }
                }
                // latest version
                return registryResources.get(registryResources.size() - 1);
            }
        }
        return null;
    }

    protected abstract List<FHIRRegistryResource> getRegistryResources(Class<? extends Resource> resourceType, String url);

    @Override
    public abstract Collection<FHIRRegistryResource> getRegistryResources(Class<? extends Resource> resourceType);

    @Override
    public abstract Collection<FHIRRegistryResource> getRegistryResources();

    @Override
    public abstract Collection<FHIRRegistryResource> getProfileResources(String type);

    @Override
    public abstract Collection<FHIRRegistryResource> getSearchParameterResources(String type);
}
