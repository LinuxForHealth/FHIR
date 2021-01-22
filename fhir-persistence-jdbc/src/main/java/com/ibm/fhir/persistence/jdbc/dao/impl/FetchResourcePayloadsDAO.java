/*
 * (C) Copyright IBM Corp. 2021
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
import com.ibm.fhir.persistence.ResourcePayload;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDataAccessException;

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

    // Start scanning from this resourceId if given. Can be null
    private final Long fromResourceId;

    // Scan up to this timestamp if given. Can be null.
    private final Instant toLastUpdated;

    // Used as a time window to limit the number of resources we process (to help query performance)
    private final int spanSeconds;

    // Consumer to process each of the records read
    private final Function<ResourcePayload,Boolean> processor;

    // The database translator to help tweak the syntax needed for different DB support
    private final IDatabaseTranslator translator;

    public FetchResourcePayloadsDAO(IDatabaseTranslator translator, String schemaName, String resourceType, Instant fromLastUpdated, Long fromResourceId, int spanSeconds, Instant toLastUpdated,
        Function<ResourcePayload,Boolean> processor) {
        this.translator = translator;
        this.schemaName = schemaName;
        this.resourceType = resourceType;
        this.fromLastUpdated = fromLastUpdated;
        this.fromResourceId = fromResourceId;
        this.spanSeconds = spanSeconds;
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

        // Calculate the end-stop for the query. There's a slight complication because
        // we also need to change the inclusion/exclusion (<= vs <) criteria depending
        // on which end timestamp is selected. As the calling algorithm walks along
        // the timeline, the semantics for the span are start <= t < end
        boolean inclusive = false;
        Instant endstop = null;
        if (this.spanSeconds > 0) {
            endstop = fromLastUpdated.plusSeconds(this.spanSeconds);

            if (this.toLastUpdated != null) {
                if (endstop.equals(this.toLastUpdated)) {
                    // Need to make sure we include the final last-updated timestamp
                    inclusive = true;
                } else if (this.toLastUpdated.isBefore(endstop)) {
                    // Reduce the span to the desired last-updated timestamp, which is also inclusive.
                    endstop = this.toLastUpdated;
                    inclusive = true;
                }
            }
        } else {
            // No spanning, so just use the toLastUpdated as the inclusive endstop (can be null)
            endstop = this.toLastUpdated;
            inclusive = true;
        }

        final String lrTableName = resourceType + "_logical_resources";
        final String rTableName = resourceType + "_resources";
        StringBuilder query = new StringBuilder();
        query.append("SELECT lr.logical_id, r.last_updated, r.resource_id, r.data FROM ");
        query.append(schemaName).append(DOT).append(rTableName).append(" AS r, ");
        query.append(schemaName).append(DOT).append(lrTableName).append(" AS lr ");
        query.append(" WHERE r.is_deleted = 'N' ");
        query.append("   AND lr.current_resource_id = r.resource_id ");

        query.append("   AND r.last_updated >= ? ");

        // Add the optional predicate to start scanning from the
        // next resource after the one previously read
        if (fromResourceId != null) {
            query.append(" AND r.resource_id > ? ");
        }

        // Add the predicate for the optional endstop. This allows callers to limit
        // how much data they attempt to process in one go. At the very least, it
        // reduces the size of the join/sort that's required
        if (endstop != null) {
            if (inclusive) {
                query.append(" AND r.last_updated <= ? ");
            } else {
                query.append(" AND r.last_updated <  ? ");
            }
        }

        // It is crucial to ORDER BY both the last_updated timestamp and the resource_id so that
        // the scan can continue from where it left off. Unfortunately the index on last_updated
        // doesn't include resource_id, so a fetch from the table is unavoidable.
        query.append(" ORDER BY r.last_updated, r.resource_id ");

        final String select = query.toString();

        if (logger.isLoggable(Level.FINE)) {
            logger.fine("Resource id fetch: " + select);
        }

        try (PreparedStatement ps = c.prepareStatement(select)) {
            int a = 1;

            // Set the variables marking the start point of the scan
            ps.setTimestamp(a++, Timestamp.from(fromLastUpdated));
            if (fromResourceId != null) {
                ps.setLong(a++, fromResourceId);
            }

            // And where we want the scan to stop (to avoid a huge join/sort)
            if (endstop != null) {
                ps.setTimestamp(a++, Timestamp.from(endstop));
            }


            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String logicalId = rs.getString(1);
                Instant lastUpdated = Instant.ofEpochMilli(rs.getTimestamp(2).getTime());
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
            logger.log(Level.SEVERE, "query: " + select + "[fromLastUpdated=" + fromLastUpdated + ", fromResourceId=" + fromResourceId + ", endstop=" + endstop + "]", x);
            throw new FHIRPersistenceDataAccessException("FetchResourceIds query failed");
        } catch (SQLException x) {
            logger.log(Level.SEVERE, "query: " + select + "[fromLastUpdated=" + fromLastUpdated + ", fromResourceId=" + fromResourceId + ", endstop=" + endstop + "]", x);
            throw new FHIRPersistenceDataAccessException("FetchResourceIds query failed");
        }

        // the last ResultPayload to be processed is returned as a convenience to the caller
        return result;
    }
}