#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

echo "Performing integration test post-processing..."
if [[ -z "${WORKSPACE}" ]]; then
    echo "ERROR: WORKSPACE environment variable not set!"
    exit 2
fi

# Gather up all the log files and test results
it_results=${WORKSPACE}/integration-test-results
zip_file=${WORKSPACE}/integration-test-results.zip
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

    echo "Gathering pre-test server logs from docker container: $containerId"
    docker cp -L $containerId:/opt/ol/wlp/usr/servers/fhir-server/logs ${it_results}/server-logs
fi

echo "Gathering integration test output"
cp -pr ${WORKSPACE}/fhir-server-test/target/surefire-reports/* ${it_results}/fhir-server-test

echo "Bringing down the fhir server docker container(s)..."
cd ${WORKSPACE}/fhir-install/docker
docker-compose down

echo "Integration test post-processing completed!"

exit 0