/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test.profiles.uscore.v311;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.client.FHIRParameters;
import com.ibm.fhir.client.FHIRResponse;
import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Observation;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.server.test.profiles.ProfilesTestBaseV2;

/**
 * Tests the US Core 3.1.1 Profile with Observation on Vital Signs
 *
 * https://www.hl7.org/fhir/us/core/StructureDefinition-us-core-smokingstatus.html
 *
 * using https://www.hl7.org/fhir/us/core/vitals-search.html as a guide
 */
public class USCoreObservationSmokingStatusTest extends ProfilesTestBaseV2 {

    private static final String CLASSNAME = USCoreObservationSmokingStatusTest.class.getName();
    private static final Logger logger = Logger.getLogger(CLASSNAME);

    public Boolean skip = Boolean.TRUE;
    public Boolean DEBUG = Boolean.FALSE;

    private String observationId1 = null;

    @Override
    public List<String> getRequiredProfiles() {
        //@formatter:off
        return Arrays.asList("http://hl7.org/fhir/us/core/StructureDefinition/us-core-smokingstatus|3.1.1");
        //@formatter:on
    }

    @Override
    public void setCheck(Boolean check) {
        this.skip = check;
        if (skip) {
            logger.info("Skipping Tests for 'fhir-ig-us-core - Observation', the profiles don't exist");
        }
    }

    @BeforeClass
    public void loadResources() throws Exception {
        if (!skip) {
            loadObservation1();
        }
    }

    @AfterClass
    public void deleteResources() throws Exception {
        if (!skip) {
            deleteObservation1();
        }
    }

    public void loadObservation1() throws Exception {
        String resource = "json/profiles/fhir-ig-us-core/Observation-some-day-smoker.json";
        WebTarget target = getWebTarget();

        Observation observation = TestUtil.readExampleResource(resource);
        observation = observation.toBuilder().effective(DateTime.of("2016")).build();
        Entity<Observation> entity = Entity.entity(observation, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Observation").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // GET [base]/Observation/12354 (first actual test, but simple)
        observationId1 = getLocationLogicalId(response);
        response = target.path("Observation/" + observationId1).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    public void deleteObservation1() throws Exception {
        WebTarget target = getWebTarget();
        Response response = target.path("Observation/" + observationId1).request(FHIRMediaType.APPLICATION_FHIR_JSON).delete();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    @Test
    public void testSearchForPatient() throws Exception {
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("patient", "Patient/example");
            FHIRResponse response = client.search(Observation.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, observationId1);
        }
    }

    @Test
    public void testSearchForPatientAndCategory() throws Exception {
        // SHALL support searching using the combination of the patient and category search parameters:
        // GET
        // [base]/Observation?patient=[reference]&category=http://terminology.hl7.org/CodeSystem/observation-category|social-history
        // GET
        // [base]/Observation?patient=1134281&category=http://terminology.hl7.org/CodeSystem/observation-category|social-history

        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("patient", "Patient/example");
            parameters.searchParam("category", "http://terminology.hl7.org/CodeSystem/observation-category|social-history");
            FHIRResponse response = client.search(Observation.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, observationId1);
        }
    }

    @Test
    public void testSearchForPatientAndCode() throws Exception {
        // SHALL support searching using the combination of the patient and code search parameters:
        // including optional support for composite OR search on code (e.g.code={system|}[code],{system|}[code],...)
        // GET [base]/Observation?patient=[reference]&code={system|}[code]{,{system|}[code],...}

        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("patient", "Patient/example");
            parameters.searchParam("code", "72166-2");
            FHIRResponse response = client.search(Observation.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, observationId1);
        }
    }

    @Test
    public void testSearchForPatientAndCodeAndSystem() throws Exception {
        // SHALL support searching using the combination of the patient and code search parameters:
        // including optional support for composite OR search on code (e.g.code={system|}[code],{system|}[code],...)
        // GET [base]/Observation?patient=[reference]&code={system|}[code]{,{system|}[code],...}

        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("patient", "Patient/example");
            parameters.searchParam("code", "http://loinc.org|72166-2");
            FHIRResponse response = client.search(Observation.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, observationId1);
        }
    }

    @Test
    public void testSearchForPatientAndCategoryAndDate() throws Exception {
        // SHALL support searching using the combination of the patient and category and date search parameters:
        // including support for these date comparators: gt,lt,ge,le
        // including optional support for composite AND search on date (e.g.date=[date]&date=[date]]&...)
        // GET [base]/Observation?patient=[reference]&category=http://terminology.hl7.org/CodeSystem/observation-category|social-history&date={gt|lt|ge|le}[date]{&date={gt|lt|ge|le}[date]&...}

        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("patient", "Patient/example");
            parameters.searchParam("category", "http://terminology.hl7.org/CodeSystem/observation-category|social-history");
            parameters.searchParam("date", "eq2016");
            FHIRResponse response = client.search(Observation.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, observationId1);
        }
    }

    @Test
    public void testSearchForPatientAndCategoryAndStatus() throws Exception {
        // SHOULD support searching using the combination of the patient and category and status search parameters:
        // including support for composite OR search on status (e.g.status={system|}[code],{system|}[code],...)
        // GET [base]/Observation?patient=[reference]&category=http://terminology.hl7.org/CodeSystem/observation-category|social-history&status={system|}[code]{,{system|}[code],...}

        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("patient", "Patient/example");
            parameters.searchParam("category", "http://terminology.hl7.org/CodeSystem/observation-category|social-history");
            parameters.searchParam("status", "final");
            FHIRResponse response = client.search(Observation.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, observationId1);
        }
    }

    @Test
    public void testSearchForPatientAndCodeAndDate() throws Exception {
        // SHOULD support searching using the combination of the patient and code and date search parameters:
        // including optional support for composite OR search on code (e.g.code={system|}[code],{system|}[code],...)
        // including support for these date comparators: gt,lt,ge,le
        // including optional support for composite AND search on date (e.g.date=[date]&date=[date]]&...)

        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("patient", "Patient/example");
            parameters.searchParam("category", "http://terminology.hl7.org/CodeSystem/observation-category|social-history");
            parameters.searchParam("code", "http://loinc.org|72166-2");
            parameters.searchParam("date", "eq2016");
            FHIRResponse response = client.search(Observation.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, observationId1);
        }
    }

    @Test
    public void testSearchForPatientAndCodeAndBadDate() throws Exception {
        // SHOULD support searching using the combination of the patient and code and date search parameters:
        // including optional support for composite OR search on code (e.g.code={system|}[code],{system|}[code],...)
        // including support for these date comparators: gt,lt,ge,le
        // including optional support for composite AND search on date (e.g.date=[date]&date=[date]]&...)

        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("patient", "Patient/example");
            parameters.searchParam("category", "http://terminology.hl7.org/CodeSystem/observation-category|social-history");
            parameters.searchParam("code", "http://loinc.org|72166-2");
            parameters.searchParam("date", "eq2019");
            FHIRResponse response = client.search(Observation.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() == 0);
        }
    }
}