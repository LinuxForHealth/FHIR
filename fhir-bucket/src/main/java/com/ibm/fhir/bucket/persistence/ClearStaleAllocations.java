/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bucket.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.DataAccessException;
import com.ibm.fhir.database.utils.api.IDatabaseStatement;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;

/**
 * DAO to encapsulate all the SQL/DML used to retrieve and persist data
 * in the schema
 */
public class ClearStaleAllocations implements IDatabaseStatement {
    private static final Logger logger = Logger.getLogger(AllocateJobs.class.getName());

    // The id of this loader instance so we can ignore self allocations
    private final long loaderInstanceId;

    // The number of MS before we consider a missed heartbeat
    private final long heartbeatTimeoutMs;

    /**
     * Public constructor
     */
    public ClearStaleAllocations(long loaderInstanceId, long heartbeatTimeoutMs) {
        this.loaderInstanceId = loaderInstanceId;
        this.heartbeatTimeoutMs = heartbeatTimeoutMs;
    }

    @Override
    public void run(IDatabaseTranslator translator, Connection c) {

        // Firstly, mark as stopped any loader instances which are currently active
        // but haven't updated their heartbeat within the required time.
        final String MARK = ""
                + "UPDATE loader_instances "
                + "   SET status = 'STOPPED' "
                + " WHERE loader_instance_id != ? "
                + "   AND status = 'RUNNING' "
                + "   AND " + translator.timestampDiff("CURRENT TIMESTAMP", "heartbeat_tstamp", null) + " >= ?"
                ;
        
        try (PreparedStatement ps = c.prepareStatement(MARK)) {
            ps.setLong(1, loaderInstanceId);
            ps.setLong(2, heartbeatTimeoutMs / 1000);
            int affectedRows = ps.executeUpdate();
            
            if (affectedRows > 0) {
                logger.info("Cleared RUNNING status record count: " + affectedRows);
            }
        } catch (SQLException x) {
            logger.log(Level.SEVERE, MARK, x);
            throw new DataAccessException("Mark stopped loader instances failed");
        }

        
        // Clear out any allocations for resource bundles for which the
        // assigned loader instance is considered dead but were not 
        // marked as complete. This will allow the bundles to be 
        // picked up again by another/new loader instance
        final String UPD = ""
                + "UPDATE resource_bundles "
                + "   SET allocation_id = NULL, "
                + "       loader_instance_id = NULL, "
                + "       load_started = NULL "
                + " WHERE resource_bundle_id IN ( "
                + "     SELECT resource_bundle_id "
                + "       FROM resource_bundles rb, "
                + "            loader_instances li  "
                + "      WHERE li.loader_instance_id = rb.loader_instance_id "
                + "        AND rb.load_completed IS NULL "
                + "        AND li.loader_instance_id != ? "
                + "        AND li.status = 'STOPPED' "
                + "     )";
        
        try (PreparedStatement ps = c.prepareStatement(UPD)) {
            ps.setLong(1, loaderInstanceId);
            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected > 0 && logger.isLoggable(Level.FINE)) {
                logger.fine("Cleared resource_bundles allocation count: " + rowsAffected);
            }
        } catch (SQLException x) {
            logger.log(Level.SEVERE, UPD, x);
            throw new DataAccessException("Clear allocation update failed");
        }
        
   }
}