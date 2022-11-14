#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2020, 2022
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################
set -ex

export WORKSPACE=$(pwd)
DIST="${WORKSPACE}/build/audit/kafka/workarea/volumes/dist"

# pre_integration
pre_integration(){
    cleanup
    config
    bringup
}

# config - update configuration
config(){
    mkdir -p ${DIST}/userlib
    mkdir -p ${DIST}/
    mkdir -p ${WORKSPACE}/build/audit/kafka/workarea/output
    mkdir -p ${DIST}/overrides
    touch ${WORKSPACE}/build/audit/kafka/workarea/output/fhir_audit-messages.log
    chmod +rwx ${WORKSPACE}/build/audit/kafka/workarea/output/fhir_audit-messages.log
    chmod -R 777 ${WORKSPACE}/build/audit/kafka/workarea/output/

    echo "Copying fhir configuration files..."
    cp -r ${WORKSPACE}/fhir-server-webapp/src/main/liberty/config/config $DIST/
    cp -r ${WORKSPACE}/fhir-server-webapp/src/test/liberty/config/config/* $DIST/config/
    # Copy over the tenant1 derby datasource definitions
    cp ${WORKSPACE}/fhir-server-webapp/src/test/liberty/config/configDropins/overrides/datasource-derby.xml $DIST/overrides/datasource-derby.xml

    echo "Copying test artifacts to install location..."
    USERLIB="${DIST}/userlib"
    mkdir -p $USERLIB
    find ${WORKSPACE}/conformance -iname 'fhir-ig*.jar' -not -iname 'fhir*-tests.jar' -not -iname 'fhir*-test-*.jar' -exec cp -f {} ${USERLIB} \;
    cp ${WORKSPACE}/operation/fhir-operation-test/target/fhir-operation-*.jar ${USERLIB}
    cp ${WORKSPACE}/term/operation/fhir-operation-term-cache/target/fhir-operation-*.jar ${USERLIB}

    echo "Finished copying fhir-server dependencies..."

    # Move over the test configurations
    cp -r ${WORKSPACE}/build/audit/kafka/resources/* ${WORKSPACE}/build/audit/kafka/workarea/volumes/dist/config/default/
    bash ${WORKSPACE}/build/common/update-server-registry-resource.sh ${WORKSPACE}/build/audit/kafka/workarea/volumes/dist/config/default/fhir-server-config-audit-cicd.json
    mv ${WORKSPACE}/build/audit/kafka/workarea/volumes/dist/config/default/fhir-server-config-audit-cicd.json ${WORKSPACE}/build/audit/kafka/workarea/volumes/dist/config/default/fhir-server-config.json
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
    docker-compose up --remove-orphans -d
    echo ">>> Current time: " $(date)

    # Allow extra time due to bootstrapping
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

    containerId=$(docker ps -a | grep fhir-server | cut -d ' ' -f 1)
    if [[ -z "${containerId}" ]]; then
        echo "Warning: Could not find the fhir container!!!"
    else
        echo "fhir container id: ${containerId}"

        # Grab the container's console log
        docker logs ${containerId} > ${pre_it_logs}/docker-console.txt

        echo "Gathering pre-test server logs from docker container: ${containerId}"
        docker cp -L ${containerId}:/logs ${pre_it_logs}
    fi

    kafkaContainerId=$(docker ps -a | grep kafka-1 | cut -d ' ' -f 1)
    if [[ -z "${kafkaContainerId}" ]]; then
        echo "Warning: Could not find the kafka-1 container!!!"
    else
        echo "kafka-1 id: ${containerId}"
        # Grab the container's console log
        docker logs ${kafkaContainerId}

    fi

    kafka2ContainerId=$(docker ps -a | grep kafka-2 | cut -d ' ' -f 1)
    if [[ -z "${kafka2ContainerId}" ]]; then
        echo "Warning: Could not find the kafka-2 container!!!"
    else
        echo "kafka-2 id: ${kafka2ContainerId}"
        # Grab the container's console log
        docker logs ${kafka2ContainerId}
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
    docker-compose -f build/audit/kafka/docker-compose.yml exec -T kafka-1 bash /bin/kafka-topics \
        --bootstrap-server kafka-1:19092,kafka-2:39096 --command-config /etc/kafka/secrets/client-ssl.properties \
        --create --topic FHIR_AUDIT --partitions 10 --replication-factor 2
    [ $? -eq 0 ] || exit 9

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
