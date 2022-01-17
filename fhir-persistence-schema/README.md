# IBM FHIR Server - fhir-persistence-schema

Builds and manages the multi-tenant FHIR R4 RDBMS schema for Db2 and PostgreSQL and includes Derby support for testing.

This module is built into two different jar files. The default jar is included with the IBM FHIR Server web application and is used for bootstrapping Apache Derby databases (if configured). There is also an executable command line interface (cli) version of this jar that packages this module with all of its dependencies.

The executable command line interface (cli) version of this module can be downloaded from the project [Releases tab](https://github.com/IBM/FHIR/releases).

The schema tool protects itself when multiple instances of the tool are run concurrently. This can happen in cloud deployment environments where multiple instances of the IBM FHIR Server are deployed, with each running their own schema-update tool before starting the server process. Instances of the schema update tool first acquire a `lease` before they perform any operations on a particular schema (for example: creating a new table or altering an existing table). An instance will try to acquire a lease for 10s. If it is unable to do so, it will exit with an error message and exit code 6. If multiple instances of the tool are run concurrently, the instance blocked waiting for the lease may eventually acquire the lease after the first instance completes within the 10s window. If the first instance successfully updated the schema, the second instance will see that the schema is now up-to-date and will skip further processing for that schema. If the first instance failed to update the schema, the second instance will attempt to apply the changes again.

## Getting started
### Creating the database and user

To create the Db2 database and database user, use the following commands:

1. If necessary on your system, create the User
``` shell
groupadd -g 1002 fhir
useradd -u 1002 -g fhir -M -d /database/config/fhirserver fhirserver
```

1. Create the Database and grant connection

``` shell
su - db2inst1 -c "db2 CREATE DB FHIRDB using codeset UTF-8 territory us PAGESIZE 32768"
su - db2inst1 -c "db2 \"connect to fhirdb\" && db2 \"grant connect on database TO USER fhirserver\""
```

**Note 1:** When creating the user, make sure there is no group with the same name; otherwise the step *Grant privileges to data access user* below will fail with `SQLCODE=-569, SQLSTATE=56092` ("Authorization ID does not uniquely identify a user, a group or a role in the system"). If there already exists a group with the same name as the user, consider renaming the group with `groupmod -n <new-name> <old-name>`.

**Note 2:** When creating the database, `PAGESIZE` is important. So *do* use the statement below and not, e.g., the environment variable `DBNAME` of the [Db2 Docker container](https://hub.docker.com/r/ibmcom/db2) to generate the database; otherwise the step *Deploy new schema* below will fail with `SQLCODE=-286, SQLSTATE=42727`.  

To create the PostgreSQL database and database user, use the following commands:

``` shell
psql postgres
>postgres=# create database fhirdb;
>postgres=# create user fhirserver with password 'change-password';
```

### Printing the schema

To print the schema DDL for review, execute `com.ibm.fhir.schema.app.SchemaPrinter`:

``` shell
java -cp ./fhir-persistence-schema-${VERSION}-cli.jar com.ibm.fhir.schema.app.SchemaPrinter [--to-file]
```

Note: Replace `${VERSION}` with the version of the jar you're using or use the wildcard `*` to match any version.

With `--to-file` it outputs to `./schema.sql`, `./grants.sql`, and `./stored-procedures.sql`; otherwise to System.out.

### Connection properties

The `fhir-persistence-schema` tool uses a properties file for database connection information.

|Property|Description|
|--------|-----------|
|db.host | The database server hostname|
|db.port | The database server port|
|db.database | The name of the database|
|user | A username with connect and admin permissions on the target database|
|password | The user password for connecting to the database|
|sslConnection | true or anything else, true triggers JDBC to use ssl, an example --prop sslConnection=true |

A sample properties file can be found at https://github.com/IBM/FHIR/blob/master/fhir-persistence-schema/db2.properties

Alternatively, properties may be passed via the command line interface `--prop` flag (`--prop <propname>=<propvalue>`). The flag can be repeated for setting multiple properties.

## Execute the fhir-persistence-schema command line interface (CLI)

To deploy and manage the schema on a target database, the `fhir-persistence-schema` project includes a Main class that can parallelize the schema updates.

``` shell
java -jar ./fhir-persistence-schema-${VERSION}-cli.jar [OPTIONS]
```

Note: Replace `${VERSION}` with the version of the jar you're using or use the wildcard `*` to match any version.

The following sections include common values for `OPTIONS`.

### Create new schema
For Db2:

```
--prop-file db2.properties
--schema-name FHIRDATA
--create-schemas
```

For PostgreSQL:

```
--prop-file postgresql.properties
--schema-name fhirdata
--create-schemas
--db-type postgresql
```

### Deploy new schema or update an existing schema
For Db2:

The FHIRSERVER user is the database user used by the IBM FHIR Server to connect
to the database. This user is granted the minimal set of privileges required
for the IBM FHIR Server to operate. The FHIRADMIN user should only be used
for schema updates, not for IBM FHIR Server access.

```
--prop-file db2.properties
--schema-name FHIRDATA
--update-schema
--grant-to FHIRSERVER
```

If the --grant-to is provided, the grants will be processed after the schema
objects have been created for a particular schema. No grant changes will be
applied if the schema is already at the latest version according to the
`{schema}.WHOLE_SCHEMA_VERSION` table. If grants need to be applied, then
run the schema tool using only the --grant-to option without --update-schema.

For PostgreSQL:

The FHIRSERVER user is the database user used by the IBM FHIR Server to connect
to the database. This user is granted the minimal set of privileges required
for the IBM FHIR Server to operate. The FHIRADMIN user should only be used
for schema updates, not for IBM FHIR Server access.

```
--prop-file postgresql.properties
--schema-name FHIRDATA
--update-schema
--grant-to FHIRSERVER
--db-type postgresql
```
If the --grant-to is provided, the grants will be processed after the schema
objects have been created for a particular schema. No grant changes will be
applied if the schema is already at the latest version according to the
`{schema}.WHOLE_SCHEMA_VERSION` table. If grants need to be applied, then
run the schema tool using only the --grant-to option without --update-schema.

When updating the postgres schema, the autovacuum settings are configured.

### Grant privileges to another data access user

```
--prop-file db2.properties
--schema-name FHIRDATA
--grant-to FHIRSERVER
```

### Add a new tenant (e.g. default)  (Db2 only)

```
--prop-file db2.properties
--schema-name FHIRDATA
--allocate-tenant default
```

**Note:** Don't forget to copy the tenant-key secret generated by `--allocate-tenant`, you will find it in one of the last lines of the log output (`com.ibm.fhir.schema.app.Main Allocated tenant: default [key=<this-is-the-relevant-text>] with Id = 1`). This key must be added to the datasource configuration to authorize the FHIR server to access this tenant.

Use `--tenant-key-file tenant.key.file` to direct the action to read the tenant-key from file.  If the file exists the tenant key (up to 44 characters) is read from the file.  If the file does not exist, the generated tenantKey is written out to the file.

Note: for tenant names other than `default`, the server must determine the tenant id to use for each request.
By default, we get the tenant id from the `X-FHIR-TENANT-ID` header, but to trust this value requires a well-planned approach to security.
Once the server has determined the tenant id for a given request, it uses this to look up the tenantKey and the two are
used in conjunction to create or retrieve data for this tenant.
For more information on multi-tenancy, see section [4.9 Multi-tenancy of the IBM FHIR Server Users Guide](https://ibm.github.io/FHIR/guides/FHIRServerUsersGuide#49-multi-tenancy).


### Refresh Tenant Following Schema Update (Db2 only)

After a schema update you must run the refresh-tenants command to ensure that any new tables added by the update have the correct partitions. The refresh-tenants process will iterate over each tenant and allocate new partitions as needed. This step is idempotent, so you can run it more than once if required.


```
    java -jar schema/fhir-persistence-schema-*-cli.jar \
      --prop-file db2.properties --refresh-tenants
```

If processing completes successfully, the program will report `SCHEMA CHANGE: OK`. If an error occurs, run the step again after correcting the issue.


### Configure tenant-key (example)  (Db2 only)

Edit `wlp/usr/servers/fhir-server/config/default/fhir-server-config.json` and add the tenant-key captured from the add-tenant step above:

```
                "default": {
                    "tenantKey": "<the-base64-tenant-key>",
                    "type": "db2",
                    "connectionProperties": {
                        "serverName": "<db2-host-name>",
                        "portNumber": 50001,
                        "databaseName": "BLUDB",
                        "apiKey": "<your-db2-api-key>",
                        "securityMechanism": 15,
                        "pluginName": "IBMIAMauth",
                        "currentSchema": "FHIRDATA",
                        "driverType": 4,
                        "sslConnection": true,
                        "sslTrustStoreLocation": "resources/security/dbTruststore.jks",
                        "sslTrustStorePassword": "<your-ssl-truststore-password>"
                    }
                }
```


### Test a tenant (Db2 only)

```
--prop-file db2.properties
--schema-name FHIRDATA
--test-tenant default
--tenant-key "<the-base64-tenant-key>"
```

Use `--tenant-key-file tenant.key` to read the tenant-key to a file. You do not need `--tenant-key` if you use the file.

### Add a Key to Existing Tenant (Db2 only)
To add a tenant key for an existing tenant, replace FHIRDATA with your client schema, and change default to your tenant's name. 

```
--prop-file db2.properties
--schema-name FHIRDATA
--add-tenant-key default
```

**Example Output**
```
2020-03-24 13:54:36.387 00000001    INFO .common.JdbcConnectionProvider Opening connection to database: jdbc:db2://localhost:50000/FHIRDB
2020-03-24 13:54:37.012 00000001    INFO   com.ibm.fhir.schema.app.Main New tenant key: TNT1 [key=LogFbM06+PLS1cur/NOTREALg=]
2020-03-24 13:54:37.014 00000001    INFO   com.ibm.fhir.schema.app.Main Processing took:   0.637 s
2020-03-24 13:54:37.015 00000001    INFO   com.ibm.fhir.schema.app.Main SCHEMA CHANGE: OK
```

Note, you may want to add a tenant key when a key is lost or needs to be changed.

Use `--tenant-key-file tenant.key.file` to direct the action to read the tenant-key from file.  If the file exists the tenant key (up to 44 characters is read from the file.  If the file does not exist, the generated tenantKey is written out to the file.


### Remove all tenant keys from an Existing Tenant (Db2 only)
To remove all tenant keys for an existing tenant, replace FHIRDATA with your client schema, and change default to your tenant's name. 

```
--prop-file db2.properties
--schema-name FHIRDATA
--db-type db2
--revoke-all-tenant-keys default
```

**Example Output**
```
2021-06-07 15:30:41.782 00000001    INFO .common.JdbcConnectionProvider Opening connection to database: jdbc:db2://demodb2:50000/fhirdb
2021-06-07 15:30:42.405 00000001    INFO   com.ibm.fhir.schema.app.Main Tenant Key revoked for 'default' total removed=[1]
2021-06-07 15:30:42.419 00000001    INFO   com.ibm.fhir.schema.app.Main Processing took:   0.699 s
2021-06-07 15:30:42.420 00000001    INFO   com.ibm.fhir.schema.app.Main SCHEMA CHANGE: OK
```

### Remove a tenant key key from an Existing Tenant (Db2 only)
To remove a tenant key for an existing tenant, replace FHIRDATA with your client schema, and change default to your tenant's name. 

```
--prop-file db2.properties
--schema-name FHIRDATA
--db-type db2
--revoke-tenant-key default
--tenant-key rZ59TLyEpjU+FAKEtgVk8J44J0=
```

**Example Output**
```
2021-06-07 15:30:41.782 00000001    INFO .common.JdbcConnectionProvider Opening connection to database: jdbc:db2://demodb2:50000/fhirdb
2021-06-07 15:30:42.405 00000001    INFO   com.ibm.fhir.schema.app.Main Tenant Key revoked for 'default' total removed=[1]
2021-06-07 15:30:42.419 00000001    INFO   com.ibm.fhir.schema.app.Main Processing took:   0.699 s
2021-06-07 15:30:42.420 00000001    INFO   com.ibm.fhir.schema.app.Main SCHEMA CHANGE: OK
```

Use `--tenant-key-file tenant.key.file` to direct the action to read the tenant-key from file.

### Update the stored procedures and functions for FHIRDATA (and not FHIR_ADMIN) (Db2 and PostgreSQL)

For Db2:

```
--prop-file db2.properties
--schema-name FHIRDATA
--update-proc
```

For PostgreSQL:

```
--prop-file postgresql.properties
--schema-name fhirdata
--update-proc
--db-type postgresql
```

### Drop the FHIR schema specified by `schema-name` (e.g. FHIRDATA)
For Db2:

```
--prop-file db2.properties
--schema-name FHIRDATA
--drop-schema-fhir
--confirm-drop
```

For PostgreSQL:

```
--prop-file postgresql.properties
--schema-name FHIRDATA
--drop-schema-fhir
--confirm-drop
--db-type postgresql
```

### Drop all tables created by `--create-schemas` (including the FHIR-ADMIN schema)
For Db2:

```
--prop-file db2.properties
--schema-name FHIRDATA
--drop-schema-fhir
--drop-schema-batch
--drop-schema-oauth
--drop-admin
--confirm-drop
```

For PostgreSQL:

```
--prop-file postgresql.properties
--schema-name FHIRDATA
--drop-schema-fhir
--drop-schema-batch
--drop-schema-oauth
--drop-admin
--db-type postgresql
```

Alternatively, you can drop specific schemas with `--drop-schema-batch schema-name-to-drop` and
`--drop-schema-oauth schema-name-to-drop`

## Alternative: Setting up a shared Db2 with separate schemas for each tenant

For those using multiple schemas for each customer, for instance, customer 2 needs to be separately configured with the database and schema. 

### Create the additional schema

```
java -jar ./fhir-persistence-schema-${VERSION}-cli.jar \ 
--prop-file db2.properties
--create-schemas
--create-schema-batch FHIR_JBATCH_2ND
--create-schema-oauth FHIR_OAUTH_2ND
--create-schema-fhir FHIRDATA_2ND
```

### Deploy the additional schema

```
java -jar ./fhir-persistence-schema-${VERSION}-cli.jar \ 
--prop-file db2.properties
--schema-name FHIRDATA
--update-schema-batch FHIR_JBATCH_2ND
--update-schema-oauth FHIR_OAUTH_2ND
--update-schema-fhir FHIRDATA_2ND
```

### Grant privileges to data access user
```
java -jar ./fhir-persistence-schema-${VERSION}-cli.jar \ 
--prop-file db2.properties
--grant-to FHIRSERVER
--target BATCH FHIR_JBATCH_2ND
```

```
java -jar ./fhir-persistence-schema-${VERSION}-cli.jar \ 
--prop-file db2.properties
--grant-to FHIRSERVER
--target OAUTH FHIR_OAUTH_2ND
```

```
java -jar ./fhir-persistence-schema-${VERSION}-cli.jar \ 
--prop-file db2.properties
--grant-to FHIRSERVER
--target DATA FHIRDATA_2ND
```

## Adjust the Vacuum Settings for PostgreSQL Tables only
Since 4.9.0, the IBM FHIR Server has implemented support for modifying the [autovacuum](https://www.postgresql.org/docs/12/runtime-config-autovacuum.html). Per [4.1.2. Tuning Auto-vacuum](https://ibm.github.io/FHIR/guides/FHIRPerformanceGuide/#412-tuning-auto-vacuum) the schema tool modifies `autovacuum_vacuum_cost_limit`, `autovacuum_vacuum_scale_factor` and `autovacuum_vacuum_threshold`.

The autovacuum_vacuum_scale_factor is not automatically configured, and not recommended on Databases for Postgres on IBM Cloud. The system configuration overrides the setting.

### Specific Tables
To update a specific tables settings, you can run with  `--vacuum-table-name`.

```
java -jar ./fhir-persistence-schema-${VERSION}-cli.jar \
--db-type postgresql --prop db.host=localhost --prop db.port=5432 \
--prop db.database=fhirdb --schema-name fhirdata \
--prop user=fhiradmin --prop password=passw0rd \
--update-vacuum --vacuum-cost-limit 2000 --vacuum-threshold 1000 \
--vacuum-scale-factor 0.01 --vacuum-table-name LOGICAL_RESOURCES
```

### All Tables in a Schema
To update all tables in a schema, you can run without the table parameter. If you omit any value, the  defaults are picked as described in the Performance guide.

```
java -jar ./fhir-persistence-schema-${VERSION}-cli.jar \
--db-type postgresql --prop db.host=localhost --prop db.port=5432 \
--prop db.database=fhirdb --schema-name fhirdata \
--prop user=fhiradmin --prop password=passw0rd \
--update-vacuum --vacuum-cost-limit 2000 --vacuum-threshold 1000 \
--vacuum-scale-factor 0.01
```

## Advanced SSL Configuration with Postgres

Create a properties file like the following:

```
db.host=<url>
db.port=30048
db.database=fhirdb
user=fhirserver
password=<password>
ssl=true
sslmode=verify-full
sslrootcert=./fhir-postgresql.cert
```
You can specify any connection property in the property file, such as `logger=TRACE` to help with debugging.

Run the Update Schema with 
```
java -jar ./fhir-persistence-schema-${VERSION}-cli.jar \
--prop-file /Users/paulbastide/git/wffh/FHIR/fhir-persistence-schema/postgresql.properties  \
--schema-name FHIRDATA \
--update-schema \
--db-type postgresql
```

If you want to log the connection, you can add `loggerLevel=TRACE` to the properties file.

Note, this was run with AdoptOpenJDK. 

## Advanced Client Execution Argument
The following are advanced execution arguments

|Property|Description|Example|
|--------|-----------|-----------|
|`--pool-size NUM` | The number of connections used to connect to the database|`--pool-size 20`|
|`--skip-allocate-if-tenant-exists` | whether or not to skip over allocating the tenant where this tenantName/tenantKey combination already exists |`--skip-allocate-if-tenant-exists`|

## Alternative: Manually apply the schema

To manually apply the DDL to a Db2 instance:

1 - Print the schema to files by executing the SchemaPrinter:

*Linux/Mac*  

```
VERSION=4.0.1
java -jar ./fhir-persistence-schema-${VERSION}-cli.jar --to-file
```

*Windows*

```
set VERSION=4.0.1
java -jar ./fhir-persistence-schema-%VERSION%-cli.jar --to-file
```

Note: the jar file is stored locally in `fhir-persistence-schema/target` or in the Artifactory repository for this project.

2 - Connect to your instance and execute each of the following:

    - schema.sql:  `db2 -tvf schema.sql`
    - grants.sql:  `db2 -tvf grants.sql`
    - stored-procedures.sql:  `db2 -td@ -vf stored-procedures.sql`


# V0021 - Drops the DOMAINRESOURCE and RESOURCE tables

If there is data in the DOMAINRESOURCE and RESOURCE table groups, which is unexpected, the administrator may run the tool with `--force-unused-table-removal` to force the removal of the unused tables.


FHIR® is the registered trademark of HL7 and is used with the permission of HL7.


# Physical Data Model

_Note_: the following description is based on the standard Derby/PostgreSQL variant of the schema. The Db2 schema uses table partitioning and row-based access control (RBAC) to support multi-tenancy. The logical design is the same, except all the primary and foreign keys are prefixed with a multi-tenant identifier `mt_id`.

By convention, tables are named using the plural form of the data they represent.

## Finding and Reading a Resource

The name of the FHIR resource type is normalized and stored in the `resource_types` table. The `resource_type_id` is then used as a foreign key to reference the resource type throughout the schema.

```
fhirdb=> \d fhirdata.resource_types
                      Table "fhirdata.resource_types"
      Column      |         Type          | Collation | Nullable | Default 
------------------+-----------------------+-----------+----------+---------
 resource_type_id | integer               |           | not null | 
 resource_type    | character varying(64) |           | not null | 
Indexes:
    "resource_types_pk" PRIMARY KEY, btree (resource_type_id)
    "idx_unq_resource_types_rt" UNIQUE, btree (resource_type)
```

Resources of all types will each have a single record in the `logical_resources` table. The primary key for this table is `logical_resource_id` but the application (or business key) for the table is the tuple `{resource_type_id, logical_id}`. Per the FHIR specification, the logical id for a resource only needs to be unique for a given resource type. Thus, `Patient/abc123` and `Observation/abc123` is valid, and refer to two different resources.


```
fhirdb=> \d fhirdata.logical_resources
                              Table "fhirdata.logical_resources"
       Column        |            Type             | Collation | Nullable |      Default      
---------------------+-----------------------------+-----------+----------+-------------------
 logical_resource_id | bigint                      |           | not null | 
 resource_type_id    | integer                     |           | not null | 
 logical_id          | character varying(255)      |           | not null | 
 reindex_tstamp      | timestamp without time zone |           | not null | CURRENT_TIMESTAMP
 reindex_txid        | bigint                      |           | not null | 0
 last_updated        | timestamp without time zone |           |          | 
 is_deleted          | character(1)                |           | not null | 'X'::bpchar
 parameter_hash      | character varying(44)       |           |          | 
Indexes:
    "logical_resources_pk" PRIMARY KEY, btree (logical_resource_id)
    "unq_logical_resources" UNIQUE, btree (resource_type_id, logical_id)
    "idx_logical_resources_lupd" btree (last_updated)
    "idx_logical_resources_rits" btree (reindex_tstamp DESC)
```

Rows in the `logical_resources` are locked for updated during ingestion to protect the data integrity of the data model during the ingestion procedure. Only the rows for the resources being changed are locked. The IBM FHIR Server tries hard to apply locks in a deterministic order to avoid deadlocks but this isn't always possible. Deadlocks may sometimes occur when processing transaction bundles involving overlapping data.

Each logical resource also has a record in a resource-specific table. This table shares the same `logical_resource_id` value for its primary key as the global `logical_resources` table. This table is used as the "parent table" for the search parameter table foreign keys. Using resource-specific tables for search parameters is an optimization of the schema design and provides the following benefits:

1. The search parameter tables and their indexes do not need to include a `resource_type_id` column, saving space;
2. Separating parameter tables by resource type improves cardinality estimation which helps the database to optimize search queries.

```
fhirdb=> \d fhirdata.patient_logical_resources
                       Table "fhirdata.patient_logical_resources"
       Column        |            Type             | Collation | Nullable |   Default   
---------------------+-----------------------------+-----------+----------+-------------
 logical_resource_id | bigint                      |           | not null | 
 logical_id          | character varying(255)      |           | not null | 
 current_resource_id | bigint                      |           |          | 
 is_deleted          | character(1)                |           | not null | 'X'::bpchar
 last_updated        | timestamp without time zone |           |          | 
 version_id          | integer                     |           |          | 
Indexes:
    "patient_logical_resources_pk" PRIMARY KEY, btree (logical_resource_id)
    "idx_patient_logical_resourcescurrent_resource_id" btree (current_resource_id)
    "idx_patient_logical_resourceslogical_id" btree (logical_id)
```

Each version of a resource is stored in a resource-specific table with the `_resources` suffix:

```
fhirdb=> \d fhirdata.patient_resources
                         Table "fhirdata.patient_resources"
       Column        |            Type             | Collation | Nullable | Default 
---------------------+-----------------------------+-----------+----------+---------
 resource_id         | bigint                      |           | not null | 
 logical_resource_id | bigint                      |           | not null | 
 version_id          | integer                     |           | not null | 
 last_updated        | timestamp without time zone |           | not null | 
 is_deleted          | character(1)                |           | not null | 
 data                | bytea                       |           |          | 
Indexes:
    "patient_resources_pk" PRIMARY KEY, btree (resource_id)
    "patient_resources_prf_in1" UNIQUE, btree (resource_id)
    "idx_patient_resources_lupd" btree (last_updated)
    "idx_patient_resourceslogical_resource_id" btree (logical_resource_id)
```

The first version of a resource is given a `version_id` value of 1 and each subsequent version increments this value by 1 leaving no gaps.

To optimize certain queries, the `resource_id` for the most recent version of a resource is referenced from the `[resourceType]_logical_resources` table with the `current_resource_id` column. This is not enforced by a foreign key because the `[resourceType]_logical_resources` record is created first and already contains the intended value of the current `resource_id` which has been obtained from a sequence. This approach avoids an `UPDATE` on `[resourceType]_logical_resources` which is expensive during ingestion.

The IBM FHIR Server uses soft-delete when processing a FHIR `DELETE` interaction. This creates a new version of the resource with a minimal resource data value and the `IS_DELETED` flag = `Y`. Soft-deletes do not delete existing database records. The Patient Erase custom operation can be used to remove all traces of a patient from a database.

All timestamps stored in the IBM FHIR Server schema are UTC.

For more details on how the resource payload data is stored, see the next section.

## Scanning Resources

The IBM FHIR Server implements the whole-system `_history` endpoint to fetch resources in the order they were ingested into the system. This endpoint is described detail in the IBM FHIR Server [Conformance Guide](https://ibm.github.io/FHIR/Conformance/#whole-system-history) guide. This service is backed by the `resource_change_log` table which records the identity of each resource version as it is ingested. The table is indexed on `change_tstamp` which reflects the UTC last-modified timestamp of the version of the resource.

```
fhirdb=> \d fhirdata.resource_change_log
                        Table "fhirdata.resource_change_log"
       Column        |            Type             | Collation | Nullable | Default 
---------------------+-----------------------------+-----------+----------+---------
 resource_id         | bigint                      |           | not null | 
 resource_type_id    | integer                     |           | not null | 
 logical_resource_id | bigint                      |           | not null | 
 change_tstamp       | timestamp without time zone |           | not null | 
 version_id          | integer                     |           | not null | 
 change_type         | character(1)                |           | not null | 
Indexes:
    "resource_change_log_pk" PRIMARY KEY, btree (resource_id)
    "unq_resource_change_log_ctrtri" UNIQUE, btree (change_tstamp, resource_type_id, resource_id)
```

As reflected in the whole-system-history REST API, the `resource_change_log` table can be scanned in two ways:

1. Ordered by `resource_id`, following the natural order of the primary key
2. Ordered by `change_tstamp`, following the timeline of resources as they arrive

Filtering and ordering based on `resource_id` is the simplest approach because there are no duplicates to deal with although care is still required when reading data near the current time (within the window of the maximum transaction timeout time). This is because ids are allocated before the transaction commits. It is therefore possible for records with a smaler `resource_id` value to appear after records which have already been committed. This is not a limitation of the IBM FHIR Server, but just a side-effect that is common in systems like this.

The `change_tstamp` column can be used to scan from a point in time and a LIMIT clause can be used to restrict the size of the result set. The last returned `change_tstamp` value can be used in the next fetch to iterate forwards over all the data. Note that because two or more resources may share the same `change_tstamp` value, it's important to scan `WHERE change_tstamp >= ?` not just `WHERE change_tstamp > ?`. The reader must be prepared to handle resources which appeared in previous scan showing up again. It is reasonable to assume that only a handful of resources will share the same timestamp, but it is important to make sure that the LIMIT value exceeds this number to avoid being stuck in a continuous loop.

The following example query will return the first 100 resource version meta-data values for resources ingested since the beginning of 2021.

```
    SELECT c.resource_id, rt.resource_type, lr.logical_id, c.change_tstamp, c.version_id, c.change_type
      FROM fhirdata.resource_change_log  c,
           fhirdata.logical_resources   lr,
           fhirdata.resource_types      rt
     WHERE lr.logical_resource_id = c.logical_resource_id
       AND rt.resource_type_id = c.resource_type_id
       AND c.change_tstamp >= '2021-01-01'
  ORDER BY c.change_tstamp, c.resource_type_id, c.resource_id 
     LIMIT 100;

```

The following example query will return the first 100 resource version meta-data values for resources ingested since the database was created:

```
    SELECT c.resource_id, rt.resource_type, lr.logical_id, c.change_tstamp, c.version_id, c.change_type
      FROM fhirdata.resource_change_log  c,
           fhirdata.logical_resources   lr,
           fhirdata.resource_types      rt
     WHERE lr.logical_resource_id = c.logical_resource_id
       AND rt.resource_type_id = c.resource_type_id
       AND c.resource_id > 0
  ORDER BY c.resource_id 
     LIMIT 100;

```

This is an index-range-scan driven query which will be very fast.

The resource object is stored in a resource-specific table. Joining each table to the above query would break the efficiency of the above query, so the resource payload data must be read in a separate query using the table name based on the `resource_type` column in the above query:


```
    SELECT data
      FROM fhirdata.patient_resources
     WHERE resource_id = 1657;
```



The data is always stored as a JSON string compressed using GZIP. For example: 

```
echo -n "1f8b0800000000000000cd566d6fdb3610fe2b82beec8b298bd4bb9015589bbda209823a5d8badfd404b678b1b256a2495c408f2df7794eddaae9d365bd1615fe2883cdedd73f7dccbbdafc1a8415770bdeac12ffd2b6e0574d69ff8a2c64f9ac5398f324816094932c8216711a993889118a29cf0820161348cc39865112411be6bc172bfbcf76f401ba1ba9f4735782eb9b1affb9a5b70272c6494504ac2e89ac625cb4b1606699a4539fbcd7f98f816eeac53622cb78341f92574a0c7b713bf16377872863fde5d2b3bf3ed3bbfb1b62fa7d3dbdbdbe0360a945e4e695114d3bbc6b6f29dffecc7ed6b6fbef2ceb8d768586c5e197cb614b619e641a5daa95975b6012baa06b8b4cde69ba38ed9fabfb3297f16fcbac6e6891a6325160274e9ddb0200e4282a1204bc82a60ac48de755ee079de158aa3b401a84b8f24519ae7394b8a9885348d131a331451fd2031f61fc4c2b329027ce68281b180ced9f3cbdfef3ffa1ab4c458a876fe02e12d955e617c6eb81ce085aa45b71c63b832165a941a74572a51972ca06990c76140034aa33c8f823460518e2f2b553b12604653128d9136bde42b3c7ad3080bfec3c3646b72ccd0c6d6ccead1d656eafd6423b4494b23b331278b46e8e960a695d230c5474365070de7b0109d70d0f18eb83ba279856a26ff11d83c25c901d84b65bd9ff08b77a2f294f65e62623af559f48f3dfbe270806d50a5b02b7fe7c1295da774f4eb8226ad42f66a73c11d672f790b47eebf52063a9e24a1f7068c4d92e233d69ee2f95c68db18b8db4b948bf90f5f00645479251d43364abfab6bec62c6a57f0c52e9cfd4601bdef6168933199b88b37ac18de1553318b0d68c04183aab9dfcebd97e72371e7dd40a824d97106adb14b0400d9f0b893609afff1890793591620164055c9bad7be7508996a3e234284256147918e571e6a8f82f4cfe35f027db4b8a202cb230a4454a139652e698b86b5963497da897a7f7c2ad1d573b451ad5348b485ea5198929e7248ff39c2cd2398450c5199f4763aaed385dee5dcd8d64fbd8321ab6a05b2c17a996ab604b064797d92837bd61246461b4abdb8b5707357b01b5a8b8f45e0112aff62e87768e101de2f52c794c6072e448a34c2f2c978169b9b66becc23a7ffe27c867b303e4335509c435836ad0488d13d01f9338817dbf0a8da85d211bd3ed01c7b14a6848b26d7ff80af8ce5f1ee03bd70277896f8cf75254380c601fd9f1dde42923200ea280257ba866082bcb53967f2d4c57579707a0aeb017f54adb13d93aba3a4e1376b4aee6ba5e93538f8cfe5cf75c2bdde8dc217f8b0da208a3227e3bfad0b9c930ce5ae36ed562211c77f0c182b7428e7cb3389eff8494baad6e89f177c3d9ffc59dc95547231fd5f4b860893b777ea14de0bf1f3bdda8b11d07d0bebe375cfe099a32f60fd48dd19288bb3dcc4edfa80ef6d0254942e2acc0024d423c5ebbd0a8764d225c2a6bd707fd0560c374efc6e172be1e17b4a0296611f75b9739a8801ba8ddddb568d7f7794442ac86f89ab2328c4bb4857f4367886f47d2d1c2766aa3718b9f1d6a38eae0310db2240aa38cb12ca42c4ed2ddc890aa5b9e7e45d22228f2988e4ba6a361f6e915e4145f9680dcaec685740c95149de305f6bd1c3be80a995f1bef7ae83a902e419bb9fb5cc32d66427f62e8f60a6fe4660f08591ad1a3498cfab0f3ba163cdbacff5f548a11b938d0b69b208703e46058b8aa6b0769452fe1b923c573a52470ccda824b034e47db0eb895ad43e4fc92bc5b0e7cf968e770ed48805d94f3aa2fe36ce70774e4f5614bffbe5b4a619a7d8f3e1c3dbc7ff81bb7a7b144b60d0000" | xxd -r -p | gzip -d

yields:

{"resourceType":"Patient","id":"1748a37e5f5-57e8e823-d532-4e38-a92e-210404273e53","meta":{"versionId":"1","lastUpdated":"2021-11-03T14:28:20.667382Z"},"text":{"status":"generated","div":"<div xmlns=\"http://www.w3.org/1999/xhtml\">Generated by <a href=\"https://github.com/synthetichealth/synthea\">Synthea</a>.Version identifier: v2.4.0-404-ge7ce2295\n .   Person seed: -5368882594201645142  Population seed: 0</div>"},"extension":[{"extension":[{"url":"ombCategory","valueCoding":{"system":"urn:oid:2.16.840.1.113883.6.238","code":"2106-3","display":"White"}},{"url":"text","valueString":"White"}],"url":"http://hl7.org/fhir/us/core/StructureDefinition/us-core-race"},{"extension":[{"url":"ombCategory","valueCoding":{"system":"urn:oid:2.16.840.1.113883.6.238","code":"2186-5","display":"Not Hispanic or Latino"}},{"url":"text","valueString":"Not Hispanic or Latino"}],"url":"http://hl7.org/fhir/us/core/StructureDefinition/us-core-ethnicity"},{"url":"http://hl7.org/fhir/StructureDefinition/patient-mothersMaidenName","valueString":"Rosena550 West559"},{"url":"http://hl7.org/fhir/us/core/StructureDefinition/us-core-birthsex","valueCode":"F"},{"url":"http://hl7.org/fhir/StructureDefinition/patient-birthPlace","valueAddress":{"city":"Southampton","state":"Massachusetts","country":"US"}},{"url":"http://synthetichealth.github.io/synthea/disability-adjusted-life-years","valueDecimal":6.902998038473883},{"url":"http://synthetichealth.github.io/synthea/quality-adjusted-life-years","valueDecimal":59.09700196152612}],"identifier":[{"system":"https://github.com/synthetichealth/synthea","value":"2963d173-8c67-41aa-8488-f6be0ec47ab3"},{"type":{"coding":[{"system":"http://terminology.hl7.org/CodeSystem/v2-0203","code":"MR","display":"Medical Record Number"}],"text":"Medical Record Number"},"system":"http://hospital.smarthealthit.org","value":"2963d173-8c67-41aa-8488-f6be0ec47ab3"},{"type":{"coding":[{"system":"http://terminology.hl7.org/CodeSystem/v2-0203","code":"SS","display":"Social Security Number"}],"text":"Social Security Number"},"system":"http://hl7.org/fhir/sid/us-ssn","value":"999-10-7559"},{"type":{"coding":[{"system":"http://terminology.hl7.org/CodeSystem/v2-0203","code":"DL","display":"Driver's License"}],"text":"Driver's License"},"system":"urn:oid:2.16.840.1.113883.4.3.25","value":"S99978628"},{"type":{"coding":[{"system":"http://terminology.hl7.org/CodeSystem/v2-0203","code":"PPN","display":"Passport Number"}],"text":"Passport Number"},"system":"http://standardhealthrecord.org/fhir/StructureDefinition/passportNumber","value":"X70090394X"}],"name":[{"use":"official","family":"Stracke611","given":["Jackelyn13"],"prefix":["Mrs."]},{"use":"maiden","family":"Walker122","given":["Jackelyn13"],"prefix":["Mrs."]}],"telecom":[{"system":"phone","value":"555-479-4150","use":"home"}],"gender":"female","birthDate":"1916-02-21","deceasedDateTime":"1983-09-14T12:04:55-04:00","address":[{"extension":[{"extension":[{"url":"latitude","valueDecimal":41.753037227012456},{"url":"longitude","valueDecimal":-69.98418825902037}],"url":"http://hl7.org/fhir/StructureDefinition/geolocation"}],"line":["298 Reynolds Tunnel"],"city":"Brewster","state":"Massachusetts","postalCode":"02631","country":"US"}],"maritalStatus":{"coding":[{"system":"http://terminology.hl7.org/CodeSystem/v3-MaritalStatus","code":"M","display":"M"}],"text":"M"},"multipleBirthBoolean":false,"communication":[{"language":{"coding":[{"system":"urn:ietf:bcp:47","code":"en-US","display":"English"}],"text":"English"}}]}
```


The resource object can be more easily retrieved from the IBM FHIR Server REST API using a versioned read (VREAD) interaction:

```
  GET [server:port]/fhir-server/api/v4/Patient/1748a37e5f5-57e8e823-d532-4e38-a92e-210404273e53/_history/1
```

Database implementations use different strategies to inline the resource payload data blob. When the size of this object exceeds the database-specific threshold, the database stores it outside of the patient_resources row which can impact how the data gets cached. To make the most of the IO capacity of the database and its underlying storage subsystem, clients should parallelize the read operation (using a thread-pool, for example).

```
                                             | ---> [thread-2] read payload data
        [thread-1]            -----------    | ---> [thread-3] read payload data
       scan-history ------->   | queue |  ---+ ---> [thread-4] read payload data
         ^             |      -----------    | ---> [thread-5] read payload data
         |             |     resource ids    | ---> [thread-6] read payload data
         \-------------/
             repeat
```


## Search Parameters

```
fhirdb=> \d fhirdata.parameter_names
                      Table "fhirdata.parameter_names"
      Column       |          Type          | Collation | Nullable | Default 
-------------------+------------------------+-----------+----------+---------
 parameter_name_id | integer                |           | not null | 
 parameter_name    | character varying(255) |           | not null | 
Indexes:
    "parameter_names_pk" PRIMARY KEY, btree (parameter_name_id)
    "idx_parameter_name_rtnm" UNIQUE, btree (parameter_name)
```

```
fhirdb=> \d fhirdata.common_token_values
                                 Table "fhirdata.common_token_values"
        Column         |          Type           | Collation | Nullable |           Default            
-----------------------+-------------------------+-----------+----------+------------------------------
 common_token_value_id | bigint                  |           | not null | generated always as identity
 code_system_id        | integer                 |           | not null | 
 token_value           | character varying(1024) |           | not null | 
Indexes:
    "common_token_values_pk" PRIMARY KEY, btree (common_token_value_id)
    "idx_common_token_values_tvcp" UNIQUE, btree (token_value, code_system_id)
```

```
fhirdb=> \d fhirdata.code_systems
                       Table "fhirdata.code_systems"
      Column      |          Type          | Collation | Nullable | Default 
------------------+------------------------+-----------+----------+---------
 code_system_id   | integer                |           | not null | 
 code_system_name | character varying(255) |           | not null | 
Indexes:
    "code_systems_pk" PRIMARY KEY, btree (code_system_id)
    "idx_code_system_cinm" UNIQUE, btree (code_system_name)

```

```
fhirdb=> select * from fhirdata.code_systems;
 code_system_id |                                 code_system_name                                 
----------------+----------------------------------------------------------------------------------
          21000 | http://unitsofmeasure.org
          21001 | Claim
          21002 | Encounter
          21003 | Observation
          21004 | Organization
          21005 | Patient
          21006 | Practitioner
          21007 | default-token-system
          21008 | http://hl7.org/fhir/claim-use
          21009 | http://hl7.org/fhir/diagnostic-report-status
```


```
             Table Name             |                        Description
------------------------------------+---------------------------------------------------------------
[resourceType]_current_refs         | Problems, Medications, Allergies, Drug allergies current lists
[resourceType]_date_values          | Search date parameters
[resourceType]_latlng_values        | Search location parameters
[resourceType]_number_values        | Search number parameters
[resourceType]_str_values           | Search string parameters
[resourceType]_quantity_values      | Search quantity parameters
[resourceType]_resource_token_refs  | Search token parameters
[resourceType]_security             | Search security parameters
[resourceType]_profiles             | Search profile parameters
[resourceType]_tags                 | Search tag parameters
logical_resource_profiles           | Profiles claimed by the resource
logical_resource_tags               | Tags associated with the resource
logical_resource_security           | Security labels associated with the resource
```


# List of IBM FHIR Server Persistence Schema Tool Flags


|Flag|Variable|Description|
|----------------|----------------|----------------|
|--help||This menu|
|--prop-file|path-to-property-file|loads the properties from a file|
|--schema-name|schema-name|uses the schema as specified, must be valid.|
|--grant-to|username|uses the user as specified, must be valid.
and grants permission to the username|
|--target|TYPE schemaName|The schemaName and type [BATCH,OAUTH,DATA]|
|--add-tenant-key|tenant-key|adds a tenant-key|
|--revoke-tenant-key||revokes the key for the specified tenant and tenant key|
|--revoke-all-tenant-keys||revokes the all of the keys for the specified tenant|
|--update-proc||updates the stored procedure for a specific tenant|
|--check-compatibility||checks feature compatibility|
|--drop-admin||drops the admin schema|
|--test-tenant|tenantName| used to test with tenantName|
|--tenant-key|tenantKey|the tenant-key in the queries|
|--tenant-key-file|tenant-key-file-location|sets the tenant key file location|
|--list-tenants||fetches list of tenants and current status|
|--db-type|dbType|Either derby, postgresql, db2|
|--delete-tenant-meta|tenantName|deletes tenant metadata given the tenantName|
|--drop-detached|tenantName|(phase 2) drops the detached tenant partition tables given the tenantName|
|--freeze-tenant||Changes the tenant state to frozen, and subsequently (Db2 only)|
|--drop-tenant|tenantName|(phase 1) drops the tenant given the tenantName|
|--refresh-tenants||(Db2 only) ensure that any new tables added by the update have the correct partitions. The refresh-tenants process will iterate over each tenant and allocate new partitions as needed.|
|--allocate-tenant||allocates a tenant|
|--confirm-drop||confirms the dropping of a schema|
|--update-vacuum||Update the Vacuum settings for PostgreSQL|
|--vacuum-table-name|tableName|Table Name to update vacuum settings|
|--vacuum-scale-factor|scaleFactor|The scale factor to alter to 'scaleFactor'|
|--vacuum-threshold|threshold|The threshold value to alter to 'threshold'|
|--vacuum-cost-limit|costLimit|The Vacuum cost limit to set|
|--skip-allocate-if-tenant-exists||Skips allocating a tenant if it already exists|
|--force-unused-table-removal||Forces the removal of unused tables - DomainResource, Resource|
|--force||Do not skip schema update process when the whole-schema-version matches.|
|--prop|name=value|name=value that is passed in on the commandline|
|--pool-size|poolSize|poolsize used with the database actions|
|--drop-schema-oauth||drop the db schema used by liberty's oauth/openid connect features|
|--drop-schema-batch||drop the db schema used by liberty's java-batch feature"|
|--drop-schema-fhir||drop the schema set by '--schema-name'|
|--update-schema||deploy or update the schema set by '--schema-name', *deprecated* use --update-schema-fhir|
|--update-schema-fhir|schemaName|Updates the FHIR Data Schema|
|--update-schema-batch|schemaName|Updates the Batch Schema|
|--update-schema-oauth|schemaName|Updates the OAuth Schema|
|--create-schemas||create the database schemas for batch, oauth, and the fhir schema set by '--schema-name'|
|--create-schema-fhir|schemaName|Create the FHIR Data Schema|
|--create-schema-batch|schemaName|Create the Batch Schema|
|--create-schema-oauth|schemaName|Create the OAuth Schema|
