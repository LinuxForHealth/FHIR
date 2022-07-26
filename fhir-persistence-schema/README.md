# LinuxForHealth FHIR Server - fhir-persistence-schema

Builds and manages the LinuxForHealth FHIR R4/R4B RDBMS schema for PostgreSQL and includes Derby support for testing.

This module is built into two different jar files. The default jar is included with the LinuxForHealth FHIR Server web application and is used for bootstrapping Apache Derby databases (if configured). There is also an executable command line interface (cli) version of this jar that packages this module with all of its dependencies.

The executable command line interface (cli) version of this module can be downloaded from the project [Releases tab](https://github.com/LinuxForHealth/FHIR/releases).

The following guides contain detailed descriptions on usage and schema design:

* [Schema Deployment and Upgrade Guide](https://github.com/LinuxForHealth/FHIR/tree/main/fhir-persistence-schema/docs/SchemaToolUsageGuide.md)
* [Schema Design](https://github.com/LinuxForHealth/FHIR/tree/main/fhir-persistence-schema/docs/SchemaDesign.md)

---------
## TL;DR

1. Create a properties file containing your database connection information:

```
$ cat fhirdb.properties
db.host=localhost
db.port=5432
db.database=fhirdb
user=postgres
password=change-password
```

2. Run the schema tool CLI to create the target schema (for example `fhirdata`). Skip this step if a DBA has already created a schema for you.

``` shell
java -jar ./fhir-persistence-schema-${VERSION}-cli.jar \
  --db-type postgresql \
  --prop-file fhirdb.properties \
  --schema-name fhirdata \
  --create-schemas
```

Note: Replace `${VERSION}` with the version of the jar you're using or use the wildcard `*` to match any version.

3. Run the schema tool CLI again to create the tables and indexes in the `fhirdata` schema. We recommend following the least-privilege access model, so the LinuxForHealth FHIR Server should connect using a non-admin user. Use the `--grant-to` option to grant the correct privileges to the non-admin user created for the LinuxForHealth FHIR Server (the user `fhirserver` in the following example):

``` shell
java -jar ./fhir-persistence-schema-${VERSION}-cli.jar \
  --db-type postgresql \
  --prop-file fhirdb.properties \
  --schema-name fhirdata \
  --update-schema \
  --grant-to fhirserver \
  --pool-size 1
```


4. To upgrade the schema for a new release, run the schema tool CLI again. The tool handles version tracking and will apply all the necessary changes to roll forward to the latest version:

``` shell
java -jar ./fhir-persistence-schema-${VERSION}-cli.jar \
  --db-type postgresql \
  --prop-file fhirdb.properties \
  --schema-name fhirdata \
  --update-schema \
  --grant-to fhirserver \
  --pool-size 1
```

For details on configuring TLS and using other databases and options, read the full [Schema Deployment and Upgrade Guide](https://github.com/LinuxForHealth/FHIR/tree/main/fhir-persistence-schema/docs/SchemaToolUsageGuide.md).

FHIR® is the registered trademark of HL7 and is used with the permission of HL7.
