/**
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.server.test;

import static org.testng.Assert.assertNotEquals;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.math.BigInteger;
import java.net.URI;
import java.util.UUID;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;

import org.testng.annotations.Test;

import com.ibm.watsonhealth.fhir.client.FHIRParameters;
import com.ibm.watsonhealth.fhir.client.FHIRResponse;
import com.ibm.watsonhealth.fhir.core.MediaType;
import com.ibm.watsonhealth.fhir.model.Bundle;
import com.ibm.watsonhealth.fhir.model.BundleTypeList;
import com.ibm.watsonhealth.fhir.model.Observation;
import com.ibm.watsonhealth.fhir.model.OperationOutcome;
import com.ibm.watsonhealth.fhir.model.Patient;

/**
 * This class tests the REST API's compliance with the FHIR spec in terms of status code and OperationOutcome responses,
 * etc.
 */
public class ServerSpecTest extends FHIRServerTestBase {
    private Patient savedPatient;
    @SuppressWarnings("unused")
    private Observation savedObservation;

    @Test(groups = { "server-spec" })
    public void testCreatePatient() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient and then call the 'create' API.
        Patient patient = readResource(Patient.class, "Patient_JohnDoe.json");
        Entity<Patient> entity = Entity.entity(patient, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.path("Patient").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());
        URI location = response.getLocation();
        assertNotNull(location);
        assertNotNull(location.toString());
        assertFalse(location.toString().isEmpty());

        // Get the patient's logical id value.
        String patientId = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new patient and verify it.
        response = target.path("Patient/" + patientId).request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Patient responsePatient = response.readEntity(Patient.class);
        assertNotNull(responsePatient);
        savedPatient = responsePatient;
    }

    // Test: create a new patient that contains an id
    @Test(groups = { "server-spec" })
    public void testCreatePatientErrorHasId() throws Exception {
        WebTarget target = getWebTarget();
        Patient patient = readResource(Patient.class, "Patient_JohnDoe.json");
        // Set an id on the patient.
        patient.setId(getObjectFactory().createId().withValue("1"));
        
        Entity<Patient> entity = Entity.entity(patient, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.path("Patient").request().post(entity, Response.class);
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
        assertExceptionOperationOutcome(response.readEntity(OperationOutcome.class), "FHIRException:");
    }

    // Test: create an invalid patient
    @Test(groups = { "server-spec" })
    public void testCreatePatientErrorInvalidResource() throws JAXBException {
        WebTarget target = getWebTarget();
        Patient patient = getObjectFactory().createPatient();
        Entity<Patient> entity = Entity.entity(patient, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.path("Patient").request().post(entity, Response.class);
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
        assertValidationOperationOutcome(response.readEntity(OperationOutcome.class), "global-1");
    }

    @Test(groups = { "server-spec" }, dependsOnMethods={"testCreatePatient"})
    public void testUpdatePatient() throws Exception {
        WebTarget target = getWebTarget();

        // First, call the 'read' API to retrieve the previously-created patient.
        assertNotNull(savedPatient);
        Response response = target.path("Patient/" + savedPatient.getId().getValue()).request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Patient patient = response.readEntity(Patient.class);
        assertNotNull(patient);
        
        // Modify the patient.
        patient.withGender(getObjectFactory().createCode().withValue("male"));
        
        // Next, update the patient and verify the response.
        Entity<Patient> entity = Entity.entity(patient, MediaType.APPLICATION_JSON_FHIR);
        response = target.path("Patient/" + patient.getId().getValue()).request().put(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        URI location = response.getLocation();
        assertNotNull(location);
        assertNotNull(location.toString());
        assertFalse(location.toString().isEmpty());
    }

    @Test(groups = { "server-spec" }, dependsOnMethods={"testUpdatePatient"})
    public void testUpdatePatientVersionAware() throws Exception {
        WebTarget target = getWebTarget();

        // First, call the 'read' API to retrieve the previously-created patient.
        assertNotNull(savedPatient);
        Response response = target.path("Patient/" + savedPatient.getId().getValue()).request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Patient patient = response.readEntity(Patient.class);
        assertNotNull(patient);
        
        // Modify the patient.
        patient
            .withName(getObjectFactory().createHumanName()
                .withGiven(getObjectFactory().createString().withValue("Jane"))
                .withFamily(getObjectFactory().createString().withValue("Doe")))
            .withGender(getObjectFactory().createCode().withValue("female"));
        
        // Next, update the patient and verify the response.
        String ifMatchValue = "W/\"" + patient.getMeta().getVersionId().getValue() + "\"";
        Entity<Patient> entity = Entity.entity(patient, MediaType.APPLICATION_JSON_FHIR);
        response = target.path("Patient/" + patient.getId().getValue())
                        .request()
                        .header("If-Match", ifMatchValue)
                        .put(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        URI location = response.getLocation();
        assertNotNull(location);
        assertNotNull(location.toString());
        assertFalse(location.toString().isEmpty());
    }

    @Test(groups = { "server-spec" }, dependsOnMethods={"testUpdatePatient"})
    public void testUpdatePatientVersionAwareError1() throws Exception {
        WebTarget target = getWebTarget();

        // First, call the 'read' API to retrieve the previously-created patient.
        assertNotNull(savedPatient);
        Response response = target.path("Patient/" + savedPatient.getId().getValue()).request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Patient patient = response.readEntity(Patient.class);
        assertNotNull(patient);
        
        // Modify the patient.
        patient
            .withName(getObjectFactory().createHumanName()
                .withGiven(getObjectFactory().createString().withValue("Jane"))
                .withFamily(getObjectFactory().createString().withValue("Doe")))
            .withGender(getObjectFactory().createCode().withValue("female"));
        
        // Next, update the patient and verify the response.
        // We'll use an incorrect value for the If-Match header (no W/" and " surrounding version id).
        String ifMatchValue = patient.getMeta().getVersionId().getValue();
        Entity<Patient> entity = Entity.entity(patient, MediaType.APPLICATION_JSON_FHIR);
        response = target.path("Patient/" + patient.getId().getValue())
                        .request()
                        .header("If-Match", ifMatchValue)
                        .put(entity, Response.class);
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
        assertExceptionOperationOutcome(response.readEntity(OperationOutcome.class), "FHIRRestException:");
    }

    @Test(groups = { "server-spec" }, dependsOnMethods={"testUpdatePatient"})
    public void testUpdatePatientVersionAwareError2() throws Exception {
        WebTarget target = getWebTarget();

        // First, call the 'read' API to retrieve the previously-created patient.
        assertNotNull(savedPatient);
        Response response = target.path("Patient/" + savedPatient.getId().getValue()).request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Patient patient = response.readEntity(Patient.class);
        assertNotNull(patient);
        
        // Modify the patient.
        patient
            .withName(getObjectFactory().createHumanName()
                .withGiven(getObjectFactory().createString().withValue("Jane"))
                .withFamily(getObjectFactory().createString().withValue("Doe")))
            .withGender(getObjectFactory().createCode().withValue("female"));
        
        // Next, update the patient and verify the response.
        // We'll use an incorrect value for the If-Match header (no " around version id).
        String ifMatchValue = "W/" + patient.getMeta().getVersionId().getValue();
        Entity<Patient> entity = Entity.entity(patient, MediaType.APPLICATION_JSON_FHIR);
        response = target.path("Patient/" + patient.getId().getValue())
                        .request()
                        .header("If-Match", ifMatchValue)
                        .put(entity, Response.class);
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
        assertExceptionOperationOutcome(response.readEntity(OperationOutcome.class), "FHIRRestException:");
    }

    @Test(groups = { "server-spec" }, dependsOnMethods={"testUpdatePatient"})
    public void testUpdatePatientVersionAwareError3() throws Exception {
        WebTarget target = getWebTarget();

        // First, call the 'read' API to retrieve the previously-created patient.
        assertNotNull(savedPatient);
        Response response = target.path("Patient/" + savedPatient.getId().getValue()).request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Patient patient = response.readEntity(Patient.class);
        assertNotNull(patient);
        
        // Modify the patient.
        patient
            .withName(getObjectFactory().createHumanName()
                .withGiven(getObjectFactory().createString().withValue("Jane"))
                .withFamily(getObjectFactory().createString().withValue("Doe")))
            .withGender(getObjectFactory().createCode().withValue("female"));
        
        // Next, update the patient and verify the response.
        // We'll use an incorrect value for the If-Match header (incorrect version #).
        String ifMatchValue = "W/\"1\"";
        Entity<Patient> entity = Entity.entity(patient, MediaType.APPLICATION_JSON_FHIR);
        response = target.path("Patient/" + patient.getId().getValue())
                        .request()
                        .header("If-Match", ifMatchValue)
                        .put(entity, Response.class);
        assertResponse(response, Response.Status.CONFLICT.getStatusCode());
        assertExceptionOperationOutcome(response.readEntity(OperationOutcome.class), "FHIRRestException:");
    }

    @Test(groups = { "server-spec" }, dependsOnMethods={"testCreatePatient"})
    public void testCreateObservation() throws Exception {
        WebTarget target = getWebTarget();
        
        // Next, create an Observation belonging to the new patient.
        String patientId = savedPatient.getId().getValue();
        Observation observation = buildObservation(patientId, "Observation1.json");
        Entity<Observation> obs = Entity.entity(observation, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.path("Observation").request().post(obs, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());
     
        String observationId = getLocationLogicalId(response);
        response = target.path("Observation/" + observationId).request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Observation responseObs = response.readEntity(Observation.class);
        savedObservation = responseObs;
    }    
    
    // Test: create an invalid observation
    @Test(groups = { "server-spec" })
    public void testCreateObservationErrorInvalidResource() throws JAXBException {
        WebTarget target = getWebTarget();
        Observation observation = getObjectFactory().createObservation();
        Entity<Observation> entity = Entity.entity(observation, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.path("Observation").request().post(entity, Response.class);
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
        assertValidationOperationOutcome(response.readEntity(OperationOutcome.class), "global-1");
    }

    // Test: include incorrect resource type in request body.
    @Test(groups = { "server-spec" })
    public void testCreatePatientErrorInvalidResourceType() throws JAXBException {
        WebTarget target = getWebTarget();

        // Build an Observation, then try to call the 'create patient' API.
        Observation observation = getObjectFactory().createObservation();
        Entity<Observation> entity = Entity.entity(observation, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.path("Patient").request().post(entity, Response.class);
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
        assertExceptionOperationOutcome(response.readEntity(OperationOutcome.class), "FHIRException:");
    }

    // Test: include incorrect resource type in request body.
    @Test(groups = { "server-spec" })
    public void testCreateObservationErrorInvalidResourceType() throws JAXBException {
        WebTarget target = getWebTarget();

        // Build an Observation, then try to call the 'create patient' API.
        Patient patient = getObjectFactory().createPatient();
        Entity<Patient> entity = Entity.entity(patient, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.path("Observation").request().post(entity, Response.class);
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
        assertExceptionOperationOutcome(response.readEntity(OperationOutcome.class), "FHIRException:");
    }

    // Test: retrieve non-existent Patient.
    @Test(groups = { "server-spec" })
    public void testReadPatientErrorNotFound() {
        WebTarget target = getWebTarget();
        Response response = target.path("Patient/123456789ABCDEF").request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(response, Response.Status.NOT_FOUND.getStatusCode());
        assertExceptionOperationOutcome(response.readEntity(OperationOutcome.class), "FHIRPersistenceResourceNotFoundException");
    }

    // Test: retrieve non-extent Observation.
    @Test(groups = { "server-spec" })
    public void testReadObservationErrorNotFound() {
        WebTarget target = getWebTarget();
        Response response = target.path("Observation/123456789ABCDEF").request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(response, Response.Status.NOT_FOUND.getStatusCode());
        assertExceptionOperationOutcome(response.readEntity(OperationOutcome.class), "FHIRPersistenceResourceNotFoundException");
    }

    // Test: retrieve non-existent MedicationDdministration
    @Test(groups = { "server-spec" })
    public void testReadMedicationAdministrationErrorNotFound() {
        WebTarget target = getWebTarget();

        // Try to retrieve a bogus MedicationAdministration.
        Response response = target.path("MedicationAdministration/123456789ABCDEF").request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(response, Response.Status.NOT_FOUND.getStatusCode());
        assertExceptionOperationOutcome(response.readEntity(OperationOutcome.class), "FHIRPersistenceResourceNotFoundException");
    }

    // Test: retrieve invalid resource type.
    @Test(groups = { "server-spec" })
    public void testReadErrorInvalidResourceType() {
        WebTarget target = getWebTarget();
        Response response = target.path("BogusResourceType/1").request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
        assertExceptionOperationOutcome(response.readEntity(OperationOutcome.class), "FHIRVirtualResourceTypeException");
    }

    // Test: retrieve non-existent Patient.
    @Test(groups = { "server-spec" })
    public void testVReadPatientErrorNotFound() {
        WebTarget target = getWebTarget();
        Response response = target.path("Patient/123456789ABCDEF/_history/1").request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(response, Response.Status.NOT_FOUND.getStatusCode());
        assertExceptionOperationOutcome(response.readEntity(OperationOutcome.class), "FHIRPersistenceResourceNotFoundException");
    }

    // Test: retrieve non-existent Observation.
    @Test(groups = { "server-spec" })
    public void testVReadObservationErrorNotFound() {
        WebTarget target = getWebTarget();
        Response response = target.path("Observation/123456789ABCDEF/_history/1").request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(response, Response.Status.NOT_FOUND.getStatusCode());
        assertExceptionOperationOutcome(response.readEntity(OperationOutcome.class), "FHIRPersistenceResourceNotFoundException");
    }

    // Test: retrieve non-existent MedicationAdministration.
    @Test(groups = { "server-spec" })
    public void testVReadMedicationAdministrationErrorNotFound() {
        WebTarget target = getWebTarget();
        Response response = target.path("MedicationAdministration/123456789ABCDEF/_history/1").request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(response, Response.Status.NOT_FOUND.getStatusCode());
        assertExceptionOperationOutcome(response.readEntity(OperationOutcome.class), "FHIRPersistenceResourceNotFoundException");
    }

    // Test: retrieve invalid resource type.
    @Test(groups = { "server-spec" })
    public void testVReadInvalidResourceType() {
        WebTarget target = getWebTarget();
        Response response = target.path("BogusResourceType/1/_history/1").request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
        assertExceptionOperationOutcome(response.readEntity(OperationOutcome.class), "FHIRVirtualResourceTypeException");
    }

    // Test: retrieve invalid version.
    @Test(groups = { "server-spec" }, dependsOnMethods = { "testCreatePatient" })
    public void testVReadInvalidVersion() {
        WebTarget target = getWebTarget();
        Response response = target.path("Patient/" + savedPatient.getId().getValue() + "/_history/-1").request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(response, Response.Status.NOT_FOUND.getStatusCode());
        assertExceptionOperationOutcome(response.readEntity(OperationOutcome.class), "FHIRPersistenceResourceNotFoundException");
    }
    
    @Test
    public void testHistoryPatientNoResults() {
        WebTarget target = getWebTarget();
        Response response = target.path("Patient/123456789ABCDEF/_history").request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);

        assertNotNull(bundle.getType());
        assertNotNull(bundle.getType().getValue());
        assertEquals(BundleTypeList.HISTORY, bundle.getType().getValue());

        assertNotNull(bundle.getEntry());
        assertEquals(0, bundle.getEntry().size());
        
        assertNotNull(bundle.getTotal());
        assertEquals(BigInteger.valueOf(0), bundle.getTotal().getValue());
    }
    
    @Test(groups = { "server-spec" })
    public void testHistoryInvalidResourceType() {
        WebTarget target = getWebTarget();
        Response response = target.path("Bogus/123456789ABCDEF/_history").request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
        assertExceptionOperationOutcome(response.readEntity(OperationOutcome.class), "FHIRVirtualResourceTypeException:");
    }
    
    @Test(groups = { "server-spec" }, dependsOnMethods={"testCreatePatient"})
    public void testSearchPatientByFamilyName() {
        WebTarget target = getWebTarget();
        String familyName = savedPatient.getName().get(0).getFamily().get(0).getValue();
        Response response = target.path("Patient").queryParam("family", familyName).request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        
        assertNotNull(bundle.getType());
        assertNotNull(bundle.getType().getValue());
        assertEquals(BundleTypeList.SEARCHSET, bundle.getType().getValue());
        
        assertNotNull(bundle.getEntry());
        assertTrue(bundle.getEntry().size() >= 1);
        
        assertNotNull(bundle.getTotal());
        assertNotNull(bundle.getTotal().getValue());
//      assertEquals(new Double(bundle.getEntry().size()), new Double(bundle.getTotal().getValue().doubleValue()));
    }
    
    @Test(groups = { "server-spec" }, dependsOnMethods={"testCreatePatient"})
    public void testSearchPatientInvalidSearchAttribute() {
        WebTarget target = getWebTarget();
        Response response = target.path("Patient").queryParam("notasearchparameter", "foo").request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
        assertExceptionOperationOutcome(response.readEntity(OperationOutcome.class), "FHIRSearchException:");
    }
    
    @Test(groups = { "server-spec" }, dependsOnMethods={"testCreatePatient", "testCreateObservation"})
    public void testSearchObservation() {
        WebTarget target = getWebTarget();
        String patientId = savedPatient.getId().getValue();
        Response response = target.path("Observation").queryParam("subject", "Patient/" + patientId).request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertEquals(1, bundle.getEntry().size());
    }    
    
    @Test(groups = { "server-spec" }, dependsOnMethods={"testCreateObservation"})
    public void testSearchObservationInvalidSearchParameter() {
        WebTarget target = getWebTarget();
        Response response = target.path("Observation").queryParam("notasearchparameter", "foo").request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
        assertExceptionOperationOutcome(response.readEntity(OperationOutcome.class), "FHIRSearchException:");
    }
    
    @Test(groups = { "server-spec" }, dependsOnMethods={"testCreateObservation"})
    public void testSearchInvalidResourceType() {
        WebTarget target = getWebTarget();
        Response response = target.path("NotAResourceType").queryParam("notasearchparameter", "foo").request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
        assertExceptionOperationOutcome(response.readEntity(OperationOutcome.class), "FHIRVirtualResourceTypeException:");
    }
    
    @Test(groups = { "server-spec" }, dependsOnMethods={"testCreatePatient"})
    public void testSearchPatientInvalidSearchOperator() {
        WebTarget target = getWebTarget();
        Response response = target.path("Patient").queryParam("family:xxx", "foo").request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
        assertExceptionOperationOutcome(response.readEntity(OperationOutcome.class), "FHIRSearchException:");
    }

    @Test(groups = { "server-spec" }, dependsOnMethods = { "testCreateObservation" })
    public void testConditionalCreateObservation() throws Exception {
        String fakePatientRef = "Patient/" + UUID.randomUUID().toString();
        Observation obs = readResource(Observation.class, "Observation1.json");
        obs.withSubject(objFactory.createReference().withReference(objFactory.createString().withValue(fakePatientRef)));
        
        // First conditional create should find no matches, so we should get back a 201.
        FHIRParameters ifNoneExistQuery = new FHIRParameters().searchParam("subject", fakePatientRef);
        FHIRResponse response = client.conditionalCreate(obs, ifNoneExistQuery);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.CREATED.getStatusCode());
        String locationURI = response.getLocation();
        assertNotNull(locationURI);
        
        // Second conditional create should find 1 match, so we should get back a 200.
        response = client.conditionalCreate(obs, ifNoneExistQuery);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        String locationURI2 = response.getLocation();
        assertNotNull(locationURI2);
        assertEquals(locationURI, locationURI2);
        
        // A search that results in multiple matches should result in a 412 status code.
        FHIRParameters multipleMatches = new FHIRParameters().searchParam("status", "final");
        response = client.conditionalCreate(obs, multipleMatches);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.PRECONDITION_FAILED.getStatusCode());
        
        // Finally, an invalid search should result in a 400 status code.
        FHIRParameters badSearch = new FHIRParameters().searchParam("NOTASEARCHPARAM", "foo");
        response = client.conditionalCreate(obs, badSearch);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.BAD_REQUEST.getStatusCode());
    }    

    @Test(groups = { "server-spec" }, dependsOnMethods = { "testConditionalCreateObservation" })
    public void testConditionalUpdateObservation() throws Exception {
        String fakePatientRef = "Patient/" + UUID.randomUUID().toString();
        String obsId = UUID.randomUUID().toString();
        Observation obs = readResource(Observation.class, "Observation1.json");
        obs.withSubject(objFactory.createReference().withReference(objFactory.createString().withValue(fakePatientRef)));
        obs.withId(objFactory.createId().withValue(obsId));
        
        // First conditional update should find no matches, so we should get back a 201.
        FHIRParameters query = new FHIRParameters().searchParam("_id", obsId);
        FHIRResponse response = client.conditionalUpdate(obs, query);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.CREATED.getStatusCode());
        String locationURI = response.getLocation();
        assertNotNull(locationURI);
        
        // Second conditional update should find 1 match, so we should get back a 200.
        response = client.conditionalUpdate(obs, query);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        String locationURI2 = response.getLocation();
        assertNotNull(locationURI2);
        
        // The location URIs should differ in the version #'s.
        assertNotEquals(locationURI, locationURI2);
        
        // Next, verify that we have two versions of the Observation resource.
        response = client.history("Observation", obsId, null);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle historyBundle = response.getResource(Bundle.class);
        assertNotNull(historyBundle);
        assertEquals(2, historyBundle.getTotal().getValue().intValue());
        
        // A search that results in multiple matches should result in a 412 status code.
        FHIRParameters multipleMatches = new FHIRParameters().searchParam("status", "final");
        response = client.conditionalUpdate(obs, multipleMatches);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.PRECONDITION_FAILED.getStatusCode());
        
        // Finally, an invalid search should result in a 400 status code.
        FHIRParameters badSearch = new FHIRParameters().searchParam("NOTASEARCHPARAM", "foo");
        response = client.conditionalUpdate(obs, badSearch);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.BAD_REQUEST.getStatusCode());
    }    
}
