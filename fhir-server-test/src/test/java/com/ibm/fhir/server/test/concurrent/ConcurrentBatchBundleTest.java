/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.server.test.concurrent;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.client.FHIRResponse;
import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.server.test.FHIRServerTestBase;
import com.ibm.fhir.server.test.operation.EraseOperationTest;

/**
 * Test concurrent application of a batch bundle. Each entry in a batch
 * bundle is processed in its own transaction. The bundles being tested
 * contain update and delete entries for the same set of resources so
 * we expect to hit some 409 concurrent update issues. At the end, we
 * vread each version of each resource to make sure it matches what is
 * expected - particularly important when dealing with payload offload
 * configurations which have additional processing to perform when a
 * transaction gets rolled back
 */
public class ConcurrentBatchBundleTest extends FHIRServerTestBase {
    private static final Logger logger = Logger.getLogger(ConcurrentBatchBundleTest.class.getName());
    private static final int MAX_THREADS = 10;
    private static final int ITERATIONS = 10;
    private AtomicBoolean failed = new AtomicBoolean(false);
    private AtomicInteger concurrentUpdateCount = new AtomicInteger();
    private Set<String> locations = ConcurrentHashMap.newKeySet();

    @BeforeClass
    public void shouldRun() throws Exception {
        if (!this.isUpdateCreateSupported()) {
            throw new SkipException("Update Create Support is not enabled");
        }
    }

    @Test
    public void testConcurrentBatchBundle() throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(MAX_THREADS);
        final Bundle bundle1 = TestUtil.readLocalResource("concurrentBatchBundle-1.json");
        final Bundle bundle2 = TestUtil.readLocalResource("concurrentBatchBundle-2.json");

        for (int i=0; i<ITERATIONS; i++) {
            pool.execute(() -> {
                processBundle(bundle1); // ensures any creates are done before
                processBundle(bundle2); // the deletes in bundle2
            });
        }

        // Wait for everything to complete
        pool.shutdown();
        pool.awaitTermination(120, TimeUnit.SECONDS);

        // Make sure that no requests failed
        assertFalse(failed.get());

        // Make sure we hit at least one concurrent update...otherwise the test
        // isn't doing anything useful
        assertTrue(concurrentUpdateCount.get() > 0);
    }

    /**
     * POST the batch bundle to the FHIR server and check the response
     * @param bundle
     */
    private void processBundle(Bundle bundle) {
        try {
            FHIRResponse response = client.batch(bundle);
            if (response.getStatus() == 200) {
                // Make sure we have a valid response for each entry
                Bundle responseBundle = response.getResource(Bundle.class);
                processBundleResponse(responseBundle);
            } else {
                throw new IllegalStateException("Expected 200 OK when posting batch bundle");
            }
        } catch (Exception x) {
            this.failed.set(true);
            logger.log(Level.SEVERE, "failed to process bundle '" + bundle.getId() + "'", x);
        }
    }

    /**
     * Check the response for each entry in the response bundle
     * @param responseBundle
     * @throws Exception
     */
    private void processBundleResponse(Bundle responseBundle) throws Exception {
        List<Bundle.Entry> entries = responseBundle.getEntry();
        for (Bundle.Entry entry: entries) {
            Bundle.Entry.Response response = entry.getResponse();
            if (response != null) {
                if ("200".equals(response.getStatus().getValue()) 
                        || "201".equals(response.getStatus().getValue())) {
                    // Now if the response contains a location, try and read it to
                    // make sure that the payload meta matches
                    if (response.getLocation() != null) {
                        recordLocation(response.getLocation());
                        vread(response.getLocation());
                    }
                } else if ("409".equals(response.getStatus().getValue())) {
                    // concurrent update...which is OK
                    this.concurrentUpdateCount.addAndGet(1);
                } else {
                    throw new IllegalStateException("unexpected entry response status: '" + response.getStatus().getValue() + "'");
                }
            } else {
                throw new IllegalStateException("response bundle entry missing response!");
            }
        }
    }

    /**
     * Perform a vread of the resource at the given location
     * @param location
     * @throws Exception
     */
    private void vread(Uri location) throws Exception {
        // "location": "Patient/3/_history/41"
        final String[] parts = location.getValue().split("/");
        if (parts.length != 4) {
            throw new IllegalArgumentException("invalid location: '" + location.toString() + "'");
        }
        final String resourceType = parts[0];
        final String logicalId = parts[1];
        final String versionId = parts[3];
        FHIRResponse response = client.vread(resourceType, logicalId, versionId);
        if (response.getStatus() == 200) {
            Resource resource = response.getResource(ModelSupport.getResourceType(resourceType));
            if (resource.getMeta() != null && resource.getMeta().getVersionId() != null) {
                if (!versionId.equals(resource.getMeta().getVersionId().getValue())) {
                    // This check is designed to catch concurrency errors when using payload offload
                    throw new IllegalStateException("Resource meta version mismatch. Location '" + location.toString() + "'; resource = " + resource.toString());
                }
            }
        } else {
            throw new IllegalStateException("failed to vread resource location: '" + location + "'");
        }
    }

    /**
     * Keep a record of all the unique resources we create during the test so that
     * we can erase them at the end
     * @param location
     */
    private void recordLocation(Uri location) {
        final String[] parts = location.getValue().split("/");
        if (parts.length != 4) {
            throw new IllegalArgumentException("invalid location: '" + location.toString() + "'");
        }
        final String resourceType = parts[0];
        final String logicalId = parts[1];
        // record just the resource id, without the version history
        locations.add(resourceType + "/" + logicalId);
    }

    /**
     * Clean up the resources created by the first part of the test
     * @throws Exception
     */
    @Test(dependsOnMethods = "testConcurrentBatchBundle")
    public void eraseTestResources() throws Exception {
        Entity<Parameters> entity = Entity.entity(EraseOperationTest.generateParameters(true, true, "Test", null), FHIRMediaType.APPLICATION_FHIR_JSON);
        for (String location: this.locations) {
            logger.info("Cleaning up after test: erasing: '" + location + "'");
            Response r = client.getWebTarget().path("/" + location
                + "/$erase").request(FHIRMediaType.APPLICATION_FHIR_JSON).header("X-FHIR-TENANT-ID", client.getTenantId()).header("X-FHIR-DSID", "default").post(entity, Response.class);
            assertTrue(r.getStatus() == Response.Status.OK.getStatusCode() || r.getStatus() == Response.Status.NOT_FOUND.getStatusCode());
        }
    }

    /**
     * Make sure we can no longer read the resources we created
     * @throws Exception
     */
    @Test(dependsOnMethods = "eraseTestResources")
    public void testGone() throws Exception {
        for (String location: this.locations) {
            final String[] parts = location.split("/");
            if (parts.length != 2) {
                throw new IllegalArgumentException("invalid location: '" + location + "'");
            }
            final String resourceType = parts[0];
            final String logicalId = parts[1];
            FHIRResponse response = client.read(resourceType, logicalId);
            assertEquals(response.getStatus(), Response.Status.NOT_FOUND.getStatusCode());
        }
    }
}
