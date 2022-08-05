/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.persistence.params.model;


/**
 * A DTO representing a record from the resource_types table
 */
public class ResourceTypeValue {
    private final String resourceType;
    private final int resourceTypeId;

    /**
     * Canonical constructor
     * @param resourceType
     * @param resourceTypeId
     */
    public ResourceTypeValue(String resourceType, int resourceTypeId) {
        this.resourceType = resourceType;
        this.resourceTypeId = resourceTypeId;
    }

    
    /**
     * @return the resourceType
     */
    public String getResourceType() {
        return resourceType;
    }

    
    /**
     * @return the resourceTypeId
     */
    public int getResourceTypeId() {
        return resourceTypeId;
    }
}
