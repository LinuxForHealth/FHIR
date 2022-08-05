/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.server.test.profiles.uscore.v400;

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
import org.linuxforhealth.fhir.model.resource.Patient;
import org.linuxforhealth.fhir.server.test.profiles.ProfilesTestBase.ProfilesTestBaseV2;

/**
 * Tests the US Core 4.0.0 Profile with Patient.
 * https://www.hl7.org/fhir/us/core/StructureDefinition-us-core-patient.html
 */
public class USCorePatientTest extends ProfilesTestBaseV2 {
    private String patientId1 = null;

    @Override
    public List<String> getRequiredProfiles() {
        return Arrays.asList("http://hl7.org/fhir/us/core/StructureDefinition/us-core-patient|4.0.0");
    }

    @Override
    public void loadResources() throws Exception {
        String resource = "Patient-example.json";
        Patient ptnt = USCoreExamplesUtil.readLocalJSONResource("400", resource);
        patientId1 = createResourceAndReturnTheLogicalId("Patient", ptnt);
    }

    @Test
    public void testSearchForId() throws Exception {
        // SHALL support fetching a Patient using the _id search parameter:
        // GET [base]/Patient[id]
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("_id", patientId1);
        FHIRResponse response = client.search(Patient.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, patientId1);
    }

    @Test
    public void testSearchByIdentifier() throws Exception {
        // SHALL support searching a patient by an identifier such as a MPI using the identifier search parameter:
        // GET [base]/Patient?identifier={system|}[code]
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("identifier", "http://hospital.smarthealthit.org|1032702");
        FHIRResponse response = client.search(Patient.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, patientId1);
    }

    @Test
    public void testSearchByIdentifierWithoutSystem() throws Exception {
        // SHALL support searching a patient by an identifier such as a MPI using the identifier search parameter:
        // GET [base]/Patient?identifier={system|}[code]
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("identifier", "1032702");
        FHIRResponse response = client.search(Patient.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, patientId1);
    }

    @Test
    public void testSearchByName() throws Exception {
        // SHALL support searching for a patient by a string match of any part of name using the name search parameter:
        // GET [base]/Patient?name=[string]
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("name:contains", "Shaw");
        FHIRResponse response = client.search(Patient.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, patientId1);
    }

    @Test
    public void testSearchByNameAndBirthDate() throws Exception {
        // SHALL support searching using the combination of the birthdate and name search parameters:
        // GET [base]/Patient?birthdate=[date]&name=[string]
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

    @Test
    public void testSearchByNameAndBirthDateNoPatient() throws Exception {
        // SHALL support searching using the combination of the birthdate and name search parameters:
        // GET [base]/Patient?birthdate=[date]&name=[string]
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("name:contains", "Shaw");
        parameters.searchParam("birthdate", "1988");
        FHIRResponse response = client.search(Patient.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() == 0);
    }

    @Test
    public void testSearchByGenderAndName() throws Exception {
        // SHALL support searching using the combination of the gender and name search parameters:
        // GET [base]/Patient?gender={system|}[code]&name=[string]
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

    @Test
    public void testSearchByNameAndGenderNoPatient() throws Exception {
        // SHALL support searching using the combination of the gender and name search parameters:
        // GET [base]/Patient?gender={system|}[code]&name=[string]
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("name:contains", "Shaw");
        parameters.searchParam("gender", "male");
        FHIRResponse response = client.search(Patient.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() == 0);
    }

    @Test
    public void testSearchByBirthdateAndFamily() throws Exception {
        // SHOULD support searching using the combination of the birthdate and family search parameters:
        // GET [base]/Patient?birthdate=[date]&family=[string]
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

    @Test
    public void testSearchByGenderAndFamily() throws Exception {
        // SHOULD support searching using the combination of the family and gender search parameters:
        // GET [base]/Patient?family=[string]&gender={system|}[code]
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