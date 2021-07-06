/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test.profiles;

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
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.test.TestUtil;

/**
 * Tests the US Core 3.1.1 Profile with Patient.
 * https://www.hl7.org/fhir/us/core/StructureDefinition-us-core-patient.html
 */
public class USCorePatientTest extends ProfilesTestBase {

    private static final String CLASSNAME = USCoreImmunizationTest.class.getName();
    private static final Logger logger = Logger.getLogger(CLASSNAME);

    public Boolean skip = Boolean.TRUE;
    public Boolean DEBUG = Boolean.FALSE;

    private String patientId1 = null;

    @Override
    public List<String> getRequiredProfiles() {
        //@formatter:off
        return Arrays.asList("http://hl7.org/fhir/us/core/StructureDefinition/us-core-patient|3.1.1");
        //@formatter:on
    }

    @Override
    public void setCheck(Boolean check) {
        this.skip = check;
        if (skip) {
            logger.info("Skipping Tests for 'fhir-ig-us-core - Patient', the profiles don't exist");
        }
    }

    @BeforeClass
    public void loadResources() throws Exception {
        if (!skip) {
            loadPatient1();
        }
    }

    @AfterClass
    public void deleteResources() throws Exception {
        if (!skip) {
            deletePatient1();
        }
    }

    public void loadPatient1() throws Exception {
        String resource = "json/profiles/fhir-ig-us-core/Patient-example.json";
        WebTarget target = getWebTarget();

        Patient goal = TestUtil.readExampleResource(resource);

        Entity<Patient> entity = Entity.entity(goal, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Patient").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // GET [base]/Patient/12354 (first actual test, but simple)
        patientId1 = getLocationLogicalId(response);
        response = target.path("Patient/" + patientId1).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    public void deletePatient1() throws Exception {
        WebTarget target = getWebTarget();
        Response response = target.path("Patient/" + patientId1).request(FHIRMediaType.APPLICATION_FHIR_JSON).delete();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    @Test
    public void testSearchForId() throws Exception {
        // SHALL support fetching a Patient using the _id search parameter:
        // GET [base]/Patient[id]
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("_id", patientId1);
            FHIRResponse response = client.search(Patient.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, patientId1);
        }
    }

    @Test
    public void testSearchByIdentifier() throws Exception {
        // SHALL support searching a patient by an identifier such as a MPI using the identifier search parameter:
        // GET [base]/Patient?identifier={system|}[code]
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("identifier", "http://hospital.smarthealthit.org|1032702");
            FHIRResponse response = client.search(Patient.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, patientId1);
        }
    }

    @Test
    public void testSearchByIdentifierWithoutSystem() throws Exception {
        // SHALL support searching a patient by an identifier such as a MPI using the identifier search parameter:
        // GET [base]/Patient?identifier={system|}[code]
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("identifier", "1032702");
            FHIRResponse response = client.search(Patient.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, patientId1);
        }
    }

    @Test
    public void testSearchByName() throws Exception {
        // SHALL support searching for a patient by a string match of any part of name using the name search parameter:
        // GET [base]/Patient?name=[string]
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("name:contains", "Shaw");
            FHIRResponse response = client.search(Patient.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, patientId1);
        }
    }

    @Test
    public void testSearchByNameAndBirthDate() throws Exception {
        // SHALL support searching using the combination of the birthdate and name search parameters:
        // GET [base]/Patient?birthdate=[date]&name=[string]
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("name:contains", "Shaw");
            parameters.searchParam("birthdate", "1987");
            FHIRResponse response = client.search(Patient.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, patientId1);
        }
    }

    @Test
    public void testSearchByNameAndBirthDateNoPatient() throws Exception {
        // SHALL support searching using the combination of the birthdate and name search parameters:
        // GET [base]/Patient?birthdate=[date]&name=[string]
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("name:contains", "Shaw");
            parameters.searchParam("birthdate", "1988");
            FHIRResponse response = client.search(Patient.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() == 0);
        }
    }

    @Test
    public void testSearchByGenderAndName() throws Exception {
        // SHALL support searching using the combination of the gender and name search parameters:
        // GET [base]/Patient?gender={system|}[code]&name=[string]
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("name:contains", "Shaw");
            parameters.searchParam("gender", "female");
            FHIRResponse response = client.search(Patient.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, patientId1);
        }
    }

    @Test
    public void testSearchByNameAndGenderNoPatient() throws Exception {
        // SHALL support searching using the combination of the gender and name search parameters:
        // GET [base]/Patient?gender={system|}[code]&name=[string]
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("name:contains", "Shaw");
            parameters.searchParam("gender", "male");
            FHIRResponse response = client.search(Patient.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() == 0);
        }
    }

    @Test
    public void testSearchByBirthdateAndFamily() throws Exception {
        // SHOULD support searching using the combination of the birthdate and family search parameters:
        // GET [base]/Patient?birthdate=[date]&family=[string]
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("family", "Shaw");
            parameters.searchParam("birthdate", "1987");
            FHIRResponse response = client.search(Patient.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, patientId1);
        }
    }

    @Test
    public void testSearchByGenderAndFamily() throws Exception {
        // SHOULD support searching using the combination of the family and gender search parameters:
        // GET [base]/Patient?family=[string]&gender={system|}[code]
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("family", "Shaw");
            parameters.searchParam("gender", "female");
            FHIRResponse response = client.search(Patient.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, patientId1);
        }
    }
}