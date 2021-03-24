/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.registry.test;

import static com.ibm.fhir.model.type.String.string;
import static org.testng.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.testng.annotations.Test;

import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.SearchParameter;
import com.ibm.fhir.model.resource.StructureDefinition;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.PublicationStatus;
import com.ibm.fhir.model.type.code.StructureDefinitionKind;
import com.ibm.fhir.registry.FHIRRegistry;
import com.ibm.fhir.registry.resource.FHIRRegistryResource;
import com.ibm.fhir.registry.resource.FHIRRegistryResource.Version;
import com.ibm.fhir.registry.spi.FHIRRegistryResourceProvider;

public class FHIRRegistryLatestVersionTest {
    static {
        FHIRRegistry.getInstance().addProvider(
            createRegistryResourceProvider(
                createRegistryResource(createStructureDefinition("1.0.0")),
                createRegistryResource(createStructureDefinition("2.0.0")),
                createRegistryResource(createStructureDefinition("3.0.0"))));
        FHIRRegistry.getInstance().addProvider(
            createRegistryResourceProvider(
                createRegistryResource(createStructureDefinition("4.0.0")),
                createRegistryResource(createStructureDefinition("5.0.0")),
                createRegistryResource(createStructureDefinition("6.0.0"))));
        FHIRRegistry.getInstance().addProvider(
            createRegistryResourceProvider(
                createRegistryResource(createStructureDefinition("7.0.0")),
                createRegistryResource(createStructureDefinition("8.0.0")),
                createRegistryResource(createStructureDefinition("9.0.0"))));
    }

    @Test
    public void testLatestVersion() throws Exception {
        assertEquals(FHIRRegistry.getInstance().getLatestVersion("http://ibm.com/fhir/StructureDefinition-test", StructureDefinition.class), "9.0.0");
    }

    private static StructureDefinition createStructureDefinition(String version) {
        return StructureDefinition.builder()
                .id("test")
                .url(Uri.of("http://ibm.com/fhir/StructureDefinition-test"))
                .version(string(version))
                .status(PublicationStatus.DRAFT)
                .name(string("Test Profile"))
                .kind(StructureDefinitionKind.RESOURCE)
                .baseDefinition(Canonical.of("http://hl7.org/fhir/StructureDefinition/Patient"))
                ._abstract(Boolean.FALSE)
                .type(Uri.of("Patient"))
                .build();
    }


    private static FHIRRegistryResource createRegistryResource(StructureDefinition structureDefinition) {
        return new FHIRRegistryResource(
                StructureDefinition.class,
                structureDefinition.getId(),
                structureDefinition.getUrl().getValue(),
                Version.from(structureDefinition.getVersion().getValue()),
                structureDefinition.getKind().getValue(),
                structureDefinition.getType().getValue()) {
            @Override
            public Resource getResource() {
                return structureDefinition;
            }
        };
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
