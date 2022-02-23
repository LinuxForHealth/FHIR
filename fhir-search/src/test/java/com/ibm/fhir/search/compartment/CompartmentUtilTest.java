/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.compartment;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.testng.annotations.Test;

import com.ibm.fhir.search.exception.FHIRSearchException;
import com.ibm.fhir.search.test.BaseSearchTest;

/**
 * CompartmentUtil is tested in this class.
 */
public class CompartmentUtilTest extends BaseSearchTest {
    CompartmentUtil compartmentHelper = new CompartmentUtil();

    @Test()
    public void testBuildCompartmentMap() {
        Map<String, CompartmentCache> cache = new HashMap<>();
        Map<String, ResourceCompartmentCache> resourceCompartmentCache = new HashMap<>();
        compartmentHelper.buildMaps(cache, resourceCompartmentCache);

        // There should be 5 compartment definitions.
        // Detailed behavior of the individual cache are tested in the CompartmentCache.
        assertEquals(5, cache.keySet().size());

    }

    @Test
    public void testGetCompartmentResourceTypeExists() throws FHIRSearchException {
        // The Compartment Does not Exist Exists
        List<String> results = compartmentHelper.getCompartmentResourceTypes("Patient");
        assertNotNull(results);
        assertFalse(results.isEmpty());
    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testGetCompartmentResourceTypeDoesNotExist() throws FHIRSearchException {
        // The Compartment Does not Exist Exists
        compartmentHelper.getCompartmentResourceTypes("FredF");
    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testCheckInvalidCompartment() throws FHIRSearchException {
        // Check invalid null value passed in
        compartmentHelper.checkValidCompartment(null);
    }

    @Test()
    public void testCheckValidCompartmentAndResource() throws FHIRSearchException {
        // Test valid check.
        compartmentHelper.checkValidCompartmentAndResource("Patient", "CommunicationRequest");
        assertTrue(true);
    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testCheckInvalidCompartmentAndResourceNull() throws FHIRSearchException {
        // Test valid check. Null
        compartmentHelper.checkValidCompartmentAndResource("Patient", null);
    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testCheckInvalidCompartmentAndResourceEmpty() throws FHIRSearchException {
        // Test valid check. Null
        compartmentHelper.checkValidCompartmentAndResource("Patient", "");

    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testCheckInvalidCompartmentAndResourceNotExist() throws FHIRSearchException {
        // Test valid check. Null
        compartmentHelper.checkValidCompartmentAndResource("Patient", "FrenchFood");
    }

    @Test()
    public void testGetCompartmentResourceTypeInclusionCriteria() throws FHIRSearchException {
        List<String> results = compartmentHelper.getCompartmentResourceTypeInclusionCriteria("Patient", "CommunicationRequest");
        assertNotNull(results);
        assertFalse(results.isEmpty());
    }

    @Test()
    public void testParamListForExplanationOfBenefit() {
        Map<String, Set<java.lang.String>> pmap = compartmentHelper.getCompartmentParamsForResourceType("ExplanationOfBenefit");
        assertNotNull(pmap);

        // EOB may belong to Patient, Encounter, Practitioner, RelatedPerson and Device compartments
        assertEquals(pmap.size(), 10);

        assertTrue(pmap.get("procedure-udi").contains("Device"));
        assertTrue(pmap.get("item-udi").contains("Device"));
        assertTrue(pmap.get("detail-udi").contains("Device"));
        assertTrue(pmap.get("subdetail-udi").contains("Device"));
        assertTrue(pmap.get("enterer").contains("Practitioner"));
        assertTrue(pmap.get("provider").contains("Practitioner"));
        assertTrue(pmap.get("payee").contains("Practitioner"));
        assertTrue(pmap.get("payee").contains("RelatedPerson"));
        assertTrue(pmap.get("care-team").contains("Practitioner"));
        assertTrue(pmap.get("patient").contains("Patient"));
        assertTrue(pmap.get("encounter").contains("Encounter"));
    }

    @Test()
    public void testParamListForInvalidResource() {
        Map<String, Set<java.lang.String>> pmap = compartmentHelper.getCompartmentParamsForResourceType("not-a-resource");
        assertNotNull(pmap);
        assertTrue(pmap.isEmpty());
    }
}