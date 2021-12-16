/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.payload;

import java.util.List;

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
     * @param resourcePayloadKey the unique key used to tie this to the RDBMS record
     * @param resource the resource to store
     * @return the payload key details and future result status.
     */
    PayloadPersistenceResponse storePayload(String resourceTypeName, int resourceTypeId, String logicalId, int version, String resourcePayloadKey, Resource resource) throws FHIRPersistenceException;

    /**
     * Retrieve the payload data for the given resourceTypeId, logicalId and version. Synchronous.
     * @param resourceType the expected resource type class
     * @param rowResourceTypeName the resource type name of the resource read from the database (matching the resourceTypeId)
     * @param resourceTypeId the unique int idenfifier for the resource type name
     * @param logicalId the logical identifier of the desired resource
     * @param version the specific version of the desired resource
     * @param elements to filter elements within the resource - can be null
     * @return the fhirResourcePayload exactly as it was provided to {@link #storePayload(String, int, String, int, String, byte[])}
     */
    <T extends Resource> T readResource(Class<T> resourceType, String rowResourceTypeName, int resourceTypeId, String logicalId, int version, String resourcePayloadKey, List<String> elements) throws FHIRPersistenceException;

    /**
     * Delete the payload item. This may be called to clean up after a failed transaction or
     * by the reconciliation process when it finds an orphaned record.
     * when performing a hard delete on a resource.
     * @param resourceType
     * @param resourceTypeId
     * @param logicalId
     * @param version the version id, or null for all versions
     * @param resourcePayloadKey the key to make sure the entry matches the RDBMS record
     * @throws FHIRPersistenceException
     */
    void deletePayload(String resourceType, int resourceTypeId, String logicalId, Integer version, String resourcePayloadKey) throws FHIRPersistenceException;
}