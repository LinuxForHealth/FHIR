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
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.ibm.fhir.config.SystemConfigHelper;
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
    private final int resourceCount;
    private final Long afterResourceId;
    private final Instant fromTstamp;
    
    // exclude resources falling inside transaction timeout window?
    private final boolean excludeTransactionTimeoutWindow;
    
    // If not null, specifies the list of resource types we want to include in the result
    private final List<Integer> resourceTypeIds;

    /**
     * Public constructor
     * @param tx
     * @param schemaName
     * @param resourceCount
     * @param fromTstamp
     * @param afterResourceId
     * @param resourceTypeIds
     * @param excludeTransactionTimeoutWindow
     */
    public FetchResourceChangesDAO(IDatabaseTranslator tx, String schemaName, int resourceCount, Instant fromTstamp, Long afterResourceId, List<Integer> resourceTypeIds,
        boolean excludeTransactionTimeoutWindow) {
        this.translator = tx;
        this.schemaName = schemaName;
        this.resourceCount = resourceCount;
        this.fromTstamp = fromTstamp;
        this.afterResourceId = afterResourceId;
        this.resourceTypeIds = resourceTypeIds;
        this.excludeTransactionTimeoutWindow = excludeTransactionTimeoutWindow;
    }

    /**
     * Run the DAO command on the database connection
     * @param c
     * @return
     * @throws FHIRPersistenceException
     */
    public List<ResourceChangeLogRecord> run(Connection c) throws FHIRPersistenceException {
        List<ResourceChangeLogRecord> result = new ArrayList<>();
        
        Instant before = null;
        if (this.excludeTransactionTimeoutWindow) {
            // compute the start time of the current transaction timeout window. Note that
            // if the FHIR_TRANSACTION_MANAGER_TIMEOUT variable is not set, we can't determine
            // the timeout value, so we can't use the feature
            long txTimeoutSecs = SystemConfigHelper.getDurationFromEnv("FHIR_TRANSACTION_MANAGER_TIMEOUT", null);
            if (txTimeoutSecs < 0) {
                logger.warning("Ignoring excludeTransactionTimeoutWindow because FHIR_TRANSACTION_MANAGER_TIMEOUT is not set");
            } else {
                before = Instant.now().minus(txTimeoutSecs, ChronoUnit.SECONDS);
            }
        }

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
        
        if (before != null) {
            query.append(" AND c.change_tstamp < ? ");
        }

        if (afterResourceId != null) {
            query.append(" AND c.resource_id > ? ");
        }

        if (resourceTypeIds != null) {
            if (resourceTypeIds.size() == 1) {
                query.append(" AND c.resource_type_id = " + resourceTypeIds.get(0));
            } else if (resourceTypeIds.size() > 1) {
                String inlist = resourceTypeIds.stream().map(x -> x.toString()).collect(Collectors.joining(","));
                query.append(" AND c.resource_type_id IN (").append(inlist).append(")");
            }
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
            logger.fine("FETCH CHANGES: " + SQL + "; [" + fromTstamp + ", " + afterResourceId + "]");
        }

        try (PreparedStatement ps = c.prepareStatement(SQL)) {
            int a = 1;
            if (this.fromTstamp != null) {
                ps.setTimestamp(a++, Timestamp.from(this.fromTstamp), CalendarHelper.getCalendarForUTC());
            }

            if (before != null) {
                ps.setTimestamp(a++, Timestamp.from(before), CalendarHelper.getCalendarForUTC());
            }

            if (this.afterResourceId != null) {
                ps.setLong(a++, this.afterResourceId);
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
