/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.payload;

/**
 * Strategy for obtaining a partition name to use when storing the payload
 * in a partitioned datastore.
 */
public interface FHIRPayloadPartitionStrategy {

    /**
     * Ask for the partition name from this strategy
     * @return
     */
    String getPartitionName(String resourceType, String logicalId);
}