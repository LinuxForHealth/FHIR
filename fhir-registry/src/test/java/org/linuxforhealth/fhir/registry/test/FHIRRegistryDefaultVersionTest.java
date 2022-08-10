/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.registry.test;

import static org.linuxforhealth.fhir.model.type.String.string;
import static org.testng.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.resource.SearchParameter;
import org.linuxforhealth.fhir.model.resource.StructureDefinition;
import org.linuxforhealth.fhir.model.type.Boolean;
import org.linuxforhealth.fhir.model.type.Canonical;
import org.linuxforhealth.fhir.model.type.Uri;
import org.linuxforhealth.fhir.model.type.code.PublicationStatus;
import org.linuxforhealth.fhir.model.type.code.StructureDefinitionKind;
import org.linuxforhealth.fhir.registry.FHIRRegistry;
import org.linuxforhealth.fhir.registry.resource.FHIRRegistryResource;
import org.linuxforhealth.fhir.registry.resource.FHIRRegistryResource.Version;
import org.linuxforhealth.fhir.registry.spi.FHIRRegistryResourceProvider;

public class FHIRRegistryDefaultVersionTest {
    static {
        FHIRRegistry.getInstance().addProvider(
            createRegistryResourceProvider(
                FHIRRegistryResource.from(createStructureDefinition("http://example.com/fhir/StructureDefinition-test", "1.0.0")),
                FHIRRegistryResource.from(createStructureDefinition("http://example.com/fhir/StructureDefinition-test", "2.0.0")),
                FHIRRegistryResource.from(createStructureDefinition("http://example.com/fhir/StructureDefinition-test", "3.0.0")),
                FHIRRegistryResource.from(createStructureDefinition("http://example.com/fhir/StructureDefinition-test-x", "1.0.0"))));
        FHIRRegistry.getInstance().addProvider(
            createRegistryResourceProvider(
                FHIRRegistryResource.from(createStructureDefinition("http://example.com/fhir/StructureDefinition-test", "4.0.0")),
                FHIRRegistryResource.from(createStructureDefinition("http://example.com/fhir/StructureDefinition-test", "5.0.0")),
                FHIRRegistryResource.from(createStructureDefinition("http://example.com/fhir/StructureDefinition-test", "6.0.0")),
                FHIRRegistryResource.from(createStructureDefinition("http://example.com/fhir/StructureDefinition-test-x", "2.0.0"), true)));
        FHIRRegistry.getInstance().addProvider(
            createRegistryResourceProvider(
                FHIRRegistryResource.from(createStructureDefinition("http://example.com/fhir/StructureDefinition-test", "7.0.0")),
                FHIRRegistryResource.from(createStructureDefinition("http://example.com/fhir/StructureDefinition-test", "8.0.0")),
                FHIRRegistryResource.from(createStructureDefinition("http://example.com/fhir/StructureDefinition-test", "9.0.0")),
                FHIRRegistryResource.from(createStructureDefinition("http://example.com/fhir/StructureDefinition-test-x", "3.0.0"))));
    }

    @Test
    public void testDefaultVersion1() throws Exception {
        assertEquals(FHIRRegistry.getInstance().getDefaultVersion("http://example.com/fhir/StructureDefinition-test", StructureDefinition.class), "9.0.0");
    }

    @Test
    public void testDefaultVersion2() throws Exception {
        assertEquals(FHIRRegistry.getInstance().getDefaultVersion("http://example.com/fhir/StructureDefinition-test-x", StructureDefinition.class), "2.0.0");
    }

    private static StructureDefinition createStructureDefinition(String url, String version) {
        return StructureDefinition.builder()
                .id("test")
                .url(Uri.of(url))
                .version(string(version))
                .status(PublicationStatus.DRAFT)
                .name(string("Test Profile"))
                .kind(StructureDefinitionKind.RESOURCE)
                .baseDefinition(Canonical.of("http://hl7.org/fhir/StructureDefinition/Patient"))
                ._abstract(Boolean.FALSE)
                .type(Uri.of("Patient"))
                .build();
    }

    private static FHIRRegistryResourceProvider createRegistryResourceProvider(FHIRRegistryResource... registryResources) {
        return new FHIRRegistryResourceProvider() {
            @Override
            public FHIRRegistryResource getRegistryResource(Class<? extends Resource> resourceType, String url, String version) {
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
                        return registryResources.get(registryResources.size() - 1);
                    }
                }
                return null;
            }

            private List<FHIRRegistryResource> getRegistryResources(Class<? extends Resource> resourceType, String url) {
                return getRegistryResources(resourceType).stream()
                        .filter(registryResource -> registryResource.getUrl().equals(url))
                        .sorted()
                        .collect(Collectors.toList());
            }

            @Override
            public Collection<FHIRRegistryResource> getRegistryResources(Class<? extends Resource> resourceType) {
                return getRegistryResources().stream()
                        .filter(registryResource -> registryResource.getResourceType().equals(resourceType))
                        .collect(Collectors.toList());
            }

            @Override
            public Collection<FHIRRegistryResource> getRegistryResources() {
                return Arrays.asList(registryResources);
            }

            @Override
            public Collection<FHIRRegistryResource> getProfileResources(String type) {
                return getRegistryResources(StructureDefinition.class).stream()
                        .filter(registryResource -> type.equals(registryResource.getType()))
                        .filter(registryResource -> "resource".equals(registryResource.getKind()))
                        .collect(Collectors.toList());
            }

            @Override
            public Collection<FHIRRegistryResource> getSearchParameterResources(String type) {
                return getRegistryResources(SearchParameter.class);
            }
        };
    }
}
