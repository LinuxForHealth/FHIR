/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.blob;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.azure.core.http.rest.Response;
import com.azure.storage.blob.BlobAsyncClient;
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
        final String blobPath = BlobPayloadSupport.getPayloadPath(resourceTypeId, logicalId, version, resourcePayloadKey);
        
        BlobAsyncClient bc = client.getClient().getBlobAsyncClient(blobPath);
        try {
            // TODO return future so we can avoid waiting for response here
            return bc.deleteWithResponse(DeleteSnapshotsOptionType.INCLUDE, null)
                    .toFuture()
                    .get(); // synchronous for now
        } catch (Exception x) {
            logger.log(Level.SEVERE, "Error deleting resource payload for blobPath=" + blobPath + "'");
            throw new FHIRPersistenceException("delete payload blob", x);
        }
    }
}