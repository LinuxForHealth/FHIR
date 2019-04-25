/**
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.server.test;

import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.codeableConcept;
import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.dateTime;
import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.reference;
import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.string;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import com.ibm.watsonhealth.fhir.client.FHIRClient;
import com.ibm.watsonhealth.fhir.client.FHIRParameters;
import com.ibm.watsonhealth.fhir.client.FHIRResponse;
import com.ibm.watsonhealth.fhir.core.MediaType;
import com.ibm.watsonhealth.fhir.model.AllergyIntolerance;
import com.ibm.watsonhealth.fhir.model.Bundle;
import com.ibm.watsonhealth.fhir.model.Composition;
import com.ibm.watsonhealth.fhir.model.CompositionSection;
import com.ibm.watsonhealth.fhir.model.CompositionStatusList;
import com.ibm.watsonhealth.fhir.model.Condition;
import com.ibm.watsonhealth.fhir.model.Observation;
import com.ibm.watsonhealth.fhir.model.OperationOutcome;
import com.ibm.watsonhealth.fhir.model.Parameters;
import com.ibm.watsonhealth.fhir.model.ParametersParameter;
import com.ibm.watsonhealth.fhir.model.Patient;
import com.ibm.watsonhealth.fhir.model.Practitioner;
import com.ibm.watsonhealth.fhir.model.ResourceContainer;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil;

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
        Practitioner practitioner = readResource(Practitioner.class, "Practitioner.json");
        Entity<Practitioner> entity = Entity.entity(practitioner, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.path("Practitioner").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());
                
        // Get the practitioner's logical id value.
        String practitionerId = getLocationLogicalId(response);
        
        // Next, call the 'read' API to retrieve the new practitioner and verify it.
        response = target.path("Practitioner/" + practitionerId).request(MediaType.APPLICATION_JSON_FHIR).get();
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
        patient.withCareProvider(reference("Practitioner/" + practitionerId));
        
        Entity<Patient> entity = Entity.entity(patient, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.path("Patient").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());
                
        // Get the patient's logical id value.
        String patientId = getLocationLogicalId(response);
        
        // Next, call the 'read' API to retrieve the new patient and verify it.
        response = target.path("Patient/" + patientId).request(MediaType.APPLICATION_JSON_FHIR).get();
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
        Entity<Observation> obs = Entity.entity(observation, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.path("Observation").request().post(obs, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());
     
        // Get the observation's logical id value.
        String observationId = getLocationLogicalId(response);
    
        // Next, retrieve the new Observation with a read operation and verify it.
        response = target.path("Observation/" + observationId).request(MediaType.APPLICATION_JSON_FHIR).get();
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
        Entity<Condition> obs = Entity.entity(condition, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.path("Condition").request().post(obs, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());
     
        // Get the condition's logical id value.
        String conditionId = getLocationLogicalId(response);
    
        // Next, retrieve the new Condition with a read operation and verify it.
        response = target.path("Condition/" + conditionId).request(MediaType.APPLICATION_JSON_FHIR).get();
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
        Entity<AllergyIntolerance> obs = Entity.entity(allergyIntolerance, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.path("AllergyIntolerance").request().post(obs, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());
     
        // Get the allergy intolerance's logical id value.
        String allergyIntoleranceId = getLocationLogicalId(response);
    
        // Next, retrieve the new AllergyIntolerance with a read operation and verify it.
        response = target.path("AllergyIntolerance/" + allergyIntoleranceId).request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        AllergyIntolerance responseAllergyIntolerance = response.readEntity(AllergyIntolerance.class);
        savedCreatedAllergyIntolerance = responseAllergyIntolerance;
        
        assertResourceEquals(allergyIntolerance, responseAllergyIntolerance);        
    }
    
    @Test(groups = { "fhir-operation" }, dependsOnMethods = { "testCreateObservation" } )
    public void testCreateComposition() throws Exception {
        String practitionerId = savedCreatedPractitioner.getId().getValue();
        String patientId = savedCreatedPatient.getId().getValue();
        String observationId = savedCreatedObservation.getId().getValue();
        String conditionId = savedCreatedCondition.getId().getValue();
        String allergyIntoleranceId = savedCreatedAllergyIntolerance.getId().getValue();
        
        WebTarget target = getWebTarget();
        
        // Build a new Composition and then call the 'create' API.
        Composition composition = buildComposition(practitionerId, patientId, observationId, conditionId, allergyIntoleranceId);
        Entity<Composition> entity = Entity.entity(composition, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.path("Composition").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());
                
        // Get the composition's logical id value.
        String compositionId = getLocationLogicalId(response);
        
        // Next, call the 'read' API to retrieve the new composition and verify it.
        response = target.path("Composition/" + compositionId).request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Composition responseComposition = response.readEntity(Composition.class);
        savedCreatedComposition = responseComposition;
        
        assertResourceEquals(composition, responseComposition);
    }
    
    private Composition buildComposition(String practitionerId, String patientId, String observationId, String conditionId, String allergyIntoleranceId) {
        Composition composition = objFactory.createComposition();
        
        composition.setDate(dateTime("2015-02-14T13:42:00+10:00"));
        composition.setType(codeableConcept("http://somesystem.org", "somecode-1234"));
        composition.setTitle(string("This is the title"));
        composition.setStatus(objFactory.createCompositionStatus().withValue(CompositionStatusList.FINAL));
        
        composition.setSubject(reference("Patient/" + patientId));
        composition.getAuthor().add(reference("Practitioner/" + practitionerId));
        
        CompositionSection section = objFactory.createCompositionSection();
        section.getEntry().add(reference("Observation/" + observationId));
        composition.getSection().add(section);
        
        CompositionSection subSection = objFactory.createCompositionSection();
        subSection.getEntry().add(reference("Condition/" + conditionId));
        section.getSection().add(subSection);

        CompositionSection subSubSection = objFactory.createCompositionSection();
        subSubSection.getEntry().add(reference("AllergyIntolerance/" + allergyIntoleranceId));
        subSection.getSection().add(subSubSection);
        
        return composition;
    }
    
    private Condition buildCondition(String patientId, String fileName) throws Exception {
        Condition condition = readResource(Condition.class, fileName)
            .withPatient(reference("Patient/" + patientId));
        return condition;
    }
    
    private AllergyIntolerance buildAllergyIntolerance(String patientId, String fileName) throws Exception {
        AllergyIntolerance allergyIntolerance = readResource(AllergyIntolerance.class, fileName)
            .withPatient(reference("Patient/" + patientId));
        return allergyIntolerance;
    }

    @Test(groups = { "fhir-operation" })
    //Testcase for "POST [baseUrl]/${operationName}"
    public void testPostHelloOperation() throws Exception {
        String message = "Hello, World!";
        
        Parameters parameters = objFactory.createParameters();
        ParametersParameter parameter = objFactory.createParametersParameter();
        parameter.setName(string("input"));
        parameter.setValueString(string(message));
        parameters.getParameter().add(parameter);
        
        FHIRClient client = getFHIRClient();
        FHIRResponse response = client.invoke("$hello", parameters);
        
        Parameters output = response.getResource(Parameters.class);
        
        assertNotNull(output);
        assertEquals(output.getParameter().get(0).getValueString().getValue(), message);
    }
    
    @Test(groups = { "fhir-operation" })
    //Testcase for GET [baseUrl]/${operationName}?input="Hello, World!"
    public void testGetHelloOperation() throws Exception {
        String message = "Hello, World!";
        
        FHIRParameters parameters = new FHIRParameters();
        parameters.queryParam("input", message);
        
        FHIRClient client = getFHIRClient();
        FHIRResponse response = client.invoke("$hello", parameters);
        
        Parameters output = response.getResource(Parameters.class);
        
        assertNotNull(output);
        assertEquals(output.getParameter().get(0).getValueString().getValue(), message);
    }
    
    @Test(groups = { "fhir-operation" })
    //Testcase for "POST [baseUrl]/{resourceTypeName}/${operationName}"
    public void testPostRscValidateOperation() throws Exception {
        Patient patient = readResource(Patient.class, "Patient_JohnDoe.json");
        
        ResourceContainer rsc = objFactory.createResourceContainer();
        rsc.setPatient(patient);
        
        Parameters parameters = objFactory.createParameters();
        ParametersParameter parameter = objFactory.createParametersParameter();
        parameter.setName(string("resource"));
        parameter.setResource(rsc);
        parameters.getParameter().add(parameter);
        
        FHIRClient client = getFHIRClient();
        FHIRResponse response = client.invoke("Patient", "$validate", parameters);
        
        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
        OperationOutcome operationOutcome = response.getResource(OperationOutcome.class);
        String text = operationOutcome.getIssue().get(0).getDetails().getText().getValue();
        assertEquals("All OK", text);
    }
    
    @Test(groups = { "fhir-operation" })
    //Testcase for "GET [baseUrl]/{resourceTypeName}/${operationName}?resource=null"
    public void testGetRscValidateOperation() throws Exception {
        Patient patient = readResource(Patient.class, "Patient_JohnDoe.json");
        
        ResourceContainer rsc = objFactory.createResourceContainer();
        rsc.setPatient(patient);
        
        FHIRParameters parameters = new FHIRParameters();
        parameters.queryParam("resource", null);
        
        FHIRClient client = getFHIRClient();
        FHIRResponse response = client.invoke("Patient", "$validate", parameters);
        
        assertEquals(response.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());
        OperationOutcome operationOutcome = response.getResource(OperationOutcome.class);
        String text = operationOutcome.getIssue().get(0).getDiagnostics().getValue();
        if(text.contains("Input parameter 'resource' is required")) {
            assertTrue(true);    //Force assertion to true
        } else {
            assertTrue(false);    //Force assertion to false
        }
    }
    
    @Test(groups = { "fhir-operation" }, dependsOnMethods = { "testCreatePractitioner", "testCreatePatient" })
    //Testcase for "POST [baseUrl]/{resourceTypeName}/{logicalId}/${operationName}"
    public void testPostRscIdValidateOperation() throws Exception {
        ResourceContainer rsc = objFactory.createResourceContainer();
        rsc.setPatient(savedCreatedPatient);
        
        Parameters parameters = objFactory.createParameters();
        ParametersParameter parameter = objFactory.createParametersParameter();
        parameter.setName(string("resource"));
        parameter.setResource(rsc);
        parameters.getParameter().add(parameter);
        
        FHIRClient client = getFHIRClient();
        FHIRResponse response = client.invoke("Patient", "$validate", savedCreatedPatient.getId().getValue(), parameters);
        
        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
        OperationOutcome operationOutcome = response.getResource(OperationOutcome.class);
        String text = operationOutcome.getIssue().get(0).getDetails().getText().getValue();
        assertEquals("All OK", text);
    }
    
    @Test(groups = { "fhir-operation" }, dependsOnMethods = { "testCreateComposition" })
    //Testcase for "GET [baseUrl]/{resourceTypeName}/{logicalId}/${operationName}?persist=true"
    public void testGetRscIdDocumentOperation() throws Exception {
        String compositionId = savedCreatedComposition.getId().getValue();
        
        FHIRParameters parameters = new FHIRParameters();
        parameters.queryParam("persist", "true");
        
        FHIRClient client = getFHIRClient();
        FHIRResponse response = client.invoke("Composition", "$document", compositionId, parameters);
        
        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());

        Bundle document = response.getResource(Bundle.class);
        
        assertNotNull(document);
        assertTrue(document.getEntry().size() == 6);
        
        Composition composition = (Composition) FHIRUtil.getResourceContainerResource(document.getEntry().get(0).getResource());
        assertResourceEquals(savedCreatedComposition, composition);
        
        Patient patient = (Patient) FHIRUtil.getResourceContainerResource(document.getEntry().get(1).getResource());
        assertResourceEquals(savedCreatedPatient, patient);
        
        Practitioner practitioner = (Practitioner) FHIRUtil.getResourceContainerResource(document.getEntry().get(2).getResource());
        assertResourceEquals(savedCreatedPractitioner, practitioner);
        
        Observation observation = (Observation) FHIRUtil.getResourceContainerResource(document.getEntry().get(3).getResource());
        assertResourceEquals(savedCreatedObservation, observation);
        
        Condition condition = (Condition) FHIRUtil.getResourceContainerResource(document.getEntry().get(4).getResource());
        assertResourceEquals(savedCreatedCondition, condition);
        
        AllergyIntolerance allergyIntolerance = (AllergyIntolerance) FHIRUtil.getResourceContainerResource(document.getEntry().get(5).getResource());
        assertResourceEquals(savedCreatedAllergyIntolerance, allergyIntolerance);
    }
    
    @Test(groups = { "fhir-operation" }, dependsOnMethods = { "testCreatePatient" })
    //Testcase for "POST [baseUrl]/{resourceTypeName}/{logicalId}/_history/{versionId}/${operationName}"
    public void testPostRscIdVersionValidateOperation() throws Exception {
        ResourceContainer rsc = objFactory.createResourceContainer();
        rsc.setPatient(savedCreatedPatient);
        
        Parameters parameters = objFactory.createParameters();
        ParametersParameter parameter = objFactory.createParametersParameter();
        parameter.setName(string("resource"));
        parameter.setResource(rsc);
        parameters.getParameter().add(parameter);
        
        FHIRClient client = getFHIRClient();
        FHIRResponse response = client.invoke("Patient", "$validate", savedCreatedPatient.getId().getValue(), "1", parameters);
        
        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
        OperationOutcome operationOutcome = response.getResource(OperationOutcome.class);
        String text = operationOutcome.getIssue().get(0).getDetails().getText().getValue();
        assertEquals("All OK", text);
    }
    
    @Test(groups = { "fhir-operation" }, dependsOnMethods = { "testCreateComposition" })
    //Testcase for "GET [baseUrl]/{resourceTypeName}/{logicalId}/_history/{versionId}/${operationName}?persist=true"
    public void testPostRscIdVersionDocumentOperation() throws Exception {
        String compositionId = savedCreatedComposition.getId().getValue();
        
        FHIRParameters parameters = new FHIRParameters();
        parameters.queryParam("persist", "true");
        
        FHIRClient client = getFHIRClient();
        FHIRResponse response = client.invoke("Composition", "$document", compositionId, "1", parameters);
        
        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());

        Bundle document = response.getResource(Bundle.class);
        
        assertNotNull(document);
        assertTrue(document.getEntry().size() == 6);
        
        Composition composition = (Composition) FHIRUtil.getResourceContainerResource(document.getEntry().get(0).getResource());
        assertResourceEquals(savedCreatedComposition, composition);
        
        Patient patient = (Patient) FHIRUtil.getResourceContainerResource(document.getEntry().get(1).getResource());
        assertResourceEquals(savedCreatedPatient, patient);
        
        Practitioner practitioner = (Practitioner) FHIRUtil.getResourceContainerResource(document.getEntry().get(2).getResource());
        assertResourceEquals(savedCreatedPractitioner, practitioner);
        
        Observation observation = (Observation) FHIRUtil.getResourceContainerResource(document.getEntry().get(3).getResource());
        assertResourceEquals(savedCreatedObservation, observation);
        
        Condition condition = (Condition) FHIRUtil.getResourceContainerResource(document.getEntry().get(4).getResource());
        assertResourceEquals(savedCreatedCondition, condition);
        
        AllergyIntolerance allergyIntolerance = (AllergyIntolerance) FHIRUtil.getResourceContainerResource(document.getEntry().get(5).getResource());
        assertResourceEquals(savedCreatedAllergyIntolerance, allergyIntolerance);
    }
}
