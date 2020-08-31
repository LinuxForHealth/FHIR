/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.scout.cql;

import java.util.logging.Logger;
import static com.ibm.fhir.persistence.scout.cql.SchemaConstants.*;

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
    private static final Logger logger = Logger.getLogger(CreateSchema.class.getName());
       
    final String keySpace;
    
    public CreateSchema(String keySpace) {
        this.keySpace = keySpace;
    }

    public void run(CqlSession session) {
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
                + "last_modified     timestamp, "
                + "current_version         int, "
                + "payload_id             text, "
                + "parameter_block        blob, "
                + "PRIMARY KEY ((partition_id, resource_type_id), logical_id)"
                + ") WITH CLUSTERING ORDER BY (logical_id ASC)";
        
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
                + "partition_id text, "
                + "payload_id   text, "
                + "ordinal       int, "
                + "chunk        blob, "
                + "PRIMARY KEY (partition_id, payload_id, ordinal)"
                + ") WITH CLUSTERING ORDER BY (payload_id ASC, ordinal ASC)";
        
        logger.info("Running: " + cql);
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
                + "payload_id             text, "
                + "PRIMARY KEY (partition_id, resource_type_id, logical_id, version, last_modified)"
                + ") WITH CLUSTERING ORDER BY (resource_type_id ASC, logical_id ASC, version ASC, last_modified ASC)";
        
        logger.info("Running: " + cql);
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
                + "payload_id             text, "
                + "PRIMARY KEY (partition_id, resource_type_id, last_modified, logical_id, version)"
                + ") WITH CLUSTERING ORDER BY (resource_type_id ASC, last_modified ASC, logical_id ASC, version ASC)";
        
        logger.info("Running: " + cql);
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
                + "payload_id        text, "
                + "PRIMARY KEY ((partition_id, resource_type_id, parameter_name_id), str_value)"
                + ") WITH CLUSTERING ORDER BY (str_value ASC)";
        
        logger.info("Running: " + cql);
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
                + "payload_id         text, "
                + "PRIMARY KEY ((partition_id, resource_type_id, parameter_name_id), str_lower_value, logical_id)"
                + ") WITH CLUSTERING ORDER BY (str_lower_value ASC, logical_id ASC)";
        
        logger.info("Running: " + cql);
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
                + "payload_id        text, "
                + "PRIMARY KEY ((parameter_name_id), str_value, resource_type_id, logical_id)"
                + ") WITH CLUSTERING ORDER BY (str_value ASC, resource_type_id ASC, logical_id ASC)";
        
        logger.info("Running: " + cql);
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
                + "payload_id        text, "
                + "PRIMARY KEY ((parameter_name_id), str_lower_value, resource_type_id, logical_id)"
                + ") WITH CLUSTERING ORDER BY (str_lower_value ASC, resource_type_id ASC, logical_id ASC)";

        logger.info("Running: " + cql);
        session.execute(cql);
    }
}