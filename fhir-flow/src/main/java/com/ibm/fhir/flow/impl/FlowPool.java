/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.flow.impl;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;

import org.apache.http.HttpStatus;

import com.ibm.fhir.bucket.client.FHIRBucketClient;
import com.ibm.fhir.bucket.client.FhirServerResponse;
import com.ibm.fhir.bucket.client.RequestOptions;
import com.ibm.fhir.flow.api.IFlowPool;
import com.ibm.fhir.flow.api.ResourceIdentifierVersion;


/**
 * Implementation of a thread pool used to support async reading of resources
 * from an upstream FHIR server
 */
public class FlowPool implements IFlowPool {
    private static final Logger logger = Logger.getLogger(FlowPool.class.getName());

    // Client configured for the upstream FHIR server
    private final FHIRBucketClient client;

    // The thread pool used to submit requests
    private final ExecutorService pool;

    // Should we parse the resource, or just keep the bytes
    private final boolean parseResource;

    /**
     * Public constructor
     * 
     * @param client
     * @param pool
     */
    public FlowPool(FHIRBucketClient client, ExecutorService pool, boolean parseResource) {
        this.client = client;
        this.pool = pool;
        this.parseResource = parseResource;
    }

    @Override
    public CompletableFuture<FlowFetchResult> requestResource(ResourceIdentifierVersion riv) {
        logger.info("ENQUEUE VREAD " + riv.toString());
        return CompletableFuture.supplyAsync(() -> doTask(riv), pool);
    }

    /**
     * Perform the upstream vread
     * @param riv
     * @return
     */
    private FlowFetchResult doTask(ResourceIdentifierVersion riv) {
        logger.info("RUNNING VREAD " + riv.toString());
        FlowFetchResult result = new FlowFetchResult(riv);
        RequestOptions.Builder requestOptions = RequestOptions.builder();
        requestOptions.parseResource(this.parseResource);
        FhirServerResponse response = client.get(riv.toString(), requestOptions.build());
        result.setStatus(response.getStatusCode());
        if (response.getStatusCode() == HttpStatus.SC_OK) {
            if (this.parseResource) {
                // Check we got what we expected
                Objects.requireNonNull(response.getResource(), "unexpected null resource in response");
                if (response.getResource().getClass().getSimpleName().equals(riv.getResourceType())) {
                    result.setResource(response.getResource());
                } else {
                    // don't trust the upstream
                    logger.severe("vread requested '" + riv.toString() + "' but got '" + response.getResource().toString() + "'");
                    throw new RuntimeException("Resource did not match requested type: '" + riv.toString() + "'");
                }
            } else {
                // Just transfer the raw resource data, which is much faster than
                // deserializing then serializing the Resource object
                result.setResourceData(response.getResourceData());
            }
        }
        return result;
    }
}