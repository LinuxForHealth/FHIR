/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.payload;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;

/**
 * Used for storing and retrieving payload data. Implementations are expected to be
 * locked to a particular tenant on instantiation.
 */
public interface FHIRPayloadPersistence {

    /**
     * Store the payload
     * @param partitionId the partition in which the payload data is to be located
     * @param resourceTypeId the unique int idenfifier for the resource type
     * @param logicalId
     * @param data
     * @return the next version of this logicalId
     */
    public int storePayload(String partitionId, int resourceTypeId, String logicalId, byte[] data) throws FHIRPersistenceException;

    /**
     * Retrieve the payload data for the given logicalId and version
     * @param partitionId the partition in which the payload data is to be located
     * @param resourceTypeId the unique int idenfifier for the resource type
     * @param logicalId
     * @param version
     * @return
     */
    public byte[] readPayload(String partitionId, int resourceTypeId, String logicalId, int version) throws FHIRPersistenceException;
}
