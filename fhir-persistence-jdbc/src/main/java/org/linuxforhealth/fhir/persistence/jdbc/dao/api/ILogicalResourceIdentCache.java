/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.dao.api;

import java.util.Collection;
import java.util.List;

import org.linuxforhealth.fhir.persistence.jdbc.dao.impl.ResourceReferenceValueRec;

/**
 * An interface for a cache of logical_resource_ident records. The
 * cache is specialized in that it supports some specific operations to
 * process list of objects with minimal locking.
 *
 */
public interface ILogicalResourceIdentCache {

    /**
     * Take the records we've touched in the current thread and update the
     * shared LRU maps.
     */
    void updateSharedMaps();

    /**
     * Lookup all the database values we have cached for the given collection. 
     * Put any objects with cache misses into the corresponding
     * miss lists (so that we know which records we need to generate inserts for)
     * @param referenceValues
     * @param misses the objects we couldn't find in the cache
     */
    void resolveReferenceValues(Collection<ResourceReferenceValueRec> referenceValues,
        List<ResourceReferenceValueRec> misses);

     /**
      * Add the LogicalResourceIdent key and id to the local cache
      * @param key
      * @param id
      */
    public void addRecord(LogicalResourceIdentKey key, long id);

    /**
     * Clear any thread-local cache maps (probably because a transaction was rolled back)
     */
    void clearLocalMaps();

    /**
     * Clear the thread-local and shared caches (for test purposes)
     */
    void reset();

    /**
     * Get the database logical_resourc_id for the given resource type
     * and logicalId
     * @param resourceTypeId
     * @param logicalId
     * @return
     */
    Long getLogicalResourceId(int resourceTypeId, String logicalId);
}
