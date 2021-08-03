/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dao.api;

import java.util.Collection;
import java.util.Map;

/**
 * Interface to a cache mapping a string to a value of type T. Supports
 * thread-local caching to support temporary staging of values pending
 * successful completion of a transaction.
 * @param<T>
 */
public interface INameIdCache<T> {

    /**
     * Get the resource type identifier for the given resourceType name
     * @param resourceType
     * @return
     */
    T getId(String resourceType);
    
    /**
     * Get all resource type identifiers in the cache
     * @return
     */
    Collection<T> getAllIds();
    
    /**
     * Add the resource type to the local cache
     * @param resourceType
     * @param resourceTypeId
     */
    void addEntry(String key, T id);

    /**
     * Called after a transaction commit() to transfer all the staged (thread-local) data
     * over to the shared cache.
     */
    void updateSharedMaps();

    /**
     * Clear both local  shared caches - useful for unit tests
     */
    void reset();
    
    /**
     * Clear anything cached in thread-local (after transaction rollback, for example)
     */
    void clearLocalMaps();

    /**
     * Prefill the shared map with the given content (must come data already 
     * committed in the database)
     * @param content
     */
    void prefill(Map<String,T> content);
}