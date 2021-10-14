/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.cassandra.payload;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.bindMarker;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.insertInto;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;
import static com.ibm.fhir.persistence.cassandra.cql.SchemaConstants.CHUNK_SIZE;
import static com.ibm.fhir.persistence.cassandra.cql.SchemaConstants.LOGICAL_RESOURCES;
import static com.ibm.fhir.persistence.cassandra.cql.SchemaConstants.PAYLOAD_CHUNKS;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
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
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDataAccessException;
import com.ibm.fhir.persistence.util.InputOutputByteStream;

/**
 * DAO to store the resource record and payload data chunks
 */
public class CqlStorePayload {
    private static final Logger logger = Logger.getLogger(CqlStorePayload.class.getName());

    // Possibly patient, or some other partitioning key
    private final String partitionId;

    private final int resourceTypeId;

    // The logical identifier we have assigned to the resource
    private final String logicalId;

    // The compressed payload
    private final InputOutputByteStream payloadStream;

    // The anticipated version
    private final int version;

    /**
     * Public constructor
     * @param partitionId
     * @param resourceTypeId
     * @param logicalId
     * @param version
     * @param payloadStream
     */
    public CqlStorePayload(String partitionId, int resourceTypeId, String logicalId, int version, InputOutputByteStream payloadStream) {
        this.partitionId = partitionId;
        this.logicalId = logicalId;
        this.resourceTypeId = resourceTypeId;
        this.version = version;
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
        // Random id string used to tie together the resource record to
        // the child payload chunk records. This is needed because
        // we may split the payload into multiple chunks - but only if
        // the payload exceeds the chunk size. If it doesn't, we store
        // it in the main resource table, avoiding the cost of a second
        // random read when we need to access it again.
        final String payloadId = payloadStream.size() > CHUNK_SIZE ? UUID.randomUUID().toString() : null;
        storeResource(session, payloadId);

        if (payloadId != null) {
            // payload too big for the main resource table, so break it
            // into smaller chunks and store as adjacent rows in a child
            // table
            storePayloadChunks(session, payloadId);
        }
    }

    /**
     * Store the resource record
     * @param session
     * @param payloadId the unique id for storing the payload in multiple chunks
     */
    private void storeResource(CqlSession session, String payloadId) throws FHIRPersistenceException {
        RegularInsert insert =
                insertInto(LOGICAL_RESOURCES)
                .value("partition_id", literal(partitionId))
                .value("resource_type_id", bindMarker())
                .value("logical_id", bindMarker())
                .value("version", bindMarker())
            ;

        // If we are given a payloadId it means that the payload is too large
        // to fit inside a single row, so instead we break it into multiple
        // rows in the payload_chunks table, using the payloadId as the key
        if (payloadId != null) {
            insert.value("payload_id", bindMarker());
        } else {
            insert.value("chunk", bindMarker());
        }

        PreparedStatement ps = session.prepare(insert.build());
        BoundStatementBuilder bsb = ps.boundStatementBuilder(resourceTypeId, logicalId, version);

        if (payloadId != null) {
            // payload is too big to go in the main table, so just store the reference id here
            bsb.setString(4, payloadId);
        } else {
            // small enough, so we store directly in the main logical_resources table
            bsb.setByteBuffer(4, payloadStream.wrap());
        }

        try {
            session.execute(bsb.build());
        } catch (Exception x) {
            logger.log(Level.SEVERE, "insert into logical_resources failed for '"
                    + partitionId + "/" + resourceTypeId + "/" + logicalId + "/" + version + "'", x);
            throw new FHIRPersistenceDataAccessException("Failed inserting into " + LOGICAL_RESOURCES);
        }
    }

    /**
     * Store the payload data as a contiguous set of rows ordered by
     * an ordinal which, being part of the key, is used to retrieve the data in the same
     * order so that the original order.
     * @param session
     * @param payloadId
     */
    private void storePayloadChunks(CqlSession session, String payloadId) {
//        + "partition_id text, "
//        + "payload_id   text, "
//        + "ordinal       int, "
//        + "chunk        blob, "

        SimpleStatement statement =
            insertInto(PAYLOAD_CHUNKS)
            .value("partition_id", bindMarker())
            .value("payload_id", bindMarker())
            .value("ordinal", bindMarker())
            .value("chunk", bindMarker())
            .build();

        PreparedStatement ps = session.prepare(statement);

        List<BatchableStatement<?>> statements = new ArrayList<>();
        int ordinal = 0;
        int offset = 0;
        ByteBuffer bb = payloadStream.wrap();
        byte[] buffer = new byte[CHUNK_SIZE];
        while (offset < payloadStream.size()) {
            // shame we have to copy the array here
            int len = Math.min(CHUNK_SIZE, payloadStream.size() - offset);
            bb.get(buffer, offset, len);
            
            statements.add(ps.bind(partitionId, payloadId, ordinal++, buffer));
            offset += CHUNK_SIZE;
        }

        BatchStatement batch =
                BatchStatement.newInstance(
                    DefaultBatchType.LOGGED,
                    statements);

        session.execute(batch);
    }
}