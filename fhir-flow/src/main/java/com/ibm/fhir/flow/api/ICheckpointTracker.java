/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.flow.api;

/**
 * Supports tracking of work units so that a checkpoint can be written
 * periodically to indicate that all work up to a given point-in-time
 * has been completed.
 * 
 *
 * @param <T> the type of the value used to represent a checkpoint
 */
public interface ICheckpointTracker<T> {

    /**
     * Add this request to the queue
     * @param requestId
     * @param an {@link ITrackerTicket} which can be used to signal completion of the work item (thread-safe)
     * @param workItems how many individual pieces of work are associated with this checkpoint value
     */
    ITrackerTicket track(T checkpointValue, int workItems);

    /**
     * Get the current checkpoint value
     * @return
     */
    T getCheckpoint();

    /**
     * Get the total number of entries processed up to the current checkpoint
     * @return
     */
    long getProcessed();

    /**
     * Is the tracker queue empty
     * @return
     */
    boolean isEmpty();
}
