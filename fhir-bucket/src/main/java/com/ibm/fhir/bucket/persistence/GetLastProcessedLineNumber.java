/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bucket.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.IDatabaseSupplier;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;

/**
 * Finds the greater line number successfully processed for a particular resource bundle
 */
public class GetLastProcessedLineNumber implements IDatabaseSupplier<Integer> {
    private static final Logger logger = Logger.getLogger(RegisterLoaderInstance.class.getName());

    // PK of the loader instance to update
    private final long resourceBundleId;
    
    // The version of file which generated the ids
    private final int version;
    
    /**
     * Public constructor
     * @param loaderInstanceId
     */
    public GetLastProcessedLineNumber(long resourceBundleId, int version) {
        this.resourceBundleId = resourceBundleId;
        this.version = version;
    }

    @Override
    public Integer run(IDatabaseTranslator translator, Connection c) {
        
        final String SQL = ""
                + "SELECT max(lr.line_number) "
                + "  FROM logical_resources lr, "
                + "       resource_bundle_loads bl "
                + " WHERE bl.resource_bundle_id = ? "
                + "   AND bl.version = ? "
                + "   AND lr.resource_bundle_load_id = bl.resource_bundle_load_id ";
        try (PreparedStatement ps = c.prepareStatement(SQL)) {
            ps.setLong(1, resourceBundleId);
            ps.setInt(2, version);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int value = rs.getInt(1);
                if (rs.wasNull()) {
                    return null;
                } else {
                    return value;
                }
            } else {
                // shouldn't ever happen, of course
                throw new IllegalStateException("max() did not return a row!");
            }
        } catch (SQLException x) {
            // log this, but don't propagate values in the exception
            logger.log(Level.SEVERE, "Error fetching last processed line_number: " + SQL + "; "
                + resourceBundleId + ", " + version);
            throw translator.translate(x);
        }
    }
}
