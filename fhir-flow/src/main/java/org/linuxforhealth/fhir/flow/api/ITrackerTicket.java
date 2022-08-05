/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.flow.api;


/**
 * Returned by the ICheckpointTracker. Can be used by the caller to indicate
 * that the work unit being tracked has completed
 */
public interface ITrackerTicket {

    /**
     * Mark the work item being tracked by this ticket as complete
     */
    void complete();
}
