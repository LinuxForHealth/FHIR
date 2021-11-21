/*
 * (C) Copyright IBM Corp. 2017,2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dto;

import java.sql.Timestamp;

import com.ibm.fhir.persistence.InteractionStatus;
import com.ibm.fhir.persistence.util.InputOutputByteStream;

/**
 * This class defines the Data Transfer Object representing a row in the FHIR Resource table.
 */
public class Resource {
    
    /**
     *  This is the <resourceType>_RESOURCES.RESOURCE_ID column
     */
    private long id;
    
    /**
     *  This is the <resourceType>_LOGICAL_RESOURCES.LOGICAL_RESOURCE_ID column. It is only
     *  set when this DTO is used to read table data. It is not set when the DTO is
     *  used to insert/update.
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getLogicalResourceId() {
        return logicalResourceId;
    }
    
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
        return "Resource [id=" + id + ", logicalResourceId=" + logicalResourceId + ", logicalId=" + logicalId +
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
}

