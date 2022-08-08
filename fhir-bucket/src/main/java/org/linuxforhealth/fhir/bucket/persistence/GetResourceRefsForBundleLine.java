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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.bucket.api.ResourceRef;
import org.linuxforhealth.fhir.database.utils.api.IDatabaseSupplier;
import org.linuxforhealth.fhir.database.utils.api.IDatabaseTranslator;
import org.linuxforhealth.fhir.database.utils.common.DataDefinitionUtil;

/**
 * Fetches the list of resources which have been created from processing a given
 * line of a bundle.
 */
public class GetResourceRefsForBundleLine implements IDatabaseSupplier<List<ResourceRef>> {
    private static final Logger logger = Logger.getLogger(GetResourceRefsForBundleLine.class.getName());

    private final String schemaName;

    // PK of the loader instance to update
    private final long resourceBundleId;

    // The version of file which generated the ids
    private final int version;

    private final int lineNumber;

    /**
     * Public constructor
     * @param schemaName
     * @param resourceBundleId
     * @param version
     * @param lineNumber
     */
    public GetResourceRefsForBundleLine(String schemaName, long resourceBundleId, int version, int lineNumber) {
        this.schemaName = schemaName;
        this.resourceBundleId = resourceBundleId;
        this.version = version;
        this.lineNumber = lineNumber;
    }

    @Override
    public List<ResourceRef> run(IDatabaseTranslator translator, Connection c) {
        List<ResourceRef> result = new ArrayList<>();
        final String logicalResources = DataDefinitionUtil.getQualifiedName(schemaName, "logical_resources");
        final String resourceBundleLoads = DataDefinitionUtil.getQualifiedName(schemaName, "resource_bundle_loads");
        final String resourceTypes = DataDefinitionUtil.getQualifiedName(schemaName, "resource_types");
        final String SQL = ""
                + "SELECT rt.resource_type, lr.logical_id "
                + "  FROM " + logicalResources + " lr, "
                + "       " + resourceBundleLoads + " bl,"
                + "       " + resourceTypes + " rt "
                + " WHERE bl.resource_bundle_id = ? "
                + "   AND bl.version = ? "
                + "   AND lr.line_number = ? "
                + "   AND lr.resource_bundle_load_id = bl.resource_bundle_load_id "
                + "   AND rt.resource_type_id = lr.resource_type_id ";
        try (PreparedStatement ps = c.prepareStatement(SQL)) {
            ps.setLong(1, resourceBundleId);
            ps.setInt(2, version);
            ps.setInt(3, lineNumber);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(new ResourceRef(rs.getString(1), rs.getString(2)));
            }
        } catch (SQLException x) {
            // log this, but don't propagate values in the exception
            logger.log(Level.SEVERE, "Error fetching resource refs for line: " + SQL + "; "
                + resourceBundleId + ", " + version + ", " + lineNumber);
            throw translator.translate(x);
        }

        return result;
    }
}