The FHIR terminology subsystem assumes that artifacts with the same canonical URL and version are stable and thus can be stored in long-living cache space. If there is a need to iterate the content of a CodeSystem or ValueSet resource without updating the business version, additional operations are needed to clear the cache.

If you are in development mode and always want to reload data, the simplest mechanism is to use the `fhirServer/term/disableCaching`  configuration option. If you are in a hybrid or production mode and require both caching and dynamic content updates, the operations in this module can be used to clear specific cached resources.

This module introduces a $clear-cache operation on the ValueSet and CodeSystem resources. Depending on the type of resource being changed, one or the other or both may need to be called in order to completely clear the cache. For instance, an intensional ValueSet that depends on a server CodeSystem resource, will require a call to both ValueSet/$clear-cache and CodeSystem/$clear-cache.

FHIR terminology caching is done on a per-JVM, per-tenant basis, so the clear-cache operations must be called on every relevant tenant of every member of a FHIR server cluster after data changes are made.
