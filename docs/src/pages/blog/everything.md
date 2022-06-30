---
layout: post
title:  Introducing the IBM FHIR Server - $everything operation
description: Introducing the IBM FHIR Server - $everything operation
date:   2021-04-21
---

By Paul Bastide    |    Published April 4, 2021

# The $everything operation
In [IBM FHIR Server 4.7.0](https://github.com/LinuxForHealth/FHIR/releases/tag/4.7.0), the IBM FHIR Server team turned on the `Patient/$everything` operation. We have this feature thanks to external contributor and an IBMer, [Luis García](https://github.com/luisgarcc) who implemented the operation in [#1044](https://github.com/LinuxForHealth/FHIR/issues/1044).

So what do you get with `Patient/$everything`? Well, following the HL7 FHIR R4 [specification](http://hl7.org/fhir/patient-operation-everything.html), you get an operation that returns a "searchset" bundle with the information related to a patient in the context it is invoked. In the IBM FHIR Server's case, the server returns whatever resources are related to the Patient - e.g. practitioners, medications, locations, organizations etc.  You also get support for `Patient/[id]/$everything` where the `[ID]` is the logical id specific to a patient.

Let's build a request and see it in action:

We need to know about the parameters of the `$everything` operation.

| Name | Type | Description |
|---|---|---|
| `start` | date | The date range relates to care dates, not record currency dates - e.g. all records relating to care provided in a certain date range. If no start date is provided, all records prior to the end date are in scope. |
| `end` | date | The date range relates to care dates, not record currency dates - e.g. all records relating to care provided in a certain date range. If no end date is provided, all records subsequent to the start date are in scope. |
| `_since` | date | Resources updated after this period will be included in the response. The intent of this parameter is to allow a client to request only records that have changed since the last request, based on either the return header time, or or (for asynchronous use), the transaction time|
| `_type` | code | One or more parameters, each containing one or more comma-delimited FHIR resource types to include in the return resources. In the absence of any specified types, the server returns all resource types |
| `return` | Bundle | The bundle is "searchset" |

Note, currently the implementation ignores the `_count` parameter.

Now, let's prepopulate some sample data on the server using the [Integration Test data](https://github.com/LinuxForHealth/FHIR/blob/main/fhir-server-test/src/test/resources/testdata/everything-operation/Antonia30_Acosta403.json).

1. Download the Sample Data

``` sh
curl -L https://raw.githubusercontent.com/IBM/FHIR/main/fhir-server-test/src/test/resources/testdata/everything-operation/Antonia30_Acosta403.json -o Antonia30_Acosta403.json
```

2. Load the Sample Data bundle to the IBM FHIR Server

``` sh
curl -k --location --request POST 'https://localhost:9443/fhir-server/api/v4' \
    --header 'Content-Type: application/fhir+json' \
    --user "fhiruser:${DUMMY_PASSWORD}" \
    --data-binary  "@Antonia30_Acosta403.json" -o response.json
```

3. Scan the response.json for any status that is not `"status": "201"`.  For example, the status is in the family of User Request Error or Server Side Error.

4. Check the `response.json` and find the id for Patient (it should be the first one).

``` json
        {
            "response": {
                "id": "178f4a3f869-6abfe1e6-3a4a-4c9a-81a8-fb8c6263e003",
                "status": "201",
                "location": "Patient/178f4a3f869-6abfe1e6-3a4a-4c9a-81a8-fb8c6263e003/_history/1",
                "etag": "W/\"1\"",
                "lastModified": "2021-04-21T13:34:50.207684Z"
            }
        },
```

5. Check the `Patient/[id]/$everything`

* Request *
``` sh
curl -k --location --request GET 'https://localhost:9443/fhir-server/api/v4/Patient/178f4a3f869-6abfe1e6-3a4a-4c9a-81a8-fb8c6263e003/$everything' \
    --header 'Content-Type: application/fhir+json' \
    --user "fhiruser:${DUMMY_PASSWORD}" \
```

* Response *

``` json
{
    "resourceType": "Bundle",
    "id": "689856f9-6ef6-4d74-8f77-a813ff7b1c6d",
    "type": "searchset",
    "total": 895,
    "entry": [
        {
            "fullUrl": "https://localhost:9443/fhir-server/api/v4/Patient/178f4a3f869-6abfe1e6-3a4a-4c9a-81a8-fb8c6263e003",
            "resource": {
                "resourceType": "Patient",
                "id": "178f4a3f869-6abfe1e6-3a4a-4c9a-81a8-fb8c6263e003",
                "meta": {
                    "versionId": "1",
                    "lastUpdated": "2021-04-21T13:34:50.207684Z"
                }
                ...
            }
        }
    ]
}
```

6. Check the `Patient/[id]/$everything` with a _type constraint and _since.

* Request *

``` sh
curl -k --location --request GET 'https://localhost:9443/fhir-server/api/v4/Patient/178f4a3f869-6abfe1e6-3a4a-4c9a-81a8-fb8c6263e003/$everything?_type=CarePlan,CareTeam&_since=2021-01-01T00:00:00Z&_count=1' \
    --header 'Content-Type: application/fhir+json' \
    --user "fhiruser:${DUMMY_PASSWORD}" -o care.json
```

* Response *

``` json
{
    "resourceType": "Bundle",
    "id": "689856f9-6ef6-4d74-8f77-a813ff7b1c6d",
    "type": "searchset",
    "total": 11,
    "entry": [
        {
            "fullUrl": "https://localhost:9443/fhir-server/api/v4/Patient/178f4a3f869-6abfe1e6-3a4a-4c9a-81a8-fb8c6263e003",
            "resource": {
                "resourceType": "Patient",
                "id": "178f4a3f869-6abfe1e6-3a4a-4c9a-81a8-fb8c6263e003",
                "meta": {
                    "versionId": "1",
                    "lastUpdated": "2021-04-21T13:34:50.207684Z"
                }
                ...
            }
        }
    ]
}
```

You'll see further down CareTeam and CarePlan resources...

7. This is where we can have a bit of fun...  download the [fhir-path-cli](https://github.com/LinuxForHealth/FHIR/releases/download/4.7.0/fhir-path-4.7.0-cli.jar)

``` sh
curl -L https://github.com/LinuxForHealth/FHIR/releases/download/4.7.0/fhir-path-4.7.0-cli.jar -o fhir-path-4.7.0-cli.jar
```

8. Let's run the fhir-path-4.7.0-cli.jar and test a FHIRPath.

*Command Line*

```
java -jar fhir-path-4.7.0-cli.jar --path 'entry.fullUrl' --file care.json
```

*Output*

```
https://localhost:9443/fhir-server/api/v4/Patient/178f4a3f869-6abfe1e6-3a4a-4c9a-81a8-fb8c6263e003
https://localhost:9443/fhir-server/api/v4/Patient/178f4a3f869-6abfe1e6-3a4a-4c9a-81a8-fb8c6263e003/CarePlan/178f4a3f86a-1a9a1d73-4d1d-46d5-8494-339dddbfe9d2
https://localhost:9443/fhir-server/api/v4/Patient/178f4a3f869-6abfe1e6-3a4a-4c9a-81a8-fb8c6263e003/CarePlan/178f4a3f86b-06c07a6c-3be7-4e6a-abb2-040ff67c8c1f
https://localhost:9443/fhir-server/api/v4/Patient/178f4a3f869-6abfe1e6-3a4a-4c9a-81a8-fb8c6263e003/CarePlan/178f4a3f873-d8069835-938e-45b1-9a70-5aba3794f6f3
https://localhost:9443/fhir-server/api/v4/Patient/178f4a3f869-6abfe1e6-3a4a-4c9a-81a8-fb8c6263e003/CarePlan/178f4a3f87c-ce204596-8037-416c-b2e0-fc7442d9a276
https://localhost:9443/fhir-server/api/v4/Patient/178f4a3f869-6abfe1e6-3a4a-4c9a-81a8-fb8c6263e003/CarePlan/178f4a3f87c-46785bc7-807a-40e0-bc08-792977ac8ec8
https://localhost:9443/fhir-server/api/v4/Patient/178f4a3f869-6abfe1e6-3a4a-4c9a-81a8-fb8c6263e003/CareTeam/178f4a3f86a-b79251c3-5019-426e-8211-748adbdfdf54
https://localhost:9443/fhir-server/api/v4/Patient/178f4a3f869-6abfe1e6-3a4a-4c9a-81a8-fb8c6263e003/CareTeam/178f4a3f86b-22a7baab-77b3-4fb5-b318-9249ea80ec85
https://localhost:9443/fhir-server/api/v4/Patient/178f4a3f869-6abfe1e6-3a4a-4c9a-81a8-fb8c6263e003/CareTeam/178f4a3f873-e370fba2-f45e-4cdd-b13b-781e249f84f8
https://localhost:9443/fhir-server/api/v4/Patient/178f4a3f869-6abfe1e6-3a4a-4c9a-81a8-fb8c6263e003/CareTeam/178f4a3f87c-596d80cc-7b42-4054-bc02-a48acfcc95b7
https://localhost:9443/fhir-server/api/v4/Patient/178f4a3f869-6abfe1e6-3a4a-4c9a-81a8-fb8c6263e003/CareTeam/178f4a3f87c-6cc197cd-b665-46e0-ad35-c016edeaf13b
```

You've seen a brief introduction to fhir-operation-everything and a bonus fhir-path-cli showing the output.

Note, `DUMMY_PASSWORD` should be replaced with or set as a value when the corresponding curl steps are executed.
