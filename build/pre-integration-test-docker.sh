#!/usr/bin/env bash
###############################################################################
# (C) Copyright IBM Corp. 2016, 2022
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

if [ -z "${1}" ]; then
  echo "This script requires an argument that points to one of the integration environment folders."
  exit 1
elif [ -z "$(find ${DIR} -maxdepth 1 -name ${1} -type d)" ]; then
  echo "'${1}' is not a valid argument. This script requires an argument that points to one of the integration environment folders."
  exit 1
fi

subdir="${1}"
echo "Preparing environment for the ${subdir} fhir-server integration tests..."

# Set the working directory
cd ${DIR}/${subdir}

# Source the tenant1 datastore variables
. ${WORKSPACE}/build/common/set-tenant1-datastore-vars.sh

echo "Bringing down any containers that might already be running as a precaution..."
docker compose kill
docker compose rm -f

# Set up the server config files
${WORKSPACE}/build/common/copy-server-config.sh
${WORKSPACE}/build/common/copy-test-operations.sh

# Set up for the bulkdata tests
${WORKSPACE}/build/common/bulkdata-setup.sh

# Delegate to the specific test env for further configuration
echo "Configuring the FHIR Server for ${subdir}"
./configure.sh
echo "Finished configuring the FHIR Server for ${subdir}"

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

echo "docker image status:"
docker images

echo "Waiting for fhir-server to complete initialization..."
${WORKSPACE}/build/common/wait-for-it.sh

echo "The fhir-server appears to be running..."
${WORKSPACE}/build/common/gather-pretest-logs.sh ${subdir}