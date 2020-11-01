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

# Install the latest fhir-examples-generator
mvn clean install -f ${WORKSPACE}/fhir-examples-generator -DskipTests

if [[ $? -eq 0 ]]
then
    mvn com.ibm.fhir:fhir-examples-generator:generate-examples -f ${WORKSPACE}/fhir-examples
fi
