/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.registry.resource;

import static com.ibm.watson.health.fhir.registry.util.FHIRRegistryUtil.loadResource;

import java.util.Comparator;
import java.util.Objects;

import com.ibm.watson.health.fhir.model.format.Format;
import com.ibm.watson.health.fhir.model.resource.Resource;

public class FHIRRegistryResource {
    public static final Comparator<FHIRRegistryResource> VERSION_COMPARATOR = new Comparator<FHIRRegistryResource>() {
        @Override
        public int compare(FHIRRegistryResource first, FHIRRegistryResource second) {
            return first.version.compareTo(second.version);
        }
    };
    
    private final String url;
    private final String version;
    private final String name;
    private final Format format;
    private final ClassLoader loader;
    
    private volatile Resource resource;
    
    public FHIRRegistryResource(String url, String version, String name, Format format, ClassLoader loader) {
        this.url = Objects.requireNonNull(url);
        this.version = Objects.requireNonNull(version);
        this.name = Objects.requireNonNull(name);
        this.format = Objects.requireNonNull(format);
        this.loader = Objects.requireNonNull(loader);
    }
    
    public String getUrl() {
        return url;
    }
    
    public String getVersion() {
        return version;
    }
    
    public Resource getResource() {
        Resource resource = this.resource;
        if (resource == null) {
            synchronized (this) {
                resource = this.resource;
                if (resource == null) {
                    resource = loadResource(name, format, loader);
                    this.resource = resource;
                }
            }
        }
        return resource;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        FHIRRegistryResource other = (FHIRRegistryResource) obj;
        return Objects.equals(url, other.url) && 
                Objects.equals(version, other.version);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(url, version);
    }
}