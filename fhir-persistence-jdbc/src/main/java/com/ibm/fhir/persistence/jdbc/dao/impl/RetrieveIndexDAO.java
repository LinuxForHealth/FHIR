/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.common.CalendarHelper;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.FHIRPersistenceJDBCCache;

/**
 * Simple DAO to retrieve index IDs (i.e. logical resource IDs) from the LOGICAL_RESOURCES table.
 */
public class RetrieveIndexDAO {
    private static final Logger logger = Logger.getLogger(RetrieveIndexDAO.class.getName());
    private final IDatabaseTranslator translator;
    private final String schemaName;
    private final String resourceTypeName;
    private final int count;
    private final Long afterLogicalResourceId;
    private final Instant notModifiedAfter;
    private final FHIRPersistenceJDBCCache cache;

    /**
     * Public constructor.
     * @param tx translator
     * @param schemaName schema name
     * @param resourceTypeName the resource type name of index IDs to return, or null
     * @param count maximum number of index IDs to return
     * @param notModifiedAfter only return resources last updated at or before the specified instant, or null
     * @param afterIndexId only return index IDs after this index ID, or null
     * @param cache the cache
     */
    public RetrieveIndexDAO(IDatabaseTranslator tx, String schemaName, String resourceTypeName, int count,
            Instant notModifiedAfter, Long afterIndexId, FHIRPersistenceJDBCCache cache) {
        this.translator = tx;
        this.schemaName = schemaName;
        this.resourceTypeName = resourceTypeName;
        this.count = count;
        this.afterLogicalResourceId = afterIndexId;
        this.notModifiedAfter = notModifiedAfter;
        this.cache = cache;
    }

    /**
     * Run the DAO command on the database connection.
     * @param c connection
     * @return list of logical resource IDs
     * @throws FHIRPersistenceException
     */
    public List<Long> run(Connection c) throws FHIRPersistenceException {
        List<Long> logicalResourceIds = new ArrayList<>();

        // Attempt to get resource type ID from cache, but since it is possible it doesn't find it in the cache,
        // since the cache is not loaded synchronously, fall back to using resource type name, if provided
        Integer resourceTypeId = resourceTypeName != null ? cache.getResourceTypeCache().getId(resourceTypeName) : null;

        StringBuilder query = new StringBuilder();
        query.append(" SELECT lr.logical_resource_id");
        query.append(" FROM ");
        if (resourceTypeId == null) {
            query.append(schemaName).append(".resource_types rt, ");
        }
        query.append(schemaName).append(".logical_resources lr ");
        query.append(" WHERE lr.is_deleted = 'N' ");
        if (resourceTypeId != null) {
            query.append(" AND lr.resource_type_id = ? ");
        } else if (resourceTypeName != null) {
            query.append(" AND rt.resource_type = ? ");
            query.append(" AND rt.resource_type_id = lr.resource_type_id ");
        } else {
            // filter out retired resource types
            query.append(" AND rt.retired = 'N' ");
            query.append(" AND rt.resource_type_id = lr.resource_type_id ");
        }
        if (notModifiedAfter != null) {
            query.append(" AND lr.last_updated <= ? ");
        }
        if (afterLogicalResourceId != null) {
            query.append(" AND lr.logical_resource_id > ? ");
        }

        query.append(" ORDER BY lr.logical_resource_id ");
        query.append(translator.limit(Integer.toString(count)));

        final String SQL = query.toString();

        if (logger.isLoggable(Level.FINE)) {
            logger.fine("RETRIEVE LOGICAL RESOURCE IDS: " + SQL + "; [" + resourceTypeName + ", " + notModifiedAfter + ", " + afterLogicalResourceId + "]");
        }

        try (PreparedStatement ps = c.prepareStatement(SQL)) {
            int i = 1;
            if (resourceTypeId != null) {
                ps.setInt(i++, resourceTypeId);
            } else if (resourceTypeName != null) {
                ps.setString(i++, resourceTypeName);
            }
            if (notModifiedAfter != null) {
                ps.setTimestamp(i++, Timestamp.from(notModifiedAfter), CalendarHelper.getCalendarForUTC());
            }
            if (afterLogicalResourceId != null) {
                ps.setLong(i++, afterLogicalResourceId);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                logicalResourceIds.add(rs.getLong(1));
            }
        } catch (SQLException x) {
            logger.log(Level.SEVERE, "Retrieve logical resource IDs query failed: " + SQL, x);
            throw new FHIRPersistenceException("Retrieve index failed");
        }

        return logicalResourceIds;
    }
}
