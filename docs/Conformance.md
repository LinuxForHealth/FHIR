# PLEASE USE the files in src/pages/ as it is part of the build now

# Conformance to the HL7 FHIR Specification
The IBM FHIR Server aims to be a conformant implementation of the HL7 FHIR specification, version 4.0.0 (R4). However, the FHIR specification is very broad and not all implementations are expected to implement every feature. We prioritize performance and configurability over spec coverage.

## Capability statement
The HL7 FHIR specification defines [an interaction](https://www.hl7.org/fhir/R4/http.html#capabilities) for retrieving a machine-readable description of the server's capabilities via the `[base]/metadata` endpoint. The IBM FHIR Server implements this interaction and generates a `CapabilityStatement` resource based on the current server configuration. While the `CapabilityStatement` resource is ideal for certain uses, this markdown document provides a human-readable summary of important details, with a special focus on limitations of the current implementation and deviations from the specification.

The IBM FHIR Server supports only version 4.0.0 of the specification and presently has no support for the MIME-type parameter `fhirVersion`.

## FHIR HTTP API
The HL7 FHIR specification is more than just a data format. It defines an [HTTP API](https://www.hl7.org/fhir/R4/http.html) for creating, reading, updating, deleting, and searching over FHIR resources. The IBM FHIR Server implements almost the full API for every resource defined in the specification, with the following exceptions:
* history is only supported at the resource instance level (no resource type history and no whole-system history)
* there are parts of the FHIR search specification which are not fully implemented as documented in the following section

The IBM FHIR Server implements a linear versioning scheme for resources and fully implements the `vread` and `history` interactions, as well as version-aware updates.

### General parameters
The `_format` parameter is supported and provides a useful mechanism for requesting a specific format (`XML` or `JSON`) in requests made from a browser. In the absence of either an `Accept` header or a `_format` query parameter, the server defaults to `application/fhir+json`.

The `_pretty` parameter is not currently supported, but should be added as part of https://github.com/IBM/FHIR/issues/269.

The `_summary` and `_elements` parameters are supported on the search interaction as documented.

## Search
The IBM FHIR Server supports search parameters of type `Number`, `Date/DateTime`, `String`, `Token`, `Reference`, `Quantity`, and `URI`.

Search parameters of type [Composite](https://www.hl7.org/fhir/R4/search.html#composite) and [Special](https://www.hl7.org/fhir/R4/search.html#special) are not currently supported.

For all other types, the IBM FHIR Server supports the parameters defined in the
specification and allows for the configuration of additional ones.

### Search parameters
Search parameters defined in the specification can be found by browsing the R4 FHIR specification by resource type. For example, to find the search parameters for the Patient resource, navigate to https://www.hl7.org/fhir/R4/patient.html and scroll to the Search Parameters section near the end of the page.

In addition, the following search parameters are supported on all resources:
* `_id`
* `_lastUpdated`
* `_tag`
* `_profile`
* `_security`
* `_source`

The `_text`, `_content`, `_list`, `_has`, `_type`, `_query`, and `_filter` parameters are not supported at this time.

Finally, the specification defines a set of <q>Search result parameters</q> for controlling the search behavior. The IBM FHIR Server supports the following:
* `_sort`
* `_count`
* `_include`
* `_revinclude`
* `_summary`
* `_elements`

The `_count` parameter can be used to return at most 1000 records. If the client specifies a `_count` of over 1000, the page size is capped at 1000. If the client specifies a `_count` of 1000 or less, the server honors the client request.

The `:iterate` modifier is not supported for the `_include` parameter (or any other).

The `_total`, `_contained`, and `_containedType` parameters are not supported at this time.

### Custom search parameters
Custom search parameters are search parameters that are not defined in the FHIR R4 specification, but are configured for search on the IBM FHIR Server. You can configure custom parameters for either extension elements or for elements that are defined in the specification but without a corresponding search parameter.

For information on how to specify custom search parameters, see [FHIRSearchConfiguration.md](FHIRSearchConfiguration.md).

### Search modifiers
FHIR search modifiers are described at https://www.hl7.org/fhir/R4/search.html#modifiers and vary by search parameter type. The IBM FHIR Server implements a subset of the spec-defined search modifiers that is defined in the following table:

|FHIR Search Parameter Type|Supported Modifiers|"Default" search behavior when no Modifier is present|
|--------------------------|-------------------|-----------------------------------------------------|
|String                 |`:exact`,`:contains`,`:missing` |Performs a "starts with" search that is case-insensitive and accent-insensitive|
|Reference              |`:[type]`,`:missing`            |Performs an exact match search|
|URI                    |`:below`,`:missing`             |Performs a "starts with" search (issue #273)|
|Token                  |`:below`,`:not`,`:missing`      |Performs an exact match search|
|Number                 |`:missing`                      |Honors prefix if present, otherwise performs an exact match search|
|Date                   |`:missing`                      |Honors prefix if present, otherwise performs an exact match search|
|Quantity               |`:missing`                      |Honors prefix if present, otherwise performs an exact match search|

Note that the default IBM FHIR Server behavior for URI search parameters differs from the behavior defined at https://www.hl7.org/fhir/R4/search.html#uri.

At present, modifiers cannot be used with chained parameters. For example, a search with query string like `subject:Basic.date:missing` will result in an `OperationOutcome` explaining that the search parameter could not be processed.

The `:text` modifier is not supported in this version of the FHIR server and use of this modifier will results in an HTTP 400 error with an `OperationOutcome` that describes the failure.

Due to performance implications, the `:exact` modifier should be used for String searches where possible.

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

For explicit range targets (parameter values extracted from Period and Range elements), the prefixes are interpreted as according to https://www.hl7.org/fhir/R4/search.html#prefix.

For example, a search like `Observation?date=2018-10-29T12:00:00Z` would *not* match an Observation with an effectivePeriod of `start=2018-10-29` and `end=2018-10-30` because "the search range does not fully contain the range of the target value." Similarly, a search like `range=5||mg` would not match a range value with `low = 1 mg` and `high = 10 mg`. To obtain all range values (Period or Range) which contain a specific value, please use the `ap` prefix which is defined to match when "the range of the search value overlaps with the range of the target value."

For other target values (parameter values extracted from elements that are not a Period or Range), `ap` is treated like `eq`, `eb` is treated like `lt`, and `sa` is treated like `gt`. Please note that this `ap` behavior differs from the behavior recommended in the specification.

In the absence of a prefix, the default behavior for number, date, and quantity searches is an exact match search.

The `eb` and `ap` prefixes are not supported for searches which target values of type integer (or derived types).

If not specified on a query string, the default prefix is `eq`.

### Searching on Date
The FHIR server adheres to the specification except in cases where a time is included in the search query value. When a time is specified, the implementation requires an hour, minute, second, and timezone value. Including these values is consistent with the way in which `instant` and `dateTime` data types are defined at https://www.hl7.org/fhir/R4/datatypes.html#primitive. However, the implementation differs from the description at https://www.hl7.org/fhir/R4/search.html#date, which allows clients to include hours and minutes, but to omit values for seconds and time zone.

The IBM FHIR Server stores up to 6 fractional seconds (microsecond granularity) for Instant and DateTime values (when present) and allows clients to search with these as well.

Query parameter values without fractional seconds will be handled as an implicit range. For example, a search like `Patient?date=2019-01-01T12:00:00Z` would include resources with the following effectiveDateTime values:
* 2019-01-01T12:00:00Z
* 2019-01-01T12:00:00.1Z
* 2019-01-01T12:00:00.999999Z

Query parameter values with fractional seconds will be handled with exact match semantics (ignoring precision). For example, a search like `Patient?date=2019-01-01T12:00:00.100Z` would include resources with the following effectiveDateTime values:
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
For fields of type `decimal`, the IBM FHIR Server does not compute an implicit range for search. Search query values must match to the same precision, or greater precision, as the value in the resource.

### Searching on Quantity
Quantity elements are not indexed unless they include either a valid `system` **and** `code` for their unit **or** a human-readable `unit` field. Quantities that don't include a `value` element are also skipped.

The FHIR server does not perform any unit conversion or unit manipulation at this time. Quantity values **must** be searched using the same unit `code` that is included in the original resource. If, and only if, a coded unit is not present on a resource, then the FHIR server indexes the human-readable `unit` field, which can then be searched by omitting the `system` in the search query.

The IBM FHIR Server does not consider the `Quantity.comparator` field as part of search processing at this time.
Similar to Numeric searches, the FHIR Server does not compute an implicit range for quantity values...the precise number given in the resource is required for retrieval via standard search (i.e. searches with either no prefix or the `eq` prefix).

### Searching on URI
URI searches on the IBM FHIR Server are case-insensitive, similar to the default behavior of searching on string values.

## HL7 FHIR R4 (v4.0.0) errata
We will add information here as we find issues with the artifacts provided with this version of the specification.

FHIR® is the registered trademark of HL7 and is used with the permission of HL7.
