/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.blob.app;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.database.utils.api.ITransaction;
import com.ibm.fhir.database.utils.pool.DatabaseSupport;
import com.ibm.fhir.persistence.blob.BlobContainerManager;
import com.ibm.fhir.persistence.blob.BlobDeletePayload;
import com.ibm.fhir.persistence.blob.BlobManagedContainer;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.cache.ResourceTypeMaps;
import com.ibm.fhir.persistence.jdbc.dao.api.ResourceRecord;
import com.ibm.fhir.persistence.jdbc.dao.impl.ResourceListExistsDAO;

/**
 * Implements an algorithm to scan the offload persistence store and check
 * that the RDBMS contains the corresponding record. As the RDBMS is the
 * source of truth, any records in the persistence store without a
 * corresponding record in the RDBMS should be deleted.
 */
public class PayloadReconciliation {
    private static final Logger logger = Logger.getLogger(PayloadReconciliation.class.getName());

    // The tenant id to use in the request context
    private final String tenantId;

    // The tenant's datastore (usually "default")
    private final String dsId;

    // Scan and report but don't delete
    private final boolean dryRun;

    // Tracking how many resource versions we process
    private long totalProcessed = 0;
    
    // Stop scanning after we hit this number of seconds
    private final int maxScanSeconds;

    // Access to the IBM FHIR Server RDBMS
    private final DatabaseSupport dbSupport;

    // Lookup support for resource types
    private final ResourceTypeMaps resourceTypeMaps;

    /**
     * Public constructor
     * @param tenantId
     * @param dsId
     * @param dbSupport
     * @param resourceTypeMaps
     * @param dryRun
     * @param maxScanSeconds
     */
    public PayloadReconciliation(String tenantId, String dsId, DatabaseSupport dbSupport, ResourceTypeMaps resourceTypeMaps, boolean dryRun, int maxScanSeconds) {
        this.tenantId = tenantId;
        this.dsId = dsId;
        this.dryRun = dryRun;
        this.maxScanSeconds = maxScanSeconds;
        this.dbSupport = dbSupport;
        this.resourceTypeMaps = resourceTypeMaps;
    }

    /**
     * Run the reconciliation process
     * 
     * @param continuationToken start from the given point, or the beginning if null
     * @return the continuation token used to start scanning from where we left off
     * @throws Exception
     */
    public String run(String continuationToken) throws Exception {
        long start = System.nanoTime();
        // Set up the request context for the configured tenant and datastore
        FHIRRequestContext.set(new FHIRRequestContext(tenantId, dsId));
        
        // Keep processing until we make no more progress
        BlobManagedContainer bmc = BlobContainerManager.getSessionForTenantDatasource();
        BlobResourceScanner scanner = new BlobResourceScanner(continuationToken, page -> processPage(bmc, page));
        scanner.run(bmc, maxScanSeconds);
        
        long end = System.nanoTime();
        double elapsed = (end - start) / 1e9;
        logger.info(String.format("Processed %d records in %5.1f seconds [rate %5.1f resources/second]", 
            totalProcessed, elapsed, totalProcessed/elapsed));

        return scanner.getContinuationToken();
    }
    
    /**
     * Consumer to process a page of records retrieved by the scanner
     * 
     * @param bmc
     * @param page
     */
    private void processPage(BlobManagedContainer bmc, List<ResourceRecord> page) {
        final long start = System.nanoTime();
        int orphanCount = 0;

        // Process each page in its own transaction
        try (ITransaction tx = dbSupport.getTransaction()) {
            try (Connection c = dbSupport.getConnection()) {
                this.totalProcessed += page.size();

                // Look for any resources missing in the RDBMS
                ResourceListExistsDAO dao = new ResourceListExistsDAO(this.resourceTypeMaps, page);
                List<ResourceRecord> missing = dao.run(c);
                orphanCount += missing.size();
                // We choose to iterate over the whole page and identify the missing
                // records so that we can also report on the records which we did find
                Set<String> missingKeys = missing.stream().map(ResourceRecord::getResourcePayloadKey).collect(Collectors.toSet());
                for (ResourceRecord record: page) {
                    try {
                        if (missingKeys.contains(record.getResourcePayloadKey())) {
                            logger.info(getLogRecord(record, "ORPHAN"));
                            handleOrphanedRecord(bmc, record);
                        } else {
                            // Only need to see OK when we're tracing
                            logger.fine(() -> getLogRecord(record, "OK"));
                        }
                    } catch (RuntimeException x) {
                        logger.log(Level.SEVERE, getLogRecord(record, "FAILED"), x);
                        tx.setRollbackOnly();
                        throw x;
                    } catch (Exception x) {
                        logger.log(Level.SEVERE, getLogRecord(record, "FAILED"), x);
                        tx.setRollbackOnly();
                        throw new RuntimeException(x); // wrap because we're used as a lambda
                    }
                }
            } catch (SQLException x) {
                tx.setRollbackOnly();
                throw dbSupport.getTranslator().translate(x);
            }
        } finally {
            double elapsed = (System.nanoTime() - start) / 1e9;
            double rate = page.size() / elapsed;
            logger.info(String.format("Page size: %d; took: %4.1f s; orphans: %d; rate: %5.0f resources/s)", page.size(), elapsed, orphanCount, rate));
        }
    }

    /**
     * Get a consistent log entry description for the given ResourceRecord
     * and status string
     * 
     * @param record
     * @param status a status string of 6 characters or less
     * @return
     */
    private String getLogRecord(ResourceRecord record, String status) {
        return String.format("[%6s] %d/%s/%d [%s]", status,
            record.getResourceTypeId(), record.getLogicalId(), 
            record.getVersion(), record.getResourcePayloadKey());
    }

    /**
     * Erase the record which exists in the offload payload store 
     * but not the RDBMS
     * 
     * @param bmc
     * @param record
     */
    private void handleOrphanedRecord(BlobManagedContainer bmc, ResourceRecord record) throws FHIRPersistenceException {
        final String action = this.dryRun ? "Would erase" : "Erasing";
        logger.info(String.format("%s orphaned payload %d/%s/%d/%s [path=%s]", 
            action,
            record.getResourceTypeId(), record.getLogicalId(), 
            record.getVersion(), record.getResourcePayloadKey(), record.getOffloadPath()));

        if (!this.dryRun) {
            BlobDeletePayload delete = new BlobDeletePayload(record.getResourceTypeId(), 
                record.getLogicalId(), record.getVersion(), 
                record.getResourcePayloadKey(), record.getOffloadPath());
           delete.run(bmc);
        }
    }
}