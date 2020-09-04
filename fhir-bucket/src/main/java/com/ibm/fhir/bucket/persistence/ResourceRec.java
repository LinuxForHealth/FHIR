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
    private final long resourceBundleLoadId;
    private final int lineNumber;

    public ResourceRec(int resourceTypeId, String logicalId, long resourceBundleLoadId, int lineNumber) {
        this.logicalId = logicalId;
        this.resourceTypeId = resourceTypeId;
        this.resourceBundleLoadId = resourceBundleLoadId;
        this.lineNumber = lineNumber;
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
    public long getResourceBundleLoadId() {
        return resourceBundleLoadId;
    }


    /**
     * @return the lineNumber
     */
    public int getLineNumber() {
        return lineNumber;
    }
}