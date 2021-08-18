/*
 * (C) Copyright IBM Corp. 2017, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test;

import static com.ibm.fhir.model.type.String.string;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

import java.io.StringWriter;
import java.util.Collections;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.resource.Observation;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.IssueSeverity;

import jakarta.json.Json;
import jakarta.json.JsonBuilderFactory;
import jakarta.json.JsonObject;

public class FHIRValidateOperationTest extends FHIRServerTestBase {
    private static final JsonBuilderFactory BUILDER_FACTORY = Json.createBuilderFactory(null);
    @Test(groups = { "validate-operation" })
    public void testValidatePatient() {
        JsonObject patient = buildPatient();
        Entity<JsonObject> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_JSON);

        WebTarget target = getWebTarget();
        Response response = target.path("Patient/$validate").request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        OperationOutcome operationOutcome = response.readEntity(OperationOutcome.class);

        assertEquals("NoError", operationOutcome.getId());
        assertEquals(1, operationOutcome.getIssue().size());
        assertEquals(IssueSeverity.WARNING, operationOutcome.getIssue().get(0).getSeverity());
        assertTrue(operationOutcome.getIssue().get(0).getDetails().getText().getValue().startsWith("dom-6"));
    }

    @Test(groups = { "validate-operation" })
    public void testValidateInvalidPatient() {
        JsonObject patient = buildInvalidPatient();
        Entity<JsonObject> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_JSON);

        WebTarget target = getWebTarget();
        Response response = target.path("Patient/$validate").request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        OperationOutcome operationOutcome = response.readEntity(OperationOutcome.class);

        assertEquals("Error", operationOutcome.getId());
        assertEquals(2, operationOutcome.getIssue().size());

        assertEquals(IssueSeverity.ERROR, operationOutcome.getIssue().get(0).getSeverity());
        assertTrue(operationOutcome.getIssue().get(0).getDetails().getText().getValue().startsWith("cpt-2"));

        assertEquals(IssueSeverity.WARNING, operationOutcome.getIssue().get(1).getSeverity());
        assertTrue(operationOutcome.getIssue().get(1).getDetails().getText().getValue().startsWith("dom-6"));
    }

    @Test(groups = { "validate-operation" })
    public void testValidateValidObsProfile() throws Exception {
        Observation obs = TestUtil.readExampleResource("json/spec/observation-example-respiratory-rate.json");
        // Clear the profile from the resource's meta
        obs = obs.toBuilder()
                .meta(obs.getMeta().toBuilder().profile(Collections.emptySet()).build())
                .build();
        Parameters parameters = Parameters.builder()
                .parameter(Parameters.Parameter.builder()
                    .name(string("resource"))
                    .resource(obs)
                    .build())
                .parameter(Parameters.Parameter.builder()
                    .name(string("profile"))
                    .value(Uri.of("http://hl7.org/fhir/StructureDefinition/vitalsigns"))
                    .build())
                .build();

        StringWriter writer = new StringWriter();
        FHIRGenerator.generator(Format.JSON).generate(parameters, writer);

        WebTarget target = getWebTarget();
        Response response = target.path("Observation/$validate").request().post(Entity.json(writer.toString()), Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        OperationOutcome operationOutcome = response.readEntity(OperationOutcome.class);

        assertEquals("NoError", operationOutcome.getId());
        assertEquals(1, operationOutcome.getIssue().size());
    }

    @Test(groups = { "validate-operation" })
    public void testValidateValidRespRate() throws Exception {
        Observation obs = TestUtil.readExampleResource("json/spec/observation-example-respiratory-rate.json");
        // Clear the profile from the resource's meta
        obs = obs.toBuilder()
                .meta(obs.getMeta().toBuilder().profile(Collections.emptySet()).build())
                .build();
        Parameters parameters = Parameters.builder()
                .parameter(Parameters.Parameter.builder()
                    .name(string("resource"))
                    .resource(obs)
                    .build())
                .parameter(Parameters.Parameter.builder()
                    .name(string("profile"))
                    .value(Uri.of("http://hl7.org/fhir/StructureDefinition/resprate"))
                    .build())
                .build();

        StringWriter writer = new StringWriter();
        FHIRGenerator.generator(Format.JSON).generate(parameters, writer);

        WebTarget target = getWebTarget();
        Response response = target.path("Observation/$validate").request().post(Entity.json(writer.toString()), Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        OperationOutcome operationOutcome = response.readEntity(OperationOutcome.class);

        assertEquals("NoError", operationOutcome.getId());
        assertEquals(1, operationOutcome.getIssue().size());
    }

    @Test(groups = { "validate-operation" })
    public void testValidateInvalidObsProfile() throws Exception {
        Observation obs = TestUtil.readExampleResource("json/spec/observation-example-respiratory-rate.json");
        // Clear the profile from the resource's meta
        obs = obs.toBuilder()
                .meta(obs.getMeta().toBuilder().profile(Collections.emptySet()).build())
                .build();
        Parameters parameters = Parameters.builder()
                .parameter(Parameters.Parameter.builder()
                    .name(string("resource"))
                    .resource(obs)
                    .build())
                .parameter(Parameters.Parameter.builder()
                    .name(string("profile"))
                    .value(Uri.of("http://hl7.org/fhir/StructureDefinition/heartrate"))
                    .build())
                .build();

        StringWriter writer = new StringWriter();
        FHIRGenerator.generator(Format.JSON).generate(parameters, writer);

        WebTarget target = getWebTarget();
        Response response = target.path("Observation/$validate").request().post(Entity.json(writer.toString()), Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        OperationOutcome operationOutcome = response.readEntity(OperationOutcome.class);

        assertEquals("Error", operationOutcome.getId());
        assertEquals(1, operationOutcome.getIssue().size());
        assertEquals(IssueSeverity.ERROR, operationOutcome.getIssue().get(0).getSeverity());
        assertTrue(operationOutcome.getIssue().get(0).getDetails().getText().getValue().startsWith("generated-heartrate"));
    }

    @Test(groups = { "validate-operation" })
    public void testValidateBadPath() throws Exception {
        Observation obs = TestUtil.readExampleResource("json/spec/observation-example-respiratory-rate.json");
        // Clear the profile from the resource's meta
        obs = obs.toBuilder()
                .meta(obs.getMeta().toBuilder().profile(Collections.emptySet()).build())
                .build();
        Parameters parameters = Parameters.builder()
                .parameter(Parameters.Parameter.builder()
                    .name(string("resource"))
                    .resource(obs)
                    .build())
                .parameter(Parameters.Parameter.builder()
                    .name(string("profile"))
                    .value(Uri.of("http://hl7.org/fhir/StructureDefinition/heartrate"))
                    .build())
                .build();

        StringWriter writer = new StringWriter();
        FHIRGenerator.generator(Format.JSON).generate(parameters, writer);

        WebTarget target = getWebTarget();
        Response response = target.path("Fudge/$validate").request().post(Entity.json(writer.toString()), Response.class);
        assertResponse(response, Response.Status.NOT_FOUND.getStatusCode());
    }

    private JsonObject buildPatient() {
        return BUILDER_FACTORY.createObjectBuilder().add("resourceType", "Patient")
            .add("name", BUILDER_FACTORY.createArrayBuilder()
                .add(BUILDER_FACTORY.createObjectBuilder()
                    .add("family", "Doe")
                    .add("given", BUILDER_FACTORY.createArrayBuilder()
                        .add("John"))))
            .add("birthDate", "1950-08-15")
            .add("telecom", BUILDER_FACTORY.createArrayBuilder()
                .add(BUILDER_FACTORY.createObjectBuilder()
                    .add("use", "home")
                    .add("system", "phone")
                    .add("value", "555-1234")))
            .build();
    }

    private JsonObject buildInvalidPatient() {
        return BUILDER_FACTORY.createObjectBuilder().add("resourceType", "Patient")
            .add("name", BUILDER_FACTORY.createArrayBuilder()
                .add(BUILDER_FACTORY.createObjectBuilder()
                    .add("family", "Doe")
                    .add("given", BUILDER_FACTORY.createArrayBuilder()
                        .add("John"))))
            .add("birthDate", "1950-08-15")
            .add("telecom", BUILDER_FACTORY.createArrayBuilder()
                .add(BUILDER_FACTORY.createObjectBuilder()
                    .add("use", "home")
                    .add("value", "555-1234")))
            .build();
    }
}
