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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.testng.annotations.Test;

import com.ibm.fhir.persistence.jdbc.dao.impl.ExternalResourceReferenceRec;
import com.ibm.fhir.persistence.jdbc.dao.impl.ResourceReferenceCacheImpl;

/**
 * unit test for {@link ResourceReferenceCacheImpl}
 */
public class ResourceReferenceCacheImplTest {

    @Test
    public void testExternalSystemNames() {
        // A cache with a limited size of 2
        ResourceReferenceCacheImpl impl = new ResourceReferenceCacheImpl(2, 2);
        impl.addExternalSystemName("sys1", 1);
        impl.addExternalSystemName("sys2", 2);
        impl.addExternalSystemName("sys3", 3);
        
        // The following fetches will be served from the thread-local map because
        // we haven't yet called ResourceReferenceCacheImpl#updateSharedMaps()
        Set<String> names = new HashSet<>();
        List<ExternalResourceReferenceRec> xrefs = new ArrayList<>();
        xrefs.add(new ExternalResourceReferenceRec(1, "Patient", 1, "pat1", "sys1", "val1"));
        xrefs.add(new ExternalResourceReferenceRec(1, "Patient", 1, "pat1", "sys2", "val2"));
        
        // Ask the cache to resolve the system/value strings
        List<ExternalResourceReferenceRec> systemMisses = new ArrayList<>();
        List<ExternalResourceReferenceRec> valueMisses = new ArrayList<>();
        impl.resolveExternalReferences(xrefs, systemMisses, valueMisses);
        
        // check we only have misses for what we expected
        assertEquals(0, systemMisses.size());
        assertEquals(2, valueMisses.size());

        // Check that we have ids assigned for the system hits
        ExternalResourceReferenceRec sys1 = xrefs.get(0);
        assertEquals("sys1", sys1.getExternalSystemName());
        assertEquals(1, sys1.getExternalSystemNameId());
        ExternalResourceReferenceRec sys2 = xrefs.get(1);
        assertEquals("sys2", sys2.getExternalSystemName());
        assertEquals(2, sys2.getExternalSystemNameId());
        
        // Update the shared cache, which will also clear the thread-local map
        // Note that the cache size is only 2, put we added 3 key-values. Because
        // we have an LRU policy, we should only keep "sys2" and "sys3".
        impl.updateSharedMaps();
        
        // Now try the fetch again...so we have to read from the shared cache.
        // Should only find "sys2", not "sys1". Reset our inputs first
        sys1.setExternalSystemNameId(-1);
        sys2.setExternalSystemNameId(-1);
        systemMisses.clear();
        valueMisses.clear();
        impl.resolveExternalReferences(xrefs, systemMisses, valueMisses);

        assertEquals(1, systemMisses.size());
        assertEquals(2, valueMisses.size());

        assertEquals(-1, sys1.getExternalSystemNameId());
        assertEquals(2, sys2.getExternalSystemNameId());

        // check that sys1 was added to the system misses
        sys1 = systemMisses.get(0);
        assertEquals("sys1", sys1.getExternalSystemName());
        

        // Make sure sys3 is also found
        xrefs.add(new ExternalResourceReferenceRec(1, "Patient", 1, "pat1", "sys3", "val3"));
        sys1.setExternalSystemNameId(-1);
        sys2.setExternalSystemNameId(-1);
        systemMisses.clear();
        valueMisses.clear();
        impl.resolveExternalReferences(xrefs, systemMisses, valueMisses);
        ExternalResourceReferenceRec sys3 = xrefs.get(2);

        // should still be missing sys1
        assertEquals(1, systemMisses.size());
        assertEquals(3, valueMisses.size());

        assertEquals(-1, sys1.getExternalSystemNameId());
        assertEquals(2, sys2.getExternalSystemNameId());
        
        assertEquals("sys3", sys3.getExternalSystemName());
        assertEquals(3, sys3.getExternalSystemNameId());
    }
}