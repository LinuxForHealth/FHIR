/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.scout.cql;

import com.datastax.oss.driver.api.core.CqlSession;

/**
 * Create the Cassandra schema.
 * The partition_id is application-defined and will be different depending on the
 * type of resource (although it is always deterministic). For resources associated
 * with a patient (e.g. observations), the partition_id will be the patient logical
 * id. If there is no obvious partition key, the key will simply be the logical id
 * for the resource.
 * TODO. In the latter case, should we use a different table? Would be more efficient
 * in that case if we have data which isn't isolated by patient.
 */
public class CreateSchema {
    public static final String LOGICAL_RESOURCES = "logical_resources";
    public static final String RESOURCE_VERSIONS = "resource_versions";
    public static final String PAYLOAD_CHUNKS = "payload_chunks";
    
    // Break binary data into bite-sized pieces when storing
    public static final long chunkSize = 1024L * 1024;
    
    final String keySpace;
    final String replicationClass;
    final int replicationFactor;
    
    public CreateSchema(String keySpace, String replicationClass, int replicationFactor) {
        this.keySpace = keySpace;
        this.replicationClass = replicationClass;
        this.replicationFactor = replicationFactor;
    }

    public void run(CqlSession session) {
        createKeyspace(session);
        useKeyspace(session);
        createLogicalResourcesTable(session);
        createPayloadChunksTable(session);
        createResourceVersionsTable(session);
    }
    
    protected void createKeyspace(CqlSession session) {
        final String cql = "CREATE KEYSPACE IF NOT EXISTS " + keySpace 
                + " WITH REPLICATION = {"
                + "'class':'" + replicationClass + "', "
                + "'replication_factor':'" + replicationFactor + "}";
        
        session.execute(cql);
    }
    
    protected void useKeyspace(CqlSession session) {
        final String cql = "USE " + this.keySpace;
        session.execute(cql);
    }

    protected void createLogicalResourcesTable(CqlSession session) {
        // partition by partition_id (application-defined, like patient logical id)
        // cluster within each partition by resource_type_id, payload_id
        final String cql = "CREATE TABLE IF NOT EXISTS " + LOGICAL_RESOURCES + " ("
                + "partition_id text, "
                + "resource_type_id int, "
                + "logical_id text, "
                + "current_version int, "
                + "parameter_block binary,"
                + "PRIMARY KEY (partition_id, resource_type_id, logical_id)"
                + ");";
        
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
                + "partition_id text, "
                + "payload_id text, "
                + "ordinal int, "
                + "chunk blob, "
                + "PRIMARY KEY (partition_id, payload_id, ordinal)"
                + ");";
        
        session.execute(cql);
    }
    
    protected void createResourceVersionsTable(CqlSession session) {
        // partition by partition_id (application-defined, like patient logical id)
        // cluster within each partition by resource_type_id, payload_id
        final String cql = "CREATE TABLE IF NOT EXISTS " + RESOURCE_VERSIONS + " ("
                + "partition_id text, "
                + "resource_type_id int, "
                + "logical_id text, "
                + "version int, "
                + "payload_id text, "
                + "PRIMARY KEY (partition_id, resource_type_id, logical_id, version)"
                + ");";
        
        session.execute(cql);
    }
}
