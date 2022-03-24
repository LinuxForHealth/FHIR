/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.ibm.fhir.config.PropertyGroup.PropertyEntry;
import com.ibm.fhir.core.ResourceTypeName;

/**
 * An abstraction for the ibm-fhir-server fhirServer/resources property group
 */
public class ResourcesConfigAdapter {
    public static final Logger log = Logger.getLogger(ResourcesConfigAdapter.class.getName());

    public static final Set<String> ALL_CONCRETE_TYPES = Arrays.stream(ResourceTypeName.values())
        .filter(v -> v != ResourceTypeName.RESOURCE && v != ResourceTypeName.DOMAIN_RESOURCE)
        .map(v -> v.value())
        .collect(Collectors.toSet());

    private final Set<String> supportedTypes;
    private final Map<Interaction, Set<String>> typesByInteraction = new HashMap<>();

    public ResourcesConfigAdapter(PropertyGroup resourcesConfig) throws Exception {
        supportedTypes = computeSupportedResourceTypes(resourcesConfig);

        if (resourcesConfig == null) {
            for (Interaction interaction : Interaction.values()) {
                typesByInteraction.put(interaction, supportedTypes);
            }
            return;
        }

        for (String resourceType : supportedTypes) {
            List<String> interactions = resourcesConfig.getStringListProperty(resourceType + "/" + FHIRConfiguration.PROPERTY_FIELD_RESOURCES_INTERACTIONS);
            if (interactions == null) {
                interactions = resourcesConfig.getStringListProperty("Resource/" + FHIRConfiguration.PROPERTY_FIELD_RESOURCES_INTERACTIONS);
            }

            if (interactions == null) {
                for (Interaction interaction : Interaction.values()) {
                    typesByInteraction.computeIfAbsent(interaction, k -> new LinkedHashSet<>()).add(resourceType);
                }
                continue;
            }

            for (String interactionString : interactions) {
                Interaction interaction = Interaction.from(interactionString);
                typesByInteraction.computeIfAbsent(interaction, k -> new LinkedHashSet<>()).add(resourceType);
            }
        }
    }

    /**
     * @return whether the server is configured to prevent searches for one or more resource types
     */
    public boolean isSearchRestricted() {
        Set<String> searchableResourceTypes = typesByInteraction.get(Interaction.SEARCH);
        return searchableResourceTypes == null || searchableResourceTypes.size() < ALL_CONCRETE_TYPES.size();
    }

    /**
     * @return whether the server is configured to prevent history interactions for one or more resource types
     */
    public boolean isHistoryRestricted() {
        Set<String> resourceTypesSupportingHistory = typesByInteraction.get(Interaction.HISTORY);
        return resourceTypesSupportingHistory == null || resourceTypesSupportingHistory.size() < ALL_CONCRETE_TYPES.size();
    }

    /**
     * @return an immutable, non-null set of concrete supported resource types
     * @throws Exception
     */
    public Set<String> getSupportedResourceTypes() {
        return supportedTypes;
    }

    /**
     * @return an immutable, non-null set of concrete resource types that are configured for the given interaction
     */
    public Set<String> getSupportedResourceTypes(Interaction interaction) {
        return typesByInteraction.get(interaction);
    }

    /**
     * Construct the list of concrete supported resource types from the passed configuration
     *
     * @param resourcesConfig
     * @return
     * @throws Exception
     */
    private Set<String> computeSupportedResourceTypes(PropertyGroup resourcesConfig) throws Exception {
        if (resourcesConfig == null || resourcesConfig.getBooleanProperty("open", true)) {
            return ALL_CONCRETE_TYPES;
        }

        Set<String> result = new LinkedHashSet<String>();
        for (PropertyEntry rsrcsEntry : resourcesConfig.getProperties()) {
            String name = rsrcsEntry.getName();

            // Ensure we skip over the special property "open"
            // and skip the abstract types Resource and DomainResource
            if (FHIRConfiguration.PROPERTY_FIELD_RESOURCES_OPEN.equals(name) ||
                    "Resource".equals(name) ||
                    "DomainResource".equals(name)) {
                continue;
            }

            if (ALL_CONCRETE_TYPES.contains(name)) {
                result.add(name);
            } else if (log.isLoggable(Level.FINE)) {
                log.fine("Configured resource type '" + name + "' is not valid.");
            }
        }

        return Collections.unmodifiableSet(result);
    }
}
