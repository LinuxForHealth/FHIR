#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2020, 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# Assumes that fhir-install has already been built
docker login -u "${DOCKERHUB_USERNAME}" -p "${DOCKERHUB_TOKEN}"

# Executes the build for the ibm-fhir-schematool
# This build runs first so that the FAILURE comes before and stops the subsequent builds/releases.
export WORKSPACE=$(pwd)
bash ${WORKSPACE}/fhir-install/src/main/docker/ibm-fhir-schematool/build.sh

bash ${WORKSPACE}/fhir-install/src/main/docker/ibm-fhir-term-graph-loader/build.sh

bash ${WORKSPACE}/fhir-install/src/main/docker/ibm-fhir-bucket-tool/build.sh

# create and remove a 1 GB file to make sure we have the room needed later
df -h
dd if=/dev/urandom oflag=direct of=balloon.dat bs=1024k count=1000
rm -f balloon.dat
sudo apt clean
docker system prune -f
df -h

mvn -B dockerfile:build -f fhir-install --no-transfer-progress -Dmaven.wagon.http.retryHandler.count=3
mvn dockerfile:tag@tag-version -f fhir-install
mvn dockerfile:push@push-version -f fhir-install
mvn dockerfile:push@push-latest -f fhir-install

# EOF
