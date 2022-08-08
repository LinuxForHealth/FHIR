/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.cassandra.cql;

import static org.linuxforhealth.fhir.persistence.cassandra.cql.SchemaConstants.PAYLOAD_CHUNKS;
import static org.linuxforhealth.fhir.persistence.cassandra.cql.SchemaConstants.RESOURCE_PAYLOADS;
import static org.linuxforhealth.fhir.persistence.cassandra.cql.SchemaConstants.RESOURCE_VERSIONS;

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
        createResourceVersionsTable(session);
        
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
        // partition by resource_type_id, logical_id, version
        // The resource_payload_key is assigned by the server and is used to help
        // manage rollbacks in a concurrent scenario (so the rollback only removes
        // rows created within the transaction that was rolled back).
        final String cql = "CREATE TABLE IF NOT EXISTS " + RESOURCE_PAYLOADS + " ("
                + "resource_type_id           int, "
                + "logical_id                text, "
                + "version                    int, "
                + "resource_payload_key      text, "
                + "last_modified        timestamp, "
                + "chunk                     blob, "
                + "parameter_block           blob, "
                + "PRIMARY KEY ((resource_type_id, logical_id, version), resource_payload_key)"
                + ") WITH CLUSTERING ORDER BY (resource_payload_key ASC)";

        logger.info("Running: " + cql);
        session.execute(cql);
    }

    /**
     * Create a table to identify the versions associated with each logical resource.
     * The partition id for this table is based on only {resource_type_id, logical_id}
     * allowing the application to select the list of versions. The versions are also
     * clustered with descending order, making it trivial to identify the latest version
     * @param session
     */
    protected void createResourceVersionsTable(CqlSession session) {
        // partition by resource_type_id, logical_id
        final String cql = "CREATE TABLE IF NOT EXISTS " + RESOURCE_VERSIONS + " ("
                + "resource_type_id           int, "
                + "logical_id                text, "
                + "version                    int, "
                + "resource_payload_key      text, "
                + "PRIMARY KEY ((resource_type_id, logical_id), version, resource_payload_key)"
                + ") WITH CLUSTERING ORDER BY (version DESC)";

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
        // The resource_payload_key is unique for each RESOURCE_PAYLOADS record
        // so is used here a foreign key (parent) and also acts as the partition
        // id for this table
        final String cql = "CREATE TABLE IF NOT EXISTS " + PAYLOAD_CHUNKS + " ("
                + "resource_payload_key   text, "
                + "ordinal                 int, "
                + "chunk                  blob, "
                + "PRIMARY KEY (resource_payload_key, ordinal)"
                + ") WITH CLUSTERING ORDER BY (ordinal ASC)";

        logger.info("Running: " + cql);
        session.execute(cql);
    }
}