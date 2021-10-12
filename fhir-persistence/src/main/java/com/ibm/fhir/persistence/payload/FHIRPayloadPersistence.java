/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.payload;

import java.util.List;
import java.util.concurrent.Future;

import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;

/**
 * Used for storing and retrieving payload data. Implementations are expected to be
 * locked to a particular tenant on instantiation.
 */
public interface FHIRPayloadPersistence {

    /**
     * Store the payload. The business key is the tuple {resourceTypeId, logicalId, version}
     * This data is stored directly, and should ideally be compressed.
     * @param resourceTypeName the type name of the resource
     * @param resourceTypeId the database id assigned to this resource type
     * @param logicalId the logical id of the resource
     * @param version the version of the resource
     * @param resource the resource to store
     * @return a {@link Future} holding the payload key and status.
     */
    Future<PayloadKey> storePayload(String resourceTypeName, int resourceTypeId, String logicalId, int version, Resource resource) throws FHIRPersistenceException;

    /**
     * Retrieve the payload data for the given resourceTypeId, logicalId and version. Synchronous.
     * @param resourceType the expected resource type class
     * @param resourceTypeId the unique int idenfifier for the resource type
     * @param logicalId the logical identifier of the desired resource
     * @param version the specific version of the desired resource
     * @param elements to filter elements within the resource - can be null
     * @return the fhirResourcePayload exactly as it was provided to {@link #storePayload(String, int, String, int, byte[])}
     */
    <T extends Resource> T readResource(Class<T> resourceType, int resourceTypeId, String logicalId, int version, List<String> elements) throws FHIRPersistenceException;

    /**
     * Fetch the resource directly using the payload key. This is faster than {@link #readResource(Class, int, String, int, List)}
     * because the payload persistence implementation can use the {@link PayloadKey} to directly address the location where the
     * payload is stored. Allows async implementations.
     * @param <T>
     * @param resourceType
     * @param payloadKey
     * @return a Future that will hold the resource after it has been read
     * @throws FHIRPersistenceException
     */
    <T extends Resource> Future<T> readResource(Class<T> resourceType, PayloadKey payloadKey) throws FHIRPersistenceException;

    /**
     * Delete the payload item. This may be called to clean up after a failed transaction
     * @param resourceTypeId
     * @param logicalId
     * @param version
     * @throws FHIRPersistenceException
     */
    void deletePayload(int resourceTypeId, String logicalId, int version) throws FHIRPersistenceException;
}