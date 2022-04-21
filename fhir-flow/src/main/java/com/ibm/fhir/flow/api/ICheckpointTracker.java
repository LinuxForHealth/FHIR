/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.flow.api;

import java.util.List;

/**
 * Supports tracking of work units so that a checkpoint can be written
 * periodically to indicate that all work up to a given point-in-time
 * has been completed.
 */
public interface ICheckpointTracker {

    /**
     * Add this request to the queue
     * @param requestId
     * @param an {@link ITrackerTicket} which can be used to signal completion of the work item (thread-safe)
     */
    ITrackerTicket track(long requestId);

    /**
     * Track all of the requestIds in the given list. The output list
     * will be in the same order as the input list. All entries will
     * be added using a single monitor lock, which should be more
     * efficient than synchronizing on each individual item
     * @param requestIds
     * @return
     */
    List<ITrackerTicket> track(List<Long> requestIds);

    /**
     * Get the current checkpoint value
     * @return
     */
    long getCheckpoint();

    /**
     * Is the tracker queue empty
     * @return
     */
    boolean isEmpty();
}
