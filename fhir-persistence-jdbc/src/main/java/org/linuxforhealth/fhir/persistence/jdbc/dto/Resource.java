/*
 * (C) Copyright IBM Corp. 2017, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.dto;

import java.sql.Timestamp;

import org.linuxforhealth.fhir.persistence.InteractionStatus;
import org.linuxforhealth.fhir.persistence.util.InputOutputByteStream;

/**
 * This class defines the Data Transfer Object representing a row in the FHIR Resource table.
 */
public class Resource {
    
    /**
     *  This is the <resourceType>_RESOURCES.RESOURCE_ID column. It is unique for a specific version
     *  of a resource. It is not used during create/update interactions.
     */
    private long resourceId;
    
    /**
     *  This is the <resourceType>_LOGICAL_RESOURCES.LOGICAL_RESOURCE_ID column. It is used during
     *  create/update interactions as well as read interactions
     */
    private long logicalResourceId;

    /**
     *  This is the <resourceType>_LOGICAL_RESOURCES.LOGICAL_ID column
     */
    private String logicalId;

    /**
     *  This is the <resourceType>_RESOURCES.VERSION_ID column
     */
    private int versionId;
    
    /**
     *  This is the resource type.
     */
    private String resourceType;
    
    /**
     * The resource type id set when reading resources from the database
     */
    private int resourceTypeId = -1;

    /**
     *  This is the <resourceType>_RESOURCES.LAST_UPDATED column
     */
    private Timestamp lastUpdated;
    
    /**
     *  This is the buffer holding the payload data of the <resourceType>_RESOURCES.DATA column
     */
    private InputOutputByteStream dataStream;
    
    /**
     *  This is the <resourceType>_RESOURCES.IS_DELETED column
     */
    private boolean deleted;
    
    /**
     * The status of the interaction at the database - what change was actually made and why
     */
    private InteractionStatus interactionStatus;

    /**
     * The version of the resource found if we hit IfNoneMatch
     */
    private Integer ifNoneMatchVersion;
    
    /**
     * A unique key (UUID value) used to tie the RDBMS record with the offloaded payload
     */
    private String resourcePayloadKey;

    /**
     * The existing parameter hash value
     */
    private String currentParameterHash;
    
    public Resource() {
        super();
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public Timestamp getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Timestamp lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    /**
     * Setter for the ifNoneMatchVersion value
     * @param version
     */
    public void setIfNoneMatchVersion(Integer version) {
        this.ifNoneMatchVersion = version;
    }

    /**
     * Getter for the ifNoneMatchVersion value
     * @return
     */
    public Integer getIfNoneMatchVersion() {
        return this.ifNoneMatchVersion;
    }

    /**
     * Getter for the database xx_resources.resource_id value
     * @return
     */
    public long getResourceId() {
        return resourceId;
    }

    /**
     * Setter for the database xx_resources.resource_id value
     * @param id
     */
    public void setResourceId(long id) {
        this.resourceId = id;
    }

    /**
     * Getter for the logical_resources.logical_resource_id value
     * @return
     */
    public long getLogicalResourceId() {
        return logicalResourceId;
    }

    /**
     * Setter for the logical_resources.logical_resource_id value
     * @param logicalResourceId
     */
    public void setLogicalResourceId(long logicalResourceId) {
        this.logicalResourceId = logicalResourceId;
    }

    public String getLogicalId() {
        return logicalId;
    }

    public void setLogicalId(String logicalId) {
        this.logicalId = logicalId;
    }

    public int getVersionId() {
        return versionId;
    }

    public void setVersionId(int versionId) {
        this.versionId = versionId;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "Resource [id=" + resourceId + ", logicalResourceId=" + logicalResourceId + ", logicalId=" + logicalId +
                ", versionId=" + versionId + ", resourceType=" + resourceType + ", lastUpdated=" + lastUpdated +
                ", deleted=" + deleted + "]";
    }

    /**
     * @return the dataStream
     */
    public InputOutputByteStream getDataStream() {
        return dataStream;
    }

    /**
     * @param dataStream the dataStream to set
     */
    public void setDataStream(InputOutputByteStream dataStream) {
        this.dataStream = dataStream;
    }

    /**
     * @return the interactionStatus
     */
    public InteractionStatus getInteractionStatus() {
        return interactionStatus;
    }

    /**
     * @param interactionStatus the interactionStatus to set
     */
    public void setInteractionStatus(InteractionStatus interactionStatus) {
        this.interactionStatus = interactionStatus;
    }

    /**
     * @return the resourceTypeId
     */
    public int getResourceTypeId() {
        return resourceTypeId;
    }

    /**
     * @param resourceTypeId the resourceTypeId to set
     */
    public void setResourceTypeId(int resourceTypeId) {
        this.resourceTypeId = resourceTypeId;
    }

    /**
     * @return the resourcePayloadKey
     */
    public String getResourcePayloadKey() {
        return resourcePayloadKey;
    }

    /**
     * @param resourcePayloadKey the resourcePayloadKey to set
     */
    public void setResourcePayloadKey(String resourcePayloadKey) {
        this.resourcePayloadKey = resourcePayloadKey;
    }

    /**
     * @return the currentParameterHash
     */
    public String getCurrentParameterHash() {
        return currentParameterHash;
    }

    /**
     * @param currentParameterHash the currentParameterHash to set
     */
    public void setCurrentParameterHash(String currentParameterHash) {
        this.currentParameterHash = currentParameterHash;
    }
}

