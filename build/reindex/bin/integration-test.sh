#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

set -ex

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
        mvn -B test -f fhir-server-test -DskipWebSocketTest=true --no-transfer-progress \
            -DskipTests=false -Dtest="com.ibm.fhir.server.test.operation.ReindexOperationTest" | \
            tee build/reindex/${reindex}/workarea/${reindex}-test.log
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