/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.payload;

/**
 * A key used to identify a payload object stored by the payload persistence layer
 */
public class PayloadKey {

    // The string name of the resource type
    private final String resourceTypeName;
    
    // The database-assigned id for the resource type
    private final int resourceTypeId;
    
    // The resource logical identifier
    private final String logicalId;
    
    // The version id of the resource
    private final int versionId;
    
    // Identifies the partition used to store the payload in a partitioned system (like Cassandra)
    private final String partitionKey;
    
    // The identifier assigned by the payload persistence layer
    private final String payloadId;

    // The status of the payload persistence operation
    private final Status status;

    /**
     * Enumeration of status types
     */
    public static enum Status {
        OK, FAILED
    }
    
    /**
     * Public constructor
     * @param resourceTypeName
     * @param resourceTypeId
     * @param logicalId
     * @param versionId
     * @param partitionKey
     * @param payloadId
     * @param status
     */
    public PayloadKey(String resourceTypeName, int resourceTypeId, String logicalId, int versionId, String partitionKey, String payloadId,
        Status status) {
        this.resourceTypeName = resourceTypeName;
        this.resourceTypeId = resourceTypeId;
        this.logicalId = logicalId;
        this.versionId = versionId;
        this.partitionKey = partitionKey;
        this.payloadId = payloadId;
        this.status = status;
    }
    
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(partitionKey);
        result.append("-");
        result.append(payloadId);
        result.append("[");
        result.append(resourceTypeName);
        result.append("/");
        result.append(logicalId);
        result.append("/");
        result.append(versionId);
        result.append("]");
        return result.toString();
    }
    
    /**
     * @return the resourceTypeName
     */
    public String getResourceTypeName() {
        return resourceTypeName;
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
     * @return the versionId
     */
    public int getVersionId() {
        return versionId;
    }
    
    /**
     * @return the partitionKey
     */
    public String getPartitionKey() {
        return partitionKey;
    }
    
    /**
     * @return the payloadId
     */
    public String getPayloadId() {
        return payloadId;
    }

    /**
     * @return the status
     */
    public Status getStatus() {
        return status;
    }
}