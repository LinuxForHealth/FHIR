/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bucket.persistence;

/**
 * DTO for adding resources to the loader tracking database
 */
public class ResourceRec {
    private final int resourceTypeId;
    private final String logicalId;
    private final long resourceBundleId;

    public ResourceRec(int resourceTypeId, String logicalId, long resourceBundleId) {
        this.logicalId = logicalId;
        this.resourceTypeId = resourceTypeId;
        this.resourceBundleId = resourceBundleId;
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
     * @return the resourceBundleId
     */
    public long getResourceBundleId() {
        return resourceBundleId;
    }
}