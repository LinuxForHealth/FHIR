---
layout: default
title: Search Configuration Overview
permalink: /FHIRSearchConfiguration/
---

# IBM FHIR Server - Search Configuration Overview
The [FHIR Specification](https://hl7.org/fhir/R4B/search.html) defines a set of searchable fields for each resource type. The IBM FHIR Server supports searching via both specification-defined search parameters and tenant-specific search parameters.

Specifically, the IBM FHIR Server supports searching on additional fields, including:
* fields that are defined in the base specification, which are not configured for search; and
* extension elements that a tenant may add to a standard FHIR resource type

The IBM FHIR Server allows deployers to define search parameters on a tenant-specific basis. This allows each tenant to share an instance of the IBM FHIR Server while maintaining the ability to have their own set of search parameters. Additionally, specification-defined search parameters can be filtered out in order to avoid the cost of extracting and storing the corresponding indices.

Tenant search parameters are defined via a [Bundle](https://hl7.org/fhir/R4B/bundle.html) of [SearchParameter](https://hl7.org/fhir/R4B/searchparameter.html) resources that define the additional search parameters which describe the searchable field and define the FHIRPath expression for extraction.  For example, a tenant that extends the `Patient` resource type with the `favorite-color` extension, enables search on `favorite-color` by defining a SearchParameter as part of this bundle.

## 1 Configuration
Since IBM FHIR Server 4.10.0, *all* SearchParameter definitions are loaded from the `fhir-registry` by the `fhir-search` module during server startup.
The definitions in the registry can come from the core specification, packaged implementation guides, or other registry resource providers via the FHIRRegistryResourceProvider SPI.
Although FHIRRegistry is dynamic, changes to search parameters in the registry (e.g. addition, removal, or modification of SearchParameter resources) are *NOT* reflected in the running server. One must restart the server in order to apply the changes. Additionally, to apply the new search parameter configuration to previously-ingested resources, it is necessary to perform [reindexing](#2-re-index).

In previous versions, only the built-in parameters were loaded from the registry; tenant-specific parameters were loaded from `extension-search-parameters.json` files in the tenant config folders.

For backwards compatibility, IBM FHIR Server 4.10.0 contains a new FHIRRegistryResourceProvider that reads these same `extension-search-parameters.json` files and contributes their contents to the registry, so that this same mechanism can still be used.

The IBM FHIR Server supports compartment search based on CompartmentDefinition resources.
Since IBM FHIR Server 4.8.1, the IBM FHIR Server will load compartment definitions from the registry.
In previous versions, the CompartmentDefinition resources came from a configuration file.

### 1.1 Tenant-specific parameters
To configure tenant-specific search parameters, create a file called `extension-search-parameters.json`, populate it with a Bundle of `SearchParameter` resources, and place it in the `${server.config.dir}/config/<tenant-id>` directory. For example, the `${server.config.dir}/config/acme/extension-search-parameters.json` file would contain the search parameters for the `acme` tenant, while `${server.config.dir}/config/qpharma/extension-search-parameters.json` would contain search parameters used by the `qpharma` tenant.

If a tenant-specific extension-search-parameters.json file does not exist, the server falls back to the default `extension-search-parameters.json` file found at `${server.config.dir}/config/default/extension-search-parameters.json`. For performance reasons, we recommend having an `extension-search-parameters.json` for each tenant.

#### 1.2 SearchParameter details
The `fhir-search` module requires that the [expression](https://hl7.org/fhir/R4B/searchparameter-definitions.html#SearchParameter.expression), [type](https://hl7.org/fhir/R4B/searchparameter-definitions.html#SearchParameter.type) and [code](https://hl7.org/fhir/R4B/searchparameter-definitions.html#SearchParameter.code) be set, as in the following example:
```json
{
  "fullUrl": "http://ibm.com/fhir/SearchParameter/Patient-favorite-color",
  "resource": {
    "resourceType": "SearchParameter",
    "id": "Patient-favorite-color",
    "url": "http://ibm.com/fhir/SearchParameter/Patient-favorite-color",
    "version": "4.0.0",
    "name": "favorite-color",
    "status": "draft",
    "experimental": false,
    "date": "2018-12-27T22:37:54+11:00",
    "publisher": "IBM FHIR Server Test",
    "contact": [{
      "telecom": [{
        "system": "url",
        "value": "http://ibm.com/fhir"
      }]
    },
    {
      "telecom": [{
        "system": "url",
        "value": "http://ibm.com/fhir"
      }]
    }],
    "description": "the patient's favorite color",
    "code": "favorite-color",
    "base": ["Patient"],
    "type": "string",
    "xpathUsage": "normal",
    "xpath": "f:Patient/f:extension[@url='http://ibm.com/fhir/extension/Patient/favorite-color']/f:valueString",
    "expression": "Patient.extension.where(url='http://ibm.com/fhir/extension/Patient/favorite-color').value",
    "multipleOr": true,
    "multipleAnd": true,
    "modifier": []
  }
}
```

A couple things to note:
- This SearchParameter includes an xpath element for completeness, but the IBM FHIR Server does not use the XPath during extraction; it only uses the expression (FHIRPath).
- SearchParameter expressions that select choice type elements should follow FHIRPath rules and omit the specific type suffixed (e.g. use `value` and not `valueString`).

Each time a resource is created, updated or reindexed, the IBM FHIR Server evaluates the FHIRPath expression applicable to the resource type and indexes the values of the matching elements, making these available via a search where the query parameter name matches the `code` element on the `SearchParameter` definition.

In the preceding example, extension elements (on a Patient resource) with a url of `http://ibm.com/fhir/extension/Patient/favorite-color` are indexed by the `favorite-color` search parameter. To search for Patients with a favorite color of "pink", users could send an HTTP GET request to a URL like `[base]/Patient?favorite-color:exact=pink`.

When creating the SearchParameter FHIRPath expression, be sure to test both the FHIRPath expression and the search parameter.

If a search parameter expression extracts an element with a data type that is incompatible with the declared search parameter type, the server skips the value and logs a message. For choice elements, like Extension.value, its recommended to restrict the expression to values of the desired type by using the `as` function. For example, to select only Decimal values from the http://example.org/decimal extension, use an expressions like `Basic.extension.where(url='http://example.org/decimal').value.as(decimal)`.

#### 1.2.1 The implicit-system extension
The IBM FHIR Server team has introduced a custom SearchParameter extension that can be used to improve search performance for queries that are made against a token SearchParameter without passing a system. Specifically, for SearchParameter resources that index elements of type Code which have a required binding with a single system, adding the following extension to the SearchParameter definition allows the server to infer the system value without requiring end users to explicitly pass it in their queries:

```json
{
    "url": "http://ibm.com/fhir/extension/implicit-system",
    "valueUri": "http://hl7.org/fhir/account-status"
}
```

See the [FHIR Performance Guide](FHIRPerformanceGuide#65-search-examples) for more information.

### 1.3 Filtering
The IBM FHIR Server supports the filtering of search parameters through `fhir-server-config.json`. The default behavior of the IBM FHIR Server is to consider all built-in and tenant-specific search parameters when storing resources or processing search requests, but you can configure inclusion filters to restrict the IBM FHIR Server's view to specific search parameters on a given resource type.

Why would you want to filter built-in search parameters? The answer lies in how search parameters are used by the IBM FHIR Server. When the FHIR server processes a _create_ or _update_ operation, it stores the resource contents in the datastore, along with search index information that is used by the IBM FHIR Server when performing search operations. The search index information stored for a particular resource instance is driven by the search parameters defined for that resource type. Therefore if you are storing a resource whose type has a lot of built-in search parameters defined for it (e.g. `Patient`), then you could potentially be storing a lot of search index information for each resource.

For performance and scalability reasons, it might be desirable to limit the number of search parameters considered during a _create_ or _update_ operation for particular resource types. If there will be no need to use the search index information, there's no need to store it. For example, if you know that due to the way in which a particular tenant's applications use the FHIR REST API that those applications will never need to search Patients by birthdate, then there would be no need to store search index information for the `birthdate` attribute in `Patient` resources. Consequently, you could filter out the `birthdate` search parameter for the `Patient` resource type and not lose any needed functionality, but yet save a little bit of storage capacity in your datastore.

The search parameter filtering feature is supported through a set of inclusion rules specified via the `fhirServer/resources` property group in `fhir-server-config.json`. The search parameter inclusion rules allow you to define a set of search parameters per resource type that should be included in the IBM FHIR Server's view of search parameters when storing resources and performing search operations. The following snippet shows the general form for specifying inclusion rules:

```
"resources": {
    "open": false,
    "CarePlan": {
        "searchParameters": {}
    },
    "ExplanationOfBenefit": {
        "searchParameters": {
            "_id": "http://hl7.org/fhir/SearchParameter/Resource-id",
            "_lastUpdated": "http://hl7.org/fhir/SearchParameter/Resource-lastUpdated",
            "patient": "http://hl7.org/fhir/us/carin-bb/SearchParameter/explanationofbenefit-patient",
            "type": "http://hl7.org/fhir/us/carin-bb/SearchParameter/explanationofbenefit-type",
            "identifier": "http://hl7.org/fhir/us/carin-bb/SearchParameter/explanationofbenefit-identifier",
            "service-date": "http://hl7.org/fhir/us/carin-bb/SearchParameter/explanationofbenefit-service-date"
        }
    },
    "Patient": {
        "searchParameters": {}
    },
    "RelatedPerson": {
        "searchParameters": {}
    }
}
```

The `fhirServer/resources/<resourceType>/searchParameters` property group is a JSON map where the key is the search parameter code that will be used to search on this parameter and the value is a canonical URL which resolves to a SearchParameter definition from the `fhir-registry` at run-time.
Omitting this property is equivalent to supporting all search parameters in the server's registry that apply to the given resource type.
An empty object, `{}`, can be used to indicate that no search parameters are supported.

It may be desirable to re-define a single search parameter code. In this case, if you do not wish to filter any other parameters for this type, a value of `"*": "*"` can be used to prevent further filtering.

Additionally, for SearchParameters defined across all resource types (i.e. SearchParameters with a base of type `Resource`), the filter can be applied at this level as well:

```
"resources": {
    "open": false,
    "CarePlan": {
        "searchParameters": {}
    },
    "ExplanationOfBenefit": {
        "searchParameters": {
            "patient": "http://hl7.org/fhir/us/carin-bb/SearchParameter/explanationofbenefit-patient",
            "type": "http://hl7.org/fhir/us/carin-bb/SearchParameter/explanationofbenefit-type",
            "identifier": "http://hl7.org/fhir/us/carin-bb/SearchParameter/explanationofbenefit-identifier",
            "service-date": "http://hl7.org/fhir/us/carin-bb/SearchParameter/explanationofbenefit-service-date"
        }
    },
    "Patient": {
        "searchParameters": {}
    },
    "RelatedPerson": {
        "searchParameters": {}
    },
    "Resource": {
        "searchParameters": {
            "_id": "http://hl7.org/fhir/SearchParameter/Resource-id",
            "_lastUpdated": "http://hl7.org/fhir/SearchParameter/Resource-lastUpdated"
        }
    }
}
```

For reference, the following is the list of codes in this category:

| code                          |
|-------------------------------|
| _id                           |
| _lastUpdated                  |
| _profile                      |
| _security                     |
| _source                       |
| _tag                          |
| _content                      |
| _query                        |

Note: `_content` and `_query` are not yet supported by the IBM FHIR Server.

The filter `"*": "*"` is not necessary to include these Resource-level parameters.

#### 1.2.1 Handling unexpected search parameters
The IBM FHIR Server supports configurable handling of unknown or unsupported search parameters as defined at https://linuxforhealth.github.io/FHIR/Conformance#http-headers.
Filtered search parameters are handled exactly the same as undefined search parameters, meaning that searches which include these parameters will fail in `strict` mode.

#### 1.2.2 Compartment search considerations with filtering
For each compartment type, the rules for determining if a resource is a member of a compartment of that type, and thus if that resource should be returned on an associated compartment search, are based on inclusion criteria. Inclusion criteria is one or more search parameters whose value is a reference to the compartment resource type. These membership rules are defined by the FHIR specification for the following compartments:
- [Device](https://hl7.org/fhir/R4B/compartmentdefinition-device.html)
- [Encounter](https://hl7.org/fhir/R4B/compartmentdefinition-encounter.html)
- [Patient](https://hl7.org/fhir/R4B/compartmentdefinition-patient.html)
- [Practitioner](https://hl7.org/fhir/R4B/compartmentdefinition-practitioner.html)
- [RelatedPerson](https://hl7.org/fhir/R4B/compartmentdefinition-relatedperson.html)

For example, for the `Patient` compartment, to determine if an `Observation` is a member, the inclusion criteria search parameters are `subject` and `performer`. If the `Observation` resource fields associated with these search parameters reference a `Patient` resource, the `Observation` resource is a member of that `Patient` compartment.

As of IBM FHIR Server 4.11.0, compartment membership is always evaluated during ingestion, even if the search parameters that define compartment membership have been filtered out in fhir-server-config.json.
However, in cases where the inclusion criteria parameters have been overridden, it is still possible for server config to affect compartment membership.

##  2 Re-index
Reindexing is implemented as a custom operation that tells the IBM FHIR Server to read a set of resources and replace the existing search parameters with those newly extracted from the resource body.

The `$reindex` operation can be invoked via an HTTP(S) POST to `[base]/$reindex`, `[base]/[type]/$reindex`, or `[base]/[type]/[instance]/$reindex`. By default, the operation at the System-level or Type-level selects 10 resources and re-extracts their search parameters values based on the current configuration of the server.

Reindexing is resource-intensive and can take several hours or even days to complete depending on the approach used, the number of resources currently in the system, and the capability of the hosting platform.

### 2.1 Server-side-driven approach
By default, the operation will select 10 resources and re-extract their search parameters values based on the current configuration of the server. The operation supports the following parameters to control the behavior:

|name|type|description|
|----|----|-----------|
|`tstamp`|string|Reindex only resources not previously reindexed since this timestamp. Format as a date YYYY-MM-DD or time YYYY-MM-DDTHH:MM:SSZ.|
|`resourceCount`|integer|The maximum number of resources to reindex in this call. If this number is too large, the processing time might exceed the transaction timeout and fail.|
|`force`|boolean|Force the parameters to be replaced even if the parameter hash matches. This is only required following a schema migration which changes how the parameters are stored in the database.|

The IBM FHIR Server tracks when a resource was last reindexed and only resources with a reindex_tstamp value less than the given tstamp parameter will be processed. When a resource is reindexed, its reindex_tstamp is set to the given tstamp value. In most cases, using the current date (for example "2020-10-27") is the best option for this value.

### 2.2 Client-side-driven approach
Another option is to have the client utilize the `$retrieve-index` and `$reindex` in parallel to drive the processing.

The `$retrieve-index` operation is called to retrieve index IDs of resources available for reindexing. This can be done repeatedly by using the `_count` and `afterIndexId` parameters for pagination. The operation supports the following parameters to control the behavior:

|name|type|description|
|----|----|-----------|
|`_count`|string|The maximum number of index IDs to retrieve. This may not exceed 1000. If not specified, the maximum number retrieved is 1000.|
|`notModifiedAfter`|string|Only retrieve index IDs for resources not last updated after this timestamp. Format as a date YYYY-MM-DD or time YYYY-MM-DDTHH:MM:SSZ.|
|`afterIndexId`|string|Retrieve index IDs starting with the first index ID after this index ID. If this parameter is not specified, the retrieved index IDs start with the first index ID.|

The `$retrieve-index` operation can be invoked via an HTTP(s) POST to `[base]/$retrieve-index` or `[base]/[type]/$retrieve-index`. Invoking this operation at the type-level only retrieves indexIDs for resources of that type.

The `$reindex` operation is called to reindex the resources with index IDs in the specified list. The operation supports the following parameters to control the behavior:

|name|type|description|
|----|----|-----------|
|`indexIds`|string|Reindex only resources with an index ID in the specified list, formatted as a comma-delimited list of strings. If number of index IDs in the list is too large, the processing time might exceed the transaction timeout and fail.|

By specifying the index IDs on the `$reindex` operation, the IBM FHIR Server avoids the database overhead of choosing the next resource to reindex and updating the reindex_tstamp. Though it requires the client side to track the reindex progress, it should allow for an overall faster reindex.

### 2.3 fhir-bucket
To aid in the re-indexing process, the IBM FHIR Server team has expanded the fhir-bucket resource-loading tool to support driving the reindex, with the option of using either the server-side-driven or client-side-driven approach. The fhir-bucket tool uses a thread-pool to make concurrent POST requests to the IBM FHIR Server `$retrieve-index` and `$reindex` custom operations.

For more information on driving the reindex operation from fhir-bucket, see https://github.com/LinuxForHealth/FHIR/tree/main/fhir-bucket#driving-the-reindex-custom-operation.

---
FHIR® is the registered trademark of HL7 and is used with the permission of HL7.
