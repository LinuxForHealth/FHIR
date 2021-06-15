/*
 * (C) Copyright IBM Corp. 2021
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
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;

/**
 * Simple DAO to retrieve index IDs (i.e. logical resource IDs) from the LOGICAL_RESOURCES table.
 */
public class RetrieveIndexDAO {
    private static final Logger logger = Logger.getLogger(RetrieveIndexDAO.class.getName());
    private static final Calendar UTC_CALENDAR = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

    private final IDatabaseTranslator translator;
    private final String schemaName;
    private final int count;
    private final Long afterLogicalResourceId;
    private final Instant notModifiedAfter;

    /**
     * Public constructor.
     * @param tx translator
     * @param schemaName schema name
     * @param count maximum number of index IDs to return
     * @param notModifiedAfter only return resources last updated at or before the specified instant, or null
     * @param afterIndexId only return index IDs after this index ID, or null
     */
    public RetrieveIndexDAO(IDatabaseTranslator tx, String schemaName, int count, Instant notModifiedAfter, Long afterIndexId) {
        this.translator = tx;
        this.schemaName = schemaName;
        this.count = count;
        this.afterLogicalResourceId = afterIndexId;
        this.notModifiedAfter = notModifiedAfter;
    }

    /**
     * Run the DAO command on the database connection.
     * @param c connection
     * @return list of logical resource IDs
     * @throws FHIRPersistenceException
     */
    public List<Long> run(Connection c) throws FHIRPersistenceException {
        List<Long> logicalResourceIds = new ArrayList<>();

        StringBuilder query = new StringBuilder();
        query.append(" SELECT lr.logical_resource_id")
            .append(" FROM ")
            .append(schemaName).append(".logical_resources lr ")
            .append(" WHERE lr.is_deleted = 'N' ")
            .append(" AND lr.last_updated <= ? ");
            ;

        if (afterLogicalResourceId != null) {
            query.append(" AND lr.logical_resource_id > ? ");
        }
        query.append(" ORDER BY lr.logical_resource_id ");
        query.append(translator.limit(Integer.toString(count)));

        final String SQL = query.toString();

        if (logger.isLoggable(Level.FINE)) {
            logger.fine("RETRIEVE LOGICAL RESOURCE IDS: " + SQL + "; [" + notModifiedAfter + ", " + afterLogicalResourceId + "]");
        }

        try (PreparedStatement ps = c.prepareStatement(SQL)) {
            int i = 1;
            ps.setTimestamp(i++, Timestamp.from(notModifiedAfter), UTC_CALENDAR);
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
