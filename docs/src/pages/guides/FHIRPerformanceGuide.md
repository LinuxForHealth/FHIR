---
layout: post
title:  IBM FHIR Server Performance Guide
description: IBM FHIR Server Performance Guide
Copyright: years 2020, 2021
lastupdated: "2020-06-01"
permalink: /FHIRServerPerformanceGuide/
---

**Table of Contents**

- [1 Overview](#1-overview)
- [2 System Sizing](#2-system-sizing)
- [3 FHIR Server Configuration](#3-fhir-server-configuration)
    - [3.1 Concurrency](#31-concurrency)
        - [3.1.1 Liberty Profile Concurrency](#311-liberty-profile-concurrency)
        - [3.1.2 Database Max Connections](#312-database-max-connections)
        - [3.1.3 JEE Datasource Default, Recommended](#313-jee-datasource-default-recommended)
    - [3.2 Transaction Timeout](#32-transaction-timeout)
    - [3.3 Session Affinity](#33-session-affinity)
    - [3.4 Value-Id Caches](#34-value-id-caches)
    - [3.5 Compartment Search Optimization](#35-compartment-search-optimization)
    - [3.6 Usage of Server Resource Provider](#36-usage-of-server-resource-provider)
    - [3.7 Usage of the extension-search-parameters.json file](#37-usage-of-the-extension-search-parameters.json-file)
- [4 Database Tuning](#4-database-tuning)
    - [4.1 PostgreSQL](#41-postgresql)
        - [4.1.1 Fillfactor](#411-fillfactor)
        - [4.1.2 Tuning Auto-vacuum](#412-tuning-auto-vacuum)
        - [4.1.3 Transaction Id Wraparound](#413-transaction-id-wraparound)
        - [4.1.4 Vacuum Monitoring](#414-vacuum-monitoring)
        - [4.1.5 Max Locks](#415-max-locks)
    - [4.2 IBM Db2](#42-ibm-db2)
    - [4.3 Derby](#43-derby)
- [5 Ingestion Scenarios](#5-ingestion-scenarios)
    - [5.1 Logical Id Generation](#51-logical-id-generation)
    - [5.2 Conditional Update](#52-conditional-update)
- [6 Client Access Scenarios](#6-client-access-scenarios)
    - [6.1 Read](#61-read)
    - [6.2 Version Read](#62-version-read)
    - [6.3 History](#63-history)
    - [6.4 Search Performance](#64-search-performance)
    - [6.5 Search Examples](#65-search-examples)
    - [6.6 Tools](#66-tools)
    - [6.7 Making FHIR Requests With curl](#67-making-fhir-requests-with-curl)
    - [6.8 Making FHIR Requests with IBM FHIR Server Client](#68-making-fhir-requests-with-the-ibm-fhir-server-client)

# 1. Overview

This guide describes how to tune IBM FHIR Server and its database to get the best performance. It also describes different FHIR query strategies which may help to work around specific performance issues.

Note: all logical-ids and resources in this guide are examples and do not refer to actual patient data.

# 2. System Sizing

The sizing table below should be considered a starting point. Actual requirements may vary greatly based on the specific scenarios for a given deployment. For example, search-heavy workloads will require more database CPU and IOPS capacity than a system servicing simple reads.

CPU consumption of the IBM FHIR Server is closely correlated with the number of resources being processed, particularly during ingestion where processing involves:

* Parsing
* Validation
* Search Parameter Evaluation
* Persistence

The following sizes are guidelines only. You should test and measure for your specific use-cases.

**Data Volume and Load Examples**

| T-Shirt Size | Patient Lives | Searches/s | Resource reads/s | Resource writes/s | Daily New Resources |
| ------------ | ------------- | ---------- | ---------------- | ----------------- | ------------------- |
| Small (S)    |     3,000,000 |         10 |              350 |               100 |           1,440,000 |
| Medium (M)   |    10,000,000 |         20 |              900 |               300 |           4,320,000 |
| Large (L)    |    35,000,000 |         70 |            3,500 |             1,100 |          15,840,000 |

<br/>

**Example System Sizing**

| T-Shirt Size | Server Nodes | Server Cores | Server GB | DB Cores | DB GB | IOPS | Total Cores | Total GB |
| ------------ | ------------ | ------------ | --------- | -------- | ----- | ---- | ----------- | -------- |
| Small (S)    |            2 |            2 |         4 |        4 |     8 |   3K |           8 |       16 |
| Medium (M)   |            4 |            4 |         8 |        8 |    16 |  10K |          24 |       48 |
| Large (L)    |            6 |            8 |         8 |       24 |    64 |  35K |          72 |      112 |

<br/>

**Note and Assumptions:**

1. Resource ingestion limited to a 4 hour window;
2. Resource reads/second represents total from all read, vread, history and search requests;
3. Searches and reads mostly occur during the business day, leaving capacity for maintenance tasks and new resource ingestion during off-peak hours;
4. Average resource size is 2KiB.

<br/>

# 3. FHIR Server Configuration

Terminology:
* **tenant_name** - the name/id of a tenant. Used interchangeably with tenant_id;
* **tenant_id** - the name/id of a tenant. Used interchangeably with tenant_name;
* **tenant_key** - a password fragment used in Db2 to verify tenant access ;
* **datastore** - represents a logical database used by the persistence layer to store/retrieve FHIR resources;
* **datasource** - the JTA object from which the persistence layer code can obtain JDBC connections to the underlying database. A datastore may include multiple datasource definitions used for different purposes;
* **ds-id** - an identifier representing a datastore used for a tenant.


## 3.1. Concurrency

This section describes how to configure the IBM FHIR Server and its database for concurrency.

### 3.1.1. Liberty Profile Concurrency

Liberty Profile uses an executor service to handle incoming HTTP/S requests. By default, the executor service automatically adjusts its thread pool size to most efficiently handle the request load. Although the executor service can be configured, we recommend using the default configuration. The best solution for supporting greater concurrency is to scale-out additional instances of the IBM FHIR Server.

### 3.1.2. Database Max Connections

Db2 and PostgreSQL limit the maximum number of open connections. It is important to configure the database in conjunction with the Liberty Profile datasource connection pools to avoid connection failures which will result in HTTP 500 errors being returned from the IBM FHIR Server.

Assuming there are N instances of the IBM FHIR Server, the recommended connection limits should be configured as follows:


| Database     |          Property Name          | Recommended Setting  |
| ------------ | ------------------------------- | -------------------- |
| IBM Db2      | MAX_CONNECTIONS/MAX_COORDAGENTS | maxPoolSize * N + 20 |
| PostgreSQL   |                 max_connections | maxPoolSize * N + 15 |
| Derby        |                             N/A |                  N/A |


See [Managing PostgreSQL Connections](https://cloud.ibm.com/docs/databases-for-postgresql?topic=databases-for-postgresql-managing-connections) in the IBM Cloud documentation for more information.

### 3.1.3. JEE Datasource (Default, Recommended)

The recommended approach for tenant datatstore configuration is to use individual JTA datasources, each with their own connection manager (connection pool):

```
    <dataSource id="fhirDatasourcePGCloudDefault" jndiName="jdbc/fhir_tenant1_default" type="javax.sql.XADataSource" statementCacheSize="200" syncQueryTimeoutWithTransactionTimeout="true" validationTimeout="30s">
        <jdbcDriver javax.sql.XADataSource="org.postgresql.xa.PGXADataSource" libraryRef="sharedLibPostgres"/>
            <properties.postgresql
                 serverName="your.postgres.host"
                 portNumber="5432"
                 databaseName="fhirdb"
                 user="fhirserver"
                 password="change-password"
                 currentSchema="fhirdata"
                 ssl="true"
                 sslmode="require"
                 sslrootcert="resources/security/your-postgres-host.crt" />
        />
        <connectionManager maxPoolSize="200" minPoolSize="20" connectionTimeout="60s" maxIdleTime="2m" numConnectionsPerThreadLocal="1"/>
    </dataSource>
```

Because each datasource gets its own connection manager, you can tune each independently. If multiple datasources point to the same database (for example using different schemas to support multi-tenancy) be sure to configure the database `max_connections` accordingly. Also, remember to sum the maxPoolSize for all datasources across all IBM FHIR Server nodes in your deployment. The `numConnectionsPerThreadLocal` value should be set to `1`. This improves concurrency and reduces the amount of time it takes to acquire a connection, especially on systems with large core counts.

Each JTA datasource should be configured in its own `.xml` server configuration file and placed into `{fhir-server-home}/configDropins/overrides` where it will be picked up automatically by Liberty Profile on startup.

## 3.2. Transaction Timeout

Long transactions consume significant resources so to protect the system, Liberty will time-out a transaction after 2 minutes (120s) by default. When a transaction times out, Liberty will forcibly close any database connection currently executing a statement and the IBM FHIR Server will return an HTTP 500 response to the caller. The maximum transaction time can be modified using the `<transaction>` element in the Liberty server configuration and, by default, the IBM FHIR Server will set this from  the `FHIR_TRANSACTION_MANAGER_TIMEOUT` variable as described in Section 3.3.1.3 Database Access TransactionManager Timeout of the [IBM FHIR Server User's Guide](https://ibm.github.io/FHIR/guides/FHIRServerUsersGuide/#331-the-jdbc-persistence-layer).

The following table summarizes how the transaction timeout is used for different request types:

| Request Type | Transaction Scope and Usage |
| ------------------ | ------------------------------------------------------------ |
| READ               | Single transaction scope for entire request |
| VREAD              | Single transaction scope for entire request |
| HISTORY READ       | Single transaction scope for entire request |
| SEARCH             | Single transaction scope for entire request |
| POST/PUT           | Single transaction scope for entire request |
| Batch Bundle       | Transaction per bundle entry. Request processing time can therefore exceed totalTranLifetimeTimeout |
| Transaction Bundle | Single transaction scope for entire request |
| $reindex           | One HTTP call can request multiple resources to be reindexed. By default, each resource is reindexed in the scope of its own transaction. Reindexing is a relatively quick operation per resource - usually well under 1s - so transaction timeouts are unlikely. However, if a list of index IDs is specified, all those resources will be reindexed within a single tranaction, so reduce the number index IDs specified if transaction timeouts occur. Use concurrent requests to increase overall throughput. |

Because some requests use multiple transactions under the covers, the overall request response time can sometimes be greater than the transaction timeout. There is no server-side tuneable property for the overall request processing time. Tuning of the client read timeout and/or network configuration may be required when extending the maximum transaction time to more than 2 minutes, or supporting multi-transaction requests which also exceed 2 minutes.

Firewalls or other components in the flow between a client and the IBM FHIR Server may forcibly close (reset) a connection which is considered idle. This is usually because no packets associated with the TCP connection will flow between the client and the IBM FHIR Server until the response is returned. There are two ways to address this:

1. Configure the network path to make sure that TCP idle timeout exceeds the client read timeout for all components in the client-server flow. This is impractical unless the infrastructure is dedicated (e.g. an internal system-to-system flow), even then it might not be desirable or allowed;
2. Configure TCP keep-alive (SO_KEEPALIVE) on the connection. This instructs the operating system to occassionally send packets over the wire to let the networking components know that the connection is still active while the client waits for a response from the server. Some clients may configure keep-alive by default, in which case no action is required. Note that TCP keep-alive should not be confused with HTTP Keep-Alive. The TCP keep-alive and client read-timeout values should be considered together. There is no point configuring TCP keep-alive if the delay before sending the first packet is longer than the client read-timeout. Likewise, TCP keep-alive will not prevent a client read from timing out. TCP keep-alive only ensures a connection is not reset by a network component thinking it is idle. The timing values you configure need to be guided by the network configuration in your particular solution.

## 3.3. Session Affinity

TLS connection setup is a costly CPU operation. It is therefore important to ensure that routing components are configured for session affinity to avoid unnecessary connection setup costs. Clients should be written to reuse connections when making multiple requests.

## 3.4. Value-Id Caches

The IBM FHIR Server uses internal memory caches for resource type names, parameter names, references, codes and systems. These caches use a least-recently-used (LRU) strategy to avoid unbound growth which would result in an out-of-memory (OOM) condition.

Currently the IBM FHIR Servers do not use sharding(*) to distribute client requests and so any client request may hit any server in a given cluster. This means that the memory used for caching value-id lookups is not distributed, but each servers' cache is complete and may contain data also cached in another server. This may be revised in future releases if it becomes a scaling issue.

(*) - sharding is really the responsibility of the component used to route requests to the IBM FHIR Servers, and if such a component were to support sharding, it would help to reduce the cache pressure by distributing values among the available cache instances.

The following datasource properties in fhir-server-config.json are used to tune the size of the value-id caches:


| Property                | Default Value | Units           | Description |
| ----------------------- | ------------- | --------------- | ----------- |
| externalSystemCacheSize |          1000 | Number of items | Size of the LRU cache used to hold unique code-system values. Per tenant/datastore. |
| externalValueCacheSize  |        100000 | Number of items | Size of the LRU cache used to hold unique token values. Per tenant/datastore. |


The caches are isolated by tenant and specific to each datasource defined for that tenant:

``` json
{
    "fhirServer": {
        "persistence": {
            "factoryClassname": "com.ibm.fhir.persistence.jdbc.FHIRPersistenceJDBCFactory",
            "common": {
                "__comment": "Configuration properties common to all persistence layer implementations",
                "updateCreateEnabled": true
            },
            "jdbc": {
                ...
                "enableCodeSystemsCache": true,
                "enableParameterNamesCache": true,
                "enableResourceTypesCache": true
            },
            "datasources": {
                "default": {
                    "jndiName": "jdbc/bootstrap_default_default",
                    "type": "derby",
                    "currentSchema": "APP",
                    "externalSystemCacheSize": 1000,
                    "externalValueCacheSize": 100000
                },
                ...
            }
        }
    }
}
```

Currently no cache-hit metrics are exposed related to the caches. Tuning relies on Application Performance Management (APM) profiling and monitoring database activity looking for frequent value-id lookups against the following tables:

* PARAMETER_NAMES
* RESOURCE_TYPES
* COMMON_TOKEN_VALUES
* CODE_SYSTEMS

The values for PARAMETER_NAMES and RESOURCE_TYPES are supposed to be fully cached. Any substantial reads (selects) from these tables after initial startup/first request should be considered a defect.

## 3.5. Compartment Search Optimization

Resources are assigned to various compartments using expressions with multiple terms. In the IBM FHIR Server JDBC persistence layer, these expressions are translated to SQL predicates with multiple `OR` statements. These `ORs` make it more difficult for the query optimizer to compute the most efficient execution plan resulting in a slow query. To address this, the IBM FHIR Server evaluates the compartment membership expression during ingestion and stores the results. The SQL query can then be written using a single value predicate resulting in faster query.

## 3.6. Usage of Server Resource Provider

The IBM FHIR Server has a dynamic registry of conformance resources. The built-in "ServerRegistryResourceProvider" can be used to bridge conformance resources from the tenant data store (uploaded through the REST API) to the registry.
When enabled, this means that each call to the registry (e.g. for extension StructureDefinition lookups during resource creation) can result in a round trip to the database.

For optimal performance, the IBM FHIR Server team recommends to disable this resource provider via the following setting:

| Configuration | Recommended value |
|------|-------|
| `fhirServer/core/serverRegistryResourceProviderEnabled` | false |

This configuration setting avoids an extra Search during ingestion.

## 3.7. Usage of the extension-search-parameters.json file

The IBM FHIR Server supports multi-tenant SearchParameter extensions described in the extension-search-parameters.json file. When the `extension-search-parameters.json` is missing, the SearchParameter value extraction tries to open the file for every resource. This is a file-system operation which results in a context switch and impacts performance.

The IBM FHIR Server team recommends each tenant include an `extension-search-parameters.json` file, even if it is empty.

An example of the empty search parameters file is: 

``` json
{
  "resourceType": "Bundle",
  "type": "collection",
  "entry": []
}
```

# 4. Database Tuning

| Tuneable | Guidance |
| -------- | -------- |
| Caching  | Avoiding physical reads is important for most database applications and the IBM FHIR Server is no different. Memory sizing and configuration is important for good ingestion performance as well as good read performance. |
| Statistics | Ensure statistics are up-to-date to allow the query optimizer to generate the best execution plans. |
| Concurrency | Ensure the database supports the required number of connections from the application server cluster, plus any administration overhead. Connections and their associated sessions consume memory which must be considered in the overall database server memory budget. |


## 4.1. PostgreSQL

For PostgreSQL, we recommend tuning the following properties:

| Property | Recommendation | Description |
| -------- | -------------- | ----------- |
| max_connections | N+15 | N is the aggregate number of connections from the IBM FHIR Server cluster, defined by the connection manager's `maxPoolSize` property. Check the PostgreSQL documentation. Connections require memory, so be careful with large values which could lead to memory pressure on the database server causing performance issues or stability problems. |
| shared_buffers | 1/2 of memory | The number of 8kB blocks used for caching table data. This is important for ingestion as well as query performance. The database must be able to find free blocks to hold table and index data it needs to modify when ingesting new data. |
| effective_cache_size | 3/4 of memory | Number of 8kB blocks. Not an allocation, just provides guidance to the query optimizer for how much data it can expect to be cached by the database and operating system file system cache. It is used to bias decisions on choosing index-based access paths. IBM FHIR Server queries rely heavily on index-driven plans so this value should be at the upper end of any recommended range. |

The recommended values should be considered a starting point. Monitor database metrics and tune appropriately for your given workload. See the [PostgreSQL wiki](https://wiki.postgresql.org/wiki/Tuning_Your_PostgreSQL_Server) for additional guidance.

FHIR search queries are translated into SQL expressions. When several search parameters are included in a request the resulting join spans many tables, several of which may be wrapped in views. In order to give the PostgreSQL query optimizer sufficient freedom to optimize the query, include the `searchOptimizerOptions` parameter map to the datasource configuration as shown below to increase the values for `from_collapse_limit` and `join_collapse_limit`. The default for both of these is 8. When there are more than 8 tables involved in the search query, the optimizer may not generate an efficient execution plan unless the limits are increased:

```
        "persistence": {
            "datasources": {
                "default": {
                    "type": "postgresql",
                    "currentSchema": "fhirdata",
                    "searchOptimizerOptions": {
                        "from_collapse_limit": 16,
                        "join_collapse_limit": 16
                    },
                    ...
```

See the [PostgreSQL Query Planning](https://www.postgresql.org/docs/12/runtime-config-query.html) guide for more information.

### 4.1.1. Fillfactor

In PostgreSQL, the default `fillfactor` for each table is 100 - no room is reserved for updates. This maximizes storage utilization, but impacts performance for updates which occur when new versions of a resource are ingested. Update statements are also used frequently during the reindex process, if index IDs are not specified.

To provide space for updates, all the `<resourceType>_logical_resources` should be configured with a `fillfactor` of 80 as a starting point. DBAs may specify their own `fillfactor` values based on their own knowledge and understanding of the system.

The `fillfactor` for the `logical_resources` table may benefit from an even lower value to support the heavy update load during a reindex operation, if index IDs are not specified. This is a special case due to the fact that every row in the table is updated once.

To change the fillfactor for existing data, a `VACUUM FULL` operation is required:

``` sql
ALTER TABLE fhirdata.logical_resources SET (fillfactor=70);
...
VACUUM FULL fhirdata.logical_resources;
```

This should only be performed during a maintenance window when there is no load on the system.

### 4.1.2. Tuning Auto-vacuum

When running reindex operations (after a search parameter configuration change, for example) without specifying index IDs, the `logical_resources` table undergoes frequent updates to an indexed column. Due to the nature of how PostgreSQL handles updates, this results in a significant amount of old index blocks which slows progress. The table storage parameters may need to be tuned to vacuum the `logical_resources` table more aggressively. To address this, tune the storage parameters for this table as follows:

``` sql
-- Lower the trigger threshold for starting work
alter table fhirdata.logical_resources SET (autovacuum_vacuum_scale_factor = 0.01, autovacuum_vacuum_threshold=1000);

-- Increase the amount of work vacuuming completes before taking a breather (default is typically 200)
alter table fhirdata.logical_resources SET (autovacuum_vacuum_cost_limit=2000);
```

The default value for autovacuum_vacuum_cost_limit is likely too restrictive for a system with good IO performance. Increasing the value to 2000 increases the throttling threshold 10x, significantly improving throughput and helping the `logical_resources` vacuuming to be completed before it negatively impacts performance of the reindex operation (when index IDs are not specified on the reindex operation).

See the [PostgreSQL VACUUM documentation](https://www.postgresql.org/docs/12/sql-vacuum.html) for more details.

In addition, administrators may also choose to run a manual vacuum as shown in the following example:

``` sql
fhirdb=> VACUUM (ANALYZE,VERBOSE) fhirdata.logical_resources;
INFO:  vacuuming "fhirdata.logical_resources"
INFO:  scanned index "logical_resources_pk" to remove 16813312 row versions
DETAIL:  CPU: user: 31.70 s, system: 14.30 s, elapsed: 75.38 s

INFO:  scanned index "unq_logical_resources" to remove 16813312 row versions
DETAIL:  CPU: user: 70.03 s, system: 56.69 s, elapsed: 232.57 s
INFO:  scanned index "idx_logical_resources_rits" to remove 16813312 row versions
DETAIL:  CPU: user: 11.42 s, system: 14.00 s, elapsed: 45.40 s
INFO:  "logical_resources": removed 16813312 row versions in 355153 pages
DETAIL:  CPU: user: 27.36 s, system: 12.08 s, elapsed: 67.53 s
INFO:  index "logical_resources_pk" now contains 77504219 row versions in 544383 pages
DETAIL:  16806938 index row versions were removed.
0 index pages have been deleted, 0 are currently reusable.
CPU: user: 0.00 s, system: 0.00 s, elapsed: 0.00 s.
INFO:  index "unq_logical_resources" now contains 77504219 row versions in 2393179 pages
DETAIL:  16805676 index row versions were removed.
0 index pages have been deleted, 0 are currently reusable.
CPU: user: 0.00 s, system: 0.00 s, elapsed: 0.00 s.
INFO:  index "idx_logical_resources_rits" now contains 77504219 row versions in 775877 pages
DETAIL:  16813298 index row versions were removed.
507613 index pages have been deleted, 445589 are currently reusable.
CPU: user: 0.00 s, system: 0.00 s, elapsed: 0.00 s.
INFO:  "logical_resources": found 7765149 removable, 16804352 nonremovable row versions in 549284 out of 4210218 pages
DETAIL:  0 dead row versions cannot be removed yet, oldest xmin: 320896669
There were 21857963 unused item identifiers.
Skipped 0 pages due to buffer pins, 3070111 frozen pages.
46 pages are entirely empty.
CPU: user: 206.67 s, system: 113.27 s, elapsed: 537.14 s.
INFO:  analyzing "fhirdata.logical_resources"
INFO:  "logical_resources": scanned 30000 of 4210218 pages, containing 551774 live rows and 0 dead rows; 30000 rows in sample, 77436294 estimated total rows
VACUUM
```

### 4.1.3. Transaction Id Wraparound

Be ware of multixact wraparound issues, as highlighted by the following warning when running a manual vacuum:

```fhirdb=> VACUUM (VERBOSE) fhirdata.logical_resources;
WARNING:  oldest multixact is far in the past
HINT:  Close open transactions with multixacts soon to avoid wraparound problems.
```

This indicates that the automatic vacuum process needs to be more aggressive. See [here](https://info.crunchydata.com/blog/managing-transaction-id-wraparound-in-postgresql) for details.


### 4.1.4. Vacuum Monitoring

Use the following query to see the impact of updates and deletes on the IBM FHIR Server tables (assuming the tenant is configured to use the `fhirdata` schema):

``` sql
 SELECT relname,
        n_tup_ins AS "inserts",
        n_tup_upd AS "updates",
        n_tup_del AS "deletes",
        n_live_tup AS "live_tuples",
        n_dead_tup AS "dead_tuples"
   FROM pg_stat_user_tables
  WHERE schemaname = 'fhirdata'
    AND (relname = 'logical_resources' OR relname LIKE '%_values')
    AND n_dead_tup > 0;
```

The values reported are since the database was last restarted. To check uptime, run the following query:

``` sql
 SELECT current_timestamp - pg_postmaster_start_time();
    ?column?     
-----------------
 05:45:41.835965
```


The following query can be used to see how many auto-vacuum jobs are currently in progress and for which tables:

``` sql
fhirdb=> SELECT r.relname, v.*
           FROM pg_stat_progress_vacuum v,
                pg_stat_user_tables r
          WHERE r.relid = v.relid;

          relname          |  pid   | datid | datname | relid |       phase       | heap_blks_total | heap_blks_scanned | heap_blks_vacuumed | index_vacuum_count | max_dead_tuples | num_dead_tuples
---------------------------+--------+-------+---------+-------+-------------------+-----------------+-------------------+--------------------+--------------------+-----------------+-----------------
 imagingstudy_token_values | 451383 | 16478 | fhirdb  | 45603 | vacuuming indexes |          332918 |            332918 |                  0 |                  0 |        96879138 |            5102
 condition_token_values    | 451793 | 16478 | fhirdb  | 52193 | scanning heap     |           53883 |             18173 |                  0 |                  0 |        15679953 |          301285
 logical_resources         | 451180 | 16478 | fhirdb  | 16533 | vacuuming indexes |         1432256 |           1432256 |                  0 |                  0 |       178956970 |          765313
```

By default, only 3 vacuum jobs can run concurrently.

### 4.1.5. Max Locks

To drop an IBM FHIR Server schema in PostgreSQL, set the following configuration in `postgresql.conf`:

```
max_locks_per_transaction = 128		# min 10
```

This change requires a database restart.

## 4.2. IBM Db2

TBD.


## 4.3. Derby

Derby is not recommended for production use and therefore tuning Derby will not be addressed in this guide.

# 5. Ingestion Scenarios

## 5.1. Logical Id Generation

Using random values for resource identifiers can cause performance issues in large databases. This is a particular issue when using PostgreSQL with the IBM FHIR Server due to an issue known as write amplification from full page writes. For details, see this blog post: https://www.2ndquadrant.com/en/blog/on-the-impact-of-full-page-writes.

For best performance, ids generated by clients should not be purely random but instead be structured to include a prefix which increments over time. This causes index entries for new values to share pages (right-hand inserts), greatly reducing the write amplification overhead.

One example of a suitable id generation strategy can be found in the [IBM FHIR Server fhir-persistence-jdbc project](https://github.com/IBM/FHIR/blob/main/fhir-persistence-jdbc/src/main/java/com/ibm/fhir/persistence/jdbc/util/TimestampPrefixedUUID.java).

This strategy provides both the desirable trait of global uniqueness as well as a low write amplification overhead thanks to the time-based prefix.

The IBM FHIR Server also uses normalization to avoid storing (and indexing) long identifier strings in multiple places. This saves space, and the database-generated identity values are based on sequences which naturally produce the desired right-hand-insert behavior.

## 5.2. Conditional Update

In scenarios where the server is not the source of truth, clients may want to reload/refresh the server with all of their data on some periodic basis.

One technique for this is to use client-assigned resource ids and perform an HTTP PUT (update or create-on-update) with the content on each ingestion run. However, this can lead to unnecessarily updating each FHIR resource on each ingestion run.

Avoiding these unnecessary updates is important for two reasons:
1. ingestion performance (each update performs work in the database)
2. database size (each version of each resource is stored in the database)

The HL7 FHIR specification includes experimental support for both [conditional create](https://www.hl7.org/fhir/R4/http.html#ccreate) and [conditional update](https://www.hl7.org/fhir/R4/http.html#cond-update) and the IBM FHIR server implements each of these. However, this approach suffers multiple issues:
1. each update must perform a search which can be more costly than simply performing read before the update
2. conditional requests require intricate locking techniques to avoid race conditions and the currently-implemented approach has [significant limitations](https://github.com/IBM/FHIR/issues/2051)

Instead, IBM FHIR Server version 4.7.1 introduces support for a server-enabled optimization to avoid performing unnecessary updates. When users pass the `X-FHIR-UPDATE-IF-MODIFIED` header with a value of `true`, the server will perform a comparison of the resource contents from the update with the contents of the resource in the database.

Two resources will be considered equivalent based on the following criteria:
* whitespace between the resource elements (both XML and JSON) is ignored
* the server-assigned fields (`Resource.meta.lastUpdated` and `Resource.meta.versionId`) are ignored
* the value of all other fields in the resource must be equivalent

When the update is skipped, the response will contain a Location header that points to the *existing* resource version (e.g. `[base]/Patient/1234/_history/1`) instead of a newly created instance of this resource (`[base]/Patient/1234/_history/2`) and the response body will be sent according to the client's [return preference](https://www.hl7.org/fhir/R4/http.html#ops).
If the client indicates a return preference of OperationOutcome and the update is skipped on the server, the response will contain an informational issue to indicate this case.

# 6. Client Access Scenarios

The IBM FHIR Server translates a FHIR search request into a SQL query. The database performs query optimization to generate what it thinks is the most efficient execution plan before running the query. This optimization depends on the database having good statistics (and a clever algorithm) to make the right choice. When this goes wrong, the result is a slow response which can also end up consuming significant resources which impact the capacity of the system as a whole.

The FHIR search specification includes a rich set of capabilities designed to make it easier for clients to find data. If a particular search performs poorly, there are likely other ways the same data can be fetched. One solution is to use multiple requests, using FHIR bundle requests to request multiple resources in one server request.

There are many ways to retrieve data:

* READ: read the latest version of a resource using its logical identifier. Fast.
* VREAD: reads a specific version of a resource using its logical identifier and version. Fast.
* SEARCH: id - fetch one resource matching the given logical id:
* SEARCH: identity - fetch all resources matching the given identity. Usually one but could be multiple
* SEARCH: last-modified - Find resources modified since a given date.
* SEARCH: multiple attributes - find resources matching the search condition. Can be slow depending on the complexity of the resulting database query and the relative cardinality (row counts) of the resource search parameters.
* SEARCH: include - fetch additional resources based on the relationships found in the resources returned by the main search criteria.
* SEARCH - has
* SEARCH - revinclude

There may also be some subtle semantic differences among searches which might appear to be equivalent. This is particularly true for compartment-based queries due to the complex definition of compartment membership defined in the FHIR specification.

## 6.1. Read

Logical id-based read requests are the fastest way to access a resource, for example:

```
    <base>/Patient/17546b5cd4a-cae29bb0-d6d7-4a1a-a8df-505e2e3a975e
```

This translates into a single query which utilizes indexes to quickly locate the required record:

``` sql
SELECT R.RESOURCE_ID, R.LOGICAL_RESOURCE_ID, R.VERSION_ID,
       R.LAST_UPDATED, R.IS_DELETED, R.DATA, LR.LOGICAL_ID
  FROM Patient_RESOURCES R,
       Patient_LOGICAL_RESOURCES LR
 WHERE LR.LOGICAL_ID = ?
   AND R.RESOURCE_ID = LR.CURRENT_RESOURCE_ID
```

When the data is currently cached by the database, this query takes less than a millisecond to execute, as shown in the following execution plan analysis from PostgreSQL:

```
Nested Loop  (cost=0.84..10.88 rows=1 width=1335) (actual time=0.046..0.058 rows=1 loops=1)
  Buffers: shared hit=8
  ->  Index Scan using idx_patient_logical_resourceslogical_id on patient_logical_resources lr  (cost=0.42..5.44 rows=1 width=53) (actual time=0.026..0.030 rows=1 loops=1)
        Index Cond: ((logical_id)::text = '17546b5cd4a-cae29bb0-d6d7-4a1a-a8df-505e2e3a975e'::text)
        Buffers: shared hit=4
  ->  Index Scan using patient_resources_prf_in1 on patient_resources r  (cost=0.42..5.44 rows=1 width=1290) (actual time=0.011..0.012 rows=1 loops=1)
        Index Cond: (resource_id = lr.current_resource_id)
        Buffers: shared hit=4
Planning Time: 0.313 ms
Execution Time: 0.127 ms
```

## 6.2. Version Read

The FHIR specification supports reading a specific version of a resource:

```
Patient/17546b5cd4a-cae29bb0-d6d7-4a1a-a8df-505e2e3a975e/_history/1
```

The resulting query is similar, except in this case, the specified version is requested from the PATIENT_RESOURCES table. Performance is similar to the plain read, depending on what data is currently cached, of course:

```
SELECT R.RESOURCE_ID, R.LOGICAL_RESOURCE_ID, R.VERSION_ID,
       R.LAST_UPDATED, R.IS_DELETED, R.DATA, LR.LOGICAL_ID
  FROM Patient_RESOURCES R, Patient_LOGICAL_RESOURCES LR
 WHERE LR.LOGICAL_ID = ?
   AND R.LOGICAL_RESOURCE_ID = LR.LOGICAL_RESOURCE_ID
   AND R.VERSION_ID = ?
```

## 6.3. History

The history query returns all versions of a resource. Because there is no limit to the number of versions for a given resource, the results are ordered by the version_id (resource version number) and paginated using OFFSET and FETCH NEXT ROWS clauses:

```
  SELECT R.RESOURCE_ID, R.LOGICAL_RESOURCE_ID, R.VERSION_ID,
         R.LAST_UPDATED, R.IS_DELETED, R.DATA, LR.LOGICAL_ID
    FROM Patient_RESOURCES R, Patient_LOGICAL_RESOURCES LR
   WHERE LR.LOGICAL_ID = ?
     AND R.LOGICAL_RESOURCE_ID = LR.LOGICAL_RESOURCE_ID
ORDER BY R.VERSION_ID DESC
  OFFSET ? ROWS
  FETCH NEXT ? ROWS ONLY
```

In order to include an overall count of the number of resource versions, the IBM FHIR Server must execute an additional SQL query:

```
SELECT COUNT(R.VERSION_ID)
  FROM Patient_RESOURCES R,
       Patient_LOGICAL_RESOURCES LR
 WHERE LR.LOGICAL_ID = ?
   AND R.LOGICAL_RESOURCE_ID = LR.LOGICAL_RESOURCE_ID;
```

In most cases the history queries will execute very quickly. Performance will be slower for cases where a single resource has thousands of versions. To avoid this, ingestion pipelines must ensure they only update a version when necessary.

## 6.4. Search Performance

**Omitting the count**

For search queries with low specificity, the response time is dominated by the "count query" that is used to determine how many total results match the query.
The IBM FHIR Server supports skipping this step when clients set a query parameter named `_total` to the value of `none` as described at https://www.hl7.org/fhir/search.html#total.

**Resource subsetting**

For search queries that return lots of data (e.g. ones that return large resources such as Patient resources that embed a profile picture), the response time can be dominated by the network between the client and the server. By default, a FHIR search response bundle will contain the entire contents of each resource being returned. However, it is possible to request the server to return a subset of each resource through either the [`_summary`](https://www.hl7.org/fhir/search.html#summary) or [`_elements`](https://www.hl7.org/fhir/search.html#elements) parameters.

**Predicate Order**

The IBM FHIR Server translates FHIR search queries into SQL statements which may require many tables to be joined. The database attempts to optimize the query execution plan by analyzing join conditions, filter predicates, available indexes and column statistics. The optimizer also attempts to order the joins in order to reduce the amount of work it must do. This usually involves computing the most selective clauses first. When there are many tables involved, the database optimizer may not always find the most efficient execution plan which can result in higher response times or `500` server errors if the total time exceeds the transaction timeout limit. For example, on a large database the following query may perform poorly if there are many ExplanationOfBenefit records with a Claim matching one of the given priorities:

```
/ExplanationOfBenefit?_pretty=true&claim.priority=normal,stat,deferred&_include=ExplanationOfBenefit:claim&_include=ExplanationOfBenefit:patient&patient:Patient.birthdate=le1915
```

The above query requires a join of around 13 tables which is too many for the database to try all possible orders and so the most efficient plan is never tried. The IBM FHIR Server builds the SQL based on the order of filter predicates in the search request. This can be used along with knowledge of the data to place the most selective filter first which, in this case, is the patient `birthdate` range. Rewriting the query as follows can significantly improve the response time:

```
/ExplanationOfBenefit?_pretty=true&patient:Patient.birthdate=le1915&claim.priority=normal,stat,deferred&_include=ExplanationOfBenefit:claim&_include=ExplanationOfBenefit:patient
```

## 6.5. Search Examples

The section contains search examples and performance considerations for various types of search parameters.

**STRING SEARCH**
Because the default behavior for string search has case-insensitive "begins-with" semantics, we encourage users to use the `:exact` modifier when possible for the best performance.

**TOKEN SEARCH**
HL7 FHIR supports a few variants of token search:
* `[parameter]=[code]`
* `[parameter]=[system]|[code]`
* `[parameter]=|[code]`

Token-based searches should include a code-system when possible. The same code value might exist in multiple code-systems and so, unless the code-system is included in the search query, the database join may need to consider multiple matches in order to find all the associated resources. This multiplies the amount of work the database must do to execute the query. This also impacts cardinality estimation by the optimizer. If both the code-system and code value are provided, this matches a unique index in the schema allowing the optimizer to infer the SQL fragment will produce a single row.

For optimal performance, users should prefer the `[system]|[code]` variant. Explicitly providing the code is always preferred. If no system is provided, in some cases the IBM FHIR Server can determine the correct code-system to use automatically, which helps query performance.

Don't do this:
`Patient/175517d8bea-32d33eec-d98f-4c99-a3cf-06a113ddcf08/CareTeam?status=active`

Instead, do this: `Patient/175517d8bea-32d33eec-d98f-4c99-a3cf-06a113ddcf08/CareTeam?status=http://hl7.org/fhir/care-team-status|active`

This is especially important for code values that are common across systems (e.g short strings like "active").
However, the IBM FHIR Server supports a SearchParameter extension which allows the server to add an implicit `[system]|` prefix for certain token parameter searches that come in with just a `[code]`.

For example, when a search parameter targets an element of type Code that has a required binding, there is typically a single implicit system for the code. In such cases, the IBM FHIR Server models these Code subtypes as Enums and the values are indexed with their implicit system. When the corresponding SearchParameter definition is decorated with the same implicit system (via this extension), it provides maximum specificity for the query to efficiently retrieve the token value.

For all such search parameters in the base spec, and first-class implementation guides that we package, we have taken the liberty to add these extensions a priori. For example, for `SearchParameter-Account-status`, the following extension tells the server to process a query like `Account?status=active` as if it were specified like `Account?status=http://hl7.org/fhir/audit-event-outcome|active`:
```
{
    "url": "http://ibm.com/fhir/extension/implicit-system",
    "valueUri": "http://hl7.org/fhir/audit-event-outcome"
}
```

**COMPARTMENT SEARCHES**

Search for all ExplanationOfBenefit records which reference the patient according to its logical id `17546b5cd4a-cae29bb0-d6d7-4a1a-a8df-505e2e3a975e`.

```
    <base>/ExplanationOfBenefit?patient=17546b5cd4a-cae29bb0-d6d7-4a1a-a8df-505e2e3a975e
```


Search the patient compartment `17546b5cd4a-cae29bb0-d6d7-4a1a-a8df-505e2e3a975e` for all matching ExplanationOfBenefit resources.

```
    <base>/Patient/17546b5cd4a-cae29bb0-d6d7-4a1a-a8df-505e2e3a975e/ExplanationOfBenefit
```

**COMPARTMENT CHAINED SEARCH**

Requests any ExplanationOfBenefit resources belonging to the compartment for patient `17546b5cd4a-cae29bb0-d6d7-4a1a-a8df-505e2e3a975e` with a Claim created on a given day: 2015-10-16.

```
    <base>/Patient/17546b5cd4a-cae29bb0-d6d7-4a1a-a8df-505e2e3a975e/ExplanationOfBenefit?claim:Claim.created=2015-10-16
```

**CLIENT SEARCH BUNDLES**

If the response time is not acceptable, an alternative strategy is to first fetch all the ExplanationOfBenefit resources associated with the given patient, then compose a second bundle request for the Claim records associated with the ExplanationOfBenefit resources returned by the first search. The initial request can fetch the Patient resource along with the ExplanationOfBenefit resources for that patient.

```
{
    "entry": [
        {
            "request": {
                "method": "GET",
                "url": "Patient/17546b5cd4a-cae29bb0-d6d7-4a1a-a8df-505e2e3a975e"
            }
        },
        {
            "request": {
                "method": "GET",
                "url": "ExplanationOfBenefit?patient=17546b5cd4a-cae29bb0-d6d7-4a1a-a8df-505e2e3a975e"
            }
        }
    ],
    "resourceType": "Bundle",
    "type": "transaction"
}
```

Note the bundle type is specified as `transaction`. Because all the entries in the bundle are reads, there's no modification to the database and so no semantic difference between using `transaction` or `batch`. However, for `transaction`, the IBM FHIR Server initiates a single transaction for the entire request. In high load/concurrency conditions, this improves throughput.

The number of ExplanationOfBenefit resources for a given patient is likely to be relatively small. By iterating over the response to the previous request, the client can package a number of resource reads into one or more bundle requests. Read requests are the most efficient type of request for accessing data from the IBM FHIR Server.

```
{
    "entry": [
        {
            "request": {
                "method": "GET",
                "url": "Claim/1747d7abed3-7f331c1b-5262-41b7-894a-09d63ddc1791"
            }
        },
        {
            "request": {
                "method": "GET",
                "url": "Practitioner/1747d7abe89-61ad12fd-61ba-4c06-9fbf-89a130babd27"
            }
        },
        {
            "request": {
                "method": "GET",
                "url": "Claim/1747d7abf2d-bbfd621f-31ec-4044-88df-a5d14efba26a"
            }
        },
        ...
        {
            "request": {
                "method": "GET",
                "url": "Practitioner/1747d7ac6b2-90d24ffc-b773-45c0-8194-a24057087c82"
            }
        }
    ],
    "resourceType": "Bundle",
    "type": "transaction"
}
```

Although this approach involves multiple requests to the IBM FHIR Server, the overall response time may be quicker due to the simplicity of the requests.

The client can filter the required Claim resources, or alternatively convert the reads to search requests:


```
{
    "entry": [
        {
            "request": {
                "method": "GET",
                "url": "Claim?_id=1747d7abed3-7f331c1b-5262-41b7-894a-09d63ddc1791&created=2015-10-16"
            }
        },
        {
            "request": {
                "method": "GET",
                "url": "Practitioner/1747d7abe89-61ad12fd-61ba-4c06-9fbf-89a130babd27"
            }
        },
        {
            "request": {
                "method": "GET",
                "url": "Claim?_id=1747d7abf2d-bbfd621f-31ec-4044-88df-a5d14efba26a&created=2015-10-16"
            }
        },
        ...
        {
            "request": {
                "method": "GET",
                "url": "Practitioner/1747d7ac6b2-90d24ffc-b773-45c0-8194-a24057087c82"
            }
        }
    ],
    "resourceType": "Bundle",
    "type": "transaction"
}
```

**INCLUDE**

Another alternative which tends to perform well uses the `_include` function. The `_include` function is useful because it avoids the need for additional round-trips (the server performs the iteration internally).

This example retrieves ExplanationOfBenefit resources for patient `17478598887-c1279929-f2ca-48e5-815a-b812bfc2e756` which were created between 2010 and 2020. The result bundle also includes the Patient, Provider, CareTeam and Coverage resources referenced by each matching ExplanationOfBenefit resource.

```
    <base>/ExplanationOfBenefit?patient=Patient/17478598887-c1279929-f2ca-48e5-815a-b812bfc2e756&created=ge2010&created=lt2021&_include=ExplanationOfBenefit:patient&_include=ExplanationOfBenefit:provider&_include=ExplanationOfBenefit:care-team&_include=ExplanationOfBenefit:coverage
```


**REVINCLUDE**

Used when a client wants a particular resource and other resources pointing to it. For example, to retrieve a Patient resource and any Observations referring to that patient as the subject:

```
    <base>/Patient?_id=17478598887-c1279929-f2ca-48e5-815a-b812bfc2e756&_revinclude=Observation.subject
```

Find all Observations matching the composite code-value-quantity value and for each such Observation, also return any DiagnosticReport referring to that Observation as the result:

```
    <base>/Observation?code-value-quantity=http://loinc.org|2339-0$69.1&_revinclude=DiagnosticReport:result
```

The above query is likely to be expensive if the number of matching Observations is high. For better performance, consider constraining the search with additional predicates like the patient id.


**LAST UPDATED**

Searching for all resources updated on a single day is expensive:

```
    <base>?_lastUpdated=2020-10-31
```

If possible, limit the search to a specific resource type. For example:

```
    <base>/Patient?_lastUpdated=2020-10-31
```

If multiple resource types are of interest, consider packaging multiple search requests into a bundle:

```
{
    "entry": [
        {
            "request": {
                "method": "GET",
                "url": "Patient?_lastUpdated=2020-10-31"
            }
        },
        {
            "request": {
                "method": "GET",
                "url": "Claim?_lastUpdated=2020-10-31"
            }
        },
        {
            "request": {
                "method": "GET",
                "url": "Practitioner?_lastUpdated=2020-10-31"
            }
        },
        ...
        {
            "request": {
                "method": "GET",
                "url": "ExplanationOfBenefit?_lastUpdated=2020-10-31"
            }
        }
    ],
    "resourceType": "Bundle",
    "type": "transaction"
}
```

## 6.6. Tools

## 6.7. Making FHIR Requests With curl

```
curl -k -i \
-H 'Content-Type: application/json' \
-H 'X-FHIR-TENANT-ID: default' \
-u 'fhiruser:change-password' 'https://localhost:9443/fhir-server/api/v4/Practitioner/1749321b933-9fd77253-7fd4-47db-8ff8-9e4ccb21441d'
```

Note that the tenant header must match the tenant header defined in the fhir-server-config.json. The default tenant header is `X-FHIR-TENANT-ID`. If this header is not provided, the tenant value will be `default`.

The curl command can also be used to make POST calls to the IBM FHIR Server. This example creates a new Observation resource for the patient subject `abc123`:

```
curl -k -i \
-H 'Content-Type: application/json' \
-H 'X-FHIR-TENANT-ID: default' \
-u 'fhiruser:change-password' 'https://localhost:9443/fhir-server/api/v4/Observation' -d '
{
    "resourceType": "Observation",
    "subject": {
        "reference": "Patient/abc123"
    },
    "status": "final",
    "code": {
        "coding": [{
            "system": "http://snomed.info/sct",
            "code": "363779003",
            "display": "Genotype determination"
        }],
        "text": "Diplotype Call"
    },
"text": {
    "status": "generated",
    "div": "__redacted for brevity__"
  }
}
'
```

Examples of valid resources can be found in the [fhir-examples](https://github.com/IBM/FHIR/tree/main/fhir-examples) project.



## 6.8. Making FHIR Requests with the IBM FHIR Server Client

See FHIR client API in the [IBM FHIR Server User's Guide](https://ibm.github.io/FHIR/guides/FHIRServerUsersGuide).
