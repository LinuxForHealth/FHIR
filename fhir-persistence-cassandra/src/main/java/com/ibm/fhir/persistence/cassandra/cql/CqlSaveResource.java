/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.cassandra.cql;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.bindMarker;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.insertInto;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;
import static com.ibm.fhir.persistence.cassandra.cql.SchemaConstants.CHUNK_SIZE;
import static com.ibm.fhir.persistence.cassandra.cql.SchemaConstants.PAYLOAD_CHUNKS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BatchStatement;
import com.datastax.oss.driver.api.core.cql.BatchableStatement;
import com.datastax.oss.driver.api.core.cql.DefaultBatchType;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;

/**
 * DAO command to save a resource payload object in Cassandra.
 */
public class CqlSaveResource {

    // Possibly patient, or some other partitioning key
    private final String partitionId;

    // The logical identifier we have assigned to the resource
    private final String logicalId;

    // The unique identifier to use for the payload
    private final String payloadId;

    // The compressed payload
    private final byte[] payload;

    // The anticipated version
    private final int version;

    /**
     * Public constructor
     * @param partitionId
     * @param logicalId
     * @param payloadId
     * @param version
     * @param payload
     */
    public CqlSaveResource(String partitionId, String logicalId, String payloadId, int version, byte[] payload) {
        this.partitionId = partitionId;
        this.logicalId = logicalId;
        this.payloadId = payloadId;
        this.version = version;
        this.payload = payload;
    }

    /**
     * Store the resource into the cassandra database. We rely on other services to
     * ensure consistency during our writes (which are not isolated/atomic). This is
     * why every object is given a UUID which we can use to check that the records we
     * read are all associated with the same update
     * @param session
     */
    public void run(CqlSession session) {
        storePayloadChunks(session);
    }

    /**
     * Store the payload data as a contiguous set of rows ordered by
     * an ordinal which is used to retrieve the data in the same
     * order so that the original payload byte array can be
     * reconstituted.
     * @param session
     */
    private void storePayloadChunks(CqlSession session) {
//        + "payload_id   text, "
//        + "ordinal       int, "
//        + "chunk        blob, "

        SimpleStatement statement =
            insertInto(PAYLOAD_CHUNKS)
            .value("partition_id", literal(partitionId))
            .value("payload_id", bindMarker())
            .value("ordinal", bindMarker())
            .value("chunk", bindMarker())
            .build();

        PreparedStatement ps = session.prepare(statement);

        List<BatchableStatement<?>> statements = new ArrayList<>();
        int ordinal = 0;
        int offset = 0;
        while (offset < payload.length) {
            // shame we have to copy the array here
            // TODO possible opportunity to optimize
            int to = Math.min(offset + CHUNK_SIZE, payload.length);
            byte[] chunk = Arrays.copyOfRange(payload, offset, to);
            statements.add(ps.bind(partitionId, payloadId, ordinal++, chunk));
            offset += CHUNK_SIZE;
        }

        BatchStatement batch =
                BatchStatement.newInstance(
                    DefaultBatchType.LOGGED,
                    statements);

        session.execute(batch);
    }
}