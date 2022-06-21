/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dao.impl;

import static com.ibm.fhir.persistence.jdbc.JDBCConstants.DOT;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.common.CalendarHelper;
import com.ibm.fhir.persistence.ResourcePayload;
import com.ibm.fhir.persistence.exception.FHIRPersistenceDataAccessException;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;

/**
 * DAO to fetch resource ids using a time range and optional current resource id as a filter.
 * Useful for scanning the database for new resources since a given timestamp (for example
 * in export scenarios).
 */
public class FetchResourcePayloadsDAO {
    private static final Logger logger = Logger.getLogger(FetchResourcePayloadsDAO.class.getName());

    // The FHIR data schema name
    private final String schemaName;

    // The name of the resource type (e.g. "Patient")
    private final String resourceType;

    // Start scanning from this timestamp. Never null
    private final Instant fromLastUpdated;

    // Scan up to this timestamp if given. Can be null.
    private final Instant toLastUpdated;

    // Consumer to process each of the records read
    private final Function<ResourcePayload,Boolean> processor;

    // The database translator to help tweak the syntax needed for different DB support
    private final IDatabaseTranslator translator;

    public FetchResourcePayloadsDAO(IDatabaseTranslator translator, String schemaName, String resourceType, Instant fromLastUpdated, Instant toLastUpdated,
        Function<ResourcePayload,Boolean> processor) {
        this.translator = translator;
        this.schemaName = schemaName;
        this.resourceType = resourceType;
        this.fromLastUpdated = fromLastUpdated;
        this.toLastUpdated = toLastUpdated;
        this.processor = processor;
    }

    /**
     * Run the query, feeding each result row to the consumer
     * @param c
     * @return the last record processed
     * @throws FHIRPersistenceException
     */
    public ResourcePayload run(Connection c) throws FHIRPersistenceException {
        ResourcePayload result = null;

        final String lrTableName = resourceType + "_logical_resources";
        final String rTableName = resourceType + "_resources";
        StringBuilder query = new StringBuilder();
        query.append("SELECT lr.logical_id, r.last_updated, r.resource_id, r.data FROM ");
        query.append(schemaName).append(DOT).append(rTableName).append(" AS r, ");
        query.append(schemaName).append(DOT).append(lrTableName).append(" AS lr ");
        query.append(" WHERE r.is_deleted = 'N' ");
        query.append("   AND lr.current_resource_id = r.resource_id ");

        // Add the predicate for the optional start
        // this aligns with the Bulk Data Specification.
        if (this.fromLastUpdated != null) {
            query.append("   AND r.last_updated >= ? ");
        }

        // Add the predicate for the optional end-stop
        if (this.toLastUpdated != null) {
            query.append(" AND r.last_updated <= ? ");
        }

        // It is crucial to ORDER BY both the last_updated timestamp and the resource_id so that
        // the scan can continue from where it left off. Unfortunately the index on last_updated
        // doesn't include resource_id, so a fetch from the table is unavoidable.
        query.append(" ORDER BY r.last_updated ");

        // For Postgres, the LIMIT is absolutely required to make sure we get a sort-free scan (the ORDER BY
        // is supported by an index, so the database should be able to start feeding results back immediately
        // without having to first gather everything for a sort, which makes this a whole lot faster and more
        // scalable. With this LIMIT, it does mean that we have to call this query more frequently, but because
        // it is so cheap, this doesn't hurt us.
        query.append(translator.limit(Integer.toString(1000)));

        final String select = query.toString();

        if (logger.isLoggable(Level.FINE)) {
            logger.fine("Fetch resource payload query: " + select);
        }

        try (PreparedStatement ps = c.prepareStatement(select)) {
            int a = 1;

            // Set the variables marking the start point of the scan
            if (this.fromLastUpdated != null) {
                ps.setTimestamp(a++, Timestamp.from(this.fromLastUpdated), CalendarHelper.getCalendarForUTC());
            }

            // And where we want the scan to stop (e.g. exporting a limited time range)
            if (this.toLastUpdated != null) {
                ps.setTimestamp(a++, Timestamp.from(this.toLastUpdated), CalendarHelper.getCalendarForUTC());
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // make sure we get the timestamp as a UTC value
                String logicalId = rs.getString(1);
                Instant lastUpdated = rs.getTimestamp(2, CalendarHelper.getCalendarForUTC()).toInstant();
                long resourceId = rs.getLong(3);
                InputStream is = new GZIPInputStream(rs.getBinaryStream(4));
                result = new ResourcePayload(logicalId, lastUpdated, resourceId, is);
                Boolean cont = processor.apply(result);
                if (cont == null || !cont) {
                    // the processor has asked us to stop
                    break;
                }
            }
        } catch (IOException x) {
            logger.log(Level.SEVERE, "query: " + select + "[fromLastUpdated=" + fromLastUpdated + ", toLastUpdated=" + toLastUpdated + "]", x);
            throw new FHIRPersistenceDataAccessException("FetchResourceIds query failed");
        } catch (SQLException x) {
            logger.log(Level.SEVERE, "query: " + select + "[fromLastUpdated=" + fromLastUpdated + ", toLastUpdated=" + toLastUpdated + "]", x);
            throw new FHIRPersistenceDataAccessException("FetchResourceIds query failed");
        }

        // the last ResultPayload to be processed is returned as a convenience to the caller
        return result;
    }

    /**
     * Get a count of the resources matching the filter predicates...for debugging...slows
     * things down a lot
     * @param c
     * @return
     * @throws FHIRPersistenceException
     */
    public int count(Connection c) throws FHIRPersistenceException {
        int result;

        final String lrTableName = resourceType + "_logical_resources";
        final String rTableName = resourceType + "_resources";
        StringBuilder query = new StringBuilder();
        query.append("SELECT count(*) FROM ");
        query.append(schemaName).append(DOT).append(rTableName).append(" AS r, ");
        query.append(schemaName).append(DOT).append(lrTableName).append(" AS lr ");
        query.append(" WHERE r.is_deleted = 'N' ");
        query.append("   AND lr.current_resource_id = r.resource_id ");

        // Add the predicate for the optional start
        if (this.fromLastUpdated != null) {
            query.append("   AND r.last_updated >= ? ");
        }

        // Add the predicate for the optional end
        if (this.toLastUpdated != null) {
            query.append(" AND r.last_updated <= ? ");
        }

        final String select = query.toString();

        if (logger.isLoggable(Level.FINE)) {
            logger.fine("Resource count query: " + select + "; [" + fromLastUpdated + "]");
        }

        try (PreparedStatement ps = c.prepareStatement(select)) {
            int a = 1;

            // Set the variables marking the start point of the scan
            if (this.fromLastUpdated != null) {
                ps.setTimestamp(a++, Timestamp.from(fromLastUpdated), CalendarHelper.getCalendarForUTC());
            }

            // And where we want the scan to stop (e.g. exporting a limited time range)
            if (this.toLastUpdated != null) {
                ps.setTimestamp(a++, Timestamp.from(this.toLastUpdated), CalendarHelper.getCalendarForUTC());
            }

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1);
            } else {
                throw new IllegalStateException("no count result"); // not gonna happen
            }
        } catch (SQLException x) {
            logger.log(Level.SEVERE, "query: " + select + "[fromLastUpdated=" + fromLastUpdated + ", toLastUpdated=" + toLastUpdated + "]", x);
            throw new FHIRPersistenceDataAccessException("FetchResourceIds query failed");
        }
        return result;
    }
}