/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bucket.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.IDatabaseStatement;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;

/**
 * Command to merge a collection of resources
 */
public class MergeResources implements IDatabaseStatement {
    private static final Logger logger = Logger.getLogger(MergeResources.class.getName());

    // The list of resource types we want to add
    private final List<ResourceRec> resources;

    /**
     * Public constructor
     * @param resourceType
     */
    public MergeResources(Collection<ResourceRec> resources) {
        this.resources = new ArrayList<>(resources); // copy for safety
    }

    @Override
    public void run(IDatabaseTranslator translator, Connection c) {

        // Support for PostgreSQL as well as Derby/Db2
        final String currentTimestamp = translator.currentTimestampString();
        final String dual = translator.dualTableName();
        final String source = dual == null ? "(SELECT 1)" : dual;

        // Use a bulk merge approach to insert resource types not previously
        // loaded
        final String merge = "MERGE INTO logical_resources tgt "
                + " USING " + source + " src "
                + "    ON tgt.resource_type_id = ? AND tgt.logical_id = ? "
                + " WHEN NOT MATCHED THEN INSERT (resource_type_id, logical_id, resource_bundle_load_id, line_number, "
                + "   created_tstamp, response_time_ms) VALUES (?, ?, ?, ?, " + currentTimestamp + ", NULL)";

        try (PreparedStatement ps = c.prepareStatement(merge)) {
            // Assume the list is small enough to process in one batch
            for (ResourceRec resource: resources) {
                ps.setInt(1, resource.getResourceTypeId());
                ps.setString(2, resource.getLogicalId());
                ps.setInt(3, resource.getResourceTypeId());
                ps.setString(4, resource.getLogicalId());
                ps.setLong(5, resource.getResourceBundleLoadId());
                ps.setInt(6, resource.getLineNumber());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException x) {
            // log this, but don't propagate values in the exception
            logger.log(Level.SEVERE, "Error adding resource types: " + merge + ";");
            throw translator.translate(x);
        }
    }
}