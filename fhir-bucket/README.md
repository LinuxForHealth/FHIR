# fhir-bucket

fhir-bucket is a multi-threaded standalone Java application to run load against the LinuxForHealth FHIR Server.

*Custom Load Types*

1. *Reindex* - Drive the $reindex custom operation using concurrent requests. The reindex operation is needed when the search parameter configuration is changed and the user wants to update the extracted parameter values which are used to support indexes.
- *Server* - Executes a Server-Side Client Reindex ($reindex)
- *Client* - Executes a Client-Side Reindex Operation ($reindex)

2. *Synthetic Data Loader* - Scans cloud object storage buckets and uploads data using the FHIR REST API

3. *Drive Load* - Make FHIR read/search calls at high volume to stress the read performance of the server.

## 1. Reindex - Driving the `$reindex` Custom Operation

When the LinuxForHealth FHIR Server stores a FHIR resource, it extracts a configurable set of searchable parameter values and stores them in specially indexed tables which are used to support search queries. When the search parameter configuration is changed (perhaps because a profile has been updated), users may want to apply this new configuration to resources already stored. By default, such configuration changes only apply to new resources.

the LinuxForHealth FHIR Server supports a custom operation to rebuild or "reindex" the search parameters extracted from resources currently stored. There are two approaches for driving the reindex, server-side-driven or client-side-driven. Using server-side-driven is the default; to use client-side-driven, include the `--reindex-client-side-driven` parameter.

With server-side-driven, the fhir-bucket will repeatedly call the `$reindex` operation. The user selects a date or timestamp as the reindex "marker", which is used to determine which resources have been reindexed, and which still need to be reindexed. When a resource is successfully reindexed, it is marked with this user-selected timestamp. Each reindex REST call will process up to the requested number of resources and return an OperationOutcome resource containing issues describing which resources were processed. When there are no resources left to update, the call returns an OperationOutcome with one issue, with an issue diagnostic value "Reindex complete", indicating that the reindex is complete.

With client-side-driven, the fhir-bucket will repeatedly call two operations in parallel; the `$retrieve-index` operation to determine the list of resources available to reindex, and the `$reindex` operation with a list of resources to reindex. Driving the reindex this way avoids database contention associated with updating the reindex timestamp of each resource with the reindex marker, which is used by the server-side-driven approach to keep track of the next resource to reindex.

To avoid read timeouts, the number of resources processed in a single reindex call can be limited. Reindex calls can be made in parallel to increase throughput. The best number for concurrent requests depends on the capabilities of the underlying platform and any desire to balance load with other users. Concurrency up to 200 threads have been tested. Monitor the LinuxForHealth FHIR Server response times when increasing concurrency. Also, make sure that the connection pool configured in the FHIR server cluster can support the required number of threads. This also means that the database needs to be configured to support this number of connections (sessions) plus any overhead.

The fhir-bucket main app has been extended to support driving a reindex operation with high concurrency. 

```
java \
  -Djava.util.logging.config.file=logging.properties \
  -jar "${JAR}" \
  --fhir-properties your-fhir-server.properties \
  --tenant-name your-tenant-name \
  --max-concurrent-fhir-requests 100 \
  --no-scan \
  --reindex-tstamp 2020-12-01T00:00:00Z \
  --reindex-resource-count 50 \
  --reindex-concurrent-requests 20 \
  --reindex-client-side-driven
```

The format of the reindex timestamp can be a date `YYYY-MM-DD` representing `00:00:00` UTC on the given day, or an ISO timestamp `YYYY-MM-DDThh:mm:ssZ`.

Values for `--reindex-resource-count` larger than 1000 will be clamped to 1000 to ensure that the `$reindex` server calls return within a reasonable time.

The value for `--reindex-concurrent-requests` can be increased/decreased to maximize throughput or avoid overloading a system. The number represents the total number of client threads used to invoke the $reindex operation. Each thread uses its own connection to the LinuxForHealth FHIR Server so you must also set `--max-concurrent-fhir-requests` to be at least equal to `--reindex-concurrent-requests`.

If the client-side-driven reindex is unable to be completed due to an error or timeout, the reindex can be resumed by using the `--reindex-start-with-index-id` parameter. If this needs to be done, first check the fhir-bucket log and find the first index ID that was not successful. Then, by specifying that index ID for the value of `--reindex-start-with-index-id` when starting the client-side-driven reindex, the reindex is resumed from that point, instead of starting completely over.

When reindexing, the server compares a hash of the parameter values extracted from the resource with the value of the parameter hash last stored in the database. If the values are equal, the reindex operation skips further processing of the resource which can save a significant amount of time in cases where a search parameter configuration change affects only a subset of resources or resource types. However, some schema upgrades may change the way search parameters are stored and indexed. In such a case, use the `--reindex-force` option. When given, this instructs the reindex operation to ignore the parameter hash comparison and instead always store the new parameters. For example:

```
java \
  -Djava.util.logging.config.file=logging.properties \
  -jar "${JAR}" \
  --fhir-properties your-fhir-server.properties \
  --tenant-name your-tenant-name \
  --max-concurrent-fhir-requests 100 \
  --no-scan \
  --reindex-tstamp 2020-12-01T00:00:00Z \
  --reindex-resource-count 50 \
  --reindex-concurrent-requests 20 \
  --reindex-client-side-driven \
  --reindex-force
```


| Property | Description |
|---|---|
| `--reindex-tstamp` | 'yyyy-MM-dd' or ISO 8601 dateTime which the reindex is using to start the operation|
| `--reindex-resource-count` | The count to index in a single request. e.g. 100 with a maximum value is 1000.|
| `--reindex-concurrent-requests` | The simultaneous requests used to execute concurrently |
| `--reindex-client-side-driven` | Switches between Client driven $reindex and Server side driven reindex. True or false, false by default. |
| `--reindex-start-with-index-id` | A index-id used with Client driven $reindex to drive reindexing from the Client side.|
| `--reindex-force` | Force the reindex operation to replace the parameters, even if the parameter hash matches.|
| `--fhir-properties` | Properties file for the LinuxForHealth FHIR Server |

### IBM FHIR Server Properties - The FHIR Properties

| Property | Type | Description |
|---|---|---|
| `fhir.server.host` |String| The hostname of the LinuxForHealth FHIR Server |
| `fhir.server.port` |Integer| The HTTPS port for the LinuxForHealth FHIR Server |
| `fhir.server.user` |String| The fhir user, e.g. fhiruser |
| `fhir.server.pass` |String| The fhir user's password e.g. change-password |
| `fhir.server.endpoint` |String| The path to the LinuxForHealth FHIR Server, typically `/fhir-server/api/v4` |
| `truststore` |String| The path to the truststore used to secure communications between the fhir-bucket Java application and the LinuxForHealth FHIR Server e.g. fhirClientTrustStore.p12|
| `truststore.pass` |String| The truststore password, e.g. `change-password` |
| `read.timeout` | Integer | The timeout for any HTTP Read in milliseconds|
| `connect.timeout` | Integer | The timeout for any HTTP Connection in milliseconds|
| `pool.connections.max` | Integer | The maximum pool size for the HTTP Connections, e.g. 400 |
| `disable.hostname.verification` | Boolean | Disables hostname checking when establishing an HTTP Connection. |

**Example**

```
fhir.server.host=localhost
fhir.server.port=9443
fhir.server.user=fhiruser
fhir.server.pass=change-password
fhir.server.endpoint=/fhir-server/api/v4
truststore=fhirClientTrustStore.p12
truststore.pass=change-password
read.timeout=125000
connect.timeout=20000
pool.connections.max=400
disable.hostname.verification=true
```

## 2. Synthetic Data Loader and 3. Drive Load

### Background

Synthea is a project for generating "synthetic" patient/population data for healthcare applications.
It generates realistic data based on census statistics and a lot of configuration.
It supports generating data in FHIR R4 

This "fhir-bucket" project will help you upload Synthea-generated data to a FHIR R4 server.

To facilitate high-volume load scenarios, multiple instances of the application can be run and their work coordinated so that each file is loaded exactly once.

The loader records the identities of the created resources. These identities can be used in other test and load generator applications to access the data.

### Steps

1. Follow the steps at https://github.com/synthetichealth/synthea to clone and install Synthea
2. Configure Synthea to generate FHIR R4 resources
3. Generate a bunch of patients
4. Clone this repo and set it up with Maven/Eclipse
5. Configure the truststore with root certs required to trust connections to your FHIR server, cloud object store and tracking database
5. Tweak fhir.properties to point at your target FHIR server
6. Tweak cos.properties to point at your COS bucket
7. Tweak db.properties to point at your tracking database
8. Execute Main.class as described in the Running section below


### Configuration

#### The cos.properties file

```
# the IBM COS API key or S3 access key.
cos.api.key=

# the IBM COS service instance id or S3 secret key.
cos.srvinstid=

# the IBM COS or S3 End point URL.
cos.endpoint.url=

# the IBM COS or S3 location.
cos.location=

# the IBM COS or S3 bucket name to import from.
cos.bucket.name=

# if use IBM credential(Y/N), default(Y).
cos.credential.ibm=Y

# COS network timeouts
cos.request.timeout=60000
cos.socket.timeout=60000

# The number of COS keys (items) to fetch per read
cos.max.keys=1000
```

#### The postgres.properties file

```
db.host=<POSTGRES-HOST-NAME>
db.port=<POSTGRES-PORT>
db.database=<POSTGRES-DATABASE>
user=<POSTGRES-USER>
password=<POSTGRES-PASSWORD>
sslrootcert=/path/to/postgres.cert
ssl=true
sslmode=require
currentSchema=FHIRBUCKET
```

#### The derby.properties file

PostgreSQL is the preferred database for hosting the fhir-bucket schema. Derby can, however, be used for development. The derby.properties file must be configured as follows:

```
db.database=derby/bucketDB
db.create=Y
```

The name of the Derby database can be anything (without spaces) but must be contained within a folder called "derby".

### Schema Deployment

As a one-time activity, the schema objects can be created using the following command:

If using a local Derby instance:

```
#!/usr/bin/env bash

JAR="~/git/FHIR/fhir-bucket/target/fhir-bucket-*-SNAPSHOT-cli.jar"

java -jar "${JAR}"                 \
  --db-type derby                  \
  --db-properties derby.properties \
  --create-schema
```

If using a local PostgreSQL instance:

```
#!/usr/bin/env bash

JAR="~/git/FHIR/fhir-bucket/target/fhir-bucket-*-SNAPSHOT-cli.jar"

java -jar "${JAR}"                    \
  --db-type postgresql                \
  --db-properties postgres.properties \
  --create-schema
```

This tracking database can be shared with the instance used by FHIR, but for proper performance testing it should be on a separate host. The standard schema for the tables is FHIRBUCKET.

Schema creation does not have to be performed separately. If you wish to create the schema as part of the main program run, specify

```
  --bootstrap-schema
```

on the command line. The schema creation and update process is idempotent. Changes are only applied when required, and concurrency is managed correctly to ensure only one instance makes changes if multiple instances of fhir-bucket are run simultaneously.

The preferred approach is to use the new `--bootstrap-schema` option when running the main workload, in which case the `--create-schema` activity isn't required.


### Running

The following script can be used to run the bucket loader from a local build:

```
#!/usr/bin/env bash

JAR="~/git/FHIR/fhir-bucket/target/fhir-bucket-*-SNAPSHOT-cli.jar"

java -jar "${JAR}"                     \
  --bootstrap-schema                   \
  --db-type postgresql                 \
  --db-properties postgres.properties  \
  --cos-properties cos.properties      \
  --fhir-properties fhir.properties    \
  --bucket example-bucket              \
  --tenant-name example-tenant         \
  --file-type NDJSON                   \
  --max-concurrent-fhir-requests 40    \
  --max-concurrent-json-files 10       \
  --max-concurrent-ndjson-files 1      \
  --connection-pool-size 20            \
  --incremental
```

To run using Derby, change the relevant arguments to:

```
...
  --db-type derby                   \
  --db-properties derby.properties  \
...
```


The `--immediate-local` option can be used to load files from a local file-system without the need for a FHIRBUCKET database or connection to COS:

```
java \
  -Djava.util.logging.config.file=logging.properties \
  -jar "${JAR}" \
  --db-type postgresql \
  --db-properties db.properties \
  --fhir-properties fhir.properties \
  --tenant-name your-tenant-name \
  --file-type JSON \
  --max-concurrent-fhir-requests 0 \
  --max-concurrent-json-files 0 \
  --max-concurrent-ndjson-files 0 \
  --connection-pool-size 10 \
  --immediate-local \
  --scan-local-dir /path/to/synthea/data
```

Because --immediate-local does not use a FHIRBUCKET database, there is no tracking of the logical ids generated by the LinuxForHealth FHIR Server. This means it is not possible to run the interop workload against this data.

To track the logical ids, you can provide a FHIRBUCKET database configuration along with the --scan-local-dir argument, but do not specify --immediate-local:

```
java \
  -Djava.util.logging.config.file=logging.properties \
  -jar "${JAR}" \
  --db-type postgresql \
  --db-properties db.properties \
  --fhir-properties fhir.properties \
  --tenant-name your-tenant-name \
  --file-type JSON \
  --max-concurrent-fhir-requests 0 \
  --max-concurrent-json-files 0 \
  --max-concurrent-ndjson-files 0 \
  --connection-pool-size 10 \
  --scan-local-dir /path/to/synthea/data
```

Once the directory scanning is complete, the program can be terminated. To load the files now registered in the FHIRBUCKET database, run the following:

```
java \
  -Djava.util.logging.config.file=logging.properties \
  -jar "${JAR}" \
  --db-type postgresql \
  --db-properties db.properties \
  --fhir-properties fhir.properties \
  --tenant-name your-tenant-name \
  --file-type JSON \
  --no-scan \
  --max-concurrent-fhir-requests 40 \
  --max-concurrent-json-files 10 \
  --max-concurrent-ndjson-files 0 \
  --connection-pool-size 40 \
  --scan-local-dir /path/to/synthea/data
```

Note that the --scan-local-dir [path-name] option must still be provided.


| Property Name | Description |
| -------------------- | -----|
| `--bootstrap-schema` | Creates/updates the schema as an initial step before starting the main workload. Simplifies cloud deployment scenarios by avoiding the need for a separate job. Ensures only one instance will try to update the schema at a time. Do not specify `--create-schema` when using this option. |
| `--db-type type` | where `type` is one of: derby, postgresql. Specifies the type of database to use for the FHIRBUCKET tracking data. |
| `--create-schema` | Creates a new or updates an existing database schema. The program will exit after the schema operations have completed.|
| `--schema-name` | The custom schema used for FHIRBUCKET tracking data. The default is `FHIRBUCKET`.|
| `--tenant-name fhir-tenant-name` | the LinuxForHealth FHIR Server tenant name|
| `--cos-properties properties-file` | Connection properties file for COS | 
| `--db-properties properties-file` | Connection properties file for the database |
| `--db-prop` | Individual Connection Property name-value pair for the database|
| `--fhir-properties properties-file` | Connection properties file for the FHIR server *Refer to prior section for details, *IBM FHIR Server Properties - The FHIR Properties*|
| `--bucket cos-bucket-name` | The bucket name in COS |
| `--bucket-path` | The path to use in the bucket in COS|
| `--file-type file-type` | One of: JSON, NDJSON. Used to limit the discovery scan to a particular type of file/entry |
| `--cos-scan-interval-ms millis` | The number of milliseconds to wait before scanning the COS bucket again to discover new entries. Default is 300000|
| `--immediate-local` | Load a directory hierarchy containing JSON/NDJSON files. Does not use a FHIRBUCKET database and therefore does not record generated ids. Requires `--scan-local-dir` to specify the directory to be used. |
| `--incremental` | If the loader is stopped or fails before a bundle completes, the bundle will be reclaimed by another loader instance after the heartbeat timeout expires (60s). If the `--incremental` option is specified, the loader skips lines already processed in the NDJSON file. This is reasonably quick but is approximate, and may end up skipping rows due to threaded processing when the loader terminated. |
| `--incremental-exact` | the FHIRBUCKET tracking schema in the database is checked for every line in the NDJSON to see if any resources have been recorded for it, and if so, processing will be skipped. |
| `--max-concurrent-ndjson-files pool-size` | The maximum number of NDJSON files to read in parallel. Typically a small number, like the default which is 1. |
| `--max-concurrent-json-files pool-size` | The maximum number of JSON files to read in parallel. Each JSON file translates to a single FHIR request, which may be a single resource, or a bundle with many resources. |
| `--max-concurrent-fhir-requests pool-size` | The maximum number concurrent FHIR requests. For example, an NDJSON file may contain millions of records. Although a single NDJSON file is read sequentially, each resource (row) can be processed in parallel, up to this limit |
| `--connection-pool-size pool-size` | The maximum size of the local tracking database's connection pool. Threads will block and wait if the current number of active connections exceeds this value. Default size is 10.|
| `--recycle-seconds seconds` | Artificially force discovered entries to be reloaded some time after they have been loaded successfully. This permits the loader to be set up in a continuous mode of operation, where the resource bundles are loaded over and over again, generating new resources to fill the target system with lots of data. The processing times for each load is tracked, so this can be used to look for regression. |
| `--path-prefix prefix` | Limit the discovery scan to keys with the given prefix. |
| `--pool-shutdown-timeout-seconds seconds` | How many seconds to wait for the resource pool to shutdown when the loader has been asked to terminate. This value should be slightly longer than the Liberty transaction timeout.|
| `--scan-local-dir` | Scan a local directory instead of a COS bucket. Can work with a FHIRBUCKET database if tracking is required, or just a simple scan-and-load if `--immediate-local` is given. |
| `--concurrent-payer-requests` | The number of concurrent requests for the workload. Default is 40.|
| `--bundle-cost-factor` | Cost to processing bundles to reduce concurrency and avoid overload/timeouts|
| `--target-bucket` | The break bundles into bite-sized pieces to avoid tx timeouts. Store new bundles under this bucket.|
| `--target-prefix` | The break bundles into bite-sized pieces to avoid tx timeouts. Store new bundles under this key prefix.|
| `--max-resources-per-bundle` | The maximum number of resources to pack in a Bundle|
| `--patient-buffer-size` | The number of patients should we load into the buffer during the synthetic load |
| `--buffer-recycle-count` | The number of times we should use the same set of patient ids during the synthetic load |
| `--no-scan` | Disables the periodic scan of COS looking for new entries. The default is false|

*Database Properties*

|Property|Description|
|--------|-----------|
|db.host | The database server hostname|
|db.port | The database server port|
|db.database | The name of the database|
|user | A username with connect and admin permissions on the target database|
|password | The user password for connecting to the database |
|ssl | true or anything else, true triggers JDBC to use ssl, an example --prop ssl=true |

A sample properties file can be found at https://github.com/LinuxForHealth/FHIR/blob/main/fhir-persistence-schema/postgres.properties

*COS Properties* 

|Property|Description|
|--------|-----------|
|cos.api.key | the IBM COS API key or S3 access key|
|cos.srvinstid | the IBM COS service instance id or S3 secret key|
|cos.endpoint.url | the IBM COS or S3 End point URL.|
|cos.location | the IBM COS or S3 location.|
|cos.bucket.name | the IBM COS or S3 bucket name to import from.|
|cos.credential.ibm | if use IBM credential(Y/N), default(Y)|
|cos.request.timeout | Request Timeout in milliseconds|
|cos.socket.timeout | Socket Timeout in milliseconds|
|cos.max.keys | The max keys to return per list objects request|

### Internals

The purpose of fhir-bucket is to exercise the ingestion capability of the LinuxForHealth FHIR Server (or any FHIR Server, for that matter). It scans IBM Cloud Object Store using the S3 connector and registers each matching entry in a tracking database.

This tracking database is used to allocate these discovered entries (resource bundles) to loaders with free capacity. Several loader instances (JVMs) can be run in parallel and will coordinate their work using the common tracking database.

When an instance of the fhir-bucket loader starts, it registers itself by creating a new entry in the loader_instances table. It periodically updates the heartbeat_tstamp of this record to publicize its liveness. Periodically, loader instances will perform a liveness check, looking for any other instances with an old heartbeat_tstamp. If the timestamp is considered too old, then the respective loader instance is considered to have failed. Any jobs it was running or had been assigned are cleared so that they can be picked up by another loader instance and hopefully completed successfully (see ClearStaleAllocations class).

The COS scanner periodically scans COS to look for new items. It also checks each current item to see if the signature (size, modified time or etag hash) has changed. If a change is detected, the version number is incremented and any current allocation is cleared (recinded).

The job allocation is performed by a thread in the `CosReader` class. This thread loops, asking the database to assign it new work when the loader has free capacity (see the AllocateJobs class). If the database assigns fewer jobs than the available capacity, this indicates we've run out of work to do so the loop will sleep for a short while before asking again so as not to overload the database.

Each time a bundle is allocated to a loader for processing, a new record in RESOURCE_BUNDLE_LOADS is created. Any logical ids or errors generated during the processing of the bundle are recorded against the specific RESOURCE_BUNDLE_LOADS record. This permits tracking of multiple loads of the same bundle over time. The same bundle may be loaded multiple times if previous loads did not complete, or if recycling is enabled.

Processing of files/entries from COS is performed with two thread pools. The first thread pool is used to parallelize the processing of individual files. This is useful when there are large numbers of files, e.g. one per patient.

The second thread pool is used to process resources read from a file/entry. This is useful when the entries are NDJSON files which may contain millions of entries. The reader reads and validates each resource then submits the resource to the thread pool to parallelize the calls to FHIR.

Limits are placed on the number of files/entries which can be allocated to a particular instance, as well as the number of resources which are currently inflight waiting to be processed. This is important to avoid unbounded memory growth. The goal is to keep the thread pool as busy as possible without consuming too much memory by reading and queueing too many resources (the assumption is that the fhir-bucket loader can read and validate resources more quickly than the FHIR server can process them).

If the resource is a Bundle, then FHIR returns a bundle containing the newly assigned logical ids of every resource created from that bundle. Each of these logical ids is recorded in the LOGICAL_RESOURCES table in the tracking database.

If the resource file/entry is not a Bundle, then FHIR returns the newly assigned logical id in the Location header. This value is also stored in the LOGICAL_RESOURCES table in the tracking database.


### Metrics

The FHIRBUCKET schema tracks some useful statistics captured during the load runs which can be used to analyze performance.

#### Schema

| Table Name | Description |
| ---------- | ----------- |
| loader_instances | Each time a loader starts up it is allocated a unique loader_instances record |
| bucket_paths | The bucket name and item path up to the last / to avoid repeating the long path string for every item |
| resource_bundles | Represents a JSON or NDJSON file found in COS |
| resource_bundle_loads | Records each attempt to load a particular bundle |
| resource_bundle_errors | records errors by line. Includes HTTP response if available |
| resource_types | The FHIR model resource names |
| logical_resources | Holds the logical id for every resource created by the FHIR server |

See the org.linuxforhealth.fhir.bucket.persistence.FhirBucketSchema class for details (columns and relationships) on the above tables.

The `resource_bundle_loads` table contains timestamp fields marking the start and end of processing. The end time is only updated if the bundle is completed before the loader is stopped. 

Each `logical_resources` record contains a `created_tstamp` column which marks the time when the record was created in the FHIRBUCKET database.

#### PostgreSQL Analytic Queries

PostgreSQL uses a different mechanism for calculating the gap between two timestamps:

```
SELECT abs(EXTRACT(EPOCH FROM end - start)) AS gap_in_seconds
  FROM ...
```

The resource rate approximation query therefore becomes:

```
SET search_path=FHIRBUCKET,PUBLIC;

SELECT loader_instance_id, substr(object_name, 1, 24) object_name, resource_type, resource_count, resource_count / run_seconds AS resources_per_second,
       EXTRACT(EPOCH FROM bundle_end - bundle_start) AS bundle_duration
  FROM (
       SELECT lr.loader_instance_id, resource_type_id, rb.object_name, count(*) AS resource_count,
              EXTRACT(EPOCH FROM max(lr.created_tstamp) - min(lr.created_tstamp)) AS run_seconds,
              min(rb.load_started) bundle_start,
              max(rb.load_completed) bundle_end
         FROM logical_resources lr,
              resource_bundles rb
        WHERE lr.loader_instance_id IS NOT NULL
          AND rb.resource_bundle_id = lr.resource_bundle_id
          AND rb.load_completed IS NOT NULL
     GROUP BY lr.loader_instance_id, resource_type_id, rb.object_name
     ) lr,
       resource_types rt
 WHERE rt.resource_type_id = lr.resource_type_id
   AND lr.run_seconds > 0
;
```


## Configuring Logging

Use `-Djava.util.logging.config.file=logging.properties` as the first argument on the command line to configure Java Util Logging using the `logging.properties` file. An example of this file is given below:

```
handlers=java.util.logging.ConsoleHandler,java.util.logging.FileHandler
.level=INFO

# Minimal console output
java.util.logging.ConsoleHandler.level = INFO
java.util.logging.ConsoleHandler.formatter=org.linuxforhealth.fhir.database.utils.common.LogFormatter

# INFO to the log file, unless you want to see more
java.util.logging.FileHandler.level=INFO

# 50MB * 20 files ~= 1GB of log retention
java.util.logging.FileHandler.formatter=org.linuxforhealth.fhir.database.utils.common.LogFormatter
java.util.logging.FileHandler.limit=50000000
java.util.logging.FileHandler.count=20
java.util.logging.FileHandler.pattern=fhirbucket-%u-%g.log


# See FINE stuff for the scanner
#org.linuxforhealth.fhir.bucket.scanner.level=FINE
```




