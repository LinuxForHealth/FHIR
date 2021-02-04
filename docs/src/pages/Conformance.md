---
layout: post
title:  Conformance
description: Notes on the Conformance of the IBM FHIR Server
date:   2021-02-04 12:00:00 -0400
permalink: /conformance/
---

# Conformance to the HL7 FHIR Specification
The IBM FHIR Server aims to be a conformant implementation of the HL7 FHIR specification, version 4.0.1 (R4). However, the FHIR specification is very broad and not all implementations are expected to implement every feature. We prioritize performance and configurability over spec coverage.

## Capability statement
The HL7 FHIR specification defines [an interaction](https://www.hl7.org/fhir/R4/http.html#capabilities) for retrieving a machine-readable description of the server's capabilities via the `[base]/metadata` endpoint. The IBM FHIR Server implements this interaction and generates a `CapabilityStatement` resource based on the current server configuration. While the `CapabilityStatement` resource is ideal for certain uses, this markdown document provides a human-readable summary of important details, with a special focus on limitations of the current implementation and deviations from the specification.

The IBM FHIR Server supports only version 4.0.1 of the specification and ignores the optional MIME-type parameter `fhirVersion`.

## FHIR HTTP API
The HL7 FHIR specification is more than just a data format. It defines an [HTTP API](https://www.hl7.org/fhir/R4/http.html) for creating, reading, updating, deleting, and searching over FHIR resources. The IBM FHIR Server implements almost the full API for every resource defined in the specification, with the following exceptions:
* history is only supported at the resource instance level (no resource type history and no whole-system history)
* there are parts of the FHIR search specification which are not fully implemented as documented in the following section

The IBM FHIR Server implements a linear versioning scheme for resources and fully implements the `vread` and `history` interactions, as well as version-aware updates.

By default, the IBM FHIR Server allows all supported API interactions (`create`, `read`, `vread`, `history`, `search`, `update`, `patch`, `delete`). However, it is possible to configure which of these interactions are allowed on a per resource basis through a set of interaction rules. See the [user guide](https://ibm.github.io/FHIR/guides/FHIRServerUsersGuide#412-fhir-rest-api) for details.

### Extended operations
The HL7 FHIR specification also defines a mechanism for extending the base API with [extended operations](https://www.hl7.org/fhir/R4/operations.html).
The IBM FHIR Server implements a handful of extended operations and provides extension points for users to extend the server with their own.

Operations are invoked via HTTP POST.
* All operations can be invoked by passing a Parameters resource instance in the body of the request.
* For operations with no input parameters, no body is required.
* For operations with a single input parameter named "resource", the Parameters wrapper can be omitted.

Alternatively, for operations with only simple input parameters (i.e. no complex datatypes like 'Identifier' or 'Reference'), operations can be invoked via HTTP GET by passing the parameters in the URL.

#### System operations
System operations are invoked at `[base]/$[operation]`

|Operation|Short Description|Notes|
|---------|-----------------|-----|
| [$convert](https://hl7.org/fhir/R4/resource-operation-convert.html) | Takes a resource in one form and returns it in another | Converts between JSON and XML but *not* between FHIR versions |
| [$export](https://hl7.org/fhir/uv/bulkdata/STU1/OperationDefinition-export.html) | Export data from the server | exports to an S3-compatible data store; see the [user guide](https://ibm.github.io/FHIR/guides/FHIRServerUsersGuide#4101-bulk-data-export) for config info |
| [$import](https://github.com/smart-on-fhir/bulk-import/blob/master/import.md) | Import FHIR Resources from a source| see the [user guide](https://ibm.github.io/FHIR/guides/FHIRServerUsersGuide#4101-bulk-data-export) for config info. This implementation is based on the proposed operation.|
| [$healthcheck](https://github.com/IBM/FHIR/blob/master/operation/fhir-operation-healthcheck/src/main/resources/healthcheck.json) | Check the health of the server | Checks for a valid connection to the database |

#### Type operations
Type operations are invoked at `[base]/[resourceType]/$[operation]`

|Operation|Type|Short Description|Notes|
|---------|----|-----------------|-----|
| [$validate](https://hl7.org/fhir/R4/operation-resource-validate.html) | * | Validate a passed resource instance | Uses fhir-validate |
| [$export](https://hl7.org/fhir/uv/bulkdata/OperationDefinition-patient-export.html) | Patient | Obtain a set of resources pertaining to all patients | exports to an S3-compatible data store; see the [user guide](https://ibm.github.io/FHIR/guides/FHIRServerUsersGuide/#410-bulk-data-operations) for config info |
| [$document](https://hl7.org/fhir/R4/operation-composition-document.html) | Composition | Generate a document | Prototype-level implementation |
| [$apply](https://hl7.org/fhir/R4/operation-plandefinition-apply.html) | PlanDefinition | Applies a PlanDefinition to a given context | A prototype implementation that performs naive conversion |

#### Instance operations
Instance operations are invoked at `[base]/[resourceType]/[id]/$[operation]`

|Operation|Type|Short Description|Notes|
|---------|----|-----------------|-----|
| [$validate](https://hl7.org/fhir/R4/operation-resource-validate.html) | * | Validate a resource instance | Uses fhir-validate |
| [$export](https://hl7.org/fhir/uv/bulkdata/OperationDefinition-group-export.html) | Group | Obtain a set resources pertaining to patients in a specific Group | Only supports static membership; does not resolve inclusion/exclusion criteria |
| [$document](https://hl7.org/fhir/R4/operation-composition-document.html) | Composition | Generate a document | Prototype-level implementation |
| [$apply](https://hl7.org/fhir/R4/operation-plandefinition-apply.html) | PlanDefinition | Applies a PlanDefinition to a given context | A prototype implementation that performs naive conversion |

### HTTP Headers
In addition to the content negotiation headers required in the FHIR specification, the IBM FHIR Server supports two client preferences via the `Prefer` header:
* [return preference](https://www.hl7.org/fhir/http.html#ops)
* [handling preference](https://www.hl7.org/fhir/search.html#errors)

The default return preference is `minimal`.
The default handling preference is configurable via the server's `fhirServer/core/defaultHandling` config property, but defaults to `strict`.
Additionally, server administrators can configure whether or not to honor the client's handling preference by setting `fhirServer/core/allowClientHandlingPref` which defaults to `true`.
For example, to ask the server to be lenient in processing a given request, but to return warnings for non-fatal errors, a client should set the Prefer header as follows:
```
Prefer: return=OperationOutcome; handling=lenient
```

In `lenient` mode, the client must [check the self uri](https://www.hl7.org/fhir/search.html#conformance) of a search response to determine which parameters were used in computing the response.

Note: In addition to controlling whether or not the server returns an error for unexpected search parameters, the handling preference is also used to control whether or not the server will return an error for unexpected elements in the JSON representation of a Resource as defined at https://www.hl7.org/fhir/json.html.

Finally, the IBM FHIR Server supports multi-tenancy through custom headers as defined at https://ibm.github.io/FHIR/guides/FHIRServerUsersGuide#49-multi-tenancy. By default, the server will look for a tenantId in a `X-FHIR-TENANT-ID` header and a datastoreId in the `X-FHIR-DSID` header, and use `default` for either one if the headers are not present.

### General parameters
The `_format` parameter is supported and provides a useful mechanism for requesting a specific format (`XML` or `JSON`) in requests made from a browser. In the absence of either an `Accept` header or a `_format` query parameter, the server defaults to `application/fhir+json`.

The `_pretty` parameter is also supported.

## Search
The IBM FHIR Server supports all search parameter types defined in the specification:
* `Number`
* `Date/DateTime`
* `String`
* `Token`
* `Reference`
* `Composite`
* `Quantity`
* `URI`
* `Special` (Location-near)

### Search parameters
Search parameters defined in the specification can be found by browsing the R4 FHIR specification by resource type. For example, to find the search parameters for the Patient resource, navigate to https://www.hl7.org/fhir/R4/patient.html and scroll to the Search Parameters section near the end of the page.

In addition, the following search parameters are supported on all resources:
* `_id`
* `_lastUpdated`
* `_tag`
* `_profile`
* `_security`
* `_source`
* `_type`
* `_has`

These parameters can be used while searching any single resource type or while searching across resource types (whole system search).

The `_type` parameter has two restrictions:
* It may only be used with whole system search.
* It may only be specified once in a search.

The `_has` parameter has two restrictions:
* It cannot be used with whole system search.
* The search parameter specified at the end of its chain cannot be a search result parameter.

The `_text`, `_content`, `_list`, `_query`, and `_filter` parameters are not supported at this time.

Finally, the specification defines a set of "Search result parameters" for controlling the search behavior. The IBM FHIR Server supports the following:
* `_sort`
* `_count`
* `_include`
* `_revinclude`
* `_summary`
* `_elements`

The `_sort`, `_count`, `_summary`, and `_elements` parameters may each only be specified once in a search.

The `_count` parameter can be used to request up to 1000 resources matching the search criteria. An attempt to exceed this `_count` limit will not be honored and returned resources will be capped at 1000. Any associated `_include` or `_revinclude` resources are not considered in the `_count` limit.

The `_include` and `_revinclude` parameters can be used to return resources related to the primary search results, in order to reduce the overall network delay of repeated retrievals of related resources. The number of `_include` or `_revinclude` resources returned for a single page of primary search results will be limited to 1000. If the number of included resources to be returned exceeds 1000, the search will fail. For example, if the primary search result is one resource and the number of included resources is 1000, the search will succeed. However, if the primary search result is one resource and the number of included resources is 1001, the search will fail. It is possible that an included resource could be referenced by more than one primary search result. Duplicate included resources will be removed before search results are returned, so a resource will not appear in the search results more than once. A resource is considered a duplicate if a primary resource or another included resource with the same logical ID and version already exists in the search results. 

The `:iterate` modifier is not supported for the `_include` parameter (or any other).

The `:missing` modifier is not supported for whole-system search.

The `_total`, `_contained`, and `_containedType` parameters are not supported at this time.

### Custom search parameters
Custom search parameters are search parameters that are not defined in the FHIR R4 specification, but are configured for search on the IBM FHIR Server. You can configure custom parameters for either extension elements or for elements that are defined in the specification but without a corresponding search parameter.

For information on how to specify custom search parameters, see [FHIRSearchConfiguration.md](https://ibm.github.io/FHIR/guides/FHIRSearchConfiguration).

### Search modifiers
FHIR search modifiers are described at https://www.hl7.org/fhir/R4/search.html#modifiers and vary by search parameter type. The IBM FHIR Server implements a subset of the spec-defined search modifiers that is defined in the following table:

|FHIR Search Parameter Type|Supported Modifiers|"Default" search behavior when no Modifier or Prefix is present|
|--------------------------|-------------------|---------------------------------------------------------------|
|String                    |`:exact`,`:contains`,`:missing` |"starts with" search that is case-insensitive and accent-insensitive|
|Reference                 |`:[type]`,`:missing`            |exact match search and targets are implicitly added|
|URI                       |`:below`,`:above`,`:missing`    |exact match search|
|Token                     |`:missing`                      |exact match search|
|Number                    |`:missing`                      |implicit range search (see http://hl7.org/fhir/R4/search.html#number)|
|Date                      |`:missing`                      |implicit range search (see https://www.hl7.org/fhir/search.html#date)|
|Quantity                  |`:missing`                      |implicit range search (see http://hl7.org/fhir/R4/search.html#quantity)|
|Composite                 |`:missing`                      |processes each parameter component according to its type|
|Special (near)            | none                           |searches a bounding area according to the value of the `fhirServer/search/useBoundingRadius` property|

Due to performance implications, the `:exact` modifier should be used for String searches where possible.

At present, the `:missing` modifier is not supported for whole-system search nor for chained parameter search. For example, a search with query string like `subject:Basic.date:missing` will result in an `OperationOutcome` explaining that the search parameter could not be processed.

The :text, :not, :above, :below, :in, :not-in, and :of-type modifiers are not supported in this version of the IBM FHIR server and use of this modifier will results in an HTTP 400 error with an OperationOutcome that describes the failure.

### Search prefixes
FHIR search prefixes are described at https://www.hl7.org/fhir/R4/search.html#prefix.

As defined in the specification, the following prefixes are supported for Number, Date, and Quantity search parameters:
* `eq`
* `ne`
* `gt`
* `lt`
* `gt`
* `le`
* `sa`
* `eb`
* `ap`

For range targets (parameter values extracted from Range, Date/Period, and DateTime elements without fractional seconds), the prefixes are interpreted as according to https://www.hl7.org/fhir/R4/search.html#prefix.

For example, a search like `Observation?date=2018-10-29T12:00:00Z` would *not* match an Observation with an effectivePeriod of `start=2018-10-29` and `end=2018-10-30` because "the search range does not fully contain the range of the target value." Similarly, a search like `range=5||mg` would not match a range value with `low = 1 mg` and `high = 10 mg`. To obtain all range values which contain a specific value, use the `ap` prefix which is defined to match when "the range of the search value overlaps with the range of the target value."

If not specified on a query string, the default prefix is `eq`.

### Searching on Date
The IBM FHIR Server implements date search as according to the specification.

The server supports up to 6 fractional seconds (microsecond granularity) for Instant and DateTime values and all extracted parameter values are stored in the database in UTC in order to improve data portability.

Dates and DateTimes which are expressed without timezones are assumed to be in the local timezone of the application server at the time of parameter extraction.
Similarly, query parameter date values with no timezone are assumed to be in the local timezone of the server at the time the search is invoked.
To ensure consistency of search results, clients are recommended to include the timezone on all search query values that include a time.

Finally, the server extends the specified capabilities with support for "exact match" semantics on fractional seconds.

Query parameter values without fractional seconds are handled as an implicit range. For example, a search like `Observatoin?date=2019-01-01T12:00:00Z` would return resources with the following effectiveDateTime values:
* 2019-01-01T12:00:00Z
* 2019-01-01T12:00:00.1Z
* 2019-01-01T12:00:00.999999Z

Query parameter values with fractional seconds are handled with exact match semantics (ignoring precision). For example, a search like `Patient?birthdate=2019-01-01T12:00:00.1Z` would include resources with the following effectiveDateTime values:
* 2019-01-01T12:00:00.1Z
* 2019-01-01T12:00:00.100Z
* 2019-01-01T12:00:00.100000Z

Indexing fields of type `Timing` is not well-defined in the specification and is not supported in this version of the IBM FHIR Server.

### Searching on Token
For search parameters of type token, resource values are not indexed unless the resource instance contains both a `system` **and** `code`. The server implements the following variations of token search defined in the specification:
* `[parameter]=[code]`
* `[parameter]=[system]|[code]`
* `[parameter]=|[code]`

However, the `|[code]` variant currently behaves like the `[code]` option, matching code values irrespective of the system instead of matching only on elements with missing/null system values as defined in the spec.

The IBM FHIR Server does not yet support searching a token value by codesystem, irrespective of the value (`|[system]|`).

For search parameters of type token that are defined on data fields of type `ContactPoint`, the FHIR server currently uses the `ContactPoint.system` and the `ContactPoint.value` instead of the `ContactPoint.use` field as described in the specification.

Searching string values via a token search parameter is not currently supported.

### Searching on Number
For fields of type `decimal`, the IBM FHIR Server computes an implicit range when the query parameter value has a prefix of `eq` (the default), `ne`, or `ap`. The computed range is based on the number of significant figures passed in the query string and further information can be found at https://www.hl7.org/fhir/R4/search.html#number.
For searches with the `ap` prefix, we use the range `[implicitLowerBound - searchQueryValue * .1, implicitUpperBound + searchQueryValue * .1)` to ensure that the `ap` range is broader than the implicit range of `eq`.

### Searching on Quantity
Quantity elements are not indexed unless they include either a valid `system` **and** `code` for their unit **or** a human-readable `unit` field.
If a Quantity element contains both a coded unit **and** a display unit, then both will be indexed. Quantities that don't include a `value` element are also skipped.

The FHIR server does not perform any unit conversion or unit manipulation at this time. Quantity values should be searched using the same unit `code` that is included in the original resource.

Similar to Numeric searches, the FHIR Server computes an implicit range for search query values with no range prefix (e.g. `eq`, `ne`, `ap`) based on the number of significant figures passed in the query string.
For searches with the `ap` prefix, we use the range `[implicitLowerBound - searchQueryValue * .1, implicitUpperBound + searchQueryValue * .1)` to ensure that the `ap` range is broader than the implicit range of `eq`.

The IBM FHIR Server does not consider the `Quantity.comparator` field as part of search processing at this time.

### Searching on URI
URI searches on the IBM FHIR Server are case-sensitive with "exact-match" semantics. The `above` and `below` prefixes can be used to perform path-based matching that is based on the `/` delimiter.

### Searching on Reference
Reference searches on the IBM FHIR Server support search on:

* a relative reference - `1` where it is reflexsively determined to be a subset of possible targets, such as `Patient/1`, `Group/1`
* a logical reference - `Patient/1` where it is explicitly set
* uri searches - where it is explicitly searched using a URI on the server, such as `reference=http://example.org/fhir/Patient/123`

We recommend using logical reference where possible.

Elements of type Reference may contain a versioned reference, such as `Patient/123/_history/2`. When performing chained, reverse chained (`_has`), `_include`, or `_revinclude` searches on versioned references, the following rules apply:

* **Chained search**: If a resource has a reference that is versioned, and a chained search is performed using the element containing the versioned reference, the search criteria will be evaluated against the current version of the referenced resource, regardless of the version specified.
    * This is because the IBM FHIR Server only stores search index information for the current versions of resources. In the case where a chained search does not act on the referenced version of a resource, the search results will contain an `OperationOutcome` with a warning that indicates the logical id of the resource and the element containing the versioned reference.
    * Example: A `Condition` resource contains a reference of `Patient/123/_history/1` in its `subject` element, and the current version of the `Patient/123` resource is 2, and a search of `Condition?subject.name=Jane` is performed. In this case, the search criteria will be evaluated against the current version of the `Patient/123` resource rather than the specified version of `1`.
* **Reverse chained search**: If a resource has a reference that is versioned, and a reverse chain search is performed using the element containing the versioned reference, then the referenced resource can only be returned as a match if the version specified is the referenced resource's current version.
    * This is because the IBM FHIR Server will only return the current version of `match` resources in search results.
    * Example: A `Condition` resource contains a reference of `Patient/123/_history/2` in its `subject` element, and the current version of the `Patient/123` resource is `2`, and a search of `Patient?_has:Condition:patient:code=1234-5` is performed. If the `Condition` resource meets the search criteria, then the `Patient/123` resource will be returned as a match since the version specified in the reference is the current version of the `Patient/123` resource. However, if the current version of the `Patient/123` resource happens to be `3`, then the `Condition` resource will not be returned as a match in the search results.
* **Include search**: If a resource has a reference that is versioned, and an `_include` search is performed using the element containing the versioned reference, then the referenced resource with the specified version will be returned as an `include` resource in the search results, assuming the version is valid.
    * Example: A `Condition` resource contains a reference of `Patient/123/_history/1` in its `subject` element, and the current version of the `Patient/123` resource is `2`, and a search of `Condition?_include=Condition:subject` is performed. Version `1` of the `Patient/123` resource will be returned as an `include` resource in the search results.
* **Revinclude search**: If a resource has a reference that is versioned, and a `_revinclude` search is performed using the element containing the versioned reference, then the resource containing the versioned reference is returned as an `include` resource only if the version specified in the reference is the referenced resource's current version.
    * This is because the IBM FHIR Server will only return the current version of `match` resources in search results. A reference to a non-current version of the resource is not considered to have met the search criteria, thus the resource containing the reference is not considered a valid `include` resource.
    * Example: A `Condition` resource contains a reference of `Patient/123/_history/2` in its `subject` element, and the current version of the `Patient/123` resource is `2`, and a search of `Patient?_revinclude=Condition:subject` is performed. The `Condition` resource will be returned as an `include` resource since the version of the `Patient` resource specified in the `subject` element is the current version of the `Patient` resource. If the current version of the `Patient/123` resource is `3`, then the `Condition` resource will not be returned as an `include` resource in the search results.

### Searching on Special Positional Search
Positional Search uses [UCUM units](https://unitsofmeasure.org/ucum.html) of distance measure along with common variants:

| Unit of Measure | Variant |
|-----------------|---------|
|KILOMETERS|km, kms, kilometer, kilometers|
|MILES|mi, mis, mile, miles|
|METERS|m, ms, meter, meters|
|FEET|ft, fts, foot|

Note, the use of the surrounding bracket, such as `[mi_us]` is optional; `mi_us` is also valid.

## HL7 FHIR R4 (v4.0.1) errata
We add information here as we find issues with the artifacts provided with this version of the specification.

FHIR® is the registered trademark of HL7 and is used with the permission of HL7.
