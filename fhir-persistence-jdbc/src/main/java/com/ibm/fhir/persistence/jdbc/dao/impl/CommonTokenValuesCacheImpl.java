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

import com.ibm.fhir.persistence.jdbc.dao.api.ICommonTokenValuesCache;
import com.ibm.fhir.persistence.jdbc.dto.CommonTokenValue;


/**
 * Implementation of a cache used for lookups of entities related
 * to local and external resource references
 */
public class CommonTokenValuesCacheImpl implements ICommonTokenValuesCache {

    // We use LinkedHashMap for the local map because we also need to maintain order
    // of insertion to make sure we have correct LRU behavior when updating the shared cache
    private final ThreadLocal<LinkedHashMap<String, Integer>> codeSystems = new ThreadLocal<>();
    
    private final ThreadLocal<LinkedHashMap<CommonTokenValue, Long>> commonTokenValues = new ThreadLocal<>();
    
    // The lru cache shared at the server level
    private final LRUCache<String, Integer> codeSystemsCache;
    
    // The lru cache shared at the server level
    private final LRUCache<CommonTokenValue, Long> tokenValuesCache;
    
    
    /**
     * Public constructor
     * @param sharedExternalSystemNameCacheSize
     */
    public CommonTokenValuesCacheImpl(int codeSystemCacheSize, int tokenValueCacheSize) {
        
        // LRU cache for quick lookup of code-systems and token-values
        codeSystemsCache = new LRUCache<>(codeSystemCacheSize);
        tokenValuesCache = new LRUCache<>(tokenValueCacheSize);
    }

    /**
     * Called after a transaction commit() to transfer all the staged (thread-local) data
     * over to the shared LRU cache.
     */
    public void updateSharedMaps() {
        
        LinkedHashMap<String,Integer> sysMap = codeSystems.get();
        if (sysMap != null) {
            synchronized(this.codeSystemsCache) {
                codeSystemsCache.update(sysMap);
            }
            
            // clear the thread-local cache
            sysMap.clear();
        }
        
        LinkedHashMap<CommonTokenValue,Long> valMap = commonTokenValues.get();
        if (valMap != null) {
            synchronized(this.tokenValuesCache) {
                tokenValuesCache.update(valMap);
            }
            
            // clear the thread-local cache
            valMap.clear();
        }

    }

    /**
     * Look up the given externalSystemName string in the cache. Looks in the thread-local
     * cache first, falling back to the shared cache if it doesn't yet exist locally.
     * @param codeSystem
     * @return
     */
    public Integer getCodeSystemId(String codeSystem) {
        // check the thread-local map first
        Integer result = null;

        if (codeSystems.get() != null) {
            result = codeSystems.get().get(codeSystem);
            
            if (result != null) {
                return result;
            }
        }
        
        // See if it's in the shared cache
        synchronized (this.codeSystemsCache) {
            result = codeSystemsCache.get(codeSystem);
        }
        
        if (result != null) {
            // We found it in the shared cache, so update our thread-local
            // cache.
            addCodeSystem(codeSystem, result);
        }
        
        return result;
    }

    @Override
    public void resolveCodeSystems(Collection<ResourceTokenValueRec> tokenValues, 
        List<ResourceTokenValueRec> misses) {
        // Make one pass over the collection and resolve as much as we can in one go. Anything
        // we can't resolve gets put into the corresponding missing lists. Worst case is two passes, when
        // there's nothing in the local cache and we have to then look up everything in the shared cache

        // See what we have currently in our thread-local cache
        LinkedHashMap<String,Integer> sysMap = codeSystems.get();

        List<String> foundKeys = new ArrayList<>(tokenValues.size()); // for updating LRU
        List<ResourceTokenValueRec> needToFindSystems = new ArrayList<>(tokenValues.size()); // for the ref systems we haven't yet found
        for (ResourceTokenValueRec tv: tokenValues) {
            if (sysMap != null) {
                Integer id = sysMap.get(tv.getCodeSystemValue());
                if (id != null) {
                    foundKeys.add(tv.getCodeSystemValue());
                    tv.setCodeSystemValueId(id);
                } else {
                    // not found, so add to the cache miss list
                    needToFindSystems.add(tv);
                }
            } else {
                // no thread-local cache yet, so need to find them all
                needToFindSystems.add(tv);
            }
        }

        // If we still have keys to find, look them up in the shared cache (which we need to lock first)
        if (needToFindSystems.size() > 0) {
            synchronized (this.codeSystemsCache) {
                for (ResourceTokenValueRec xr: needToFindSystems) {
                    Integer id = codeSystemsCache.get(xr.getCodeSystemValue());
                    if (id != null) {
                        xr.setCodeSystemValueId(id);
                        
                        // Update the local cache with this value
                        addCodeSystem(xr.getCodeSystemValue(), id);
                    } else {
                        // cache miss so add this record to the miss list for further processing
                        misses.add(xr);
                    }
                }
            }
        }
    }

    
    @Override
    public void resolveTokenValues(Collection<ResourceTokenValueRec> tokenValues, 
        List<ResourceTokenValueRec> misses) {
        // Make one pass over the collection and resolve as much as we can in one go. Anything
        // we can't resolve gets put into the corresponding missing lists. Worst case is two passes, when
        // there's nothing in the local cache and we have to then look up everything in the shared cache

        // See what we have currently in our thread-local cache
        LinkedHashMap<CommonTokenValue,Long> valMap = commonTokenValues.get();

        List<CommonTokenValue> foundKeys = new ArrayList<>(tokenValues.size()); // for updating LRU
        List<ResourceTokenValueRec> needToFindValues = new ArrayList<>(tokenValues.size()); // for the ref values we haven't yet found
        for (ResourceTokenValueRec tv: tokenValues) {
            if (valMap != null) {
                CommonTokenValue key = new CommonTokenValue(tv.getParameterNameId(), tv.getCodeSystemValueId(), tv.getTokenValue());
                Long id = valMap.get(key);
                if (id != null) {
                    foundKeys.add(key);
                    tv.setCommonTokenValueId(id);
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
            synchronized (this.tokenValuesCache) {
                for (ResourceTokenValueRec tv: needToFindValues) {
                    CommonTokenValue key = new CommonTokenValue(tv.getParameterNameId(), tv.getCodeSystemValueId(), tv.getTokenValue());
                    Long id = tokenValuesCache.get(key);
                    if (id != null) {
                        tv.setCommonTokenValueId(id);
                        
                        // Update the local cache with this value
                        addTokenValue(key, id);
                    } else {
                        // cache miss so add this record to the miss list for further processing
                        misses.add(tv);
                    }
                }
            }
        }
    }


    @Override
    public void addCodeSystem(String codeSystem, int id) {
        LinkedHashMap<String,Integer> map = codeSystems.get();
        
        if (map == null) {
            map = new LinkedHashMap<>();
            codeSystems.set(map);
        }

        // add the id to the thread-local cache. The shared cache is updated
        // only if a call is made to #updateSharedMaps()
        map.put(codeSystem, id);
    }

    @Override
    public void addTokenValue(CommonTokenValue key, long id) {
        LinkedHashMap<CommonTokenValue,Long> map = commonTokenValues.get();
        
        if (map == null) {
            map = new LinkedHashMap<>();
            commonTokenValues.set(map);
        }

        // add the id to the thread-local cache. The shared cache is updated
        // only if a call is made to #updateSharedMaps()
        map.put(key, id);
    }

    @Override
    public void reset() {
        LinkedHashMap<String,Integer> sysMap = codeSystems.get();

        // Clear code-system caches
        if (sysMap != null) {
            sysMap.clear();
            codeSystems.set(null);
        }
        
        synchronized (this.codeSystemsCache) {
            this.codeSystemsCache.clear();
        }

        // Clear the common token values caches
        LinkedHashMap<CommonTokenValue,Long> valMap = commonTokenValues.get();
        
        if (valMap != null) {
            valMap.clear();
            commonTokenValues.set(null);
        }
        
        synchronized (this.tokenValuesCache) {
            this.tokenValuesCache.clear();
        }
    }
}