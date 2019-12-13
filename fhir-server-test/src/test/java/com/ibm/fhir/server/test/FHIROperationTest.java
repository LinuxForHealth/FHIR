/*
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test;

import static com.ibm.fhir.model.type.String.string;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import com.ibm.fhir.client.FHIRClient;
import com.ibm.fhir.client.FHIRParameters;
import com.ibm.fhir.client.FHIRResponse;
import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.resource.AllergyIntolerance;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Composition;
import com.ibm.fhir.model.resource.Composition.Section;
import com.ibm.fhir.model.resource.Condition;
import com.ibm.fhir.model.resource.Observation;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Practitioner;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.CompositionStatus;

public class FHIROperationTest extends FHIRServerTestBase {
    private Patient savedCreatedPatient = null;
    private Practitioner savedCreatedPractitioner = null;
    private Observation savedCreatedObservation = null;
    private Condition savedCreatedCondition = null;
    private AllergyIntolerance savedCreatedAllergyIntolerance = null;
    private Composition savedCreatedComposition = null;

    @Test(groups = { "fhir-operation" })
    public void testCreatePractitioner() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Practitioner and then call the 'create' API.
        Practitioner practitioner = TestUtil.readLocalResource("Practitioner.json");
        Entity<Practitioner> entity = Entity.entity(practitioner, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Practitioner").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the practitioner's logical id value.
        String practitionerId = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new practitioner and verify it.
        response = target.path("Practitioner/" + practitionerId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Practitioner reponsePractitioner = response.readEntity(Practitioner.class);
        savedCreatedPractitioner = reponsePractitioner;

        TestUtil.assertResourceEquals(practitioner, reponsePractitioner);
    }

    @Test(groups = { "fhir-operation" }, dependsOnMethods = { "testCreatePractitioner" })
    public void testCreatePatient() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient and then call the 'create' API.
        String practitionerId = savedCreatedPractitioner.getId();
        Patient patient = TestUtil.readLocalResource("Patient_JohnDoe.json");
        patient = patient.toBuilder()
                .generalPractitioner(Reference.builder().reference(string("Practitioner/" + practitionerId)).build())
                .build();

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

    @Test(groups = { "fhir-operation" }, dependsOnMethods = { "testCreatePatient" })
    public void testCreateObservation() throws Exception {
        WebTarget target = getWebTarget();

        // Next, create an Observation belonging to the new patient.
        String patientId = savedCreatedPatient.getId();
        Observation observation = TestUtil.buildPatientObservation(patientId, "Observation1.json");
        Entity<Observation> obs = Entity.entity(observation, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Observation").request().post(obs, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the observation's logical id value.
        String observationId = getLocationLogicalId(response);

        // Next, retrieve the new Observation with a read operation and verify it.
        response = target.path("Observation/" + observationId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Observation responseObs = response.readEntity(Observation.class);
        savedCreatedObservation = responseObs;

        TestUtil.assertResourceEquals(observation, responseObs);
    }

    @Test(groups = { "fhir-operation" }, dependsOnMethods = { "testCreatePatient" })
    public void testCreateCondition() throws Exception {
        WebTarget target = getWebTarget();

        // Next, create a Condition belonging to the new patient.
        String patientId = savedCreatedPatient.getId();
        Condition condition = buildCondition(patientId, "Condition.json");
        Entity<Condition> obs = Entity.entity(condition, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Condition").request().post(obs, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the condition's logical id value.
        String conditionId = getLocationLogicalId(response);

        // Next, retrieve the new Condition with a read operation and verify it.
        response = target.path("Condition/" + conditionId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Condition responseCondition = response.readEntity(Condition.class);
        savedCreatedCondition = responseCondition;

        TestUtil.assertResourceEquals(condition, responseCondition);
    }

    @Test(groups = { "fhir-operation" }, dependsOnMethods = { "testCreatePatient" })
    public void testCreateAllergyIntolerance() throws Exception {
        WebTarget target = getWebTarget();

        // Next, create a AllergyIntolerance belonging to the new patient.
        String patientId = savedCreatedPatient.getId();
        AllergyIntolerance allergyIntolerance = buildAllergyIntolerance(patientId, "AllergyIntolerance.json");
        Entity<AllergyIntolerance> obs = Entity.entity(allergyIntolerance, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("AllergyIntolerance").request().post(obs, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the allergy intolerance's logical id value.
        String allergyIntoleranceId = getLocationLogicalId(response);

        // Next, retrieve the new AllergyIntolerance with a read operation and verify
        // it.
        response = target.path("AllergyIntolerance/" + allergyIntoleranceId).request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        AllergyIntolerance responseAllergyIntolerance = response.readEntity(AllergyIntolerance.class);
        savedCreatedAllergyIntolerance = responseAllergyIntolerance;

        TestUtil.assertResourceEquals(allergyIntolerance, responseAllergyIntolerance);
    }

    @Test(groups = { "fhir-operation" }, dependsOnMethods = { "testCreateObservation" })
    public void testCreateComposition() throws Exception {
        String practitionerId = savedCreatedPractitioner.getId();
        String patientId = savedCreatedPatient.getId();
        String observationId = savedCreatedObservation.getId();
        String conditionId = savedCreatedCondition.getId();
        String allergyIntoleranceId = savedCreatedAllergyIntolerance.getId();

        WebTarget target = getWebTarget();

        // Build a new Composition and then call the 'create' API.
        Composition composition = buildComposition(practitionerId, patientId, observationId, conditionId,
                allergyIntoleranceId);
        Entity<Composition> entity = Entity.entity(composition, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Composition").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the composition's logical id value.
        String compositionId = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new composition and verify it.
        response = target.path("Composition/" + compositionId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Composition responseComposition = response.readEntity(Composition.class);
        savedCreatedComposition = responseComposition;

        TestUtil.assertResourceEquals(composition, responseComposition);
    }

    private Composition buildComposition(String practitionerId, String patientId, String observationId,
            String conditionId, String allergyIntoleranceId) {
        List<Reference> authorList = new ArrayList<Reference>();
        authorList.add(Reference.builder().reference(string("Practitioner/" + practitionerId)).build());
        Composition composition = Composition
                .builder()
                .status(CompositionStatus.FINAL)
                .type(CodeableConcept.builder()
                        .coding(Coding.builder().code(Code.of("somecode-1234"))
                                .system(Uri.of("http://somesystem.org")).build())
                        .build())
                .date(DateTime.of("2015-02-14T13:42:00+10:00"))
                .title(string("This is the title"))
                .author(authorList)
                .subject(Reference.builder().reference(string("Patient/" + patientId)).build())
                .section(Section.builder()
                        .entry(Reference.builder().reference(string("Observation/" + observationId)).build()).build())
                .section(Section.builder()
                        .entry(Reference.builder().reference(string("Condition/" + conditionId)).build()).build())
                .section(
                        Section.builder()
                                .entry(Reference.builder()
                                        .reference(string("AllergyIntolerance/" + allergyIntoleranceId)).build())
                                .build())
                .build();

        return composition;
    }

    private AllergyIntolerance buildAllergyIntolerance(String patientId, String fileName) throws Exception {
        AllergyIntolerance allergyIntolerance = TestUtil.readLocalResource(fileName);
        allergyIntolerance = allergyIntolerance
                .toBuilder().patient(Reference.builder().reference(string("Patient/" + patientId)).build()).build();
        return allergyIntolerance;
    }

    @Test(groups = { "fhir-operation" })
    // Testcase for "POST [baseUrl]/${operationName}"
    public void testPostHelloOperation() throws Exception {
        String message = "Hello, World!";

        Parameters parameters = Parameters.builder()
                .parameter(Parameter.builder().name(string("input-string")).value(string(message)).build()).build();

        FHIRClient client = getFHIRClient();
        FHIRResponse response = client.invoke("$hello", parameters);

        Parameters output = response.getResource(Parameters.class);

        assertNotNull(output);
        assertEquals(((com.ibm.fhir.model.type.String) output.getParameter().get(0).getValue()).getValue(),
                message);
    }

    @Test(groups = { "fhir-operation" })
    // Testcase for GET [baseUrl]/${operationName}?input-string="Hello, World!"
    public void testGetHelloOperation() throws Exception {
        String message = "Hello, World!";

        FHIRParameters parameters = new FHIRParameters();
        parameters.queryParam("input-string", message);

        FHIRClient client = getFHIRClient();
        FHIRResponse response = client.invoke("$hello", parameters);

        assertEquals(response.getStatus(), 200, "Expect HTTP 200 response but got HTTP " + response.getStatus());
        assertFalse(response.isEmpty(), "Response unexpectedly contains no resource.");
        Parameters output = response.getResource(Parameters.class);

        assertNotNull(output);
        assertFalse(output.getParameter().isEmpty());
        assertEquals(((com.ibm.fhir.model.type.String) output.getParameter().get(0).getValue()).getValue(),
                message);
    }
    
    @Test(groups = { "fhir-operation" })
    // Testcase for GET [baseUrl]/${operationName}?input-boolean=true&input-canonical="https://examples.com"&input-code...
    public void testGetHelloOperationWithPrimitives() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.queryParam("input-boolean", "true");                                         //1
        parameters.queryParam("input-canonical", "http://example.com");                         //2
        parameters.queryParam("input-code", "testCode");                                        //3
        parameters.queryParam("input-date", "2019-09-04");                                      //4
        parameters.queryParam("input-dateTime", "2019-09-19T11:10:00.000Z");                    //5
        parameters.queryParam("input-id", "abc123");                                            //6
        parameters.queryParam("input-instant", "2019-09-19T11:10:00.000Z");                     //7
        parameters.queryParam("input-integer", "1");                                            //8
        parameters.queryParam("input-oid", "urn:oid:2.16.840.1.113883.3.18");                   //9
        parameters.queryParam("input-positiveInt", "1");                                        //10
        parameters.queryParam("input-string", "Hello, World!");                                 //11
        parameters.queryParam("input-time", "11:12:00");                                        //12
        parameters.queryParam("input-unsignedInt", "1");                                        //13
        parameters.queryParam("input-uri", "http://example.com");                               //14
        parameters.queryParam("input-url", "http://example.com");                               //15
        parameters.queryParam("input-uuid", "urn:uuid:f35473c3-bf5e-4c76-b184-bca9b8bc1cc2");   //16

        FHIRClient client = getFHIRClient();
        FHIRResponse response = client.invoke("$hello", parameters);

        assertEquals(response.getStatus(), 200, "Expect HTTP 200 response but got HTTP " + response.getStatus());
        assertFalse(response.isEmpty(), "Response unexpectedly contains no resource.");
        Parameters output = response.getResource(Parameters.class);

        assertNotNull(output);
        assertFalse(output.getParameter().isEmpty());
        assertEquals(output.getParameter().size(), 16);
    }
    
    @Test(groups = { "fhir-operation" })
    // Testcase for GET [baseUrl]/${operationName}"
    public void testGetHelloOperationWithEmptyBody() throws Exception {
        FHIRParameters parameters = null;

        FHIRClient client = getFHIRClient();
        FHIRResponse response = client.invoke("$hello", parameters);

        assertEquals(response.getStatus(), 200, "Expect HTTP 200 response but got HTTP " + response.getStatus());
        assertTrue(response.isEmpty(), "Response contains an unexpected resource.");
    }

    @Test(groups = { "fhir-operation" })
    // Testcase for "POST [baseUrl]/{resourceTypeName}/${operationName}"
    public void testPostRscValidateOperation() throws Exception {
        Patient patient = TestUtil.readLocalResource("Patient_JohnDoe.json");

        Parameters parameters = Parameters.builder()
                .parameter(Parameter.builder().name(string("resource")).resource(patient).build()).build();

        FHIRClient client = getFHIRClient();
        FHIRResponse response = client.invoke("Patient", "$validate", parameters);

        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
        OperationOutcome operationOutcome = response.getResource(OperationOutcome.class);
        assertNotNull(operationOutcome);
    }

    @Test(groups = { "fhir-operation" })
    // Testcase for "GET
    // [baseUrl]/{resourceTypeName}/${operationName}?resource=null"
    public void testGetRscValidateOperation() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.queryParam("resource", null);

        FHIRClient client = getFHIRClient();
        FHIRResponse response = client.invoke("Patient", "$validate", parameters);

        assertEquals(response.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());
        OperationOutcome operationOutcome = response.getResource(OperationOutcome.class);
        String text = operationOutcome.getIssue().get(0).getDetails().getText().getValue();
        if (text.contains("Input parameter 'resource' is required")) {
            assertTrue(true); // Force assertion to true
        } else {
            assertTrue(false); // Force assertion to false
        }
    }

    @Test(groups = { "fhir-operation" }, dependsOnMethods = { "testCreatePractitioner", "testCreatePatient" })
    // Testcase for "POST [baseUrl]/{resourceTypeName}/{logicalId}/${operationName}"
    public void testPostRscIdValidateOperation() throws Exception {
        Parameters parameters = Parameters.builder()
                .parameter(Parameter.builder().name(string("resource")).resource(savedCreatedPatient).build()).build();

        FHIRClient client = getFHIRClient();
        FHIRResponse response = client.invoke("Patient", "$validate", savedCreatedPatient.getId(),
                parameters);

        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
        OperationOutcome operationOutcome = response.getResource(OperationOutcome.class);
        assertNotNull(operationOutcome);
    }

    @Test(groups = { "fhir-operation" }, dependsOnMethods = { "testCreateComposition" })
    // Testcase for "GET
    // [baseUrl]/{resourceTypeName}/{logicalId}/${operationName}?persist=true"
    public void testGetRscIdDocumentOperation() throws Exception {
        String compositionId = savedCreatedComposition.getId();

        FHIRParameters parameters = new FHIRParameters();
        parameters.queryParam("persist", "true");

        FHIRClient client = getFHIRClient();
        FHIRResponse response = client.invoke("Composition", "$document", compositionId, parameters);

        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());

        Bundle document = response.getResource(Bundle.class);

        assertNotNull(document);
        assertTrue(document.getEntry().size() == 6);

        Composition composition = (Composition) document.getEntry().get(0).getResource();
        TestUtil.assertResourceEquals(savedCreatedComposition, composition);

        Patient patient = (Patient) document.getEntry().get(1).getResource();
        TestUtil.assertResourceEquals(savedCreatedPatient, patient);

        Practitioner practitioner = (Practitioner) document.getEntry().get(2).getResource();
        TestUtil.assertResourceEquals(savedCreatedPractitioner, practitioner);

        Observation observation = (Observation) document.getEntry().get(3).getResource();
        TestUtil.assertResourceEquals(savedCreatedObservation, observation);

        Condition condition = (Condition) document.getEntry().get(4).getResource();
        TestUtil.assertResourceEquals(savedCreatedCondition, condition);

        AllergyIntolerance allergyIntolerance = (AllergyIntolerance) document.getEntry().get(5).getResource();
        TestUtil.assertResourceEquals(savedCreatedAllergyIntolerance, allergyIntolerance);
    }

    @Test(groups = { "fhir-operation" }, dependsOnMethods = { "testCreatePatient" })
    // Testcase for "POST
    // [baseUrl]/{resourceTypeName}/{logicalId}/_history/{versionId}/${operationName}"
    public void testPostRscIdVersionValidateOperation() throws Exception {
        Parameters parameters = Parameters.builder()
                .parameter(Parameter.builder().name(string("resource")).resource(savedCreatedPatient).build()).build();

        FHIRClient client = getFHIRClient();
        FHIRResponse response = client.invoke("Patient", "$validate", savedCreatedPatient.getId(), "1",
                parameters);

        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
        OperationOutcome operationOutcome = response.getResource(OperationOutcome.class);
        assertNotNull(operationOutcome);
    }

    @Test(groups = { "fhir-operation" }, dependsOnMethods = { "testCreateComposition" })
    // Testcase for "GET
    // [baseUrl]/{resourceTypeName}/{logicalId}/_history/{versionId}/${operationName}?persist=true"
    public void testPostRscIdVersionDocumentOperation() throws Exception {
        String compositionId = savedCreatedComposition.getId();

        FHIRParameters parameters = new FHIRParameters();
        parameters.queryParam("persist", "true");

        FHIRClient client = getFHIRClient();
        FHIRResponse response = client.invoke("Composition", "$document", compositionId, "1", parameters);

        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());

        Bundle document = response.getResource(Bundle.class);

        assertNotNull(document);
        assertTrue(document.getEntry().size() == 6);

        Composition composition = (Composition) document.getEntry().get(0).getResource();
        TestUtil.assertResourceEquals(savedCreatedComposition, composition);

        Patient patient = (Patient) document.getEntry().get(1).getResource();
        TestUtil.assertResourceEquals(savedCreatedPatient, patient);

        Practitioner practitioner = (Practitioner) document.getEntry().get(2).getResource();
        TestUtil.assertResourceEquals(savedCreatedPractitioner, practitioner);

        Observation observation = (Observation) document.getEntry().get(3).getResource();
        TestUtil.assertResourceEquals(savedCreatedObservation, observation);

        Condition condition = (Condition) document.getEntry().get(4).getResource();
        TestUtil.assertResourceEquals(savedCreatedCondition, condition);

        AllergyIntolerance allergyIntolerance = (AllergyIntolerance) document.getEntry().get(5).getResource();
        TestUtil.assertResourceEquals(savedCreatedAllergyIntolerance, allergyIntolerance);
    }
}
