/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bucket.interop;

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

import com.ibm.fhir.bucket.client.FhirClient;
import com.ibm.fhir.bucket.scanner.DataAccess;

/**
 * Very simple emulation of a possible CMS Payer Interop workload. Adds some random
 * read activity to the system (FHIR server/database) so we can measure the impact
 * this has on ingestion and visa versa.
 */
public class CmsPayerInterop {
    private static final Logger logger = Logger.getLogger(CmsPayerInterop.class.getName());

    // The scenario we use to process each randomly picked patient
    private final IPatientScenario patientScenario;
    
    // the maximum number of requests we permit
    private final int maxConcurrentRequests;
    
    private final Lock lock = new ReentrantLock();
    private final Condition capacityCondition = lock.newCondition();
    
    private volatile int runningRequests;
    
    private volatile boolean running = true;

    // The thread running the main loop
    private Thread thread;
    
    private final int patientsPerBatch;
    
    // Access to the FHIRBATCH schema
    private final DataAccess dataAccess;

    // thread pool for processing requests
    private final ExecutorService pool = Executors.newCachedThreadPool();

    /**
     * Public constructor
     * @param client
     * @param maxConcurrentRequests
     */
    public CmsPayerInterop(DataAccess dataAccess, IPatientScenario patientScenario, int maxConcurrentRequests, int patientsPerBatch) {
        this.dataAccess = dataAccess;
        this.patientScenario = patientScenario;
        this.maxConcurrentRequests = maxConcurrentRequests;
        this.patientsPerBatch = patientsPerBatch;
    }
    

    /**
     * Start the main loop
     */
    public void init() {
        if (!running) {
            throw new IllegalStateException("Already shutdown");
        }

        thread = new Thread(() -> mainLoop());
        thread.start();
    }
    
    public void signalStop() {
        this.running = false;
        
        lock.lock();
        try {
            // wake up the thread if it's waiting on the capacity condition
            capacityCondition.signalAll();
        } finally {
            lock.unlock();
        }
        
        
        // try to break into any IO operation for a quicker exit
        if (thread != null) {
            thread.interrupt();
        }
        
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
     * The main loop in this object which starts when {@link #init()} is called
     * and will run until {@link #shutdown()}.
     */
    protected void mainLoop() {
        // The list of patientIds we process
        List<String> patientIdBuffer = new ArrayList<>();
        int bufferOffset = 0; // current index into the patientIds list
        while (this.running) {
            int freeCapacity = -1;
            int remainingInBuffer = patientIdBuffer.size() - bufferOffset;

            // Grab some patient ids and stick them into the buffer
            if (remainingInBuffer == 0) {
                patientIdBuffer.clear();
                dataAccess.selectRandomPatientIds(patientIdBuffer, this.patientsPerBatch);
                bufferOffset = 0;
                remainingInBuffer = patientIdBuffer.size();
            }

            // calculate how many requests we want to submit from the buffer, based
            // on the maxConcurrentRequests.
            int batchSize = 0;
            lock.lock();
            try {
                while (running && runningRequests == maxConcurrentRequests) {
                    capacityCondition.await(5, TimeUnit.SECONDS);
                }
                
                // Submit as many requests as we have available without exceeding 
                // the max concurrent request capacity
                freeCapacity = maxConcurrentRequests - runningRequests;
                batchSize = Math.min(remainingInBuffer, freeCapacity);
                runningRequests += batchSize;
            } catch (InterruptedException x) {
                if (running) {
                    throw new IllegalStateException("Interrupted but still running. Use #shutdown() instead.");
                }
            } finally {
                lock.unlock();
            }

            // Submit a request for each allocated patient to the thread pool
            for (int i=0; i<batchSize && running; i++) {
                final String patientId = patientIdBuffer.get(bufferOffset++);
                pool.submit(() -> processPatientThr(patientId));
            }
        }
    }

    /**
     * Here's where we start the scenario for our randomly picked patient
     * @param patientId
     */
    private void processPatientThr(String patientId) {
        try {
            logger.info("Processing patient: '" + patientId + "'");
            patientScenario.process(patientId);
        } catch (Exception x) {
            logger.log(Level.SEVERE, "Processing patient '" + patientId + "'" , x);
        } finally {
            // free up capacity
            lock.lock();
            try {
                runningRequests--;
                capacityCondition.signal();
            } finally {
                lock.unlock();
            }
        }
    }
}
