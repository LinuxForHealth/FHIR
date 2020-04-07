/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.registry.spi;

import java.util.Collection;

import com.ibm.fhir.registry.resource.FHIRRegistryResource;

/**
 * An SPI for {@link FHIRRegistryResource} instances
 */
public interface FHIRRegistryResourceProvider {
    /**
     * Get the FHIR registry resources for this provider. A FHIR registry resource contains all of the information necessary
     * to load an actual FHIR resource into memory. FHIR registry resources must have a unique url+version pair. If this method
     * returns a collection containing FHIR registry resources with duplicate url+version pairs, then the first one will be
     * added to the registry and any subsequent duplicates will be ignored.
     *
     * @return
     *     the FHIR registry resources for this provider.
     */
    Collection<FHIRRegistryResource> getResources();

    /**
     * Get the FHIR registry resource with the given url and version. If the version is null, then the latest version of
     * the registry resource is returned.
     *
     * @param url
     *     the url of the registry resource
     * @param version
     *     the version of the registry resource
     * @return
     *     the FHIR registry resource for the given url+version pair if exists, null otherwise
     */
    FHIRRegistryResource getResource(String url, String version);

    /**
     * Get the profile resources from this provider for the given type. A profile resource is a FHIR registry resource that refers
     * to a StructureDefinition with kind=resource and is not defined in the base specification.
     *
     * @param type
     *     the type of resource that the profile resources are derived from
     * @return
     *     the profile resources from this provider for the given type
     */
    Collection<FHIRRegistryResource> getProfileResources(String type);
}