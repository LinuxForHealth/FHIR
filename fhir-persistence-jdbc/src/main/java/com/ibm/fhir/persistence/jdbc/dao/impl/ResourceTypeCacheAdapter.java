/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dao.impl;

import com.ibm.fhir.persistence.jdbc.dao.api.IResourceTypeCache;

/**
 * Adapter to expose a method to obtain the resourceTypeId
 * for a given name
 */
public class ResourceTypeCacheAdapter implements IResourceTypeCache {

    // The dao we are wrapping
    private final ResourceDAOImpl dao;

    /**
     * Public constructor
     * @param dao
     */
    public ResourceTypeCacheAdapter(ResourceDAOImpl dao) {
        this.dao = dao;
    }

    @Override
    public Integer getResourceTypeId(String resourceType) {
        return dao.getResourceTypeIdFromCaches(resourceType);
    }
}
