/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.time.Instant;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Bundle.Entry;
import com.ibm.fhir.model.resource.Encounter;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Procedure;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.code.AdministrativeGender;
import com.ibm.fhir.model.type.code.IssueSeverity;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.model.type.code.ResourceType;

/**
 * The tests execute the chained behavior in order to exercise reference chains.
 */
public class SearchChainTest extends FHIRServerTestBase {
    private String patientId;
    private String procedureId;
    private String encounterId;

    @Test(groups = { "server-search-chain" })
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
        response = target.path("Patient/" + patientId).request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Patient responsePatient = response.readEntity(Patient.class);
        TestUtil.assertResourceEquals(patient, responsePatient);
    }

    @Test(groups = { "server-search-chain" }, dependsOnMethods = {"testCreatePatient"})
    public void testCreateProcedure() throws Exception {
        WebTarget target = getWebTarget();

        Reference reference = Reference.builder()
                .reference(com.ibm.fhir.model.type.String.of("Patient/" + patientId)).build();

        // Build a new Procedure and then call the 'create' API.
        Procedure procedure = TestUtil.readExampleResource("json/spec/procedure-example-physical-therapy.json");
        procedure = procedure.toBuilder().subject(reference).build();

        // Add Subject reference to the procedure
        Entity<Procedure> entity =
                Entity.entity(procedure, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Procedure").request()
                .post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the patient's logical id value.
        procedureId = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new patient and verify it.
        response = target.path("Procedure/" + procedureId).request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    @Test(groups = { "server-search-chain" }, dependsOnMethods = {"testCreatePatient"})
    public void testCreateEncounter() throws Exception {
        WebTarget target = getWebTarget();

        Reference reference = Reference.builder()
                .reference(com.ibm.fhir.model.type.String.of("Patient/" + patientId + "/_history/2")).build();

        // Build a new Procedure and then call the 'create' API.
        Encounter encounter = TestUtil.getMinimalResource(ResourceType.ENCOUNTER, Format.JSON);
        encounter = encounter.toBuilder().subject(reference).build();

        // Call the 'create' API.
        Entity<Encounter> entity = Entity.entity(encounter, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Encounter").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the encounter's logical id value.
        encounterId = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new encounter and verify it.
        response = target.path("Encounter/" + encounterId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    @AfterClass
    public void testDeleteResources() {
        WebTarget target = getWebTarget();
        if (patientId != null) {
            Response response   = target.path("Patient/" + patientId).request(FHIRMediaType.APPLICATION_FHIR_JSON).delete();
            assertResponse(response, Response.Status.OK.getStatusCode());
        }
        if (procedureId != null) {
            Response response   = target.path("Procedure/" + procedureId).request(FHIRMediaType.APPLICATION_FHIR_JSON).delete();
            assertResponse(response, Response.Status.OK.getStatusCode());
        }
        if (encounterId != null) {
            Response response   = target.path("Encounter/" + encounterId).request(FHIRMediaType.APPLICATION_FHIR_JSON).delete();
            assertResponse(response, Response.Status.OK.getStatusCode());
        }
    }

    @Test(groups = { "server-search-chain" }, dependsOnMethods = {"testCreatePatient" , "testCreateProcedure"})
    public void testSearchPatientWithId() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Procedure").queryParam("subject:Patient._id", patientId)
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search-chain" }, dependsOnMethods = {"testCreatePatient" , "testCreateProcedure"})
    public void testSearchPatientWithLastUpdated() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Procedure").queryParam("subject:Patient._lastUpdated", "gt" + Instant.now().minusSeconds(600).toString())
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search-chain" }, dependsOnMethods = {"testCreatePatient" , "testCreateProcedure"})
    public void testSearchPatientWithIdThatDoesntExist() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Procedure").queryParam("subject:Patient._id", patientId + "BAD")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() == 0);
    }

    @Test(groups = { "server-search-chain" }, dependsOnMethods = {"testCreatePatient" , "testCreateEncounter"})
    public void testVersionedReferenceSearchPatientWithId() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Encounter").queryParam("subject:Patient._id", patientId)
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 2);
        OperationOutcome oo = null;
        for (Entry bundleEntry : bundle.getEntry()) {
            if (bundleEntry.getResource().is(OperationOutcome.class)) {
                oo = bundleEntry.getResource().as(OperationOutcome.class);
            }
        }
        assertNotNull(oo);
        assertEquals(oo.getIssue().get(0).getSeverity(), IssueSeverity.WARNING);
        assertEquals(oo.getIssue().get(0).getCode(), IssueType.NOT_SUPPORTED);
        assertEquals(oo.getIssue().get(0).getDetails().getText().getValue(), "Resource with id '" + encounterId +
            "' contains a versioned reference in an element used for chained search, but chained search does not act on versioned references.");
        assertEquals(oo.getIssue().get(0).getExpression().get(0).getValue(), "Encounter.subject");
    }

    @Test(groups = { "server-search-chain" }, dependsOnMethods = {"testCreatePatient" , "testCreateProcedure"})
    public void testSearchProcedureForPatientWithActiveMissing() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Procedure")
                .queryParam("_id", procedureId)
                .queryParam("subject:Patient.active:missing", "true")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search-chain" }, dependsOnMethods = {"testCreatePatient" , "testCreateProcedure"})
    public void testSearchProcedureForPatientWithActiveNotMissing() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Procedure")
                .queryParam("_id", procedureId)
                .queryParam("subject:Patient.active:missing", "false")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertTrue(bundle.getEntry().isEmpty());
    }

    @Test(groups = { "server-search-chain" }, dependsOnMethods = {"testCreatePatient" , "testCreateProcedure"})
    public void testSearchProcedureForPatientWithGenderMissing() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Procedure")
                .queryParam("_id", procedureId)
                .queryParam("subject:Patient.gender:missing", "true")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertTrue(bundle.getEntry().isEmpty());
    }

    @Test(groups = { "server-search-chain" }, dependsOnMethods = {"testCreatePatient" , "testCreateProcedure"})
    public void testSearchProcedureForPatientWithGenderNotMissing() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Procedure")
                .queryParam("_id", procedureId)
                .queryParam("subject:Patient.gender:missing", "false")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

}