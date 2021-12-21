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
import java.util.zip.GZIPInputStream;

import com.azure.core.util.BinaryData;
import com.azure.storage.blob.BlobClient;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.payload.PayloadPersistenceHelper;
import com.ibm.fhir.persistence.util.InputOutputByteStream;

/**
 * DAO command to store the configured payload in the Azure blob
 */
public class BlobReadPayload {
    private static final Logger logger = Logger.getLogger(BlobReadPayload.class.getName());
    final int resourceTypeId;
    final String logicalId;
    final int version;
    final String resourcePayloadKey;
    private final List<String> elements;

    /**
     * Public constructor
     * @param resourceTypeId
     * @param logicalId
     * @param version
     * @param resourcePayloadKey
     */
    public BlobReadPayload(int resourceTypeId, String logicalId, int version, String resourcePayloadKey, List<String> elements) {
        this.resourceTypeId = resourceTypeId;
        this.logicalId = logicalId;
        this.version = version;
        this.resourcePayloadKey = resourcePayloadKey;
        this.elements = elements;
    }

    /**
     * Execute this command against the given client
     * @param client
     * @throws FHIRPersistenceException
     */
    public <T extends Resource> T run(Class<T> resourceType, BlobManagedContainer client) throws FHIRPersistenceException {
        T result;
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
            BinaryData binaryData = bc.downloadContent();
            InputOutputByteStream readStream = new InputOutputByteStream(binaryData.toBytes(), 0);
            try (InputStream in = new GZIPInputStream(readStream.inputStream())) {
                result = PayloadPersistenceHelper.parse(resourceType, in, this.elements);
            }
        } catch (Exception x) {
            logger.log(Level.SEVERE, "Error reading resource, resourceTypeId=" + resourceTypeId
                + ", logicalId=" + logicalId);
            throw new FHIRPersistenceException("Error reading resource payload");
        }
        
        
        return result;
    }
}