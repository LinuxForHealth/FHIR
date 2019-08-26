# Conformance to the HL7 FHIR Specification
The Watson Health&trade; FHIR Server aims to be a conformant implementation of the HL7 FHIR specification, version 1.0.2 (DSTU2). However, the FHIR specification is sprawling, with a wide range in terms of maturity level. For this reason, day-to-day development is led by the priorities of our services and solutions, so we prioritize performance and configurability over spec coverage.

This section aims to document the spec coverage and highlight areas of the specification that the Watson Health FHIR Server doesn't implement.

CODE_REMOVED

## Conformance statement
The HL7 FHIR specification defines [an interaction](https://www.hl7.org/fhir/DSTU2/http.html#conformance) for retrieving the machine-readable definition of a server's conformance statement via the `[base]/api/v1/metadata` endpoint. The Watson Health FHIR Server implements this interaction and generates a `Conformance` resource based on the current server configuration. While the `Conformance` resource is ideal for certain uses, this document aims to provide a more human-readable summary of the key conformance details, with a special focus on deviations from the specification and limitations of the current implementation.

## FHIR HTTP API
The HL7 FHIR specification is more than just a content standard.  It defines an [HTTP API](https://www.hl7.org/fhir/DSTU2/http.html) for creating, updating, deleting, and searching FHIR resources.  The Watson Health FHIR Server implements the full API for every resource defined in the specification, with the following exceptions:
* history is only supported at the resource instance level (no resource type history and no whole-system history)
* there are parts of the FHIR search specification which are not fully implemented as documented in the following section

## Search
The Watson Health FHIR Server supports search parameters of type `Number`, `Date/DateTime`, `String`, `Token`, `Reference`, `Quantity`, and `URI`.

Search parameters of type [Composite](https://www.hl7.org/fhir/dstu2/search.html#composite) are not currently supported.

The Watson Health FHIR Server supports all [non-Composite] search parameters that are defined by the base specification and allows for the configuration of additional ones.

### Base specification search parameters
The search parameters that are defined by the base specification can be found by browsing the DSTU2 FHIR specification by resource type. For example, to find search parameters for a Patient resource, go to https://www.hl7.org/fhir/DSTU2/patient.html and scroll to the Search Parameters section later in the page.

In addition, the following search parameters are supported on all resources:
* `_id`
* `_lastUpdated`
* `_tag`
* `_profile`
* `_security`

The `_text`, `_content`, `_list`, and `_query` parameters are not supported at this time.

Finally, the specification defines a set of <q>Search result parameters</q> for controlling the search behavior. The Watson Health FHIR Server supports the following:
* `_sort`
* `_count`
* `_include`
* `_revinclude`
* `_elements`

The `_count` parameter can be used to return at most 1000 records. If the client specifies a `_count` of > 1000, the page size is capped at 1000.  If the client specifies a `_count` of 1000 or less, the server honors the client request.

The `_summary`, `_contained`, and `_containedType` parameters are not supported at this time.

### Custom search parameters
Custom search parameters are search parameters that are not defined in the base DSTU2 specification, but that are configured for search on the FHIR server. You can configure custom parameters for extension elements, or for
elements that are defined in the base specification, but that are not configured to be searchable.

For information on how to specify custom search parameters, see [Section 3.5 Search parameters](#35-search-parameters) of the FHIR Server User Guide.

### Search modifiers
FHIR search modifiers are described at https://www.hl7.org/fhir/dstu2/search.html#modifiers and vary by search parameter type. The Watson Health FHIR Server implements a subset of the spec-defined search modifiers that is defined in the following table:

|FHIR Search Parameter Type|Supported Modifiers|"Default" search behavior when no Modifier is present|
|--------------------------|-------------------|-----------------------------------------------------|
|String                 |`:exact`,`:contains`,`:missing` |Performs a "starts with" search that is case-insensitive and accent-insensitive|
|Reference              |`:[type]`,`:missing`            |Performs an exact match search|
|URI                    |`:below`,`:missing`             |Performs a "starts with" search|
|Token                  |`:below`,`:not`,`:missing`      |Performs an exact match search|
|Number                 |`:missing`                      |Honors prefix if present, otherwise performs an exact match search|
|Date                   |`:missing`                      |Honors prefix if present, otherwise performs an exact match search|
|Quantity               |`:missing`                      |Honors prefix if present, otherwise performs an exact match search|

Note that the default Watson Health FHIR Server behavior for URI search parameters differs from the behavior defined at https://www.hl7.org/fhir/DSTU2/search.html#uri.

At present, modifiers cannot be used with chained parameters. For example, a search with query string like `subject:Basic.date:missing` will result in an `OperationOutcome` explaining that the search parameter could not be processed.

The `:text` modifier is not supported in this version of the FHIR server and use of this modifier will results in an HTTP 400 error with an `OperationOutcome` that describes the failure.

Due to performance implications, the `:exact` modifier should be used for String searches where possible.

### Search prefixes
FHIR search prefixes are described at https://www.hl7.org/fhir/DSTU2/search.html#prefix.

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

For explicit range targets (parameter values extracted from Period and Range elements), the prefixes are interpreted as according to https://www.hl7.org/fhir/DSTU2/search.html#prefix.

For example, a search like `Observation?date=2018-10-29T12:00:00Z` would *not* match an Observation with an effectivePeriod of `start=2018-10-29` and `end=2018-10-30` because "the search range does not fully contain the range of the target value." Similarly, a search like `range=5||mg` would not match a range value with `low=1 mg` and `high= 10 mg`. To obtain all range values (Period or Range) which contain a specific value, please use the "ap" prefix which is defined to match when "the range of the search value overlaps with the range of the target value."

For other target values (parameter values extracted from elements that are not a Period or Range), "ap" is treated like "eq", "eb" is treated like "lt", and "sa" is treated like "gt". Please note that this "ap" behavior differs from the behavior recommended in the specification.

In the absence of a prefix, the "default" behavior for number, date, and quantity searches is an exact match search.

The `eb` and `ap` prefixes are not supported for searches which target values of type integer (or derived types).

If not specified on a query string, the default prefix is `eq`.

### Searching on Date
The FHIR server adheres to the specification except in cases where a time is included in the search query value. When a time is specified, the implementation requires an hour, minute, second, and timezone value. Including these values is consistent with the way in which `instant` and `dateTime` data types are defined at https://www.hl7.org/fhir/DSTU2/datatypes.html#primitive. However, the implementation differs from the description at https://www.hl7.org/fhir/DSTU2/search.html#date, which allows clients to include hours and minutes, but to omit values for seconds and time zone.

Indexing of fields of type `Timing` is not well-defined in the specification and is not supported in this version of the Watson Health FHIR Server.

### Searching on Token
For search parameters of type token, resource values are not indexed unless the resource instance contains both a `system` **and** `code`. The server implements all three variations of token search that are defined in version DSTU2 of the specification:
* `[parameter]=[code]`
* `[parameter]=[system]|[code]`
* `[parameter]=|[code]`

However, the `|[code]` variant currently behaves like the `[code]` option, matching code values irrespective of the system instead of matching only on elements with missing/null system values as defined in the spec.

Note: later versions of the FHIR specification include a variation of token search which allows you to search a token value by codesystem, irrespective of the value. This is not supported in the current version of the Watson Health FHIR Server.

For search parameters of type token that are defined on data fields of type `ContactPoint`, the FHIR server currently uses the `ContactPoint.system` and the `ContactPoint.value` instead of the `ContactPoint.use` field as described in the specification.

Searching string values via a token search parameter is not currently supported.

### Searching on Number
For fields of type `decimal`, the Watson Health FHIR Server does not compute an implicit range for search. Search query values must match to the same precision, or greater precision, as the value in the resource.

### Searching on Quantity
Quantity elements are not indexed unless they include either a valid `system` **and** `code` for their unit **or** a human-readable `unit` field. Quantities that don't include a `value` element are also skipped.

The FHIR server does not perform any unit conversion or unit manipulation at this time. Quantity values **must** be searched using the same unit `code` that is included in the original resource. If, and only if, a coded unit is not present on a resource, then the FHIR server indexes the human-readable `unit` field, which can then be searched by omitting the `system` in the search query.

The Watson Health FHIR Server does not consider the `Quantity.comparator` field as part of search processing at this time.
Similar to Numeric searches, the FHIR Server does not compute an implicit range for quantity values...the precise number given in the resource is required for retrieval via standard search (i.e. searches with either no prefix or the `eq` prefix).

### Searching on URI
URI searches on the Watson Health FHIR Server are case-insensitive, similar to the default behavior of searching on string values.

## HL7 FHIR DSTU2 (v1.0.2) errata
The Watson Health FHIR Server includes a Java-based object model that is based on the artifacts provided with the HL7 FHIR specification. In general, we seek to avoid modification to these core artifacts, but in a few places this can lead to minor issues:

1. The values of Patient.gender are not constrained to an XML enumeration and therefore are not validated accordingly by the XML schemas provided with the specification.
2. Observation constraint `obs-7` cannot be validated due to an error in the corresponding schematron rule provided with the specification.
3. The XML schema provided with the FHIR DSTU2 spec allows for dateTime values that include a time, but do not explicity include a timezone offset.
4. The specification defines the Encounter-length search parameter as the <q>Length of encounter in days</q> but the actual field can use any duration unit. Searching by this parameter will query for whatever value is in the resource(s) with no unit conversion.
5. The spec-defined search parameter XPath expressions don't always match the verbiage in the specification. In such cases (`DocumentReference-identifier`, `Procedure-patient`, `Observation-patient`, and `Observation-value-date`), we favor the specification text and have overwritten the search parameter definition's XPath.
6.  The spec-provided XPath for the `Patient-deceased` search parameter will return a result of type dateTime when the `deceasedDateTime` value is set (instead of `deceasedBoolean`). However, because the search parameter is of type `token`, this value will not be indexed.
