#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2020
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
./setup-dependencies-docker.sh

# Stand up a docker container running the fhir server configured for integration tests
echo "Bringing down any fhir server containers that might already be running as a precaution"
docker-compose kill
docker-compose rm -f

echo "Bringing up postgres... be patient, this will take a minute"
docker-compose build --pull fhirdb-postgres
docker-compose up -d fhirdb-postgres
echo ">>> Current time: " $(date)

# TODO wait for it to be healthy instead of just Sleeping
(docker-compose logs --timestamps --follow fhirdb-postgres & P=$! && sleep 100 && kill $P)

echo "Deploying the Postgres schema..."
./setup-deploy-schema-and-tenant.sh

echo "Bringing up the FHIR server... be patient, this will take a minute"
./setup-copy-test-operations.sh
docker-compose build --pull fhir
docker-compose up -d fhir
echo ">>> Current time: " $(date)

# TODO wait for it to be healthy instead of just Sleeping
(docker-compose logs --timestamps --follow fhir & P=$! && sleep 100 && kill $P)

# Gather up all the server logs so we can trouble-shoot any problems during startup
cd -
pre_it_logs=${WORKSPACE}/pre-it-logs
zip_file=${WORKSPACE}/pre-it-logs.zip
rm -fr ${pre_it_logs} 2>/dev/null
mkdir -p ${pre_it_logs}
rm -f ${zip_file}

echo "
Docker container status:"
docker ps -a

containerId=$(docker ps -a | grep fhir | cut -d ' ' -f 1)
if [[ -z "${containerId}" ]]; then
    echo "Warning: Could not find the fhir container!!!"
else
    echo "fhir container id: $containerId"

    # Grab the container's console log
    docker logs $containerId  >& ${pre_it_logs}/docker-console.txt

    echo "Gathering pre-test server logs from docker container: $containerId"
    docker cp -L $containerId:/opt/ol/wlp/usr/servers/fhir-server/logs ${pre_it_logs}

    echo "Zipping up pre-test server logs"
    zip -r ${zip_file} ${pre_it_logs}
fi

# Wait until the fhir server is up and running...
echo "Waiting for fhir-server to complete initialization..."
healthcheck_url='https://localhost:9443/fhir-server/api/v4/$healthcheck'
tries=0
status=0
while [ $status -ne 200 -a $tries -lt 3 ]; do
    tries=$((tries + 1))
    cmd="curl -k -o ${WORKSPACE}/health.json -I -w "%{http_code}" -u fhiruser:change-password $healthcheck_url"
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
    exit 1
fi

echo "The fhir-server appears to be running..."
exit 0