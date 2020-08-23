/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bucket.scanner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpStatus;

import com.ibm.fhir.bucket.api.ResourceEntry;
import com.ibm.fhir.bucket.api.ResourceIdValue;
import com.ibm.fhir.bucket.client.FhirClient;
import com.ibm.fhir.bucket.client.FhirServerResponse;
import com.ibm.fhir.bucket.client.PostResource;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Bundle.Entry.Response;
import com.ibm.fhir.model.resource.Resource;

/**
 * Calls the FHIR REST API to create resources, supported by a thread pool
 */
public class ResourceHandler {
    private static final Logger logger = Logger.getLogger(ResourceHandler.class.getName());
    private static final int BATCH_SIZE = 200;
    
    // Nanos in a millisecond
    private static final long NANOS_MS = 1000000;

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
            // Add this so we can track when the job completes
            entry.getJob().addEntry();
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
    public void processThr(ResourceEntry re) {
        
        boolean success = false;
        try {
            Resource resource = re.getResource();
            final String resourceType = resource.getClass().getSimpleName();
            logger.info("Processing resource: " + resourceType);
            
            // Build a post request for the resource and send to FHIR
            long start = System.nanoTime();
            PostResource post = new PostResource(resource);
            FhirServerResponse response = post.run(fhirClient);
            long end = System.nanoTime();
            switch (response.getStatusCode()) {
            case HttpStatus.SC_OK:
            case HttpStatus.SC_CREATED:
                String locn = response.getLocationHeader();
                if (response.getResource() != null) {
                    // Process the response bundle
                    success = processResponseResource(re, response.getResource());
                } else if (locn != null) {
                    if (locn.startsWith("https://")) {
                        // the response was empty, so in this case we need to extract the id from
                        // the location header
                        int responseTimeMs = (int)((end - start) / NANOS_MS);
                        success = processLocation(re, locn, responseTimeMs);
                    } else {
                        logger.warning("FHIR bad location format [" + re.toString() + "]: " + 
                                locn);
                    }
                    
                } else {
                    logger.warning("FHIR request id not found [" + re.toString() + "]: " + 
                            response.getStatusCode() + " " + response.getStatusMessage());
                }
                break;
            default:
                logger.warning("FHIR request failed [" + re.toString() + "]: " + 
                        response.getStatusCode() + " " + response.getStatusMessage());
                break;
            }
        } catch (Throwable x) {
            logger.log(Level.SEVERE, re.toString(), x);
        } finally {
            // Signal the processing is complete for this entry
            re.getJob().operationComplete(success);
        }
    }

    /**
     * Process the bundle we received in the FHIR POST response to extract all the ids
     * Synthetic example:
        {           
            "entry": [
                {
                    "response": {
                        "etag": "W/\"1\"",
                        "id": "1740ce473c9-aecca6ca-6824-44a0-a8d8-4cfd230e0309",
                        "lastModified": "2020-08-20T17:22:12.554128Z",
                        "location": "Patient/1740ce473c9-aecca6ca-6824-44a0-a8d8-4cfd230e0309/_history/1",
                        "status": "201"
                    }
                },
                {
                    "response": {
                        "etag": "W/\"1\"",
                        "id": "1740ce47574-fb9b6b7e-15a4-4abc-bc33-f6b4fdb3d1e3",
                        "lastModified": "2020-08-20T17:22:12.980788Z",
                        "location": "Organization/1740ce47574-fb9b6b7e-15a4-4abc-bc33-f6b4fdb3d1e3/_history/1",
                        "status": "201"
                    }
                },
                ...
            ],  
            "resourceType": "Bundle",
            "type": "transaction-response"
        }
     * 
     * @param bundle
     * @return
     */
    private boolean processResponseResource(ResourceEntry re, Resource resource) {
        boolean result;
        
        if (Bundle.class.isAssignableFrom(resource.getClass())) {
            Bundle bundle = resource.as(Bundle.class);
            result = processResponseBundle(re, bundle);
        } else {
            logger.severe("Resource is not a bundle. Skipping: " + resource.getClass().getSimpleName());
            result = false;
        }
        
        
        return result;
    }
    
    private boolean processResponseBundle(ResourceEntry re, Bundle bundle) {
        
        // Extract the location from every entry in the bundle. Collect them
        // together so that we can make a single batch insert into the database
        // which is going to be a lot more efficient than individual inserts
        List<ResourceIdValue> idValues = new ArrayList<>();
        for (Bundle.Entry entry: bundle.getEntry()) {
            Response response = entry.getResponse();
            if (response != null) {
                if (response.getLocation() != null && response.getLocation().getValue() != null) {
                    String locn = response.getLocation().getValue();
                    logger.info("New resource: " + locn);
                    ResourceIdValue rid = getResourceIdValue(locn);
                    if (rid != null) {
                        idValues.add(rid);
                    }
                }
            }
        }

        processResourceIdValues(re, idValues);
        return idValues.size() > 0;
    }

    /**
     * Process the list of resource ids as a batch
     * @param re
     * @param idValues
     * @return
     */
    private void processResourceIdValues(ResourceEntry re, List<ResourceIdValue> idValues) {
        dataAccess.recordLogicalIds(re.getJob().getResourceBundleId(), re.getLineNumber(), idValues, BATCH_SIZE);
    }
    /**
     * Parse the location to create a {@link ResourceIdValue} DTO object.
     * The location can take one of two forms:
     *   "Patient/1740ce473c9-aecca6ca-6824-44a0-a8d8-4cfd230e0309/_history/1"
     *   "https://localhost:9443/fhir-server/api/v4/DiagnosticReport/173eed87a99-605de23b-266d-4b4d-b64f-31e769fda112/_history/1"
     * @param location
     * @return
     */
    private ResourceIdValue getResourceIdValue(String location) {
        ResourceIdValue result;
    
        String[] parts = location.split("/");
        if (parts.length == 10) {
            String resourceType = parts[6];
            String id = parts[7];
            result = new ResourceIdValue(resourceType, id);
        } else if (parts.length == 4) {
            String resourceType = parts[0];
            String id = parts[1];
            result = new ResourceIdValue(resourceType, id);
        } else {
            result = null;
        }
        
        return result;
    }
    
    private boolean processLocation(ResourceEntry re, String location, int responseTimeMs) {
        boolean result = false;
        // the response was empty, so in this case we need to extract the id from
        // the location header, which means cracking the string into parts:
        // https://localhost:9443/fhir-server/api/v4/DiagnosticReport/173eed87a99-605de23b-266d-4b4d-b64f-31e769fda112/_history/1
        String[] parts = location.split("/");
        if (parts.length == 10) {
            String resourceType = parts[6];
            String id = parts[7];
            logger.info("[" +re.toString() + "] new " + resourceType + "/" + id);
            dataAccess.recordLogicalId(resourceType, id, re.getJob().getResourceBundleId(), re.getLineNumber(), responseTimeMs);
            result = true;
        }
        
        return result;
    }

    /**
     * 
     */
    public void init() {
    }
}
