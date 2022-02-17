/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.blob;

import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.specialized.AppendBlobClient;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.persistence.FHIRPersistenceSupport;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;

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
     * Execute this command against the given client
     * @param resourceType
     * @param client
     * @throws FHIRPersistenceException
     */
    public <T extends Resource> T run(Class<T> resourceType, BlobManagedContainer client) throws FHIRPersistenceException {
        T result;
        final String blobPath = BlobPayloadSupport.getPayloadPath(resourceTypeId, logicalId, version, resourcePayloadKey);
        logger.fine(() -> "Reading payload using storage path: " + blobPath);
        BlobClient bc = client.getClient().getBlobClient(blobPath);
        AppendBlobClient abc = bc.getAppendBlobClient();
        try (InputStream is = abc.openInputStream()) {
            result = FHIRPersistenceSupport.parse(resourceType, is, this.elements, this.compress);
        } catch (Exception x) {
            logger.log(Level.SEVERE, "Error reading resource, blobPath='" + blobPath + "'", x);
            throw new FHIRPersistenceException("Error reading resource payload");
        }

        return result;
    }
}