/**
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.server.test;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

import java.util.UUID;

import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import com.ibm.watsonhealth.fhir.client.FHIRRequestHeader;
import com.ibm.watsonhealth.fhir.client.FHIRResponse;
import com.ibm.watsonhealth.fhir.model.resource.Bundle;
import com.ibm.watsonhealth.fhir.model.resource.MedicationAdministration;
import com.ibm.watsonhealth.fhir.model.resource.Observation;
import com.ibm.watsonhealth.fhir.model.resource.Patient;
import com.ibm.watsonhealth.fhir.model.resource.Practitioner;
import com.ibm.watsonhealth.fhir.model.type.Id;

/**
 * Tests that involve multiple datastores defined for tenant1
 */
public class MultiDataStoreTest extends FHIRServerTestBase {

    private String patientId = UUID.randomUUID().toString();
    private String practitionerId = UUID.randomUUID().toString();
    private String medadminId = UUID.randomUUID().toString();
    private String observationId = UUID.randomUUID().toString();

    private boolean debug = false;

    @Test
    public void testCreatePatient() throws Exception {
        Patient patient = readResource(Patient.class, "Patient_JohnDoe.json");
        assertNotNull(patient);
        patient = patient.toBuilder().id(Id.of(patientId)).build();

        FHIRRequestHeader[] headers = getHeaders("tenant1", "profile");
        FHIRResponse response = client.update(patient, headers);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.CREATED.getStatusCode());
        logmsg("Created resource: " + response.getLocation());
    }

    @Test(dependsOnMethods = { "testCreatePatient" })
    public void testReadPatient() throws Exception {
        FHIRRequestHeader[] headers = getHeaders("tenant1", "profile");
        FHIRResponse response = client.read("Patient", patientId, headers);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
    }

    @Test(dependsOnMethods = { "testCreatePatient" })
    public void testVReadPatient() throws Exception {
        FHIRRequestHeader[] headers = getHeaders("tenant1", "profile");
        FHIRResponse response = client.vread("Patient", patientId, "1", headers);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
    }

    @Test(dependsOnMethods = { "testCreatePatient" })
    public void testHistoryPatient() throws Exception {
        FHIRRequestHeader[] headers = getHeaders("tenant1", "profile");
        FHIRResponse response = client.history("Patient", patientId, null, headers);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle historyBundle = response.getResource(Bundle.class);
        assertNotNull(historyBundle);
        assertEquals(1, historyBundle.getEntry().size());
    }

    @Test
    public void testCreatePractitioner() throws Exception {
        Practitioner practitioner = readResource(Practitioner.class, "Practitioner.json");
        assertNotNull(practitioner);
        practitioner = practitioner.toBuilder().id(Id.of(practitionerId)).build();

        FHIRRequestHeader[] headers = getHeaders("tenant1", "reference");
        FHIRResponse response = client.update(practitioner, headers);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.CREATED.getStatusCode());
        logmsg("Created resource: " + response.getLocation());

        String[] locationTokens = response.parseLocation(response.getLocation());
        response = client.read(locationTokens[0], locationTokens[1], headers);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
    }

    @Test(dependsOnMethods = { "testCreatePractitioner" })
    public void testReadPractitioner() throws Exception {
        FHIRRequestHeader[] headers = getHeaders("tenant1", "reference");
        FHIRResponse response = client.read("Practitioner", practitionerId, headers);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
    }

    @Test(dependsOnMethods = { "testCreatePractitioner" })
    public void testVReadPractitioner() throws Exception {
        FHIRRequestHeader[] headers = getHeaders("tenant1", "reference");
        FHIRResponse response = client.vread("Practitioner", practitionerId, "1", headers);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
    }

    @Test(dependsOnMethods = { "testCreatePractitioner" })
    public void testHistoryPractitioner() throws Exception {
        FHIRRequestHeader[] headers = getHeaders("tenant1", "reference");
        FHIRResponse response = client.history("Practitioner", practitionerId, null, headers);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle historyBundle = response.getResource(Bundle.class);
        assertNotNull(historyBundle);
        assertEquals(1, historyBundle.getEntry().size());
    }

    @Test
    public void testCreateMedicationAdministration() throws Exception {
        MedicationAdministration medadmin = readResource(MedicationAdministration.class,
                "MedicationAdministration.json");
        assertNotNull(medadmin);
        medadmin = medadmin.toBuilder().id(Id.of(medadminId)).build();

        FHIRRequestHeader[] headers = getHeaders("tenant1", "study1");
        FHIRResponse response = client.update(medadmin, headers);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.CREATED.getStatusCode());
        logmsg("Created resource: " + response.getLocation());

        String[] locationTokens = response.parseLocation(response.getLocation());
        response = client.read(locationTokens[0], locationTokens[1], headers);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
    }

    @Test(dependsOnMethods = { "testCreateMedicationAdministration" })
    public void testReadMedicationAdministration() throws Exception {
        FHIRRequestHeader[] headers = getHeaders("tenant1", "study1");
        FHIRResponse response = client.read("MedicationAdministration", medadminId, headers);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
    }

    @Test(dependsOnMethods = { "testCreateMedicationAdministration" })
    public void testVReadMedicationAdministration() throws Exception {
        FHIRRequestHeader[] headers = getHeaders("tenant1", "study1");
        FHIRResponse response = client.vread("MedicationAdministration", medadminId, "1", headers);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
    }

    @Test(dependsOnMethods = { "testCreateMedicationAdministration" })
    public void testHistoryMedicationAdministration() throws Exception {
        FHIRRequestHeader[] headers = getHeaders("tenant1", "study1");
        FHIRResponse response = client.history("MedicationAdministration", medadminId, null, headers);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle historyBundle = response.getResource(Bundle.class);
        assertNotNull(historyBundle);
        assertEquals(1, historyBundle.getEntry().size());
    }

    @Test
    public void testCreateObservation() throws Exception {
        Observation observation = readResource(Observation.class, "Observation1.json");
        assertNotNull(observation);
        observation = observation.toBuilder().id(Id.of(observationId)).build();

        FHIRRequestHeader[] headers = getHeaders("tenant1", "default");
        FHIRResponse response = client.update(observation, headers);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.CREATED.getStatusCode());
        logmsg("Created resource: " + response.getLocation());
    }

    @Test(dependsOnMethods = { "testCreatePatient", "testCreatePractitioner", "testCreateMedicationAdministration" })
    public void testInvalidDataStores() throws Exception {
        // Verify that the various resources do not exist in the datastores they are not
        // supposed to be in.

        verifyNoResource("Patient", patientId, "tenant1", "reference");
        verifyNoResource("Patient", patientId, "tenant1", "study1");
        verifyNoResource("Patient", patientId, "tenant1", "default");

        verifyNoResource("Practitioner", practitionerId, "tenant1", "profile");
        verifyNoResource("Practitioner", practitionerId, "tenant1", "study1");
        verifyNoResource("Practitioner", practitionerId, "tenant1", "default");

        verifyNoResource("MedicationAdministration", medadminId, "tenant1", "profile");
        verifyNoResource("MedicationAdministration", medadminId, "tenant1", "reference");
        verifyNoResource("MedicationAdministration", medadminId, "tenant1", "default");

        verifyNoResource("Observation", observationId, "tenant1", "profile");
        verifyNoResource("Observation", observationId, "tenant1", "reference");
        verifyNoResource("Observation", observationId, "tenant1", "study1");
    }

    private void verifyNoResource(String resourceType, String id, String tenantId, String dsId) throws Exception {
        FHIRRequestHeader[] headers = getHeaders(tenantId, dsId);
        FHIRResponse response = client.read(resourceType, id, headers);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.NOT_FOUND.getStatusCode());

    }

    private void logmsg(String msg) {
        if (debug) {
            System.out.println(msg);
        }
    }

    private FHIRRequestHeader[] getHeaders(String tenantId, String dsId) {
        FHIRRequestHeader[] result = new FHIRRequestHeader[2];
        result[0] = new FHIRRequestHeader("X-FHIR-TENANT-ID", tenantId);
        result[1] = new FHIRRequestHeader("X-FHIR-DSID", dsId);
        return result;
    }
}
