/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bucket.interop;

import java.security.SecureRandom;
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

import com.ibm.fhir.bucket.client.FHIRBucketClient;
import com.ibm.fhir.database.utils.thread.ThreadHandler;

/**
 * Calls the $bench custom operator which applies some heavy CPU load to the target
 * system to help determine how much CPU capacity is available on
 * a particular node (and allow relative comparisons with other systems. This can
 * be useful when trying to identify bottlenecks in a given system. In this case,
 * no database work is involved and the network traffic to invoke the operation is
 * light, leaving CPU as the main component.
 */
public class BenchWorkload {
    private static final Logger logger = Logger.getLogger(BenchWorkload.class.getName());

    // the maximum number of requests we permit
    private final int maxConcurrentRequests;

    // The client we use to make the $bench operation call
    private final FHIRBucketClient client;

    // Parameter values used in the $bench operation call
    private final int benchThreads;
    private final int benchSize;

    private final Lock lock = new ReentrantLock();
    private final Condition capacityCondition = lock.newCondition();

    private volatile int runningRequests;

    private volatile boolean running = true;

    // The thread running the main loop
    private Thread thread;

    // thread pool for processing requests
    private final ExecutorService pool = Executors.newCachedThreadPool();

    // for picking random patient ids
    private final SecureRandom random = new SecureRandom();

    private final AtomicInteger fhirRequests = new AtomicInteger();
    private final AtomicLong fhirRequestTime = new AtomicLong();

    // how many nanoseconds between stats reports
    private static final long STATS_REPORT_TIME = 10L * 1000000000L;

    /**
     * Public constructor
     * @param client
     * @param maxConcurrentRequests
     * @param benchThreads
     * @param benchSize
     */
    public BenchWorkload(FHIRBucketClient client, int maxConcurrentRequests, int benchThreads, int benchSize) {
        this.client = client;
        this.maxConcurrentRequests = maxConcurrentRequests;
        this.benchThreads = benchThreads;
        this.benchSize = benchSize;
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

        long statsStartTime = System.nanoTime();
        long nextStatsReport = statsStartTime + STATS_REPORT_TIME;
        while (this.running) {
            try {
                // calculate how many requests we want to submit, based
                // on the maxConcurrentRequests.
                int batchSize = 0;
                lock.lock();
                try {
                    while (running && runningRequests == maxConcurrentRequests) {
                        capacityCondition.await(5, TimeUnit.SECONDS);
                    }

                    // Submit as many requests as we have available.
                    batchSize = maxConcurrentRequests - runningRequests;
                    runningRequests += batchSize;
                } catch (InterruptedException x) {
                    if (running) {
                        running = false; // force termination so we don't get stuck here
                        throw new IllegalStateException("Interrupted but still running. Use #shutdown() instead.");
                    }
                } finally {
                    lock.unlock();
                }

                // Submit each request to the pool
                for (int i=0; i<batchSize && running; i++) {
                    pool.submit(() -> submitRequestThr());
                }

                long now = System.nanoTime();
                if (now >= nextStatsReport) {
                    // Time to report average throughput stats
                    double elapsed = (now - statsStartTime) / 1e9;
                    double avgResponseTime = Double.NaN;
                    double avgCallPerSecond = this.fhirRequests.get() / elapsed;
                    if (this.fhirRequests.get() > 0) {
                        avgResponseTime = this.fhirRequestTime.get() / 1e9 / this.fhirRequests.get();
                    }

                    logger.info(String.format("STATS: FHIR=%7.1f calls/s, response time=%5.3f s",
                        avgCallPerSecond, avgResponseTime));

                    // Reset the stats for the next report window
                    statsStartTime = now;
                    nextStatsReport = now + STATS_REPORT_TIME;
                    this.fhirRequestTime.set(0);
                    this.fhirRequests.set(0);
                }
            } catch (Exception x) {
                // log and snooze a little before we try again
                logger.severe("Error in main loop: " + x.getMessage());
                ThreadHandler.safeSleep(ThreadHandler.MINUTE);
            }
        }
    }

    /**
     * Make the $bench call
     * @param patientId
     */
    private void submitRequestThr() {
        try {
            long start = System.nanoTime();
            CallBench call = new CallBench(benchThreads, benchSize);
            call.run(client);
            long elapsed = System.nanoTime() - start;
            this.fhirRequests.addAndGet(1);
            this.fhirRequestTime.addAndGet(elapsed);
        } catch (Exception x) {
            logger.log(Level.SEVERE, "$bench operation call failed", x);
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
