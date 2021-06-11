/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bucket.reindex;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.apache.http.HttpStatus;

import com.ibm.fhir.bucket.client.FHIRBucketClient;
import com.ibm.fhir.bucket.client.FHIRBucketClientUtil;
import com.ibm.fhir.bucket.client.FhirServerResponse;
import com.ibm.fhir.database.utils.api.DataAccessException;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Parameters.Builder;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.model.resource.Resource;

/**
 * Drives the $reindex custom operation in parallel from the client side via use of the $retrieve-index operation.
 * Processing continues until logical resource IDs indicate that no resources remain to be reindexed.
 */
public class ClientDrivenReindexOperation extends DriveReindexOperation {
    private static final Logger logger = Logger.getLogger(ClientDrivenReindexOperation.class.getName());

    private static final String COUNT_PARAM = "_count";
    private static final String NOT_MODIFIED_AFTER_PARAM = "notModifiedAfter";
    private static final String AFTER_LOGICAL_RESOURCE_ID_PARAM = "afterLogicalResourceId";
    private static final String LOGICAL_RESOURCE_IDS_PARAM = "logicalResourceIds";
    private static final int MAX_RETRIEVE_COUNT = 1000;
    private static final int OFFER_TIMEOUT_IN_SEC = 10;
    private static final String RETRIEVE_INDEX_URL = "$retrieve-index";
    private static final String REINDEX_URL = "$reindex";

    // Flags to indicate if we should be running
    private volatile boolean running = true;
    private volatile boolean active = false;
    private volatile boolean doneRetrieving = false;

    // FHIR client
    private final FHIRBucketClient fhirClient;

    // Maximum number of concurrent requests
    private final int maxConcurrentRequests;

    // Timestamp the reindex began
    private final String reindexTimestamp;

    // Maximum number of resources reindexed per thread
    private final int maxResourceCount;

    // Queue for holding logical resource IDs to reindex
    private BlockingQueue<String> blockingQueue;

    // Last logical resource ID found by monitor thread
    private String lastLogicalResourceId;

    // Monitor thread
    private Thread monitorThread;

    // Thread pool for processing requests
    private final ExecutorService pool = Executors.newCachedThreadPool();

    // Number of threads currently running
    private AtomicInteger currentlyRunning = new AtomicInteger();

    /**
     * Public constructor.
     * @param fhirClient the FHIR client
     * @param maxConcurrentRequests the number of threads to spin up
     * @param reindexTimestamp timestamp the reindex began
     * @param maxResourceCount resources processed per request per thread
     */
    public ClientDrivenReindexOperation(FHIRBucketClient fhirClient, int maxConcurrentRequests, String reindexTimestamp, int maxResourceCount) {
        this.fhirClient = fhirClient;
        this.maxConcurrentRequests = maxConcurrentRequests;
        this.reindexTimestamp = reindexTimestamp;
        this.maxResourceCount = maxResourceCount;
        this.blockingQueue = new LinkedBlockingDeque<>(MAX_RETRIEVE_COUNT + (maxResourceCount * maxConcurrentRequests));
    }

    /**
     * Start the main loop
     */
    @Override
    public void init() {
        if (!running) {
            throw new IllegalStateException("Already shutdown");
        }

        // Initiate the monitorThread. This will fill the pool
        // with worker threads, and monitor for completion or failure
        logger.info("Starting monitor thread");
        this.monitorThread = new Thread(() -> monitorLoop());
        this.monitorThread.start();
    }

    /**
     * Program is stopping, so tell the threads they can stop too
     */
    @Override
    public void signalStop() {
        this.running = false;

        // make sure the pool doesn't start new work
        pool.shutdown();
    }

    /**
     * Wait until things are stopped
     */
    @Override
    public void waitForStop() {
        if (this.running) {
            signalStop();
        }

        try {
            pool.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException x) {
            logger.warning("Wait for pool shutdown interrupted");
        }

        try {
            // break any sleep inside the monitorThread
            this.monitorThread.interrupt();
            this.monitorThread.join();
        } catch (InterruptedException x) {
            logger.warning("Interrupted waiting for monitorThread completion");
        }
    }

    /**
     * The main monitor loop.
     */
    public void monitorLoop() {
        while (this.running) {
            if (!this.active) {
                // See if we can make one successful request before filling the pool
                // with hundreds of parallel requests
                int currentThreadCount = this.currentlyRunning.get();
                if (currentThreadCount == 0) {
                    // Nothing currently running, so make one test call to verify things are working
                    doneRetrieving = doneRetrieving || !callRetrieveIndex();
                    if (!blockingQueue.isEmpty()) {
                        // Should be OK now to fill the pool with workers
                        logger.info("Logical resource IDs available for processing - filling worker pool");
                        this.active = true;
                    }

                    for (int i=0; i<this.maxConcurrentRequests && this.active && this.running; i++) {
                        this.currentlyRunning.addAndGet(1);
                        pool.execute(() -> callReindexOperationInLoop());

                        // Slow down the ramp-up so we don't hit a new server with
                        // hundreds of requests in one go
                        safeSleep(1000);
                    }

                    // Keep attempting to retrieve logical resource IDs if we need to
                    while (this.active && this.running) {
                        doneRetrieving = doneRetrieving || !callRetrieveIndex();
                        if (doneRetrieving) {
                            if (blockingQueue.isEmpty()) {
                                // Tell all the running threads they can stop now
                                logger.info("Nothing left to do, tell all the worker threads to exit");
                                this.running = false;
                            } else {
                                // Worker threads are still processing, so sleep for a bit before we check again
                                safeSleep(5000);
                            }
                        }
                    }

                } else {
                    // Need to wait for all the existing threads to die off before we try to restart. This
                    // could take a while because we have a long tx timeout in Liberty.
                    logger.info("Waiting for current threads to complete before restart: " + currentThreadCount);
                    safeSleep(5000);
                }
            } else { // active
                // Worker threads are active, so sleep for a bit before we check again
                safeSleep(5000);
            }
        }
    }

    /**
     * Call the FHIR server $retrieve-index operation.
     * @return true if the call was successful (200 OK) and logical resources IDs were found, otherwise false
     */
    private boolean callRetrieveIndex() {
        boolean result = false;

        Builder builder = Parameters.builder();
        builder.parameter(Parameter.builder().name(str(COUNT_PARAM)).value(intValue(MAX_RETRIEVE_COUNT)).build());
        builder.parameter(Parameter.builder().name(str(NOT_MODIFIED_AFTER_PARAM)).value(str(reindexTimestamp)).build());
        if (lastLogicalResourceId != null) {
            builder.parameter(Parameter.builder().name(str(AFTER_LOGICAL_RESOURCE_ID_PARAM)).value(str(lastLogicalResourceId)).build());
        }
        Parameters parameters = builder.build();
        String requestBody = FHIRBucketClientUtil.resourceToString(parameters);

        // Get logical resource IDs of resources available to be reindexed
        long start = System.nanoTime();
        FhirServerResponse response = fhirClient.post(RETRIEVE_INDEX_URL, requestBody);
        long end = System.nanoTime();

        double elapsed = (end - start) / 1e9;
        logger.info(String.format("called $retrieve-index: %d %s [took %5.3f s]", response.getStatusCode(), response.getStatusMessage(), elapsed));

        if (response.getStatusCode() == HttpStatus.SC_OK) {
            Resource resource = response.getResource();
            if (resource != null) {
                if (resource.is(Parameters.class)) {
                    // Check the result to see if we should keep running
                    result = extractLogicalResourceIds((Parameters) resource);
                    if (!result) {
                        logger.info("No more logical resource IDs to retrieve");
                    }
                } else {
                    logger.severe("FHIR Server retrieve-index response is not an Parameters: " + response.getStatusCode() + " " + response.getStatusMessage());
                    logger.severe("Actual response: " + FHIRBucketClientUtil.resourceToString(resource));
                }
            } else {
                // This would be a bit weird
                logger.severe("FHIR Server retrieve-index operation returned no Parameters: " + response.getStatusCode() + " " + response.getStatusMessage());
            }
        } else {
            // Stop as soon as we hit an error
            logger.severe("FHIR Server retrieve-index operation returned an error: " + response.getStatusCode() + " " + response.getStatusMessage());
        }

        return result;
    }

    /**
     * Extract the logical resource IDs from the retrieve-index operation output.
     * @param output the retrieve-index operation output
     * @return true if logical resources IDs were found, otherwise false
     */
    private boolean extractLogicalResourceIds(Parameters output) {
        for (Parameter parameter : output.getParameter()) {
            if (LOGICAL_RESOURCE_IDS_PARAM.equals(parameter.getName().getValue())) {
                String lrIdsString = parameter.getValue().as(com.ibm.fhir.model.type.String.class).getValue();
                if (lrIdsString != null) {
                    String[] lrIdArray = lrIdsString.split(",");
                    for (String logicalResourceId : lrIdArray) {
                        boolean queued = false;
                        while (!queued && this.running) {
                            try {
                                queued = blockingQueue.offer(logicalResourceId, OFFER_TIMEOUT_IN_SEC, TimeUnit.SECONDS);
                            } catch (InterruptedException e) {
                                // NOP
                            }

                            if (queued) {
                                lastLogicalResourceId = logicalResourceId;
                            } else {
                                logger.warning("Unable to add logicalResourceId '" + logicalResourceId + "' to queue");
                                if (!this.active) {
                                    logger.warning("Worker threads are not active yet, so try adding again later");
                                    return true;
                                }
                            }
                        }
                    }
                    return lrIdArray.length > 0;
                }
            }
        }
        return false;
    }

    /**
     * Thread to repeatedly call the $reindex operation until done or error.
     */
    private void callReindexOperationInLoop() {
        while (this.running && this.active) {
            String logicalResourceIds = getLogicalResourceIdsToReindex();
            if (!logicalResourceIds.isEmpty()) {
                boolean ok = false;
                try {
                    ok = callReindexOperation(logicalResourceIds);
                } catch (DataAccessException x) {
                    // allow active be set to false.  This will notify monitorLoop something is wrong.
                    // Probably all threads will encounter the same exception and monitorLoop will
                    // try to refill the pool if all threads exit.
                    logger.severe("DataAccessException caught when contacting FHIR server. FHIR client thread will exit." + x.toString() );
                } catch (IllegalStateException x) {
                    // Fail for this exception too. fhir-bucket fhir client suggests this exception results from config error.
                    // So probably this will be caught first time monitorLoop calls callOnce and not here.
                    logger.severe("IllegalStateException caught. FHIR client thread will exit." + x.toString() );
                }
                if (!ok) {
                    // stop everything on the first failure
                    this.active = false;
                }
            } else {
                // queue is empty, wait a partial second and try again
                safeSleep(100);
            }
        }

        this.currentlyRunning.decrementAndGet();
    }

    /**
     * Call the FHIR server $reindex operation.
     * @param logicalResourceIds the logical resource IDs to reindex
     * @return true if the call was successful (200 OK), otherwise false
     */
    private boolean callReindexOperation(String logicalResourceIds) {
        boolean result = true;

        Builder builder = Parameters.builder();
        builder.parameter(Parameter.builder().name(str(LOGICAL_RESOURCE_IDS_PARAM)).value(str(logicalResourceIds)).build());
        Parameters parameters = builder.build();
        String requestBody = FHIRBucketClientUtil.resourceToString(parameters);

        // Tell the FHIR Server to reindex the specified resources
        long start = System.nanoTime();
        FhirServerResponse response = fhirClient.post(REINDEX_URL, requestBody);
        long end = System.nanoTime();

        double elapsed = (end - start) / 1e9;
        logger.info(String.format("called $reindex: %d %s [took %5.3f s]", response.getStatusCode(), response.getStatusMessage(), elapsed));

        if (response.getStatusCode() != HttpStatus.SC_OK) {
            // Stop as soon as we hit an error
            logger.severe("FHIR Server reindex operation returned an error: " + response.getStatusCode() + " " + response.getStatusMessage());
            result = false;
        }

        return result;
    }

    /**
     * Get a comma-delimited string of the next logical resource IDs to reindex.
     * @return a comma-delimited string
     */
    private String getLogicalResourceIdsToReindex() {
        List<String> drainToList = new ArrayList<>(maxResourceCount);
        blockingQueue.drainTo(drainToList, maxResourceCount);
        return drainToList.stream().collect(Collectors.joining(","));
    }
}
