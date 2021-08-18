# Operation: fhir-operation-erase 

This document outlines the design and acceptance criteria for the *fhir-operation-erase*. 

The $erase operation hard deletes the version or instance of a Resource, completely removing the Resource from the database.

**API Path** 
- `/fhir-server/api/v4/[RESOURCE]/[ID]/$erase`
- `/fhir-server/api/v4/[RESOURCE]/$erase`

|PATH Name|Required|Value|
|------|-----|-----|
|`RESOURCE`|required|The Resource Type|
|`ID`|optional|The Resource ID. If specified, the parameter logicalId must not be set in the parameters object. If not specified, the parameter logicalId must be set in the parameters object.|

**Method**
- POST - We do not support **GET**.

**Parameters**

|Parameter Name|Type|Value|Description|
|------|-----|-----|-----|
|`reason`|required|string|the textual reason to add to the Audit Event. A maximum of a 1000 characters.|
|`patient`|optional|string|the patient id to add to the Audit Event. Required, if the resource being erased is within the Patient Compartment.|
|`id`|optional|string|Required, if executing an erase at the Resource Type level, else it must not be included.|
|`version`|optional|integer|If included, the version of the resource is erased.|

These values are passed as a FHIR Parameters Resource.

**HTTP Response Codes**

- 200 OK - The resource or the resource-version is permanently deleted.
- 400 BAD_REQUEST - The request is malformed.
- 401 NOT_AUTHENTICATED - The users is not authenticated.
- 403 NOT_AUTHORIZED - The user is not an administrator in an authorized user's group.
- 404 NOT_FOUND - The resource is not found.
- 500 INTERNAL SERVER ERROR - Failed to process the request.

**Examples**

### POST

*Delete all versions on Instance*

```
curl --location --request POST 'https://test.fhirexample.com/fhir-server/api/v4/Patient/1785fb0759b-1452d2e5-f568-442e-9841-3dc3940af5bc/$erase' \
--header 'Content-Type: application/fhir+json' \
--header 'Authorization: Basic ...' \
--data-raw '{
  "resourceType": "Parameters",
  "parameter": [
    {
      "name": "patient",
      "valueString": "patient-id-is-this-id"
    },
    {
      "name": "reason",
      "valueString": "My Reason for removing this resource"
    }
  ]
}'
```

*Delete all versions on Resource*

```
curl --location --request POST 'https://test.fhirexample.com/fhir-server/api/v4/Patient/$erase' \
--header 'Content-Type: application/fhir+json' \
--header 'Authorization: Basic ...' \
--data-raw '{
  "resourceType": "Parameters",
  "parameter": [
    {
      "name": "patient",
      "valueString": "patient-id-is-this-id"
    },
    {
      "name": "reason",
      "valueString": "My Reason for removing this resource"
    },
    {
      "name": "id",
      "valueString": "1785fb0759b-1452d2e5-f568-442e-9841-3dc3940af5bc"
    }
  ]
}'
```

*Delete a specific version on Instance*

```
curl --location --request POST 'https://test.fhirexample.com/fhir-server/api/v4/Patient/1785fb0759b-1452d2e5-f568-442e-9841-3dc3940af5bc/$erase' \
--header 'Content-Type: application/fhir+json' \
--header 'Authorization: Basic ...' \
--data-raw '{
  "resourceType": "Parameters",
  "parameter": [
    {
      "name": "patient",
      "valueString": "patient-id-is-this-id"
    },
    {
      "name": "reason",
      "valueString": "My Reason for removing this resource"
    },
    {
      "name": "version",
      "valueInteger": 1
    }
  ]
}'
```

*Delete a specific version on Resource*

```
curl --location --request POST 'https://test.fhirexample.com/fhir-server/api/v4/Patient/$erase' \
--header 'Content-Type: application/fhir+json' \
--header 'Authorization: Basic ...' \
--data-raw '{
  "resourceType": "Parameters",
  "parameter": [
    {
      "name": "patient",
      "valueString": "patient-id-is-this-id"
    },
    {
      "name": "reason",
      "valueString": "My Reason for removing this resource"
    },
    {
      "name": "id",
      "valueString": "1785fb0759b-1452d2e5-f568-442e-9841-3dc3940af5bc"
    },
    {
      "name": "version",
      "valueInteger": 1
    }
  ]
}'
```

*Delete all versions on Instance outside the Patient Compartment*

```
curl --location --request POST 'https://test.fhirexample.com/fhir-server/api/v4/StructureDefinition/1785fb0759b-1452d2e5-f568-442e-9841-3dc3940af5bc/$erase' \
--header 'Content-Type: application/fhir+json' \
--header 'Authorization: Basic ...' \
--data-raw '{
  "resourceType": "Parameters",
  "parameter": [
    {
      "name": "reason",
      "valueString": "My Reason for removing this resource"
    }
  ]
}'
```

*Delete all versions on Resource outside the Patient Compartment*

```
curl --location --request POST 'https://test.fhirexample.com/fhir-server/api/v4/StructureDefinition/$erase' \
--header 'Content-Type: application/fhir+json' \
--header 'Authorization: Basic ...' \
--data-raw '{
  "resourceType": "Parameters",
  "parameter": [
    {
      "name": "reason",
      "valueString": "My Reason for removing this resource"
    },
    {
      "name": "id",
      "valueString": "1785fb0759b-1452d2e5-f568-442e-9841-3dc3940af5bc"
    }
  ]
}'
```

*Delete a specific version on Resource outside the Patient Compartment*

```
curl --location --request POST 'https://test.fhirexample.com/fhir-server/api/v4/StructureDefinition/$erase' \
--header 'Content-Type: application/fhir+json' \
--header 'Authorization: Basic ...' \
--data-raw '{
  "resourceType": "Parameters",
  "parameter": [
    {
      "name": "reason",
      "valueString": "My Reason for removing this resource"
    },
    {
      "name": "id",
      "valueString": "1785fb0759b-1452d2e5-f568-442e-9841-3dc3940af5bc"
    },
    {
      "name": "version",
      "valueInteger": 1
    }
  ]
}'
```

#### Response - All Done delete every version

Response Code: 200
Response Body: Return Operation Outcome telling the person that it is confirmed to be deleted.

If a TRANSACTION TIMEOUT (Rollback) is hit, a user is able to execute an erase on each version of the Resource, and then Erase the latest.

``` json
{
    "resourceType": "Parameters",
    "id": "38fa6c99-69e0-4e6f-a134-f2658deebe07",
    "parameter": [
        {
            "name": "resource",
            "valueString": "Patient/17957682e44-bdd38817-6048-4db2-9502-9cb81175119b"
        },
        {
            "name": "partial",
            "valueBoolean": false
        },
        {
            "name": "total",
            "valueInteger": 3
        }
    ]
}
```

#### Response - All Done delete a specific version

Response Code: 200 
Response Body: Return Operation Outcome.

``` json
{
    "resourceType": "Parameters",
    "id": "38fa6c99-69e0-4e6f-a134-f2658deebe07",
    "parameter": [
        {
            "name": "resource",
            "valueString": "Patient/17957682e44-bdd38817-6048-4db2-9502-9cb81175119b/_history/1"
        },
        {
            "name": "partial",
            "valueBoolean": true
        },
        {
            "name": "total",
            "valueInteger": 1
        }
    ]
}
```

**Server Invariants**

1. For Resources where the latest resource is deleted, FHIR search cannot be used to identify Resources specific to the Patient.
2. For Resources updated inflight, the operation may fail due to locking, and the client is expected to retry.

**Implementation Considerations**

1. Resources which are created incorrectly, should be $erased, rather than updated. For instance, a resource incorrectly identified with MRN1 which should be MRN2. However, deleting the offending version is possible once the offending version is NOT the latest version.
2. When executing on the Instance, erase removes all versions of a Resource to maintain the integrity of the datastore. When executing on the Instance and the version, erase removes the specific version.
3. The Audit Record that is generated includes a patient specific id and reason for the deleted record that is added to the Audit Record.
4. While not preferred, the UUID may be reused, however any dangling references may reference the new resource improperly.
5. Bundles, both Batch and Transaction types, are supported.

An implementors note, the first version of ther Erase Resource DAO used a ThreadPool from OpenLiberty to manage the timeout related to long running Erase operations. The timeout was passed from the client (and no longer supported).  The code picked the current connection, a ManagedConnection (see WSRdbManagedConnectionImpl.java in OpenLiberty), and parked it in a new Thread (with a new threadId JEEContext,SecurityContext and BaseContext set). The ManagedConnection created a second transaction with Postgres and forks a new Connection inside the new UserTransaction (with one already running). More importantly, this fork sets AutoCommit to true... which is dangerous enough that we've removed this approach from the code base. If timeouts are encountered, one should use the version specific erase to remove older copies, and then execute the non-versioned erase.

**Out-of-Scope**

The following are out-of-scope: 
1. Included Resources are not deleted and are the user's choice to remove - for instance, a patient with a provenance, only the patient is deleted. The user must delete the included resources.
2. The downstream caches are not erased - e.g. Private Http Caches.

**Business Logic Considerations**

The following should be considered:
1. If you are looking up the Patient ID via MRN using Search, then Patient resource should be deleted last.

# Acceptance Criteria

The following are the acceptance criteria for the `$erase` operation.

## Acceptance Criteria 1: Resource Exists, not Deleted and No Audit

- GIVEN Patient Resource (ID1)
    - AND Resource latest is not deleted
    - AND Audit is disabled
- WHEN the ID1 is $erase
- THEN Patient Resource is no longer searchable
    - AND READ does not return GONE
    - AND READ does not return the resource
    - AND READ does returns not found
    - AND READ does not return a Resource
    - AND No Audit Record created

## Acceptance Criteria 2: Resource Exists, not Deleted and Audit

- GIVEN Patient Resource (ID1)
    - AND Resource latest is not deleted
    - AND Audit is enabled
- WHEN the ID1 is $erase
- THEN Patient Resource is no longer searchable
    - AND READ does not return GONE
    - AND READ does not return the resource
    - AND READ does returns not found
    - AND READ does not return a Resource
    - AND an Audit Record is created

## Acceptance Criteria 3: Resource Exists, latest is Deleted and No Audit

- GIVEN Patient Resource (ID2)
    - AND Resource latest is deleted
    - AND Audit is disabled
- WHEN the ID1 is $erase
- THEN Patient Resource is no longer searchable
    - AND READ does not return GONE
    - AND READ does not return the resource
    - AND READ does returns not found
    - AND READ does not return a Resource
    - AND No Audit Record created

## Acceptance Criteria 4: Resource Exists, latest is Deleted and Audit

- GIVEN Patient Resource (ID2)
    - AND Resource latest is deleted
    - AND Audit is enabled
- WHEN the ID1 is $erase
- THEN Patient Resource is no longer searchable
    - AND READ does not return GONE
    - AND READ does not return the resource
    - AND READ does returns not found
    - AND READ does not return a Resource
    - AND Audit Record created

## Acceptance Criteria 5: Resource Exists, not Deleted and No Audit

- GIVEN Patient Resource v1
    - AND Resource v2 is created
    - AND Resource v3 is deleted
    - AND Resource latest is not deleted
    - AND Audit is disabled
- WHEN the $erase is called on the resource
- THEN Patient Resource is no longer searchable
    - AND READ does not return GONE
    - AND READ does not return the resource
    - AND READ does returns not found
    - AND READ does not return a Resource
    - AND No Audit Record created
    - AND no History is accessible

## Acceptance Criteria 6: Resource Exists, not Deleted and Audit

- GIVEN Patient Resource v1
    - AND Resource v2 is created
    - AND Resource v3 is deleted
    - AND Resource latest is not deleted
    - AND Audit is enabled
- WHEN the $erase is called on the resource
- THEN Patient Resource is no longer searchable
    - AND READ does not return GONE
    - AND READ does not return the resource
    - AND READ does returns not found
    - AND READ does not return a Resource
    - AND an Audit Record created
    - AND no History is accessible

## Acceptance Criteria 7: Operation from Bundle (Batch)

- GIVEN Resource v1 is created
    - AND Resource v2 is updated
    - AND Resource-Faux does not exist
    - AND Audit is enabled
- WHEN the batch of $erase is called on the resource
- THEN Resource1 is no longer searchable
    - AND READ does not return the resource
    - AND READ does returns not found
    - AND READ does not return a Resource
    - AND the BATCH does not fail
    - AND an Audit Record created
    - AND no History is accessible

## Acceptance Criteria 8: Operation from Bundle (Transaction)

- GIVEN Resource v1 is created
    - AND Resource v2 is updated
    - AND Resource-Faux does not exist
    - AND Audit is enabled
- WHEN the batch of $erase is called on the resource
- THEN Resource1 is searchable
    - AND READ does return the resource
    - AND the TRANSACTION does not fail
    - AND an Audit Record created
    - AND no History is accessible

## Acceptance Criteria 9: Lots of Versions on the Same Resource and a Small Transaction Timeout

- GIVEN Resource v1 is created
    - AND Resource versions are updated to version 350,000 with a variety of Create-Update-Deletes
    - AND Audit is enabled
- WHEN $erase is called on the resource
    - AND timeout has not finished
- THEN Resource1 is not searchable
    - AND READ does not return the resource
    - AND Erase is complete (200)
    - AND an Audit Record created
    - AND no History is accessible

## Acceptance Criteria 10: Version Erase - Resource Exists, not Deleted and not Audit

- GIVEN Patient Resource v1
    - AND Resource v2 is created
    - AND Resource v3 is deleted
    - AND Resource latest is not deleted
- WHEN the $erase on v2 is called on the resource
- THEN Patient Resource is searchable
    - AND READ does return the resource
    - AND READ does not return _history/2 
    - AND search does return a Resource

## Acceptance Criteria 11: Version Erase - Resource Exists, not Deleted and not Audit

- GIVEN Patient Resource v1
    - AND Resource v2 is created
    - AND Resource v3 is deleted
    - AND Resource latest is not deleted
- WHEN the $erase on v2 is called on the resource
- THEN Patient Resource is searchable
    - AND READ does return the resource
    - AND READ does not return _history/2 
    - AND search does return a Resource

Some edge case Acceptance Criteria include: 
- Multiple Versions fail
- Multiple Ids fail
- Logical Id and Ids fail
- Logical Id missing fails on Resource Type
- No Patient when inside Patient Compartment
- Data Absent Parameters

See EraseOperationTest, JdbcEraseTest, EraseTest for the corresponding tests.

# Useful References

The following resources are useful. 

*Db2*

1. [Control Structures](https://www.ibm.com/docs/en/db2-for-zos/11?topic=hscisp-controlling-how-errors-are-handled-within-different-scopes-in-sql-procedure)
2. [Get Diagnostics](https://www.ibm.com/docs/en/db2/11.1?topic=statements-get-diagnostics)
3. [Reference](https://www.ibm.com/support/producthub/db2/docs/content/SSEPGG_11.5.0/com.ibm.db2.luw.sql.ref.doc/doc/r0000939.html)

*Postgres*

1. [Control Structures](https://www.postgresql.org/docs/13/plpgsql-control-structures.html)
