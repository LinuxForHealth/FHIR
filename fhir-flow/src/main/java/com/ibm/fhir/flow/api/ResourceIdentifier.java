/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.flow.api;

import java.util.Objects;

import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.util.ModelSupport;

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