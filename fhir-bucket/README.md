## Synthetic Data Loader
Scans cloud object storage buckets and uploads data using the FHIR REST API

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
cos.api.key=MCnKzrs_JoQ0DZGKihvTYA6osHqetN_7K5wb2-g8_X5x

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

#### The db2.properties file

```
db.host=<DB2-HOST-NAME>
db.port=<DB2-PORT>
db.database=<DB2-DATABASE>
user=<DB2-USER>
password=<DB2-PASSWORD>
sslConnection=true
sslTrustStoreLocation=/path/to/dbTruststore.p12
sslTrustStorePassword=<TRUSTSTORE-PASSWORD>
currentSchema=FHIRBUCKET
```

#### The derby.properties file

Db2 is the preferred database for hosting the fhir-bucket schema. Derby can, however, be used for development. The derby.properties file must be configured as follows:

```
db.database=derby/bucketDB
db.create=Y
```

The name of the Derby database can be anything (without spaces) but must be contained within a folder called "derby".

### Schema Deployment

As a one-time activity, create the schema objects using the following command:

```
#!/bin/bash

JAR="~/git/FHIR/fhir-bucket/target/fhir-bucket-4.4.0-SNAPSHOT-cli.jar"
CP="${JAR}:/path/to/db2/jcc-11.5.0.0.jar"

java -classpath "${CP}"          \
  com.ibm.fhir.bucket.app.Main   \
  --db-type db2                  \
  --db-properties db2.properties \
  --create-schema
```

Or alernatively, if using a local Derby instance:

```
#!/bin/bash

JAR="~/git/FHIR/fhir-bucket/target/fhir-bucket-4.4.0-SNAPSHOT-cli.jar"
CP="${JAR}:/path/to/db2/jcc-11.5.0.0.jar"

java -classpath "${CP}"            \
  com.ibm.fhir.bucket.app.Main     \
  --db-type derby                  \
  --db-properties derby.properties \
  --create-schema
```

This tracking database can be shared with FHIR, but for proper performance testing should be a separate instance. The standard schema for the tables is FHIRBUCKET.



### Running

The following script can be used to run the bucket loader from a local build:

```
#!/bin/bash

JAR="~/git/FHIR/fhir-bucket/target/fhir-bucket-4.4.0-SNAPSHOT-cli.jar"
CP="${JAR}:/path/to/db2/jcc-11.5.0.0.jar"

java -classpath "${CP}"             \
  com.ibm.fhir.bucket.app.Main      \
  --db-type db2                     \
  --db-properties db2.properties    \
  --cos-properties cos.properties   \
  --fhir-properties fhir.properties \
  --bucket fhir-performance         \
  --tenant-name performance         \
  --file-type JSON                  \
  --reader-pool-size 1              \
  --handler-pool-size 10
```

To run using Derby, change the relevant arguments to:

```
...
  --db-type derby                   \
  --db-properties derby.properties  \
...
```


### Internals

The purpose of fhir-bucket is to exercise the ingestion capability of the IBM FHIR Server (or any FHIR Server, for that matter). It scans IBM Cloud Object Store using the S3 connector and registers each matching entry in a tracking database.

This tracking database is used to allocate these discovered entries (resource bundles) to loaders with free capacity. Several loader instances (JVMs) can be run in parallel and will coordinate their work using the common tracking database.

When an instance of the fhir-bucket loader starts, it registers itself by creating a new entry in the loader_instances table. It periodically updates the heartbeat_tstamp of this record to indicate its liveness. Periodically, loader instances will perform a liveness check, looking for any instances with an old heartbeat_tstamp. If the timestamp is considered too old, then the loader instance is considered to have failed. Any jobs it was running or had been assigned are cleared so that they can be picked up by another loader instance.

Processing of files/entries from COS is performed with two thread pools. The first thread pool is used to parallelize the processing of individual files. This is useful when there are large numbers of files, e.g. one per patient.

The second thread pool is used to process resources read from a file/entry. This is useful when the entries are NDJSON files which may contain millions of entries. The reader reads and validates each resource then submits the resource to the thread pool to parallelize the calls to FHIR.

Limits are placed on the number of files/entries which can be allocated to a particular instance, as well as the number of resources which are currently inflight waiting to be processed. This is important to avoid unbounded memory growth. The goal is to keep the thread pool as busy as possible without consuming too much memory by reading and queueing too many resources (the assumption is that the fhir-bucket loader can read and validate resources more quickly than the FHIR server can process them).

If the resource is a Bundle, then FHIR returns a bundle containing the newly assigned logical ids of every resource created from that bundle. Each of these logical ids is recorded in the fhir-bucket tracking database.

If the resource file/entry is not a Bundle, then FHIR returns the newly assigned logical id in the Location header. This value is also stored in the fhir-bucket tracking database.


### Metrics

The FHIRBUCKET schema tracks some useful statistics captured during the load runs which can be used to analyze performance.

#### Schema

| Table Name | Description |
| ---------- | ----------- |
| loader_instances | Each time a loader starts up it is allocated a unique loader_instances record |
| bucket_paths | The bucket name and item path up to the last / to avoid repeating the long path string for every item |
| resource_bundles | Represents a JSON or NDJSON file found in COS |
| resource_types | The FHIR model resource names |
| logical_resources | Holds the logical id for every resource created by the FHIR server |

See the com.ibm.fhir.bucket.persistence.FhirBucketSchema class for details (columns and relationships) on the above tables.

The `resource_bundles` table contains timestamp fields marking the start and end of processing. This is not valid if a bundle entry has been processed more than once with the `--incremental` option. This is because it only records the most recent run. A resource_bundle_history table may be more appropriate and should be considered if incremental loading is important.

Each `logical_resources` record contains a `created_tstamp` column which marks the time when the record was created in the FHIRBUCKET database.


#### Db2 Analytic Queries

To compute an approximate resources-per-second rate for each NDJSON bundle:
```
SET CURRENT SCHEMA FHIRBUCKET;

SELECT loader_instance_id, substr(object_name, 1, 24) object_name, resource_type, resource_count, resource_count / run_seconds AS resources_per_second,
       timestampdiff(2, bundle_end - bundle_start) bundle_duration
  FROM (
       SELECT lr.loader_instance_id, resource_type_id, rb.object_name, count(*) AS resource_count,
              timestampdiff(2, max(lr.created_tstamp) - min(lr.created_tstamp)) run_seconds,
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

The resource rate is calculated using the first and last creation timestamps from the logical_resources table.

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
