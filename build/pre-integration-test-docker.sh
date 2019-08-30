#!/usr/bin/env bash
###############################################################################
# (C) Copyright IBM Corp. 2016, 2019
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################
set -ex

echo "Preparing environment for fhir-server integration tests..."
if [[ -z "${WORKSPACE}" ]]; then
    echo "ERROR: WORKSPACE environment variable not set!"
    exit 2
fi

# Set up installers and config files where docker processing can see them
cd ${WORKSPACE}/fhir-install/docker
./copy-dependencies.sh

# resolve environment variables in the Dockerfile(s).
cat ./Dockerfile-fhirtest.template | envsubst > ./Dockerfile-fhirtest

# Stand up a docker container running the fhir server configured for integration tests
echo "Bringing down any fhir server containers that might already be running as a precaution"
docker-compose down

echo "Removing existing fhir-related images to force a rebuild"
docker rmi fhir-basic fhir-test fhir-proxy

echo "Building new fhir-related images"
docker-compose build fhir-baseos
docker-compose build fhir-basic
docker-compose build fhir-test

echo "
>>> Current time: " $(date)
echo "Bringing up a 'fhir-test' container... be patient, this will likely take a few minutes"
docker-compose up -d fhir-proxy
echo ">>> Current time: " $(date)

# Gather up all the server logs so we can trouble-shoot any problems during startup
pre_it_logs=${WORKSPACE}/pre-it-logs
zip_file=${WORKSPACE}/pre-it-logs.zip
rm -fr ${pre_it_logs} 2>/dev/null
mkdir -p ${pre_it_logs}
rm ${zip_file}

echo "
Docker container status:"
docker ps -a

containerId=$(docker ps -a | grep fhir-test | cut -d ' ' -f 1)
if [[ -z "${containerId}" ]]; then
    echo "Warning: Could not find fhir-test container!!!"
else
    echo "fhir-test container id: $containerId"

    # Grab the container's console log
    docker logs $containerId  >& ${pre_it_logs}/docker-console.txt

    echo "Gathering pre-test server logs from docker container: $containerId"
    docker cp -L $containerId:/opt/ibm/fhir-server/wlp/usr/servers/fhir-server/logs ${pre_it_logs}

    echo "Zipping up pre-test server logs"
    zip -r ${zip_file} ${pre_it_logs}
fi

# Wait until the fhir server is up and running...
echo "Waiting for fhir-server to complete initialization..."
metadata_url="https://localhost:9443/fhir-server/api/v4/metadata"
tries=0
status=0
while [ $status -ne 200 -a $tries -lt 3 ]; do
    let tries++
    cmd="curl -k -o ${WORKSPACE}/metadata.json -I -w "%{http_code}" -u fhiruser:fhiruser $metadata_url"
    echo "Executing[$tries]: $cmd"
    status=$($cmd)
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
    exit 0
fi

echo "The fhir-server appears to be running..."
exit 0
