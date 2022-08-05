#!/usr/bin/env bash
###############################################################################
# (C) Copyright IBM Corp. 2021, 2022
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# targeted by wait_for_it.sh (from this same directory) for backwards compatiblity
# please delete that version after it is no longer needed by the migration tests

if [[ -z "${WORKSPACE}" ]]; then
    echo "ERROR: WORKSPACE environment variable not set!"
    exit 1
fi

healthcheck_url='https://localhost:9443/fhir-server/api/v4/$healthcheck'
tries=0
status=0

# Spins for a set time until the server is up
while [ $status -ne 200 -a $tries -lt 30 ]; do
    tries=$((tries + 1))
    set +o errexit
    cmd="curl --max-time 5 -k -s -o ${WORKSPACE}/health.json -w "%{http_code}" -u fhiruser:change-password $healthcheck_url"
    echo "Executing[$tries]: $cmd"
    status=$($cmd)
    set -o errexit
    echo "Status code: $status"
    if [ $status -ne 200 ]; then
    if [ $status -eq 500 ]; then
        echo "Server error on attempt ${tries}:"
        cat ${WORKSPACE}/health.json
        exit 10
    fi
    echo "Sleeping 10 secs..."
    sleep 10
    fi
done

if [ $status -ne 200 ]; then
    echo "Could not establish a connection to the fhir-server within ${tries} REST API invocations!"
    exit 1
fi