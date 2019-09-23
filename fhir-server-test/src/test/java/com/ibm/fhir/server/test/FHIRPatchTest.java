/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test;

import static com.ibm.fhir.model.type.String.string;
import static com.ibm.fhir.model.type.Xhtml.xhtml;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.json.Json;
import javax.json.JsonArray;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.HumanName;
import com.ibm.fhir.model.type.Id;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.NarrativeStatus;

public class FHIRPatchTest extends FHIRServerTestBase {    
    @Test(groups = { "fhir-patch" })
    public void testPatchAddOperation() throws Exception {
        WebTarget target = getWebTarget();
        
        // Build a new Patient and then call the 'create' API.
        Patient patient = buildPatient();

        Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Patient/" + patient.getId().getValue()).request().put(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());
        
        // Get the patient's logical id value.
        String patientId = getLocationLogicalId(response);

        // create a copy of the patient and update it using the model API
        Patient.Builder patientBuilder = patient.toBuilder();
        List<HumanName> name = new ArrayList<>(patient.getName());
        patientBuilder.name(Collections.singletonList(
            name.get(0).toBuilder()
                .given(string("Jack"))
                .build()));
        
        JsonArray array = Json.createPatchBuilder()
                .add("/name/0/given/1", "Jack")
                .build().toJsonArray();
        
        Entity<JsonArray> patchEntity = Entity.entity(array, "application/json-patch+json");
        response = target.path("Patient/" + patient.getId().getValue())
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .method("PATCH", patchEntity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());

        // Next, call the 'read' API to retrieve the new patient and verify it.
        response = target.path("Patient/" + patientId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Patient responsePatient = response.readEntity(Patient.class);
        
        patientBuilder.meta(responsePatient.getMeta());
        Patient updatedPatient = patientBuilder.build();

        Assert.assertEquals(updatedPatient, responsePatient);
    }
    
    @Test(groups = { "fhir-patch" })
    public void testPatchRemoveOperation() throws Exception {
        WebTarget target = getWebTarget();
        
        // Build a new Patient and then call the 'create' API.
        Patient patient = buildPatient();

        Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Patient/" + patient.getId().getValue()).request().put(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());
        
        // Get the patient's logical id value.
        String patientId = getLocationLogicalId(response);

        // create a copy of the patient and update it using the model API
        Patient.Builder patientBuilder = patient.toBuilder();
        patientBuilder.active(null);
        
        JsonArray array = Json.createPatchBuilder()
                .remove("/active")
                .build().toJsonArray();
        
        Entity<JsonArray> patchEntity = Entity.entity(array, FHIRMediaType.APPLICATION_JSON_PATCH);
        response = target.path("Patient/" + patient.getId().getValue())
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .method("PATCH", patchEntity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());

        // Next, call the 'read' API to retrieve the new patient and verify it.
        response = target.path("Patient/" + patientId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Patient responsePatient = response.readEntity(Patient.class);
        
        patientBuilder.meta(responsePatient.getMeta());
        Patient updatedPatient = patientBuilder.build();

        Assert.assertEquals(updatedPatient, responsePatient);
    }

    @Test(groups = { "fhir-patch" })
    public void testPatchReplaceOperation() throws Exception {
        WebTarget target = getWebTarget();
        
        // Build a new Patient and then call the 'create' API.
        Patient patient = buildPatient();

        Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Patient/" + patient.getId().getValue()).request().put(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());
        
        // Get the patient's logical id value.
        String patientId = getLocationLogicalId(response);

        // create a copy of the patient and update it using the model API
        Patient.Builder patientBuilder = patient.toBuilder();
        patientBuilder.active(Boolean.FALSE);
        
        JsonArray array = Json.createPatchBuilder()
                .replace("/active", false)
                .build().toJsonArray();
        
        Entity<JsonArray> patchEntity = Entity.entity(array, FHIRMediaType.APPLICATION_JSON_PATCH);
        response = target.path("Patient/" + patient.getId().getValue())
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .method("PATCH", patchEntity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());

        // Next, call the 'read' API to retrieve the new patient and verify it.
        response = target.path("Patient/" + patientId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Patient responsePatient = response.readEntity(Patient.class);
        
        patientBuilder.meta(responsePatient.getMeta());
        Patient updatedPatient = patientBuilder.build();

        Assert.assertEquals(updatedPatient, responsePatient);
    }

    @Test(groups = { "fhir-patch" })
    public void testPatchCopyOperation() throws Exception {
        WebTarget target = getWebTarget();
        
        // Build a new Patient and then call the 'create' API.
        Patient patient = buildPatient();

        Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Patient/" + patient.getId().getValue()).request().put(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());
        
        // Get the patient's logical id value.
        String patientId = getLocationLogicalId(response);

        // create a copy of the patient and update it using the model API
        Patient.Builder patientBuilder = patient.toBuilder();
        List<HumanName> name = new ArrayList<>(patient.getName());
        patientBuilder.name(Collections.singletonList(
            name.get(0).toBuilder()
                .family(patient.getName().get(0).getGiven().get(0))
                .build()));
        
        JsonArray array = Json.createPatchBuilder()
                .copy("/name/0/family", "/name/0/given/0")
                .build().toJsonArray();
        
        Entity<JsonArray> patchEntity = Entity.entity(array, FHIRMediaType.APPLICATION_JSON_PATCH);
        response = target.path("Patient/" + patient.getId().getValue())
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .method("PATCH", patchEntity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());

        // Next, call the 'read' API to retrieve the new patient and verify it.
        response = target.path("Patient/" + patientId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Patient responsePatient = response.readEntity(Patient.class);
        
        patientBuilder.meta(responsePatient.getMeta());
        Patient updatedPatient = patientBuilder.build();

        Assert.assertEquals(updatedPatient, responsePatient);
    }
    
    @Test(groups = { "fhir-patch" })
    public void testPatchMoveOperation() throws Exception {
        WebTarget target = getWebTarget();
        
        // Build a new Patient and then call the 'create' API.
        Patient patient = buildPatient();

        Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Patient/" + patient.getId().getValue()).request().put(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());
        
        // Get the patient's logical id value.
        String patientId = getLocationLogicalId(response);

        // create a copy of the patient and update it using the model API
        Patient.Builder patientBuilder = patient.toBuilder();
        List<HumanName> name = new ArrayList<>(patient.getName());
        patientBuilder.name(Collections.singletonList(
            name.get(0).toBuilder()
                .family(patient.getName().get(0).getGiven().get(0))
                .given(Collections.emptyList())
                .build()));
        
        JsonArray array = Json.createPatchBuilder()
                .move("/name/0/family", "/name/0/given/0")
                .build().toJsonArray();
        
        Entity<JsonArray> patchEntity = Entity.entity(array, FHIRMediaType.APPLICATION_JSON_PATCH);
        response = target.path("Patient/" + patient.getId().getValue())
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .method("PATCH", patchEntity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());

        // Next, call the 'read' API to retrieve the new patient and verify it.
        response = target.path("Patient/" + patientId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Patient responsePatient = response.readEntity(Patient.class);
        
        patientBuilder.meta(responsePatient.getMeta());
        Patient updatedPatient = patientBuilder.build();

        Assert.assertEquals(updatedPatient, responsePatient);
    }
    
    private Patient buildPatient() {
        java.lang.String div = "<div xmlns=\"http://www.w3.org/1999/xhtml\"><p><b>Generated Narrative</b></p></div>";
        
        Id id = Id.builder()
                .value(UUID.randomUUID().toString())
                .build();
        
        Meta meta = Meta.builder()
                .versionId(Id.of("1"))
                .lastUpdated(Instant.now(ZoneOffset.UTC))
                .build();
        
        HumanName name = HumanName.builder()
                .given(string("John"))
                .family(string("Doe"))
                .build();
        
        Narrative text = Narrative.builder()
                .status(NarrativeStatus.GENERATED)
                .div(xhtml(div))
                .build();
        
        return Patient.builder()
                .id(id)
                .meta(meta)
                .text(text)
                .active(Boolean.TRUE)
                .name(name)
                .birthDate(Date.of("1980-01-01"))
                .build();
    }
}
