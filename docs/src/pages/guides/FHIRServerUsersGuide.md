---
layout: post
title:  IBM FHIR Server User's Guide
description: IBM FHIR Server User's Guide
Copyright: years 2017, 2022
lastupdated: "2022-02-09"
permalink: /FHIRServerUsersGuide/
---

- [1 Overview](#1-overview)
- [2 Installation](#2-installation)
  * [2.1 Installing a new server](#21-installing-a-new-server)
  * [2.2 Upgrading an existing server](#22-upgrading-an-existing-server)
  * [2.3 Docker](#23-docker)
- [3 Configuration](#3-configuration)
  * [3.1 Liberty server configuration](#31-liberty-server-configuration)
  * [3.2 FHIR server configuration](#32-fhir-server-configuration)
  * [3.3 Persistence layer configuration](#33-persistence-layer-configuration)
  * [3.4 "Update/Create" feature](#34-updatecreate-feature)
- [4 Customization](#4-customization)
  * [4.1 Extended operations](#41-extended-operations)
  * [4.2 Notification Service](#42-notification-service)
  * [4.3 Persistence interceptors](#43-persistence-interceptors)
  * [4.4 Registry resources](#44-registry-resources)
  * [4.5 Resource validation](#45-resource-validation)
  * [4.6 Extending search](#46-extending-search)
  * [4.7 FHIR client API](#47-fhir-client-api)
  * [4.8 Using local references within request bundles](#48-using-local-references-within-request-bundles)
  * [4.9 Multi-tenancy](#49-multi-tenancy)
  * [4.10 Bulk data operations](#410-bulk-data-operations)
  * [4.11 Audit logging service](#411-audit-logging-service)
  * [4.12 FHIR REST API](#412-fhir-rest-api)
- [5 Appendix](#5-appendix)
  * [5.1 Configuration properties reference](#51-configuration-properties-reference)
  * [5.2 Keystores, truststores, and the FHIR server](#52-keystores-truststores-and-the-fhir-server)
  * [5.3 OpenID Connect and OAuth 2.0](#53-openid-connect-and-oauth-20)
  * [5.4 Custom HTTP Headers](#54-custom-http-headers)
- [6 Related topics](#6-related-topics)

# 1 Overview
The IBM FHIR Server implements the HL7 FHIR HTTP API and supports the full set of FHIR-defined resource types.
This FHIR server is intended to be a common component for providing FHIR capabilities within health services and solutions.

# 2 Installation

## 2.1 Installing a new server
0.  Prereqs: The IBM FHIR Server requires Java 11 and has been tested with OpenJDK 11. To install Java on your system, we recommend downloading and installing OpenJDK 11 from https://adoptium.net/.

1.  To install the IBM FHIR Server, build or download the `fhir-install` zip installer (e.g. `fhir-server-distribution.zip` or `fhir-install-4.0.0-rc1-20191014-1610`).
The Maven build creates the zip package under `fhir-install/target`. Alternatively, releases will be made available from the [Releases tab](https://github.com/ibm/fhir/releases).

2.  Unzip the `.zip` package into a clean directory (referred to as `fhir-installer` here):
    ```
        mkdir fhir-installer
        cd fhir-installer
        unzip fhir-server-distribution.zip
    ```

3.  Determine an install location for the OpenLiberty server and the IBM FHIR Server webapp. Example:  `/opt/ibm/fhir-server`

4.  Run the `install.sh/.bat` script to install the server:
    ```
        ./fhir-server-dist/install.sh /opt/ibm/fhir-server
    ```
    This step installs the OpenLiberty runtime and the IBM FHIR Server web application. The Liberty runtime is installed in a directory called `wlp` within the installation directory that you specify. For example, in the preceding command, the root directory of the Liberty server runtime would be `/opt/ibm/fhir-server/wlp`.

5.  Configure the fhir-server's `server.xml` file as needed by completing the following steps:
    * Configure the ports that the server listen on. The server is installed with only port 9443 (HTTPS) enabled by default. To change the port numbers, modify the values in the `httpEndpoint` element.
    * Configure a server keystore and truststore. The IBM FHIR Server is installed with a default keystore file that contains a single self-signed certificate for localhost. For production use, you must create and configure your own keystore and truststore files for the FHIR server deployment (that is, generate your own server certificate or obtain a trusted certificate, and then share the public key certificate with API consumers so that they can insert it into their client-side truststore). The keystore and truststore files are used along with the server's HTTPS endpoint and the FHIR server's client-certificate-based authentication protocol to secure the FHIR server's endpoint. For more information, see [Section 5.2 Keystores, truststores, and the FHIR server](#52-keystores-truststores-and-the-fhir-server).
    * Configure an appropriate user registry. The FHIR server is installed with a basic user registry that contains a single user named `fhiruser`. For production use, it's best to configure your own user registry. For more information about configuring user registries, see the [OpenLiberty documentation](https://openliberty.io/guides/security-intro.html#configuring-the-user-registry).

    To override the default fhiruser's password, one may set an Environment variable `FHIR_USER_PASSWORD` and for the fhiradmin's password one may set an Environment variable `FHIR_ADMIN_PASSWORD`.

6.  Make sure that your selected database product is running and ready to accept requests as needed:
    * By default, the FHIR server is installed with the JDBC persistence layer configured to use an Embedded Derby database. When using the `ibmcom/ibm-fhir-server` docker image, set the `BOOTSTRAP_DB` environment variable to `true` in order to bootstrap this database. For any other configuration, note the database host and port and, if necessary, create a user with privileges for deploying the schema.

7.  Create and deploy the IBM FHIR Server database schema as needed:
    * By default, the FHIR server is installed with the JDBC persistence layer configured to use an Embedded Derby database. When using the `ibmcom/ibm-fhir-server` docker image, set the `BOOTSTRAP_DB` environment variable to `true` in order to bootstrap this database. For any other configuration, use the `fhir-persistence-schema` module to create and deploy the database schema.
    * The Db2 persistence layer is always tenant-aware, so you must provision a tenant in addition to deploying the schema. Note the tenant name you provisioned and the tenantKey generated by `fhir-persistence-schema` for the next step.

8.  Configure the `fhir-server-config.json`<sup id="a1">[1](#f1)</sup> configuration file as needed:
    * By default, the FHIR server is installed with the JDBC persistence layer configured to use a single-tenant Embedded Derby database. For more information, see [Section 3.3 Persistence layer configuration](#33-persistence-layer-configuration).

9.  To start and stop the server, use the Liberty server command:
```
    <WLP_HOME>/bin/server start fhir-server
    <WLP_HOME>/bin/server stop fhir-server
```

9.  After you start the server, you can verify that it's running properly by invoking the `$healthcheck` endpoint like this:

```
    curl -k -u '<username>:<password>' 'https://<host>:<port>/fhir-server/api/v4/$healthcheck'
```

where `<username>` is one of the users configured in `server.xml` (default is `fhiruser`).  
Use single quotes around the URL to prevent $healthcheck from being evaluated as an environment variable on unix-based operating systems.  

One should see an empty body in the response with a HTTP Response Code 200.

For more information about the capabilities of the implementation, see [Conformance](https://ibm.github.io/FHIR/Conformance).

## 2.2 Upgrading an existing server
The IBM FHIR Server does not include an upgrade installer. To upgrade a server to the next version, install the new version to a separate location and copy the configuration files from your existing installation (reconciling any configuration-related changes from the new release in the process).

To manage database updates over time, the IBM FHIR Server uses custom tools from the `fhir-database-utils` project. Through the use of a metadata table, the database utilities can detect the currently installed version of the database schema and apply any new changes that are needed to bring the database to the current level.

Complete the following steps to upgrade the server:

1. Run the fhir-installer on a separate server.
2. Configure the new server as appropriate (`fhir-server/server.xml` and anything under the `fhir-server/configDropins`, `fhir-server/config`, and `fhir-server/userlib` directories).
3. Back up your database.  
4. Run the migration program (see [Section 3.3.1.1 Supported databases](#3311-supported-databases)).  
5. Disable traffic to the old server and enable traffic to the new server.

## 2.3 Docker
The IBM FHIR Server includes a Docker image [ibmcom/ibm-fhir-server](https://hub.docker.com/r/ibmcom/ibm-fhir-server).

Note, logging for the IBM FHIR Server docker image is to stderr and stdout, and is picked up by Logging agents.

The IBM FHIR Server is configured using Environment variables using:

| Environment Variable | Description |
|----------------------|-------------|
|`DISABLED_OPERATIONS`|A comma-separated list of operations which are disabled on the IBM FHIR Server, for example, `validate,import`. Note, do not include the dollar sign `$`|

*Development-Only*: If you are using the IBM FHIR Server on a development machine with under 3.25G of RAM. You should download [jvm-dev.options](https://github.com/IBM/FHIR/blob/main/fhir-server-webapp/src/main/liberty/config/configDropins/disabled/jvm-dev.options) and mount it when starting the Docker container.  You can use the following pattern for starting up in a restricted test environment (or build your own layer).

```
docker run -d -p 9443:9443 -e BOOTSTRAP_DB=true \
  -v $(pwd)/jvm-dev.options:/config/configDropins/default/jvm.options \
  ibmcom/ibm-fhir-server
```

# 3 Configuration
This chapter contains information about the various ways in which the IBM FHIR Server can be configured by users.

## 3.1 Liberty server configuration
The IBM FHIR Server is built on the open source [OpenLiberty project](https://openliberty.io/) but also works on the commercial [IBM WebSphere Liberty](https://www.ibm.com/cloud/websphere-liberty). In documentation and elsewhere, we use `Liberty` to refer to either/or.

As a Liberty application, the IBM FHIR Server is configurable like any other Liberty server. See https://openliberty.io/docs/latest/reference/config/server-configuration-overview.html for more information.

By default, we include:
* `server.xml` with core server details like the Liberty features, HTTP properties, application info, and a default user registry
* `jvm.options` with TLS and heap size settings
* `server.env` with timezone set to UTC
* `configDropins` with server.xml dropins sorted into `disabled`, `defaults`, and `overrides`

## 3.1.1 Encoded passwords
In the examples within the following sections, you'll see the default password `change-password`. In order to secure your server, these values should be changed.

For example, the basic user registry in the default server.xml defines users `fhiruser` and `fhiradmin` and variables FHIR_USER_PASSWORD and FHIR_ADMIN_PASSWORD, each with a default value of `change-password`.

These variables can be changed by setting environment variables by the same name.
Alternatively, users can change the server.xml or use Liberty configDropins to change the values.

Optionally, server.xml values can be encoded via the Liberty `securityUtility` command. For example, to encode a string value with the default `{xor}` encoding, run the following command:

``` sh
<WLP_HOME>/bin/securityUtility encode stringToEncode
```

The output of this command can then be copied and pasted into your `server.xml` or `fhir-server-config.json` file as needed. The `fhir-server-config.json` does not support the securityUtility's `{aes}` encoding at this time, but per the limits to protection through password encryption<sup>[a]</sup>, this encoding does not provide significant additional security beyond `exclusive or` (XOR) encoding.

## 3.1.2 Logging and trace
The default server.xml includes variables for configuring the log output:
```
    <variable name="TRACE_SPEC" defaultValue="*=info"/>
    <variable name="TRACE_FILE" defaultValue="trace.log"/>
    <variable name="TRACE_FORMAT" defaultValue="BASIC"/>
```

By default, INFO level messages will go to both the console and a log file at `wlp/usr/servers/fhir-server/logs/messages.log`. For the `ibmcom/ibm-fhir-server` docker image, the default is changed to go just to the console.

To enable tracing, set the TRACE_SPEC variable (e.g. by setting an environment variable by the same name) to a `:`-delimited list of `component = level` pair values. For example, to turn on tracing for the SQL generated by `fhir-persistence-jdbc`, set the TRACE_SPEC to `com.ibm.fhir.persistence.jdbc.dao.impl.*=fine:com.ibm.fhir.database.utils.query.QueryUtil=fine`.

By default, that trace output appears alongside the `messages.log` file in a file named `trace.log`, but the trace output can be sent to the console instead by setting the `TRACE_FILE` variable to `stdout`.

For more information on logging and tracing in Liberty, see https://openliberty.io/docs/latest/log-trace-configuration.html.

## 3.2 FHIR server configuration

### 3.2.1 Property names
Configuration properties stored within a `fhir-server-config.json` file are structured in a hierarchical manner. Here is an example:

```
    {
        "fhirServer":{
            "core":{
                "defaultPrettyPrint":false
            }
        }
    }
```

Throughout this document, we use a path notation to refer to property names. For example, the name of the `defaultPrettyPrint` property in the preceding example would be `fhirServer/core/defaultPrettyPrint`.

### 3.2.2 Tenant-specific configuration properties
The IBM FHIR server supports certain multi-tenant features. One such feature is the ability to set certain configuration properties on a per-tenant basis.

In general, the configuration properties for a particular tenant are stored in the `<WLP_HOME>/usr/servers/fhir-server/config/<tenant-id>/fhir-server-config.json` file, where `<tenant-id>` refers to the tenant's “short name” or tenant id.

The global configuration is considered to be associated with a tenant named `default`, so those properties are stored in the `<WLP_HOME>/usr/servers/fhir-server/config/default/fhir-server-config.json` file.

Similarly, tenant-specific search parameters are found at `<WLP_HOME>/usr/servers/fhir-server/config/<tenant-id>/extension-search-parameters.json`, whereas the global/default extension search parameters are at `<WLP_HOME>/usr/servers/fhir-server/config/default/extension-search-parameters.json`.

Search parameters are handled like a single configuration properly; providing a tenant-specific file will override the global/default extension search parameters as defined at [FHIRSearchConfiguration](https://ibm.github.io/FHIR/guides/FHIRSearchConfiguration).

More information about multi-tenant support can be found in [Section 4.9 Multi-tenancy](#49-multi-tenancy).

### 3.2.3 Compartment Search Performance

The IBM FHIR Server supports the ability to compute and store compartment membership values during ingestion. Once stored, these values can help accelerate compartment-related search queries. To use this feature, update the IBM FHIR Server to at least version 4.5.1 and run a reindex operation as described in the [fhir-bucket](https://github.com/IBM/FHIR/tree/main/fhir-bucket) project [README](https://github.com/IBM/FHIR/blob/main/fhir-bucket/README.md). The reindex operation reprocesses the resources stored in the database, computing and storing the new compartment reference values. After the reindex operation has completed, add the `useStoredCompartmentParam` configuration element to the relevant tenant fhir-server-config.json file to allow the search queries to use the pre-computed values:

```
    {
        "fhirServer": {
            "search": {
                "useStoredCompartmentParam": true
            }
        }
    }
```

Note that this parameter only enables or disables the compartment search query optimization feature. The compartment membership values are always computed and stored during ingestion or reindexing, regardless of the setting of this value. After the reindex operation is complete, it is recommended to set `useStoredCompartmentParam` to true. No reindex is required if this value is subsequently set to false.

## 3.3 Persistence layer configuration
The IBM FHIR Server allows deployers to select a persistence layer implementation that fits their needs. Currently, the server includes a JDBC persistence layer which supports Apache Derby, IBM Db2, and PostgreSQL.  However, Apache Derby is not recommended for production usage.

Before you can configure the server to use the JDBC persistence layer implementation, you first need to prepare the database. This step depends on the database product in use and is covered in more detail in [Section 3.3.1.1 Supported databases](#3311-supported-databases).

The IBM FHIR Server is delivered with a default configuration that is already configured to use the JDBC persistence layer implementation with an Embedded Derby database. This provides the easiest out-of-the-box experience since it requires very little setup.

The IBM FHIR Server persistence configuration is split between `fhir-server-config.json` and the Liberty `server.xml` and `configDropins`.

Within `fhir-server-config.json`, the value of the `fhirServer/persistence/factoryClassname` is used to instantiate a FHIRPersistence object. By default, the server is configured to use the FHIRPersistenceJDBCFactory:

``` json
    {
        "fhirServer": {
            …
            "persistence": {
                "factoryClassname": "com.ibm.fhir.persistence.jdbc.FHIRPersistenceJDBCFactory",
                …
            }
    }
```

### 3.3.1 The JDBC persistence layer
When the FHIRPersistenceJDBCFactory is in use, the `fhirServer/persistence/datasources` property must specify a mapping from datastore-id values to Liberty datasource definitions. For example, here is the configuration for a datastore with id `default` that is configured for the `jdbc/fhir_default_default` datasource of type `postgresql`:

``` json
{
    "fhirServer":{
        "persistence":{
            "factoryClassname":"com.ibm.fhir.persistence.jdbc.FHIRPersistenceJDBCFactory",
            "datasources": {
                "default": {
                    "type": "postgresql",
                    "currentSchema": "fhirdata",
                    "jndiName": "jdbc/fhir_default_default"
                }
            }
        }
    }
}
```

Both the `type` and the `currentSchema` properties are required.
The `type` value must be set to one of the supported JDBC types (currently `db2`, `postgresql`, or `derby`) and it must match the actual database type referenced by the datasource definition. If the type does not match, the behavior is undefined.
Because the IBM FHIR Server does not quote the schema name in our generated queries, the `currentSchema` property is effectively case-insensitive.

The `jndiName` property is optional; it will default to `jdbc/fhir_<tenantId>_<dsId>` where `<tenantId>` and `<dsId>` represent the tenant and datastore ids respectively<sup id="a4">[4](#f4)</sup>.

The datasource definitions themselves are configured in accordance with the [Liberty documentation on Relational database connections with JDBC](https://openliberty.io/docs/latest/relational-database-connections-JDBC.html). Previous versions of the IBM FHIR Server supported a proxy datasource that allowed for datasource definitions in the fhir-server-config.json, but now Liberty JDBC datasource configuration is used due to benefits related to configuring pool sizes, transaction recovery, and monitoring.

For example, the fhir-server-config snippet from above would have a corresponding Liberty config like this:
```xml
<server>
    <!-- ============================================================== -->
    <!-- TENANT: default; DSID: default; TYPE: read-write               -->
    <!-- ============================================================== -->
    <dataSource id="fhirDefaultDefault" jndiName="jdbc/fhir_default_default" type="javax.sql.XADataSource" statementCacheSize="200" syncQueryTimeoutWithTransactionTimeout="true" validationTimeout="30s">
        <jdbcDriver javax.sql.XADataSource="org.postgresql.xa.PGXADataSource" libraryRef="sharedLibPostgres"/>
        <properties.postgresql
             serverName="postgres_postgres_1"
             portNumber="5432"
             databaseName="fhirdb"
             user="fhirserver"
             password="change-password"
             currentSchema="fhirdata"
         />
        <connectionManager maxPoolSize="200" minPoolSize="40"/>
    </dataSource>
</server>
```

Datasource elements should not be defined in the main `server.xml` file but instead should be defined in the `configDropins/overrides` directory. See the [Liberty Profile Server configuration guide](https://openliberty.io/docs/latest/reference/config/server-configuration-overview.html) for more details and general guidance on creating modular configurations.

The IBM FHIR Server is packaged with the following sample datasource definitions at `configDropins/disabled`:
* datasource-postgresql.xml
* datasource-db2.xml
* datasource-derby.xml

There are 3 libraries defined in the main `server.xml` that reference the required client drivers for each database type:
* sharedLibDerby
* sharedLibDb2
* sharedLibPostgres

When a datasource definition is included in a configDropin under `configDropins/overrides`, this file is picked up when the server starts as indicated by the following AUDIT message:

```
[AUDIT   ] CWWKG0093A: Processing configuration drop-ins resource: <WLP_HOME>/usr/servers/fhir-server/configDropins/overrides/datasource.xml
```

The IBM FHIR Server will look up the tenant and datastore id for each request and use the corresponding JNDI name to obtain a connection for the corresponding datasource.

#### 3.3.1.1 Supported databases
##### Embedded Derby (default)
If you are using the `ibmcom/ibm-fhir-server` docker image, you can ask the entrypoint script to create (bootstrap) the database and the schema during startup by setting the `BOOTSTRAP_DB` environment variable to `true`.

This database bootstrap step is only supported for Embedded Derby and will only bootstrap the default datastore of the default tenant (the default for requests with no tenant or datastore headers).

*Reminder*: the Embedded Derby support is designed to support simple getting started scenarios and is not recommended for production use.

``` xml
<server>
    <!-- ============================================================== -->
    <!-- This datasource aligns with the Apache Derby database that is  -->
    <!-- created by the IBM FHIR Server's DB_BOOTSTRAP process.         -->
    <!-- ============================================================== -->
    <dataSource id="fhirDefaultDefault" jndiName="jdbc/fhir_default_default" type="javax.sql.XADataSource" statementCacheSize="200" syncQueryTimeoutWithTransactionTimeout="true" validationTimeout="30s">
        <jdbcDriver javax.sql.XADataSource="org.apache.derby.jdbc.EmbeddedXADataSource" libraryRef="sharedLibDerby"/>
        <properties.derby.embedded createDatabase="create" databaseName="derby/fhirDB"/>
        <connectionManager maxPoolSize="50" minPoolSize="10"/>
    </dataSource>
</server>
```

##### Db2
If you configure the FHIR server to use an IBM Db2 database, you must:

1. Create the database if it doesn't already exist.

2. Execute the `fhir-persistence-schema` utility to create the necessary schemas (tables, indices, stored procedures, etc) and tenants.

3. Configure the IBM FHIR Server with the tenantKey generated in step number 2.

An executable `fhir-persistence-schema` jar can be downloaded from the project's [Releases tab](https://github.com/IBM/FHIR/releases) and documentation can be found at https://github.com/IBM/FHIR/tree/main/fhir-persistence-schema.

For a detailed guide on configuring IBM Db2 on Cloud for the IBM FHIR Server, see [DB2OnCloudSetup](https://ibm.github.io/FHIR/guides/DB2OnCloudSetup).

Since release 4.3.2 you can use the `search.reopt` query optimizer hint to improve the performance of certain search queries involving multiple search parameters. This optimization is currently only available for Db2. Valid values are "ALWAYS" and "ONCE". See Db2 documentation for `REOPT` for more details.

##### PostgreSQL
If you configure the FHIR server to use a PostgreSQL database, you must:

1. create the database if it doesn't already exist

2. execute the `fhir-persistence-schema` utility with a db-type of `postgresql` to create the necessary schemas (tables, indices, functions, etc)

An executable `fhir-persistence-schema` jar can be downloaded from the project's [Releases tab](https://github.com/IBM/FHIR/releases) and documentation can be found at https://github.com/IBM/FHIR/tree/main/fhir-persistence-schema.

Since release 4.5.5 you can set the `searchOptimizerOptions/from_collapse_limit` and `searchOptimizerOptions/join_collapse_limit` properties to improve the performance of certain search queries involving multiple search parameters. This optimization is currently only available for PostgreSQL.

##### Other

To enable the IBM FHIR Server to work with other relational database systems, see
https://ibm.github.io/FHIR/guides/BringYourOwnPersistence#adding-support-for-another-relational-database


#### 3.3.1.2 Datastore configuration examples
To understand how the configuration properties are defined for one or more datastores, let's start off with a couple of examples.

##### Example 1
Here is a simple example of a single (default) Derby datastore.

**config/default/fhir-server-config.json**
```json
{
    "fhirServer":{
        …
        "persistence":{
            "factoryClassname":"com.ibm.fhir.persistence.jdbc.FHIRPersistenceJDBCFactory",
            "datasources": {
                "default": {
                    "type": "derby",
                    "currentSchema": "APP"
                }
            }
        }
    }
}
```

**configDropins/overrides/datasource-derby.xml**
```xml
<server>
    <library id="fhirSharedLib">
        <fileset dir="${shared.resource.dir}/lib/derby" includes="*.jar"/>
    </library>

    <!-- ============================================================== -->
    <!-- TENANT: default; DSID: default; TYPE: read-write               -->
    <!-- ============================================================== -->
    <dataSource id="fhirDefaultDefault" jndiName="jdbc/fhir_default_default" type="javax.sql.XADataSource" statementCacheSize="200" syncQueryTimeoutWithTransactionTimeout="true" validationTimeout="30s">
        <jdbcDriver javax.sql.XADataSource="org.apache.derby.jdbc.EmbeddedXADataSource" libraryRef="sharedLibDerby"/>
        <properties.derby.embedded createDatabase="create" databaseName="derby/fhirDB"/>
        <connectionManager maxPoolSize="50" minPoolSize="10"/>
    </dataSource>
</server>
```

In this example, we define an embedded Derby database named `derby/fhirDB` (a location relative to the `<WLP_HOME>/usr/servers/fhir-server` directory). The datastore-id associated with this datastore is `default`, which is the value that is used if no `X-FHIR-DSID` request header is found in the incoming request. So, when only a single database is being used, it's wise to leverage the `default` datastore-id value to allow REST API consumers to avoid having to set the `X-FHIR-DSID` request header on each request.

##### Example 2
This example shows a slightly more complex scenario. In this scenario, the `acme` tenant would like to store data in one of two study-specific Db2 databases with datastore-id values `study1` and `study2`.

Furthermore, the REST API consumers associated with Acme applications will be coded to always set the `X-FHIR-TENANT-ID` request header to be `acme` and the `X-FHIR-DSID` request header to the specific datastore-id associated with each request (either `study1` or `study2`). In this case, the following properties would be configured:

**config/acme/fhir-server-config.json**
```json
{
    "__comment":"Acme's FHIR server configuration",
    "fhirServer":{
        …
        "persistence":{
            "factoryClassname":"com.ibm.fhir.persistence.jdbc.FHIRPersistenceJDBCFactory",
            "datasources": {
                "study1": {
                    "tenantKey": "<the-tenant-key>",
                    "type": "db2",
                    "currentSchema": "DB2INST1"
                },
                "study2": {
                    "tenantKey": "<the-tenant-key>",
                    "type": "db2",
                    "currentSchema": "DB2INST1"
                }
            }
        }
    }
}
```

**configDropins/overrides/datasources-acme.xml**
```xml
<server>
    <library id="fhirSharedLib">
        <fileset dir="${shared.resource.dir}/lib/db2" includes="*.jar"/>
    </library>

    <!-- ============================================================== -->
    <!-- TENANT: acme; DSID: study1; TYPE: read-write                   -->
    <!-- ============================================================== -->
    <dataSource id="fhirDefaultDefault" jndiName="jdbc/fhir_acme_study1" type="javax.sql.XADataSource" statementCacheSize="200" syncQueryTimeoutWithTransactionTimeout="true" validationTimeout="30s">
        <jdbcDriver javax.sql.XADataSource="com.ibm.db2.jcc.DB2XADataSource" libraryRef="sharedLibDb2"/>
        <properties.db2.jcc
            serverName="dbserver1"
            portNumber="50000"
            user="db2inst1"
            password="change-password"
            databaseName="ACMESTUDY1"
            currentSchema="DB2INST1"
            driverType="4"
         />
        <connectionManager maxPoolSize="200" minPoolSize="40"/>
    </dataSource>

    <!-- ============================================================== -->
    <!-- TENANT: acme; DSID: study2; TYPE: read-write                   -->
    <!-- ============================================================== -->
    <dataSource id="fhirDefaultDefault" jndiName="jdbc/fhir_acme_study2" type="javax.sql.XADataSource" statementCacheSize="200" syncQueryTimeoutWithTransactionTimeout="true" validationTimeout="30s">
        <jdbcDriver javax.sql.XADataSource="com.ibm.db2.jcc.DB2XADataSource" libraryRef="sharedLibDb2"/>
        <properties.db2.jcc
            serverName="dbserver1"
            portNumber="50000"
            user="db2inst1"
            password="change-password"
            databaseName="ACMESTUDY2"
            currentSchema="DB2INST1"
            driverType="4"
         />
        <connectionManager maxPoolSize="200" minPoolSize="40"/>
    </dataSource>
</server>
```

Note that because this example is using the default FHIR server naming scheme for datasource JNDI names, there is no need to include the 'jndiName' property in the fhir-server-config.json, although you can specify it should you wish to make the mapping clear.

Additionally, note that each datasource definition in the configDropin gets its own connection pool with properties defined by the 'connectionManager' element.

#### 3.3.1.3 Database Access TransactionManager Timeout
The TransactionManager controls the timeout of database queries.  

To modify the default transaction timeout value, set the environment variable `FHIR_TRANSACTION_MANAGER_TIMEOUT` or enter the value in the server.env file at the root of the WLP fhir-server instance. Example values are `120s` (seconds) or `2m` (minutes).

## 3.4 “Update/Create” feature
Normally, the _update_ operation is invoked with a FHIR resource which represents a new version of an existing resource. The resource specified in the _update_ operation would contain the same id of that existing resource. If a resource containing a non-existent id were specified in the _update_ invocation, an error would result.

The FHIR specification defines optional behavior for the _update_ operation where it can create a new resource if a non-existent resource is specified in the invocation. The FHIR server supports this optional behavior via the `fhirServer/persistence/common/updateCreateEnabled` configuration parameter. If this configuration parameter is set to `true` (the default), then the _update_ operation will create a new resource if it is invoked with a resource containing a non-existent id. If the option is set to false, then the optional behavior is disabled and an error would be returned.

The following example shows the JSON for enabling _update_ operations to create resources:
```
{
    "fhirServer":{
        …
        "persistence":{
            "common":{
                "updateCreateEnabled":true
            }
        }
        …
    }
}
```

# 4 Customization
You can modify the default server implementation by taking advantage of the IBM FHIR server's extensibility. The following extension points are available:
 * Custom operations framework:  The IBM FHIR Server defines an operations framework that builds on the FHIR OperationDefinition resource in order to extend the FHIR REST API with custom endpoints.
 * Pluggable audit service:  Logging and auditing options including Cloud Auditing Data Federation (CADF) over Apache Kafka.
 * Persistence interceptors:  Intercept requests before and/or after each persistence action.
 * Resource validation:  FHIRPath-based validation of FHIR resources on create or update with the ability to extend the system with custom constraints.

## 4.1 Extended operations
In addition to the standard REST API (create, update, search, and so forth), the IBM FHIR Server supports the FHIR operations framework as described in the [FHIR specification](https://www.hl7.org/fhir/r4/operations.html).

### 4.1.1 Packaged operations
The FHIR team provides implementations for the standard `$validate`, `$document`, `$everything`, `$expand`, `$lookup`, `$subsumes`, `$closure`, `$export`, `$import`, `$convert`, `$apply` and `$translate` operations, as well as a custom operation named `$healthcheck`, which queries the configured persistence layer to report its health.

The server also bundles `$reindex` to reindex instances of Resources so they are searchable, `$retrieve-index` to retrieve lists of resources available to be reindexed, and `$erase` to hard delete instances of Resources. To learn more about the $erase operation, read the [design document](https://github.com/IBM/FHIR/tree/main/operation/fhir-operation-erase/README.md).

To extend the server with additional operations, see [Section 4.1.2 Custom operations](#412-custom-operations)

#### 4.1.1.1 $validate
The `$validate` operation checks whether the attached content would be acceptable either generally, or as a create, update, or delete against an existing resource instance or type. By default, the `$validate` operation will validate a resource against the base specification and any profiles asserted in its `Resource.meta.profile` element. The default behavior may be changed in one of the following ways:
* If a profile is specified via the optional `profile` parameter, the $validate operation will validate a resource against the base specification and the specified profile only. It will not validate against any other profiles asserted in the `Resource.meta.profile` element.
* If the `profile` parameter is not specified, but the `mode` parameter is specified, and the `mode` parameter value is either `create` or `update`, the $validate operation will validate a resource against the base specification and any profiles asserted in its `Resource.meta.profile` element, but will do so based on profile configuration properties specified in the `fhirServer/resources/<resourceType>/profiles` section of the `fhir-server-config.json` file (see the [Configuration properties reference](#51-configuration-properties-reference) for configuration details).


https://www.hl7.org/fhir/r4/resource-operations.html#validate

#### 4.1.1.2 $document
The `$document` operation generates a fully bundled document from a composition resource.

https://www.hl7.org/fhir/r4/composition-operations.html#document

#### 4.1.1.3 $healthcheck
The `$healthcheck` operation returns the health of the FHIR server and its datastore. In the default JDBC persistence layer, this operation creates a connection to the configured database and return its status. The operations returns `200 OK` when healthy. Otherwise, it returns an HTTP error code and an `OperationOutcome` with one or more issues.

### 4.1.2 Custom operations
In addition to the provided operations, the FHIR server supports user-provided custom operations through a Java Service Provider Interface (SPI).

To contribute an operation:
1. Implement each operation as a Java class that extends `com.ibm.fhir.operation.AbstractOperation` from `fhir-operation.jar`. Ensure that your implementation returns an appropriate `OperationDefinition` in its `getDefinition()` method, because the framework validates both the request and response payloads to ensure that they conform to the definition.

2. Create a file named `com.ibm.fhir.operation.FHIROperation` with one or more fully qualified `FHIROperation` classnames and package it in your jar under `META-INF/services/`.

3. Include your jar file under the `<WLP_HOME>/usr/servers/fhir-server/userlib/` directory of your installation.

4. Restart the FHIR server. Changes to custom operations require a server restart, because the server discovers and instantiates operations during server startup only.

After you register your operation with the server, it is available via HTTP POST at `[base]/api/1/$<yourCode>`, where `<yourCode>` is the value of your OperationDefinition's [code](https://www.hl7.org/fhir/r4/operationdefinition-definitions.html#OperationDefinition.code).

## 4.2 Notification Service
The FHIR server provides a notification service that publishes notifications about persistence events, specifically _create_, _update_, and _delete_ operations. The notification service can be used by other Healthcare components to trigger specific actions that need to occur as resources are being updated in the FHIR server datastore.

The notification service supports two implementations: WebSocket and Kafka.

### 4.2.1 FHIRNotificationEvent
The `FHIRNotificationEvent` class defines the information that is published by the notification service. Each notification event published to the WebSocket or Kafka topic is an instance of `FHIRNotificationEvent`, serialized as a JSON object. This JSON object will have the following fields:

Field name     | Type   | Description
|--------------| -----  | ------------|
`operationType`| String | The operation associated with the notification event. Valid values are _create_ and _update_.
`location`     | String | The location URI of the resource associated with the notification event. To retrieve this resource, invoke a GET request using the location URI value as the URL string.
`lastUpdated`  | String | The date and time of the last update made to the resource associated with the notification event.
`resourceId`   | String | The logical id of the resource associated with the notification event.
`resource`     | String | A stringified JSON object which is the resource associated with the notification event.
`tenantId`     | String | The tenant that generated this notification
`datasourceId` | String | The datasource used by the tenant

The following JSON is an example of a serialized notification event:

```
{
  "lastUpdated":"2016-06-01T10:36:23.232-05:00",
  "location":"Observation/3859/_history/1",
  "operationType":"create",
  "resourceId":"3859",
  "tenantId":"default",
  "datasourceId":"default",
  "resource":{ …<contents of resource>… }
}
```

If the resource is over the limit specified in `fhirServer/notifications/common/maxNotificationSizeBytes`, the default value is to subset `id`, `meta`, `resourceType`, and all Resource required fields and add the subset to the FHIRNotificationEvent. In alternative configurations, user may set `fhirServer/notifications/common/maxNotificationSizeBehavior` to `omit` and subsequently retrieve the resource using the location.

### 4.2.2 WebSocket
The WebSocket implementation of the notification service will publish notification event messages to a WebSocket. To enable WebSocket notifications, set the `fhirServer/notifications/websocket/enabled` property to `true`, as in the following example:

```
{
    "fhirServer":{
        …
        "notifications":{
            …
            "websocket":{
                "enabled":true
            }
        …
    }
}
```

The WebSocket location URI is `ws://<host>:<port>/fhir-server/api/v4/notification`, where `<host>` and `<port>` represent the host and port of the FHIR server's REST API endpoint. So for example, if the FHIR server endpoint's base URL is `https://localhost:9443/fhir-server/api/v4` then the corresponding location of the WebSocket would be `ws://localhost:9443/fhir-server/api/v4/notification`.

### 4.2.3 Kafka
The Kafka implementation of the notification service will publish notification event messages to a Kafka topic. To configure the Kafka notification publisher, configure properties in the `fhir-server-config.json` file as indicated in the following example:

```
{
    "fhirServer":{
        …
        "notifications":{
            …
            "kafka":{
                "enabled":true,
                "topicName":"fhirNotifications",
                "connectionProperties":{
                    "group.id":"securing-kafka-group",
                    "bootstrap.servers":"localhost:9093",
                    "security.protocol":"SSL",
                    "ssl.truststore.location":"resources/security/kafka.client.truststore.jks",
                    "ssl.truststore.password":"change-password",
                    "ssl.keystore.location":"resources/security/kafka.client.keystore.jks",
                    "ssl.keystore.password":"change-password",
                    "ssl.key.password":"change-password",
                    "ssl.truststore.type":"JKS",
                    "ssl.keystore.type":"JKS"
                }
            }
        …
    }
}
```

The `fhirServer/notifications/kafka/enabled` property is used to enable or disable the Kafka publisher component.

The `fhirServer/notifications/kafka/topicName` property is used to configure the appropriate topic name to which the notification events should be published.

The `fhirServer/notifications/kafka/connectionProperties` property group is used to configure the properties necessary to successfully connect to the Kafka server. You can specify an arbitrary `group.id`. The `bootstrap.servers` property is required, but the rest are optional, although if your Kafka server is configured to require an SSL connection and client authentication, then the remaining properties must also be set. For more details about Kafka-related properties, see the Kafka documentation.

In the `connectionProperties` property group in preceding example, you'll notice that the password-related properties have encoded values. To store a value requiring security (such as a password), you can use Liberty's `securityUtility` command to encode the value. See [Section 3.1 Encoded passwords](#31-encoded-passwords) for details.

Before you enable Kafka notifications, it's important to understand the topology of the environment in which the FHIR server instance will be running. Your topic name selection should be done in consideration of the topology. If you have multiple instances of the FHIR server clustered together to form a single logical endpoint, then each of those instances should be configured to use the same Kafka topic for notifications. This is so that notification consumers (subscribers) can subscribe to a single topic and receive all the notifications published by each of the FHIR server instances within the cluster.

On the other hand, if you have two completely independent FHIR server instances, then you should configure each one with its own topic name.

The FHIRNotificationEvent is asynchronous by default. If you want to specify a synchronous request, you can set `fhirServer/notifications/kafka/sync` to true, which ensures no message is lost in publishing, however it does add latency in each request.

### 4.2.3 NATS
The [NATS](http://nats.io) implementation of the notification service publishes notification event messages to a NATS streaming cluster. To configure the NATS notification publisher, configure properties in the `fhir-server-config.json` file as shown in the following example:

```
{
    "fhirServer":{
        ...
        "notifications":{
            ...
            "nats": {
		        "enabled": true,
		        "cluster": "nats-streaming",
		        "channel": "fhirNotifications",
		        "clientId": "fhir-server",
		        "servers": "nats://nats-node1:4222,nats://nats-node2:4222,nats://nats-node3:4222",
		        "useTLS": true,
		        "truststoreLocation": "resources/security/nats.client.truststore.p12",
		        "truststorePassword": "change-password",
		        "keystoreLocation": "resources/security/nats.client.keystore.p12",
		        "keystorePassword": "change-password"
    }
        ...
    }
}
```

Set the `fhirServer/notifications/nats/enabled` property to true and provide the name of your NATS cluster for the value of `fhirServer/notifications/nats/cluster`.  You may leave `fhirServer/notifications/nats/channel` and `fhirServer/notifications/nats/clientId` as defined.  Provide the URL for one or more NATS servers as the value for `fhirServer/notifications/nats/servers`.

To use TLS to connect to your NATS cluster, set `fhirServer/notifications/nats/useTLS` to true and provide client truststore and keystore locations and passwords as the remaining config values. Ensure that your NATS cluster is configured for TLS client connections.

To store a value requiring security, such as a password, use Liberty's `securityUtility` command to encode the value. See Section 3.1 Encoded passwords for details.

### 4.2.4 Resource type filtering
By default, notification messages are published for all _create_ and _update_ persistence operations. However, the FHIR server allows you to configure a list of resource types for which notification events will be published. To do this, list the resource types for which you want to generate notifications in an array of strings within the  `fhirServer/notifications/common/includeResourceTypes` property in the `fhir-server-config.json` file, as in the following example:

```
{
    "fhirServer":{
        …
        "notifications":{
            …
            "common":{
                "includeResourceTypes":[
                     "Observation",
                     "Patient"
                ]
            },
        …
    }
}
```

With the `includeResourceTypes`property set as in the preceding example, the FHIR server publishes notification events only for `Patient` and `Observation` resources. If you omit this property or set its value to `[]` (an empty array), then the FHIR server publishes notifications for all resource types.

## 4.3 Persistence interceptors
The IBM FHIR Server supports a persistence interceptor feature that enables users to add their own logic to the REST API processing flow around persistence events. This can be used to enforce application-specific business rules associated with resources. Interceptor methods are called immediately before or after each persistence operation.

### 4.3.1 FHIRPersistenceInterceptor interface
A persistence interceptor implementation must implement the `com.ibm.fhir.persistence.interceptor.FHIRPersistenceInterceptor`
interface.

Each interceptor method receives a parameter of type `FHIRPersistenceEvent`, which contains context information related to the request being processed at the time that the interceptor method is invoked. It includes the FHIR resource, security information, request URI information, and the collection of HTTP headers associated with the request.

There are many use cases for persistence interceptors:

1.  Enforce certain application-specific governance rules, such as making sure that a patient has signed a consent form prior to allowing his/her data to be persisted. For example, the `beforeCreate` and `beforeUpdate` methods could verify that the patient has a consent agreement on file and, if not, then throw a `FHIRPersistenceInterceptorException` to prevent the _create_ or _update_ events from completing. The exception thrown by the interceptor method should include one or more OperationOutcome issues and these issues will be added to an `OperationOutcome` in the REST API response. The HTTP status code of the response will be determined by the IssueType of the first issue in the list.

2.  Perform additional access control. For example, `beforeSearch` can be used to alter the incoming SearchContext (e.g. by adding additional search parameters). Similarly `afterRead`, `afterVRead`, `afterHistory`, and `afterSearch` can be used to verify that the end user is authorized to access the resources before they are returned.

It is also possible to modify the incoming resources from the `beforeCreate` and `beforeUpdate` methods. For example, an interceptor could be used to add tags to resources on their way into the server. However, it is important to realize that interceptors are called *after* resource validation. Therefore, interceptor authors must be careful not to alter the resources in a way that breaks conformance with the profiles claimed in Resource.meta.profile or the secondary constraints in the specification. When in doubt, interceptors that modify the incoming resource can use the FHIRValidator to re-validate the resource(s) after they are altered.

### 4.3.2 Implementing a persistence interceptor
To implement a persistence interceptor, complete the following steps:

1.  Develop a Java class which implements the `FHIRPersistenceInterceptor` interface.
2.  Store the fully-qualified classname of your interceptor implementation class in a file called :

      `META-INF/services/com.ibm.fhir.persistence.interceptor.FHIRPersistenceInterceptor`

    Here's an example of the file contents:

    `com.ibm.mysolution.MyInterceptor`

3.  Copy your jar to the `<WLP_HOME>/usr/servers/fhir-server/userlib` directory so that it is accessible to the FHIR server via the classpath (the `server.xml` file contains a library element that defines this directory as a shared library).

4.  Re-start the FHIR server.

## 4.4 Registry resources
The IBM FHIR Server includes a dynamic registry of conformance resources.
The registry pre-packages all [FHIR Definitions from the specification](https://www.hl7.org/fhir/R4/downloads.html)
and uses a Java ServiceLoader to discover additional registry resource providers on the classpath.

The server consults this registry for:
* StructureDefinition, ValueSet, and CodeSystem resources for resource validation.
* SearchParameter and CompartmentDefinition resources for search.
* ValueSet and CodeSystem resources for terminology operations.
* And more.

One common technique for extending FHIR with a set of conformance resources is to build or reference an [Implementation Guide](https://www.hl7.org/fhir/implementationguide.html).

The IBM FHIR Server includes a [PackageRegistryResourceProvider](https://ibm.github.io/FHIR/guides/FHIRValidationGuide#making-profiles-available-to-the-fhir-registry-component-fhirregistry) for registering implementation guide resources.

Additionally, we [pre-package a number of popular implementation guides](https://ibm.github.io/FHIR/guides/FHIRValidationGuide#optional-profile-support) and make those available from both our GitHub Releases and [Maven Central](https://repo1.maven.org/maven2/com/ibm/fhir/).

Finally, the IBM FHIR Server includes a built-in ServerRegistryResourceProvider that can be used to bridge conformance resources from the tenant data store (uploaded through the REST API) to the registry.
This provider can be enabled/disabled via the `fhirServer/core/serverRegistryResourceProviderEnabled` property, but we recommend leaving it disabled for performance-intensive workloads.

## 4.5 Resource validation
As mentioned in the previous section, the IBM FHIR Server registry is consulted during resource validation.

### 4.5.1 HL7 spec-defined validation support
The FHIR specification provides a number of different validation resources including:

1.  XML Schemas
2.  ISO XML Schematron rules
3.  Structure Definitions / Profiles for standard resource types, data types and built-in value sets

The FHIR server validates incoming resources for create and update interactions using the resource definitions and their corresponding FHIRPath constraints. Additionally, the FHIR server provides the [$validate](#411-packaged-operations) operation that API consumers can use to POST resources and get validation feedback:
```
POST <base>/<resourceType>/$validate
```

### 4.5.2 Extension validation
FHIR resources can be extended. Each extension has a url and servers can use these urls to validate the contents of the extension.

The IBM FHIR Server performs extension validation by looking up the extension definition in its internal registry.
If an instance contains extensions that are unknown to the server, then the server will include a validation warning that indicates this extension could not be validated.

### 4.5.3 Profile validation
The IBM FHIR Server can be extended with custom profile validation. This allows one to apply validation rules on the basis of the `Resource.meta.profile` values included on the resource instance.

For example, you can configure a set of FHIRPath Constraints to run for resources that claim conformance to the `http://ibm.com/fhir/profile/partner` profile when you see an input resource like the following:
```
{
    "resourceType" : "Patient",
    "meta": {
        "profile": [ "http://ibm.com/fhir/profile/partner" ]
    },
    "name" : [ {
        "family" : [ "Doe" ],
        "given" : [ "John" ]
    } ],
    "telecom" : [ {
        "value" : "555-1234",
        "system": "phone",
        "use" : "home"
    } ],
    "birthDate" : "1950-08-15"
}
```

The following configuration parameters can be used to specify rules relating to the set of profiles that are specified in a resource's `meta.profile` element:
* `fhirServer/resources/<resourceType>/profiles/atLeastOne` - this configuration parameter is used to specify a set of profiles, at least one of which a resource must claim conformance to and be successfully validated against in order to be persisted to the FHIR server.
* `fhirServer/resources/<resourceType>/profiles/notAllowed` - this configuration parameter is used to specify a set of profiles to which a resource is *not allowed* to claim conformance.
* `fhirServer/resources/<resourceType>/profiles/allowUnknown` - this configuration parameter is used to indicate whether a warning or an error is issued if a profile specified in a resource's `meta.profile` element is not loaded in the FHIR server. The default value is `true`, meaning unknown profiles are allowed to be specified. The profile will be ignored and just a warning will be returned. If set to `false`, this means unknown profiles are not allowed to be specified. An error will be returned and resource validation will fail.
* `fhirServer/resources/<resourceType>/profiles/defaultVersions` - this configuration parameter is used to specify which version of a profile will be used during resource validation if the profile specified in a resource's `meta.profile` element does not contain a version. If a default profile version is not configured using this configuration parameter for an asserted profile, the FHIR server will determine the default version to use for validation.

Before calling the FHIR validator to validate a resource against the set of profiles specified in its `meta.profile` element that it is claiming conformance to, the following pre-validation will be performed for that set of profiles based on the configuration parameters listed above:
1. If any specified profile does not contain a version, and that profile is in the set of profiles configured to have a default version via the `fhirServer/resources/<resourceType>/profiles/defaultVersions` configuration parameter, the default version for that profile will be appended to the profile name, and it is this new profile name containing the version which will be evaluated against in the following steps.
2. If the `fhirServer/resources/<resourceType>/profiles/notAllowed` configuration parameter is set to a non-empty list, an error will be returned for any specified profile that is in the list, and validation will fail.
3. If the `fhirServer/resources/<resourceType>/profiles/allowUnknown` configuration parameter is set to `false`,  an error will be returned for any specified profile that is not loaded in the FHIR server, and validation will fail.
4. If the `fhirServer/resources/<resourceType>/profiles/atLeastOne` configuration parameter is set to a non-empty list, an error will be returned if none of the specified profiles is in the list, and validation will fail.

If the resource passes pre-validation successfully, it will then be passed to the FHIR validator to validate conformance to the specified set of profiles.

The following example configuration shows how to define these configuration parameters:
```
{
    "fhirServer":{
        …
        "resources":{
            "open": true,
            "Observation": {
                "profiles": {
                    "atLeastOne": [
                        "http://ibm.com/fhir/profile/partnerA",
                        "http://ibm.com/fhir/profile/partnerB|1.0"
                    ],
                    "allowUnknown": false,
                    "defaultVersions": {
                        "http://ibm.com/fhir/profile/partnerA": "1.1",
                        "http://ibm.com/fhir/profile/partnerE": "2.0"
                    }
                }
            },
            "Patient": {
                "profiles": {
                    "notAllowed": [
                        "http://ibm.com/fhir/profile/partnerC",
                        "http://ibm.com/fhir/profile/partnerD|2.0"
                    ]
                }
            },
            "Resource": {
                "profiles": {
                    "notAllowed": [
                        "http://ibm.com/fhir/profile/partnerF",
                        "http://ibm.com/fhir/profile/partnerG|4.0.0",
                        "http://ibm.com/fhir/profile/partnerH"
                    ],
                    "defaultVersions": {
                        "http://ibm.com/fhir/profile/partnerI": "1.0",
                        "http://ibm.com/fhir/profile/partnerJ": "3.1.1",
                        "http://ibm.com/fhir/profile/partnerK": "1.1"
                    }
                }
            }
        },
        …
    }
}
```

Given this configuration, in order for an `Observation` resource to be persisted to the FHIR server:
* The resource's `meta.profile` element must specify either the `http://ibm.com/fhir/profile/partnerA` profile or the `http://ibm.com/fhir/profile/partnerB|1.0` profile or both, based on the `fhirServer/resources/Observation/profiles/atLeastOne` list. Other profiles may be specified as well assuming they pass the following checks.
* The resource's `meta.profile` element must not specify any profile which is specified in the `fhirServer/resources/Resource/profiles/notAllowed` list. Since the `notAllowed` property is not specified in the `fhirServer/resources/Observation/profiles` section, it is inherited from the `fhirServer/resources/Resource/profiles` section if specified there.
* The resource's `meta.profile` element must not specify any profile which is not loaded in the FHIR server, based on the `fhirServer/resources/Observation/profiles/allowUnknown` value of `false`.
* The resource must successfully validate against all specified profiles. Note that if the `http://ibm.com/fhir/profile/partnerA` profile is specified, what is actually evaluated against and eventually passed to the FHIR validator is `http://ibm.com/fhir/profile/partnerA|1.1`. This is because the `http://ibm.com/fhir/profile/partnerA` profile is specified in the `fhirServer/resources/Observation/profiles/defaultVersions` set with a default version of `1.1`.

In order for a `Patient` resource to be persisted to the FHIR server:
* The resource's `meta.profile` element is not required to specify any profile since the `atLeastOne` property is not specified in the `fhirServer/resources/Patient/profiles` section, nor is it specified in the `fhirServer/resources/Resource/profiles` section, where the property would be inherited from if specified. Any valid `Patient` profile may be specified assuming it passes the following checks.
* The resource's `meta.profile` element cannot specify either the `http://ibm.com/fhir/profile/partnerC` profile or the `http://ibm.com/fhir/profile/partnerD|2.0` profile, based on the `fhirServer/resources/Patient/profiles/notAllowed` list.
* The resource's `meta.profile` element may specify a profile which is not loaded in the FHIR server. Based on the absence of the `allowUnknown` property in the `fhirServer/resources/Patient/profiles` section, as well as the absence of that property in the `fhirServer/resources/Resource/profiles` section (where the property would be inherited from if specified), the default value of `true` is used. This means unknown profiles (not loaded in the FHIR server) will be allowed and will simply be ignored.
* The resource must successfully validate against all specified profiles. Note that since the `defaultVersions` property is not specified in the `fhirServer/resources/Patient/profiles` section, this property will be inherited from the `fhirServer/resources/Resource/profiles/defaultVersions` property. So if a profile is specified in the resource's `meta.profile` element that is in the set of `defaultVersions` profiles, what will actually be evaluated against and eventually passed to the FHIR validator is the original profile name with its specified default version appended to it.

If a profile in either the list specified by the `fhirServer/resources/<resourceType>/profiles/atLeastOne` configuration parameter or the list specified by the `fhirServer/resources/<resourceType>/profiles/notAllowed` configuration parameter contains a version, for example `http://ibm.com/fhir/profile/partner|1.0`, then a profile of the same name specified in the resource's `meta.profile` element will only be considered a match if it contains exactly the same version. However, if a profile in the lists specified by the configuration parameters does not contain a version, for example `http://ibm.com/fhir/profile/partner`, then a profile of the same name specified in the resource's `meta.profile` element will be considered a match whether it contains a version or not.

Keep in mind that a profile name specified in the resource's `meta.profile` element could be modified due to the resource's `fhirServer/resources/<resourceType>/profiles/defaultVersions` configuration. It is this modified profile name that is used in the matching process and in the resource's validation, but only for those purposes. The `meta.profile` element of the original resource itself is not updated with the modified profile name.

Using the example configuration above for the `Observation` resource type, if the profile `http://ibm.com/fhir/profile/partnerA|3.2` was specified in a resource's `meta.profile` element then it would be considered a match for the `http://ibm.com/fhir/profile/partnerA` profile specified in the `fhirServer/resources/Observation/profiles/atLeastOne` list. Conversely, if the profile `http://ibm.com/fhir/profile/partnerB` was specified in the resource's `meta.profile` element then it would *not* be considered a match for the `http://ibm.com/fhir/profile/partnerB|1.0` profile specified in the `fhirServer/resources/Observation/profiles/atLeastOne` list.

The IBM FHIR Server pre-packages all conformance resources from the core specification.

See [Validation Guide - Optional profile support](https://ibm.github.io/FHIR/guides/FHIRValidationGuide#optional-profile-support) for a list of pre-built Implementation Guide resources and how to load them into the IBM FHIR server.

See [Validation Guide - Making profiles available to the fhir registry](https://ibm.github.io/FHIR/guides/FHIRValidationGuide#making-profiles-available-to-the-fhir-registry-component-fhirregistry) for information about how to extend the server with additional Implementation Guide artifacts.


## 4.6 Extending search
In addition to supporting tenant-specific search parameter extensions as described in [Section 3.2.2 Tenant-specific configuration properties](#322-tenant-specific-configuration-properties), the IBM FHIR Server also supports loading
search parameters from the registry.

For performance reasons, the registry search parameters are retrieved once and only once during startup.

The set of search parameters can filtered / refined via `fhirServer/resources/[resourceType]/searchParameters` as described in the [Search configuration guide](https://ibm.github.io/FHIR/guides/FHIRSearchConfiguration#13-filtering).

## 4.7 FHIR client API

### 4.7.1 Overview
In addition to the server, we also offer a Java API for invoking the FHIR REST APIs. The IBM FHIR Client API is based on the JAX-RS 2.0 standard and provides a simple properties-driven client that can be easily configured for a given endpoint, mutual authentication, request/response logging, and more.

### 4.7.2 Maven coordinates
To use the FHIR Client from your application, specify the `fhir-client` artifact as a dependency within your `pom.xml` file, as in the following example:

```
        <dependency>
            <groupId>com.ibm.fhir</groupId>
            <artifactId>fhir-client</artifactId>
            <version>${fhir.client.version}</version>
        </dependency>
```

### 4.7.3 Sample usage
For examples on how to use the IBM FHIR Client, look for tests like `com.ibm.fhir.client.test.mains.FHIRClientSample` from the `fhir-client` project in git. Additionally, the FHIR Client is heavilly used from our integration tests in `fhir-server-test`.

## 4.8 Using local references within request bundles
Inter-dependencies between resources are typically defined by one resource containing a field of type `Reference` which contains an _external reference_<sup id="a5">[5](#f5)</sup> to another resource. For example, an `Observation` resource could reference a `Patient` resource via the Observation's `subject` field. The value that is stored in the `Reference.reference` field (for example, `Observation.subject.reference` in the case of the `Observation` resource) could be an absolute URL, such as `https://fhirserver1:9443/fhir-server/api/v4/Patient/12345`, or a relative URL, such as `Patient/12345`.

As described above, in order to establish a reference to a resource, you must first know its resource identifier. However, if you are using a request bundle to create both the referenced resource (`Patient` in this example) and the resource which references it (`Observation`), then it is impossible to know the `Patient`resource identifier before the request bundle has been processed (that is, before the new `Patient` resource is created).
 
Thankfully, the HL7 FHIR specification defines a way to express a dependency between two resources within a request bundle by using a _local identifier_ to identify the resource being referenced, and a _local reference_<sup id="a6">[6](#f6)</sup> to reference the resource via its local identifier. In the following example, a request bundle contains a `POST` request to create a new `Patient` resource, along with a `POST` request to create a new `Observation` resource that references that `Patient`:

<a id="example-obs-ref-pat"></a>

```
{
    "resourceType": "Bundle",
    "type": "batch",
    "entry" : [ {
        "fullUrl" : "urn:uuid:7113a0bb-d9e0-49df-9855-887409388c69",
        "resource" : {
            "resourceType" : "Patient",
            …
        },
        "request" : {
            "method" : "POST",
            "url" : "Patient"
        }
    }, {
        "resource" : {
            "resourceType" : "Observation",
            …
            "subject" : {
                "reference" : "urn:uuid:7113a0bb-d9e0-49df-9855-887409388c69"
            },
            …
        },
        "request" : {
            "method" : "POST",
            "url" : "Observation"
        }
    } ]
}
```

To define a local identifier for a resource, you must specify it in the `Bundle.entry.fullUrl` field of the resource's bundle entry. The HL7 FHIR specification recommends that the local identifier be the persistent identity of the resource if known, otherwise a UUID value prefixed with `urn:uuid:` as in the preceding example. However, the FHIR server does not require the use of a UUID value. You can use any `fullUrl` value you like as long as it is unique within the bundle. For example, you can use `urn:Patient_1`, `urn:ABC`, or `urn:Foo`.

After you define a local identifier for the referenced resource, you can then define one or more references to that resource by using the local identifier instead of an external identifier. In the preceding example, you can see that the Observation's `subject.reference` field specifies the Patient's local identifier as specified in the `fullUrl` field of the Patient's request entry.

### 4.8.1 Processing rules
The following processing rules apply for the use of local references within a request bundle:
1.  A local identifier must be defined via a request bundle entry's `fullUrl` field in order for that local identifier to be used in a local reference.
2.  Local references will only be recognized for local identifiers associated with request bundle entries with a request method of `POST` or `PUT`.
3.  `POST` requests will be processed before `PUT` requests.
4.  <a id="order-dependency-rule"></a>There is no order dependency within a request bundle for bundle entries defining local identifiers and bundle entries which reference those local identifiers via local references.
5.  <a id="relative-reference-rule"></a>If a resource in a request bundle entry contains a field of type `Reference` having a value which is a relative URL, and if that bundle entry has a local identifier (`fullUrl`) which is an absolute URL conforming to the FHIR specification's [RESTful URL definition](https://www.hl7.org/fhir/references.html#regex), then the FHIR server will attempt to resolve the local reference as follows, based on the [FHIR specification](https://www.hl7.org/fhir/bundle.html#references):
    - it will extract the FHIR base URL from the local identifier and append the local reference to it
    - it will then try to resolve the reference within the request bundle using the updated local reference

    See the [relative local reference example](#example-ref-via-relative-local-ref) below for an illustration of this rule. 

In the [example above](#example-obs-ref-pat), you can see that there are two POST requests and the `Patient` request entry appears in the bundle before the `Observation` request entry. However, based on the [order dependency processing rule](#order-dependency-rule), it would still be a valid request bundle even if the `Observation` request entry appeared before the `Patient` request entry.

The following examples also satisfy the local reference processing rules:

#### Example: Circular references
```
{
    "resourceType" : "Bundle",
    "type" : "batch",
    "entry" : [ {
        "fullUrl" : "urn:Encounter_1",
        "resource" : {
            "resourceType" : "Encounter",
            …
            "reasonReference" : [ {
                    "reference" : "urn:Procedure_1"
            } ],
            …
        },
        "request" : {
            "method" : "POST",
            "url" : "Encounter"
        }
    }, {
        "fullUrl" : "urn:Procedure_1",
        "resource" : {
            "resourceType" : "Procedure",
            …
            "encounter" : {
                    "reference" : "urn:Encounter_1"
            },
            …
        },
        "request" : {
            "method" : "POST",
            "url" : "Procedure"
        }
    } ]
}
```

While processing a request bundle, but before processing individual request entries, the IBM FHIR server detects the use of a local identifier within any `POST` or `PUT` request entry's `fullUrl` field, and establishes a mapping between that local identifier and the corresponding external identifier that results from performing the `POST` or `PUT` operation.

Using the circular reference example above, the FHIR server detects the use of local identifiers in the `Encounter` request entry (`urn:Encounter_1`) and in the `Procedure` request entry (`urn:Procedure_1`), and establishes a mapping between the local identifiers and the external references to be associated with the new `Encounter` and `Procedure` resources (for example, `Encounter/1cc5d299-d2be-4f93-8745-a121232ffe5b` and `Procedure/22b21fcf-8d00-492d-9de0-e25ddd409eaf`).

Then when the FHIR server processes the POST requests for the `Encounter` and `Procedure` resources, it detects the use of the local references and substitutes the corresponding external references for them before creating the new resources. Below is the response bundle for the request bundle in the circular references example. We can see that the Encounter's `reasonReference.reference` field now contains a proper external reference to the newly-created `Procedure` resource, and the Procedure's `encounter.reference` field now contains a proper external reference to the newly-created `Encounter` resource:

```
{
    "resourceType" : "Bundle",
    "type" : "batch-response",
    "entry" : [ {
        "resource" : {
            "resourceType" : "Encounter",
            "id" : "1cc5d299-d2be-4f93-8745-a121232ffe5b",
            …
            "reasonReference" : [ {
                    "reference" : "Procedure/22b21fcf-8d00-492d-9de0-e25ddd409eaf"
            } ],
            …
        },
        "response" : {
            "id" : "1cc5d299-d2be-4f93-8745-a121232ffe5b",
            "status" : "201",
            "location" : "Encounter/1cc5d299-d2be-4f93-8745-a121232ffe5b/_history/1",
            "etag" : "W/\"1\"",
            "lastModified" : "2017-03-01T20:56:59.540Z"
        }
    }, {
        "resource" : {
            "resourceType" : "Procedure",
            "id" : "22b21fcf-8d00-492d-9de0-e25ddd409eaf",
            …
            "encounter" : {
                "reference" : "Encounter/1cc5d299-d2be-4f93-8745-a121232ffe5b"
            },
            …
        },
        "response" : {
            "id" : "22b21fcf-8d00-492d-9de0-e25ddd409eaf",
            "status" : "201",
            "location" : "Procedure/22b21fcf-8d00-492d-9de0-e25ddd409eaf/_history/1",
            "etag" : "W/\"1\"",
            "lastModified" : "2017-03-01T20:56:59.652Z"
        }
    } ]
}
```

#### Example: Reference to entry with conditional create
```
{
    "resourceType" : "Bundle",
    "type" : "batch",
    "entry" : [ {
        "resource" : {
            "resourceType" : "Observation",
            "id" : "25b1fe08-7612-45eb-af80-7e15d9806b2b",
            …
            "subject" : {
                    "reference" : "urn:Patient_1"
            },
            …
        },
        "request" : {
            "method" : "PUT",
            "url" : "Observation/25b1fe08-7612-45eb-af80-7e15d9806b2b"
        }
    }, {
        "fullUrl" : "urn:Patient_1",
        "resource" : {
            "resourceType" : "Patient",
            …
        },
        "request" : {
            "method" : "POST",
            "url" : "Patient",
            "ifNoneExist": "identifier=http://my-system|123"
        }
    } ]
}
```

In the above example, even though the `Patient` request entry is a conditional create request, this is still a valid request bundle, because the FHIR server resolves any conditional requests before it establishes the mapping between local identifiers and the corresponding external identifiers that will result from performing the `POST` or `PUT` operation. 

#### <a id="example-ref-via-relative-local-ref"></a>Example: Reference via relative local reference
```
{
    "resourceType" : "Bundle",
    "type" : "batch",
    "entry" : [ {
        "fullUrl" : "https://fhirserver1:9443/fhir-server/api/v4/Patient/new",
        "resource" : {
            "resourceType" : "Patient",
            …
        },
        "request" : {
            "method" : "POST",
            "url" : "Patient"
        }
    }, {
        "fullUrl" : "https://fhirserver1:9443/fhir-server/api/v4/Observation/new",
        "resource" : {
            "resourceType" : "Observation",
            …
            "subject" : {
                "reference" : "Patient/new"
            },
            …
        },
        "request" : {
            "method" : "POST",
            "url" : "Observation"
        }
    } ]
}
```

In this example, we demonstrate the [relative reference processing rule](#relative-reference-rule). The local identifiers for the request bundle entries (`https://fhirserver1:9443/fhir-server/api/v4/Patient/new` and `https://fhirserver1:9443/fhir-server/api/v4/Observation/new`) are absolute URLs that conform to the FHIR specification's [RESTful URL definition](https://www.hl7.org/fhir/references.html#regex) . When the FHIR server attempts to resolve the `Observation` request entry's `subject` reference (`Patient/new`), it will apply the processing rule for relative references, since the reference is a relative URL. The `subject` reference will be modified to be the original local reference (`Patient/new`) appended to the FHIR base URL extracted from the `Observation` entry's local identifier (`https://fhirserver1:9443/fhir-server/api/v4/`). The resulting local reference  (`https://fhirserver1:9443/fhir-server/api/v4/Patient/new`) will then be a valid reference  to the `Patient` request bundle entry.

## 4.9 Multi-tenancy
The FHIR server includes features that allow a single instance of the server to simultaneously support multiple tenants. A tenant is defined as a group of one or more FHIR REST API consumers that share a FHIR server configuration along with one or more data stores associated with that configuration. A tenant could be a single application using the FHIR REST API, or it could be a group of applications belonging to a single customer. The main idea behind multi-tenancy is that each tenant can experience its own customized FHIR server runtime behavior and its data can be physically isolated from other tenants' data for increased security and privacy.

### 4.9.1 Specifying the tenant id
To support multi-tenancy, the FHIR server must know which tenant an incoming REST API request is intended for. To provide the tenant id to the FHIR server, a REST API consumer must set a request header in each REST API request. The name of this request header is itself configurable by setting the `fhirServer/core/tenantIdHeaderName` configuration property in the FHIR server's global configuration file (located at `$⁠{server.config.dir}/config/default/fhir-server-config.json`). The following example shows the default setting for this configuration parameter:
```

{
  "fhirServer":{
      "core":{
            "tenantIdHeaderName":"X-FHIR-TENANT-ID"
      },
      …
   }
}
```

With this configuration in place, each REST API consumer would need to set the `X-FHIR-TENANT-ID` request header to the appropriate tenant id. Each tenant's tenant id value is assigned by the deployer. It is simply a short name associated with the tenant and must be unique among all tenants supported by a single FHIR server instance. For example, let's suppose Acme Healthcare, Inc. is using the FHIR server and their tenant id has been assigned by the deployer as “acme”. In this case, Acme-related applications would set the following request header in each REST API request: `X-FHIR-TENANT-ID: acme`

As mentioned earlier, the name of the request header is configurable in the FHIR server's global configuration file. For example, the FHIR server deployer could configure the request header name to be `X-WHCLSF-tenant-id` by setting `tenantIdHeaderName` as shown in the following example:
```
{
   "fhirServer":{
      "core":{
            "tenantIdHeaderName":"X-WHCLSF-tenant-id"
      },
      …
   }
}
```

This would be useful in an environment where the applications might already be using a similar type of request header. With this configuration in place, the “Acme Healthcare, Inc.” tenant would set the following request header in each REST API request:
    `X-WHCLSF-tenant-id: acme`


### 4.9.2 Configuration properties
The FHIR server allows a deployer to configure a subset of the supported configuration properties on a tenant-specific basis.
For a complete list of configuration properties supported on a per-tenant basis, see [Section 5.1.3 Property attributes](#513-property-attributes).

When the FHIR server needs to retrieve any of the tenant-specific configuration properties, it does so dynamically each time the property value is needed. This means that a deployer can change the value of a tenant-specific property within a tenant's configuration file on disk, and the FHIR server will immediately “see” the new value the next time it tries to retrieve it. For example, suppose the deployer initially defines the `acme` tenant's `fhir-server-config.json` file such that the `fhirServer/core/defaultPrettyPrint` property is set to true.

Requests from the `acme` tenant would result in pretty-printed responses (with newlines and indentation), making it easier for humans to read.
Now suppose the deployer changes the value of that property to true within the `acme` tenant's `fhir-server-config.json` file.
A subsequent REST API request would then see the output condensed into a single line with minimal whitespace.

#### 4.9.2.1 Examples
This section contains examples of both a global (default) configuration and a tenant-specific configuration.

##### Global configuration (default)
The global configuration contains non-tenant specific configuration parameters (configuration parameters that are not resolved or used on a tenant-specific basis), as well as default values for tenant-specific configuration parameters.

`${server.config.dir}/config/default/fhir-server-config.json`

``` json
{
    "__comment":"FHIR server global (default) configuration",
    "fhirServer":{
        "core":{
            "defaultPrettyPrint":false,
            "tenantIdHeaderName":"X-FHIR-TENANT-ID"
        },
        "notifications":{
            "common":{
                "includeResourceTypes":[
                    "QuestionnaireResponse",
                    "CarePlan",
                    "MedicationAdministration",
                    "Device",
                    "DeviceComponent",
                    "DeviceMetric",
                    "MedicationOrder",
                    "Observation"
                ]
            },
            "websocket":{
                "enabled":false
            },
            "kafka":{
                "enabled":false,
                "topicName":"fhirNotifications",
                "connectionProperties":{
                }
            }
        },
        "audit": {
            "serviceClassName" : "com.ibm.fhir.audit.impl.NopService",
            "serviceProperties" : {
            }
        },
        "persistence":{
            "factoryClassname":"com.ibm.fhir.persistence.jdbc.FHIRPersistenceJDBCFactory",
            "common":{
                "__comment":"Configuration properties common to all persistence layer implementations",
                "updateCreateEnabled":true
            },
            "jdbc":{
                "__comment":"Configuration properties for the JDBC persistence implementation",
            },
        …
        }
    …
    }
```

##### Acme Healthcare, Inc. (acme)
The Acme Healthcare, Inc tenant configuration overrides the `fhirServer/core/defaultPrettyPrint` property so that the development
team can more easilly read the messages.

`${server.config.dir}/config/acme/fhir-server-config.json`

```
{
    "__comment":"FHIR server configuration for tenant: Acme Healthcare, Inc.",
    "fhirServer":{
        "core":{
            "defaultPrettyPrint":true
        }
    }
}
```

It is also possible to configure the persistence properties for a specific tenant, for example to set an alternate
database hostname or database schema name.

## 4.10 Bulk data operations
The IBM FHIR Server implements bulk data export according to the [HL7 FHIR BulkDataAccess IG: STU1](http://hl7.org/fhir/uv/bulkdata/STU1/export/index.html), and bulk data import is implemented according to the [Proposal for $import Operation](https://github.com/smart-on-fhir/bulk-import/blob/main/import.md).

There are 2 modules involved:

- fhir-operation-bulkdata
- fhir-bulkdata-webapp   

The *fhir-operation-bulkdata* module implements the REST APIs for bulk data export, import and status as FHIR Operations.  There are five operations:

| Operation | Path |
|-----|-----|
| ExportOperation| system `$export` |
| PatientExportOperation| Patient `Patient/$export` |
| GroupExportOperation| Group `Group/[id]/$export` |
| ImportOperation | import resources using the system endpoint, `$import` |
| StatusOperation | polling status for import and export `$bulkdata-status` |

Each operation queues a job with the Open Liberty JavaBatch framework. Each job instance unit-of-work is executed as a job with the *fhir-bulkdata-webapp* module. There are 5 JavaBatch jobs defined in the *fhir-bulkdata-webapp* module for the above 3 export operations and 1 import operation:

| Java Batch Job | Operation |
|-----|-----|
| FhirBulkExportChunkJob| `$export` |
| FhirBulkExportFastJob| `$export` |
| FhirBulkExportPatientChunkJob| `Patient/$export` |
| FhirBulkExportGroupChunkJob| `Group/[id]/$export` |
| FhirBulkImportChunkJob | `$import` |

The *fhir-bulkdata-webapp* module is a wrapper for the whole BulkData web application, which is the build artifact - fhir-bulkdata-webapp.war. This web archive is copied to the `apps/` directory of the liberty server. The feature is configured using the `configDropins/default/bulkdata.xml`, such as:

```xml
<webApplication id="fhir-bulkdata-webapp" location="fhir-bulkdata-webapp.war" name="fhir-bulkdata-webapp">
    <classloader privateLibraryRef="configResources,fhirUserLib"/>
    <application-bnd>
        <security-role id="users" name="FHIRUsers">
            <group id="bulkUsersGroup" name="FHIRUsers"/>
        </security-role>
    </application-bnd>
</webApplication>
```

The Bulk Data web application writes the exported FHIR resources to an IBM Cloud Object Storage (COS), any Amazon S3-Compatible bucket (e.g, Amazon S3, minIO etc), or directory as configured in the per-tenant server configuration under `fhirServer/bulkdata`. The following is an example configuration for bulkdata; please refer to section 5 for the detailed description of these properties:

``` json
"bulkdata": {
    "enabled": true,
    "core": {
        "api": {
            "url": "https://localhost:9443/ibm/api/batch",
            "user": "fhiradmin",
            "password": "change-password",
            "truststore": "resources/security/fhirTrustStore.p12",
            "truststorePassword": "change-password"
        },
        "cos" : {
            "useServerTruststore": true
        },
        "pageSize": 100,
        "batchIdEncryptionKey": "example-password",
        "maxPartitions": 3,
        "maxInputs": 5
    },
    "storageProviders": {
        "default" : {
            "type": "file",
            "fileBase": "/output/bulkdata",
            "disableOperationOutcomes": true,
            "duplicationCheck": false,
            "validateResources": false
        },
        "minio" : {
            "type": "aws-s3",
            "bucketName": "fhirbulkdata",
            "location": "us",
            "endpointInternal": "https://minio:9000",
            "endpointExternal": "https://localhost:9000",
            "auth" : {
                "type": "hmac",
                "accessKeyId": "example",
                "secretAccessKey": "example-password"
            },
            "disableBaseUrlValidation": true,
            "disableOperationOutcomes": true,
            "duplicationCheck": false,
            "validateResources": false,
            "create": false,
            "presigned": true
        }
    }
}
```

Each tenant's configuration may define multiple storageProviders. The default is assumed, unless specified with the `X-FHIR-BULKDATA-PROVIDER` and `X-FHIR-BULKDATA-PROVIDER-OUTCOME`. Each tenant's configuration may mix the different providers, however each provider is only of a single type. For instance, `minio` is `aws-s3` and `default` is `file`. Note, type `http` is only applicable to `$import` operations. Export is only supported with s3 and file.

To use Amazon S3 bucket for exporting, please set `accessKeyId` to S3 access key, and set `secretAccessKey` to the S3 secret key, and the auth type to `hmac`.

Basic system exports to S3 without typeFilters use a streamlined implementation which bypasses the IBM FHIR Server Search API for direct access to the data enabling better throughput. The `fhirServer/bulkdata/core/systemExportImpl` property can be used to disable the streamlined system export implementation. To use the legacy implementation based on IBM FHIR Server search, set the value to "legacy". The new system export implementation is used by default for any export not using typeFilters. Exports using typeFilters use FHIR Search, and cannot use the streamlined export.

To import using the `$import` operation with `https`, one must additionally configure the `fhirServer/bulkdata/storageProviders/(source)/validBaseUrls`. For example, if one stores bulk data at `https://test-url1.cos.ibm.com/bucket1/test.ndjson` and `https://test-url2.cos.ibm.com/bucket2/test2.ndjson` you must specify both baseUrls in the configuration:

```json
    "validBaseUrls": [
        "https://test-url1.cos.ibm.com/bucket1",
        "https://test-url2.cos.ibm.com/bucket2"
    ]
```

These base urls are not checked when using cloud object store and bulk-import. If you need to disable the validBaseUrls feature you may add `fhirServer/bulkdata/storageProviders/(source)/disableBaseUrlValidation` as `true`.

For Bulk Data import, the `fhirServer/bulkdata/core/maxInputs` is used to configure a maximum number of inputs supported by the instance. The default number is 5. There is a hard character limit on the total input type and url must be under 4096 characters, as such the configuration may be tuned for each url scheme.

Note: When `$import` is executed, if a resource to import includes a `Resource.id` then this id is honored (via create-on-update). If `Resource.id` is not valued, the server will perform a create and assign a new `Resource.id` for this resource.

Following is the beautified response of sample polling location request after the export is finished:

```json
{
"transactionTime": "2020/01/20 16:53:41.160 -0500",
"request": "https://myserver/fhir-server/api/v4/$export?_type=Patient",
"requiresAccessToken": false,
"output" : [
  { "type" : "AllergyIntolerance",
      "url": "https://s3.us-south.cloud-object-storage.appdomain.cloud/fhir-bulkimexport-connectathon/6SfXzbGvYl1nTjGbf5qeqJDFNjyljiGdKxXEJb4yJn8=/AllergyIntolerance_1.ndjson",
    "count": 20},
  { "type" : "AllergyIntolerance",
      "url": "https://s3.us-south.cloud-object-storage.appdomain.cloud/fhir-bulkimexport-connectathon/6SfXzbGvYl1nTjGbf5qeqJDFNjyljiGdKxXEJb4yJn8=/AllergyIntolerance_2.ndjson",
    "count": 8},
  { "type" : "Observation",
      "url": "https://s3.us-south.cloud-object-storage.appdomain.cloud/fhir-bulkimexport-connectathon/6SfXzbGvYl1nTjGbf5qeqJDFNjyljiGdKxXEJb4yJn8=/Observation_1.ndjson",
    "count": 234},
  { "type" : "Observation",
      "url": "https://s3.us-south.cloud-object-storage.appdomain.cloud/fhir-bulkimexport-connectathon/6SfXzbGvYl1nTjGbf5qeqJDFNjyljiGdKxXEJb4yJn8=/Observation_2.ndjson",
    "count": 81}]
}
```

For the Import Operation, the polled status includes an indication of `$import` and the location of the OperationOutcome NDJSON files and the corresponding failure and success counts.

For S3 exports, the bulk data feature may use presigned urls.  To enable presigned urls, the authentication type must be `hmac` and `fhirServer/bulkdata/storageProviders/(source)/presigned` must be `true`.  The urls become:

```
https://s3.appdomain.cloud/fhir-example/Patient_1.ndjson?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=fcEXbf9cc1ac49e99eEX77%2F20210228%2Fus%2Fs3%2Faws4_request&X-Amz-Date=20210EXT160538Z&X-Amz-Expires=86400&X-Amz-SignedHeaders=host&X-Amz-Signature=5ecfc207546b9737fd38d4f0EXEX1c58f4f82976338a44c
```

The presigned URL is valid for 86400 seconds (1 day).

To cancel a running job (or delete a completed job), issue an HTTP DELETE request to the polling URL.
* Invoking DELETE on a running job will return HTTP 202 (Accepted) and stop the job
* Invoking DELETE on a completed job will return HTTP 204 (Deleted) and delete the job
* Invoking DELETE on a deleted job will return a 404 (Not Found)

Prior to version 4.8.1, the exported `ndjson` file is configured with public access automatically and with 2 hours expiration time using `fhirServer/bulkdata/storageProviders/(source)/exportPublic`. The exported content is best made available with  presigned urls with the `hmac` authentication type.

In 4.8.1, the randomly generated path is used to uniquely identify the exported files in a single folder.

JavaBatch feature must be enabled in `server.xml` as following on the Liberty server:

```xml
<featureManager>
    ...
    <feature>batchManagement-1.0</feature>
    ...
</featureManager>
```

The JavaBatch user is configured in `bulkdata.xml` and the `fhir-server-config.json`:

```xml
<authorization-roles id="com.ibm.ws.batch">
	<security-role name="batchAdmin">
		<user name="fhiradmin"/>
	</security-role>
	<security-role name="batchSubmitter">
		<user name="fhiruser"/>
	</security-role>
	<security-role name="batchMonitor">
		<user name="fhiradmin"/>
		<user name="fhiruser"/>
	</security-role>
</authorization-roles>
```

Note: The batch-user referenced in the `fhir-server-config.json` must have a role of at least batchSubmitter.

By default, in-memory Derby database is used for persistence of the JavaBatch Jobs as configured in `fhir-server/configDropins/defaults/bulkdata.xml`. This database is destroyed on the restart of the IBM FHIR Server, and does not support load balancing.

To support IBM Db2 on IBM Cloud, copy `fhir-server/configDropins/disabled/db2-cloud/bulkdata.xml` to `fhir-server/configDropins/defaults/bulkdata.xml` replacing the existing bulkdata.xml. One can configure the Datasource by setting the following environment variables:

| Variable          | Default     | Description                                                    |
|-------------------|-------------|----------------------------------------------------------------|
| BATCH_DB_HOSTNAME | `blank`     | The hostname of the db2 instance                               |
| BATCH_DB_NAME     | BLUDB       | The database name                                              |
| BATCH_DB_SCHEMA   | FHIR_JBATCH | The Schema Name configured to support the Java Batch framework |
| BATCH_DB_PORT     | 50001       | The port configured to support the database                    |
| BATCH_DB_APIKEY   | `blank`     | The API Key for the Db2 Cloud database                         |

Instruction is also provided in 'Configuring a Liberty Datasource with API Key' section of the DB2OnCloudSetup guide to configure DB2 service in IBM Clouds as JavaBatch persistence store. The JavaBatch schema is created using the `fhir-persistence-schema` command line interface jar.

To support IBM Db2 with a user-name and password , copy `fhir-server/configDropins/disabled/db2/bulkdata.xml` to `fhir-server/configDropins/defaults/bulkdata.xml` replacing the existing bulkdata.xml. One can configure the Datasource by setting the following environment variables:

| Variable          | Default         | Description                                                    |
|-------------------|-----------------|----------------------------------------------------------------|
| BATCH_DB_HOSTNAME | `blank`         | The hostname of the db2 instance                               |
| BATCH_DB_NAME     | FHIRDB          | The database name                                              |
| BATCH_DB_SCHEMA   | FHIR_JBATCH     | The Schema Name configured to support the Java Batch framework |
| BATCH_DB_PORT     | 50000           | The port configured to support the database                    |
| BATCH_DB_USER     | db2inst1        | The user for the Db2 database                                  |
| BATCH_DB_PASSWORD | `blank`         | The password for the Db2 database                              |
| BATCH_DB_SSL      | true            | The ssl connection is either true or false                     |

If one wants to support Postgres with a user-name and password , one should copy `fhir-server/configDropins/disabled/postgres/bulkdata.xml` to `fhir-server/configDropins/defaults/bulkdata.xml` replacing the existing bulkdata.xml. One can configure the Datasource by setting the following environment variables:

| Variable               | Default         | Description                                                    |
|------------------------|-----------------|----------------------------------------------------------------|
| BATCH_DB_HOSTNAME      | `blank`         | The hostname of the postgres instance                          |
| BATCH_DB_NAME          | FHIRDB          | The database name                                              |
| BATCH_DB_SCHEMA        | FHIR_JBATCH     | The Schema Name configured to support the Java Batch framework |
| BATCH_DB_PORT          | 5432            | The port configured to support the database                    |
| BATCH_DB_USER          | fhirserver      | The user for the postgres database                             |
| BATCH_DB_PASSWORD      | `blank`         | The password for the postgres database                         |
| BATCH_DB_SSL           | true            | The ssl connection is either true or false                     |
| BATCH_DB_SSL_CERT_PATH | false           | The ssl connection is either true or false                     |

Note: If you use PostgreSQL database as IBM FHIR Server data store or the JavaBatch job repository, please enable `max_prepared_transactions` in postgresql.conf, otherwise the import/export JavaBatch jobs fail.

For more information about Liberty JavaBatch configuration, please refer to [IBM WebSphere Liberty Java Batch White paper](https://www-03.ibm.com/support/techdocs/atsmastr.nsf/webindex/wp102544).

If you are running in a Kubernetes deployment, be sure to set the environment variable MY_POD_NAME to `metadata.name` as shown in [deployment.yaml](https://github.com/Alvearie/alvearie-helm/blob/main/charts/ibm-fhir-server/templates/deployment.yaml#:~:text=MY_POD_NAME). This setting allows the stopping and deleting of jobs on all the hosts in the deployment.

### 4.10.1 *Path* and *Virtual Host* Bucket Access

For BulkData storage types of `ibm-cos` and `aws-s3`, the IBM FHIR Server supports two styles of accessing the `s3` bucket - virtual host and path.  In the IBM FHIR Server, `path` is the default access. [Link](https://docs.aws.amazon.com/AmazonS3/latest/userguide/VirtualHosting.html)

With path style access, the objects in a bucket are accessed using the pattern - `https://s3.region.host-name.com/bucket-name/object-key`. To configure path style access, one needs to configure the fhir-server-config.json.

There are three critical elements in the configuration to configure path style:

|Configuration|Details|
|-------------|-------|
|`endpointInternal`|the direct S3 API provider for the S3 API|
|`endpointExternal`|the endpoint url used to generate the downloadUrl used in S3 Export|
|`accessType`|"path", the default access type|

Example of `path` based access:

``` json
"bulkdata": {
    ...
    "storageProviders": {
        "default" : {
            "type": "aws-s3",
            "bucketName": "bucket-name",
            "location": "us",
            "endpointInternal": "https://s3.region.host-name.com",
            "endpointExternal": "https://s3.region.host-name.com",
            "auth" : {
                "type": "hmac",
                "accessKeyId": "example",
                "secretAccessKey": "example-password"
            },
            "disableBaseUrlValidation": true,
            "disableOperationOutcomes": true,
            "duplicationCheck": false,
            "validateResources": false,
            "create": false,
            "presigned": true,
            "accessType": "path"
        }
    }
}
```

With virtual host style access, the objects in a bucket are accessed using the pattern - `https://bucket-name.s3.region.host-name.com/object-key`. To configure virtual host style access, one needs to configure the API.

There are three critical elements in the configuration to configure virtual host style:

|Configuration|Details|
|-------------|-------|
|`endpointInternal`|the direct API provider for the S3 API, and not the virtual host, the underlying S3 libraries generate the virtual host url|
|`endpointExternal`|the Virtual Host endpoint url used to generate the downloadUrl generated after an Export|
|`accessType`|"host"|

Note, while the endpointInternal is specified with the S3 region endpoint, the calls to the API will use the virtual host directly.

Example of `host` based access:

``` json
"bulkdata": {
    ...
    "storageProviders": {
        "default" : {
            "type": "aws-s3",
            "bucketName": "bucket-name",
            "location": "us",
            "endpointInternal": "https://s3.region.host-name.com",
            "endpointExternal": "https://bucket-name.s3.region.host-name.com",
            "auth" : {
                "type": "hmac",
                "accessKeyId": "example",
                "secretAccessKey": "example-password"
            },
            "disableBaseUrlValidation": true,
            "disableOperationOutcomes": true,
            "duplicationCheck": false,
            "validateResources": false,
            "create": false,
            "presigned": true,
            "accessType": "host"
        }
    }
}
```

### 4.10.2 S3 Import File with matching segments

When Importing from an S3 Bucket, the IBM FHIR Server identifies the matching file segments. The `parameter.input.url` is used to query the S3 API to find the matching files.  For instance, the following import of `Patient.ndjson` matches `Patient.ndjson_seg0` and `Patient.ndjson_seg1` which are imported to the IBM FHIR Server.

This feature is useful for imports which follow a prefix pattern:

``` json
{
    "resourceType": "Parameters",
    "id": "30321130-5032-49fb-be54-9b8b82b2445a",
    "parameter": [
        {
            "name": "inputFormat",
            "valueString": "application/fhir+ndjson"
        },
        {
            "name": "inputSource",
            "valueUri": "https://localhost:9443/source-fhir-server"
        },
        {
            "name": "input",
            "part": [
                {
                    "name": "type",
                    "valueString": "Patient"
                },
                {
                    "name": "url",
                    "valueUrl": "test-import.ndjson"
                }
            ]
        },
        {
            "name": "storageDetail",
            "valueString": "ibm-cos"
        }
    ]
}
```


### 4.10.3 Integration Testing
To integration test, there are tests in `ExportOperationTest.java` in `fhir-server-test` module with server integration test cases for system, patient and group export. Further, there are tests in `ImportOperationTest.java` in `fhir-server-test` module. These tests rely on the `fhir-server-config-db2.json` which specifies two storageProviders.

### 4.10.5 Job Logs
Because the bulk import and export operations are built on Liberty's java batch implementation, users may need to check the [Liberty batch job logs](https://www.ibm.com/support/knowledgecenter/SSEQTP_liberty/com.ibm.websphere.wlp.doc/ae/rwlp_batch_view_joblog.html) for detailed step information / troubleshooting.

In a standard installation, these logs will be at `wlp/usr/servers/fhir-server/logs/joblogs`.
In the `ibmcom/ibm-fhir-server` docker image, these logs will be at `/logs/joblogs`.

Note, if you are using the default derby, the logs are overwritten upon restart of the server. You should use Db2 or Postgres for production purposes.

### 4.10.6 Known Limitations

The IBM FHIR Server's fhir-bulkdata-webapp does not support [persistence interceptors](https://github.com/IBM/FHIR/blob/main/docs/src/pages/guides/FHIRServerUsersGuide.md#43-persistence-interceptors). Therefor, $import requests will not lead to `beforeCreate`/`beforeUpdate` or `afterCreate`/`afterUpdate` method calls and $export requests will not lead to `beforeRead`/`beforeSearch` or `afterRead`/`afterSearch` method calls.
Because the IBM FHIR Server's notifications feature is implemented as a persistence interceptor, bulk operations will not result in any notification events.

## 4.11 Audit logging service
The Audit logging service pushes FHIR server audit events for FHIR operations in [Cloud Auditing Data Federation (CADF)](https://www.dmtf.org/standards/cadf) standard format to a Kafka backend, such as *IBM Cloud Event Streams service*.

There is early support for the [FHIR Standard: AuditEvent format](https://www.hl7.org/fhir/auditevent.html).

### 4.11.1 CADF audit log entry

Each FHIR interaction triggers a CADF audit log entry to be logged. The mapping of FHIR interaction to CADF action is as follows:

| FHIR Interaction                                    | CADF Action |
|-----------------------------------------------------|-------------|
|`history,metadata,read,search,validate,version read` |   read      |
|`create`                                             |   create    |
|`update`                                             |   update    |
|`delete`                                             |   delete    |
|`bundle,custom operation,patch`                      |   unknown   |

The following table describes the JSON fields of the CADF audit log entries logged by the FHIR server:

| CADF Audit Log Entry Field                           | Description |
|------------------------------------------------------|-------------|
|`action`                                              |Action that created the audit event. Possible values are "read", "update", "create", "delete", and "unknown".|
|`eventTime`                                           |Audit event creation timestamp.|
|`eventType`                                           |Audit event type. Value is always "activity".|
|`id`                                                  |Globally unique identifier for the audit event.|
|`outcome`                                             |Action outcome. Possible values are "success", "failure", "unknown", and "pending".|
|`typeURI`                                             |TypeURI property of the CADF event entity. Value is always "http://schemas.dmtf.org/cloud/audit/1.0/event".|
|`attachments`                                         |Note: Contains FHIR server-specific audit event data.|
|`attachments/contentType`                             |FHIR server-specific audit event data content type. Value is always "application/json".|
|`attachments/content`                                 |Note: Contents of this field (with its subfields) is encoded as Base64.
|`attachments/content/request_unique_id`               |Globally unique identifier for the FHIR server request.|
|`attachments/content/action`                          |FHIR action type. Possible values are "C" (create), "U" (update), "R" (read), "D" (delete), "P" (patch), and "O" (custom operation).|
|`attachments/content/operation_name`                  |FHIR custom operation name.|
|`attachments/content/start_time`                      |FHIR request start time.|
|`attachments/content/end_time`                        |FHIR request end time.|
|`attachments/content/api_parameters/request`          |FHIR request URL.|
|`attachments/content/api_parameters/request_status`   |FHIR request HTTP status (e.g. 200).|
|`attachments/content/data/resource_type`              |Resource type of FHIR resource that was created, updated, or deleted.|
|`attachments/content/data/id`                         |Resource ID of FHIR resource that was created, updated, or deleted.|
|`attachments/content/data/version`                    |Updated version of FHIR resource that was created, updated, or deleted.|
|`attachments/content/batch/resources_read`            |FHIR resource count retrieved on a search request.|
|`attachments/content/event_type`                      |FHIR event type. Possible values are "fhir-create", "fhir-update", "fhir-patch", "fhir-delete", "fhir-read", "fhir-version-read", "fhir-history", "fhir-search", "fhir-bundle", "fhir-validate", "fhir-metadata", and "fhir-operation".|
|`attachments/content/description`                     |FHIR event type description. Possible values are "FHIR Create request", "FHIR Update request", "FHIR Patch request", "FHIR Delete request", "FHIR Read request", "FHIR VersionRead request", "FHIR History request", "FHIR Search request", "FHIR Bundle request", "FHIR Validate request", "FHIR Metadata request", and "FHIR Operation request".|
|`attachments/content/client_cert_cn`                  |Value is determined by "IBM-App-cli-CN" HTTP header of the FHIR request.|
|`attachments/content/client_cert_issuer_ou`           |Value is determined by "IBM-App-iss-OU" HTTP header of the FHIR request.|
|`attachments/content/location`                        |IP address and hostname of the source of the FHIR request.|
|`initiator/id`                                        |Value is always "TENANT_ID@fhir-server", where TENANT_ID is replaced with the tenant ID.|
|`initiator/typeURI`                                   |Value is always "compute/machine".|
|`initiator/host`                                      |IP address of FHIR server localhost.|
|`initiator/credential/token`                          |Value is always "user-AUTH_USER", where AUTH_USER is replaced with the name of the authenticated user.|
|`initiator/geolocation/city`                          |Value determined by "fhirServer/audit/serviceProperties/geoCity" configuration property.|
|`initiator/geolocation/state`                         |Value determined by "fhirServer/audit/serviceProperties/geoState" configuration property.|
|`initiator/geolocation/region`                        |Value determined by "fhirServer/audit/serviceProperties/geoCounty" configuration property.|
|`observer/id`                                         |Value is always "fhir-server".|
|`observer/typeURI`                                    |Value is always "compute/node".|
|`observer/name`                                       |Value is always "Fhir Audit".|
|`observer/geolocation/city`                           |Value is determined by "fhirServer/audit/serviceProperties/geoCity" configuration property.|
|`observer/geolocation/state`                          |Value is determined by "fhirServer/audit/serviceProperties/geoState" configuration property.|
|`observer/geolocation/region`                         |Value is determined by "fhirServer/audit/serviceProperties/geoCounty" configuration property.|
|`target/id`                                           |Value is the Logical ID from the resource|
|`target/typeUri`                                      |Value is always "compute/node".|
|`target/addresses/url`                                |Value determined by the HttpServletRequest for the server. The value does not use the `X-FHIR-FORWARDED-URL` http header|
|`target/geolocation/city`                             |Value determined by "fhirServer/audit/serviceProperties/geoCity" configuration property.|
|`target/geolocation/state`                            |Value determined by "fhirServer/audit/serviceProperties/geoState" configuration property.|
|`target/geolocation/region`                           |Value determined by "fhirServer/audit/serviceProperties/geoCounty" configuration property.|

Note for Batch/Transactions, attachments/content includes counts of the number of C-R-U-D-E actions.

### 4.11.2 Enable audit logging service
Please refer to the property names that start with `fhirServer/audit/` in [5.1 Configuration properties reference](#51-configuration-properties-reference) for how to enable and configure the CADF audit logging service.

### 4.11.3 Configuration of audit logging service

There are two types of configuration for the Audit Logging Service. The first type is using the environment variable, and the second is the configuration driven from the fhir-server-config.json.

For example, the following uses the 'config' in order to config the Kafka publisher as part of the audit logging service.

```
"audit": {
    "serviceClassName" : "com.ibm.fhir.audit.impl.KafkaService",
    "serviceProperties" : {
        "load": "config",
    }
```

Or, you could load from an environment, note, this is the default behavior.

```
"audit": {
    "serviceClassName" : "com.ibm.fhir.audit.impl.KafkaService",
    "serviceProperties" : {
        "load": "environment",
    }
```

#### 4.11.3.1 Environment Variable Configuration of audit logging service

The audit logging service gets the event streams service credential from environment variable EVENT_STREAMS_AUDIT_BINDING with values like this:

```
    {
  "api_key": "xxxxxxxxxxxxxxxx_xxxxx_xxxxxxxxxxxxxxxxxxx",
  "apikey": "xxxxxxxxxxxxxxxx_xxxxx_xxxxxxxxxxxxxxxxxxx",
   …
  "kafka_brokers_sasl": [
    "broker-1-0server:9093",
    "broker-2-0server:9093",
    "broker-5-0server:9093",
    "broker-4-0server:9093",
    "broker-3-0server:9093",
    "broker-0-0server:9093"
  ],
   …
}
```

If you are using the IBM EventStreams on IBM Cloud, the service credential can be generated automatically when you run:

```
    ibmcloud ks cluster-service-bind --cluster <cluster_name_or_ID> --namespace <namespace> --service <event_streams_service_instance_name> …
```

to bind your event streams service instance to your Kubernetes cluster.

And then in the YAML file for your Kubernetes deployment, specify the environment variable EVENT_STREAMS_AUDIT_BINDING that references the binding key of the generated secret(binding-<event_streams_service_instance_name>) as following:

```
            - name: EVENT_STREAMS_AUDIT_BINDING
                valueFrom:
                  secretKeyRef:
                    key: binding
                    name: binding-<event_streams_service_instance_name>
```

Please refer to https://cloud.ibm.com/docs/containers?topic=containers-service-binding for detailed instructions if needed.

#### 4.11.3.2 fhir-server-config.json Configuration of audit logging service

```
"audit": {
    "serviceClassName" : "com.ibm.fhir.audit.impl.KafkaService",
    "serviceProperties" : {
        "load": "config",
        "mapper": "cadf",
        "auditTopic": "FHIR_AUDIT",
        "geoCity": "Dallas",
        "geoState": "TX",
        "geoCounty": "US",
        "kafka" : {
            "sasl.jaas.config": "********",
            "bootstrap.servers": "********",
            "sasl.mechanism": "PLAIN",
            "security.protocol": "SASL_SSL",
            "ssl.protocol": "TLSv1.2",
            "ssl.enabled.protocols": "TLSv1.2",
            "ssl.endpoint.identification.algorithm": "HTTPS"
        },
        "kafkaServers": "********",
        "kafkaApiKey": "********"
    }
```

The service can map to the CADF format or the FHIR AuditEvent resource format by declaring a mapper type - 'cadf' or 'auditevent'.

- *CADF* Example
```
{
    "action": "create",
    "eventTime": "2020-12-10 16:49:22.307",
    "eventType": "activity",
    "id": "c62ee8f8-be77-49d0-aad0-c0bf52a05fdf",
    "outcome": "success",
    "typeURI": "http://schemas.dmtf.org/cloud/audit/1.0/event",
    "tags": [
    ],
    "target": {
        "id": "176629076d7-ef3e8608-cd07-4c20-8823-ddfe46237052",
        "typeURI": "data/database",
        "addresses": [
            {
                "url": "https://test.io:443/fhir-server/api/v4/Patient",
                "name": "",
                "port": ""
            }
        ],
        "geolocation": {
            "city": "Hamil",
            "state": "TEXAS",
            "region": "USA",
            "annotations": [
            ]
        }
    },
    "attachments": [
        {
            "contentType": "application/json",
            "content": "rO0ABXQCUQp7CiAgICAicmVxdWVzdF91bmlxdWVfaWQiOiAiYzYyZWU4ZjgtYmU3Ny00OWQwLWFhZDAtYzBiZjUyYTA1ZmRmIiwKICAgICJhY3Rpb24iOiAiQyIsCiAgICAic3RhcnRfdGltZSI6ICIyMDIwLTEyLTEwIDE2OjQ5OjIyLjE2OCIsCiAgICAiZW5kX3RpbWUiOiAiMjAyMC0xMi0xMCAxNjo0OToyMi4zMDciLAogICAgImFwaV9wYXJhbWV0ZXJzIjogewogICAgICAgICJyZXF1ZXN0IjogImh0dHBzOi8vbG9jYWxob3N0Ojk0NDMvZmhpci1zZXJ2ZXIvYXBpL3Y0L1ZlcmlmaWNhdGlvblJlc3VsdCIsCiAgICAgICAgInJlcXVlc3Rfc3RhdHVzIjogMjAxCiAgICB9LAogICAgImRhdGEiOiB7CiAgICAgICAgInJlc291cmNlX3R5cGUiOiAiVmVyaWZpY2F0aW9uUmVzdWx0IiwKICAgICAgICAiaWQiOiAiMTc2NGQ4ZWEzMGEtYzUxOGMxOWItMWM0Zi00Yjc0LThhMDMtNGM5NjA0YmZkYjZlIiwKICAgICAgICAidmVyc2lvbl9pZCI6ICIxIgogICAgfSwKICAgICJldmVudF90eXBlIjogImZoaXItY3JlYXRlIiwKICAgICJkZXNjcmlwdGlvbiI6ICJGSElSIENyZWF0ZSByZXF1ZXN0IiwKICAgICJsb2NhdGlvbiI6ICIxMjcuMC4wLjEvbG9jYWxob3N0Igp9"
        }
    ],
    "initiator": {
        "id": "default@fhir-server",
        "typeURI": "compute/machine",
        "host": "192.168.86.20",
        "credential": {
            "token": "user-fhiruser"
        },
        "geolocation": {
            "city": "Dallas",
            "state": "TX",
            "region": "US",
            "annotations": [
            ]
        }
    },
    "observer": {
        "id": "fhir-server",
        "typeURI": "compute/node",
        "name": "IBM FHIR Server - Audit",
        "geolocation": {
            "city": "Dallas",
            "state": "TX",
            "region": "US",
            "annotations": [
            ]
        }
    }
}
```

- *AuditEvent* Example

```
{
    "resourceType": "AuditEvent",
    "type": {
        "system": "http://terminology.hl7.org/CodeSystem/audit-event-type",
        "code": "rest",
        "display": "Restful Operation"
    },
    "subtype": [
        {
            "system": "http://hl7.org/fhir/restful-interaction",
            "code": "search",
            "display": "search"
        }
    ],
    "action": "E",
    "period": {
        "start": "2020-12-10T16:38:14.466Z",
        "end": "2020-12-10T16:38:14.631Z"
    },
    "recorded": "2020-12-10T11:38:14.632173-05:00",
    "outcome": "0",
    "outcomeDesc": "success",
    "purposeOfEvent": [
        {
            "coding": [
                {
                    "system": "http://terminology.hl7.org/CodeSystem/v3-ActReason",
                    "code": "PurposeOfUse",
                    "display": "PurposeOfUse"
                }
            ]
        }
    ],
    "agent": [
        {
            "role": [
                {
                    "coding": [
                        {
                            "system": "http://terminology.hl7.org/CodeSystem/extra-security-role-type",
                            "code": "datacollector",
                            "display": "datacollector"
                        }
                    ]
                }
            ],
            "name": "fhir-server",
            "requestor": true,
            "network": {
                "address": "internal-ip",
                "type": "1"
            }
        }
    ],
    "source": {
        "site": "127.0.0.1/localhost",
        "observer": {
            "reference": "fhir-server"
        },
        "type": [
            {
                "system": "http://terminology.hl7.org/CodeSystem/security-source-type",
                "code": "4",
                "display": "Application Server"
            }
        ]
    },
    "entity": [
        {
            "securityLabel": [
                {
                    "system": "http://terminology.hl7.org/CodeSystem/v3-Confidentiality",
                    "code": "N",
                    "display": "normal"
                }
            ],
            "description": "FHIR Search request",
            "query": "MTI3LjAuMC4xL2xvY2FsaG9zdC97X3RhZz1baHR0cDovL2libS5jb20vZmhpci90YWd8dGFnMixodHRwOi8vaWJtLmNvbS9maGlyL3RhZ3x0YWddLCBfY291bnQ9WzEwMDBdLCBfcGFnZT1bMV19",
            "detail": [
                {
                    "type": "FHIR Context",
                    "valueBase64Binary": "CnsKICAgICJyZXF1ZXN0X3VuaXF1ZV9pZCI6ICIzMmJmMDNhNS1kNmQ1LTQ2MTEtYmFjYS0wNjdkMzcwMDYyMzUiLAogICAgImFjdGlvbiI6ICJSIiwKICAgICJzdGFydF90aW1lIjogIjIwMjAtMTItMTAgMTY6Mzg6MTQuNDY2IiwKICAgICJlbmRfdGltZSI6ICIyMDIwLTEyLTEwIDE2OjM4OjE0LjYzMSIsCiAgICAiYXBpX3BhcmFtZXRlcnMiOiB7CiAgICAgICAgInJlcXVlc3QiOiAiaHR0cHM6Ly9sb2NhbGhvc3Q6OTQ0My9maGlyLXNlcnZlci9hcGkvdjQvX3NlYXJjaD9fdGFnPWh0dHAlM0ElMkYlMkZpYm0uY29tJTJGZmhpciUyRnRhZyU3Q3RhZzIlMkNodHRwJTNBJTJGJTJGaWJtLmNvbSUyRmZoaXIlMkZ0YWclN0N0YWcmX2NvdW50PTEwMDAmX3BhZ2U9MSIsCiAgICAgICAgInJlcXVlc3Rfc3RhdHVzIjogMjAwCiAgICB9LAogICAgInF1ZXJ5IjogIntfdGFnPVtodHRwOi8vaWJtLmNvbS9maGlyL3RhZ3x0YWcyLGh0dHA6Ly9pYm0uY29tL2ZoaXIvdGFnfHRhZ10sIF9jb3VudD1bMTAwMF0sIF9wYWdlPVsxXX0iLAogICAgImJhdGNoIjogewogICAgICAgICJyZXNvdXJjZXNfcmVhZCI6IDIKICAgIH0sCiAgICAiZXZlbnRfdHlwZSI6ICJmaGlyLXNlYXJjaCIsCiAgICAiZGVzY3JpcHRpb24iOiAiRkhJUiBTZWFyY2ggcmVxdWVzdCIsCiAgICAibG9jYXRpb24iOiAiMTI3LjAuMC4xL2xvY2FsaG9zdCIKfQ=="
                }
            ]
        }
    ]
}
```

### 4.11.4 Query CADF events in COS

[Watson Studio stream flow](https://cloud.ibm.com/docs/tutorials?topic=solution-tutorials-big-data-log-analytics#create-a-streams-flow-source) can be created to push those FHIR Audit CADF events from the Event Streams service to a COS bucket (e.g. fhir-audit-dev) in CSV format. Another option is to configure Event Streams (Kafka) S3 connect to push those CADF events to a COS bucket (e.g. fhir-audit-dev) but in raw CADF json format.

A service instance of the [IBM Cloud SQL Query](https://www.ibm.com/cloud/blog/analyzing-data-with-ibm-cloud-sql-query) service can be created to allow you to query those CADF audit events in COS with SQL queries. Before running an SQL query, it's recommended to first create a COS bucket to store your query results, otherwise the query results will be stored in a bucket which is automatically created by the SQL query service.

Sample queries for CSV records expanded from the JSON CADF events:

```
select * from cos://us-south/fhir-audit-dev where EVENTTIME BETWEEN "2019-06-07" and "2019-06-08" and action="unknown" into cos://us-south/fhir-audit-dev-res stored as csv

select action, count(*) from cos://us-south/fhir-audit-dev where EVENTTIME BETWEEN "2019-06-05" and "2019-06-06" group by action into cos://us-south/fhir-audit-dev-res stored as csv

select * from cos://us-south/fhir-audit-dev where EVENTTIME BETWEEN "2019-06-05" and "2019-06-06" and action="create" and initiator_id="default@fhir-server" into cos://us-south/fhir-audit-dev-res stored as csv

select * from cos://us-south/fhir-audit-dev where EVENTTIME BETWEEN "2019-06-05" and "2019-06-06" and ATTACHMENTS_CONTENT LIKE '%fhir-read%' into cos://us-south/fhir-audit-dev-res stored as csv
```

Sample queries for the raw JSON CADF events:

```
select * from cos://us-south/fhir-audit-dev0 stored as json where EVENTTIME BETWEEN "2019-06-01" and "2019-06-08" and action="unknown" into cos://us-south/fhir-audit-dev0-res stored as json

select action, count(*) from cos://us-south/fhir-audit-dev0 stored as json where EVENTTIME BETWEEN "2019-06-01" and "2019-06-06" group by action into cos://us-south/fhir-audit-dev0-res stored as csv

select * from cos://us-south/fhir-audit-dev0 stored as json where EVENTTIME BETWEEN "2019-06-01" and "2019-06-06" and action="create" and initiator.id="default@fhir-server" into cos://us-south/fhir-audit-dev-res stored as json

select * from cos://us-south/fhir-audit-dev0 stored as json where EVENTTIME BETWEEN "2019-06-01" and "2019-06-06" and ATTACHMENTS[0].CONTENT LIKE '%fhir-read%' into cos://us-south/fhir-audit-dev-res stored as json

```

## 4.12 FHIR REST API
By default, the IBM FHIR Server allows the following RESTful interactions associated with the FHIR REST API: `create`, `read`, `vread`, `history`, `search`, `update`, `patch`, `delete`. However, it is possible to configure which of these interactions are allowed on a per resource basis through a set of interaction rules specified via the `fhirServer/resources/<resourceType>/interactions` property in `fhir-server-config.json`. The following snippet shows the general form for specifying interaction rules:

```
"resources": {
    "open": true,
    "Condition": {
        "interactions": ["create", "read", "vread", "history", "search", "update", "patch", "delete"]
    },
    "Observation": {
        "interactions": ["create", "read", "vread", "history", "delete"]
    },
    "Patient": {
        "interactions": ["read", "vread", "history", "search"]
    },
    "Procedure": {
        "interactions": ["create", "read", "vread", "history", "delete"]
    }
}
```

The `fhirServer/resources/<resourceType>/interactions` property is a JSON array of strings that represent the RESTful interactions allowed for the given resource type. If an interaction is not in the list of strings specified for a resource type, that interaction will not be allowed for that resource type. In the example above, the following interactions are allowed for the `Observation` resource type: [`create`, `read`, `vread`, `history`, `delete`]. This means a user will not be able to search for `Observation` resources because the `search` interaction is not specified in the list of allowed interactions.

Omitting the `fhirServer/resources/<resourceType>/interactions` property is equivalent to allowing all interactions for a given resource type. An empty array, `[]`, can be used to indicate that no interactions are allowed. Additionally, to define the set of interactions allowed for resource types which are not specifically configured in the `fhirServer/resources` property group, or for resource types which are configured, but which do not specify the `fhirServer/resources/<resourceType>/interactions` property, the base type of `Resource` may be specified:

```
"resources": {
    "open": true,
    "Condition": {
        "interactions": ["create", "read", "vread", "history", "search", "update", "delete"]
    },
    "Observation": {
        "interactions": ["create", "read", "vread", "history", "delete"]
    },
    "Patient": {
        "interactions": ["read", "vread", "history", "search"]
    },
    "Procedure": {
    },
    "Resource": {
        "interactions": ["create", "read", "vread", "history", "search", "update", "patch", "delete"]
    }
}
```

In the example above, for any resource type which is not specifically configured, such as `Encounter`, or for any resource type which is configured but does not specify the `fhirServer/resources/<resourceType>/interactions` property, such as `Procedure`, all of the interactions listed for the `Resource` resource type will be allowed.

One final consideration when configuring interactions is the `fhirServer/resources/open` property. If this property is specified and its value is set to `false`, then no interactions will be allowed for resource types which are not specifically listed in the `fhirServer/resources` property group. Assume the following configuration:

```
"resources": {
    "open": false,
    "Condition": {
        "interactions": ["create", "read", "vread", "history", "search", "update", "delete"]
    },
    "Observation": {
        "interactions": ["create", "read", "vread", "history", "delete"]
    },
    "Patient": {
        "interactions": ["read", "vread", "history", "search"]
    }
}
```

In this case, since the `fhirServer/resources/open` property is set to `false`, only the resource types listed (`Condition`, `Observation`, `Patient`) are allowed to be interacted with via the FHIR REST API. For example, a `create` request of a `Procedure` resource will fail since that resource type is not specified.

Whole-system search and whole-system history are special cases. Since no resource type is specified on a whole-system request, validation will be done against the `Resource` resource type. In the above configuration example, a whole-system search request such as `GET [base]?_lastUpdated=gt2020-01-01` will fail because the `Resource` resource type is not specified. If the configuration were to have the `fhirServer/resources/open` property set to `true`, or if the `Resource` resource type were specified in the `fhirServer/resources` property group, then the whole-system search request would be allowed, assuming the `search` interaction was valid for the `Resource` resource type.

In addition to interaction configuration, the `fhirServer/resources` property group also provides the ability to configure search parameter filtering and profile validation. See [Search configuration](https://ibm.github.io/FHIR/guides/FHIRSearchConfiguration#13-filtering) and [Resource validation](#44-resource-validation) respectively for details.

## 4.12.1 Using the IBM FHIR Server behind a proxy
It is possible to run the IBM FHIR Server behind a reverse proxy such as Kubernetes Ingress or an API Gateway.

Because the FHIR API relies on links and references between resources (both absolute and relative), operators must ensure that the IBM FHIR Server is configured appropriately for the front-end URL being used by the FHIR clients.

This can be accomplished by configuring the `fhirServer/core/originalRequestUriHeaderName` property in the default fhir-server-config.json. When this parameter is configured, the IBM FHIR Server will use the value of the corresponding header to set the "originalRequestUri" for the scope of the request.

For example, consider a FHIR Server that is listening at https://fhir:9443/fhir-server/api/v4 and is configured with an  originalRequestUriHeaderName of `X-FHIR-FORWARDED-URL`. If this server is proxied by a server at https://example.com/fhir, then the proxy must set the `X-FHIR-FORWARDED-URL` header to the value of the front-end request URL (e.g. https://example.com/fhir/Patient/abc-123). In alternative deployments, the `fhirServer/core/externalBaseUrl` may be used in-lieu of the `X-FHIR-FORWARDED-URL`.

The originalRequestUriHeader is expected to contain the full path of the original request. Values with no scheme (e.g. `https://`) will be handled like relative URLs, but full URL values (including scheme, hostname, optional port, and path) are recommended. Query string values can be included in the header value but will be ignored by the server; the server will use the query string of the actual request to process the request.

# 5 Appendix

## 5.1 Configuration properties reference
This section contains reference information about each of the configuration properties supported by the FHIR server.

### 5.1.1 Property descriptions
| Property Name                 | Type | Description     |
|-------------------------------|------|-----------------|
|`fhirServer/core/defaultPrettyPrint`|boolean|A boolean flag which indicates whether "Pretty Printing" should be used by default. Applies to both XML and JSON.|
|`fhirServer/core/tenantIdHeaderName`|string|The name of the request header that will be used to specify the tenant-id for each incoming FHIR REST API request. For headers with semicolon-delimited parts, setting a header name like `<headerName>:<partName>` will select the value from the part of header `<headerName>`'s value with a name of `<partName>` (e.g. setting `X-Test:part1` would select `someValue` from the header `X-Test: part1=someValue;part2=someOtherValue`).|
|`fhirServer/core/dataSourceIdHeaderName`|string|The name of the request header that will be used to specify the datastore-id for each incoming FHIR REST API request. For headers with semicolon-delimited parts, setting a header name like `<headerName>:<partName>` will select the value from the part of header `<headerName>`'s value with a name of `<partName>` (e.g. setting `X-Test:part1` would select `someValue` from the header `X-Test: part1=someValue;part2=someOtherValue`).|
|`fhirServer/core/originalRequestUriHeaderName`|string|The name of the request header that will be used to indicate the original, end-user-facing, request URI for a given request. This optional config parameter is provided for cases where the server is deployed behind a reverse proxy that overwrites the host and/or path portions of the original request.|
|`fhirServer/core/defaultHandling`|string|The default handling preference of the server (*strict* or *lenient*) which determines how the server handles unrecognized search parameters and resource elements.|
|`fhirServer/core/allowClientHandlingPref`|boolean|Indicates whether the client is allowed to override the server default handling preference using the `Prefer:handling` header value part.|
|`fhirServer/core/checkReferenceTypes`|boolean|Indicates whether reference type checking is performed by the server during parsing / deserialization.|
|`fhirServer/core/checkControlCharacters`|boolean|Indicates whether Strings and Codes are checked for invalid control characters (ASCII and UTF-8).|
|`fhirServer/core/serverRegistryResourceProviderEnabled`|boolean|Indicates whether the server registry resource provider should be used by the FHIR registry component to access definitional resources through the persistence layer.|
|`fhirServer/core/serverResolveFunctionEnabled`|boolean|Indicates whether the server resolve function should be used by the FHIRPath evaluator to resolve references through the persistence layer.|
|`fhirServer/core/conditionalDeleteMaxNumber`|integer|The maximum number of matches supported in conditional delete. |
|`fhirServer/core/capabilityStatementCacheTimeout`|integer|The number of minutes that a tenant's CapabilityStatement is cached for the metadata endpoint. |
|`fhirServer/core/extendedCodeableConceptValidation`|boolean|A boolean flag which indicates whether extended validation is performed by the server during object construction for code, Coding, CodeableConcept, Quantity, Uri, and String elements which have required bindings to value sets.|
|`fhirServer/core/disabledOperations`|string|A comma-separated list of operations which are not allowed to run on the IBM FHIR Server, for example, `validate,import`. Note, do not include the dollar sign `$`|
|`fhirServer/core/defaultPageSize`|integer|Sets the page size for search and history request results when no `_count` parameter is specified.|
|`fhirServer/core/maxPageSize`|integer|Sets the maximum page size for search and history request results. If a user-specified `_count` parameter value exceeds the maximum page size, then a warning is logged and the maximum page size will be used.|
|`fhirServer/core/maxPageIncludeCount`|integer|Sets the maximum number of 'include' resources allowed per page for search and history request results. If the number of 'include' resources returned for a page of results from a search or history request will exceed the maximum number of 'include' resources allowed per page, then an error will be returned in the request results.|
|`fhirServer/core/ifNoneMatchReturnsNotModified`|boolean|When If-None-Match is specified, overrides the standard return status "412 Precondition Failed" to be "304 Not Modified". Useful in transaction bundles for clients not wanting the bundle to fail when a conflict is found.|
|`fhirServer/core/capabilitiesUrl`|string|The URL that is embedded in the default Capabilities statement|
|`fhirServer/core/externalBaseUrl`|string|The base URL that is embedded in the Search bundle response, as of version 4.9.0. Note that the base URL must not include a path segment that matches any FHIR resource type name (case-sensitive). For example, "https://example.com" or "https://example.com/my/patient/api" are fine, but "https://example.com/my/Patient/api" is not.|
|`fhirServer/validation/failFast`|boolean|Indicates whether validation should fail fast on create and update interactions|
|`fhirServer/term/capabilitiesUrl`|string|The URL that is embedded in the Terminology Capabilities statement using `mode=terminology`|
|`fhirServer/term/disableCaching`|boolean|Indicates whether caching is disabled for the FHIR terminology module, this includes caching in `CodeSystemSupport`, `ValueSetSupport`, `GraphTermServiceProvider`, and `RemoteTermServiceProvider`|
|`fhirServer/term/graphTermServiceProviders`|array of objects|The `graphTermServiceProviders` element is an array of objects|
|`fhirServer/term/graphTermServiceProviders/enabled`|boolean|Indicates whether the graph term service provider should be used by the FHIR term service to access code system content|
|`fhirServer/term/graphTermServiceProviders/timeLimit`|integer|Graph traversal time limit (in milliseconds)|
|`fhirServer/term/graphTermServiceProviders/configuration`|object (name/value pairs)|A JSON object that contains the name/value pairs used to configure the graph database behind the graph term service provider see: [https://docs.janusgraph.org/basics/configuration-reference/](https://docs.janusgraph.org/basics/configuration-reference/)|
|`fhirServer/term/remoteTermServiceProviders`|array of objects|The `remoteTermServiceProviders` element is an array of objects|
|`fhirServer/term/remoteTermServiceProviders/enabled`|boolean|Indicates whether this remote term service provider should be used by the FHIR term service to access code system content|
|`fhirServer/term/remoteTermServiceProviders/base`|string|The base URL for this remote term service provider|
|`fhirServer/term/remoteTermServiceProviders/trustStore/location`|string|The trust store location for this remote term service provider|
|`fhirServer/term/remoteTermServiceProviders/trustStore/password`|string|The trust store password for this remote term service provider|
|`fhirServer/term/remoteTermServiceProviders/trustStore/type`|string|The trust store type (e.g. pkcs12) for this remote term service provider|
|`fhirServer/term/remoteTermServiceProviders/hostnameVerificationEnabled`|boolean|Indicates whether hostname verification should be performed when using SSL transport|
|`fhirServer/term/remoteTermServiceProviders/basicAuth/username`|string|The basic authentication username for this remote term service provider|
|`fhirServer/term/remoteTermServiceProviders/basicAuth/password`|string|The basic authentication password for this remote term service provider|
|`fhirServer/term/remoteTermServiceProviders/headers`|array of objects|The `headers` element is an array of objects|
|`fhirServer/term/remoteTermServiceProviders/headers/name`|string|The HTTP header name that will be added to requests by this remote term service provider|
|`fhirServer/term/remoteTermServiceProviders/headers/value`|string|The HTTP header value that will be added to requests by this remote term service provider|
|`fhirServer/term/remoteTermServiceProviders/httpTimeout`|integer|The HTTP read timeout for this remote term service provider (in milliseconds)|
|`fhirServer/term/remoteTermServiceProviders/supports`|array of objects|The `supports` element is an array of objects|
|`fhirServer/term/remoteTermServiceProviders/supports/system`|string|The system URI supported by this remote term service provider|
|`fhirServer/term/remoteTermServiceProviders/supports/version`|string|The system version supported by this remote term service provider|
|`fhirServer/resources/open`|boolean|Whether resources that are not explicitly listed in the configuration should be supported by the FHIR Server REST layer. When open is set to `false`, only the resources listed in fhir-server-config.json are supported.|
|`fhirServer/resources/Resource/interactions`|string list|A list of strings that represent the RESTful interactions (create, read, vread, update, patch, delete, history, and/or search) supported for resource types. Omitting this property is equivalent to supporting all FHIR interactions for the supported resources. An empty list, `[]`, can be used to indicate that no REST methods are supported. This property can be overridden for specific resource types via the `fhirServer/resources/<resourceType>/interactions` property.|
|`fhirServer/resources/Resource/searchParameters`|object|The set of search parameters to support for all supported resource types. Omitting this property is equivalent to supporting all search parameters in the server's registry that apply to resource type "Resource" (all resources). An empty object, `{}`, can be used to indicate that no global search parameters are supported.|
|`fhirServer/resources/Resource/searchParameters/<code>`|string|The URL of the search parameter definition to use for the search parameter `<code>`. Individual resource types may override this value via `fhirServer/resources/<resourceType>/searchParameters/<code>`|
|`fhirServer/resources/Resource/searchIncludes`|string list|A comma-separated list of \_include values supported for all resource types. Individual resource types may override this value via `fhirServer/resources/<resourceType>/searchIncludes`. Omitting this property is equivalent to supporting all \_include values for the supported resources. An empty list, `[]`, can be used to indicate that no \_include values are supported.|
|`fhirServer/resources/Resource/searchRevIncludes`|string list|A comma-separated list of \_revinclude values supported for all resource types. Individual resource types may override this value via `fhirServer/resources/<resourceType>/searchRevIncludes`. Omitting this property is equivalent to supporting all \_revinclude values for the supported resources. An empty list, `[]`, can be used to indicate that no \_revinclude values are supported.|
|`fhirServer/resources/Resource/searchParameterCombinations`|string list|A comma-separated list of search parameter combinations supported for all resource types. Each search parameter combination is a string, where a plus sign, `+`, separates the search parameters that can be used in combination. To indicate that searching without any search parameters is allowed, an empty string must be included in the list. Including an asterisk, `*`, in the list indicates support of any search parameter combination. Individual resource types may override this value via `fhirServer/resources/<resourceType>/searchParameterCombinations`. Omitting this property is equivalent to supporting any search parameter combination.|
|`fhirServer/resources/Resource/profiles/atLeastOne`|string list|A comma-separated list of profiles, at least one of which must be specified in a resource's `meta.profile` element and successfully validated against in order for a resource to be persisted to the FHIR server. Individual resource types may override this value via `fhirServer/resources/<resourceType>/profiles/atLeastOne`. Omitting this property or specifying an empty list is equivalent to not requiring any profile assertions for a resource.|
|`fhirServer/resources/Resource/profiles/notAllowed`|string list|A comma-separated list of profiles that are not allowed to be specified in a resource's `meta.profile` element, and thus cannot be validated against in order for a resource to be persisted to the FHIR server. Individual resource types may override this value via `fhirServer/resources/<resourceType>/profiles/notAllowed`. Omitting this property or specifying an empty list is equivalent to allowing any profile assertions for a resource.|
|`fhirServer/resources/Resource/profiles/allowUnknown`|boolean|Indicates if profiles are allowed to be specified in a resource's `meta.profile` element that are not loaded in the FHIR server. If set to `false`, specifying a profile that is not loaded in the FHIR server results in an error and a resource validation failure. If set to `true`, a warning will be issued and the profile will be ignored. The default value is `true`. Individual resource types may override this value via `fhirServer/resources/<resourceType>/profiles/allowUnknown`.|
|`fhirServer/resources/Resource/profiles/defaultVersions`|object|A set of profiles for which default profile versions are specified. If a profile in this set is asserted for a resource, and the asserted profile URL does not include a version, the version specified for the profile in this set will be used during resource validation. In cases where asserted profiles do not include a version, and are not in this set, the FHIR server will determine the default version of the profile to be used during resource validation. Individual resource types may override this set of profiles via `fhirServer/resources/<resourceType>/profiles/defaultVersions`.|
|`fhirServer/resources/Resource/profiles/defaultVersions/<profile>`|string|The version of the profile definition to use for the profile `<profile>` during resource validation when this profile is asserted.|
|`fhirServer/resources/<resourceType>/interactions`|string list|A list of strings that represent the RESTful interactions (create, read, vread, update, patch, delete, history, and/or search) to support for this resource type. For resources without the property, the value of `fhirServer/resources/Resource/interactions` is used.|
|`fhirServer/resources/<resourceType>/searchParameters`|object|The set of search parameters to support for this resource type. Global search parameters defined on the `Resource` resource can be overridden on a per-resourceType basis.|
|`fhirServer/resources/<resourceType>/searchParameters/<code>`|string|The URL of the search parameter definition to use for the search parameter `<code>` on resources of type `<resourceType>`.|
|`fhirServer/resources/<resourceType>/searchIncludes`|string list|A comma-separated list of \_include values supported for this resource type. An empty list, `[]`, can be used to indicate that no \_include values are supported. For resources without the property, the value of `fhirServer/resources/Resource/searchIncludes` is used.|
|`fhirServer/resources/<resourceType>/searchRevIncludes`|string list|A comma-separated list of \_revinclude values supported for this resource type. An empty list, `[]`, can be used to indicate that no \_revinclude values are supported. For resources without the property, the value of `fhirServer/resources/Resource/searchRevIncludes` is used.|
|`fhirServer/resources/<resourceType>/searchParameterCombinations`|string list|A comma-separated list of search parameter combinations supported for this resource type. Each search parameter combination is a string, where a plus sign, `+`, separates the search parameters that can be used in combination. To indicate that searching without any search parameters is allowed, an empty string must be included in the list. Including an asterisk, `*`, in the list indicates support of any search parameter combination. For resources without the property, the value of `fhirServer/resources/Resource/searchParameterCombinations` is used.|
|`fhirServer/resources/<resourceType>/profiles/atLeastOne`|string list|A comma-separated list of profiles, at least one of which must be specified in a resource's `meta.profile` element and be successfully validated against in order for a resource of this type to be persisted to the FHIR server. If this property is not specified, the value of `fhirServer/resources/Resource/profiles/atLeastOne` will be used. If an empty list is specified, it is equivalent to not requiring any profile assertions for this resource type.|
|`fhirServer/resources/<resourceType>/profiles/notAllowed`|string list|A comma-separated list of profiles that are not allowed to be specified in a resource's `meta.profile` element, and thus cannot be validated against in order for a resource to be persisted to the FHIR server. If this property is not specified, the value of `fhirServer/resources/Resource/profiles/notAllowed` will be used. If an empty list is specified, it is equivalent to allowing any profile assertions for this resource type.|
|`fhirServer/resources/<resourceType>/profiles/allowUnknown`|boolean|Indicates if profiles are allowed to be specified in a resource's `meta.profile` element that are not loaded in the FHIR server. If set to `false`, specifying a profile that is not loaded in the FHIR server results in an error and a resource validation failure. If set to `true`, a warning will be issued and the profile will be ignored. If this property is not specified, the value of `fhirServer/resources/Resource/profiles/allowUnknown` will be used.|
|`fhirServer/resources/<resourceType>/profiles/defaultVersions`|object|A set of profiles for which default profile versions are specified for this resource type. If a profile in this set is asserted for a resource, and the asserted profile URL does not include a version, the version specified for the profile in this set will be used during resource validation. In cases where asserted profiles do not include a version, and are not in this set, the FHIR server will determine the default version of the profile to be used during resource validation. If this property is not specified, the value of `fhirServer/resources/Resource/profiles/defaultVersions` will be used.|
|`fhirServer/resources/<resourceType>/profiles/defaultVersions/<profile>`|string|The version of the profile definition to use for the profile `<profile>` during resource validation when this profile is asserted for this resource type.|
|`fhirServer/notifications/common/includeResourceTypes`|string list|A comma-separated list of resource types for which notification event messages should be published.|
|`fhirServer/notifications/common/maxNotificationSizeBytes`|integer|The maximum size in bytes of the notification that should be sent|
|`fhirServer/notifications/common/maxNotificationSizeBehavior`|string|The behavior of the notification framework when a notification is over the maxNotificationSizeBytes. Valid values are subset and omit|
|`fhirServer/notifications/websocket/enabled`|boolean|A boolean flag which indicates whether or not websocket notifications are enabled.|
|`fhirServer/notifications/kafka/enabled`|boolean|A boolean flag which indicates whether or not kafka notifications are enabled.|
|`fhirServer/notifications/kafka/sync`|boolean|A boolean flag which indicates whether or not the FHIRNotificationEvent is sent in a synchronous mode|
|`fhirServer/notifications/kafka/topicName`|string|The name of the topic to which kafka notification event messages should be published.|
|`fhirServer/notifications/kafka/connectionProperties`|property list|A group of connection properties used to configure the KafkaProducer. These properties are used as-is when instantiating the KafkaProducer used by the FHIR server for publishing notification event messages.|
|`fhirServer/notifications/nats/enabled`|boolean|A boolean flag which indicates whether or not NATS notifications are enabled.|
|`fhirServer/notifications/nats/cluster`|string|The name of the NATS streaming cluster to which to connect.|
|`fhirServer/notifications/nats/channel`|string|The name of the NATS channel on which NATS notification event messages are to be published.|
|`fhirServer/notifications/nats/clientId`|string|The name to use for the connections to the NATS streaming cluster.|
|`fhirServer/notifications/nats/servers`|string|The URL of one or more NATS servers in the NATS streaming cluster.|
|`fhirServer/notifications/nats/useTLS`|boolean|A boolean flag which indicates whether or not to use TLS for connections to the NATS streaming cluster.|
|`fhirServer/notifications/nats/truststoreLocation`|string|The file location of the truststore to use for TLS.|
|`fhirServer/notifications/nats/truststorePassword`|string|The password for the truststore.|
|`fhirServer/notifications/nats/keystoreLocation`|string|The file location of the keystore to use for TLS.|
|`fhirServer/notifications/nats/keystorePassword`|string|The password for the keystore.|
|`fhirServer/persistence/factoryClassname`|string|The name of the factory class to use for creating instances of the persistence layer implementation.|
|`fhirServer/persistence/common/updateCreateEnabled`|boolean|A boolean flag which indicates whether or not the 'update/create' feature should be enabled in the selected persistence layer.|
|`fhirServer/persistence/datasources`|map|A map containing datasource definitions. See [Section 3.3.1 The JDBC persistence layer](#331-the-jdbc-persistence-layer) for more information.|
|`fhirServer/persistence/datasources/<datasourceId>/type`|string|`derby` or `db2` or `postgresql`|
|`fhirServer/persistence/datasources/<datasourceId>/jndiName`|string|The non-default jndiName for the datasource|
|`fhirServer/persistence/datasources/<datasourceId>/currentSchema`|string|The current schema for the datasource|
|`fhirServer/persistence/datasources/<datasourceId>/searchOptimizerOptions/from_collapse_limit`|int| For PostgreSQL, sets the from_collapse_limit query optimizer parameter to improve search performance. If not set, the IBM FHIR Server uses a value of 12. To use the database default (8), explicitly set this value to null. |
|`fhirServer/persistence/datasources/<datasourceId>/searchOptimizerOptions/join_collapse_limit`|int| For PostgreSQL, sets the join_collapse_limit query optimizer parameter to improve search performance. If not set, the IBM FHIR Server uses a value of 12. To use the database default (8), explicitly set this value to null. |
|`fhirServer/persistence/datasources/<datasourceId>/hints/search.reopt`|string|For Db2, reopt pragma that is injected into the Search query, ALWAYS or ONCE are valid values|
|`fhirServer/security/cors`|boolean|Used to convey to clients whether cors is supported or not; actual cors support is configured separately in the Liberty server.xml configuration|
|`fhirServer/security/basic/enabled`|boolean|Whether or not the server is enabled for HTTP Basic authentication|
|`fhirServer/security/certificates/enabled`|boolean|Whether or not the server is enabled for Certificate-based client authentication|
|`fhirServer/security/oauth/enabled`|boolean|Whether or not the server is enabled for OAuth-based authentication/authorization|
|`fhirServer/security/oauth/regUrl`|string|The registration URL associated with the OAuth 2.0 authentication/authorization support.|
|`fhirServer/security/oauth/authUrl`|string|The authorization URL associated with the OAuth 2.0 authentication/authorization support.|
|`fhirServer/security/oauth/tokenUrl`|string|The token URL associated with the OAuth 2.0 authentication/authorization support.|
|`fhirServer/security/oauth/manageUrl`|string|The URL where an end-user can view which applications currently have access to data and can make adjustments to these access rights.|
|`fhirServer/security/oauth/introspectUrl`|string|The URL of the server’s introspection endpoint that can be used to validate a token.|
|`fhirServer/security/oauth/revokeUrl`|string|The URL to the server’s endpoint that can be used to revoke a token.|
|`fhirServer/security/oauth/smart/enabled`|boolean|Whether or not the server is enabled for OAuth-based authentication/authorization|
|`fhirServer/security/oauth/smart/scopes`|array|The list of SMART scopes to advertise in the `.well-known/smart-configuration endpoint|
|`fhirServer/security/oauth/smart/capabilities`|array|The list of SMART capabilities to advertise in the `.well-known/smart-configuration endpoint|
|`fhirServer/audit/serviceClassName`|string|The audit service to use. Currently, com.ibm.fhir.audit.impl.NopService to indicate the logger service is disabled, and com.ibm.fhir.audit.impl.KafkaService to indicate using Kafka as a destination.|
|`fhirServer/audit/serviceProperties/auditTopic`|string|The kafka topic to use for CADF audit logging service|
|`fhirServer/audit/serviceProperties/geoCity`|string|The Geo City configured for audit logging service.|
|`fhirServer/audit/serviceProperties/geoState`|string|The Geo State configured for audit logging service.|
|`fhirServer/audit/serviceProperties/geoCounty`|string|The Geo Country configured for audit logging service.|
|`fhirServer/audit/serviceProperties/kafkaServers`|string|The CSV list of Kafka brokers.|
|`fhirServer/audit/serviceProperties/kafkaApiKey`|string|The apikey for the JAAS configuration.|
|`fhirServer/audit/serviceProperties/mapper`|string|The AuditEventLog mapper that determines the output format - valid types are 'cadf' and 'auditevent'. 'auditevent' refers to the FHIR Resource AuditEvent, and 'cadf' refers to the Cloud logging standard.|
|`fhirServer/audit/serviceProperties/load`|string|The location that the configuration is loaded from 'environment' or 'config'.|
|`fhirServer/audit/serviceProperties/kafka`|object|A set of name value pairs used as part of the 'config' for publishing to the kafka service. These should only be Kafka properties.|
|`fhirServer/audit/hostname`|string|A string used to identify the Hostname, useful in containerized environments|
|`fhirServer/audit/ip`|string|A string used to identify the IP address, useful to identify only one IP|
|`fhirServer/search/useBoundingRadius`|boolean|If true then the bounding area is a radius, else the bounding area is a box.|
|`fhirServer/search/useStoredCompartmentParam`|boolean|True, compute and store parameter to accelerate compartment searches. Requires reindex using at least IBM FHIR Server version 4.5.1 before this feature is enabled |
|`fhirServer/search/enableLegacyWholeSystemSearchParams`|boolean|True, searches specifying whole-system search parameters `_profile`, `_tag`, and `_security` run against legacy search index data, else those searches run against new search index data. This property can be set to `true` before a reindex operation is run, and after migrating to IBM FHIR Server version 4.9.0, to allow searches to work while the reindex operation is in progress. After the reindex has completed successfully, the property should be set to `false` or removed from the configuration. |
|`fhirServer/bulkdata/enabled`| string|Enabling the BulkData operations |
|`fhirServer/bulkdata/core/api/url`|string|The URL to access the FHIR server hosting the batch web application |
|`fhirServer/bulkdata/core/api/user`|string|User for submitting JavaBatch job |
|`fhirServer/bulkdata/core/api/password`|string|Password for above batch user |
|`fhirServer/bulkdata/core/api/truststore`|string|Trust store for JavaBatch job submission |
|`fhirServer/bulkdata/core/api/truststorePassword`|string|Password for above trust store |
|`fhirServer/bulkdata/core/api/trustAll`|boolean|Indicates calls to the local API should skip hostname verification|
|`fhirServer/bulkdata/core/cos/partUploadTriggerSizeMB`|number|The size, in megabytes, at which to write a "part" for multi-part uploads. The S3 API requires parts to be between 5 and 5000 MB and does not allow more than 10,000 parts per object. |
|`fhirServer/bulkdata/core/cos/objectSizeThresholdMB`|number|The size, in megabytes, at which to finish writing a given object. Use `0` to indicate that all resources of a given type should be written to a single object, but be aware that S3 objects can have a maximum of 10,000 parts and a maximum size of 5,000,000 MB (5 TB). |
|`fhirServer/bulkdata/core/cos/objectResourceCountThreshold`|number|The number of resources at which to finish writing a given object. The actual number of resources written to a single object may be slightly above this number, dependent on the configured page size. Use `0` to indicate that there is no limit to the number of resources to be written to a single object.|
|`fhirServer/bulkdata/core/cos/requestTimeout`|number|The request timeout in second for the COS client|
|`fhirServer/bulkdata/core/cos/socketTimeout`|number|The socket timeout in second for the COS client|
|`fhirServer/bulkdata/core/cos/useServerTruststore`|boolean|If the COS Client should use the IBM FHIR Server's TrustStore to access S3/IBMCOS service |
|`fhirServer/bulkdata/core/cos/presignedExpiry`|number|The time in seconds of the presigned download URL; must be using HMAC auth|
|`fhirServer/bulkdata/core/file/writeTriggerSizeMB`|number|The size, in megabytes, at which to write the buffer to file.|
|`fhirServer/bulkdata/core/file/sizeThresholdMB`|number|The size, in megabytes, at which to finish writing a given file. Use `0` to indicate that all resources of a given type should be written to a single file.|
|`fhirServer/bulkdata/core/file/resourceCountThreshold`|number|The number of resources at which to finish writing a given file. The actual number of resources written to a single file may be slightly above this number, dependent on the configured page size. Use `0` to indicate that there is no limit to the number of resources to be written to a single file.|
|`fhirServer/bulkdata/core/azure/objectSizeThresholdMB`|number|The size, in megabytes, at which to finish writing a given object.|
|`fhirServer/bulkdata/core/azure/objectResourceCountThreshold`|number|The number of resources at which to finish writing a given object. The actual number of resources written to a single object may be slightly above this number, dependent on the configured page size.|
|`fhirServer/bulkdata/core/batchIdEncryptionKey`|string|Encoding key for JavaBatch job id |
|`fhirServer/bulkdata/core/pageSize`|number|The search page size for patient/group export and the legacy export, the default value is 1000 |
|`fhirServer/bulkdata/core/maxPartitions`|number| The maximum number of simultaneous partitions that are processed per Export and Import |
|`fhirServer/bulkdata/core/maxInputs`|number| The number of inputs allowed for $import |
|`fhirServer/bulkdata/core/iamEndpoint`|string| Override the system's IAM endpoint |
|`fhirServer/bulkdata/core/maxChunkReadTime`|string| Maximum time in milliseconds to read during a bulkdata export without type filters. The time should be three quarters of the transactionManager timeout (often the FHIR_TRANSACTION_MANAGER_TIMEOUT value). Note, this value is a string representation of a long value.|
|`fhirServer/bulkdata/core/defaultExportProvider`|string| The default storage provider used by Bulk Data Export|
|`fhirServer/bulkdata/core/defaultImportProvider`|string| The default storage provider used by Bulk Data Import|
|`fhirServer/bulkdata/core/defaultOutcomeProvider`|string| The default storage provider used to output Operation Outcomes (file, s3 only)|
|`fhirServer/bulkdata/core/enableSkippableUpdates`|boolean|Enables the skipping of identical resources|
|`fhirServer/bulkdata/storageProviders/<source>/type`|string|The type of storageProvider aws-s3, ibm-cos, file, https, azure-blob |
|`fhirServer/bulkdata/storageProviders/<source>/bucketName`|string| Object store bucket name |
|`fhirServer/bulkdata/storageProviders/<source>/location`|string|Object store location |
|`fhirServer/bulkdata/storageProviders/<source>/endpointInternal`|string|Object store end point url used to read/write from COS |
|`fhirServer/bulkdata/storageProviders/<source>/endpointExternal`|string|Object store end point url used in the constructed download URLs|
|`fhirServer/bulkdata/storageProviders/<source>/fileBase`|string| The absolute path of the output directory. It is recommended this path is not the mount point of a volume. For instance, if a volume is mounted to /output/bulkdata, use /output/bulkdata/data to ensure a failed mount does not result in writing to the root file system.|
|`fhirServer/bulkdata/storageProviders/<source>/validBaseUrls`|list|The list of supported urls which are approved for the fhir server to access|
|`fhirServer/bulkdata/storageProviders/<source>/disableBaseUrlValidation`|boolean|Disables the URL checking feature, allowing all URLs to be imported|
|`fhirServer/bulkdata/storageProviders/<source>/disableOperationOutcomes`|boolean|Disables the base url validation, allowing all URLs to be imported|
|`fhirServer/bulkdata/storageProviders/<source>/duplicationCheck`|boolean|Enables duplication check on import|
|`fhirServer/bulkdata/storageProviders/<source>/validateResources`|boolean|Enables the validation of imported resources|
|`fhirServer/bulkdata/storageProviders/<source>/presigned`|boolean|When an hmac auth type is used, presigns the URLs of an export|
|`fhirServer/bulkdata/storageProviders/<source>/create`|boolean|Enables the creation of buckets|
|`fhirServer/bulkdata/storageProviders/<source>/auth/type`|string|A type of hmac, iam, basic or connection|
|`fhirServer/bulkdata/storageProviders/<source>/auth/accessKeyId`|string|For HMAC, API key for accessing COS|
|`fhirServer/bulkdata/storageProviders/<source>/auth/secretAccessKey`|string|For HMAC, secret key for accessing COS|
|`fhirServer/bulkdata/storageProviders/<source>/auth/iamApiKey`|string|For IAM, API key for accessing IBM COS|
|`fhirServer/bulkdata/storageProviders/<source>/auth/iamResourceInstanceId`|string|For IAM, secret key for accessing IBM COS|
|`fhirServer/bulkdata/storageProviders/<source>/auth/username`|string|For basic, user COS|
|`fhirServer/bulkdata/storageProviders/<source>/auth/password`|string|For basic, password for accessing COS|
|`fhirServer/bulkdata/storageProviders/<source>/auth/connection`|string|For Azure Blob Service, the connection string is used|
|`fhirServer/bulkdata/storageProviders/<source>/operationOutcomeProvider`|string| the default storage provider used to output Operation Outcomes (file, s3 only)|
|`fhirServer/bulkdata/storageProviders/<source>/accessType`|string| The s3 access type, `host` or `path` (s3 only) [Link](https://docs.aws.amazon.com/AmazonS3/latest/userguide/VirtualHosting.html)|
|`fhirServer/bulkdata/storageProviders/<source>/requiresAccessToken`|boolean|controls the `$bulkdata-status` response to indicate Bulk Data storageprovider requires an accessToken using `requiresAccessToken`. When presigned URLs are enabled, this setting is overridden and shows as false in the $export response.|
|`fhirServer/operations/erase/enabled`|boolean|Enables the $erase operation|
|`fhirServer/operations/erase/allowedRoles`|list|The list of allowed roles, allowed entries are: `FHIRUsers` every authenticated user, `FHIROperationAdmin` which is authenticated `FHIRAdmin` users|
|`fhirServer/operations/membermatch/enabled`|boolean|Enables or disables the $member-match|
|`fhirServer/operations/membermatch/strategy`|string|The key identifying the Member Match strategy|
|`fhirServer/operations/membermatch/extendedProps`|object|The extended options for the extended member match implementation|
|`fhirServer/operations/everything/includeTypes`|list|The list of related resources to retrieve, allowed entries are `Location`, `Medication`, `Organization`, and `Practitioner`|


### 5.1.2 Default property values
| Property Name                 | Default value   |
|-------------------------------| ----------------|
|`fhirServer/core/defaultPrettyPrint`|false|
|`fhirServer/core/tenantIdHeaderName`|X-FHIR-TENANT-ID|
|`fhirServer/core/dataSourceIdHeaderName`|X-FHIR-DSID|
|`fhirServer/core/originalRequestUriHeaderName`|null|
|`fhirServer/core/defaultHandling`|strict|
|`fhirServer/core/allowClientHandlingPref`|true|
|`fhirServer/core/checkReferenceTypes`|true|
|`fhirServer/core/checkControlCharacters`|true|
|`fhirServer/core/serverRegistryResourceProviderEnabled`|false|
|`fhirServer/core/serverResolveFunctionEnabled`|false|
|`fhirServer/core/conditionalDeleteMaxNumber`|10|
|`fhirServer/core/capabilityStatementCacheTimeout`|60|
|`fhirServer/core/extendedCodeableConceptValidation`|true|
|`fhirServer/core/defaultPageSize`|10|
|`fhirServer/core/maxPageSize`|1000|
|`fhirServer/core/maxPageIncludeCount`|1000|
|`fhirServer/core/capabilitiesUrl`|null|
|`fhirServer/core/externalBaseUrl`|null|
|`fhirServer/core/ifNoneMatchReturnsNotModified`|false|
|`fhirServer/validation/failFast`|false|
|`fhirServer/term/capabilitiesUrl`|null|
|`fhirServer/term/cachingDisabled`|false|
|`fhirServer/term/graphTermServiceProviders/enabled`|false|
|`fhirServer/term/graphTermServiceProviders/timeLimit`|90000|
|`fhirServer/term/remoteTermServiceProviders/enabled`|false|
|`fhirServer/term/remoteTermServiceProviders/hostnameVerificationEnabled`|true|
|`fhirServer/term/remoteTermServiceProviders/httpTimeout`|60000|
|`fhirServer/resources/open`|true|
|`fhirServer/resources/Resource/interactions`|null (all interactions supported)|
|`fhirServer/resources/Resource/searchParameters`|null (all global search parameters supported)|
|`fhirServer/resources/Resource/searchParameters/<code>`|null|
|`fhirServer/resources/Resource/searchIncludes`|null (all \_include values supported)|
|`fhirServer/resources/Resource/searchRevIncludes`|null (all \_revinclude values supported)|
|`fhirServer/resources/Resource/searchParameterCombinations`|null (all search parameter combinations supported)|
|`fhirServer/resources/Resource/profiles/atLeastOne`|null (no resource profile assertions required)|
|`fhirServer/resources/Resource/profiles/notAllowed`|null (any resource profile assertions allowed)|
|`fhirServer/resources/Resource/profiles/allowUnknown`|true|
|`fhirServer/resources/Resource/profiles/defaultVersions`|null (FHIR server determines default versions)|
|`fhirServer/resources/Resource/profiles/defaultVersions/<profile>`|null|
|`fhirServer/resources/<resourceType>/interactions`|null (inherits from `fhirServer/resources/Resource/interactions`)|
|`fhirServer/resources/<resourceType>/searchParameters`|null (all type-specific search parameters supported)|
|`fhirServer/resources/<resourceType>/searchParameters/<code>`|null|
|`fhirServer/resources/<resourceType>/searchIncludes`|null (inherits from `fhirServer/resources/Resource/searchIncludes`)|
|`fhirServer/resources/<resourceType>/searchRevIncludes`|null (inherits from `fhirServer/resources/Resource/searchRevIncludes`)|
|`fhirServer/resources/<resourceType>/searchParameterCombinations`|null (inherits from `fhirServer/resources/Resource/searchParameterCombinations`)|
|`fhirServer/resources/<resourceType>/profiles/atLeastOne`|null (inherits from `fhirServer/resources/Resource/profiles/atLeastOne`)|
|`fhirServer/resources/<resourceType>/profiles/notAllowed`|null (inherits from `fhirServer/resources/Resource/profiles/notAllowed`)|
|`fhirServer/resources/<resourceType>/profiles/allowUnknown`|null (inherits from `fhirServer/resources/Resource/profiles/allowUnknown`)|
|`fhirServer/resources/<resourceType>/profiles/defaultVersions`|null (inherits from `fhirServer/resources/Resource/profiles/defaultVersions`)|
|`fhirServer/resources/<resourceType>/profiles/defaultVersions/<profile>`|null|
|`fhirServer/notifications/common/includeResourceTypes`|`["*"]`|
|`fhirServer/notifications/common/maxNotificationSizeBytes`|1000000|
|`fhirServer/notifications/common/maxNotificationSizeBehavior`|subset|
|`fhirServer/notifications/websocket/enabled`|false|
|`fhirServer/notifications/kafka/enabled`|false|
|`fhirServer/notifications/kafka/sync`|false|
|`fhirServer/notifications/kafka/topicName`|fhirNotifications|
|`fhirServer/notifications/kafka/connectionProperties`|`{}`|
|`fhirServer/notifications/nats/enabled`|false|
|`fhirServer/notifications/nats/cluster`|nats-streaming|
|`fhirServer/notifications/nats/channel`|fhirNotifications|
|`fhirServer/notifications/nats/clientId`|fhir-server|
|`fhirServer/notifications/nats/servers`||
|`fhirServer/notifications/nats/useTLS`|true|
|`fhirServer/notifications/nats/truststoreLocation`||
|`fhirServer/notifications/nats/truststorePassword`||
|`fhirServer/notifications/nats/keystoreLocation`||
|`fhirServer/notifications/nats/keystorePassword`||
|`fhirServer/persistence/factoryClassname`|com.ibm.fhir.persistence.jdbc.FHIRPersistenceJDBCFactory|
|`fhirServer/persistence/common/updateCreateEnabled`|true|
|`fhirServer/persistence/datasources`|embedded Derby database: derby/fhirDB|
|`fhirServer/persistence/datasources/<datasourceId>/type`|derby|
|`fhirServer/persistence/datasources/<datasourceId>/jndiName`|`jndi/fhir_<tenantId>_<datasourceId>`|
|`fhirServer/persistence/datasources/<datasourceId>/currentSchema`|null|
|`fhirServer/persistence/datasources/<datasourceId>/searchOptimizerOptions/from_collapse_limit`|16|
|`fhirServer/persistence/datasources/<datasourceId>/searchOptimizerOptions/join_collapse_limit`|16|
|`fhirServer/search/useBoundingRadius`|false|
|`fhirServer/search/useStoredCompartmentParam`|boolean|true|
|`fhirServer/search/enableLegacyWholeSystemSearchParams`|false|
|`fhirServer/security/cors`|true|
|`fhirServer/security/basic/enabled`|false|
|`fhirServer/security/certificates/enabled`|false|
|`fhirServer/security/oauth/enabled`|false|
|`fhirServer/security/oauth/regUrl`|""|
|`fhirServer/security/oauth/authUrl`|""|
|`fhirServer/security/oauth/tokenUrl`|""|
|`fhirServer/security/oauth/manageUrl`|""|
|`fhirServer/security/oauth/introspectUrl`|""|
|`fhirServer/security/oauth/revokeUrl`|""|
|`fhirServer/security/oauth/smart/enabled`|false|
|`fhirServer/security/oauth/smart/scopes`|null|
|`fhirServer/security/oauth/smart/capabilities`|null|
|`fhirServer/audit/serviceClassName`|""|
|`fhirServer/audit/serviceProperties/auditTopic`|FHIR_AUDIT|
|`fhirServer/audit/serviceProperties/geoCity`|UnknownCity|
|`fhirServer/audit/serviceProperties/geoState`|UnknownState|
|`fhirServer/audit/serviceProperties/geoCounty`|UnknownCountry|
|`fhirServer/audit/serviceProperties/mapper`|cadf|
|`fhirServer/audit/serviceProperties/load`|environment|
|`fhirServer/bulkdata/validBaseUrlsDisabled`|false|
|`fhirServer/bulkdata/cosFileMaxResources`|200000|
|`fhirServer/bulkdata/cosFileMaxSize`|209715200|
|`fhirServer/bulkdata/patientExportPageSize`|200|
|`fhirServer/bulkdata/useFhirServerTrustStore`|false|
|`fhirServer/bulkdata/ignoreImportOutcomes`|false|
|`fhirServer/bulkdata/enabled`|true |
|`fhirServer/bulkdata/core/api/trustAll`|false|
|`fhirServer/bulkdata/core/cos/partUploadTriggerSizeMB`|10 |
|`fhirServer/bulkdata/core/cos/objectSizeThresholdMB`|200 |
|`fhirServer/bulkdata/core/cos/objectResourceCountThreshold`|200000|
|`fhirServer/bulkdata/core/cos/requestTimeout`|120|
|`fhirServer/bulkdata/core/cos/socketTimeout`|120|
|`fhirServer/bulkdata/core/cos/useServerTruststore`|false|
|`fhirServer/bulkdata/core/cos/presignedExpiry`|86400|
|`fhirServer/bulkdata/core/azure/objectSizeThresholdMB`|200|
|`fhirServer/bulkdata/core/azure/objectResourceCountThreshold`|200000|
|`fhirServer/bulkdata/core/pageSize`|1000|
|`fhirServer/bulkdata/core/maxPartitions`|5|
|`fhirServer/bulkdata/core/maxInputs`|5|
|`fhirServer/bulkdata/core/iamEndpoint`|https://iam.cloud.ibm.com/oidc/token|
|`fhirServer/bulkdata/core/maxChunkReadTime`|90000|
|`fhirServer/bulkdata/core/defaultExportProvider`|default|
|`fhirServer/bulkdata/core/defaultImportProvider`|default|
|`fhirServer/bulkdata/core/defaultOutcomeProvider`|default|
|`fhirServer/bulkdata/core/enableSkippableUpdates`|true|
|`fhirServer/bulkdata/storageProviders/<source>/disableBaseUrlValidation`|false|
|`fhirServer/bulkdata/storageProviders/<source>/disableOperationOutcomes`|false|
|`fhirServer/bulkdata/storageProviders/<source>/duplicationCheck`|false|
|`fhirServer/bulkdata/storageProviders/<source>/validateResources`|false|
|`fhirServer/bulkdata/storageProviders/<source>/presigned`|false|
|`fhirServer/bulkdata/storageProviders/<source>/create`|false|
|`fhirServer/bulkdata/storageProviders/<source>/accessType`|`path`|
|`fhirServer/bulkdata/storageProviders/<source>/requiresAccessToken`|false|
|`fhirServer/operations/erase/enabled`|false|
|`fhirServer/operations/erase/allowedRoles`|empty, all roles|
|`fhirServer/operations/membermatch/enabled`|true|
|`fhirServer/operations/membermatch/strategy`|default|
|`fhirServer/operations/membermatch/extendedProps`|empty|
|`fhirServer/operations/everything/includeTypes`|null|

### 5.1.3 Property attributes
Depending on the context of their use, config properties can be:
* tenant-specific or global
* dynamic or static

The following table tracks which properties can be set on a tenant-specific basis
and which properties are loaded dynamically.
If you change a properties that has an `N` in the `Dynamic?` column, it means you
must restart the server for that change to take effect.

| Property Name                 | Tenant-specific? | Dynamic? |
|-------------------------------|------------------|----------|
|`fhirServer/core/defaultPrettyPrint`|Y|Y|
|`fhirServer/core/tenantIdHeaderName`|N|N|
|`fhirServer/core/dataSourceIdHeaderName`|N|N|
|`fhirServer/core/originalRequestUriHeaderName`|N|N|
|`fhirServer/core/defaultHandling`|Y|Y|
|`fhirServer/core/allowClientHandlingPref`|Y|Y|
|`fhirServer/core/checkReferenceTypes`|N|N|
|`fhirServer/core/checkControlCharacters`|N|N|
|`fhirServer/core/serverRegistryResourceProviderEnabled`|N|N|
|`fhirServer/core/serverResolveFunctionEnabled`|N|N|
|`fhirServer/core/conditionalDeleteMaxNumber`|Y|Y|
|`fhirServer/core/capabilityStatementCacheTimeout`|Y|Y|
|`fhirServer/core/extendedCodeableConceptValidation`|N|N|
|`fhirServer/core/disabledOperations`|N|N|
|`fhirServer/core/defaultPageSize`|Y|Y|
|`fhirServer/core/ifNoneMatchReturnsNotModified`|Y|Y|
|`fhirServer/core/maxPageSize`|Y|Y|
|`fhirServer/core/maxPageIncludeCount`|Y|Y|
|`fhirServer/core/capabilitiesUrl`|Y|Y|
|`fhirServer/core/externalBaseUrl`|Y|Y|
|`fhirServer/validation/failFast`|Y|Y|
|`fhirServer/term/cachingDisabled`|N|N|
|`fhirServer/term/graphTermServiceProviders/enabled`|N|N|
|`fhirServer/term/graphTermServiceProviders/timeLimit`|N|N|
|`fhirServer/term/graphTermServiceProviders/configuration`|N|N|
|`fhirServer/term/remoteTermServiceProviders/enabled`|N|N|
|`fhirServer/term/remoteTermServiceProviders/base`|N|N|
|`fhirServer/term/remoteTermServiceProviders/trustStore`|N|N|
|`fhirServer/term/remoteTermServiceProviders/hostnameVerificationEnabled`|N|N|
|`fhirServer/term/remoteTermServiceProviders/basicAuth`|N|N|
|`fhirServer/term/remoteTermServiceProviders/headers`|N|N|
|`fhirServer/term/remoteTermServiceProviders/httpTimeout`|N|N|
|`fhirServer/term/remoteTermServiceProviders/supports`|N|N|
|`fhirServer/term/capabilitiesUrl`|Y|Y|
|`fhirServer/resources/open`|Y|Y|
|`fhirServer/resources/Resource/interactions`|Y|Y|
|`fhirServer/resources/Resource/searchParameters`|Y|Y|
|`fhirServer/resources/Resource/searchParameters/<code>`|Y|Y|
|`fhirServer/resources/Resource/searchIncludes`|Y|Y|
|`fhirServer/resources/Resource/searchRevIncludes`|Y|Y|
|`fhirServer/resources/Resource/searchParameterCombinations`|Y|Y|
|`fhirServer/resources/Resource/profiles/atLeastOne`|Y|Y|
|`fhirServer/resources/Resource/profiles/notAllowed`|Y|Y|
|`fhirServer/resources/Resource/profiles/allowUnknown`|Y|Y|
|`fhirServer/resources/Resource/profiles/defaultVersions`|Y|Y|
|`fhirServer/resources/Resource/profiles/defaultVersions/<profile>`|Y|Y|
|`fhirServer/resources/<resourceType>/interactions`|Y|Y|
|`fhirServer/resources/<resourceType>/searchParameters`|Y|N|
|`fhirServer/resources/<resourceType>/searchParameters/<code>`|Y|N|
|`fhirServer/resources/<resourceType>/searchIncludes`|Y|Y|
|`fhirServer/resources/<resourceType>/searchRevIncludes`|Y|Y|
|`fhirServer/resources/<resourceType>/searchParameterCombinations`|Y|Y|
|`fhirServer/resources/<resourceType>/profiles/atLeastOne`|Y|Y|
|`fhirServer/resources/<resourceType>/profiles/notAllowed`|Y|Y|
|`fhirServer/resources/<resourceType>/profiles/allowUnknown`|Y|Y|
|`fhirServer/resources/<resourceType>/profiles/defaultVersions`|Y|Y|
|`fhirServer/resources/<resourceType>/profiles/defaultVersions/<profile>`|Y|Y|
|`fhirServer/notifications/common/includeResourceTypes`|N|N|
|`fhirServer/notifications/common/maxNotificationSizeBytes`|Y|N|
|`fhirServer/notifications/common/maxNotificationSizeBehavior`|Y|N|
|`fhirServer/notifications/websocket/enabled`|N|N|
|`fhirServer/notifications/kafka/enabled`|N|N|
|`fhirServer/notifications/kafka/sync`|Y|N|
|`fhirServer/notifications/kafka/topicName`|N|N|
|`fhirServer/notifications/kafka/connectionProperties`|N|N|
|`fhirServer/notifications/nats/enabled`|N|N|
|`fhirServer/notifications/nats/cluster`|N|N|
|`fhirServer/notifications/nats/channel`|N|N|
|`fhirServer/notifications/nats/clientId`|N|N|
|`fhirServer/notifications/nats/servers`|N|N|
|`fhirServer/notifications/nats/useTLS`|N|N|
|`fhirServer/notifications/nats/truststoreLocation`|N|N|
|`fhirServer/notifications/nats/truststorePassword`|N|N|
|`fhirServer/notifications/nats/keystoreLocation`|N|N|
|`fhirServer/notifications/nats/keystorePassword`|N|N|
|`fhirServer/persistence/factoryClassname`|N|N|
|`fhirServer/persistence/common/updateCreateEnabled`|N|N|
|`fhirServer/persistence/datasources`|Y|N|
|`fhirServer/persistence/datasources/<datasourceId>/type`|Y|N|
|`fhirServer/persistence/datasources/<datasourceId>/jndiName`|Y|Y|
|`fhirServer/persistence/datasources/<datasourceId>/currentSchema`|Y|Y|
|`fhirServer/persistence/datasources/<datasourceId>/searchOptimizerOptions/from_collapse_limit`|Y|Y|
|`fhirServer/persistence/datasources/<datasourceId>/searchOptimizerOptions/join_collapse_limit`|Y|Y|
|`fhirServer/search/useBoundingRadius`|Y|Y|
|`fhirServer/search/useStoredCompartmentParam`|Y|Y|
|`fhirServer/search/enableLegacyWholeSystemSearchParams`|Y|Y|
|`fhirServer/security/cors`|Y|Y|
|`fhirServer/security/basic/enabled`|Y|Y|
|`fhirServer/security/certificates/enabled`|Y|Y|
|`fhirServer/security/oauth/enabled`|Y|Y|
|`fhirServer/security/oauth/regUrl`|Y|Y|
|`fhirServer/security/oauth/authUrl`|Y|Y|
|`fhirServer/security/oauth/tokenUrl`|Y|Y|
|`fhirServer/security/oauth/manageUrl`|Y|Y|
|`fhirServer/security/oauth/introspectUrl`|Y|Y|
|`fhirServer/security/oauth/revokeUrl`|Y|Y|
|`fhirServer/security/oauth/smart/enabled`|Y|Y|
|`fhirServer/security/oauth/smart/scopes`|Y|Y|
|`fhirServer/security/oauth/smart/capabilities`|Y|Y|
|`fhirServer/audit/serviceClassName`|N|N|
|`fhirServer/audit/serviceProperties/auditTopic`|N|N|
|`fhirServer/audit/serviceProperties/geoCity`|N|N|
|`fhirServer/audit/serviceProperties/geoState`|N|N|
|`fhirServer/audit/serviceProperties/geoCounty`|N|N|
|`fhirServer/audit/serviceProperties/mapper`|N|N|
|`fhirServer/audit/serviceProperties/load`|N|N|
|`fhirServer/audit/hostname`|N|N|
|`fhirServer/audit/ip`|N|N|
|`fhirServer/bulkdata/enabled`|Y|Y|
|`fhirServer/bulkdata/core/api/url`|Y|Y|
|`fhirServer/bulkdata/core/api/user`|Y|Y|
|`fhirServer/bulkdata/core/api/password`|Y|Y|
|`fhirServer/bulkdata/core/api/truststore`|Y|Y|
|`fhirServer/bulkdata/core/api/truststorePassword`|Y|Y|
|`fhirServer/bulkdata/core/api/trustAll`|Y|Y|
|`fhirServer/bulkdata/core/cos/partUploadTriggerSizeMB`|N|N|
|`fhirServer/bulkdata/core/cos/objectSizeThresholdMB`|N|N|
|`fhirServer/bulkdata/core/cos/objectResourceCountThreshold`|N|N|
|`fhirServer/bulkdata/core/cos/requestTimeout`|N|N|
|`fhirServer/bulkdata/core/cos/socketTimeout`|N|N|
|`fhirServer/bulkdata/core/cos/useServerTruststore`|Y|Y|
|`fhirServer/bulkdata/core/cos/presignedExpiry`|Y|Y|
|`fhirServer/bulkdata/core/azure/objectSizeThresholdMB`|N|N|
|`fhirServer/bulkdata/core/azure/objectResourceCountThreshold`|N|N|
|`fhirServer/bulkdata/core/batchIdEncryptionKey`|Y|N|
|`fhirServer/bulkdata/core/pageSize`|Y|Y|
|`fhirServer/bulkdata/core/maxPartitions`|Y|Y|
|`fhirServer/bulkdata/core/maxInputs`|Y|Y|
|`fhirServer/bulkdata/core/iamEndpoint`|N|N|
|`fhirServer/bulkdata/core/fastTxTimeout`|N|N|
|`fhirServer/bulkdata/core/defaultExportProvider`|Y|Y|
|`fhirServer/bulkdata/core/defaultImportProvider`|Y|Y|
|`fhirServer/bulkdata/core/defaultOutcomeProvider`|Y|Y|
|`fhirServer/bulkdata/core/enableSkippableUpdates`|Y|Y|
|`fhirServer/bulkdata/storageProviders/<source>/type`|Y|Y|
|`fhirServer/bulkdata/storageProviders/<source>/bucketName`|Y|Y|
|`fhirServer/bulkdata/storageProviders/<source>/location`|Y|Y|
|`fhirServer/bulkdata/storageProviders/<source>/endpointInternal`|Y|Y|
|`fhirServer/bulkdata/storageProviders/<source>/endpointExternal`|Y|Y|
|`fhirServer/bulkdata/storageProviders/<source>/fileBase`|Y|Y|
|`fhirServer/bulkdata/storageProviders/<source>/validBaseUrls`|Y|Y|
|`fhirServer/bulkdata/storageProviders/<source>/disableBaseUrlValidation`|Y|Y|
|`fhirServer/bulkdata/storageProviders/<source>/disableOperationOutcomes`|Y|Y|
|`fhirServer/bulkdata/storageProviders/<source>/duplicationCheck`|Y|Y|
|`fhirServer/bulkdata/storageProviders/<source>/validateResources`|Y|Y|
|`fhirServer/bulkdata/storageProviders/<source>/presigned`|Y|Y|
|`fhirServer/bulkdata/storageProviders/<source>/create`|Y|Y|
|`fhirServer/bulkdata/storageProviders/<source>/auth/type`|Y|Y|
|`fhirServer/bulkdata/storageProviders/<source>/auth/accessKeyId`|Y|Y|
|`fhirServer/bulkdata/storageProviders/<source>/auth/secretAccessKey`|Y|Y|
|`fhirServer/bulkdata/storageProviders/<source>/auth/iamApiKey`|Y|Y|
|`fhirServer/bulkdata/storageProviders/<source>/auth/iamResourceInstanceId`|Y|Y|
|`fhirServer/bulkdata/storageProviders/<source>/auth/username`|Y|Y|
|`fhirServer/bulkdata/storageProviders/<source>/auth/password`|Y|Y|
|`fhirServer/bulkdata/storageProviders/<source>/auth/connection`|Y|Y|
|`fhirServer/bulkdata/storageProviders/<source>/operationOutcomeProvider`|Y|Y|
|`fhirServer/bulkdata/storageProviders/<source>/accessType`|Y|Y|
|`fhirServer/bulkdata/storageProviders/<source>/requiresAccessToken`|Y|Y|
|`fhirServer/operations/erase/enabled`|Y|Y|
|`fhirServer/operations/erase/allowedRoles`|Y|Y|
|`fhirServer/operations/membermatch/enabled`|Y|Y|
|`fhirServer/operations/membermatch/strategy`|Y|Y|
|`fhirServer/operations/membermatch/extendedProps`|Y|Y|
|`fhirServer/operations/everything/includeTypes`|Y|Y|

## 5.2 Keystores, truststores, and the IBM FHIR server

### 5.2.1 Background
As stated earlier, the FHIR server is installed with a default configuration in `server.xml` which includes the definition of a keystore (`fhirKeyStore.p12`) and a truststore (`fhirTrustStore.p12`)<sup id="a7">[7](#f7)</sup>. These files are provided only as examples and while they may suffice in a test environment, the FHIR server deployer should generate a new keystore and truststore for any installations where security is a concern. Review the information in the following topics to learn how to configure a secure keystore and truststore.

Additionally, the server has a trustDefault.xml config dropin that references the SEC_TLS_TRUSTDEFAULTCERTS variable (defaultValue = true) to indicate whether or not the JVM truststore should be used in combination with the configured trust store.

### 5.2.2 WebApp security
By default, the FHIR server REST API is only available via HTTPS on port 9443 and is protected by HTTP basic authentication.
Alternatively, the server can use OpenID Connect and OAuth 2.0 via a Bearer Token as described in [Section 5.3 OpenID Connect and OAuth 2.0](#53-openid-connect-and-oauth-20).
In addition, the FHIR server web application can be secured via client certificate-based authentication.

Here are some notes related to these authentication schemes:
*   Basic authentication is a very simple authentication scheme and should only be used over HTTPS because the username and password are essentially transmitted in plain text.
*   OAuth 2.0 authentication can only be used in conjunction with an HTTPS endpoint because the OAuth authorization steps rely on SSL handshake negotiations.
*   Client certificate-based authentication can only be used in conjunction with an HTTPS endpoint since it involves SSL handshake negotiations. The main value of client authentication is that the server is able to securely authenticate the client through the use of certificates.

### 5.2.3 Configuring mutual TLS authentication
To properly configure the FHIR server's keystore and truststore files, perform the following steps.

### 5.2.3.1 Configure the keyStores
1.  Create a new self-signed server certificate<sup id="a8">[8](#f8)</sup> and store it in a new keystore file located in the `<WLP_HOME>/usr/servers/fhir-server/resources/security` directory.

    In these instructions, we'll call this file `serverKeystore.jks`, although you can name your server keystore file anything you choose. We'll use the `keytool`<sup id="a9">[9](#f9)</sup> command for all keystore- and truststore-related steps, although there are several ways to perform these actions.

    The following command will generate a new self-signed certificate and store it in `serverKeystore.jks`:

    ```
    keytool -keystore serverKeystore.jks -storepass change-password -genkey
        -alias default -keyalg RSA -keypass change-password
    ```

2.  Export the server certificate so that it can be imported into the client's truststore:

    ```
    keytool -keystore serverKeystore.jks -storepass change-password -export
        -alias default -file server-public-key.cer
    ```

3.  Create the client's certificate and store it in `clientKeystore.jks`:

    ```
    keytool -keystore clientKeystore.jks -storepass change-password -genkey
        -alias client-auth -keyalg RSA -keypass change-password
    ```

    Note: `keytool` will prompt you for the various components of the distinguished name (DN) associated with the certificate (similar to Step 1). The value that you specify for the common name (CN) component of the DN must match a username in the basic user registry<sup id="a10">[10](#f10)</sup> configured within the `server.xml` file. This step is crucial for the client certificate-based authentication to work properly. The whole point of this authentication scheme is for the client to transmit its identity to the server via the client certificate, and the client's username must be contained in that certificate (the CN component of the DN) so that the FHIR server can properly authenticate the client.

4.  Export the client's certificate so that it can be imported into the server's truststore:

    ```
    keytool -keystore clientKeystore.jks -storepass change-password -export -alias client-auth -file client-public-key.cer
    ```

5.  Import the server's public key certificate into the client's truststore:

    ```
    keytool -keystore clientTruststore.jks -storepass change-password -import -file server-public-key.cer
    ```

6.  Import the client's public key certificate into the server's truststore:

    ```
    keytool -keystore serverTruststore.jks -storepass change-password -import -file client-public-key.cer
    ```

At this point, you should have a client keystore that contains a client certificate whose Distinguished Name's Common Name component is set to the username. You should also have a client truststore which contains the server's public key certificate. Essentially, the server and client both have a keystore that contains their own private and public key certificate and they both have a truststore which contains the public key certificate of their counterpart.

### 5.2.3.1 Configure the server
Copy the server keystore (`serverKeystore.jks`) and truststore (`serverTruststore.jks`) files to the appropriate directory (`<WLP_HOME>/usr/servers/fhir-server/resources/security`). Then configure the `server.xml` file correctly to reference your new keystore and truststore files.

### 5.2.3.2 Configure the client
The precise steps required to configure certificate-based authentication for a client application depend on the specific REST API client framework, but these are the general rules:

*   If the client is using the FHIR server's HTTPS endpoint, then the client's truststore should be configured with the certificate of the FHIR server<sup id="a11">[11](#f11)</sup>.
*   If the client is using basic authentication, then it must send an appropriate Authorization request header containing the username and password information in the HTTP request.
*   If the client is using client certificate-based Authentication, then the client keystore must be configured with a certificate that is trusted by the FHIR server<sup id="a12">[12](#f12)</sup>.
*   If the client is using OAuth 2.0 Authentication, then the client keystore must be configured with the REST API client framework. In addition, it must send an appropriate Authorization request header containing the Bearer token in the HTTP request.

## 5.3 OpenID Connect and OAuth 2.0
The FHIR specification recommends the use of OAuth 2.0.
The IBM FHIR Server supports OAuth 2.0 through the use of WebSphere Liberty / OpenLiberty features.

While it is possible to configure Liberty as an [OpenID Connect Client](https://openliberty.io/docs/latest/reference/config/openidConnectClient.html), more typically the IBM FHIR Server will be configured as a generic OAuth 2.0 "Protected Resource Server" that works with JWT access tokens that have been issued by a trusted Authorization Server like [Keycloak](https://www.keycloak.org).

### 5.3.1 Configure Liberty to be an OAuth 2.0 Protected Resource Server
Liberty can be configured to act as an OAuth 2.0 Protected Resource Server via either the [openidConnectClient feature](https://www.ibm.com/docs/en/was-liberty/core?topic=connect-configuring-oauth-20-protected-resources-in-liberty) or the [mpJwt feature](https://openliberty.io/guides/microprofile-jwt.html).

One advantage of the mpJwt approach is that users can be mapped into pre-defined JEE security roles.
To enable this feature without modifying the default `server.xml`, move the `jwtRS.xml` config snippet from `configDropins/disabled/` to `configDropins/defaults/` and modify as desired.

A copy of this snippet is provided here for illustrative purposes:
```xml
<server description="fhir-server">
    <featureManager>
        <feature>mpJwt-1.1</feature>
    </featureManager>

    <!-- Override the application-bnd binding of the main webapp -->
    <webApplication contextRoot="fhir-server/api/v4" id="fhir-server-webapp" location="fhir-server.war" name="fhir-server-webapp">
        <application-bnd id="bind">
            <security-role id="users" name="FHIRUsers">
                <group id="usersGroup" access-id="group:https://localhost:8443/auth/realms/test/fhirUser"/>
            </security-role>
        </application-bnd>
    </webApplication>

    <mpJwt id="jwtConsumer"
           jwksUri="http://keycloak:8080/auth/realms/test/protocol/openid-connect/certs"
           issuer="https://localhost:8443/auth/realms/test"
           audiences="http://fhir-server:9080/fhir-server/api/v4"
           userNameAttribute="sub"
           groupNameAttribute="group"
           authFilterRef="filter"/>

    <authFilter id="filter">
        <requestUrl urlPattern="/fhir-server" />
        <requestUrl matchType="notContain" urlPattern="/fhir-server/api/v4/metadata" />
        <requestUrl matchType="notContain" urlPattern="/fhir-server/api/v4/.well-known/smart-configuration" />
    </authFilter>
</server>
```

In the snippet above, the `mpJwt` element is configured to obtain JWK information from http://keycloak:8080/auth/realms/test/protocol/openid-connect/certs and use this to validate JWT tokens that are passed to the server.

Additionally, the server will validate the `iss` (issuer) and `aud` (audiences) claims of the JWT and use the `sub` claim as the user principal (for audit logging).

Finally, the value(s) of the `group` claim are used to map this user into a corresponding JEE security-role as defined in the `application-bnd` section of the `webApplication` element.

### 5.3.2 Advertise the OAuth endpoints via fhir-server-config
To make the FHIR Server advertise the OAuth endpoints of the configured provider, supply values for at least the following properties in the default fhir-server-config.json file:
* `fhirServer/security/oauth/authUrl`
* `fhirServer/security/oauth/tokenUrl`

These values will be used to populate the corresponding entries in both the server capability statement (`GET [base]/metadata`) and the smart-configuration (`GET [base]/.well-known/smart-configuration`).

For example, the following excerpt from a CapabilityStatement shows sample OAuth-related URLs (register, authorize, and token) values in the `valueUri` elements.
```
…
"rest": [
    {
        "mode": "server",
        "security": {
        "extension": [
            {
                "url": "http://fhir-registry.smarthealthit.org/StructureDefinition/oauth-uris"
                "extension": [
                    {
                        "url": "authorize",
                        "valueUri": "https://localhost:8443/auth/realms/test/protocol/openid-connect/auth"
                    },
                    {
                        "url": "token",
                        "valueUri": "https://localhost:8443/auth/realms/test/protocol/openid-connect/token"
                    },
                    {
                        "url": "register",
                        "valueUri": "https://localhost:8443/auth/realms/test/clients-registrations/openid-connect"
                    }
                ]
            }
        ],
        …
```

SMART on FHIR applications should use the `.well-known/smart-configuration` endpoint to determine the OAuth URLs to use for authorization,
but the entries in the Capability Statement are needed for backwards compatibility.

### 5.3.3 SMART App Launch
To support [SMART App Launch](https://www.hl7.org/fhir/smart-app-launch), the IBM FHIR Server can be used with a SMART-enabled authorization server. For an example of a SMART-enabled Authorization Server, see the [Alvearie Keycloak extensions for FHIR](https://github.com/Alvearie/keycloak-extensions-for-fhir).

The OAuth configuration described in the previous sections will restrict API access to clients with a valid access token.
However, SMART defines additional access controls via OAuth 2.0 scopes and context parameters.

To enforce authorization policy on the server, drop the `fhir-smart` module into the server's userlib directory.

This component uses the IBM FHIR Server's PersistenceInterceptor feature to automatically scope searches to the compartments for which the user has access (as indicated by a special `patient_id` claim in the access token).

Additionally, before returning resources to the client, the `fhir-smart` component performs authorization policy enforcement based on the list of SMART scopes included in the token's `scope` claim and the list of patient compartments in the `patient_id` claim.

For an example of using the IBM FHIR Server together with a SMART-enabled Keycloak authorization server, please see the data-access pattern at https://github.com/Alvearie/health-patterns/tree/main/data-access.

## 5.4 Custom HTTP Headers
IBM FHIR Server Supports the following custom HTTP Headers:

| Header Name      | Description                |
|------------------|----------------------------|
|`X-FHIR-TENANT-ID`|Specifies which tenant config should be used for the request. Default is `default`. The header name can be overridden via config property `fhirServer/core/tenantIdHeaderName`.|
|`X-FHIR-DSID`|Specifies which datastore config should be used for the request. Default is `default`. The header name can be overridden via config property `fhirServer/core/dataSourceIdHeaderName`.|
|`X-FHIR-FORWARDED-URL`|The original (user-facing) request URL; used for constructing absolute URLs within the server response. Only enabled when explicitly configured in the default fhir-server-config.json. If either the config property or the header itself is missing, the server will use the actual request URL. The header name can be overridden via config property `fhirServer/core/originalRequestUriHeaderName`. Note that `fhirServer/core/externalBaseUrl` overrides the `X-FHIR-FORWARDED-URL` and is used to construct the absolute URL. Also note that the base URL's value must not include a path segment that matches any FHIR resource type name (case-sensitive). For example, "https://example.com" or "https://example.com/my/patient/api" are fine, but "https://example.com/my/Patient/api" is not.|
|`X-FHIR-UPDATE-IF-MODIFIED`|When set to true, for update and patch requests, the server will perform a resource comparison and only perform the update if the contents of the resource have changed. For all other values, the update will be executed as normal.|

# 6 Related topics
For more information about topics related to configuring a FHIR server, see the following documentation:
*   [Java 11 Keytool documentation](https://docs.oracle.com/en/java/javase/11/tools/keytool.html)
*   [WebSphere Liberty documentation: Configuring your web application and server for client certificate authentication](https://www.ibm.com/support/knowledgecenter/SSEQTP_liberty/com.ibm.websphere.wlp.doc/ae/twlp_sec_clientcert.html)
*   [Java EE 7 Tutorial, Chapter 50](https://docs.oracle.com/javaee/7/tutorial/security-advanced002.htm#GLIEN)
*   [WebSphere Liberty documentation: Defining an OAuth Service Provider](https://www.ibm.com/support/knowledgecenter/SSEQTP_liberty/com.ibm.websphere.wlp.doc/ae/twlp_oauth_defining.html)
*   [WebSphere Liberty documentation: Configuring an Open ID Connect Client in liberty](https://www.ibm.com/support/knowledgecenter/SSEQTP_liberty/com.ibm.websphere.wlp.doc/ae/twlp_config_oidc_rp.html)

<hr/>

- <b name="f1">1</b>

    The fhir-server-config.json file contains configuration information associated with the FHIR server. The global configuration is located in `WLP_HOME/wlp/usr/servers/fhir-server/config/default/fhir-server-config.json`, with tenant-specific configuration contained in `config/TENANT_ID/fhir-server-config.json`. [↩](#a1)

- <b name="f2">2</b>

    When running database-related commands (e.g. createDB.sh, liquibase, etc.) you'll need to make sure that the Db2-related executables are in your PATH, and also that you are logged in as a user that has the necessary authority to create the database and/or create the schema.  Normally, if you log in as the Db2 administrative user (typically "db2inst1") then you should be fine. [↩](#a2)

- <b id="f3">3</b>

    The names of these request headers are configurable within the FHIR server's fhir-server-config.json file.  For more information, see [Section 5.1 Configuration properties reference](#51-configuration-properties-reference). [↩](#a3)

- <b id="f4">4</b>

    For more information on multi-tenant support, including multi-tenant configuration properties, jump to [Section 4.9 Multi-Tenancy](#49-multi-tenancy). [↩](#a4)

- <b id="f5">5</b>

    An external reference is a reference to a resource which is meaningful outside a particular request bundle.  The value typically includes the resource type and the resource identifier, and could be an absolute or relative URL. Examples:  `https://fhirserver1:9443/fhir-server/api/v4/Patient/12345`, `Patient/12345`, etc.  Requiring relative URLs to include a valid resource type can be configured via the `fhirServer/core/checkReferenceTypes` config property. For more information, see [Section 5.1 Configuration properties reference](#51-configuration-properties-reference). [↩](#a5)

- <b id="f6">6</b>

    A local reference is a reference used within a request bundle that refers to another resource within the same request bundle and is meaningful only within that request bundle. A local reference starts with `urn:`. [↩](#a6)

- <b id="f7">7</b>

    Keystore and truststore files have the same basic structure. They both provide a secure means for storing certificates. Typically, we think of a keystore as a file that contains certificates that consist of a private/public key pair, whereas a truststore contains certificates that consist of a public key or trusted certificates. [↩](#a7)

- <b id="f8">8</b>

    While the instructions here show examples of creating self-signed certificates, in reality the FHIR Server deployer will likely need to use certificates that have been signed by a Certificate Authority (CA) such as Verisign, etc. [↩](#a8)

- <b id="f9">9</b>

    The _keytool_ command is provided as part of the Java 11 JRE.  The command can be found in $JAVA_HOME/jre/bin. [↩](#a9)

- <b id="f10">10</b> These instructions assume the use of a basic user registry in the server.xml file.  If you are instead using an LDAP registry, then the entire DN associated with the client certificate must match the DN of a user in the LDAP registry. [↩](#a10)

- <b id="f11">11</b>

    For the JAX-RS 2.0 Client API, you would call the ClientBuilder.truststore() method. [↩](#a11)

- <b id="f12">12</b>

    For the JAX-RS 2.0 Client API, you would call the ClientBuilder.keystore() method. [↩](#a12)

[a]:https://www.ibm.com/support/knowledgecenter/en/SSD28V_9.0.0/com.ibm.websphere.wlp.core.doc/ae/cwlp_pwd_encrypt.html

<hr/>

FHIR&reg; is the registered trademark of HL7 and is used with the permission of HL7.
