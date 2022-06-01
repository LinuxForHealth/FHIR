---
layout: post
title:  Conformance
description: Notes on the Conformance of the IBM FHIR Server
date:   2022-05-31
permalink: /conformance/
---

# Conformance to the HL7 FHIR Specification
The IBM FHIR Server aims to be a conformant implementation of the HL7 FHIR specification. However, the FHIR specification is very broad and not all implementations are expected to implement every feature. We prioritize performance and configurability over spec coverage.

## Capability statement
The HL7 FHIR specification defines [an interaction](https://hl7.org/fhir/R4B/http.html#capabilities) for retrieving a machine-readable description of the server's capabilities via the `[base]/metadata` endpoint. The IBM FHIR Server implements this interaction and generates a `CapabilityStatement` resource based on the combination of request headers and server configuration. The `CapabilityStatement` is suited for programatic use, whereas this document provides a human-readable summary with a focus on limitations, extensions, and deviations from the specification.

## FHIR Versions
As of version 5.0.0, the IBM FHIR Server supports all resource types from HL7 FHIR version 4.3.0 (R4B). Because this version of FHIR is almost entirely backwards compatible with version 4.0.1 (R4), we are able to serve as an R4-compliant server for [almost all](#unsupported-R4-resource-types) R4 resource types from the same server endpoints.

Clients can request a specific FHIR version by using the specification-defined [`fhirVersion` MIME-type parameter](https://hl7.org/fhir/R4B/http.html#version-parameter) in their HTTP headers (`Content-Type` and `Accept`). The server will use version 4.0 by default, but can be configured to default to 4.3 instead.

For most resource-level interactions, the behavior is the same in either version.
However, for system-level interactions like whole-system search and whole-system history, extended operations like $export and $retrieve-index, or even just retrieving the server capabilities, the set of resource types included in the response will depend on the fhirVersion of the request.

## FHIR HTTP API
The HL7 FHIR specification is more than just a data format. It defines an [HTTP API](https://hl7.org/fhir/R4B/http.html) for creating, reading, updating, deleting, and searching over FHIR resources.

The IBM FHIR Server implements a linear versioning scheme for resources, fully implements `vread` and `history` interactions, and supports version-aware updates.

By default, the IBM FHIR Server allows all supported API interactions (`create`, `read`, `vread`, `history`, `search`, `update`, `patch`, and `delete`). However, it is possible to configure which of these interactions are allowed on a per-resource-type basis. See the [user guide](https://ibm.github.io/FHIR/guides/FHIRServerUsersGuide#412-fhir-rest-api) for details.

### HTTP Headers
The IBM FHIR Server supports headers to modify the behavior of interactions according to the HL7 FHIR specification [HTTP API summary](https://hl7.org/fhir/R4B/http.html#summary) with the following changes/additions:

| Interaction    | Path         | Verb          | Header                    | Description |
| -------------- | ------------ | ------------- | ------------------------- | ----------- |
| update         | /[type]/[id] | PUT           | If-None-Match             | Custom support for conditional create-on-update   |
| update         | /[type]/[id] | PUT           | X-FHIR-FORCE-UPDATE       | Overrides server optimization for "no-op" updates |
| _any_          | _any_        | _any_         | X-FHIR-TENANT-ID          | Custom support for multi-tenancy                  |
| _any_          | _any_        | _any_         | X-FHIR-DSID               | Custom support for multiple datasources           |

In addition to the content negotiation headers required in the FHIR specification, the IBM FHIR Server supports two client preferences via the `Prefer` header:
* [return preference](https://hl7.org/fhir/R4B/http.html#ops)
* [handling preference](https://hl7.org/fhir/R4B/search.html#errors)

The default return preference is `minimal`.
The default handling preference is configurable via the server's `fhirServer/core/defaultHandling` config property, but defaults to `strict`.
Additionally, server administrators can configure whether or not to honor the client's handling preference by setting `fhirServer/core/allowClientHandlingPref` which defaults to `true`.

For example, to ask the server to be lenient in processing a given request, but to return warnings for non-fatal errors, a client should set the Prefer header as follows:
```
Prefer: return=OperationOutcome, handling=lenient
```

In `lenient` mode, the client must [check the self uri](https://hl7.org/fhir/R4B/search.html#conformance) of a search response to determine which parameters were used in computing the response.

Note: In addition to controlling whether or not the server returns an error for unexpected search parameters, the handling preference is also used to control whether or not the server will return an error for unexpected elements in the JSON representation of a Resource as defined at https://hl7.org/fhir/R4B/json.html.

The IBM FHIR Server supports conditional create-on-update using the `If-None-Match` header. This IBM FHIR Server-specific feature allows clients to use a `PUT` (update) interaction which behaves as follows:

    1. If the resource does not yet exist, create the resource and return `201 Created`;
    2. If the resource does exist, do nothing and return `412 Precondition Failed` (default behavior);
    3. If the resource does exist and the fhir-server-config element `fhirServer/core/ifNoneMatchReturnsNotModified` is set to `true`, do nothing and return `304 Not Modified`.

The only supported value for the If-None-Match header for update interactions is `"*"`. This feature can also be used for `PUT` requests in transaction or batch bundles by specifying the `ifNoneMatch` field similarly in the request element:
```
{
    "resourceType" : "Bundle",
    "id": "1234abc",
    "type" : "transaction",
    "entry" : [ {
        "resource" : {
            "resourceType" : "Patient",
            "id": "17be8b731ac-25b7637c-97a8-468a-b37f-a6ea4d75d122",
            "active" : true,
            "name" : [ {
                "family" : "Lovelace",
                "given" : [ "Ada" ]
            } ],
            "gender" : "female"
        },
        "request" : {
            "method" : "PUT",
            "url" : "Patient/17be8b731ac-25b7637c-97a8-468a-b37f-a6ea4d75d122",
            "ifNoneMatch": "*"
        }
    }
    ]
}
```

If a match is found and the fhir-server-config element `fhirServer/core/ifNoneMatchReturnsNotModified` is not configured or is set to `false`, the condition is treated as an error which will cause transaction bundles to fail, returning a status of `400` (Bad Request). For batch bundles, the entry response status will be `412` (Precondition Failed).

If a match is found and the fhir-server-config element `fhirServer/core/ifNoneMatchReturnsNotModified` is set to `true`, the response bundle entry contains the location of the current resource and a response status of `304` (Not Modified):
```
{
  "resourceType": "Bundle",
  "type": "transaction-response",
  "entry": [
    {
      "response": {
        "id": "17be8b731ac-25b7637c-97a8-468a-b37f-a6ea4d75d122",
        "status": "304",
        "location": "Patient/17be8b731ac-25b7637c-97a8-468a-b37f-a6ea4d75d122/_history/1",
        "etag": "W/\"1\"",
        "lastModified": "2021-10-28T10:11:08.824322Z"
      }
    }
  ]
}
```

The server also implements an optimization for updates that do not change the resource contents. See Section 5.2. Conditional Update of the [Performance Guide](guides/FHIRPerformanceGuide#52-conditional-update) for more information.

Finally, the IBM FHIR Server supports multi-tenancy through custom headers as defined at https://ibm.github.io/FHIR/guides/FHIRServerUsersGuide#49-multi-tenancy. By default, the server will look for a tenantId in a `X-FHIR-TENANT-ID` header and a datastoreId in the `X-FHIR-DSID` header, and use `default` for either one if the headers are not present.

### General parameters
The `_format` parameter is supported and provides a useful mechanism for requesting a specific format (`XML` or `JSON`) in requests made from a browser. In the absence of either an `Accept` header or a `_format` query parameter, the server defaults to `application/fhir+json`.

The `_pretty` parameter is also supported.

## Whole System History
The whole system history interaction can be used to obtain a list of changes (create, update, delete) to resources in the IBM FHIR Server. This may be useful for other systems to reliably track these changes and keep themselves in-sync.

```
    curl -k -u '<username>:<password>' 'https://<host>:<port>/fhir-server/api/v4/_history'
```

The response is a history bundle as described by the [FHIR specification](https://hl7.org/fhir/R4B/http.html#history) with one exception. Whereas the specification requires each entry in the history bundle to contain the full contents of the resource (at least for entries with a `entry.request.method` of PUT or POST), clients can specify the HTTP header value `Prefer: return=minimal` in which case the whole-system history response bundle contains only references to the resources. This allows clients to decide which resources they actually need to read (and can do so in parallel to further improve throughput).

There are three possible traversal paths through the data which are requested using the `_sort` request parameter:

1. `_sort=-_lastUpdated` [Default] Decreasing time - most recent changes first
2. `_sort=_lastUpdated` Increasing time - oldest changes first
3. `_sort=none` Increasing id order - oldest changes first

Option 1 is useful for users interested in finding the most recent changes that have been applied to the server. Option 2 is useful for system-to-system synchronization because it represents the change history of the server. Option 3 is also useful for system-to-system synchronization cases, but guarantees uniqueness across pages when following the `next` links (versus option 2 which may repeat entries at the time window boundary when multiple resources share the same modification timestamp).

The `_since` and `_before` search parameters can be used to specify a time range filter. They are both defined as [instant](https://hl7.org/fhir/R4B/datatypes.html#instant) datatypes which must include a time specified to at least second accuracy and include a timezone.  To return all changes that have occurred since a known point in time, use both `_since` and `_sort=_lastUpdated` query parameters:

```
    curl -k -u '<username>:<password>' 'https://<host>:<port>/fhir-server/api/v4/_history?_since=2021-02-21T00:00:00Z&_sort=_lastUpdated'
```

To return all changes that have occurred before a known point in time, use `_before` and `_sort=-_lastUpdated` query parameters:

```
    curl -k -u '<username>:<password>' 'https://<host>:<port>/fhir-server/api/v4/_history?_before=2021-02-21T00:00:00Z&_sort=-_lastUpdated'
```

To return changes within a known window of time, specify both the `_since` and `_before` query parameters and choose either ascending or descending sort.

When sorting with ascending time, the `_since` parameter in the `next` link is calculated to fetch the next page of resources and the `_before` parameter (if given) remains constant. When sorting with descending time, the `_before` parameter in the `next` link is calculated to fetch the next page of resources and the `_since` parameter (if given) remains constant.

When sorting using ascending or descending time, the `next` link also includes a value for `_changeIdMarker`. This value is used to exclude this resource change record from the next result page. This is required because multiple resources may share the same `_lastUpdated` time, so filters for `_since` and `_before` must always be inclusive which can lead to duplicates across result page boundaries.

By default, the returned bundle will contain up to 100 resources. The number of resources can be increased (up to 1000) using the `_count` parameter. Specifying `_count` values larger than 1000 will return no more than 1000 resources:

```
    curl -k -u '<username>:<password>' 'https://<host>:<port>/fhir-server/api/v4/_history?_count=1000'
```

As mentioned above, to simplify client implementations in system-to-system synchronization scenarios, the client may specify `_sort=none` as follows:

```
    curl -k -u '<username>:<password>' 'https://<host>:<port>/fhir-server/api/v4/_history?_count=100&_sort=none'
```

In this case, the IBM FHIR Server uses `_changeIdMarker` as a custom paging attribute which references a single `Bundle.entry.id` value. This value can be used by clients to checkpoint where they are in the sequence of changes, and ask for only changes that come after the given id. The simplest way to do this is to follow the `next` link returned in the response Bundle. If the next link is not present in the response Bundle, the end has been reached for the current point in time. Note that `_lastUpdated` and `Bundle.entry.id` values are not perfectly correlated - clients should not mix ordering. The ids used for `Bundle.entry.id` are guaranteed to be unique within a single IBM FHIR Server database (tenant/datasource).

The `_changeIdMarker` query parameter is intended to be used only as part of a `next` link defined within a search result. It is not intended for general use by an end user.

### Whole System History - Type Filters
The query parameter `_type` can be used to limit which resource types are returned from a `_history` request. For example, the following request will return only Patient and Observation resources:

```
    curl -k -u '<username>:<password>' 'https://<host>:<port>/fhir-server/api/v4/_history?_count=1000&_type=Patient,Observation'
```

If only a single resource type is required, the `[type]/_history` endpoint can be used:

```
    curl -k -u '<username>:<password>' 'https://<host>:<port>/fhir-server/api/v4/Patient/_history?_count=1000'
```

This form has identical behavior to the whole-system-history endpoint with a single `_type=[type]` value.

### Whole System History - Shared Timestamps
In a highly concurrent system, several resources could share the same timestamp. Also, the internal id used to identify individual resource changes may not correlate perfectly with the `_lastUpdated` time. For example:

| logical-id | version |  time | changeIdMarker |  change-type |
| ---------- | ------- | ----- | --------- | ------------ |
| patient-1  |       1 | 12:00 |         1 | CREATE |
| patient-2  |       1 | 12:05 |         2 | CREATE |
| patient-3  |       1 | 12:05 |         3 | CREATE |
| patient-4  |       1 | 12:04 |         4 | CREATE |
| patient-1  |       2 | 12:06 |         5 | UPDATE |
| patient-3  |       2 | 12:06 |         6 | DELETE |

Note how the change time for `patient-3` and `patient-4` is the same, although they have different change ids. Also, `patient-4` has a change time before `patient-2` even though its id is greater. This can happen if the clocks in a cluster are not perfectly synchronized or the resource was created when processing a large Bundle. This only applies to different resources - changes can _never_ appear out of order for a specific resource because the IBM FHIR Server uses database locking to ensure consistency.

The IBM FHIR Server requires clocks in a cluster to be synchronized and expects a maximum clock drift of no more than 2 seconds.

### Whole System History - The Transaction Timeout Window
Clients must exercise caution when reading recently ingested resources. When processing large bundles in parallel, an id may be assigned by the database but ACID isolation means that the record will not be visible to a reader until the transaction is committed. This could be up to 120s or longer if a larger transaction-timeout property has been defined. If a smaller bundle starts after the larger bundle and its transaction is committed first, its change ids and timestamps will be visible to readers before the resources from the other bundle, which will have some earlier change ids and timestamps. If clients do not take this into account, they may miss some resources. This behavior is a common concern in databases and not specific to the IBM FHIR Server.

To guarantee no data is skipped, clients should not process resources with a `_lastUpdated` timestamp which is after `{current_time} - {transaction_timeout} - {max_cluster_clock_drift}`. By waiting for this time window to close, the client can be sure the data being returned is complete and in order, and can safely checkpoint using the `_since`, `_before` or `_changeIdMarker` values depending on the chosen sort option. The default value for transaction timeout is 120s but this is configurable. A value of 2 seconds is a reasonable default to consider for `max_cluster_clock_drift` in lieu of specific information about the infrastructure. Implementers should check with server administrators on the appropriate values to use.

To simplify the handling of this scenario, clients may specify the optional query parameter `_excludeTransactionTimeoutWindow=true` to perform this filtering within the server. This relies on the IBM FHIR Server transaction timeout having been configured using the `FHIR_TRANSACTION_MANAGER_TIMEOUT` environment variable. If this environment variable is not configured, a default transaction timeout of 120s is assumed, although this may not be the actual timeout if the server is otherwise configured in a non-standard way.

Note that using `_excludeTransactionTimeoutWindow=true` intentionally introduces a delay as to when freshly ingested resources become visible from the `_history` endpoints. When this parameter is used, clients can safely iterate along the resource history timeline using the `next` links in the Bundle response without the risk of missing data.

| | |
| -------------- | ---- |
| **Recommendation**: | To iterate over multiple pages, always use the `next` links in the response Bundle instead of trying to compose the URL and its query parameters each time. |

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
Search parameters defined in the specification can be found by browsing the FHIR specification by resource type. For example, to find the search parameters for the Patient resource, navigate to https://hl7.org/fhir/R4B/patient.html and scroll to the Search Parameters section near the end of the page.

In addition, the following search parameters are supported on all resource types:
* `_id`
* `_lastUpdated`
* `_tag`
* `_profile`
* `_security`
* `_source`
* `_type`
* `_has`

These parameters can be used while searching any single resource type or while searching across resource types (whole system search).

The `_type` parameter may only be used with whole system search.

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
* `_total`

The `_sort`, `_count`, `_summary`, `_elements`, and `_total` parameters may each only be specified once in a search. In `lenient` mode, only the first occurrence of each of these parameters is used; additional occurrences are ignored.

The `_count` parameter can be used to limit the number of resources matching the search criteria to return. The maximum allowed value for `_count` is configured via the `fhirServer/core/maxPageSize` configuration property; see the [user guide](https://ibm.github.io/FHIR/guides/FHIRServerUsersGuide#511-property-descriptions) for details. If the specified value of `_count` exceeds the maximum allowed value, the maximum allowed value will be used instead. Any associated `_include` or `_revinclude` resources are not considered in the `_count` limit.

The `_include` and `_revinclude` parameters can be used to return resources related to the primary search results, in order to reduce the overall network delay of repeated retrievals of related resources. The maximum allowed number of `_include` or `_revinclude` resources returned for a single page of primary search results is configured via the `fhirServer/core/maxPageIncludeCount` configuration property; see the [user guide](https://ibm.github.io/FHIR/guides/FHIRServerUsersGuide#511-property-descriptions) for details. If the number of included resources to be returned exceeds the maximum allowed number, the search will fail. For example, if the `fhirServer/core/maxPageIncludeCount` configuration property value is 1000, and the search result is 1 matching resource plus 1000 included resources, the search will succeed. However, if the `fhirServer/core/maxPageIncludeCount` configuration property value is 1000, and the search result is 1 matching resource plus 1001 included resources, the search will fail. It is possible that an included resource could be referenced by more than one primary search result. Duplicate included resources will be removed before search results are returned, so a resource will not appear in the search results more than once. A resource is considered a duplicate if a primary resource or another included resource with the same logical ID and version already exists in the search results.

The `:iterate` modifier is supported for the `_include` and `_revinclude` parameters. The number of iterations is limited to 1. This means the iteration depth will be limited to one level beyond the depth of the resources being iterated against, whether primary search resources or included resources. One exception to this is the case where an iterative `_include` or `_revinclude` is specified that will return the same resource type as the primary search resource type (for example `.../Patient?_include:iterate=Patient:link:Patient`). In this case, the iteration depth will be limited to a maximum of two levels beyond the primary search resource type.

The `_contained` and `_containedType` parameters are not supported at this time.

### Custom search parameters
Custom search parameters are search parameters that are not defined in the FHIR R4 specification, but are configured for search on the IBM FHIR Server. You can configure custom parameters for either extension elements or for elements that are defined in the specification but without a corresponding search parameter.

For information on how to specify custom search parameters, see [FHIRSearchConfiguration.md](https://ibm.github.io/FHIR/guides/FHIRSearchConfiguration).

### Search modifiers
FHIR search modifiers are described at https://hl7.org/fhir/R4B/search.html#modifiers and vary by search parameter type. The IBM FHIR Server implements a subset of the spec-defined search modifiers that is defined in the following table:

|FHIR Search Parameter Type|Supported Modifiers|"Default" search behavior when no Modifier or Prefix is present|
|--------------------------|-------------------|---------------------------------------------------------------|
|String                    |`:exact`,`:contains`,`:missing`    |"starts with" search that is case-insensitive and accent-insensitive|
|Reference                 |`:[type]`,`:missing`,`:identifier` |exact match search and targets are implicitly added|
|URI                       |`:below`,`:above`,`:missing`       |exact match search|
|Token                     |`:missing`,`:not`,`:of-type`,`:in`,`:not-in`,`:text`,`:above`,`:below`       |exact match search|
|Number                    |`:missing`                         |implicit range search (see https://hl7.org/fhir/R4B/search.html#number)|
|Date                      |`:missing`                         |implicit range search (see https://hl7.org/fhir/R4B/search.html#date)|
|Quantity                  |`:missing`                         |implicit range search (see https://hl7.org/fhir/R4B/search.html#quantity)|
|Composite                 |`:missing`                         |processes each parameter component according to its type|
|Special (near)            | none                              |searches a bounding area according to the value of the `fhirServer/search/useBoundingRadius` property|

Due to performance implications, the `:exact` modifier should be used for String search parameters when possible.

The `:above` and `:below` modifiers for Token search parameters are supported, with the following restrictions:
* The search parameter value must have the form of `[system]|[code]`.
* The referenced code system must exist in the FHIR registry.

The `:in` and `:not-in` modifiers for Token search parameters are supported, with the following restrictions:
* The search parameter value must be an absolute URI that identifies a value set.
* The referenced value set must exist in the FHIR registry and must be expandable.

### Search prefixes
FHIR search prefixes are described at https://hl7.org/fhir/R4B/search.html#prefix.

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

For range targets (parameter values extracted from Range, Date/Period, and DateTime elements without fractional seconds), the prefixes are interpreted as according to https://hl7.org/fhir/R4B/search.html#prefix.

For example, a search like `Observation?date=2018-10-29T12:00:00Z` would *not* match an Observation with an effectivePeriod of `start=2018-10-29` and `end=2018-10-30` because "the search range does not fully contain the range of the target value." Similarly, a search like `range=5||mg` would not match a range value with `low = 1 mg` and `high = 10 mg`. To obtain all range values which contain a specific value, use the `ap` prefix which is defined to match when "the range of the search value overlaps with the range of the target value."

If not specified on a query string, the default prefix is `eq`.

### Searching on Date
The IBM FHIR Server implements date search as according to the specification.

The server supports up to 6 fractional seconds (microsecond granularity) for Instant and DateTime values and all extracted parameter values are stored in the database in UTC in order to improve data portability.

Dates and DateTimes which are expressed without timezones are assumed to be in the local timezone of the application server at the time of parameter extraction.
Similarly, query parameter date values with no timezone are assumed to be in the local timezone of the server at the time the search is invoked.
To ensure consistency of search results, clients are recommended to include the timezone on all search query values that include a time.

Finally, the server extends the specified capabilities with support for "exact match" semantics on fractional seconds.

Query parameter values without fractional seconds are handled as an implicit range. For example, a search like `Observation?date=2019-01-01T12:00:00Z` would return resources with the following effectiveDateTime values:
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
* `[parameter]=[system]|`

However, the `|[code]` variant currently behaves like the `[code]` option, matching code values irrespective of the system instead of matching only on elements with missing/null system values as defined in the spec.

For search parameters of type token that are defined on data fields of type `ContactPoint`, the FHIR server currently uses the `ContactPoint.system` and the `ContactPoint.value` instead of the `ContactPoint.use` field as described in the specification.

For search parameters of type token that are defined on data fields of type `string`, `Coding`, `CodeableConcept`, and `Identifier`, the case-sensitivity of the specified code system is used to determine how resource values are indexed and how the FHIR server performs the search. The following table describes how a resource value is indexed, based on the case-sensitivity of the code system specified in the resource value:

| Code System case-sensitivity setting | How resource value is indexed |
|--------------------------------------|-------------------------------|
|case-sensitive                        |Value is stored unmodified (case-sensitive)|
|not case-sensitive                    |Value is stored as a normalized value (case-insensitive)|
|case sensitivity not specified        |Value is stored as a normalized value (case-insensitive)|
|code system not specified             |Value is stored as a normalized value (case-insensitive)|

For search parameters of type token that are defined on data fields of type `code`, code values which include a system value are always treated as case-sensitive, regardless of the case-sensitivity setting of the associated code system. Code values which do not include a system value are always treated as case-insensitive. This behavior is summarized in the following table:


| Code System                          | How Code value is indexed |
|--------------------------------------|-------------------------------|
|code has system                       |Value is stored unmodified (case-sensitive)|
|code does not have system             |Value is stored as a normalized value (case-insensitive)|

The following table describes how the FHIR server performs a token search, based on the case-sensitivity of the code system specified in the search parameter value:

| Code System case-sensitivity setting | How search is performed |
|--------------------------------------|-------------------------|
|case-sensitive                        |Search is performed to match on unmodified search parameter token value (case-sensitive)|
|not case-sensitive                    |Search is performed to match on normalized search parameter token value (case-insensitive)|
|case sensitivity not specified        |Search is performed to match on normalized search parameter token value (case-insensitive)|
|code system not specified             |Search is performed to match on unmodified search parameter token value OR normalized search parameter value|

### Searching on Number
For fields of type `decimal`, the IBM FHIR Server computes an implicit range when the query parameter value has a prefix of `eq` (the default), `ne`, or `ap`. The computed range is based on the number of significant figures passed in the query string and further information can be found at https://hl7.org/fhir/R4B/search.html#number.
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

There is one exception to the statement above. The `url` search parameter, which is defined in the base FHIR specification on definitional resource types as a URI search parameter, is actually treated as a canonical search parameter of type REFERENCE, as documented in the [FHIR specification](http://hl7.org/fhir/R4B/references.html#canonical). The following section of this document describes how the IBM FHIR Server processes canonical reference searches.

### Searching on Reference
Reference searches on the IBM FHIR Server support search on elements of type Reference and on elements of type canonical.

**Search on Elements of Type Reference:**

Reference searches may be of the following types:

* a relative reference - `1` where it is reflectively determined to be a subset of possible targets, such as `Patient/1`, `Group/1`
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

**Search on Elements of Type Canonical:**

Canonical searches search against elements of type canonical which contain a reference to a canonical URL. The canonical reference is to a resource's defined element `url` which is the URL that always identifies the resource across all contexts of use. The list of resource types that have a canonical URL and are allowed to be the target of a canonical reference are documented in the [FHIR specification](http://hl7.org/fhir/R4B/references.html#canonical). A canonical reference can optionally contain a version, delimited by the `|` character (i.e. `http://example.org/fhir/ValueSet/123|1.0.0`). The version is a reference to a resource’s business version - the indexed `version` element value (not to be confused with a resource’s `meta.versionId` element).

When a resource which contains a canonical element is indexed for search, the reference contained in the canonical element will be parsed into the URL part and the version part (if specified). These values will be stored separately and compared against during canonical searches as described below.

Canonical searches are processed following these rules:
* The search parameter value is a reference to a canonical URL, which may optionally contain a version delimited by the `|` character. If a version is specified, it will be automatically detected and used in the search.
* Searches which do not specify a version will match against all resources whose indexed `url` portion of the reference matches the search parameter value, regardless if the reference contains a version. For example, assume one Measure resource contains a canonical reference of `http://example.org/fhir/Library/abc` in its `library` field, and another Measure resource contains a canonical reference of `http://example.org/fhir/Library/abc|1.0.0` in its `library` field. If a search parameter value of `http://example.org/fhir/Library/abc` is used in a canonical search against the `library` field, both of the Measure resources will be a match because the `url` portion of the reference is a match for both resources, regardless of the `version` portion of the reference.
* Searches which do specify a version will match only against those resources whose indexed `url` portion of the reference and indexed `version` portion of the reference match the `url` and `version` components of the search parameter value. For example, assume the same Measure resources as in the previous example. If a search parameter value of `http://example.org/fhir/Library/abc|2.0.0` is used in a canonical search against the `library` field, neither of the Measure resources will be a match. Although the indexed `url` portion of the reference is a match for both resources, since a version was specified in the search parameter value, the search will also attempt to match the `version` component of the search parameter value with the indexed `version` portion of the references, and neither is a match.
* Canonical search parameters may be used when performing chained, reverse chained (`_has`), `_include`, or `_revinclude` searches. As described above, versioned canonical references act against the business version of a resource, not it’s versionId. The special rules that apply to chaining and include references specifying versions of the form `/_history/xx` do not apply to versioned canonical references. Versions of the form `/_history/xx` are not supported with canonical references.
* The `:above` and `:below` modifiers are not supported with canonical search parameters.
* Canonical references will be resolved via indexed search values. They will not be resolved via registry look-ups.

Notes:
* For search parameter definitions whose FHIRPath expression selects element types of both Reference and Canonical, the search parameter will be treated as a canonical search parameter, and will follow the rules described above for canonical searches.
* For search parameter definitions whose FHIRPath expression selects a choice element, and one of the choice element types is Canonical, the search parameter will be treated as a canonical search parameter only if the choice element is explicitly specified to be of type Canonical (for example, if a reference search parameter is defined over an extension's `value` element, it must contain the `as canonical` clause in order to be treated as a canonical search parameter, i.e. `extension.where(url='http://example.org/canonical-value').value as canonical`.

### Searching on Special Positional Search
Positional Search uses [UCUM units](https://unitsofmeasure.org/ucum.html) of distance measure along with common variants:

| Unit of Measure | Variant |
|-----------------|---------|
|KILOMETERS|km, kms, kilometer, kilometers|
|MILES|mi, mis, mile, miles|
|METERS|m, ms, meter, meters|
|FEET|ft, fts, foot|

Note, the use of the surrounding bracket, such as `[mi_us]` is optional; `mi_us` is also valid.

## Batch/transaction support
The IBM FHIR Server implements the HL7 FHIR [batch/transaction](https://hl7.org/fhir/R4B/http.html#transaction) endpoint. We support the batch/transaction processing rules as defined in the specification and we support resolving and replacing both:
* literal references to [bundle-local identities](https://hl7.org/fhir/R4B/bundle.html#bundle-unique) for creates and updates in batch and transaction bundles; and
* [conditional literal references](https://hl7.org/fhir/R4B/http.html#trules) for creates and updates in a transaction bundle.

One discrepency from the specification is that, during reference replacement, we *do not* replace string matches of the fullUrl within
> elements of type uri, url, oid, uuid, and <a href="" & <img src="" in the narrative

We've opened the following issue on the specification to request this requirement to be relaxed in future versions of the specification:  https://jira.hl7.org/browse/FHIR-36032

## Extended operations
The HL7 FHIR specification also defines a mechanism for extending the base API with [extended operations](https://hl7.org/fhir/R4B/operations.html).
The IBM FHIR Server implements a handful of extended operations and provides extension points for users to extend the server with their own.

Operations are invoked via HTTP POST.
* All operations can be invoked by passing a Parameters resource instance in the body of the request.
* For operations with a single input parameter named "resource", the Parameters wrapper can be omitted.

Alternatively, for operations with only primitive input parameters (i.e. no complex datatypes like 'Identifier' or 'Reference'), operations can be invoked via HTTP GET by passing the parameters in the URL.

Note: operations invoked via POST will ignore all query parameters and operations invoked via GET will ignore any body.

### System operations
System operations are invoked at `[base]/$[operation]`

|Operation|Short Description|Notes|
|---------|-----------------|-----|
| [$convert](https://hl7.org/fhir/R4B/resource-operation-convert.html) | Takes a resource in one form and returns it in another | Converts between JSON and XML but *not* between FHIR versions |
| [$export](https://hl7.org/fhir/uv/bulkdata/STU1/OperationDefinition-export.html) | Export data from the server | exports to an S3-compatible data store; see the [user guide](https://ibm.github.io/FHIR/guides/FHIRServerUsersGuide#4101-bulk-data-export) for config info |
| [$import](https://github.com/smart-on-fhir/bulk-import/blob/main/import.md) | Import FHIR Resources from a source| see the [user guide](https://ibm.github.io/FHIR/guides/FHIRServerUsersGuide#4101-bulk-data-export) for config info. This implementation is based on the proposed operation.|
| [$healthcheck](https://github.com/IBM/FHIR/blob/main/operation/fhir-operation-healthcheck/src/main/resources/healthcheck.json) | Check the health of the server | Checks for a valid connection to the database |
| [$versions](https://hl7.org/fhir/R4B/capabilitystatement-operation-versions.html) | Returns the list of supported FHIR versions | |

### Type operations
Type operations are invoked at `[base]/[resourceType]/$[operation]`

|Operation|Type|Short Description|Notes|
|---------|----|-----------------|-----|
| [$validate](https://hl7.org/fhir/R4B/operation-resource-validate.html) | * | Validate a passed resource instance | Uses fhir-validate |
| [$export](https://hl7.org/fhir/uv/bulkdata/OperationDefinition-patient-export.html) | Patient | Obtain a set of resources pertaining to all patients | exports to an S3-compatible data store; see the [user guide](https://ibm.github.io/FHIR/guides/FHIRServerUsersGuide/#410-bulk-data-operations) for config info |
| [$document](https://hl7.org/fhir/R4B/operation-composition-document.html) | Composition | Generate a document | Prototype-level implementation |
| [$apply](https://hl7.org/fhir/R4B/operation-plandefinition-apply.html) | PlanDefinition | Applies a PlanDefinition to a given context | A prototype implementation that performs naive conversion |
| [$everything](https://hl7.org/fhir/R4B/operation-patient-everything.html) | Patient | Obtain all resources pertaining to a patient | Current implementation supports obtaining all resources for a patient up to an aggregate total of 10,000 resources (at which point it is recommended to use the `$export` operation). This implementation does not currently support using the `_since` and `_count` query parameters. Pagination is not currently supported. If there is no nominated patient and the context is not associated with a single patient record, an error will be returned indicating the patient cannot be determined for the request. The resource types returned can be configured. If a server has restricted down the list of supported resource types, only those resource types will be returned. Use of the `start` and `end` query parameters will not work if the `date` search parameter was filtered out for a resource type in the server configuration file. |

### Instance operations
Instance operations are invoked at `[base]/[resourceType]/[id]/$[operation]`

|Operation|Type|Short Description|Notes|
|---------|----|-----------------|-----|
| [$validate](https://hl7.org/fhir/R4B/operation-resource-validate.html) | * | Validate a resource instance | Uses fhir-validate |
| [$export](https://hl7.org/fhir/uv/bulkdata/OperationDefinition-group-export.html) | Group | Obtain a set resources pertaining to patients in a specific Group | Only supports static membership; does not resolve inclusion/exclusion criteria |
| [$document](https://hl7.org/fhir/R4B/operation-composition-document.html) | Composition | Generate a document | Prototype-level implementation |
| [$apply](https://hl7.org/fhir/R4B/operation-plandefinition-apply.html) | PlanDefinition | Applies a PlanDefinition to a given context | A prototype implementation that performs naive conversion |
| [$everything](https://hl7.org/fhir/R4B/operation-patient-everything.html) | Patient | Obtain all resources pertaining to a patient | Current implementation supports obtaining all resources for a patient up to an aggregate total of 10,000 resources (at which point it is recommended to use the `$export` operation). This implementation does not currently support using the `_since` and `_count` query parameters. Pagination is not currently supported. The resource types returned can be configured. If a server has restricted down the list of supported resource types, only those resource types will be returned. Use of the `start` and `end` query parameters will not work if the `date` search parameter was filtered out for a resource type in the server configuration file. |

## Unsupported R4 resource types
With the introduction of support for HL7 R4B in IBM FHIR Server 5.0.0, we no longer support FHIR 4.0.1 resource types that have been removed or drastically changed in 4.3.0. This set of resource types is discussed at https://hl7.org/fhir/R4B/history.html but repeated here for convenience:
* Evidence
* EvidenceVariable
* MedicinalProductAuthorization
* MedicinalProductContraindication
* MedicinalProductIndication
* MedicinalProductInteraction
* MedicinalproductManufactured
* MedicinalproductPackaged
* MedicinalproductPharmaceutical
* MedicinalproductUndesirableEffect
* SubstanceAmount
* SubstanceNucleicAcid
* SubstancePolymer
* SubstanceProtein
* SubstanceReferenceInformation
* SubstanceSourceMaterial

---

FHIR® is the registered trademark of HL7 and is used with the permission of HL7.
