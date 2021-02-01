/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test;

import static org.testng.Assert.assertNotNull;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.code.AdministrativeGender;

public class ReadTest extends FHIRServerTestBase {
    private String patientId;

    @Test(groups = { "server-read" })
    public void testCreatePatient() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient and then call the 'create' API.
        Patient patient = TestUtil.readLocalResource("Patient_JohnDoe.json");

        patient = patient.toBuilder().gender(AdministrativeGender.MALE).build();
        Entity<Patient> entity =
                Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response =
                target.path("Patient").request()
                .post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the patient's logical id value.
        patientId = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new patient and verify it.
        response = target.path("Patient/"
                + patientId).request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Patient responsePatient = response.readEntity(Patient.class);
        TestUtil.assertResourceEquals(patient, responsePatient);
    }

    @Test(groups = { "server-read" }, dependsOnMethods = {"testCreatePatient" })
    public void testReadPatient_elements() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient/" + patientId)
                .queryParam("_elements", "active")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Patient patient = response.readEntity(Patient.class);
        assertNotNull(patient);
    }

    @Test(groups = { "server-read" }, dependsOnMethods = {"testCreatePatient" })
    public void testReadPatient_summary() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient/" + patientId)
                .queryParam("_summary", "true")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Patient patient = response.readEntity(Patient.class);
        assertNotNull(patient);
    }

    @Test(groups = { "server-read" }, dependsOnMethods = {"testCreatePatient" })
    public void testReadPatient_nonGeneralSearchParameter() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient/" + patientId)
                .queryParam("_lastUpdated", "ge2020-11-02")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
        assertExceptionOperationOutcome(response.readEntity(OperationOutcome.class),
            "Read only supports general search parameters.");
    }

}
