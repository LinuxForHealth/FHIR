/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.bucket.api;


/**
 * Container for a resource type and logical id
 */
public class ResourceRef {
    private final String resourceType;
    
    private final String logicalId;

    /**
     * Public constructor
     * @param resourceType
     * @param logicalId
     */
    public ResourceRef(String resourceType, String logicalId) {
        this.resourceType = resourceType;
        this.logicalId = logicalId;
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
