/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.parameters;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.model.resource.Observation;
import com.ibm.fhir.model.resource.SearchParameter;
import com.ibm.fhir.search.test.BaseSearchTest;
import com.ibm.fhir.search.util.SearchUtil;

/**
 * Tests the ParametersUtil through the SearchUtil.
 */
public class ParametersSearchUtilTest extends BaseSearchTest {
    public static final boolean DEBUG = false;

    @Override
    @BeforeClass
    public void setup() {
        FHIRConfiguration.setConfigHome("src/test/resources");
    }

    @AfterMethod
    public void cleanup() throws FHIRException {
        // Restore the threadLocal FHIRRequestContext to the default tenant
        FHIRRequestContext.get().setTenantId("default");
    }

    @Test
    public void testGetSearchParameters1Default() throws Exception {
        // Simple test looking only for built-in search parameters for Observation.class.
        // Use default tenant id ("default") which has no Observation tenant-specific
        // search parameters.
        List<SearchParameter> result = SearchUtil.getApplicableSearchParameters(Observation.class.getSimpleName());
        assertNotNull(result);
        assertFalse(result.isEmpty());
        printSearchParameters("testGetSearchParameters1", result);

        if (DEBUG) {
            ParametersUtil.print(System.out);
        }

        assertEquals(44, result.size());
    }

    @Test
    public void testGetSearchParameters2Default() throws Exception {
        // Looking for built-in and tenant-specific search parameters for "Patient"
        // and "Observation". Use the default tenant since it has some Patient search
        // parameters defined.
        FHIRRequestContext.get().setTenantId("default");

        List<SearchParameter> result = SearchUtil.getApplicableSearchParameters("Patient");
        assertNotNull(result);
        printSearchParameters("testGetSearchParameters2/Patient", result);
        assertEquals(37, result.size());

        result = SearchUtil.getApplicableSearchParameters("Observation");
        assertNotNull(result);
        printSearchParameters("testGetSearchParameters2/Observation", result);
        assertEquals(44, result.size());
    }

    @Test
    public void testGetSearchParameters3Tenant() throws Exception {
        // Looking for built-in and tenant-specific search parameters for "Observation".

        // Use tenant1 since it has some Patient search parameters defined.
        FHIRRequestContext.get().setTenantId("tenant1");

        // tenant1's filtering includes only 1 search parameter for Observation.
        List<SearchParameter> result = SearchUtil.getApplicableSearchParameters("Observation");
        assertNotNull(result);
        printSearchParameters("testGetSearchParameters3/Observation", result);

        // Simple conversion and output.
        if (DEBUG) {
            System.out.println("As Follows: ");
            System.out.println(result.stream().map(in -> in.getCode().getValue()).collect(Collectors.toList()));
        }
        assertEquals(8, result.size());
        Set<String> codes = result.stream().map(sp -> sp.getCode().getValue()).collect(Collectors.toSet());
        assertTrue(codes.contains("code"));
        assertTrue(codes.contains("value-range"));
        assertTrue(codes.contains("_lastUpdated"));
        assertTrue(codes.contains("_id"));

        result = SearchUtil.getApplicableSearchParameters("Immunization");
        assertNotNull(result);
        printSearchParameters("testGetSearchParameters3/Immunization", result);
        assertEquals(22, result.size());
    }

    @Test
    public void testGetSearchParameters4Tenant() throws Exception {
        // Test filtering of search parameters for Device (tenant1).
        FHIRRequestContext.get().setTenantId("tenant1");

        List<SearchParameter> result = SearchUtil.getApplicableSearchParameters("Device");
        assertNotNull(result);
        printSearchParameters("testGetSearchParameters4/Device", result);
        assertEquals(8, result.size());
        List<String> codes = getSearchParameterCodes(result);
        assertTrue(codes.contains("patient"));
        assertTrue(codes.contains("organization"));
    }

    @Test
    public void testGetSearchParameters5Tenant() throws Exception {
        // Test filtering of search parameters for Patient (tenant1).
        FHIRRequestContext.get().setTenantId("tenant1");

        List<SearchParameter> result = SearchUtil.getApplicableSearchParameters("Patient");
        assertNotNull(result);
        printSearchParameters("testGetSearchParameters5/Patient", result);
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
        printSearchParameters("testGetSearchParameters5/MedicationAdministration", result);
        assertEquals(19, result.size());
    }

    @Test
    public void testGetSearchParameters6Default() throws Exception {
        // Test filtering of search parameters for Patient (default tenant).
        FHIRRequestContext.get().setTenantId("default");

        List<SearchParameter> result = SearchUtil.getApplicableSearchParameters("Patient");
        assertNotNull(result);
        printSearchParameters("testGetSearchParameters6/Patient", result);
        assertEquals(37, result.size());

        result = SearchUtil.getApplicableSearchParameters("Device");
        assertNotNull(result);
        printSearchParameters("testGetSearchParameters6/Device", result);
        assertEquals(20, result.size());
    }

    @Test
    public void testVersionedSearchParameterFilter() throws Exception {
        // Test filtering of search parameters for Patient (default tenant).
        FHIRRequestContext.get().setTenantId("tenant4");

        List<SearchParameter> result = SearchUtil.getApplicableSearchParameters("Device");
        assertNotNull(result);
        printSearchParameters("testVersionedSearchParameterFilter/Device", result);
        boolean found = false;
        for (SearchParameter sp : result) {
            System.out.println(sp.getUrl().getValue() + "|" + sp.getVersion().getValue());
            if ("http://example.com/SearchParameter/sp_a".equals(sp.getUrl().getValue())) {
                assertNotEquals("1.0.1", sp.getVersion());
                found = true;
            }
        }
        assertTrue(found);

        FHIRRequestContext.get().setTenantId("tenant5");

        result = SearchUtil.getApplicableSearchParameters("Device");
        assertNotNull(result);
        printSearchParameters("testVersionedSearchParameterFilter/Device", result);
        found = false;
        for (SearchParameter sp : result) {
            if ("http://example.com/SearchParameter/sp_a".equals(sp.getUrl().getValue())) {
                assertNotEquals("1.0.0", sp.getVersion());
                found = true;
            }
        }
        assertTrue(found);
    }
}
