/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.cassandra.cql;

import static com.ibm.fhir.persistence.cassandra.cql.SchemaConstants.PAYLOAD_CHUNKS;
import static com.ibm.fhir.persistence.cassandra.cql.SchemaConstants.PAYLOAD_RECONCILIATION;
import static com.ibm.fhir.persistence.cassandra.cql.SchemaConstants.PAYLOAD_TRACKING;
import static com.ibm.fhir.persistence.cassandra.cql.SchemaConstants.RESOURCE_PAYLOADS;

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
        createPayloadTrackingTable(session);
        createPayloadReconciliationTable(session);
        
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
        // cluster within each partition by resource_type_id, logical_id, version
        // The resource_payload_key is assigned by the server and is used to help
        // manage rollbacks in a concurrent scenario (so the rollback only removes
        // rows created within the transaction that was rolled back).
        final String cql = "CREATE TABLE IF NOT EXISTS " + RESOURCE_PAYLOADS + " ("
                + "partition_id              text, "
                + "resource_type_id           int, "
                + "logical_id                text, "
                + "version                    int, "
                + "resource_payload_key      text, "
                + "last_modified        timestamp, "
                + "chunk                     blob, "
                + "parameter_block           blob, "
                + "PRIMARY KEY (partition_id, resource_type_id, logical_id, version, resource_payload_key)"
                + ") WITH CLUSTERING ORDER BY (resource_type_id ASC, logical_id ASC, version DESC, resource_payload_key ASC)";

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
        // The resource_payload_key ties the chunks to the RESOURCE_PAYLOADS record
        final String cql = "CREATE TABLE IF NOT EXISTS " + PAYLOAD_CHUNKS + " ("
                + "partition_id           text, "
                + "resource_payload_key   text, "
                + "ordinal                 int, "
                + "chunk                  blob, "
                + "PRIMARY KEY (partition_id, resource_payload_key, ordinal)"
                + ") WITH CLUSTERING ORDER BY (resource_payload_key ASC, ordinal ASC)";

        logger.info("Running: " + cql);
        session.execute(cql);
    }
    
    /**
     * Create the table to track the insertion of payload records. This is used
     * by the reconciliation process to make sure that payload records are attached
     * to a logical resource in the RDBMS system of record. Records in this table
     * can be removed once they have been reconciled, but this is not required.
     * @param session
     */
    protected void createPayloadTrackingTable(CqlSession session) {
        // partition by partition_id. This is application defined, and is used to distribute
        // the data across a number of partitions, which all must be scanned to make sure
        // every record gets processed
        // cluster within each partition by tstamp
        final String cql = "CREATE TABLE IF NOT EXISTS " + PAYLOAD_TRACKING + " ("
                + "partition_id         smallint, "
                + "tstamp                 bigint, "
                + "resource_type_id          int, "
                + "logical_id               text, "
                + "version                   int, "
                + "resource_payload_key     text, "
                + "payload_partition_id     text, "
                + "PRIMARY KEY (partition_id, tstamp, resource_type_id, logical_id, version, resource_payload_key)"
                + ") WITH CLUSTERING ORDER BY (tstamp ASC)";

        logger.info("Running: " + cql);
        session.execute(cql);
    }

    /**
     * Create the table to track the reconciliation of payload records. This tells
     * the reconciliation service where to start scanning the payload_tracking table
     * within each partition. The reconciliation scanner needs to be careful to
     * avoid issues with clock drift in clusters so should stop attempting to
     * reconcile records more recent than the ingestion transaction timeout value (e.g.
     * 2 minutes by default). Some systems may be configured with even larger
     * timeouts, so this must be taken into account. This is to ensure that
     * reconciliation doesn't miss records which appear after but with timestamps before
     * the latest tstamp in a given partition. Handling this doesn't generate any
     * logical inconsistencies, but may require a small amount of work to be repeated.
     * @param session
     */
    protected void createPayloadReconciliationTable(CqlSession session) {
        final String cql = "CREATE TABLE IF NOT EXISTS " + PAYLOAD_RECONCILIATION + " ("
                + "partition_id   smallint, " // FK to payload_tracking table
                + "tstamp           bigint, " // FK to payload_tracking table
                + "PRIMARY KEY (partition_id, tstamp) "
                + ") WITH CLUSTERING ORDER BY (tstamp ASC)";

        logger.info("Running: " + cql);
        session.execute(cql);
    }
}