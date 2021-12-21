/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.blob;

import com.azure.core.http.rest.Response;
import com.azure.core.util.BinaryData;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.models.BlockBlobItem;
import com.azure.storage.blob.options.BlobParallelUploadOptions;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.util.InputOutputByteStream;

/**
 * DAO command to store the configured payload in the Azure blob
 */
public class BlobStorePayload {
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
    public Response<BlockBlobItem> run(BlobManagedContainer client) throws FHIRPersistenceException {
        final StringBuilder blobNameBuilder = new StringBuilder();
        blobNameBuilder.append(resourceTypeId);
        blobNameBuilder.append("/");
        blobNameBuilder.append(logicalId);
        blobNameBuilder.append("/");
        blobNameBuilder.append(version);
        blobNameBuilder.append("/");
        blobNameBuilder.append(resourcePayloadKey);
        BlobClient bc = client.getClient().getBlobClient(blobNameBuilder.toString());
        
        BlobParallelUploadOptions uploadOptions = new BlobParallelUploadOptions(BinaryData.fromBytes(ioStream.getRawBuffer()));
        Response<BlockBlobItem> response = bc.uploadWithResponse(uploadOptions, client.getProperties().getTimeout(), null);
        
        return response;
    }
}