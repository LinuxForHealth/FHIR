/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.database.utils.api;


/**
 * The flavor of database schema
 *   PLAIN       - the schema we typically deploy to Derby or PostgreSQL
 *   MULTITENANT - on Db2 supporting multiple tenants using partitioning and RBAC
 *   DISTRIBUTED - for use with distributed technologies like Citus DB
 *   SHARDED     - explicitly sharded using an injected shard_key column
 */
public enum SchemaType {
    PLAIN,
    MULTITENANT,
    DISTRIBUTED,
    SHARDED
}
