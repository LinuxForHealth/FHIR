/**
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.persistence.jdbc.dto;

import java.sql.Timestamp;

/**
 * This class defines the Data Transfer Object representing a row in the FHIR Resource table.
 * @author markd
 *
 */
public class Resource {
    
    private long id;
    private String logicalId;
    private int versionId;
    private String resourceType;
    private Timestamp lastUpdated;
    private byte[] data;
    private boolean deleted;

    
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    
    @Override
    public String toString() {
        return "Resource [id=" + id + ", logicalId=" + logicalId + ", versionId=" + versionId + ", resourceType="
                + resourceType + ", lastUpdated=" + lastUpdated + ", deleted=" + deleted + "]";
    }

}

