/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bucket.reindex;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpStatus;

import com.ibm.fhir.bucket.client.FhirClient;
import com.ibm.fhir.bucket.client.FhirClientUtil;
import com.ibm.fhir.bucket.client.FhirServerResponse;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.Parameters.Parameter;

/**
 * Drives the $reindex custom operation in parallel. Each thread keeps running
 * until the OperationOutcome indicates that no work remains to be processed.
 */
public class DriveReindexOperation {
    private static final Logger logger = Logger.getLogger(DriveReindexOperation.class.getName());
    
    // the maximum number of requests we permit
    private final int maxConcurrentRequests;
    
    private volatile boolean running = true;

    // thread pool for processing requests
    private final ExecutorService pool = Executors.newCachedThreadPool();

    private final FhirClient fhirClient;
    
    private final String url = "$reindex";
    
    // The serialized Parameters resource sent with each POST
    private final String requestBody;
    
    /**
     * Public constructor
     * @param client the FHIR client
     * @param maxConcurrentRequests the number of threads to spin up
     */
    public DriveReindexOperation(FhirClient fhirClient, int maxConcurrentRequests, String tstampParam, int resourceCountParam) {
        this.fhirClient = fhirClient;
        this.maxConcurrentRequests = maxConcurrentRequests;

        Parameters parameters = Parameters.builder()
                .parameter(Parameter.builder().name(str("_tstamp")).value(str(tstampParam)).build())
                .parameter(Parameter.builder().name(str("_resourceCount")).value(intValue(resourceCountParam)).build())
                .build();
        
        // Serialize into the requestBody string used by all the threads
        this.requestBody = FhirClientUtil.resourceToString(parameters);
        
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("Reindex request parameters: " + requestBody);
        }
    }

    /**
     * Syntactic sugar for providing string values
     * @param str
     * @return
     */
    private static com.ibm.fhir.model.type.String str(String str) {
        return com.ibm.fhir.model.type.String.of(str);
    }
    
    private static com.ibm.fhir.model.type.Integer intValue(int val) {
        return com.ibm.fhir.model.type.Integer.of(val);
    }

    /**
     * Start the main loop
     */
    public void init() {
        if (!running) {
            throw new IllegalStateException("Already shutdown");
        }
        
        for (int i=0; i<this.maxConcurrentRequests; i++) {
            pool.execute(() -> callReindexOperation());
        }
    }

    /**
     * Program is stopping, so tell the threads they can stop too
     */
    public void signalStop() {
        this.running = false;
        
        // make sure the pool doesn't start new work
        pool.shutdown();
    }
    
    /**
     * Wait until things are stopped
     */
    public void waitForStop() {
        if (this.running) {
            signalStop();
        }
        
        try {
            pool.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException x) {
            logger.warning("Wait for pool shutdown interrupted");
        }
    }

    /**
     * Thread to repeatedly call the $reindex operation until the response
     * indicates all the work is complete.
     */
    private void callReindexOperation() {
        while (this.running) {
            // tell the FHIR Server to reindex a number of resources
            long start = System.nanoTime();
            FhirServerResponse response = fhirClient.post(url, requestBody);
            long end = System.nanoTime();
            
            double elapsed = (end - start) / 1e9;
            logger.info(String.format("called $reindex: %d %s [took %5.3f s]", response.getStatusCode(), response.getStatusMessage(), elapsed));
            
            if (response.getStatusCode() == HttpStatus.SC_OK) {
                Resource result = response.getResource();
                if (result != null) {
                    if (result.is(OperationOutcome.class)) {
                        // check the result to see if we should stop running
                        checkResult((OperationOutcome)result);
                    } else {
                        logger.severe("FHIR Server reindex response is not an OperationOutcome: " + response.getStatusCode() + " " + response.getStatusMessage());
                        logger.severe("Actual response: " + FhirClientUtil.resourceToString(result));
                        this.running = false;
                    }
                } else {
                    // this would be a bit weird
                    logger.severe("FHIR Server reindex operation returned no OperationOutcome: " + response.getStatusCode() + " " + response.getStatusMessage());
                    this.running = false;
                }
            } else {
                // Stop as soon as we hit an error
                logger.severe("FHIR Server reindex operation returned an error: " + response.getStatusCode() + " " + response.getStatusMessage());
                this.running = false;
            }
        }
    }

    /**
     * Check the result to see if the server is telling us it's done
     * @param result
     */
    private void checkResult(OperationOutcome result) {
        List<Issue> issues = result.getIssue();
        if (issues.size() == 1) {
            Issue one = issues.get(0);
            if ("Reindex complete".equals(one.getDiagnostics().getValue())) {
                logger.info("Reindex - all done");
                
                // tell all the running threads they can stop now
                this.running = false;
            }
        }
    }
}
