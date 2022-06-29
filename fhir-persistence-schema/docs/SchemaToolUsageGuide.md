# IBM FHIR Server - Schema Deployment

This document details how to deploy the IBM FHIR Server schema and apply release upgrades. This document also describes how the schema changes are managed.

The schema tool generates the following object types that require management: 

- DB2 PACKAGE
- TABLESPACES
- SCHEMA 
- GLOBAL VARIABLE 
- INDEX 
- PERMISSION 
- PROCEDURE 
- SEQUENCE 
- TABLE
- TABLE CONSTRAINT 

For details on the schema design, refer to the [Schema Design](https://github.com/IBM/FHIR/tree/main/fhir-persistence-schema/docs/SchemaDesign.md) document.

----------------------------------------------------------------
## Database Support


| Database   |        Version | Support |
|------------|----------------|-----------------------------------|
| DB2        |          11.5+ | Supports multi-tenancy. |
| PostgreSQL |            12+ | Single tenant per database. |
| Derby      |      10.14.2.0 | Development only. Single tenant per database. |
| Citus      | PostgreSQL 12+ | Experimental. |

----------------------------------------------------------------
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

A sample properties file can be found at https://github.com/IBM/FHIR/blob/main/fhir-persistence-schema/db2.properties

Alternatively, properties may be passed via the command line interface `--prop` flag (`--prop <propname>=<propvalue>`). The flag can be repeated for setting multiple properties.

## Execute the fhir-persistence-schema command line interface (CLI)

### Note on Concurrency Protection

The schema tool protects itself when multiple instances of the tool are run concurrently. This can happen in cloud deployment environments where multiple instances of the IBM FHIR Server are deployed, with each running their own schema-update tool before starting the server process. Instances of the schema update tool first acquire a `lease` before they perform any operations on a particular schema (for example: creating a new table or altering an existing table). An instance will try to acquire a lease for 10s. If it is unable to do so, it will exit with an error message and exit code 6. If multiple instances of the tool are run concurrently, the instance blocked waiting for the lease may eventually acquire the lease after the first instance completes within the 10s window. If the first instance successfully updated the schema, the second instance will see that the schema is now up-to-date and will skip further processing for that schema. If the first instance failed to update the schema, the second instance will attempt to apply the changes again.

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
For Db2:

```
--db-type db2 \
--prop-file db2.properties \
--schema-name FHIRDATA \
--create-schemas
```

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
For Db2:

The FHIRSERVER user is the database user used by the IBM FHIR Server to connect
to the database. This user is granted the minimal set of privileges required
for the IBM FHIR Server to operate. The FHIRADMIN user should only be used
for schema updates, not for IBM FHIR Server access.

```
--db-type db2 \
--prop-file db2.properties \
--schema-name FHIRDATA \
--update-schema \
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

When `--db-type citus` is specified, the resulting schema includes different behavior for some indexes and foreign key constraints. For details on the DISTRIBUTED schema design, refer to the [Schema Design](https://github.com/IBM/FHIR/tree/main/fhir-persistence-schema/docs/SchemaDesign.md) document.

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
--db-type db2 \
--prop-file db2.properties \
--schema-name FHIRDATA \
--grant-to FHIRSERVER
```

### Add a new tenant (e.g. default)  (Db2 only)

```
--db-type db2 \
--prop-file db2.properties \
--schema-name FHIRDATA \
--allocate-tenant default
```

**Note:** Don't forget to copy the tenant-key secret generated by `--allocate-tenant`, you will find it in one of the last lines of the log output (`com.ibm.fhir.schema.app.Main Allocated tenant: default [key=<this-is-the-relevant-text>] with Id = 1`). This key must be added to the datasource configuration to authorize the FHIR server to access this tenant.

Use `--tenant-key-file tenant.key.file` to direct the action to read the tenant-key from file.  If the file exists the tenant key (up to 44 characters) is read from the file.  If the file does not exist, the generated tenantKey is written out to the file.

Note: for tenant names other than `default`, the server must determine the tenant id to use for each request.
By default, we get the tenant id from the `X-FHIR-TENANT-ID` header, but to trust this value requires a well-planned approach to security.
Once the server has determined the tenant id for a given request, it uses this to look up the tenantKey and the two are
used in conjunction to create or retrieve data for this tenant.
For more information on multi-tenancy, see section [4.9 Multi-tenancy of the IBM FHIR Server Users Guide](https://linuxforhealth.github.io/FHIR/guides/FHIRServerUsersGuide#49-multi-tenancy).


### Refresh Tenant Following Schema Update (Db2 only)

After a schema update you must run the refresh-tenants command to ensure that any new tables added by the update have the correct partitions. The refresh-tenants process will iterate over each tenant and allocate new partitions as needed. This step is idempotent, so you can run it more than once if required.


```
    java -jar schema/fhir-persistence-schema-*-cli.jar \
      --db-type db2 \
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
--db-type db2 \
--prop-file db2.properties \
--schema-name FHIRDATA \
--test-tenant default \
--tenant-key "<the-base64-tenant-key>"
```

Use `--tenant-key-file tenant.key` to read the tenant-key to a file. You do not need `--tenant-key` if you use the file.

### Add a Key to Existing Tenant (Db2 only)
To add a tenant key for an existing tenant, replace FHIRDATA with your client schema, and change default to your tenant's name. 

```
--db-type db2 \
--prop-file db2.properties \
--schema-name FHIRDATA \
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
--db-type db2 \
--prop-file db2.properties \
--schema-name FHIRDATA \
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
--db-type db2 \
--prop-file db2.properties \
--schema-name FHIRDATA \
--revoke-tenant-key default \
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
--db-type db2 \
--prop-file db2.properties \
--schema-name FHIRDATA \
--update-proc
```

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
For Db2:

```
--db-type db2 \
--prop-file db2.properties \
--schema-name FHIRDATA \
--drop-schema-fhir \
--confirm-drop
```

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
For Db2:

```
--db-type db2 \
--prop-file db2.properties \
--schema-name FHIRDATA \
--drop-schema-fhir \
--drop-schema-batch \
--drop-schema-oauth \
--drop-admin \
--confirm-drop
```

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

## Alternative: Setting up a shared Db2 with separate schemas for each tenant

For those using multiple schemas for each customer, for instance, customer 2 needs to be separately configured with the database and schema. 

### Create the additional schema

```
java -jar ./fhir-persistence-schema-${VERSION}-cli.jar \ 
--db-type db2 \
--prop-file db2.properties \
--create-schemas \
--create-schema-batch FHIR_JBATCH_2ND \
--create-schema-oauth FHIR_OAUTH_2ND \
--create-schema-fhir FHIRDATA_2ND
```

### Deploy the additional schema

```
java -jar ./fhir-persistence-schema-${VERSION}-cli.jar \ 
--db-type db2 \
--prop-file db2.properties \
--schema-name FHIRDATA \
--update-schema-batch FHIR_JBATCH_2ND \
--update-schema-oauth FHIR_OAUTH_2ND \
--update-schema-fhir FHIRDATA_2ND
```

### Grant privileges to data access user
```
java -jar ./fhir-persistence-schema-${VERSION}-cli.jar \ 
--db-type db2 \
--prop-file db2.properties \
--grant-to FHIRSERVER \
--target BATCH FHIR_JBATCH_2ND
```

```
java -jar ./fhir-persistence-schema-${VERSION}-cli.jar \ 
--db-type db2 \
--prop-file db2.properties \
--grant-to FHIRSERVER \
--target OAUTH FHIR_OAUTH_2ND
```

```
java -jar ./fhir-persistence-schema-${VERSION}-cli.jar \ 
--db-type db2 \
--prop-file db2.properties \
--grant-to FHIRSERVER \
--target DATA FHIRDATA_2ND
```

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

To manually apply the DDL to a Db2 instance:

1 - Print the schema to files by executing the SchemaPrinter:

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

2 - Connect to your instance and execute each of the following:

    - schema.sql:  `db2 -tvf schema.sql`
    - grants.sql:  `db2 -tvf grants.sql`
    - stored-procedures.sql:  `db2 -td@ -vf stored-procedures.sql`


----------------------------------------------------------------
# V0021 - Drops the DOMAINRESOURCE and RESOURCE tables

If there is data in the DOMAINRESOURCE and RESOURCE table groups, which is unexpected, the administrator may run the tool with `--force-unused-table-removal` to force the removal of the unused tables.

----------------------------------------------------------------
# Database Size Report (Db2, PostgreSQL)

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

**Note:** For Db2, you must also include the desired tenant name, for example:

``` shell
java -jar ./fhir-persistence-schema-${VERSION}-cli.jar \
--db-type db2 \
--prop-file fhiradmin.properties \
--schema-name FHIRDATA \
--tenant-name MY_TENANT_NAME \
--show-db-size
```

The detail rows are tab-separated, making it easy to load the data into a spreadsheet for further analysis.

**Notes:**
1. The size report is only supported on Db2 and PostgreSQL databases.
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
|--add-tenant-key|tenant-key|adds a tenant-key|
|--revoke-tenant-key||revokes the key for the specified tenant and tenant key|
|--revoke-all-tenant-keys||revokes the all of the keys for the specified tenant|
|--update-proc||updates the stored procedure for a specific tenant|
|--check-compatibility||checks feature compatibility|
|--drop-admin||drops the admin schema|
|--test-tenant|tenantName| used to test with tenantName|
|--tenant-name|tenantName| specify the tenantName|
|--tenant-key|tenantKey|the tenant-key in the queries|
|--tenant-key-file|tenant-key-file-location|sets the tenant key file location|
|--list-tenants||fetches list of tenants and current status|
|--db-type|dbType|Either derby, postgresql, db2, citus. Required.|
|--schema-type|schemaType|Override the default schema type created for the configured database type. PostgresSQL->PLAIN, Derby->PLAIN, Db2->MULTITENANT, Citus->DISTRIBUTED. Experimental. |
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
