/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.cassandra.payload;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.bindMarker;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.deleteFrom;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.selectFrom;
import static com.ibm.fhir.persistence.cassandra.cql.SchemaConstants.PAYLOAD_CHUNKS;
import static com.ibm.fhir.persistence.cassandra.cql.SchemaConstants.RESOURCE_PAYLOADS;
import static com.ibm.fhir.persistence.cassandra.cql.SchemaConstants.RESOURCE_VERSIONS;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.BoundStatementBuilder;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import com.ibm.fhir.persistence.exception.FHIRPersistenceDataAccessException;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;

/**
 * DAO to delete all the records associated with this resource payload
 */
public class CqlDeletePayload {
    private static final Logger logger = Logger.getLogger(CqlDeletePayload.class.getName());

    // The int id representing the resource type (much shorter than the string name)
    private final int resourceTypeId;

    // The logical identifier we have assigned to the resource
    private final String logicalId;

    // The version, or null for all versions
    private final Integer version;
    
    // The resourcePayloadKey value, or null if all matches should be deleted
    private final String resourcePayloadKey;
    
    /**
     * Public constructor
     * @param resourceTypeId
     * @param logicalId
     * @param version
     */
    public CqlDeletePayload(int resourceTypeId, String logicalId, Integer version, String resourcePayloadKey) {
        this.resourceTypeId = resourceTypeId;
        this.logicalId = logicalId;
        this.version = version;
        this.resourcePayloadKey = resourcePayloadKey;
    }

    /**
     * Hard delete the payload records for the configured resource.
     * @param session
     */
    public void run(CqlSession session) throws FHIRPersistenceException {

        // We need to drive the deletion of both resource_payloads and
        // payload_chunks using a select from resource_versions to obtain
        // the resource_payload_key. Using this key ensures we can maintain
        // (eventual) consistency between the tables in a non-transactional
        // system with concurrent activity
        final BoundStatement bs;
        if (version != null) {
            if (resourcePayloadKey != null) {
                final Select statement =
                        selectFrom("resource_versions")
                        .column("version")
                        .column("resource_payload_key")
                        .whereColumn("resource_type_id").isEqualTo(literal(resourceTypeId))
                        .whereColumn("logical_id").isEqualTo(bindMarker())
                        .whereColumn("version").isEqualTo(bindMarker())
                        .whereColumn("resource_payload_key").isEqualTo(bindMarker())
                        ;
                PreparedStatement ps = session.prepare(statement.build());
                bs = ps.bind(logicalId, version, resourcePayloadKey);
            } else {
                final Select statement =
                        selectFrom("resource_versions")
                        .column("version")
                        .column("resource_payload_key")
                        .whereColumn("resource_type_id").isEqualTo(literal(resourceTypeId))
                        .whereColumn("logical_id").isEqualTo(bindMarker())
                        .whereColumn("version").isEqualTo(bindMarker())
                        ;
                PreparedStatement ps = session.prepare(statement.build());
                bs = ps.bind(logicalId, version);
            }
        } else {
            // To find the versions, we need to use the resource_versions tables
            final Select statement =
                    selectFrom("resource_versions")
                    .column("version")
                    .column("resource_payload_key")
                    .whereColumn("resource_type_id").isEqualTo(literal(resourceTypeId))
                    .whereColumn("logical_id").isEqualTo(bindMarker())
                    ;
            PreparedStatement ps = session.prepare(statement.build());
            bs = ps.bind(logicalId);
        }

        try {
            ResultSet rs = session.execute(bs);
            
            // Can't use forEach because we have to propagate checked exceptions
            for (Row row = rs.one(); row != null; row = rs.one()) {
                // Remove any chunks we've stored using the resource_payload_key
                final int version = row.getInt(0);
                final String resourcePayloadKey = row.getString(1);
                deletePayloadChunks(session, resourcePayloadKey);
                
                // And the specific resource_payloads row we just selected
                deleteResourcePayloads(session, version, resourcePayloadKey);
                
                // Because resource_versions is used to enumerate the versions,
                // for safety reasons we do this delete last
                deleteResourceVersion(session, version, resourcePayloadKey);
            }
        } catch (Exception x) {
            logger.log(Level.SEVERE, "delete failed for '"
                    + resourceTypeId + "/" + logicalId + "/" + version + "'", x);
            
            // don't propagate potentially sensitive info
            throw new FHIRPersistenceDataAccessException("Delete failed. See server log for details.");
        }
    }

    /**
     * Hard delete all the resource record entries from the RESOURCE_PAYLOADS table
     * for the configured resourceTypeId/logicalId and given version and
     * resourcePayloadKey
     * @param session
     * @param version
     * @param resourcePayloadKey
     * @throws FHIRPersistenceException
     */
    private void deleteResourcePayloads(CqlSession session, int version, String resourcePayloadKey) throws FHIRPersistenceException {
        
        final SimpleStatement del;
        final PreparedStatement ps;
        final BoundStatementBuilder bsb;
        
        del = deleteFrom(RESOURCE_PAYLOADS)
            .whereColumn("resource_type_id").isEqualTo(literal(resourceTypeId))
            .whereColumn("logical_id").isEqualTo(bindMarker())
            .whereColumn("version").isEqualTo(bindMarker())
            .whereColumn("resource_payload_key").isEqualTo(bindMarker())
            .build();
        ps = session.prepare(del);
        bsb = ps.boundStatementBuilder(logicalId, version, resourcePayloadKey);
        
        try {
            session.execute(bsb.build());
        } catch (Exception x) {
            logger.log(Level.SEVERE, "delete from resource_payloads failed for '"
                    + resourceTypeId + "/" + logicalId + "/" + version + "'", x);
            throw new FHIRPersistenceDataAccessException("Failed deleting from " + RESOURCE_PAYLOADS);
        }
    }
    
    /**
     * Hard delete all the resource record entries from the RESOURCE_PAYLOADS table
     * for the configured resourceTypeId/logicalId and given version and
     * resourcePayloadKey
     * @param session
     * @param version
     * @param resourcePayloadKey
     * @throws FHIRPersistenceException
     */
    private void deleteResourceVersion(CqlSession session, int version, String resourcePayloadKey) throws FHIRPersistenceException {
        
        final SimpleStatement del;
        final PreparedStatement ps;
        final BoundStatementBuilder bsb;
        
        del = deleteFrom(RESOURCE_VERSIONS)
            .whereColumn("resource_type_id").isEqualTo(literal(resourceTypeId))
            .whereColumn("logical_id").isEqualTo(bindMarker())
            .whereColumn("version").isEqualTo(bindMarker())
            .whereColumn("resource_payload_key").isEqualTo(bindMarker())
            .build();
        ps = session.prepare(del);
        bsb = ps.boundStatementBuilder(logicalId, version, resourcePayloadKey);
        
        try {
            session.execute(bsb.build());
        } catch (Exception x) {
            logger.log(Level.SEVERE, "delete from resource_payloads failed for '"
                    + resourceTypeId + "/" + logicalId + "/" + version + "'", x);
            throw new FHIRPersistenceDataAccessException("Failed deleting from " + RESOURCE_PAYLOADS);
        }
    }

    /**
     * Hard delete all the resource record entries from the PAYLOAD_CHUNKS table
     * for the given resourcePayloadKey
     * @param session
     * @param resourcePayloadKey
     * @throws FHIRPersistenceException
     */
    private void deletePayloadChunks(CqlSession session, String resourcePayloadKey) throws FHIRPersistenceException {
        final SimpleStatement del;
        
        del = deleteFrom(PAYLOAD_CHUNKS)
            .whereColumn("resource_payload_key").isEqualTo(bindMarker())
            .build();
        final PreparedStatement ps = session.prepare(del);
        final BoundStatementBuilder bsb = ps.boundStatementBuilder(resourcePayloadKey);
        
        try {
            session.execute(bsb.build());
        } catch (Exception x) {
            logger.log(Level.SEVERE, "delete from payload_chunks failed for '"
                    + resourceTypeId + "/" + logicalId + "[resourcePayloadKey=" + resourcePayloadKey + "]'", x);
            throw new FHIRPersistenceDataAccessException("Failed deleting from " + PAYLOAD_CHUNKS);
        }
    }
}