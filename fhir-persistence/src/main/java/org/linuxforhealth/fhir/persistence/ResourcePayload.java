/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Instant;
import java.util.Objects;

/**
 * Data carrier used by the Consumer to send back streaming payload result data to a caller
 */
public class ResourcePayload {
    private final String logicalId;
    private final Instant lastUpdated;
    private final long resourceId;
    private final InputStream decompressedPayload;

    /**
     * @param logicalId the non-null row identifier from the logical_resources table for this resource
     * @param lastUpdated the non-null lastUpdated time for this resource
     * @param resourceId the non-null row identifier from the x_resources table for this resource
     * @param decompressedPayload a non-null InputStream with the contents of a given resource in JSON format
     */
    public ResourcePayload(String logicalId, Instant lastUpdated, long resourceId, InputStream decompressedPayload) {
        Objects.requireNonNull(logicalId, "logicalId");
        Objects.requireNonNull(lastUpdated, "lastUpdated");
        Objects.requireNonNull(resourceId, "resourceId");
        Objects.requireNonNull(decompressedPayload, "decompressedPayload");
        this.logicalId = logicalId;
        this.lastUpdated = lastUpdated;
        this.resourceId = resourceId;
        this.decompressedPayload = decompressedPayload;
    }

    /**
     * Getter for the logicalId of this resource
     * @return
     */
    public String getLogicalId() {
        return this.logicalId;
    }

    /**
     * Getter for the resourceId
     * @return
     */
    public long getResourceId() {
        return this.resourceId;
    }

    /**
     * Copy the contents of the payload stream into the given {@link OutputStream}
     * @param os the OutputStream to transfer the bytes into
     * @return the number of bytes transferred into the {@link OutputStream}
     */
    public long transferTo(OutputStream os) throws IOException {
        try {
            long result = 0;

            // Do not increase the size of this buffer - doing so may have
            // a serious negative impact on performance
            byte[] buffer = new byte[4096];
            int len;

            while ((len = this.decompressedPayload.read(buffer)) >= 0) {
                if (len > 0) {
                    os.write(buffer, 0, len);
                    result += len;
                }
            }

            // how many bytes did we transfer
            return result;
        } finally {
            // Must close the source here...it's a GZipInputStream and leaks off-heap memory
            // if we fail to close it.
            decompressedPayload.close();
        }
    }

    /**
     * @return the lastUpdated
     */
    public Instant getLastUpdated() {
        return lastUpdated;
    }
}
