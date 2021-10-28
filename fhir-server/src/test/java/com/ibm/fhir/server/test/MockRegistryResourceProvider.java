/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test;

import java.util.Collection;
import java.util.Collections;

import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.StructureDefinition;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.PublicationStatus;
import com.ibm.fhir.model.type.code.StructureDefinitionKind;
import com.ibm.fhir.model.type.code.TypeDerivationRule;
import com.ibm.fhir.registry.resource.FHIRRegistryResource;
import com.ibm.fhir.registry.resource.FHIRRegistryResource.Version;
import com.ibm.fhir.registry.spi.FHIRRegistryResourceProvider;

/**
 * A mock FHIRRegistryResourceProvider
 */
public class MockRegistryResourceProvider implements FHIRRegistryResourceProvider {
    StructureDefinition profile1sd = StructureDefinition.builder()
            .url(Uri.of("profile1"))
            .name(com.ibm.fhir.model.type.String.of("profile1"))
            .status(PublicationStatus.ACTIVE)
            .kind(StructureDefinitionKind.RESOURCE)
            ._abstract(com.ibm.fhir.model.type.Boolean.FALSE)
            .type(Uri.of("Patient"))
            .derivation(TypeDerivationRule.CONSTRAINT)
            .build();

    FHIRRegistryResource profile1 = new FHIRRegistryResource(
        StructureDefinition.class,
        profile1sd.getId(),
        profile1sd.getUrl().getValue(),
        Version.from("1"),
        profile1sd.getKind().getValue(),
        profile1sd.getType().getValue()) {
        @Override
        public Resource getResource() {
            return profile1sd;
        }
    };

    StructureDefinition profile2sd = StructureDefinition.builder()
            .url(Uri.of("profile2"))
            .name(com.ibm.fhir.model.type.String.of("profile2"))
            .status(PublicationStatus.ACTIVE)
            .kind(StructureDefinitionKind.RESOURCE)
            ._abstract(com.ibm.fhir.model.type.Boolean.FALSE)
            .type(Uri.of("Patient"))
            .derivation(TypeDerivationRule.CONSTRAINT)
            .build();

    FHIRRegistryResource profile2 = new FHIRRegistryResource(
        StructureDefinition.class,
        profile2sd.getId(),
        profile2sd.getUrl().getValue(),
        Version.from("1"),
        profile2sd.getKind().getValue(),
        profile2sd.getType().getValue()) {
        @Override
        public Resource getResource() {
            return profile2sd;
        }
    };

    StructureDefinition profile3sd = StructureDefinition.builder()
            .url(Uri.of("profile3"))
            .name(com.ibm.fhir.model.type.String.of("profile3"))
            .status(PublicationStatus.ACTIVE)
            .kind(StructureDefinitionKind.RESOURCE)
            ._abstract(com.ibm.fhir.model.type.Boolean.FALSE)
            .type(Uri.of("Patient"))
            .derivation(TypeDerivationRule.CONSTRAINT)
            .build();

    FHIRRegistryResource profile3 = new FHIRRegistryResource(
        StructureDefinition.class,
        profile3sd.getId(),
        profile3sd.getUrl().getValue(),
        Version.from("1"),
        profile3sd.getKind().getValue(),
        profile3sd.getType().getValue()) {
        @Override
        public Resource getResource() {
            return profile3sd;
        }
    };

    StructureDefinition profile4sd = StructureDefinition.builder()
            .url(Uri.of("profile4"))
            .name(com.ibm.fhir.model.type.String.of("profile4"))
            .status(PublicationStatus.ACTIVE)
            .kind(StructureDefinitionKind.RESOURCE)
            ._abstract(com.ibm.fhir.model.type.Boolean.FALSE)
            .type(Uri.of("Encounter"))
            .derivation(TypeDerivationRule.CONSTRAINT)
            .build();

    FHIRRegistryResource profile4 = new FHIRRegistryResource(
        StructureDefinition.class,
        profile4sd.getId(),
        profile4sd.getUrl().getValue(),
        Version.from("1"),
        profile4sd.getKind().getValue(),
        profile4sd.getType().getValue()) {
        @Override
        public Resource getResource() {
            return profile4sd;
        }
    };

    StructureDefinition profile5sd = StructureDefinition.builder()
            .url(Uri.of("profile5"))
            .name(com.ibm.fhir.model.type.String.of("profile5"))
            .status(PublicationStatus.ACTIVE)
            .kind(StructureDefinitionKind.RESOURCE)
            ._abstract(com.ibm.fhir.model.type.Boolean.FALSE)
            .type(Uri.of("Patient"))
            .derivation(TypeDerivationRule.CONSTRAINT)
            .build();

    FHIRRegistryResource profile5 = new FHIRRegistryResource(
        StructureDefinition.class,
        profile5sd.getId(),
        profile5sd.getUrl().getValue(),
        Version.from("1"),
        profile5sd.getKind().getValue(),
        profile5sd.getType().getValue()) {
        @Override
        public Resource getResource() {
            return profile5sd;
        }
    };

    /**
     * Get the registry resource from this provider for the given resource type, url and version
     *
     * <p>If the version is null, then the latest version of the registry resource is returned (if available)
     *
     * @param resourceType
     *     the resource type of the registry resource
     * @param url
     *     the url of the registry resource
     * @param version
     *     the version of the registry resource (optional)
     * @return
     *     the registry resource from this provider for the given resource type, url and version if exists, null otherwise
     */
    @Override
    public FHIRRegistryResource getRegistryResource(Class<? extends Resource> resourceType, String url, String version) {
        if (url.equals("profile1") && (version == null || version.equals("1"))) {
            return profile1;
        } else if (url.equals("profile2") && (version == null || version.equals("1"))) {
            return profile2;
        } else if (url.equals("profile3") && (version == null || version.equals("1"))) {
            return profile3;
        } else if (url.equals("profile4") && (version == null || version.equals("1"))) {
            return profile4;
        } else if (url.equals("profile5") && (version == null || version.equals("1"))) {
            return profile5;
        } else {
            return null;
        }
    }

    /**
     * Get the registry resources from this provider for the given resource type
     *
     * @param resourceType
     *     the resource type of the registry resource
     * @return
     *     the registry resources from this provider for the given resource type
     */
    @Override
    public Collection<FHIRRegistryResource> getRegistryResources(Class<? extends Resource> resourceType) {
        return Collections.emptyList();
    }

    /**
     * Get all the registry resources from this provider
     *
     * @return
     *     all of the registry resources from this provider
     */
    @Override
    public Collection<FHIRRegistryResource> getRegistryResources() {
        return Collections.emptyList();
    }

    /**
     * Get the profile resources from this provider that constrain the given resource type
     *
     * @param type
     *     the constrained resource type
     * @return
     *     the profile resources from this provider that constrain the given resource type
     */
    @Override
    public Collection<FHIRRegistryResource> getProfileResources(String type) {
        return Collections.emptyList();
    }

    /**
     * Get the search parameter resources from this provider with the given search parameter type
     * (e.g. string, token, etc.)
     *
     * @param type
     *     the search parameter type
     * @return
     *     the search parameter resources from this provider with the given search parameter type
     */
    @Override
    public Collection<FHIRRegistryResource> getSearchParameterResources(String type) {
        return Collections.emptyList();
    }

    /**
     * Get the profiles for all of the resources.
     *
     * @return
     *  the profile resources from this provider that constrain the resource types
     */
    @Override
    public Collection<FHIRRegistryResource> getProfileResources() {
        return Collections.emptyList();
    };
}