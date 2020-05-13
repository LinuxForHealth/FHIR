#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################
set -ex

# Gathers the logs
package_logs(){
    echo "Gathering logs for [${1}]"

    it_results=${WORKSPACE}/integration-test-results
    rm -fr ${it_results} 2>/dev/null
    mkdir -p ${it_results}/server-logs
    mkdir -p ${it_results}/fhir-server-test
    containerId=$(docker ps -a | grep fhir | cut -d ' ' -f 1)
    if [[ -z "${containerId}" ]]; then
        echo "Warning: Could not find fhir container!!!"
    else
        echo "fhir container id: $containerId"
        # Grab the container's console log
        docker logs $containerId  >& ${it_results}/docker-console.txt
        
        echo "Gathering post-test server logs from docker container: $containerId"
        docker cp -L $containerId:/logs ${it_results}/server-logs
    fi

    echo "Gathering integration test output"
    cp -pr ${WORKSPACE}/fhir-server-test/target/surefire-reports/* ${it_results}/fhir-server-test
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

# Change to the persistence/bin directory
cd "$(dirname ${BASH_SOURCE[0]})"

package_logs "${1}"

# Reset to Original Directory
popd > /dev/null

# EOF
###############################################################################