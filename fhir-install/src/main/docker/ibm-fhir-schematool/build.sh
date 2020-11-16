#!/usr/bin/env bash

# ----------------------------------------------------------------------------
# (C) Copyright IBM Corp. 2020
#
# SPDX-License-Identifier: Apache-2.0
# ----------------------------------------------------------------------------

set -e -o pipefail

##############################################################################

docker build --build-arg FHIR_VERSION=4.5.0 -t ibm-fhir-schematool:latest .
docker images | grep -i ibm-fhir-schematool | grep -i latest

docker login
docker tag 6bd9e9fbe2a6 ibmcom/ibm-fhir-schematool:4.5.0
docker tag 6bd9e9fbe2a6 ibmcom/ibm-fhir-schematool:latest
docker push ibmcom/ibm-fhir-schematool:4.5.0
docker push ibmcom/ibm-fhir-schematool:latest

# EOF