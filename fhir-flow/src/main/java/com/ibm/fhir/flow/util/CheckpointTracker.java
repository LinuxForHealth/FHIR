/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.flow.util;

import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Encapsulates the tracking of a checkpoint we use for scanning the history
 * API. This is non-trivial, because work may be completed by different threads
 * in an arbitrary order, so we need to move the checkpoint only when all the
 * work before it has been completed.
 */
public class CheckpointTracker {
    // Requests in order of submission, which will be in increasing value (but not guaranteed contiguous)
    private final ConcurrentLinkedDeque<Long> queued = new ConcurrentLinkedDeque<>();

    // Requests we have completed, but have not yet been removed from queued because older requests are still in flight
    private final TreeSet<Long> completed = new TreeSet<>();

    // The max request id we've completed
    private long checkpoint = -1;

    // The id of the last request we were asked to track.
    private long lastRequestId = -1;

    /**
     * Add this request to the queue
     * @param requestId
     */
    public void track(long requestId) {
        if (requestId <= lastRequestId) {
            // we expect 
            throw new IllegalArgumentException("tracking requests must be submitted in order");
        }
        this.lastRequestId = requestId;
        
        // Need to sync on the same monitor whenever we access queued or completed objects
        synchronized(this.completed) {
            this.queued.add(requestId);
        }
    }

    /**
     * Add the list of requests to the queue using a single lock
     * @param requestIds
     */
    public void track(List<Long> requestIds) {
        synchronized (this.completed) {
            for (Long requestId: requestIds) {
                if (requestId <= lastRequestId) {
                    // we expect 
                    throw new IllegalArgumentException("tracking requests must be submitted in order");
                }
                this.lastRequestId = requestId;
                this.queued.add(requestId);
            }
        }
    }

    /**
     * Mark the request as completed.
     * @param requestId
     */
    public void completed(long requestId) {
        synchronized (this.completed) {
            if (queued.isEmpty()) {
                throw new IllegalStateException("completed queue is already empty");
            }
            Long oldest = queued.peek(); // can't be null
            if (oldest == requestId) {
                queued.remove();
                this.checkpoint = requestId;
                // See if we can move the checkpoint along by processing
                // any pending requests from the completed set
                Iterator<Long> dit = completed.iterator();
                while (dit.hasNext()) {
                    if (queued.isEmpty()) {
                        // underflow of the queued list...something broken with our implementation
                        throw new IllegalStateException("completed queue is already empty");
                    }
                    long rid = dit.next();
                    if (rid == queued.peek()) {
                        queued.remove();
                        dit.remove();
                        this.checkpoint = rid;
                    }
                }
            } else {
                // work not completed in order, so we need to stash this in an ordered
                // set so we can handle it later when the older work is completed
                completed.add(requestId);
            }
        }
    }

    /**
     * Get the current checkpoint value
     * @return
     */
    public long getCheckpoint() {
        synchronized(this.completed) {
            return this.checkpoint;
        }
    }

    /**
     * See if we have completed all the requests that we tracked
     * @return
     */
    public boolean isEmpty() {
        // always sync on the same monitor
        synchronized(this.completed) {
            return this.queued.isEmpty();
        }
    }
}