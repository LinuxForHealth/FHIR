/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.cassandra.payload;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.bindMarker;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.deleteFrom;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;
import static com.ibm.fhir.persistence.cassandra.cql.SchemaConstants.LOGICAL_RESOURCES;
import static com.ibm.fhir.persistence.cassandra.cql.SchemaConstants.PAYLOAD_CHUNKS;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatementBuilder;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.ibm.fhir.persistence.cassandra.cql.CqlDataUtil;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDataAccessException;

/**
 * DAO to delete all the records associated with this resource payload
 */
public class CqlDeletePayload {
    private static final Logger logger = Logger.getLogger(CqlDeletePayload.class.getName());

    // Possibly patient, or some other partitioning key
    private final String partitionId;

    // The int id representing the resource type (much shorter than the string name)
    private final int resourceTypeId;

    // The logical identifier we have assigned to the resource
    private final String logicalId;

    // The version, or null for all versions
    private final Integer version;
    /**
     * Public constructor
     * @param partitionId
     * @param resourceTypeId
     * @param logicalId
     * @param version
     */
    public CqlDeletePayload(String partitionId, int resourceTypeId, String logicalId, Integer version) {
        CqlDataUtil.safeBase64(partitionId);
        this.partitionId = partitionId;
        this.resourceTypeId = resourceTypeId;
        this.logicalId = logicalId;
        this.version = version;
    }

    /**
     * Hard delete the payload records for the configured resource.
     * @param session
     */
    public void run(CqlSession session) throws FHIRPersistenceException {
        deleteFromPayloadChunks(session);
        deleteFromLogicalResources(session);
    }

    /**
     * Hard delete all the resource record entries from the LOGICAL_RESOURCES table
     * for the configured resourceTypeId/logicalId
     * @param session
     * @throws FHIRPersistenceException
     */
    private void deleteFromLogicalResources(CqlSession session) throws FHIRPersistenceException {
        
        final SimpleStatement del;
        final PreparedStatement ps;
        final BoundStatementBuilder bsb;
        
        if (version != null) {
            del = deleteFrom(LOGICAL_RESOURCES)
                .whereColumn("partition_id").isEqualTo(literal(partitionId))
                .whereColumn("resource_type_id").isEqualTo(literal(resourceTypeId))
                .whereColumn("logical_id").isEqualTo(bindMarker())
                .whereColumn("version").isEqualTo(bindMarker())
                .build();
            ps = session.prepare(del);
            bsb = ps.boundStatementBuilder(logicalId, version);
        } else {
            // Deletes all versions for the logical resource
            del = deleteFrom(LOGICAL_RESOURCES)
                    .whereColumn("partition_id").isEqualTo(literal(partitionId))
                    .whereColumn("resource_type_id").isEqualTo(literal(resourceTypeId))
                    .whereColumn("logical_id").isEqualTo(bindMarker())
                    .build();
            ps = session.prepare(del);
            bsb = ps.boundStatementBuilder(logicalId);
        }
        
        try {
            session.execute(bsb.build());
        } catch (Exception x) {
            logger.log(Level.SEVERE, "delete from logical_resources failed for '"
                    + partitionId + "/" + resourceTypeId + "/" + logicalId + "/" + version + "'", x);
            throw new FHIRPersistenceDataAccessException("Failed deleting from " + LOGICAL_RESOURCES);
        }
    }
    
    /**
     * Hard delete all the resource record entries from the PAYLOAD_CHUNKS table
     * for the configured resourceTypeId/logicalId. The payload_chunks key is
     * prefixed with partitionId/resourceTypeId/logicalId which makes this
     * delete a simple range.
     * @param session
     * @throws FHIRPersistenceException
     */
    private void deleteFromPayloadChunks(CqlSession session) throws FHIRPersistenceException {
        final SimpleStatement del;
        final PreparedStatement ps;
        final BoundStatementBuilder bsb;
        
        if (version != null) {
            // deletes all the chunks for the specific resource version
            del = deleteFrom(PAYLOAD_CHUNKS)
                .whereColumn("partition_id").isEqualTo(literal(partitionId))
                .whereColumn("resource_type_id").isEqualTo(literal(resourceTypeId))
                .whereColumn("logical_id").isEqualTo(bindMarker())
                .whereColumn("version").isEqualTo(bindMarker())
                .build();
            ps = session.prepare(del);
            bsb = ps.boundStatementBuilder(logicalId, version);
        } else {
            // Deletes chunks for all versions of the resource
            del = deleteFrom(PAYLOAD_CHUNKS)
                    .whereColumn("partition_id").isEqualTo(literal(partitionId))
                    .whereColumn("resource_type_id").isEqualTo(literal(resourceTypeId))
                    .whereColumn("logical_id").isEqualTo(bindMarker())
                    .build();
            ps = session.prepare(del);
            bsb = ps.boundStatementBuilder(logicalId);
        }
        
        try {
            session.execute(bsb.build());
        } catch (Exception x) {
            logger.log(Level.SEVERE, "delete from payload_chunks failed for '"
                    + partitionId + "/" + resourceTypeId + "/" + logicalId + "'", x);
            throw new FHIRPersistenceDataAccessException("Failed deleting from " + PAYLOAD_CHUNKS);
        }
    }
}