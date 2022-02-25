/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.blob.app;

import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.azure.core.util.BinaryData;
import com.azure.storage.blob.BlobAsyncClient;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.persistence.blob.BlobContainerManager;
import com.ibm.fhir.persistence.blob.BlobManagedContainer;

/**
 * Do a plain read of the resource blob value and print it to stdout for debugging
 */
public class ReadBlobValue {
    private static final Logger logger = Logger.getLogger(ReadBlobValue.class.getName());
    private final String tenantId;
    private final String dsId;
    private final String blobName;

    /**
     * Public constructor
     * @param tenantId
     * @param dsId
     * @param blobName
     */
    public ReadBlobValue(String tenantId, String dsId, String blobName) {
        this.tenantId = tenantId;
        this.dsId = dsId;
        this.blobName = blobName;
    }

    /**
     * Create the container for the configured tenant and datasource pair
     */
    public void run() throws FHIRException {
        // Set up the request context for the configured tenant and datastore
        FHIRRequestContext.set(new FHIRRequestContext(tenantId, dsId));

        // Check to see if the container already exists, and if not, then 
        // issue the create container command and wait for the response
        BlobManagedContainer bmc = BlobContainerManager.getSessionForTenantDatasource();
        try {
            BlobAsyncClient bac = bmc.getClient().getBlobAsyncClient(blobName);
            BinaryData result = bac.downloadContent().block();
            String blobValue = new String(result.toBytes(), StandardCharsets.UTF_8);
            System.out.println(blobValue);
        } catch (RuntimeException x) {
            logger.log(Level.SEVERE, "failed to create container: " + bmc.getContainerName(), x);
            throw x;
        }
    }
}