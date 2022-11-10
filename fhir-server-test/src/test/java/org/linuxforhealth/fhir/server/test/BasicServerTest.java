/*
 * (C) Copyright IBM Corp. 2017, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.server.test;

import static org.linuxforhealth.fhir.model.type.String.string;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;
import static org.testng.AssertJUnit.fail;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.linuxforhealth.fhir.core.FHIRMediaType;
import org.linuxforhealth.fhir.model.format.Format;
import org.linuxforhealth.fhir.model.generator.FHIRGenerator;
import org.linuxforhealth.fhir.model.resource.Bundle;
import org.linuxforhealth.fhir.model.resource.Bundle.Entry;
import org.linuxforhealth.fhir.model.resource.CapabilityStatement;
import org.linuxforhealth.fhir.model.resource.Observation;
import org.linuxforhealth.fhir.model.resource.OperationOutcome;
import org.linuxforhealth.fhir.model.resource.OperationOutcome.Issue;
import org.linuxforhealth.fhir.model.resource.Patient;
import org.linuxforhealth.fhir.model.resource.TerminologyCapabilities;
import org.linuxforhealth.fhir.model.test.TestUtil;
import org.linuxforhealth.fhir.model.type.ContactPoint;
import org.linuxforhealth.fhir.model.type.code.ContactPointSystem;
import org.linuxforhealth.fhir.model.type.code.ContactPointUse;
import org.linuxforhealth.fhir.path.FHIRPathBooleanValue;
import org.linuxforhealth.fhir.path.FHIRPathNode;
import org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator;
import org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;
import org.linuxforhealth.fhir.path.exception.FHIRPathException;
import org.linuxforhealth.fhir.validation.FHIRValidator;
import org.linuxforhealth.fhir.validation.exception.FHIRValidationException;
import org.linuxforhealth.fhir.validation.util.FHIRValidationUtil;
import org.testng.annotations.Test;

import jakarta.json.JsonObject;

/**
 * Basic sniff test of the FHIR Server.
 */
public class BasicServerTest extends FHIRServerTestBase {
    private static final Boolean DEBUG = Boolean.FALSE;
    private Patient savedCreatedPatient;
    private Observation savedCreatedObservation;

    /**
     * Verify the 'metadata' API.
     */
    @Test(groups = { "server-basic" })
    public void testMetadataAPI() throws FHIRPathException, FHIRValidationException {
        WebTarget target = getWebTarget();
        Response response = target.path("metadata").request().get();
        assertResponse(response, Response.Status.OK.getStatusCode());

        CapabilityStatement conf = response.readEntity(CapabilityStatement.class);
        assertNotNull(conf);
        assertNotNull(conf.getFormat());
        assertEquals(conf.getFormat().size(), 6);
        assertNotNull(conf.getVersion());
        assertNotNull(conf.getTitle());

        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        EvaluationContext evaluationContext = new EvaluationContext(conf);
        Collection<FHIRPathNode> result = evaluator.evaluate(evaluationContext, "(kind != 'instance') or implementation.exists()");
        Iterator<FHIRPathNode> iter = result.iterator();
        boolean instance = false;
        while (iter.hasNext()) {
            FHIRPathBooleanValue node = iter.next().as(FHIRPathBooleanValue.class);
            instance = node._boolean();
        }
        assertTrue(instance);

        List<Issue> issues = FHIRValidator.validator().validate(conf);
        assertFalse(FHIRValidationUtil.hasErrors(issues), "CapabilityStatement has validation errors: " + issues);
        if (FHIRValidationUtil.hasWarnings(issues)) {
            String out;
            if (DEBUG) {
                out = FHIRValidationUtil.getWarnings(issues).stream()
                    .map(i -> i.getDetails().getText().getValue())
                    .collect(Collectors.joining("\n"));
            } else {
                out = Long.toString(
                    FHIRValidationUtil.getWarnings(issues).stream()
                        .map(i -> 1)
                        .collect(Collectors.counting()));
            }

            System.out.println("CapabilityStatement warnings: \n" + out);
        }
    }

    @Test(groups = { "server-basic" })
    public void testMetadataAPITerminology() throws FHIRPathException, FHIRValidationException {
        WebTarget target = getWebTarget();
        Response response = target.path("metadata").queryParam("mode", "terminology").request().get();
        assertResponse(response, Response.Status.OK.getStatusCode());

        TerminologyCapabilities conf = response.readEntity(TerminologyCapabilities.class);
        assertNotNull(conf);
        assertNotNull(conf.getVersion());
        assertNotNull(conf.getTitle());

        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        EvaluationContext evaluationContext = new EvaluationContext(conf);
        Collection<FHIRPathNode> result = evaluator.evaluate(evaluationContext, "(kind != 'instance') or implementation.exists()");
        Iterator<FHIRPathNode> iter = result.iterator();
        boolean instance = false;
        while (iter.hasNext()) {
            FHIRPathBooleanValue node = iter.next().as(FHIRPathBooleanValue.class);
            instance = node._boolean();
        }
        assertTrue(instance);

        List<Issue> issues = FHIRValidator.validator().validate(conf);
        assertFalse(FHIRValidationUtil.hasErrors(issues));
        if (FHIRValidationUtil.hasWarnings(issues)) {
            String out;
            if (DEBUG) {
                out = FHIRValidationUtil.getWarnings(issues).stream()
                    .map(i -> i.getDetails().getText().getValue())
                    .collect(Collectors.joining("\n"));
            } else {
                out = Long.toString(
                    FHIRValidationUtil.getWarnings(issues).stream()
                        .map(i -> 1)
                        .collect(Collectors.counting()));
            }
            System.out.println("TerminologyStatement warnings: \n" + out);
        }
    }

    /**
     * Verify the 'metadata' API with Content-Type XML.
     */
    @Test(groups = { "server-basic" })
    public void testMetadataAPI_XML() {
        WebTarget target = getWebTarget();
        Response response = target.path("metadata").request(FHIRMediaType.APPLICATION_FHIR_XML).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        assertEquals(response.getMediaType(), FHIRMediaType.APPLICATION_FHIR_XML_TYPE);

        CapabilityStatement conf = response.readEntity(CapabilityStatement.class);
        assertNotNull(conf);
        assertNotNull(conf.getFormat());
        assertEquals(conf.getFormat().size(), 6);
        assertNotNull(conf.getVersion());
        assertNotNull(conf.getTitle());
    }

    /**
     * Verify the 'metadata' API with valid fhirVersion in Accept header.
     */
    @Test(groups = { "server-basic" })
    public void testMetadataAPI_validFhirVersion() {
        WebTarget target = getWebTarget();
        MediaType mediaType = new MediaType("application", "fhir+json",
            Collections.singletonMap(FHIRMediaType.FHIR_VERSION_PARAMETER, "4.0"));
        Response response = target.path("metadata").request(mediaType).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        assertEquals(response.getMediaType(), mediaType);

        CapabilityStatement conf = response.readEntity(CapabilityStatement.class);
        assertNotNull(conf);
        assertNotNull(conf.getFormat());
        assertEquals(conf.getFormat().size(), 6);
        assertNotNull(conf.getVersion());
        assertNotNull(conf.getTitle());
    }

    /**
     * Verify the 'metadata' API with invalid fhirVersion in Accept header.
     */
    @Test(groups = { "server-basic" })
    public void testMetadataAPI_invalidFhirVersion() {
        WebTarget target = getWebTarget();
        MediaType mediaType = new MediaType("application", "fhir+json",
            Collections.singletonMap(FHIRMediaType.FHIR_VERSION_PARAMETER, "3.0"));
        Response response = target.path("metadata").request(mediaType).get();
        assertResponse(response, Response.Status.NOT_ACCEPTABLE.getStatusCode());
    }

    /**
     * Create a Patient, then make sure we can retrieve it.
     */
    @Test(groups = { "server-basic" })
    public void testCreatePatient() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient and then call the 'create' API.
        Patient patient = TestUtil.readLocalResource("Patient_JohnDoe.json");
        Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Patient").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the patient's logical id value.
        String patientId = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new patient and verify it.
        response = target.path("Patient/" + patientId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Patient responsePatient = response.readEntity(Patient.class);
        savedCreatedPatient = responsePatient;

        TestUtil.assertResourceEquals(patient, responsePatient);
    }

    /**
     * Create a minimal Patient, then make sure we can retrieve it.
     */
    @Test(groups = { "server-basic" })
    public void testCreatePatient_minimal() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient and then call the 'create' API.
        Patient patient = TestUtil.readLocalResource("Patient_DavidOrtiz.json");

        Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Patient").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the patient's logical id value.
        String patientId = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new patient and verify it.
        response = target.path("Patient/" + patientId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Patient responsePatient = response.readEntity(Patient.class);

        TestUtil.assertResourceEquals(patient, responsePatient);
    }

    /**
     * Create a minimal Patient with valid fhirVersion in Content-Type header, then make sure we can retrieve it.
     */
    @Test(groups = { "server-basic" })
    public void testCreatePatient_minimal_validFhirVersion() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient and then call the 'create' API.
        Patient patient = TestUtil.readLocalResource("Patient_DavidOrtiz.json");

        MediaType mediaType = new MediaType("application", "fhir+json",
            Collections.singletonMap(FHIRMediaType.FHIR_VERSION_PARAMETER, "4.0"));
        Entity<Patient> entity = Entity.entity(patient, mediaType);
        Response response = target.path("Patient").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the patient's logical id value.
        String patientId = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new patient and verify it.
        response = target.path("Patient/" + patientId).request(mediaType).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Patient responsePatient = response.readEntity(Patient.class);

        TestUtil.assertResourceEquals(patient, responsePatient);
    }

    /**
     * Attempt to create a minimal Patient with invalid fhirVersion in Content-Type header.
     */
    @Test( groups = { "server-basic" })
    public void testCreatePatient_minimal_invalidFhirVersion() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient and then call the 'create' API.
        Patient patient = TestUtil.readLocalResource("Patient_DavidOrtiz.json");

        MediaType mediaType = new MediaType("application", "fhir+json",
            Collections.singletonMap(FHIRMediaType.FHIR_VERSION_PARAMETER, "3.0"));
        Entity<Patient> entity = Entity.entity(patient, mediaType);
        Response response = target.path("Patient").request().post(entity, Response.class);
        assertResponse(response, Response.Status.UNSUPPORTED_MEDIA_TYPE.getStatusCode());
    }

    /**
     * Create a minimal Patient, then make sure we can retrieve it with varying format
     */
    @Test(groups = { "server-basic" })
    public void testCreatePatientMinimalWithFormat() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient and then call the 'create' API.
        Patient patient = TestUtil.readLocalResource("Patient_DavidOrtiz.json");

        Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Patient").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the patient's logical id value.
        String patientId = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new patient and verify it.
        response = target.path("Patient/" + patientId).queryParam("_format", "application/fhir+json").request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Patient responsePatient = response.readEntity(Patient.class);

        TestUtil.assertResourceEquals(patient, responsePatient);
    }

    /**
     * Create a minimal Patient, then make sure we can retrieve it with varying format with valid FHIR version
     */
    @Test(groups = { "server-basic" })
    public void testCreatePatientMinimalWithFormat_validFhirVersion() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient and then call the 'create' API.
        Patient patient = TestUtil.readLocalResource("Patient_DavidOrtiz.json");

        Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Patient").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the patient's logical id value.
        String patientId = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new patient and verify it.
        response = target.path("Patient/" + patientId).queryParam("_format", "application/fhir+json;fhirVersion=4.0").request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Patient responsePatient = response.readEntity(Patient.class);

        TestUtil.assertResourceEquals(patient, responsePatient);
    }

    /**
     * Create a minimal Patient, then attempt to retrieve it with varying format with invalid FHIR version
     */
    @Test(groups = { "server-basic" })
    public void testCreatePatientMinimalWithFormat_invalidFhirVersion() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient and then call the 'create' API.
        Patient patient = TestUtil.readLocalResource("Patient_DavidOrtiz.json");

        Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Patient").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the patient's logical id value.
        String patientId = getLocationLogicalId(response);

        // Next, call the 'read' API to attempt to retrieve the new patient
        response = target.path("Patient/" + patientId).queryParam("_format", "application/fhir+json;fhirVersion=3.0").request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.NOT_ACCEPTABLE.getStatusCode());
    }

    /**
     * Create a minimal Patient, then make sure we can retrieve it.
     */
    @Test(groups = { "server-basic" })
    public void testCreatePatient_minimal_extraElement() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient and then call the 'create' API.
        Patient patient = TestUtil.readLocalResource("Patient_DavidOrtiz.json");

        StringWriter writer = new StringWriter();
        FHIRGenerator.generator(Format.JSON, false).generate(patient, writer);
        String patientResourceString = writer.toString();
        String patientResourceStringWithFakeElement = patientResourceString.substring(0, 1) + "\"fake\":\"value\","
                + patientResourceString.substring(1);

        Entity<String> entity = Entity.entity(patientResourceStringWithFakeElement, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Patient").request().post(entity, Response.class);

        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
        OperationOutcome responseResource = response.readEntity(OperationOutcome.class);
        assertNotNull(responseResource);
        assertTrue(responseResource.getIssue().size() > 0);
    }

    /**
     * Create an Observation and make sure we can retrieve it.
     */
    @Test(groups = { "server-basic" }, dependsOnMethods = { "testCreatePatient" })
    public void testCreateObservation() throws Exception {
        WebTarget target = getWebTarget();

        // Next, create an Observation belonging to the new patient.
        String patientId = savedCreatedPatient.getId();
        Observation observation = TestUtil.buildPatientObservation(patientId, "Observation1.json");
        Entity<Observation> obs = Entity.entity(observation, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Observation").request().post(obs, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        String observationId = getLocationLogicalId(response);

        // Next, retrieve the new Observation with a read operation and verify it.
        response = target.path("Observation/" + observationId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Observation responseObs = response.readEntity(Observation.class);
        savedCreatedObservation = responseObs;

        TestUtil.assertResourceEquals(observation, responseObs);
    }

    @Test( groups = { "server-basic" })
    public void testCreateObservationWithUnrecognizedElements_strict() throws Exception {
        WebTarget target = getWebTarget();
        JsonObject jsonObject = TestUtil.readJsonObject("testdata/observation-unrecognized-elements.json");
        Entity<JsonObject> entity = Entity.entity(jsonObject, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Observation").request().post(entity, Response.class);
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test( groups = { "server-basic" })
    public void testCreateObservationWithUnrecognizedElements_lenient_minimal() throws Exception {
        WebTarget target = getWebTarget();
        JsonObject jsonObject = TestUtil.readJsonObject("testdata/observation-unrecognized-elements.json");
        Entity<JsonObject> entity = Entity.entity(jsonObject, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Observation").request()
                    .header("Prefer", "handling=lenient")
                    .post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());
    }

    @Test( groups = { "server-basic" })
    public void testCreateObservationWithUnrecognizedElements_lenient_representation() throws Exception {
        WebTarget target = getWebTarget();
        JsonObject jsonObject = TestUtil.readJsonObject("testdata/observation-unrecognized-elements.json");
        Entity<JsonObject> entity = Entity.entity(jsonObject, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Observation").request()
                    .header("Prefer", "handling=lenient,return=representation")
                    .post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());
        assertNotNull(response.readEntity(Observation.class));
    }

    @Test( groups = { "server-basic" })
    public void testCreateObservationWithUnrecognizedElements_lenient_OperationOutcome() throws Exception {
        WebTarget target = getWebTarget();
        JsonObject jsonObject = TestUtil.readJsonObject("testdata/observation-unrecognized-elements.json");
        Entity<JsonObject> entity = Entity.entity(jsonObject, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Observation").request()
                    .header("Prefer", "handling=lenient, return=OperationOutcome")
                    .post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());
        OperationOutcome outcome = response.readEntity(OperationOutcome.class);
        assertNotNull(outcome);
        assertTrue(outcome.getIssue().size() > 0);
    }

    /**
     * Tests the update of the original patient that was previously created.
     */
    @Test(groups = { "server-basic" }, dependsOnMethods = { "testCreatePatient" })
    public void testUpdatePatient() throws Exception {
        WebTarget target = getWebTarget();

        // Create a fresh copy of the mock Patient.
        Patient patient = TestUtil.readLocalResource("Patient_JohnDoe.json");

        // Be sure to set the saved patient's id as well.
        // And add an additional contact phone number
        patient = patient.toBuilder().id(savedCreatedPatient.getId()).telecom(ContactPoint.builder()
                .system(ContactPointSystem.PHONE).use(ContactPointUse.WORK).value(string("999-111-1111")).build())
                .build();

        Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);

        // Now call the 'update' API.
        String targetPath = "Patient/" + patient.getId();
        Response response = target.path(targetPath).request().put(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());

        // Get the updated patient's logical id value.
        String patientId = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the updated patient and verify it.
        response = target.path("Patient/" + patientId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Patient responsePatient = response.readEntity(Patient.class);

        TestUtil.assertResourceEquals(patient, responsePatient);
    }

    /**
     * Tests the update of the original observation that was previously created.
     */
    @Test(groups = { "server-basic" }, dependsOnMethods = { "testCreateObservation" })
    public void testUpdateObservation() throws Exception {
        WebTarget target = getWebTarget();

        // Create an updated Observation based on the original saved observation
        String patientId = savedCreatedPatient.getId();
        Observation observation = TestUtil.buildPatientObservation(patientId, "Observation2.json");

        observation = observation.toBuilder().id(savedCreatedObservation.getId()).build();

        Entity<Observation> obs = Entity.entity(observation, FHIRMediaType.APPLICATION_FHIR_JSON);

        // Call the 'update' API.
        String targetPath = "Observation/" + observation.getId();
        Response response = target.path(targetPath).request().put(obs, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        String observationId = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the updated observation and verify it.
        response = target.path("Observation/" + observationId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Observation responseObservation = response.readEntity(Observation.class);
        assertNotNull(responseObservation);
        TestUtil.assertResourceEquals(observation, responseObservation);
    }

    /**
     * Tests the retrieval of the history for a previously saved and updated patient.
     */
    @Test(groups = { "server-basic" }, dependsOnMethods = { "testCreatePatient", "testUpdatePatient" })
    public void testHistoryPatient() {
        WebTarget target = getWebTarget();

        // Call the 'history' API to retrieve both the original and updated versions of the patient.
        String targetPath = "Patient/" + savedCreatedPatient.getId() + "/_history";
        Response response = target.path(targetPath).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());

        Bundle resources = response.readEntity(Bundle.class);
        assertNotNull(resources);
        assertEquals(resources.getEntry().size(), 2);

        for (Entry entry : resources.getEntry()) {
            String fullUrl = entry.getFullUrl().getValue();
            assertEquals(fullUrl, getRestBaseURL() + "/Patient/" + savedCreatedPatient.getId());
        }

        Patient updatedPatient = (Patient) resources.getEntry().get(0).getResource();
        Patient originalPatient = (Patient) resources.getEntry().get(1).getResource();
        // Make sure patient ids are equal, and versionIds are NOT equal.
        assertEquals(originalPatient.getId(), updatedPatient.getId());
        assertTrue(!updatedPatient.getMeta().getVersionId().getValue()
                .equals(originalPatient.getMeta().getVersionId().getValue()));
        // Patient create time should be earlier than Patient update time.
        assertTrue(originalPatient.getMeta().getLastUpdated().getValue()
                .compareTo(updatedPatient.getMeta().getLastUpdated().getValue()) < 0);

    }

    /**
     * Tests the retrieval of the history for a previously saved and updated observation.
     */
    @Test(groups = { "server-basic" }, dependsOnMethods = { "testCreateObservation", "testUpdateObservation" })
    public void testHistoryObservation() {
        WebTarget target = getWebTarget();

        // Call the 'history' API to retrieve both the original and updated versions of the observation.
        String targetPath = "Observation/" + savedCreatedObservation.getId() + "/_history";
        Response response = target.path(targetPath).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());

        Bundle resources = response.readEntity(Bundle.class);
        assertNotNull(resources);
        assertEquals(resources.getEntry().size(), 2);

        for (Entry entry : resources.getEntry()) {
            String fullUrl = entry.getFullUrl().getValue();
            assertEquals(fullUrl, getRestBaseURL() + "/Observation/" + savedCreatedObservation.getId());
        }

        Observation updatedObservation = (Observation) resources.getEntry().get(0).getResource();
        Observation originalObservation = (Observation) resources.getEntry().get(1).getResource();
        // Make sure observation ids are equal, and versionIds are NOT equal.
        assertEquals(originalObservation.getId(), updatedObservation.getId());
        assertTrue(!updatedObservation.getMeta().getVersionId().getValue()
                .equals(originalObservation.getMeta().getVersionId().getValue()));
        // Observation create time should be earlier than Observation update time.
        assertTrue(originalObservation.getMeta().getLastUpdated().getValue()
                .compareTo(updatedObservation.getMeta().getLastUpdated().getValue()) < 0);
    }

    /**
     * Tests the retrieval of a particular version of a patient.
     */
    @Test(groups = { "server-basic" }, dependsOnMethods = { "testCreatePatient", "testUpdatePatient" })
    public void testVreadPatient() {
        WebTarget target = getWebTarget();

        // Call the 'version read' API to retrieve the original created version of the
        // patient.
        String targetPath = "Patient/" + savedCreatedPatient.getId() + "/_history/"
                + savedCreatedPatient.getMeta().getVersionId().getValue();
        Response response = target.path(targetPath).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Patient originalPatient = response.readEntity(Patient.class);
        assertNotNull(originalPatient);
        TestUtil.assertResourceEquals(savedCreatedPatient, originalPatient);

        // Now try reading a Patient, passing a bogus version.
        targetPath = "Patient/" + savedCreatedPatient.getId() + "/_history/" + "-44";
        response = target.path(targetPath).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.NOT_FOUND.getStatusCode());
    }

    /**
     * Tests the retrieval of a particular version of an observation.
     */
    @Test(groups = { "server-basic" }, dependsOnMethods = { "testCreateObservation", "testUpdateObservation" })
    public void testVreadObservation() {
        WebTarget target = getWebTarget();

        // Call the 'version read' API to retrieve the original created version of the
        // observation.
        String targetPath = "Observation/" + savedCreatedObservation.getId() + "/_history/"
                + savedCreatedObservation.getMeta().getVersionId().getValue();
        Response response = target.path(targetPath).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Observation originalObservation = response.readEntity(Observation.class);
        assertNotNull(originalObservation);
        TestUtil.assertResourceEquals(savedCreatedObservation, originalObservation);

        // Now try reading an Observation, passing a bogus version.
        targetPath = "Observation/" + savedCreatedObservation.getId() + "/_history/" + "-44";
        response = target.path(targetPath).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.NOT_FOUND.getStatusCode());
    }

    /**
     * Tests a search for a patient.
     */
    @Test(groups = { "server-basic" }, dependsOnMethods = { "testCreatePatient", "testUpdatePatient" })
    public void testSearchPatient() {
        WebTarget target = getWebTarget();

        String familyName = savedCreatedPatient.getName().get(0).getFamily().getValue();
        Response response = target.path("Patient").queryParam("family", familyName)
                .request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);

        assertNotNull(bundle.getTotal());
        assertNotNull(bundle.getTotal().getValue());
        // assertEquals(new Double(bundle.getEntry().size()), new
        // Double(bundle.getTotal().getValue().doubleValue()));

        String homePhone = savedCreatedPatient.getTelecom().get(0).getValue().getValue();
        response = target.path("Patient").queryParam("telecom", homePhone).request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    /**
     * Searches for all Patients, and ensures that no duplicates for the same
     * Patient are returned. (This ensures that multiple versions of a Patient with
     * the same logical id are not returned.)
     */
    @Test(groups = { "server-basic" }, dependsOnMethods = { "testCreatePatient", "testUpdatePatient" })
    public void testSearchAllPatients() {
        WebTarget target = getWebTarget();

        Response response = target.path("Patient").request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertNotNull(bundle.getTotal());
        assertNotNull(bundle.getTotal().getValue());

        List<String> patientLogicalIds = new ArrayList<>();
        Patient patient;
        for (Bundle.Entry entry : bundle.getEntry()) {
            patient = (Patient) entry.getResource();
            if (patientLogicalIds.contains(patient.getId())) {
                fail("Duplicate logicalId found: " + patient.getId());
            } else {
                patientLogicalIds.add(patient.getId());
            }
        }
    }

    /**
     * Tests a search for an observation based on its association with a patient.
     */
    @Test(groups = { "server-basic" }, dependsOnMethods = { "testCreatePatient", "testUpdatePatient",
            "testCreateObservation" })
    public void testSearchObservation() {
        WebTarget target = getWebTarget();

        // Next, retrieve the Observation via a search.
        String patientId = savedCreatedPatient.getId();
        Response response = target.path("Observation").queryParam("subject", "Patient/" + patientId)
                .request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertEquals(bundle.getEntry().size(), 1);
    }
}
