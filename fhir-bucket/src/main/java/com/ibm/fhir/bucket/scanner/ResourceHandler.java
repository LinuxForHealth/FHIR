/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bucket.scanner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.bucket.api.IResourceEntryProcessor;
import com.ibm.fhir.bucket.api.IResourceHandler;
import com.ibm.fhir.bucket.api.ResourceEntry;

/**
 * Calls the FHIR REST API to create resources, supported by a thread pool
 */
public class ResourceHandler implements IResourceHandler {
    private static final Logger logger = Logger.getLogger(ResourceHandler.class.getName());
    
    // The number of concurrent FHIR requests we allow
    private final int maxConcurrentFhirRequests;
    
    // The thread pool
    private final ExecutorService pool;

    // flow control so we don't overload the thread pool queue
    private final Lock lock = new ReentrantLock();
    private final Condition capacityCondition = lock.newCondition();
    
    // how many resources are currently queued or being processed
    private int inflight;
    
    // flag used to handle shutdown
    private volatile boolean running = true;

    // used to process each ResourceEntry within the thread pool
    private final IResourceEntryProcessor resourceEntryProcessor;
    
    /**
     * Public constructor
     * @param poolSize
     */
    public ResourceHandler(ExecutorService commonPool, int maxConcurrentFhirRequests, IResourceEntryProcessor rep) {
        this.maxConcurrentFhirRequests = maxConcurrentFhirRequests;
        this.pool = commonPool;
        this.resourceEntryProcessor = rep;
    }

    /**
     * Tell the ResourceHandler to shut down processing
     */
    public void signalStop() {
        if (running) {
            logger.info("Shutting down resource handler");
            this.running = false;
        }
        
        // Wake up anything which may be blocked
        lock.lock();
        try {
            capacityCondition.signalAll();
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Shut down all resource processing
     */
    public void waitForStop() {
        signalStop();
        
        // We don't own the pool, so we don't wait for it to shut down
    }

    @Override
    public boolean process(ResourceEntry entry) {
        boolean result = false;

        lock.lock();
        try {
            while (running && inflight >= maxConcurrentFhirRequests) {
                capacityCondition.await();
            }
            
            if (running) {
                inflight += entry.getCost(); // Grab the capacity while we're locked
                entry.getJob().addEntry(); // Add to row count so we can track when the job completes
                result = true;
            }
        } catch (InterruptedException x) {
            logger.info("Interrupted while waiting for capacity");
        }
        finally {
            lock.unlock();
        }

        // only submit to the pool if we have permission
        if (running && result) {
            pool.submit(() -> {
                try {
                    this.resourceEntryProcessor.process(entry);
                } catch (Exception x) {
                    // don't let exceptions propagate to the thread-pool
                    logger.log(Level.SEVERE, entry.toString(), x);
                } finally {
                    lock.lock();
                    try {
                        // Free up the capacity consumed by this entry
                        inflight -= entry.getCost();
                        capacityCondition.signalAll();
                    } finally {
                        lock.unlock();
                    }
                }
            });
        }
        
        return result;
    }
}
