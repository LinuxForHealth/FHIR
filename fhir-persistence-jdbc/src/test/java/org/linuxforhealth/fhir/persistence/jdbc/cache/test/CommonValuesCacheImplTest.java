/*
 * (C) Copyright IBM Corp. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.cache.test;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.persistence.jdbc.cache.CommonValuesCacheImpl;
import org.linuxforhealth.fhir.persistence.jdbc.dao.impl.ResourceTokenValueRec;

/**
 * unit test for {@link CommonValuesCacheImpl}
 */
public class CommonValuesCacheImplTest {

    @Test
    public void testExternalSystemNames() {
        // A cache with a limited size of 3 code systems and 2 token values
        // For this test to work, we have to make sure we can always resolve
        // all the code systems, so don't make the cache size smaller than 3
        CommonValuesCacheImpl impl = new CommonValuesCacheImpl(3, 2, 1);
        impl.addCodeSystem("sys1", 1);
        impl.addCodeSystem("sys2", 2);
        impl.addCodeSystem("sys3", 3);

        // The following fetches will be served from the thread-local map because
        // we haven't yet called ResourceReferenceCacheImpl#updateSharedMaps()
        List<ResourceTokenValueRec> xrefs = new ArrayList<>();
        xrefs.add(new ResourceTokenValueRec("one", "Patient", 1, 1L, "sys1", "val1", null, false));
        xrefs.add(new ResourceTokenValueRec("one", "Patient", 1, 1L, "sys2", "val2", null, false));

        // Ask the cache to resolve the system/value strings
        List<ResourceTokenValueRec> systemMisses = new ArrayList<>();
        impl.resolveCodeSystems(xrefs, systemMisses);

        // check we only have misses for what we expected
        assertEquals(0, systemMisses.size());

        // Check that we have ids assigned for the system hits
        ResourceTokenValueRec sys1 = xrefs.get(0);
        assertEquals("sys1", sys1.getCodeSystemValue());
        assertEquals(1, sys1.getCodeSystemValueId());
        ResourceTokenValueRec sys2 = xrefs.get(1);
        assertEquals("sys2", sys2.getCodeSystemValue());
        assertEquals(2, sys2.getCodeSystemValueId());

        List<ResourceTokenValueRec> valueMisses = new ArrayList<>();
        impl.resolveTokenValues(xrefs, valueMisses);
        assertEquals(2, valueMisses.size());

        // Update the shared cache, which will also clear the thread-local map
        // Note that the cache size is only 2, put we added 3 key-values. Because
        // we have an LRU policy, we should only keep "sys2" and "sys3".
        impl.updateSharedMaps();

        // Now try the fetch again...so we have to read from the shared cache.
        // Should only find "sys2", not "sys1". Reset our inputs first
        sys1.setCodeSystemValueId(-1);
        sys2.setCodeSystemValueId(-1);
        systemMisses.clear();
        valueMisses.clear();

        impl.resolveCodeSystems(xrefs, systemMisses);
        assertEquals(0, systemMisses.size());

        impl.resolveTokenValues(xrefs, valueMisses);
        assertEquals(2, valueMisses.size());

        assertEquals(1, sys1.getCodeSystemValueId());
        assertEquals(2, sys2.getCodeSystemValueId());

        // Make sure sys3 is found
        xrefs.add(new ResourceTokenValueRec("one", "Patient", 1, 1L, "sys3", "val3", null, false));
        sys1.setCodeSystemValueId(-1);
        sys2.setCodeSystemValueId(-1);
        systemMisses.clear();
        valueMisses.clear();

        impl.resolveCodeSystems(xrefs, systemMisses);
        impl.resolveTokenValues(xrefs, valueMisses);
        ResourceTokenValueRec sys3 = xrefs.get(2);

        assertEquals(0, systemMisses.size());
        assertEquals(3, valueMisses.size());

        assertEquals(1, sys1.getCodeSystemValueId());
        assertEquals(2, sys2.getCodeSystemValueId());

        assertEquals("sys3", sys3.getCodeSystemValue());
        assertEquals(3, sys3.getCodeSystemValueId());
    }
}