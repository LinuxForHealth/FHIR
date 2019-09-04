#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2016, 2019
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# installs the fhir-tools
mvn clean package install -f ../fhir-tools/pom.xml

if [[ $? -eq 0 ]]
then 
    mvn com.ibm.watson.health.fhir:fhir-tools:generate-model -f ../fhir-model/pom.xml
fi

