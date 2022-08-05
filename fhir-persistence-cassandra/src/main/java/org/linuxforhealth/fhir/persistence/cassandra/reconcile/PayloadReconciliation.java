/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.persistence.cassandra.reconcile;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.datastax.oss.driver.api.core.CqlSession;
import org.linuxforhealth.fhir.config.FHIRRequestContext;
import org.linuxforhealth.fhir.database.utils.api.ITransaction;
import org.linuxforhealth.fhir.database.utils.pool.DatabaseSupport;
import org.linuxforhealth.fhir.persistence.cassandra.cql.DatasourceSessions;
import org.linuxforhealth.fhir.persistence.cassandra.payload.CqlDeletePayload;
import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceException;
import org.linuxforhealth.fhir.persistence.jdbc.dao.api.IResourceTypeMaps;
import org.linuxforhealth.fhir.persistence.jdbc.dao.api.ResourceRecord;
import org.linuxforhealth.fhir.persistence.jdbc.dao.impl.ResourceExistsDAO;

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

    // Provides access to database connections and transaction handling
    private final DatabaseSupport dbSupport;

    // Tracking how many resource versions we process
    private long totalProcessed = 0;
    
    // Mapping between resource type id and name
    final IResourceTypeMaps resourceTypeMaps;
    
    /**
     * Public constructor
     * @param tenantId
     * @param dsId
     * @param dbSupport
     * @param resourceTypeMaps
     * @param dryRun
     */
    public PayloadReconciliation(String tenantId, String dsId, DatabaseSupport dbSupport, IResourceTypeMaps resourceTypeMaps, boolean dryRun) {
        this.tenantId = tenantId;
        this.dsId = dsId;
        this.dryRun = dryRun;
        this.resourceTypeMaps = resourceTypeMaps;
        this.dbSupport = dbSupport;
    }

    /**
     * Run the reconciliation process
     */
    public void run() throws Exception {
        long start = System.nanoTime();
        // Set up the request context for the configured tenant and datastore
        FHIRRequestContext.set(new FHIRRequestContext(tenantId, dsId));
        
        // Keep processing until we make no more progress
        long firstToken = Long.MIN_VALUE;
        long lastToken;
        do {
            // To avoid hundreds of tiny transactions, we process one batch of fetches 
            // inside a single RDBMS transaction
            try (ITransaction tx = dbSupport.getTransaction()) {
                try (Connection c = dbSupport.getConnection()) {
                    lastToken = process(c, firstToken);
                    firstToken = lastToken + 1; // the next starting point
                } catch (SQLException x) {
                    tx.setRollbackOnly();
                    throw dbSupport.getTranslator().translate(x);
                }
            }
        } while (lastToken > Long.MIN_VALUE);

        long end = System.nanoTime();
        double elapsed = (end - start) / 1e9;
        logger.info(String.format("Processed %d records in %5.1f seconds [rate %5.1f resources/second]", 
            totalProcessed, elapsed, totalProcessed/elapsed));
    }
    
    /**
     * Scan and process resources within the given partition
     * @param firstToken
     * @return the last token read, or Long.MIN_VALUE when there aren't any more rows to scan
     */
    private long process(Connection c, long firstToken) throws Exception {
        CqlSession session = DatasourceSessions.getSessionForTenantDatasource();
        CqlScanResources scanner = new CqlScanResources(firstToken, r->processRecord(session, c, r));
        return scanner.run(session);
    }

    /**
     * Function to process a record retrieved by the scanner
     * @param session
     * @param connecion
     * @param record
     * @return true to continue scanning, false to stop scanning immediately
     */
    private Boolean processRecord(CqlSession session, Connection connection, ResourceRecord record) {
        boolean keepGoing = true;
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
            if (dao.run(connection)) {
                // Found the record, so log it
                logger.info(getLogRecord(record, "OK"));
            } else {
                logger.info(getLogRecord(record, "ORPHAN"));
                handleOrphanedRecord(session, record);
            }
        } catch (Exception x) {
            // This probably means there's an issue talking to the database,
            // or Cassandra, either of which is fatal so we just have to stop
            logger.log(Level.SEVERE, getLogRecord(record, "FAILED"), x);
            keepGoing = false;
        }
        return keepGoing;
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
    private void handleOrphanedRecord(CqlSession session, ResourceRecord record) throws FHIRPersistenceException {
        final String action = this.dryRun ? "Would erase" : "Erasing";
        logger.info(String.format("%s orphaned payload %d/%s/%d [%s]", 
            action,
            record.getResourceTypeId(), record.getLogicalId(), 
            record.getVersion(), record.getResourcePayloadKey()));

        if (!this.dryRun) {
            CqlDeletePayload delete = new CqlDeletePayload(record.getResourceTypeId(), 
                record.getLogicalId(), record.getVersion(), 
                record.getResourcePayloadKey());
           delete.run(session);
        }
    }
}
