/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.persistence.payload;

import java.util.concurrent.Future;

/**
 * Data carrier encapsulating the response from the payload persistence component
 * when making a call to offload the resource payload.
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
    
    // The (future) result status of the async persistence call
    private final Future<PayloadPersistenceResult> result;

    /**
     * Public constructor
     * @param resourcePayloadKey
     * @param resourceTypeName
     * @param resourceTypeId
     * @param logicalId
     * @param versionId
     * @param result
     */
    public PayloadPersistenceResponse(String resourcePayloadKey, String resourceTypeName, int resourceTypeId, String logicalId, int versionId,
            Future<PayloadPersistenceResult> result) {
        this.resourcePayloadKey = resourcePayloadKey;
        this.resourceTypeName = resourceTypeName;
        this.resourceTypeId = resourceTypeId;
        this.logicalId = logicalId;
        this.versionId = versionId;
        this.result = result;
    }
    
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
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