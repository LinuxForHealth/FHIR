/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.bucket.persistence;

import org.linuxforhealth.fhir.bucket.api.ResourceEntry;
import org.linuxforhealth.fhir.bucket.scanner.DataAccess;

/**
 * Handles the recording of logical ids which can arrive from many threads.
 * To avoid unnecessary synchronization, each thread gets its own batch which
 * will be submitted to the database when it reaches a threshold or a timeout
 * expires (so we don't have ids sitting around in memory for too long before
 * that are persisted. For the purposes of this application, it isn't critical
 * if we lose one or two ids now and then (program crash/forceful termination).
 * 
 * TODO. A future option to avoid data loss but maintain performance would be
 * to push the ids to Redis in a pub/sub model.
 */
public class LogicalIdRecorder {

    // access to the loader bucket persistence layer
    private final DataAccess dataAccess;
    
    /**
     * Public constructor
     * @param dataAccess
     */
    public LogicalIdRecorder(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    /**
     * Save the new logical id and map it to the bundle it came from
     * @param job
     * @param logicalId
     */
    public void recordLogicalId(ResourceEntry entry, String logicalId, int responseTimeMs) {
        dataAccess.recordLogicalId(entry.getResource().getClass().getSimpleName(), logicalId,
            entry.getJob().getResourceBundleLoadId(), entry.getLineNumber(), responseTimeMs);
    }
}
