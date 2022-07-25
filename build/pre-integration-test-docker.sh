#!/usr/bin/env bash
###############################################################################
# (C) Copyright IBM Corp. 2016, 2022
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################
set -ex

# The full path to the directory of this script, no matter where its called from
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
export WORKSPACE="$( dirname "${DIR}" )"

echo "Preparing environment for fhir-server integration tests..."

# Log output locations
pre_it_logs=${WORKSPACE}/pre-it-logs
zip_file=${WORKSPACE}/pre-it-logs.zip

echo "Clearing out any existing pre-it test logs..."
rm -rf ${pre_it_logs} 2>/dev/null
rm -f ${zip_file}
mkdir -p ${pre_it_logs}

. ${WORKSPACE}/build/common/set_tenant1_datastore_vars.sh

# Set the working directory
cd ${DIR}/docker

echo "Bringing down any containers that might already be running as a precaution..."
docker-compose kill
docker-compose rm -f

# Set up the server config files
./copy-server-config.sh
./copy-test-operations.sh

# Set up for the bulkdata tests
./bulkdata-setup.sh

# Stand up a docker container running the fhir server configured for integration tests
echo "Bringing up the test environment... be patient, this will take a minute"
docker-compose up -d
echo ">>> Current time: " $(date)

# Print 60 seconds of logs
#(docker-compose logs --timestamps --follow & P=$! && sleep 60 && kill $P)

# Gather up all the server logs so we can trouble-shoot any problems during startup
cd -
echo "
Docker container status:"
docker ps -a

containerId=$(docker ps -a | grep fhir | cut -d ' ' -f 1)
if [[ -z "${containerId}" ]]; then
    echo "Warning: Could not find the fhir container!!!"
else
    echo "fhir container id: $containerId"

    echo "Gathering pre-test server logs from docker container: $containerId"
    docker logs $containerId  >& ${pre_it_logs}/docker-console.txt
    docker cp -L $containerId:/logs ${pre_it_logs}

    echo "Zipping up pre-test server logs"
    zip -r ${zip_file} ${pre_it_logs}
fi

# Wait until the fhir server is up and running...
echo "Waiting for fhir-server to complete initialization..."
healthcheck_url='https://localhost:9443/fhir-server/api/v4/$healthcheck'
tries=0
status=0
while [ $status -ne 200 -a $tries -lt 30 ]; do
    tries=$((tries + 1))
    set +o errexit
    cmd="curl --max-time 5 -k -s -o ${WORKSPACE}/health.json -w "%{http_code}" -u fhiruser:change-password $healthcheck_url"
    echo "Executing[$tries]: $cmd"
    status=$($cmd)
    set -o errexit
    echo "Status code: $status"
    if [ $status -ne 200 ]
    then
       echo "Sleeping 10 secs..."
       sleep 10
    fi
done

if [ $status -ne 200 ]
then
    echo "Could not establish a connection to the fhir-server within $tries REST API invocations!"
    exit 1
fi

echo "The fhir-server appears to be running..."
exit 0
