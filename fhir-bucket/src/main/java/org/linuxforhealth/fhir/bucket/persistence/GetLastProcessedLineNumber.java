/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.bucket.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.database.utils.api.IDatabaseSupplier;
import org.linuxforhealth.fhir.database.utils.api.IDatabaseTranslator;
import org.linuxforhealth.fhir.database.utils.common.DataDefinitionUtil;

/**
 * Finds the greater line number successfully processed for a particular resource bundle
 */
public class GetLastProcessedLineNumber implements IDatabaseSupplier<Integer> {
    private static final Logger logger = Logger.getLogger(RegisterLoaderInstance.class.getName());

    // The schema holding the FHIRBUCKET tables
    private final String schemaName;

    // PK of the loader instance to update
    private final long resourceBundleId;

    // The version of file which generated the ids
    private final int version;

    /**
     * Public constructor
     * @param schemaName
     * @param resourceBundleId
     * @param version
     */
    public GetLastProcessedLineNumber(String schemaName, long resourceBundleId, int version) {
        this.schemaName = schemaName;
        this.resourceBundleId = resourceBundleId;
        this.version = version;
    }

    @Override
    public Integer run(IDatabaseTranslator translator, Connection c) {

        final String logicalResources = DataDefinitionUtil.getQualifiedName(schemaName, "logical_resources");
        final String resourceBundleLoads = DataDefinitionUtil.getQualifiedName(schemaName, "resource_bundle_loads");
        final String SQL = ""
                + "SELECT max(lr.line_number) "
                + "  FROM " + logicalResources + " lr, "
                + "       " + resourceBundleLoads + " bl "
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
