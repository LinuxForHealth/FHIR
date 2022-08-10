/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.persistence.blob.app;

import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.config.FHIRRequestContext;
import org.linuxforhealth.fhir.exception.FHIRException;
import org.linuxforhealth.fhir.persistence.blob.BlobContainerManager;
import org.linuxforhealth.fhir.persistence.blob.BlobManagedContainer;
import org.linuxforhealth.fhir.persistence.blob.BlobName;
import org.linuxforhealth.fhir.persistence.jdbc.dao.api.IResourceTypeMaps;

import com.azure.core.util.BinaryData;
import com.azure.storage.blob.BlobAsyncClient;
import com.azure.storage.blob.BlobContainerAsyncClient;
import com.azure.storage.blob.models.BlobItem;

/**
 * Do a plain read of the resource blob value and print it to stdout for debugging
 */
public class ReadBlobValue {
    private static final Logger logger = Logger.getLogger(ReadBlobValue.class.getName());
    private final String tenantId;
    private final String dsId;
    private final IResourceTypeMaps resourceTypeMaps;
    private final BlobName blobName;

    /**
     * Public constructor
     * @param tenantId
     * @param dsId
     * @param resourceTypeMaps
     * @param blobName
     */
    public ReadBlobValue(String tenantId, String dsId, IResourceTypeMaps resourceTypeMaps, BlobName blobName) {
        this.tenantId = tenantId;
        this.dsId = dsId;
        this.resourceTypeMaps = resourceTypeMaps;
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
            if (blobName.isPartial()) {
                // resourceTypeId/logicalId/version
                final String prefix = blobName.toBlobPath();
                listBlobs(bmc.getClient(), prefix);
            } else {
                // fully qualified, so we can go straight to the blob
                // resourceTypeId/logicalId/version/resourcePayloadKey
                displaySingleValue(bmc.getClient(), this.blobName);
            }
        } catch (RuntimeException x) {
            logger.log(Level.SEVERE, "blob fetch failed: " + bmc.getContainerName() + ":" + blobName, x);
            throw x;
        }
    }

    /**
     * When the blobName does not include the resourcePayloadKey, list all matches
     * @param bcc
     * @param prefix
     * @throws FHIRException
     */
    private void listBlobs(BlobContainerAsyncClient bcc, String prefix) {
        logger.info("Scanning all entries with prefix: '" + prefix + "'");

        // List blobs using the key prefix
        bcc.listBlobsByHierarchy(prefix)
        // .filter(blobItem -> !blobItem.getName().endsWith("/")) // skip interim paths in the hierarchy
            .doOnNext(blobItem -> process(bcc, blobItem))
            .blockLast(); // wait for scan to complete
    }

    /**
     * Process the given item. If the item is a prefix (i.e. ends in a /) then
     * we recurse and list the contents of that path. Otherwise, retrieve the
     * blob content at the location and display its value as text.
     * @param bcc
     * @param blobItem
     * @return
     */
    private void process(BlobContainerAsyncClient bcc, BlobItem blobItem) {
        final Boolean isPrefix = blobItem.isPrefix();
        if (isPrefix != null && isPrefix) {
            // recurse down to the next hierarchy level
            logger.fine(() -> "Listing blobs under: '" + blobItem.getName() + "'");
            listBlobs(bcc, blobItem.getName());
        } else {
            // actual blob, so we can display the value
            logger.fine(() -> "Fetching blob at: '" + blobItem.getName() + "'");
            displaySingleValue(bcc, blobItem.getName());
        }
    }

    /**
     * Display the blob content for the given full blob path
     * @param bcc
     * @param fullBlobPath
     */
    private void displaySingleValue(BlobContainerAsyncClient bcc, String fullBlobPath) {
        // Need to parse the blob path provided by the database
        BlobAsyncClient bac = bcc.getBlobAsyncClient(fullBlobPath);
        BinaryData result = bac.downloadContent().block();
        String blobValue = new String(result.toBytes(), StandardCharsets.UTF_8);
        // Use the human-readable form of the blob-name, which translates the resource type id
        // to the resource type name
        BlobName blobName = BlobName.create(resourceTypeMaps, fullBlobPath);
        System.out.println(blobName.toString() + ": " + blobValue);
    }

    /**
     * Display the blob content for the given blob name (which is the complete
     * key, including the resourcePayloadKey).
     * @param bcc
     * @param fullBlobName
     */
    private void displaySingleValue(BlobContainerAsyncClient bcc, BlobName fullBlobName) {
        BlobAsyncClient bac = bcc.getBlobAsyncClient(fullBlobName.toBlobPath());
        BinaryData result = bac.downloadContent().block();
        String blobValue = new String(result.toBytes(), StandardCharsets.UTF_8);
        // Use the human-readable form of the blob-name, which translates the resource type id
        // to the resource type name
        System.out.println(fullBlobName.toString() + ": " + blobValue);
    }
}