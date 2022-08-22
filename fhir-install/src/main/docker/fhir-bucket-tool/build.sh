#!/usr/bin/env bash
# ----------------------------------------------------------------------------
# (C) Copyright IBM Corp. 2021, 2022
#
# SPDX-License-Identifier: Apache-2.0
# ----------------------------------------------------------------------------

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

cd ${WORKSPACE}/fhir-install/src/main/docker/fhir-bucket-tool/

# Copy the files over
mkdir -p target/
cp ${WORKSPACE}/fhir-bucket/target/fhir-bucket-*cli.jar target/
cp ${WORKSPACE}/LICENSE target/

docker build --build-arg FHIR_VERSION=${BUILD_ID} -t linuxforhealth/fhir-bucket-tool:latest .
DOCKER_IMAGE=$(docker images --filter=reference='linuxforhealth/fhir-bucket-tool:latest' --format "{{.ID}}")
echo "Docker Image is:  ${DOCKER_IMAGE}"

docker tag ${DOCKER_IMAGE} ghcr.io/linuxforhealth/fhir-bucket-tool:${BUILD_ID}
docker tag ${DOCKER_IMAGE} ghcr.io/linuxforhealth/fhir-bucket-tool:latest
docker push ghcr.io/linuxforhealth/fhir-bucket-tool:${BUILD_ID}
docker push ghcr.io/linuxforhealth/fhir-bucket-tool:latest

popd > /dev/null