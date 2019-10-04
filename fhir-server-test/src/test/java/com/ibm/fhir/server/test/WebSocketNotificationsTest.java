/*
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.util.concurrent.TimeUnit;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.resource.Observation;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.notification.FHIRNotificationEvent;

/**
 * 
 * The following tests are intentionally marked as singleThreaded. 
 * The singleThreaded property addresses an issue where the 
 * session does not start-connect-receive a message. 
 *
 */
public class WebSocketNotificationsTest extends FHIRServerTestBase {
    
    private Patient savedCreatedPatient;
    private Observation savedCreatedObservation;

    /**
     * Create a Patient, then make sure we can retrieve it.
     */
    @Test(groups = { "websocket-notifications" }, singleThreaded = true)
    public void testCreatePatient() throws Exception {
        FHIRNotificationServiceClientEndpoint endpoint = getWebsocketClientEndpoint();
        assertNotNull(endpoint);

        WebTarget target = getWebTarget();

        // Build a new Patient and then call the 'create' API.
        Patient patient = readResource(Patient.class, "Patient_JohnDoe.json");
        Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Patient").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the patient's logical id value.
        String patientId = getLocationLogicalId(response);
        System.out.println(">>> [CREATE] Patient Resource -> Id: " + patientId);

        // Next, call the 'read' API to retrieve the new patient and verify it.
        response = target.path("Patient/" + patientId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());

        Patient responsePatient = response.readEntity(Patient.class);
        savedCreatedPatient = responsePatient;

        endpoint.getLatch().await(10, TimeUnit.SECONDS);

        FHIRNotificationEvent event = endpoint.getFirstEvent();
        assertTrue(event != null);

        assertEquals(event.getResourceId(), responsePatient.getId().getValue());
        assertResourceEquals(patient, responsePatient);
    }

    /**
     * Create an Observation and make sure we can retrieve it.
     */
    @Test(groups = { "websocket-notifications" }, dependsOnMethods = { "testCreatePatient" }, singleThreaded = true)
    public void testCreateObservation() throws Exception {
        FHIRNotificationServiceClientEndpoint endpoint = getWebsocketClientEndpoint();

        WebTarget target = getWebTarget();

        // Next, create an Observation belonging to the new patient.
        String patientId = savedCreatedPatient.getId().getValue();
        Observation observation = buildObservation(patientId, "Observation1.json");
        Entity<Observation> obs = Entity.entity(observation, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Observation").request().post(obs, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        String observationId = getLocationLogicalId(response);

        // Next, retrieve the new Observation with a read operation and verify it.
        response = target.path("Observation/" + observationId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());

        Observation responseObs = response.readEntity(Observation.class);
        savedCreatedObservation = responseObs;

        endpoint.getLatch().await(5, TimeUnit.SECONDS);

        FHIRNotificationEvent event = endpoint.getFirstEvent();
        assertTrue(event != null);

        assertEquals(event.getResourceId(), responseObs.getId().getValue());
        assertResourceEquals(observation, responseObs);
    }

    /**
     * Tests the update of the original observation that was previously created.
     */
    @Test(groups = { "websocket-notifications" }, dependsOnMethods = { "testCreateObservation" },  singleThreaded = true)
    public void testUpdateObservation() throws Exception {
        FHIRNotificationServiceClientEndpoint endpoint = getWebsocketClientEndpoint();

        WebTarget target = getWebTarget();

        // Create an updated Observation based on the original saved observation
        String patientId = savedCreatedPatient.getId().getValue();
        Observation observation = buildObservation(patientId, "Observation2.json");
        observation = observation.toBuilder().id(savedCreatedObservation.getId()).build();
        Entity<Observation> obs = Entity.entity(observation, FHIRMediaType.APPLICATION_FHIR_JSON);

        // Call the 'update' API.
        String targetPath = "Observation/" + observation.getId().getValue();
        Response response = target.path(targetPath).request().put(obs, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        String observationId = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the updated observation and verify it.
        response = target.path("Observation/" + observationId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());

        Observation responseObservation = response.readEntity(Observation.class);

        endpoint.getLatch().await(10, TimeUnit.SECONDS);

        FHIRNotificationEvent event = endpoint.getFirstEvent();
        assertTrue(event != null);

        assertEquals(event.getResourceId(), responseObservation.getId().getValue());
        assertNotNull(responseObservation);
        assertResourceEquals(observation, responseObservation);
    }
}
