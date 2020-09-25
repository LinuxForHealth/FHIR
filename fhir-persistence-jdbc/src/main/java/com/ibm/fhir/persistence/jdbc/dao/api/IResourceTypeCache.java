/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dao.api;


/**
 * Interface to a cache for obtaining resource type information
 */
public interface IResourceTypeCache {

    /**
     * Get the resource type identifier for the given resourceType name
     * @param resourceType
     * @return
     */
    Integer getResourceTypeId(String resourceType);
}
