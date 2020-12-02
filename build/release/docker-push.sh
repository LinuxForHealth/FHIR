#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# Assumes that fhir-install has already been built
docker login -u ${DOCKERHUB_USERNAME} -p ${DOCKERHUB_TOKEN}

# Executes the build for the ibm-fhir-schematool
# This build runs first so that the FAILURE comes before and stops the subsequent builds/releases.
export WORKSPACE=$(pwd)
bash fhir-install/src/main/docker/ibm-fhir-schematool/build.sh

mvn dockerfile:build -f fhir-install -Dmaven.wagon.http.retryHandler.count=3
mvn dockerfile:tag@tag-version -f fhir-install -Dmaven.wagon.http.retryHandler.count=3
mvn dockerfile:push@push-version -f fhir-install -Dmaven.wagon.http.retryHandler.count=3
mvn dockerfile:push@push-latest -f fhir-install -Dmaven.wagon.http.retryHandler.count=3
