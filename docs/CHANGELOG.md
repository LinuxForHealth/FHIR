## [Unreleased]

### Fixed
* Nothing yet

### Changed
* Nothing yet

## [2.2.1] - 2019-5-17

### Fixed
* NPE when specifying `_elements` on Resource query
* `_elements` doesn't support comma-separated list of values
* NPE on searches involving partially-incomplete Timing or ContactPoint values
* FHIRJsonParser support for primitive elements with extensions
* transaction-response bundles return entries with empty responses
  * now the server will include all required fields with extensions that explain why the data is missing
* Bundle request entry urls are never unencoded
  * query parameter values should now be percent-encoded (e.g. to distinguish between `&` in a value (`%26`) and `&` as a separator)

### Changed
* !BREAKING! Renamed FHIRUtil.read() and FHIRJsonParser.parse() methods which support `_elements` filter to readAndFilter / parseAndFilter respectively
* FHIRJsonParser is now configurable; users can set fhirServer/core/jsonParserLenient and fhirServer/core/jsonParserValidating in fhir-server-config.json
* !BREAKING! FHIRJsonParser is now strict (jsonParserLenient=false) and validating (jsonParserValidating=true) by default
  * for backwards-compatibility, users should set jsonParserLenient=true and jsonParserValidating=false in fhir-server-config.json
* !BREAKING! Unrecognized parameters passed during a search operation will now be ignored by the server (instead of returning a 400 error)
  * clients should check the self link of the searchset bundle response to determine which parameters were used to service the request
  * to opt into the old behavior, clients can include a Prefer header with a value of `handling=strict`
* when a tenant has no tenant-specific extension-search-parameters.xml, we now fall back to the "default" tenant's extension-search-parameters.xml
* search now supports "eb" (ends before), "sa" (starts after), and "ap" (approximate) prefixes for date, number, and quantity search parameters
  * for non-explicit range targets, "ap" is treated like "eq", "eb" is treated like "lt", and "sa" is treated like "gt"
* !BREAKING! for explicit range targets (Period and Range), "eq" searches now match when "the range of the search value fully contains the range of the target value"
  * for a search on a specific value within a range to match, please use the "ap" prefix (or "lt" or "gt")
* SearchParameter xpath values can (and should) now be given to the valueX element, not just to the parent extension
  * for backwards-compatibility, we will continue to support xpath values which point to the extension element, but this is now considered deprecated
* search improvements for values of type Period, Timing, and ContactPoint
* validation now executes schematron constaints on contained resources and entry resources within a bundle
  * this is accomplished by loading a single file with all constraints on the first request, so we recommend hitting the $validate endpoint (or triggering validation via a create operation) in order to warm the cache before serving client requests
* you can now match values containing `,`, `|`, and `$` by prefixing those characters with a `\` in your search query
  * !BREAKING! to search for a value containing `\` itself, be sure to use `\\` instead
* new configuration parameters added to support toggling pretty-print
* server now responds with pretty-print off by default for both XML and JSON
* custom operations can now be invoked from batch/transaction bundle requests
  * search-all (`GET /_search`) and `POST [resource]/_search` with query string parameters can now be included in batch requests as well
* upgrade to WebSphere Liberty 19.0.0.4, IBM Java 8.0-5.35, and db2jcc4 4.25.1301
* updated README

### Added
* Packaging for use on IBM Cloud, including
  * $healthcheck operation for testing the db connection
  * fhir-server is now built into a docker image that is available from Artifactory (see fhir-server README)

## [2.2.0] - 2018-10-15
Release 2.2.0 is the first <q>common service</q> release from the `IBM Server for HL7 FHIR` codebase.

### Security
* client should perform hostname verification by default
* improve security of default configuration
* addressed appscan-identified issues including
  * input validation of tenantId and datastoreId values provided via header
  * output validation while logging user-provided data
  * improve error handling

### Fixed
* race condition in add_resource stored procedure
* server error on incomplete identifier/code/quantity fields
* broken DocumentReference-identifier search
* JDBCNormalizedQueryBuilder assumes chained parameters are of type string

### Changed
* support configurable file-based audit logging
* expand stored procedure parametertype arrays
* upgrade to Liberty 18.0.0.2 and update features
* !BREAKING! - use FHIROperationException as base exception and add severity and issueType
* avoid duplicate read operations during update operation
* moved user guide to docs

### Added
* high-performance FHIRJsonGenerator for serializing FHIR
* search parser support for `_include` and `_revinclude`
* JDBC persistence layer support for `_include` and `_revinclude` search parameters
* support the `_elements` search parameter
* $healthcheck operation which tests connection to the db

* Update to OpenAPI 3.0 and remove fhir-swagger-ui project
