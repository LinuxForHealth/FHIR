#!/usr/bin/env bash
###############################################################################
# (C) Copyright IBM Corp. 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

set -e

# Version 4.0.1 of the fhir-persistence-schema cli doesn't support derby, but starting with 4.1.0 we should do something like this:
#
# curl https://dl.bintray.com/ibm-watson-health/ibm-fhir-server-releases/com/ibm/fhir/fhir-persistence-schema/4.0.1/:fhir-persistence-schema-4.0.1-cli.jar \
#      --output target/fhir-persistence-schema-4.0.1-cli.jar \
#      --fail
#
# java -jar fhir-persistence-schema-4.0.1-cli.jar \
#      --derby
#      --prop-file derby.properties
#      --schema-name FHIRDATA
#      --update-schema
