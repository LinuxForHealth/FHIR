/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.jdbc.dao.api;


/**
 * A DTO used to represent a resource record stored in Cassandra
 */
public class ResourceRecord {
    private final int resourceTypeId;
    private final String logicalId;
    private final int version;
    private final String resourcePayloadKey;

    // The original offload path obtained from the offload store. Can be null
    private final String offloadPath;

    /**
     * Public constructor
     * @param resourceTypeId
     * @param logicalId
     * @param version
     * @param resourcePayloadKey
     * @param offloadPath
     */
    public ResourceRecord(int resourceTypeId, String logicalId, int version, String resourcePayloadKey, String offloadPath) {
        this.resourceTypeId = resourceTypeId;
        this.logicalId = logicalId;
        this.version = version;
        this.resourcePayloadKey = resourcePayloadKey;
        this.offloadPath = offloadPath;
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
     * @return the version
     */
    public int getVersion() {
        return version;
    }

    /**
     * @return the resourcePayloadKey
     */
    public String getResourcePayloadKey() {
        return resourcePayloadKey;
    }

    /**
     * @return the offloadPath
     */
    public String getOffloadPath() {
        return this.offloadPath;
    }
}