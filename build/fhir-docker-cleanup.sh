#!/usr/bin/env bash
###############################################################################
# (C) Copyright IBM Corp. 2016, 2017,2019
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

echo "Cleaning up fhir-server integration test environment..."
if [[ -z "${WORKSPACE}" ]]; then
    echo "ERROR: WORKSPACE environment variable not set!"
    exit 2
fi

# Make sure the FHIR_DOCKER_TAG build parameter (env variable) is set.
if [[ -z "${FHIR_DOCKER_TAG}" ]]; then
    echo "ERROR: FHIR_DOCKER_TAG environment variable not set!"
    exit 2
fi

echo "Using FHIR_DOCKER_TAG=${FHIR_DOCKER_TAG}"

cd ${WORKSPACE}/fhir-install/docker

# resolve environment variables in the Dockerfile(s).
cat ./Dockerfile-fhirtest.template | envsubst > ./Dockerfile-fhirtest

echo "Bringing down fhir server containers"
docker-compose down

echo "Removing fhir-related docker images with tag value ${FHIR_DOCKER_TAG}"
docker rmi fhir-basic:${FHIR_DOCKER_TAG} fhir-test:${FHIR_DOCKER_TAG} fhir-proxy:${FHIR_DOCKER_TAG}
