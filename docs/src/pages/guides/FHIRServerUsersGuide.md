---
layout: post
title:  IBM FHIR Server User's Guide
description: IBM FHIR Server User's Guide
Copyright: years 2017, 2021
lastupdated: "2021-01-21"
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
- [4 Customization](#4-customization)
  * [4.1 Extended operations](#41-extended-operations)
  * [4.2 Notification Service](#42-notification-service)
  * [4.3 Persistence interceptors](#43-persistence-interceptors)
  * [4.4 Resource validation](#44-resource-validation)
  * [4.5 "Update/Create" feature](#45-updatecreate-feature)
  * [4.6 FHIR client API](#46-fhir-client-api)
  * [4.7 FHIR command-line interface (fhir-cli)](#47-fhir-command-line-interface-fhir-cli)
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
0.  Prereqs: The IBM FHIR Server requires Java 8 or higher and has been tested with OpenJDK 8, OpenJDK 11, and the IBM SDK, Java Technology Edition, Version 8. To install Java on your system, we recommend downloading and installing OpenJDK 8 from https://adoptopenjdk.net/.

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
    * By default, the FHIR server is installed with the JDBC persistence layer configured to use an Embedded Derby database. The default config will automatically bootstrap this database and so this step can be skipped. For any other configuration, note the database host and port and, if necessary, create a user with privileges for deploying the schema.

7.  Create and deploy the IBM FHIR Server database schema as needed:
    * By default, the FHIR server is installed with the JDBC persistence layer configured to use an Embedded Derby database. The default config will automatically bootstrap this database and so this step can be skipped. For any other configuration, use the `fhir-persistence-schema` module to create and deploy the database schema.
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

    One should see `All OK` in the response.  The preceding command should produce output similar to the following:
```
    {
    "resourceType": "OperationOutcome",
    "issue": [
        {
            "severity": "information",
            "code": "informational",
            "details": {
                "text": "All OK"
            }
        }
    ]
    }
```

For more information about the capabilities of the implementation, see [Conformance](https://ibm.github.io/FHIR/Conformance).

## 2.2 Upgrading an existing server
The IBM FHIR Server does not include an upgrade installer. To upgrade a server to the next version, you can run the installer on a separate server, and then copy the resulting configuration files over to the existing server.

To manage database updates over time, the IBM FHIR Server uses custom tools from the `fhir-database-utils` project. Through the use of a metadata table, the database utilities can detect the currently installed version of the database and apply any new changes that are needed to bring the database to the current level.

Complete the following steps to upgrade the server:

1. Run the fhir-installer on a separate server.
2. Configure the new server as appropriate (`fhir-server/server.xml` and anything under the `fhir-server/configDropins`, `fhir-server/config`, and `fhir-server/userlib` directories).
3. Back up your database.  
4. Run the migration program (see [Section 3.3.1.1.2 Db2](#33112-db2)).  
5. Disable traffic to the old server and enable traffic to the new server  

## 2.3 Docker
The IBM FHIR Server includes a Docker image [ibmcom/ibm-fhir-server](https://hub.docker.com/r/ibmcom/ibm-fhir-server).

Note, logging for the IBM FHIR Server docker image is to stderr and stdout, and is picked up by Logging agents.

The IBM FHIR Server is configured using Environment variables using: 

| Environment Variable | Description |
|----------------------|-------------|
|`DISABLED_OPERATIONS`|A comma-separated list of operations which are disabled on the IBM FHIR Server, for example, `validate,import`. Note, do not include the dollar sign `$`|

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

Optionally, the values can be encoded via the Liberty `securityUtility` command. For example, to encode a string value with the default `{xor}` encoding, run the following command:
```
<WLP_HOME>/bin/securityUtility encode stringToEncode
```

The output of this command can then be copied and pasted into your `server.xml` or `fhir-server-config.json` file as needed. The `fhir-server-config.json` does not support the securityUtility's `{aes}` encoding at this time, but per the limits to protection through password encryption<sup>[a]</sup>, this encoding does not provide significant additional security beyond `exclusive or` (XOR) encoding.

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

The IBM FHIR Server supports the ability to compute and store compartment membership values during ingestion. Once stored, these values can help accelerate compartment-related search queries. To use this feature, update the IBM FHIR Server to at least version 4.5.1 and run a reindex operation as described in the [fhir-bucket](https://github.com/IBM/FHIR/tree/master/fhir-bucket) project [README](https://github.com/IBM/FHIR/blob/master/fhir-bucket/README.md). The reindex operation reprocesses the resources stored in the database, computing and storing the new compartment reference values. After the reindex operation has completed, add the `useStoredCompartmentParam` configuration element to the relevant tenant fhir-server-config.json file to allow the search queries to use the pre-computed values:

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

The IBM FHIR Server is delivered with a default configuration that is already configured to use the JDBC persistence layer implementation with an Embedded Derby database. This provides the easiest out-of-the-box experience since it requires very little setup. The sections that follow in this chapter will focus on how to configure the JDBC persistence layer implementation with either Embedded Derby or Db2.

### 3.3.1 Configuring the JDBC persistence layer
#### 3.3.1.1 Database preparation
Before you can configure the FHIR server to use the JDBC persistence layer implementation, you first need to prepare the database. This step depends on the database product in use.

##### 3.3.1.1.1 Embedded Derby (default)
If you are configuring the FHIR server to use a single embedded Derby database, then you can configure it to create (bootstrap) the database and the schema and tables during startup. To configure the FHIR server to “bootstrap” the database in this way, modify the `fhirServer/persistence/jdbc/bootstrapDb` property in `fhir-server-config.json` as in the following example:

```
    {
        "fhirServer":{
            …
            "persistence":{
                …
                "jdbc":{
                    "bootstrapDb":true,
                    …
                },
            …
            }
        }
    }
```

This database bootstrap step is only performed for a Derby database.

The default configuration above assumes use of the IBM FHIR Server proxy datasource. To use standard JEE datasources instead you need to explicitly disable the proxy datasource behavior and provide a base name used by the bootstrap mechanism to calculate the JNDI names.

```
    {
        "fhirServer":{
            "persistence": {
                ...
                "jdbc": {
                    "bootstrapDb": true,
                    "enableProxyDatasource": false,
                    "bootstrapDataSourceBase": "jdbc/bootstrap"
                    ...
                },
                "datasources": {
                    "default": {
                        "jndiName": "jdbc/bootstrap_default_default",
                        "type": "derby",
                        "currentSchema": "APP"
                    },
                    ...
                }
            }
        }
    }
```

Property Name | Type | Default Value | Description
------------- | ---- | ------------- | --------------
bootstrapDb   | Boolean | false | Bootstrap the Derby built-in databases
enableProxyDatasource   | Boolean | true | Use IBM FHIR Server proxy datasource
bootstrapDataSourceBase | String | | JNDI base name prefix for bootstrapped databases
jndiName | String | | The JNDI name referencing the JEE datasource to use for the fhir-server-config datasource (required for bootstrapped Derby databases)

The "jndiName" property value points to a JEE datasource which must also be configured. JEE datasources are typically defined in the Liberty Profile '.xml' files added to configDropins/overrides, for example datasource.xml:

```
<server>
    <!-- ============================================================== -->
    <!-- This datasources is used by the FHIR server Derby bootstrap  -->
    <!-- process. We have to name them differently so they do not       -->
    <!-- clash with other datasources which may be assigned JNDI names  -->
    <!-- like "jdbc/fhir_default_default"                               -->
    <!-- ============================================================== -->
    <dataSource id="fhirDatasourceBootstrapDefaultDefault" jndiName="jdbc/bootstrap_default_default" type="javax.sql.XADataSource" statementCacheSize="200" syncQueryTimeoutWithTransactionTimeout="true">
        <jdbcDriver javax.sql.XADataSource="org.apache.derby.jdbc.EmbeddedXADataSource" libraryRef="fhirSharedLib"/>
            <properties.derby.embedded createDatabase="create" databaseName="derby/fhirDB"/>
        <connectionManager maxPoolSize="50" minPoolSize="10"/>
    </dataSource>
    ...
```

When bootstrapping is enabled, four Derby databases are created to help demonstrate the IBM FHIR Server support for multiple tenants and multiple datastores per tenant.

##### 3.3.1.1.2 Db2
If you configure the FHIR server to use an IBM Db2 database, you must:

1. create the database if it doesn't already exist

2. execute the `fhir-persistence-schema` utility to create the necessary schemas (tables, indices, stored procedures, etc) and tenants

3. configure the server with the tenantKey generated in step number 2.

An executable `fhir-persistence-schema` jar can be downloaded from the project's [Releases tab](https://github.com/IBM/FHIR/releases) and documentation can be found at https://github.com/IBM/FHIR/tree/master/fhir-persistence-schema.

For a detailed guide on configuring IBM Db2 on Cloud for the IBM FHIR Server, see [DB2OnCloudSetup](https://ibm.github.io/FHIR/guides/DB2OnCloudSetup).

##### 3.3.1.1.3 PostgreSQL
If you configure the FHIR server to use a PostgreSQL database, you must:

1. create the database if it doesn't already exist

2. execute the `fhir-persistence-schema` utility with a db-type of `postgresql` to create the necessary schemas (tables, indices, functions, etc)

An executable `fhir-persistence-schema` jar can be downloaded from the project's [Releases tab](https://github.com/IBM/FHIR/releases) and documentation can be found at https://github.com/IBM/FHIR/tree/master/fhir-persistence-schema.


##### 3.3.1.1.4 Other

To enable the IBM FHIR Server to work with other relational database systems, see
https://ibm.github.io/FHIR/guides/BringYourOwnPersistence#adding-support-for-another-relational-database

#### 3.3.1.2 FHIR server configuration
The IBM FHIR Server persistence configuration is split between two files:  `fhir-server-config.json` and `server.xml`.

1.  The value of the `fhirServer/persistence/factoryClassname` property in `fhir-server-config.json` is used to instantiate a FHIRPersistence object. By default, the server is configured to use the FHIRPersistenceJDBCFactory:
```
    {
        "fhirServer": {
            …
            "persistence": {
                "factoryClassname": "com.ibm.fhir.persistence.jdbc.FHIRPersistenceJDBCFactory",
                …
            }
    }
```

2.  When the FHIRPersistenceJDBCFactory is in use, the `fhirServer/persistence/jdbc/dataSourceJndiName` property in `fhir-server-config.json` specifies the JNDI name of the target datasource. By default, the server uses a dataSourceJndiName of `jdbc/fhirProxyDataSource`:
```
    {
        "fhirServer": {
            …
            "persistence": {
                …
                "jdbc": {
                    …
                    "dataSourceJndiName": "jdbc/fhirProxyDataSource"
                }
                …
            }
    }
```
    The `jdbc/fhirProxyDataSource` datasource is defined in the server's `server.xml` config file and, by default, specifies the `FHIRProxyXADataSource` which supports "Liberty-managed" distributed transactions across disaparate datasources defined in the `fhir-server-config.json` config.

3.  When the FHIRProxyXADataSource is in use, modify the `fhirServer/persistence/datasources` property group to reflect the datastore(s) that you want to use. The following example defines the `default` datastore as an embedded derby database located in `wlp/usr/servers/fhir-server/derby/fhirDB`:
```
    {
        "fhirServer":{
            …
            "persistence":{
                …
                "datasources": {
                    "default": {
                        "type": "derby",
                        "connectionProperties": {
                            "databaseName": "derby/fhirDB"
                        }
                    },
                …
            }
    }
```

The next example defines the `default` datastore as a Db2 database accessible on the `db2server1` host:
```
    {
        "fhirServer":{
            …
            "persistence":{
                "datasources": {
                    "default": {
                        "tenantKey": "<the-base64-tenant-key>",
                        "type": "db2",
                        "hints" : {
                            "search.reopt": "ONCE"
                        },
                        "connectionProperties": {
                            "serverName": "db2server1",
                            "portNumber": 50000,
                            "user": "db2inst1",
                            "password": "********",
                            "databaseName": "FHIRDB",
                            "currentSchema": "FHIRDATA",
                            "driverType": 4
                        }
                    }
                }
            …
            }
        }
    }
```

For more information on how to configure datastore properties, see [Section 3.3.2.2 Datastore configuration examples](#3422-datastore-configuration-examples).

Since release 4.3.2 you can use the `search.reopt` query optimizer hint (shown above) to improve the performance of certain search queries involving multiple search parameters. This optimization is currently only available for Db2. Valid values are "ALWAYS" and "ONCE". See Db2 documentation for `REOPT` for more details.

### 3.3.2 Properties-based datastore configuration

Normally, a Liberty application that uses one or more JDBC datastores will require a datasource to be defined within the Liberty server.xml file for each database. One drawback to this approach is that each of the datasources are statically defined in the 'server.xml' file, which means that any updates (modifications, additions, etc.) will require a server re-start.

As part of it's multi-tenant support, the IBM FHIR Server provides an alternate mechanism which consists of a single “proxy datasource” along with a set of properties configured in the `fhir-server-config.json` file. This proxy datasource can be used by the JDBC persistence layer implementation to establish connections to either Derby or Db2 databases. This approach allows for new datastores to be configured without the need to restart the FHIR server.

Since issue #916, the FHIR server supports standard JDBC datasources defined in the server '.xml' files. Datasource elements should not be defined in the main 'server.xml' file but instead should be defined in the 'configDropins/overrides' directory. See the Liberty Profile Server configuration guide for more details and general guidance on creating modular configurations.

The FHIRProxyXADataSource remains the default strategy for defining datasources in the current release. However, the standard JDBC datasource configuration is now considered the preferred approach due to benefits it brings in terms of configuring pool sizes, transaction recovery and monitoring.

#### 3.3.2.1 Proxy datasource
The FHIR server's proxy datasource allows us to configure a single statically-defined datasource in the Liberty 'server.xml' file, and then dynamically configure each of the datastores to be used by the FHIR server within the `fhir-server-config.json` file. The datasource definition within `server.xml` looks like this:

```
<dataSource id="fhirProxyDataSource" jndiName="jdbc/fhirProxyDataSource" type="javax.sql.XADataSource">
    <jdbcDriver libraryRef="fhirSharedLib"
       javax.sql.XADataSource="com.ibm.fhir.persistence.proxy.FHIRProxyXADataSource" />
</dataSource>
```

When the proxy datasource is obtained via a JNDI lookup by the JDBC persistence layer, and then its “getConnection()” method is called, the proxy datasource will use the current tenant-id and datastore-id to retrieve the configuration properties from the `fhir-server-config.json` file. The result will be the instantiation (or a successful cache lookup) of the appropriate XADataSource implementation class (according to the `type` field in the datastore configuration) and then a new connection will be obtained from it.

The proxy datasource relies on the presence of the tenant-id and datastore-id information within the thread-local `FHIRRequestContext` information. In a simple configuration of the FHIR server that involves the use of the JDBC persistence layer implementation, the `FHIRRequestContext` information is obtained via request headers from the incoming REST API request. The tenant-id is obtained from the `X-FHIR-TENANT-ID` request header and the datastore-id is obtained from the `X-FHIR-DSID` request header<sup id="a3">[3](#f3)</sup>.

#### 3.3.2.2 Standard JDBC Datasources

To use standard JDBC datasources, disable the proxy datasource behavior by setting the 'enableProxyDatasource' flag to false (default is true):

```
    {
        "fhirServer": {
            …
            "persistence": {
                …
                "jdbc": {
                    …
                    "enableProxyDatasource": false
                }
                …
            }
    }
```

The 'dataSourceJndiName' property can be removed as it is only used when 'enableProxyDatasource' is true. The 'connectionProperties' element can also be removed (these properties are defined in the Liberty JDBC datasource definition). By default, the JNDI name of the datasource is `jdbc/fhir_<tenantId>_<dsId>` where `<tenantId>` and `<dsId>` represent the tenant and datastore ids respectively. The JNDI address of the datasource can also be provided explicitly using the 'jndiName' property as shown in the following example:

```
{
    "fhirServer":{
        …
        "persistence":{
            "datasources": {
                "default": {
                    "tenantKey": "<the-base64-tenant-key>",
                    "jndiName": "jdbc/fhir_default_default",
                    "type": "db2",
                    "hints" : {
                        "search.reopt": "ONCE"
                    }
                }
            }
        …
        }
    }
}
```

When configured explicitly, the jndiName does not have to follow the standard naming convention, although this is not recommended.

Continuing the above example, configure a drop-in server configuration file 'configDropins/overrides/datasource.xml' as follows:

```
<server>
    <dataSource id="fhirDatasourceDefaultDefault" jndiName="jdbc/fhir_default_default" type="javax.sql.XADataSource" statementCacheSize="200" syncQueryTimeoutWithTransactionTimeout="true">
        <jdbcDriver javax.sql.XADataSource="com.ibm.db2.jcc.DB2XADataSource" libraryRef="fhirSharedLib"/>
            <properties.db2.jcc
                 serverName="db2"
                 portNumber="*****"
                 user="*****"
                 password="*****"
                 databaseName="*****"
                 currentSchema="*****"
                 driverType="4"
             />
        />
        <connectionManager maxPoolSize="200" minPoolSize="40"/>
    </dataSource>
</server>
```

This file is picked up when the server starts as indicated by the following AUDIT message:

```
[AUDIT   ] CWWKG0093A: Processing configuration drop-ins resource: /fhir/wlp/usr/servers/fhir-server/configDropins/overrides/datasource.xml
```

A special case exists for configuring the Derby 'bootstrapped' datasources. To avoid naming conflicts, these datasources must use custom JNDI names, not the default naming pattern. For example, a Derby datasource defined in `configDropins/disabled/datasource-derby.xml` might look like this:

```
<server>
    <dataSource id="fhirDatasourceBootstrapDefaultDefault" jndiName="jdbc/bootstrap_default_default" type="javax.sql.XADataSource" statementCacheSize="200" syncQueryTimeoutWithTransactionTimeout="true">
        <jdbcDriver javax.sql.XADataSource="org.apache.derby.jdbc.EmbeddedXADataSource" libraryRef="fhirSharedLib"/>
            <properties.derby.embedded createDatabase="create" databaseName="derby/fhirDB"/>
        <connectionManager maxPoolSize="50" minPoolSize="10"/>
    </dataSource>
</server>
```

Note the use of 'jdbc/bootstrap'. How this is used is described below. The corresponding definition in 'fhir-server-config.json' must be configured like this:

```
    {
        "fhirServer":{

            "persistence": {
                ...
                "jdbc": {
                    "bootstrapDb": true,
                    "enableProxyDatasource": false,
                    "bootstrapDataSourceBase": "jdbc/bootstrap"
                    ...
                },
                "datasources": {
                    "default": {
                        "jndiName": "jdbc/bootstrap_default_default",
                        "type": "derby",
                        "currentSchema": "APP"
                    },
                    ...
                }
            }
        }
    }
```

Note the introduction of the new property 'bootstrapDataSourceBase'. This value is required to correctly identify the datasources to use when bootstrapping the Derby databases. The value 'jdbc/bootstrap' is the prefix which should be common to all the Derby datasource JNDI names referencing bootstrapped Derby databases:

Reference | JNDI Name | Derby Database
--------- | --------- | --------------
default   | jdbc/bootstrap_default_default | {serverHome}/derby/fhirDB
profile   | jdbc/bootstrap_default_profile | {serverHome}/derby/profile
reference | jdbc/bootstrap_default_reference | {serverHome}/derby/reference
study1    | jdbc/bootstrap_default_study1 | {serverHome}/derby/study1

Where {serverHome} is the 'wlp/usr/servers/fhir-server' directory containing the 'server.xml' file.

#### 3.3.2.3 Datastore configuration examples
To understand how the configuration properties are defined for one or more datastores, let's start off with a couple of examples.

##### 3.3.2.3.1 Example 1
Here is a simple example of a single (default) Derby datastore using the proxy mechanism:
```
{
    "fhirServer":{
        "persistence":{
            "factoryClassname":"com.ibm.fhir.persistence.jdbc.FHIRPersistenceJDBCFactory",
            "jdbc": {
                "dataSourceJndiName": "jdbc/fhirProxyDataSource"
            },
            ...
            "datasources": {
                "default": {
                    "type": "derby",
                    "connectionProperties": {
                        "databaseName": "derby/fhirDB"
                    }
                }
            },
            "jdbc":{
                “bootstrapDb":true,
                "dataSourceJndiName": "jdbc/fhirDB"
            },
        }
    }
}
```
In this example, we define an embedded Derby database named `derby/fhirDB` (a location relative to the `<WLP_HOME>/usr/servers/fhir-server` directory. The datastore-id associated with this datastore is `default`, which is the value that is used if no `X-FHIR-DSID` request header is found in the incoming request. So, when only a single database is being used, it's wise to leverage the `default` datastore-id value to allow REST API consumers to avoid having to set the `X-FHIR-DSID` request header on each request.

##### 3.3.2.3.2 Example 2
This example shows a slightly more complex scenario. In this scenario, the `acme` tenant would like to store data in one of two study-specific Db2 databases with datastore-id values `study1` and `study2`. All resource types pertaining to a given study will be stored in that study's database so there's no need for a proxy persistence layer or routing rules, and so forth.

Furthermore, the REST API consumers associated with Acme applications will be coded to always set the `X-FHIR-TENANT-ID` request header to be `acme` and the `X-FHIR-DSID` request header to the specific datastore-id associated with each request (either `study1` or `study2`). In this case, the following properties would be configured within the “acme” tenant's `fhir-server-config.json` file<sup id="a4">[4](#f4)</sup> (`$⁠{server.config.dir}/config/acme/fhir-server-config.json`):
```
{
    "__comment":"Acme's FHIR server configuration",
    "fhirServer":{
        …
        "persistence":{
            "factoryClassname":"com.ibm.fhir.persistence.jdbc.FHIRPersistenceJDBCFactory",
            "jdbc": {
                "dataSourceJndiName": "jdbc/fhirProxyDataSource"
            },
            …
            "datasources": {
                "study1": {
                    "tenantKey": "<the-base64-tenant-key>",
                    "type": "db2",
                    "connectionProperties": {
                        "serverName": "dbserver1",
                        "portNumber": "50000",
                        "user": "db2inst1",
                        "password": "change-password",
                        "database": "ACMESTUDY1",
                        "currentSchema": "DB2INST1"
                    }
                },
                "study2": {
                    "tenantKey": "<the-base64-tenant-key>",
                    "type": "db2",
                    "connectionProperties": {
                        "serverName": "dbserver1",
                        "portNumber": "50000",
                        "user": "db2inst1",
                        "password": "change-password",
                        "database": "ACMESTUDY2",
                        "currentSchema": "DB2INST1"
                    }
                }
            }
            …
        }
    }
}
```

##### 3.3.2.3.3 Example 3
Example 3 implements the same configuration as Example 2 using standard Liberty datasource definitions.

```
{
    "__comment":"Acme's FHIR server configuration",
    "fhirServer":{
        …
        "persistence":{
            "factoryClassname":"com.ibm.fhir.persistence.jdbc.FHIRPersistenceJDBCFactory",
            "jdbc": {
                "enableProxyDatasource": false
            },
            …
            "datasources": {
                "study1": {
                    "tenantKey": "<the-base64-tenant-key>",
                    "type": "db2"
                },
                "study2": {
                    "tenantKey": "<the-base64-tenant-key>",
                    "type": "db2"
                }
            }
            …
        }
    }
}
```

Note that because this example is using the default FHIR server naming scheme for datasource JNDI names, there is no need to include the 'jndiName' property, although you can specify it should you wish to make the mapping clear. The "type" must match the actual database type referenced by the datasource definition. If the type does not match, the behavior is undefined.

The datasource definitions for the 'acme' tenant are defined as a drop-in configuration in '{serverHome}/configDropins/datasources-acme.xml':

```
<server>
    <dataSource id="fhirDatasourceAcmeStudy1" jndiName="jdbc/fhir_acme_study1" type="javax.sql.XADataSource" statementCacheSize="200" syncQueryTimeoutWithTransactionTimeout="true">
        <jdbcDriver javax.sql.XADataSource="com.ibm.db2.jcc.DB2XADataSource" libraryRef="fhirSharedLib"/>
            <properties.db2.jcc
                 serverName="dbserver1"
                 portNumber="50000"
                 user="db2inst1"
                 password="change-password"
                 databaseName="ACMESTUDY1"
                 currentSchema="DB2INST1"
                 driverType="4"
             />
        />
        <connectionManager maxPoolSize="200" minPoolSize="40"/>
    </dataSource>

    <dataSource id="fhirDatasourceAcmeStudy2" jndiName="jdbc/fhir_acme_study2" type="javax.sql.XADataSource" statementCacheSize="200" syncQueryTimeoutWithTransactionTimeout="true">
        <jdbcDriver javax.sql.XADataSource="com.ibm.db2.jcc.DB2XADataSource" libraryRef="fhirSharedLib"/>
            <properties.db2.jcc
                 serverName="dbserver1"
                 portNumber="50000"
                 user="db2inst1"
                 password="change-password"
                 databaseName="ACMESTUDY2"
                 currentSchema="DB2INST1"
                 driverType="4"
             />
        />
        <connectionManager maxPoolSize="200" minPoolSize="40"/>
    </dataSource>

</server>
```

In the above configuration, each datasource gets its own connection pool with properties defined by the 'connectionManager' element.


#### 3.3.2.4 Datastore configuration reference
Within each tenant's `fhir-server-config.json` file, the `fhirServer/persistence/datasources` property specifies a map that relates the datastore-id value to a group of properties used to establish a connection to that datasource (database). Each datasource's property group contains properties named `type` and `connectionProperties`. Here is an example depicting a Db2 datasource definition:
```
{
    "fhirServer":{
        "persistence":{
            "factoryClassname":"com.ibm.fhir.persistence.jdbc.FHIRPersistenceJDBCFactory",
            "datasources": {
                "study1": {
                    "tenantKey": "<the-base64-tenant-key>",
                    "type": "db2",
                    "connectionProperties": {
                        "serverName": "mydb2server",
                        "portNumber": 50000,
                        "user": "db2inst1",
                        "password": "********",
                        "databaseName": "FHIRDB",
                        "currentSchema": "FHIR1",
                        "driverType": 4
                    }
                }
            }
        }
    }
}
```

The `type` property indicates the database type (currently only `db2` or `derby`).

The `connectionProperties` property is a set of driver-specific properties needed to connect to an instance of that database type. For a Db2-related datasource definition, any bean property supported by the `DB2XADataSource` class can be specified within the `connectionProperties` property group. For a discussion of the specific properties that can be used to configure a `DB2XADataSource` instance, see the [Db2 Knowledge Center](https://www.ibm.com/support/knowledgecenter/SSEPGG_11.1.0/com.ibm.db2.luw.apdv.java.doc/src/tpc/imjcc_rjvdsprp.html).

For a Derby-related datasource definition, any bean property supported by the `EmbeddedXADataSource` class can be specified within the `connectionProperties` property group. For more information about the properties supported by the `EmbeddedXADataSource` class, and its super classes, see the [Apache Derby documentation](https://db.apache.org/derby/docs/10.13/publishedapi/org/apache/derby/jdbc/EmbeddedXADataSource.html).

To disable the multitenant feature for a particular offering add to your `fhirServer/persistence/datasources` entry `multitenant` and set false to disable, and true to enable, only for Db2 is the default set to true.

#### 3.3.2.4 Database Access TransactionManager Timeout
The TransactionManager controls the timeout of database queries.  

To expand the transaction timeout value, one can copy over the `transaction-manager-long.xml` from the WLP configDropins from `/disabled` to `/overrides` folder, or set the Environment variable `FHIR_TRANSACTION_MANAGER_TIMEOUT=120s` or enter the value in the server.env file at the root of the WLP instance.  The value should be at least as granular as seconds or minutes.  Example values are 120s or 2m.  You should not lower this below 120s.

# 4 Customization
You can modify the default server implementation by taking advantage of the IBM FHIR server's extensibility. The following extension points are available:
 * Custom operations framework:  The IBM FHIR Server defines an operations framework that builds on the FHIR OperationDefinition resource in order to extend the FHIR REST API with custom endpoints.
 * Pluggable audit service:  Logging and auditing options including Cloud Auditing Data Federation (CADF) over Apache Kafka.
 * Persistence interceptors:  Intercept requests before and/or after each persistence action.
 * Resource validation:  FHIRPath-based validation of FHIR resources on create or update with the ability to extend the system with custom constraints.

## 4.1 Extended operations
In addition to the standard REST API (create, update, search, and so forth), the IBM FHIR Server supports the FHIR operations framework as described in the [FHIR specification]( https://www.hl7.org/fhir/r4/operations.html).

### 4.1.1 Packaged operations
The FHIR team provides implementations for the standard `$validate` and `$document` operations, as well as a custom operation named `$healthcheck`, which queries the configured persistence layer to report its health.

No other extended operations are packaged with the server at this time, but you can extend the server with your own operations.

#### 4.1.1.1 $validate
The `$validate` operation checks whether the attached content would be acceptable either generally, or as a create, update, or delete against an existing resource instance or type.

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
The FHIR server provides a notification service that publishes notifications about persistence events, specifically _create_ and _update_ operations. The notification service can be used by other Healthcare components to trigger specific actions that need to occur as resources are being updated in the FHIR server datastore.

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

The following JSON is an example of a serialized notification event:
```
{
  "lastUpdated":"2016-06-01T10:36:23.232-05:00",
  "location":"Observation/3859/_history/1",
  "operationType":"create",
  "resourceId":"3859",
  "resource":{ …<contents of resource>… }
}
```

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
The Kafka implementation of the notification service will publish notification event messages to a Kafka topic. To configure the Kafka notification publisher, configure properties in the `fhir-server-config.json` file as indicatesd in the following example:

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
The FHIR server supports a persistence interceptor feature that enables users to add their own logic to the REST API processing flow around persistence events. This could be used to enforce application-specific business rules associated with resources. Interceptor methods can be called immediately before or after _create_ and _update_ persistence operations.

### 4.3.1 FHIRPersistenceInterceptor interface
A persistence interceptor implementation must implement the `com.ibm.fhir.persistence.interceptor.FHIRPersistenceInterceptor`
interface.

Each interceptor method receives a parameter of type `FHIRPersistenceEvent`, which contains context information related to the request being processed at the time that the interceptor method is invoked. It includes the FHIR resource, security information, request URI information, and the collection of HTTP headers associated with the request.

There are two primary use cases for persistence interceptors:

1.  Enforce certain application-specific governance rules, such as making sure that a patient has signed a consent form prior to allowing his/her data to be stored in the FHIR server's datastore. In this case, the `beforeCreate` or `beforeUpdate` interceptor methods could verify that the patient has a consent agreement on file, and if not then throw a `FHIRPersistenceInterceptorException` to prevent the _create_ or _update_ persistence events from completing normally. The exception thrown by the interceptor method will be propagated back to the FHIR server request processing flow and would result in an `OperationOutcome` being returned in the REST API response, along with a `Bad Request` HTTP status code.

2.  Perform some additional processing steps associated with a _create_ or _update_ persistence event, such as additional audit logging. In this case, the `afterCreate` and `afterUpdate` interceptor methods could add records to an audit log to indicate the request URI that was invoked, the user associated with the invocation request, and so forth.

In general, the `beforeCreate` and `beforeUpdate` interceptor methods would be useful to perform an enforcement-type action where you would potentially want to prevent the request processing flow from finishing. Conversely, the `afterCreate` and `afterUpdate` interceptor methods would be useful in situations where you need to perform additional steps after the _create_ or _update_ persistence events have been performed.

### 4.3.2 Implementing a persistence interceptor
To implement a persistence interceptor, complete the following steps:

1.  Develop a Java class which implements the `FHIRPersistenceInterceptor` interface.
2.  Store the fully-qualified classname of your interceptor implementation class in a file called :

      `META-INF/services/com.ibm.fhir.persistence.interceptor.FHIRPersistenceInterceptor`

    Here's an example of the file contents:

    `com.ibm.mysolution.MyInterceptor`

3.  Copy your jar to the `<WLP_HOME>/usr/servers/fhir-server/userlib` directory so that it is accessible to the FHIR server via the classpath (the `server.xml` file contains a library element that defines this directory as a shared library).

4.  Re-start the FHIR server.

## 4.4 Resource validation

### 4.4.1 HL7 spec-defined validation support
The FHIR specification provides a number of different validation resources including:

1.  XML Schemas
2.  ISO XML Schematron rules
3.  Structure Definitions / Profiles for standard resource types, data types and built-in value sets

The FHIR server validates incoming resources for create and update interactions using the resource definitions and their corresponding FHIRPath constraints. Additionally, the FHIR server provides the following `$validate` operation that API consumers can use to POST resources and get validation feedback:
 `POST <base>/Resource/$validate`

### 4.4.2 User-provided validation
The IBM FHIR Server can be extended with custom profile validation. This allows one to apply validation rules on the basis of the `Resource.meta.profile` codes included on the resource instance.

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

It is also possible to configure a set of profiles, one or more of which a resource must claim conformance to and be successfully validated against in order to be persisted to the FHIR server. The FHIR server supports this optional behavior via the `fhirServer/resources/<resourceType>/profiles/atLeastOne` configuration parameter. If this configuration parameter is set to a non-empty list of profiles, then the FHIR server will perform the following validation, returning FAILURE if not successful:
 * Validate that at least one profile in the list is specified in the resource's `meta.profile` element
 * Validate that all profiles specified in the resource's `meta.profile` element are supported by the FHIR server
 * Validate that the resource's data conforms to all profiles specified in the resource's `meta.profile` element

If a profile in the list specified by the configuration parameter contains a version, for example `http://ibm.com/fhir/profile/partner|1.0`, then a profile of the same name specified in the resource's `meta.profile` element will only be considered a match if it contains exactly the same version. However, if a profile in the list specified by the configuration parameter does not contain a version, for example `http://ibm.com/fhir/profile/partner`, then a profile of the same name specified in the resource's `meta.profile` element will be considered a match whether it contains a version or not.

If this configuration parameter is not set or is set to an empty list, then the FHIR server will perform its standard validation.

The IBM FHIR Server pre-packages all conformance resources from the core specification.

See [Validation Guide - Optional profile support](https://ibm.github.io/FHIR/guides/FHIRValidationGuide#optional-profile-support) for a list of pre-built Implementation Guide resources and how to load them into the IBM FHIR server.

See [Validation Guide - Making profiles available to the fhir registry](https://ibm.github.io/FHIR/guides/FHIRValidationGuide#making-profiles-available-to-the-fhir-registry-component-fhirregistry) for information about how to extend the server with additional Implementation Guide artifacts.

## 4.5 “Update/Create” feature
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

## 4.6 FHIR client API

### 4.6.1 Overview
In addition to the server, we also offer a Java API for invoking the FHIR REST APIs. The IBM FHIR Client API is based on the JAX-RS 2.0 standard and provides a simple properties-driven client that can be easily configured for a given endpoint, mutual authentication, request/response logging, and more.

### 4.6.2 Maven coordinates
To use the FHIR Client from your application, specify the `fhir-client` artifact as a dependency within your `pom.xml` file, as in the following example:

```
        <dependency>
            <groupId>com.ibm.fhir</groupId>
            <artifactId>fhir-client</artifactId>
            <version>${fhir.client.version}</version>
        </dependency>
```

### 4.6.3 Sample usage
For examples on how to use the IBM FHIR Client, look for tests like `com.ibm.fhir.client.test.mains.FHIRClientSample` from the `fhir-client` project in git. Additionally, the FHIR Client is heavilly used from our integration tests in `fhir-server-test`.

## 4.7 FHIR command-line interface (fhir-cli)
The FHIR command-line interface (fhir-cli for short) is a command that can be used to invoke FHIR REST API operations from the command line. The compressed file for installing the fhir-cli tool zip is part of the FHIR server installation in `${WLP_HOME}/fhir/client/fhir-cli.zip`, and the `fhir-cli.zip` file is also available from [Bintray server](
https://dl.bintray.com/ibm-watson-health/ibm-fhir-server-releases/com/ibm/fhir/fhir-cli/).

### 4.7.1 Installing fhir-cli
Because the fhir-cli tool is intended to be used by clients that need to access the FHIR server, it has its own installation process separate from the server. To install the fhir-cli tool, complete the following steps:

1.  Obtain the `fhir-cli.zip` file from the FHIR server installation zip or Bintray.
2.  Decompress the `fhir-cli.zip` file into a directory of your choosing, for example:

    ```
    cd /mydir
    unzip fhir-cli.zip
    ```

3.  [Optional] To enable you to run fhir-cli from multiple directories, run the following command to add `fhir-cli` to your `PATH` environment variable.
    ```
    export PATH=$PATH:/mydir/fhir-cli
    ```

### 4.7.2 Configuring fhir-cli
The fhir-cli tool requires a properties file containing various configuration properties, such as the base endpoint URL, the username and password for basic authentication, amd so forth. The properties contained in this file are the same properties supported by the FHIR client API. The fhir-cli tool comes with a sample properties file named `fhir-cli.properties` which contains a collection of default property settings.

Using the sample properties file as a guide, you can create your own properties file to reflect the required endpoint configuration associated with the FHIR server endpoint that you would like to access. In the examples that follow, we'll refer to this file as `my-fhir-cli.properties`, although you can name the file anything you'd like.

### 4.7.3 Running fhir-cli
The fhir-cli tool comes with two shell scripts: `fhir-cli` (Linux&reg;) and `fhir-cli.bat` (Windows&trade;). In the examples that follow, we'll use `<fhir-cli-home>` as the location of the fhir-cli tool (that is, the `/mydir/fhir-cli` directory mentioned in preceding section).

The following examples illustrate how to invoke the fhir-cli tool:

*   Display help text
```
$ <fhir-cli-home>/fhir-cli –help

FHIR Client Command Line Interface (fhir-cli)    (c) Copyright IBM Corporation, 2016.

usage: fhir-cli [options]

Provides access to the FHIR Client API via the command line.

Options:
   -h,--help                           Display this help text
   -id,--resourceId <ID>               Use resource identifier <ID> for the operation invocation
   -o,--output <FILE>                  Write output resource to <FILE> (e.g. searchresults.json)
   -op,--operation <OPERATION>         The operation to be invoked
   -p,--properties <FILE>              Use FHIR Client properties contained in <FILE> (e.g.
                                       fhir-cli.properties)
   -qp,--queryParameter <NAME=VALUE>   Include query parameter NAME=VALUE with the operation
                                       invocation (e.g. _count=100).
   -r,--resource <FILE>                Use FHIR resource contained in <FILE> for operation
                                       invocation (e.g. patient.json)
   -t,--type <TYPE>                    Use resource type <TYPE> for the operation invocation (e.g.
                                       "Patient")
   -v,--verbose                        Display detailed output
   -vid,--versionId <VID>              Use version # <VID> for the operation invocation

OPERATION should be one of: batch | create | history | metadata | read | search | search-all |
search-post | transaction | update | validate | vread
```
*   Invoke the 'metadata' operation
```
$ <fhir-cli-home>/fhir-cli --properties my-fhir-cli.properties --operation metadata --output conformance.json

FHIR Client Command Line Interface (fhir-cli)    (c) Copyright IBM Corporation, 2016.

Invoking operation 'metadata'... done! (651ms)
Status code: 200
Response resource written to file: conformance.json
```
Note: in this example the “--output” option is used to specify that the Conformance resource should be saved in file 'conformance.json'.
*   Perform a _create_ operation
```
$ <fhir-cli-home>/fhir-cli --properties my-fhir-cli.properties --operation create --resource newpatient.json

FHIR Client Command Line Interface (fhir-cli)    (c) Copyright IBM Corporation, 2016.

Invoking operation _create_... done! (2719ms)
Status code: 201
Location URI: http://localhost:9443/fhir-server/api/v4/Patient/26b694ef-cea7-4485-a896-5ac2a1da9f64/_history/1
ETag: W/"1"
Last modified: 2016-09-13T20:51:21.048Z
```
Note: In this example, the “--resource” option is used to indicate that the contents of the new resource to be created should be read from file 'newpatient.json'.
*   Perform a 'read' operation
```
$ <fhir-cli-home>/fhir-cli --properties my-fhir-cli.properties --operation read --type Patient --resourceId 26b694ef-cea7-4485-a896-5ac2a1da9f64 -o patient1.json

FHIR Client Command Line Interface (fhir-cli)    (c) Copyright IBM Corporation, 2016.

Invoking operation 'read'... done! (321ms)
Status code: 200
ETag: W/"1"
Last modified: 2016-09-13T20:51:21.048Z
Response resource written to file: patient1.json
```
Note: the “-o” option is used as a shortcut for “--output”
*   Perform an _update_ operation
```
$ <fhir-cli-home>/fhir-cli --properties my-fhir-cli.properties --operation update --resource updatedpatient1.json
FHIR Client Command Line Interface (fhir-cli)    (c) Copyright IBM Corporation, 2016.

Invoking operation _update_... done! (2707ms)
Status code: 200
Location URI: http://localhost:9443/fhir-server/api/v4/Patient/26b694ef-cea7-4485-a896-5ac2a1da9f64/_history/2
ETag: W/"2"
Last modified: 2016-09-13T21:11:48.988Z
```
*   Perform a 'vread' operation
```
$ <fhir-cli-home>/fhir-cli --properties my-fhir-cli.properties --operation vread -t Patient -id 26b694ef-cea7-4485-a896-5ac2a1da9f64 -vid 3 -o patient1v3.json

FHIR Client Command Line Interface (fhir-cli)    (c) Copyright IBM Corporation, 2016.

Invoking operation 'vread'... done! (290ms)
Status code: 200
ETag: W/"3"
Last modified: 2016-09-13T21:18:28.412Z
Response resource written to file: patient1v3.json
```
*   Perform a 'history' operation
```
$ <fhir-cli-home>/fhir-cli --properties my-fhir-cli.properties --operation history -t Patient -id 26b694ef-cea7-4485-a896-5ac2a1da9f64 -o patient1history.json

FHIR Client Command Line Interface (fhir-cli)    (c) Copyright IBM Corporation, 2016.

Invoking operation 'history'... done! (414ms)
Status code: 200
Response resource written to file: patient1history.json
```
Note: in this example, the response resource is a Bundle containing the versions of the Patient resource.


*   Perform a 'search' operation

```
$ <fhir-cli-home>/fhir-cli --properties my-fhir-cli.properties --operation search -t Patient -qp name=Doe -o searchresults.json

FHIR Client Command Line Interface (fhir-cli)    (c) Copyright IBM Corporation, 2016.

Invoking operation 'search'... done! (543ms)
Status code: 200
Response resource written to file: searchresults.json
```

Note: in this example the `-qp` option (shortcut for `–queryParameter`) is used to specify the search criteria (that is, `name=Doe`). The response resource is a Bundle that is written to the file `searchresults.json`.

*   Perform a 'validate' operation

```
$ <fhir-cli-home>/fhir-cli --properties my-fhir-cli.properties --operation validate --resource newpatient.json

FHIR Client Command Line Interface (fhir-cli)    (c) Copyright IBM Corporation, 2016.

Invoking operation 'validate'... done! (3182ms)
Status code: 200
Response resource:

{
    "resourceType" : "OperationOutcome",
    "id" : "allok",
    "text" : {
        "status" : "additional",
        "div" : "<div xmlns=\"http://www.w3.org/1999/xhtml\"><p>All OK</p></div>"
    },
    "issue" : [ {
        "severity" : "information",
        "code" : "informational",
        "details" : {
            "text" : "All OK"
        }
    } ]
}
```

## 4.8 Using local references within request bundles
Inter-dependencies between resources are typically defined by one resource containing a field of type `Reference` which contains an _external reference_<sup id="a5">[5](#f5)</sup> to another resource. For example, an `Observation` resource could reference a `Patient` resource via the Observation's `subject` field. The value that is stored in the `Reference-type` field (for example, `subject` in the case of the `Observation` resource) could be an absolute URL, such as `https://fhirserver1:9443/fhir-server/api/v4/Patient/12345`, or a relative URL (for example, `Patient/12345`).

In order to establish a reference to a resource, you must first know its resource identifier. However, if you are using a request bundle to create both the referenced resource (`Patient` in this example) and the resource which references it (`Observation`), then it is impossible to know the `Patient`resource identifier before the request bundle has been processed (that is, before the new `Patient` resource is created).

Thankfully, the HL7 FHIR specification defines a way to express a dependency between two resources within a request bundle by using a _local reference_<sup id="a6">[6](#f6)</sup>. In the following example, a request bundle contains a `POST` request to create a new `Patient` resource, along with a `POST` request to create a new `Observation` resource that references that `Patient`:

#### 4.8.0.1 Example 1: Observation references Patient via local reference
```
{
    "resourceType" : "Bundle",
    "type" : "batch",
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

In order to reference a resource via a local reference, you must first define a local identifier for that resource by specifying the `fullUrl` field within the request entry. To define a local identifier, use a value that starts with `urn:` as in the preceding example. The HL7 FHIR specification highly recommends that the value of the identifier should be a UUID-type value, although the FHIR server does not require the use of a UUID value. You can use any value you like after the `urn:` prefix as long as it is unique within the bundle. For example, you can use urn:Patient_1, urn:ABC, or urn:Foo. Just make sure that no two request entries share the same fullUrl value within the request bundle.

After you define a local identifier for the referenced resource, you can then define one or more references to that resource by using the local identifier instead of an external identifier. In the preceding example, you can see that the Observation's `subject.reference` field specifies the Patient's local identifier as specified in the `fullUrl` field of the Patient's request entry.

### 4.8.1 Processing rules
The following processing rules apply for the use of local references within a request bundle:
1.  A local identifier must be defined via a request entry's `fullUrl` field in order for that local identifier to be used in a local reference.
2.  Local references will only be recognized for local identifiers associated with requst entries with a request method of `POST` or `PUT`.
3.  `POST` requests will be processed before `PUT` requests.
4.  There is no order dependency within a request bundle for request entries defining local identifiers, and request entries which reference those local identifiers via local reference. The exception to this rule is for request entries which specify [conditional create](https://www.hl7.org/fhir/http.html#ccreate) or [conditional update](https://www.hl7.org/fhir/http.html#cond-update) requests.
5.  If a request entry specifying a conditional create or update request defines a local identifier, that request entry must be processed before a request entry which references the local identifier in a local reference.

In the example in [Section 4.8.0.1](#4801-example-1-observation-references-patient-via-local-reference), you can see that there are two POST requests and the `Patient` request entry appears in the bundle before the `Observation` request entry. However, based on rule 4, it would still be a valid request bundle if the `Observation` request entry appeared before the `Patient` request entry, unless the `Patient` request entry specified a conditional create (rule 5).

If those entries were reversed, and the `Patient` request entry specified a conditional create, then the FHIR server would return an error when processing the `Observation` request entry, because the `Patient` local identifier would not be defined yet.

The following examples also satisfy the local reference processing rules:

#### 4.8.1.1 Example 2: Observation (PUT) appears before Patient (POST)
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
            "url" : "Patient"
        }
    } ]
}
```

In Example 2, if the `Patient` request entry was a conditional create request, this would still be a valid request bundle, because `POST` requests are processed before `PUT` requests (rule 3). This means the `Patient` request entry would be processed before the `Observation` request entry, and thus the `Patient` local identifier would be defined when the `Observation` request entry was processed.

#### 4.8.1.2 Example 3: Encounter and Procedure circular references
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

Using Example 3, the FHIR server detects the use of local identifiers in the `Encounter` request entry (`urn:Encounter_1`) and in the `Procedure` request entry (`urn:Procedure_1`), and establishes a mapping between the local identifiers and the external references to be associated with the new `Encounter` and `Procedure` resources (for example, `Encounter/1cc5d299-d2be-4f93-8745-a121232ffe5b` and `Procedure/22b21fcf-8d00-492d-9de0-e25ddd409eaf`).

Then when the FHIR server processes the POST requests for the `Encounter` and `Procedure` resources, it detects the use of the local references and substitutes the corresponding external references for them before creating the new resources. Here is an example of a response bundle for the request bundle depicted in Example 3 in which we can see that the Encounter's `reasonReference.reference` field now contains a proper external reference to the newly-created `Procedure` resource, and the Procedure's `encounter.reference` field now contains a proper external reference to the newly-created `Encounter` resource:

#### 4.8.1.3 Example 4: Response bundle for Example 3
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

```
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
                "bootstrapDb":false
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
Bulk data export is implemented according to the [HL7 FHIR BulkDataAccess IG: STU1](http://hl7.org/fhir/uv/bulkdata/STU1/export/index.html), and Bulk data import is implemented according to the [Proposal for $import Operation](https://github.com/smart-on-fhir/bulk-import/blob/master/import.md).

There are 2 modules involved:

- fhir-operation-bulkdata
- fhir-bulkimportexport-webapp   

The *fhir-operation-bulkdata* module implements the REST APIs for bulk data export, import and status as FHIR Operations.  There are five operations:

* ExportOperation - system export
* PatientExportOperation - Patient export
* GroupExportOperation - Group export
* ImportOperation - import resources using the system endpoint
* StatusOperation - polling status for import and export ($bulkdata-status)

Each operation calls the JavaBatch framework defined in the *fhir-bulkimportexport-webapp* module to execute the export unit-of-work.
There are 4 JavaBatch jobs defined as following in *fhir-bulkimportexport-webapp* module for the above 3 export operations and 1 import operation:

- FhirBulkExportChunkJob
- FhirBulkExportPatientChunkJob
- FhirBulkExportGroupChunkJob
- FhirBulkImportChunkJob

The *fhir-bulkimportexport-webapp* module is a wrapper for the whole BulkData web application, which is the build artifact - fhir-bulkimportexport.war. This web archive is copied to the apps directory of the liberty fhir-server instance. Following is a sample liberty server configuration (server.xml) for fhir-bulkimportexport.war:

```xml
<webApplication id="fhir-bulkimportexport-webapp" location="fhir-bulkimportexport.war" name="fhir-bulkimportexport-webapp">
     <classloader commonLibraryRef="fhirSharedLib" privateLibraryRef="configResources,fhirUserLib"/>
        <application-bnd>
            <security-role id="users" name="FHIRUsers">
                     <group name="FHIRUsers"/>
            </security-role>
        </application-bnd>
</webApplication>
```

BulkData web application writes the exported FHIR resources to an IBM Cloud Object Storage (COS) or any Amazon S3-Compatible bucket (e.g, Amazon S3, minIO etc) as configured in the per-tenant server configuration under `fhirServer/bulkdata`. The following is an example configuration for bulkdata; please refer to section 5 for the detailed description of these properties:

```
"bulkdata": {
    "bulkDataBatchJobIdEncryptionKey": "change-password",
    "applicationName": "fhir-bulkimportexport-webapp",
    "moduleName": "fhir-bulkimportexport.war",
    "jobParameters": {
        "cos.bucket.name": "exports",
        "cos.location": "us",
        "cos.endpoint.internal": "fake",
        "cos.endpoint.external": "fake",
        "cos.credential.ibm": "N",
        "cos.api.key": "fake",
        "cos.srvinst.id": "fake"
    },
    "implementation_type": "cos",
    "batch-uri": "https://localhost:9443/ibm/api/batch/jobinstances",
    "batch-user": "fhiradmin",
    "batch-user-password": "change-password",
    "batch-truststore": "resources/security/fhirTrustStore.p12",
    "batch-truststore-password": "change-password",
    "isExportPublic": true,
    "validBaseUrls": [
        "https://test-url/"
    ],
    "maxInputPerRequest": 5,
    "systemExportImpl": "fast"
}
```

To use Amazon S3 bucket for exporting, please set `cos.credential.ibm` to `N`, set `cos.api.key` to S3 access key, and set `cos.srvinst.id` to the S3 secret key. The following is a sample path to the exported ndjson file, the full path can be found in the response to the polling location request after the export request (please refer to the FHIR BulkDataAccess spec for details).  

`.../exports/6xjd4M8afi6Xo95eYv7zPxBqSCoOEFywZLoqH1QBtbw=/Patient_1.ndjson`

Basic system exports (without typeFilters) use a streamlined implementation which bypasses the IBM FHIR Server search API for direct access to the data, enabling better throughput. The `fhirServer/bulkdata/systemExportImpl` property can be used to disable the streamlined system export implementation. To use the legacy implementation based on IBM FHIR Server search, set the value to "legacy". The new system export implementation will be used by default for any export not using typeFilters. Exports using typeFilters use FHIR search capabilities so cannot use the streamlined export function.

To import using the `$import` proposal, one must additionally configure the `fhirServer/bulkdata/validBaseUrls`. For example, if one stores bulkdata on `https://test-url.cos.ibm.com/bucket1` and `https://test-url.cos.ibm.com/bucket2` you must specify both baseUrls in the configuration:

```json
    "validBaseUrls": [
        "https://test-url.cos.ibm.com/bucket1",
        "https://test-url.cos.ibm.com/bucket2"
    ]
```

These base urls are not checked when using cloud object store and bulk-import. If you need to disable the validBaseUrls feature you may add `fhirServer/bulkdata/validBaseUrlsDisabled` as `true`.

The `fhirServer/bulkdata/maxInputPerRequest` is used to configure a maximum number of inputs supported by the instance.  The default number is 5.

Note: When `$import` is executed, if a resource to import includes a `Resource.id` then this id is honored (via create-on-update). If `Resource.id` is not valued, the server will perform a create and assign a new `Resource.id` for this resource.

Following is the beautified response of sample polling location request after the export is finished:

```json
{
"transactionTime": "2020/01/20 16:53:41.160 -0500",
"request": "/$export?_type=Patient",
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

For the Import Operation, the polled status includes an indication of `$import` and the location of the OperationOutcome NDJsons and the corresponding failure and success counts.

Note, the deletion of an a job is split into two phases, ACCEPTED (202) response and DELETED (204).  202 is returned until the operation is stopped or removed, and then 204.

By default, the exported `ndjson` file is configured with public access automatically and with 2 hours expiration time, the randomly generated secret in the path is used to protect the file. please note that IBM COS does not support expiration time for each single COS object, so please configure retention policy (e.g, 1 day) for the bucket if IBM COS is used. For both Amazon S3 and IBM COS, please remember that public access should never be configured to the bucket itself.

Note: `fhirServer/bulkdata/isExportPublic` can be set to "false" to disable public access.
      minio doesn't support object level ACL, so access token is always needed to download the exported `ndjson` files.

JavaBatch feature must be enabled in `server.xml` as following on the Liberty server:

```xml
<featureManager>
    ...
    <feature>batchManagement-1.0</feature>
    ...
</featureManager>
```

The JavaBatch user is configured in `server.xml` and the `fhir-server-config.json`:

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

Note: The user referenced in the `fhir-server-config.json` must have a role of at least batchSubmitter.

By default, in-memory Derby database is used for persistence of the JavaBatch Jobs as configured in `fhir-server/configDropins/bulkdata.xml`. Instruction is also provided in "Configuring a Liberty Datasource with API Key" section of the DB2OnCloudSetup guide to configure DB2 service in IBM Clouds as JavaBatch persistence store. The JavaBatch schema is created by default via the `fhir-persistence-schema` command line interface jar.

You can also choose to use postgresql or other RDBMS as your Job repository. To enable a postgresql job repository, uncomment the corresponding section of the `bulkdata.xml` server config.

Note: If you use PostgreSQL database as IBM FHIR Server data store or the JavaBatch job repository, please enable `max_prepared_transactions` in postgresql.conf, otherwise the import/export JavaBatch jobs fail.

For more information about Liberty JavaBatch configuration, please refer to [IBM WebSphere Liberty Java Batch White paper](https://www-03.ibm.com/support/techdocs/atsmastr.nsf/webindex/wp102544).

### 4.10.1 Integration Testing
To integration test, there are tests in `ExportOperationTest.java` in `fhir-server-test` module with server integration test cases for system, patient and group export. Further, there are tests in `ImportOperationTest.java` in `fhir-server-test` module.

### 4.10.2 Export to Parquet
Version 4.4 of the IBM FHIR Server introduces experimental support for exporting to Parquet format (as an alternative to the default NDJSON export). However, due to the size of the dependencies needed to make this work, this feature is disabled by default.

To enable export to parquet, an administrator must:
1. make Apache Spark (version 3.0) and the IBM Stocator adapter (version 1.1) available to the fhir-bulkimportexport-webapp; and
2. set the `/fhirServer/bulkdata/enableParquet` config property to `true`

One way to accomplish the first part of this is to change the scope of these dependencies from the fhir-bulkimportexport-webapp pom.xml and rebuild the webapp to include them.

### 4.10.3 Job Logs
Because the bulk import and export operations are built on Liberty's java batch implementation, users may need to check the [Liberty batch job logs](https://www.ibm.com/support/knowledgecenter/SSEQTP_liberty/com.ibm.websphere.wlp.doc/ae/rwlp_batch_view_joblog.html) for detailed step information / troubleshooting.

In a standard installation, these logs will be at `wlp/usr/servers/fhir-server/logs/joblogs`.
In the `ibmcom/ibm-fhir-server` docker image, these logs will be at `/logs/joblogs`.

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
        },
        "addresses": [
            {
                "url": "https://test.io:443/fhir-server/api/v4/Patient",
                "name": "",
                "port": ""
            }
        ]
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

Whole-system search is a special case of this resource type validation, since no resource type is specified on a whole-system search request. In this case, validation will be done against the `Resource` resource type. In the above configuration example, a whole-system search request such as `GET [base]?_lastUpdated=gt2020-01-01` will fail because the `Resource` resource type is not specified. If the configuration were to have the `fhirServer/resources/open` property set to `true`, or if the `Resource` resource type were specified in the `fhirServer/resources` property group, then the whole-system search request would be allowed, assuming the `search` interaction was valid for the `Resource` resource type.

In addition to interaction configuration, the `fhirServer/resources` property group also provides the ability to configure search parameter filtering and profile validation. See [Search configuration](https://ibm.github.io/FHIR/guides/FHIRSearchConfiguration#12-filtering) and [Resource validation](#44-resource-validation) respectively for details.

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
|`fhirServer/core/serverRegistryResourceProviderEnabled`|boolean|Indicates whether the server registry resource provider should be used by the FHIR registry component to access definitional resources through the persistence layer.|
|`fhirServer/core/conditionalDeleteMaxNumber`|integer|The max number of matches supported in conditional delete. |
|`fhirServer/core/capabilityStatementCacheTimeout`|integer|The number of minutes that a tenant's CapabilityStatement is cached for the metadata endpoint. |
|`fhirServer/core/extendedCodeableConceptValidation`|boolean|A boolean flag which indicates whether extended validation is performed by the server during object construction for code, Coding, CodeableConcept, Quantity, Uri, and String elements which have required bindings to value sets.|
|`fhirServer/core/disabledOperations`|string|A comma-separated list of operations which are not allowed to run on the IBM FHIR Server, for example, `validate,import`. Note, do not include the dollar sign `$`|
|`fhirServer/resources/open`|boolean|Whether resources that are not explicitly listed in the configuration should be supported by the FHIR Server REST layer. When open is set to `false`, only the resources listed in fhir-server-config.json are supported.|
|`fhirServer/resources/Resource/interactions`|string list|A list of strings that represent the RESTful interactions (create, read, vread, update, patch, delete, history, and/or search) supported for resource types. Omitting this property is equivalent to supporting all FHIR interactions for the supported resources. An empty list, `[]`, can be used to indicate that no REST methods are supported. This property can be overridden for specific resource types via the `fhirServer/resources/<resourceType>/interactions` property.|
|`fhirServer/resources/Resource/searchParameters`|object|The set of search parameters to support for all supported resource types. Omitting this property is equivalent to supporting all search parameters in the server's registry that apply to resource type "Resource" (all resources). An empty object, `{}`, can be used to indicate that no global search parameters are supported.|
|`fhirServer/resources/Resource/searchParameters/<code>`|string|The URL of the search parameter definition to use for the search parameter `<code>`. Individual resource types may override this value via `fhirServer/resources/<resourceType>/searchParameters/<code>`|
|`fhirServer/resources/Resource/searchIncludes`|string list|A comma-separated list of \_include values supported for all resource types. Individual resource types may override this value via `fhirServer/resources/<resourceType>/searchIncludes`. Omitting this property is equivalent to supporting all \_include values for the supported resources. An empty list, `[]`, can be used to indicate that no \_include values are supported.|
|`fhirServer/resources/Resource/searchRevIncludes`|string list|A comma-separated list of \_revinclude values supported for all resource types. Individual resource types may override this value via `fhirServer/resources/<resourceType>/searchRevIncludes`. Omitting this property is equivalent to supporting all \_revinclude values for the supported resources. An empty list, `[]`, can be used to indicate that no \_revinclude values are supported.|
|`fhirServer/resources/Resource/searchParameterCombinations`|string list|A comma-separated list of search parameter combinations supported for all resource types. Each search parameter combination is a string, where a plus sign, `+`, separates the search parameters that can be used in combination. To indicate that searching without any search parameters is allowed, an empty string must be included in the list. Including an asterisk, `*`, in the list indicates support of any search parameter combination. Individual resource types may override this value via `fhirServer/resources/<resourceType>/searchParameterCombinations`. Omitting this property is equivalent to supporting any search parameter combination.|
|`fhirServer/resources/Resource/profiles/atLeastOne`|string list|A comma-separated list of profiles, at least one of which must be specified in a resource's `meta.profile` element and successfully validated against in order for a resource to be persisted to the FHIR server. Individual resource types may override this value via `fhirServer/resources/<resourceType>/profiles/atLeastOne`. Omitting this property or specifying an empty list is equivalent to not requiring any profile assertions for a resource.|
|`fhirServer/resources/<resourceType>/interactions`|string list|A list of strings that represent the RESTful interactions (create, read, vread, update, patch, delete, history, and/or search) to support for this resource type. For resources without the property, the value of `fhirServer/resources/Resource/interactions` is used.|
|`fhirServer/resources/<resourceType>/searchParameters`|object|The set of search parameters to support for this resource type. Global search parameters defined on the `Resource` resource can be overridden on a per-resourceType basis.|
|`fhirServer/resources/<resourceType>/searchParameters/<code>`|string|The URL of the search parameter definition to use for the search parameter `<code>` on resources of type `<resourceType>`.|
|`fhirServer/resources/<resourceType>/searchIncludes`|string list|A comma-separated list of \_include values supported for this resource type. An empty list, `[]`, can be used to indicate that no \_include values are supported. For resources without the property, the value of `fhirServer/resources/Resource/searchIncludes` is used.|
|`fhirServer/resources/<resourceType>/searchRevIncludes`|string list|A comma-separated list of \_revinclude values supported for this resource type. An empty list, `[]`, can be used to indicate that no \_revinclude values are supported. For resources without the property, the value of `fhirServer/resources/Resource/searchRevIncludes` is used.|
|`fhirServer/resources/<resourceType>/searchParameterCombinations`|string list|A comma-separated list of search parameter combinations supported for this resource type. Each search parameter combination is a string, where a plus sign, `+`, separates the search parameters that can be used in combination. To indicate that searching without any search parameters is allowed, an empty string must be included in the list. Including an asterisk, `*`, in the list indicates support of any search parameter combination. For resources without the property, the value of `fhirServer/resources/Resource/searchParameterCombinations` is used.|
|`fhirServer/resources/<resourceType>/profiles/atLeastOne`|string list|A comma-separated list of profiles, at least one of which must be specified in a resource's `meta.profile` element and be successfully validated against in order for a resource of this type to be persisted to the FHIR server. If this property is not specified, or if an empty list is specified, the value of `fhirServer/resources/Resource/profiles/atLeastOne` will be used.|
|`fhirServer/notifications/common/includeResourceTypes`|string list|A comma-separated list of resource types for which notification event messages should be published.|
|`fhirServer/notifications/websocket/enabled`|boolean|A boolean flag which indicates whether or not websocket notifications are enabled.|
|`fhirServer/notifications/kafka/enabled`|boolean|A boolean flag which indicates whether or not kafka notifications are enabled.|
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
|`fhirServer/persistence/datasources`|map|A map containing datasource definitions. See [Section 3.3.2.3 Datastore configuration reference](#3323-datastore-configuration-reference) for more information.|
|`fhirServer/persistence/jdbc/dataSourceJndiName`|string|The JNDI name of the DataSource to be used by the JDBC persistence layer.|
|`fhirServer/persistence/jdbc/bootstrapDb`|boolean|A boolean flag which indicates whether the JDBC persistence layer should attempt to create or update the database and schema at server startup time.|
|`fhirServer/persistence/datasources/<datasourceId>/searchOptimizerOptions/from_collapse_limit`|int| For PostgreSQL, sets the from_collapse_limit query optimizer parameter to improve search performance. If not set, the IBM FHIR Server uses a value of 16. To use the database default (8), explicitly set this value to null. |
|`fhirServer/persistence/datasources/<datasourceId>/searchOptimizerOptions/join_collapse_limit`|int| For PostgreSQL, sets the join_collapse_limit query optimizer parameter to improve search performance. If not set, the IBM FHIR Server uses a value of 16. To use the database default (8), explicitly set this value to null. |
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
|`fhirServer/search/useBoundingRadius`|boolean|True, the bounding area is a Radius, else the bounding area is a box.|
|`fhirServer/search/useStoredCompartmentParam`|boolean|False, Compute and store parameter to accelerate compartment searches. Requires reindex using at least IBM FHIR Server version 4.5.1 before this feature is enabled |
|`fhirServer/bulkdata/applicationName`| string|Fixed value, always set to fhir-bulkimportexport-webapp |
|`fhirServer/bulkdata/moduleName`|string| Fixed value, always set to fhir-bulkimportexport.war |
|`fhirServer/bulkdata/jobParameters/cos.bucket.name`|string|Object store bucket name |
|`fhirServer/bulkdata/jobParameters/cos.location`|string|Object store location |
|`fhirServer/bulkdata/jobParameters/cos.endpoint.internal`|string|Object store end point url used to read/write from COS |
|`fhirServer/bulkdata/jobParameters/cos.endpoint.external`|string|Object store end point url used in the constructed download URLs|
|`fhirServer/bulkdata/jobParameters/credential.ibm`|string|If use IBM credential, "Y" or "N" |
|`fhirServer/bulkdata/jobParameters/cos.api.key`|string|API key for accessing IBM COS |
|`fhirServer/bulkdata/jobParameters/cos.srvinst.id`|string|Service instance Id for accessing IBM COS |
|`fhirServer/bulkdata/implementation_type`|string|Use "cos" for any S3-compatible object store |
|`fhirServer/bulkdata/batch-uri`|string|The URL to access the FHIR server hosting the batch web application |
|`fhirServer/bulkdata/batch-user`|string|User for submitting JavaBatch job |
|`fhirServer/bulkdata/batch-user-password`|string|Password for above batch user |
|`fhirServer/bulkdata/batch-truststore`|string|Trust store for JavaBatch job submission |
|`fhirServer/bulkdata/batch-truststore-password`|string|Password for above trust store |
|`fhirServer/bulkdata/bulkDataBatchJobIdEncryptionKey`|string|Encryption key for JavaBatch job id |
|`fhirServer/bulkdata/isExportPublic`|boolean|If give public read only access to the exported files |
|`fhirServer/bulkdata/validBaseUrls`|string|The list of supported urls which are approved for the fhir server to access|
|`fhirServer/bulkdata/validBaseUrlsDisabled`|boolean|Disables the URL checking feature|
|`fhirServer/bulkdata/maxInputPerRequest`|integer|The maximum inputs per bulk import|
|`fhirServer/bulkdata/cosFileMaxResources`|int|The maximum number of FHIR resources per COS file, "-1" means no limit, the default value is 200000 |
|`fhirServer/bulkdata/cosFileMaxSize`|int|The maximum COS file size in bytes, "-1" means no limit, the default value is 209715200 (200M) |
|`fhirServer/bulkdata/patientExportPageSize`|int| The search page size for patient/group export, the default value is 200 |
|`fhirServer/bulkdata/useFhirServerTrustStore`|boolean| If the COS Client should use the IBM FHIR Server's TrustStore to access S3/IBMCOS service |
|`fhirServer/bulkdata/enableParquet`|boolean| Whether or not the server is configured to support export to parquet; to properly enable it the administrator must first make spark and stocator available to the fhir-bulkimportexport-webapp (e.g through the shared lib at `wlp/user/shared/resources/lib`) |

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
|`fhirServer/core/serverRegistryResourceProviderEnabled`|false|
|`fhirServer/core/conditionalDeleteMaxNumber`|10|
|`fhirServer/core/capabilityStatementCacheTimeout`|60|
|`fhirServer/core/extendedCodeableConceptValidation`|true|
|`fhirServer/resources/open`|true|
|`fhirServer/resources/Resource/interactions`|null (all interactions supported)|
|`fhirServer/resources/Resource/searchParameters`|null (all global search parameters supported)|
|`fhirServer/resources/Resource/searchIncludes`|null (all \_include values supported)|
|`fhirServer/resources/Resource/searchRevIncludes`|null (all \_revinclude values supported)|
|`fhirServer/resources/Resource/searchParameterCombinations`|null (all search parameter combinations supported)|
|`fhirServer/resources/Resource/profiles/atLeastOne`|null (no resource profile assertions required)|
|`fhirServer/resources/<resourceType>/interactions`|null (inherits from `fhirServer/resources/Resource/interactions`)|
|`fhirServer/resources/<resourceType>/searchParameters`|null (all type-specific search parameters supported)|
|`fhirServer/resources/<resourceType>/searchParameters/<code>`|null|
|`fhirServer/resources/<resourceType>/searchIncludes`|null (inherits from `fhirServer/resources/Resource/searchIncludes`)|
|`fhirServer/resources/<resourceType>/searchRevIncludes`|null (inherits from `fhirServer/resources/Resource/searchRevIncludes`)|
|`fhirServer/resources/<resourceType>/searchParameterCombinations`|null (inherits from `fhirServer/resources/Resource/searchParameterCombinations`)|
|`fhirServer/resources/<resourceType>/profiles/atLeastOne`|null (inherits from `fhirServer/resources/Resource/profiles/atLeastOne`)|
|`fhirServer/notifications/common/includeResourceTypes`|`["*"]`|
|`fhirServer/notifications/websocket/enabled`|false|
|`fhirServer/notifications/kafka/enabled`|false|
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
|`fhirServer/persistence/jdbc/dataSourceJndiName`|jdbc/fhirProxyDataSource|
|`fhirServer/persistence/jdbc/bootstrapDb`|false|
|`fhirServer/persistence/datasources/<datasourceId>/searchOptimizerOptions/from_collapse_limit`|16|
|`fhirServer/persistence/datasources/<datasourceId>/searchOptimizerOptions/join_collapse_limit`|16|
|`fhirServer/security/cors`|boolean|true|
|`fhirServer/security/basic/enabled`|boolean|false|
|`fhirServer/security/certificates/enabled`|boolean|false|
|`fhirServer/security/oauth/enabled`|boolean|false|
|`fhirServer/security/oauth/regUrl`|""|
|`fhirServer/security/oauth/authUrl`|""|
|`fhirServer/security/oauth/tokenUrl`|""|
|`fhirServer/security/oauth/manageUrl`|""|
|`fhirServer/security/oauth/introspectUrl`|""|
|`fhirServer/security/oauth/revokeUrl`|""|
|`fhirServer/security/oauth/smart/enabled`|boolean|false|
|`fhirServer/security/oauth/smart/scopes`|array|null|
|`fhirServer/security/oauth/smart/capabilities`|array|null|
|`fhirServer/audit/serviceClassName`|""|
|`fhirServer/audit/serviceProperties/auditTopic`|FHIR_AUDIT|
|`fhirServer/audit/serviceProperties/geoCity`|UnknownCity|
|`fhirServer/audit/serviceProperties/geoState`|UnknownState|
|`fhirServer/audit/serviceProperties/geoCounty`|UnknownCountry|
|`fhirServer/audit/serviceProperties/mapper`|cadf|
|`fhirServer/audit/serviceProperties/load`|environment|
|`fhirServer/bulkdata/isExportPublic`|true|
|`fhirServer/bulkdata/validBaseUrlsDisabled`|false|
|`fhirServer/bulkdata/cosFileMaxResources`|200000|
|`fhirServer/bulkdata/cosFileMaxSize`|209715200|
|`fhirServer/bulkdata/patientExportPageSize`|200|
|`fhirServer/bulkdata/useFhirServerTrustStore`|false|
|`fhirServer/bulkdata/enableParquet`|false|

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
|`fhirServer/core/serverRegistryResourceProviderEnabled`|N|N|
|`fhirServer/core/conditionalDeleteMaxNumber`|Y|Y|
|`fhirServer/core/capabilityStatementCacheTimeout`|Y|Y|
|`fhirServer/core/extendedCodeableConceptValidation`|N|N|
|`fhirServer/core/disabledOperations`|N|N|
|`fhirServer/resources/open`|Y|Y|
|`fhirServer/resources/Resource/interactions`|Y|Y|
|`fhirServer/resources/Resource/searchParameters`|Y|Y|
|`fhirServer/resources/Resource/searchParameters/<code>`|Y|Y|
|`fhirServer/resources/Resource/searchIncludes`|Y|Y|
|`fhirServer/resources/Resource/searchRevIncludes`|Y|Y|
|`fhirServer/resources/Resource/searchParameterCombinations`|Y|Y|
|`fhirServer/resources/Resource/profiles/atLeastOne`|Y|Y|
|`fhirServer/resources/<resourceType>/interactions`|Y|Y|
|`fhirServer/resources/<resourceType>/searchParameters`|Y|Y|
|`fhirServer/resources/<resourceType>/searchParameters/<code>`|Y|Y|
|`fhirServer/resources/<resourceType>/searchIncludes`|Y|Y|
|`fhirServer/resources/<resourceType>/searchRevIncludes`|Y|Y|
|`fhirServer/resources/<resourceType>/searchParameterCombinations`|Y|Y|
|`fhirServer/resources/<resourceType>/profiles/atLeastOne`|Y|Y|
|`fhirServer/notifications/common/includeResourceTypes`|N|N|
|`fhirServer/notifications/websocket/enabled`|N|N|
|`fhirServer/notifications/kafka/enabled`|N|N|
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
|`fhirServer/persistence/datasources/<datasourceId>/searchOptimizerOptions/from_collapse_limit`|Y|Y|
|`fhirServer/persistence/datasources/<datasourceId>/searchOptimizerOptions/join_collapse_limit`|Y|Y|
|`fhirServer/persistence/jdbc/dataSourceJndiName`|N|N|
|`fhirServer/persistence/jdbc/bootstrapDb`|N|N|
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
|`fhirServer/bulkdata/jobParameters/cos.bucket.name`|Y|Y|
|`fhirServer/bulkdata/jobParameters/cos.location`|Y|Y|
|`fhirServer/bulkdata/jobParameters/cos.endpoint.internal`|Y|Y|
|`fhirServer/bulkdata/jobParameters/cos.endpoint.external`|Y|Y|
|`fhirServer/bulkdata/jobParameters/credential.ibm`|Y|Y|
|`fhirServer/bulkdata/jobParameters/cos.api.key`|Y|Y|
|`fhirServer/bulkdata/jobParameters/cos.srvinst.id`|Y|Y|
|`fhirServer/bulkdata/bulkDataBatchJobIdEncryptionKey`|Y|Y|
|`fhirServer/bulkdata/isExportPublic`|Y|Y|
|`fhirServer/bulkdata/validBaseUrls`|Y|Y|
|`fhirServer/bulkdata/maxInputPerRequest`|Y|Y|
|`fhirServer/bulkdata/validBaseUrlsDisabled`|Y|Y|
|`fhirServer/bulkdata/cosFileMaxResources`|Y|Y|
|`fhirServer/bulkdata/cosFileMaxSize`|Y|Y|
|`fhirServer/bulkdata/patientExportPageSize`|Y|Y|
|`fhirServer/bulkdata/useFhirServerTrustStore`|Y|Y|
|`fhirServer/bulkdata/enableParquet`|Y|Y|

## 5.2 Keystores, truststores, and the FHIR server

### 5.2.1 Background
As stated earlier, the FHIR server is installed with a default configuration in `server.xml` which includes the definition of a keystore (`fhirKeyStore.p12`) and a truststore (`fhirTrustStore.p12`)<sup id="a7">[7](#f7)</sup>. These files are provided only as examples and while they may suffice in a test environment, the FHIR server deployer should generate a new keystore and truststore for any installations where security is a concern. Review the information in the following topics to learn how to configure a secure keystore and truststore.

### 5.2.2 WebApp security
By default, the FHIR server REST API is only available via HTTPS on port 9443 and is protected by HTTP basic authentication.
Alternatively, the server can use OpenID Connect and OAuth 2.0 via a Bearer Token as described in [Section 5.2.4 Oauth 2.0](#524-oauth-20).
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
The FHIR specification recommends the use of OpenID Connect and OAuth 2.0.
The IBM FHIR Server supports these via either:
* An external Authorization Server and/or Identity Provider like [IBM Cloud App ID](https://www.ibm.com/cloud/app-id) or [Keycloak](https://www.keycloak.org)
* Liberty's own OpenID Connect and OAuth 2.0 support

The following sections focus on the latter and are adapted from the [WebSphere Liberty Knowledge Center](https://www.ibm.com/support/knowledgecenter/SSD28V_liberty/com.ibm.websphere.wlp.core.doc/ae/twlp_config_oidc_pc_examp_beginner.html), but the steps apply to OpenLiberty as well.

### 5.3.1 Configure Liberty as the OpenID Connect Provider
Liberty can be configured to act as an OpenID Connect Provider via the [openidConnectServer-1.0 feature](https://openliberty.io/docs/ref/feature/#openidConnectServer-1.0.html). To enable this feature without modifying the default `server.xml`, move the `oidcProvider.xml` config snippet on the installed FHIR Server from `<WLP_HOME>/usr/servers/fhir-server/configDropins/disabled/` to `<WLP_HOME>/usr/servers/fhir-server/configDropins/defaults/` and modify as desired.

A copy of this snippet is provided here for illustrative purposes:
```xml
<featureManager>
    <feature>openidConnectServer-1.0</feature>
</featureManager>

<openidConnectProvider id="oidc-provider"
    oauthProviderRef="oauth2-provider"
    keyStoreRef="defaultKeyStore"
    signatureAlgorithm="RS256" />

<oauth-roles>
    <authenticated>
        <special-subject type="ALL_AUTHENTICATED_USERS" />
    </authenticated>
    <clientManager>
        <group name="clientAdministrator" />
    </clientManager>
</oauth-roles>

<oauthProvider id="oauth2-provider" oauthOnly="false" allowPublicClients="true" jwtAccessToken="true">
    <grantType>authorization_code</grantType>
    <databaseStore dataSourceRef="OAuthDataSource" schema="FHIR_OAUTH" />
</oauthProvider>

<dataSource id="OAuthDataSource" jndiName="jdbc/OAuth2DB">
    <properties.derby.embedded createDatabase="create" databaseName="derby/oauth2db" />
    <jdbcDriver libraryRef="derbyLib" />
</dataSource>

<library id="derbyLib">
    <fileset dir="${shared.resource.dir}/lib/derby" includes="*.jar" />
</library>
```

### 5.3.1.1 oidcProvider.xml snippet details
OpenID Connect is built on OAuth 2.0 and so the `oidcProvider.xml` snippet configures Liberty as an OAuth 2.0 provider as described at [Defining OAuth](https://www.ibm.com/support/knowledgecenter/SSEQTP_liberty/com.ibm.websphere.wlp.doc/ae/twlp_oauth_defining.html).

Liberty supports the registration of clients through either a localStore in the server.xml or a databaseStore in a configured dataSource.
To support "dynamic client registration", the snippet configures an oauthProvider databaseStore and a clientManager oauth-role for managing the OAuth 2.0 clients.

Additionally, the `fhir-persistence-schema` project (which is also used to deploy the main IBM FHIR Server schema) creates the tables required by this Liberty feature by default (as described at [OAuth Databases](https://www.ibm.com/support/knowledgecenter/SSD28V_liberty/com.ibm.websphere.wlp.core.doc/ae/twlp_oauth_dbs.html)).

Finally, the `openidConnectProvider` element is specified with a default signatureAlgorithm of RS256 and a reference to the defaultKeyStore which must contain a private key that can be used for signing.

### 5.3.1.2 Register a client
Now that the server is configured as an OAuth 2.0 provider, clients can self-register by invoking the https://[host]:9443/oauth2/endpoint/oauth2-provider/registration endpoint.

For example, for a server running on localhost:
```sh
curl -u 'fhiruser:change-password' 'https://localhost:9443/oauth2/endpoint/oauth2-provider/registration' \
--header 'Content-Type: application/json' \
--data-raw '{
   "token_endpoint_auth_method":"client_secret_basic",
   "scope":"launch launch/patient offline_access openid profile user/*.* patient/*.*",
   "grant_types":[
      "authorization_code",
      "client_credentials",
      "implicit",
      "refresh_token",
      "urn:ietf:params:oauth:grant-type:jwt-bearer"
   ],
   "response_types":[
      "code",
      "token",
      "id_token token"
   ],
   "application_type":"web",
   "subject_type":"public",
   "post_logout_redirect_uris":[
      "http://localhost:4567/inferno/oauth2/static/redirect"
   ],
   "preauthorized_scope":"launch launch/patient offline_access openid profile user/*.* patient/*.*",
   "introspect_tokens":true,
   "trusted_uri_prefixes":[
      "https://server.example.com:9000/trusted/"
   ],
   "redirect_uris":[
      "http://localhost:4567/inferno/oauth2/static/redirect"
   ]
}'
```

For more information on Liberty's support for client registration, see https://www.ibm.com/support/knowledgecenter/SSEQTP_liberty/com.ibm.websphere.wlp.doc/ae/twlp_client_registration.html.

### 5.3.1.3 Request an access token
After you've registered a client, invoke the authorization endpoint with the client_id assigned to your client in order to obtain an authorization code that can be exchanged for an access token.
For a server running on localhost, the auth URL is `https://localhost:9443/oidc/endpoint/oidc-provider/authorize`.

The provider endpoint will present an HTML login form and you must enter a valid username/password from Liberty's configured user registry (e.g. `fhiruser`/`change-password`).
This will redirect you to the configured redirect uri for your client, passing it a code that can be exchanged for an access token at the token endpoint.
For a server running on localhost, the token URL is `https://localhost:9443/oidc/endpoint/oidc-provider/token`.

### 5.3.2 Configure Liberty to be an Oauth 2.0 Protected Resource Server
Liberty can be configured to act as an OAuth 2.0 Protected Resource Server via the [openidConnectClient-1.0 feature](https://openliberty.io/docs/ref/feature/#openidConnectClient-1.0.html). To enable this feature without modifying the default `server.xml`, move the `oauthResourceServer.xml` config snippet on the installed FHIR Server from `<WLP_HOME>/usr/servers/fhir-server/configDropins/disabled/` to `<WLP_HOME>/usr/servers/fhir-server/configDropins/defaults/` and modify as desired.

A copy of this snippet is provided here for illustrative purposes:
```xml
<featureManager>
    <feature>openidConnectClient-1.0</feature>
</featureManager>

<!-- Liberty acts as an OAuth 2.0 protected resource server when inboundPropagation=”required” -->
<openidConnectClient id="RS" inboundPropagation="required"
    trustStoreRef="defaultTrustStore"
    trustAliasName="libertyop"
    issuerIdentifier="https://localhost:9443/oauth2/endpoint/oauth2-provider,https://host.docker.internal:9443/oauth2/endpoint/oauth2-provider"
    validationEndpointUrl="https://localhost:9443/oauth2/endpoint/oauth2-provider/introspect"
    signatureAlgorithm="RS256"
    authFilterRef="filter"/>

<authFilter id="filter">
    <requestUrl urlPattern="/fhir-server"/>
</authFilter>
```

### 5.3.2.1 oauthResourceServer.xml snippet details
By default, the server is configured with a defaultTrustStore that includes a copy of the server's signed certificate with alias `libertyop` ("op" for OpenID Connect Provider).

The server is configured to accept tokens with an issuerIdentifier that has a hostname of either localhost or host.docker.internal so that we can test it from the [Inferno test tool's](https://github.com/onc-healthit/inferno) docker-compose environment.

The signatureAlgorithm must match the signature used by the OAuth provider and the authFilter is required when the server is also acting as both an OAuth Resource Server *and* an OAuth/OpenId Connect provider so that the OAuth endpoints themselves can be accessed to grant the access tokens needed for accessing the rest of the server endpoints.

### 5.3.2.2 Configuring the trustStore
If you are using Liberty as both the openIdConnect server and the openIdConnect client and you have modified the defaultKeyStore to use a different key, you can configure the corresponding trustStore based on the following keytool commands:

1. Export the OAuth 2.0 provider's certificate from the configured keystore via one of the following commands:
* `keytool -exportcert -keystore key.p12 -storepass Password -alias default -file libertyOP.cer`
* `keytool -exportcert -keystore key.jks -storepass Password -alias default -file libertyOP.cer`

2. Import the certificate into the server's trustStore. Assuming you use the same keystore for both, then use of these:
* `keytool -importcert -keystore key.p12 -storepass Password -alias libertyop -file libertyOP.cer -noprompt`
* `keytool -importcert -keystore key.jks -storepass Password -alias libertyop -file libertyOP.cer -noprompt`

### 5.3.3 Advertise the OAuth endpoints via fhir-server-config
To configure the FHIR Server to advertise the OpenID Connect and OAuth 2.0 endpoints of the providers, provide values for at least the following properties in the default fhir-server-config.json file:
* `fhirServer/security/oauth/authUrl`
* `fhirServer/security/oauth/tokenUrl`

When the Liberty server is the OpenID Connect / OAuth 2.0 provider, use a placeholder of `<host>` in the property values to have the server automatically replace this text with the hostname used by requestors (see `fhirServer/core/originalRequestUriHeaderName`).

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
                "url": "register",
                "valueUri": "https://localhost:9443/oauth2/endpoint/oauth2-provider/registration"
              },
              {
                "url": "authorize",
                "valueUri": "https://localhost:9443/oauth2/endpoint/oauth2-provider/authorize"
              },
              {
                "url": "token",
                "valueUri": "https://localhost:9443/oauth2/endpoint/oauth2-provider/token"
              }
            ]
          }
        ],
…
```

SMART on FHIR applications should use the `.well-known/smart-configuration` endpoint to determine the OAuth URLs to use for authorization,
but the entries in the Capability Statement are needed for backwards compatibility.

## 5.4 Custom HTTP Headers
IBM FHIR Server Supports the following custom HTTP Headers:

| Header Name      | Description                |
|------------------|----------------------------|
|`X-FHIR-TENANT-ID`|Specifies which tenant config should be used for the request. Default is `default`. Header name can be overridden via config property `fhirServer/core/tenantIdHeaderName`.|
|`X-FHIR-DSID`|Specifies which datastore config should be used for the request. Default is `default`. Header name can be overridden via config property `fhirServer/core/dataSourceIdHeaderName`.|

# 6 Related topics
For more information about topics related to configuring a FHIR server, see the following documentation:
*   [IBM Java 8 Keytool documentation](https://www.ibm.com/support/knowledgecenter/SSYKE2_8.0.0/com.ibm.java.security.component.80.doc/security-component/keytoolDocs/keytool_overview.html)
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

    An external reference is a reference to a resource which is meaningful outside a particular request bundle.  The value typically includes the resource type and the resource identifier, and could  be an absolute or relative URL. Examples:  `https://fhirserver1:9443/fhir-server/api/v4/Patient/12345`, `Patient/12345`, etc. [↩](#a5)

- <b id="f6">6</b>

    A local reference is a reference used within a request bundle that refers to another resource within the same request bundle and is meaningful only within that request bundle. A local reference starts with `urn:`. [↩](#a6)

- <b id="f7">7</b>

    Keystore and truststore files have the same basic structure. They both provide a secure means for storing certificates. Typically, we think of a keystore as a file that contains certificates that consist of a private/public key pair, whereas a truststore contains certificates that consist of a public key or trusted certificates. [↩](#a7)

- <b id="f8">8</b>

    While the instructions here show examples of creating self-signed certificates, in reality the FHIR Server deployer will likely need to use certificates that have been signed by a Certificate Authority (CA) such as Verisign, etc. [↩](#a8)

- <b id="f9">9</b>

    The _keytool_ command is provided as part of the Java 8 JRE.  The command can be found in $JAVA_HOME/jre/bin. [↩](#a9)

- <b id="f10">10</b> These instructions assume the use of a basic user registry in the server.xml file.  If you are instead using an LDAP registry, then the entire DN associated with the client certificate must match the DN of a user in the LDAP registry. [↩](#a10)

- <b id="f11">11</b>

    For the JAX-RS 2.0 Client API, you would call the ClientBuilder.truststore() method. [↩](#a11)

- <b id="f12">12</b>

    For the JAX-RS 2.0 Client API, you would call the ClientBuilder.keystore() method. [↩](#a12)

[a]:https://www.ibm.com/support/knowledgecenter/en/SSD28V_9.0.0/com.ibm.websphere.wlp.core.doc/ae/cwlp_pwd_encrypt.html

<hr/>

FHIR&reg; is the registered trademark of HL7 and is used with the permission of HL7.
