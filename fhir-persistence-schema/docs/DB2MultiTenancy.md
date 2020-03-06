# The IBM FHIR Server - DB2 Multi-Tenancy

This document outlines the schema design and implementation for the IBM FHIR Server's DB2 Multi-tenancy feature. 

The Multi-tenancy feature has two main areas of focus: 
- Tenant Provisioning  
- Security

## Multi-Tenancy Schema Design

The design has an administrative schema and at least one tenant schema. Each tenant schema is in a separate tenant specific tablespace. 

### Schema: Administrative

The administrative schema's name is FHIR_ADMIN.  FHIR_ADMIN has 
- three tables: TENANTS, TENANT_KEYS, VERSION_HISTORY
- one sequence: FHIR_ADMIN.TENANT_SEQUENCE
- one stored procedure: SET_TENANT

The tables have supporting indicies and privileges. 

**Table: TENANTS**

The Tenants table maps a given tenant to the Multi-Tenant ID (MT_ID) for the given tenant.  The MT_ID is subsequently used to restrict row access based on the matching value. 

| Column Name | Data Type | Length | Null | Purpose |
|----------|---------------|---------------|---------------|---------------------------------------------|
| MT_ID | Integer | 4 | No | Unique ID identifying the Tenant's ID|
| TENANT_NAME | VARCHAR | 36 | No | The name of the tenant |
| TENANT_STATUS | VARCHAR | 16 | No | The status of the Tenant|

MT_ID is the value used to assign ROW Access Control in the Multi-Tenant schema. The MT_ID is assigned from the FHIR_ADMIN.TENANT_SEQUENCE.  The value is to 1 to 9999, with no cycles. 

Tenant Status is one of the following: 
| Status | Purpose |
|----------|---------------------------------------------|
| PROVISIONING | Indicates the tenant schema is in the process of adding a set of tenant tables/partitions |
| ALLOCATED | Indicates the process of creating the tenant schema is finished, and the schema is ready for use. Only allocated tenants are available for use in the `fhir-persistence-jdbc` code |
| FROZEN | Indicates the previous tenant data is not to be used, and the tenant data is in the process of being removed |
| DROPPED | Indicates the previous tenant data is now removed |
| FREE | Indicates a set of tables is provisioned, and available to be allocated to a specific tenant |

**Table: TENANT_KEYS**

The Tenant Keys table stores a hashed version of the tenant specific key. Upon creating a session and calling the SET_TENANT procedure, the SET_TENANT queries the incoming key against the TENANT_KEYS database to map to a specific tenant MT_ID. 

| Column Name | Data Type | Length | Null | Purpose |
|----------|---------------|---------------|---------------|---------------------------------------------|
| TENANT_KEY_ID | Integer | 4 | No | The ID is from the sequence, and is used to identify the unique entry in the row |
| MT_ID | Integer | 4 | No | Matches to the MT_ID in the TENANTS table |
| TENANT_SALT | VARCHAR | 44 | No | A 32 byte random value used as a salt when computing the SHA-256 tenant hash |
| TENANT_HASH | VARBINARY | 32 | No | Each tenant's data is encrypted. |

The tenant_key is a 32 byte random value created when the tenant is provisioned. This is a secret, and must be passed by the client to the database in order to obtain access to a tenant. A tenant may have multiple tenant keys to support key rotation. An example of the tenant key is `GQUHRi0ETFw0Xe+p1VtVDki4JFZs885bGM35hXRyq/E=`

Multiple keys per tenant are supported, and key rotation includes adding, and then subsequently removing prior keys. 

**Table: VERSION_HISTORY**

The VERSION_HISTORY table enables Tenant version isolation, so upgrades and patches are separately applied to each tenant's schema. 

| Column Name | Data Type | Length | Null | Purpose |
|----------|---------------|---------------|---------------|---------------------------------------------|
| SCHEMA_NAME | VARCHAR | 64 | No | The name of the schema where the object resides|
| OBJECT_TYPE | VARCHAR | 16 | No | The type of the object - Procedure, Sequence, Table, TableSpace, Variable |
| OBJECT_NAME | VARCHAR | 64 | No | The name of the object in the schema |
| VERSION | INTEGER | 4 | No | The version starting at one and incrementing with every version change |
| APPLIED | TIMESTAMP | 10 | No | The time of the latest object update |

### Schema: Tenant-Specific

The Tenant specific data is partitioned and isolated in a customer specific tablespace.  The tenant data is not mixed with other tenant data physically using the tablespace isolation. 

Tenants are never able to see each other's data, each user is only able to see a single tenant's data. 

Each tenant's data is able to be moved, backedup, restored or exported a tenant-at-a-time.  This approach allows tenant independent restore points.

The isolated data supports tenant independent query plan caches, scaling and disambiguating tenant specific disk and space usage. The performance metrics are tenant specific, and diagnostics are tenant specific. 

**Table: Resources**

Each resource is contains a root RESOURCE and LOGICAL RESOURCE, such as VISIONPRESCRIPTION:

```
VISIONPRESCRIPTION_RESOURCES
VISIONPRESCRIPTION_LOGICAL_RESOURCES
```

These values are mapped to specific FHIR Search value types, supporting parameter searches: 

```
VISIONPRESCRIPTION_COMPOSITES
VISIONPRESCRIPTION_DATE_VALUES
VISIONPRESCRIPTION_LATLNG_VALUES
VISIONPRESCRIPTION_NUMBER_VALUES
VISIONPRESCRIPTION_QUANTITY_VALUES
VISIONPRESCRIPTION_STR_VALUES
VISIONPRESCRIPTION_TOKEN_VALUES
```

There are also global tables which enable a global search using the logical_resource and resource.  

Each row is secured using DB2 Row Permission. The Stored Procedure is the ONLY way for a user to set the variable, which is used in the DB2 Row Permission. 

Each tenant's data is encrypted. 

Offboarding a tenant's data is accomplished using dettach/drop partition, and subsequently dropping the tablespace. 

**Stored Procedure: FHIR_ADMIN.SET_TENANT**

The FHIR_ADMIN.SET_TENANT is used at the beginning of a connection to the IBM FHIR Server's multi-tenant schema. The SET_TENANT takes as input: tenant_name VARCHAR(36), tenant_key  VARCHAR(44).  The content outputs a connection specific variable `fhir_admin.sv_tenant_id` with the `tenant.mt_id`, which is automatically applied to the INSERT,SELECT,UPDATE,DELETE.  Only tenant's with status ALLOCATED are allowed to execute the actions.

To use the stored procedure from the db2 administrative console. 

* Map Tenant and Key to Tenant ID (Stored Procedure) - `CALL FHIR_ADMIN.set_tenant('a-tenant-name', 'BLAH_BLAH');`
* Show the value set for the session - `VALUES fhir_admin.sv_tenant_id;`
* Execute the query, such as `SELECT * FROM FHIRDATA.MEDICATION_RESOURCES`

**Schema: Permissions**

The Multi-tenant schema sets the following permissions: 
- All objects in schema FHIRADMIN are owned by user FHIRADMIN
- All objects in schema FHIRDATA are owned by user FHIRADMIN
- GRANT EXECUTE ON FHIRADMIN.SET_TENANT TO FHIRUSER;
- GRANT READ ON FHIRADMIN.SV_TENANT_ID TO FHIRUSER;
- GRANT EXECUTE ON FHIRDATA.ADD_RESOURCE_TYPE TO FHIRUSER;
- GRANT EXECUTE ON FHIRDATA.ADD_RESOURCE TO FHIRUSER;
- GRANT SELECT,INSERT,UPDATE,DELETE ON FHIRDATA.PATIENT_RESOURCES TO FHIRUSER;

FHIRADMIN owns all schema objects. 
FHIR User is granted a minimum set of priveleges. 
SET_TENANT is the only way to write the SV_TENANT_ID. 

The diagram outlines the relationships between the administrative and tenant schema. 
![permissions-overview.png](permissions-overview.png)

## References
[StackExchange: Recommendations on Partitioning Multi-Tenant Data in SQL-Server](https://dba.stackexchange.com/questions/184969/recommendations-on-partitioning-multi-tenant-data-in-sql-server)

FHIR® is the registered trademark of HL7 and is used with the permission of HL7.