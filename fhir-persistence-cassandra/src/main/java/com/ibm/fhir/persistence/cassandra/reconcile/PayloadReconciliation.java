/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.cassandra.reconcile;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.datastax.oss.driver.api.core.CqlSession;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.database.utils.model.DbType;
import com.ibm.fhir.database.utils.pool.DatabaseSupport;
import com.ibm.fhir.persistence.cassandra.cql.DatasourceSessions;

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
    
    /**
     * Public constructor
     * @param tenantId
     * @param dsId
     * @param dbProperties
     * @param dbType
     * @param dryRun
     */
    public PayloadReconciliation(String tenantId, String dsId, Properties dbProperties, DbType dbType, boolean dryRun) {
        this.tenantId = tenantId;
        this.dsId = dsId;
        this.dryRun = dryRun;
        this.dbSupport = new DatabaseSupport(dbProperties, dbType);
    }

    /**
     * Run the reconciliation process
     */
    public void run() throws Exception {
        // TODO obviously
        run("SMdc");
    }
    
    private void run(String partitionId) throws Exception {
        long start = System.nanoTime();
        // Set up the request context for the configured tenant and datastore
        FHIRRequestContext.set(new FHIRRequestContext(tenantId, dsId));
        long totalProcessed = 0;
        
        // Keep processing until we make no more progress
        long firstToken = Long.MIN_VALUE;
        long lastToken;
        do {
            lastToken = process(firstToken);
            firstToken = lastToken + 1; // the next starting point
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
    private long process(long firstToken) throws Exception {
        CqlSession session = DatasourceSessions.getSessionForTenantDatasource();
        CqlScanResources scanner = new CqlScanResources(firstToken, r->processRecord(r));
        return scanner.run(session);
    }

    /**
     * Function to process a record retrieved by the scanner
     * @param record
     * @return true to continue scanning, false to stop scanning immediately
     */
    private Boolean processRecord(ResourceRecord record) {
        if (logger.isLoggable(Level.FINE)) {
            logger.fine(String.format("Checking %d/%s/%d [%s]", 
                record.getResourceTypeId(), record.getLogicalId(), 
                record.getVersion(), record.getResourcePayloadKey()));
        }
        return true;
    }
}
