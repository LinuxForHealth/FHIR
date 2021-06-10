#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

set -ex
set -o pipefail

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
            -DskipTests=false -Dtest="com.ibm.fhir.server.test.operation.ReindexOperationTest" | tee build/reindex/${reindex}/workarea/${reindex}-test1.log

        # Test 2 - Long Run Tests *895 Resources*
        mvn -B test -f fhir-server-test -DskipWebSocketTest=true --no-transfer-progress \
            -DskipTests=false -Dtest="com.ibm.fhir.server.test.operation.ReindexOperationLongRunTest" | tee build/reindex/${reindex}/workarea/${reindex}-test2.log

        # Test 3 Phase 1
        mvn -B test -f fhir-server-test -DskipWebSocketTest=true --no-transfer-progress \
            -DskipTests=false -Dtest="com.ibm.fhir.server.test.operation.ReindexOperationPhase1Test" | tee build/reindex/${reindex}/workarea/${reindex}-test3.log

        # Update SPs
        cp -pr ${WORKSPACE}/fhir-server-test/src/test/resources/testdata/reindex-operation/extension-search-parameters-test1.json \
            ${WORKSPACE}/build/reindex/${reindex}/workarea/volumes/dist/config/default/extension-search-parameters.json

        # Restart
        cd build/reindex/${reindex}
        docker-compose restart --timeout 30
        cd -

        # Test 3 Phase 2
        mvn -B test -f fhir-server-test -DskipWebSocketTest=true --no-transfer-progress \
            -DskipTests=false -Dtest="com.ibm.fhir.server.test.operation.ReindexOperationPhase2Test" | tee build/reindex/${reindex}/workarea/${reindex}-test4.log

        # Update SPs
        cp -pr ${WORKSPACE}/fhir-server-test/src/test/resources/testdata/reindex-operation/extension-search-parameters-test2.json \
            ${WORKSPACE}/build/reindex/${reindex}/workarea/volumes/dist/config/default/extension-search-parameters.json

        # Restart
        cd build/reindex/${reindex}
        docker-compose restart --timeout 30
        cd -

        # Test 3 Phase 3
        mvn -B test -f fhir-server-test -DskipWebSocketTest=true --no-transfer-progress \
            -DskipTests=false -Dtest="com.ibm.fhir.server.test.operation.ReindexOperationPhase3Test" | tee build/reindex/${reindex}/workarea/${reindex}-test5.log
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