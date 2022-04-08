/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.flow.impl;

import java.util.logging.Logger;

import org.apache.http.HttpStatus;

import com.ibm.fhir.bucket.client.FHIRBucketClient;
import com.ibm.fhir.bucket.client.FHIRBucketClientUtil;
import com.ibm.fhir.bucket.client.FhirServerResponse;
import com.ibm.fhir.flow.api.FlowInteraction;
import com.ibm.fhir.flow.api.IFlowInteractionHandler;
import com.ibm.fhir.flow.api.IFlowWriter;
import com.ibm.fhir.flow.api.ResourceIdentifier;
import com.ibm.fhir.flow.util.PartitionExecutor;
import com.ibm.fhir.model.resource.Resource;

/**
 * Handle a stream of interactions received from the upstream system
 * by invoking the same interactions on downstream system represented
 * by the client. We can do this using a thread pool - we only need to
 * make sure that we serialize requests for a given logical resource
 * (e.g. Patient/123)
 */
public class DownstreamFHIRWriter implements IFlowWriter, IFlowInteractionHandler {
    private static final Logger logger = Logger.getLogger(DownstreamFHIRWriter.class.getName());

    // Client configured to point to a downstream FHIR server
    private final FHIRBucketClient client;

    private final PartitionExecutor<FlowInteraction> pool;

    // set to false to shut down
    private boolean running = true;

    /**
     * Public constructor
     * @param client
     * @param partitionCount
     * @param partitionQueueSize
     */
    public DownstreamFHIRWriter(FHIRBucketClient client, int partitionCount, int partitionQueueSize) {
        this.client = client;
        this.pool = new PartitionExecutor<>(partitionCount, partitionQueueSize, (interaction) -> partitionKey(interaction), (interaction) -> handler(interaction));
    }

    @Override
    public void submit(FlowInteraction interaction) {
        // Submit the interaction to the partitioned thread pool. The interactions
        // are distributed across the threads in the pool using a partition key
        // based on the resourceType/logicalId value. This guarantees that we
        // serialize any interactions on the same resource, thus preserving the
        // order of changes as they occurred on the upstream system
        if (this.running) {
            this.pool.submit(interaction);
        } else {
            logger.warning("BLOCKED: " + interaction.toString());
            throw new IllegalStateException("downstream writer shut down");
        }
    }

    /**
     * Perform the interaction
     * @param interaction
     */
    private void handler(FlowInteraction interaction) {
        interaction.accept(this); // this is also an IFlowInteractionHandler
    }

    /**
     * Compute a partition key value
     * @param interaction
     * @return
     */
    private String partitionKey(FlowInteraction interaction) {
        // make sure that the same resourceType/identifier values go to
        // one partition - this ensures that they will be processed
        // in order, thus preserving the correct change history for
        // any given resource.
        return interaction.getIdentifier().toString();
    }

    @Override
    public void delete(long changeId, ResourceIdentifier identifier) {
        // issue a delete using the downstream client
        final String request = identifier.getFullUrl();
        FhirServerResponse response = client.delete(request);
        if (response.getStatusCode() != HttpStatus.SC_OK) {
            throw new IllegalStateException("FAILED delete on '" + identifier.getFullUrl() + "'");
        }
    }

    @Override
    public void createOrUpdate(long changeId, ResourceIdentifier identifier, String resourceData, Resource resource) {
        // issue a PUT for create-or-update on the downstream client
        final String request = identifier.getFullUrl();
        // if resourceData is provided we don't need to incur the cost of
        // serializing the resource
        if (resourceData == null) {
            resourceData = FHIRBucketClientUtil.resourceToString(resource);
        }
        FhirServerResponse response = client.put(request, resourceData);
        if (response.getStatusCode() == 201) {
            logger.info("Created downstream resource [" + changeId + "] " + request);
        } else if (response.getStatusCode() == 200) {
            logger.info("Updated downstream resource [" + changeId + "] " + request);
        } else {
            logger.warning("FAILED create/update [" + changeId + "] status=" + response.getStatusCode() + ", resource=" + request);
        }
    }

    @Override
    public void waitForShutdown() {
        this.running = false;
        this.pool.shutdown();
    }
}