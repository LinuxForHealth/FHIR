#!/usr/bin/env bash

# ----------------------------------------------------------------------------
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
# ----------------------------------------------------------------------------

# export patient with a _type of Patient

# 0 - Set the tenant id and password
export TENANT_ID=''
export PASS=''

# 1 - Create an Export Request
curl -k -u "fhiruser:${PASS}" -H "Content-Type: application/fhir+json" -X GET \
    'https://localhost:9443/fhir-server/api/v4/Group/177f58d50e8-4d342940-2324-4110-ae72-6cb57ebac3bd/$export?_outputFormat=application/fhir+ndjson&_type=Patient' \
    -v --header "X-FHIR-TENANT-ID: ${TENANT_ID}"

# 2 - Get the Content-Location and the Job
# < HTTP/2 202
# < content-location: https://localhost:9443/fhir-server/api/v4/$bulkdata-status?job=eikWD%2BJszJJ_DkN6HWMLYQ

# 3 - Set the Job Id and execute the request
# Repeat until 200(OK)
curl --location --request GET 'https://localhost:9443/fhir-server/api/v4/$bulkdata-status?job=O5X6d9LOYDZV35OjHkt5wA' \
    --header 'Content-Type: application/fhir+json' -k \
    -u "fhiruser:${PASS}" -v --header "X-FHIR-TENANT-ID: ${TENANT_ID}" -o /tmp/export.json

# 4 - Check the file that is output.
for DOWNLOAD in $(cat /tmp/export.json | jq -r .output[].url)
do
    curl "$DOWNLOAD"
done