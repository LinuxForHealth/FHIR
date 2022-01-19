# IBM FHIR Server - fhir-persistence-schema

Builds and manages the multi-tenant FHIR R4 RDBMS schema for Db2 and PostgreSQL and includes Derby support for testing.

This module is built into two different jar files. The default jar is included with the IBM FHIR Server web application and is used for bootstrapping Apache Derby databases (if configured). There is also an executable command line interface (cli) version of this jar that packages this module with all of its dependencies.

The executable command line interface (cli) version of this module can be downloaded from the project [Releases tab](https://github.com/IBM/FHIR/releases).

The following guides contain detailed descriptions on usage, design and the multi-tenant variant used with Db2:

* [Schema Deployment and Upgrade Guide](https://github.com/IBM/FHIR/tree/master/fhir-persistence-schema/docs/SchemaToolUsageGuide.md)
* [Schema Design](https://github.com/IBM/FHIR/tree/master/fhir-persistence-schema/docs/SchemaDesign.md)
* [Db2 Multi-tenancy](https://github.com/IBM/FHIR/tree/master/fhir-persistence-schema/docs/DB2MultiTenancy.md)

FHIR® is the registered trademark of HL7 and is used with the permission of HL7.
