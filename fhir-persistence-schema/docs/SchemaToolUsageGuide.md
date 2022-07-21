# IBM FHIR Server - Schema Deployment

This document details how to deploy the IBM FHIR Server schema and apply release upgrades. This document also describes how the schema changes are managed.

The schema tool generates the following object types that require management: 

- TABLESPACES
- SCHEMA 
- INDEX 
- PROCEDURE 
- SEQUENCE 
- TABLE
- TABLE CONSTRAINT 

For details on the schema design, refer to the [Schema Design](https://github.com/LinuxForHealth/FHIR/tree/main/fhir-persistence-schema/docs/SchemaDesign.md) document.

----------------------------------------------------------------
## Database Support


| Database   |        Version | Support |
|------------|----------------|-----------------------------------|
| PostgreSQL |            12+ | Single tenant per database for PHI. Tenant per schema for dev/test. |
| Derby      |      10.14.2.0 | Development only. Single tenant per database. |
| Citus      | PostgreSQL 12+ | Experimental. |

----------------------------------------------------------------
## Getting started

### Creating the database and user

To create the PostgreSQL database and database user, use the following commands:

``` shell
psql postgres
>postgres=# create database fhirdb;
>postgres=# create user fhirserver with password 'change-password';
```

## Printing the schema DDL for review

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

A sample properties file can be found at https://github.com/LinuxForHealth/FHIR/blob/main/fhir-persistence-schema/postgresql.properties

Alternatively, properties may be passed via the command line interface `--prop` flag (`--prop <propname>=<propvalue>`). The flag can be repeated for setting multiple properties.

## Execute the fhir-persistence-schema command line interface (CLI)

### Note on Concurrency Protection

The schema tool protects itself when multiple instances of the tool are run concurrently. This can happen in cloud deployment environments where multiple instances of the LinuxForHealth FHIR Server are deployed, with each running their own schema-update tool before starting the server process. Instances of the schema update tool first acquire a `lease` before they perform any operations on a particular schema (for example: creating a new table or altering an existing table). An instance will try to acquire a lease for 10s. If it is unable to do so, it will exit with an error message and exit code 6. If multiple instances of the tool are run concurrently, the instance blocked waiting for the lease may eventually acquire the lease after the first instance completes within the 10s window. If the first instance successfully updated the schema, the second instance will see that the schema is now up-to-date and will skip further processing for that schema. If the first instance failed to update the schema, the second instance will attempt to apply the changes again.

### Running the CLI

There are three core projects which are referenced: 

| Project | Description |
|----------|---------------------------------------------|
| `fhir-model` | The HL7 FHIR model - generated Java code |
| `fhir-database-utils` | The SQL/database constructs used to create SQL compliant statements |
| `fhir-persistence-schema` | Uses database-utils to define a physical data model for storing FHIR resources and supporting the FHIR API |

To deploy and manage the schema on a target database, the `fhir-persistence-schema` project includes a Main class that can apply and manage the schema updates.

``` shell
java -jar ./fhir-persistence-schema-${VERSION}-cli.jar [OPTIONS]
```

Note: Replace `${VERSION}` with the version of the jar you're using or use the wildcard `*` to match any version.

Note: Prior to IBM FHIR Server Release 5.0.0, the default value for `--db-type` was `db2`. As of IBM FHIR Server Release 5.0.0, there is no longer a default value and `--db-type` must be specified every time.

The following sections include common values for `OPTIONS`.

### Create new schema

For PostgreSQL:

```
--db-type postgresql \
--prop-file postgresql.properties \
--schema-name fhirdata \
--create-schemas
```

For Citus:

```
--db-type citus \
--prop-file citus.properties \
--schema-name fhirdata \
--create-schemas
```


### Deploy new schema or update an existing schema

For PostgreSQL:

The FHIRSERVER user is the database user used by the IBM FHIR Server to connect
to the database. This user is granted the minimal set of privileges required
for the IBM FHIR Server to operate. The FHIRADMIN user should only be used
for schema updates, not for IBM FHIR Server access.

```
--db-type postgresql \
--prop-file postgresql.properties \
--schema-name FHIRDATA \
--update-schema \
--grant-to FHIRSERVER
```

If the --grant-to is provided, the grants will be processed after the schema
objects have been created for a particular schema. No grant changes will be
applied if the schema is already at the latest version according to the
`{schema}.WHOLE_SCHEMA_VERSION` table. If grants need to be applied, then
run the schema tool using only the --grant-to option without --update-schema.

When updating the postgres schema, the autovacuum settings are configured.

For Citus:

IBM FHIR Server Release 5.0.0 includes experimental support for Citus. Configuration is mostly identical to PostgreSQL, except that the `-db-type` argument should be given as `citus`. The schema tool builds a
modified version of the schema leveraging the distribution capabilities of the
Citus database to provide increased scalability.


```
--db-type citus \
--prop-file citus.properties \
--schema-name FHIRDATA \
--update-schema \
--grant-to FHIRSERVER
```

When `--db-type citus` is specified, the resulting schema includes different behavior for some indexes and foreign key constraints. For details on the DISTRIBUTED schema design, refer to the [Schema Design](https://github.com/LinuxForHealth/FHIR/tree/main/fhir-persistence-schema/docs/SchemaDesign.md) document.

Note that the datasource must also be identified as type `citus` in the fhir-server-config.json file. See the [IBM FHIR Server Users Guide](https://linuxforhealth.github.io/FHIR/guides/FHIRServerUsersGuide) for more details.


You can create the standard version of the schema on a target Citus database
by specifying `postgresql` as the database type (Citus is just an extension of
PostgreSQL). In this case, all the schema objects will remain local to the
coordinator node. This may be useful to compare the scaling benefits of Citus:

```
--db-type postgresql \
--prop-file citus.properties \
--schema-name FHIRDATA \
--update-schema \
--grant-to FHIRSERVER
```


### Grant privileges to another data access user

```
--db-type citus \
--prop-file citus.properties \
--schema-name FHIRDATA \
--grant-to FHIRSERVER
```

### Update the stored procedures and functions for FHIRDATA (and not FHIR_ADMIN) (PostgreSQL and Citus)

For PostgreSQL:

```
--db-type postgresql \
--prop-file postgresql.properties \
--schema-name fhirdata \
--update-proc
```

For Citus:

```
--db-type citus \
--prop-file citus.properties \
--schema-name fhirdata \
--update-proc
```

### Drop the FHIR schema specified by `schema-name` (e.g. FHIRDATA)

For PostgreSQL:

```
--db-type postgresql \
--prop-file postgresql.properties \
--schema-name FHIRDATA \
--drop-schema-fhir \
--confirm-drop
```

For Citus:

```
--db-type citus \
--prop-file citus.properties \
--schema-name FHIRDATA \
--drop-schema-fhir \
--confirm-drop
```

### Drop all tables created by `--create-schemas` (including the FHIR-ADMIN schema)

For PostgreSQL:

```
--db-type postgresql \
--prop-file postgresql.properties \
--schema-name FHIRDATA \
--drop-schema-fhir \
--drop-schema-batch \
--drop-schema-oauth \
--drop-admin
```

For Citus:

```
--db-type citus \
--prop-file citus.properties \
--schema-name FHIRDATA \
--drop-schema-fhir \
--drop-schema-batch \
--drop-schema-oauth \
--drop-admin
```

Alternatively, you can drop specific schemas with `--drop-schema-batch schema-name-to-drop` and
`--drop-schema-oauth schema-name-to-drop`

## Adjust the Vacuum Settings for PostgreSQL Tables only
Since 4.9.0, the IBM FHIR Server has implemented support for modifying the [autovacuum](https://www.postgresql.org/docs/12/runtime-config-autovacuum.html). Per [4.1.2. Tuning Auto-vacuum](https://linuxforhealth.github.io/FHIR/guides/FHIRPerformanceGuide/#412-tuning-auto-vacuum) the schema tool modifies `autovacuum_vacuum_cost_limit`, `autovacuum_vacuum_scale_factor` and `autovacuum_vacuum_threshold`.

The autovacuum_vacuum_scale_factor is not automatically configured, and not recommended on Databases for Postgres on IBM Cloud. The system configuration overrides the setting.

### Specific Tables
To update a specific tables settings, you can run with  `--vacuum-table-name`.

```
java -jar ./fhir-persistence-schema-${VERSION}-cli.jar \
--db-type postgresql --prop db.host=localhost --prop db.port=5432 \
--prop db.database=fhirdb --schema-name fhirdata \
--prop user=fhiradmin --prop password=change-password \
--update-vacuum --vacuum-cost-limit 2000 --vacuum-threshold 1000 \
--vacuum-scale-factor 0.01 --vacuum-table-name LOGICAL_RESOURCES
```

The above command can also be used for Citus databases.

### All Tables in a Schema
To update all tables in a schema, you can run without the table parameter. If you omit any value, the  defaults are picked as described in the Performance guide.

```
java -jar ./fhir-persistence-schema-${VERSION}-cli.jar \
--db-type postgresql --prop db.host=localhost --prop db.port=5432 \
--prop db.database=fhirdb --schema-name fhirdata \
--prop user=fhiradmin --prop password=change-password \
--update-vacuum --vacuum-cost-limit 2000 --vacuum-threshold 1000 \
--vacuum-scale-factor 0.01
```

## Advanced SSL Configuration with Postgres (and Citus)

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
--prop-file postgresql.properties  \
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
|`--pool-size NUM` | The number of connections used to connect to the database|`--pool-size 30`|
|`--thread-pool-size NUM` | The number of threads to use for concurrent database operations|`--thread-pool-size 20`|
|`--skip-allocate-if-tenant-exists` | whether or not to skip over allocating the tenant where this tenantName/tenantKey combination already exists |`--skip-allocate-if-tenant-exists`|

Note: the schema tool design supports use of a thread pool to allow DDL operations to be applied in parallel. In practice, this leads to deadlocks in the database catalog, often related to the definition of foreign keys. Although the code includes retry protection against these deadlocks, the result is reduced DDL thoughput and longer schema creation times (DDL parallel processing was intended to make schema creation faster). The recommendation is therefore to not specify `--pool-size` or `--thread-pool-size` and rely on the default values instead.

The connection pool size must include additional headroom above the configured thread pool size. The schema tool will automatically increase the connection pool size if the configured value is too small for a given thread pool size. This prevents critical background functions from having to wait for connections when all the connections are consumed by long-running activities in the thread pool.

## Alternative: Manually apply the schema

To see the LinuxForHealth FHIR Server schema DDL:

### Print the schema to files by executing the SchemaPrinter:

*Linux/Mac*  

```
VERSION=5.0.0
java -jar ./fhir-persistence-schema-${VERSION}-cli.jar --to-file
```

*Windows*

```
set VERSION=5.0.0
java -jar ./fhir-persistence-schema-%VERSION%-cli.jar --to-file
```

Note: the jar file is stored locally in `fhir-persistence-schema/target` or in the Artifactory repository for this project.

----------------------------------------------------------------
# V0021 - Drops the DOMAINRESOURCE and RESOURCE tables

If there is data in the DOMAINRESOURCE and RESOURCE table groups, which is unexpected, the administrator may run the tool with `--force-unused-table-removal` to force the removal of the unused tables.

----------------------------------------------------------------
# Database Size Report (PostgreSQL)

Run this command to show a summary of the space used by IBM FHIR Server resources and their related schema objects:

``` shell
java -jar ./fhir-persistence-schema-${VERSION}-cli.jar \
--db-type postgresql \
--prop-file fhiradmin.properties \
--schema-name FHIRDATA \
--show-db-size
```

To include per-table and per-index in the output, add the `--show-db-size-detail` flag:

``` shell
java -jar ./fhir-persistence-schema-${VERSION}-cli.jar \
--db-type postgresql \
--prop-file fhiradmin.properties \
--schema-name FHIRDATA \
--show-db-size \
--show-db-size-detail
```

The detail rows are tab-separated, making it easy to load the data into a spreadsheet for further analysis.

**Notes:**
1. The size report is only supported on PostgreSQL databases.
2. The size report is intended as a guide to understand the relative space distribution of objects in the IBM FHIR Server data schema. The report is not intended to replace database utilities for calculating the total size of the database.

----------------------------------------------------------------
# List of IBM FHIR Server Persistence Schema Tool Flags

|Flag|Variable|Description|
|----------------|----------------|----------------|
|--help||This menu|
|--prop-file|path-to-property-file|loads the properties from a file|
|--schema-name|schema-name|uses the schema as specified, must be valid.|
|--grant-to|username|uses the user as specified, must be valid.
and grants permission to the username|
|--target|TYPE schemaName|The schemaName and type [BATCH,OAUTH,DATA]|
|--update-proc||updates the stored procedure for a specific schema|
|--check-compatibility||checks feature compatibility|
|--drop-admin||drops the admin schema|
|--db-type|dbType|Either derby, postgresql, citus. Required.|
|--schema-type|schemaType|Override the default schema type created for the configured database type. PostgresSQL->PLAIN, Derby->PLAIN, Citus->DISTRIBUTED. Experimental. |
|--confirm-drop||confirms the dropping of a schema|
|--update-vacuum||Update the Vacuum settings for PostgreSQL|
|--vacuum-table-name|tableName|Table Name to update vacuum settings|
|--vacuum-scale-factor|scaleFactor|The scale factor to alter to 'scaleFactor'|
|--vacuum-threshold|threshold|The threshold value to alter to 'threshold'|
|--vacuum-cost-limit|costLimit|The Vacuum cost limit to set|
|--force||Do not skip schema update process when the whole-schema-version matches.|
|--force-unused-table-removal||Forces the removal of unused tables - DomainResource, Resource|
|--prop|name=value|name=value that is passed in on the commandline|
|--pool-size|poolSize|database connection pool size (default 10)|
|--thread-pool-size|threadPoolSize|the pool size for concurrent database operations (default 1)|
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
|--show-db-size||Generate report with a breakdown of database size|
|--show-db-size-detail||Include detailed table and index info in size report|

FHIR® is the registered trademark of HL7 and is used with the permission of HL7.
