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
    private final int version;

    /**
     * Public constructor
     * 
     * @param resourceType
     * @param logicalId
     * @param version
     */
    public ResourceIdentifierVersion(String resourceType, String logicalId, int version) {
        super(resourceType, logicalId);
        this.version = version;
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
                return new ResourceIdentifierVersion(resourceType, logicalId, Integer.parseInt(versionStr));
            } else {
                throw new IllegalArgumentException("Not a valid resource url: " + location);
            }
        } else {
            throw new IllegalArgumentException("Not a valid resource url: " + location);
        }
    }

    /**
     * @return the version
     */
    public int getVersion() {
        return version;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(getResourceType());
        result.append("/");
        result.append(getLogicalId());
        result.append("/_history/");
        result.append(this.version);
        return result.toString();
    }
}
