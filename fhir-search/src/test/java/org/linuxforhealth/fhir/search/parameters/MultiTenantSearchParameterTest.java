/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.search.parameters;

import static org.testng.Assert.assertFalse;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertNull;
import static org.testng.AssertJUnit.assertTrue;

import java.util.Map;
import java.util.Set;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import org.linuxforhealth.fhir.config.FHIRRequestContext;
import org.linuxforhealth.fhir.exception.FHIRException;
import org.linuxforhealth.fhir.model.resource.Basic;
import org.linuxforhealth.fhir.model.resource.Device;
import org.linuxforhealth.fhir.model.resource.Observation;
import org.linuxforhealth.fhir.model.resource.Patient;
import org.linuxforhealth.fhir.model.resource.SearchParameter;
import org.linuxforhealth.fhir.registry.FHIRRegistry;
import org.linuxforhealth.fhir.search.test.BaseSearchTest;

/**
 * This class tests various multi-tenant enabled search parameter-related methods of the SearchUtil class.
 */
public class MultiTenantSearchParameterTest extends BaseSearchTest {

    @AfterMethod
    public void cleanup() throws FHIRException {
        // Restore the threadLocal FHIRRequestContext to the default tenant
        FHIRRequestContext.remove();
    }

    @Test
    public void testGetApplicableSearchParameters2() throws Exception {
        // Looking only for built-in search parameters for "Patient".

        // Use tenant1 since it doesn't have any tenant-specific search parameters for resourceType Medication.
        FHIRRequestContext.get().setTenantId("tenant1");

        // Using Medication because tenant1 has filters in place for Patient and Observation
        Map<String, SearchParameter> result = searchHelper.getSearchParameters("Medication");
        assertNotNull(result);
        printSearchParameters("testGetApplicableSearchParameters2", result);
        assertEquals(15, result.size());
    }

    @Test
    public void testGetApplicableSearchParameters1() throws Exception {
        // Simple test looking only for built-in search parameters for Observation.class.
        // Use default tenant id ("default") which has no Observation tenant-specifc search parameters.
        Map<String, SearchParameter> result = searchHelper.getSearchParameters("Observation");
        assertNotNull(result);
        printSearchParameters("testGetApplicableSearchParameters1", result);
        assertEquals(44, result.size());
    }

    @Test
    public void testGetApplicableSearchParameters3() throws Exception {
        // Looking for built-in and tenant-specific search parameters for "Patient" and "Observation".

        // Use the extended tenant since it has some Patient search parameters defined.
        FHIRRequestContext.get().setTenantId("extended");

        Map<String, SearchParameter> result = searchHelper.getSearchParameters("Patient");
        assertNotNull(result);
        printSearchParameters("testGetApplicableSearchParameters3/Patient", result);
        assertEquals(37, result.size());

        result = searchHelper.getSearchParameters("Observation");
        assertNotNull(result);
        printSearchParameters("testGetApplicableSearchParameters3/Observation", result);
        assertEquals(44, result.size());
    }

    @Test
    public void testGetApplicableSearchParameters4() throws Exception {
        // Looking for built-in and tenant-specific search parameters for "Observation".

        // Use tenant1 since it has some Observation search parameters defined.
        FHIRRequestContext.get().setTenantId("tenant1");

        // tenant1's filtering includes 2 search parameters for Observation.
        Map<String, SearchParameter> result = searchHelper.getSearchParameters("Observation");
        assertNotNull(result);
        printSearchParameters("testGetApplicableSearchParameters4/Observation", result);
        assertEquals(8, result.size());

        Set<String> codes = result.keySet();
        assertTrue(codes.contains("code"));
        assertTrue(codes.contains("_id"));
    }

    @Test
    public void testGetApplicableSearchParameters5() throws Exception {
        // Test filtering of search parameters for Device (tenant1).
        FHIRRequestContext.get().setTenantId("tenant1");

        Map<String, SearchParameter> result = searchHelper.getSearchParameters("Device");
        assertNotNull(result);
        printSearchParameters("testGetApplicableSearchParameters5/Device", result);
        assertEquals(8, result.size());

        Set<String> codes = result.keySet();
        assertTrue(codes.contains("patient"));
        assertTrue(codes.contains("organization"));
    }

    @Test
    public void testGetApplicableSearchParameters6() throws Exception {
        // Test filtering of search parameters for Patient (tenant1).
        FHIRRequestContext.get().setTenantId("tenant1");

        Map<String, SearchParameter> result = searchHelper.getSearchParameters("Patient");
        assertNotNull(result);
        printSearchParameters("testGetApplicableSearchParameters6/Patient", result);
        assertEquals(10, result.size());

        Set<String> codes = result.keySet();
        assertTrue(codes.contains("active"));
        assertTrue(codes.contains("address"));
        assertTrue(codes.contains("birthdate"));
        assertTrue(codes.contains("name"));

        // Make sure we get all of the MedicationAdministration search parameters.
        // (No filtering configured for these)
        result = searchHelper.getSearchParameters("MedicationAdministration");
        assertNotNull(result);
        printSearchParameters("testGetApplicableSearchParameters6/MedicationAdministration", result);
        assertEquals(19, result.size());
    }

    @Test
    public void testGetApplicableSearchParameters7() throws Exception {
        // Test filtering of search parameters for Patient (extended tenant).
        FHIRRequestContext.get().setTenantId("extended");

        Map<String, SearchParameter> result = searchHelper.getSearchParameters("Patient");
        assertNotNull(result);
        printSearchParameters("testGetApplicableSearchParameters7/Patient", result);
        assertEquals(37, result.size());

        result = searchHelper.getSearchParameters("Device");
        assertNotNull(result);
        printSearchParameters("testGetApplicableSearchParameters7/Device", result);
        assertEquals(20, result.size());
    }

    @Test
    public void testGetSearchParameters() throws Exception {
        // Looking only for built-in search parameters for "Patient".

        // Use tenant3 since it doesn't have any tenant-specific search parameters.
        FHIRRequestContext.get().setTenantId("tenant3");

        Map<String, SearchParameter> result = searchHelper.getSearchParameters("Patient");
        printSearchParameters("testGetSearchParameters", result);
        System.out.println("tenant: " + FHIRRequestContext.get().getTenantId());
        assertNotNull(result);
        assertEquals(31, result.size());
    }

    @Test
    public void testGetSearchParameter1() throws Exception {
        // Use tenant1 since it has some Basic search parameters defined.
        FHIRRequestContext.get().setTenantId("tenant1");

        SearchParameter result = searchHelper.getSearchParameter(Basic.class, "measurement-type");
        assertNotNull(result);
    }

    @Test
    public void testGetSearchParameter2() throws Exception {
        // Use tenant1 since it has some Patient search parameters defined.
        FHIRRequestContext.get().setTenantId("tenant1");

        SearchParameter result = searchHelper.getSearchParameter(Observation.class, "value-range");
        assertNotNull(result);

        result = searchHelper.getSearchParameter(Observation.class, "code");
        assertNotNull(result);

        result = searchHelper.getSearchParameter(Observation.class, "category");
        assertNull(result);

        // "active" is a search parameter belonging to Patient, not Observation.
        result = searchHelper.getSearchParameter(Observation.class, "active");
        assertNull(result);

        result = searchHelper.getSearchParameter(Observation.class, "bad-sp-name");
        assertNull(result);
    }

    @Test
    public void testGetSearchParameter3() throws Exception {
        FHIRRequestContext.get().setTenantId("tenant1");
        SearchParameter result = searchHelper.getSearchParameter("Observation", "value-range-value");
        assertNull(result);
    }

    @Test
    public void testGetSearchParameter4() throws Exception {
        // Use tenant1 since it has some Patient search parameters defined.
        FHIRRequestContext.get().setTenantId("tenant1");

        SearchParameter result = searchHelper.getSearchParameter("Device", "patient");
        assertNotNull(result);

        result = searchHelper.getSearchParameter("Device", "organization");
        assertNotNull(result);

        result = searchHelper.getSearchParameter(Device.class, "organization");
        assertNotNull(result);

        result = searchHelper.getSearchParameter(Observation.class, "category");
        assertNull(result);

        // "active" is a search parameter belonging to Patient, not Observation.
        result = searchHelper.getSearchParameter(Observation.class, "active");
        assertNull(result);

        result = searchHelper.getSearchParameter(Observation.class, "bad-sp-name");
        assertNull(result);
    }

    @Test
    public void testGetSearchParameter5() throws Exception {
        SearchParameter result = searchHelper.getSearchParameter("Observation", "_lastUpdated");
        assertNotNull(result);
    }

    @Test
    public void testGetSearchParameter6() throws Exception {
        SearchParameter result = searchHelper.getSearchParameter(Observation.class, "_lastUpdated");
        assertNotNull(result);
    }

    @Test
    public void testGetSearchParameter7() throws Exception {
        FHIRRequestContext.get().setTenantId("tenant1");
        SearchParameter result = searchHelper.getSearchParameter("Observation", "_lastUpdated");
        assertNotNull(result);
    }

    @Test
    public void testGetSearchParameter8() throws Exception {
        FHIRRequestContext.get().setTenantId("tenant1");
        SearchParameter result = searchHelper.getSearchParameter(Observation.class, "_lastUpdated");
        assertNotNull(result);
    }

    @Test
    public void testGetSearchParameter9() throws Exception {
        SearchParameter result = searchHelper.getSearchParameter("Observation", "device");
        assertNotNull(result);
    }

    @Test
    public void testGetSearchParameter10() throws Exception {
        FHIRRequestContext.get().setTenantId("tenant1");
        SearchParameter result = searchHelper.getSearchParameter("Observation", "device");
        assertNull(result);
    }

    @Test
    public void testGetSearchParameter11() throws Exception {
        FHIRRequestContext.get().setTenantId("tenant1");
        SearchParameter result = searchHelper.getSearchParameter("Observation", "code");
        assertNotNull(result);
    }

    @Test
    public void testGetSearchParameter12() throws Exception {
        SearchParameter result = searchHelper.getSearchParameter("FamilyMemberHistory", "code");
        assertNotNull(result);
    }

    @Test
    void testDynamicSearchParameters() throws Exception {
        FHIRRegistry registry = FHIRRegistry.getInstance();
        SearchParameter searchParameter;

        // Test behavior of dynamic updates to search parameters.
        FHIRRequestContext.get().setTenantId("tenant2");

        String mainFile = "target/test-classes/config/tenant2/extension-search-parameters.json";
        String hiddenFile1 = mainFile + ".hide1";
        String hiddenFile2 = mainFile + ".hide2";

        // First, remove any copy of the main file that might exist.
        // Our initial state will be no tenant-specific search parameters present.
        deleteFile(mainFile);

        // Next, let's make sure we cannot find either of the tenant-specific search parameters.
        assertSearchParamsArentVisible();

        // Sleep a bit to allow file mod times to register.
        Thread.sleep(500);

        // Copy our first hidden file into place.
        // This should add two tenant-specific search parameters.
        copyFile(hiddenFile1, mainFile);

        // Verify that we can now "see" the two tenant-specific search parameters in the registry (dynamic)
        searchParameter = registry.getResource("http://ibm.com/fhir/SearchParameter/Patient-favorite-mlb-team",
                SearchParameter.class);
        assertNotNull(searchParameter);
        searchParameter = registry.getResource("http://ibm.com/fhir/SearchParameter/Patient-favorite-nfl-team",
                SearchParameter.class);
        assertNotNull(searchParameter);

        // Verify that we still CANT "see" the two tenant-specific search parameters from SearchUtil (not dynamic)
        assertSearchParamsArentVisible();

        // Sleep a bit to allow file mod times to register.
        Thread.sleep(500);

        // Next, copy our second hidden file into place.
        // This should effectively remove the "favorite-nfl-team" search parameter.
        copyFile(hiddenFile2, mainFile);

        // Verify that we can now "see" only the first tenant-specific search parameter in the registry (dynamic)
        searchParameter = registry.getResource("http://ibm.com/fhir/SearchParameter/Patient-favorite-mlb-team",
                SearchParameter.class);
        assertNotNull(searchParameter);
        searchParameter = registry.getResource("http://ibm.com/fhir/SearchParameter/Patient-favorite-nfl-team",
                SearchParameter.class);
        assertNull(searchParameter);

        // Verify that we still CANT "see" the two tenant-specific search parameters from SearchUtil (not dynamic)
        assertSearchParamsArentVisible();

        // Finally, remove the tenant-specific file altogether and make sure we
        // can't find either search parameter in the registry.
        deleteFile(mainFile);

        searchParameter = registry.getResource("http://ibm.com/fhir/SearchParameter/Patient-favorite-mlb-team",
                SearchParameter.class);
        assertNull(searchParameter);
        searchParameter = registry.getResource("http://ibm.com/fhir/SearchParameter/Patient-favorite-nfl-team",
                SearchParameter.class);
        assertNull(searchParameter);
    }

    private void assertSearchParamsArentVisible() throws Exception {
        SearchParameter searchParameter = searchHelper.getSearchParameter(Patient.class, "favorite-mlb-team");
        assertNull(searchParameter);
        searchParameter = searchHelper.getSearchParameter(Patient.class, "favorite-nfl-team");
        assertNull(searchParameter);
    }

    @Test
    public void testGetSearchParametersWithAllResource() throws Exception {
        // Looking only for built-in search parameters for "Patient" versus "Resource"
        FHIRRequestContext.get().setTenantId("tenant6");

        Map<String, SearchParameter> result = searchHelper.getSearchParameters("Patient");
        assertNotNull(result);
        printSearchParameters("testGetSearchParametersWithAllResource", result);
        assertEquals(33, result.size());

        // confirm that favorite-number exists as well as the RESOURCE level favorite-color
        Set<String> codes = result.keySet();
        assertTrue(codes.contains("favorite-number"));
        assertTrue(codes.contains("favorite-color"));

        result = searchHelper.getSearchParameters("CarePlan");
        assertNotNull(result);
        printSearchParameters("testGetSearchParametersWithAllResource", result);
        assertEquals(27, result.size());
        codes = result.keySet();

        // confirm that favorite-number exists as well and the RESOURCE level favorite-color does not
        assertTrue(codes.contains("favorite-number"));
        assertFalse(codes.contains("favorite-color"));
    }
}
