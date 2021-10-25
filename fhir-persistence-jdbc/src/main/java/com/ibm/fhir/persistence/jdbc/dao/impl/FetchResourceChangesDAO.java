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
import com.ibm.fhir.database.utils.common.CalendarHelper;
import com.ibm.fhir.persistence.ResourceChangeLogRecord;
import com.ibm.fhir.persistence.ResourceChangeLogRecord.ChangeType;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;

/**
 * Simple DAO to read records from the RESOURCE_CHANGE_LOG table
 */
public class FetchResourceChangesDAO {
    private static final Logger logger = Logger.getLogger(FetchResourceChangesDAO.class.getName());

    private final IDatabaseTranslator translator;
    private final String schemaName;
    private final String resourceTypeName;
    private final int resourceCount;
    private final Long afterResourceId;
    private final Instant fromTstamp;

    /**
     * Public constructor
     * @param tx
     * @param schemaName
     * @param resourceCount
     * @param resourceTypeName
     * @param fromTstamp
     * @param afterResourceId
     */
    public FetchResourceChangesDAO(IDatabaseTranslator tx, String schemaName, int resourceCount, String resourceTypeName, Instant fromTstamp, Long afterResourceId) {
        this.translator = tx;
        this.schemaName = schemaName;
        this.resourceCount = resourceCount;
        this.resourceTypeName = resourceTypeName;
        this.fromTstamp = fromTstamp;
        this.afterResourceId = afterResourceId;
    }

    /**
     * Run the DAO command on the database connection
     * @param c
     * @return
     * @throws FHIRPersistenceException
     */
    public List<ResourceChangeLogRecord> run(Connection c) throws FHIRPersistenceException {
        List<ResourceChangeLogRecord> result = new ArrayList<>();

        StringBuilder query = new StringBuilder();
        query.append(" SELECT c.resource_id, rt.resource_type, lr.logical_id, c.change_tstamp, c.version_id, c.change_type")
            .append("  FROM ")
            .append(schemaName).append(".resource_change_log  c, ")
            .append(schemaName).append(".logical_resources   lr, ")
            .append(schemaName).append(".resource_types      rt  ")
            .append(" WHERE lr.logical_resource_id = c.logical_resource_id ")
            .append("   AND rt.resource_type_id = c.resource_type_id ")
            ;

        // Only filter/order by _since or _afterHistoryId never both. This is crucial because
        // the values are not strictly correlated and mixing them could cause the client to miss
        // data without realizing.
        if (fromTstamp != null && afterResourceId != null) {
            // Shouldn't get here because this condition should be trapped in the REST helper
            throw new FHIRPersistenceException("_since and _afterHistoryId filters must be used exclusively");
        }

        if (fromTstamp != null) {
            query.append(" AND c.change_tstamp >= ? ");
        }

        if (afterResourceId != null) {
            query.append(" AND c.resource_id > ? ");
        }

        if (resourceTypeName != null) {
            query.append(" AND rt.resource_type = ? ");
        }

        if (afterResourceId != null) {
            // ORDER BY can match the PK. Because this is unique, no additional order columns required
            query.append(" ORDER BY c.resource_id "); // PK scan with limit
        } else {
            // ORDER BY needs to match the index unq_resource_change_log_ctrtri
            query.append(" ORDER BY c.change_tstamp, c.resource_type_id, c.resource_id "); // index scan with limit
        }

        query.append(translator.limit(Integer.toString(resourceCount)));

        final String SQL = query.toString();

        if (logger.isLoggable(Level.FINE)) {
            logger.fine("FETCH CHANGES: " + SQL + "; [" + fromTstamp + ", " + afterResourceId + ", " + resourceTypeName + "]");
        }

        try (PreparedStatement ps = c.prepareStatement(SQL)) {
            int a = 1;
            if (this.fromTstamp != null) {
                ps.setTimestamp(a++, Timestamp.from(this.fromTstamp), CalendarHelper.getCalendarForUTC());
            }

            if (this.afterResourceId != null) {
                ps.setLong(a++, this.afterResourceId);
            }

            if (this.resourceTypeName != null) {
                ps.setString(a++, this.resourceTypeName);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ResourceChangeLogRecord.ChangeType ct;
                switch (rs.getString(6)) {
                case "C": ct = ChangeType.CREATE; break;
                case "U": ct = ChangeType.UPDATE; break;
                case "D": ct = ChangeType.DELETE; break;
                default:
                    throw new FHIRPersistenceException("Invalid ChangeType in change log"); // DBA can find the bad row if it ever happens
                }
                ResourceChangeLogRecord record = new ResourceChangeLogRecord(rs.getString(2), rs.getString(3), rs.getInt(5), rs.getLong(1), rs.getTimestamp(4, CalendarHelper.getCalendarForUTC()).toInstant(), ct);
                result.add(record);
            }
        } catch (SQLException x) {
            logger.log(Level.SEVERE, "Change log query failed: " + SQL, x);
            throw new FHIRPersistenceException("Fetch changes failed");
        }

        return result;
    }
}
