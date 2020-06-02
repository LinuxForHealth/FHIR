[![Build Status](https://travis-ci.com/IBM/FHIR.svg?branch=master)](https://travis-ci.com/IBM/FHIR)

## IBM FHIR Server
The IBM FHIR Server is a modular Java implementation of version 4 of the [HL7 FHIR specification](https://www.hl7.org/fhir/r4/http.html) with a focus on performance and configurability.

For a detailed description of FHIR conformance, see https://ibm.github.io/FHIR/Conformance.

The server can be packaged as a set of jar files, a web application archive (war), an application installer, or a Docker image.

### Running the IBM FHIR Server
The IBM FHIR Server is available from the [Releases tab](https://github.com/IBM/FHIR/releases) as a zip file with installation scripts for Mac/Linux and Windows or as a docker image at https://hub.docker.com/r/ibmcom/ibm-fhir-server.

More information on installing and running the server is available in the User Guide at https://ibm.github.io/FHIR/guides/FHIRServerUsersGuide.

### Building on top of the IBM FHIR Server Modules
Each of the IBM FHIR Server modules are published to a public Maven repository on [JFrog Bintray](https://bintray.com/ibm-watson-health/ibm-fhir-server-releases).

To use the artifacts from a Maven project:
1. Add the repository to your pom.xml:

    ```
    <repositories>
        <repository>
            <id>ibm-fhir</id>
            <url>https://dl.bintray.com/ibm-watson-health/ibm-fhir-server-releases</url>
        </repository>
        ...
    ```

2. Declare the dependencies:

    For example, to use our visitable, thread-safe FHIR R4 object model (including our high-performance parsers and generators), declare a dependency on the `fhir-model` module:
    ```
    ...
    <dependencies>
        <dependency>
          <groupId>com.ibm.fhir</groupId>
          <artifactId>fhir-model</artifactId>
          <version>${fhir.version}</version>
        </dependency>
        ...
    ```

### IBM FHIR Server Module Catalog

#### Core
|Module|Description|API-stable|
|------|-----------|----------|
|fhir-parent|The parent project for all projects which make up the IBM FHIR Server|false|
|fhir-core|Core helpers and utilities|false|

#### Model and Profile Support
|Module|Description|API-stable|
|------|-----------|----------|
|fhir-model|An object model generated from the FHIR R4 specification and corresponding parsers and generators for XML and JSON|true|
|fhir-registry|A resource registry, registry provider interfaces, and pre-registered resources shipped with the FHIR specification|false|
|fhir-term|A terminology service provider interface with a default implementation that implements the ValueSet expand operation|false|
|fhir-profile|Helper methods for validating ValueSet membership and Profile conformance|false|
|fhir-path|An implementation of version 2.0.0 of the FHIRPath specification assumed by FHIR R4|false|
|fhir-validation|Validation utility for validating resource instances against the base specification and/or configured profiles|false|
|fhir-ig-us-core|A packaging of the US Core Implementation Guide for extending the IBM FHIR Server with US Core Profile validation|false|
|fhir-ig-mcode|A packaging of the minimal Common Oncology Data Elements for extending the IBM FHIR Server with minimal Common Oncology Data Elements Profile validation|false|
|fhir-ig-carin-bb|A packaging of the Consumer-Directed Payer Data Exchange Guide for extending the IBM FHIR Server with  Consumer-Directed Payer Data Exchange Profile validation|false|
|fhir-ig-davinci-pdex-plan-net|A packaging of the DaVinci Payer Data Exchange (PDEX) Plan Net Implementation Guide for extending the IBM FHIR Server with DaVinci Payer Data Exchange (PDEX) Plan Net Profile validation|false|

#### Server
|Module|Description|API-stable|
|------|-----------|----------|
|fhir-config|Configuration property definitions and helpers for working with the fhir-server-config.json config files and multi-tenancy|false|
|fhir-audit|Audit-related interfaces and implementations including 1) a No-op AuditLogService and 2) an AuditLogService that writes audit events to Apache Kafka in the Cloud Auditing Data Federation (CADF) JSON format|false|
|fhir-search|Utilities for working with the FHIR search specification|false|
|fhir-persistence|Interfaces, helpers, and tests for implementing a persistence layer or persistence interceptors for the IBM FHIR Server|false|
|fhir-persistence-schema|Classes for deploying and updating the IBM FHIR Server relational database schema|false|
|fhir-persistence-jdbc|A relational FHIRPersistence implementation that uses JDBC to store and query FHIR resources|false|
|fhir-persistence-proxy|A custom XADataSource implementation for managing distributed transactions across multiple backends|false|
|fhir-provider|JAX-RS Providers for FHIR XML and JSON and related patch formats|false|
|fhir-notification|[Subscription](https://www.hl7.org/fhir/R4/subscription.html) and notification interfaces and helpers|false|
|fhir-notification-websocket|A fhir-notification implementation that uses WebSockets as described at https://www.hl7.org/fhir/R4/subscription.html#2.46.7.2 |false|
|fhir-notification-kafka|An experimental fhir-notification implementation that uses Apache Kafka instead of WebSockets|false|
|fhir-notification-nats|An experimental fhir-notification implementation that uses [NATS](https://nats.io/) instead of WebSockets|false|
|fhir-operation|An operations framework for implementing Extended Operations as describe at https://www.hl7.org/fhir/R4/operations.html |false|
|fhir-server|JAX-RS resources and related classes for implementing the FHIR REST API and extended operations|false|
|fhir-server-webapp|A web application that packages the fhir-server with a set of built-in extended operations|false|
|fhir-server-test|End-to-end integration tests for testing a running server|false|

#### Extended Operations
|Module|Description|API-stable|
|------|-----------|----------|
|fhir-operation-apply|A naive implementation of the `$apply` operation defined at https://www.hl7.org/fhir/operation-activitydefinition-apply.html |false|
|fhir-operation-bulkdata|`$import` and `$export` implementations which translate bulk data requests into JSR352 Java Batch jobs|false|
|fhir-bulkimportexport-webapp|Standalone web application for serving bulk import and export requests via JSR352 Java Batch jobs|false|
|fhir-operation-convert|A limited implementation of the FHIR [$convert operation](https://www.hl7.org/fhir/R4/resource-operation-convert.html), able to convert between JSON and XML but *not* between FHIR versions|false|
|fhir-operation-document|Basic support for the Composition `$document` operation defined at https://www.hl7.org/fhir/operation-composition-document.html |false|
|fhir-operation-healthcheck|The `$healthcheck` operation checks for a valid connection to the database and returns the server status|false|
|fhir-operation-validate|An implementation of the FHIR resource [$validate operation](https://www.hl7.org/fhir/R4/operation-resource-validate.html)|false|

#### Client
|Module|Description|API-stable|
|------|-----------|----------|
|fhir-client|A FHIR Client that re-uses the IBM FHIR Server model and its JAX-RS Providers|false|
|fhir-cli|Experimental command line interface utility for working with the IBM FHIR Server client from the command line|false|

#### Tools and Utilities
|Module|Description|API-stable|
|------|-----------|----------|
|fhir-tools|Code generation tools and logic for generating the FHIR object model, XML and JSON parsers, and the DefaultVisitor base class|false|
|fhir-database-utils|Generic database utilities for working with Apache Derby and IBM Db2 relational database management systems|false|
|fhir-examples-generator|A utility for generating resource examples which range from minimal (only required fields) to complete (every field present)|false|
|fhir-examples|A set of FHIR resource examples including 1) all examples from the FHIR Specification 2) a set of generated examples for test purposes|false|
|fhir-swagger-generator|Utilities for generating Swagger 2.0 and OpenAPI 3.0 definitions for a subset of the FHIR HTTP interface|false|
|fhir-openapi|A web application that provides a simplified OpenAPI 3.0 definition of the FHIR HTTP interface|false|
|fhir-install|Packaging and installation scripts for creating the fhir-distribution zip and the corresponding IBM FHIR Server Docker image|false|
|fhir-benchmark|Java Microbenchmark Harness (JMH) tests for measuring read/write/validation performance for the IBM FHIR Server and the HL7 FHIR Java Reference Implementation|false|

### Contributing to the IBM FHIR Server
The IBM FHIR Server is under active development. To help develop the server, clone or download the project and build it using Maven.
See [Setting up for development](https://github.com/IBM/FHIR/wiki/Setting-up-for-development) for more information.

See [CONTRIBUTING.md](CONTRIBUTING.md) for contributing your changes back to the project.

### License
The IBM FHIR Server is licensed under the Apache 2.0 license. Full license text is
available at [LICENSE](LICENSE).

FHIR® is the registered trademark of HL7 and is used with the permission of HL7.
