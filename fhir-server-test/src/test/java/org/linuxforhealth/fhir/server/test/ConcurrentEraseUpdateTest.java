/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.server.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;
import static org.testng.AssertJUnit.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import org.linuxforhealth.fhir.client.FHIRResponse;
import org.linuxforhealth.fhir.model.resource.Patient;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.test.TestUtil;
import org.linuxforhealth.fhir.server.test.concurrent.InteractionFactory.InteractionCallable;
import org.linuxforhealth.fhir.server.test.concurrent.InteractionFactory.InteractionType;

/**
 * Tests the concurrent creation and updating of a single patient, with the updateCreate feature enabled.
 * The idea is to ensure that:
 * 1. No updates are "lost".
 * 2. No deadlocks occur.
 * 3. No duplicate logical ids are created.
 * 4. The resource is searchable in the end.
 */
public class ConcurrentEraseUpdateTest extends FHIRServerTestBase {

    private static final int MAX_THREADS = 10;

    @BeforeClass
    public void shouldRun() throws Exception {
        if (!this.isUpdateCreateSupported()) {
            throw new SkipException("Update Create Support is not enabled");
        }
    }

    @Test
    public void testConcurrentUpdateCreate() throws Exception {
        List<Callable<Resource>> concurrentUpdates = new ArrayList<>();
        List<Future<Resource>> futureResults = new ArrayList<>();

        // Read a JSON Patient and set the id.
        String logicalId = UUID.randomUUID().toString();
        Patient patient = TestUtil.readLocalResource("Patient_SalMonella.json");
        patient = patient.toBuilder().id(logicalId).build();

        // Initialize multi-thread Executor Service.
        ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS);

        // Prepare PatientUpdater instances for running each on its own thread.
        for (int i = 0; i < MAX_THREADS; i++) {
            concurrentUpdates.add(new InteractionCallable(InteractionType.UPDATE, client, patient, "Patient", logicalId, MAX_THREADS));
            concurrentUpdates.add(new InteractionCallable(InteractionType.ERASE, client, patient, "Patient", logicalId, MAX_THREADS));
            concurrentUpdates.add(new InteractionCallable(InteractionType.DELETE, client, patient, "Patient", logicalId, MAX_THREADS));
            concurrentUpdates.add(new InteractionCallable(InteractionType.READ, client, patient, "Patient", logicalId, MAX_THREADS));
        }

        // Run each PatientUpdater on its own thread.
        futureResults = executor.invokeAll(concurrentUpdates);
        assertNotNull(futureResults);

        // The final call to Erase
        InteractionCallable call = new InteractionCallable(InteractionType.ERASE, client, patient, "Patient", logicalId, MAX_THREADS);
        call.call();

        try {
            FHIRResponse response = client.read("Patient", logicalId);
            assertEquals(response.getStatus(), 404);
        } catch (Exception e) {
            fail("Unexpected", e);
        }
    }


}