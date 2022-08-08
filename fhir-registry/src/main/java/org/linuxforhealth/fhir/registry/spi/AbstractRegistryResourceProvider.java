/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.registry.spi;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.registry.FHIRRegistry;
import org.linuxforhealth.fhir.registry.resource.FHIRRegistryResource;
import org.linuxforhealth.fhir.registry.resource.FHIRRegistryResource.Version;

/**
 * An abstract base class for {@link FHIRRegistryResourceProvider} implementations
 */
public abstract class AbstractRegistryResourceProvider implements FHIRRegistryResourceProvider {
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

    /**
     * facilitates the retrieval of a resource from the registry allowing the provider to
     * be excluded from the resource retrieval.
     *
     * designed to be used at startup.
     *
     * @param <T>
     * @param url
     * @param resourceType
     * @return
     */
    protected <T extends Resource> T loadFromRegistry(String url, Class<T> resourceType){
        return FHIRRegistry.getInstance()
                    .getResource(url, resourceType, getClass().getCanonicalName());
    }

    /**
     * Return a sorted list of FHIRRegistryResource with the passed canonical url
     *
     * @param resourceType
     * @param url the canonical url for this resource (without version suffix)
     * @return a list of FHIRRegistryResources with this url, sorted from low to high by version
     */
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
