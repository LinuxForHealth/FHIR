/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.parameters;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertNull;
import static org.testng.AssertJUnit.assertTrue;

import java.util.List;
import java.util.stream.Collectors;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.model.resource.Basic;
import com.ibm.fhir.model.resource.Device;
import com.ibm.fhir.model.resource.Observation;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.SearchParameter;
import com.ibm.fhir.search.test.BaseSearchTest;
import com.ibm.fhir.search.util.SearchUtil;

/**
 * This class tests various multi-tenant enabled search parameter-related methods of the SearchUtil class.
 */
public class MultiTenantSearchParameterTest extends BaseSearchTest {

    @Override
    @BeforeClass
    public void setup() {
        FHIRConfiguration.setConfigHome("target/test-classes");
    }

    @Test
    public void testGetApplicableSearchParameters2() throws Exception {
        // Looking only for built-in search parameters for "Patient".

        // Use tenant1 since it doesn't have any tenant-specific search parameters for resourceType Medication.
        FHIRRequestContext.set(new FHIRRequestContext("tenant1"));

        // Using Medication because tenant1 has filters in place for Patient and Observation
        List<SearchParameter> result = SearchUtil.getApplicableSearchParameters("Medication");
        assertNotNull(result);
        printSearchParameters("testGetApplicableSearchParameters2", result);
        assertEquals(15, result.size());
    }

    @Test
    public void testGetApplicableSearchParameters1() throws Exception {
        // Simple test looking only for built-in search parameters for Observation.class.
        // Use default tenant id ("default") which has no Observation tenant-specifc search parameters.
        List<SearchParameter> result = SearchUtil.getApplicableSearchParameters("Observation");
        assertNotNull(result);
        printSearchParameters("testGetApplicableSearchParameters1", result);
        assertEquals(44, result.size());
    }

    @Test
    public void testGetApplicableSearchParameters3() throws Exception {
        // Looking for built-in and tenant-specific search parameters for "Patient" and "Observation".

        // Use the default tenant since it has some Patient search parameters defined.
        FHIRRequestContext.set(new FHIRRequestContext("default"));

        List<SearchParameter> result = SearchUtil.getApplicableSearchParameters("Patient");
        assertNotNull(result);
        printSearchParameters("testGetApplicableSearchParameters3/Patient", result);
        assertEquals(35, result.size());

        result = SearchUtil.getApplicableSearchParameters("Observation");
        assertNotNull(result);
        printSearchParameters("testGetApplicableSearchParameters3/Observation", result);
        assertEquals(44, result.size());
    }

    @Test
    public void testGetApplicableSearchParameters4() throws Exception {
        // Looking for built-in and tenant-specific search parameters for "Observation".

        // Use tenant1 since it has some Patient search parameters defined.
        FHIRRequestContext.set(new FHIRRequestContext("tenant1"));

        // tenant1's filtering includes only 1 search parameter for Observation.
        List<SearchParameter> result = SearchUtil.getApplicableSearchParameters("Observation");
        assertNotNull(result);
        printSearchParameters("testGetApplicableSearchParameters4/Observation", result);
        assertEquals(8, result.size());
        List<String> codes = getSearchParameterCodes(result);
        assertTrue(codes.contains("code"));
        assertTrue(codes.contains("_id"));
    }

    @Test
    public void testGetApplicableSearchParameters5() throws Exception {
        // Test filtering of search parameters for Device (tenant1).
        FHIRRequestContext.set(new FHIRRequestContext("tenant1"));

        List<SearchParameter> result = SearchUtil.getApplicableSearchParameters("Device");
        assertNotNull(result);
        printSearchParameters("testGetApplicableSearchParameters5/Device", result);
        assertEquals(8, result.size());
        List<String> codes = getSearchParameterCodes(result);
        assertTrue(codes.contains("patient"));
        assertTrue(codes.contains("organization"));
    }

    @Test
    public void testGetApplicableSearchParameters6() throws Exception {
        // Test filtering of search parameters for Patient (tenant1).
        FHIRRequestContext.set(new FHIRRequestContext("tenant1"));

        List<SearchParameter> result = SearchUtil.getApplicableSearchParameters("Patient");
        assertNotNull(result);
        printSearchParameters("testGetApplicableSearchParameters6/Patient", result);
        assertEquals(10, result.size());
        List<String> codes = getSearchParameterCodes(result);
        assertTrue(codes.contains("active"));
        assertTrue(codes.contains("address"));
        assertTrue(codes.contains("birthdate"));
        assertTrue(codes.contains("name"));

        // Make sure we get all of the MedicationAdministration search parameters.
        // (No filtering configured for these)
        result = SearchUtil.getApplicableSearchParameters("MedicationAdministration");
        assertNotNull(result);
        printSearchParameters("testGetApplicableSearchParameters6/MedicationAdministration", result);
        assertEquals(19, result.size());
    }

    @Test
    public void testGetApplicableSearchParameters7() throws Exception {
        // Test filtering of search parameters for Patient (default tenant).
        FHIRRequestContext.set(new FHIRRequestContext("default"));

        List<SearchParameter> result = SearchUtil.getApplicableSearchParameters("Patient");
        assertNotNull(result);
        printSearchParameters("testGetApplicableSearchParameters7/Patient", result);
        assertEquals(35, result.size());

        result = SearchUtil.getApplicableSearchParameters("Device");
        assertNotNull(result);
        printSearchParameters("testGetApplicableSearchParameters7/Device", result);
        assertEquals(18, result.size());
    }

    @Test
    public void testGetSearchParameters2() throws Exception {
        // Looking only for built-in search parameters for "Patient".

        // Use tenant3 since it doesn't have any tenant-specific search parameters.
        FHIRRequestContext.set(new FHIRRequestContext("tenant3"));

        List<SearchParameter> result = SearchUtil.getApplicableSearchParameters("Patient");
        assertNotNull(result);
        printSearchParameters("testGetSearchParameters2", result);
        assertEquals(29, result.size());
    }

    @Test
    public void testGetSearchParameter1() throws Exception {
        // Use tenant1 since it has some Patient search parameters defined.
        FHIRRequestContext.set(new FHIRRequestContext("tenant1"));

        SearchParameter result = SearchUtil.getSearchParameter(Basic.class, "measurement-type");
        assertNotNull(result);
    }

    @Test
    public void testGetSearchParameter2() throws Exception {
        // Use tenant1 since it has some Patient search parameters defined.
        FHIRRequestContext.set(new FHIRRequestContext("tenant1"));

        SearchParameter result = SearchUtil.getSearchParameter(Observation.class, "value-range");
        assertNotNull(result);

        result = SearchUtil.getSearchParameter(Observation.class, "code");
        assertNotNull(result);

        result = SearchUtil.getSearchParameter(Observation.class, "category");
        assertNull(result);

        // "active" is a search parameter belonging to Patient, not Observation.
        result = SearchUtil.getSearchParameter(Observation.class, "active");
        assertNull(result);

        result = SearchUtil.getSearchParameter(Observation.class, "bad-sp-name");
        assertNull(result);
    }

    @Test
    public void testGetSearchParameter3() throws Exception {
        FHIRRequestContext.set(new FHIRRequestContext("tenant1"));
        SearchParameter result = SearchUtil.getSearchParameter("Observation", "value-range-value");
        assertNull(result);
    }

    @Test
    public void testGetSearchParameter4() throws Exception {
        // Use tenant1 since it has some Patient search parameters defined.
        FHIRRequestContext.set(new FHIRRequestContext("tenant1"));

        SearchParameter result = SearchUtil.getSearchParameter("Device", "patient");
        assertNotNull(result);

        result = SearchUtil.getSearchParameter("Device", "organization");
        assertNotNull(result);

        result = SearchUtil.getSearchParameter(Device.class, "organization");
        assertNotNull(result);

        result = SearchUtil.getSearchParameter(Observation.class, "category");
        assertNull(result);

        // "active" is a search parameter belonging to Patient, not Observation.
        result = SearchUtil.getSearchParameter(Observation.class, "active");
        assertNull(result);

        result = SearchUtil.getSearchParameter(Observation.class, "bad-sp-name");
        assertNull(result);
    }

    @Test
    public void testGetSearchParameter5() throws Exception {
        SearchParameter result = SearchUtil.getSearchParameter("Observation", "_lastUpdated");
        assertNotNull(result);
    }

    @Test
    public void testGetSearchParameter6() throws Exception {
        SearchParameter result = SearchUtil.getSearchParameter(Observation.class, "_lastUpdated");
        assertNotNull(result);
    }

    @Test
    public void testGetSearchParameter7() throws Exception {
        FHIRRequestContext.set(new FHIRRequestContext("tenant1"));
        SearchParameter result = SearchUtil.getSearchParameter("Observation", "_lastUpdated");
        assertNotNull(result);
    }

    @Test
    public void testGetSearchParameter8() throws Exception {
        FHIRRequestContext.set(new FHIRRequestContext("tenant1"));
        SearchParameter result = SearchUtil.getSearchParameter(Observation.class, "_lastUpdated");
        assertNotNull(result);
    }

    @Test
    public void testGetSearchParameter9() throws Exception {
        SearchParameter result = SearchUtil.getSearchParameter("Observation", "device");
        assertNotNull(result);
    }

    @Test
    public void testGetSearchParameter10() throws Exception {
        FHIRRequestContext.set(new FHIRRequestContext("tenant1"));
        SearchParameter result = SearchUtil.getSearchParameter("Observation", "device");
        assertNull(result);
    }

    @Test
    public void testGetSearchParameter11() throws Exception {
        FHIRRequestContext.set(new FHIRRequestContext("tenant1"));
        SearchParameter result = SearchUtil.getSearchParameter("Observation", "code");
        assertNotNull(result);
    }

    @Test
    void testDynamicSearchParameters1() throws Exception {
        // Test behavior of dynamic updates to search parameters.
        FHIRRequestContext.set(new FHIRRequestContext("tenant2"));

        String mainFile = "target/test-classes/config/tenant2/extension-search-parameters.json";
        String hiddenFile1 = mainFile + ".hide1";
        String hiddenFile2 = mainFile + ".hide2";

        // First, remove any copy of the main file that might exist.
        // Our initial state will be no tenant-specific search parameters present.
        deleteFile(mainFile);

        // Next, let's make sure we cannot find either of the tenant-specific search parameters.
        SearchParameter searchParameter = SearchUtil.getSearchParameter(Patient.class, "favorite-mlb-team");
        assertNull(searchParameter);
        searchParameter = SearchUtil.getSearchParameter(Patient.class, "favorite-nfl-team");
        assertNull(searchParameter);

        // Sleep a bit to allow file mod times to register.
        Thread.sleep(1000);

        // Copy our first hidden file into place.
        // This should add two tenant-specific search parameters.
        copyFile(hiddenFile1, mainFile);

        // Verify that we can now "see" the two tenant-specific search parameters.
        searchParameter = SearchUtil.getSearchParameter(Patient.class, "favorite-mlb-team");
        assertNotNull(searchParameter);
        searchParameter = SearchUtil.getSearchParameter(Patient.class, "favorite-nfl-team");
        assertNotNull(searchParameter);

        // Sleep a bit to allow file mod times to register.
        Thread.sleep(1000);

        // Next, copy our second hidden file into place.
        // This should effectively remove the "favorite-nfl-team" search parameter.
        copyFile(hiddenFile2, mainFile);

        // Verify that we can now "see" only the first tenant-specific search parameter.
        searchParameter = SearchUtil.getSearchParameter(Patient.class, "favorite-mlb-team");
        assertNotNull(searchParameter);
        searchParameter = SearchUtil.getSearchParameter(Patient.class, "favorite-nfl-team");
        assertNull(searchParameter);

        // Finally, remove the tenant-specific file altogether and make sure we
        // can't find either search parameter again.
        deleteFile(mainFile);
        searchParameter = SearchUtil.getSearchParameter(Patient.class, "favorite-mlb-team");
        assertNull(searchParameter);
        searchParameter = SearchUtil.getSearchParameter(Patient.class, "favorite-nfl-team");
        assertNull(searchParameter);
    }

    @Test
    void testDynamicSearchParameters2() throws Exception {
        // Test behavior related to falling back to default tenant.
        FHIRRequestContext.set(new FHIRRequestContext("tenant2"));

        String mainFile = "target/test-classes/config/tenant2/extension-search-parameters.json";
        String hiddenFile1 = mainFile + ".hide1";

        // First, remove any copy of the main file that might exist.
        // Our initial state will be no tenant-specific search parameters present.
        deleteFile(mainFile);

        // With no tenant-specific search parameters, we should fall back to the default tenant which defines patient
        // extensions
        SearchParameter searchParameter = SearchUtil.getSearchParameter(Patient.class, "favorite-mlb-team");
        assertNull(searchParameter);
        searchParameter = SearchUtil.getSearchParameter(Patient.class, "favorite-color");
        assertNotNull(searchParameter);

        List<SearchParameter> result = SearchUtil.getApplicableSearchParameters("Patient");
        assertNotNull(result);
        printSearchParameters("testDynamicSearchParameters2/Patient", result);
        assertEquals(35, result.size());

        // Sleep a bit to allow file mod times to register.
        Thread.sleep(1000);

        // Copy our first hidden file into place.
        // This should add two tenant-specific search parameters.
        copyFile(hiddenFile1, mainFile);

        // Verify that we can now see the two tenant-specific search parameters and NOT the default tenant ones
        searchParameter = SearchUtil.getSearchParameter(Patient.class, "favorite-mlb-team");
        assertNotNull(searchParameter);
        searchParameter = SearchUtil.getSearchParameter(Patient.class, "favorite-color");
        assertNull(searchParameter);

        result = SearchUtil.getApplicableSearchParameters("Patient");
        assertNotNull(result);
        printSearchParameters("testDynamicSearchParameters2/Patient", result);
        assertNotEquals(33, result.size());

        // Sleep a bit to allow file mod times to register.
        Thread.sleep(1000);

        // Finally, delete the tenant-specific config file and verify that we fall back to the default ones
        deleteFile(mainFile);
        searchParameter = SearchUtil.getSearchParameter(Patient.class, "favorite-mlb-team");
        assertNull(searchParameter);
        searchParameter = SearchUtil.getSearchParameter(Patient.class, "favorite-color");
        assertNotNull(searchParameter);

        result = SearchUtil.getApplicableSearchParameters("Patient");
        assertNotNull(result);
        printSearchParameters("testDynamicSearchParameters2/Patient", result);
        assertEquals(35, result.size());
    }

    @Test
    public void testGetSearchParametersWithAllResource() throws Exception {
        // Looking only for built-in search parameters for "Patient" versus "Resource"
        FHIRRequestContext.set(new FHIRRequestContext("tenant6"));

        List<SearchParameter> result = SearchUtil.getApplicableSearchParameters("Patient");
        assertNotNull(result);
        printSearchParameters("testGetSearchParametersWithAllResource", result);
        assertEquals(31, result.size());

        // confirm that favorite-number exists as well as the RESOURCE level favorite-color
        List<String> codes = result.stream().map(r -> r.getCode().getValue()).collect(Collectors.toList());
        assertTrue(codes.contains("favorite-number"));
        assertTrue(codes.contains("favorite-color"));

        result = SearchUtil.getApplicableSearchParameters("CarePlan");
        assertNotNull(result);
        printSearchParameters("testGetSearchParametersWithAllResource", result);
        assertEquals(27, result.size());
        codes = result.stream().map(r -> r.getCode().getValue()).collect(Collectors.toList());

        // confirm that favorite-number exists as well and the RESOURCE level favorite-color does not
        assertTrue(codes.contains("favorite-number"));
        assertFalse(codes.contains("favorite-color"));
    }
}
