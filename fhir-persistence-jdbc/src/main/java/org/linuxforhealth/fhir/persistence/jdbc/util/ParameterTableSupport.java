/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.linuxforhealth.fhir.database.utils.common.DataDefinitionUtil;

/**
 * Support functions for managing the search parameter value tables
 */
public class ParameterTableSupport {

    /**
     * Delete any current parameters from the whole-system and resource-specific parameter tables
     * for the given resourceType and logical_resource_id
     * @param conn
     * @param resourceType
     * @param v_logical_resource_id
     * @throws SQLException
     */
    public static void deleteFromParameterTables(Connection conn, String resourceType, long v_logical_resource_id) throws SQLException {
        final String tablePrefix = DataDefinitionUtil.assertValidName(resourceType);
        deleteFromParameterTable(conn, tablePrefix + "_str_values", v_logical_resource_id);
        deleteFromParameterTable(conn, tablePrefix + "_number_values", v_logical_resource_id);
        deleteFromParameterTable(conn, tablePrefix + "_date_values", v_logical_resource_id);
        deleteFromParameterTable(conn, tablePrefix + "_latlng_values", v_logical_resource_id);
        deleteFromParameterTable(conn, tablePrefix + "_resource_token_refs", v_logical_resource_id);
        deleteFromParameterTable(conn, tablePrefix + "_quantity_values", v_logical_resource_id);
        deleteFromParameterTable(conn, tablePrefix + "_profiles", v_logical_resource_id);
        deleteFromParameterTable(conn, tablePrefix + "_tags", v_logical_resource_id);
        deleteFromParameterTable(conn, tablePrefix + "_security", v_logical_resource_id);
        deleteFromParameterTable(conn, tablePrefix + "_ref_values", v_logical_resource_id);

        // delete any system level parameters we have for this resource
        deleteFromParameterTable(conn, "str_values", v_logical_resource_id);
        deleteFromParameterTable(conn, "date_values", v_logical_resource_id);
        deleteFromParameterTable(conn, "resource_token_refs", v_logical_resource_id);
        deleteFromParameterTable(conn, "logical_resource_profiles", v_logical_resource_id);
        deleteFromParameterTable(conn, "logical_resource_tags", v_logical_resource_id);
        deleteFromParameterTable(conn, "logical_resource_security", v_logical_resource_id);
    }

    /**
     * Delete all parameters for the given resourceId from the parameters table
     *
     * @param conn
     * @param tableName
     * @param logicalResourceId
     * @throws SQLException
     */
    private static void deleteFromParameterTable(Connection conn, String tableName, long logicalResourceId) throws SQLException {
        final String delStrValues = "DELETE FROM " + tableName + " WHERE logical_resource_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(delStrValues)) {
            // bind parameters
            stmt.setLong(1, logicalResourceId);
            stmt.executeUpdate();
        }

    }
}