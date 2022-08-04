#!/usr/bin/env bash

# ----------------------------------------------------------------------------
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
# ----------------------------------------------------------------------------

##############################################################################

if [ -z "${WORKSPACE}" ]
then
    echo "The WORKSPACE is not set" 
    exit 10;
fi

if [ -z "${BUILD_ID}" ]
then
    echo "The Build ID is not set"
    exit 11;
fi

# docker login is already complete at this point.

pushd $(pwd)

cd ${WORKSPACE}/fhir-install/src/main/docker/fhir-term-graph-loader/

# Copy the files over
mkdir -p target/
cp ${WORKSPACE}/term/fhir-term-graph-loader/target/fhir-term-graph-loader-*-cli.jar target/
cp ${WORKSPACE}/LICENSE target/

docker build --build-arg FHIR_VERSION=${BUILD_ID} -t linuxforhealth/fhir-term-loader:latest .
DOCKER_IMAGE=$(docker images --filter=reference='linuxforhealth/fhir-term-loader:latest' --format "{{.ID}}")
echo "Docker Image is:  ${DOCKER_IMAGE}"

docker tag ${DOCKER_IMAGE} linuxforhealth/fhir-term-loader:${BUILD_ID}
docker tag ${DOCKER_IMAGE} linuxforhealth/fhir-term-loader:latest
docker push linuxforhealth/fhir-term-loader:${BUILD_ID}
docker push linuxforhealth/fhir-term-loader:latest

popd > /dev/null

# EOF