#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

set -ex

# Gathers the logs
package_logs(){
    reindex="${1}"
    echo "Gathering logs for [${1}]"

    it_results=${WORKSPACE}/build/reindex/integration-test-results
    if [ ! -d ${it_results} ]
    then
        rm -fr ${it_results} 2>/dev/null
    fi

    mkdir -p ${it_results}
    mkdir -p ${it_results}/server-logs
    mkdir -p ${it_results}/fhir-server-test

    # Runtime Date
    echo $(date) > ${it_results}/runtime.txt

    # Look for the FHIR Server Container
    containerId=$(docker ps -a | grep ibm-fhir-server | cut -d ' ' -f 1)
    if [ -z "${containerId}" ]
    then
        echo "Warning: Could not find fhir container!!!"
    else
        echo "fhir container id: $containerId"
        # Grab the container's console log
        docker logs $containerId  >& ${it_results}/docker-console.txt
        
        echo "Gathering post-test server logs from docker container: $containerId"
        docker cp -L $containerId:/logs ${it_results}/server-logs
    fi

    echo "Gathering integration test output"
    if [ -d ${WORKSPACE}/fhir-server-test/target/surefire-reports ]
    then
        cp -pr ${WORKSPACE}/fhir-server-test/target/surefire-reports/* ${it_results}/fhir-server-test
    fi

    if [ -d ${WORKSPACE}/build/reindex/${reindex}/workarea ]
    then
        echo "Move the '${reindex}' Elements to the output area'"
        cp -pr build/reindex/${reindex}/workarea/${reindex}-test.log ${it_results}
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

package_logs "${1}"

# Reset to Original Directory
popd > /dev/null

# EOF
###############################################################################