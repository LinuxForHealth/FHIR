/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.registry.resource;

import java.util.Objects;
import java.util.logging.Logger;

import com.ibm.fhir.model.resource.CodeSystem;
import com.ibm.fhir.model.resource.ConceptMap;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.ValueSet;
import com.ibm.fhir.registry.resource.FHIRRegistryResource;
import com.ibm.fhir.registry.util.FHIRRegistryUtil;

/*
 * A registry resource that contains a CodeSystem, ConceptMap, or ValueSet
 */
public class FHIRTermRegistryResource<T extends Resource> extends FHIRRegistryResource {
    private static final Logger log = Logger.getLogger(FHIRTermRegistryResource.class.getName());

    private final T resource;

    private FHIRTermRegistryResource(String id, String url, Version version, T resource) {
        super(resource.getClass(), id, url, version, null, null);
        this.resource = Objects.requireNonNull(resource, "resource");
    }

    @Override
    public Resource getResource() {
        return resource;
    }

    public static <T extends Resource> FHIRRegistryResource from(T resource) {
        Objects.requireNonNull(resource, "resource");

        Class<?> resourceType = resource.getClass();
        if (!CodeSystem.class.equals(resourceType) && !ConceptMap.class.equals(resourceType) && !ValueSet.class.equals(resourceType)) {
            throw new IllegalArgumentException("Expected resource type: CodeSystem, ConceptMap, or ValueSet but found: " + resourceType.getClass().getSimpleName());
        }

        String id = resource.getId();
        String url = FHIRRegistryUtil.getUrl(resource);
        String version = FHIRRegistryUtil.getVersion(resource);

        if (url == null) {
            log.warning(String.format("Could not create FHIRTermRegistryResource from " + resource.getClass().getSimpleName() + " with: id: %s, url: %s, and version: %s", id, url, version));
            return null;
        }

        return new FHIRTermRegistryResource<T>(id, url, (version != null) ? Version.from(version) : FHIRRegistryResource.NO_VERSION, resource);
    }
}
