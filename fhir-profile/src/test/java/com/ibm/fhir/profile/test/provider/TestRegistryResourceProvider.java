/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.profile.test.provider;

import static com.ibm.fhir.model.type.String.string;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.SearchParameter;
import com.ibm.fhir.model.resource.StructureDefinition;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Markdown;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.ExtensionContextType;
import com.ibm.fhir.model.type.code.PublicationStatus;
import com.ibm.fhir.model.type.code.StructureDefinitionKind;
import com.ibm.fhir.registry.resource.FHIRRegistryResource;
import com.ibm.fhir.registry.spi.AbstractRegistryResourceProvider;

/**
 * A TestRegistryResourceProvider that supplies a custom StructureDefinition for an Extension
 */
public class TestRegistryResourceProvider extends AbstractRegistryResourceProvider {

    private List<FHIRRegistryResource> registryResources = null;

    /**
     * When using a ServiceLoader statics/class level instances the ServiceLoader
     * loads from the classloader and the order is not fixed.
     * Once these are created, then we can use the resource lookup to support retrieval.
     */
    @Override
    public void init() {
        deferredLoad();
    }
    private void deferredLoad() {
        if (registryResources == null) {
            synchronized (TestRegistryResourceProvider.class) {
                if (registryResources == null) {
                    // @implNote this double check idiom is intentional.
                    registryResources = Arrays.asList(
                        FHIRRegistryResource.from(generateStructureDefinition()));
                }
            }
        }
    }

    /**
     * generates the structured definition used in the deferredLoad
     * @return
     */
    private StructureDefinition generateStructureDefinition() {
        StructureDefinition.Builder builder = StructureDefinition.builder();
        builder.setValidating(false);

        return builder.url(Uri.of("http://ibm.com/example/fhir/StructureDefinition/orgRef"))
                .name(string("OrganizationRef"))
                .description(Markdown.of("Sample example organization ref."))
                ._abstract(Boolean.FALSE)
                .type(Uri.of("Extension"))
                .status(PublicationStatus.DRAFT)
                .kind(StructureDefinitionKind.COMPLEX_TYPE)
                .context(com.ibm.fhir.model.resource.StructureDefinition.Context.builder()
                    .type(ExtensionContextType.ELEMENT)
                    .expression(string("Endpoint")).build())
                .baseDefinition(Canonical.builder().value("Extension")
                    .build())
                .snapshot(loadFromRegistry("http://hl7.org/fhir/StructureDefinition/Extension", StructureDefinition.class).getSnapshot())
                .differential(null)
                .build();
    }

    @Override
    protected List<FHIRRegistryResource> getRegistryResources(Class<? extends Resource> resourceType, String url) {
        return registryResources.stream()
                .filter(rr -> rr.getResourceType() == resourceType && rr.getUrl().equals(url))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<FHIRRegistryResource> getRegistryResources(Class<? extends Resource> resourceType) {
        return registryResources.stream()
                .filter(rr -> rr.getResourceType().equals(resourceType))
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<FHIRRegistryResource> getRegistryResources() {
        return registryResources;
    }

    @Override
    public Collection<FHIRRegistryResource> getProfileResources(String type) {
        return Collections.emptySet();
    }

    @Override
    public Collection<FHIRRegistryResource> getSearchParameterResources(String type) {
        return registryResources.stream()
                .filter(rr -> rr.getResourceType() == SearchParameter.class)
                .filter(rr -> ((SearchParameter) rr.getResource()).getType().getValue().equals(type))
                .collect(Collectors.toSet());
    }
}