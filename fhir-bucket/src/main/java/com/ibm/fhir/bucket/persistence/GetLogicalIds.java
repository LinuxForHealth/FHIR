/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bucket.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.IDatabaseStatement;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.common.DataDefinitionUtil;

/**
 * Fetch a batch of roughly random patientIds. Should not be used for any
 * statistical purposes, because the randomness is not guaranteed.
 */
public class GetLogicalIds implements IDatabaseStatement {
    private static final Logger logger = Logger.getLogger(GetLogicalIds.class.getName());

    // The schema holding the FHIRBUCKET tables
    final String schemaName;

    // The list to fill with logical ids
    final List<String> logicalIds;

    // How many ids to fetch
    private final int maxCount;

    // The resource type
    private final String resourceType;

    /**
     * Public constructor
     * @param schemaName
     * @param logicalIds
     * @param resourceType
     * @param maxCount
     */
    public GetLogicalIds(String schemaName, List<String> logicalIds, String resourceType, int maxCount) {
        this.schemaName = schemaName;
        this.logicalIds = logicalIds;
        this.resourceType = resourceType;
        this.maxCount = maxCount;
    }

    @Override
    public void run(IDatabaseTranslator translator, Connection c) {

        // Fetch the list of patient ids up to the given max
        final String logicalResources = DataDefinitionUtil.getQualifiedName(this.schemaName, "logical_resources");
        final String resourceTypes = DataDefinitionUtil.getQualifiedName(this.schemaName, "resource_types");
        final String SQL = ""
                + "SELECT lr.logical_id "
                + "  FROM " + logicalResources + " lr,"
                + "       " + resourceTypes + " rt "
                + " WHERE lr.resource_type_id = rt.resource_type_id "
                + "   AND rt.resource_type = ? "
                + "FETCH FIRST ? ROWS ONLY;";
        try (PreparedStatement ps = c.prepareStatement(SQL)) {
            ps.setString(1, this.resourceType);
            ps.setInt(2, this.maxCount);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                logicalIds.add(rs.getString(1));
            }
        } catch (SQLException x) {
            // log this, but don't propagate values in the exception
            logger.log(Level.SEVERE, "Error selecting logical ids: " + SQL + "; resource_type=" + resourceType + ", fetch=" + maxCount);
            throw translator.translate(x);
        }
    }
}
