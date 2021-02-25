/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.registry;

import java.util.Objects;
import java.util.logging.Logger;

import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.SearchParameter;
import com.ibm.fhir.model.resource.StructureDefinition;
import com.ibm.fhir.registry.resource.FHIRRegistryResource;
import com.ibm.fhir.registry.util.FHIRRegistryUtil;

public class ServerRegistryResource extends FHIRRegistryResource {
    private static final Logger log = Logger.getLogger(ServerRegistryResource.class.getName());

    private final Resource resource;

    public ServerRegistryResource(
            Class<? extends Resource> resourceType,
            String id,
            String url,
            Version version,
            String kind,
            String type,
            Resource resource) {
        super(resourceType, id, url, version, kind, type);
        this.resource = Objects.requireNonNull(resource);
    }

    @Override
    public Resource getResource() {
        return resource;
    }

    public static ServerRegistryResource from(Resource resource) {
        Class<? extends Resource> resourceType = resource.getClass();
        String id = resource.getId();
        String url = FHIRRegistryUtil.getUrl(resource);
        String version = FHIRRegistryUtil.getVersion(resource);
        if (url == null) {
            log.warning(String.format("Could not create ServerRegistryResource from Resource with resourceType: %s, id: %s, url: %s, and version: %s", resourceType.getSimpleName(), id, url, version));
            return null;
        }
        String kind = null;
        String type = null;
        if (resource instanceof StructureDefinition) {
            StructureDefinition structureDefinition = (StructureDefinition) resource;
            kind = structureDefinition.getKind().getValue();
            type = structureDefinition.getType().getValue();
        } else if (resource instanceof SearchParameter) {
            SearchParameter searchParameter = (SearchParameter) resource;
            type = searchParameter.getType().getValue();
        }
        return new ServerRegistryResource(resourceType, id, url, (version != null) ? Version.from(version) : FHIRRegistryResource.NO_VERSION, kind, type, resource);
    }
}
