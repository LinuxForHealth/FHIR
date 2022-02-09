#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

set -ex

# pre_integration
pre_integration() {
    cleanup
    config
    bringup
}

# config - update configuration
config() {
    DIST="${WORKSPACE}/build/reindex/derby/workarea/volumes/dist"
    echo "Create the db volume..."
    mkdir -p "${DIST}/db"

    # Setup the Configurations for Reindex
    echo "Copying fhir configuration files..."
    mkdir -p "${DIST}/config"
    cp -r "${WORKSPACE}/fhir-server-webapp/src/main/liberty/config/config" "$DIST"
    cp -r ${WORKSPACE}/fhir-server-webapp/src/test/liberty/config/config/* "$DIST/config"

    echo "Copying test artifacts to install location..."
    USERLIB="${DIST}/userlib"
    mkdir -p "${USERLIB}"
    find "${WORKSPACE}/conformance" -iname 'fhir-ig*.jar' -not -iname 'fhir*-tests.jar' -not -iname 'fhir*-test-*.jar' -exec cp -f {} "${USERLIB}" \;

    # Move over the test configurations
    echo "Checking dynamic resource provider"
    if [ "$(jq -r '.fhirServer.core.serverRegistryResourceProviderEnabled' ${DIST}/config/default/fhir-server-config.json)" = "true" ]
    then 
        echo "serverRegistryResourceProviderEnabled is true"
    else 
        echo "serverRegistryResourceProviderEnabled is false, tests cannot run"
        exit 1;
    fi

}

# cleanup - cleanup existing docker
cleanup() {
    # Stand up a docker container running the fhir server configured for integration tests
    echo "Bringing down any containers that might already be running as a precaution"
    docker-compose kill
    docker-compose rm -f
}

# bringup
bringup() {
    echo "Bringing up containers"
    docker-compose up --remove-orphans -d
    echo ">>> Current time: $(date)"

    # Startup FHIR
    cx=0
    while [ $(docker container inspect derby_fhir_1 | jq -r '.[] | select (.Config.Hostname == "fhir").State.Status' | wc -l) -gt 0 ] && [ $(docker container inspect derby_fhir_1 | jq -r '.[] | select (.Config.Hostname == "fhir").State.Running' | grep false | wc -l) -eq 1 ]
    do
        echo "Waiting on startup of fhir ${cx}"
        cx=$((cx + 1))
        if [ ${cx} -ge 300 ]
        then
            echo "Failed to start fhir"
        fi
        sleep 1
    done

    # Gather up all the server logs so we can trouble-shoot any problems during startup
    pre_it_logs=${WORKSPACE}/pre-it-logs
    zip_file=${WORKSPACE}/pre-it-logs.zip
    rm -rf "${pre_it_logs}" 2>/dev/null
    mkdir -p "${pre_it_logs}"
    rm -f "${zip_file}"

    echo "Docker container status:"
    docker ps -a

    containerId=$(docker ps -a | grep derby_fhir_1 | cut -d ' ' -f 1)
    if [ -z "${containerId}" ]
    then
        echo "Warning: Could not find the fhir container!!!"
    else
        echo "fhir container id: ${containerId}"

        # Grab the container's console log
        docker logs ${containerId} > ${pre_it_logs}/docker-console.txt

        echo "Gathering pre-test server logs from docker container: ${containerId}"
        docker cp -L ${containerId}:/logs ${pre_it_logs}
    fi

    # Wait until the fhir server is up and running...
    echo "Waiting for fhir-server to complete initialization..."
    healthcheck_url='https://localhost:9443/fhir-server/api/v4/$healthcheck'
    tries=0
    status=0
    while [ $status -ne 200 ] && [ $tries -lt 30 ]
    do
        tries=$((tries + 1))

        set +o errexit
        cmd="curl -k -o ${WORKSPACE}/health.json --max-time 5 -I -w "%{http_code}" -u fhiruser:change-password $healthcheck_url"
        echo "Executing[$tries]: $cmd"
        status=$($cmd)
        set -o errexit

        echo "Status code: $status"
        if [ $status -ne 200 ]
        then
            echo "Sleeping 30 secs..."
            sleep 30
        fi
    done

    if [ $status -ne 200 ]
    then
        echo "Could not establish a connection to the fhir-server within $tries REST API invocations!"
        exit 1
    fi

    echo "The fhir-server appears to be running..."
    exit 0
}

# is_ready_to_run - is this ready to run?
is_ready_to_run() {
    echo "Preparing environment for fhir-server integration tests..."
    if [ -z "${WORKSPACE}" ]
    then
        echo "ERROR: WORKSPACE environment variable not set!"
        exit 1
    fi
}

###############################################################################
is_ready_to_run

cd build/reindex/derby
pre_integration

# EOF
###############################################################################