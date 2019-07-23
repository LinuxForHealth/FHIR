# fhir-persistence-schema

Builds and manages the multi-tenant FHIR R4 RDBMS schema (DB2). Includes Derby support for use in unit-tests.


# Run Configurations

## Grant privileges to data access user
--prop-file fhir.properties
--schema-name FHIRDATA
--grant-to FHIRSERVER

## Add a new tenant (e.g. TNT1)
--prop-file config/fhir.properties
--schema-name FHIRDATA
--allocate-tenant TNT1


## Test a tenant
--prop-file fhir-apikey.properties
--schema-name FHIRDATA
--test-tenant TNT1
--tenant-key "<a-base-64-tenant-key"

## Update the data (not admin) procedures only
--prop-file config/fhir.properties
--schema-name FHIRDATA
--update-proc
