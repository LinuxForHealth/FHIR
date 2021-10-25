/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bucket.interop;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.bucket.scanner.DataAccess;
import com.ibm.fhir.database.utils.thread.ThreadHandler;

/**
 * Very simple emulation of a possible interop workload. Adds some random
 * read activity to the system (FHIR server/database) so we can measure the impact
 * this has on ingestion and visa versa.
 */
public class InteropWorkload {
    private static final Logger logger = Logger.getLogger(InteropWorkload.class.getName());

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

    // How many patients should we load into the buffer
    private final int patientBufferSize;

    // How many times should we use the same set of patient ids?
    private final int bufferRecycleCount;

    // Access to the FHIRBATCH schema
    private final DataAccess dataAccess;

    // thread pool for processing requests
    private final ExecutorService pool = Executors.newCachedThreadPool();

    // for picking random patient ids
    private final SecureRandom random = new SecureRandom();

    private long statsResetTime = -1;
    private final AtomicInteger fhirRequests = new AtomicInteger();
    private final AtomicLong fhirRequestTime = new AtomicLong();
    private final AtomicInteger resourceCount = new AtomicInteger();

    // how many nanoseconds between stats reports
    private static final long STATS_REPORT_TIME = 10L * 1000000000L;

    /**
     * Public constructor
     * @param dataAccess
     * @param patientScenario
     * @param maxConcurrentRequests
     * @param patientBufferSize
     * @param bufferRecycleCount
     */
    public InteropWorkload(DataAccess dataAccess, IPatientScenario patientScenario, int maxConcurrentRequests, int patientBufferSize, int bufferRecycleCount) {
        if (bufferRecycleCount < 1) {
            throw new IllegalArgumentException("bufferRecycleCount must be >= 1");
        }
        this.dataAccess = dataAccess;
        this.patientScenario = patientScenario;
        this.maxConcurrentRequests = maxConcurrentRequests;
        this.patientBufferSize = patientBufferSize;
        this.bufferRecycleCount = bufferRecycleCount;
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

        // How many samples have we taken from the current buffer?
        int samples = 0;
        // The list of patientIds we process
        List<String> patientIdBuffer = new ArrayList<>(patientBufferSize);
        long statsStartTime = System.nanoTime();
        long nextStatsReport = statsStartTime + STATS_REPORT_TIME;
        while (this.running) {

            try {
                if (patientIdBuffer.isEmpty() || samples > patientIdBuffer.size() * this.bufferRecycleCount) {
                    // Refill the buffer of patient ids. There might be more available now
                    patientIdBuffer.clear();
                    samples = 0;
                    dataAccess.selectRandomPatientIds(patientIdBuffer, this.patientBufferSize);
                }

                // calculate how many requests we want to submit from the buffer, based
                // on the maxConcurrentRequests.
                int batchSize = 0;
                lock.lock();
                try {
                    while (running && runningRequests == maxConcurrentRequests) {
                        capacityCondition.await(5, TimeUnit.SECONDS);
                    }

                    // Submit as many requests as we have available. If we have a small
                    // patient buffer, then patients are more likely to be picked more than once
                    int freeCapacity = maxConcurrentRequests - runningRequests;
                    batchSize = Math.min(patientIdBuffer.size(), freeCapacity);
                    runningRequests += batchSize;
                } catch (InterruptedException x) {
                    if (running) {
                        running = false; // force termination so we don't get stuck here
                        throw new IllegalStateException("Interrupted but still running. Use #shutdown() instead.");
                    }
                } finally {
                    lock.unlock();
                }

                // Submit a request for each allocated patient to the thread pool
                for (int i=0; i<batchSize && running; i++) {
                    // pick a random patient id in the buffer
                    int bufferIndex = random.nextInt(patientIdBuffer.size());
                    final String patientId = patientIdBuffer.get(bufferIndex);
                    samples++; // track how many times we've sampled from the buffer
                    pool.submit(() -> processPatientThr(patientId));
                }

                long now = System.nanoTime();
                if (now >= nextStatsReport) {
                    // Time to report average throughput stats
                    double elapsed = (now - statsStartTime) / 1e9;
                    double avgResourcesPerSecond = this.resourceCount.get() / elapsed;
                    double avgResponseTime = Double.NaN;
                    double avgCallPerSecond = this.fhirRequests.get() / elapsed;
                    if (this.fhirRequests.get() > 0) {
                        avgResponseTime = this.fhirRequestTime.get() / 1e9 / this.fhirRequests.get();
                    }

                    logger.info(String.format("STATS: FHIR=%7.1f calls/s, rate=%7.1f resources/s, response time=%5.3f s",
                        avgCallPerSecond, avgResourcesPerSecond, avgResponseTime));

                    // Reset the stats for the next report window
                    statsStartTime = now;
                    nextStatsReport = now + STATS_REPORT_TIME;
                    this.fhirRequestTime.set(0);
                    this.fhirRequests.set(0);
                    this.resourceCount.set(0);
                }
            } catch (Exception x) {
                // log and snooze a little before we try again
                logger.severe("Error in main loop: " + x.getMessage());
                ThreadHandler.safeSleep(ThreadHandler.MINUTE);
            }
        }
    }

    /**
     * Here's where we start the scenario for our randomly picked patient
     * @param patientId
     */
    private void processPatientThr(String patientId) {
        try {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("Processing patient: '" + patientId + "'");
            }

            patientScenario.process(patientId, fhirRequests, fhirRequestTime, resourceCount);
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
