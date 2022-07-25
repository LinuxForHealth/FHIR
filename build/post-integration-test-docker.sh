#!/usr/bin/env bash
###############################################################################
# (C) Copyright IBM Corp. 2016, 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################
set -x

# The full path to the directory of this script, no matter where its called from
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
WORKSPACE="$( dirname "${DIR}" )"

echo "Performing integration test post-processing..."

# Log output locations
it_results=${WORKSPACE}/integration-test-results
zip_file=${WORKSPACE}/integration-test-results.zip

echo "Clearing out any existing pre-it test logs..."
rm -rf ${it_results} 2>/dev/null
mkdir -p ${it_results}/server-logs
mkdir -p ${it_results}/fhir-server-test

containerId=$(docker ps -a | grep fhir | cut -d ' ' -f 1)
if [[ -z "${containerId}" ]]; then
    echo "Warning: Could not find fhir container!!!"
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
cd ${DIR}/docker
docker-compose down

echo "Integration test post-processing completed!"

exit 0
