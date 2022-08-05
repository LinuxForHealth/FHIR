#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2016, 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################
set -ex

# The full path to the directory of this script, no matter where its called from
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
WORKSPACE="$( dirname "${DIR}" )"

# Install the latest fhir-tools
mvn clean install -f ${WORKSPACE}/fhir-tools

if [[ $? -eq 0 ]]
then
    mvn org.linuxforhealth.fhir:fhir-tools:generate-model -f ${WORKSPACE}/fhir-model -e
fi
