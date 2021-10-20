/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bucket.reindex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.apache.http.HttpStatus;

import com.ibm.fhir.bucket.client.FHIRBucketClient;
import com.ibm.fhir.bucket.client.FHIRBucketClientUtil;
import com.ibm.fhir.bucket.client.FhirServerResponse;
import com.ibm.fhir.database.utils.thread.ThreadHandler;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Parameters.Builder;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.model.resource.Resource;

/**
 * Drives the $reindex custom operation in parallel from the client side via use of the $retrieve-index operation.
 * Processing continues until index IDs indicate that no resources remain to be reindexed.
 */
public class ClientDrivenReindexOperation extends DriveReindexOperation {
    private static final Logger logger = Logger.getLogger(ClientDrivenReindexOperation.class.getName());

    private static final String COUNT_PARAM = "_count";
    private static final String NOT_MODIFIED_AFTER_PARAM = "notModifiedAfter";
    private static final String AFTER_INDEX_ID_PARAM = "afterIndexId";
    private static final String INDEX_IDS_PARAM = "indexIds";
    private static final int MAX_RETRIEVE_COUNT = 1000;
    private static final int OFFER_TIMEOUT_IN_SEC = 30;
    private static final int POLL_TIMEOUT_IN_SEC = 5;
    private static final int MAX_RESTARTS = 10;
    private static final int MAX_WAIT_TO_FINISH = 60;
    private static final String RETRIEVE_INDEX_URL = "$retrieve-index";
    private static final String REINDEX_URL = "$reindex";

    // Flags to indicate if we should be running
    private volatile boolean running = true;
    private volatile boolean active = false;
    private volatile boolean successRetrieving = false;
    private volatile boolean doneRetrieving = false;

    // FHIR client
    private final FHIRBucketClient fhirClient;

    // Maximum number of concurrent requests
    private final int maxConcurrentRequests;

    // Timestamp the reindex began
    private final String reindexTimestamp;

    // Maximum number of resources reindexed per thread
    private final int maxResourceCount;

    // Queue for holding index IDs of resources to reindex
    private BlockingQueue<String> blockingQueue;

    // Set for holding index IDs currently being processed
    Set<String> inProgressIndexIds;

    // Last index ID found by monitor thread
    private String lastIndexId;

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
     * @param startWithIndexId index ID from which to start, or null
     */
    public ClientDrivenReindexOperation(FHIRBucketClient fhirClient, int maxConcurrentRequests, String reindexTimestamp, int maxResourceCount, String startWithIndexId) {
        this.fhirClient = fhirClient;
        this.maxConcurrentRequests = maxConcurrentRequests;
        this.reindexTimestamp = reindexTimestamp;
        this.maxResourceCount = maxResourceCount;
        this.blockingQueue = new LinkedBlockingDeque<>(MAX_RETRIEVE_COUNT + (maxResourceCount * maxConcurrentRequests));
        this.inProgressIndexIds = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>(maxResourceCount * maxConcurrentRequests));
        if (startWithIndexId != null) {
            // Subtract 1 since the $retrieve-index operation retrieves index IDs after a specified index ID
            this.lastIndexId = String.valueOf(Long.parseLong(startWithIndexId) - 1);
        }
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
        try {
            int numRestarts = 0;
            while (this.running && numRestarts < MAX_RESTARTS) {
                if (!this.active) {

                    // See if we can make one successful request before filling the pool
                    // with hundreds of parallel requests
                    int currentThreadCount = this.currentlyRunning.get();
                    if (currentThreadCount < 1) {
                        // Before starting, ensure the last index IDs is reset so index IDs that were in progress are not skipped
                        resetProgress();
                        // Add limit to number of times this will attempt to restart due to errors
                        numRestarts++;

                        // Nothing currently running, so make one test call to verify things are working
                        doneRetrieving = doneRetrieving || (!callRetrieveIndex() && successRetrieving);
                        if (blockingQueue.isEmpty()) {
                            // Do not even start the worker threads if nothing to do
                            logger.info("Nothing to do, so do not even start the worker threads");
                            this.running = false;
                        } else {
                            // Should be OK now to fill the pool with workers
                            logger.info("Index IDs available for processing - filling worker pool");
                            this.active = true;
                        }

                        for (int i=0; i<this.maxConcurrentRequests && this.active && this.running; i++) {
                            int threadCount = this.currentlyRunning.addAndGet(1);
                            logger.fine("Worker thread starting; " + threadCount + " running");
                            pool.execute(() -> callReindexOperationInLoop());

                            // Slow down the ramp-up so we don't hit a new server with
                            // hundreds of requests in one go
                            ThreadHandler.safeSleep(ThreadHandler.SECOND);
                        }

                        // Keep attempting to retrieve index IDs if we need to
                        while (this.active && this.running) {
                            doneRetrieving = doneRetrieving || (!callRetrieveIndex() && successRetrieving);
                            if (doneRetrieving) {
                                if (blockingQueue.isEmpty()) {
                                    // Tell all the running threads they can stop now
                                    logger.info("Nothing left to do, so tell all the worker threads to exit");
                                    this.running = false;
                                } else {
                                    // Worker threads are still processing, so sleep for a bit before we check again
                                    ThreadHandler.safeSleep(ThreadHandler.FIVE_SECONDS);
                                }
                            }
                        }

                    } else {
                        // Need to wait for all the existing threads to die off before we try to restart. This
                        // could take a while because we have a long tx timeout in Liberty.
                        String lastIndexIdProcessed = getLastIndexIdProcessed();
                        logger.info("Waiting for " + currentThreadCount + " threads to complete, then will resume from index ID '" + lastIndexIdProcessed + "'");
                        ThreadHandler.safeSleep(ThreadHandler.FIVE_SECONDS);
                    }
                } else { // active
                    // Worker threads are active, so sleep for a bit before we check again
                    ThreadHandler.safeSleep(ThreadHandler.FIVE_SECONDS);
                }
            }
        } finally {
            // Wait for worker threads to end
            int waitCount = 0;
            while (waitCount++ < MAX_WAIT_TO_FINISH) {
                int currentThreadCount = this.currentlyRunning.get();
                if (currentThreadCount < 1) {
                    break;
                }
                // Worker threads are still running, so sleep for a bit before we check again
                logger.info("Waiting for " + currentThreadCount + " threads to complete before exiting");
                ThreadHandler.safeSleep(ThreadHandler.FIVE_SECONDS);
            }

            // Check if there is any reindexing work left to do, and log an appropriate message based on the situation
            if (!doneRetrieving || !inProgressIndexIds.isEmpty()) {
                String lastIndexIdProcessed = getLastIndexIdProcessed();
                if (lastIndexIdProcessed != null) {
                    logger.severe("Reindexing was not fully completed, restart reindex from index ID '" + lastIndexIdProcessed + "' to resume");
                } else {
                    logger.severe("No reindexing was completed");
                }
            } else {
                logger.info("Reindexing was completed");
            }
        }
    }

    /**
     * If any index IDs were in progress of being reindexed, set the last index ID (which is used for $retrieve-index)
     * back to before the first index ID that was in progress and clear the in progress list.
     */
    private void resetProgress() {
        this.doneRetrieving = false;
        this.lastIndexId = getLastIndexIdProcessed();
        inProgressIndexIds.clear();
        blockingQueue.clear();
    }

    /**
     * Gets the last index ID processed.
     * @return the last index ID processed
     */
    private String getLastIndexIdProcessed() {
        Long firstInProgress = getFirstInProgressIndexId();
        if (firstInProgress != null) {
            return String.valueOf(firstInProgress - 1);
        } else {
            return this.lastIndexId;
        }
    }

    /**
     * Gets the earliest index ID that was in progress.
     * @return the earliest index ID
     */
    private Long getFirstInProgressIndexId() {
        Long firstInProgress = null;
        for (String inProgressIndexId : inProgressIndexIds) {
            Long indexIdAsLong = Long.valueOf(inProgressIndexId);
            if (firstInProgress == null || firstInProgress > indexIdAsLong) {
                firstInProgress = indexIdAsLong;
            }
        }
        return firstInProgress;
    }

    /**
     * Call the FHIR server $retrieve-index operation.
     * Sets successRetrieving to true only if the call was successful (200 OK).
     * @return true if the call was successful (200 OK) and index IDs were found, otherwise false
     */
    private boolean callRetrieveIndex() {
        successRetrieving = false;
        boolean result = false;

        Builder builder = Parameters.builder();
        builder.parameter(Parameter.builder().name(str(COUNT_PARAM)).value(intValue(MAX_RETRIEVE_COUNT)).build());
        builder.parameter(Parameter.builder().name(str(NOT_MODIFIED_AFTER_PARAM)).value(str(reindexTimestamp)).build());
        if (lastIndexId != null) {
            builder.parameter(Parameter.builder().name(str(AFTER_INDEX_ID_PARAM)).value(str(lastIndexId)).build());
        }
        Parameters parameters = builder.build();
        String requestBody = FHIRBucketClientUtil.resourceToString(parameters);

        /*
         * @see ServerDrivenReindexOperation.callReindexOperation which captures the throwables
         * in order to indicate that this thread is not working properly, and fails the whole
         * FHIRBucket operation.
         */
        try {
            // Get index IDs of resources available to be reindexed
            long start = System.nanoTime();
            FhirServerResponse response = fhirClient.post(RETRIEVE_INDEX_URL, requestBody);
            long end = System.nanoTime();

            double elapsed = (end - start) / 1e9;
            String afterIndexIdString = lastIndexId != null ? (" (" + AFTER_INDEX_ID_PARAM + "=" + lastIndexId + ")") : "";
            logger.info(String.format("called $retrieve-index%s: %d %s [took %5.3f s]", afterIndexIdString, response.getStatusCode(), response.getStatusMessage(), elapsed));

            if (response.getStatusCode() == HttpStatus.SC_OK) {
                Resource resource = response.getResource();
                if (resource != null) {
                    if (resource.is(Parameters.class)) {
                        // Check the result to see if we should keep running
                        result = extractIndexIds((Parameters) resource);
                        if (!result) {
                            logger.info("No more index IDs to retrieve");
                        }
                        successRetrieving = true;
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
        } catch(Throwable t) {
            logger.log(Level.SEVERE, "Throwable caught. FHIR client thread will exit", t);
        }

        return result;
    }

    /**
     * Extract the index IDs from the retrieve-index operation output.
     * @param output the retrieve-index operation output
     * @return true if index IDs were found (even if not successfully queued), otherwise if no index IDs found
     */
    private boolean extractIndexIds(Parameters output) {
        for (Parameter parameter : output.getParameter()) {
            if (INDEX_IDS_PARAM.equals(parameter.getName().getValue())) {
                String indexIdsString = parameter.getValue().as(com.ibm.fhir.model.type.String.class).getValue();
                if (indexIdsString != null) {
                    String[] indexIdsArray = indexIdsString.split(",");
                    for (String indexId : indexIdsArray) {
                        boolean queued = false;
                        while (!queued && this.running) {
                            try {
                                queued = blockingQueue.offer(indexId, OFFER_TIMEOUT_IN_SEC, TimeUnit.SECONDS);
                            } catch (InterruptedException e) {
                                // NOP
                            }

                            if (queued) {
                                lastIndexId = indexId;
                                inProgressIndexIds.add(indexId);
                            } else {
                                logger.warning("Unable to add indexId '" + indexId + "' to queue");
                                if (!this.active) {
                                    logger.warning("Worker threads are not active yet, so try adding again later");
                                    return true;
                                }
                            }
                        }
                    }
                    return indexIdsArray.length > 0;
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
            String indexIds = getIndexIdsToReindex();
            if (!indexIds.isEmpty()) {
                boolean ok = false;
                try {
                    ok = callReindexOperation(indexIds);
                } catch (Throwable t) {
                    logger.log(Level.SEVERE, "Throwable caught. FHIR client thread will exit.", t);
                }
                if (!ok) {
                    // stop everything on the first failure
                    this.active = false;
                }
            }
        }
        int threadCount = currentlyRunning.decrementAndGet();
        logger.fine("Worker thread exited; " + threadCount + " remaining");
    }

    /**
     * Call the FHIR server $reindex operation.
     * @param indexIds the index IDs to reindex
     * @return true if the call was successful (200 OK), otherwise false
     */
    private boolean callReindexOperation(String indexIds) {
        boolean result = true;

        Builder builder = Parameters.builder();
        builder.parameter(Parameter.builder().name(str(INDEX_IDS_PARAM)).value(str(indexIds)).build());
        Parameters parameters = builder.build();
        String requestBody = FHIRBucketClientUtil.resourceToString(parameters);

        // Tell the FHIR Server to reindex the specified resources
        long start = System.nanoTime();
        FhirServerResponse response = fhirClient.post(REINDEX_URL, requestBody);
        long end = System.nanoTime();

        double elapsed = (end - start) / 1e9;
        int index = indexIds.indexOf(",");
        String indexIdString = lastIndexId != null ? (" (" + INDEX_IDS_PARAM + "=" + (index > 0 ? (indexIds.substring(0, index + 1) + "...") : indexIds) + ")") : "";
        logger.info(String.format("called $reindex%s: %d %s [took %5.3f s]", indexIdString, response.getStatusCode(), response.getStatusMessage(), elapsed));

        if (response.getStatusCode() != HttpStatus.SC_OK) {
            // Stop as soon as we hit an error
            logger.severe("FHIR Server reindex operation returned an error: " + response.getStatusCode() + " " + response.getStatusMessage());
            result = false;
        } else {
            inProgressIndexIds.removeAll(Arrays.asList(indexIds.split(",")));
        }

        return result;
    }

    /**
     * Get a comma-delimited string of the next index IDs to reindex.
     * @return a comma-delimited string
     */
    private String getIndexIdsToReindex() {
        List<String> drainToList = new ArrayList<>(maxResourceCount);
        try {
            String indexId = blockingQueue.poll(POLL_TIMEOUT_IN_SEC, TimeUnit.SECONDS);
            if (indexId != null) {
                drainToList.add(indexId);
                blockingQueue.drainTo(drainToList, maxResourceCount - 1);
            }
        } catch (InterruptedException e) {
            // NOP
        }
        return drainToList.stream().collect(Collectors.joining(","));
    }
}
