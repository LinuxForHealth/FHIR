#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################
set -ex

run_tests(){
    # The integration tests may be overriden completely, or fall through to the default. 
    PERSISTENCE="${1}"
    if [ -z "${PERSISTENCE}" && -f ../${PERSISTENCE}/integration-test-docker.sh ]
    then 
        # 
        echo "Running [${PERSISTENCE}] specific integration tests"
        bash ../${PERSISTENCE}/integration-test-docker.sh
    else 
        # Go to the Default
        echo "Executing the default integration tests"
        mvn -B test -DskipTests=false -f fhir-server-test -DskipWebSocketTest=true --no-transfer-progress
    fi
}

###############################################################################
# Store the current directory to reset to
pushd $(pwd) > /dev/null

# Change to the persistence/bin directory
cd "$(dirname ${BASH_SOURCE[0]})"

run_tests ${1}

# Reset to Original Directory
popd > /dev/null

# EOF
###############################################################################