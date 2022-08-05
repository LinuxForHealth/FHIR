#!/usr/bin/env bash

# ----------------------------------------------------------------------------
# (C) Copyright IBM Corp. 2020
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

cd ${WORKSPACE}/fhir-install/src/main/docker/fhir-schematool/

# Copy the files over
mkdir -p target/
cp ${WORKSPACE}/fhir-persistence-schema/target/fhir-persistence-schema-*cli.jar target/
cp ${WORKSPACE}/LICENSE target/

docker build --build-arg FHIR_VERSION=${BUILD_ID} -t fhir-schematool:latest .
DOCKER_IMAGE=$(docker images --filter=reference='fhir-schematool:latest' --format "{{.ID}}")
echo "Docker Image is:  ${DOCKER_IMAGE}"

docker tag ${DOCKER_IMAGE} linuxforhealth/fhir-schematool:${BUILD_ID}
docker tag ${DOCKER_IMAGE} linuxforhealth/fhir-schematool:latest
docker push linuxforhealth/fhir-schematool:${BUILD_ID}
docker push linuxforhealth/fhir-schematool:latest

popd > /dev/null

# EOF