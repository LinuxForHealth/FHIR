/*
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test.db2;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.ws.rs.core.Response;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.client.FHIRParameters;
import com.ibm.fhir.client.FHIRResponse;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.server.test.FHIRServerTestBase;

/**
 * Tests specifically the concurrent creation and updating of a single patient, with the updateCreate feature enabled.
 * The idea is to ensure that no patients with duplicate logical ids are created.
 * 
 * NOTE: This test is NOT included in the fhir-server testng suite because of an internal problem with Derby that
 * surfaces after the first execution of this test against a clean Derby DB. Subsequent test runs of this class will
 * fail with an XAException that has an unknown error code. It should only be run against a DB2 database.
 */
public class ConcurrentUpdateTest extends FHIRServerTestBase {
    private static boolean ON = false;

    @BeforeClass
    public void setup() throws Exception {
        Properties testProperties = TestUtil.readTestProperties("test.properties");
        ON = Boolean.parseBoolean(testProperties.getProperty("test.db2.enabled", "false"));
    }

    @Test
    public void testConcurrentUpdateCreate() throws Exception {
        if (ON) {
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
                    concurrentUpdates.add(new PatientUpdater(patient));
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
    }

    /**
     * This inner class invokes the FHIRClient update API for the Patient instance it encapsulates.
     */
    private class PatientUpdater implements Callable<Patient> {
        private Patient patient;

        PatientUpdater(Patient newPatient) {
            this.patient = newPatient;
        }

        @Override
        public Patient call() throws Exception {
            FHIRResponse response = client.update(this.patient);
            boolean goodResponse =
                    (response.getStatus() == Response.Status.OK.getStatusCode() || response.getStatus() == Response.Status.CREATED.getStatusCode());
            assertTrue(goodResponse);
            return this.patient;
        }
    }
}