/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.dao.impl;

import static org.linuxforhealth.fhir.persistence.jdbc.JDBCConstants.DOT;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

import org.linuxforhealth.fhir.database.utils.common.CalendarHelper;
import org.linuxforhealth.fhir.persistence.ResourcePayload;
import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceException;

/**
 * DAO to fetch the payload objects for a list of resource ids
 */
public class FetchPayloadsForIdsDAO {
    private final static Logger logger = Logger.getLogger(FetchPayloadsForIdsDAO.class.getName());

    // The list of resource ids for which we will fetch the payload data
    private final List<Long> resourceIds;

    // The fhir data schema
    private final String schemaName;

    // The resource type name (i.e. Patient)
    private final String resourceType;

    // The consumer accept is called for each record
    private final Consumer<ResourcePayload> consumer;

    /**
     * Public constructor
     * @param schemaName the FHIR data schema
     * @param the resource type name
     * @param resourceIds the list of database resource_id values
     * @param consumer the consumer to process each result. The stream must be processed before returning from the accept method
     */
    public FetchPayloadsForIdsDAO(String schemaName, String resourceType, List<Long> resourceIds, Consumer<ResourcePayload> consumer) {
        this.schemaName = schemaName;
        this.resourceType = resourceType;

        // no need to copy because this DAO is expected to be short-lived
        this.resourceIds = resourceIds;
        this.consumer = consumer;
    }

    /**
     * Fetch the payloads using the given connection
     * @param c
     * @throws FHIRPersistenceException
     */
    public void run(Connection c) throws FHIRPersistenceException {
        if (this.resourceIds.isEmpty()) {
            return;
        }

        final String lrTableName = resourceType + "_logical_resources";
        final String rTableName = resourceType + "_resources";
        StringBuilder query = new StringBuilder();

        // We already did the deleted check when performing the initial scan for resource-ids
        // so this query does not need to include the deleted check by design - it won't change
        // for this resource version. Ordering is not important (because it is arbitrary anyway)
        query.append("SELECT lr.logical_id, r.resource_id, r.data FROM ");
        query.append(schemaName).append(DOT).append(rTableName).append(" AS r, ");
        query.append(schemaName).append(DOT).append(lrTableName).append(" AS lr ");
        query.append(" WHERE lr.logical_resource_id = r.logical_resource_id "); // join to parent PK
        query.append("   AND r.resource_id IN (");

        // Add bind-variable markers for every resourceId value we want to fetch
        for (int i=0; i<resourceIds.size(); i++) {
            if (i == 0) {
                query.append("?");
            } else {
                query.append(",?");
            }
        }

        query.append(")"); // close the in-list

        final String select = query.toString();
        try (PreparedStatement ps = c.prepareStatement(select)) {
            // bind all the resource_id values
            int a=1;
            for (Long resourceId: resourceIds) {
                ps.setLong(a++, resourceId);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // process the payload data. For this special interface, we are bypassing the resource step
                // and just handing back the uncompressed stream. By avoiding deserialization/serialization,
                // we can save a ton of CPU. The stream is closed by ResultSet (according to the docs). ResultSet
                // will be closed when the PreparedStatement is closed
                String logicalId = rs.getString(1);
                Instant lastUpdated = rs.getTimestamp(2, CalendarHelper.getCalendarForUTC()).toInstant();
                long resourceId = rs.getLong(3);
                InputStream is = new GZIPInputStream(rs.getBinaryStream(4));
                ResourcePayload rp =  new ResourcePayload(logicalId, lastUpdated, resourceId, is);
                consumer.accept(rp);
            }
        } catch (SQLException x) {
            FHIRPersistenceException fx = new FHIRPersistenceException("Unexpected database error while reading payload data");
            logger.log(Level.SEVERE, fx.getMessage(), x);
            throw fx;
        } catch (IOException x) {
            FHIRPersistenceException fx = new FHIRPersistenceException("Unexpected IO error while reading payload data");
            logger.log(Level.SEVERE, fx.getMessage(), x);
            throw fx;
        }
    }
}