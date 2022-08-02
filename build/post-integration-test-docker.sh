#!/usr/bin/env bash
###############################################################################
# (C) Copyright IBM Corp. 2016, 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# Exit the script if any commands fail
set -e
# Print each command before executing it
#set -x
# This allows subshells to inheret the options above
export SHELLOPTS

# The full path to the directory of this script, no matter where its called from
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
export WORKSPACE="$( dirname "${DIR}" )"

if [ -z ${1} ]; then
  echo "This script requires an argument that points to one of the integration folders"
  exit 1
fi

echo "Bringing down environment for the ${1} fhir-server integration tests..."

# Log output locations
it_results=${WORKSPACE}/integration-test-results
zip_file=${WORKSPACE}/integration-test-results.zip

echo "Clearing out any existing pre-it test logs..."
rm -rf ${it_results} 2>/dev/null
mkdir -p ${it_results}/server-logs
mkdir -p ${it_results}/fhir-server-test

containerId=$(docker ps -a | grep ${1}[-_]fhir-server | cut -d ' ' -f 1)
if [ -z "${containerId}" ]; then
    echo "Warning: Could not find fhir-server container!!!"
else
    echo "fhir container id: $containerId"

    # Grab the container's console log
    docker logs $containerId  >& ${it_results}/docker-console.txt

    echo "Gathering pre-test server logs from docker container: $containerId"
    docker cp -L $containerId:/logs ${it_results}/server-logs
fi

echo "Gathering integration test output"
cp -r ${WORKSPACE}/fhir-server-test/target/surefire-reports/* ${it_results}/fhir-server-test

echo "Bringing down the fhir server docker container(s)..."
cd ${DIR}/${1}
docker-compose down

echo "Integration test post-processing completed!"
