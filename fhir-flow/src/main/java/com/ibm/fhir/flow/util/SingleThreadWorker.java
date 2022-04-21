/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.flow.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A single thread which can asynchronously process work units submitted 
 * to a bound work-queue
 */
public class SingleThreadWorker implements Executor {
    private final Logger logger = Logger.getLogger(SingleThreadWorker.class.getName());

    private final BlockingQueue<Runnable> workQueue;
    private final Thread thread;
    private volatile boolean running;
    private volatile boolean drain;

    /**
     * Public constructor
     * @param capacity
     */
    public SingleThreadWorker(int capacity) {
        workQueue = new LinkedBlockingQueue<>(capacity);
        thread = new Thread(() -> mainLoop());
        thread.start();
    }

    @Override
    public void execute(Runnable command) {
        if (!running || drain) {
            // reject new requests if we've been shut down
            throw new IllegalStateException("already shut down");
        }

        try {
            workQueue.put(command);
        } catch (InterruptedException x) {
            throw new IllegalStateException(x);
        }
    }

    /**
     * The worker thread which processes requests from the workQueue
     */
    private void mainLoop() {
        this.running = true;
        while (this.running) {
            try {
                // use poll with a timeout instead of take(). This makes
                // it easier to do a shutdown when there's no work in the
                // queue
                Runnable workItem = workQueue.poll(1000, TimeUnit.MILLISECONDS);
                if (workItem != null) {
                    workItem.run();
                }
                // If the workQueue is empty and the drain flag is set, then we
                // know that because no more work can be submitted, it is safe
                // for us to shut down
                if (workItem == null && drain) {
                    this.running = false;
                }
            } catch (InterruptedException x) {
                // NOP - likely a shutdown in progress
            } catch (Throwable t) {
                logger.log(Level.SEVERE, "error running work item", t);
                // sink the error here, because we don't want the whole loop to fail
            }
        }
    }

    /**
     * Stop processing any new work, and try to interrupt the
     * current work item.
     */
    public void shutdown() {
        this.running = false;
        this.workQueue.clear();
        thread.interrupt();
    }

    /**
     * stop accepting new requests and stop running as soon
     * as the work queue is empty (drained)
     */
    public void setToDrain() {
        drain = true;
    }

    /**
     * Wait for the work queue to be drained
     */
    public void waitForDrain() {
        try {
            thread.join();
        } catch (InterruptedException x) {
            logger.warning("Interrupted waiting for worker thread to join");
        }
    }
}