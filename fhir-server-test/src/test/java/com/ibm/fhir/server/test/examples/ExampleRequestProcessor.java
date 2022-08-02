/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test.examples;

import java.util.logging.Logger;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.spec.test.DriverMetrics;
import com.ibm.fhir.model.spec.test.IExampleProcessor;
import com.ibm.fhir.model.util.SaltHash;
import com.ibm.fhir.model.util.test.ResourceComparatorVisitor;
import com.ibm.fhir.model.visitor.ResourceFingerprintVisitor;
import com.ibm.fhir.server.test.FHIRServerTestBase;

/**
 * Exercises the FHIR REST API. Create is called for each resource, then the
 * resource is retrieved with a GET and its fingerprint is compared with the
 * original to verify its integrity.
 *
 * Fingerprinting is used for the comparison because the FHIR server adds
 * additional (meta) content, which is ignored when the fingerprint is
 * computed.
 */
public class ExampleRequestProcessor implements IExampleProcessor {
    private static final Logger logger = Logger.getLogger(ExampleRequestProcessor.class.getName());
    private final FHIRServerTestBase base;

    // The id of the tenant to use in each FHIR server request
    private final String tenantId;

    // Some simple intrumentation (optional)
    private final DriverMetrics metrics;

    // Read (GET) multiplier for more interesting (simple) performance checks.
    private final int readIterations;

    // Target (once configured) can be shared amongst threads
    private final WebTarget target;

    /**
     * Public constructor
     * @param base
     * @param tenantId
     * @param metrics
     * @param readIterations
     */
    public ExampleRequestProcessor(FHIRServerTestBase base, String tenantId, DriverMetrics metrics, int readIterations) {
        this.base = base;
        this.tenantId = tenantId;
        this.metrics = metrics;
        this.readIterations = readIterations;
        this.target = base.getWebTarget();
    }

    @Override
    public void process(String jsonFile, Resource resource) throws Exception {
        String resourceTypeName = resource.getClass().getSimpleName();

        // clear the id value from the example so we can assign our own
        resource = resource.toBuilder().id(null).build();

        // Compute a fingerprint which can be used to check the consistency of the
        // resource we read back from FHIR
        ResourceFingerprintVisitor v = new ResourceFingerprintVisitor();
        resource.accept(resourceTypeName, v);
        SaltHash baseline = v.getSaltAndHash();

        // Build a new resource and then call the 'create' API.
        long postStart = System.nanoTime();
        Entity<Resource> entity = Entity.entity(resource, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path(resourceTypeName).request()
                .header(FHIRConfiguration.DEFAULT_TENANT_ID_HEADER_NAME, tenantId)
                .post(entity, Response.class);

        try {
            base.assertResponse(response, Response.Status.CREATED.getStatusCode());
        } catch (AssertionError x) {
            // definitely not what we were expecting, so log what the FHIR server gave us
            String msg = response.readEntity(String.class);
            logger.warning("Response body: " + msg);
            throw new Exception("Unexpected response for JSON file: " + jsonFile, x);
        }
        long postEnd = System.nanoTime();
        metrics.addPostTime((postEnd - postStart) / DriverMetrics.NANOS_MS);

        // Get the logical id value.
        String logicalId = base.getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new resource and verify it. We
        // can repeat this a number of times to help get some more useful performance numbers
        for (int i=0; i<this.readIterations; i++) {
            postEnd = System.nanoTime(); // update for each iteration
            response = target.path(resourceTypeName + "/" + logicalId)
                    .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                    .header(FHIRConfiguration.DEFAULT_TENANT_ID_HEADER_NAME, tenantId)
                    .get();
            base.assertResponse(response, Response.Status.OK.getStatusCode());
            metrics.addGetTime((System.nanoTime() - postEnd) / DriverMetrics.NANOS_MS);
        }

        // Now...do we need some reflection here?
        Resource responseResource = response.readEntity(resource.getClass());

        // Now we can check what we sent equals what we got back (minus the meta changes)
        // making sure to seed the visitor with the same salt we used above
        ResourceFingerprintVisitor v2 = new ResourceFingerprintVisitor(baseline);
        responseResource.accept(responseResource.getClass().getSimpleName(), v2);
        SaltHash responseHash = v2.getSaltAndHash();

        if (!responseHash.equals(baseline)) {
            // Use the ResourceComparatorVisitor to provide some detail about what's different
            ResourceComparatorVisitor originals = new ResourceComparatorVisitor();
            resource.accept(resource.getClass().getSimpleName(), originals);

            ResourceComparatorVisitor others = new ResourceComparatorVisitor();
            responseResource.accept(responseResource.getClass().getSimpleName(), others);

            // Perform a bi-directional comparison of values in the maps
            ResourceComparatorVisitor.compare(originals.getValues(), others.getValues());

            // throw the error so it is handled by the test framework
            throw new Exception("The retrieved resource does not match the expected resource.");
        }
    }
}
