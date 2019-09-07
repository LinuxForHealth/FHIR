/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.conformance;

import static com.ibm.watson.health.fhir.conformance.util.ConformanceUtil.loadResource;

import com.ibm.watson.health.fhir.model.format.Format;
import com.ibm.watson.health.fhir.model.resource.Resource;

public class ConformanceResource {
    private final String url;
    private final String name;
    private final Format format;
    private final ClassLoader loader;
    
    private volatile Resource resource;
    
    public ConformanceResource(String url, String name, Format format, ClassLoader loader) {
        this.url = url;
        this.name = name;
        this.format = format;
        this.loader = loader;
    }
    
    public String getUrl() {
        return url;
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
}
