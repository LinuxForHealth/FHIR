#!/usr/bin/env bash
###############################################################################
# (C) Copyright IBM Corp. 2021, 2022
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

if [ -z "${WORKSPACE}" ]; then
    echo "ERROR: WORKSPACE environment variable not set!"
    exit 1
fi
if [ -z "${1}" ]; then
  echo "This script requires an argument that points to one of the integration environment folders."
  exit 1
fi

compose_environment="${1}"

# Log output locations
pre_it_logs=${WORKSPACE}/pre-it-logs
zip_file=${WORKSPACE}/pre-it-logs.zip

echo "Clearing out any existing pre-it test logs..."
rm -rf ${pre_it_logs} 2>/dev/null
rm -f ${zip_file}
mkdir -p ${pre_it_logs}

containerId=$(docker ps -a | grep ${compose_environment}[-_]fhir-server | cut -d ' ' -f 1)
if [ -z "${containerId}" ]; then
    echo "Warning: Could not find the fhir-server container!!!"
else
    echo "fhir container id: ${containerId}"

    echo "Gathering pre-test server logs from docker container: ${containerId}"
    docker logs ${containerId}  >& ${pre_it_logs}/docker-console.txt
    docker cp -L ${containerId}:/logs ${pre_it_logs}

    echo "Zipping up pre-test server logs"
    zip -r ${zip_file} ${pre_it_logs}
fi