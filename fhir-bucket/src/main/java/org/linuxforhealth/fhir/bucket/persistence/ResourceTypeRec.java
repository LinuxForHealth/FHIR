/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.bucket.persistence;


/**
 * A DTO representing resource type records read from the database
 */
public class ResourceTypeRec {
    private final int resourceTypeId;
    private final String resourceType;
    
    public ResourceTypeRec(int resourceTypeId, String resourceType) {
        this.resourceTypeId = resourceTypeId;
        this.resourceType = resourceType;
    }
    
    /**
     * Getter for resourceTypeId
     * @return
     */
    public int getResourceTypeId() {
        return this.resourceTypeId;
    }

    /**
     * Getter for resourceType
     * @return
     */
    public String getResourceType() {
        return this.resourceType;
    }
}
