#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

set -o errexit
set -o nounset
set -o pipefail

# pre_integration - 
pre_integration(){
    cleanup_prior
    copy_server_config
    cleanup_existing_docker
    bringup_database
    copy_schema_jar
    deploy_schema
    copy_test_operations
    bringup_fhir
}

# cleanup_prior - Cleans up the prior files
cleanup_prior(){
    echo "Removing old dependencies..."
    DIST="volumes/dist"
    SCHEMA="volumes/schema"
    rm -rf $DIST/* 2> /dev/null
    rm -rf $SCHEMA/* 2> /dev/null
    mkdir -p $DIST
    mkdir -p $SCHEMA
}

# copy_server_config - Copy assembled files
copy_server_config(){
    echo "Copying installation zip files..."
    cp -p ../target/fhir-server-distribution.zip $DIST

    echo "Copying fhir configuration files..."
    cp -pr ../../fhir-server/liberty-config/config $DIST
    cp -pr ../../fhir-server/liberty-config-tenants/config/* $DIST/config
    cp -pr ../../fhir-server/liberty-config/config/default/fhir-server-config-postgres.json $DIST/config/default/fhir-server-config.json

    echo "Copying fhir-persistence-schema tool..."
    cp -pr ../../fhir-persistence-schema/target/fhir-persistence-schema-*-cli.jar $SCHEMA

    echo "Finished copying fhir-server dependencies..."


    bash ./setup-copy-test-operations.sh
}

# cleanup_existing_docker - cleanup existing docker
cleanup_existing_docker(){
    # Stand up a docker container running the fhir server configured for integration tests
    echo "Bringing down any containers that might already be running as a precaution"
    docker-compose kill
    docker-compose rm -f
}

# bringup_database - brings up database
bringup_database(){
    echo "Bringing up db2... be patient, this will take a minute"
    docker-compose build --pull db2
    docker-compose up -d db2
    echo ">>> Current time: " $(date)

    # TODO wait for it to be healthy instead of just Sleeping
    (docker-compose logs --timestamps --follow db2 & P=$! && sleep 100 && kill $P)
}

# copy_schema_jar
copy_schema_jar(){

}

# setup_schema - sets up the schema (in concert with the db)
deploy_schema(){
    # The full path to the directory of this script, no matter where its called from
    DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
    cd ${DIR}

    # Schemas are created in the image
    java -jar schema/fhir-persistence-schema-*-cli.jar \
        --prop-file postgres.properties --schema-name FHIRDATA --update-schema --pool-size 2

    #java -jar schema/fhir-persistence-schema-*-cli.jar \
    #   --prop-file postgres.properties --schema-name FHIRDATA --grant-to FHIRSERVER --pool-size 2
}

# bringup_fhir 
bringup_fhir(){
    echo "Bringing up the FHIR server... be patient, this will take a minute"
    docker-compose up -d fhir-server
    echo ">>> Current time: " $(date)

    # TODO wait for it to be healthy instead of just Sleeping
    (docker-compose logs --timestamps --follow fhir-server & P=$! && sleep 60 && kill $P)

    # Gather up all the server logs so we can trouble-shoot any problems during startup
    cd -
    pre_it_logs=${WORKSPACE}/pre-it-logs
    zip_file=${WORKSPACE}/pre-it-logs.zip
    rm -rf ${pre_it_logs} 2>/dev/null
    mkdir -p ${pre_it_logs}
    rm -f ${zip_file}

    echo "
    Docker container status:"
    docker ps -a

    containerId=$(docker ps -a | grep fhir | cut -d ' ' -f 1)
    if [[ -z "${containerId}" ]]; then
        echo "Warning: Could not find the fhir container!!!"
    else
        echo "fhir container id: $containerId"

        # Grab the container's console log
        docker logs $containerId  >& ${pre_it_logs}/docker-console.txt

        echo "Gathering pre-test server logs from docker container: $containerId"
        docker cp -L $containerId:/opt/ol/wlp/usr/servers/fhir-server/logs ${pre_it_logs}

        echo "Zipping up pre-test server logs"
        zip -r ${zip_file} ${pre_it_logs}
    fi

    # Wait until the fhir server is up and running...
    echo "Waiting for fhir-server to complete initialization..."
    healthcheck_url='https://localhost:9443/fhir-server/api/v4/$healthcheck'
    tries=0
    status=0
    while [ $status -ne 200 -a $tries -lt 3 ]; do
        tries=$((tries + 1))
        cmd="curl -k -o ${WORKSPACE}/health.json -I -w "%{http_code}" -u fhiruser:change-password $healthcheck_url"
        echo "Executing[$tries]: $cmd"
        status=$($cmd)
        echo "Status code: $status"
        if [ $status -ne 200 ]
        then
        echo "Sleeping 10 secs..."
        sleep 10
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
is_ready_to_run(){
    echo "Preparing environment for fhir-server integration tests..."
    if [[ -z "${WORKSPACE}" ]]; then
        echo "ERROR: WORKSPACE environment variable not set!"
        exit 2
    fi
}

###############################################################################
is_ready_to_run
pre_integration

# EOF 
###############################################################################