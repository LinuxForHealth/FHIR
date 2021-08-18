/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dao.api;

import java.util.Collection;
import java.util.Map;

/**
 * Interface to a cache mapping an id of type T to a string. Supports
 * thread-local caching to support temporary staging of values pending
 * successful completion of a transaction.
 * @param<T>
 */
public interface IIdNameCache<T> {

    /**
     * Get the name for the given id
     * @param id
     * @return
     */
    String getName(T id);
    
    /**
     * Get all names in the cache
     * @return
     */
    Collection<String> getAllNames();
    
    /**
     * Add the entry to the local cache
     * @param id
     * @param name
     */
    void addEntry(T id, String name);

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
    void prefill(Map<T,String> content);
}