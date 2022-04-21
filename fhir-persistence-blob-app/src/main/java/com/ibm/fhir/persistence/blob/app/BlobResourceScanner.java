/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.blob.app;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;

import com.azure.core.http.rest.PagedResponse;
import com.azure.storage.blob.BlobContainerAsyncClient;
import com.azure.storage.blob.models.BlobItem;
import com.ibm.fhir.persistence.blob.BlobManagedContainer;
import com.ibm.fhir.persistence.blob.BlobPayloadSupport;
import com.ibm.fhir.persistence.jdbc.dao.api.ResourceRecord;

/**
 * Scans resource payload records which have been stored in the blob
 * store. Used by the reconciliation process to identify records
 * existing in the blob store which do not have a corresponding
 * record in the RDBMS.
 */
public class BlobResourceScanner {
    private static final Logger logger = Logger.getLogger(BlobResourceScanner.class.getName());

    // Where to start scanning from
    private String continuationToken;

    // A function used to process a page of blob items
    private final Consumer<List<ResourceRecord>> pageHandler;

    /**
     * Public constructor
     * @param continuationToken the Azure Blob continuation token to mark the scan start point
     * @param pageHandler
     */
    public BlobResourceScanner(String continuationToken, Consumer<List<ResourceRecord>> pageHandler) {
        this.continuationToken = continuationToken;
        this.pageHandler = pageHandler;
    }

    /**
     * Get the latest continuationToken which can be used to checkpoint processing
     * of the container
     * @return
     */
    public String getContinuationToken() {
        return this.continuationToken;
    }
    /**
     * Start scanning using the given {@link BlobManagedContainer}
     * @param bmc
     * @maxScanSeconds
     * @return the marker which can be used to resume the scan
     */
    public void run(BlobManagedContainer bmc, int maxScanSeconds) {
        BlobContainerAsyncClient containerClient = bmc.getClient();

        if (continuationToken == null) {
            // start scanning from the very first page
            containerClient.listBlobs()
                .byPage(1000)
                .take(Duration.ofSeconds(maxScanSeconds)) // keep processing for this many seconds
                .doOnNext(page -> processPage(page))
                .blockLast(); // wait for scan to complete
        } else {
            containerClient.listBlobs()
                .byPage(continuationToken, 1000) // limit page size do avoid overloading page processing
                .take(Duration.ofSeconds(maxScanSeconds)) // keep processing for this many seconds
                .doOnNext(page -> processPage(page))
                .blockLast(); // wait for scan to complete
        }
    }

    /**
     * Process a page of entries, which is a useful boundary
     * for a database transaction.
     * @param page
     */
    protected void processPage(PagedResponse<BlobItem> page) {
        List<ResourceRecord> records = new ArrayList<>();
        for (BlobItem item: page.getElements()) {
            ResourceRecord rr = BlobPayloadSupport.buildResourceRecordFromPath(item.getName());
            if (rr != null) {
                records.add(rr);
            }
        }

        pageHandler.accept(records);

        // Update the continuation token so we know where the
        // next page should start
        this.continuationToken = page.getContinuationToken();
        if (this.continuationToken != null) {
            // write the continuation to the log so that it can be used as a checkpoint
            // in case processing is interrupted for some reason
            logger.info("__CONTINUATION_TOKEN__ = " + this.continuationToken); // make it easy for a script to identify and extract
        }
    }
}
