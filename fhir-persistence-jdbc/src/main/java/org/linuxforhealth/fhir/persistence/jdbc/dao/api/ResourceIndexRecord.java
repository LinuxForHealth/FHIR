/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.dao.api;


/**
 * Describes a resource being reindexed
 */
public class ResourceIndexRecord {

    // The LOGICAL_RESOURCES.LOGICAL_RESOURCE_ID database id
    private final long logicalResourceId;

    // The resource type of the resource to reindex
    private final int resourceTypeId;

    // The resource's logical identifier
    private final String logicalId;

    // The resource type, which gets set after we identify the resourceTypeId
    private String resourceType;

    // support for optimistic locking pattern
    private final long transactionId;

    // Deletion flag for the resource. Set when we read the resource
    private boolean deleted;

    // Base64-encoded SHA-256 hash of the search parameters
    private String parameterHash;

    public ResourceIndexRecord(long logicalResourceId, int resourceTypeId, String logicalId, long transactionId, String parameterHash) {
        this.logicalResourceId = logicalResourceId;
        this.resourceTypeId = resourceTypeId;
        this.logicalId = logicalId;
        this.transactionId = transactionId;
        this.parameterHash = parameterHash;
    }

    /**
     * @return the resourceTypeId
     */
    public int getResourceTypeId() {
        return resourceTypeId;
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

    /**
     * @return the resourceType
     */
    public String getResourceType() {
        return resourceType;
    }

    /**
     * @param resourceType the resourceType to set
     */
    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    /**
     * @return the transactionId
     */
    public long getTransactionId() {
        return transactionId;
    }

    /**
     * @return the deleted
     */
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * @param deleted the deleted to set
     */
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * Gets the Base64-encoded SHA-256 hash of the search parameters.
     * @return the Base64-encoded SHA-256 hash
     */
    public String getParameterHash() {
        return parameterHash;
    }

    /**
     * Gets the Base64-encoded SHA-256 hash of the search parameters.
     * @param parameterHash the Base64-encoded SHA-256 hash
     */
    public void setParameterHash(String parameterHash) {
        this.parameterHash = parameterHash;
    }
}
