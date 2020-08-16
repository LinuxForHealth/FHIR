/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bucket.scanner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.bucket.api.ResourceEntry;
import com.ibm.fhir.bucket.client.FhirClient;
import com.ibm.fhir.bucket.client.PostResource;
import com.ibm.fhir.model.resource.Resource;

/**
 * Calls the FHIR REST API to create resources, supported by a thread pool
 */
public class ResourceHandler {
    private static final Logger logger = Logger.getLogger(ResourceHandler.class.getName());

    // The number of resources we can process in parallel
    private final int poolSize;
    
    // The thread pool
    private final ExecutorService pool;

    // Client for making FHIR server requests
    private final FhirClient fhirClient;
    
    // flow control so we don't overload the thread pool queue
    private final Lock lock = new ReentrantLock();
    private final Condition capacityCondition = lock.newCondition();
    
    // how many resources are currently queued or being processed
    private int inflight;
    
    // flag used to handle shutdown
    private volatile boolean running = true;

    // Access to the FHIR bucket persistence layer to record logical ids
    private final DataAccess dataAccess;
    
    /**
     * Public constructor
     * @param poolSize
     */
    public ResourceHandler(int poolSize, FhirClient fc, DataAccess dataAccess) {
        this.poolSize = poolSize;
        this.fhirClient = fc;
        this.pool = Executors.newFixedThreadPool(poolSize);
        this.dataAccess = dataAccess;
    }

    /**
     * Shut down all resource processing
     */
    public void stop() {
        this.running = false;
        
        // Wake up anything which may be blocked
        lock.lock();
        try {
            capacityCondition.signalAll();
        } finally {
            lock.unlock();
        }
        
        // Shut down the pool
        this.pool.shutdown();
        
        try {
            this.pool.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException x) {
            // Not much to do other than moan
            logger.warning("Interrupted waiting for pool to shut down");
        }
    }

    /**
     * Add the resource entry to the thread-pool for processing, subject to the
     * rate limiting we have to make sure memory consumption is kept in check
     * @param entry
     * @return
     */
    public boolean process(ResourceEntry entry) {
        boolean result = false;

        // Add this so we can track when the job completes
        entry.getJob().incInflight();

        // Throttle how many resources we allow to be inflight
        // at any point in time...this helps to keep memory
        // consumption reasonable, because we can read and parse
        // more quickly than the FHIR server(s) can process
        int maxInflight = 3 * poolSize;
        lock.lock();
        try {
            while (running && inflight == maxInflight) {
                capacityCondition.await();
            }
            
            if (running) {
                inflight++;
                result = true;
            }
        } catch (InterruptedException x) {
            logger.info("Interrupted while waiting for capacity");
        }
        finally {
            lock.unlock();
        }

        // only submit to the queue if we have permission
        if (result) {
            pool.submit(() -> {
                try {
                    processThr(entry);
                } catch (Exception x) {
                    logger.log(Level.SEVERE, entry.toString(), x);
                } finally {
                    lock.lock();
                    try {
                        inflight--;
                        capacityCondition.signalAll();
                    } finally {
                        lock.unlock();
                    }
                }
            });
        }
        
        return result;
    }
    
    /**
     * Process the resource in the thread pool
     * @param resource
     */
    public void processThr(ResourceEntry entry) {
        
        boolean success = false;
        try {
            Resource resource = entry.getResource();
            final String resourceType = resource.getClass().getSimpleName();
            logger.info("Processing resource: " + resourceType);
            
            // Build a post request for the resource and send to FHIR
            PostResource post = new PostResource(resource);
            String id = post.run(fhirClient);
            if (id != null) {
                
                if (id.startsWith("https://")) {
                    // We want to extract the raw logical identifier from the full location string
                    // https://localhost:9443/fhir-server/api/v4/DiagnosticReport/173eed87a99-605de23b-266d-4b4d-b64f-31e769fda112/_history/1
                    String[] parts = id.split("/");
                    if (parts.length == 10) {
                        id = parts[7];
                    }
                }
                
                logger.info("New " + entry.toString() + ": " + id);
                dataAccess.recordLogicalId(resourceType, id, entry.getJob().getResourceBundleId(), entry.getLineNumber());
                success = true;
            }
        } finally {
            // if this is the last operation for a job, then the entire
            // job will be marked as complete
            entry.getJob().operationComplete(success);
        }
    }

    /**
     * 
     */
    public void init() {
    }
}
