#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

set -ex
set -o pipefail

# spins for a set time until the server is up
wait_for_it(){
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
}

run_tests(){
    # The integration tests may be overriden completely, or fall through to the default. 
    reindex="${1}"

    if [ ! -z "${reindex}" ] && [ -f "build/reindex/${reindex}/integration-test.sh" ]
    then 
        echo "Running [${reindex}] specific integration tests"
        bash build/reindex/${reindex}/integration-test.sh
    else
        # Go to the Default
        echo "Executing the default integration tests"
        sed -i -e 's/test.reindex.enabled = false/test.reindex.enabled = true/g' ${WORKSPACE}/fhir-server-test/src/test/resources/test.properties

        # Test 1 - Basic Tests for Reindex
        mvn -B test -f fhir-server-test -DskipWebSocketTest=true --no-transfer-progress \
            -DskipTests=false -Dtest="org.linuxforhealth.fhir.server.test.operation.ReindexOperationTest" | tee build/reindex/${reindex}/workarea/${reindex}-test1.log

        # Test 2 - Long Run Tests *895 Resources*
        mvn -B test -f fhir-server-test -DskipWebSocketTest=true --no-transfer-progress \
            -DskipTests=false -Dtest="org.linuxforhealth.fhir.server.test.operation.ReindexOperationLongRunTest" | tee build/reindex/${reindex}/workarea/${reindex}-test2.log

        # Test 3 Phase 1
        mvn -B test -f fhir-server-test -DskipWebSocketTest=true --no-transfer-progress \
            -DskipTests=false -Dtest="org.linuxforhealth.fhir.server.test.operation.ReindexOperationPhase1Test" | tee build/reindex/${reindex}/workarea/${reindex}-test3.log

        # Update SPs
        cp -pr ${WORKSPACE}/fhir-server-test/src/test/resources/testdata/reindex-operation/extension-search-parameters-test1.json \
            ${WORKSPACE}/build/reindex/${reindex}/workarea/volumes/dist/config/default/extension-search-parameters.json

        # Restart
        cd build/reindex/${reindex}
        docker-compose restart --timeout 30 fhir
        cd -
        wait_for_it

        # Test 3 Phase 2
        mvn -B test -f fhir-server-test -DskipWebSocketTest=true --no-transfer-progress \
            -DskipTests=false -Dtest="org.linuxforhealth.fhir.server.test.operation.ReindexOperationPhase2Test" | tee build/reindex/${reindex}/workarea/${reindex}-test4.log

        # Update SPs
        cp -pr ${WORKSPACE}/fhir-server-test/src/test/resources/testdata/reindex-operation/extension-search-parameters-test2.json \
            ${WORKSPACE}/build/reindex/${reindex}/workarea/volumes/dist/config/default/extension-search-parameters.json

        # Restart
        cd build/reindex/${reindex}
        docker-compose restart --timeout 30 fhir
        cd -
        wait_for_it

        # Test 3 Phase 3
        mvn -B test -f fhir-server-test -DskipWebSocketTest=true --no-transfer-progress \
            -DskipTests=false -Dtest="org.linuxforhealth.fhir.server.test.operation.ReindexOperationPhase3Test" | tee build/reindex/${reindex}/workarea/${reindex}-test5.log
    fi
}

###############################################################################
# Check if the workspace is set.
if [ -z "${WORKSPACE}" ]
then 
    echo "The WORKSPACE value is unset"
    exit -1
fi 

# Store the current directory to reset to
pushd $(pwd) > /dev/null

# Change to the reindex/bin directory
cd "${WORKSPACE}"

run_tests "${1}"

# Reset to Original Directory
popd > /dev/null

# EOF
###############################################################################