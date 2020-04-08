#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# Assumes that fhir-install has already been built which should have built and tagged the image
docker login -u ${DOCKERHUB_USERNAME} -p ${DOCKERHUB_TOKEN}
mvn dockerfile:push@push-version -f fhir-install
mvn dockerfile:push@push-latest -f fhir-install
