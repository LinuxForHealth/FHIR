/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dao.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.ibm.fhir.persistence.jdbc.dao.api.IResourceReferenceCache;


/**
 * Implementation of a cache used for lookups of entities related
 * to local and external resource references
 */
public class ResourceReferenceCacheImpl implements IResourceReferenceCache {

    // We use LinkedHashMap for the local map because we also need to maintain order
    // of insertion to make sure we have correct LRU behavior when updating the shared cache
    private final ThreadLocal<LinkedHashMap<String, Integer>> externalSystemNames = new ThreadLocal<>();
    
    // The lru cache shared at the server level
    private final LRUCache<String, Integer> sharedExternalSystemNames;
    
    /**
     * Public constructor
     * @param sharedExternalSystemNameCacheSize
     */
    public ResourceReferenceCacheImpl(int sharedExternalSystemNameCacheSize) {
        
        // LRU cache for external system names
        sharedExternalSystemNames = new LRUCache<>(sharedExternalSystemNameCacheSize);
    }

    /**
     * Called after a transaction commit() to transfer all the staged (thread-local) data
     * over to the shared LRU cache.
     */
    public void updateSharedMaps() {
        
        LinkedHashMap<String,Integer> map = externalSystemNames.get();
        if (map != null) {
            synchronized(this.sharedExternalSystemNames) {
                sharedExternalSystemNames.update(map);
            }
            
            // clear the thread-local cache
            map.clear();
        }
    }

    /**
     * Look up the given externalSystemName string in the cache. Looks in the thread-local
     * cache first, falling back to the shared cache if it doesn't yet exist locally.
     * @param externalSystemName
     * @return
     */
    public Integer getExternalSystemNameId(String externalSystemName) {
        // check the thread-local map first
        Integer result = null;

        if (externalSystemNames.get() != null) {
            result = externalSystemNames.get().get(externalSystemName);
            
            if (result != null) {
                return result;
            }
        }
        
        // See if it's in the shared cache
        synchronized (this.sharedExternalSystemNames) {
            result = sharedExternalSystemNames.get(externalSystemName);
        }
        
        if (result != null) {
            // We found it in the shared cache, so update our thread-local
            // cache.
            addExternalSystemName(externalSystemName, result);
        }
        
        return result;
    }

    /**
     * Bulk lookup of a list of system names. Cheaper locking than individual
     * lookups when dealing with big lists
     * @param names
     * @return
     */
    public List<ExternalSystemNameRec> getExternalSystemNames(List<String> names) {
        List<ExternalSystemNameRec> result = new ArrayList<>(names.size());

        // See what we have currently in our thread-local cache
        LinkedHashMap<String,Integer> map = externalSystemNames.get();

        List<String> foundKeys = new ArrayList<>(names.size()); // for updating LRU
        List<String> needToFind; // for the names we haven't yet found
        if (map != null) {
            needToFind = new ArrayList<>(names.size());
            for (String xsn: names) {
                Integer id = map.get(xsn);
                if (id != null) {
                    foundKeys.add(xsn);
                    result.add(new ExternalSystemNameRec(id, xsn));
                } else {
                    // not found, so add to the need to find list
                    needToFind.add(xsn);
                }
            }
        } else {
            // need to find all of them still
            needToFind = names;
        }

        // If we still have keys to find, look them up in the shared cache (which we need to lock first)
        if (needToFind.size() > 0) {
            List<ExternalSystemNameRec> addToLocal = new ArrayList<>(needToFind.size());
            synchronized (this.sharedExternalSystemNames) {
                for (String xsn: needToFind) {
                    Integer id = sharedExternalSystemNames.get(xsn);
                    if (id != null) {
                        ExternalSystemNameRec rec = new ExternalSystemNameRec(id, xsn);
                        foundKeys.add(xsn);
                        result.add(rec);
                        addToLocal.add(rec);
                    }
                }
                
                // while we still have the lock, update the LRU
                sharedExternalSystemNames.update(foundKeys);
            }
            
            // update the local cache with anything we found in the shared cache
            addToLocal.forEach(rec -> addExternalSystemName(rec.getExternalSystemName(), rec.getExternalSystemNameId()));
        } else {
            // we found everything, so update the LRU...which has to be done under a lock
            synchronized (this.sharedExternalSystemNames) {
                sharedExternalSystemNames.update(names);
            }
        }

        return result;
    }

    /**
     * Add the id to the local cache
     * @param externalSystemName
     * @param id
     */
    public void addExternalSystemName(String externalSystemName, int id) {
        LinkedHashMap<String,Integer> map = externalSystemNames.get();
        
        if (map == null) {
            map = new LinkedHashMap<>();
            externalSystemNames.set(map);
        }

        // add the id to the thread-local cache. The shared cache is updated
        // only if a call is made to #updateSharedMaps()
        map.put(externalSystemName, id);
    }

    @Override
    public void reset() {
        LinkedHashMap<String,Integer> map = externalSystemNames.get();
        
        if (map != null) {
            map.clear();
            externalSystemNames.set(null);
        }
        
        synchronized (this.sharedExternalSystemNames) {
            this.sharedExternalSystemNames.clear();
        }
    }
}