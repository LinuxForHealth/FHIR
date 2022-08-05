/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.dao.impl;

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

import org.linuxforhealth.fhir.config.SystemConfigHelper;
import org.linuxforhealth.fhir.database.utils.api.IDatabaseTranslator;
import org.linuxforhealth.fhir.database.utils.common.CalendarHelper;
import org.linuxforhealth.fhir.persistence.HistorySortOrder;
import org.linuxforhealth.fhir.persistence.ResourceChangeLogRecord;
import org.linuxforhealth.fhir.persistence.ResourceChangeLogRecord.ChangeType;
import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceException;

/**
 * Simple DAO to read records from the RESOURCE_CHANGE_LOG table
 */
public class FetchResourceChangesDAO {
    private static final Logger logger = Logger.getLogger(FetchResourceChangesDAO.class.getName());

    private final IDatabaseTranslator translator;
    private final String schemaName;
    private final int resourceCount;
    private final Long changeIdMarker;
    private final Instant sinceTstamp;
    private final Instant beforeTstamp;
    
    // exclude resources falling inside transaction timeout window?
    private final boolean excludeTransactionTimeoutWindow;
    
    // If not null, specifies the list of resource types we want to include in the result
    private final List<Integer> resourceTypeIds;
    
    // the ordering to use for the query, and the semantics to apply to the filter predicates
    private final HistorySortOrder historySortOrder;

    /**
     * Public constructor
     * @param tx
     * @param schemaName
     * @param resourceCount
     * @param sinceTstamp
     * @param beforeTstamp
     * @param afterResourceId
     * @param resourceTypeIds
     * @param excludeTransactionTimeoutWindow
     * @param historySortOrder
     */
    public FetchResourceChangesDAO(IDatabaseTranslator tx, String schemaName, int resourceCount, Instant sinceTstamp, Instant beforeTstamp, Long changeIdMarker, List<Integer> resourceTypeIds,
            boolean excludeTransactionTimeoutWindow, HistorySortOrder historySortOrder) {
        this.translator = tx;
        this.schemaName = schemaName;
        this.resourceCount = resourceCount;
        this.sinceTstamp = sinceTstamp;
        this.beforeTstamp = beforeTstamp;
        this.changeIdMarker = changeIdMarker;
        this.resourceTypeIds = resourceTypeIds;
        this.excludeTransactionTimeoutWindow = excludeTransactionTimeoutWindow;
        this.historySortOrder = historySortOrder;
    }

    /**
     * Run the DAO command on the database connection
     * @param c
     * @return
     * @throws FHIRPersistenceException
     */
    public List<ResourceChangeLogRecord> run(Connection c) throws FHIRPersistenceException {
        List<ResourceChangeLogRecord> result = new ArrayList<>();
        
        Instant beforeTxWindow = null;
        if (this.excludeTransactionTimeoutWindow) {
            // compute the start time of the current transaction timeout window. Note that
            // if the FHIR_TRANSACTION_MANAGER_TIMEOUT variable is not set we fall back
            // to using the default Liberty transaction timeout which is 120s.
            long txTimeoutSecs = SystemConfigHelper.getDurationFromEnv("FHIR_TRANSACTION_MANAGER_TIMEOUT", "120s");
            if (txTimeoutSecs < 0) {
                logger.warning("Ignoring excludeTransactionTimeoutWindow because FHIR_TRANSACTION_MANAGER_TIMEOUT is invalid");
            } else {
                // add a couple of seconds to cover any clock drift in a cluster environment.
                final int clockDriftBuffer = 2;
                beforeTxWindow = Instant.now().minus(txTimeoutSecs + clockDriftBuffer, ChronoUnit.SECONDS);
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

        if (sinceTstamp != null) {
            query.append(" AND c.change_tstamp >= ? ");
        }
        
        if (beforeTstamp != null) {
            query.append(" AND c.change_tstamp <= ? ");
        }
        
        if (beforeTxWindow != null) {
            // filter out records from inside the current transaction timeout window to
            // make sure clients don't miss records when paging (see Conformance guide)
            query.append(" AND c.change_tstamp < ? ");
        }

        if (changeIdMarker != null) {
            if (historySortOrder == HistorySortOrder.NONE) {
                // range scan following the primary key
                query.append(" AND c.resource_id > ? ");
            } else {
                // we're sorting on time, so this is useful to avoid repeating the last
                // resource we returned every time follow the next link. There can still
                // be duplicates if multiple resources share the same timestamp, but this
                // at least gets rid of the most common case
                query.append(" AND c.resource_id != ? ");
            }
        }

        if (resourceTypeIds != null) {
            if (resourceTypeIds.size() == 1) {
                query.append(" AND c.resource_type_id = " + resourceTypeIds.get(0));
            } else if (resourceTypeIds.size() > 1) {
                String inlist = resourceTypeIds.stream().map(x -> x.toString()).collect(Collectors.joining(","));
                query.append(" AND c.resource_type_id IN (").append(inlist).append(")");
            }
        }
        
        switch (historySortOrder) {
        case NONE:
            // ORDER BY can match the PK. Because this is unique, no additional order columns required
            query.append(" ORDER BY c.resource_id "); // PK scan with limit
            break;
        case ASC_LAST_UPDATED:
            // ORDER BY needs to match the index unq_resource_change_log_ctrtri for forward scan
            query.append(" ORDER BY c.change_tstamp, c.resource_type_id, c.resource_id ");
            break;
        case DESC_LAST_UPDATED:
            // The new default ordering per the spec. Reverse scan
            query.append(" ORDER BY c.change_tstamp DESC, c.resource_type_id DESC, c.resource_id DESC ");
            break;
        }

        query.append(translator.limit(Integer.toString(resourceCount)));

        final String SQL = query.toString();

        if (logger.isLoggable(Level.FINE)) {
            logger.fine("FETCH CHANGES: " + SQL + "; [" + sinceTstamp + ", " + changeIdMarker + "]");
        }

        try (PreparedStatement ps = c.prepareStatement(SQL)) {
            int a = 1;
            if (this.sinceTstamp != null) {
                ps.setTimestamp(a++, Timestamp.from(this.sinceTstamp), CalendarHelper.getCalendarForUTC());
            }

            if (this.beforeTstamp != null) {
                ps.setTimestamp(a++, Timestamp.from(this.beforeTstamp), CalendarHelper.getCalendarForUTC());
            }

            if (beforeTxWindow != null) {
                ps.setTimestamp(a++, Timestamp.from(beforeTxWindow), CalendarHelper.getCalendarForUTC());
            }

            if (this.changeIdMarker != null) {
                ps.setLong(a++, this.changeIdMarker);
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
