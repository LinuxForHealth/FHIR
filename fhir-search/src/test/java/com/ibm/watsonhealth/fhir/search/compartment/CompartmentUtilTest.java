/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.search.compartment;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import com.ibm.watsonhealth.fhir.search.exception.FHIRSearchException;
import com.ibm.watsonhealth.fhir.search.test.BaseSearchTest;

/**
 * CompartmentUtil is tested in this class.
 * 
 * @author pbastide
 *
 */
public class CompartmentUtilTest extends BaseSearchTest {

    @Test()
    public void testBuildCompartmentMap() {
        Map<String, CompartmentCache> cache = CompartmentUtil.buildCompartmentMap();

        // There should be 5 compartment definitions.
        // Detailed behavior of the individual cache are tested in the CompartmentCache.
        assertEquals(5, cache.keySet().size());

    }

    @Test(expectedExceptions = { UnsupportedOperationException.class }, dependsOnMethods = { "testBuildCompartmentMap" })
    public void testBuildCompartmentMapWithModification() {
        // Verifies the cache is unmodifiable.
        Map<String, CompartmentCache> cache = CompartmentUtil.buildCompartmentMap();
        cache.clear();

    }

    @Test(expectedExceptions = {})
    public void testGetCompartmentResourceTypeExists() throws FHIRSearchException {
        // The Compartment Does not Exist Exists
        List<String> results = CompartmentUtil.getCompartmentResourceTypes("Patient");
        assertNotNull(results);
        assertFalse(results.isEmpty());
    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testGetCompartmentResourceTypeDoesNotExist() throws FHIRSearchException {
        // The Compartment Does not Exist Exists
        CompartmentUtil.getCompartmentResourceTypes("FredF");

    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testCheckInvalidCompartment() throws FHIRSearchException {
        // Check invalid null value passed in
        CompartmentUtil.checkValidCompartment(null);
    }

    @Test()
    public void testCheckValidCompartmentAndResource() throws FHIRSearchException {
        // Test valid check.
        CompartmentUtil.checkValidCompartmentAndResource("Patient", "CommunicationRequest");
        assertTrue(true);
    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testCheckInvalidCompartmentAndResourceNull() throws FHIRSearchException {
        // Test valid check. Null
        CompartmentUtil.checkValidCompartmentAndResource("Patient", null);
    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testCheckInvalidCompartmentAndResourceEmpty() throws FHIRSearchException {
        // Test valid check. Null
        CompartmentUtil.checkValidCompartmentAndResource("Patient", "");

    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testCheckInvalidCompartmentAndResourceNotExist() throws FHIRSearchException {
        // Test valid check. Null
        CompartmentUtil.checkValidCompartmentAndResource("Patient", "FrenchFood");
    }

    @Test()
    public void testGetCompartmentResourceTypeInclusionCriteria() throws FHIRSearchException {
        List<String> results = CompartmentUtil.getCompartmentResourceTypeInclusionCriteria("Patient", "CommunicationRequest");
        assertNotNull(results);
        assertFalse(results.isEmpty());
    }

    @Test()
    public void testBuildCompositeBundle() {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); PrintStream out = new PrintStream(outputStream);) {
            CompartmentUtil.buildCompositeBundle(out);
            String o = outputStream.toString("UTF8");
            assertNotNull(o);
            assertFalse(o.isEmpty());
        }catch(Exception e) {
            e.printStackTrace();
            fail();
        }

    }
}
