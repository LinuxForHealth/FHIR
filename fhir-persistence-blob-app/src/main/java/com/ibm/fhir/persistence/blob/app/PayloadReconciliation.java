/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.blob.app;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.azure.core.http.rest.Response;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.database.utils.api.ITransaction;
import com.ibm.fhir.database.utils.pool.DatabaseSupport;
import com.ibm.fhir.persistence.blob.BlobContainerManager;
import com.ibm.fhir.persistence.blob.BlobDeletePayload;
import com.ibm.fhir.persistence.blob.BlobManagedContainer;
import com.ibm.fhir.persistence.blob.BlobResourceScanner;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.dao.api.ResourceRecord;
import com.ibm.fhir.persistence.jdbc.dao.impl.ResourceExistsDAO;

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
     * @param dbProperties
     * @param dbType
     * @param dryRun
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
     * @return the continuation token used to start scanning from where we left off
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
     * @param session
     * @param connecion
     * @param record
     */
    private void processPage(BlobManagedContainer bmc, List<ResourceRecord> page) {

        // Process each page in its own transaction
        try (ITransaction tx = dbSupport.getTransaction()) {
            try (Connection c = dbSupport.getConnection()) {
                for (ResourceRecord record: page) {
                    this.totalProcessed++;
                    if (logger.isLoggable(Level.FINE)) {
                        logger.fine(getLogRecord(record, "CHECK"));
                    }
                    
                    // Check that we have the resource in the RDBMS configured for
                    // this tenant
                    try {
                        ResourceExistsDAO dao = new ResourceExistsDAO(this.resourceTypeMaps, 
                            record.getResourceTypeId(), record.getLogicalId(), record.getVersion(), 
                            record.getResourcePayloadKey());
                        if (dao.run(c)) {
                            // Found the record, so log it
                            logger.info(getLogRecord(record, "OK"));
                        } else {
                            logger.info(getLogRecord(record, "ORPHAN"));
                            handleOrphanedRecord(bmc, record);
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
        }
    }

    /**
     * Get a consistent log entry description for the given ResourceRecord
     * and status string
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
     * @param session
     * @param record
     */
    private void handleOrphanedRecord(BlobManagedContainer bmc, ResourceRecord record) throws FHIRPersistenceException {
        final String action = this.dryRun ? "Would erase" : "Erasing";
        logger.info(String.format("%s orphaned payload %d/%s/%d [%s]", 
            action,
            record.getResourceTypeId(), record.getLogicalId(), 
            record.getVersion(), record.getResourcePayloadKey()));

        if (!this.dryRun) {
            BlobDeletePayload delete = new BlobDeletePayload(record.getResourceTypeId(), 
                record.getLogicalId(), record.getVersion(), 
                record.getResourcePayloadKey());
           Response<Void> response = delete.run(bmc);
           
           if (response.getStatusCode() == 200 || response.getStatusCode() == 404) {
               logger.fine(() -> getLogRecord(record, "DELETED"));
           } else {
               throw new FHIRPersistenceException(getLogRecord(record, "STATUS:" + response.getStatusCode()));
           }
        }
    }
}