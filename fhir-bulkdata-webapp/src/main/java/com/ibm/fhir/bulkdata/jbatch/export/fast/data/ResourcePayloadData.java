/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkdata.jbatch.export.fast.data;

import com.ibm.fhir.bulkdata.jbatch.export.fast.ResourcePayloadReader;

/**
 * A container holding the raw payload for a single resource. A list of these
 * objects is returned by the {@link ResourcePayloadReader#readItem()} method.
 */
public class ResourcePayloadData {
    private final String logicalId;
    private final byte[] rawPayload;

    /**
     * Public constructor
     * @param logicalId
     * @param rawPayload
     */
    public ResourcePayloadData(String logicalId, byte[] rawPayload) {
        this.logicalId = logicalId;
        this.rawPayload = rawPayload;
    }

    /**
     * @return the logicalId
     */
    public String getLogicalId() {
        return logicalId;
    }

    /**
     * @return the rawPayload
     */
    public byte[] getRawPayload() {
        return rawPayload;
    }
}
