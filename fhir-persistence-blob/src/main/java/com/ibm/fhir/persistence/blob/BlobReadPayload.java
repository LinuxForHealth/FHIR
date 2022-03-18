/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.blob;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

import com.azure.core.util.BinaryData;
import com.azure.storage.blob.BlobAsyncClient;
import com.azure.storage.blob.models.BlobStorageException;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.persistence.FHIRPersistenceSupport;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.exception.FHIRPersistenceResourceNotFoundException;

/**
 * DAO command to store the configured payload in the Azure blob
 */
public class BlobReadPayload {
    private static final Logger logger = Logger.getLogger(BlobReadPayload.class.getName());
    private final int resourceTypeId;
    private final String logicalId;
    private final int version;
    private final String resourcePayloadKey;
    private final List<String> elements;
    private final boolean compress;

    /**
     * Public constructor
     * @param resourceTypeId
     * @param logicalId
     * @param version
     * @param resourcePayloadKey
     * @param elements
     * @param compress
     */
    public BlobReadPayload(int resourceTypeId, String logicalId, int version, String resourcePayloadKey, List<String> elements, boolean compress) {
        this.resourceTypeId = resourceTypeId;
        this.logicalId = logicalId;
        this.version = version;
        this.resourcePayloadKey = resourcePayloadKey;
        this.elements = elements;
        this.compress = compress;
    }

    /**
     * Parse the downloaded content and transform into a FHIR resource
     * @param <T>
     * @param resourceType
     * @param data
     * @return
     */
    private static <T extends Resource> T transform(Class<T> resourceType, BinaryData data) {
        try {
            return FHIRPersistenceSupport.parse(resourceType, data.toStream(), null, false);
        } catch (Exception x) {
            throw new RuntimeException(x);
        }
    }

    /**
     * Execute this command against the given client
     * @param resourceType
     * @param client
     * @throws FHIRPersistenceException
     */
    public <T extends Resource> CompletableFuture<T> run(Class<T> resourceType, BlobManagedContainer client) throws FHIRPersistenceException {
        final String blobPath = BlobPayloadSupport.getPayloadPath(resourceTypeId, logicalId, version, resourcePayloadKey);
        logger.fine(() -> "Reading payload using storage path: " + blobPath);
        BlobAsyncClient bc = client.getClient().getBlobAsyncClient(blobPath);

        return bc.getAppendBlobAsyncClient()
                .downloadContent()
                .map(data -> transform(resourceType, data))
                .onErrorMap(BlobReadPayload::transformException)
                .toFuture();
    }

    /**
     * Transform the exception so that we can more easily interpret things like
     * 404 errors in upper layers without leaking information about our
     * implementation (separation of concerns).
     * @param x
     * @return
     */
    private static Throwable transformException(Throwable e) {
        if (e instanceof BlobStorageException) {
            BlobStorageException bse = (BlobStorageException)e;
            if (bse.getStatusCode() == 404) {
                // Blob doesn't exist. This likely means it has been erased, so we just return null
                // just as we would if we had tried to read this from the RDBMS record
                return new RuntimeException(new FHIRPersistenceResourceNotFoundException("resource not found"));
            } else {
                return e;
            }
        } else {
            return e;
        }
    }
}