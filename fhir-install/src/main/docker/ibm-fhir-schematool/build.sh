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

cd ${WORKSPACE}/fhir-install/src/main/docker/ibm-fhir-schematool/

# Copy the files over
mkdir -p target/
cp ${WORKSPACE}/fhir-persistence-schema/target/fhir-persistence-schema-*cli.jar target/
cp ${WORKSPACE}/LICENSE target/

docker build --build-arg FHIR_VERSION=${BUILD_ID} -t ibm-fhir-schematool:latest .
DOCKER_IMAGE=$(docker images --filter=reference='ibm-fhir-schematool:latest' --format "{{.ID}}")
echo "Docker Image is:  ${DOCKER_IMAGE}"

docker tag ${DOCKER_IMAGE} ibmcom/ibm-fhir-schematool:${BUILD_ID}
docker tag ${DOCKER_IMAGE} ibmcom/ibm-fhir-schematool:latest
docker push ibmcom/ibm-fhir-schematool:${BUILD_ID}
docker push ibmcom/ibm-fhir-schematool:latest

popd > /dev/null

# EOF