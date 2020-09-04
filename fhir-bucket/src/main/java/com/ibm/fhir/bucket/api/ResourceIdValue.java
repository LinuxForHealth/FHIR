/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bucket.api;


/**
 * A DTO for passing resource-type/id pair
 */
public class ResourceIdValue {

    // the resource type name
    private final String resourceType;
    
    // the logical id
    private final String logicalId;

    /**
     * Public constructor
     * @param resourceType
     * @param logicalId
     */
    public ResourceIdValue(String resourceType, String logicalId) {
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
