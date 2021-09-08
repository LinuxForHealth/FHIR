/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.payload;

import java.util.List;

import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.util.InputOutputByteStream;

/**
 * Used for storing and retrieving payload data. Implementations are expected to be
 * locked to a particular tenant on instantiation.
 */
public interface FHIRPayloadPersistence {

    /**
     * Store the payload. The business key is the tuple {resourceTypeId, logicalId, version}
     * This data is stored directly, and should ideally be compressed.
     * @param resourceTypeId the unique int idenfifier for the resource type
     * @param logicalId the logical id of the resource
     * @param version the version of the resource
     * @param compressedPayload the serialized compressed payload data representing the FHIR resource
     */
    void storePayload(String resourceTypeName, int resourceTypeId, String logicalId, int version, InputOutputByteStream payloadStream) throws FHIRPersistenceException;

    /**
     * Retrieve the payload data for the given resourceTypeId, logicalId and version.
     * @param resourceTypeId the unique int idenfifier for the resource type
     * @param logicalId the logical identifier of the desired resource
     * @param version the specific version of the desired resource
     * @param elements to filter elements within the resource - can be null
     * @return the fhirResourcePayload exactly as it was provided to {@link #storePayload(String, int, String, int, byte[])}
     */
    <T extends Resource> T readResource(Class<T> resourceType, int resourceTypeId, String logicalId, int version, List<String> elements) throws FHIRPersistenceException;

    /**
     * Delete the payload item. This may be called to clean up after a failed transaction
     * @param resourceTypeId
     * @param logicalId
     * @param version
     * @throws FHIRPersistenceException
     */
    void deletePayload(int resourceTypeId, String logicalId, int version) throws FHIRPersistenceException;
}