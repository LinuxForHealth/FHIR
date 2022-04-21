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
 * A resource type, id and version tuple
 */
public class ResourceIdentifierVersion extends ResourceIdentifier {
    // the resource meta.versionId value
    private final String versionId;

    /**
     * Public constructor
     * 
     * @param resourceType
     * @param logicalId
     * @param versionId
     */
    public ResourceIdentifierVersion(String resourceType, String logicalId, String versionId) {
        super(resourceType, logicalId);
        this.versionId = versionId;
    }

    /**
     * Parse a location uri and extract the resource type, logical id and version
     * 
     * @param uri
     * @return
     */
    public static ResourceIdentifierVersion from(Uri uri) {
        Objects.requireNonNull(uri, "uri");
        Objects.requireNonNull(uri.getValue(), "uri-value");
        return from(uri.getValue());
    }

    /**
     * Parse a location string and extract the resource type, logical id and version
     * 
     * @param location
     * @return
     */
    public static ResourceIdentifierVersion from(String location) {
        String[] pieces = location.split("/");
        
        // We need the last 4 components
        if (pieces.length > 3) {
            final String resourceType = pieces[pieces.length-4];
            final String logicalId = pieces[pieces.length-3];
            final String history = pieces[pieces.length-2];
            final String versionStr = pieces[pieces.length-1];
            if (ModelSupport.isResourceType(resourceType) && "_history".equals(history)) {
                return new ResourceIdentifierVersion(resourceType, logicalId, versionStr);
            } else {
                throw new IllegalArgumentException("Not a valid resource url: " + location);
            }
        } else {
            throw new IllegalArgumentException("Not a valid resource url: " + location);
        }
    }

    /**
     * @return the meta versionId value
     */
    public String getVersionId() {
        return versionId;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(getResourceType());
        result.append("/");
        result.append(getLogicalId());
        result.append("/_history/");
        result.append(getVersionId());
        return result.toString();
    }
}
