/*
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.server.test;

import static org.linuxforhealth.fhir.model.type.String.string;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.core.FHIRMediaType;
import org.linuxforhealth.fhir.model.format.Format;
import org.linuxforhealth.fhir.model.generator.FHIRGenerator;
import org.linuxforhealth.fhir.model.resource.AllergyIntolerance;
import org.linuxforhealth.fhir.model.resource.Bundle;
import org.linuxforhealth.fhir.model.resource.Composition;
import org.linuxforhealth.fhir.model.resource.Composition.Section;
import org.linuxforhealth.fhir.model.resource.Condition;
import org.linuxforhealth.fhir.model.resource.Observation;
import org.linuxforhealth.fhir.model.resource.Patient;
import org.linuxforhealth.fhir.model.resource.Practitioner;
import org.linuxforhealth.fhir.model.test.TestUtil;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.CodeableConcept;
import org.linuxforhealth.fhir.model.type.Coding;
import org.linuxforhealth.fhir.model.type.DateTime;
import org.linuxforhealth.fhir.model.type.Reference;
import org.linuxforhealth.fhir.model.type.Uri;
import org.linuxforhealth.fhir.model.type.code.CompositionStatus;

public class FHIRDocumentOperationTest extends FHIRServerTestBase {
    private Patient savedCreatedPatient = null;
    private Practitioner savedCreatedPractitioner = null;
    private Observation savedCreatedObservation = null;
    private Condition savedCreatedCondition = null;
    private AllergyIntolerance savedCreatedAllergyIntolerance = null;
    private Composition savedCreatedComposition = null;

    private static final boolean DEBUG = false;

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

        if (DEBUG) {
            System.out.println("allergyIntolernace: ");
            FHIRGenerator.generator(Format.JSON, false).generate(allergyIntolerance, System.out);

            System.out.println("responseAllergyIntolerance:  ");
            FHIRGenerator.generator(Format.JSON, false).generate(responseAllergyIntolerance, System.out);
        }

        TestUtil.assertResourceEquals(allergyIntolerance, responseAllergyIntolerance);
    }

    @Test(groups = { "fhir-operation" }, dependsOnMethods = { "testCreateObservation",
            "testCreateCondition", "testCreateAllergyIntolerance" })
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

    @Test(groups = { "fhir-operation" })
    public void testCompositionDoesNotExist() throws Exception {
        WebTarget target = getWebTarget();

        String compositionId = "DOES-NOT-EXIST";

        Response response = target.path("Composition/" + compositionId + "/$document").queryParam("persist", "true")
                .request().get(Response.class);
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
    }

    private Composition buildComposition(String practitionerId, String patientId, String observationId,
            String conditionId, String allergyIntoleranceId) {
        List<Reference> authorList = new ArrayList<Reference>();
        authorList.add(Reference.builder().reference(string("Practitioner/" + practitionerId)).build());
        Composition composition = Composition
                .builder()
                .status(CompositionStatus.FINAL)
                .date(DateTime.of("2015-02-14T13:42:00+10:00"))
                .title(string("This is the title"))
                .author(authorList)
                .type(CodeableConcept.builder()
                        .coding(Coding.builder().code(Code.of("somecode-1234"))
                                .system(Uri.of("http://somesystem.org")).build())
                        .build())
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

    @Test(groups = { "fhir-operation" }, dependsOnMethods = { "testCreateComposition" })
    public void testDocumentOperation() throws Exception {
        WebTarget target = getWebTarget();

        String compositionId = savedCreatedComposition.getId();
        Response response = target.path("Composition/" + compositionId + "/$document").request().get(Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());

        Bundle document = response.readEntity(Bundle.class);

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

    @Test(groups = { "fhir-operation" }, dependsOnMethods = { "testCreateComposition" })
    public void testDocumentOperationWithPersistence() throws Exception {
        WebTarget target = getWebTarget();

        String compositionId = savedCreatedComposition.getId();
        Response response = target.path("Composition/" + compositionId + "/$document").queryParam("persist", "true")
                .request().get(Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());

        String documentId = getLocationLogicalId(response);

        Bundle document = response.readEntity(Bundle.class);

        assertEquals(documentId, document.getId());

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

        target = getWebTarget();
        response = target.path("Bundle/" + documentId).request().get(Response.class);

        Bundle responseDocument = response.readEntity(Bundle.class);
        TestUtil.assertResourceEquals(responseDocument, document);
    }

    private AllergyIntolerance buildAllergyIntolerance(String patientId, String fileName) throws Exception {
        AllergyIntolerance allergyIntolerance = TestUtil.readLocalResource(fileName);

        allergyIntolerance = allergyIntolerance
                .toBuilder().patient(Reference.builder().reference(string("Patient/" + patientId)).build()).build();
        return allergyIntolerance;
    }
}
