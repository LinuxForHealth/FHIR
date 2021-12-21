/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.blob;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.azure.core.http.rest.Response;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.models.DeleteSnapshotsOptionType;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;

/**
 * DAO command to delete the configured Azure blob (holding a FHIR payload object)
 */
public class BlobDeletePayload {
    private static final Logger logger = Logger.getLogger(BlobReadPayload.class.getName());
    final int resourceTypeId;
    final String logicalId;
    final int version;
    final String resourcePayloadKey;

    /**
     * Public constructor
     * @param resourceTypeId
     * @param logicalId
     * @param version
     * @param resourcePayloadKey
     */
    public BlobDeletePayload(int resourceTypeId, String logicalId, int version, String resourcePayloadKey) {
        this.resourceTypeId = resourceTypeId;
        this.logicalId = logicalId;
        this.version = version;
        this.resourcePayloadKey = resourcePayloadKey;
    }

    /**
     * Execute this command against the given client
     * @param client
     * @throws FHIRPersistenceException
     */
    public Response<Void> run(BlobManagedContainer client) throws FHIRPersistenceException {
        final StringBuilder blobNameBuilder = new StringBuilder();
        blobNameBuilder.append(resourceTypeId);
        blobNameBuilder.append("/");
        blobNameBuilder.append(logicalId);
        blobNameBuilder.append("/");
        blobNameBuilder.append(version);
        blobNameBuilder.append("/");
        blobNameBuilder.append(resourcePayloadKey);
        BlobClient bc = client.getClient().getBlobClient(blobNameBuilder.toString());

        try {
            Response<Void> response = bc.deleteWithResponse(DeleteSnapshotsOptionType.INCLUDE, null, client.getProperties().getTimeout(), null);
            return response;
        } catch (Exception x) {
            logger.log(Level.SEVERE, "Error deleting resource payload for resourceTypeId=" + resourceTypeId
                + ", logicalId=" + logicalId + ", resourcePayloadKey = " + resourcePayloadKey);
            throw new FHIRPersistenceException("Error deleting resource payload");
        }
    }
}