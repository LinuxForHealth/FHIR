/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.cassandra.cql;

import static com.ibm.fhir.persistence.cassandra.cql.SchemaConstants.LOGICAL_RESOURCES;
import static com.ibm.fhir.persistence.cassandra.cql.SchemaConstants.PAYLOAD_CHUNKS;

import java.util.logging.Logger;

import com.datastax.oss.driver.api.core.CqlSession;

/**
 * Create the Cassandra schema.
 * The partition_id is application-defined and will be different depending on the
 * type of resource (although it is always deterministic). For resources associated
 * with a patient (e.g. observations), the partition_id will be the patient logical
 * id. If there is no obvious partition key, the key will simply be the logical id
 * for the resource.
 */
public class CreateSchema {
    private static final Logger logger = Logger.getLogger(CreateSchema.class.getName());

    // The (tenant-specific) keyspace
    final String keySpace;

    /**
     * Public constructor
     * @param keySpace representing the area private to each tenant
     */
    public CreateSchema(String keySpace) {
        this.keySpace = keySpace;
    }

    /**
     * Create the tables used to support payload persistence for this keyspace (each tenant gets
     * its own keyspace)
     * @param session
     */
    public void run(CqlSession session) {
        useKeyspace(session);
        createLogicalResourcesTable(session);
        createPayloadChunksTable(session);

        logger.info("Schema definition complete for keySpace '" + this.keySpace + "'");
    }

    /**
     * Each tenant gets its own keyspace
     * @param session
     * @param replicationClass
     * @param replicationFactor
     */
    public void createKeyspace(CqlSession session, String replicationClass, int replicationFactor) {
        final String cql = "CREATE KEYSPACE IF NOT EXISTS " + keySpace
                + " WITH REPLICATION = {"
                + "'class':'" + replicationClass + "', "
                + "'replication_factor':" + replicationFactor + "}";

        logger.info("Running: " + cql);
        session.execute(cql);
    }

    protected void useKeyspace(CqlSession session) {
        final String cql = "USE " + this.keySpace;
        logger.info("Running: " + cql);
        session.execute(cql);
    }

    protected void createLogicalResourcesTable(CqlSession session) {
        // partition by partition_id (application-defined, like patient logical id)
        // cluster within each partition by resource_type_id, payload_id
        final String cql = "CREATE TABLE IF NOT EXISTS " + LOGICAL_RESOURCES + " ("
                + "partition_id           text, "
                + "resource_type_id        int, "
                + "logical_id             text, "
                + "version                 int, "
                + "last_modified     timestamp, "
                + "payload_id             text, "
                + "chunk                  blob, "
                + "parameter_block        blob, "
                + "PRIMARY KEY (partition_id, resource_type_id, logical_id, version)"
                + ") WITH CLUSTERING ORDER BY (resource_type_id ASC, logical_id ASC, version DESC)";

        logger.info("Running: " + cql);
        session.execute(cql);
    }

    /**
     * In Cassandra, blobs are limited to 2GB, but the document states that the practical limit
     * is less than 1MB. To avoid issues when storing arbitrarily large FHIR resources, the
     * payload is compressed and stored as a series of chunks each <= 1MB. Chunk order is
     * maintained by the ordinal field.
     * @param session
     */
    protected void createPayloadChunksTable(CqlSession session) {
        // partition by partition_id (application-defined, like patient logical id)
        // cluster within each partition by resource_type_id, payload_id
        final String cql = "CREATE TABLE IF NOT EXISTS " + PAYLOAD_CHUNKS + " ("
                + "payload_id   text, "
                + "ordinal       int, "
                + "chunk        blob, "
                + "PRIMARY KEY (payload_id, ordinal)"
                + ") WITH CLUSTERING ORDER BY (ordinal ASC)";

        logger.info("Running: " + cql);
        session.execute(cql);
    }
}