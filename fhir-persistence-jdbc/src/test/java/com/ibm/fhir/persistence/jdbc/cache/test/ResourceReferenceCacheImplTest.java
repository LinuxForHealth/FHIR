/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.cache.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import com.ibm.fhir.persistence.jdbc.dao.impl.ExternalSystemNameRec;
import com.ibm.fhir.persistence.jdbc.dao.impl.ResourceReferenceCacheImpl;

/**
 * unit test for {@link ResourceReferenceCacheImpl}
 */
public class ResourceReferenceCacheImplTest {

    @Test
    public void testExternalSystemNames() {
        // A cache with a limited size of 2
        ResourceReferenceCacheImpl impl = new ResourceReferenceCacheImpl(2);
        impl.addExternalSystemName("sys1", 1);
        impl.addExternalSystemName("sys2", 2);
        impl.addExternalSystemName("sys3", 3);
        
        // The following fetches will be served from the thread-local map because
        // we haven't yet called ResourceReferenceCacheImpl#updateSharedMaps()
        List<String> names = new ArrayList<>();
        names.add("sys1");
        names.add("sys2");
        List<ExternalSystemNameRec> result = impl.getExternalSystemNames(names);
        assertNotNull(result);
        assertEquals(2, result.size());

        ExternalSystemNameRec sys1 = result.get(0);
        assertEquals("sys1", sys1.getExternalSystemName());
        assertEquals(1, sys1.getExternalSystemNameId());
        ExternalSystemNameRec sys2 = result.get(1);
        assertEquals("sys2", sys2.getExternalSystemName());
        assertEquals(2, sys2.getExternalSystemNameId());
        
        // Update the shared cache, which will also clear the thread-local map
        // Note that the cache size is only 2, put we added 3 key-values. Because
        // we have an LRU policy, we should only keep "sys2" and "sys3".
        impl.updateSharedMaps();
        
        // Now try the fetch again...so we have to read from the shared cache.
        // Should only find "sys2", not "sys1"
        result = impl.getExternalSystemNames(names);
        assertNotNull(result);
        assertEquals(1, result.size());
        sys2 = result.get(0);
        assertEquals("sys2", sys2.getExternalSystemName());
        assertEquals(2, sys2.getExternalSystemNameId());

        // Make sure sys3 is also found
        names.add("sys3");
        result = impl.getExternalSystemNames(names);
        assertNotNull(result);
        assertEquals(2, result.size());
        sys2 = result.get(0);
        assertEquals("sys2", sys2.getExternalSystemName());
        assertEquals(2, sys2.getExternalSystemNameId());
        ExternalSystemNameRec sys3 = result.get(1);
        assertEquals("sys3", sys3.getExternalSystemName());
        assertEquals(3, sys3.getExternalSystemNameId());
    }
    
    @Test
    public void testExternalSystemNameLRU() {
        // cache with an LRU size of 2
        ResourceReferenceCacheImpl cache = new ResourceReferenceCacheImpl(2);
        cache.addExternalSystemName("system1", 1);
        cache.addExternalSystemName("system2", 2);
        
        assertEquals(1L, (long)cache.getExternalSystemNameId("system1"));
        assertEquals(2L, (long)cache.getExternalSystemNameId("system2"));
        cache.updateSharedMaps();
        
        // Access system 1 again so it is most recently used
        assertEquals(1L, (long)cache.getExternalSystemNameId("system1"));

        // Add a new system
        cache.addExternalSystemName("system3", 3);
        
        // Update the shared map. This should push out system2
        cache.updateSharedMaps();
        assertEquals(1L, (long)cache.getExternalSystemNameId("system1"));
        assertEquals(3L, (long)cache.getExternalSystemNameId("system3"));
        
        // evicted from the cache because we limited the size to 2
        assertNull(cache.getExternalSystemNameId("system2"));
    }

}