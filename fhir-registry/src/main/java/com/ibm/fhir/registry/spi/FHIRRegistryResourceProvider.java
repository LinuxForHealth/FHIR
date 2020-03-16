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
}