/**
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.server.test;

import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.Coding;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.Uri;

import static com.ibm.watsonhealth.fhir.model.type.String.string;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import com.ibm.watsonhealth.fhir.core.MediaType;
import com.ibm.watsonhealth.fhir.model.resource.AllergyIntolerance;
import com.ibm.watsonhealth.fhir.model.resource.Bundle;
import com.ibm.watsonhealth.fhir.model.resource.Composition;
import com.ibm.watsonhealth.fhir.model.resource.Composition.Section;
import com.ibm.watsonhealth.fhir.model.type.CompositionStatus;
import com.ibm.watsonhealth.fhir.model.resource.Condition;
import com.ibm.watsonhealth.fhir.model.resource.Observation;
import com.ibm.watsonhealth.fhir.model.resource.Patient;
import com.ibm.watsonhealth.fhir.model.resource.Practitioner;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil;
import com.ibm.watsonhealth.fhir.model.format.Format;

public class FHIRDocumentOperationTest extends FHIRServerTestBase {
    private Patient savedCreatedPatient = null;
    private Practitioner savedCreatedPractitioner = null;
    private Observation savedCreatedObservation = null;
    private Condition savedCreatedCondition = null;
    private AllergyIntolerance savedCreatedAllergyIntolerance = null;
    private Composition savedCreatedComposition = null;

    public FHIRDocumentOperationTest() {
        DEBUG_JSON = false;
    }

    @Test(groups = { "fhir-operation" })
    public void testCreatePractitioner() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Practitioner and then call the 'create' API.
        Practitioner practitioner = readResource(Practitioner.class, "Practitioner.json");
        Entity<Practitioner> entity = Entity.entity(practitioner, MediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Practitioner").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the practitioner's logical id value.
        String practitionerId = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new practitioner and verify it.
        response = target.path("Practitioner/" + practitionerId).request(MediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Practitioner reponsePractitioner = response.readEntity(Practitioner.class);
        savedCreatedPractitioner = reponsePractitioner;

        assertResourceEquals(practitioner, reponsePractitioner);
    }

    @Test(groups = { "fhir-operation" }, dependsOnMethods = { "testCreatePractitioner" })
    public void testCreatePatient() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient and then call the 'create' API.
        String practitionerId = savedCreatedPractitioner.getId().getValue();
        Patient patient = readResource(Patient.class, "Patient_JohnDoe.json");

        patient = patient.toBuilder()
                .generalPractitioner(Reference.builder().reference(string("Practitioner/" + practitionerId)).build())
                .build();

        Entity<Patient> entity = Entity.entity(patient, MediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Patient").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the patient's logical id value.
        String patientId = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new patient and verify it.
        response = target.path("Patient/" + patientId).request(MediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Patient responsePatient = response.readEntity(Patient.class);
        savedCreatedPatient = responsePatient;

        assertResourceEquals(patient, responsePatient);
    }

    @Test(groups = { "fhir-operation" }, dependsOnMethods = { "testCreatePatient" })
    public void testCreateObservation() throws Exception {
        WebTarget target = getWebTarget();

        // Next, create an Observation belonging to the new patient.
        String patientId = savedCreatedPatient.getId().getValue();
        Observation observation = buildObservation(patientId, "Observation1.json");
        Entity<Observation> obs = Entity.entity(observation, MediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Observation").request().post(obs, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the observation's logical id value.
        String observationId = getLocationLogicalId(response);

        // Next, retrieve the new Observation with a read operation and verify it.
        response = target.path("Observation/" + observationId).request(MediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Observation responseObs = response.readEntity(Observation.class);
        savedCreatedObservation = responseObs;

        assertResourceEquals(observation, responseObs);
    }

    @Test(groups = { "fhir-operation" }, dependsOnMethods = { "testCreatePatient" })
    public void testCreateCondition() throws Exception {
        WebTarget target = getWebTarget();

        // Next, create a Condition belonging to the new patient.
        String patientId = savedCreatedPatient.getId().getValue();
        Condition condition = buildCondition(patientId, "Condition.json");
        Entity<Condition> obs = Entity.entity(condition, MediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Condition").request().post(obs, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the condition's logical id value.
        String conditionId = getLocationLogicalId(response);

        // Next, retrieve the new Condition with a read operation and verify it.
        response = target.path("Condition/" + conditionId).request(MediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Condition responseCondition = response.readEntity(Condition.class);
        savedCreatedCondition = responseCondition;

        assertResourceEquals(condition, responseCondition);
    }

    @Test(groups = { "fhir-operation" }, dependsOnMethods = { "testCreatePatient" })
    public void testCreateAllergyIntolerance() throws Exception {
        WebTarget target = getWebTarget();

        // Next, create a AllergyIntolerance belonging to the new patient.
        String patientId = savedCreatedPatient.getId().getValue();
        AllergyIntolerance allergyIntolerance = buildAllergyIntolerance(patientId, "AllergyIntolerance.json");
        Entity<AllergyIntolerance> obs = Entity.entity(allergyIntolerance, MediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("AllergyIntolerance").request().post(obs, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the allergy intolerance's logical id value.
        String allergyIntoleranceId = getLocationLogicalId(response);

        // Next, retrieve the new AllergyIntolerance with a read operation and verify
        // it.
        response = target.path("AllergyIntolerance/" + allergyIntoleranceId).request(MediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        AllergyIntolerance responseAllergyIntolerance = response.readEntity(AllergyIntolerance.class);
        savedCreatedAllergyIntolerance = responseAllergyIntolerance;

        if (DEBUG_JSON) {
            System.out.println("allergyIntolernace: ");
            FHIRUtil.write(allergyIntolerance, Format.JSON, System.out);

            System.out.println("responseAllergyIntolerance:  ");
            FHIRUtil.write(responseAllergyIntolerance, Format.JSON, System.out);
        }

        assertResourceEquals(allergyIntolerance, responseAllergyIntolerance);
    }

    @Test(groups = { "fhir-operation" }, dependsOnMethods = { "testCreateObservation" })
    public void testCreateComposition() throws Exception {
        String practitionerId = savedCreatedPractitioner.getId().getValue();
        String patientId = savedCreatedPatient.getId().getValue();
        String observationId = savedCreatedObservation.getId().getValue();
        String conditionId = savedCreatedCondition.getId().getValue();
        String allergyIntoleranceId = savedCreatedAllergyIntolerance.getId().getValue();

        WebTarget target = getWebTarget();

        // Build a new Composition and then call the 'create' API.
        Composition composition = buildComposition(practitionerId, patientId, observationId, conditionId,
                allergyIntoleranceId);
        Entity<Composition> entity = Entity.entity(composition, MediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Composition").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the composition's logical id value.
        String compositionId = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new composition and verify it.
        response = target.path("Composition/" + compositionId).request(MediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Composition responseComposition = response.readEntity(Composition.class);
        savedCreatedComposition = responseComposition;

        assertResourceEquals(composition, responseComposition);
    }

    private Composition buildComposition(String practitionerId, String patientId, String observationId,
            String conditionId, String allergyIntoleranceId) {
        List<Reference> authorList = new ArrayList<Reference>();
        authorList.add(Reference.builder().reference(string("Practitioner/" + practitionerId)).build());
        Composition composition = Composition
                .builder(CompositionStatus.FINAL,
                        CodeableConcept.builder()
                                .coding(Coding.builder().code(Code.of("somecode-1234"))
                                        .system(Uri.of("http://somesystem.org")).build())
                                .build(),
                        DateTime.of("2015-02-14T13:42:00+10:00"), authorList, string("This is the title"))
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

        String compositionId = savedCreatedComposition.getId().getValue();
        Response response = target.path("Composition/" + compositionId + "/$document").request().get(Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());

        Bundle document = response.readEntity(Bundle.class);

        assertNotNull(document);
        assertTrue(document.getEntry().size() == 6);

        Composition composition = (Composition) document.getEntry().get(0).getResource();
        assertResourceEquals(savedCreatedComposition, composition);

        Patient patient = (Patient) document.getEntry().get(1).getResource();
        assertResourceEquals(savedCreatedPatient, patient);

        Practitioner practitioner = (Practitioner) document.getEntry().get(2).getResource();
        assertResourceEquals(savedCreatedPractitioner, practitioner);

        Observation observation = (Observation) document.getEntry().get(3).getResource();
        assertResourceEquals(savedCreatedObservation, observation);

        Condition condition = (Condition) document.getEntry().get(4).getResource();
        assertResourceEquals(savedCreatedCondition, condition);

        AllergyIntolerance allergyIntolerance = (AllergyIntolerance) document.getEntry().get(5).getResource();
        assertResourceEquals(savedCreatedAllergyIntolerance, allergyIntolerance);
    }

    @Test(groups = { "fhir-operation" }, dependsOnMethods = { "testCreateComposition" })
    public void testDocumentOperationWithPersistence() throws Exception {
        WebTarget target = getWebTarget();

        String compositionId = savedCreatedComposition.getId().getValue();
        Response response = target.path("Composition/" + compositionId + "/$document").queryParam("persist", "true")
                .request().get(Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());

        String documentId = getLocationLogicalId(response);

        Bundle document = response.readEntity(Bundle.class);

        assertEquals(documentId, document.getId().getValue());

        assertNotNull(document);
        assertTrue(document.getEntry().size() == 6);

        Composition composition = (Composition) document.getEntry().get(0).getResource();
        assertResourceEquals(savedCreatedComposition, composition);

        Patient patient = (Patient) document.getEntry().get(1).getResource();
        assertResourceEquals(savedCreatedPatient, patient);

        Practitioner practitioner = (Practitioner) document.getEntry().get(2).getResource();
        assertResourceEquals(savedCreatedPractitioner, practitioner);

        Observation observation = (Observation) document.getEntry().get(3).getResource();
        assertResourceEquals(savedCreatedObservation, observation);

        Condition condition = (Condition) document.getEntry().get(4).getResource();
        assertResourceEquals(savedCreatedCondition, condition);

        AllergyIntolerance allergyIntolerance = (AllergyIntolerance) document.getEntry().get(5).getResource();
        assertResourceEquals(savedCreatedAllergyIntolerance, allergyIntolerance);

        target = getWebTarget();
        response = target.path("Bundle/" + documentId).request().get(Response.class);

        Bundle responseDocument = response.readEntity(Bundle.class);
        assertResourceEquals(responseDocument, document);
    }

    private Condition buildCondition(String patientId, String fileName) throws Exception {
        Condition condition = readResource(Condition.class, fileName);
        condition = condition.toBuilder().subject(Reference.builder().reference(string("Patient/" + patientId)).build()).build();

        return condition;
    }

    private AllergyIntolerance buildAllergyIntolerance(String patientId, String fileName) throws Exception {
        AllergyIntolerance allergyIntolerance = readResource(AllergyIntolerance.class, fileName);

        allergyIntolerance = allergyIntolerance
                .toBuilder().patient(Reference.builder().reference(string("Patient/" + patientId)).build()).build();
        return allergyIntolerance;
    }
}
