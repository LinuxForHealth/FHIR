---
layout: post
title:  IBM FHIR Server User's Guide
description: IBM FHIR Server User's Guide
Copyright: years 2017, 2020
lastupdated: "2020-05-13"
permalink: /FHIRServerUsersGuide/
---

- [1 Overview](#1-overview)
- [2 Installation](#2-installation)
  * [2.1 Installing a new server](#21-installing-a-new-server)
  * [2.2 Upgrading an existing server](#22-upgrading-an-existing-server)
- [3 Configuration](#3-configuration)
  * [3.1 Encoded passwords](#31-encoded-passwords)
  * [3.2 Property names](#32-property-names)
  * [3.3 Tenant-specific configuration properties](#33-tenant-specific-configuration-properties)
  * [3.4 Persistence layer configuration](#34-persistence-layer-configuration)
- [4 Customization](#4-customization)
  * [4.1 Extended operations](#41-extended-operations)
  * [4.2 Notification Service](#42-notification-service)
  * [4.3 Persistence interceptors](#43-persistence-interceptors)
  * [4.4 Resource validation](#44-resource-validation)
  * [4.5 “Update/Create” feature](#45-updatecreate-feature)
  * [4.6 FHIR client API](#46-fhir-client-api)
  * [4.7 FHIR command-line interface (fhir-cli)](#47-fhir-command-line-interface-fhir-cli)
  * [4.8 Using local references within request bundles](#48-using-local-references-within-request-bundles)
  * [4.9 Multi-tenancy](#49-multi-tenancy)
  * [4.10 Bulk data operations](#410-bulk-data-operations)
  * [4.11 CADF audit logging service](#411-CADF-audit-logging-service)
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

1.  To install the FHIR server, build or download the `fhir-install` zip installer (e.g. `fhir-server-distribution.zip` or `fhir-install-4.0.0-rc1-20191014-1610`).
The Maven build creates the zip package under `fhir-install/target`. Alternatively, releases will be made available from the [Releases tab](https://github.com/ibm/fhir/releases).

2.  Unzip the `.zip` package into a clean directory (referred to as `fhir-installer` here):
    ```
        mkdir fhir-installer
        cd fhir-installer
        unzip fhir-server-distribution.zip
    ```

3.  Determine an install location for the OpenLiberty server and the FHIR server webapp. Example:  `/opt/ibm/fhir-server`

4.  Run the `install.sh/.bat` script to install the server:
    ```
        ./fhir-server-dist/install.sh /opt/ibm/fhir-server
    ```
    This step installs the OpenLiberty runtime and the FHIR server web application. The Liberty runtime is installed in a directory called `wlp` within the installation directory that you specify. For example, in the preceding command, the root directory of the Liberty server runtime would be `/opt/ibm/fhir-server/wlp`.

5.  Configure the fhir-server's `server.xml` file as needed by completing the following steps:
    * Configure the ports that the server listen on. The server is installed with only port 9443 (HTTPS) enabled by default. To change the port numbers, modify the values in the `httpEndpoint` element.
    * Configure a server keystore and truststore. The FHIR server is installed with a default keystore file that contains a single self-signed certificate for localhost. For production use, you must create and configure your own keystore and truststore files for the FHIR server deployment (that is, generate your own server certificate or obtain a trusted certificate, and then share the public key certificate with API consumers so that they can insert it into their client-side truststore). The keystore and truststore files are used along with the server's HTTPS endpoint and the FHIR server's client-certificate-based authentication protocol to secure the FHIR server's endpoint. For more information, see [Section 5.2 Keystores, truststores, and the FHIR server](#52-keystores-truststores-and-the-fhir-server).
    * Configure an appropriate user registry. The FHIR server is installed with a basic user registry that contains a single user named `fhiruser`. For production use, it's best to configure your own user registry. For more information about configuring user registries, see the [OpenLiberty documentation](https://openliberty.io/guides/security-intro.html#configuring-the-user-registry).

6.  Make sure that your selected database product is running and ready to accept requests as needed:
    * By default, the FHIR server is installed with the JDBC persistence layer configured to use an Embedded Derby database. The default config will automatically bootstrap this database and so this step can be skipped. For any other configuration, note the database host and port and, if necessary, create a user with privileges for deploying the schema.

7.  Create and deploy the IBM FHIR Server database schema as needed:
    * By default, the FHIR server is installed with the JDBC persistence layer configured to use an Embedded Derby database. The default config will automatically bootstrap this database and so this step can be skipped. For any other configuration, use the `fhir-persistence-schema` module to create and deploy the database schema.
    * The Db2 persistence layer is always tenant-aware, so you must provision a tenant in addition to deploying the schema. Note the tenant name you provisioned and the tenantKey generated by `fhir-persistence-schema` for the next step.

8.  Configure the `fhir-server-config.json`<sup id="a1">[1](#f1)</sup> configuration file as needed:
    * By default, the FHIR server is installed with the JDBC persistence layer configured to use a single-tenant Embedded Derby database. For more information, see [Section 3.4 Persistence layer configuration](#34-persistence-layer-configuration).

8.  To start and stop the server, use the Liberty server command:
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
The FHIR server does not include an upgrade installer. To upgrade a server to the next version, you can run the installer on a separate server, and then copy the resulting configuration files over to the existing server.

To manage database updates over time, the FHIR server uses custom tools from the `fhir-database-utils` project. Through the use of a metadata table, the database utilities can detect the currently installed version of the database and apply any new changes that are needed to bring the database to the current level.

Complete the following steps to upgrade the server:

1. Run the fhir-installer on a separate server.  
2. Configure the new server as appropriate (`fhir-server.xml` and anything under the `fhir-server/config` and `fhir-server/userlib` directories).  
3. Back up your database.  
4. Run the migration program (see [Section 3.4.1.1.2 Db2](#34112-db2)).  
5. Disable traffic to the old server and enable traffic to the new server  

# 3 Configuration
This chapter contains information about the various ways in which the FHIR server can be configured by users.

## 3.1 Encoded passwords
In the examples within the following sections, you'll see the default password `change-password`. In order to secure your server, these values should be changed.

Optionally, the values can be encoded via the Liberty `securityUtility` command. For example, to encode a string value with the default `{xor}` encoding, run the following command:
```
<WLP_HOME>/bin/securityUtility encode stringToEncode
```

The output of this command can then be copied and pasted into your `server.xml` or `fhir-server-config.json` file as needed. The `fhir-server-config.json` does not support the securityUtility's `{aes}` encoding at this time, but per the limits to protection through password encryption<sup>[a]</sup>, this encoding does not provide significant additional security beyond `exclusive or` (XOR) encoding.

## 3.2 Property names
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

## 3.3 Tenant-specific configuration properties
The FHIR server supports certain multi-tenant features. One such feature is the ability to set certain configuration properties on a per-tenant basis.

In general, the configuration properties for a particular tenant are stored in the `<WLP_HOME>/usr/servers/fhir-server/config/<tenant-id>/fhir-server-config.json` file, where `<tenant-id>` refers to the tenant's “short name” or tenant id.

The global configuration is considered to be associated with a tenant named `default`, so those properties are stored in the `<WLP_HOME>/usr/servers/fhir-server/config/default/fhir-server-config.json` file.

Similarly, tenant-specific search parameters are found at `<WLP_HOME>/usr/servers/fhir-server/config/<tenant-id>/extension-search-parameters.json`, whereas the global/default extension search parameters are at `<WLP_HOME>/usr/servers/fhir-server/config/default/extension-search-parameters.json`.

Search parameters are handled like a single configuration properly; providing a tenant-specific file will override the global/default extension search parameters as defined at [FHIRSearchConfiguration](https://ibm.github.io/FHIR/guides/FHIRSearchConfiguration).

More information about multi-tenant support can be found in [Section 4.9 Multi-tenancy](#49-multi-tenancy).

## 3.4 Persistence layer configuration
The FHIR server is architected in a way that allows deployers to select the persistence layer implementation that fits their needs. Currently, the FHIR server includes a JDBC persistence layer which supports both Apache Derby and IBM Db2. Apache Derby is used for testing, whereas IBM Db2 is used in production environments.

The FHIR server is delivered with a default configuration that is already configured to use the JDBC persistence layer implementation with an Embedded Derby database. This provides the easiest out-of-the-box experience since it requires very little setup. The sections that follow in this chapter will focus on how to configure the JDBC persistence layer implementation with either Embedded Derby or Db2.

### 3.4.1 Configuring the JDBC persistence layer
#### 3.4.1.1 Database preparation
Before you can configure the FHIR server to use the JDBC persistence layer implementation, you first need to prepare the database. This step depends on the database product in use.

##### 3.4.1.1.1 Embedded Derby (default)
If you are configuring the FHIR server to use a single embedded Derby database, then you can configure the FHIR server to create the database and the schema and tables during startup. To configure the FHIR server to “bootstrap” the database in this way, modify the `fhirServer/persistence/jdbc/bootstrapDb` property in `fhir-server-config.json` as in the following example:

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

##### 3.4.1.1.2 Db2
If you configure the FHIR server to use an IBM Db2 database, you must:

1. create the database if it doesn't already exist

2. execute the `fhir-persistence-schema` utility to create the necessary schemas (tables, indices, stored procedures, etc) and tenants

3. configure the server with the tenantKey generated in step number 2.

An executable `fhir-persistence-schema` jar can be downloaded from the project's [Releases tab](https://github.com/IBM/FHIR/releases) and documentation can be found at https://github.com/IBM/FHIR/tree/master/fhir-persistence-schema.

For a detailed guide on configuring IBM Db2 on Cloud for the IBM FHIR Server, see [DB2OnCloudSetup](https://ibm.github.io/FHIR/guides/DB2OnCloudSetup).

#### 3.4.1.2 FHIR server configuration
To configure the FHIR server to use the JDBC persistence layer, complete the following steps:

1.  First, modify the `fhirServer/persistence/factoryClassname` property in `fhir-server-config.json` to specify the JDBC persistence factory, like this:
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

2.  Next, modify the `fhirServer/persistence/jdbc/dataSourceJndiName` property in `fhir-server-config.json` to specify the proxy datasource's JNDI name, like this:
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

3.  Next, modify the `fhirServer/persistence/datasources` property group to reflect the datastore(s) that you want to use. The following example defines the `default` datastore as an embedded derby database located in `wlp/usr/servers/fhir-server/derby/fhirDB`:
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
                        "connectionProperties": {
                            "serverName": "db2server1",
                            "portNumber": 50000,
                            "user": "db2inst1",
                            "password": "********",
                            "databaseName": "FHIRDB",
                            "currentSchema": "FHIR1",
                            "driverType": 4
                        }
                    }
                }
            …
            }
        }
    }
    ```

For more information on how to configure datastore properties, see [Section 3.4.2.2 Datastore configuration examples](#3422-datastore-configuration-examples).

### 3.4.2 Properties-based datastore configuration

Normally, a Liberty application that uses one or more Derby or Db2 datastores will require a datasource to be defined within the Liberty server.xml file for each database. One drawback to this approach is that each of the datasources are statically defined in the 'server.xml' file, which means that any updates (modifications, additions, etc.) will require a server re-start.

As part of it's multi-tenant support, the FHIR server provides an alternate mechanism which consists of a single “proxy datasource” along with a set of properties configured in the `fhir-server-config.json` file. This proxy datasource can be used by the JDBC persistence layer implementation to establish connections to either Derby or Db2 databases. This approach allows for new datastores to be configured without the need to restart the FHIR server.

#### 3.4.2.1 Proxy datasource
The FHIR server's proxy datasource allows us to configure a single statically-defined datasource in the Liberty 'server.xml' file, and then dynamically configure each of the datastores to be used by the FHIR server within the `fhir-server-config.json` file. The datasource definition within `server.xml` looks like this:

```
<dataSource id="fhirProxyDataSource" jndiName="jdbc/fhirProxyDataSource" type="javax.sql.XADataSource">
    <jdbcDriver libraryRef="fhirSharedLib"
       javax.sql.XADataSource="com.ibm.fhir.persistence.proxy.FHIRProxyXADataSource" />
</dataSource>
```

When the proxy datasource is obtained via a JNDI lookup by the JDBC persistence layer, and then its “getConnection()” method is called, the proxy datasource will use the current tenant-id and datastore-id to retrieve the configuration properties from the `fhir-server-config.json` file. The result will be the instantiation (or a successful cache lookup) of the appropriate XADataSource implementation class (according to the `type` field in the datastore configuration) and then a new connection will be obtained from it.

The proxy datasource relies on the presence of the tenant-id and datastore-id information within the thread-local `FHIRRequestContext` information. In a simple configuration of the FHIR server that involves the use of the JDBC persistence layer implementation, the `FHIRRequestContext` information is obtained via request headers from the incoming REST API request. The tenant-id is obtained from the `X-FHIR-TENANT-ID` request header and the datastore-id is obtained from the `X-FHIR-DSID` request header<sup id="a3">[3](#f3)</sup>.

#### 3.4.2.2 Datastore configuration examples
To understand how the configuration properties are defined for one or more datastores, let's start off with a couple of examples.

##### 3.4.2.2.1 Example 1
Here is a simple example of a single (default) datastore:
```
{
    "fhirServer":{
        "persistence":{
            "factoryClassname":"com.ibm.fhir.persistence.jdbc.FHIRPersistenceJDBCFactory",
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

##### 3.4.2.2.2 Example 2
This example shows a slightly more complex scenario. In this scenario, the `acme` tenant would like to store data in one of two study-specific Db2 databases with datastore-id values `study1` and `study2`. All resource types pertaining to a given study will be stored in that study's database so there's no need for a proxy persistence layer or routing rules, and so forth.

Furthermore, the REST API consumers associated with Acme applications will be coded to always set the `X-FHIR-TENANT-ID` request header to be `acme` and the `X-FHIR-DSID` request header to the specific datastore-id associated with each request (either `study1` or `study2`). In this case, the following properties would be configured within the “acme” tenant's `fhir-server-config.json` file<sup id="a4">[4](#f4)</sup> (`$⁠{server.config.dir}/config/acme/fhir-server-config.json`):
```
{
    "__comment":"Acme's FHIR server configuration",
    "fhirServer":{
        …
        "persistence":{
            "factoryClassname":"com.ibm.fhir.persistence.jdbc.FHIRPersistenceJDBCFactory",
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

#### 3.4.2.3 Datastore configuration reference
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

See the [Validation Guide - Built-in profile support](https://ibm.github.io/FHIR/guides/FHIRValidationGuide#built-in-profile-support) for a list of pre-packaged Implmentation Guide resources and see [Validation Guide - Making profiles available to the fhir registry](https://ibm.github.io/FHIR/guides/FHIRValidationGuide#making-profiles-available-to-the-fhir-registry-component-fhirregistry) for information about how to extend the server with additional Implementation Guide artifacts.

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

In order to establish a reference to a resource, you must first know its resource identifier. However, if you are using a request bundle to create both the referenced resource (`Patient` in this example) and the resource which references it (`Observation`), then it is impossible to know the `Patient`resource identifier before the request bundle has been process (that is, before the new `Patient` resource is created).

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
There is one rule for the use of local references within a request bundle:  A local identifier must be defined via a request entry's `fullUrl` field before that local identifier can be used in a local reference.

In the example in [Section 4.8.0.1](#4801-example-1-observation-references-patient-via-local-reference), you can see that there are two POST requests and the `Patient` request entry appears in the bundle before the `Observation` request entry. This example satisfies the rule because the FHIR server will process the POST request entries in the order in which they appear within the request bundle.

If, however, those entries were reversed, the FHIR server would return an error when processing the `Observation` request entry, because the `Patient` local identifier is not defined yet.

The following example also satisfies the rule:

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

The FHIR server first processes all POST requests found within a request bundle and then processes all PUT requests. So, the FHIR server will, in fact, process the `Patient` request entry (a POST) before it processes the `Observation` request entry (a PUT). Therefore, this would be considered a valid request bundle as well.

While processing a POST or PUT request entry within a request bundle, the FHIR server will detect the use of a local identifier within the entry's `fullUrl` field, and will establish a mapping between that local identifier and the corresponding external identifier that results from performing the POST or PUT operation.

For example, in Example 1 from [Section 4.8.0.1](#4801-example-1-observation-references-patient-via-local-reference), the FHIR server detects the use of the local identifier in the `Patient` request entry (`urn:uuid:7113a0bb-d9e0-49df-9855-887409388c69`) and -- after creating the new `Patient` resource -- establishes a mapping between the local identifier and the resulting external reference associated with the new `Patient` (for example, `Patient/1cc5d299-d2be-4f93-8745-a121232ffe5b`).

Then when the FHIR server processes the POST request for the `Observation`, it detects the use of the local reference and substitutes the corresponding external reference for it before creating the new `Observation` resource. Here is an example of a response bundle for the request bundle depicted in Example 1 in which we can see that the Observation's `subject.reference` field now contains a proper external reference to the newly-created `Patient` resource:

#### 4.8.1.2 Example 3: Response bundle for Example 1
```
{
    "resourceType" : "Bundle",
    "type" : "batch-response",
    "entry" : [ {
        "resource" : {
            "resourceType" : "Patient",
            "id" : "1cc5d299-d2be-4f93-8745-a121232ffe5b",
            …
        },
        "response" : {
            "id" : "1cc5d299-d2be-4f93-8745-a121232ffe5b",
            "status" : "201",
            "location" : "Patient/1cc5d299-d2be-4f93-8745-a121232ffe5b/_history/1",
            "etag" : "W/\"1\"",
            "lastModified" : "2017-03-01T20:56:59.540Z"
        }
    }, {
        "resource" : {
            "resourceType" : "Observation",
            "id" : "22b21fcf-8d00-492d-9de0-e25ddd409eaf",
            …
            "subject" : {
                "reference" : "Patient/1cc5d299-d2be-4f93-8745-a121232ffe5b"
            },
            …
        },
        "response" : {
            "id" : "22b21fcf-8d00-492d-9de0-e25ddd409eaf",
            "status" : "201",
            "location" : "Observation/22b21fcf-8d00-492d-9de0-e25ddd409eaf/_history/1",
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
            "serviceClassName" : "com.ibm.fhir.audit.logging.impl.DisabledAuditLogService",
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

There are 2 modules involved are:

- fhir-operation-bulkdata
- fhir-bulkimportexport-webapp   

The *fhir-operation-bulkdata* module implements the REST APIs for bulk data export, import and status as FHIR Operations.  There are four operations:

* ExportOperation - system export
* PatientExportOperation - Patient export
* GroupExportOperation - group export
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
</webApplicaion>
```

BulkData web application writes the exported FHIR resources to an IBM Cloud Object Storage (COS) or any Amazon S3-Compatible bucket as configured in the per-tenant server configuration under `fhirServer/bulkdata`. The following is an example configuration for bulkdata, please refer to section 5 for the detailed description of these properties:

```
"bulkdata": {
    "bulkDataBatchJobIdEncryptionKey": "change-password",
    "applicationName": "fhir-bulkimportexport-webapp",
    "moduleName": "fhir-bulkimportexport.war",
    "jobParameters": {
        "cos.bucket.name": "fhir-r4-connectathon",
        "cos.location": "us",
        "cos.endpointurl": "fake",
        "cos.credential.ibm": "Y",
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
    "maxInputPerRequest": 5
}
```

To use Amazon S3 bucket for exporting, please set cos.credential.ibm to `N`, set `cos.api.key` to S3 access key, and set `cos.srvinst.id` to the S3 secret key. The following is a sample path to the exported ndjson file, the full path can be found in the response to the polling location request after the export request (please refer to the FHIR BulkDataAccess spec for details).  

`.../fhir-bulkimexport-connectathon/6xjd4M8afi6Xo95eYv7zPxBqSCoOEFywZLoqH1QBtbw=/Patient_1.ndjson`

To import using the `$import` proposal, one must additionally configure the `fhirServer/bulkdata/validBaseUrls`. For example, if one stores bulkdata on `https://test-url.cos.ibm.com/bucket1` and `https://test-url.cos.ibm.com/bucket2` you must specify both baseUrls in the configuration:

```json
    "validBaseUrls": [
        "https://test-url.cos.ibm.com/bucket1",
        "https://test-url.cos.ibm.com/bucket2"
    ]
```

These base urls are not checked when using cloud object store and bulk-import. If you need to disable the validBaseUrls feature you may add `fhirServer/bulkdata/validBaseUrlsDisabled` as `true`.

The `fhirServer/bulkdata/maxInputPerRequest` is used to configure a maximum number of inputs supported by the instance.  The default number is 5.

Note: When `$import` is executed, the data must include a specified `Resource.id`, if not specified, the data is logged as being in Error.

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

For the Import Operation, the polled status includes an indication of `$import` and the location of the OperationOutcome NDJsons and the corresonding failure and success counts.

Note, the deletion of an a job is split into two phases, ACCEPTED (202) response and DELETED (204).  202 is returned until the oepration is stopped or removed, and then 204.

By default, the exported `ndjson` file is configured with public access automatically and with 2 hours expiration time, the randomly generated secret in the path is used to protect the file. please note that IBM COS does not support expiration time for each single COS object, so please configure retention policy (e.g, 1 day) for the bucket if IBM COS is used. For both Amazon S3 and IBM COS, please remember that public access should never be configured to the bucket itself.

Note: `fhirServer/bulkdata/isExportPublic` can be set to "false" to disable public access.

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

By default, in-memory Derby database is used for persistence of the JavaBatch Jobs as configured in batchDs.xml. Instruction is also provided in "Configuring a Liberty Datasource with API Key" section of the DB2OnCloudSetup guide to configure DB2 service in IBM Clouds as JavaBatch persistence store. Liberty JavaBatch framework creates DB schema and tables automatically by default for both approaches, and the configured database is created automatically, if the in-memory Apache Derby approach is used.

You can also choose to use postgresql or other RDBMS as your Job repository, if taking this approach, you will need to generate the DDL for the job tables first following [IBM Websphere Liberty Batch - Job Repository Configuration](https://www-03.ibm.com/support/techdocs/atsmastr.nsf/WebIndex/WP102716) , then make necessary changes to the generated DDL to make it valid for your RDBMS, and then create the job database, create javabatch user, create jbatch schema and then run the DDL to generate the job tables and enable it in batchDs.xml. let's use postgresql as an example:

(1) Generate DDL and modify it according to your RDBMS.

``` shell
 ./ddlGen generate fhir-server
```
then modify the generated DLL file to make it valid for postgresql.
or you can simply use this sample DDL file: [fhir-bulkimportexport-webapp/src/test/resources/batchPersistence-postgresql.ddl](https://github.com/IBM/FHIR/tree/master/fhir-bulkimportexport-webapp/src/test/resources/batchPersistence-postgresql.ddl)

(2) Create the Job database and user.  

``` shell
psql postgres
>postgres=# create database jobdb;
>postgres=# create user javabatch with password 'change-password';
>postgres=# grant all privileges on database jobdb to javabatch;
```
(3) Create jbatch schema.

``` shell
psql -d jobdb -U javabatch
jobdb=> CREATE SCHEMA jbatch;
```
(4) Run the modified DDL with javabatch user against the job database.

``` shell
psql -v ON_ERROR_STOP=1 -1 -U javabatch -f batchPersistence-postgresql.ddl -d jobdb
```
(5) Enable postgresql job repository in batchDs.xml as following.

``` xml
<server description="fhir-server">
    <dataSource id="fhirbatchDS" jndiName="jdbc/fhirbatchDB" schema="JBATCH">
    <jdbcDriver libraryRef="fhirSharedLib" />
    <properties.postgresql
        databaseName="jobdb"
        portNumber="5432"
        serverName="postgresql-server-hostname"
        user="javabatch"
        password="change-password" />
    </dataSource>
    <batchPersistence jobStoreRef="BatchDatabaseStore"/>
    <databaseStore dataSourceRef="fhirbatchDS" id="BatchDatabaseStore" schema="JBATCH" tablePrefix=""/>
</server>
```

Note: If you use PostgreSQL database as IBM FHIR Server data store or the JavaBatch job repository, please enable `max_prepared_transactions` in postgresql.conf, otherwise the import/export JavaBatch jobs fail.

For more information about Liberty JavaBatch configuration, please refer to [IBM WebSphere Liberty Java Batch White paper](https://www-03.ibm.com/support/techdocs/atsmastr.nsf/webindex/wp102544).

### 4.10.1 Integration Testing
To integration test, there are tests in `ExportOperationTest.java` in `fhir-server-test` module with server integration test cases for system, patient and group export. Further, there are tests in `ImportOperationTest.java` in `fhir-server-test` module.

## 4.11 CADF audit logging service
The CADF audit logging service pushs FHIR server audit events for FHIR operations in [Cloud Auditing Data Federation (CADF)]( https://www.dmtf.org/standards/cadf) standard format to IBM Cloud Event Streams service, these FHIR operations include create, read, update, delete, version read, history, search, validate, custom operation, meta and bundle, these operations are mapped to CADF actions as following:

| FHIR Operation                 | CADF Action   |
|--------------------------------| --------------|
|`read,versionread,history,search,validate,meta` |    read       |
|`create`                        |    create     |
|`update`                        |    update     |
|`delete`                        |    delete     |
|`operation,bundle`              |    unknown    |

Each FHIR create, update, delete, bundle or custom operation triggers 2 CADF events - begins with an event with "pending" outcome and ends with an event with "success" or "failure" outcome; All the other FHIR operations only trigger 1 CADF event with either "success" or "failure" outcome.

### 4.11.1 Enable CADF audit logging service
Please refer to the properties names started wtih fhirServer/audit/ in [5.1 Configuration properties reference](#51-configuration-properties-reference) for how to enable and configure CADF audit logging service.

### 4.11.2 Event Streams configuation of CADF audit logging service
The CADF audit logging service gets event streams service credential from env variable EVENT_STREAMS_AUDIT_BINDING with values like this:

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
The service credential is generated automatically when you run

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
please refer to https://cloud.ibm.com/docs/containers?topic=containers-service-binding for detailed instruction if need.

### 4.11.3 Query CADF events in COS
[Waston studio stream flow]( https://cloud.ibm.com/docs/tutorials?topic=solution-tutorials-big-data-log-analytics#create-a-streams-flow-source ) can be created to push those FHIR Audit CADF events from Event Streams service to COS bucket(e.g fhir-audit-dev0) in CSV format; Another option is to configure Event Streams(Kafka) S3 connect to push those CADF events to COS bucket(e.g, fhir-audit-dev0) but in raw CADF json format.
A service instance of the [IBM Cloud SQL Query]( https://www.ibm.com/cloud/blog/analyzing-data-with-ibm-cloud-sql-query ) service can be created to allow you to query those CADF audit events in COS with SQL queries, before you run sql query, you'd better create a COS bucket to store your query results, otherwise, the query results will be stored in a bucket which is automatically created by the SQL query service.

Samples queries for CSV records expaned from the JSON CADF events:

```
select * from cos://us-south/fhir-audit-dev where EVENTTIME BETWEEN "2019-06-07" and "2019-06-08" and action="unknown" into cos://us-south/fhir-audit-dev-res stored as csv

select action, count(*) from cos://us-south/fhir-audit-dev where EVENTTIME BETWEEN "2019-06-05" and "2019-06-06" group by action into cos://us-south/fhir-audit-dev-res stored as csv

select * from cos://us-south/fhir-audit-dev where EVENTTIME BETWEEN "2019-06-05" and "2019-06-06" and action="create" and initiator_id="default@fhir-server" into cos://us-south/fhir-audit-dev-res stored as csv

select * from cos://us-south/fhir-audit-dev where EVENTTIME BETWEEN "2019-06-05" and "2019-06-06" and ATTACHMENTS_CONTENT LIKE '%fhir-read%' into cos://us-south/fhir-audit-dev-res stored as csv
```

Samples queries for the raw JSON CADF events:

```
select * from cos://us-south/fhir-audit-dev0 stored as json where EVENTTIME BETWEEN "2019-06-01" and "2019-06-08" and action="unknown" into cos://us-south/fhir-audit-dev0-res stored as json

select action, count(*) from cos://us-south/fhir-audit-dev0 stored as json where EVENTTIME BETWEEN "2019-06-01" and "2019-06-06" group by action into cos://us-south/fhir-audit-dev0-res stored as csv

select * from cos://us-south/fhir-audit-dev0 stored as json where EVENTTIME BETWEEN "2019-06-01" and "2019-06-06" and action="create" and initiator.id="default@fhir-server" into cos://us-south/fhir-audit-dev-res stored as json

select * from cos://us-south/fhir-audit-dev0 stored as json where EVENTTIME BETWEEN "2019-06-01" and "2019-06-06" and ATTACHMENTS[0].CONTENT LIKE '%fhir-read%' into cos://us-south/fhir-audit-dev-res stored as json

```


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
|`fhirServer/core/defaultHandling`|string|The default handling preference of the server (`strict | lenient`) which determines how the server handles unrecognized search parameters and resource elements.|
|`fhirServer/core/allowClientHandlingPref`|boolean|Indicates whether the client is allowed to override the server default handling preference using the `Prefer:handling` header value part.|
|`fhirServer/core/checkReferenceTypes`|boolean|Indicates whether reference type checking is performed by the server during parsing / deserialization.|
|`fhirServer/core/serverRegistryResourceProviderEnabled`|boolean|Indicates whether the server registry resource provider should be used by the FHIR registry component to access definitional resources through the persistence layer.|
|`fhirServer/core/conditionalDeleteMaxNumber`|integer|The max number of matches supported in conditional delete. |
|`fhirServer/core/capabilityStatementCacheTimeout`|integer|The number of minutes that a tenant's CapabilityStatement is cached for the metadata endpoint. |
|`fhirServer/searchParameterFilter`|property list|A set of inclusion rules for search parameters. See [FHIR Search Configuration](https://ibm.github.io/FHIR/guides/FHIRSearchConfiguration#12-Configuration--Filtering-of-search-parameters) for more information.|
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
|`fhirServer/persistence/datasources`|map|A map containing datasource definitions. See [Section 3.4.2.3 Datastore configuration reference](#3423-datastore-configuration-reference) for more information.|
|`fhirServer/persistence/jdbc/dataSourceJndiName`|string|The JNDI name of the DataSource to be used by the JDBC persistence layer.|
|`fhirServer/persistence/jdbc/bootstrapDb`|boolean|A boolean flag which indicates whether the JDBC persistence layer should attempt to create or update the database and schema at server startup time.|
|`fhirServer/oauth/regUrl`|string|The registration URL associated with the OAuth 2.0 authentication/authorization support.|
|`fhirServer/oauth/authUrl`|string|The authorization URL associated with the OAuth 2.0 authentication/authorization support.|
|`fhirServer/oauth/tokenUrl`|string|The token URL associated with the OAuth 2.0 authentication/authorization support.|
|`fhirServer/audit/serviceClassName`|string|The audit service to use. Currently, com.ibm.fhir.audit.logging.impl.WhcAuditCadfLogService and com.ibm.fhir.audit.logging.impl.DisabledAuditLogService are supported.|
|`fhirServer/audit/serviceProperties/auditTopic`|string|The kafka topic to use for CADF audit logging service|
|`fhirServer/audit/serviceProperties/geoCity`|string|The Geo City configure for CADF audit logging service.|
|`fhirServer/audit/serviceProperties/geoState`|string|The Geo State configure for CADF audit logging service.|
|`fhirServer/audit/serviceProperties/geoCounty`|string|The Geo Country configure for CADF audit logging service.|
|`fhirServer/search/useBoundingRadius`|boolean|True, the bounding area is a Radius, else the bounding area is a box.|
|`fhirServer/bulkdata/applicationName`| string|Fixed value, always set to fhir-bulkimportexport-webapp |
|`fhirServer/bulkdata/moduleName`|string| Fixed value, always set to fhir-bulkimportexport.war |
|`fhirServer/bulkdata/jobParameters/cos.bucket.name`|string|Object store bucket name |
|`fhirServer/bulkdata/jobParameters/cos.location`|string|Object store location |
|`fhirServer/bulkdata/jobParameters/cos.endpointurl`|string|Object store end point url |
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
|`fhirServer/bulkdata/cosFileMaxResources`|int|The maximum number of FHIR resources per COS file, "-1" means no limit, the default value is 500000 |
|`fhirServer/bulkdata/cosFileMaxSize`|int|The maximum COS file size in bytes, "-1" means no limit, the default value is 209715200 (200M) |


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
|`fhirServer/searchParameterFilter`|`"*": [*]`|
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
|`fhirServer/oauth/regUrl`|""|
|`fhirServer/oauth/authUrl`|""|
|`fhirServer/oauth/tokenUrl`|""|
|`fhirServer/audit/serviceClassName`|""|
|`fhirServer/audit/serviceProperties/auditTopic`|FHIR_AUDIT|
|`fhirServer/audit/serviceProperties/geoCity`|Dallas|
|`fhirServer/audit/serviceProperties/geoState`|TX|
|`fhirServer/audit/serviceProperties/geoCounty`|US|
|`fhirServer/bulkdata/isExportPublic`|true|
|`fhirServer/bulkdata/validBaseUrlsDisabled`|false|
|`fhirServer/bulkdata/cosFileMaxResources`|500000|
|`fhirServer/bulkdata/cosFileMaxSize`|209715200|


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
|`fhirServer/searchParameterFilter`|Y|Y|
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
|`fhirServer/persistence/jdbc/dataSourceJndiName`|N|N|
|`fhirServer/persistence/jdbc/bootstrapDb`|N|N|
|`fhirServer/oauth/regUrl`|N|N|
|`fhirServer/oauth/authUrl`|N|N|
|`fhirServer/oauth/tokenUrl`|N|N|
|`fhirServer/audit/serviceClassName`|N|N|
|`fhirServer/audit/serviceProperties/auditTopic`|N|N|
|`fhirServer/audit/serviceProperties/geoCity`|N|N|
|`fhirServer/audit/serviceProperties/geoState`|N|N|
|`fhirServer/audit/serviceProperties/geoCounty`|N|N|
|`fhirServer/bulkdata/jobParameters/cos.bucket.name`|Y|Y|
|`fhirServer/bulkdata/jobParameters/cos.location`|Y|Y|
|`fhirServer/bulkdata/jobParameters/cos.endpointurl`|Y|Y|
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
The IBM FHIR Server supports these via Liberty's OpenID Connect support.
The following sections are adapted from the [WebSphere Liberty Knowledge Center](https://www.ibm.com/support/knowledgecenter/SSD28V_liberty/com.ibm.websphere.wlp.core.doc/ae/twlp_config_oidc_pc_examp_beginner.html), but the steps apply to OpenLiberty as well.

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
To configure the FHIR Server with the OpenID Connect and OAuth 2.0 endpoints of the providers, specify the following values in the default fhir-server-config.json file:
* `fhirServer/oauth/regUrl`
* `fhirServer/oauth/authUrl`
* `fhirServer/oauth/tokenUrl`

When the Liberty server is the OpenID Connect / OAuth 2.0 provider, use a placeholder of `<host>` in the property values to have the server automatically replace this text with the hostname used by requestors (see `fhirServer/core/originalRequestUriHeaderName`).

These values will be used to populate the corresponding entries in both the server capability statement (`GET [base]/metadata`) and the smart-configuration (`GET [base]/.well-known/smart-configuration`).

For example, the following excerpt from a CapabilityStatement shows sample OAuth-related URLs (token, authorize, and register) as values of the `valueUri` elements.
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
