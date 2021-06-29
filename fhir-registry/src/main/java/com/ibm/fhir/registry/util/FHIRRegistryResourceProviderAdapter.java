/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.registry.util;

import java.util.Collection;
import java.util.Collections;

import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.registry.resource.FHIRRegistryResource;
import com.ibm.fhir.registry.spi.FHIRRegistryResourceProvider;

public class FHIRRegistryResourceProviderAdapter implements FHIRRegistryResourceProvider {
    @Override
    public FHIRRegistryResource getRegistryResource(Class<? extends Resource> resourceType, String url, String version) {
        return null;
    }

    @Override
    public Collection<FHIRRegistryResource> getRegistryResources(Class<? extends Resource> resourceType) {
        return Collections.emptyList();
    }

    @Override
    public Collection<FHIRRegistryResource> getRegistryResources() {
        return Collections.emptyList();
    }

    @Override
    public Collection<FHIRRegistryResource> getProfileResources(String type) {
        return Collections.emptyList();
    }

    @Override
    public Collection<FHIRRegistryResource> getSearchParameterResources(String type) {
        return Collections.emptyList();
    }
}
