/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.persistence.jdbc.dao.api;


/**
 * Interface providing lookups of resource type by name or id
 */
public interface IResourceTypeMaps {

    /**
     * @return true if the implementation has been initialized (the maps are not empty)
     */
    public boolean isInitialized();

    /**
     * Get the resource type name for the given resourceTypeId
     * @param resourceTypeId
     * @return the resource type name
     */
    String getResourceTypeName(int resourceTypeId);

    /**
     * Get the resource type id for the given resourceTypeName
     * @param resourceTypeName
     * @return the resource type id
     */
    int getResourceTypeId(String resourceTypeName);
}
