/**
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.server.test;

import static com.ibm.watsonhealth.fhir.model.type.Code.code;
import static com.ibm.watsonhealth.fhir.model.type.Uri.uri;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import com.ibm.watsonhealth.fhir.client.FHIRParameters;
import com.ibm.watsonhealth.fhir.client.FHIRResponse;
import com.ibm.watsonhealth.fhir.core.MediaType;
import com.ibm.watsonhealth.fhir.model.resource.Bundle;
import com.ibm.watsonhealth.fhir.model.resource.Patient;
import com.ibm.watsonhealth.fhir.model.type.Canonical;
import com.ibm.watsonhealth.fhir.model.type.Coding;
import com.ibm.watsonhealth.fhir.model.type.Instant;
import com.ibm.watsonhealth.fhir.model.type.Meta;

public class SearchAllTest extends FHIRServerTestBase {
    private String patientId;
    private Instant lastUpdated;

    @Test(groups = { "server-search-all" })
    public void testCreatePatient() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient and then call the 'create' API.
        Patient patient = readResource(Patient.class, "Patient_JohnDoe.json");

        Coding tag = Coding.builder().system(uri("http://ibm.com/watsonhealth/fhir/tag")).code(code("security"))
                .build();

        patient = patient.toBuilder().meta(Meta.builder().tag(tag)
                .profile(Canonical.of("http://ibm.com/watsonhealth/fhir/profile/Profile")).build()).build();

        Entity<Patient> entity = Entity.entity(patient, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.path("Patient").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the patient's logical id value.
        patientId = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new patient and verify it.
        response = target.path("Patient/" + patientId).request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Patient responsePatient = response.readEntity(Patient.class);
        assertResourceEquals(patient, responsePatient);

        lastUpdated = responsePatient.getMeta().getLastUpdated();
    }

    @Test(groups = { "server-search-all" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchAllUsingId() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("_id", patientId);
        FHIRResponse response = client.searchAll(parameters);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search-all" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchAllUsingLastUpdated() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("_lastUpdated", lastUpdated.getValue().toString());
        FHIRResponse response = client.searchAll(parameters);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search-all" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchAllUsingTag() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("_tag", "http://ibm.com/watsonhealth/fhir/tag|tag");
        FHIRResponse response = client.searchAll(parameters);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search-all" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchAllUsingSecurity() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("_security", "http://ibm.com/watsonhealth/fhir/security|security");
        FHIRResponse response = client.searchAll(parameters);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search-all" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchAllUsingProfile() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("_profile", "http://ibm.com/watsonhealth/fhir/profile/Profile");
        FHIRResponse response = client.searchAll(parameters);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }
}
