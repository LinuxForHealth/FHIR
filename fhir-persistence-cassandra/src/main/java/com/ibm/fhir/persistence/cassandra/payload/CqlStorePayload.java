/*
 * (C) Copyright IBM Corp. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.cassandra.payload;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.bindMarker;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.insertInto;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;
import static com.ibm.fhir.persistence.cassandra.cql.SchemaConstants.CHUNK_SIZE;
import static com.ibm.fhir.persistence.cassandra.cql.SchemaConstants.PAYLOAD_CHUNKS;
import static com.ibm.fhir.persistence.cassandra.cql.SchemaConstants.RESOURCE_PAYLOADS;
import static com.ibm.fhir.persistence.cassandra.cql.SchemaConstants.RESOURCE_VERSIONS;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BatchStatement;
import com.datastax.oss.driver.api.core.cql.BatchableStatement;
import com.datastax.oss.driver.api.core.cql.BoundStatementBuilder;
import com.datastax.oss.driver.api.core.cql.DefaultBatchType;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.querybuilder.insert.RegularInsert;
import com.ibm.fhir.persistence.exception.FHIRPersistenceDataAccessException;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.util.InputOutputByteStream;

/**
 * DAO to store the resource record and payload data chunks
 */
public class CqlStorePayload {
    private static final Logger logger = Logger.getLogger(CqlStorePayload.class.getName());

    // The RDBMS identifier for the resource type
    private final int resourceTypeId;

    // The logical identifier we have assigned to the resource
    private final String logicalId;

    // The compressed payload
    private final InputOutputByteStream payloadStream;

    // The anticipated version
    private final int version;
    
    // The unique key value for this resource payload assigned by the server
    private final String resourcePayloadKey;

    /**
     * Public constructor
     * @param resourceTypeId
     * @param logicalId
     * @param version
     * @param resourcePayloadKey
     * @param payloadStream
     */
    public CqlStorePayload(int resourceTypeId, String logicalId, int version, String resourcePayloadKey, InputOutputByteStream payloadStream) {
        this.logicalId = logicalId;
        this.resourceTypeId = resourceTypeId;
        this.version = version;
        this.resourcePayloadKey = resourcePayloadKey;
        this.payloadStream = payloadStream;
    }

    /**
     * Store the resource into the Cassandra database. We rely on other services to
     * ensure consistency during our writes (which are not isolated/atomic). This is
     * why every object is given a UUID which we can use to check that the records we
     * read are all associated with the same update
     * @param session
     */
    public void run(CqlSession session) throws FHIRPersistenceException {
        // We may split the payload into multiple chunks - but only if
        // the payload exceeds the chunk size. If it doesn't, we store
        // it in the main resource table, avoiding the cost of a second
        // random read when we need to access it again.
        final boolean storeInline = payloadStream.size() <= CHUNK_SIZE;
        storeResourceVersion(session);
        storeResource(session, storeInline);

        if (!storeInline) {
            // payload too big for the main resource table, so break it
            // into smaller chunks and store as adjacent rows in a child
            // table
            storePayloadChunks(session);
        }
    }

    /**
     * Store the resource version record
     * @param session
     */
    private void storeResourceVersion(CqlSession session) throws FHIRPersistenceException {
        RegularInsert insert =
                insertInto(RESOURCE_VERSIONS)
                .value("resource_type_id", literal(resourceTypeId))
                .value("logical_id", bindMarker())
                .value("version", bindMarker())
                .value("resource_payload_key", bindMarker())
            ;

        if (logger.isLoggable(Level.FINE)) {
            logger.fine("Storing resource version record for '"
                    + resourceTypeId + "/" + logicalId + "/" + version + "'; size=" + payloadStream.size());
        }

        PreparedStatement ps = session.prepare(insert.build());
        final BoundStatementBuilder bsb = ps.boundStatementBuilder(logicalId, version, resourcePayloadKey);

        try {
            session.execute(bsb.build());
        } catch (Exception x) {
            logger.log(Level.SEVERE, "insert into resource_payloads failed for '"
                    + resourceTypeId + "/" + logicalId + "/" + version + "'", x);
            throw new FHIRPersistenceDataAccessException("Failed inserting into " + RESOURCE_PAYLOADS);
        }
    }

    /**
     * Store the resource record
     * @param session
     * @param storeInline when true, store the payload inline in resource_payloads
     */
    private void storeResource(CqlSession session, boolean storeInline) throws FHIRPersistenceException {
        RegularInsert insert =
                insertInto(RESOURCE_PAYLOADS)
                .value("resource_type_id", literal(resourceTypeId))
                .value("logical_id", bindMarker())
                .value("version", bindMarker())
                .value("resource_payload_key", bindMarker())
            ;

        if (logger.isLoggable(Level.FINE)) {
            logger.fine("Storing payload for '"
                    + resourceTypeId + "/" + logicalId + "/" + version + "'; size=" + payloadStream.size());
        }

        // If the payload is small enough to fit in a single chunk, we can store
        // it in line with the main resource record
        if (storeInline) {
            insert = insert.value("chunk", bindMarker());
        }

        PreparedStatement ps = session.prepare(insert.build());
        final BoundStatementBuilder bsb;
        
        if (storeInline) {
            // small enough, so we bind the payload here
            bsb = ps.boundStatementBuilder(logicalId, version, resourcePayloadKey, payloadStream.wrap());
        } else {
            // too big to be inlined, so don't include the payload here
            bsb = ps.boundStatementBuilder(logicalId, version, resourcePayloadKey);
        }

        try {
            session.execute(bsb.build());
        } catch (Exception x) {
            logger.log(Level.SEVERE, "insert into resource_payloads failed for '"
                    + resourceTypeId + "/" + logicalId + "/" + version + "'", x);
            throw new FHIRPersistenceDataAccessException("Failed inserting into " + RESOURCE_PAYLOADS);
        }
    }

    /**
     * Store the payload data as a contiguous set of rows ordered by
     * an ordinal which, being part of the key, is used to retrieve the data in the same
     * order they were inserted.
     * @param session
     */
    private void storePayloadChunks(CqlSession session) {

        SimpleStatement statement =
            insertInto(PAYLOAD_CHUNKS)
            .value("resource_payload_key", bindMarker())
            .value("ordinal", bindMarker())
            .value("chunk", bindMarker())
            .build();

        PreparedStatement ps = session.prepare(statement);
        
        List<BatchableStatement<?>> statements = new ArrayList<>();
        int ordinal = 0;
        ByteBuffer bb = payloadStream.wrap();
        byte[] buffer = new byte[CHUNK_SIZE];
        while (bb.hasRemaining()) {
            // shame we have to copy the array here rather than subset it
            int len = Math.min(CHUNK_SIZE, bb.remaining());
            bb.get(buffer, 0, len); // read len bytes into the buffer
            
            // Wrap the byte array into a new read-only ByteBuffer
            ByteBuffer chunk = ByteBuffer.wrap(buffer, 0, len).asReadOnlyBuffer();

            // and add the chunk statement to the batch
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("Payload chunk offset[" + ordinal + "] size = " + len);
            }
            BoundStatementBuilder bsb = ps.boundStatementBuilder(resourcePayloadKey, ordinal++, chunk);
            statements.add(bsb.build());

            if (bb.hasRemaining()) {
                // The ByteBuffer adopts the buffer byte array, so we need to make sure
                // we create a new one each time
                buffer = new byte[CHUNK_SIZE];
            }
        }

        BatchStatement batch =
                BatchStatement.newInstance(
                    DefaultBatchType.LOGGED,
                    statements);

        session.execute(batch);
    }
}