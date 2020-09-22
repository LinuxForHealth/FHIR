/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dao.api;

import java.util.Collection;
import java.util.List;

import com.ibm.fhir.persistence.jdbc.dao.impl.ExternalResourceReferenceRec;

/**
 * An interface to a cache of entities used for managing references between
 * resources (local or external).
 */
public interface IResourceReferenceCache {

    /**
     * Clear the thread-local and shared caches
     */
    void reset();
    
    /**
     * Take the records we've touched in the current thread and update the
     * shared LRU maps.
     */
    void updateSharedMaps();

    /**
     * Lookup all the database values we have cached for the system and value names
     * in the given collection. Put any objects with cache misses into the corresponding
     * miss lists (so that we know which records we need to generate inserts for
     * @param xrefs
     * @param systemMisses
     * @param valueMisses
     */
    public void resolveExternalReferences(Collection<ExternalResourceReferenceRec> xrefs, 
        List<ExternalResourceReferenceRec> systemMisses, List<ExternalResourceReferenceRec> valueMisses);
}
