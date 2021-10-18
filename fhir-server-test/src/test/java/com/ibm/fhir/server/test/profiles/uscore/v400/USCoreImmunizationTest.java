/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test.profiles.uscore.v400;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import com.ibm.fhir.client.FHIRParameters;
import com.ibm.fhir.client.FHIRResponse;
import com.ibm.fhir.ig.us.core.tool.USCoreExamplesUtil;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Immunization;
import com.ibm.fhir.server.test.profiles.ProfilesTestBase.ProfilesTestBaseV2;

/**
 * Tests the US Core 4.0.0 Profile with Immunization.
 * https://www.hl7.org/fhir/us/core/StructureDefinition-us-core-Immunization.html
 */
public class USCoreImmunizationTest extends ProfilesTestBaseV2 {

    private String immunizationId1 = null;

    @Override
    public List<String> getRequiredProfiles() {
        return Arrays.asList("http://hl7.org/fhir/us/core/StructureDefinition/us-core-immunization|4.0.0");
    }

    @Override
    public void loadResources() throws Exception {
        String resource = "Immunization-imm-1.json";
        Immunization immunization = USCoreExamplesUtil.readLocalJSONResource("400", resource);
        immunizationId1 = createResourceAndReturnTheLogicalId("Immunization", immunization);
    }

    @Test
    public void testSearchForPatient() throws Exception {
        // SHALL support searching for all immunizations for a patient using the patient search parameter:
        // GET [base]/Immunization?patient=[reference]
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("patient", "Patient/example");
        parameters.searchParam("_sort", "-_lastUpdated");
        FHIRResponse response = client.search(Immunization.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, immunizationId1);
    }

    @Test
    public void testSearchForPatientAndDate() throws Exception {
        // SHOULD support searching using the combination of the patient and lifecycle-status search parameters:
        // GET [base]/Immunization?patient=[reference]&lifecycle-status={system|}[code]
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("patient", "Patient/example");
        parameters.searchParam("date", "ge2019");
        parameters.searchParam("date", "lt2021");
        parameters.searchParam("_sort", "-_lastUpdated");
        FHIRResponse response = client.search(Immunization.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, immunizationId1);
    }

    @Test
    public void testSearchForPatientAndStatus() throws Exception {
        // SHOULD support searching using the combination of the patient and status search parameters:
        // GET [base]/Immunization?patient=[reference]&status={system|}[code]
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("patient", "Patient/example");
        parameters.searchParam("status", "completed");
        parameters.searchParam("_sort", "-_lastUpdated");
        FHIRResponse response = client.search(Immunization.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, immunizationId1);
    }
}