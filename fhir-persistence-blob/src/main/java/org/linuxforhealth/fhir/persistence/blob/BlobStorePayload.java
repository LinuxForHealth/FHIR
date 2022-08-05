/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.persistence.blob;

import java.nio.ByteBuffer;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

import com.azure.storage.blob.BlobAsyncClient;
import com.azure.storage.blob.options.BlobParallelUploadOptions;
import com.azure.storage.blob.specialized.BlockBlobAsyncClient;
import org.linuxforhealth.fhir.model.resource.OperationOutcome.Issue;
import org.linuxforhealth.fhir.model.type.code.IssueSeverity;
import org.linuxforhealth.fhir.model.type.code.IssueType;
import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceException;
import org.linuxforhealth.fhir.persistence.payload.PayloadPersistenceResult;
import org.linuxforhealth.fhir.persistence.util.InputOutputByteStream;

import reactor.core.publisher.Flux;

/**
 * DAO command to store the configured payload in the Azure blob
 */
public class BlobStorePayload {
    private static final Logger logger = Logger.getLogger(BlobStorePayload.class.getName());
    
    // The maximum size of a single block blob upload call
    private static final long MAX_BLOB_UPLOAD_BYTES = BlockBlobAsyncClient.MAX_UPLOAD_BLOB_BYTES_LONG;

    // The normalized resource type id from the RDBMS resource_types table
    final int resourceTypeId;

    // The resource id (logical identifier)
    final String logicalId;

    // The resource version as an integer
    final int version;

    // The key to make this particular resource payload record unique
    final String resourcePayloadKey;

    // The resource payload bytes to be uploaded
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
    public CompletableFuture<PayloadPersistenceResult> run(BlobManagedContainer client) throws FHIRPersistenceException {
        
        if (ioStream.size() > MAX_BLOB_UPLOAD_BYTES) {
            FHIRPersistenceException x = new FHIRPersistenceException("Resource payload size cannot exceed " + MAX_BLOB_UPLOAD_BYTES + " bytes");
            x.withIssue(Issue.builder().code(IssueType.TOO_LONG).severity(IssueSeverity.ERROR).diagnostics("Resource too large for payload offload").build());
            throw x;
        }
        final String blobPath = BlobPayloadSupport.getPayloadPath(resourceTypeId, logicalId, version, resourcePayloadKey);
        logger.fine(() -> "Payload storage path: " + blobPath);
        BlobAsyncClient bc = client.getClient().getBlobAsyncClient(blobPath);

        // Reactor pipeline to upload 
        // RequestConditions can be null because we want create-or-replace
        Flux<ByteBuffer> data = Flux.just(ioStream.wrap());
        return bc.uploadWithResponse(new BlobParallelUploadOptions(data))
            .map(response -> new PayloadPersistenceResult(response.getStatusCode() == 201 ? PayloadPersistenceResult.Status.OK : PayloadPersistenceResult.Status.FAILED))
            .toFuture();
    }
}