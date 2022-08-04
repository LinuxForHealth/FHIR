#!/usr/bin/env bash
###############################################################################
# (C) Copyright IBM Corp. 2021, 2022
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# Gathers the logs
package_logs(){
    workflow="${1}"
    job="${2}"
    echo "Gathering logs for [${workflow}/${job}]"

    it_results=${WORKSPACE}/build/${workflow}/integration-test-results
    if [ ! -d ${it_results} ]
    then
        rm -fr ${it_results} 2>/dev/null
    fi

    mkdir -p ${it_results}
    mkdir -p ${it_results}/server-logs
    mkdir -p ${it_results}/schema-tool
    mkdir -p ${it_results}/fhir-server-test

    # Runtime Date
    echo $(date) > ${it_results}/runtime.txt

    # Look for the FHIR Server Container
    containerId=$(docker ps -a | grep fhir-server | cut -d ' ' -f 1)
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

    echo "Gathering schematool logs"
    if [ -f ${WORKSPACE}/build/${workflow}/${job}/fhirschema.log ]
    then
        cp ${WORKSPACE}/build/${workflow}/${job}/fhirschema.log ${it_results}/schema-tool/
    fi

    echo "Gathering integration test output"
    if [ -d ${WORKSPACE}/fhir-server-test/target/surefire-reports ]
    then
        cp -r ${WORKSPACE}/fhir-server-test/target/surefire-reports/* ${it_results}/fhir-server-test
    fi

    if [ -f ${WORKSPACE}/build/${workflow}/${job}/workarea/${job}-test1.log ]
    then
        echo "Move the '${job}' Elements to the output area'"
        cp build/${workflow}/${job}/workarea/${job}-test*.log ${it_results}
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

package_logs "${1}" "${2}"

# Reset to Original Directory
popd > /dev/null

# EOF
###############################################################################
