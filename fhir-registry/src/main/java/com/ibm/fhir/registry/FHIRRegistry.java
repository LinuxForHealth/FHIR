/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.registry;

import static com.ibm.fhir.registry.util.FHIRRegistryUtil.isDefinitionalResourceType;
import static com.ibm.fhir.registry.util.FHIRRegistryUtil.requireDefinitionalResourceType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.ibm.fhir.model.resource.DomainResource;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.SearchParameter;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.code.SearchParamType;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.registry.resource.FHIRRegistryResource;
import com.ibm.fhir.registry.spi.FHIRRegistryResourceProvider;

/**
 * A singleton registry for FHIR definitional resources: <a href="http://hl7.org/fhir/definition.html">http://hl7.org/fhir/definition.html</a>
 */
public final class FHIRRegistry {
    private static final Logger log = Logger.getLogger(FHIRRegistry.class.getName());

    private static final FHIRRegistry INSTANCE = new FHIRRegistry();

    private final List<FHIRRegistryResourceProvider> providers;

    private FHIRRegistry() {
        providers = new CopyOnWriteArrayList<>(loadProviders());
    }

    /**
     * Get the singleton instance of this class
     *
     * <p>This first time that this method is called, all registry resource providers made available through the
     * service loader are added to the registry
     *
     * @return
     *     the singleton instance of this class
     */
    public static FHIRRegistry getInstance() {
        return INSTANCE;
    }

    /**
     * Add a registry resource provider to the registry
     *
     * @implNote
     *     This method should not be called by consumers that make their registry resource providers available through
     *     the service loader
     * @param provider
     *     the registry resource provider to be added
     */
    public void register(FHIRRegistryResourceProvider provider) {
        Objects.requireNonNull(provider);
        providers.add(provider);
    }

    /**
     * Indicates whether a resource for the given canonical url and resource type exists in the registry
     *
     * @param url
     *     the canonical url
     * @param resourceType
     *     the resource type
     * @return
     *     true if a resource for the given canonical url and resource type exists in the registry, false otherwise
     */
    public boolean hasResource(String url, Class<? extends Resource> resourceType) {
        if (url == null || resourceType == null || !isDefinitionalResourceType(resourceType)) {
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

        FHIRRegistryResource registryResource = findRegistryResource(resourceType, url, version);
        return (id != null) ? (getResource(registryResource, url, id) != null) : (registryResource != null);
    }

    /**
     * Get the latest version of a resource for the given url and resource type
     *
     * @param url
     *     the url
     * @param resourceType
     *     the resource type
     * @return
     *     the latest version of a resource for the given url and resource type if exists, null otherwise
     */
    public String getLatestVersion(String url, Class<? extends Resource> resourceType) {
        if (url == null || resourceType == null || !isDefinitionalResourceType(resourceType)) {
            return null;
        }

        int index = url.indexOf("|");
        if (index != -1) {
            url = url.substring(0, index);
        }

        List<FHIRRegistryResource> registryResources = getLatestVersionRegistryResources(resourceType, url);
        return !registryResources.isEmpty() ? registryResources.get(registryResources.size() - 1).getVersion().toString() : null;
    }

    private List<FHIRRegistryResource> getLatestVersionRegistryResources(Class<? extends Resource> resourceType, String url) {
        return providers.stream()
                .map(provider -> provider.getRegistryResource(resourceType, url, null))
                .filter(Objects::nonNull)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Get the resource for the given canonical url and resource type
     *
     * @param url
     *     the canonical url
     * @param resourceType
     *     the resource type
     * @return
     *     the resource for the given canonical url and resource type if exists, null otherwise
     * @throws ClassCastException
     *     if the resource exists in the registry but its type does not match given resource type
     * @throws IllegalArgumentException
     *     if the resource type is not a definitional resource type
     */
    public <T extends Resource> T getResource(String url, Class<T> resourceType) {
        Objects.requireNonNull(url);
        Objects.requireNonNull(resourceType);
        requireDefinitionalResourceType(resourceType);

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

        return resourceType.cast(getResource(findRegistryResource(resourceType, url, version), url, id));
    }

    /**
     * Get the resources for the given resource type
     *
     * @param resourceType
     *     the resource type
     * @return
     *     the resources for the given resource type
     * @throws IllegalArgumentException
     *     if the resource type is not a definitional resource type
     */
    public <T extends Resource> Collection<T> getResources(Class<T> resourceType) {
        Objects.requireNonNull(resourceType);
        requireDefinitionalResourceType(resourceType);
        return providers.stream()
                .map(provider -> provider.getRegistryResources(resourceType))
                .flatMap(Collection::stream)
                .map(registryResource -> resourceType.cast(registryResource.getResource()))
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
    }

    /**
     * Get the profiles that constrain the given resource type as a collection of {@link Canonical} URLs
     *
     * @param type
     *     the constrained resource type
     * @return
     *     the profiles that constrain the given type as a collection of {@link Canonical} URLs
     */
    public Collection<Canonical> getProfiles(String type) {
        Objects.requireNonNull(type);
        if (!ModelSupport.isResourceType(type)) {
            throw new IllegalArgumentException("The type argument must be a valid FHIR resource type name");
        }
        return providers.stream().map(provider -> provider.getProfileResources(type))
                .flatMap(Collection::stream)
                .sorted()
                .map(registryResource -> Canonical.of(registryResource.getUrl(), registryResource.getVersion().toString()))
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
    }

    /**
     * Get the search parameters with the given search parameter type (e.g. string, token, etc.)
     *
     * <p>The method {@link FHIRRegistry#getResources(Class)} can be used to get all search parameters regardless of type
     *
     * @param type
     *     the search parameter type
     * @return
     *     the search parameters with the given search parameter type
     */
    public Collection<SearchParameter> getSearchParameters(String type) {
        Objects.requireNonNull(type);
        SearchParamType.ValueSet.from(type);
        return providers.stream()
                .map(provider -> provider.getSearchParameterResources(type))
                .flatMap(Collection::stream)
                .map(registryResource -> registryResource.getResource().as(SearchParameter.class))
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
    }

    private FHIRRegistryResource findRegistryResource(Class<? extends Resource> resourceType, String url, String version) {
        return providers.stream()
                .map(provider -> provider.getRegistryResource(resourceType, url, version))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    private Resource getResource(FHIRRegistryResource registryResource, String url, String id) {
        if (registryResource == null) {
            return null;
        }
        Resource resource = registryResource.getResource();
        if (resource != null && id != null) {
            if (resource.is(DomainResource.class)) {
                for (Resource contained : resource.as(DomainResource.class).getContained()) {
                    if (id.equals(contained.getId())) {
                        return contained;
                    }
                }
                log.warning("Unable to find contained resource with id: " + id + " in resource: " + url);
            } else {
                log.warning("Resource: " + url + " is not a DomainResource");
            }
            return null;
        }
        return resource;
    }

    private List<FHIRRegistryResourceProvider> loadProviders() {
        List<FHIRRegistryResourceProvider> providers = new ArrayList<>();
        for (FHIRRegistryResourceProvider provider : ServiceLoader.load(FHIRRegistryResourceProvider.class)) {
            providers.add(provider);
        }
        return providers;
    }
}
