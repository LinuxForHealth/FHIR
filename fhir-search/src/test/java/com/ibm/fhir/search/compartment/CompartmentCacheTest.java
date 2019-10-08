/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.compartment;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.testng.annotations.Test;

import com.ibm.fhir.search.test.BaseSearchTest;

/**
 * The CompartmentCacheTest tests the signature of the CompartmentCache.
 * 
 * @author pbastide
 *
 */
public class CompartmentCacheTest extends BaseSearchTest {

    @Test()
    public void testGetResourceTypesInCompartmentWhenEmptyCompartmentCache() {
        CompartmentCache cache = new CompartmentCache();
        List<String> results = cache.getResourceTypesInCompartment();
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test()
    public void testGetParametersByResourceTypeInCompartmentWhenEmptyCompartmentCache() {
        CompartmentCache cache = new CompartmentCache();

        // Null Test
        List<String> results = cache.getParametersByResourceTypeInCompartment(null);
        assertNotNull(results);
        assertTrue(results.isEmpty());

        // Non Null Test
        results = cache.getParametersByResourceTypeInCompartment("FudgeBrownie");
        assertNotNull(results);
        assertTrue(results.isEmpty());

    }

    @Test(expectedExceptions = { UnsupportedOperationException.class })
    public void testGetResourceTypesInCompartmentUnmodifiable() {
        // Confirms unmodifiable results.
        CompartmentCache cache = new CompartmentCache();
        List<String> results = cache.getParametersByResourceTypeInCompartment("FudgeBrownie");
        assertNotNull(results);
        results.clear();
    }

    @Test(expectedExceptions = { UnsupportedOperationException.class })
    public void testGetParametersByResourceTypeInCompartmentUnmodifiable() {
        // Confirms unmodifiable results.
        CompartmentCache cache4 = new CompartmentCache();
        cache4.add("FrenchFries", Collections.emptyList());
        assertNotNull(cache4);

        // Assemble a Real List and Test
        List<com.ibm.fhir.model.type.String> params = new ArrayList<>();
        params.add(com.ibm.fhir.model.type.String.string("Ketchup"));
        cache4.add("FrenchFries", params);

        // - Should no longer be Empty
        List<String> results = cache4.getParametersByResourceTypeInCompartment("FrenchFries");
        assertNotNull(results);
        results.clear();
    }

    @Test()
    public void testCompartmentCacheAddOneDefinition() {
        // Null - inclusionCode
        CompartmentCache cache1 = new CompartmentCache();

        cache1.add(null, Collections.emptyList());
        assertNotNull(cache1);

        // Empty - inclusionCode
        CompartmentCache cache2 = new CompartmentCache();
        cache2.add("", Collections.emptyList());
        assertNotNull(cache2);

        // Skips over inclusion if null
        CompartmentCache cache3 = new CompartmentCache();
        cache3.add(null, null);

        // Real with Empty List
        CompartmentCache cache4 = new CompartmentCache();
        cache4.add("FrenchFries", Collections.emptyList());
        assertNotNull(cache4);

        // - should still respond with Empty.
        List<String> results = cache4.getParametersByResourceTypeInCompartment("FrenchFries");
        assertNotNull(results);
        assertTrue(results.isEmpty());

        // Assemble a Real List and Test
        List<com.ibm.fhir.model.type.String> params = new ArrayList<>();
        params.add(com.ibm.fhir.model.type.String.string("Ketchup"));
        cache4.add("FrenchFries", params);

        // - Should no longer be Empty
        results = cache4.getParametersByResourceTypeInCompartment("FrenchFries");
        assertNotNull(results);
        assertFalse(results.isEmpty());

        // - Should be Empty
        results = cache4.getParametersByResourceTypeInCompartment("Ketchup");
        assertNotNull(results);
        assertTrue(results.isEmpty());

    }

}
