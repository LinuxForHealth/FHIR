/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc;

import com.ibm.fhir.persistence.jdbc.dao.api.IResourceReferenceCache;

/**
 * Manages caches separated by tenant
 */
public interface FHIRPersistenceJDBCCache {

    /**
     * Getter for the resource reference cache
     * @return
     */
    IResourceReferenceCache getResourceReferenceCache();

}
