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

docker build fhir-install -t ibmcom/ibm-fhir-server
mvn dockerfile:tag@tag-version -f fhir-install
mvn dockerfile:push@push-version -f fhir-install
mvn dockerfile:push@push-latest -f fhir-install

# EOF
