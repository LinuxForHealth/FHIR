/*
 * (C) Copyright IBM Corp. 2017, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.code.AdministrativeGender;

public class SearchReferenceTest extends FHIRServerTestBase {

    private String patientResourceIdWithReferenceId;
    private String patientResourceIdWithReferenceTypeId;
    private String patientResourceIdWithReferenceUrl;
    private String patientResourceIdWithReferenceCanonicalUri;

    /*
     * creates the various test cases for a patient with a reference
     */
    public String createPatientWithReference(String reference) throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient and then call the 'create' API.
        Patient patient = TestUtil.readLocalResource("Patient_JohnDoe.json");

        patient =
                patient.toBuilder().gender(AdministrativeGender.MALE).managingOrganization(Reference.builder().display(com.ibm.fhir.model.type.String.of("Test Organization")).reference(com.ibm.fhir.model.type.String.of(reference)).build()).build();
        Entity<Patient> entity =
                Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response =
                target.path("Patient").request()
                .post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the patient's logical id value.
        String tmpPatientId = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new patient and verify it.
        response = target.path("Patient/"
                + tmpPatientId).request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Patient responsePatient = response.readEntity(Patient.class);
        TestUtil.assertResourceEquals(patient, responsePatient);
        return tmpPatientId;
    }

    @Test(groups = { "server-search-reference" })
    public void testCreatePatient() throws Exception {
        patientResourceIdWithReferenceId = createPatientWithReference("3001");
        patientResourceIdWithReferenceTypeId = createPatientWithReference("Organization/3002");
        patientResourceIdWithReferenceUrl = createPatientWithReference("https://localhost:9443/fhir-server/api/v4/Organization/3003");
        patientResourceIdWithReferenceCanonicalUri = createPatientWithReference("https://localhost:9443/fhir-server/api/v4/Organization/3004|1.0.0");

    }

    @Test(groups = { "server-search-reference" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchWithRelativePatientIdUsingId() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient").queryParam("organization", "3001")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        Patient p = null;
        for (Bundle.Entry entry : bundle.getEntry()) {
            if (entry.getResource() != null && entry.getResource() instanceof Patient) {
                p = (Patient) entry.getResource();
            }
        }
        assertNotNull(p);
        assertEquals("3001", p.getManagingOrganization().getReference().getValue());
    }

    @Test(groups = { "server-search-reference" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchWithRelativePatientIdUsingTypeId() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient").queryParam("organization", "Organization/3001").request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);

        assertTrue(bundle.getEntry().size() >= 1);
        Patient p = null;
        for (Bundle.Entry entry : bundle.getEntry()) {
            Patient tmpP = (Patient) entry.getResource();

            if (entry.getResource() != null && entry.getResource() instanceof Patient
                    && tmpP.getManagingOrganization() != null
                    && tmpP.getManagingOrganization().getReference() != null
                    && tmpP.getManagingOrganization().getReference().getValue().equals("3001")) {
                p = tmpP;
            }
        }
        assertNotNull(p);
        assertEquals("3001", p.getManagingOrganization().getReference().getValue());
    }

    @Test(groups = { "server-search-reference" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchWithRelativePatientIdUsingUrl() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient").queryParam("organization", "https://localhost:9443/fhir-server/api/v4/Organization/3001").request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        Patient p = null;
        for (Bundle.Entry entry : bundle.getEntry()) {
            Patient tmpP = (Patient) entry.getResource();
            if (entry.getResource() != null && entry.getResource() instanceof Patient
                    && tmpP.getManagingOrganization() != null
                    && tmpP.getManagingOrganization().getReference() != null
                    && tmpP.getManagingOrganization().getReference().getValue().equals("3001")) {
                p = tmpP;
            }
        }
        assertNotNull(p);
        assertEquals("3001", p.getManagingOrganization().getReference().getValue());
    }

    @Test(groups = { "server-search-reference" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchWithRelativePatientTypeIdUsingId() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient").queryParam("organization", "3002").request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        Patient p = null;
        for (Bundle.Entry entry : bundle.getEntry()) {
            if (entry.getResource() != null && entry.getResource() instanceof Patient) {
                p = (Patient) entry.getResource();
            }
        }
        assertNotNull(p);
        assertEquals("Organization/3002", p.getManagingOrganization().getReference().getValue());
    }

    @Test(groups = { "server-search-reference" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchWithRelativePatientTypeIdUsingTypeId() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient").queryParam("organization", "Organization/3002").request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        Patient p = null;
        for (Bundle.Entry entry : bundle.getEntry()) {
            if (entry.getResource() != null && entry.getResource() instanceof Patient) {
                p = (Patient) entry.getResource();
            }
        }
        assertNotNull(p);
        assertEquals("Organization/3002", p.getManagingOrganization().getReference().getValue());
    }

    @Test(groups = { "server-search-reference" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchWithRelativePatientTypeIdUsingTypeUrl() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient").queryParam("organization", "https://localhost:9443/fhir-server/api/v4/Organization/3002").request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        Patient p = null;
        for (Bundle.Entry entry : bundle.getEntry()) {
            if (entry.getResource() != null && entry.getResource() instanceof Patient) {
                p = (Patient) entry.getResource();
            }
        }
        assertNotNull(p);
        assertEquals("Organization/3002", p.getManagingOrganization().getReference().getValue());
    }

    @Test(groups = { "server-search-reference" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchWithRelativePatientUrlUsingId() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient").queryParam("organization", "3003").request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        Patient p = null;
        for (Bundle.Entry entry : bundle.getEntry()) {
            if (entry.getResource() != null && entry.getResource() instanceof Patient) {
                p = (Patient) entry.getResource();
            }
        }
        assertNotNull(p);
        assertEquals("https://localhost:9443/fhir-server/api/v4/Organization/3003", p.getManagingOrganization().getReference().getValue());
    }

    @Test(groups = { "server-search-reference" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchWithRelativePatientUrlUsingTypeId() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient").queryParam("organization", "Organization/3003").request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        Patient p = null;
        for (Bundle.Entry entry : bundle.getEntry()) {
            if (entry.getResource() != null && entry.getResource() instanceof Patient) {
                p = (Patient) entry.getResource();
            }
        }
        assertNotNull(p);
        assertEquals("https://localhost:9443/fhir-server/api/v4/Organization/3003", p.getManagingOrganization().getReference().getValue());
    }

    @Test(groups = { "server-search-reference" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchWithRelativePatientUrlUsingUrl() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient").queryParam("organization", "https://localhost:9443/fhir-server/api/v4/Organization/3003").request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        Patient p = null;
        for (Bundle.Entry entry : bundle.getEntry()) {
            if (entry.getResource() != null && entry.getResource() instanceof Patient) {
                p = (Patient) entry.getResource();
            }
        }
        assertNotNull(p);
        assertEquals("https://localhost:9443/fhir-server/api/v4/Organization/3003", p.getManagingOrganization().getReference().getValue());
    }

    @Test(groups = { "server-search-reference" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchWithRelativePatientCanonicalUrl() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient").queryParam("organization", "https://localhost:9443/fhir-server/api/v4/Organization/3003|1.0.0").request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        Patient p = null;
        for (Bundle.Entry entry : bundle.getEntry()) {
            if (entry.getResource() != null && entry.getResource() instanceof Patient) {
                p = (Patient) entry.getResource();
            }
        }
        assertNotNull(p);
        assertEquals("https://localhost:9443/fhir-server/api/v4/Organization/3003", p.getManagingOrganization().getReference().getValue());
    }
}