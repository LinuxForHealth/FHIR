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

import com.ibm.fhir.database.utils.api.IDatabaseStatement;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;

/**
 * Updates the LOAD_COMPLETED timestamp of the resource_bundles record
 */
public class MarkBundleDone implements IDatabaseStatement {
    private static final Logger logger = Logger.getLogger(MarkBundleDone.class.getName());

    // PK of the resource_bundle to update
    private final long resourceBundleId;
    
    // How many records failed when processing this file/bundle
    private final int failureCount;
    
    /**
     * Public constructor
     * @param loaderInstanceId
     */
    public MarkBundleDone(long resourceBundleId, int failureCount) {
        this.resourceBundleId = resourceBundleId;
        this.failureCount = failureCount;
    }

    @Override
    public void run(IDatabaseTranslator translator, Connection c) {
        
        final String DML = ""
                + "UPDATE resource_bundles "
                + "   SET load_completed = CURRENT TIMESTAMP,"
                + "       failure_count = ? "
                + " WHERE resource_bundle_id = ? "
                + "   AND load_completed IS NULL ";
        try (PreparedStatement ps = c.prepareStatement(DML)) {
            ps.setInt(1, failureCount);
            ps.setLong(2, resourceBundleId);
            ps.executeUpdate();
        } catch (SQLException x) {
            // log this, but don't propagate values in the exception
            logger.log(Level.SEVERE, "Error updating bundle done: " + DML + "; "
                + resourceBundleId);
            throw translator.translate(x);
        }
    }
}
