/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.flow.util;

import com.ibm.fhir.flow.api.ITrackerTicket;


/**
 * Represents a work item in progress and being tracked by a
 * CheckpointTracker
 */
public class CheckpointTrackerTicket implements ITrackerTicket {
    private final CheckpointTracker checkpointTracker;
    private final long requestId;

    /**
     * Public constructor
     * @param cpt
     * @param requestId
     */
    public CheckpointTrackerTicket(CheckpointTracker cpt, long requestId) {
        this.checkpointTracker = cpt;
        this.requestId = requestId;
    }

    @Override
    public void complete() {
        checkpointTracker.completed(this.requestId);
    }
}