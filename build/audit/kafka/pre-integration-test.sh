#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

set -o errexit
set -o nounset
set -o pipefail

# pre_integration
pre_integration(){
    cleanup
    config
    bringup
}

# config - update configuration
config(){
    echo "Copying fhir configuration files..."
    cp -pr ${WORKSPACE}/fhir-server/liberty-config/config $DIST
    cp -pr ${WORKSPACE}/fhir-server/liberty-config-tenants/config/* $DIST/config
    cp -pr ${WORKSPACE}/fhir-server/liberty-config/config/default/ \
        ${WORKSPACE}/build/audit/kafka/workarea

    # Move over the test configurations
    cp -pr ${WORKSPACE}/build/audit/kafka/resources/* ${WORKSPACE}/build/audit/kafka/workarea/default/
    mv ${WORKSPACE}/build/audit/kafka/workarea/default/fhir-server-config-audit-cicd.json mv ${WORKSPACE}/build/audit/kafka/workarea/default/fhir-server-config.json
}

# cleanup - cleanup existing docker
cleanup(){
    # Stand up a docker container running the fhir server configured for integration tests
    echo "Bringing down any containers that might already be running as a precaution"
    docker-compose kill
    docker-compose rm -f
}

# bringup
bringup(){
    echo "Bringing up containers"
    docker-compose up --remove-orphans
    echo ">>> Current time: " $(date)

    # TODO wait for it to be healthy instead of just Sleeping
    (docker-compose logs --timestamps --follow fhir-server & P=$! && sleep 120 && kill $P)

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
    while [ $status -ne 200 -a $tries -lt 30 ]; do
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

    # Create the FHIR_AUDIT topic
    docker-compose exec kafka-1 bash /bin/kafka-topics --create --topic=FHIR_AUDIT \
        --zookeeper=zookeeper-1:22181,zookeeper-2:32181 --partitions=5 --replication-factor=2
    echo "Topic is created 'FHIR_AUDIT'"
    exit 0
}

# is_ready_to_run - is this ready to run?
is_ready_to_run(){
    echo "Preparing environment for fhir-server integration tests..."
    if [ -z "${WORKSPACE}" ]
    then
        echo "ERROR: WORKSPACE environment variable not set!"
        exit 1
    fi
}

###############################################################################
is_ready_to_run

cd build/audit/kafka
pre_integration

# EOF
###############################################################################