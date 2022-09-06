---
layout: post
title:  IBM FHIR Server 4.5.0 - Performance 
description: Performance
date:   2020-10-29 01:00:00 -0400
permalink: /performance/performance450/
---

# IBM FHIR Server 4.5.0 - Performance

## Workloads

1. Ingestion
2. Client Access

### Ingestion Workload

The *fhir-bucket* utility scans an IBM Cloud Object Store (COS) bucket for matching **json** or **ndjson** files. Matching files are recorded in the fhirbucket database and this information is subsequently used to drive the loading of those files into the LinuxForHealth FHIR Server.

The test described here uses bundles based on Synthea - Synthetic Mass data. The original bundles have been preprocessed and broken into smaller bundles of no more than 100 resources. Resources within original bundles contain references to other resources within the same bundle. Traditionally, these references would be resolved by the LinuxForHealth FHIR Server on ingestion. However, because the bundles are being broken into smaller units of work, the references must be resolved in advance. This requires that any create requests using server-asserted ids must be rewritten as client asserted id requests, using PUT.

The *fhir-bucket* uses a large thread-pool to fetch the bundles from COS and POST each to the LinuxForHealth FHIR Server. The result bundle is parsed and each of the returned logical resource ids are stored in the fhirbucket database, along with some timing metrics.

### Client Access

The client access workload is generated using fhir-bucket running within the same K8s cluster. Random patients are selected from the `fhirbucket.logical_resources` table which is populated by first running the fhir-bucket ingestion mode. The client workload is comprised of the following steps:

1. Select a random patient id and submit to the thread-pool;
2. Create a Bundle resource containing a read for the patient resource and a search for ExplanationOfBenefit resources which refer to that patient **limit 20**;
3. POST the bundle request;
4. Process the response, constructing a new Bundle resource containing GET requests for each Claim and Provider referenced by the ExplanationOfBenefit resources contained in the first response.
5. Collect statistics on the number of calls, number of resources returned and the time taken for each request.

The request bundles are configured to use a type of TRANSACTION. This provides the best performance as the LinuxForHealth FHIR Server processes each request within the bundle using a single transaction, reducing the commit load on the database.


## System Configuration

|Configuration Item | Value|
|------------------ | -----|
|IBM FHIR Server | Release 4.5.0|
|Client thread-pool size | 300|
|FHIR Server Deployment Size | 6 PODS|
|PostgreSQL Service | IBM Cloud Database|
|PostgreSQL Version | 12.4|
|PostgreSQL CPU | 9 cores **dedicated**|
|PostgreSQL RAM | 24GB|
|PostgreSQL Storage | 1TB **up to 10K IOPS**|


## Top Resource Counts

Top resource counts in the database using the query: 
```
  SELECT rt.resource_type, count(*)
    FROM fhirdata.logical_resources lr,
         fhirdata.resource_types rt
   WHERE rt.resource_type_id = lr.resource_type_id
GROUP BY rt.resource_type
ORDER BY 2 DESC;
```

| resource_type                  |  count    |
| ------------------ | --------------------- |
|Observation                       | 28643640|
|Claim                             |  9809811|
|Procedure                         |  9533768|
|Encounter                         |  8788209|
|ExplanationOfBenefit              |  7795369|
|DiagnosticReport                  |  2942204|
|MedicationRequest                 |  2761835|
|Condition                         |  1755335|
|Immunization                      |  1656535|
|CarePlan                          |  1320978|
|Goal                              |   347835|
|CareTeam                          |   341226|
|Practitioner                      |   319783|
|Organization                      |   319729|
|AllergyIntolerance                |   216350|
|Patient                           |   174388|

The full list is included at the end of this report.

## Ingestion Performance

To compute the ingestion rate we can query the `fhirbucket.logical_resources` table, looking for the first and last creation times for the latest run of the loader, `loader_instance_id = 212` in the example below:

```
select count(*), max(created_tstamp) - min(created_tstamp) as elapsed 
  from fhirbucket.logical_resources lr, fhirbucket.resource_bundle_loads bl 
 where bl.resource_bundle_load_id = lr.resource_bundle_load_id
   and bl.loader_instance_id = 212;
```

| count  |     elapsed      |
|------- | -----------------|
| 525525 | 00:09:19.253087  |


Ingestion rate: 

```
525525 resources 
---------------- = 940 resources/second
   559 seconds
```

The inserts into the Observation parameter tables for token_values and quantity_values come from composite values, and there is room for improvement for how these inserts are handled:

![Application Breakdown](https://linuxforhealth.github.io/FHIR/images/performance-450/Ingest_Application_Breakdown.png)

The GC activity is very reasonable given the complexity of FHIR resource parsing and validation:

![GC CPU Time](https://linuxforhealth.github.io/FHIR/images/performance-450/Ingest_GC_CPU_Time.png)

The throughput is stable throughout the loading period:

![Throughput](https://linuxforhealth.github.io/FHIR/images/performance-450/Ingest_Throughput.png)

The top database operations reflect what is seen in the application breakdown, with observation_token_values and observation_quantity_values being a good target for future improvement:

![Top DB Operations](https://linuxforhealth.github.io/FHIR/images/performance-450/Ingest_Top_DB_Operations.png)

Time series view of the same data:

![TOP DB Operations Timeseries](https://linuxforhealth.github.io/FHIR/images/performance-450/Ingest_Top_DB_Ops_Timeseries.png)

## Client Read Performance

The average server response time reported by the client is consistently under 180ms as shown below (statistics are sampled and printed by *fhir-bucket* every 10 seconds:

```
STATS: FHIR= 1643.8 calls/s, rate=16825.1 resources/s, response time=0.179 s
STATS: FHIR= 1655.6 calls/s, rate=16860.8 resources/s, response time=0.178 s
STATS: FHIR= 1693.2 calls/s, rate=17323.5 resources/s, response time=0.176 s
STATS: FHIR= 1699.1 calls/s, rate=17431.5 resources/s, response time=0.174 s
STATS: FHIR= 1647.1 calls/s, rate=16849.2 resources/s, response time=0.179 s
STATS: FHIR= 1676.6 calls/s, rate=17180.6 resources/s, response time=0.176 s
STATS: FHIR= 1691.3 calls/s, rate=17242.2 resources/s, response time=0.175 s
STATS: FHIR= 1663.7 calls/s, rate=17043.8 resources/s, response time=0.178 s
STATS: FHIR= 1639.9 calls/s, rate=16723.9 resources/s, response time=0.175 s
STATS: FHIR= 1688.2 calls/s, rate=17329.2 resources/s, response time=0.175 s
STATS: FHIR= 1686.3 calls/s, rate=17232.8 resources/s, response time=0.174 s
STATS: FHIR= 1709.0 calls/s, rate=17542.6 resources/s, response time=0.173 s
STATS: FHIR= 1710.6 calls/s, rate=17441.1 resources/s, response time=0.173 s
STATS: FHIR= 1696.9 calls/s, rate=17409.0 resources/s, response time=0.174 s
STATS: FHIR= 1694.8 calls/s, rate=17330.2 resources/s, response time=0.173 s
STATS: FHIR= 1706.7 calls/s, rate=17415.4 resources/s, response time=0.173 s
STATS: FHIR= 1674.9 calls/s, rate=17205.6 resources/s, response time=0.177 s
```

Throughput is constrained by exhaustion of PostgreSQL CPU.

## Client Workload Performance Metrics

Database processing rate is slightly more than 3000 transactions per second. This reached 15K transactions/second when using BATCH as the bundle type instead of TRANSACTION. In that case, although the TPS is higher, the overall throughput is lower because each request required multiple transactions.

![DB Transactions Per Second](https://linuxforhealth.github.io/FHIR/images/performance-450/Client_DB_Transactions_Per_Second.png)

![Web Transaction Time](https://linuxforhealth.github.io/FHIR/images/performance-450/Client_Web_Transaction_Time.png)

The peak aggregate CPU usage for the POD (including the fhirbucket instance driving the test workload) is around 36 cores.

![FHIR POD CPU Usage](https://linuxforhealth.github.io/FHIR/images/performance-450/Client_FHIR_CPU_Usage.png)

No significant hotspots in the application breakdown:

![Application Breakdown](https://linuxforhealth.github.io/FHIR/images/performance-450/Client_Application_Breakdown.png)

GC times are remarkably good, especially given the complexity of processing FHIR resources.

![GC CPU Time](https://linuxforhealth.github.io/FHIR/images/performance-450/Client_GC_CPU_Time.png)

The top DB operations align with the above Application Breakdown.

![Top DB Operations](https://linuxforhealth.github.io/FHIR/images/performance-450/Client_Top_DB_Operations.png)

![Top DB Ops Timeseries](https://linuxforhealth.github.io/FHIR/images/performance-450/Client_Top_DB_Ops_Timeseries.png)

The database CPU is hitting its limit, suggesting that scaling up to more cores would increase throughput.

![DB CPU Usage](https://linuxforhealth.github.io/FHIR/images/performance-450/Client_DB_CPU_Usage.png)

##  Postgres Database Configuration

Name | Category | Setting | Unit | Description
---- | -------- | ------- | ---- | -----------
DateStyle | Client Connection Defaults / Locale and Formatting | ISO, MDY |  | Sets the display format for date and time values.
IntervalStyle | Client Connection Defaults / Locale and Formatting | postgres |  | Sets the display format for interval values.
TimeZone | Client Connection Defaults / Locale and Formatting | UTC |  | Sets the time zone for displaying and interpreting time stamps.
allow_system_table_mods | Developer Options | off |  | Allows modifications of the structure of system tables.
application_name | Reporting and Logging / What to Log | pgAdmin 4 - DB:fhirdb |  | Sets the application name to be reported in statistics and logs.
archive_cleanup_command | Write-Ahead Log / Archive Recovery |  |  | Sets the shell command that will be executed at every restart point.
archive_command | Write-Ahead Log / Archiving | /usr/bin/pgbackrest --config /conf/postgresql/pgbackrest.conf --stanza=formation archive-push %p |  | Sets the shell command that will be called to archive a WAL file.
archive_mode | Write-Ahead Log / Archiving | on |  | Allows archiving of WAL files using archive_command.
archive_timeout | Write-Ahead Log / Archiving | 1800 | s | Forces a switch to the next WAL file if a new file has not been started within N seconds.
array_nulls | Version and Platform Compatibility / Previous PostgreSQL Versions | on |  | Enable input of NULL elements in arrays.
authentication_timeout | Connections and Authentication / Authentication | 30 | s | Sets the maximum allowed time to complete client authentication.
autovacuum | Autovacuum | on |  | Starts the autovacuum subprocess.
autovacuum_analyze_scale_factor | Autovacuum | 0.1 |  | Number of tuple inserts, updates, or deletes prior to analyze as a fraction of reltuples.
autovacuum_analyze_threshold | Autovacuum | 50 |  | Minimum number of tuple inserts, updates, or deletes prior to analyze.
autovacuum_freeze_max_age | Autovacuum | 200000000 |  | Age at which to autovacuum a table to prevent transaction ID wraparound.
autovacuum_max_workers | Autovacuum | 3 |  | Sets the maximum number of simultaneously running autovacuum worker processes.
autovacuum_multixact_freeze_max_age | Autovacuum | 400000000 |  | Multixact age at which to autovacuum a table to prevent multixact wraparound.
autovacuum_naptime | Autovacuum | 60 | s | Time to sleep between autovacuum runs.
autovacuum_vacuum_cost_delay | Autovacuum | 20 | ms | Vacuum cost delay in milliseconds, for autovacuum.
autovacuum_vacuum_cost_limit | Autovacuum | -1 |  | Vacuum cost amount available before napping, for autovacuum.
autovacuum_vacuum_scale_factor | Autovacuum | 0.2 |  | Number of tuple updates or deletes prior to vacuum as a fraction of reltuples.
autovacuum_vacuum_threshold | Autovacuum | 50 |  | Minimum number of tuple updates or deletes prior to vacuum.
autovacuum_work_mem | Resource Usage / Memory | -1 | kB | Sets the maximum memory to be used by each autovacuum worker process.
backend_flush_after | Resource Usage / Asynchronous Behavior | 0 | 8kB | Number of pages after which previously performed writes are flushed to disk.
backslash_quote | Version and Platform Compatibility / Previous PostgreSQL Versions | safe_encoding |  | Sets whether "\'" is allowed in string literals.
bgwriter_delay | Resource Usage / Background Writer | 200 | ms | Background writer sleep time between rounds.
bgwriter_flush_after | Resource Usage / Background Writer | 64 | 8kB | Number of pages after which previously performed writes are flushed to disk.
bgwriter_lru_maxpages | Resource Usage / Background Writer | 100 |  | Background writer maximum number of LRU pages to flush per round.
bgwriter_lru_multiplier | Resource Usage / Background Writer | 2 |  | Multiple of the average buffer usage to free per round.
block_size | Preset Options | 8192 |  | Shows the size of a disk block.
bonjour | Connections and Authentication / Connection Settings | off |  | Enables advertising the server via Bonjour.
bonjour_name | Connections and Authentication / Connection Settings |  |  | Sets the Bonjour service name.
bytea_output | Client Connection Defaults / Statement Behavior | hex |  | Sets the output format for bytea.
check_function_bodies | Client Connection Defaults / Statement Behavior | on |  | Check function bodies during CREATE FUNCTION.
checkpoint_completion_target | Write-Ahead Log / Checkpoints | 0.7 |  | Time spent flushing dirty buffers during checkpoint, as fraction of checkpoint interval.
checkpoint_flush_after | Write-Ahead Log / Checkpoints | 32 | 8kB | Number of pages after which previously performed writes are flushed to disk.
checkpoint_timeout | Write-Ahead Log / Checkpoints | 300 | s | Sets the maximum time between automatic WAL checkpoints.
checkpoint_warning | Write-Ahead Log / Checkpoints | 30 | s | Enables warnings if checkpoint segments are filled more frequently than this.
client_encoding | Client Connection Defaults / Locale and Formatting | UNICODE |  | Sets the client's character set encoding.
client_min_messages | Client Connection Defaults / Statement Behavior | notice |  | Sets the message levels that are sent to the client.
cluster_name | Process Title | postgresql |  | Sets the name of the cluster, which is included in the process title.
commit_delay | Write-Ahead Log / Settings | 0 |  | Sets the delay in microseconds between transaction commit and flushing WAL to disk.
commit_siblings | Write-Ahead Log / Settings | 5 |  | Sets the minimum concurrent open transactions before performing commit_delay.
constraint_exclusion | Query Tuning / Other Planner Options | partition |  | Enables the planner to use constraints to optimize queries.
cpu_index_tuple_cost | Query Tuning / Planner Cost Constants | 0.005 |  | Sets the planner's estimate of the cost of processing each index entry during an index scan.
cpu_operator_cost | Query Tuning / Planner Cost Constants | 0.0025 |  | Sets the planner's estimate of the cost of processing each operator or function call.
cpu_tuple_cost | Query Tuning / Planner Cost Constants | 0.01 |  | Sets the planner's estimate of the cost of processing each tuple (row).
cursor_tuple_fraction | Query Tuning / Other Planner Options | 0.1 |  | Sets the planner's estimate of the fraction of a cursor's rows that will be retrieved.
data_checksums | Preset Options | on |  | Shows whether data checksums are turned on for this cluster.
data_directory_mode | Preset Options | 0700 |  | Mode of the data directory.
data_sync_retry | Error Handling | off |  | Whether to continue running after a failure to sync data files.
db_user_namespace | Connections and Authentication / Authentication | off |  | Enables per-database user names.
deadlock_timeout | Lock Management | 10000 | ms | Sets the time to wait on a lock before checking for deadlock.
debug_assertions | Preset Options | off |  | Shows whether the running server has assertion checks enabled.
debug_pretty_print | Reporting and Logging / What to Log | on |  | Indents parse and plan tree displays.
debug_print_parse | Reporting and Logging / What to Log | off |  | Logs each query's parse tree.
debug_print_plan | Reporting and Logging / What to Log | off |  | Logs each query's execution plan.
debug_print_rewritten | Reporting and Logging / What to Log | off |  | Logs each query's rewritten parse tree.
default_statistics_target | Query Tuning / Other Planner Options | 100 |  | Sets the default statistics target.
default_table_access_method | Client Connection Defaults / Statement Behavior | heap |  | Sets the default table access method for new tables.
default_tablespace | Client Connection Defaults / Statement Behavior |  |  | Sets the default tablespace to create tables and indexes in.
default_text_search_config | Client Connection Defaults / Locale and Formatting | pg_catalog.english |  | Sets default text search configuration.
default_transaction_deferrable | Client Connection Defaults / Statement Behavior | off |  | Sets the default deferrable status of new transactions.
default_transaction_isolation | Client Connection Defaults / Statement Behavior | read committed |  | Sets the transaction isolation level of each new transaction.
default_transaction_read_only | Client Connection Defaults / Statement Behavior | off |  | Sets the default read-only status of new transactions.
dynamic_shared_memory_type | Resource Usage / Memory | posix |  | Selects the dynamic shared memory implementation used.
effective_cache_size | Query Tuning / Planner Cost Constants | 2359296 | 8kB | Sets the planner's assumption about the total size of the data caches.
effective_io_concurrency | Resource Usage / Asynchronous Behavior | 12 |  | Number of simultaneous requests that can be handled efficiently by the disk subsystem.
enable_bitmapscan | Query Tuning / Planner Method Configuration | on |  | Enables the planner's use of bitmap-scan plans.
enable_gathermerge | Query Tuning / Planner Method Configuration | on |  | Enables the planner's use of gather merge plans.
enable_hashagg | Query Tuning / Planner Method Configuration | on |  | Enables the planner's use of hashed aggregation plans.
enable_hashjoin | Query Tuning / Planner Method Configuration | on |  | Enables the planner's use of hash join plans.
enable_indexonlyscan | Query Tuning / Planner Method Configuration | on |  | Enables the planner's use of index-only-scan plans.
enable_indexscan | Query Tuning / Planner Method Configuration | on |  | Enables the planner's use of index-scan plans.
enable_material | Query Tuning / Planner Method Configuration | on |  | Enables the planner's use of materialization.
enable_mergejoin | Query Tuning / Planner Method Configuration | on |  | Enables the planner's use of merge join plans.
enable_nestloop | Query Tuning / Planner Method Configuration | on |  | Enables the planner's use of nested-loop join plans.
enable_parallel_append | Query Tuning / Planner Method Configuration | on |  | Enables the planner's use of parallel append plans.
enable_parallel_hash | Query Tuning / Planner Method Configuration | on |  | Enables the planner's use of parallel hash plans.
enable_partition_pruning | Query Tuning / Planner Method Configuration | on |  | Enables plan-time and run-time partition pruning.
enable_partitionwise_aggregate | Query Tuning / Planner Method Configuration | off |  | Enables partitionwise aggregation and grouping.
enable_partitionwise_join | Query Tuning / Planner Method Configuration | off |  | Enables partitionwise join.
enable_seqscan | Query Tuning / Planner Method Configuration | on |  | Enables the planner's use of sequential-scan plans.
enable_sort | Query Tuning / Planner Method Configuration | on |  | Enables the planner's use of explicit sort steps.
enable_tidscan | Query Tuning / Planner Method Configuration | on |  | Enables the planner's use of TID scan plans.
escape_string_warning | Version and Platform Compatibility / Previous PostgreSQL Versions | on |  | Warn about backslash escapes in ordinary string literals.
event_source | Reporting and Logging / Where to Log | PostgreSQL |  | Sets the application name used to identify PostgreSQL messages in the event log.
exit_on_error | Error Handling | off |  | Terminate session on any error.
extra_float_digits | Client Connection Defaults / Locale and Formatting | 1 |  | Sets the number of digits displayed for floating-point values.
force_parallel_mode | Query Tuning / Other Planner Options | off |  | Forces use of parallel query facilities.
from_collapse_limit | Query Tuning / Other Planner Options | 8 |  | Sets the FROM-list size beyond which subqueries are not collapsed.
fsync | Write-Ahead Log / Settings | on |  | Forces synchronization of updates to disk.
full_page_writes | Write-Ahead Log / Settings | on |  | Writes full pages to WAL when first modified after a checkpoint.
geqo | Query Tuning / Genetic Query Optimizer | on |  | Enables genetic query optimization.
geqo_effort | Query Tuning / Genetic Query Optimizer | 5 |  | GEQO: effort is used to set the default for other GEQO parameters.
geqo_generations | Query Tuning / Genetic Query Optimizer | 0 |  | GEQO: number of iterations of the algorithm.
geqo_pool_size | Query Tuning / Genetic Query Optimizer | 0 |  | GEQO: number of individuals in the population.
geqo_seed | Query Tuning / Genetic Query Optimizer | 0 |  | GEQO: seed for random path selection.
geqo_selection_bias | Query Tuning / Genetic Query Optimizer | 2 |  | GEQO: selective pressure within the population.
geqo_threshold | Query Tuning / Genetic Query Optimizer | 12 |  | Sets the threshold of FROM items beyond which GEQO is used.
gin_fuzzy_search_limit | Client Connection Defaults / Other Defaults | 0 |  | Sets the maximum allowed result for exact search by GIN.
gin_pending_list_limit | Client Connection Defaults / Statement Behavior | 4096 | kB | Sets the maximum size of the pending list for GIN index.
hot_standby | Replication / Standby Servers | on |  | Allows connections and queries during recovery.
hot_standby_feedback | Replication / Standby Servers | off |  | Allows feedback from a hot standby to the primary that will avoid query conflicts.
huge_pages | Resource Usage / Memory | off |  | Use of huge pages on Linux or Windows.
idle_in_transaction_session_timeout | Client Connection Defaults / Statement Behavior | 0 | ms | Sets the maximum allowed duration of any idling transaction.
ignore_checksum_failure | Developer Options | off |  | Continues processing after a checksum failure.
ignore_system_indexes | Developer Options | off |  | Disables reading from system indexes.
integer_datetimes | Preset Options | on |  | Datetimes are integer based.
jit | Query Tuning / Other Planner Options | on |  | Allow JIT compilation.
jit_above_cost | Query Tuning / Planner Cost Constants | 100000 |  | Perform JIT compilation if query is more expensive.
jit_debugging_support | Developer Options | off |  | Register JIT compiled function with debugger.
jit_dump_bitcode | Developer Options | off |  | Write out LLVM bitcode to facilitate JIT debugging.
jit_expressions | Developer Options | on |  | Allow JIT compilation of expressions.
jit_inline_above_cost | Query Tuning / Planner Cost Constants | 500000 |  | Perform JIT inlining if query is more expensive.
jit_optimize_above_cost | Query Tuning / Planner Cost Constants | 500000 |  | Optimize JITed functions if query is more expensive.
jit_profiling_support | Developer Options | off |  | Register JIT compiled function with perf profiler.
jit_tuple_deforming | Developer Options | on |  | Allow JIT compilation of tuple deforming.
join_collapse_limit | Query Tuning / Other Planner Options | 8 |  | Sets the FROM-list size beyond which JOIN constructs are not flattened.
krb_caseins_users | Connections and Authentication / Authentication | off |  | Sets whether Kerberos and GSSAPI user names should be treated as case-insensitive.
lc_collate | Client Connection Defaults / Locale and Formatting | C.UTF-8 |  | Shows the collation order locale.
lc_ctype | Client Connection Defaults / Locale and Formatting | C.UTF-8 |  | Shows the character classification and case conversion locale.
lc_messages | Client Connection Defaults / Locale and Formatting | en_US.UTF-8 |  | Sets the language in which messages are displayed.
lc_monetary | Client Connection Defaults / Locale and Formatting | en_US.UTF-8 |  | Sets the locale for formatting monetary amounts.
lc_numeric | Client Connection Defaults / Locale and Formatting | en_US.UTF-8 |  | Sets the locale for formatting numbers.
lc_time | Client Connection Defaults / Locale and Formatting | en_US.UTF-8 |  | Sets the locale for formatting date and time values.
listen_addresses | Connections and Authentication / Connection Settings | 0.0.0.0 |  | Sets the host name or IP address(es) to listen to.
lo_compat_privileges | Version and Platform Compatibility / Previous PostgreSQL Versions | off |  | Enables backward compatibility mode for privilege checks on large objects.
local_preload_libraries | Client Connection Defaults / Shared Library Preloading | pgextwlist |  | Lists unprivileged shared libraries to preload into each backend.
lock_timeout | Client Connection Defaults / Statement Behavior | 0 | ms | Sets the maximum allowed duration of any wait for a lock.
log_autovacuum_min_duration | Reporting and Logging / What to Log | 1000 | ms | Sets the minimum execution time above which autovacuum actions will be logged.
log_checkpoints | Reporting and Logging / What to Log | on |  | Logs each checkpoint.
log_connections | Reporting and Logging / What to Log | off |  | Logs each successful connection.
log_destination | Reporting and Logging / Where to Log | stderr |  | Sets the destination for server log output.
log_disconnections | Reporting and Logging / What to Log | off |  | Logs end of a session, including duration.
log_duration | Reporting and Logging / What to Log | off |  | Logs the duration of each completed SQL statement.
log_error_verbosity | Reporting and Logging / What to Log | default |  | Sets the verbosity of logged messages.
log_executor_stats | Statistics / Monitoring | off |  | Writes executor performance statistics to the server log.
log_file_mode | Reporting and Logging / Where to Log | 0600 |  | Sets the file permissions for log files.
log_hostname | Reporting and Logging / What to Log | off |  | Logs the host name in the connection logs.
log_line_prefix | Reporting and Logging / What to Log | %t [%a] [%e] [%p]: [%l-1] user=%u,db=%d,client=%h |  | Controls information prefixed to each log line.
log_lock_waits | Reporting and Logging / What to Log | on |  | Logs long lock waits.
log_min_duration_statement | Reporting and Logging / When to Log | 100 | ms | Sets the minimum execution time above which statements will be logged.
log_min_error_statement | Reporting and Logging / When to Log | error |  | Causes all statements generating error at or above this level to be logged.
log_min_messages | Reporting and Logging / When to Log | notice |  | Sets the message levels that are logged.
log_parser_stats | Statistics / Monitoring | off |  | Writes parser performance statistics to the server log.
log_planner_stats | Statistics / Monitoring | off |  | Writes planner performance statistics to the server log.
log_replication_commands | Reporting and Logging / What to Log | off |  | Logs each replication command.
log_rotation_age | Reporting and Logging / Where to Log | 1440 | min | Automatic log file rotation will occur after N minutes.
log_rotation_size | Reporting and Logging / Where to Log | 10240 | kB | Automatic log file rotation will occur after N kilobytes.
log_statement | Reporting and Logging / What to Log | none |  | Sets the type of statements logged.
log_statement_stats | Statistics / Monitoring | off |  | Writes cumulative performance statistics to the server log.
log_temp_files | Reporting and Logging / What to Log | 0 | kB | Log the use of temporary files larger than this number of kilobytes.
log_timezone | Reporting and Logging / What to Log | UTC |  | Sets the time zone to use in log messages.
log_transaction_sample_rate | Reporting and Logging / When to Log | 0 |  | Set the fraction of transactions to log for new transactions.
log_truncate_on_rotation | Reporting and Logging / Where to Log | off |  | Truncate existing log files of same name during log rotation.
logging_collector | Reporting and Logging / Where to Log | off |  | Start a subprocess to capture stderr output and/or csvlogs into log files.
maintenance_work_mem | Resource Usage / Memory | 1572864 | kB | Sets the maximum memory to be used for maintenance operations.
max_connections | Connections and Authentication / Connection Settings | 400 |  | Sets the maximum number of concurrent connections.
max_files_per_process | Resource Usage / Kernel Resources | 1000 |  | Sets the maximum number of simultaneously open files for each server process.
max_function_args | Preset Options | 100 |  | Shows the maximum number of function arguments.
max_identifier_length | Preset Options | 63 |  | Shows the maximum identifier length.
max_index_keys | Preset Options | 32 |  | Shows the maximum number of index keys.
max_locks_per_transaction | Lock Management | 64 |  | Sets the maximum number of locks per transaction.
max_logical_replication_workers | Replication / Subscribers | 4 |  | Maximum number of logical replication worker processes.
max_parallel_maintenance_workers | Resource Usage / Asynchronous Behavior | 2 |  | Sets the maximum number of parallel processes per maintenance operation.
max_parallel_workers | Resource Usage / Asynchronous Behavior | 8 |  | Sets the maximum number of parallel workers that can be active at one time.
max_parallel_workers_per_gather | Resource Usage / Asynchronous Behavior | 2 |  | Sets the maximum number of parallel processes per executor node.
max_pred_locks_per_page | Lock Management | 2 |  | Sets the maximum number of predicate-locked tuples per page.
max_pred_locks_per_relation | Lock Management | -2 |  | Sets the maximum number of predicate-locked pages and tuples per relation.
max_pred_locks_per_transaction | Lock Management | 64 |  | Sets the maximum number of predicate locks per transaction.
max_prepared_transactions | Resource Usage / Memory | 150 |  | Sets the maximum number of simultaneously prepared transactions.
max_replication_slots | Replication / Sending Servers | 10 |  | Sets the maximum number of simultaneously defined replication slots.
max_stack_depth | Resource Usage / Memory | 2048 | kB | Sets the maximum stack depth, in kilobytes.
max_standby_archive_delay | Replication / Standby Servers | 30000 | ms | Sets the maximum delay before canceling queries when a hot standby server is processing archived WAL data.
max_standby_streaming_delay | Replication / Standby Servers | 30000 | ms | Sets the maximum delay before canceling queries when a hot standby server is processing streamed WAL data.
max_sync_workers_per_subscription | Replication / Subscribers | 2 |  | Maximum number of table synchronization workers per subscription.
max_wal_senders | Replication / Sending Servers | 12 |  | Sets the maximum number of simultaneously running WAL sender processes.
max_wal_size | Write-Ahead Log / Checkpoints | 1024 | MB | Sets the WAL size that triggers a checkpoint.
max_worker_processes | Resource Usage / Asynchronous Behavior | 8 |  | Maximum number of concurrent worker processes.
min_parallel_index_scan_size | Query Tuning / Planner Cost Constants | 64 | 8kB | Sets the minimum amount of index data for a parallel scan.
min_parallel_table_scan_size | Query Tuning / Planner Cost Constants | 1024 | 8kB | Sets the minimum amount of table data for a parallel scan.
min_wal_size | Write-Ahead Log / Checkpoints | 80 | MB | Sets the minimum size to shrink the WAL to.
old_snapshot_threshold | Resource Usage / Asynchronous Behavior | -1 | min | Time before a snapshot is too old to read pages changed after the snapshot was taken.
operator_precedence_warning | Version and Platform Compatibility / Previous PostgreSQL Versions | off |  | Emit a warning for constructs that changed meaning since PostgreSQL 9.4.
parallel_leader_participation | Resource Usage / Asynchronous Behavior | on |  | Controls whether Gather and Gather Merge also run subplans.
parallel_setup_cost | Query Tuning / Planner Cost Constants | 1000 |  | Sets the planner's estimate of the cost of starting up worker processes for parallel query.
parallel_tuple_cost | Query Tuning / Planner Cost Constants | 0.1 |  | Sets the planner's estimate of the cost of passing each tuple (row) from worker to master backend.
password_encryption | Connections and Authentication / Authentication | md5 |  | Chooses the algorithm for encrypting passwords.
pg_stat_statements.max | Customized Options | 5000 |  | Sets the maximum number of statements tracked by pg_stat_statements.
pg_stat_statements.save | Customized Options | on |  | Save pg_stat_statements statistics across server shutdowns.
pg_stat_statements.track | Customized Options | top |  | Selects which statements are tracked by pg_stat_statements.
pg_stat_statements.track_utility | Customized Options | on |  | Selects whether utility commands are tracked by pg_stat_statements.
pgaudit.log | Customized Options | none |  | Specifies which classes of statements will be logged by session audit logging. Multiple classes can be provided using a comma-separated list and classes can be subtracted by prefacing the class with a - sign.
pgaudit.log_catalog | Customized Options | off |  | Specifies that session logging should be enabled in the case where all relations in a statement are in pg_catalog. Disabling this setting will reduce noise in the log from tools like psql and PgAdmin that query the catalog heavily.
pgaudit.log_client | Customized Options | off |  | Specifies whether audit messages should be visible to the client. This setting should generally be left disabled but may be useful for debugging or other purposes.
pgaudit.log_level | Customized Options | log |  | Specifies the log level that will be used for log entries. This setting is used for regression testing and may also be useful to end users for testing or other purposes. It is not intended to be used in a production environment as it may leak which statements are being logged to the user.
pgaudit.log_parameter | Customized Options | off |  | Specifies that audit logging should include the parameters that were passed with the statement. When parameters are present they will be be included in CSV format after the statement text.
pgaudit.log_relation | Customized Options | off |  | Specifies whether session audit logging should create a separate log entry for each relation referenced in a SELECT or DML statement. This is a useful shortcut for exhaustive logging without using object audit logging.
pgaudit.log_statement_once | Customized Options | off |  | Specifies whether logging will include the statement text and parameters with the first log entry for a statement/substatement combination or with every entry. Disabling this setting will result in less verbose logging but may make it more difficult to determine the statement that generated a log entry, though the statement/substatement pair along with the process id should suffice to identify the statement text logged with a previous entry.
pgaudit.role | Customized Options |  |  | Specifies the master role to use for object audit logging. Muliple audit roles can be defined by granting them to the master role. This allows multiple groups to be in charge of different aspects of audit logging.
plan_cache_mode | Query Tuning / Other Planner Options | auto |  | Controls the planner's selection of custom or generic plan.
port | Connections and Authentication / Connection Settings | 5432 |  | Sets the TCP port the server listens on.
post_auth_delay | Developer Options | 0 | s | Waits N seconds on connection startup after authentication.
pre_auth_delay | Developer Options | 0 | s | Waits N seconds on connection startup before authentication.
primary_slot_name | Replication / Standby Servers |  |  | Sets the name of the replication slot to use on the sending server.
promote_trigger_file | Replication / Standby Servers |  |  | Specifies a file name whose presence ends recovery in the standby.
quote_all_identifiers | Version and Platform Compatibility / Previous PostgreSQL Versions | off |  | When generating SQL fragments, quote all identifiers.
random_page_cost | Query Tuning / Planner Cost Constants | 2.5 |  | Sets the planner's estimate of the cost of a nonsequentially fetched disk page.
recovery_end_command | Write-Ahead Log / Archive Recovery |  |  | Sets the shell command that will be executed once at the end of recovery.
recovery_min_apply_delay | Replication / Standby Servers | 0 | ms | Sets the minimum delay for applying changes during recovery.
recovery_target | Write-Ahead Log / Recovery Target |  |  | Set to "immediate" to end recovery as soon as a consistent state is reached.
recovery_target_action | Write-Ahead Log / Recovery Target | pause |  | Sets the action to perform upon reaching the recovery target.
recovery_target_inclusive | Write-Ahead Log / Recovery Target | on |  | Sets whether to include or exclude transaction with recovery target.
recovery_target_lsn | Write-Ahead Log / Recovery Target |  |  | Sets the LSN of the write-ahead log location up to which recovery will proceed.
recovery_target_name | Write-Ahead Log / Recovery Target |  |  | Sets the named restore point up to which recovery will proceed.
recovery_target_time | Write-Ahead Log / Recovery Target |  |  | Sets the time stamp up to which recovery will proceed.
recovery_target_timeline | Write-Ahead Log / Recovery Target | latest |  | Specifies the timeline to recover into.
recovery_target_xid | Write-Ahead Log / Recovery Target |  |  | Sets the transaction ID up to which recovery will proceed.
restart_after_crash | Error Handling | on |  | Reinitialize server after backend crash.
restore_command | Write-Ahead Log / Archive Recovery | /usr/bin/pgbackrest --config /conf/postgresql/pgbackrest.conf --stanza=formation archive-get %f "%p" |  | Sets the shell command that will retrieve an archived WAL file.
row_security | Client Connection Defaults / Statement Behavior | on |  | Enable row security.
search_path | Client Connection Defaults / Statement Behavior | "$user", public |  | Sets the schema search order for names that are not schema-qualified.
segment_size | Preset Options | 131072 | 8kB | Shows the number of pages per disk file.
seq_page_cost | Query Tuning / Planner Cost Constants | 1 |  | Sets the planner's estimate of the cost of a sequentially fetched disk page.
server_encoding | Client Connection Defaults / Locale and Formatting | UTF8 |  | Sets the server (database) character set encoding.
server_version | Preset Options | 12.4 |  | Shows the server version.
server_version_num | Preset Options | 120004 |  | Shows the server version as an integer.
session_replication_role | Client Connection Defaults / Statement Behavior | origin |  | Sets the session's behavior for triggers and rewrite rules.
shared_buffers | Resource Usage / Memory | 786432 | 8kB | Sets the number of shared memory buffers used by the server.
shared_memory_type | Resource Usage / Memory | mmap |  | Selects the shared memory implementation used for the main shared memory region.
ssl | Connections and Authentication / SSL | on |  | Enables SSL connections.
ssl_ca_file | Connections and Authentication / SSL |  |  | Location of the SSL certificate authority file.
ssl_cert_file | Connections and Authentication / SSL | /conf/cert/tls.crt |  | Location of the SSL server certificate file.
ssl_crl_file | Connections and Authentication / SSL |  |  | Location of the SSL certificate revocation list file.
ssl_key_file | Connections and Authentication / SSL | /conf/cert/tls.key |  | Location of the SSL server private key file.
ssl_library | Preset Options | OpenSSL |  | Name of the SSL library.
ssl_passphrase_command | Connections and Authentication / SSL |  |  | Command to obtain passphrases for SSL.
ssl_passphrase_command_supports_reload | Connections and Authentication / SSL | off |  | Also use ssl_passphrase_command during server reload.
ssl_prefer_server_ciphers | Connections and Authentication / SSL | on |  | Give priority to server ciphersuite order.
standard_conforming_strings | Version and Platform Compatibility / Previous PostgreSQL Versions | on |  | Causes '...' strings to treat backslashes literally.
statement_timeout | Client Connection Defaults / Statement Behavior | 43200000 | ms | Sets the maximum allowed duration of any statement.
superuser_reserved_connections | Connections and Authentication / Connection Settings | 15 |  | Sets the number of connection slots reserved for superusers.
synchronize_seqscans | Version and Platform Compatibility / Previous PostgreSQL Versions | on |  | Enable synchronized sequential scans.
synchronous_commit | Write-Ahead Log / Settings | local |  | Sets the current transaction's synchronization level.
synchronous_standby_names | Replication / Master Server | "c-a343dd6d-23fd-4229-aa12-1171a4fc8c13-m-0","c-a343dd6d-23fd-4229-aa12-1171a4fc8c13-m-1" |  | Number of synchronous standbys and list of names of potential synchronous ones.
syslog_facility | Reporting and Logging / Where to Log | local0 |  | Sets the syslog "facility" to be used when syslog enabled.
syslog_ident | Reporting and Logging / Where to Log | postgres |  | Sets the program name used to identify PostgreSQL messages in syslog.
syslog_sequence_numbers | Reporting and Logging / Where to Log | on |  | Add sequence number to syslog messages to avoid duplicate suppression.
syslog_split_messages | Reporting and Logging / Where to Log | on |  | Split messages sent to syslog by lines and to fit into 1024 bytes.
tcp_keepalives_count | Client Connection Defaults / Other Defaults | 6 |  | Maximum number of TCP keepalive retransmits.
tcp_keepalives_idle | Client Connection Defaults / Other Defaults | 300 | s | Time between issuing TCP keepalives.
tcp_keepalives_interval | Client Connection Defaults / Other Defaults | 30 | s | Time between TCP keepalive retransmits.
tcp_user_timeout | Client Connection Defaults / Other Defaults | 0 | ms | TCP user timeout.
temp_buffers | Resource Usage / Memory | 1024 | 8kB | Sets the maximum number of temporary buffers used by each session.
temp_file_limit | Resource Usage / Disk | -1 | kB | Limits the total size of all temporary files used by each process.
temp_tablespaces | Client Connection Defaults / Statement Behavior |  |  | Sets the tablespace(s) to use for temporary tables and sort files.
timezone_abbreviations | Client Connection Defaults / Locale and Formatting | Default |  | Selects a file of time zone abbreviations.
trace_notify | Developer Options | off |  | Generates debugging output for LISTEN and NOTIFY.
trace_recovery_messages | Developer Options | log |  | Enables logging of recovery-related debugging information.
trace_sort | Developer Options | off |  | Emit information about resource usage in sorting.
track_activities | Statistics / Query and Index Statistics Collector | on |  | Collects information about executing commands.
track_activity_query_size | Resource Usage / Memory | 1024 | B | Sets the size reserved for pg_stat_activity.query, in bytes.
track_commit_timestamp | Replication | on |  | Collects transaction commit time.
track_counts | Statistics / Query and Index Statistics Collector | on |  | Collects statistics on database activity.
track_functions | Statistics / Query and Index Statistics Collector | none |  | Collects function-level statistics on database activity.
track_io_timing | Statistics / Query and Index Statistics Collector | on |  | Collects timing statistics for database I/O activity.
transaction_deferrable | Client Connection Defaults / Statement Behavior | off |  | Whether to defer a read-only serializable transaction until it can be executed with no possible serialization failures.
transaction_isolation | Client Connection Defaults / Statement Behavior | read committed |  | Sets the current transaction's isolation level.
transaction_read_only | Client Connection Defaults / Statement Behavior | off |  | Sets the current transaction's read-only status.
transform_null_equals | Version and Platform Compatibility / Other Platforms and Clients | off |  | Treats "expr=NULL" as "expr IS NULL".
unix_socket_group | Connections and Authentication / Connection Settings |  |  | Sets the owning group of the Unix-domain socket.
unix_socket_permissions | Connections and Authentication / Connection Settings | 0777 |  | Sets the access permissions of the Unix-domain socket.
update_process_title | Process Title | on |  | Updates the process title to show the active SQL command.
vacuum_cleanup_index_scale_factor | Client Connection Defaults / Statement Behavior | 0.1 |  | Number of tuple inserts prior to index cleanup as a fraction of reltuples.
vacuum_cost_delay | Resource Usage / Cost-Based Vacuum Delay | 0 | ms | Vacuum cost delay in milliseconds.
vacuum_cost_limit | Resource Usage / Cost-Based Vacuum Delay | 200 |  | Vacuum cost amount available before napping.
vacuum_cost_page_dirty | Resource Usage / Cost-Based Vacuum Delay | 20 |  | Vacuum cost for a page dirtied by vacuum.
vacuum_cost_page_hit | Resource Usage / Cost-Based Vacuum Delay | 1 |  | Vacuum cost for a page found in the buffer cache.
vacuum_cost_page_miss | Resource Usage / Cost-Based Vacuum Delay | 10 |  | Vacuum cost for a page not found in the buffer cache.
vacuum_defer_cleanup_age | Replication / Master Server | 0 |  | Number of transactions by which VACUUM and HOT cleanup should be deferred, if any.
vacuum_freeze_min_age | Client Connection Defaults / Statement Behavior | 50000000 |  | Minimum age at which VACUUM should freeze a table row.
vacuum_freeze_table_age | Client Connection Defaults / Statement Behavior | 150000000 |  | Age at which VACUUM should scan whole table to freeze tuples.
vacuum_multixact_freeze_min_age | Client Connection Defaults / Statement Behavior | 5000000 |  | Minimum age at which VACUUM should freeze a MultiXactId in a table row.
vacuum_multixact_freeze_table_age | Client Connection Defaults / Statement Behavior | 150000000 |  | Multixact age at which VACUUM should scan whole table to freeze tuples.
wal_block_size | Preset Options | 8192 |  | Shows the block size in the write ahead log.
wal_buffers | Write-Ahead Log / Settings | 2048 | 8kB | Sets the number of disk-page buffers in shared memory for WAL.
wal_compression | Write-Ahead Log / Settings | on |  | Compresses full-page writes written in WAL file.
wal_consistency_checking | Developer Options |  |  | Sets the WAL resource managers for which WAL consistency checks are done.
wal_init_zero | Write-Ahead Log / Settings | on |  | Writes zeroes to new WAL files before first use.
wal_keep_segments | Replication / Sending Servers | 16 |  | Sets the number of WAL files held for standby servers.
wal_level | Write-Ahead Log / Settings | replica |  | Set the level of information written to the WAL.
wal_log_hints | Write-Ahead Log / Settings | on |  | Writes full pages to WAL when first modified after a checkpoint, even for a non-critical modifications.
wal_receiver_status_interval | Replication / Standby Servers | 10 | s | Sets the maximum interval between WAL receiver status reports to the sending server.
wal_receiver_timeout | Replication / Standby Servers | 60000 | ms | Sets the maximum wait time to receive data from the sending server.
wal_recycle | Write-Ahead Log / Settings | on |  | Recycles WAL files by renaming them.
wal_retrieve_retry_interval | Replication / Standby Servers | 5000 | ms | Sets the time to wait before retrying to retrieve WAL after a failed attempt.
wal_segment_size | Preset Options | 16777216 | B | Shows the size of write ahead log segments.
wal_sender_timeout | Replication / Sending Servers | 60000 | ms | Sets the maximum time to wait for WAL replication.
wal_sync_method | Write-Ahead Log / Settings | fdatasync |  | Selects the method used for forcing WAL updates to disk.
wal_writer_delay | Write-Ahead Log / Settings | 200 | ms | Time between WAL flushes performed in the WAL writer.
wal_writer_flush_after | Write-Ahead Log / Settings | 128 | 8kB | Amount of WAL written out by WAL writer that triggers a flush.
work_mem | Resource Usage / Memory | 15728 | kB | Sets the maximum memory to be used for query workspaces.
xmlbinary | Client Connection Defaults / Statement Behavior | base64 |  | Sets how binary values are to be encoded in XML.
xmloption | Client Connection Defaults / Statement Behavior | content |  | Sets whether XML data in implicit parsing and serialization operations is to be considered as documents or content fragments.
zero_damaged_pages | Developer Options | off |  | Continues processing past damaged page headers.


## Complete List of Resource Counts

Resource counts prior to running the ingestion load:

resource_type                  |  count
------------------ | ----------------------- |
Observation                       | 28643640
Claim                             |  9809811
Procedure                         |  9533768
Encounter                         |  8788209
ExplanationOfBenefit              |  7795369
DiagnosticReport                  |  2942204
MedicationRequest                 |  2761835
Condition                         |  1755335
Immunization                      |  1656535
CarePlan                          |  1320978
Goal                              |   347835
CareTeam                          |   341226
Practitioner                      |   319783
Organization                      |   319729
AllergyIntolerance                |   216350
Patient                           |   174388
ImagingStudy                      |   131088
MedicationAdministration          |    58620
Device                            |     5444
ValueSet                          |      688
CodeSystem                        |      533
Parameters                        |      196
StructureMap                      |      162
Task                              |      155
ConceptMap                        |       94
Questionnaire                     |       68
Contract                          |       66
QuestionnaireResponse             |       59
ActivityDefinition                |       44
Location                          |       43
MedicationDispense                |       40
Bundle                            |       39
PlanDefinition                    |       37
FamilyMemberHistory               |       32
Medication                        |       32
EvidenceVariable                  |       29
Library                           |       28
Coverage                          |       28
ServiceRequest                    |       28
Group                             |       27
MedicationKnowledge               |       26
RequestGroup                      |       25
ClaimResponse                     |       25
DeviceRequest                     |       21
Schedule                          |       21
Appointment                       |       21
SubstanceSpecification            |       18
Person                            |       17
Consent                           |       17
RelatedPerson                     |       17
List                              |       17
AuditEvent                        |       17
NutritionOrder                    |       17
CoverageEligibilityResponse       |       16
Slot                              |       16
GuidanceResponse                  |       16
Account                           |       15
Communication                     |       15
CommunicationRequest              |       15
DeviceUseStatement                |       15
Flag                              |       15
Measure                           |       15
ResearchElementDefinition         |       15
AdverseEvent                      |       14
ChargeItem                        |       14
MedicationStatement               |       14
HealthcareService                 |       14
PractitionerRole                  |       13
RiskAssessment                    |       13
AppointmentResponse               |       13
SupplyRequest                     |       13
Media                             |       13
DetectedIssue                     |       13
Provenance                        |       12
Specimen                          |       11
CompartmentDefinition             |       11
CoverageEligibilityRequest        |       11
Composition                       |       11
SupplyDelivery                    |       11
CapabilityStatement               |       11
ImmunizationEvaluation            |       10
DeviceDefinition                  |       10
OperationOutcome                  |       10
EventDefinition                   |       10
BiologicallyDerivedProduct        |       10
Invoice                           |       10
ClinicalImpression                |       10
MessageDefinition                 |       10
Substance                         |        9
MessageHeader                     |        9
MedicinalProduct                  |        9
MedicinalProductContraindication  |        8
Endpoint                          |        8
MedicinalProductAuthorization     |        8
MedicinalProductIndication        |        8
MedicinalProductInteraction       |        8
ResearchDefinition                |        8
SpecimenDefinition                |        8
SubstanceReferenceInformation     |        8
Basic                             |        8
ImmunizationRecommendation        |        7
Binary                            |        7
DocumentManifest                  |        7
ChargeItemDefinition              |        7
EnrollmentResponse                |        6
EpisodeOfCare                     |        6
GraphDefinition                   |        6
NamingSystem                      |        6
CatalogEntry                      |        6
DocumentReference                 |        6
ExampleScenario                   |        6
Evidence                          |        6
OrganizationAffiliation           |        6
EffectEvidenceSynthesis           |        6
EnrollmentRequest                 |        6
BodyStructure                     |        6
ObservationDefinition             |        5
PaymentReconciliation             |        5
MedicinalProductUndesirableEffect |        5
MedicinalProductPharmaceutical    |        5
MedicinalProductPackaged          |        5
MedicinalProductManufactured      |        5
MedicinalProductIngredient        |        5
VisionPrescription                |        5
ResearchStudy                     |        5
ResearchSubject                   |        5
RiskEvidenceSynthesis             |        5
Subscription                      |        5
PaymentNotice                     |        5
InsurancePlan                     |        4
VerificationResult                |        4
DeviceMetric                      |        4
SubstancePolymer                  |        3
SubstanceNucleicAcid              |        3
TerminologyCapabilities           |        3
SubstanceProtein                  |        3
SubstanceSourceMaterial           |        3
ImplementationGuide               |        3
MeasureReport                     |        2
