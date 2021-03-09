#!/usr/bin/env bash

# ----------------------------------------------------------------------------
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
# ----------------------------------------------------------------------------

# import

# 0 - Set the tenant id and password
export TENANT_ID=''
export PASS=''

# 1 - Create an Import
curl --location --request POST 'https://localhost:9443/fhir-server/api/v4/$import' \
--header 'Content-Type: application/fhir+json' -k \
-u "fhiruser:${PASS}" --header "X-FHIR-TENANT-ID: ${TENANT_ID}" \
--data-raw '{
    "resourceType": "Parameters",
    "id": "9aa41f86-e3ed-4163-9775-276f8839d2a8",
    "parameter": [
        {
            "name": "inputFormat",
            "valueString": "application/fhir+ndjson"
        },
        {
            "name": "inputSource",
            "valueUri": "https://localhost:9443/source-fhir-server"
        },
        {
            "name": "input",
            "part": [
                {
                    "name": "type",
                    "valueString": "Patient"
                },
                {
                    "name": "url",
                    "valueUrl": "test-import.ndjson"
                }
            ]
        },
        {
            "name": "storageDetail",
            "valueString": "ibm-cos"
        }
    ]
}' -v

# 2 - Copy the content-location
# < HTTP/2 202
# < content-location: https://localhost:9443/fhir-server/api/v4/$bulkdata-status?job=N_IUNkjjIGFE1qm4Md2NDA

# 3 - Loop until outcome
curl --location --request GET 'https://localhost:9443/fhir-server/api/v4/$bulkdata-status?job=N_IUNkjjIGFE1qm4Md2NDA' \
--header 'Content-Type: application/fhir+json' -k --header "X-FHIR-TENANT-ID: ${TENANT_ID}" \
-u "fhiruser:${PASS}" 

