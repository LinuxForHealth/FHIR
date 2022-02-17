/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.blob;

import java.util.logging.Logger;

import com.azure.core.http.rest.Response;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.models.AppendBlobItem;
import com.azure.storage.blob.specialized.AppendBlobClient;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.util.InputOutputByteStream;

/**
 * DAO command to store the configured payload in the Azure blob
 */
public class BlobStorePayload {
    private static final Logger logger = Logger.getLogger(BlobStorePayload.class.getName());
    final int resourceTypeId;
    final String logicalId;
    final int version;
    final String resourcePayloadKey;
    final InputOutputByteStream ioStream;

    /**
     * Public constructor
     * @param resourceTypeId
     * @param logicalId
     * @param version
     * @param resourcePayloadKey
     * @param ioStream
     */
    public BlobStorePayload(int resourceTypeId, String logicalId, int version, String resourcePayloadKey, InputOutputByteStream ioStream) {
        this.resourceTypeId = resourceTypeId;
        this.logicalId = logicalId;
        this.version = version;
        this.resourcePayloadKey = resourcePayloadKey;
        this.ioStream = ioStream;
    }

    /**
     * Execute this command against the given client
     * @param client
     * @throws FHIRPersistenceException
     */
    public Response<AppendBlobItem> run(BlobManagedContainer client) throws FHIRPersistenceException {
        final String blobPath = BlobPayloadSupport.getPayloadPath(resourceTypeId, logicalId, version, resourcePayloadKey);
        logger.fine(() -> "Payload storage path: " + blobPath);
        BlobClient bc = client.getClient().getBlobClient(blobPath);
        AppendBlobClient abc = bc.getAppendBlobClient();
        if (!abc.exists()) {
            abc.create(true);
        }
        Response<AppendBlobItem> response = abc.appendBlockWithResponse(ioStream.inputStream(), ioStream.size(), null, null, null, null);
//        BlobParallelUploadOptions uploadOptions = new BlobParallelUploadOptions(BinaryData.fromBytes(ioStream.getRawBuffer()));
//        Response<BlockBlobItem> response = bc.uploadWithResponse(uploadOptions, client.getProperties().getTimeout(), null);
        return response;
    }
}