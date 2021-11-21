/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.registry;

import static com.ibm.fhir.registry.util.FHIRRegistryUtil.isDefinitionalResourceType;
import static com.ibm.fhir.registry.util.FHIRRegistryUtil.requireDefinitionalResourceType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

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
     * Add a registry resource provider to the registry
     *
     * @implNote
     *     This method should not be called by consumers that make their registry resource providers available through
     *     the service loader
     * @param provider
     *     the registry resource provider to be added
     */
    public void addProvider(FHIRRegistryResourceProvider provider) {
        Objects.requireNonNull(provider);
        providers.add(provider);
        provider.init();
    }

    /**
     * Get the default (or latest) version of a resource with the given url and resource type
     *
     * @param url
     *     the url
     * @param resourceType
     *     the resource type
     * @return
     *     the default (or latest) version of a resource with the given url and resource type if exists, null otherwise
     */
    public String getDefaultVersion(String url, Class<? extends Resource> resourceType) {
        if (url == null || resourceType == null || !isDefinitionalResourceType(resourceType)) {
            return null;
        }

        int index = url.indexOf("|");
        if (index != -1) {
            url = url.substring(0, index);
        }

        FHIRRegistryResource resource = findRegistryResource(resourceType, url, null, null);
        return (resource != null) ? resource.getVersion().toString() : null;
    }

    /**
     * Get a map containing sets of type specific canonical URLs for all profile resources across all providers.
     *
     * @return
     *     the map of sets
     */
    public Map<String, Set<Canonical>> getProfiles() {
        Map<String, Set<Canonical>> map = new HashMap<>();
        for (FHIRRegistryResourceProvider provider : providers) {
            for (FHIRRegistryResource r : provider.getProfileResources()) {
                map.computeIfAbsent(r.getType(), k -> new LinkedHashSet<>())
                    .add(Canonical.of(r.getUrl(), r.getVersion().toString()));
            }
        }
        return map;
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
        List<FHIRRegistryResource> registryResources = new ArrayList<>();
        for (FHIRRegistryResourceProvider provider : providers) {
            registryResources.addAll(provider.getProfileResources(type));
        }
        Collections.sort(registryResources);
        List<Canonical> profiles = new ArrayList<>();
        for (FHIRRegistryResource registryResource : registryResources) {
            profiles.add(Canonical.of(registryResource.getUrl(), registryResource.getVersion().toString()));
        }
        return Collections.unmodifiableList(profiles);
    }

    /**
     * Get the resource for the given canonical url and resource type
     *
     * @param url
     *     the canonical url (with optional version postfix)
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
        return getResource(url, resourceType, null);
    }

    /**
     * Get the resource for the given canonical url and resource type
     *
     * @param url
     *     the canonical url (with optional version postfix)
     * @param resourceType
     *     the resource type
     * @param providerNameToExclude
     *     the canonical class name of the provider that is to be excluded
     * @return
     *     the resource for the given canonical url and resource type if exists, null otherwise
     * @throws ClassCastException
     *     if the resource exists in the registry but its type does not match given resource type
     * @throws IllegalArgumentException
     *     if the resource type is not a definitional resource type
     */
    public <T extends Resource> T getResource(String url, Class<T> resourceType, String providerNameToExclude) {
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

        return resourceType.cast(getResource(findRegistryResource(resourceType, url, version, providerNameToExclude), url, id));
    }

    /**
     * Get the resources for the given resource type
     *
     * <p>Use this method to get actual FHIR resources and not FHIR registry resources (metadata)
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
        List<T> resources = new ArrayList<>();
        for (FHIRRegistryResourceProvider provider : providers) {
            for (FHIRRegistryResource registryResource : provider.getRegistryResources(resourceType)) {
                resources.add(resourceType.cast(registryResource.getResource()));
            }
        }
        return Collections.unmodifiableList(resources);
    }

    /**
     * Get the registry resources for the given resource type
     *
     * <p>Use this method to get FHIR registry resources (metadata) and not actual FHIR resources
     *
     * @param resourceType
     *     the resource type
     * @return
     *     the registry resources for the given resource type
     * @throws IllegalArgumentException
     *     if the resource type is not a definitional resource type
     */
    public Collection<FHIRRegistryResource> getRegistryResources(Class<? extends Resource> resourceType) {
        Objects.requireNonNull(resourceType);
        requireDefinitionalResourceType(resourceType);
        List<FHIRRegistryResource> registryResources = new ArrayList<>();
        for (FHIRRegistryResourceProvider provider : providers) {
            registryResources.addAll(provider.getRegistryResources(resourceType));
        }
        return registryResources;
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
        SearchParamType.Value.from(type);
        List<SearchParameter> searchParameters = new ArrayList<>();
        for (FHIRRegistryResourceProvider provider : providers) {
            for (FHIRRegistryResource registryResource : provider.getSearchParameterResources(type)) {
                searchParameters.add(registryResource.getResource().as(SearchParameter.class));
            }
        }
        return Collections.unmodifiableList(searchParameters);
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

        FHIRRegistryResource registryResource = findRegistryResource(resourceType, url, version, null);
        return (id != null) ? (getResource(registryResource, url, id) != null) : (registryResource != null);
    }

    private FHIRRegistryResource findRegistryResource(Class<? extends Resource> resourceType, String url, String version, String providerNameToExclude) {
        if (version != null) {
            // find the first registry resource with the specified resourceType, url, and version (across all providers)
            for (FHIRRegistryResourceProvider provider : providers) {
                FHIRRegistryResource registryResource = provider.getRegistryResource(resourceType, url, version);
                if (registryResource != null) {
                    return registryResource;
                }
            }
        } else {
            // find the default (or latest) version of the registry resource with the specified resourceType and url (across all providers)
            Set<FHIRRegistryResource> distinct = new HashSet<>();
            for (FHIRRegistryResourceProvider provider : providers) {
                if (providerNameToExclude != null && providerNameToExclude.equals(provider.getClass().getCanonicalName())) {
                    // Needs to skip as the provider is calling the findRegistryResource and trying to find it in itself.
                    continue;
                }
                FHIRRegistryResource registryResource = provider.getRegistryResource(resourceType, url, version);
                if (registryResource != null) {
                    distinct.add(registryResource);
                }
            }
            List<FHIRRegistryResource> registryResources = new ArrayList<>(distinct);
            Collections.sort(registryResources);
            if (!registryResources.isEmpty()) {
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

    /**
     * initializes the Resource Providers.
     */
    public static void init() {
        for (FHIRRegistryResourceProvider provider : getInstance().providers) {
            provider.init();
        }
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
}
