/*
 * (C) Copyright IBM Corp. 2020, 2021
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
import com.ibm.fhir.database.utils.common.DataDefinitionUtil;

/**
 * DAO to encapsulate all the SQL/DML used to retrieve and persist data
 * in the schema
 */
public class ClearStaleAllocations implements IDatabaseStatement {
    private static final Logger logger = Logger.getLogger(AllocateJobs.class.getName());

    // the schema holding the tables
    private final String schemaName;

    // The id of this loader instance so we can ignore self allocations
    private final long loaderInstanceId;

    // The number of MS before we consider a missed heartbeat
    private final long heartbeatTimeoutMs;

    // Number of seconds to wait before recycling a completed job
    private final int recycleSeconds;

    /**
     * Public constructor
     * @param schemaName
     * @param loaderInstanceId
     * @param heartbeatTimeoutMs
     * @param recycleSeconds
     */
    public ClearStaleAllocations(String schemaName, long loaderInstanceId, long heartbeatTimeoutMs, int recycleSeconds) {
        this.schemaName = schemaName;
        this.loaderInstanceId = loaderInstanceId;
        this.heartbeatTimeoutMs = heartbeatTimeoutMs;
        this.recycleSeconds = recycleSeconds;
    }

    @Override
    public void run(IDatabaseTranslator translator, Connection c) {

        // Firstly, mark as stopped any loader instances which are currently active
        // but haven't updated their heartbeat within the required time.
        final String loaderInstances = DataDefinitionUtil.getQualifiedName(schemaName, "loader_instances");
        final String currentTimestamp = translator.currentTimestampString();
        final String MARK = ""
                + "UPDATE " + loaderInstances
                + "   SET status = 'STOPPED' "
                + " WHERE loader_instance_id != ? "
                + "   AND status = 'RUNNING' "
                + "   AND " + translator.timestampDiff(currentTimestamp, "heartbeat_tstamp", null) + " >= ?"
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
        final String resourceBundles = DataDefinitionUtil.getQualifiedName(schemaName, "resource_bundles");
        final String resourceBundleLoads = DataDefinitionUtil.getQualifiedName(schemaName, "resource_bundle_loads");
        final String UPD = ""
                + "UPDATE " + resourceBundles
                + "   SET allocation_id = NULL, "
                + "       loader_instance_id = NULL "
                + " WHERE resource_bundle_id IN ( "
                + "     SELECT rb.resource_bundle_id "
                + "       FROM " + resourceBundles + " rb, "
                + "            " + loaderInstances + " li, "
                + "            " + resourceBundleLoads + " bl "
                + "      WHERE li.loader_instance_id = rb.loader_instance_id "
                + "        AND rb.allocation_id IS NOT NULL "
                + "        AND li.loader_instance_id != ? "
                + "        AND li.status = 'STOPPED' "
                + "        AND bl.allocation_id = rb.allocation_id "
                + "        AND bl.loader_instance_id = rb.loader_instance_id "
                + "        AND bl.resource_bundle_id = rb.resource_bundle_id "
                + "        AND bl.load_completed IS NULL "
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

        // If recycling is enabled, make any matching bundles look as though
        // they haven't yet been loaded. This occurs recycleSeconds after the
        // previous load completed. We bump the version number here too, which
        // will make incremental loads re-process everything, which is what we want
        if (this.recycleSeconds >= 0) {
            final String current = translator.currentTimestampString();
            final String diff = translator.timestampDiff(current, "bl.load_completed", null);
            final String RECYCLE = ""
                    + "UPDATE " + resourceBundles
                    + "   SET loader_instance_id = NULL, " // deallocate so it gets picked up again
                    + "       allocation_id = NULL, "
                    + "       version = version + 1 "      // pretend it's a new version of the file
                    + " WHERE resource_bundle_id IN ( "
                    + "     SELECT rb.resource_bundle_id "
                    + "       FROM " + resourceBundles + " rb, "
                    + "            " + resourceBundleLoads + " bl "
                    + "      WHERE bl.allocation_id = rb.allocation_id " // most recent allocation
                    + "        AND bl.loader_instance_id = rb.loader_instance_id "
                    + "        AND bl.resource_bundle_id = rb.resource_bundle_id "
                    + "        AND bl.load_completed IS NOT NULL " // just in case someone changes the diff expression
                    + "        AND " + diff + " >= ? "             // more than <?> seconds after the last load completed
                    + "     )";

            try (PreparedStatement ps = c.prepareStatement(RECYCLE)) {
                ps.setInt(1, recycleSeconds);
                int rowsAffected = ps.executeUpdate();

                if (rowsAffected > 0 && logger.isLoggable(Level.FINE)) {
                    logger.fine("Recycled bundles count: " + rowsAffected);
                }
            } catch (SQLException x) {
                logger.log(Level.SEVERE, RECYCLE, x);
                throw new DataAccessException("Recycle update failed");
            }
        }
    }
}