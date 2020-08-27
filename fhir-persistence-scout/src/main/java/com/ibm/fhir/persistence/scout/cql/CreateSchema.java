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
    public static final String RESOURCE_HISTORY = "resource_history";
    public static final String PAYLOAD_CHUNKS = "payload_chunks";
    public static final String LAST_MODIFIED = "last_modified";
    
    // Resource type Parameter tables
    public static final String PARAM_STR_VALUES = "param_str_values";
    public static final String PARAM_STR_LOWER_VALUES = "param_str_lower_values";

    // System Parameter tables
    public static final String SYSTEM_STR_VALUES = "system_str_values";
    public static final String SYSTEM_STR_LOWER_VALUES = "system_str_lower_values";

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
        createResourceHistoryTable(session);
        createLastModifiedTable(session);
        createParamStrValuesTable(session);
        createParamStrLowerValuesTable(session);
        
        // support for global system search
        createSystemStrValuesTable(session);
        createSystemStrLowerValuesTable(session);
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
                + "partition_id           text, "
                + "resource_type_id        int, "
                + "logical_id             text, "
                + "last_modified     timestamp, "
                + "current_version         int, "
                + "payload_id             text, "
                + "parameter_block        blob  "
                + "PRIMARY KEY ((partition_id, resource_type_id), logical_id)"
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
                + "payload_id   text, "
                + "ordinal       int, "
                + "chunk        blob  "
                + "PRIMARY KEY (partition_id, payload_id, ordinal)"
                + ");";
        
        session.execute(cql);
    }
    
    /**
     * A table which maps each version of a resource to the identifier
     * of the payload object, which is held in a separate chunks table
     * which allows large objects to be split across multiple rows (or
     * stored offline in an S3 object store, for example.
     * Also, payloads are immutable objects and so they can be cached
     * using payload_id as the key.
     * @param session
     */
    protected void createResourceHistoryTable(CqlSession session) {
        // partition by partition_id (application-defined, like patient logical id)
        // cluster within each partition by resource_type_id, payload_id
        final String cql = "CREATE TABLE IF NOT EXISTS " + RESOURCE_HISTORY + " ("
                + "partition_id           text, "
                + "resource_type_id        int, "
                + "logical_id             text, "
                + "version                 int, "
                + "last_modified     timestamp, "
                + "payload_id             text  "
                + "PRIMARY KEY (partition_id, resource_type_id, logical_id, version, last_modified)"
                + ");";
        
        session.execute(cql);
    }

    /**
     * Create a table which can be used to find resources of a particular type modified since
     * a particular time
     * @param session
     */
    protected void createLastModifiedTable(CqlSession session) {
        // partition by partition_id (application-defined, like patient logical id)
        // cluster within each partition by resource_type_id, payload_id
        final String cql = "CREATE TABLE IF NOT EXISTS " + LAST_MODIFIED + " ("
                + "partition_id           text, "
                + "resource_type_id        int, "
                + "last_modified     timestamp, "
                + "logical_id             text, "
                + "version                 int, "
                + "payload_id             text  "
                + "PRIMARY KEY (partition_id, resource_type_id, last_modified, logical_id, version)"
                + ");";
        
        session.execute(cql);
    }

    /**
     * Create a table which can be used to find resources based on a named string parameter
     * @param session
     */
    protected void createParamStrValuesTable(CqlSession session) {
        
        final String cql = "CREATE TABLE IF NOT EXISTS " + PARAM_STR_VALUES + " ("
                + "partition_id      text, "
                + "resource_type_id   int, "
                + "parameter_name_id  int, "
                + "str_value         text, "
                + "logical_id        text, "
                + "payload_id        text  "
                + "PRIMARY KEY ((partition_id, resource_type_id, parameter_name_id), str_value)"
                + ");";
        
        session.execute(cql);
    }

    /**
     * Create a table which can be used to find resources based on a named string parameter
     * @param session
     */
    protected void createParamStrLowerValuesTable(CqlSession session) {
        // partition by partition_id (application-defined, like patient logical id)
        // cluster within each partition by resource_type_id, payload_id
        final String cql = "CREATE TABLE IF NOT EXISTS " + PARAM_STR_LOWER_VALUES + " ("
                + "partition_id       text, "
                + "resource_type_id    int, "
                + "parameter_name_id   int, "
                + "str_lower_value    text, "
                + "logical_id         text, "
                + "payload_id         text  "
                + "PRIMARY KEY ((partition_id, resource_type_id, parameter_name_id), str_lower_value, logical_id)"
                + ");";
        
        session.execute(cql);
    }

    /**
     * Create a table which can be used to find resources based on a named string parameter
     * at the system level
     * @param session
     */
    protected void createSystemStrValuesTable(CqlSession session) {
        // For system search, the only practical partitioning key we can use unfortunately
        // is the parameter_name_id. This is because we need these parameters to
        // span multiple resource types and therefore cannot use resource type as
        // a partition key
        final String cql = "CREATE TABLE IF NOT EXISTS " + SYSTEM_STR_VALUES + " ("
                + "parameter_name_id  int, "
                + "str_value         text, "
                + "resource_type_id   int, "
                + "logical_id        text, "
                + "payload_id        text  "
                + "PRIMARY KEY (parameter_name_id), str_value, resource_type_id, logical_id)"
                + ");";
        
        session.execute(cql);
    }

    /**
     * Create a table which can be used to find resources based on a named string parameter
     * at the system level
     * @param session
     */
    protected void createSystemStrLowerValuesTable(CqlSession session) {
        // For system search, the only practical partitioning key we can use unfortunately
        // is the parameter_name_id. This is because we need these parameters to
        // span multiple resource types and therefore cannot use resource type as
        // a partition key
        final String cql = "CREATE TABLE IF NOT EXISTS " + SYSTEM_STR_LOWER_VALUES + " ("
                + "parameter_name_id  int, "
                + "str_lower_value   text, "
                + "resource_type_id   int, "
                + "logical_id        text, "
                + "payload_id        text  "
                + "PRIMARY KEY (parameter_name_id), str_lower_value, resource_type_id, logical_id)"
                + ");";

        session.execute(cql);
    }
}