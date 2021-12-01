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
        // We may split the payload into multiple chunks - but only if
        // the payload exceeds the chunk size. If it doesn't, we store
        // it in the main resource table, avoiding the cost of a second
        // random read when we need to access it again.
        final boolean storeInline = payloadStream.size() <= CHUNK_SIZE;
        storeResource(session, storeInline);

        if (!storeInline) {
            // payload too big for the main resource table, so break it
            // into smaller chunks and store as adjacent rows in a child
            // table
            storePayloadChunks(session);
        }
    }

    /**
     * Store the resource record
     * @param session
     * @param storeInline when true, store the payload inline in logical_resources
     */
    private void storeResource(CqlSession session, boolean storeInline) throws FHIRPersistenceException {
        RegularInsert insert =
                insertInto(LOGICAL_RESOURCES)
                .value("partition_id", literal(partitionId))
                .value("resource_type_id", literal(resourceTypeId))
                .value("logical_id", bindMarker())
                .value("version", bindMarker())
            ;

        // If the payload is small enough to fit in a single chunk, we can store
        // it in line with the main resource record
        if (storeInline) {
            insert = insert.value("chunk", bindMarker());
        }

        PreparedStatement ps = session.prepare(insert.build());
        final BoundStatementBuilder bsb;
        
        if (storeInline) {
            // small enough, so we bind the payload here
            bsb = ps.boundStatementBuilder(logicalId, version, payloadStream.wrap());
        } else {
            // too big to be inlined, so don't include the payload here
            bsb = ps.boundStatementBuilder(logicalId, version);
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
     * order they were inserted.
     * @param session
     */
    private void storePayloadChunks(CqlSession session) {

        SimpleStatement statement =
            insertInto(PAYLOAD_CHUNKS)
            .value("partition_id", literal(partitionId))
            .value("resource_type_id", literal(resourceTypeId))
            .value("logical_id", bindMarker())
            .value("version", bindMarker())
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
            
            statements.add(ps.bind(logicalId, version, ordinal++, buffer));
            offset += CHUNK_SIZE;
        }

        BatchStatement batch =
                BatchStatement.newInstance(
                    DefaultBatchType.LOGGED,
                    statements);

        session.execute(batch);
    }
}