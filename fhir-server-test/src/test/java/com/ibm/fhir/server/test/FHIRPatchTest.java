/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test;

import static com.ibm.fhir.model.type.String.string;
import static com.ibm.fhir.model.type.Xhtml.xhtml;
import static org.testng.Assert.assertNotNull;
import static org.testng.AssertJUnit.assertEquals;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.core.HTTPReturnPreference;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Bundle.Entry;
import com.ibm.fhir.model.resource.Bundle.Entry.Request;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.HumanName;
import com.ibm.fhir.model.type.Id;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.Integer;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BundleType;
import com.ibm.fhir.model.type.code.DataAbsentReason;
import com.ibm.fhir.model.type.code.HTTPVerb;
import com.ibm.fhir.model.type.code.NarrativeStatus;

import jakarta.json.Json;
import jakarta.json.JsonArray;

public class FHIRPatchTest extends FHIRServerTestBase {
    @Test(groups = { "fhir-patch" })
    public void testJSONPatchAddOperation() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient and then call the 'create' API.
        Patient patient = buildPatient();

        Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Patient/" + patient.getId()).request().put(entity, Response.class);
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

        Entity<JsonArray> patchEntity = Entity.entity(array, FHIRMediaType.APPLICATION_JSON_PATCH);
        response = target.path("Patient/" + patient.getId())
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
    public void testJSONPatchRemoveOperation() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient and then call the 'create' API.
        Patient patient = buildPatient();

        Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Patient/" + patient.getId()).request().put(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the patient's logical id value.
        String patientId = getLocationLogicalId(response);

        // create a copy of the patient and update it using the model API
        Patient.Builder patientBuilder = patient.toBuilder();
        patientBuilder.active((Boolean)null);

        JsonArray array = Json.createPatchBuilder()
                .remove("/active")
                .build().toJsonArray();

        Entity<JsonArray> patchEntity = Entity.entity(array, FHIRMediaType.APPLICATION_JSON_PATCH);
        response = target.path("Patient/" + patient.getId())
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
    public void testJSONPatchRemoveOperationOnAlreadyRemoved() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient and then call the 'create' API.
        Patient patient = buildPatient();

        Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Patient/" + patient.getId()).request().put(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // create a copy of the patient and update it using the model API
        Patient.Builder patientBuilder = patient.toBuilder();
        patientBuilder.active((Boolean)null);

        JsonArray array = Json.createPatchBuilder()
                .remove("/active")
                .build().toJsonArray();

        Entity<JsonArray> patchEntity = Entity.entity(array, FHIRMediaType.APPLICATION_JSON_PATCH);
        response = target.path("Patient/" + patient.getId())
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .method("PATCH", patchEntity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());

        // And again...
        patchEntity = Entity.entity(array, FHIRMediaType.APPLICATION_JSON_PATCH);
        response = target.path("Patient/" + patient.getId())
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .method("PATCH", patchEntity, Response.class);
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test(groups = { "fhir-patch" })
    public void testJSONPatchOperationDoesNotExist() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient and then call the 'create' API.
        Patient patient = buildPatient();

        Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Patient/" + patient.getId()).request().put(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // create a copy of the patient and update it using the model API
        Patient.Builder patientBuilder = patient.toBuilder();
        patientBuilder.active((Boolean)null);

        JsonArray array = Json.createArrayBuilder()
                .add(Json.createObjectBuilder()
                        .add("op", "delete")
                        .add("path", "/active")
                    .build())
                .build();

        Entity<JsonArray> patchEntity = Entity.entity(array, FHIRMediaType.APPLICATION_JSON_PATCH);
        response = target.path("Patient/" + patient.getId())
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .method("PATCH", patchEntity, Response.class);
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test(groups = { "fhir-patch" })
    public void testJSONPatchOperationReallyBadPath() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient and then call the 'create' API.
        Patient patient = buildPatient();

        Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Patient/" + patient.getId()).request().put(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // create a copy of the patient and update it using the model API
        Patient.Builder patientBuilder = patient.toBuilder();
        patientBuilder.active((Boolean)null);

        JsonArray array = Json.createArrayBuilder()
                .add(Json.createObjectBuilder()
                        .add("op", "remove")
                        .add("path", "/activeFudge67891234!/1235~###&?")
                    .build())
                .build();

        Entity<JsonArray> patchEntity = Entity.entity(array, FHIRMediaType.APPLICATION_JSON_PATCH);
        response = target.path("Patient/" + patient.getId())
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .method("PATCH", patchEntity, Response.class);
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test(groups = { "fhir-patch" })
    public void testJSONPatchOperationWithCopy() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient and then call the 'create' API.
        Patient patient = buildPatient();

        Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Patient/" + patient.getId()).request().put(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // create a copy of the patient and update it using the model API
        Patient.Builder patientBuilder = patient.toBuilder();
        patientBuilder.active((Boolean)null);

        // https://datatracker.ietf.org/doc/html/rfc6902#section-4.5
        // ‘[{“op”:“copy”,“from”:“/name/0/given/0",“path”:“/name/0/given/1"}]
        JsonArray array = Json.createArrayBuilder()
                .add(Json.createObjectBuilder()
                        .add("op", "copy")
                        .add("from", "/name/0/given/0")
                        .add("path", "/name/0/given/1")
                    .build())
                .build();

        Entity<JsonArray> patchEntity = Entity.entity(array, FHIRMediaType.APPLICATION_JSON_PATCH);
        response = target.path("Patient/" + patient.getId())
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .method("PATCH", patchEntity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    @Test(groups = { "fhir-patch" })
    public void testJSONPatchOperationWithCopyOutOfRangeOfNext() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient and then call the 'create' API.
        Patient patient = buildPatient();

        Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Patient/" + patient.getId()).request().put(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // create a copy of the patient and update it using the model API
        Patient.Builder patientBuilder = patient.toBuilder();
        patientBuilder.active((Boolean)null);

        // https://datatracker.ietf.org/doc/html/rfc6902#section-4.5
        // ‘[{“op”:“copy”,“from”:“/name/0/given/0",“path”:“/name/0/given/1"}]
        // Note there is only an array of size 1 (the value is only 2)
        JsonArray array = Json.createArrayBuilder()
                .add(Json.createObjectBuilder()
                        .add("op", "copy")
                        .add("from", "/name/0/given/0")
                        .add("path", "/name/0/given/3")
                    .build())
                .build();

        Entity<JsonArray> patchEntity = Entity.entity(array, FHIRMediaType.APPLICATION_JSON_PATCH);
        response = target.path("Patient/" + patient.getId())
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .method("PATCH", patchEntity, Response.class);
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test(groups = { "fhir-patch" })
    public void testJSONPatchOperationWithMove() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient and then call the 'create' API.
        Patient patient = buildPatient();
        patient = patient.toBuilder()
                .name(HumanName.builder()
                        .given(string("One"), string("Two"), string("Three"))
                        .family(string("Doe"))
                    .build())
                .build();

        Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Patient/" + patient.getId()).request().put(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // create a copy of the patient and update it using the model API
        Patient.Builder patientBuilder = patient.toBuilder();
        patientBuilder.active((Boolean)null);

        // https://datatracker.ietf.org/doc/html/rfc6902#section-4.4
        // [{“op”:“move”,“from”:“/name/0/given/0",“path”:“/name/0/given/1"}]
        JsonArray array = Json.createArrayBuilder()
                .add(Json.createObjectBuilder()
                        .add("op", "move")
                        .add("from", "/name/1/given/0")
                        .add("path", "/name/1/given/2")
                    .build())
                .build();

        Entity<JsonArray> patchEntity = Entity.entity(array, FHIRMediaType.APPLICATION_JSON_PATCH);
        response = target.path("Patient/" + patient.getId())
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .method("PATCH", patchEntity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());

        // Next, call the 'read' API to retrieve the new patient and verify it.
        response = target.path("Patient/" + patient.getId()).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Patient responsePatient = response.readEntity(Patient.class);
        assertEquals("Two,Three,One",
            responsePatient.getName().get(1).getGiven().stream().map(m -> m.getValue()).collect(Collectors.joining(",")));
    }

    @Test(groups = { "fhir-patch" })
    public void testJSONPatchOperationWithMoveBadLocation() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient and then call the 'create' API.
        Patient patient = buildPatient();
        patient = patient.toBuilder()
                .name(HumanName.builder()
                        .given(string("Fred"), string("John"), string("Joe"))
                        .family(string("Doe"))
                    .build())
                .build();

        Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Patient/" + patient.getId()).request().put(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // create a copy of the patient and update it using the model API
        Patient.Builder patientBuilder = patient.toBuilder();
        patientBuilder.active((Boolean)null);

        // https://datatracker.ietf.org/doc/html/rfc6902#section-4.4
        // [{“op”:“move”,“from”:“/name/0/given/0",“path”:“/name/0/given/4"}]
        JsonArray array = Json.createArrayBuilder()
                .add(Json.createObjectBuilder()
                        .add("op", "move")
                        .add("from", "/name/1/given/0")
                        .add("path", "/name/1/given/4")
                    .build())
                .build();

        Entity<JsonArray> patchEntity = Entity.entity(array, FHIRMediaType.APPLICATION_JSON_PATCH);
        response = target.path("Patient/" + patient.getId())
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .method("PATCH", patchEntity, Response.class);
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test(groups = { "fhir-patch" })
    public void testJSONPatchOperationWithTestDoesNotCreateNewVersionOfResource() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient and then call the 'create' API.
        Patient patient = buildPatient();

        Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Patient/" + patient.getId()).request().put(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // create a copy of the patient and update it using the model API
        Patient.Builder patientBuilder = patient.toBuilder();
        patientBuilder.active((Boolean)null);

        // https://datatracker.ietf.org/doc/html/rfc6902#section-4.6
        // [{"op":"test","path":"/name/0/given/0","value":"Jack"}]
        JsonArray array = Json.createArrayBuilder()
                .add(Json.createObjectBuilder()
                        .add("op", "test")
                        .add("path", "/name/0/given/0")
                        .add("value", "John")
                    .build())
                .build();

        Entity<JsonArray> patchEntity = Entity.entity(array, FHIRMediaType.APPLICATION_JSON_PATCH);
        response = target.path("Patient/" + patient.getId())
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .method("PATCH", patchEntity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());

        // Next, call the 'read' API to retrieve the new patient and verify it.
        response = target.path("Patient/" + patient.getId()).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Patient responsePatient = response.readEntity(Patient.class);
        assertEquals("1", responsePatient.getMeta().getVersionId().getValue());
    }

    @Test(groups = { "fhir-patch" })
    public void testJSONPatchOperationWithTestDoesNotCreateNewVersionOfResourceWithBadValue() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient and then call the 'create' API.
        Patient patient = buildPatient();

        Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Patient/" + patient.getId()).request().put(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // create a copy of the patient and update it using the model API
        Patient.Builder patientBuilder = patient.toBuilder();
        patientBuilder.active((Boolean)null);

        // https://datatracker.ietf.org/doc/html/rfc6902#section-4.6
        // [{"op":"test","path":"/name/0/given/0","value":"Jack"}]
        JsonArray array = Json.createArrayBuilder()
                .add(Json.createObjectBuilder()
                        .add("op", "test")
                        .add("path", "/name/0/given/0")
                        .add("value", "Jack")
                    .build())
                .build();

        Entity<JsonArray> patchEntity = Entity.entity(array, FHIRMediaType.APPLICATION_JSON_PATCH);
        response = target.path("Patient/" + patient.getId())
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .method("PATCH", patchEntity, Response.class);
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test(groups = { "fhir-patch" })
    public void testJSONPatchReplaceOperation() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient and then call the 'create' API.
        Patient patient = buildPatient();

        Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Patient/" + patient.getId()).request().put(entity, Response.class);
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
        response = target.path("Patient/" + patient.getId())
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
    public void testJSONPatchCopyOperation() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient and then call the 'create' API.
        Patient patient = buildPatient();

        Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Patient/" + patient.getId()).request().put(entity, Response.class);
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
        response = target.path("Patient/" + patient.getId())
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
    public void testJSONPatchMoveOperation() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient and then call the 'create' API.
        Patient patient = buildPatient();

        Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Patient/" + patient.getId()).request().put(entity, Response.class);
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
        response = target.path("Patient/" + patient.getId())
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
    public void testFhirPatchAddOperation() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient and then call the 'create' API.
        Patient patient = buildPatient();

        Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Patient/" + patient.getId()).request().put(entity, Response.class);
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

        Parameters patch = Parameters.builder()
                .parameter(Parameter.builder()
                    .name(string("operation"))
                    .part(Parameter.builder()
                        .name(string("type"))
                        .value(Code.of("add"))
                        .build())
                    .part(Parameter.builder()
                        .name(string("path"))
                        .value(string("Patient.name[0]"))
                        .build())
                    .part(Parameter.builder()
                        .name(string("name"))
                        .value(string("given"))
                        .build())
                    .part(Parameter.builder()
                        .name(string("value"))
                        .value(string("Jack"))
                        .build())
                    .build())
                .build();

        Entity<Parameters> patchEntity = Entity.entity(patch, FHIRMediaType.APPLICATION_FHIR_JSON);
        response = target.path("Patient/" + patient.getId())
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
    public void testFhirPatchInsertOperation() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient and then call the 'create' API.
        Patient patient = buildPatient();

        Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Patient/" + patient.getId()).request().put(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the patient's logical id value.
        String patientId = getLocationLogicalId(response);

        // create a copy of the patient and update it using the model API
        Patient.Builder patientBuilder = patient.toBuilder();
        List<HumanName> name = new ArrayList<>(patient.getName());
        patientBuilder.name(Collections.singletonList(
            name.get(0).toBuilder()
                .given(patient.getName().get(0).getFamily())
                .build()));

        Parameters patch = Parameters.builder()
                .parameter(Parameter.builder()
                    .name(string("operation"))
                    .part(Parameter.builder()
                        .name(string("type"))
                        .value(Code.of("insert"))
                        .build())
                    .part(Parameter.builder()
                        .name(string("path"))
                        .value(string("Patient.name[0].given"))
                        .build())
                    .part(Parameter.builder()
                        .name(string("value"))
                        .value(patient.getName().get(0).getFamily())
                        .build())
                    .part(Parameter.builder()
                        .name(string("index"))
                        .value(Integer.of(1))
                        .build())
                    .build())
                .build();

        Entity<Parameters> patchEntity = Entity.entity(patch, FHIRMediaType.APPLICATION_FHIR_JSON);
        response = target.path("Patient/" + patient.getId())
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
    public void testFhirPatchDeleteOperation() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient and then call the 'create' API.
        Patient patient = buildPatient();

        Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Patient/" + patient.getId()).request().put(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the patient's logical id value.
        String patientId = getLocationLogicalId(response);

        // create a copy of the patient and update it using the model API
        Patient.Builder patientBuilder = patient.toBuilder();
        patientBuilder.active((Boolean)null);

        Parameters patch = Parameters.builder()
                .parameter(Parameter.builder()
                    .name(string("operation"))
                    .part(Parameter.builder()
                        .name(string("type"))
                        .value(Code.of("delete"))
                        .build())
                    .part(Parameter.builder()
                        .name(string("path"))
                        .value(string("Patient.active"))
                        .build())
                    .build())
                .build();

        Entity<Parameters> patchEntity = Entity.entity(patch, FHIRMediaType.APPLICATION_FHIR_JSON);
        response = target.path("Patient/" + patient.getId())
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
    public void testFhirPatchReplaceOperation() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient and then call the 'create' API.
        Patient patient = buildPatient();

        Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Patient/" + patient.getId()).request().put(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the patient's logical id value.
        String patientId = getLocationLogicalId(response);

        // create a copy of the patient and update it using the model API
        Patient.Builder patientBuilder = patient.toBuilder();
        patientBuilder.active(Boolean.FALSE);

        Parameters patch = Parameters.builder()
                .parameter(Parameter.builder()
                    .name(string("operation"))
                    .part(Parameter.builder()
                        .name(string("type"))
                        .value(Code.of("replace"))
                        .build())
                    .part(Parameter.builder()
                        .name(string("path"))
                        .value(string("Patient.active"))
                        .build())
                    .part(Parameter.builder()
                        .name(string("value"))
                        .value(com.ibm.fhir.model.type.Boolean.FALSE)
                        .build())
                    .build())
                .build();

        Entity<Parameters> patchEntity = Entity.entity(patch, FHIRMediaType.APPLICATION_FHIR_JSON);
        response = target.path("Patient/" + patient.getId())
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
    public void testFhirPatchMoveOperation() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient and then call the 'create' API.
        Patient patient = buildPatient();
        // Add a second given name so we have something to move
        patient = patient.toBuilder()
                .name(Collections.singletonList(patient.getName().get(0).toBuilder()
                        .given(string("Jack"))
                        .build()))
                .build();

        Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Patient/" + patient.getId()).request().put(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the patient's logical id value.
        String patientId = getLocationLogicalId(response);

        // create a copy of the patient and update it using the model API
        Patient.Builder patientBuilder = patient.toBuilder();
        List<HumanName> name = new ArrayList<>(patient.getName());
        patientBuilder.name(Collections.singletonList(
            name.get(0).toBuilder()
                .given(Arrays.asList(patient.getName().get(0).getGiven().get(1),
                                     patient.getName().get(0).getGiven().get(0)))
                .build()));

        Parameters patch = Parameters.builder()
                .parameter(Parameter.builder()
                    .name(string("operation"))
                    .part(Parameter.builder()
                        .name(string("type"))
                        .value(Code.of("move"))
                        .build())
                    .part(Parameter.builder()
                        .name(string("path"))
                        .value(string("Patient.name[0].given"))
                        .build())
                    .part(Parameter.builder()
                        .name(string("source"))
                        .value(Integer.of(1))
                        .build())
                    .part(Parameter.builder()
                        .name(string("destination"))
                        .value(Integer.of(0))
                        .build())
                    .build())
                .build();

        Entity<Parameters> patchEntity = Entity.entity(patch, FHIRMediaType.APPLICATION_FHIR_JSON);
        response = target.path("Patient/" + patient.getId())
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
    public void testBundlePatch() throws Exception {

        WebTarget target = getWebTarget();
        // Build a new Patient and then call the 'create' API.
        Patient patient = buildPatient();

        Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Patient/" + patient.getId()).request().put(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the patient's logical id value.
        String patientId = getLocationLogicalId(response);

        //replace patch request for the patient
        Parameters replacePatch = Parameters.builder()
                .parameter(Parameter.builder()
                    .name(string("operation"))
                    .part(Parameter.builder()
                        .name(string("type"))
                        .value(Code.of("replace"))
                        .build())
                    .part(Parameter.builder()
                        .name(string("path"))
                        .value(string("Patient.active"))
                        .build())
                    .part(Parameter.builder()
                        .name(string("value"))
                        .value(com.ibm.fhir.model.type.Boolean.FALSE)
                        .build())
                    .build())
                .build();

        //Delete Patch request for the patient
        Parameters deletePatch = Parameters.builder()
                .parameter(Parameter.builder()
                    .name(string("operation"))
                    .part(Parameter.builder()
                        .name(string("type"))
                        .value(Code.of("delete"))
                        .build())
                    .part(Parameter.builder()
                        .name(string("path"))
                        .value(string("Patient.active"))
                        .build())
                    .build())
                .build();

        //Creating bundle containing multiple patch request for the patient
        Bundle.Builder patchBundleBuilder=Bundle.builder();
        String patientUrl="Patient/" + patientId;

        //Patch request object for the bundle
        Request req = Request.builder().method(HTTPVerb.PATCH).url(Uri.of(patientUrl)).build();
        Entry bundleReplaceReq = Entry.builder().resource(replacePatch).request(req).build();
        Entry bundleDeletePatch = Entry.builder().resource(deletePatch).request(req).build();
        Bundle patchRequestBundle = patchBundleBuilder.entry(bundleReplaceReq,bundleDeletePatch).type(BundleType.BATCH).build();

        Entity<Bundle> bundleEntity = Entity.entity(patchRequestBundle, FHIRMediaType.APPLICATION_FHIR_JSON);

        //Call FHRI-Api for the Post operation
        Response patchResponse = target.request().post(bundleEntity,Response.class);
        Bundle responseBundle = patchResponse .readEntity(Bundle.class);

        assertResponseBundle(responseBundle, BundleType.BATCH_RESPONSE, 2);
        assertGoodGetResponse(responseBundle.getEntry().get(0), Status.OK.getStatusCode(),HTTPReturnPreference.MINIMAL);
    }

    @Test(groups = { "fhir-patch" })
    public void testBundleWithBadPatches() throws Exception {

        WebTarget target = getWebTarget();
        // Build a new Patient and then call the 'create' API.
        Patient patient = buildPatient();

        Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Patient/" + patient.getId()).request().put(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the patient's logical id value.
        String patientId = getLocationLogicalId(response);

        //replace patch request for the patient
        Parameters replacePatch = Parameters.builder()
                .parameter(Parameter.builder()
                    .name(string("operation"))
                    .part(Parameter.builder()
                        .name(string("type"))
                        .value(Code.of("replace"))
                        .build())
                    .part(Parameter.builder()
                        .name(string("path"))
                        .value(string("Patient.active"))
                        .build())
                    .part(Parameter.builder()
                        .name(string("value"))
                        .value(DataAbsentReason.NOT_APPLICABLE)
                        .build())
                    .build())
                .build();

        //Delete Patch request for the patient
        Parameters deletePatch = Parameters.builder()
                .parameter(Parameter.builder()
                    .name(string("operation"))
                    .part(Parameter.builder()
                        .name(string("type"))
                        .value(Code.of("delete"))
                        .build())
                    .part(Parameter.builder()
                        .name(string("path"))
                        .value(string("Patient.invalid"))
                        .build())
                    .build())
                .build();

        //Creating bundle containing multiple patch request for the patient
        Bundle.Builder patchBundleBuilder=Bundle.builder();
        String patientUrl="Patient/" + patientId;

        //Patch request object for the bundle
        Request req = Request.builder().method(HTTPVerb.PATCH).url(Uri.of(patientUrl)).build();
        Entry bundleReplaceReq = Entry.builder().resource(replacePatch).request(req).build();
        Entry bundleDeletePatch = Entry.builder().resource(deletePatch).request(req).build();
        Bundle patchRequestBundle = patchBundleBuilder.entry(bundleReplaceReq,bundleDeletePatch).type(BundleType.BATCH).build();

        Entity<Bundle> bundleEntity = Entity.entity(patchRequestBundle, FHIRMediaType.APPLICATION_FHIR_JSON);

        //Call FHRI-Api for the Post operation
        Response patchResponse = target.request().post(bundleEntity,Response.class);
        Bundle responseBundle = patchResponse .readEntity(Bundle.class);

        assertResponseBundle(responseBundle, BundleType.BATCH_RESPONSE, 2);

        assertGoodGetResponse(responseBundle.getEntry().get(0), Status.BAD_REQUEST.getStatusCode(), HTTPReturnPreference.OPERATION_OUTCOME);
        assertGoodGetResponse(responseBundle.getEntry().get(1), Status.BAD_REQUEST.getStatusCode(), HTTPReturnPreference.OPERATION_OUTCOME);
    }

    @Test(groups = { "fhir-patch" })
    public void testPatchNotExistingResource() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient, but do not call the 'create' API.
        Patient patient = buildPatient();

        Parameters patch = Parameters.builder()
                .parameter(Parameter.builder()
                    .name(string("operation"))
                    .part(Parameter.builder()
                        .name(string("type"))
                        .value(Code.of("add"))
                        .build())
                    .part(Parameter.builder()
                        .name(string("path"))
                        .value(string("Patient.name[0]"))
                        .build())
                    .part(Parameter.builder()
                        .name(string("name"))
                        .value(string("given"))
                        .build())
                    .part(Parameter.builder()
                        .name(string("value"))
                        .value(string("Jack"))
                        .build())
                    .build())
                .build();

        // Call the 'patch` API
        Entity<Parameters> patchEntity = Entity.entity(patch, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Patient/" + patient.getId())
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .method("PATCH", patchEntity, Response.class);
        assertResponse(response, Response.Status.NOT_FOUND.getStatusCode());
        OperationOutcome oo = response.readEntity(OperationOutcome.class);
        assertNotNull(oo);
        assertEquals("Resource 'Patient/" + patient.getId() + "' not found.", oo.getIssue().get(0).getDetails().getText().getValue());
    }

    @Test(groups = { "fhir-patch" })
    public void testJSONPatchNotExistingResource() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient, but do not call the 'create' API.
        Patient patient = buildPatient();

        JsonArray array = Json.createPatchBuilder()
                .add("/name/0/given/1", "Jack")
                .build().toJsonArray();

        // Call the 'patch` API
        Entity<JsonArray> patchEntity = Entity.entity(array, FHIRMediaType.APPLICATION_JSON_PATCH);
        Response response = target.path("Patient/" + patient.getId())
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .method("PATCH", patchEntity, Response.class);
        assertResponse(response, Response.Status.NOT_FOUND.getStatusCode());
        OperationOutcome oo = response.readEntity(OperationOutcome.class);
        assertNotNull(oo);
        assertEquals("Resource 'Patient/" + patient.getId() + "' not found.", oo.getIssue().get(0).getDetails().getText().getValue());
    }

    @Test(groups = { "fhir-patch" })
    public void testPatchDeletedResource() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient and then call the 'create' API.
        Patient patient = buildPatient();

        Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Patient/" + patient.getId()).request().put(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the patient's logical id value.
        String patientId = getLocationLogicalId(response);

        // Next, call the 'delete' API to delete the new patient.
        response = target.path("Patient/" + patientId).request(FHIRMediaType.APPLICATION_FHIR_JSON).delete();
        assertResponse(response, Response.Status.OK.getStatusCode());

        Parameters patch = Parameters.builder()
                .parameter(Parameter.builder()
                    .name(string("operation"))
                    .part(Parameter.builder()
                        .name(string("type"))
                        .value(Code.of("add"))
                        .build())
                    .part(Parameter.builder()
                        .name(string("path"))
                        .value(string("Patient.name[0]"))
                        .build())
                    .part(Parameter.builder()
                        .name(string("name"))
                        .value(string("given"))
                        .build())
                    .part(Parameter.builder()
                        .name(string("value"))
                        .value(string("Jack"))
                        .build())
                    .build())
                .build();

        // Call the 'patch` API
        Entity<Parameters> patchEntity = Entity.entity(patch, FHIRMediaType.APPLICATION_FHIR_JSON);
        response = target.path("Patient/" + patient.getId())
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .method("PATCH", patchEntity, Response.class);
        assertResponse(response, Response.Status.NOT_FOUND.getStatusCode());
        OperationOutcome oo = response.readEntity(OperationOutcome.class);
        assertNotNull(oo);
        assertEquals("Resource 'Patient/" + patient.getId() + "' not found.", oo.getIssue().get(0).getDetails().getText().getValue());
    }

    @Test(groups = { "fhir-patch" })
    public void testJSONPatchDeletedResource() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient and then call the 'create' API.
        Patient patient = buildPatient();

        Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Patient/" + patient.getId()).request().put(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the patient's logical id value.
        String patientId = getLocationLogicalId(response);

        // Next, call the 'delete' API to delete the new patient.
        response = target.path("Patient/" + patientId).request(FHIRMediaType.APPLICATION_FHIR_JSON).delete();
        assertResponse(response, Response.Status.OK.getStatusCode());

        JsonArray array = Json.createPatchBuilder()
                .add("/name/0/given/1", "Jack")
                .build().toJsonArray();

        // Call the 'patch` API
        Entity<JsonArray> patchEntity = Entity.entity(array, FHIRMediaType.APPLICATION_JSON_PATCH);
        response = target.path("Patient/" + patient.getId())
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .method("PATCH", patchEntity, Response.class);
        assertResponse(response, Response.Status.NOT_FOUND.getStatusCode());
        OperationOutcome oo = response.readEntity(OperationOutcome.class);
        assertNotNull(oo);
        assertEquals("Resource 'Patient/" + patient.getId() + "' not found.", oo.getIssue().get(0).getDetails().getText().getValue());
    }

    private void assertGoodGetResponse(Bundle.Entry entry, int expectedStatusCode, HTTPReturnPreference returnPref) throws Exception {
        assertNotNull(entry);
        Bundle.Entry.Response response = entry.getResponse();
        assertNotNull(response);

        assertNotNull(response.getStatus());
        assertEquals(java.lang.Integer.toString(expectedStatusCode), response.getStatus().getValue());

        if (returnPref != null && !returnPref.equals(HTTPReturnPreference.MINIMAL)) {
            Resource rc = entry.getResource();
            assertNotNull(rc);
        }
    }
    private Patient buildPatient() {
        java.lang.String div = "<div xmlns=\"http://www.w3.org/1999/xhtml\"><p><b>Generated Narrative</b></p></div>";

        java.lang.String id = UUID.randomUUID().toString();

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
