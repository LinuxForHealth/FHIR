/*
 * (C) Copyright IBM Corp. 2017, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import com.ibm.fhir.client.FHIRParameters;
import com.ibm.fhir.client.FHIRResponse;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.test.TestUtil;

/**
 * Tests the concurrent creation and updating of a single patient, with the updateCreate feature enabled.
 * The idea is to ensure that:
 * 1. No updates are "lost".
 * 2. No deadlocks occur.
 * 3. No duplicate logical ids are created.
 * 4. The resource is searchable in the end.
 */
public class ConcurrentUpdateTest extends FHIRServerTestBase {

    @Test
    public void testConcurrentUpdateCreate() throws Exception {
        List<Callable<Patient>> concurrentUpdates = new ArrayList<>();
        List<Future<Patient>> futureResults = new ArrayList<>();
        int maxThreads = 12;
        FHIRParameters parameters;
        FHIRResponse response;

        if (this.isUpdateCreateSupported()) {

            // Read a JSON Patient and set the id.
            String patientLogicalId = UUID.randomUUID().toString();
            Patient patient = TestUtil.readLocalResource("Patient_SalMonella.json");
            patient = patient.toBuilder().id(patientLogicalId).build();

            // Initialize multi-thread Executor Service.
            ExecutorService executor = Executors.newFixedThreadPool(maxThreads);

            // Prepare PatientUpdater instances for running each on its own thread.
            for (int i = 0; i < maxThreads; i++) {
                concurrentUpdates.add(new PatientUpdater(patient, i));
            }

            // Run each PatientUpdater on its own thread.
            futureResults = executor.invokeAll(concurrentUpdates);
            assertNotNull(futureResults);

            // Search on the patient id, and see how many results we get. There should only
            // be one.
            parameters = new FHIRParameters().searchParam("_id", patientLogicalId);
            response = client.search("Patient", parameters);
            assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
            Bundle searchResults = response.getResource(Bundle.class);
            assertEquals(1, searchResults.getEntry().size());
            assertNotNull(searchResults.getEntry().get(0).getResource());
            assertEquals(patientLogicalId, searchResults.getEntry().get(0).getResource().getId());

            // Pull the history on the patient id. There should be the same number of
            // versions as the value of maxThreads.
            parameters = new FHIRParameters().count(maxThreads);
            response = client.history("Patient", patientLogicalId, parameters);
            assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertNotNull(bundle.getEntry());
            assertEquals(maxThreads, bundle.getEntry().size());

            // Make sure that the versions are in descending order, and that none are
            // duplicates.
            Integer previousVersionId = null;
            Integer versionId;
            for (Bundle.Entry entry : bundle.getEntry()) {
                Patient bundlePatient = (Patient) entry.getResource();
                versionId = Integer.valueOf(bundlePatient.getMeta().getVersionId().getValue());
                if (previousVersionId != null) {
                    assertTrue(previousVersionId > versionId);
                }
                previousVersionId = versionId;
            }
        }
    }

    /**
     * This inner class invokes the FHIRClient update API for the Patient instance it encapsulates,
     * retrying in a tight loop in the case of a 409 Conflict.
     */
    private class PatientUpdater implements Callable<Patient> {
        private Patient patient;

        PatientUpdater(Patient newPatient, int counter) {
            // the counter is used to ensure that each instance is unique
            this.patient = newPatient.toBuilder()
                    .multipleBirth(counter)
                    .build();
        }

        @Override
        public Patient call() throws Exception {
            int status;
            do {
                FHIRResponse response = client.update(this.patient);
                status = response.getStatus();
            } while (status == Response.Status.CONFLICT.getStatusCode());

            assertTrue(status == Response.Status.OK.getStatusCode() || status == Response.Status.CREATED.getStatusCode());
            return this.patient;
        }
    }
}