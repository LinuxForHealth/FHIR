# Operation: fhir-operation-erase 

This document outlines the design and acceptance criteria for the fhir-operation-erase. 

**API Path** - `/fhir-server/api/v4/<RESOURCE>/<ID>/$erase`

|PATH Parameter Name|Required|Value|
|------|-----|-----|
|`RESOURCE`|required|The Resource Type|
|`ID`|required|The Resource ID|

**Method** - GET or POST

|Parameter Name|Type|Value|
|------|-----|-----|
|`patient`|optional|the patient id to add to the Audit Event|
|`reason`|optional|the textual reason to add to the Audit Event|

**HTTP Response Codes**

- 400 BAD_REQUEST - The request is malformed.
- 404 NOT_FOUND - The resource is not found.
- 410 GONE - The resource is permamently deleted.
- 500 INTERNAL SERVER ERROR - Failed to process the request.

**Examples**

### GET

```
curl --location --request GET 'https://localhost:9443/fhir-server/api/v4/Patient/1785fb0759b-1452d2e5-f568-442e-9841-3dc3940af5bc/$erase?patient=patient-id-is-this-id&reason=My%20Reason%20for%20removing%20this%20resource' \
--header 'Content-Type: application/fhir+json' \
--header 'Authorization: Basic ...'
```

Response Code: 410

### POST

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

Response Code: 410

**Server Invariants**

1. For Resources where the latest resource is deleted, Search is not possible.
2. For Resources updated inflight
3. Operations `$history` use custom change tables which do not include Patient data.

**Implementation Considerations**

1. Resources which are created incorrectly, should be $erased, rather than updated. For instance, a resource incorrectly identified with MRN1 which should be MRN2.
2. Erase remove all versions of a Resource to maintain the integrity of the datastore.
3. The Audit Record that is generated includes a patient specific id and reason for the deleted record that is added to the Audit Record.
4. While not preferred, the UUID may be reused, however any dangling references may reference the new resource improperly.

**Out-of-Scope**

The following are out-of-scope: 
1. Version specific deletes, however, constructing certain Bundle types results in non-deterministic results. This was deemed to be too error prone.
2. No Version specific reindexing.
3. Included Resources are not deleted and are implementor choice to remove - for instance, a patient with a provenance, only the patient is deleted.

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

