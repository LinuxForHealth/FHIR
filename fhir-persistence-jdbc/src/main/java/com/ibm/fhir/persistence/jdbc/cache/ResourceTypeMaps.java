/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.jdbc.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibm.fhir.persistence.jdbc.dao.api.IResourceTypeMaps;
import com.ibm.fhir.schema.model.ResourceType;

/**
 * Maps to support easy lookup of resource type by name or id
 */
public class ResourceTypeMaps implements IResourceTypeMaps {
    // Support for looking up resource types by id or name
    private final Map<Integer,String> resourceTypeByIdMap = new HashMap<>();
    private final Map<String,Integer> resourceTypeByNameMap = new HashMap<>();

    /**
     * Initialize the lookup maps using the given {@link ResourceType} list
     * @param resourceTypes
     */
    public void init(List<ResourceType> resourceTypes) {
        for (ResourceType rt: resourceTypes) {
            this.resourceTypeByIdMap.put(rt.getId(), rt.getName());
            this.resourceTypeByNameMap.put(rt.getName(), rt.getId());
        }
    }

    @Override
    public String getResourceTypeName(int resourceTypeId) {
        if (this.resourceTypeByIdMap.isEmpty()) {
            throw new IllegalStateException("Resource type map not initialized");
        }

        String result = this.resourceTypeByIdMap.get(resourceTypeId);
        if (result == null) {
            throw new IllegalArgumentException("Invalid resourceTypeId: " + resourceTypeId);
        }
        return result;
    }

    @Override
    public int getResourceTypeId(String resourceTypeName) {
        if (this.resourceTypeByNameMap.isEmpty()) {
            throw new IllegalStateException("Resource type map not initialized");
        }
        Integer result = this.resourceTypeByNameMap.get(resourceTypeName);
        if (result == null) {
            throw new IllegalArgumentException("Invalid resourceTypeName: '" + resourceTypeName + "'");
        }
        return result;
    }

    @Override
    public boolean isInitialized() {
        return this.resourceTypeByIdMap.size() > 0;
    }
}
