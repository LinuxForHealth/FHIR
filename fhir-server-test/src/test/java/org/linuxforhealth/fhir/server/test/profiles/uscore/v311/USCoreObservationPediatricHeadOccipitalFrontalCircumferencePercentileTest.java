/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.server.test.profiles.uscore.v311;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.client.FHIRParameters;
import org.linuxforhealth.fhir.client.FHIRResponse;
import org.linuxforhealth.fhir.ig.us.core.tool.USCoreExamplesUtil;
import org.linuxforhealth.fhir.model.resource.Bundle;
import org.linuxforhealth.fhir.model.resource.Observation;
import org.linuxforhealth.fhir.server.test.profiles.ProfilesTestBase.ProfilesTestBaseV2;

/**
 * Tests the US Core 3.1.1 Profile with Observation on Vital Signs
 *
 * https://www.hl7.org/fhir/us/core/StructureDefinition-head-occipital-frontal-circumference-percentile.html
 *
 * using https://www.hl7.org/fhir/us/core/vitals-search.html as a guide
 */
public class USCoreObservationPediatricHeadOccipitalFrontalCircumferencePercentileTest extends ProfilesTestBaseV2 {

    private String observationId1 = null;

    @Override
    public List<String> getRequiredProfiles() {
        return Arrays.asList("http://hl7.org/fhir/us/core/StructureDefinition/head-occipital-frontal-circumference-percentile|3.1.1");
    }

    @Override
    public void loadResources() throws Exception {
        String resource = "Observation-ofc-percentile.json";
        Observation observation = USCoreExamplesUtil.readLocalJSONResource("311", resource);
        observationId1 = createResourceAndReturnTheLogicalId("Observation", observation);
    }

    @Test
    public void testSearchForPatient() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("patient", "Patient/infant-example");
        FHIRResponse response = client.search(Observation.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, observationId1);
    }

    @Test
    public void testSearchForPatientAndCategory() throws Exception {
        // SHALL support searching using the combination of the patient and category search parameters:
        // GET
        // [base]/Observation?patient=[reference]&category=http://terminology.hl7.org/CodeSystem/observation-category|vital-signs
        // GET
        // [base]/Observation?patient=1134281&category=http://terminology.hl7.org/CodeSystem/observation-category|vital-signs
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("patient", "Patient/infant-example");
        parameters.searchParam("category", "http://terminology.hl7.org/CodeSystem/observation-category|vital-signs");
        FHIRResponse response = client.search(Observation.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, observationId1);
    }

    @Test
    public void testSearchForPatientAndCode() throws Exception {
        // SHALL support searching using the combination of the patient and code search parameters:
        // including optional support for composite OR search on code (e.g.code={system|}[code],{system|}[code],...)
        // GET [base]/Observation?patient=[reference]&code={system|}[code]{,{system|}[code],...}
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("patient", "Patient/infant-example");
        parameters.searchParam("code", "8289-1");
        FHIRResponse response = client.search(Observation.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, observationId1);
    }

    @Test
    public void testSearchForPatientAndCodeAndSystem() throws Exception {
        // SHALL support searching using the combination of the patient and code search parameters:
        // including optional support for composite OR search on code (e.g.code={system|}[code],{system|}[code],...)
        // GET [base]/Observation?patient=[reference]&code={system|}[code]{,{system|}[code],...}
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("patient", "Patient/infant-example");
        parameters.searchParam("code", "http://loinc.org|8289-1");
        FHIRResponse response = client.search(Observation.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, observationId1);
    }

    @Test
    public void testSearchForPatientAndCategoryAndDate() throws Exception {
        // SHALL support searching using the combination of the patient and category and date search parameters:
        // including support for these date comparators: gt,lt,ge,le
        // including optional support for composite AND search on date (e.g.date=[date]&date=[date]]&...)
        // GET
        // [base]/Observation?patient=[reference]&category=http://terminology.hl7.org/CodeSystem/observation-category|vital-signs&date={gt|lt|ge|le}[date]{&date={gt|lt|ge|le}[date]&...}
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("patient", "Patient/infant-example");
        parameters.searchParam("category", "http://terminology.hl7.org/CodeSystem/observation-category|vital-signs");
        parameters.searchParam("date", "eq2020");
        FHIRResponse response = client.search(Observation.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, observationId1);
    }

    @Test
    public void testSearchForPatientAndCategoryAndStatus() throws Exception {
        // SHOULD support searching using the combination of the patient and category and status search parameters:
        // including support for composite OR search on status (e.g.status={system|}[code],{system|}[code],...)
        // GET
        // [base]/Observation?patient=[reference]&category=http://terminology.hl7.org/CodeSystem/observation-category|vital-signs&status={system|}[code]{,{system|}[code],...}
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("patient", "Patient/infant-example");
        parameters.searchParam("category", "http://terminology.hl7.org/CodeSystem/observation-category|vital-signs");
        parameters.searchParam("status", "final");
        FHIRResponse response = client.search(Observation.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, observationId1);
    }

    @Test
    public void testSearchForPatientAndCodeAndDate() throws Exception {
        // SHOULD support searching using the combination of the patient and code and date search parameters:
        // including optional support for composite OR search on code (e.g.code={system|}[code],{system|}[code],...)
        // including support for these date comparators: gt,lt,ge,le
        // including optional support for composite AND search on date (e.g.date=[date]&date=[date]]&...)
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("patient", "Patient/infant-example");
        parameters.searchParam("category", "http://terminology.hl7.org/CodeSystem/observation-category|vital-signs");
        parameters.searchParam("code", "http://loinc.org|8289-1");
        // parameters.searchParam("date", "eq2020");
        FHIRResponse response = client.search(Observation.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, observationId1);
    }

    @Test
    public void testSearchForPatientAndCodeAndBadDate() throws Exception {
        // SHOULD support searching using the combination of the patient and code and date search parameters:
        // including optional support for composite OR search on code (e.g.code={system|}[code],{system|}[code],...)
        // including support for these date comparators: gt,lt,ge,le
        // including optional support for composite AND search on date (e.g.date=[date]&date=[date]]&...)
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("patient", "Patient/infant-example");
        parameters.searchParam("category", "http://terminology.hl7.org/CodeSystem/observation-category|vital-signs");
        parameters.searchParam("code", "http://loinc.org|8289-1");
        parameters.searchParam("date", "eq2019");
        FHIRResponse response = client.search(Observation.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() == 0);
    }
}