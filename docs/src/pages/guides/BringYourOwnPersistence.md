---
layout: post
title: Bring your own Persistence Layer
description: Learn how to build and test a persistence layer for the LinuxForHealth FHIR Server
permalink: /BringYourOwnPersistence/
---

## Overview
The LinuxForHealth FHIR Server is a modular Java implementation of the HL7 FHIR specification with a focus on performance and configurability. The LinuxForHealth FHIR Server ships with a JDBC persistence layer that works with PostgreSQL, Citus and Apache Derby. With this modular design, it's possible to add support for other relational databases and/or plug in any other persistence layer.

This document outlines the interfaces that need to be implemented and the behaviors required for the related methods to work with the LinuxForHealth FHIR Server.

### Interfaces
Persistence layer interfaces are defined in the `fhir-persistence` module.

* [FHIRPersistence](https://github.com/LinuxForHealth/FHIR/blob/main/fhir-persistence/src/main/java/org/linuxforhealth/fhir/persistence/FHIRPersistence.java) defines the contract between the REST layer and the persistence layer.
* [FHIRPersistenceFactory](https://github.com/LinuxForHealth/FHIR/blob/main/fhir-persistence/src/main/java/org/linuxforhealth/fhir/persistence/FHIRPersistenceFactory.java) is the interface for providing instances of FHIRPersistence to the server.

### Configuration
Which persistence layer is used by the server is determined by the `/fhirServer/persistence/factoryClassname` property in `fhir-server-config.json`. There is more detail on the configuration in the [LinuxForHealth FHIR Server's User Guide](https://linuxforhealth.github.io/FHIR/guides/FHIRServerUsersGuide#33-persistence-layer-configuration)

When the default `org.linuxforhealth.fhir.persistence.jdbc.FHIRPersistenceJDBCFactory` is used, the returned FHIRPersistenceJDBCImpl instance will look up a corresponding datasource, by JNDI name, using the combination of the tenant id and datastore id from the request context. Specifically, the connection strategy will use the `fhirServer/persistence/<datasourceId>/jndiName` property in the tenant config, or -- if the property is omitted -- it will construct the name via the following pattern:

``` md
jdbc/fhir_<tenantId>_<datasourceId>[_ro]
```

Note, the `_ro` postfix means the datasource is 'Read Only'.

## Adding support for another relational database
Adding a new relational database type is not for the faint of heart, but the LinuxForHealth FHIR Server team is here to help!
To add support for an alternative relational database, there are multiple modules to consider:

1. `fhir-database-utils`
2. `fhir-persistence-schema`
3. `fhir-persistence-jdbc`

The `fhir-database-utils` module provides generic utilities for defining a PhysicalDataModel and applying it to a target database via the IDatabaseAdapter and IVersionHistoryService interfaces. Check out the `org.linuxforhealth.fhir.database.utils.postgres` and `org.linuxforhealth.fhir.database.utils.derby` packages to understand how you might extend the framework with support for a new database type.

The `fhir-persistence-schema` module is used to programmatically construct DDL statements and execute them against a target database. This module uses the generic utilities in `fhir-database-utils` to deploy both an admin schema (used for tenant provisioning and schema versioning) and an application schema for the FHIR data.

Presently, the `fhir-persistence-schema` and `fhir-persistence-jdbc` modules work with Postgres, Derby and Citus databases, but it should be possible to either:

1. Print the DDL and manually tweak it for your desired database; or
2. Add configuration to control which IDatabaseAdapter and IDatabaseTranslator are used.

Note, the Postgres implementation uses Functions and the Derby implementation is JDBC.

Finally, the `fhir-persistence-jdbc` module provides the default implementation of the `FHIRPersistence` interface.

The module makes heavy use of Data Access Objects (DAO) and Data Transfer Objects (DTO) to abstract the details of the database. Most of the code is common across database types, but there is a branch in `ResourceDAOImpl.insert` which corresponds to the differences between the database flavor.

## Building your own persistence layer
Most FHIR projects are interoperability projects - the data already exists in some datastore.

Due to performance considerations and the complexities of the FHIR API (especially search), we generally recommend converting that data to FHIR and storing the binary representation in the LinuxForHealth FHIR Server's database. However, in some cases, it might be better to configure the LinuxForHealth FHIR Server to work directly with an existing datastore or APIs.

If you are using Maven, add the following dependencies to your persistence layer module (replacing the version variables with your desired version):

``` xml
<dependency>
    <groupId>org.linuxforhealth.fhir</groupId>
    <artifactId>fhir-persistence</artifactId>
    <version>${fhir.persistence.version}</version>
</dependency>
<dependency>
    <groupId>org.linuxforhealth.fhir</groupId>
    <artifactId>fhir-persistence</artifactId>
    <version>${fhir.persistence.version}</version>
    <type>test-jar</type>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.linuxforhealth.fhir</groupId>
    <artifactId>fhir-examples</artifactId>
    <version>${fhir.examples.version}</version>
    <scope>test</scope>
</dependency>
```

You might also want to use `fhir-persistence-jdbc` as an example.

### Implementing the FHIRPersistence Interface
As described [above](#interfaces), implementing your own persistence layer boils down to configuring the server to use your own FHIRPersistenceFactory and returning your own implementation of FHIRPersistence from the getInstance call. The REST layer processes HTTP requests and distills them into one or more calls to this instance. Parameters are passed via a combination of the passed FHIRPersistenceContext and the ThreadLocal FHIRRequestContext.

You do not need to provide an implementation of the getPayloadPersistence call unless you intend to also implement payload offloading (used in cases where the resource payload object is stored outside the primary database).

Although the HL7 FHIR specification [doesn't strictly require all servers to support versioning](https://hl7.org/fhir/R4B/http.html#versions), the LinuxForHealth FHIR Server is built to be version-aware. This means that all FHIRPersistence implementations should implement the `vread` and `history` interactions.
Similarly, the LinuxForHealth FHIR Server was written for read/write datastores and so `create` and `update` should be supported as well.
If you have a use case for a read-only or non-version-aware server, please contact us and consider contributing the necessary modifications to the server to make this supported.

The LinuxForHealth FHIR Server does support persistence implementations which do not support `delete` or transactions (e.g. for transaction bundles), so please implement `FHIRPersistence.isTransactional()` and `isDeleteSupported()` accordingly.

#### Create
```
    /**
     * Stores a new FHIR Resource in the datastore. The resource is not modified before it is stored. It
     * must therefore already include correct Meta fields.
     *
     * @param context the FHIRPersistenceContext instance associated with the current request
     * @param resource the FHIR Resource instance to be created in the datastore
     * @return a SingleResourceResult with a ResourceResult that holds the unmodified resource and/or
     *         an OperationOutcome with hints, warnings, or errors related to the interaction
     * @throws FHIRPersistenceException
     */
    <T extends Resource> SingleResourceResult<T> create(FHIRPersistenceContext context, T resource) throws FHIRPersistenceException;
```
Create requests include a FHIRPersistenceContext and an already-validated instance of the resource to create. The resource will already contain `Resource.id`, `Resource.meta.lastUpdate`, and `Resource.meta.versionId` elements and the persistence layer should not modify the resource in any way before persisting it.

If `SingleResourceResult.success` is set to true, the `SingleResourceResult.resource` should be set to the given resource.

If `SingleResourceResult.success` is set to false, the `SingleResourceResult.outcome` should be an OperationOutcome with a list of one or more issues that prevented the success of the operation.

Note that a `generateResourceId` implementation is required to generate server-assigned resource ids. For performance reasons, recommend the use of `TimestampPrefixedUUID`.

#### Read
```
    /**
     * Retrieves the most recent version of a FHIR Resource from the datastore.
     *
     * @param context the FHIRPersistenceContext instance associated with the current request
     * @param resourceType the resource type of the Resource instance to be retrieved
     * @param logicalId the logical id of the Resource instance to be retrieved
     * @return a SingleResourceResult with a ResourceResult populated from from the datastore and/or
     *         an OperationOutcome with hints, warnings, or errors related to the interaction
     * @throws FHIRPersistenceException
     * @implSpec Reading a non-existent resource will result in a SingleResourceResult with success=false and isDeleted=false
     * @implSpec Reading a deleted resource will result in a SingleResourceResult with both success=true and isDeleted=true
     */
    <T extends Resource> SingleResourceResult<T> read(FHIRPersistenceContext context, Class<T> resourceType, String logicalId)
            throws FHIRPersistenceException;

```
Read requests include a FHIRPersistenceContext, a Class object for the resource type being read, and the logical id of the resource to read.
Implementations should check the FHIRSearchContext of the FHIRPersistenceContext to determine whether the caller would like the full resource back, the resource text or data, or just a summary (see `FHIRSearchContext.getSummaryParameter()`).

For successful requests, `SingleResourceResult.success` must be set to true and `SingleResourceResult.resource` must include a [potentially filtered] resource of the type passed in the request.

If the resource with this id could not be found for the passed resource type, set the return values as follows:
  - `SingleResourceResult.success` set to false
  - `SingleResourceResult.resource` set to null
  - `SingleResourceResult.isDeleted` set to false

If the latest version of this resource is marked as deleted, set the return values as follows:
  - `SingleResourceResult.success` set to true
  - `SingleResourceResult.resource` set to null
  - `SingleResourceResult.isDeleted` set to true

For all other errors, the implementation should return a `SingleResourceResult` with a success status of false and a non-null outcome with one or more issues to indicate the failure.

Note: we plan to deprecate the use of exceptions and use only `SingleResourceResult` as part of https://github.com/LinuxForHealth/FHIR/issues/194.

#### Version read
```
    /**
     * Retrieves a specific version of a FHIR Resource from the datastore.
     *
     * @param context the FHIRPersistenceContext instance associated with the current request
     * @param resourceType the resource type of the Resource instance to be retrieved
     * @param logicalId the logical id of the Resource instance to be retrieved
     * @param versionId the version of the Resource instance to be retrieved
     * @return a SingleResourceResult with a ResourceResult populated from the datastore and/or
     *         an OperationOutcome with hints, warnings, or errors related to the interaction
     * @throws FHIRPersistenceException
     * @implSpec Reading a non-existent resource version will result in a SingleResourceResult with success=false and isDeleted=false
     * @implSpec Reading a deleted resource version will result in a SingleResourceResult with both success=true and isDeleted=true
     */
    <T extends Resource> SingleResourceResult<T> vread(FHIRPersistenceContext context, Class<T> resourceType, String logicalId, String versionId)
            throws FHIRPersistenceException;
```
Version read requests work just like read requests except that the caller passes a version identifier and the persistence implementation must return that specific version of the resource.

#### Update
```
    /**
     * Updates an existing FHIR Resource by storing a new version in the datastore.
     * This new method expects the resource being passed in to already be modified with correct
     * meta and id information. It no longer updates the meta itself.
     *
     * @param context the FHIRPersistenceContext instance associated with the current request
     * @param resource the new contents of the FHIR Resource to be stored
     * @return a SingleResourceResult with a ResourceResult that holds a copy of the input resource and/or
     *         an OperationOutcome with hints, warnings, or errors related to the interaction
     * @throws FHIRPersistenceException
     */
    <T extends Resource> SingleResourceResult<T> update(FHIRPersistenceContext context, T resource) throws FHIRPersistenceException;
```
Update requests include a FHIRPersistenceContext, a resource logical id, and an updated version of the resource to save. As for [create](#Create), the given resource should be treated as immutable and not modified in any way. If the persistence implementation requires the versionId, this can be obtained from the `Resource.meta.versionId` field. The LinuxForHealth FHIR Server uses a contiguous sequence of integer values for versionId, starting with 1.

Note: at the REST layer, an update request will first invoke read and then invoke update. Similarly, PATCH requests are converted to normal updates before reaching the persistence layer's update implementation.

To ensure consistency and data integrity, the persistence implementation SHOULD lock the record representing the resource and verify that the versionId of the current record is one less than the versionId of the new resource value. If this check is implemented and the rule is violated, the persistence layer SHALL throw `FHIRPersistenceVersionIdMismatchException`.

FHIRPersistence implementations SHOULD use the value of the [`fhirServer/persistence/common/updateCreateEnabled`](https://linuxforhealth.github.io/FHIR/guides/FHIRServerUsersGuide#45-updatecreate-feature) property to determine whether they should allow an update to a resource that doesn't exist yet.

#### Delete
Delete requests include a FHIRPersistenceContext, a Class object for the resource type being deleted, and the logical id of the resource to delete.
FHIRPersistence implementations are expected to be version-aware and therefore must perform a "soft" delete, handling the delete like an update by creating a new resource marker in the database. It is not necessary for the persistence layer to store a copy of the resource (because a READ/VREAD of a deleted version will always return a null resource).

For implementations that do not implement delete, FHIRPersistence includes a default implementation which throws a FHIRPersistenceNotSupportedException.

#### History
The LinuxForHealth FHIR Server currently only supports history at the resource instance level. History requests include a FHIRPersistenceContext with an embedded FHIRHistoryContext, a Class object for the resource type being requested, and the logical id of the resource for which to show the history. Implementations should also check the FHIRSearchContext of the FHIRPersistenceContext to determine whether the caller would like the full resources back, the resource text or data, or just a summary (see `FHIRSearchContext.getSummaryParameter()`).

FHIRHistoryContext extends FHIRPagingContext and provides the requested page size and page number to return.
Similarly, FHIRPersistence implementations should check and honor the the `since` attribute (when valued).

In addition to setting the MultiResourceResult success indicator and the resource version instances for the requested page, FHIRPersistence implementations must set the total number of versions for the requested resource (`FHIRPagingContext.setTotalCount(int)`) and a map of deleted resource versions (`FHIRHistoryContext.setDeletedResources()`) for the REST layer to properly construct the response bundle and accurately reflect which versions are deletes (rather than updates).

The FHIRPersistence also supports whole system `_history`. If supported, `isChangesSupported` must return true, and `changes` must be implemented to return a list of ResourceChangeLogRecord DTOs.

#### Search
The [FHIR Search specification](https://hl7.org/fhir/R4B/search.html) is sprawling and difficult to implement in its entirety. At the persistence layer, search requests will include a FHIRPersistenceContext with an embedded FHIRSearchContext and a Class object to indicate the resource type(s) to search on.
A Class of `org.linuxforhealth.fhir.model.type.Resource` is passed for searches performed at the "whole-system" level.

The query parameters passed in the HTTP request are parsed at the REST layer and passed to the persistence layer in the form of a FHIRSearchContext.
The FHIRSearchContext separates "return" parameters (like `_include`, `_revinclude`, `_sort`, etc.) from search parameters and makes them available through dedicated getters.
Each search parameter is parsed into a QueryParameter and a QueryParameterValue. [Compartment](https://hl7.org/fhir/R4B/compartmentdefinition.html) searches are converted into [chained parameters](https://hl7.org/fhir/R4B/search.html#chaining), which are represented through nested QueryParameter objects within the outermost QueryParameter.
Check `org.linuxforhealth.fhir.search.util.SearchUtil.parseQueryParameters()` for more information.

FHIRSearchContext extends FHIRPagingContext and provides the requested page size and page number to return.
FHIRPersistence implementations are responsible for setting the total number of search results (`FHIRPagingContext.setTotalCount(int)`) for the given query.

On success, set `MultiResourceResult.success` to true and set `MultiResourceResult.resource` to the list of resources [or resource summaries] for the requested page.

On failure, set `MultiResourceResult.success` to false and set `MultiResourceResult.outcome` to an OperationOutcome with one or more issues which indicate the failure.

#### Extended Operations

The LinuxForHealth FHIR Server supports [extended operations](https://hl7.org/fhir/R4B/operations.html). The LinuxForHealth FHIR Server has some operations which use custom persistence interactions:

| Operation Name | Interfaces to implement |
|----------------|-------------------------|
| `$healthcheck` | `getHealth` must be implemented to indicate if the persistence layer is in a healthy state and can access the data store |
| `$erase`  | `erase` must be implemented so that ResourceEraseRecord is returned for each Erase operation.  If not implemented it throws an exception|
| `$export` | `fetchResourcePayloads` must be implemented so that the ResourcePayload is returned as a BinaryStream |
| `$reindex`| `isReindexSupported` must be true and, if true, `reindex` must be implemented so that users can apply the latest search configuration to a resource after it is already ingested|

## Testing your persistence layer
In addition to defining the interfaces, the `fhir-persistence` module includes a set of tests that you can extend to test your implementation.

Most of the tests defined in this module relate to search, but they also exercise the create, update, and delete interactions in the process.
The tests in the `org.linuxforhealth.fhir.persistence.search.test` package are organized by search parameter type and they utilize tenant-specific search parameter definitions from the `fhir-persistence/src/test/resources/config` directory and search for fields on the generated example resources at `fhir-examples/src/main/resources/json/basic`. The `fhir-examples` module is available on Maven Central [link](https://repo1.maven.org/maven2/org/linuxforhealth/fhir/fhir-examples/).

For an example of how to extend these tests, see the `org.linuxforhealth.fhir.persistence.jdbc.search.test` package under `fhir-persistence-jdbc/src/test/java`.

Finally, the LinuxForHealth FHIR Server contains a number of end-to-end (e2e) integration tests under the [`fhir-server-test`](https://github.com/LinuxForHealth/FHIR/tree/main/fhir-server-test) module. These tests can be executed against a running server that is configured with your persistence layer to provide further confidence in your implementation.
