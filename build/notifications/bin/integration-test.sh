#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

set -o errexit
set -o nounset
set -o pipefail

run_tests(){
    # The integration tests may be overriden completely, or fall through to the default. 
    notifications="${1}"

    if [ ! -z "${notifications}" ] && [ -f "build/notifications/${notifications}/integration-test.sh" ]
    then 
        echo "Running [${notifications}] specific integration tests"
        bash build/notifications/${notifications}/integration-test.sh
    else 
        # Go to the Default
        echo "Executing the default integration tests"
        mvn -B test -DskipTests=false -f fhir-server-test -DskipWebSocketTest=true --no-transfer-progress
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

# Change to the notifications/bin directory
cd "${WORKSPACE}"

run_tests "${1}"

# Reset to Original Directory
popd > /dev/null

# EOF
###############################################################################