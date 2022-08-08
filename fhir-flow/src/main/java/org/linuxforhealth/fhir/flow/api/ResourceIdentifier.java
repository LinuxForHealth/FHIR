/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.flow.api;

import java.util.Objects;

import org.linuxforhealth.fhir.model.type.Uri;
import org.linuxforhealth.fhir.model.util.ModelSupport;

/**
 * Represents a resource type/id
 */
public class ResourceIdentifier {
    private final String resourceType;
    private final String logicalId;

    /**
     * Public constructor
     * 
     * @param resourceType
     * @param logicalId
     */
    public ResourceIdentifier(String resourceType, String logicalId) {
        this.resourceType = resourceType;
        this.logicalId = logicalId;
    }

    @Override
    public String toString() {
        return getFullUrl();
    }

    /**
     * Get the fullUrl value which is just resourceType/logicalId
     * @return
     */
    public String getFullUrl() {
        StringBuilder result = new StringBuilder();
        result.append(getResourceType());
        result.append("/");
        result.append(getLogicalId());
        return result.toString();
    }

    /**
     * Parse a Uri and extract the resource type and id fields
     * 
     * @param uri
     */
    public static ResourceIdentifier from(Uri uri) {
        Objects.requireNonNull(uri, "uri");
        Objects.requireNonNull(uri.getValue(), "uri-value");
        return from(uri.getValue());
    }

    /**
     * Parse a URL and extract the resource type and id fields
     * 
     * @param url
     * @return
     */
    public static ResourceIdentifier from(String url) {
        String[] pieces = url.split("/");
        
        // We need the last two components
        if (pieces.length > 1) {
            final String resourceType = pieces[pieces.length-2];
            final String logicalId = pieces[pieces.length-1];
            if (ModelSupport.isResourceType(resourceType)) {
                return new ResourceIdentifier(resourceType, logicalId);
            } else {
                throw new IllegalArgumentException("Not a valid resource url: " + url);
            }
        } else {
            throw new IllegalArgumentException("Not a valid resource url: " + url);
        }
    }

    /**
     * @return the resourceType
     */
    public String getResourceType() {
        return resourceType;
    }

    /**
     * @return the logicalId
     */
    public String getLogicalId() {
        return logicalId;
    }
}