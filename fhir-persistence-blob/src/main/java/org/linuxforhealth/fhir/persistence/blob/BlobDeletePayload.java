/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.persistence.blob;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceException;

import com.azure.core.http.rest.Response;
import com.azure.storage.blob.BlobAsyncClient;
import com.azure.storage.blob.BlobContainerAsyncClient;
import com.azure.storage.blob.models.BlobItem;
import com.azure.storage.blob.models.DeleteSnapshotsOptionType;

/**
 * DAO command to delete the configured Azure blob (holding a FHIR payload object)
 */
public class BlobDeletePayload {
    private static final Logger logger = Logger.getLogger(BlobDeletePayload.class.getName());
    final int resourceTypeId;
    final String logicalId;
    final Integer version;
    final String resourcePayloadKey;

    // The actual offload path, if we know it
    final String path;

    /**
     * Public constructor
     * @param resourceTypeId
     * @param logicalId
     * @param version can be null
     * @param resourcePayloadKey can be null
     * @param path
     */
    public BlobDeletePayload(int resourceTypeId, String logicalId, Integer version, String resourcePayloadKey, String path) {
        this.resourceTypeId = resourceTypeId;
        this.logicalId = logicalId;
        this.version = version;
        this.resourcePayloadKey = resourcePayloadKey;
        this.path = path;
    }

    /**
     * Execute this command against the given client
     * @param client
     * @throws FHIRPersistenceException if the delete fails
     */
    public void run(BlobManagedContainer client) throws FHIRPersistenceException {
        BlobContainerAsyncClient bcc = client.getClient();
        if (path != null) {
            // Use the given path instead of trying to reconstruct it
            deleteSingleBlob(bcc, path);
        } else {
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
                deleteBlobsUnderPrefix(bcc, blobName.toBlobPath());
            } else {
                deleteSingleBlob(bcc, blobName.toBlobPath());
            }
        }
    }

    /**
     * Delete all the blobs matching the prefix described by blobName
     * @param bcc
     * @param prefix
     * @throws FHIRPersistenceException
     */
    private void deleteBlobsUnderPrefix(BlobContainerAsyncClient bcc, String prefix) {
        logger.fine(() -> "Scanning all entries to erase under prefix: '" + prefix + "'");

        // List blobs using the key prefix
        bcc.listBlobsByHierarchy(prefix)
        .doOnNext(blobItem -> process(bcc, blobItem))
        .blockLast(); // wait for scan to complete
    }

    /**
     * Process the given item. If the item is a prefix (i.e. ends in a /) then
     * we recurse and process the contents of that path. Otherwise, delete the
     * blob at the fully qualified path.
     * @param bcc
     * @param blobItem
     * @return
     */
    private void process(BlobContainerAsyncClient bcc, BlobItem blobItem) {
        final Boolean isPrefix = blobItem.isPrefix();
        if (isPrefix != null && isPrefix) {
            // recurse down to the next hierarchy level
            logger.fine(() -> "Listing blobs under: '" + blobItem.getName() + "'");
            deleteBlobsUnderPrefix(bcc, blobItem.getName());
        } else {
            // actual blob, so we can display the value
            logger.fine(() -> "Fetching blob at: '" + blobItem.getName() + "'");
            deleteSingleBlob(bcc, blobItem.getName());
        }
    }


    /**
     * Delete the blob if it exists using the given blobPath.
     * @param bcc
     * @param blobPath
     * @return
     * @throws FHIRPersistenceException
     */
    private Response<Boolean> deleteSingleBlob(BlobContainerAsyncClient bcc, String blobPath) {
        
        BlobAsyncClient bc = bcc.getBlobAsyncClient(blobPath);
        try {
            // TODO return future so we can avoid waiting for response here
            logger.fine(() -> "Erasing blob: '" + blobPath + "'");
            return bc.deleteIfExistsWithResponse(DeleteSnapshotsOptionType.INCLUDE, null)
                    .toFuture()
                    .get(); // synchronous for now
        } catch (RuntimeException rx) {
            logger.log(Level.SEVERE, "Error deleting resource payload for blobPath=" + blobPath + "'");
            throw rx;
        } catch (Exception x) {
            logger.log(Level.SEVERE, "Error deleting resource payload for blobPath=" + blobPath + "'");
            throw new RuntimeException("Error deleting resource payload blob: '" + blobPath + "'", x);
        }
    }
}