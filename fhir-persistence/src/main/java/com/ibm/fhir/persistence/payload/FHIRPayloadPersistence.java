/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.payload;

import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;

/**
 * Used for storing and retrieving payload data. Implementations are expected to be
 * locked to a particular tenant on instantiation.
 */
public interface FHIRPayloadPersistence {

    /**
     * Store the payload. The business key is the tuple {resourceTypeId, logicalId, version}
     * but implementations may use to the partitionId as part of a physical key to distribute
     * the data. In practical terms, the partitionId could be something like patientId (or a
     * function of patientId) such that all data for a given patient would be stored in the
     * same partition. This data is stored directly, and should ideally be compressed.
     * @param partitionId the partition in which the payload data is to be located
     * @param resourceTypeId the unique int idenfifier for the resource type
     * @param logicalId the logical id of the resource
     * @param version the version of the resource
     * @param compressedPayload the serialized compressed payload data representing the FHIR resource
     */
    public void storePayload(String partitionId, int resourceTypeId, String logicalId, int version, byte[] compressedPayload) throws FHIRPersistenceException;

    /**
     * Retrieve the payload data for the given partitionId, resourceTypeId, logicalId and version.
     * @param partitionId the partition in which the payload data is located
     * @param resourceTypeId the unique int idenfifier for the resource type
     * @param logicalId the logical identifier of the desired resource
     * @param version the specific version of the desired resource
     * @return the fhirResourcePayload exactly as it was provided to {@link #storePayload(String, int, String, int, byte[])}
     */
    public <T extends Resource> T readResource(String partitionId, int resourceTypeId, String logicalId, int version) throws FHIRPersistenceException;
}
