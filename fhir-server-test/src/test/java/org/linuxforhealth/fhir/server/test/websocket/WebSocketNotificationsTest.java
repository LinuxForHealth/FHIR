/*
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.server.test.websocket;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import org.linuxforhealth.fhir.core.FHIRMediaType;
import org.linuxforhealth.fhir.model.resource.Observation;
import org.linuxforhealth.fhir.model.resource.Patient;
import org.linuxforhealth.fhir.model.test.TestUtil;
import org.linuxforhealth.fhir.server.notification.FHIRNotificationEvent;
import org.linuxforhealth.fhir.server.test.FHIRServerTestBase;

/**
 * The following tests are intentionally marked as singleThreaded. The singleThreaded property addresses an issue where
 * the session does not start-connect-receive a message.
 */
public class WebSocketNotificationsTest extends FHIRServerTestBase {
    // Disabled by default
    private static boolean ON = false;

    private Patient savedCreatedPatient;
    private Observation savedCreatedObservation;

    private FHIRNotificationServiceClientEndpoint endpoint = null;
    private WebTarget target = null;

    @BeforeClass
    public void startup() throws Exception {
        Properties testProperties = TestUtil.readTestProperties("test.properties");

        // We no longer enable websockets in the default config (due to security/privacy concerns)
        // and so this test must be explicitly enabled via the test.websocket.enabled property.
        ON = Boolean.parseBoolean(testProperties.getProperty("test.websocket.enabled", "false"));

        if (ON) {
            target = getWebTarget();
            endpoint = getWebsocketClientEndpoint();
            assertNotNull(endpoint);
        }
    }

    @AfterClass
    public void shutdown() {
        if (ON) {
            endpoint.close();
        }
    }

    public FHIRNotificationEvent getEvent(String id) throws InterruptedException {
        FHIRNotificationEvent event = null;
        int checkCount = 30;
        while (event == null && checkCount > 0) {
            // Only if null, we're going to wait.
            endpoint.getLatch().await(1, TimeUnit.SECONDS);
            event = endpoint.checkForEvent(id);
            checkCount--;
        }
        assertNotNull(event);
        return event;
    }

    /**
     * Create a Patient, then make sure we can retrieve it.
     */
    @Test(groups = { "websocket-notifications" })
    public void testCreatePatient() throws Exception {

        if (!ON) {
            System.out.println("skipping this test ");
        } else {

            // Build a new Patient and then call the 'create' API.
            Patient patient = TestUtil.readLocalResource("Patient_JohnDoe.json");
            Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
            Response response = target.path("Patient").request().post(entity, Response.class);
            assertResponse(response, Response.Status.CREATED.getStatusCode());

            // Get the patient's logical id value.
            String patientId = getLocationLogicalId(response);
            System.out.println(">>> [CREATE] Patient Resource -> Id: " + patientId);

            // Next, call the 'read' API to retrieve the new patient and verify it.
            response = target.path("Patient/"
                    + patientId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
            assertResponse(response, Response.Status.OK.getStatusCode());

            Patient responsePatient = response.readEntity(Patient.class);
            savedCreatedPatient = responsePatient;

            FHIRNotificationEvent event = getEvent(responsePatient.getId());
            assertEquals(event.getResourceId(), responsePatient.getId());
            TestUtil.assertResourceEquals(patient, responsePatient);
        }

    }

    /**
     * Create an Observation and make sure we can retrieve it.
     */
    @Test(groups = { "websocket-notifications" }, dependsOnMethods = { "testCreatePatient" })
    public void testCreateObservation() throws Exception {
        if (!ON) {
            System.out.println("skipping this test ");
        } else {
            // Next, create an Observation belonging to the new patient.
            String patientId = savedCreatedPatient.getId();
            Observation observation = TestUtil.buildPatientObservation(patientId, "Observation1.json");
            Entity<Observation> obs =
                    Entity.entity(observation, FHIRMediaType.APPLICATION_FHIR_JSON);
            Response response = target.path("Observation").request().post(obs, Response.class);
            assertResponse(response, Response.Status.CREATED.getStatusCode());

            String observationId = getLocationLogicalId(response);

            // Next, retrieve the new Observation with a read operation and verify it.
            response = target.path("Observation/"
                    + observationId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
            assertResponse(response, Response.Status.OK.getStatusCode());

            Observation responseObs = response.readEntity(Observation.class);
            savedCreatedObservation = responseObs;

            FHIRNotificationEvent event = getEvent(savedCreatedObservation.getId());

            assertEquals(event.getResourceId(), responseObs.getId());
            TestUtil.assertResourceEquals(observation, responseObs);
        }
    }

    /**
     * Tests the update of the original observation that was previously created.
     */
    @Test(groups = { "websocket-notifications" }, dependsOnMethods = {
            "testCreateObservation" }, singleThreaded = true)
    public void testUpdateObservation() throws Exception {
        if (!ON) {
            System.out.println("skipping this test ");
        } else {
            // Create an updated Observation based on the original saved observation
            String patientId = savedCreatedPatient.getId();
            Observation observation = TestUtil.buildPatientObservation(patientId, "Observation2.json");
            observation = observation.toBuilder().id(savedCreatedObservation.getId()).build();
            Entity<Observation> obs =
                    Entity.entity(observation, FHIRMediaType.APPLICATION_FHIR_JSON);

            // Call the 'update' API.
            String targetPath = "Observation/" + observation.getId();
            Response response = target.path(targetPath).request().put(obs, Response.class);
            assertResponse(response, Response.Status.OK.getStatusCode());
            String observationId = getLocationLogicalId(response);

            // Next, call the 'read' API to retrieve the updated observation and verify it.
            response = target.path("Observation/"
                    + observationId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
            assertResponse(response, Response.Status.OK.getStatusCode());

            Observation responseObservation = response.readEntity(Observation.class);

            FHIRNotificationEvent event = getEvent(responseObservation.getId());

            assertEquals(event.getResourceId(), responseObservation.getId());
            assertNotNull(responseObservation);
            TestUtil.assertResourceEquals(observation, responseObservation);
        }
    }
}
