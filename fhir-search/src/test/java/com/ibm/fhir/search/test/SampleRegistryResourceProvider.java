/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.search.test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.ibm.fhir.cache.annotation.Cacheable;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.SearchParameter;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Markdown;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.PublicationStatus;
import com.ibm.fhir.model.type.code.ResourceTypeCode;
import com.ibm.fhir.model.type.code.SearchParamType;
import com.ibm.fhir.registry.resource.FHIRRegistryResource;
import com.ibm.fhir.registry.util.FHIRRegistryResourceProviderAdapter;

/**
 * FHIRRegistryResourceProvider for providing versioned SearchParameter definitions to the registry for search tests
 */
public class SampleRegistryResourceProvider extends FHIRRegistryResourceProviderAdapter {
    SearchParameter sp_a1 = SearchParameter.builder()
            .url(Uri.of("http://example.com/SearchParameter/sp_a"))
            .version("1.0.0")
            .status(PublicationStatus.DRAFT)
            .description(Markdown.of("sample search param for ParametersSearchUtilTest.testVersionedSearchParameterFilter"))
            .name("a")
            .code(Code.of("a"))
            .base(ResourceTypeCode.DEVICE, ResourceTypeCode.PATIENT)
            .type(SearchParamType.STRING)
            .expression("Device.id")
            .build();

    SearchParameter sp_a2 = sp_a1.toBuilder()
            .version("1.0.1")
            .build();

    SearchParameter sp_b1 = SearchParameter.builder()
            .url(Uri.of("http://example.com/SearchParameter/sp_b"))
            .version("1.0.0")
            .status(PublicationStatus.DRAFT)
            .description(Markdown.of("sample search param for ParametersSearchUtilTest.testVersionedSearchParameterFilter"))
            .name("b")
            .code(Code.of("b"))
            .base(ResourceTypeCode.PATIENT, ResourceTypeCode.DEVICE)
            .type(SearchParamType.STRING)
            .expression("Device.id")
            .build();

    SearchParameter sp_b2 = sp_a1.toBuilder()
            .version("2.0.0")
            .build();

    private List<FHIRRegistryResource> registryResources = Arrays.asList(
        // Add them in descending version order so that we don't need to sort them later to have the highest version first
        FHIRRegistryResource.from(sp_a2),
        FHIRRegistryResource.from(sp_a1),

        FHIRRegistryResource.from(sp_b2),
        FHIRRegistryResource.from(sp_b1)
    );

    @Cacheable
    @Override
    public FHIRRegistryResource getRegistryResource(Class<? extends Resource> resourceType, String url, String version) {
        return registryResources.stream()
                .filter(rr -> rr.getResourceType() == resourceType)
                .filter(rr -> rr.getUrl().equals(url))
                .filter(rr -> rr.getVersion() == null || version == null || rr.getVersion().toString().equals(version))
                .findFirst()
                .orElse(null);
    }

    @Cacheable
    @Override
    public Collection<FHIRRegistryResource> getRegistryResources(Class<? extends Resource> resourceType) {
        return registryResources.stream()
                .filter(rr -> rr.getResourceType() == resourceType)
                .collect(Collectors.toSet());
    }

    @Cacheable
    @Override
    public Collection<FHIRRegistryResource> getRegistryResources() {
        return registryResources;
    }

    @Cacheable
    @Override
    public Collection<FHIRRegistryResource> getProfileResources(String type) {
        return Collections.emptySet();
    }

    @Cacheable
    @Override
    public Collection<FHIRRegistryResource> getSearchParameterResources(String type) {
        return registryResources.stream()
                .filter(rr -> rr.getResourceType() == SearchParameter.class)
                .filter(rr -> ((SearchParameter) rr.getResource()).getType().getValue().equals(type))
                .collect(Collectors.toSet());
    }
}
