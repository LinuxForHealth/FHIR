/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence;

import java.time.Instant;

/**
 * DTO for processing resource/last_modified time
 */
public class ResourceTime {

    // The database id for example patient_resources.resource_id
    private final long resourceId;

    // The last_modified value e.g. patient_resources.last_modified
    private final Instant lastModified;

    /**
     * Public constructor
     * @param resourceId
     * @param lastModified
     */
    public ResourceTime(long resourceId, Instant lastModified) {
        this.resourceId = resourceId;
        this.lastModified = lastModified;
    }

    /**
     * @return the resourceId
     */
    public long getResourceId() {
        return resourceId;
    }

    /**
     * @return the lastModified
     */
    public Instant getLastModified() {
        return lastModified;
    }
}