/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.persistence.jdbc.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.linuxforhealth.fhir.persistence.jdbc.dao.api.IResourceTypeMaps;
import org.linuxforhealth.fhir.persistence.jdbc.dao.api.ResourceRecord;

/**
 * DAO to see which of the resources in the list exist
 */
public class ResourceListExistsDAO {
    private static final Logger logger = Logger.getLogger(ResourceListExistsDAO.class.getName());
    final IResourceTypeMaps resourceTypeMaps;

    final List<ResourceRecord> resourceList;
    /**
     * Public constructor
     * @param resourceTypeMap
     * @param resourceList
     */
    public ResourceListExistsDAO(IResourceTypeMaps resourceTypeMaps, List<ResourceRecord> resourceList) {
        this.resourceTypeMaps = resourceTypeMaps;
        this.resourceList = resourceList;
    }
 
    /**
     * Run the query to see which records currently exist.
     * Does not care about deletion status, just that the row is there.
     * @param c
     * @return a list of resources which do not exist
     * @throws SQLException
     */
    public List<ResourceRecord> run(Connection c) throws SQLException {
        // Because we need to look at the specific version and payload key, we
        // must join with the xx_resources table. However, the resourceList
        // contains resources from different resource types, so we need to
        // process these separately
        final List<ResourceRecord> missing = new ArrayList<>();
        Map<Integer,List<ResourceRecord>> groupedByType = resourceList.stream().collect(Collectors.groupingBy(ResourceRecord::getResourceTypeId));
        for (Map.Entry<Integer, List<ResourceRecord>> entry: groupedByType.entrySet()) {
            String resourceTypeName = resourceTypeMaps.getResourceTypeName(entry.getKey());
            findMissing(missing, c, resourceTypeName, entry.getValue());
        }
        return missing;
    }

    /**
     * Find which records in recordList are missing from the database
     * @param missing list to which missing records are to be added
     * @param resourceTypeName
     * @param value
     */
    private void findMissing(List<ResourceRecord> missing, Connection c, String resourceTypeName, List<ResourceRecord> recordList) throws SQLException {
        // Set of resourcePayloadKey values we were able to find
        final Set<String> resourceRecordSet = new HashSet<>();

        // Build the filter predicate
        final StringBuilder filter = new StringBuilder();
        for (ResourceRecord rr: recordList) {
            if (filter.length() > 0) {
                filter.append(" OR ");
            }
            // We use parens here for readability
            filter.append("(LR.LOGICAL_ID = ? AND R.VERSION_ID = ? AND R.RESOURCE_PAYLOAD_KEY = ? AND R.IS_DELETED = 'N')");
        }
        final String SQL = ""
                + "SELECT r.resource_payload_key "
                + "  FROM " + resourceTypeName + "_resources r, " 
                + "       " + resourceTypeName + "_logical_resources lr "
                + " WHERE r.logical_resource_id = lr.logical_resource_id "
                + "   AND (" + filter + ")";

        try (PreparedStatement ps = c.prepareStatement(SQL)) {
            // bind all the parameter values
            int index = 1;
            for (ResourceRecord rr: recordList) {
                ps.setString(index++, rr.getLogicalId());
                ps.setInt(index++, rr.getVersion());
                ps.setString(index++, rr.getResourcePayloadKey());
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                resourceRecordSet.add(rs.getString(1));
            }
        } catch (SQLException x) {
            logger.log(Level.SEVERE, SQL, x);
            throw x;
        }

        // add any records we didn't find to the missing list
        for (ResourceRecord rr: recordList) {
            if (!resourceRecordSet.contains(rr.getResourcePayloadKey())) {
                missing.add(rr);
            }
        }
    }
}