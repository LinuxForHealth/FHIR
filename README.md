## LinuxForHealth FHIR Server
The LinuxForHealth FHIR® Server (formerly the IBM® FHIR® Server) is a modular Java implementation of the [HL7 FHIR specification](https://hl7.org/fhir/R4B/http.html) that supports versions R4 and R4B with a focus on performance and configurability.

For a detailed description of FHIR conformance, see https://linuxforhealth.github.io/FHIR/Conformance.

The server is available in the following forms:
* a web application archive (war)
* a zip file with installation script
* a [Linux container image](https://hub.docker.com/r/ibmcom/ibm-fhir-server) from the ibmcom org on DockerHub
* a [helm chart](https://artifacthub.io/packages/helm/alvearie/ibm-fhir-server) from the alvearie org on ArtifactHub

### Running the server
Guides for configuring, operating, and extending the LinuxForHealth FHIR Server are available from https://linuxforhealth.github.io/FHIR/guides/FHIRServerUsersGuide.

#### From the zip installer
Download the fhir-persistence-schema and fhir-install assets from the [Releases tab](https://github.com/LinuxForHealth/FHIR/releases) and follow the instructions from the [User's Guide](https://linuxforhealth.github.io/FHIR/guides/FHIRServerUsersGuide#21-installing-a-new-server) to:
1. Use fhir-persistence-schema-VERSION-cli.jar to deploy the schema.
2. Unzip, install, and configure the server.

#### From the container image
Quickstart:
```
docker run -p 9443:9443 -e BOOTSTRAP_DB=true ibmcom/ibm-fhir-server
```

See https://hub.docker.com/r/ibmcom/ibm-fhir-server for more information.

Note:
1. The Docker image [ibmcom/ibm-fhir-schematool](https://hub.docker.com/r/ibmcom/ibm-fhir-schematool) is an early technology preview and is experimental.
2. The Docker image [ibmcom/ibm-fhir-bucket-tool](https://hub.docker.com/r/ibmcom/ibm-fhir-bucket-tool) is an early technology preview and is experimental.
3. The Docker image [ibmcom/ibm-fhir-term-loader](https://hub.docker.com/r/ibmcom/ibm-fhir-term-loader) is an early technology preview and is experimental.

#### From the helm chart
Quickstart:
```
helm repo add alvearie https://alvearie.io/alvearie-helm
export POSTGRES_PASSWORD=$(openssl rand -hex 20)
helm upgrade --install --render-subchart-notes ibm-fhir-server alvearie/ibm-fhir-server --set postgresql.postgresqlPassword=${POSTGRES_PASSWORD} --set ingress.hostname=example.com --set 'ingress.tls[0].secretName=cluster-tls-secret'
```

See https://artifacthub.io/packages/helm/alvearie/ibm-fhir-server for more information.

### Building with the LinuxForHealth FHIR Modules
Each of the LinuxForHealth FHIR Server modules are published to Maven Central under [org.linuxforhealth.fhir](https://repo1.maven.org/maven2/org/linuxforhealth/fhir/).

To use the artifacts from a Maven project, declare the dependencies. For example, to use our visitable, thread-safe FHIR R4 object model (including our high-performance parsers and generators), declare a dependency on the `fhir-model` module:

```
...
<dependencies>
    <dependency>
      <groupId>org.linuxforhealth.fhir</groupId>
      <artifactId>fhir-model</artifactId>
      <version>${fhir.version}</version>
    </dependency>
    ...
```

Note, if you are using a local repository or private host, you must add the repository to your pom.xml:

```
<repositories>
    <repository>
        <id>ibm-fhir</id>
        <url>https://myhost.com/ibm-fhir-server-releases</url>
    </repository>
    ...
```

For versions prior to 4.7.0, the LinuxForHealth FHIR Server modules are only available from the [Releases tab](https://github.com/LinuxForHealth/FHIR/releases) in an archived Maven repository format.

### LinuxForHealth FHIR modules
The LinuxForHealth FHIR Server is modular and extensible. The following tables provide an overview of all the modules, along with an indicator of the stability of the Java APIs defined in each module. This indicator is only applicable to the direct usage of the modules, not for usage of the LinuxForHealth FHIR Server as a whole.

#### Core
|Module|Description|Java API-stable|
|------|-----------|----------|
|fhir-parent|The parent project for all projects which make up the LinuxForHealth FHIR Server|false|
|fhir-core|Core helpers and utilities|false|
|fhir-cache|Cache-related helpers and utilities|false|

#### Model and Profile Support
|Module|Description|Java API-stable|
|------|-----------|----------|
|fhir-model|An object model generated from the FHIR R4B specification and corresponding parsers and generators for XML and JSON|true|
|fhir-registry|A resource registry and registry provider interfaces for extending the registry|false|
|term/fhir-term|A terminology service provider interface with a default implementation that implements terminology services from fully-defined CodeSystems in the registry|false|
|term/fhir-term-graph|An expermental terminology service provider that implements terminology services using JanusGraph|false|
|term/fhir-term-graph-loader|Utilities to populate the fhir-term-graph JanusGraph with concepts|false|
|term/fhir-term-remote|A terminology service provider that connects to an external service using a REST client to access code system content|false|
|fhir-profile|Helper methods for validating ValueSet membership and Profile conformance|false|
|fhir-path|An implementation of version 2.0.0 of the FHIRPath specification|false|
|fhir-validation|Validation utility for validating resource instances against the base specification and configured profiles|false|
|conformance/fhir-core-r4|Conformance artifacts for HL7 FHIR version 4.0.1|false|
|conformance/fhir-core-r4b|Conformance artifacts for HL7 FHIR version 4.3.0|false|
|conformance/fhir-hl7-terminology|CodeSystems and ValueSets from HL7 Terminology (THO) version 3.1.0|false|
|conformance/fhir-ig-us-core|Packaging the US Core Implementation Guide for the LinuxForHealth FHIR registry|false|
|conformance/fhir-ig-mcode|Packaging the minimal Common Oncology Data Elements for the LinuxForHealth FHIR registry|false|
|conformance/fhir-ig-carin-bb|Packaging the Consumer-Directed Payer Data Exchange Guide for the LinuxForHealth FHIR registry|false|
|conformance/fhir-ig-davinci-pdex|Packaging the Da Vinci Payer Data Exchange (PDEX) Implementation Guide for the LinuxForHealth FHIR registry|false|
|conformance/fhir-ig-davinci-hrex|Packaging the Da Vinci Health Record Exchange (HREX) Implementation Guide for the LinuxForHealth FHIR registry|false|
|conformance/fhir-ig-davinci-pdex-plan-net|Packaging the Da Vinci Payer Data Exchange (PDEX) Plan Net Implementation Guide for the LinuxForHealth FHIR registry|false|
|conformance/fhir-ig-davinci-pdex-formulary|Packaging the Da Vinci Payer Data Exchange (PDex) US Drug Formulary Implementation Guide for the LinuxForHealth FHIR registry|false|

#### Server
|Module|Description|Java API-stable|
|------|-----------|----------|
|fhir-config|Configuration property definitions and helpers for working with the fhir-server-config.json config files and multi-tenancy|false|
|fhir-audit|Audit-related interfaces and implementations including 1) a No-op AuditLogService and 2) an AuditLogService that writes audit events to Apache Kafka in the Cloud Auditing Data Federation (CADF) JSON format|false|
|fhir-search|Utilities for working with the FHIR search specification|false|
|fhir-persistence|Interfaces, helpers, and tests for implementing a persistence layer for the server|false|
|fhir-persistence-jdbc|A relational FHIRPersistence implementation that uses JDBC to store and query FHIR resources|false|
|fhir-persistence-scout|A scale out persistence layer to store and query FHIR resources *experimental* |false|
|fhir-persistence-cos|Decorates the fhir-persistence-jdbc module with the ability to offload payload storage to IBM Cloud Object Storage *experimental* |false|
|fhir-persistence-cassandra|Decorates the fhir-persistence-jdbc module with the ability to offload payload storage to Cassandra *experimental* |false|
|fhir-persistence-blob|Decorates the fhir-persistence-jdbc module with the ability to offload payload storage to Azure Blob *experimental* |false|
|fhir-provider|JAX-RS Providers for FHIR XML and JSON and related patch formats|false|
|fhir-server|JAX-RS resources and related classes for implementing the FHIR REST API and extended operations|false|
|fhir-server-webapp|A web application that packages the fhir-server with a set of built-in extended operations|false|
|fhir-server-test|End-to-end integration tests for testing a running server|false|
|fhir-smart|An interceptor that provides SMART-on-FHIR authorization policy enforcement|false|

#### Extended Operations
|Module|Description|Java API-stable|
|------|-----------|----------|
|fhir-operation-test|Sample operations for testing Extended Operations as describe at https://hl7.org/fhir/R4B/operations.html |false|
|fhir-operation-bulkdata|`$import` and `$export` implementations which translate bulk data requests into JSR352 Java Batch jobs|false|
|fhir-bulkdata-webapp|Standalone web application for serving bulk import and export requests via JSR352 Java Batch jobs|false|
|fhir-operation-convert|A limited implementation of the FHIR [$convert operation](https://hl7.org/fhir/R4B/resource-operation-convert.html), able to convert between JSON and XML but *not* between FHIR versions|false|
|fhir-operation-document|Basic support for the Composition `$document` operation defined at https://hl7.org/fhir/R4B/operation-composition-document.html |false|
|fhir-operation-healthcheck|The `$healthcheck` operation checks for a valid connection to the database and returns the server status|false|
|fhir-operation-term|[Terminology service](https://hl7.org/fhir/R4B/terminology-service.html) operations which use the default fhir-term TerminologyServiceProvider to implement $expand, $lookup, $subsumes, $closure, $validate and $translate|false|
|fhir-operation-term-cache|Add-on module that provides operations for clearing the terminology subsystem caches for non-production scenarios|false|
|fhir-operation-validate|An implementation of the FHIR resource [$validate operation](https://hl7.org/fhir/R4B/operation-resource-validate.html)|false|
|fhir-operation-everything|An implementation of the FHIR patient [`$everything`](https://hl7.org/fhir/R4B/operation-patient-everything.html) operation|false|
|fhir-operation-erase|A hard delete operation for resource instances referred to as the `$erase` operation. See the [README.md](operation/fhir-operation-erase/README.md)|false|
|fhir-operation-member-match|An extensible framework and reference implementation for Davinci HREX $member-match using the default IBM FHIR Server. See the [README.md](operation/fhir-operation-member-match/README.md) *experimental*|false|

#### Client
|Module|Description|Java API-stable|
|------|-----------|----------|
|fhir-client|A FHIR Client that re-uses the LinuxForHealth FHIR Server model and its JAX-RS Providers|false|

#### Clinical Quality

|Module|Description|Java API-stable|
|------|-----------|----------|
|cql/fhir-cql|Foundation classes for implementing the CQL Engine backend in IBM FHIR Server|false|
|cql/fhir-cql-rest|REST Client-based implementation of CQL Engine backend|false|
|cql/fhir-cql-server|Internal API-based implementation of CQL Engine backend|false|
|cql/fhir-quality-measure|FHIR Quality Measure evaluation logic|false|
|cql/operation/fhir-operation-cpg|*Optional* module that implements CQL operations|false|
|cql/operation/fhir-operation-cqf|*Optional* module that implements CQF operation|false|
|cql/operation/fhir-operation-apply|A naive implementation of the `$apply` operation defined at https://hl7.org/fhir/R4B/operation-activitydefinition-apply.html |false|

#### Tools and Utilities
|Module|Description|Java API-stable|
|------|-----------|----------|
|fhir-tools|Code generation tools and logic for generating the FHIR object model, XML and JSON parsers, and the DefaultVisitor base class|false|
|fhir-database-utils|Generic database utilities for working with Apache Derby and PostgreSQL relational database management systems|false|
|fhir-examples-generator|A utility for generating resource examples which range from minimal (only required fields) to complete (every field present)|false|
|fhir-examples|A set of FHIR resource examples including 1) all examples from the FHIR Specification 2) a set of generated examples for test purposes|false|
|fhir-swagger-generator|Utilities for generating Swagger 2.0 and OpenAPI 3.0 definitions for a subset of the FHIR HTTP interface|false|
|fhir-openapi|A web application that provides a simplified OpenAPI 3.0 definition of the FHIR HTTP interface|false|
|fhir-install|Packaging and installation scripts for creating the fhir-distribution zip and the corresponding IBM FHIR Server Docker image|false|
|fhir-benchmark|Java Microbenchmark Harness (JMH) tests for measuring read/write/validation performance for the LinuxForHealth FHIR Server and the HL7 FHIR Java Reference Implementation|false|
|fhir-bucket|Scans cloud object storage buckets and uploads data using the FHIR REST API|false|
|fhir-persistence-schema|Classes for deploying and updating the LinuxForHealth FHIR Server relational database schema|false|
|fhir-persistence-cassandra-app|CLI utility application supporting payload storage to Cassandra *experimental* |false|

### Contributing to the LinuxForHealth FHIR Server
The LinuxForHealth FHIR Server is under active development. To help develop the server, clone or download the project and build it using Maven.
See [Setting up for development](https://github.com/LinuxForHealth/FHIR/wiki/Setting-up-for-development) for more information.

See [CONTRIBUTING.md](CONTRIBUTING.md) for contributing your changes back to the project.

See [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md) for code of conduct.

### License
The LinuxForHealth FHIR Server and its corresponding modules are licensed under the Apache 2.0 license.
The full license text is available at [LICENSE](LICENSE).

FHIR® is the registered trademark of HL7 and is used with the permission of HL7. Use of the FHIR trademark does not constitute endorsement of this product by HL7.
IBM and the IBM logo are trademarks of International Business Machines Corporation, registered in many jurisdictions worldwide. Other product and service names might be trademarks of IBM or other companies. A current list of IBM trademarks is available at [https://ibm.com/trademark](https://ibm.com/trademark).
