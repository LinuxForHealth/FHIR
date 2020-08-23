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
 * Updates the last seen timestamp of the LOADER_INSTANCES record
 * to indicate this particular instance is still alive
 */
public class GetLastProcessedLineNumber implements IDatabaseSupplier<Integer> {
    private static final Logger logger = Logger.getLogger(RegisterLoaderInstance.class.getName());

    // PK of the loader instance to update
    private final long resourceBundleId;
    
    /**
     * Public constructor
     * @param loaderInstanceId
     */
    public GetLastProcessedLineNumber(long resourceBundleId) {
        this.resourceBundleId = resourceBundleId;
    }

    @Override
    public Integer run(IDatabaseTranslator translator, Connection c) {
        
        final String SQL = ""
                + "SELECT max(line_number) "
                + "  FROM logical_resources "
                + " WHERE resource_bundle_id = ?";
        try (PreparedStatement ps = c.prepareStatement(SQL)) {
            ps.setLong(1, resourceBundleId);
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
                + resourceBundleId);
            throw translator.translate(x);
        }
    }


}
