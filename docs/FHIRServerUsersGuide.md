<!--

---

Copyright:

  years: 2017, 2019
lastupdated: "2019-09-04"

---

-->
- [1 Overview](#1-overview)
  * [1.1 Recent updates](#11-recent-updates)
- [2 Installation](#2-installation)
  * [2.1 Installing a new server](#21-installing-a-new-server)
  * [2.2 Upgrading an existing server](#22-upgrading-an-existing-server)
- [3 Configuration](#3-configuration)
  * [3.1 Encoded passwords](#31-encoded-passwords)
  * [3.2 Property names](#32-property-names)
  * [3.3 Tenant-specific configuration properties](#33-tenant-specific-configuration-properties)
  * [3.4 Persistence layer configuration](#34-persistence-layer-configuration)
- [4 Customization options](#4-customization-options)
  * [4.2 Notification Service](#42-notification-service)
  * [4.3 Persistence interceptors](#43-persistence-interceptors)
  * [4.4 Resource validation](#44-resource-validation)
  * [4.5 “Update/Create” feature](#45-updatecreate-feature)
  * [4.6 FHIR client API](#46-fhir-client-api)
  * [4.7 FHIR command-line interface (fhir-cli)](#47-fhir-command-line-interface-fhir-cli)
  * [4.8 Using local references within request bundles](#48-using-local-references-within-request-bundles)
  * [4.9 Multi-tenancy](#49-multi-tenancy)
  * [4.10 Extended operations](#410-extended-operations)
  * [4.11 CADF audit logging service](#411-CADF-audit-logging-service)
- [5 Appendix](#5-appendix)
  * [5.1 Configuration properties reference](#51-configuration-properties-reference)
  * [5.2 Keystores, truststores, and the FHIR server](#52-keystores-truststores-and-the-fhir-server)
  * [5.3 Custom HTTP Headers](#53-custom-http-headers)
  * [5.4 Notes about the FHIRJsonParser](#54-notes-about-the-fhirjsonparser)
- [6 Related topics](#6-related-topics)

# 1 Overview

The IBM FHIR Server provides a REST API that is patterned after the HL7 FHIR specification and supports the full set of FHIR-defined resource types.
The FHIR server is intended to be a common component for providing FHIR capabilities within health services and solutions.

## 1.1 Recent updates
View information about recent changes that were made to this document. For more information about changes that were made to the FHIR server codebase, see the [CHANGELOG](CHANGELOG.md).

### Release 2.2
* Moved User Guide to this repo converted to Markdown
* Split large table in [Section 5.1](#51-configuration-properties-reference) into three separate tables to improve display on GitHub Enterprise
* Replaced references to Jenkins with links to Artifactory
* Deleted the <q>Historical Information</q> section
* Introduced [CHANGELOG.md](CHANGELOG.md) for tracking changes to the FHIR server from one version to the next
* Introduced [Conformance.md](Conformance.md) for documenting spec coverage and list any deviations
* Added section on upgrading from one version to the next

###	Release 2.1
IBM FHIR Server version 2.1 was developed under the Watson Health development organization. 

###	Release 1.2
*	Added information about the update/create feature.
*	Added information about the new JSON-based configuration model, and updated configuration examples throughout the document.
*	Updated the information about configuring the FHIR server for IBM Db2&reg;.
*	Added instructions for creating the database and the schema that is required for OAuth 2.0 support.
*	Corrected links to Liberty OAuth documentation.
*	Added the section on Local References within request bundles.
*	Added information about configuring the JDBC persistence layer.
*	Added information about tenant-specific configuration parameters.
*	Added reference table of supported configuration properties ([Section 5.1](#51-configuration-properties-reference)).
*	Added information about tenant-specific data store configuration.
*	Added the table of contents.
*	Corrected link to installer.

# 2 Installation

## 2.1 Installing a new server
1.	To install the FHIR server, first obtain the installation package `fhir-server-distribution.zip`.
The Maven build creates the zip package under `fhir-install/target`. Alternatively, releases will be made available from the [Releases tab](https://github.com/ibm/fhir/releases).

2.	Decompress the `.zip` file into a clean directory (referred to as `/fhir-installer` here):
```
    mkdir /fhir-installer
    cd /fhir-installer
    unzip fhir-install-2.2.0-5-201810161109.zip
```

3.	Determine an installation location for the WebSphere&reg; Liberty server and FHIR server web app. Example:  `/opt/ibm/fhir-server`

4.	Run the `install.sh/.bat` script to install the server:
```
    cd /fhir-installer
    ./fhir-server-dist/install.sh /opt/ibm/fhir-server
```
This step installs the WebSphere Liberty runtime and the FHIR server web application. The Liberty runtime is installed in a directory that is called `wlp` within the installation directory that you specify. For example, in the preceding command, the root directory of the Liberty server runtime is  `/opt/ibm/fhir-server/wlp`.

5.	Configure the fhir-server's `server.xml` file as needed by completing the following steps:
*	Configure the ports that the server listen on. The server is installed with only port 9443 (HTTPS) enabled by default. To change the port numbers, modify the values in the `httpEndpoint` element.
*	Configure a server keystore and truststore. The FHIR server is installed with a default keystore file that contains a single self-signed certificate for localhost. For production use, you must create and configure your own keystore and truststore files for the FHIR server deployment (that is, generate your own server certificate or obtain a trusted certificate, and then share the public key certificate with API consumers so that they can insert it into their client-side truststore). The keystore and truststore files are used along with the server's HTTPS endpoint and the FHIR server's client-certificate-based authentication protocol to secure the FHIR server's endpoint. For more information, see [Section 5.2 Keystores, truststores, and the FHIR server](#52-keystores-truststores-and-the-fhir-server).
*	Configure an appropriate user registry. The FHIR server is installed with a basic user registry that contains a single user named `fhiruser`. For production use, it's best to configure your own user registry. More information about configuring user registries, see the WebSphere Liberty documentation.

6.	Configure the `fhir-server-config.json`<sup id="a1">[1](#f1)</sup> configuration file as needed:
*	By default, the FHIR server is installed with the JDBC persistence layer configured to use an Embedded Derby database. This configuration provides a convenient default, but for production usage, it's best to configure the persistence layer to use Db2. For more information, see [Section 3.4 Persistence layer configuration](#34-persistence-layer-configuration).
*	Configure an encryption key to support the encryption of REST API payloads.

7.	Make sure that your selected database product is running and ready to accept requests.
*	If you're using Db2, make sure that it's started and that the Db2 server is listening on the port that is configured in your `fhir-server-config.json`. Also, make sure that you've created or updated the schema to be used, and that you've configured the schema name in the datasource entries of the `fhir-server-config.json` file. As described in [Section 3.4.1.1.2 Db2](#34112-db2), the fhir-install package includes scripts that invoke liquibase to create the database and database schema.

8.	Before you start the server, make sure that you are using a FIPS-configured Java&trade; 8, which is required by the FHIR server. Java 8 is installed as part of the installation package, which is in the following directory: `${WLP_HOME}/ibm-java-x86_64-80`. Be sure to set the `JAVA_HOME` environment variable to this location. The FHIR server does not initialize properly if you are not using Java 8, and the IBMJCEFIPS configuration is required due to the encryption requirements of the audit logging component.

9.	To start and stop the server, use the WebSphere Liberty server command:
```
<WLP_HOME>/bin/server start fhir-server
<WLP_HOME>/bin/server stop fhir-server
```

10.	After you start the server, you can verify that it's running properly by invoking the `metadata` REST API, like this:
```
curl -k -u <username> https://<host>:<port>/fhir-server/api/v1/metadata
```
where `<username>` is one of the users configured in `server.xml` (default is `fhiruser`).
The preceding command should produce output similar to the following:
```
{
    "resourceType" : "Conformance",
    "version" : "1.0.0",
    "name" : "IBM FHIR Server server",
    "publisher" : "IBM Corporation",
    "date" : "Mon Jun 27 16:06:45 CDT 2016",
    "description" : "IBM FHIR Server version 1.0.0 build id development",
    "copyright" : "(c) Copyright IBM Corporation 2016",
    "kind" : "instance",
    "software" : {
        "id" : "development",
        "name" : "IBM FHIR Server",
        "version" : "1.0.0"
    },
    "fhirVersion" : "1.0.2 - r4",
    "format" : [ "application/json", "application/json+fhir", "application/xml", "application/xml+fhir" ]
    ...
}
```

For more information about the conformance of the implementation, see [Conformance.md](Conformance.md).

## 2.2 Upgrading an existing server
The FHIR server does not include an upgrade installer. To upgrade a server to the next version, you can run the installer on a separate server, and then copy the resulting configuration files over to the existing server.

To manage database updates over time, the FHIR server uses [liquibase](https://www.liquibase.org/). By defining all schema updates via changeset, liquibase can detect the currently installed version of the database and apply any new changes that are needed to bring the database to the current level.

Complete the following steps to upgrade the server:
1. Run the fhir-installer on a separate server.
2. Configure the new server as appropriate (`fhir-server.xml` and anything under the `fhir-server/config` and `fhir-server/userlib` directories).
3. Back up your database.
4. Run the liquibase migration script (see example at [Section 3.4.1.1.2 Db2](#34112-db2)).
5. Disable traffic to the old server and enable traffic to the new server

### 2.2.1 Upgrading from 2.1.x to 2.2.0
The FHIR 2.2.0 release contains a few database updates, which makes it difficult to avoid downtime during an upgrade. Be particularly aware of the following changes during the upgrade:
1. Issue #53 - expanded stored procedure parametertype arrays; and
2. Issue #13 - fixed race condition in the `add_resource` stored procedures.

FHIR 2.2.0 upgrades WebSphere Liberty from version 17.0.0.1 to version 18.0.0.2. There are also updates to several Liberty features and a few of the FHIR server dependencies. If you modified your `server.xml`, you might want to merge your changes with the new `server.xml` that was created by the installer.

Finally, for users who extended the FHIR server (via custom operations or custom persistence options), Release 2.2.0 contains a few breaking changes to the Java libraries. The keys changes to be aware of are:
1. Issue #63 - refactored FHIRException class hierarchy and improved error handling; and
2. Issue #40 - introduced required getHealth() method on the FHIRPersistence interface (for use with the new $healthcheck operation).

A complete list of changes is available in the [CHANGELOG](CHANGELOG.md).

# 3 Configuration
This chapter contains information about the various ways in which the FHIR server can be configured by users.

## 3.1 Encoded passwords
In the examples contained within the following sections, you'll see encoded passwords and other values which appear as `“{xor}...”`. These values have been encoded by the `securityUtility` command provided by the WebSphere Liberty server.
To encode a string value, run the following command:
```
<WLP_HOME>/bin/securityUtility encode stringToEncode
```

and the following output is generated:

`{xor}abc-change-me=`

This output can then be copied and pasted into your server.xml or `fhir-server-config.json` file as needed.

The `fhir-server-config.json` does not support the securityUtility's {aes} encoding at this time, but per the limits to protection through password encryption<sup>[a]</sup>, this encoding does not provide significant additional security beyond `exclusive or` (XOR) encoding.

## 3.2 Property names
Configuration properties stored within a `fhir-server-config.json` file are structured in a hierarchical manner. Here is an example:
```
{
     "fhirServer":{
        "core":{
            "truststoreLocation":"resources/security/fhirTruststore.jks",
            "truststorePassword":"{xor}change-me=",
            "userDefinedSchematronEnabled":true
        }
    …
    }
}
```

To enable the encryption feature, you would set the “enabled” field within the “encryption” sub-structure to true.

Throughout this document, we use a path notation to refer to property names. For example, the name of the `enabled` property in the preceding example would be `fhirServer/test/enabled`. This refers to the fact that the `enabled` field exists within the `test` field within the `fhirServer` field.

## 3.3 Tenant-specific configuration properties
The FHIR server supports certain multi-tenant features. One such feature is the ability to set certain configuration properties on a per-tenant basis.
In general, the configuration properties for a particular tenant are stored in the `<WLP_HOME>/wlp/usr/servers/fhir-server/config/<tenant-id>/fhir-server-config.json` file, where `<tenant-id>` refers to the tenant's “short name” or tenant id. The global configuration is considered to be associated with a tenant named `default`, so those properties are stored in the `<WLP_HOME>/wlp/usr/servers/fhir-server/config/default/fhir-server-config.json` file. Similarly, tenant-specific search parameters are found at `<WLP_HOME>/wlp/usr/servers/fhir-server/config/<tenant-id>/extension-search-parameters.json` whereas the global/default extension search parameters are at `<WLP_HOME>/wlp/usr/servers/fhir-server/config/default/extension-search-parameters.json`. Search parameters are handled like a single configuration properly; providing a tenant-specific file will override the global/default extension search parameters.

[FHIRSearchConfiguration.md](FHIRSearchConfiguration.md)

More information about multi-tenant support can be found in [Section 4.10 Multi-tenancy](#410-multi-tenancy).

## 3.4 Persistence layer configuration
The FHIR server is architected in a way that allows deployers to select the persistence layer implementation that fits their needs. Currently, the FHIR server includes a JDBC persistence layer which supports both Derby and Db2.
The FHIR server is delivered with a default configuration that is already configured to use the JDBC persistence layer implementation with an Embedded Derby database. This provides the easiest out-of-the-box experience since it requires very little setup. The sections that follow in this chapter will focus on how to configure the JDBC persistence layer implementation with either Embedded Derby or Db2.

### 3.4.1 Configuring the JDBC persistence layer
#### 3.4.1.1 Database preparation
Before you can configure the FHIR server to use the JDBC persistence layer implementation, you first need to prepare the database. This step depends on the database product in use.
##### 3.4.1.1.1 Embedded Derby (default)
If you are configuring the FHIR server to use a single embedded Derby database, then you can configure the FHIR server to create the database and the required FHIR server schema DDL (tables, and so on) automatically during server startup. To configure the FHIR server to “bootstrap” the database during server startup, modify the `fhirServer/persistence/jdbc/bootstrapDb` property in `fhir-server-config.json`, as in the following example:

```
{
    "fhirServer":{
        ...
        "persistence":{
            ...
            "jdbc":{
                "bootstrapDb":true,
                ...
            },
	    ...
        }
    }
}
```

This configuration causes the server to run the liquibase tool during server startup to bring the schema DDL in sync with the currently installed FHIR server code. This database bootstrap step is only performed for a Derby database.

##### 3.4.1.1.2 Db2
If you configure the FHIR server to use a Db2 database, you must create the database if it doesn't already exist, and then run the liquibase tool to create the necessary schema (tables, indexes, and other elements). The FHIR server distribution includes some database-related tools, including the `createDB.sh` script. You can use this script to create your database, or you can create it on your own. To run the `createDB.sh` script, run the following command<sup id="a2">[2](#f2)</sup>:
```
<WLP_HOME>/fhir/bin/createDB.sh <db-user>
```
where `<db-user>` represents the user name of the administrative user for the database. This user name is typically the same as the administrative user associated with the Db2 instance in which the database is being created (typically `db2inst1`). Specify a user name that is appropriate for your situation. If you don't specify a user name, the currently logged in user is  used.

Note: The `createDB.sh` script is provided for your convenience, but you can choose the method that you use to create the database. The default name of the database is `FHIRDB`, but you can choose another name. Make sure that you configure the datastore-related properties to reflect your chosen database name.

After the database is created, run the liquibase command to create the schema (tables, indexes, and other elements) as in the following example:
```
cd <WLP_HOME>/fhir/bin/schemaddl

./liquibase --defaultsFile=./fhir.properties --changeLogFile=ddl/db2/normalized-schema/fhirserver.db2.normalized.xml update
```

#### 3.4.1.2 FHIR server configuration
To configure the FHIR server to use the JDBC persistence layer, complete the following steps:
1.	First, modify the `fhirServer/persistence/factoryClassname` property in `fhir-server-config.json` to specify the JDBC persistence factory, like this:
```
{
    “fhirServer”: {
        ...
        “persistence”: {
            "factoryClassname": "com.ibm.fhir.persistence.jdbc.FHIRPersistenceJDBCFactory",
            …
        }
}
```
2.	Next, modify the `fhirServer/persistence/jdbc/dataSourceJndiName` property in `fhir-server-config.json` to specify the proxy datasource's JNDI name, like this:
```
{
    “fhirServer”: {
        ...
        “persistence”: {
            ...
            "jdbc": {
                …
                “dataSourceJndiName”: “jdbc/fhirProxyDataSource”
            }
            …
        }
}
```
3.	Next, modify the `fhirServer/persistence/datasources` property group to reflect the datastore(s) that you want to use. The following example defines the `default` datastore as an embedded derby database located in `wlp/usr/servers/fhir-server/derby/fhirDB`:
```
{
    "fhirServer":{
        ...
        "persistence":{
            ...
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

The next example defines the `default` datastore as a DB2 database accessible on the `db2server1` host:
```
{
    "fhirServer":{
        ...
        "persistence":{
            "datasources": {
                "default": {
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
	    ...
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
                "schemaType":"normalized",
                "dataSourceJndiName": "jdbc/fhirDB"
            },

	}
    }
}
```
In this example, we define an embedded Derby database named `derby/fhirDB` (a location relative to the `<WLP_HOME>/wlp/usr/servers/fhir-server` directory. The datastore-id associated with this datastore is `default`, which is the value that is used if no `X-FHIR-DSID` request header is found in the incoming request. So, when only a single database is being used, it's wise to leverage the `default` datastore-id value to allow REST API consumers to avoid having to set the `X-FHIR-DSID` request header on each request.

##### 3.4.2.2.2 Example 2
This example shows a slightly more complex scenario. In this scenario, the `acme` tenant would like to store data in one of two study-specific Db2 databases with datastore-id values `study1` and `study2`. All resource types pertaining to a given study will be stored in that study's database so there's no need for a proxy persistence layer or routing rules, and so forth.

Furthermore, the REST API consumers associated with Acme applications will be coded to always set the `X-FHIR-TENANT-ID` request header to be `acme` and the `X-FHIR-DSID` request header to the specific datastore-id associated with each request (either `study1` or `study2`). In this case, the following properties would be configured within the “acme” tenant's `fhir-server-config.json` file<sup id="a4">[4](#f4)</sup> (`$⁠{server.config.dir}/config/acme/fhir-server-config.json`):
```
{
    "__comment":"Acme's FHIR server configuration",
    "fhirServer":{
        ...
        "persistence":{
            "factoryClassname":"com.ibm.fhir.persistence.jdbc.FHIRPersistenceJDBCFactory",
            ...
            "datasources": {
                "study1": {
                    "type": "db2",
                    "connectionProperties": {
                        "serverName": "dbserver1",
                        "portNumber": "50000",
                        "user": "db2inst1",
                        "password": "{xor}change-me=",
                        "database": "ACMESTUDY1",
                        "currentSchema": "DB2INST1"
                    }
                },
                "study2": {
                    "type": "db2",
                    "connectionProperties": {
                        "serverName": "dbserver1",
                        "portNumber": "50000",
                        "user": "db2inst1",
                        "password": "{xor}change-me=",
                        "database": "ACMESTUDY2",
                        "currentSchema": "DB2INST1"
                    }
                }
            }
            ...
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

The `type` property indicates the database type (currently the supported types are `db2` and `derby`).

The `connectionProperties` property is a set of driver-specific properties needed to connect to an instance of that database type. For a Db2-related datasource definition, any bean property supported by the `DB2XADataSource` class can be specified within the `connectionProperties` property group. For a discussion of the specific properties that can be used to configure a `DB2XADataSource` instance, see the [Db2 Knowledge Center](https://www.ibm.com/support/knowledgecenter/SSEPGG_11.1.0/com.ibm.db2.luw.apdv.java.doc/src/tpc/imjcc_rjvdsprp.html).

For a Derby-related datasource definition, any bean property supported by the `EmbeddedXADataSource` class can be specified within the `connectionProperties` property group. For more information about the properties supported by the `EmbeddedXADataSource` class, and its super classes, see the [Apache Derby documentation](https://db.apache.org/derby/docs/10.13/publishedapi/org/apache/derby/jdbc/EmbeddedXADataSource.html).


# 4 Customization options
You can modify the default server implementation by taking advantage of the FHIR server's extensibility. The following extension points are available:
 * Virtual resource types: FHIR REST API consumers can define custom resource types and treat them like standard FHIR resource types. The FHIR server does the work between incoming and outgoing requests so that custom resources are indistinguishable from specification-defined ones.
 * Notification service: Logging and auditing options available with Websockets, Apache Kafka
 * Persistence interceptors: Customers can specify code to be called before or after persistence operations, to enforce custom governance rules when a resource operation occurs.
 * Resource validation: Supports validation of FHIR resources on creation or update with spec-defined ISO schematron rules out of box, but also supports user-defined schematrons. A 'validate' operation is also bundled into the FHIR REST API layer to verify resources before ingestion.
 * Custom operations framework: The IBM FHIR Server implementation defines an extended operations framework that standardizes the inputs and outputs and the URI patterns for extensions to the standard REST API. By using the custom operations framework, developers can  extend the capabilities of the FHIR server by developing adapters, wrappers, or connectors to other services.

## 4.1 Virtual resource types

### 4.1.1 Introduction
The FHIR specification recommends the use of FHIR extension elements on existing resource types as a mechanism for providing data elements that do not map to existing resource types. The specification also discourages the development of custom resource types. The alternative is to use the Basic resource type along with extension elements. The Basic resource type has a few standard fields (`id`,`meta`,`subject`,`author`,and `created`) but its semantics are given based on the extension elements that are added. The trouble with using Basic resource types with extensions, rather than defining a custom resource type, is that the syntax is cumbersome and error prone. To address the difficulties related to supporting new data elements, the FHIR server enables you to define virtual resource types.

A virtual resource type is a JSON structure that is structured just like a standard FHIR resource type. Before the FHIR server  processes a create or update operation that involves a virtual resource, the server automatically converts the JSON structure into a valid FHIR Basic-with-extensions structure. For read operations, such as read, vread, history, and search, the FHIR server does the opposite, converting Basic-with-extensions structures back to virtual resource structures.

For example, the following JSON defines a virtual resource type called `WeatherDetail`:

```
{
  "resourceType": "WeatherDetail",
  "geolocation": {
    "latitude": 35.732652,
    "longitude": -78.850286
  },
  "references": {
    "reference": "Patient/1234"
  },
  "measurements": [
    {
      "type": "humidity",
      "value": 35
    },
    {
      "type": "chanceOfRain",
      "value": 0
    }
  ],
  "description": "70 degrees farenheit and sunny"
}
```

The FHIR server converts the preceding `WeatherDetail` virtual resource into a Basic resource with extensions, as represented by the following JSON:
```
{
  "resourceType": "Basic",
  "extension": [
    {
      "url": "http://ibm.com/fhir/extension/geolocation",
      "extension": [
        {
          "url": "http://ibm.com/fhir/extension/latitude",
          "valueDecimal": 35.732652
        },
        {
          "url": "http://ibm.com/fhir/extension/longitude",
          "valueDecimal": -78.850286
        }
      ]
    },
    {
      "url": "http://ibm.com/fhir/extension/references",
      "valueReference": {
        "reference": "Patient/1234"
      }
    },
    {
      "url": "http://ibm.com/fhir/extension/measurements",
      "extension": [
        {
          "url": "_item",
          "extension": [
            {
              "url": "http://ibm.com/fhir/extension/type",
              "valueString": "humidity"
            },
            {
              "url": "http://ibm.com/fhir/extension/value",
              "valueInteger": 35
            }
          ]
        },
        {
          "url": "_item",
          "extension": [
            {
              "url": "http://ibm.com/fhir/extension/type",
              "valueString": "chanceOfRain"
            },
            {
              "url": "http://ibm.com/fhir/extension/value",
              "valueInteger": 0
            }
          ]
        }
      ]
    },
    {
      "url": "http://ibm.com/fhir/extension/description",
      "valueString": "70 degrees farenheit and sunny"
    }
  ],
  "code": {
    "coding": [
      {
        "system": "http://ibm.com/fhir/basic-resource-type",
        "code": "WeatherDetail"
      }
    ]
  }
}
```

A `WeatherDetail` resource can be retrieved as a virtual resource or a Basic with extensions resource, depending on the URI pattern that is specified in the request URL:

`<base>/WeatherDetail/<id>` for virtual resource

`<base>/Basic/<id>` for Basic with extensions

Virtual resource types that are used in the preceding fashion can use any of the following FHIR data types:
*	String
*	Boolean
*	Decimal
*	Integer
*	Uri
*	CodeableConcept
*	Quantity
*	Reference

Virtual resource types support arbitrary nesting and the use of the preceding data types in JSON arrays and objects.

### 4.1.2 Virtual resource type configuration
You configure Virtual Resource Types through properties in the `fhir-server-config.json` file, as in the following example:

```
{
    "fhirServer":{
        ...
        "virtualResources":{
            "enabled":true,
            "allowableResourceTypes":[
                "WeatherDetail"
            ]
        },
        …
    }
}
```

The `fhirServer/virtualResources/enabled` property enables or disables the feature. The default value is `false`.

The `fhirServer/virtualResources/allowableResourceTypes` property specifies the names of the allowable virtual resource types. The value of this property is a JSON array of strings. To allow any virtual resource type to be used, specify the value `[“*”]` (that is, an array with the single string `“*”`)

Requests to perform FHIR operations on virtual resource types that are not configured through one of the preceding methods result in an error returned to the client.

### 4.1.3 Using structure definitions with virtual resource types
You can use virtual resource types without pre-defining their structure to the FHIR server, but that usage pattern restricts you to the subset of allowable datatypes mentioned in [Section 4.1.1 Introduction](#411-introduction). This prevents you from using more complex structures, because the FHIR server has to guess at your intent when it encounters a virtual resource type.

A `StructureDefinition` can be used to fully describe the structure and datatypes that are associated with a virtual resource type. Virtual resource types for which you pre-define a structure can use any of the standard FHIR datatypes, can have more complex structures among the range of FHIR data types that are used within the virtual resource type, and can support basic validation.

The following example show how you might define the structure for the `WeatherDetail` virtual resource type:

```
<StructureDefinition xmlns="http://hl7.org/fhir" xmlns:xhtml="http://www.w3.org/1999/xhtml">
    <id value="WeatherDetail"/>
    <url value="http://hl7.org/fhir/StructureDefinition/WeatherDetail"/>
    <name value="WeatherDetail"/>
    <status value="draft"/>
    <kind value="resource"/>
    <abstract value="false"/>
    <base value="Basic"/>
    <differential>
        <element>
            <path value="WeatherDetail"/>
            <min value="0"/>
            <max value="*"/>
            <type>
                <code value="Basic"/>
            </type>
        </element>
        <element>
            <path value="WeatherDetail.geolocation"/>
            <min value="0"/>
            <max value="1"/>
            <type>
                <code value="BackboneElement"/>
            </type>
        </element>
        <element>
            <path value="WeatherDetail.geolocation.latitude"/>
            <min value="1"/>
            <max value="1"/>
            <type>
                <code value="decimal"/>
            </type>
        </element>
        <element>
            <path value="WeatherDetail.geolocation.longitude"/>
            <min value="1"/>
            <max value="1"/>
            <type>
                <code value="decimal"/>
            </type>
        </element>
        <element>
            <path value="WeatherDetail.references"/>
            <min value="1"/>
            <max value="1"/>
            <type>
                <code value="Reference"/>
            </type>
        </element>
        <element>
            <path value="WeatherDetail.measurements"/>
            <min value="0"/>
            <max value="*"/>
            <type>
                <code value="BackboneElement"/>
            </type>
        </element>
        <element>
            <path value="WeatherDetail.measurements.type"/>
            <min value="1"/>
            <max value="1"/>
            <type>
                <code value="string"/>
            </type>
        </element>
        <element>
            <path value="WeatherDetail.measurements.value"/>
            <min value="1"/>
            <max value="1"/>
            <type>
                <code value="decimal"/>
            </type>
        </element>
        <element>
            <path value="WeatherDetail.description"/>
            <min value="0"/>
            <max value="1"/>
            <type>
                <code value="string"/>
            </type>
        </element>
    </differential>
</StructureDefinition>
```

Note that this `StructureDefinition` refers to `Basic` as the base and that only the `differential` is specified. Structure definitions must be defined as if they were standard resource types and the preceding example contains all of the elements required for a valid definition. Structure definitions for virtual resource types must be added to the `resource` element in the Bundle entry of the `$⁠{server.config.dir}/config/<tenant-id>/profiles-virtual-resources.xml` file, as shown in the following example:

```
<Bundle
    xmlns="http://hl7.org/fhir">
    <id value="virtualResourceProfiles" />
    <type value="collection" />
    <!-- insert structure definition bundle entries here (one bundle entry for each structure definition) -->
    <entry>
        <fullUrl value="http://hl7.org/fhir/StructureDefinition/WeatherDetail" />
        <resource>
            <!-- StructureDefinition element goes here -->
        </resource>
    </entry>
</Bundle>
```

As compared with a virtual resource type for which no pre-defined structure is defined, using structure definitions with virtual resource types increases the number of available datatypes from 8 to 34 (that is the full range of value types supported by a FHIR extension element). Specifying structure definitions also allows us to apply basic minimum and maximum cardinality constraints and type checking.

## 4.2 Notification Service
The FHIR server provides a notification service that publishes notifications about persistence events, specifically _create_ and _update_ operations. The notification service can be used by other Healthcare components to trigger specific actions that need to occur as resources are being updated in the FHIR server datastore.

The notification service supports two implementations: WebSocket and Kafka.

### 4.2.1 FHIRNotificationEvent
The `FHIRNotificationEvent` class defines the information that is published by the notification service. Each notification event published to the WebSocket or Kafka topic is an instance of `FHIRNotificationEvent`, serialized as a JSON object. This JSON object will have the following fields:

Field name     | Type   | Description
|--------------| -----  | ------------|
`operationType`| String | The operation associated with the notification event. Valid values are _create_ and _update_.
`location`	   | String	| The location URI of the resource associated with the notification event. To retrieve this resource, invoke a GET request using the location URI value as the URL string.
`lastUpdated`	   | String |	The date and time of the last update made to the resource associated with the notification event.
`resourceId`     | String	| The logical id of the resource associated with the notification event.
`resource`       | String	| A stringified JSON object which is the resource associated with the notification event.

The following JSON is an example of a serialized notification event:
```
{
  "lastUpdated":"2016-06-01T10:36:23.232-05:00",
  "location":"Observation/3859/_history/1",
  "operationType":"create",
  "resourceId":"3859",
  "resource":{ ...<contents of resource>... }
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

The WebSocket location URI is `ws://<host>:<port>/fhir-server/api/v1/notification`, where `<host>` and `<port>` represent the host and port of the FHIR server's REST API endpoint. So for example, if the FHIR server endpoint's base URL is `https://localhost:9080/fhir-server/api/v1` then the corresponding location of the WebSocket would be `ws://localhost:9080/fhir-server/api/v1/notification`.

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
                    "ssl.truststore.password":"{xor}change-me=",
                    "ssl.keystore.location":"resources/security/kafka.client.keystore.jks",
                    "ssl.keystore.password":"{xor}change-me=",
                    "ssl.key.password":"{xor}change-me=",
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
                     “Patient”
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
A persistence interceptor implementation must implement the following `com.ibm.fhir.persistence.interceptor.FHIRPersistenceInterceptor`
interface:

```
package com.ibm.fhir.persistence.interceptor;

/**
 * This interface describes a persistence interceptor.
 * Persistence interceptors are invoked by the FHIR server to allow
 * users to inject business logic into the REST API processing flow.
 * To make use of this interceptor, develop a class that implements this interface,
 * then store your implementation class name in a file called
 * META-INF/services/com.ibm.fhir.persistence.FHIRPersistenceInterceptor within
 * your jar file.
 */
public interface FHIRPersistenceInterceptor {

    /**
     * This method is called during the processing of a _create_ REST API invocation,
     * immediately before the new resource is stored by the persistence layer.
     * @param event information about the _create_ event
     * @throws FHIRPersistenceInterceptorException
     */
    void beforeCreate(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException;

    /**
     * This method is called during the processing of a _create_ REST API invocation,
     * immediately after the new resource has been stored by the persistence layer.
     * @param event information about the _create_ event
     * @throws FHIRPersistenceInterceptorException
     */
    void afterCreate(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException;

    /**
     * This method is called during the processing of an _update_ REST API invocation,
     * immediately before the updated resource is stored by the persistence layer.
     * @param event information about the _update_ event
     * @throws FHIRPersistenceInterceptorException
     */
    void beforeUpdate(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException;

    /**
     * This method is called during the processing of an _update_ REST API invocation,
     * immediately after the updated resource has been stored by the persistence layer.
     * @param event information about the _update_ event
     * @throws FHIRPersistenceInterceptorException
     */
    void afterUpdate(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException;
}
```

Each interceptor method receives a parameter of type `FHIRPersistenceEvent`, which contains context information related to the request being processed at the time that the interceptor method is invoked. It includes the FHIR resource, security information, request URI information, and the collection of HTTP headers associated with the request.

There are two primary use-cases for persistence interceptors:

1.	Enforce certain application-specific governance rules, such as making sure that a patient has signed a consent form prior to allowing his/her data to be stored in the FHIR server's datastore. In this case, the `beforeCreate` or `beforeUpdate` interceptor methods could verify that the patient has a consent agreement on file, and if not then throw a `FHIRPersistenceInterceptorException` to prevent the _create_ or _update_ persistence events from completing normally. The exception thrown by the interceptor method will be propagated back to the FHIR server request processing flow and would result in an `OperationOutcome` being returned in the REST API response, along with a `Bad Request` HTTP status code.

2.	Perform some additional processing steps associated with a _create_ or _update_ persistence event, such as additional audit logging. In this case, the `afterCreate` and `afterUpdate` interceptor methods could add records to an audit log to indicate the request URI that was invoked, the user associated with the invocation request, and so forth.

In general, the `beforeCreate` and `beforeUpdate` interceptor methods would be useful to perform an enforcement-type action where you would potentially want to prevent the request processing flow from finishing. Conversely, the `afterCreate` and `afterUpdate` interceptor methods would be useful in situations where you need to perform additional steps after the _create_ or _update_ persistence events have been performed.

### 4.3.2 Implementing a persistence interceptor
To implement a persistence interceptor, complete the following steps:

1.	Develop a Java class which implements the `FHIRPersistenceInterceptor` interface.
2.	Store the fully-qualified classname of your interceptor implementation class in a file called :

      `META-INF/services/com.ibm.fhir.persistence.interceptor.FHIRPersistenceInterceptor`

    Here's an example of the file contents:

	`com.ibm.mysolution.MyInterceptor`

3.	Copy your jar to the `<WLP_HOME>/usr/servers/fhir-server/config` directory so that it is accessible to the FHIR server via the classpath (the `server.xml` file contains a library element that defines this directory as a shared library).

4.	Re-start the FHIR server.

## 4.4 Resource validation

### 4.4.1 HL7 spec-defined validation support
The FHIR specification provides a number of different validation resources including:
1.	XML Schemas
2.	ISO XML Schematron rules
3.	Structure Definitions / Profiles for standard resource types, data types and built-in value sets

The FHIR server validates incoming resources for create and update interactions using numbers 1 and 2 in the preceding list. Additionally, the WHC FHIR server provides the following `validate` interaction that API consumers can use to POST resources and get validation feedback:
 `POST <base>/Resource/$validate`

### 4.4.2 User-defined validation support
In addition to the standard validations that are performed on resources, users can provide additional schematron rules that will get applied to incoming resources as well. For example, let's say that we have the following Patient resource:
```
{
    "resourceType" : "Patient",
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

This resources would be validated using the XML schemas and ISO schematron rules. If we omit a value for the given name, as in the following example:
```
{
    "resourceType" : "Patient",
    "name" : [ {
        "family" : [ "Doe" ],
        "given" : [ "" ]
    } ],
    "telecom" : [ {
        "value" : "555-1234",
        "system": "phone",
        "use" : "home"
    } ],
    "birthDate" : "1950-08-15"
}
```
the validation operation returns the following response to indicate that the 'given' name field cannot be empty:
```
{
  "resourceType": "OperationOutcome",
  "id": "validationfail",
  "text": {
    "status": "generated"
  },
  "issue": [
    {
      "severity": "error",
      "code": "invalid",
      "diagnostics": "cvc-minLength-valid: Value '' with length = '0' is not facet-valid with respect to minLength '1' for type 'string-primitive'.",
      "location": [
        "/f:Patient/f:name/f:given"
      ]
    },
    {
      "severity": "error",
      "code": "invalid",
      "diagnostics": "cvc-attribute.3: The value '' of attribute 'value' on element 'given' is not valid with respect to its type, 'string-primitive'.",
      "location": [
        "/f:Patient/f:name/f:given"
      ]
    }
  ]
}
```

Now, let's say that we want to add an extension to this `Patient` resource as in the following example:

```
{
    "resourceType" : "Patient",
    "name" : [ {
        "family" : [ "Doe" ],
        "given" : [ "" ]
    } ],
    "telecom" : [ {
        "value" : "555-1234",
        "system": "phone",
        "use" : "home"
    } ],
    "birthDate" : "1950-08-15",
    "extension": [{
        "url": "http://ibm.com/fhir/extension/partner/study_ID",
        "valueString": "abc-1234"
    }]
}
```

We could validate this extension by providing the following schematron rule:

```
<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">
  <sch:ns prefix="f" uri="http://hl7.org/fhir"/>
  <sch:ns prefix="h" uri="http://www.w3.org/1999/xhtml"/>
  <sch:pattern>
    <sch:rule context="//f:Patient">
      <sch:assert test="exists(f:extension[@url='http://ibm.com/fhir/extension/partner/study_ID'])">partner-1: Patient must have a study ID specified.</sch:assert>
    </sch:rule>
  </sch:pattern>
</sch:schema>
```

This validation rule runs after the XML schema and ISO schematron rules that are defined in the FHIR specification are applied. In validating this extension of the `Patient` resource, if no `study_ID` extension element is specified, the following error message will be returned to the client for the validate operation:

```
{
  "resourceType": "OperationOutcome",
  "id": "validationfail",
  "text": {
    "status": "generated"
  },
  "issue": [
    {
      "severity": "error",
      "code": "invalid",
      "diagnostics": "partner-1: Patient must have a study ID specified.",
      "location": [
        "/*:Patient[namespace-uri()='http://hl7.org/fhir'][1]"
      ]
    }
  ]
}
```

### 4.4.3 User-Defined validation configuration
All user-defined schematron rules must use the following naming format:
     `<lowercase resource type name>.sch` (for example. `patient.sch`).

User-defined schematron rules must be stored in the following location:
     `${server.config.dir}/config/<tenant-id>/schematron`

Provide only one file per resource type per tenant. That is, include all patient rules for a tenant in one `patient.sch` file within the tenant's `schematron` directory. Enable or disable the use of user-defined schematrons by setting the `fhirServer/core/userDefinedSchematronEnabled` property in the `fhir-server-config.json` file, as in the following example:

```
{
    "fhirServer":{
        "core":{
            "userDefinedSchematronEnabled":true
        }
        …
    }
}
```

The deployer can add new schematron rules files or update existing ones at any time and the FHIR server will “see” the new or updated files the next time it needs validation rules associated with a particular tenant. Any requests associated with that tenant that are received after the updates are made will be processed using the new or updated validation rules, without requiring a server re-start.

### 4.4.4 Using profiles
You might want to organize your user-defined schematron rules so that they only fire if a reference to a specific profile is present in the resource. For example, we could modify our original patient resource as follows:
```

{
    "resourceType" : "Patient",
    "meta": {
        "profile": [ "http://ibm.com/fhir/profile/partner" ]
    },
    "name" : [ {
        "family" : [ "Doe" ],
        "given" : [ "" ]
    } ],
    "telecom" : [ {
        "value" : "555-1234",
        "system": "phone",
        "use" : "home"
    } ],
    "birthDate" : "1950-08-15"
}
```

and then we could modify the user-defined schematron rule as follows:

```
<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">
  <sch:ns prefix="f" uri="http://hl7.org/fhir"/>
  <sch:ns prefix="h" uri="http://www.w3.org/1999/xhtml"/>
  <sch:pattern>
    <sch:rule context="//f:Patient[./f:meta/f:profile/@value='http://ibm.com/fhir/profile/partner']">
      <sch:assert test="exists(f:extension[@url='http://ibm.com/fhir/extension/partner/study_ID'])">partner-1: Patient must have a study ID specified.</sch:assert>
    </sch:rule>
  </sch:pattern>
</sch:schema>
```

By doing this, we have a much more flexible user-defined validation mechanism that allows us to apply rules only when 'http://ibm.com/fhir/profile/partner' is present in the instance.

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
Along with the FHIR server, we also offer a high-level Java API that can be used to invoke the FHIR REST APIs. The FHIR Client API is based on the JAX-RS 2.0 standard and provides several capabilities that make it an attractive client API for use with the FHIR server:
*	Easily configured via properties.
*	Supports the use of an SSL transport (HTTPS:) along with the ability to configure a client truststore file.
*	Can be configured to perform client certificate-based authentication as an alternative to basic authentication.
*	Supports encryption of REST API payloads, working in concert with the FHIR server.

### 4.6.2 Maven coordinates
The FHIR Client API can be found on the WHC Nexus artifact server. In order to use the FHIR Client API within your own application, you'll need to specify the `fhir-client` artifact as a dependency within your `pom.xml` file, as in the following example:

```
        <dependency>
            <groupId>com.ibm.fhir</groupId>
            <artifactId>fhir-client</artifactId>
            <version>${fhir.server.version}</version>
        </dependency>
```

where `${fhir.server.version}` is one of:
*	`99-SNAPSHOT` (associated with the 'master' branch)
*	`1.1.0-SNAPSHOT` (associated with the 'release-1.1.x' branch)
*	`1.0.0-SNAPSHOT` (associated with the 'release-1.0.x' branch)

### 4.6.3 Sample
Within the master branch of the FHIR Git repository, you can find the “fhir-client-sample” project which provides a stand-alone sample application that uses the FHIR client API.

## 4.7 FHIR command-line interface (fhir-cli)
The FHIR command-line interface (fhir-cli for short) is a command that can be used to invoke FHIR REST API operations from the command line. The compressed file for installing the fhir-cli tool zip is part of the FHIR server installation in `${WLP_HOME}/fhir/client/fhir-cli.zip`, and the `fhir-cli.zip` file is also available from [our Artifactory server](
https://na.artifactory.swg-devops.com/artifactory/webapp/#/artifacts/browse/simple/General/wh-fhir-server-releases-maven-local/com/ibm/fhir/fhir-cli/).

### 4.7.1 Installing fhir-cli
Because the fhir-cli tool is intended to be used by clients that need to access the FHIR server, it has its own installation process separate from the server. To install the fhir-cli tool, complete the following steps:

1.	Obtain the `fhir-cli.zip` file from the FHIR server installation zip or Artifactory.
2.	Decompress the `fhir-cli.zip` file into a directory of your choosing, for example:
```
cd /mydir
unzip fhir-cli.zip
```

3.	[Optional] To enable you to run fhir-cli from multiple directories, run the following command to add `fhir-cli` to your `PATH` environment variable.
```
export PATH=$PATH:/mydir/fhir-cli
```

### 4.7.2 Configuring fhir-cli
The fhir-cli tool requires a properties file containing various configuration properties, such as the base endpoint URL, the username and password for basic authentication, amd so forth. The properties contained in this file are the same properties supported by the FHIR client API. The fhir-cli tool comes with a sample properties file named `fhir-cli.properties` which contains a collection of default property settings.

Using the sample properties file as a guide, you can create your own properties file to reflect the required endpoint configuration associated with the FHIR server endpoint that you would like to access. In the examples that follow, we'll refer to this file as `my-fhir-cli.properties`, although you can name the file anything you'd like.

### 4.7.3 Running fhir-cli
The fhir-cli tool comes with two shell scripts: `fhir-cli` (Linux&reg;) and `fhir-cli.bat` (Windows&trade;). In the examples that follow, we'll use `<fhir-cli-home>` as the location of the fhir-cli tool (that is, the `/mydir/fhir-cli` directory mentioned in preceding section).

The following examples illustrate how to invoke the fhir-cli tool:

*	Display help text
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
*	Invoke the 'metadata' operation
```
$ <fhir-cli-home>/fhir-cli --properties my-fhir-cli.properties --operation metadata --output conformance.json

FHIR Client Command Line Interface (fhir-cli)    (c) Copyright IBM Corporation, 2016.

Invoking operation 'metadata'... done! (651ms)
Status code: 200
Response resource written to file: conformance.json
```
Note: in this example the “--output” option is used to specify that the Conformance resource should be saved in file 'conformance.json'.
*	Perform a _create_ operation
```
$ <fhir-cli-home>/fhir-cli --properties my-fhir-cli.properties --operation create --resource newpatient.json

FHIR Client Command Line Interface (fhir-cli)    (c) Copyright IBM Corporation, 2016.

Invoking operation _create_... done! (2719ms)
Status code: 201
Location URI: http://localhost:9080/fhir-server/api/v1/Patient/26b694ef-cea7-4485-a896-5ac2a1da9f64/_history/1
ETag: W/"1"
Last modified: 2016-09-13T20:51:21.048Z
```
Note: In this example, the “--resource” option is used to indicate that the contents of the new resource to be created should be read from file 'newpatient.json'.
*	Perform a 'read' operation
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
*	Perform an _update_ operation
```
$ <fhir-cli-home>/fhir-cli --properties my-fhir-cli.properties --operation update --resource updatedpatient1.json
FHIR Client Command Line Interface (fhir-cli)    (c) Copyright IBM Corporation, 2016.

Invoking operation _update_... done! (2707ms)
Status code: 200
Location URI: http://localhost:9080/fhir-server/api/v1/Patient/26b694ef-cea7-4485-a896-5ac2a1da9f64/_history/2
ETag: W/"2"
Last modified: 2016-09-13T21:11:48.988Z
```
*	Perform a 'vread' operation
```
$ <fhir-cli-home>/fhir-cli --properties my-fhir-cli.properties --operation vread -t Patient -id 26b694ef-cea7-4485-a896-5ac2a1da9f64 -vid 3 -o patient1v3.json

FHIR Client Command Line Interface (fhir-cli)    (c) Copyright IBM Corporation, 2016.

Invoking operation 'vread'... done! (290ms)
Status code: 200
ETag: W/"3"
Last modified: 2016-09-13T21:18:28.412Z
Response resource written to file: patient1v3.json
```
*	Perform a 'history' operation
```
$ <fhir-cli-home>/fhir-cli --properties my-fhir-cli.properties --operation history -t Patient -id 26b694ef-cea7-4485-a896-5ac2a1da9f64 -o patient1history.json

FHIR Client Command Line Interface (fhir-cli)    (c) Copyright IBM Corporation, 2016.

Invoking operation 'history'... done! (414ms)
Status code: 200
Response resource written to file: patient1history.json
```
Note: in this example, the response resource is a Bundle containing the versions of the Patient resource.


*	Perform a 'search' operation

```
$ <fhir-cli-home>/fhir-cli --properties my-fhir-cli.properties --operation search -t Patient -qp name=Doe -o searchresults.json

FHIR Client Command Line Interface (fhir-cli)    (c) Copyright IBM Corporation, 2016.

Invoking operation 'search'... done! (543ms)
Status code: 200
Response resource written to file: searchresults.json
```

Note: in this example the `-qp` option (shortcut for `–queryParameter`) is used to specify the search criteria (that is, `name=Doe`). The response resource is a Bundle that is written to the file `searchresults.json`.

*	Perform a 'validate' operation

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
Inter-dependencies between resources are typically defined by one resource containing a field of type `Reference` which contains an _external reference_<sup id="a5">[5](#f5)</sup> to another resource. For example, an `Observation` resource could reference a `Patient` resource via the Observation's `subject` field. The value that is stored in the `Reference-type` field (for example, `subject` in the case of the `Observation` resource) could be an absolute URL, such as `https://fhirserver1:9443/fhir-server/api/v1/Patient/12345`, or a relative URL (for example, `“Patient/12345”`).

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
            ...
        },
        "request" : {
            "method" : "POST",
            "url" : "Patient"
        }
    }, {
        "resource" : {
            "resourceType" : "Observation",
            ...
            "subject" : {
                "reference" : "urn:uuid:7113a0bb-d9e0-49df-9855-887409388c69"
            },
            ...
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
There is really only one main rule related to the use of local references within a request bundle:

A local identifier must be defined via a request entry's `fullUrl` field before that local identifier can be used in a local reference.

In the example in [Section 4.9.0.1](#4901-example-1-observation-references-patient-via-local-reference), you can see that there are two POST requests and the `Patient` request entry appears in the bundle before the `Observation` request entry, so that example satisfies the rule because the FHIR server will process the POST request entries in the order in which they appear within the request bundle.

If, however, those entries were reversed, the FHIR server returns an error when processing the `Observation` request entry, because the `Patient` local identifier is not defined yet.

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
		...
		"subject" : {
                    "reference" : "urn:Patient_1"
		},
		...
        },
        "request" : {
            "method" : "PUT",
            "url" : "Observation/25b1fe08-7612-45eb-af80-7e15d9806b2b"
        }
    }, {
        "fullUrl" : "urn:Patient_1",
        "resource" : {
            "resourceType" : "Patient",
		...
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

For example, in Example 1 from [Section 4.9.0.1](#4901-example-1-observation-references-patient-via-local-reference), the FHIR server detects the use of the local identifier in the `Patient` request entry (`urn:uuid:7113a0bb-d9e0-49df-9855-887409388c69`) and -- after creating the new `Patient` resource -- establishes a mapping between the local identifier and the resulting external reference associated with the new `Patient` (for example, `Patient/1cc5d299-d2be-4f93-8745-a121232ffe5b`).

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
            ...
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
	    ...
            "subject" : {
                "reference" : "Patient/1cc5d299-d2be-4f93-8745-a121232ffe5b"
            },
	    ...
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
The FHIR server allows a deployer to configure a subset of the supported configuration properties on a tenant-specific basis. For example, one tenant might want to enable virtual resources, but another tenant might want to explicitly disable it for security reasons.

For example, the following configuration properties can be specified on a tenant-specific basis:
*	`fhirServer/core/userDefinedSchematronEnabled` – This property allows each tenant to specify whether or not user-defined schematron rules for resource validation are enabled; if this feature is enabled, the FHIR server will load the tenant's user-defined schematron rules from the `$⁠{server.config.dir}/config/<tenant-id>/schematron` directory.
*	`fhirServer/virtualResources/enabled`,`fhirServer/virtualResources/allowableResourceTypes` – These properties allow each tenant to specify whether or not virtual resources are enabled and if so, the names of the allowable virtual resource types.

For a complete list of configuration properties supported on a per-tenant basis, see [Section 5.1.3 Property attributes](#513-property-attributes).

When the FHIR server needs to retrieve any of the tenant-specific configuration properties, it does so dynamically each time the property value is needed. This means that a deployer can change the value of a tenant-specific property within a tenant's configuration file on disk, and the FHIR server will immediately “see” the new value the next time it tries to retrieve it. For example, suppose the deployer initially defines the `acme` tenant's `fhir-server-config.json` file such that the `fhirServer/virtualResources/enabled` property is set to false.

    An incoming REST API request specifying the `acme` tenant would not be allowed to create an instance of a virtual resource type, and would result in a `400 “Bad Request”` response. Now suppose the deployer then changes the value of that property to true within the `acme` tenant's `fhir-server-config.json` file. A subsequent REST API request attempting to create an instance of a virtual resource type would then succeed since the FHIR server now sees the value of that property as true.

#### 4.9.2.1 Examples
This section contains an example of the FHIR server's global configuration, along with two tenant-specific configurations. The global configuration contains non-tenant specific configuration parameters (that is, configuration parameters that are not resolved or used on a tenant-specific basis), as well as default values for tenant-specific configuration parameters.

##### Global configuration (default)
`${server.config.dir}/config/default/fhir-server-config.json`
```
{
    "__comment":"FHIR server global (default) configuration",
    "fhirServer":{
        "core":{
            "userDefinedSchematronEnabled":false,
            "tenantIdHeaderName":"X-FHIR-TENANT-ID"
        },
        "virtualResources":{
            "enabled":false,
            "allowableResourceTypes":[
                "*"
            ]
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
        "audit":{
            "logPath":"logs/",
            "logMaxSize": 20
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
`${server.config.dir}/config/acme/fhir-server-config.json`
```
{
    "__comment":"FHIR server configuration for tenant: Acme Healthcare, Inc.",
    "fhirServer":{
        "core":{
            "userDefinedSchematronEnabled":true
        },
       
    }
}
```

##### Quality Pharmaceuticals, Inc. (qpharma)
`${server.config.dir}/config/qpharma/fhir-server-config.json`
```
{
    "__comment":"FHIR server configuration for tenant: Quality Pharmaceuticals, Inc.",
    "fhirServer":{
        
    }
}
```

In the preceding examples, you can see that in the global configuration, the user-defined validation feature has both been disabled by default.

For the `Quality Pharmaceuticals, Inc.` tenant, the user-defined validation feature is disabled, and the virtual resources feature is enabled (with any virtual resource type allowed). Note that because the user-defined validation feature is disabled by default within the global configuration, we didn't need to explicitly set that configuration parameter in the `Quality Pharmaceuticals` configuration file.

## 4.10 Extended operations
In addition to the standard REST API (create, update, search, and so forth), the IBM FHIR Server supports the FHIR operations framework as described in the [FHIR specification]( https://www.hl7.org/fhir/r4/operations.html).

### 4.10.1 Packaged operations
The FHIR team provides implementations for the standard `$validate` and `$document` operations, as well as a custom operation named `$healthcheck`, which queries the configured persistence layer to report its health.

No other extended operations are packaged with the server at this time, but you can extend the server with your own operations.

#### 4.10.1.1 $validate
The `$validate` operation checks whether the attached content would be acceptable either generally, or as a create, update, or delete against an existing resource instance or type.

https://www.hl7.org/fhir/r4/resource-operations.html#validate

#### 4.10.1.2 $document
The `$document` operation generates a fully bundled document from a composition resource.

https://www.hl7.org/fhir/r4/composition-operations.html#document

#### 4.10.1.3 $healthcheck
The `$healthcheck` operation returns the health of the FHIR server and its datastore. In the default JDBC persistence layer, this operation creates a connection to the configured database and return its status. The operations returns `200 OK` when healthy. Otherwise, it returns an HTTP error code and an `OperationOutcome` with one or more issues.

### 4.10.2 Custom operations
In addition to the provided operations, the FHIR server supports user-provided custom operations through a Java Service Provider Interface (SPI).

To contribute an operation:

1. Implement each operation as a Java class that extends `com.ibm.fhir.operation.AbstractOperation` from `fhir-operation.jar`. Ensure that your implementation returns an appropriate `OperationDefinition` in its `getDefinition()` method, because the framework validates both the request and response payloads to ensure that they conform to the definition.
2. Create a file named `com.ibm.fhir.operation.FHIROperation` with one or more fully qualified `FHIROperation` classnames and package it in your jar under `META-INF/services/`.
3. Include your jar file under the `<WLP_HOME>/wlp/usr/servers/fhir-server/userlib/` directory of your installation.
4. Restart the FHIR server. Changes to custom operations require a server restart, because the server discovers and instantiates operations during server startup only.

After you register your operation with the server, it is available via HTTP POST at `[base]/api/1/$<yourCode>`, where `<yourCode>` is the value of your OperationDefinition's [code](https://www.hl7.org/fhir/r4/operationdefinition-definitions.html#OperationDefinition.code).

## 4.11 CADF audit logging service 
The CADF audit logging service pushs FHIR server audit events for FHIR operations in [Cloud Auditing Data Federation (CADF)]( https://www.dmtf.org/standards/cadf) standard format to IBM Cloud Event Streams service, these FHIR operations include create, read, update, delete, version read, history, search, validate, custom operation, meta and bundle, these operations are mapped to CADF actions as following: 

| FHIR Operation                 | CADF Action   |
|--------------------------------| --------------|
|`read,versionread,history,search,validate,meta` |    read       |
|`create`                        |    create     |
|`update`                        |    update     |
|`delete`                        |    delete     |
|`operation,bundle`                        |    unknown     |

Each FHIR create, update, delete, bundle or custom operation triggers 2 CADF events - begins with an event with "pending" outcome and ends with an event with "success" or "failure" outcome; All the other FHIR operations only trigger 1 CADF event with either "success" or "failure" outcome. 

### 4.11.1 Enable CADF audit logging service
Please refer to the properties names started wtih fhirServer/audit/ in [5.1 Configuration properties reference](#51-configuration-properties-reference) for how to enable and configure CADF audit logging service.

### 4.11.2 Event Streams configuation of CADF audit logging service
The CADF audit logging service gets event streams service credential from env variable EVENT_STREAMS_AUDIT_BINDING with values like this: 
```
    {
  "api_key": "xxxxxxxxxxxxxxxx_xxxxx_xxxxxxxxxxxxxxxxxxx",
  "apikey": "xxxxxxxxxxxxxxxx_xxxxx_xxxxxxxxxxxxxxxxxxx",
   ...
  "kafka_brokers_sasl": [
    "broker-1-0server:9093",
    "broker-2-0server:9093",
    "broker-5-0server:9093",
    "broker-4-0server:9093",
    "broker-3-0server:9093",
    "broker-0-0server:9093"
  ],
   ...
}
```
The service credential is generated automatically when you run
```
    ibmcloud ks cluster-service-bind --cluster <cluster_name_or_ID> --namespace <namespace> --service <event_streams_service_instance_name> ...
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
|`fhirServer/core/userDefinedSchematronEnabled`|boolean|A boolean flag which indicates whether or not user-defined validation rules should be used to validate resources contained within _create_ and _update_ requests.|
|`fhirServer/core/defaultPrettyPrint`|boolean|A boolean flag which indicates whether JSON "Pretty Printing" or XML Formatted output should be the container default for responses.|
|`fhirServer/core/tenantIdHeaderName`|string|The name of the request header that will be used to specify the tenant-id for each incoming FHIR REST API request. For headers with semicolon-delimited parts, setting a header name like `<headerName>:<partName>` will select the value from the part of header `<headerName>`'s value with a name of `<partName>` (e.g. setting `X-Test:part1` would select `someValue` from the header `X-Test: part1=someValue;part2=someOtherValue`).|
|`fhirServer/core/dataSourceIdHeaderName`|string|The name of the request header that will be used to specify the datastore-id for each incoming FHIR REST API request. For headers with semicolon-delimited parts, setting a header name like `<headerName>:<partName>` will select the value from the part of header `<headerName>`'s value with a name of `<partName>` (e.g. setting `X-Test:part1` would select `someValue` from the header `X-Test: part1=someValue;part2=someOtherValue`).|
|`fhirServer/core/jsonParserLenient`|boolean|A boolean flag which indicates whether the FHIRJsonParser will be lenient with respect to element cardinality (singleton vs array) and string values for numbers/booleans.|
|`fhirServer/core/jsonParserValidating`|boolean|A boolean flag which indicates whether the FHIRJsonParser will do limited validation during the parse including checking for missing required fields and unrecognized fields.|
|`fhirServer/searchParameterFilter`|property list|A set of inclusion rules for search parameters. See [Section 4.10.3.1 Filtering of search parameters](#41031-filtering-of-search-parameters) for more information.|
|`fhirServer/notifications/common/includeResourceTypes`|string list|A comma-separated list of resource types for which notification event messages should be published.|
|`fhirServer/notifications/websocket/enabled`|boolean|A boolean flag which indicates whether or not websocket notifications are enabled.|
|`fhirServer/notifications/kafka/enabled`|boolean|A boolean flag which indicates whether or not kafka notifications are enabled.|
|`fhirServer/notifications/kafka/topicName`|string|The name of the topic to which kafka notification event messages should be published.|
|`fhirServer/notifications/kafka/connectionProperties`|property list|A group of connection properties used to configure the KafkaProducer. These properties are used as-is when instantiating the KafkaProducer used by the FHIR server for publishing notification event messages.|
|`fhirServer/persistence/factoryClassname`|string|The name of the factory class to use for creating instances of the persistence layer implementation.|
|`fhirServer/persistence/common/updateCreateEnabled`|boolean|A boolean flag which indicates whether or not the 'update/create' feature should be enabled in the selected persistence layer.|
|`fhirServer/persistence/datasources`|map|A map containing datasource definitions. See [Section 3.4.2.3 Datastore configuration reference](#3423-datastore-configuration-reference) for more information.|
|`fhirServer/persistence/jdbc/dataSourceJndiName`|string|The JNDI name of the DataSource to be used by the JDBC persistence layer.|
|`fhirServer/persistence/jdbc/bootstrapDb`|boolean|A boolean flag which indicates whether the JDBC persistence layer should attempt to run the liquibase-based schema creation at server startup time.|
|`fhirServer/persistence/jdbc/schemaType`|string|Indicates the type of schema to be used by the JDBC persistence layer. Valid values are “basic” and “normalized”.|
|`fhirServer/oauth/regUrl`|string|The registration URL associated with the OAuth 2.0 authentication/authorization support.|
|`fhirServer/oauth/authUrl`|string|The authorization URL associated with the OAuth 2.0 authentication/authorization support.|
|`fhirServer/oauth/tokenUrl`|string|The token URL associated with the OAuth 2.0 authentication/authorization support.|
|`fhirServer/audit/serviceClassName`|string|The audit service to use. Currently, com.ibm.fhir.audit.logging.impl.WhcAuditCadfLogService and com.ibm.fhir.audit.logging.impl.DisabledAuditLogService are supported.|
|`fhirServer/audit/serviceProperties/auditTopic`|string|The kafka topic to use for CADF audit logging service|
|`fhirServer/audit/serviceProperties/geoCity`|string|The Geo City configure for CADF audit logging service.|
|`fhirServer/audit/serviceProperties/geoState`|string|The Geo State configure for CADF audit logging service.|
|`fhirServer/audit/serviceProperties/geoCounty`|string|The Geo Country configure for CADF audit logging service.|


### 5.1.2 Default property values
| Property Name                 | Default value   |
|-------------------------------| ----------------|
|`fhirServer/core/userDefinedSchematronEnabled`|false|
|`fhirServer/core/defaultPrettyPrint`|false|
|`fhirServer/core/tenantIdHeaderName`|`X-FHIR-TENANT-ID`|
|`fhirServer/core/dataSourceIdHeaderName`|`X-FHIR-DSID`|
|`fhirServer/core/jsonParserLenient`|false|
|`fhirServer/core/jsonParserValidating`|true|
|`fhirServer/searchParameterFilter`|`"*": [*]`|
|`fhirServer/notifications/common/includeResourceTypes`|["*"]|
|`fhirServer/notifications/websocket/enabled`|false|
|`fhirServer/notifications/kafka/enabled`|false|
|`fhirServer/notifications/kafka/topicName`|`fhirNotifications`|
|`fhirServer/notifications/kafka/connectionProperties`|`{}`|
|`fhirServer/persistence/factoryClassname`|com.ibm.fhir.persistence.jdbc.FHIRPersistenceJDBCFactory|
|`fhirServer/persistence/common/updateCreateEnabled`|true|
|`fhirServer/persistence/datasources`|embedded Derby database: derby/fhirDB|
|`fhirServer/persistence/jdbc/dataSourceJndiName`|jdbc/fhirProxyDataSource|
|`fhirServer/persistence/jdbc/bootstrapDb`|false|
|`fhirServer/persistence/jdbc/schemaType`|“basic”|
|`fhirServer/oauth/regUrl`|""|
|`fhirServer/oauth/authUrl`|""|
|`fhirServer/oauth/tokenUrl`|""|
|`fhirServer/audit/serviceProperties/auditTopic`|FHIR_AUDIT|
|`fhirServer/audit/serviceProperties/geoCity`|Dallas|
|`fhirServer/audit/serviceProperties/geoState`|TX|
|`fhirServer/audit/serviceProperties/geoCounty`|US|


### 5.1.3 Property attributes
| Property Name                 | Tenant-specific? | Dynamic? |
|-------------------------------|------------------|----------|
|`fhirServer/core/userDefinedSchematronEnabled`|Y|Y|
|`fhirServer/core/defaultPrettyPrint`|Y|Y|
|`fhirServer/core/tenantIdHeaderName`|N|Y|
|`fhirServer/core/dataSourceIdHeaderName`|N|N|
|`fhirServer/core/jsonParserLenient`|Y|Y|
|`fhirServer/core/jsonParserValidating`|Y|Y|
|`fhirServer/searchParameterFilter`|Y|Y|
|`fhirServer/notifications/common/includeResourceTypes`|N|N|
|`fhirServer/notifications/websocket/enabled`|Y|Y|
|`fhirServer/notifications/kafka/enabled`|Y|Y|
|`fhirServer/notifications/kafka/topicName`|N|N|
|`fhirServer/notifications/kafka/connectionProperties`|N|N|
|`fhirServer/persistence/factoryClassname`|N|N|
|`fhirServer/persistence/common/updateCreateEnabled`|N|N|
|`fhirServer/persistence/datasources`|Y|N|
|`fhirServer/persistence/jdbc/dataSourceJndiName`|N|N|
|`fhirServer/persistence/jdbc/bootstrapDb`|N|N|
|`fhirServer/persistence/jdbc/schemaType`|N|N|
|`fhirServer/oauth/regUrl`|N|N|
|`fhirServer/oauth/authUrl`|N|N|
|`fhirServer/oauth/tokenUrl`|N|N|
|`fhirServer/audit/serviceClassName`|N|N|
|`fhirServer/audit/serviceProperties/auditTopic`|N|N|
|`fhirServer/audit/serviceProperties/geoCity`|N|N|
|`fhirServer/audit/serviceProperties/geoState`|N|N|
|`fhirServer/audit/serviceProperties/geoCounty`|N|N|


## 5.2 Keystores, truststores, and the FHIR server

### 5.2.1 Background
As stated earlier, the FHIR server is installed with a default configuration in `server.xml` which includes the definition of a keystore (`fhirKeystore.jks`) and a truststore (`fhirTruststore.jks`)<sup id="a7">[7](#f7)</sup>. These files are provided only as examples and while they may suffice in a test environment, the FHIR server deployer should generate a new keystore and truststore for any installations where security is a concern. Review the information in the following topics to learn how to configure a secure keystore and truststore.

### 5.2.2 WebApp security
By default, the FHIR server REST API is available on both HTTP and HTTPS endpoints. In addition, the web application (WAR) that contains the FHIR server's REST API implementation is secured via client certificate-based authentication. As a backup to the client authentication scheme, basic authentication is also supported. If the client chooses to use OAuth 2.0 authentication using a Bearer Token mechanism, this can be done as well and is described in more detail in [Section 5.2.5 Oauth 2.0](#525-oauth-20).

Here are some notes related to these authentication schemes:
*	Basic authentication is a very simple authentication scheme and can be used on both HTTP or HTTPS endpoints, although it is extremely insecure when used with an HTTP endpoint since the user and password information is essentially transmitted in plain text.
*	Client certificate-based authentication can only be used in conjunction with an HTTPS endpoint since it involves SSL handshake negotiations.
*	The main value of client authentication is that the server is able to securely authenticate the client through the use of certificates.
*	OAuth 2.0 authentication can only be used in conjunction with an HTTPS endpoint, because OAuth authorization steps rely on SSL handshake negotiations.

### 5.2.3 Server configuration steps
To properly configure the FHIR server's keystore and truststore files, perform the following steps:

1.	Create a new self-signed server certificate<sup id="a8">[8](#f8)</sup> and store it in a new keystore file located in the `$WLP_HOME/usr/servers/fhir-server/resources/security` directory.

    In these instructions, we'll call this file `serverKeystore.jks`, although you can name your server keystore file anything you choose. We'll use the `keytool`<sup id="a9">[9](#f9)</sup> command for all keystore- and truststore-related steps, although there are several ways to perform these actions.

    The following command will generate a new self-signed certificate and store it in `serverKeystore.jks`:

    ```
    keytool -keystore serverKeystore.jks -storepass password -genkey
        -alias default -keyalg RSA -keypass password
    ```

2.	Export the server certificate so that it can be imported into the client's truststore:

    ```
    keytool -keystore serverKeystore.jks -storepass password -export
        -alias default -file server-public-key.cer
    ```

3.	Create the client's certificate and store it in `clientKeystore.jks`:

    ```
    keytool -keystore clientKeystore.jks -storepass password -genkey
        -alias client-auth -keyalg RSA -keypass password
    ```

    Note: `keytool` will prompt you for the various components of the distinguished name (DN) associated with the certificate (similar to Step 1). The value that you specify for the common name (CN) component of the DN must match a username in the basic user registry<sup id="a10">[10](#f10)</sup> configured within the `server.xml` file. This step is crucial for the client certificate-based authentication to work properly. The whole point of this authentication scheme is for the client to transmit its identity to the server via the client certificate, and the client's username must be contained in that certificate (the CN component of the DN) so that the FHIR server can properly authenticate the client.

4.	Export the client's certificate so that it can be imported into the server's truststore:

    ```
    keytool -keystore clientKeystore.jks -storepass password -export -alias client-auth -file client-public-key.cer
    ```

5.	Import the server's public key certificate into the client's truststore:

    ```
    keytool -keystore clientTruststore.jks -storepass password -import -file server-public-key.cer
    ```

6.	Import the client's public key certificate into the server's truststore:
keytool -keystore serverTruststore.jks -storepass password -import -file client-public-key.cer

7.	Be sure to copy the server keystore (`serverKeystore.jks`) and truststore (`serverTruststore.jks`) files to the appropriate directory (`$WLP_HOME/usr/servers/fhir-server/resources/security`). Then configure the `server.xml` file correctly to reference your new keystore and truststore files.

### 5.2.4 Client configuration
FHIR REST API consumers (clients) must be properly configured to successfully participate in client certificate-based authentication. The previous section provided instructions on how to configure a keystore and truststore on the server. Those instructions also included the steps involving the client keystore and truststore as well. At this point, you should have a client keystore that contains a client certificate whose Distinguished Name's Common Name component is set to the username. You should also have a client truststore which contains the server's public key certificate. Essentially, the server and client both have a keystore that contains their own private and public key certificate and they both have a truststore which contains the public key certificate of their counterpart.

Although the steps required to configure certificate-based authentication for a client application depend on the specific REST API client framework, the client has several main requirements, as summarized in the following list:

*	If the client is using the FHIR server's HTTPS endpoint, then the client truststore should be configured with the REST API client framework<sup id="a11">[11](#f11)</sup>.
*	If the client is using basic authentication, then it must send an appropriate Authorization request header containing the username and password information in the HTTP request.
*	If the client is using client certificate-based Authentication, then the client keystore must be configured with the REST API client framework<sup id="a12">[12](#f12)</sup>.
*	If the client is using OAuth 2.0 Authentication, then the client keystore must be configured with the REST API client framework. In addition, it must send an appropriate Authorization request header containing the Bearer token in the HTTP request.

### 5.2.5 OAuth 2.0
In the default configuration, the FHIR server acts as an authorization server as well as a resource server. The FHIR server's conformance statement includes the OAuth-related URLs you will need to get started. The following excerpt from a conformance statement shows sample OAuth-related URLs (token, authorize, and register) as values of the `valueUri` elements.

```
...
"rest": [
    {
      "mode": "server",
      "security": {
        "extension": [
          {
            "url": "http://<SERVER>",
            "extension": [
              {
                "url": "token",
                "valueUri": "https://<host>:<httpsPort>/oauth2/endpoint/oauth2-provider/token"
              },
              {
                "url": "authorize",
                "valueUri": "https://<host>:<httpsPort>/oauth2/endpoint/oauth2-provider/authorize"
              },
              {
                "url": "register",
                "valueUri": "https://<host>:<httpsPort>/oidc/endpoint/oidc-provider/registration"
              }
            ]
          }
        ],
...
```

First, a client application (web or mobile) must register itself with the Open ID Connect Provider using the following URL:  
`https://<host>:<httpsPort>/oidc/endpoint/oidc-provider/registration`

For more information about client registration, see the [WebSphere Application Server Liberty base documentation](https://www.ibm.com/support/knowledgecenter/SSEQTP_liberty/com.ibm.websphere.wlp.doc/ae/twlp_client_registration.html).

By default, a Derby database is used to persist the OAuth-related configuration. If you would like to use Db2 instead of Derby, complete the following steps before using the OAuth URLs for client registration or authorization:

1. (If you intend to use Db2 for both the persistence layer for FHIR server and for OAuth configuration, skip to Step 2.) Modify `WLP_HOME>/fhir/bin/createDB.sql` to use `OAUTH2B` as the database name instead of `FHIRDB`, and the run the following command to create the `OAUTH2DB` database:
    ```
    <WLP_HOME>/fhir/bin/createDB.sh <db-user>
    ```
    where `<db-user>` represents the username that will be the administrative user for the database. This is typically the same as the administrative user associated with the Db2 instance in which the database is being created.
    Complete this step one time only <!--For all client installations? -->.

    Use a username which is appropriate for your situation. If omitted, the current logged in user will be used.

    Note: You can create the database using any method you choose. The “createDB.sh” script is simply provided for your convenience. Also, you can choose an alternate name for the database, but make sure you configure the datasource entries in 'server.xml' accordingly. However, we recommend that you do not reuse “FHIRDB” for OAuth configuration.

2. [once after each <!-- Client? --> install] Create or update the schema DDL by completing the following steps:

    a.	Modify <WLP_HOME>/fhir/bin/schemaddl/oauth.properties to reflect your database parameters. In particular, make sure that the url, username, and password properties are set correctly. Note that you can modify the schema name that is configured as part of the url property to suit your requirements (but make sure your datasource entries in 'server.xml' are configured accordingly).

    b.	Run the following commands:

        cd <WLP_HOME>/fhir/bin/schemaddl

        ./liquibase --defaultsFile=./oauth.properties –changeLogFile=ddl/db2/oauth.xml update


    This will create or update your schema to ensure that it is in sync with the freshly-installed FHIR server code.

    Note: The preceding instructions assume that you are running commands on the FHIR server host and reference the `<WLP_HOME>` directory (that is, the root installation directory where you installed the FHIR server). To run these commands directly from the Db2 server host, download the `fhir-schemaddl-distribution.zip` file to your Db2 server host from the same location as the `fhir-server-distribution.zip` file, and decompress it into a clean directory. You can then follow the preceding instructions,substituting the directory where you expanded the ZIP file for the `<WLP_HOME>` variable.

3.	Modify the `server.xml` file to uncomment the Db2-related configuration, and also make sure that the `OAuthDataSource` configuration is commented out under the `Derby/Embedded-related configuration` section. The Db2-related configuration consists of a `library` element, and a `dataSource` element.

    The following example presents a typical Db2-related configuration:

    ```
    <!-- DB2-related configuration -->
    <library id="db2Lib">
    <fileset dir="${shared.resource.dir}/lib/db2" includes="*.jar"/>
    </library>

    <dataSource id="OAuthDataSource" jndiName="jdbc/oAuthConfigDB">
            <jdbcDriver libraryRef="db2Lib"/>
            <properties.db2.jcc user="db2inst1" password="change-password" databaseName="OAUTH2DB" currentSchema="OAUTHDBSCHEMA" driverType="4"/>
    </dataSource>
    ```

4.	Make sure that you configure the correct `user`, `password`, `serverName` and `portNumber` properties for your Db2 server. For OAuth configuration, do not edit the `currentSchema` property.

5.	Restart the FHIR server.

    After you register a client, it is assigned a `client_id` and `client_secret`. The `client_id`, `client_secret`, `token` URL (`https://<host>:<httpsPort>/oauth2/endpoint/oauth2-provider/token`) and `authorize` URL (`https://<host>:<httpsPort>/oauth2/endpoint/oauth2-provider/authorize`) can be used to obtain an access token. For more information about obtaining an access token using the various authorization grant types, see [OAuth 2.0 service invocation](https://www.ibm.com/support/knowledgecenter/SS7K4U_liberty/com.ibm.websphere.wlp.zseries.doc/ae/cwlp_oauth_invoking.html) in the WebSphere Liberty documentation.

    NOTE: By default, `client_credentials` is configured as the grant type in `server.xml`. To change this, modify the `oauthProvider` configuration as follows for SMART on FHIR applications:

    ```
    <oauthProvider id="oauth2-provider" oauthOnly="false">
	<grantType>authorization_code</grantType>
	<databaseStore dataSourceRef="OAuthDerbyDataSource"/>
    </oauthProvider>
    ```


    `authorization_code` is the recommended grant type as mentioned by the SMART on FHIR project (http://docs.smarthealthit.org/authorization/best-practices).

You can use the access token that you obtain in the Authorization Header as a Bearer token to access protected resources on fhir-server. For example:
```
GET /Patient/<id> HTTP/1.1
     Host: <host>:<port>/fhir-server/api/v1
     Authorization: Bearer <access_token>
```

If you would like to configure a different authorization server, uncomment the following lines in `server.xml`:

```
<openidConnectClient authorizationEndpointUrl="https://<SERVER>/authorize"
					clientId="<id>"
					clientSecret=""
					id="client01"
					tokenEndpointUrl="https://<SERVER>/token"> 	
</openidConnectClient>
```

After you register your application, you will be assigned an id and secret. Update `clientId` and `clientSecret` with the assigned values. Update the `authorizationEndpointUrl` and `tokenEndpointUrl` to the correct URLs for your authorization server. The application should be registered to use `https://<host>:<httpsPort>/oidcclient/redirect/client01` as a redirect URL. Also, update the published OAuth URLs in the conformance statement by modifying the following values in `fhir-server-config.json`:

```
"fhirServer":{
        …...
        "oauth":{
        	"regUrl": "https://<host>:9443/oidc/endpoint/oidc-provider/registration",
        	"authUrl": "https://<host>:9443/oauth2/endpoint/oauth2-provider/authorize",
        	"tokenUrl": "https://<host>:9443/oauth2/endpoint/oauth2-provider/token"
        },
```

SMART on FHIR applications use the conformance statement to determine the OAuth URLs to use for authorization.

## 5.3 Custom HTTP Headers
IBM FHIR Server Supports the following custom HTTP Headers:

| Header Name      | Description                |
|------------------|----------------------------|
|`X-FHIR-TENANT-ID`|Specifies which tenant config should be used for the request. Default is `default`. Header name can be overridden via config property `fhirServer/core/tenantIdHeaderName`.|
|`X-FHIR-DSID`|Specifies which datastore config should be used for the request. Default is `default`. Header name can be overridden via config property `fhirServer/core/dataSourceIdHeaderName`.|
|`X-FHIR-FORMATTED`|Specifies whether the response from FHIR Server should be formatted (pretty-printed). Note that this can inflate the size of responses and affect performance. Possible values "true" or "false" override the default, handled as specified in [Section 5.1 Configuration properties reference](#51-configuration-properties-reference).|

## 5.4 Notes about the FHIRJsonParser
The FHIRJsonParser can be configured through the `jsonParserLenient` and `jsonParserValidating` properties. Please see [Section 5.1 Configuration properties reference](#51-configuration-properties-reference) for more information. All `fhir_comments` properties that appear in the JSON input structure are dropped during deserialization. Therefore, they will not appear in the output after re-serialization.

# 6 Related topics
For more information about topics related to configuring a FHIR server, see the following documentation:
*	[IBM Java 8 Keytool documentation](https://www.ibm.com/support/knowledgecenter/SSYKE2_8.0.0/com.ibm.java.security.component.80.doc/security-component/keytoolDocs/keytool_overview.html)
*	[WebSphere Liberty documentation: Configuring your web application and server for client certificate authentication](https://www.ibm.com/support/knowledgecenter/SSEQTP_liberty/com.ibm.websphere.wlp.doc/ae/twlp_sec_clientcert.html)
*	[Java EE 7 Tutorial, Chapter 50](https://docs.oracle.com/javaee/7/tutorial/security-advanced002.htm#GLIEN)
*	[WebSphere Liberty documentation: Defining an OAuth Service Provider](https://www.ibm.com/support/knowledgecenter/SSEQTP_liberty/com.ibm.websphere.wlp.doc/ae/twlp_oauth_defining.html)
*	[WebSphere Liberty documentation: Configuring an Open ID Connect Client in liberty](https://www.ibm.com/support/knowledgecenter/SSEQTP_liberty/com.ibm.websphere.wlp.doc/ae/twlp_config_oidc_rp.html)

<hr />

<b id="f1">1</b> The fhir-server-config.json file contains configuration information associated with the FHIR server. The global configuration is located in `<WLP_HOME>/wlp/usr/servers/fhir-server/config/default/fhir-server-config.json`, with tenant-specific configuration contained in `config/<tenant-id>/fhir-server-config.json`. [↩](#a1)

<b id="f2">2</b> When running database-related commands (e.g. createDB.sh, liquibase, etc.) you'll need to make sure that the DB2-related executables are in your PATH, and also that you are logged in as a user that has the necessary authority to create the database and/or create the schema.  Normally, if you log in as the DB2 administrative user (typically “db2inst1”) then you should be fine. [↩](#a2)

<b id="f3">3</b> The names of these request headers are configurable within the FHIR server's fhir-server-config.json file.  For more information, see [Section 5.1 Configuration properties reference](#51-configuration-properties-reference). [↩](#a3)

<b id="f4">4</b> For more information on multi-tenant support, including multi-tenant configuration properties, jump to [Section 4.10 Multi-Tenancy](#410multi-tenancy). [↩](#a4)

<b id="f5">5</b> An external reference is a reference to a resource which is meaningful outside a particular request bundle.  The value typically includes the resource type and the resource identifier, and could  be an absolute or relative URL.  Examples:  `https://fhirserver1:9443/fhir-server/api/v1/Patient/12345`, `Patient/12345`, etc. [↩](#a5)

<b id="f6">6</b> A local reference is a reference used within a request bundle that refers to another resource within the same request bundle and is meaningful only within that request bundle.  A local reference starts with `urn:`. [↩](#a6)

<b id="f7">7</b> Keystore and truststore files have the same basic structure.   They both provide a secure means for storing certificates.   Typically, we think of a keystore as a file that contains certifcates that consist of a private/public key pair.   And we typically think of a truststore as a file that contains certificates that consist of a public key or trusted certificates. [↩](#a7)

<b id="f8">8</b> While the instructions here show examples of creating self-signed certificates, in reality the FHIR Server deployer will likely need to use certificates that have been signed by a Certificate Authority (CA) such as Verisign, etc. [↩](#a8)

<b id="f9">9</b> The _keytool_ command is provided as part of the Java 8 JRE.  The command can be found in $JAVA_HOME/jre/bin. [↩](#a9)

<b id="f10">10</b> These instructions assume the use of a basic user registry in the server.xml file.  If you are instead using an LDAP registry, then the entire DN associated with the client certificate must match the DN of a user in the LDAP registry. [↩](#a10)

<b id="f11">11</b> For the JAX-RS 2.0 Client API, you would call the ClientBuilder.truststore() method. [↩](#a11)

<b id="f12">12</b> For the JAX-RS 2.0 Client API, you would call the ClientBuilder.keystore() method. [↩](#a12)


FHIR® is the registered trademark of HL7 and is used with the permission of HL7.

[a]:https://www.ibm.com/support/knowledgecenter/en/SSD28V_9.0.0/com.ibm.websphere.wlp.core.doc/ae/cwlp_pwd_encrypt.html

