/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dao.api;


/**
 * Describes a resource being reindexed
 */
public class ResourceIndexRecord {

    // The resource type of the resource to reindex
    private final String resourceType;
    
    // The resource's logical identifier
    private final String logicalId;

    // The LOGICAL_RESOURCES.LOGICAL_RESOURCE_ID database id
    private final long logicalResourceId;
    
    public ResourceIndexRecord(String resourceType, String logicalId, long logicalResourceId) {
        this.resourceType = resourceType;
        this.logicalId = logicalId;
        this.logicalResourceId = logicalResourceId;
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

    /**
     * @return the logicalResourceId
     */
    public long getLogicalResourceId() {
        return logicalResourceId;
    }
}
