/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.registry.util;

import static com.ibm.fhir.registry.util.FHIRRegistryUtil.loadResource;

import java.util.Objects;

import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.registry.resource.FHIRRegistryResource;

/**
 * A FHIR registry resource that can load a FHIR resource given a path
 */
public class PackageRegistryResource extends FHIRRegistryResource {
    protected final String path;

    public PackageRegistryResource(
            Class<? extends Resource> resourceType,
            String id,
            String url,
            Version version,
            String kind,
            String type,
            String path) {
        super(resourceType, id, url, version, kind, type);
        this.path = Objects.requireNonNull(path);
    }

    public String getPath() {
        return path;
    }

    /**
     * Get the FHIR resource associated with this registry resource
     *
     * @return
     *     the FHIR resource associated with this registry resource
     */
    @Override
    public Resource getResource() {
        Resource resource = this.resource;
        if (resource == null) {
            synchronized (this) {
                resource = this.resource;
                if (resource == null) {
                    resource = loadResource(path);
                    this.resource = resource;
                }
            }
        }
        return resource;
    }
}
