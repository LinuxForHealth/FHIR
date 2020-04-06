# The IBM FHIR Server - Db2 Schema Migration and Schema Management

This document outlines the migration and management of changes in the IBM FHIR Server schema. These cover the supporting Java code and the supporting SQL. There are three core projects which are referenced: 

| Project | Description |
|----------|---------------------------------------------|
| `fhir-model` | The HL7 FHIR model - generated Java code |
| `fhir-database-utils` | The SQL/database constructs used to create SQL compliant statements |
| `fhir-persistence-schema` | Uses database-utils to define a physical data model for storing FHIR resources and supporting the FHIR API |

The schema generates the following object types that require management: 

- DB2 PACKAGE
- TABLESPACES
- SCHEMA 
- GLOBAL VARIABLE 
- INDEX 
- PERMISSION 
- PROCEDURE 
- SEQUENCE 
- TABLE
- TABLE CONSTRAINT 

----------------------------------------------------------------
# Schema: FHIR_ADMIN

The Schema FHIR_ADMIN is an administrative schema.

## Managing TABLESPACES

There are multiple TABLESPACES used. For the administrative tablespaces, the `FHIR_TS` tablespace is created with automatic storage.  The tablespace is managed automatically with a default EXTENTSIZE of 4.

The `FHIR_TS` tablespace is created one time and should not be changed.

## Managing SEQUENCES

The FHIR_ADMIN schema has a single sequence - `TENANT_SEQUENCE`. 
The sequence is created a single time, and is NOT replaced or upgraded at any point in time. 

The `fhir-persistence-schema` actions - `Allocate Tenant - AddTenantDAO` and `Add Tenant - AddTenantKeyDAO` depend on `next value for`.  If the sequence is removed, the `TENANT_SEQUENCE` must be manually recreated starting with an integer higher than the `TENANTS.MT_ID` and `TENANT_KEYS.TENANT_KEY_ID` otherwise a conflict with the PRIMARY_KEY in the `FHIR_ADMIN` tables occurs. 

The variable privileges (grants) are updated one time, and applied to `FHIRUSER` group.  If you recreate the sequence, you must recreate the `USE` permission. 

## Managing GLOBAL VARIABLE 

The `FHIR_ADMIN.SV_TENANT_ID` is the only variable in the FHIR_ADMIN schema.  The variable is set in `FHIRSchemaGenerator.addVariable`, and should not be updated or removed. 

The variable privileges (grants) are updated one time, and applied to `FHIRUSER` group. 

## Managing TABLE
There are three administrative tables in `FHIR_ADMIN`. These table definitions are more completely described in [DB2MultiTenancy.md](DB2MultiTenancy.md). 

**Table: VERSION_HISTORY**

Before any table or schema object is created, the `VERSION_HISTORY` table is created using `CreateVersionHistory.createTableIfNeeded(adminSchemaName, adapter);`  This table includes VERSION_HISTORY for resources created in the schema. If an object in the schema is updated, then the VERSION_HISTORY table must be updated to track the changes. 

The `VERSION_HISTORY` table enables Tenant version isolation, so upgrades and patches are separately applied to the schema to which the tenant belongs. Specifically, if the tenant shares the table definitions with another client, the patching is applied to both tenants at the same time.  If the tenant has a specific schema, the table definitions may be updated independent of each other.

The `CreateVersionHistory` class controls the creation the `VERSION_HISTORY` table. Importantly, the VERISON_HISTORY table does not support migrations and changes. The Primary Key has a corresponding index `PK_VERSION_HISTORY`, and the code does not support updating the index. 

This table is an administrative table and should not require updating and migration. 

**Table: TENANTS**

The `FHIR_ADMIN.TENANTS` table maps a given tenant to the Multi-Tenant ID (MT_ID) for the given tenant.  The MT_ID is subsequently used to restrict row access based on the matching value. 

`AdminSchemaGenerator.addTenantTable` method adds the TENANT table, and updates to this table definition requries a change to the setVersion (incrementing by 1). A corresponding change must be made to `FHIRSchemaGenerator.addTenantTable` method.

**Table: TENANT_KEYS**

The Tenant Keys table stores a hashed version of the tenant specific key. Upon creating a session and calling the SET_TENANT procedure, the SET_TENANT queries the incoming key against the TENANT_KEYS database to map to a specific tenant MT_ID. 

`AdminSchemaGenerator.addTenantKeysTable` method adds the TENANT_KEYS table, and updates to this table definition requries a change to the setVersion (incrementing by 1).

``` java
this.tenantKeysTable = Table.builder(adminSchemaName, TENANT_KEYS
    .setVersion(2)
    .addIntColumn(        TENANT_KEY_ID,             false)
    .addIntColumn(                MT_ID,             false)
    .addVarcharColumn(      TENANT_SALT,        44,  false)
    .addVarbinaryColumn(    TENANT_HASH,        32,  false)
    .addUniqueIndex(IDX + "TENANT_KEY_SALT", TENANT_SALT)
    .addUniqueIndex(IDX + "TENANT_KEY_TIDH", MT_ID, TENANT_HASH)
    .addPrimaryKey("TENANT_KEY_PK", TENANT_KEY_ID)
    .addForeignKeyConstraint(FK + TENANT_KEYS + "_TNID", adminSchemaName, TENANTS, MT_ID)
    .setTablespace(fhirTablespace)
    .build(model);
```

Changes to the FHIR_ADMIN schema are not supported.

If a table in FHIR_ADMIN table is updated, the changes must be manually applied as the migration steps to the each table and their indices. The Java code must also be updated to indicate the version change, such as the following for `TENANTS`:

``` java
this.tenantsTable = Table.builder(adminSchemaName, TENANTS)
    .setVersion(1)
    .addIntColumn(            MT_ID,             false)
    .addVarcharColumn(      TENANT_NAME,        36,  false)
    .addVarcharColumn(    TENANT_STATUS,        16,  false)
    .addUniqueIndex(IDX + "TENANT_TN", TENANT_NAME)
    .addPrimaryKey("TENANT_PK", MT_ID)
    .setTablespace(fhirTablespace)
    .build(model);
```

If the UniqueIndex or ForeignKeyConstraint is removed, and the constraint has been previously applied to a database, the active schema must be updated, and the object must be dropped.

## Managing PROCEDURE

`FHIR_ADMIN.set_tenant` is created in the FHIR_ADMIN schema. If updates are made to the `set_tenant.sql`, the `FhirSchemaGenerator.buildAdminSchema`'s FhirSchemaConstants.INITIAL_VERSION must be updated to the next highest integer (in this case 2). 

``` java
// The set_tenant procedure can be created after all the admin tables are done
ProcedureDef setTenant = model.addProcedure(this.adminSchemaName, 
    SET_TENANT, 
    FhirSchemaConstants.INITIAL_VERSION, 
    () -> SchemaGeneratorUtil.readTemplate(adminSchemaName, adminSchemaName, SET_TENANT.toLowerCase() + ".sql", null), 
    Arrays.asList(allAdminTablesComplete), 
    procedurePrivileges);
        setTenant.addTag(SCHEMA_GROUP_TAG, ADMIN_GROUP);
```

When the `fhir-persistence-schema` updateProcedure is executed, the READ grant is applied again, and the procedure is updated. The Procedure privilege is reset upon re-executing the action. 

If you change the stored procedure signature, you MUST drop the stored procedure before applying the stored procedure to the database. 

----------------------------------------------------------------

# Schema: Tenant Data 

The schema is based on the resource types in `FHIRResourceType.ValueSet`.  Each resource has a set of supporting tables for FHIR Search Parameters. 

> ![mt-table.png](mt-table.png)

## Managing the TABLESPACE

Each tenant receives a tenant-specific tablespace. The tablespace is managed automatically with a default block storage size ([EXTENTSIZE](https://www.ibm.com/support/knowledgecenter/SSEPGG_11.5.0/com.ibm.db2.luw.admin.dbobj.doc/doc/c0004964.html)) of 128. Note, The extentsize is multiplied by the pagesize to arrive at a storage block.

The schema is created in the multi-tenant schema  and subsequently allocated using `fhir-persistence-schema`.

Each tenant is allocated a partition based on the MT_ID and assigned to a tablespace. 

The tenant `tablespace` is created one time and no changes to the tablespace are expected. 

## Managing the SEQUENCES

Each tenant has a two tenant specific sequences. These sequences are created a single time, and are NOT replaced or upgraded at any point in time. 

| Sequence Name | Description |
|----------|---------------------------------------------|
| `fhir_sequence` | The `LOGICAL_ID` in the database, used for storing each FHIR Resource |
| `fhir_ref_sequence` | The reference sequence is a logical id for Parameter Names, Code Systems and Resource Types tables |

Note, the `LOGICAL_ID` is used to assign an internal database ID.  The LOGICAL_ID is different from the FHIR `Resource.id`.  

The stored procedures `add_resource_type`, `add_parameter_name`, `add_code_system`, and `add_any_resource` use the value to uniquely store the resource or supporting data element. 

Do not remove any of the tenant sequences; these sequences must provide unique, non-cycled integer values for the persistence layer to work properly. 

## Managing TABLE 

Each resource has a RESOURCE and LOGICAL RESOURCE table. `_LOGICAL_RESOURCES` has one record per logical resource.  `_RESOURCES` has one record for each version of each logical resource. 

For example, VISIONPRESCRIPTION has: 

```
VISIONPRESCRIPTION_RESOURCES
VISIONPRESCRIPTION_LOGICAL_RESOURCES
```

The resource tables store the FHIR resource as a compressed blob and so changes to the specification or extensions should only impact Search values.

The data definition has several reference tables to support FHIR Search for each resource type. Each of the following table types are defined in the `FHIRResourceTableGroup.java`. 

| Search Parameter Types | Search Value Table | Description |
|------------------------|--------------------|-------------|
| [Number](https://www.hl7.org/fhir/r4/search.html#number) | **`<RESOURCE>`_NUMBER_VALUES** | Numerical Search |
| [Date/DateTime](https://www.hl7.org/fhir/r4/search.html#date) | **`<RESOURCE>`_DATE_VALUES** | Date Search |
| [String](https://www.hl7.org/fhir/r4/search.html#string)<br> [Uri](https://www.hl7.org/fhir/r4/search.html#uri)<br> [Reference](https://www.hl7.org/fhir/r4/search.html#reference)| **`<RESOURCE>`_STR_VALUES** | String, URI, and Reference Search |
| [Token](https://www.hl7.org/fhir/r4/search.html#token) | **`<RESOURCE>`_TOKEN_VALUES** | Token Search |
| [Composite](https://www.hl7.org/fhir/r4/search.html#composite) | **`<RESOURCE>`_COMPOSITES** | Composite Search |
| [Quantity](https://www.hl7.org/fhir/r4/search.html#quantity) | **`<RESOURCE>`_QUANTITY_VALUES** | Quantity Search |
| [Positional](https://www.hl7.org/fhir/r4/location.html#positional) | **`<RESOURCE>`_LATLNG_VALUES** | Positional (near) Search |

For instance, for VISIONPRESCRIPTION there are: 

```
VISIONPRESCRIPTION_NUMBER_VALUES
VISIONPRESCRIPTION_DATE_VALUES
VISIONPRESCRIPTION_STR_VALUES
VISIONPRESCRIPTION_TOKEN_VALUES
VISIONPRESCRIPTION_COMPOSITES
VISIONPRESCRIPTION_QUANTITY_VALUES
VISIONPRESCRIPTION_LATLNG_VALUES
```

There are also additional tables to support search: `LOGICAL_RESOURCE`, `RESOURCE_TYPES` and `PARAMETER_NAMES` These tables are partitioned, and are specific for each tenant. 

The tables have various indices - PrimaryKey, Index, and UniqueIndex. These indices are created as part of the Java object - Table.

To modify a table definition:
1. Increment the version of the table (`setVersion`)
2. Add migration steps to move from any previous version of the Table to this version

For example, for the `STRING_VALUES` table:

``` java
// Parameters are tied to the logical resource
Table tbl = Table.builder(schemaName, tableName)
    .setVersion(2)
    .addTag(FhirSchemaTags.RESOURCE_TYPE, prefix)
    .setTenantColumnName(MT_ID)
    .addBigIntColumn(             ROW_ID,      false)
    .addIntColumn(     PARAMETER_NAME_ID,      false)
    .addVarcharColumn(         STR_VALUE, msb,  true)
    .addVarcharColumn(   STR_VALUE_LCASE, msb,  true)
    .addBigIntColumn(LOGICAL_RESOURCE_ID,      false)
    .addIndex(IDX + tableName + "_PSR", PARAMETER_NAME_ID, STR_VALUE, LOGICAL_RESOURCE_ID)
    .addIndex(IDX + tableName + "_PLR", PARAMETER_NAME_ID, STR_VALUE_LCASE, LOGICAL_RESOURCE_ID)
    .addIndex(IDX + tableName + "_RPS", LOGICAL_RESOURCE_ID, PARAMETER_NAME_ID, STR_VALUE)
    .addIndex(IDX + tableName + "_RPL", LOGICAL_RESOURCE_ID, PARAMETER_NAME_ID, STR_VALUE_LCASE)
    .addPrimaryKey(PK + tableName, ROW_ID)
    .setIdentityColumn(ROW_ID, Generated.BY_DEFAULT)
    .addForeignKeyConstraint(FK + tableName + "_PNID", schemaName, PARAMETER_NAMES, PARAMETER_NAME_ID)
    .addForeignKeyConstraint(FK + tableName + "_RID", schemaName, logicalResourcesTable, LOGICAL_RESOURCE_ID)
    .setTablespace(fhirTablespace)
    .addPrivileges(resourceTablePrivileges)
    .enableAccessControl(this.sessionVariable)
    .addMigration(priorVersion -> {
            List<IDatabaseStatement> statements = new ArrayList<>();
                if (priorVersion == 1) {
                    // Add statements here
                }
                return statements;
            })
    .build(model);
```

When the schema is applied for the first time, it creates the table (and related constructs) as according to the definition.
When the schema is applied to an existing database, the framework checks the `FHIR_ADMIN.VERSION_HISTORY` table and, 
if the version in the table is less than the version being applied, the framework invokes the Migrations *instead*
of calling CREATE with the table definition. **Care** should be taken to ensure that the migrated schema matches a freshly applied schema.
See [Testing migrations](#testing-migrations) for information on verifying the fidelity of the schema migrations.

NOTE: In Db2, certain alter table statements require a table REORG before the table becomes usable again. Additionally, the REORG
may commit the current "unit of work" which can prevent the "all or nothing" semantics of the migration. For these reasons, its recommended
to:
1. avoid destructive changes like dropping columns;
2. backup the database before invoking a migration; and
3. perform the migration offline

In this way, if a new resource is added to the specification, the schema utility automatically provisions the corresponding table on the next execution of the fhir-persistence-schema `--update-schema` action. 

Common reasons to modify the Resource tables are: 
- **Specification Change (Version-to-Version changes)**
    - The Resources are saved as Blobs in the database, and are transparent to the FHIR Version changes. The changes from version-to-version are resilient to field add-remove changes, type changes, and resources additions.  This migration must be done manually.
    - Resource removals must be done manually. 
- **Column Attribute Changes (Space, Value Type)**
    - As the column definitions change, a specific alter statement must be executed on the table, and constraints must be changed or relaxed. This migration must be done manually.
- **Index - Add or Remove or Update**
    - As indices are removed from the table definition, the removed indices must be manually dropped for each Resource table. 
    - As indices are added to the table definition, the version of the table must be updated, and the index must be applied and updated manually.
- **Constraint Updates** If there are Foreign Key updates, the changes must be applied manually and reflected in the code base. 
- **Search Parameter Changes (Specification and Tenant)**
    - If the the parameter type or code is changed, the PARAMETERS_NAME and the corresponding table must be updated to remove references to the removed parameter (based on `SearchParameter.code`). 
    - If there is a new or altered SearchParameter `code` added to the server, the SearchParameter values are only changed if/when the resource is updated. 
    - If a `SearchParameter.code` is removed, the corresponding parameter remains until the resource is reprocessed.  The code to parameter mapping remains in `PARAMETER_NAMES` table until manually removed. 

Each of these tables has row-level permissions based on the conditional READ-only global variable `SV_TENANT_ID`. For example, for AUDITEVENT_COMPOSITES:

``` sql
CREATE PERMISSION FHIRDATA.AUDITEVENT_COMPOSITES_TENANT
    ON FHIRDATA.AUDITEVENT_COMPOSITES FOR ROWS
    WHERE FHIRDATA.AUDITEVENT_COMPOSITES.MT_ID = FHIR_ADMIN.SV_TENANT_ID 
    ENFORCED FOR ALL ACCESS ENABLE ;
```

For security reasons, these permissions should not be removed, migrated, or altered. However, they are automatically REPLACED by the framework after applying migration steps to a given table (required for Db2).

## Managing Stored Procedures

In the tenant's schema, there are four stored procedures `add_code_system`, `add_parameter_name`, `add_resource_type`, and `add_any_resource` which are created.

If updates are made to the `add_code_system.sql`, the `FhirSchemaGenerator.buildSchema`'s FhirSchemaConstants.INITIAL_VERSION must be updated to the next highest integer (in this case 2). 

``` java
ProcedureDef pd = model.addProcedure(this.schemaName, 
    ADD_CODE_SYSTEM, 
    FhirSchemaConstants.INITIAL_VERSION, 
    () -> SchemaGeneratorUtil.readTemplate(adminSchemaName, schemaName, ADD_CODE_SYSTEM.toLowerCase() + ".sql", null), 
    Arrays.asList(fhirSequence, codeSystemsTable, allTablesComplete), 
    procedurePrivileges);
```

When the `fhir-persistence-schema` actions are executed with `--grant-to`, the **INSERT**, **SELECT**, **UPDATE**, **DELETE**, **EXECUTE** grants are applied again, and the procedures are updated. Each Procedure privilege is reset upon re-executing the action. 

If you change the stored procedure signature, the `fhir-persistence-schema` does not automatically drop the prior stored procedure and signature, and the stored procedure MUST be dropped manually.

## Managing GRANTS

The Db2 data definition secures data access using `GRANT` predicates. To update or change, use the `--grant-to` predicate to apply the grants.

If a grant is removed from the Java code, a manual process must be followed to remove or change the grant for the corresponding tables, procedures and variables.

# Testing migrations

We currently have two migration tests in place; one for Apache Derby which runs with the Maven build and one for Db2 which runs as part of the CI pipeline.

With each release of the IBM FHIR Server, these tests should be expanded to cover [at least] the migrations from the previous version.

## Testing migrations with Apache Derby

The `fhir-persistence-schema` project includes a single DerbyMigrationTest. Currently, this test invokes a copy of the FhirSchemaGenerator that was extracted from version 4.0.1 of the `fhir-persistence-schema` project and added directly to the package.

This was necessary because version 4.0.1 of the fhir-persistence-schema cli doesn't support deploying schemas for Apache Derby. However, starting with 4.1.0, we should use the released cli jar to deploy the previous versions of the schema. This will ensure the validity of the test and improve maintainability.

## Testing migrations with IBM Db2

The `fhir-install` module contains scripts for building Docker containers of the IBM FHIR Server and IBM Db2 and, optionally, bringing them up via `docker-compose`. When releasing new versions of the IBM FHIR Server, the `SCHEMA_VERSION` variable should be updated within `fhir-install/docker/copy-dependencies-db2-migration.sh` in order to test migrations from the previously released version of the `fhir-persistencne-schema` module.

## References
- [Git Issue: Document the schema migration process on the project wiki #270](https://github.com/IBM/FHIR/issues/270)
- [Db2 11.5: Extent sizes in table spaces](https://www.ibm.com/support/knowledgecenter/SSEPGG_11.5.0/com.ibm.db2.luw.admin.dbobj.doc/doc/c0004964.html)
- [Db2 11.5: Altering table spaces](https://www.ibm.com/support/producthub/db2/docs/content/SSEPGG_11.5.0/com.ibm.db2.luw.admin.dbobj.doc/doc/t0005096.html)

FHIR® is the registered trademark of HL7 and is used with the permission of HL7.
