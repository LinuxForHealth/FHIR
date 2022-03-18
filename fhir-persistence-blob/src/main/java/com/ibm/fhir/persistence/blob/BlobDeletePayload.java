/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.blob;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.azure.core.http.rest.Response;
import com.azure.storage.blob.BlobAsyncClient;
import com.azure.storage.blob.BlobContainerAsyncClient;
import com.azure.storage.blob.models.DeleteSnapshotsOptionType;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;

/**
 * DAO command to delete the configured Azure blob (holding a FHIR payload object)
 */
public class BlobDeletePayload {
    private static final Logger logger = Logger.getLogger(BlobReadPayload.class.getName());
    final int resourceTypeId;
    final String logicalId;
    final Integer version;
    final String resourcePayloadKey;

    /**
     * Public constructor
     * @param resourceTypeId
     * @param logicalId
     * @param version can be null
     * @param resourcePayloadKey can be null
     */
    public BlobDeletePayload(int resourceTypeId, String logicalId, Integer version, String resourcePayloadKey) {
        this.resourceTypeId = resourceTypeId;
        this.logicalId = logicalId;
        this.version = version;
        this.resourcePayloadKey = resourcePayloadKey;
    }

    /**
     * Execute this command against the given client
     * @param client
     * @throws FHIRPersistenceException if the delete fails
     */
    public void run(BlobManagedContainer client) throws FHIRPersistenceException {
        BlobName.Builder builder = BlobName.builder();
        builder.resourceTypeId(this.resourceTypeId);
        builder.logicalId(this.logicalId);
        if (this.version != null) {
            builder.version(this.version);
            if (this.resourcePayloadKey != null) {
                builder.resourcePayloadKey(this.resourcePayloadKey);
            }
        }
        BlobName blobName = builder.build();
        
        if (blobName.isPartial()) {
            deleteBlobsForPrefix(client, blobName);
        } else {
            deleteSingleBlob(client, blobName.toBlobPath());
        }
    }

    /**
     * Delete all the blobs matching the prefix described by BlobName
     * @param client
     * @param blobName
     * @throws FHIRPersistenceException
     */
    private void deleteBlobsForPrefix(BlobManagedContainer client, BlobName blobName) throws FHIRPersistenceException {
        final String prefix = blobName.toBlobPath();
        logger.fine(() -> "Scanning all entries to erase under prefix: '" + prefix + "'");

        // List blobs using the key prefix
        BlobContainerAsyncClient bcc = client.getClient();
        bcc.listBlobsByHierarchy(prefix)
        .doOnNext(blobItem -> deleteSingleBlob(client, blobItem.getName()))
        .blockLast(); // wait for scan to complete
    }

    /**
     * Delete the blob using the given resourcePayloadKey
     * @param client
     * @param resourcePayloadKey
     * @return
     * @throws FHIRPersistenceException
     */
    private Response<Void> deleteSingleBlob(BlobManagedContainer client, String blobPath) {
        
        BlobAsyncClient bc = client.getClient().getBlobAsyncClient(blobPath);
        try {
            // TODO return future so we can avoid waiting for response here
            logger.fine(() -> "Erasing blob: '" + blobPath + "'");
            return bc.deleteWithResponse(DeleteSnapshotsOptionType.INCLUDE, null)
                    .toFuture()
                    .get(); // synchronous for now
        } catch (RuntimeException rx) {
            logger.log(Level.SEVERE, "Error deleting resource payload for blobPath=" + blobPath + "'");
            throw rx;
        } catch (Exception x) {
            logger.log(Level.SEVERE, "Error deleting resource payload for blobPath=" + blobPath + "'");
            throw new RuntimeException("delete payload blob", x);
        }
    }
}