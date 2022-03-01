/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.jdbc.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ibm.fhir.persistence.jdbc.dao.api.IResourceTypeMaps;

/**
 * DAO to check if the configured resource exists
 */
public class ResourceExistsDAO {
    final int resourceTypeId;
    final String logicalId;
    final int versionId;
    final String resourcePayloadKey;
    final IResourceTypeMaps resourceTypeMaps;

    /**
     * Public constructor
     * @param resourceTypeMap
     * @param resourceTypeId
     * @param logicalId
     * @param versionId
     * @param resourcePayloadKey
     */
    public ResourceExistsDAO(IResourceTypeMaps resourceTypeMaps, int resourceTypeId, String logicalId, int versionId, String resourcePayloadKey) {
        this.resourceTypeMaps = resourceTypeMaps;
        this.resourceTypeId = resourceTypeId;
        this.logicalId = logicalId;
        this.versionId = versionId;
        this.resourcePayloadKey = resourcePayloadKey;
    }
 
    /**
     * Run the query to see if the resource version currently exists.
     * Does not care about deletion status, just that the row is there.
     * @param c
     * @return
     * @throws SQLException
     */
    public boolean run(Connection c) throws SQLException {
        final String resourceTypeName = resourceTypeMaps.getResourceTypeName(resourceTypeId);
        final String SQL =
                "SELECT 1 "
                + "  FROM " + resourceTypeName + "_RESOURCES R, " 
                + resourceTypeName + "_LOGICAL_RESOURCES LR "
                + " WHERE LR.LOGICAL_ID = ? "
                + "   AND R.LOGICAL_RESOURCE_ID = LR.LOGICAL_RESOURCE_ID "
                + "   AND R.VERSION_ID = ?"
                + "   AND R.RESOURCE_PAYLOAD_KEY = ?";

        boolean result = false;
        try (PreparedStatement ps = c.prepareStatement(SQL)) {
            ps.setString(1, logicalId);
            ps.setInt(2, versionId);
            ps.setString(3, resourcePayloadKey);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result = true;
            }
        }
        return result;
    }
}