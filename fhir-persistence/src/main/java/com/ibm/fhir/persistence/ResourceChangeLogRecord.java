/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence;

import java.time.Instant;

/**
 * Represents a record read from the RESOURCE_CHANGE_LOG
 */
public class ResourceChangeLogRecord {
    public static enum ChangeType {
        CREATE, UPDATE, DELETE
    }

    // The type of the resource
    private final String resourceTypeName;

    // The logical identifier of the resource
    private final String logicalId;

    // The version number of the resource
    private final int versionId;

    // The resource id (PK of the change log)
    private final long resourceId;

    // The change tstamp in the change log
    private final Instant changeTstamp;

    // The type of change which modified the resource
    private final ChangeType changeType;


    public ResourceChangeLogRecord(String resourceTypeName, String logicalId, int versionId, long resourceId, Instant changeTstamp, ChangeType ct) {
        this.resourceTypeName = resourceTypeName;
        this.resourceId = resourceId;
        this.logicalId = logicalId;
        this.versionId = versionId;
        this.changeTstamp = changeTstamp;
        this.changeType = ct;
    }

    /**
     * @return the resourceTypeName
     */
    public String getResourceTypeName() {
        return resourceTypeName;
    }


    /**
     * @return the resourceId
     */
    public long getResourceId() {
        return resourceId;
    }

    /**
     * @return the changeType
     */
    public ChangeType getChangeType() {
        return changeType;
    }

    /**
     * @return the changeTstamp
     */
    public Instant getChangeTstamp() {
        return changeTstamp;
    }

    /**
     * @return the versionId
     */
    public int getVersionId() {
        return versionId;
    }

    /**
     * @return the logicalId
     */
    public String getLogicalId() {
        return logicalId;
    }
}
