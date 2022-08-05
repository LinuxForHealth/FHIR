/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.cache;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * An LRU implementation which limits the maximum number
 * of entries and ejects older entries. Null values are
 * not permitted in this implementation.
 */
public class LRUCache<K,V> extends LinkedHashMap<K,V> {

    private static final long serialVersionUID = -5096546078810356521L;
    
    // The maximum number of elements permitted inside the map
    private final int maxSize;

    /**
     * Public constructor
     * @param maxSize
     */
    public LRUCache(int maxSize) {
        this.maxSize = maxSize;
    }
    
    @Override
    protected boolean removeEldestEntry(Map.Entry<K,V> eldest) {
        return this.size() > this.maxSize;
    }

    /**
     * Update this LRU with the updates in the given LinkedHashMap,
     * which we use because we apply the updates in a specific order
     * (as defined by the entrySet of the updates parameter).
     * @param updates
     */
    public void update(LinkedHashMap<K,V> updates) {

        // Note that the entrySet we're iterating on is from the parameter,
        // not this.entrySet, so there's no concurrent modification issue
        for (Map.Entry<K, V> entry: updates.entrySet()) {
            remove(entry.getKey());
            put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Update the LRU by processing each of the keys in the
     * updates parameter
     * @param updates
     */
    public void update(List<K> updates) {
        for (K key: updates) {
            V value = remove(key);
            if (value != null) {
                put(key, value);
            }
        }
    }
    
    /**
     * Create a new instance of a LinkedHashMap which can be used for collecting
     * updates to apply to the cache.
     * 
     * @implNote common implementation would be to apply thread-local updates
     * to a shared cache after a transaction commits
     * @return a new empty instance of a {@link LinkedHashMap}
     */
    public LinkedHashMap<K,V> newUpdateMap() {
        return new LinkedHashMap<>();
    }
}
