/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.linuxforhealth.fhir.persistence.jdbc.dao.api.ILogicalResourceIdentCache;
import org.linuxforhealth.fhir.persistence.jdbc.dao.api.LogicalResourceIdentKey;
import org.linuxforhealth.fhir.persistence.jdbc.dao.impl.ResourceReferenceValueRec;


/**
 * Implementation of a cache used for lookups of entities related
 * to local and external resource references
 */
public class LogicalResourceIdentCacheImpl implements ILogicalResourceIdentCache {

    // We use LinkedHashMap for the local map because we also need to maintain order
    // of insertion to make sure we have correct LRU behavior when updating the shared cache
    private final ThreadLocal<LinkedHashMap<LogicalResourceIdentKey, Long>> localLogicalResourceIdents = new ThreadLocal<>();

    // The lru token values cache shared at the server level
    private final LRUCache<LogicalResourceIdentKey, Long> cache;

    /**
     * Public constructor
     * @param logicalResourceCacheSize
     */
    public LogicalResourceIdentCacheImpl(int logicalResourceCacheSize) {

        // LRU cache for quick lookup of code-systems and token-values
        cache = new LRUCache<>(logicalResourceCacheSize);
    }

    /**
     * Called after a transaction commit() to transfer all the staged (thread-local) data
     * over to the shared LRU cache.
     */
    @Override
    public void updateSharedMaps() {

        LinkedHashMap<LogicalResourceIdentKey,Long> localMap = localLogicalResourceIdents.get();
        if (localMap != null) {
            synchronized(this.cache) {
                cache.update(localMap);
            }

            // clear the thread-local cache
            localMap.clear();
        }
    }

    @Override
    public Long getLogicalResourceId(int resourceTypeId, String logicalId) {
        // check the thread-local map first
        Long result = null;

        LogicalResourceIdentKey key = new LogicalResourceIdentKey(resourceTypeId, logicalId);
        Map<LogicalResourceIdentKey, Long> localMap = this.localLogicalResourceIdents.get();
        if (localMap != null) {
            result = localMap.get(key);

            if (result != null) {
                return result;
            }
        }

        // See if it's in the shared cache
        synchronized (this.cache) {
            result = cache.get(key);
        }

        if (result != null) {
            // We found it in the shared cache, so update our thread-local
            // cache.
            addRecord(key, result);
        }

        return result;
    }

    @Override
    public void resolveReferenceValues(Collection<ResourceReferenceValueRec> values, List<ResourceReferenceValueRec> misses) {
        // Make one pass over the collection and resolve as much as we can in one go. Anything
        // we can't resolve gets put into the corresponding missing lists. Worst case is two passes, when
        // there's nothing in the local cache and we have to then look up everything in the shared cache

        // See what we have currently in our thread-local cache
        LinkedHashMap<LogicalResourceIdentKey,Long> valMap = localLogicalResourceIdents.get();

        List<ResourceReferenceValueRec> foundKeys = new ArrayList<>(values.size()); // for updating LRU
        List<ResourceReferenceValueRec> needToFindValues = new ArrayList<>(values.size()); // for the ref values we haven't yet found
        for (ResourceReferenceValueRec tv: values) {
            if (valMap != null) {
                LogicalResourceIdentKey key = new LogicalResourceIdentKey(tv.getRefResourceTypeId(), tv.getRefLogicalId());
                Long id = valMap.get(key);
                if (id != null) {
                    foundKeys.add(tv);
                    tv.setRefLogicalResourceId(id);
                } else {
                    // not found, so add to the cache miss list
                    needToFindValues.add(tv);
                }
            } else {
                needToFindValues.add(tv);
            }
        }

        // If we still have keys to find, look them up in the shared cache (which we need to lock first)
        if (needToFindValues.size() > 0) {
            synchronized (this.cache) {
                for (ResourceReferenceValueRec tv: needToFindValues) {
                    LogicalResourceIdentKey key = new LogicalResourceIdentKey(tv.getRefResourceTypeId(), tv.getRefLogicalId());
                    Long id = cache.get(key);
                    if (id != null) {
                        tv.setRefLogicalResourceId(id);

                        // Update the local cache with this value
                        addRecord(key, id);
                    } else {
                        // cache miss so add this record to the miss list for further processing
                        misses.add(tv);
                    }
                }
            }
        }
    }


    @Override
    public void addRecord(LogicalResourceIdentKey key, long id) {
        LinkedHashMap<LogicalResourceIdentKey,Long> map = localLogicalResourceIdents.get();

        if (map == null) {
            map = new LinkedHashMap<>();
            localLogicalResourceIdents.set(map);
        }

        // add the id to the thread-local cache. The shared cache is updated
        // only if a call is made to #updateSharedMaps()
        map.put(key, id);
    }

    @Override
    public void reset() {
        localLogicalResourceIdents.remove();

        // clear the shared caches too
        synchronized (this.cache) {
            this.cache.clear();
        }
    }

    @Override
    public void clearLocalMaps() {
        // clear the maps, but keep the maps in place because they'll be used again
        // the next time this thread is picked from the pool
        LinkedHashMap<LogicalResourceIdentKey,Long> map = localLogicalResourceIdents.get();

        if (map != null) {
            map.clear();
        }
    }
}