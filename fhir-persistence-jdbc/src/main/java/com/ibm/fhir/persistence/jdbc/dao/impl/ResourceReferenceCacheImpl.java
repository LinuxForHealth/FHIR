/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dao.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import com.ibm.fhir.persistence.jdbc.dao.api.IResourceReferenceCache;


/**
 * Implementation of a cache used for lookups of entities related
 * to local and external resource references
 */
public class ResourceReferenceCacheImpl implements IResourceReferenceCache {

    // We use LinkedHashMap for the local map because we also need to maintain order
    // of insertion to make sure we have correct LRU behavior when updating the shared cache
    private final ThreadLocal<LinkedHashMap<String, Integer>> externalSystemNames = new ThreadLocal<>();
    
    private final ThreadLocal<LinkedHashMap<String, Long>> externalReferenceValues = new ThreadLocal<>();
    
    // The lru cache shared at the server level
    private final LRUCache<String, Integer> sharedExternalSystemNames;
    
    // The lru cache shared at the server level
    private final LRUCache<String, Long> sharedExternalReferenceValues;
    
    
    /**
     * Public constructor
     * @param sharedExternalSystemNameCacheSize
     */
    public ResourceReferenceCacheImpl(int sharedExternalSystemNameCacheSize, int sharedExternalReferenceValuesCacheSize) {
        
        // LRU cache for external system names
        sharedExternalSystemNames = new LRUCache<>(sharedExternalSystemNameCacheSize);
        sharedExternalReferenceValues = new LRUCache<>(sharedExternalReferenceValuesCacheSize);
    }

    /**
     * Called after a transaction commit() to transfer all the staged (thread-local) data
     * over to the shared LRU cache.
     */
    public void updateSharedMaps() {
        
        LinkedHashMap<String,Integer> sysMap = externalSystemNames.get();
        if (sysMap != null) {
            synchronized(this.sharedExternalSystemNames) {
                sharedExternalSystemNames.update(sysMap);
            }
            
            // clear the thread-local cache
            sysMap.clear();
        }
        
        LinkedHashMap<String,Long> valMap = externalReferenceValues.get();
        if (valMap != null) {
            synchronized(this.sharedExternalReferenceValues) {
                sharedExternalReferenceValues.update(valMap);
            }
            
            // clear the thread-local cache
            valMap.clear();
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

    @Override
    public void resolveExternalReferences(Collection<ExternalResourceReferenceRec> xrefs, 
        List<ExternalResourceReferenceRec> systemMisses, List<ExternalResourceReferenceRec> valueMisses) {
        // Make one pass over the collection and resolve as much as we can in one go. Anything
        // we can't resolve gets put into the corresponding missing lists. Worst case is two passes, when
        // there's nothing in the local cache and we have to then look up everything in the shared cache

        // See what we have currently in our thread-local cache
        LinkedHashMap<String,Integer> sysMap = externalSystemNames.get();
        LinkedHashMap<String,Long> valMap = externalReferenceValues.get();

        List<String> foundKeys = new ArrayList<>(xrefs.size()); // for updating LRU
        List<ExternalResourceReferenceRec> needToFindSystems = new ArrayList<>(xrefs.size()); // for the ref systems we haven't yet found
        List<ExternalResourceReferenceRec> needToFindValues = new ArrayList<>(xrefs.size()); // for the ref values we haven't yet found
        for (ExternalResourceReferenceRec xr: xrefs) {
            if (sysMap != null) {
                Integer id = sysMap.get(xr.getExternalSystemName());
                if (id != null) {
                    foundKeys.add(xr.getExternalSystemName());
                    xr.setExternalSystemNameId(id);
                } else {
                    // not found, so add to the cache miss list
                    needToFindSystems.add(xr);
                }
            } else {
                needToFindSystems.add(xr);
            }
            
            // Now repeat for the value lookup
            if (valMap != null) {
                Long id = valMap.get(xr.getExternalRefValue());
                if (id != null) {
                    foundKeys.add(xr.getExternalRefValue());
                    xr.setExternalRefValueId(id);
                } else {
                    // not found, so add to the cache miss list
                    needToFindValues.add(xr);
                }
            } else {
                needToFindValues.add(xr);
            }
        }

        // If we still have keys to find, look them up in the shared cache (which we need to lock first)
        if (needToFindSystems.size() > 0) {
            synchronized (this.sharedExternalSystemNames) {
                for (ExternalResourceReferenceRec xr: needToFindSystems) {
                    Integer id = sharedExternalSystemNames.get(xr.getExternalSystemName());
                    if (id != null) {
                        xr.setExternalSystemNameId(id);
                        
                        // Update the local cache with this value
                        addExternalSystemName(xr.getExternalSystemName(), id);
                    } else {
                        // cache miss so add this record to the miss list for further processing
                        systemMisses.add(xr);
                    }
                }
            }
        }
        
        // Now do the same for reference values
        if (needToFindValues.size() > 0) {
            synchronized (this.sharedExternalReferenceValues) {
                for (ExternalResourceReferenceRec xr: needToFindValues) {
                    Long id = sharedExternalReferenceValues.get(xr.getExternalRefValue());
                    if (id != null) {
                        xr.setExternalRefValueId(id);
                        
                        // Update the local cache with this value
                        addExternalRefValue(xr.getExternalRefValue(), id);
                    } else {
                        // cache miss so add this record to the miss list for further processing
                        valueMisses.add(xr);
                    }
                }
            }
        }
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

    /**
     * Add the id to the local external reference value cache
     * @param externalRefValue
     * @param id
     */
    public void addExternalRefValue(String externalRefValue, long id) {
        LinkedHashMap<String,Long> map = externalReferenceValues.get();
        
        if (map == null) {
            map = new LinkedHashMap<>();
            externalReferenceValues.set(map);
        }

        // add the id to the thread-local cache. The shared cache is updated
        // only if a call is made to #updateSharedMaps()
        map.put(externalRefValue, id);
    }

    @Override
    public void reset() {
        LinkedHashMap<String,Integer> sysMap = externalSystemNames.get();

        // Clear external system names
        if (sysMap != null) {
            sysMap.clear();
            externalSystemNames.set(null);
        }
        
        synchronized (this.sharedExternalSystemNames) {
            this.sharedExternalSystemNames.clear();
        }

        // Clear external reference values
        LinkedHashMap<String,Long> valMap = externalReferenceValues.get();
        
        if (valMap != null) {
            valMap.clear();
            externalReferenceValues.set(null);
        }
        
        synchronized (this.sharedExternalReferenceValues) {
            this.sharedExternalReferenceValues.clear();
        }
    }
}