/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.payload;

import java.util.concurrent.Future;

/**
 * A key used to identify a payload object stored by the payload persistence layer
 */
public class PayloadPersistenceResponse {

    // The UUID value used to tie together the RDBMS and offload records
    private final String resourcePayloadKey;
    
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
    
    // The (future) result status of the async persistence call
    private final Future<PayloadPersistenceResult> result;

    /**
     * Public constructor
     * @param resourceTypeName
     * @param resourceTypeId
     * @param logicalId
     * @param versionId
     * @param partitionKey
     * @param result
     */
    public PayloadPersistenceResponse(String resourcePayloadKey, String resourceTypeName, int resourceTypeId, String logicalId, int versionId, String partitionKey,
        Future<PayloadPersistenceResult> result) {
        this.resourcePayloadKey = resourcePayloadKey;
        this.resourceTypeName = resourceTypeName;
        this.resourceTypeId = resourceTypeId;
        this.logicalId = logicalId;
        this.versionId = versionId;
        this.partitionKey = partitionKey;
        this.result = result;
    }
    
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("partitionKey[");
        result.append(partitionKey);
        result.append("]/");
        result.append(resourceTypeName);
        result.append("[");
        result.append(resourceTypeId);
        result.append("]/");
        result.append(logicalId);
        result.append("/");
        result.append(versionId);
        result.append("/");
        result.append(this.resourcePayloadKey);
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
     * @return the resourcePayloadKey
     */
    public String getResourcePayloadKey() {
        return resourcePayloadKey;
    }

    /**
     * @return the result
     */
    public Future<PayloadPersistenceResult> getResult() {
        return result;
    }
}